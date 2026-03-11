package com.example.axxionsystem.alquiler

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.common.api.RetrofitInstance
import com.example.axxionsystem.alquiler.model.SolicitudCreateRequest
import com.example.axxionsystem.alquiler.model.SolicitudResponse
import com.example.axxionsystem.alquiler.model.RentaResponse
import com.example.axxionsystem.common.adapters.SimpleTextAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.axxionsystem.alquiler.model.EntregaFirmaRequest
import com.example.axxionsystem.alquiler.model.EntregaResponse
import com.example.axxionsystem.alquiler.model.DevolucionFirmaRequest
import com.example.axxionsystem.alquiler.model.DevolucionResponse

/**
 * AlquilerActivity centraliza las operaciones relacionadas con el alquiler de maquinaria.
 * Gestiona múltiples flujos operativos:
 * - Creación y visualización de solicitudes de alquiler interactuando con `/api/alquiler/solicitudes`.
 * - Visualización de las rentas activas asignadas a un cliente por medio de `/api/alquiler/rentas/cliente/{id}`.
 * - Firma digital de los procesos de Entrega (en dirección de obra) y Devolución, enviando
 *   la información a través de los respectivos endpoints de firma.
 */
class AlquilerActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recycler: RecyclerView
    private lateinit var btnVolver: Button
    private lateinit var btnNueva: Button
    private lateinit var btnMisRentas: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alquiler)

        progressBar   = findViewById(R.id.progressBar)
        recycler      = findViewById(R.id.recyclerAlquiler)
        btnVolver     = findViewById(R.id.btnVolver)
        btnNueva      = findViewById(R.id.btnNueva)
        btnMisRentas  = findViewById(R.id.btnMisRentas)

        recycler.layoutManager = LinearLayoutManager(this)

        btnVolver.setOnClickListener { finish() }

        // Cargar solicitudes al iniciar
        cargarSolicitudes()

        btnNueva.setOnClickListener { mostrarDialogoNuevaSolicitud() }
        btnMisRentas.setOnClickListener { cargarRentas(clienteId = 1) }
    }

    // ─── Solicitudes ──────────────────────────────────

    private fun cargarSolicitudes() {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.consultarSolicitudes()
            .enqueue(object : Callback<List<SolicitudResponse>> {
                override fun onResponse(
                    call: Call<List<SolicitudResponse>>,
                    response: Response<List<SolicitudResponse>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val lista = response.body() ?: emptyList()
                        mostrarSolicitudes(lista)
                    } else {
                        Toast.makeText(this@AlquilerActivity, "Error ${response.code()}: al cargar solicitudes", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<SolicitudResponse>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@AlquilerActivity, "Conexión fallida: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun mostrarSolicitudes(lista: List<SolicitudResponse>) {
        val datos = lista.map {
            "Solicitud #${it.id} | Estado: ${it.estado}\n" +
            "Cliente ID: ${it.clienteId} | Cantidad: ${it.cantidadSolicitada ?: 1}\n" +
            (if (!it.descripcionNecesidad.isNullOrBlank()) "Desc: ${it.descripcionNecesidad}" else "")
        }
        recycler.adapter = SimpleTextAdapter(datos, "#4FC3F7")
    }

    private fun mostrarDialogoNuevaSolicitud() {
        val dialogView = LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, null)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 16)
        }
        val inputClienteId = EditText(this).apply { hint = "Cliente ID (número)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        val inputDesc = EditText(this).apply { hint = "Descripción de necesidad" }
        val inputCantidad = EditText(this).apply { hint = "Cantidad (número)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER; setText("1") }
        val inputProductoAlt = EditText(this).apply { hint = "Producto alternativo (opcional)" }
        layout.addView(inputClienteId)
        layout.addView(inputCantidad)
        layout.addView(inputDesc)
        layout.addView(inputProductoAlt)

        AlertDialog.Builder(this)
            .setTitle("Nueva Solicitud de Alquiler")
            .setView(layout)
            .setPositiveButton("Crear") { _, _ ->
                val clienteId = inputClienteId.text.toString().toIntOrNull()
                if (clienteId == null) {
                    Toast.makeText(this, "El ID de cliente debe ser un número", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val request = SolicitudCreateRequest(
                    clienteId = clienteId,
                    cantidadSolicitada = inputCantidad.text.toString().toIntOrNull() ?: 1,
                    descripcionNecesidad = inputDesc.text.toString().ifBlank { null },
                    nombreProductoAlternativo = inputProductoAlt.text.toString().ifBlank { null }
                )
                crearSolicitud(request)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun crearSolicitud(request: SolicitudCreateRequest) {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.crearSolicitud(request)
            .enqueue(object : Callback<SolicitudResponse> {
                override fun onResponse(call: Call<SolicitudResponse>, response: Response<SolicitudResponse>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@AlquilerActivity, "✅ Solicitud creada: #${response.body()?.id}", Toast.LENGTH_SHORT).show()
                        cargarSolicitudes()
                    } else {
                        Toast.makeText(this@AlquilerActivity, "Error ${response.code()}: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<SolicitudResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@AlquilerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    // ─── Rentas ───────────────────────────────────────

    private fun cargarRentas(clienteId: Int) {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.rentasPorCliente(clienteId)
            .enqueue(object : Callback<List<RentaResponse>> {
                override fun onResponse(call: Call<List<RentaResponse>>, response: Response<List<RentaResponse>>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val lista = response.body() ?: emptyList()
                        if (lista.isEmpty()) {
                            Toast.makeText(this@AlquilerActivity, "Sin rentas para el cliente #$clienteId", Toast.LENGTH_SHORT).show()
                        } else {
                            mostrarRentas(lista)
                        }
                    } else {
                        Toast.makeText(this@AlquilerActivity, "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<RentaResponse>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@AlquilerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun mostrarRentas(lista: List<RentaResponse>) {
        val datos = lista.map {
            "Renta #${it.id} | Estado: ${it.estado}\n" +
            "Inicio: ${it.fechaInicio?.substringBefore("T") ?: "–"}\n" +
            "Fin previsto: ${it.fechaFinPrevista?.substringBefore("T") ?: "–"}\n" +
            "Items: ${it.items.size}"
        }
        recycler.adapter = SimpleTextAdapter(datos, "#4FC3F7") { position ->
            val renta = lista[position]
            mostrarOpcionesRenta(renta)
        }
    }

    private fun mostrarOpcionesRenta(renta: RentaResponse) {
        val opciones = arrayOf("Firmar Entrega (Dirección ID 1)", "Firmar Devolución", "Cerrar")
        AlertDialog.Builder(this)
            .setTitle("Opciones Renta #${renta.id}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarDialogoFirmaEntrega(renta)
                    1 -> mostrarDialogoFirmaDevolucion(renta)
                }
            }
            .show()
    }

    private fun mostrarDialogoFirmaEntrega(renta: RentaResponse) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 16)
        }
        val inputDir = EditText(this).apply { hint = "ID Dirección (número)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER; setText("1") }
        val inputFirma = EditText(this).apply { hint = "Firma Digital (texto/Base64)" }
        val inputNotas = EditText(this).apply { hint = "Notas de entrega" }
        layout.addView(inputDir); layout.addView(inputFirma); layout.addView(inputNotas)

        AlertDialog.Builder(this)
            .setTitle("Firmar Entrega Renta #${renta.id}")
            .setView(layout)
            .setPositiveButton("Firmar") { _, _ ->
                val dirId = inputDir.text.toString().toIntOrNull() ?: 1
                val request = EntregaFirmaRequest(
                    rentaId = renta.id,
                    direccionId = dirId,
                    firmaDigital = inputFirma.text.toString(),
                    notas = inputNotas.text.toString()
                )
                RetrofitInstance.api.firmarEntrega(request).enqueue(object : Callback<EntregaResponse> {
                    override fun onResponse(call: Call<EntregaResponse>, response: Response<EntregaResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AlquilerActivity, "✅ Entrega firmada", Toast.LENGTH_SHORT).show()
                            cargarRentas(renta.clienteId)
                        } else {
                            Toast.makeText(this@AlquilerActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<EntregaResponse>, t: Throwable) {
                        Toast.makeText(this@AlquilerActivity, "Falla: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoFirmaDevolucion(renta: RentaResponse) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 16)
        }
        val inputFirma = EditText(this).apply { hint = "Firma Digital" }
        val inputRecibe = EditText(this).apply { hint = "Nombre de quien recibe" }
        val inputNotas = EditText(this).apply { hint = "Notas generales" }
        layout.addView(inputFirma); layout.addView(inputRecibe); layout.addView(inputNotas)

        AlertDialog.Builder(this)
            .setTitle("Firmar Devolución Renta #${renta.id}")
            .setView(layout)
            .setPositiveButton("Firmar") { _, _ ->
                val request = DevolucionFirmaRequest(
                    rentaId = renta.id,
                    firmaDigital = inputFirma.text.toString(),
                    personaRecibe = inputRecibe.text.toString(),
                    notasGenerales = inputNotas.text.toString()
                )
                RetrofitInstance.api.firmarDevolucion(request).enqueue(object : Callback<DevolucionResponse> {
                    override fun onResponse(call: Call<DevolucionResponse>, response: Response<DevolucionResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AlquilerActivity, "✅ Devolución firmada", Toast.LENGTH_SHORT).show()
                            cargarRentas(renta.clienteId)
                        } else {
                            Toast.makeText(this@AlquilerActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<DevolucionResponse>, t: Throwable) {
                        Toast.makeText(this@AlquilerActivity, "Falla: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

package com.example.axxionsystem.alquiler

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.common.api.RetrofitInstance
import com.example.axxionsystem.alquiler.model.AlquilerItem
import com.example.axxionsystem.alquiler.model.SolicitudCreateRequest
import com.example.axxionsystem.alquiler.model.SolicitudResponse
import com.example.axxionsystem.alquiler.model.RentaResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    private lateinit var tvEmpty: TextView

    // Listas para mantener los datos
    private var listaSolicitudes = mutableListOf<SolicitudResponse>()
    private var listaRentas = mutableListOf<RentaResponse>()
    private var mostrandoRentas = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alquiler)

        progressBar   = findViewById(R.id.progressBar)
        recycler      = findViewById(R.id.recyclerAlquiler)
        btnVolver     = findViewById(R.id.btnVolver)
        btnNueva      = findViewById(R.id.btnNueva)
        btnMisRentas  = findViewById(R.id.btnMisRentas)
        tvEmpty       = findViewById(R.id.tvEmpty)

        recycler.layoutManager = LinearLayoutManager(this)

        btnVolver.setOnClickListener { finish() }

        // Cargar solicitudes al iniciar
        cargarSolicitudes()

        btnNueva.setOnClickListener { mostrarDialogoNuevaSolicitud() }
        btnMisRentas.setOnClickListener { 
            if (!mostrandoRentas) {
                cargarRentas(clienteId = 1)
            } else {
                cargarSolicitudes()
            }
        }
    }

    // ─── Solicitudes ──────────────────────────────────

    private fun cargarSolicitudes() {
        mostrandoRentas = false
        btnMisRentas.text = "📦 Ver mis Rentas (cliente ID 1)"
        progressBar.visibility = View.VISIBLE
        tvEmpty.visibility = View.GONE
        
        RetrofitInstance.api.consultarSolicitudes()
            .enqueue(object : Callback<List<SolicitudResponse>> {
                override fun onResponse(
                    call: Call<List<SolicitudResponse>>,
                    response: Response<List<SolicitudResponse>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        listaSolicitudes = (response.body() ?: emptyList()).toMutableList()
                        mostrarSolicitudes(listaSolicitudes)
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
        val items = lista.map { response ->
            val estadoStr = response.estado?.name?.replace("_", " ") ?: "SIN ESTADO"
            AlquilerItem(
                id = response.id,
                tipo = AlquilerItem.TipoItem.SOLICITUD,
                estado = estadoStr,
                clienteId = response.clienteId,
                cantidad = response.cantidadSolicitada,
                descripcion = response.descripcionNecesidad,
                mostrarAcciones = false
            )
        }
        
        if (items.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            tvEmpty.text = "📋 No hay solicitudes de alquiler.\n¡Crea una nueva solicitud!"
            recycler.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            recycler.visibility = View.VISIBLE
            recycler.adapter = AlquilerAdapter(items)
        }
    }

    private fun mostrarDialogoNuevaSolicitud() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nueva_solicitud, null)
        
        val inputClienteId = dialogView.findViewById<TextInputEditText>(R.id.inputClienteId)
        val inputCantidad = dialogView.findViewById<TextInputEditText>(R.id.inputCantidad)
        val inputDescripcion = dialogView.findViewById<TextInputEditText>(R.id.inputDescripcion)
        val inputProductoAlt = dialogView.findViewById<TextInputEditText>(R.id.inputProductoAlt)

        // Valores por defecto
        inputCantidad.setText("1")

        AlertDialog.Builder(this, R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Nueva Solicitud de Alquiler")
            .setView(dialogView)
            .setPositiveButton("Crear Solicitud") { _, _ ->
                val clienteId = inputClienteId.text.toString().toIntOrNull()
                if (clienteId == null) {
                    Toast.makeText(this, "El ID de cliente debe ser un número", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val request = SolicitudCreateRequest(
                    clienteId = clienteId,
                    cantidadSolicitada = inputCantidad.text.toString().toIntOrNull() ?: 1,
                    descripcionNecesidad = inputDescripcion.text.toString().ifBlank { null },
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
        mostrandoRentas = true
        btnMisRentas.text = "📋 Ver Solicitudes"
        progressBar.visibility = View.VISIBLE
        tvEmpty.visibility = View.GONE
        
        RetrofitInstance.api.rentasPorCliente(clienteId)
            .enqueue(object : Callback<List<RentaResponse>> {
                override fun onResponse(call: Call<List<RentaResponse>>, response: Response<List<RentaResponse>>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        listaRentas = (response.body() ?: emptyList()).toMutableList()
                        if (listaRentas.isEmpty()) {
                            Toast.makeText(this@AlquilerActivity, "Sin rentas para el cliente #$clienteId", Toast.LENGTH_SHORT).show()
                        } else {
                            mostrarRentas(listaRentas)
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
        val items = lista.map { response ->
            val estadoStr = response.estado?.name?.replace("_", " ") ?: "SIN ESTADO"
            AlquilerItem(
                id = response.id,
                tipo = AlquilerItem.TipoItem.RENTA,
                estado = estadoStr,
                clienteId = response.clienteId,
                fechaInicio = response.fechaInicio?.substringBefore("T"),
                fechaFinPrevista = response.fechaFinPrevista?.substringBefore("T"),
                itemsCount = response.items.size,
                mostrarAcciones = true
            )
        }
        
        if (items.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            tvEmpty.text = "📦 No hay rentals activos.\n¡Crea una solicitud de alquiler!"
            recycler.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            recycler.visibility = View.VISIBLE
            recycler.adapter = AlquilerAdapter(items) { position ->
                val renta = lista[position]
                mostrarOpcionesRenta(renta)
            }
        }
    }

    private fun mostrarOpcionesRenta(renta: RentaResponse) {
        val opciones = arrayOf("✍️ Firmar Entrega (Dirección obra)", "↩️ Firmar Devolución", "Cerrar")
        AlertDialog.Builder(this, R.style.Theme_AxxionSystem_Dialog)
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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_firma_entrega, null)
        
        val tvRentaId = dialogView.findViewById<TextView>(R.id.tvRentaId)
        val inputDireccionId = dialogView.findViewById<TextInputEditText>(R.id.inputDireccionId)
        val inputFirma = dialogView.findViewById<TextInputEditText>(R.id.inputFirma)
        val inputNotas = dialogView.findViewById<TextInputEditText>(R.id.inputNotas)

        tvRentaId.text = "Renta #${renta.id}"
        inputDireccionId.setText("1")

        AlertDialog.Builder(this, R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Firmar Entrega")
            .setView(dialogView)
            .setPositiveButton("Firmar Entrega") { _, _ ->
                val dirId = inputDireccionId.text.toString().toIntOrNull() ?: 1
                val request = EntregaFirmaRequest(
                    rentaId = renta.id,
                    direccionId = dirId,
                    firmaDigital = inputFirma.text.toString(),
                    notas = inputNotas.text.toString()
                )
                RetrofitInstance.api.firmarEntrega(request).enqueue(object : Callback<EntregaResponse> {
                    override fun onResponse(call: Call<EntregaResponse>, response: Response<EntregaResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AlquilerActivity, "✅ Entrega firmada correctamente", Toast.LENGTH_SHORT).show()
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
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_firma_devolucion, null)
        
        val tvRentaId = dialogView.findViewById<TextView>(R.id.tvRentaId)
        val inputFirma = dialogView.findViewById<TextInputEditText>(R.id.inputFirma)
        val inputRecibe = dialogView.findViewById<TextInputEditText>(R.id.inputRecibe)
        val inputNotas = dialogView.findViewById<TextInputEditText>(R.id.inputNotas)

        tvRentaId.text = "Renta #${renta.id}"

        AlertDialog.Builder(this, R.style.Theme_AxxionSystem_Dialog)
            .setTitle("Firmar Devolución")
            .setView(dialogView)
            .setPositiveButton("Firmar Devolución") { _, _ ->
                val request = DevolucionFirmaRequest(
                    rentaId = renta.id,
                    firmaDigital = inputFirma.text.toString(),
                    personaRecibe = inputRecibe.text.toString(),
                    notasGenerales = inputNotas.text.toString()
                )
                RetrofitInstance.api.firmarDevolucion(request).enqueue(object : Callback<DevolucionResponse> {
                    override fun onResponse(call: Call<DevolucionResponse>, response: Response<DevolucionResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AlquilerActivity, "✅ Devolución firmada correctamente", Toast.LENGTH_SHORT).show()
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

package com.example.axxionsystem.mantenimiento

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.common.api.RetrofitInstance
import com.example.axxionsystem.mantenimiento.model.MantenimientoCreateRequest
import com.example.axxionsystem.mantenimiento.model.MantenimientoResponse
import com.example.axxionsystem.mantenimiento.model.MantenimientoUpdateRequest
import com.example.axxionsystem.mantenimiento.model.TipoMantenimiento
import com.example.axxionsystem.common.adapters.SimpleTextAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MantenimientoActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recycler: RecyclerView
    private lateinit var btnVolver: Button
    private lateinit var btnReportar: Button
    private lateinit var btnBuscar: Button
    private lateinit var inputResponsable: EditText

    private var listaCargada: List<MantenimientoResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mantenimiento)

        progressBar      = findViewById(R.id.progressBar)
        recycler         = findViewById(R.id.recyclerMantenimiento)
        btnVolver        = findViewById(R.id.btnVolver)
        btnReportar      = findViewById(R.id.btnReportar)
        btnBuscar        = findViewById(R.id.btnBuscar)
        inputResponsable = findViewById(R.id.inputResponsable)

        recycler.layoutManager = LinearLayoutManager(this)

        btnVolver.setOnClickListener { finish() }
        btnReportar.setOnClickListener { mostrarDialogoReporte() }
        btnBuscar.setOnClickListener {
            val responsable = inputResponsable.text.toString().ifBlank { null }
            cargarMantenimientos(responsable)
        }

        // Cargar todo al inicio
        cargarMantenimientos(null)
    }

    // ─── Cargar lista ──────────────────────────────────

    private fun cargarMantenimientos(responsable: String?) {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.consultarMantenimientos(responsable)
            .enqueue(object : Callback<List<MantenimientoResponse>> {
                override fun onResponse(
                    call: Call<List<MantenimientoResponse>>,
                    response: Response<List<MantenimientoResponse>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        listaCargada = response.body() ?: emptyList()
                        mostrarLista(listaCargada)
                    } else {
                        Toast.makeText(this@MantenimientoActivity, "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<MantenimientoResponse>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MantenimientoActivity, "Conexión fallida: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun mostrarLista(lista: List<MantenimientoResponse>) {
        if (lista.isEmpty()) {
            Toast.makeText(this, "Sin registros de mantenimiento", Toast.LENGTH_SHORT).show()
        }
        val datos = lista.map {
            "Mant. #${it.id} | Estado: ${it.estadoMantenimiento ?: "Pendiente"}\n" +
            "Ítem Inventario: ${it.inventarioItemId}\n" +
            "Responsable: ${it.responsable ?: "–"}\n" +
            "Problema: ${it.descripcionProblema ?: "–"}"
        }
        recycler.adapter = SimpleTextAdapter(datos, "#FFB300") {
            // Al tocar, ofrecer actualización
            val item = lista[it]
            mostrarDialogoActualizar(item)
        }
    }

    // ─── Reportar problema ────────────────────────────

    private fun mostrarDialogoReporte() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 16)
        }
        val inputItem = EditText(this).apply { hint = "ID Ítem Inventario (número)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        val inputDesc = EditText(this).apply { hint = "Descripción del problema" }
        val inputResp = EditText(this).apply { hint = "Responsable" }
        val inputCostoEst = EditText(this).apply { hint = "Costo Estimado (ej: 150.00)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL }
        val inputFechaPrev = EditText(this).apply { hint = "Fecha Fin Prevista (AAAA-MM-DD)" }
        
        layout.addView(inputItem); layout.addView(inputDesc); layout.addView(inputResp)
        layout.addView(inputCostoEst); layout.addView(inputFechaPrev)

        AlertDialog.Builder(this)
            .setTitle("Reportar Problema de Mantenimiento")
            .setView(layout)
            .setPositiveButton("Reportar") { _, _ ->
                val itemId = inputItem.text.toString().toIntOrNull()
                if (itemId == null) {
                    Toast.makeText(this, "El ID de ítem debe ser un número", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                crearMantenimiento(
                    MantenimientoCreateRequest(
                        inventarioItemId = itemId,
                        descripcionProblema = inputDesc.text.toString().ifBlank { null },
                        responsable = inputResp.text.toString().ifBlank { null },
                        tipoMantenimiento = TipoMantenimiento.Correctivo,
                        costoEstimado = inputCostoEst.text.toString().toBigDecimalOrNull(),
                        fechaFinPrevista = inputFechaPrev.text.toString().ifBlank { null }
                    )
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun crearMantenimiento(request: MantenimientoCreateRequest) {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.crearMantenimiento(request)
            .enqueue(object : Callback<MantenimientoResponse> {
                override fun onResponse(call: Call<MantenimientoResponse>, response: Response<MantenimientoResponse>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@MantenimientoActivity, "✅ Mantenimiento #${response.body()?.id} creado", Toast.LENGTH_SHORT).show()
                        cargarMantenimientos(null)
                    } else {
                        Toast.makeText(this@MantenimientoActivity, "Error ${response.code()}: ${response.errorBody()?.string()?.take(100)}", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<MantenimientoResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MantenimientoActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    // ─── Actualizar mantenimiento ──────────────────────

    private fun mostrarDialogoActualizar(item: MantenimientoResponse) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 16)
        }
        val inputEstado = EditText(this).apply { hint = "Nuevo estado (ej: En_Proceso, Completado)"; setText(item.estadoMantenimiento ?: "") }
        val inputTrabajo = EditText(this).apply { hint = "Trabajo realizado (opcional)"; setText(item.descripcionTrabajoRealizado ?: "") }
        val inputResp = EditText(this).apply { hint = "Responsable"; setText(item.responsable ?: "") }
        val inputCostoReal = EditText(this).apply { hint = "Costo Real (decimal)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL }
        val inputFechaFin = EditText(this).apply { hint = "Fecha Fin Real (AAAA-MM-DD)" }

        layout.addView(inputEstado); layout.addView(inputTrabajo); layout.addView(inputResp)
        layout.addView(inputCostoReal); layout.addView(inputFechaFin)

        AlertDialog.Builder(this)
            .setTitle("Actualizar Mantenimiento #${item.id}")
            .setView(layout)
            .setPositiveButton("Actualizar") { _, _ ->
                actualizarMantenimiento(
                    item.id,
                    MantenimientoUpdateRequest(
                        estadoMantenimiento = inputEstado.text.toString().ifBlank { null },
                        descripcionTrabajoRealizado = inputTrabajo.text.toString().ifBlank { null },
                        responsable = inputResp.text.toString().ifBlank { null },
                        costoReal = inputCostoReal.text.toString().toBigDecimalOrNull(),
                        fechaFinReal = inputFechaFin.text.toString().ifBlank { null }
                    )
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun actualizarMantenimiento(id: Int, request: MantenimientoUpdateRequest) {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.actualizarMantenimiento(id, request)
            .enqueue(object : Callback<MantenimientoResponse> {
                override fun onResponse(call: Call<MantenimientoResponse>, response: Response<MantenimientoResponse>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@MantenimientoActivity, "✅ Actualizado correctamente", Toast.LENGTH_SHORT).show()
                        cargarMantenimientos(null)
                    } else {
                        Toast.makeText(this@MantenimientoActivity, "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<MantenimientoResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MantenimientoActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}

package com.example.axxionsystem.clientes

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
import com.example.axxionsystem.common.adapters.SimpleTextAdapter
import com.example.axxionsystem.common.api.RetrofitInstance
import com.example.axxionsystem.common.model.ClienteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientesActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var recycler: RecyclerView
    private lateinit var btnVolver: Button
    private lateinit var btnNuevo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        progressBar = findViewById(R.id.progressBar)
        recycler = findViewById(R.id.recyclerClientes)
        btnVolver = findViewById(R.id.btnVolver)
        btnNuevo = findViewById(R.id.btnNuevoCliente)

        recycler.layoutManager = LinearLayoutManager(this)

        btnVolver.setOnClickListener { finish() }
        btnNuevo.setOnClickListener { mostrarDialogoNuevoCliente() }

        cargarClientes()
    }

    private fun cargarClientes() {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.getClientes().enqueue(object : Callback<List<ClienteResponse>> {
            override fun onResponse(call: Call<List<ClienteResponse>>, response: Response<List<ClienteResponse>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    val datos = lista.map {
                        "${it.nombre} ${it.apellido1}\n" +
                        "Email: ${it.correoElectronico ?: "N/A"}\n" +
                        "Tel: ${it.telefonoPrincipal ?: "N/A"}"
                    }
                    recycler.adapter = SimpleTextAdapter(datos, "#4FC3F7")
                } else {
                    Toast.makeText(this@ClientesActivity, "Error ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<ClienteResponse>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@ClientesActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun mostrarDialogoNuevoCliente() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 16)
        }
        val inputNombre = EditText(this).apply { hint = "Primer Nombre" }
        val inputApellidoS = EditText(this).apply { hint = "Primer Apellido" }
        val inputEmail = EditText(this).apply { hint = "Email" }
        val inputTel = EditText(this).apply { hint = "Teléfono" }
        
        layout.addView(inputNombre)
        layout.addView(inputApellidoS)
        layout.addView(inputEmail)
        layout.addView(inputTel)

        AlertDialog.Builder(this)
            .setTitle("Nuevo Cliente")
            .setView(layout)
            .setPositiveButton("Crear") { _, _ ->
                // Nota: El backend recibe el objeto Cliente directamente.
                // En una app real usaríamos un DTO, pero aquí usaremos el modelo del backend simplificado si es necesario.
                // Por ahora, solo mostramos el Toast ya que necesitaría mapear todo el modelo Cliente del backend.
                Toast.makeText(this, "Funcionalidad de guardado en desarrollo", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

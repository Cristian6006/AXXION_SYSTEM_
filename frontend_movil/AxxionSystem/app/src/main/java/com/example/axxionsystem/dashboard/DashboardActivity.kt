package com.example.axxionsystem.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axxionsystem.R
import com.example.axxionsystem.common.adapters.PersonaAdapter
import com.example.axxionsystem.common.api.DataResponse
import com.example.axxionsystem.common.api.RetrofitInstance
import com.example.axxionsystem.common.api.TokenManager
import com.example.axxionsystem.alquiler.AlquilerActivity
import com.example.axxionsystem.clientes.ClientesActivity
import com.example.axxionsystem.mantenimiento.MantenimientoActivity
import com.example.axxionsystem.productos.ProductosActivity
import com.google.android.material.button.MaterialButton
import com.example.axxionsystem.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * DashboardActivity es el panel de control principal o "hub" de la aplicación.
 * A esta pantalla se accede una vez que el usuario ha iniciado sesión exitosamente.
 * Proporciona los accesos directos (botones) para navegar hacia los distintos módulos operativos:
 * - Alquiler y configuración de mantenimiento.
 * - Catálogo de productos y gestión de clientes.
 * También permite visualizar una lista de usuarios registrados consumiendo la API correspondiente, 
 * y provee la opción para cerrar la sesión actual (`logout`).
 */
class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        TokenManager.init(this)

        val logoutButton = findViewById<MaterialButton?>(R.id.buttonLogout)
        val mostrarButton = findViewById<MaterialButton?>(R.id.MostrarApikotlin)
        val segundaButton = findViewById<View?>(R.id.buttonSegundaActividad)
        val alquilerButton = findViewById<View?>(R.id.buttonAlquiler)
        val mantenButton = findViewById<View?>(R.id.buttonMantenimiento)
        val clientesButton = findViewById<View?>(R.id.buttonClientes)
        val progressBar = findViewById<ProgressBar?>(R.id.progressBar)
        val recyclerView = findViewById<RecyclerView?>(R.id.RecyPersonas)

        if (logoutButton == null || mostrarButton == null || segundaButton == null ||
            alquilerButton == null || mantenButton == null || clientesButton == null ||
            progressBar == null || recyclerView == null
        ) {
            Toast.makeText(this, "Error al cargar el dashboard", Toast.LENGTH_LONG).show()
            return
        }

        logoutButton.setOnClickListener {
            TokenManager.clearToken()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        segundaButton.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }

        alquilerButton.setOnClickListener {
            startActivity(Intent(this, AlquilerActivity::class.java))
        }

        mantenButton.setOnClickListener {
            startActivity(Intent(this, MantenimientoActivity::class.java))
        }

        clientesButton.setOnClickListener {
            startActivity(Intent(this, ClientesActivity::class.java))
        }

        mostrarButton.setOnClickListener {
            cargarUsuarios(progressBar, recyclerView)
        }

        // Cargar usuarios al entrar
        cargarUsuarios(progressBar, recyclerView)
    }

    private fun cargarUsuarios(progressBar: ProgressBar, recyclerView: RecyclerView) {
        progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Developer note: If the dashboard is not loading data, check the following:
        // 1. The API endpoint `getPersonas()` is correct and the server is running.
        // 2. The server is accessible from the emulator/device at the BASE_URL in RetrofitInstance.
        // 3. The authentication token is valid.
        // 4. The JSON response from the server matches the `DataResponse` model.
        // Use the Logcat to see the full error from Retrofit's onFailure or onResponse.
        RetrofitInstance.api.getPersonas().enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val dataResponse = response.body()
                    val data = dataResponse?.message
                    if (!data.isNullOrEmpty()) {
                        recyclerView.adapter = PersonaAdapter(data)
                    } else {
                        Toast.makeText(this@DashboardActivity, "No hay usuarios registrados", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("DashboardActivity", "Error: ${response.code()}, body: $errorBody")
                    if (response.code() == 401 || response.code() == 403) {
                        Toast.makeText(
                            this@DashboardActivity,
                            "Sesión expirada. Inicia sesión nuevamente.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@DashboardActivity, "Error al obtener usuarios: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("DashboardActivity", "Failure: ${t.message}", t)
                Toast.makeText(this@DashboardActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}

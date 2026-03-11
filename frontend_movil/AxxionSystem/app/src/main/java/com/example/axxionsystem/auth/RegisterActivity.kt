package com.example.axxionsystem.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.axxionsystem.R
import com.example.axxionsystem.common.api.RetrofitInstance
import com.example.axxionsystem.auth.model.RegisterRequest
import com.example.axxionsystem.auth.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nombreInput      = findViewById<EditText>(R.id.inputNombre)
        val nombre2Input     = findViewById<EditText>(R.id.inputNombre2)
        val apellido1Input   = findViewById<EditText>(R.id.inputApellido1)
        val apellido2Input   = findViewById<EditText>(R.id.inputApellido2)
        val emailInput       = findViewById<EditText>(R.id.inputEmail)
        val userNameInput    = findViewById<EditText>(R.id.inputUserName)
        val telefonoInput    = findViewById<EditText>(R.id.inputTelefono)
        val departamentoInput = findViewById<EditText>(R.id.inputDepartamento)
        val estadoInput      = findViewById<EditText>(R.id.inputEstado)
        val passwordInput    = findViewById<EditText>(R.id.inputPassword)
        val registerButton   = findViewById<Button>(R.id.buttonRegister)
        val backButton       = findViewById<Button>(R.id.buttonBack)
        val progressBar      = findViewById<ProgressBar>(R.id.progressBar)

        backButton.setOnClickListener { finish() }

        registerButton.setOnClickListener {
            val nombre      = nombreInput.text.toString().trim()
            val nombre2     = nombre2Input.text.toString().trim()
            val apellido1   = apellido1Input.text.toString().trim()
            val apellido2   = apellido2Input.text.toString().trim()
            val email       = emailInput.text.toString().trim()
            val userName    = userNameInput.text.toString().trim()
            val telefono    = telefonoInput.text.toString().trim()
            val departamento = departamentoInput.text.toString().trim()
            val estado      = estadoInput.text.toString().trim()
            val password    = passwordInput.text.toString()

            if (listOf(nombre, nombre2, apellido1, apellido2, email, userName, telefono, departamento, estado, password).any { it.isBlank() }) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerButton.isEnabled = false
            progressBar.visibility = View.VISIBLE

            val request = RegisterRequest(
                nombre = nombre,
                email = email,
                nombreUsuario = userName,
                apellido1 = apellido1,
                nombre2 = nombre2,
                apellido2 = apellido2,
                telefono = telefono,
                departamento = departamento,
                estado = estado,
                password = password
            )

            RetrofitInstance.publicApi.register(request)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        progressBar.visibility = View.GONE
                        registerButton.isEnabled = true
                        if (response.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "¡Usuario registrado! Ya puedes iniciar sesión", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                            Toast.makeText(this@RegisterActivity, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        registerButton.isEnabled = true
                        Toast.makeText(this@RegisterActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }
}

package com.example.axxionsystem.auth

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.axxionsystem.R

/**
 * LoginFormController se encarga de gestionar la lógica de interacción de la vista de login.
 * Este controlador procesa los eventos de los campos de texto (email, contraseña), botones de 
 * inicio de sesión y registro, y muestra indicadores de progreso o mensajes de error (Toasts).
 * Utiliza LoginRepository para ejecutar la autenticación contra el backend.
 */
class LoginFormController(
    private val activity: ComponentActivity,
    private val loginRepository: LoginRepository,
    private val onLoginSuccess: () -> Unit,
    private val onRegister: () -> Unit
) {
    fun bind() {
        val emailInput = activity.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordInput = activity.findViewById<EditText>(R.id.editTextTextPassword)
        val loginButton = activity.findViewById<Button>(R.id.button_login)
        val registerButton = activity.findViewById<Button>(R.id.button_register)
        val progressBar = activity.findViewById<ProgressBar>(R.id.progressBar)

        loginButton.setOnClickListener {
            val email = emailInput.text?.toString()?.trim().orEmpty()
            val password = passwordInput.text?.toString().orEmpty()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(activity, "Completa email y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginButton.isEnabled = false
            progressBar.visibility = View.VISIBLE

            loginRepository.login(email, password) { result ->
                progressBar.visibility = View.GONE
                loginButton.isEnabled = true

                when (result) {
                    is LoginResult.Success -> onLoginSuccess()
                    is LoginResult.Error -> {
                        when (result.type) {
                            LoginResult.ErrorType.INVALID_CREDENTIALS -> {
                                Toast.makeText(activity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                            }
                            LoginResult.ErrorType.INVALID_RESPONSE -> {
                                Toast.makeText(activity, "Respuesta inválida del servidor", Toast.LENGTH_SHORT).show()
                            }
                            LoginResult.ErrorType.NETWORK -> {
                                val msg = result.message ?: "Error de conexión"
                                Toast.makeText(activity, "Error de conexión: $msg", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }

        registerButton.setOnClickListener {
            onRegister()
        }
    }
}

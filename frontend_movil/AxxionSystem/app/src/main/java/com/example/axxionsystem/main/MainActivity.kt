package com.example.axxionsystem.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.axxionsystem.common.api.TokenManager
import com.example.axxionsystem.auth.LoginFormController
import com.example.axxionsystem.auth.LoginRepository
import com.example.axxionsystem.auth.RegisterActivity
import com.example.axxionsystem.dashboard.DashboardActivity
import com.example.axxionsystem.R

/**
 * MainActivity es el punto de entrada principal de la aplicación.
 * Su responsabilidad principal es verificar si el usuario tiene una sesión activa:
 * - Si existe un token guardado, redirige directamente al DashboardActivity.
 * - Si no existe un token, inicializa y muestra la vista de inicio de sesión,
 *   delegando el manejo del formulario a LoginFormController.
 */
class MainActivity : ComponentActivity() {

    private val loginRepository = LoginRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar TokenManager con contexto
        TokenManager.init(this)

        // Si ya tiene token, ir directo al Dashboard
        if (TokenManager.isLoggedIn()) {
            goToDashboard()
            return
        }

        setContentView(R.layout.login)

        LoginFormController(
            activity = this,
            loginRepository = loginRepository,
            onLoginSuccess = { goToDashboard() },
            onRegister = { startActivity(Intent(this, RegisterActivity::class.java)) }
        ).bind()
    }

    private fp.exampleun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish() // Add finish() to prevent returning to MainActivity
    }
}

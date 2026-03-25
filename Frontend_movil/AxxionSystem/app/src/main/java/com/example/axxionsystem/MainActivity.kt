package com.example.axxionsystem

/**
 * Activity de entrada de la aplicacion.
 *
 * Instala la SplashScreen del sistema y carga el layout `activity_main`, que
 * contiene el host de navegacion/fragments de la app.
 */
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

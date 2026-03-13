package com.example.axxionsystem

import android.app.Application
import com.example.axxionsystem.common.api.TokenManager

/**
 * AxxionSystemApp es la clase base de la aplicación que hereda de [Application].
 * Se ejecuta antes que cualquier otra actividad u objeto, por lo que es el lugar ideal
 * para realizar inicializaciones globales, como el `TokenManager` para la gestión de sesiones.
 */
class AxxionSystemApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)
    }
}

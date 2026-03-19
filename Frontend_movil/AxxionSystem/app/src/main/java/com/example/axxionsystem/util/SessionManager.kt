package com.example.axxionsystem.util

/**
 * Maneja la sesion local de forma segura.
 *
 * Envuelve [EncryptedSharedPreferences] para guardar/leer:
 * - JWT (access token)
 * - rol del usuario
 * - device id (si se usa)
 *
 * Tambien permite limpiar toda la sesion con [clearSession].
 */
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SessionManager(context: Context) {

    // Crear una llave maestra en el hardware del celular (Keystore)
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Crear el archivo de preferencias encriptado
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_session_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("JWT_TOKEN", token).apply()
    }

    fun saveUserRole(role: String) {
        sharedPreferences.edit().putString("USER_ROLE", role).apply()
    }

    fun saveDeviceid(deviceId: String) {
        sharedPreferences.edit().putString("DEVICE_ID", deviceId).apply()
    }


    fun getAuthToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    fun getUserRole(): String? {
        return sharedPreferences.getString("USER_ROLE", null)
    }

    fun getDeviceId(): String? {
        return sharedPreferences.getString("DEVICE_ID", null)
    }


    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
}

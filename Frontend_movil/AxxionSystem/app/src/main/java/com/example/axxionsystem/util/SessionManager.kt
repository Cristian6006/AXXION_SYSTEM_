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
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

class SessionManager(context: Context) {

    private val KEY_BIOMETRIC_ENABLED = "IS_BIOMETRIC_ENABLED"

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
        sharedPreferences.edit { putString("JWT_TOKEN", token) }
    }

    fun saveUserRole(role: String) {
        sharedPreferences.edit { putString("USER_ROLE", role) }
    }

    fun saveDeviceid(deviceId: String) {
        sharedPreferences.edit { putString("DEVICE_ID", deviceId) }
    }


    fun getAuthToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    fun getUserRole(): String? {
        return sharedPreferences.getString("USER_ROLE", null)
    }



    fun clearSession() {
        sharedPreferences.edit { clear() }
    }


    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun setBiometricEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_BIOMETRIC_ENABLED, isEnabled).apply()
    }

    fun isBiometricEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_BIOMETRIC_ENABLED, false)
    }
}

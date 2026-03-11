package com.example.axxionsystem.common.api

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "axxion_prefs"
    private const val KEY_TOKEN = "access_token"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun getPrefsOrNull(): SharedPreferences? {
        return if (this::prefs.isInitialized) {
            prefs
        } else {
            null
        }
    }

    fun saveToken(token: String) {
        getPrefsOrNull()
            ?.edit()
            ?.putString(KEY_TOKEN, token)
            ?.apply()
    }

    fun getToken(): String? = getPrefsOrNull()?.getString(KEY_TOKEN, null)

    fun clearToken() {
        getPrefsOrNull()
            ?.edit()
            ?.remove(KEY_TOKEN)
            ?.apply()
    }

    fun isLoggedIn(): Boolean = !getToken().isNullOrEmpty()
}

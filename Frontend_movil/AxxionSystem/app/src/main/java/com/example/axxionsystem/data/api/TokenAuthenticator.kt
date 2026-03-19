package com.example.axxionsystem.data.api

/**
 * Authenticator de OkHttp para renovar el token automaticamente.
 *
 * Cuando el servidor responde 401/403 (dependiendo del backend), intenta llamar
 * al endpoint de refresh de forma sincrona. Si obtiene un nuevo access token,
 * lo guarda en [SessionManager] y reintenta el request original con el header
 * `Authorization` actualizado. Si falla, limpia la sesion.
 */
import android.content.Context
import com.example.axxionsystem.util.SessionManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(private val context: Context, private val sessionManager: SessionManager): Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.url.encodedPath.contains("api/auth/refresh")) {
            return null
        }

        val apiService = RetrofitClient.getApiService(context)

        try {
            val refreshResponse = apiService.refreshTokenSync().execute()

            if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                val newToken = refreshResponse.body()!!.accessToken

                sessionManager.saveAuthToken(newToken)

                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
            } else {
                sessionManager.clearSession()
                return null
            }
        } catch(e: Exception) {
            return null
        }
    }
}

package com.example.axxionsystem.data.api

/**
 * Interceptor de OkHttp que adjunta el token JWT en cada request.
 *
 * Si existe un token en [SessionManager], agrega el header:
 * `Authorization: Bearer <token>`.
 */
import com.example.axxionsystem.util.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        val token = sessionManager.getAuthToken()

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}

package com.example.axxionsystem.data.api

/**
 * Factory singleton de Retrofit/OkHttp.
 *
 * Construye y cachea una instancia de [ApiService] con:
 * - [AuthInterceptor] para enviar el JWT.
 * - Logging de requests/responses.
 * - CookieJar en memoria (cookies de sesion).
 * - [TokenAuthenticator] para intentar refrescar el token ante 401.
 * - Timeouts y un interceptor de delay (simula latencia).
 */
import android.content.Context
import com.example.axxionsystem.util.SessionManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Variable para guardar la instancia única
    private var apiService: ApiService? = null

    fun getApiService(context: Context): ApiService {
        if (apiService == null) {
            val sessionManager = SessionManager(context)

            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val delayInterceptor = Interceptor { chain ->
                Thread.sleep(1000)
                chain.proceed(chain.request())
            }

            val cookieJar = object : CookieJar {
                private val cookies = mutableListOf<Cookie>()

                override fun saveFromResponse(url: HttpUrl, newCookies: List<Cookie>) {
                    cookies.clear()
                    cookies.addAll(newCookies)
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return cookies
                }
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(loggingInterceptor)
            .cookieJar(cookieJar)
            .addInterceptor(delayInterceptor)
            .authenticator(TokenAuthenticator(context, sessionManager))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService!!
    }
}

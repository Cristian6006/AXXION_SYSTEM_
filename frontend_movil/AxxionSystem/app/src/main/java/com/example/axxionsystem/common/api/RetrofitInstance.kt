package com.example.axxionsystem.common.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // Gson configurado: lenient para tolerar respuestas no estandar,
    // serializeNulls para enviar campos nulos al backend correctamente
    private val gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttp con token Bearer (rutas protegidas)
    private val authenticatedClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val token = TokenManager.getToken()
                val request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .addHeader("Content-Type", "application/json")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // OkHttp sin token (login y registro)
    private val publicClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Instancia autenticada (todas las rutas protegidas)
    val api: ApiServicesKotlin by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authenticatedClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServicesKotlin::class.java)
    }

    // Instancia publica (login y registro)
    val publicApi: ApiServicesKotlin by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(publicClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServicesKotlin::class.java)
    }
}

package com.example.axxionsystem.data.repository.home

import com.example.axxionsystem.data.api.ApiService
import com.example.axxionsystem.data.model.resumen.ResumenResponse

class DashboardRepository(private val apiService: ApiService) {

    suspend fun getDashboardKpis(): ResumenResponse {
        val response = apiService.getResumen()

        if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                return ResumenResponse(
                    totalProductos = body.totalProductos ?: 0,
                    totalAlquileres = body.totalAlquileres ?: 0,
                    totalMantenimientos = body.totalMantenimientos ?: 0
                )
            } else {
                throw Exception("El servidor no envió datos")
            }
        } else {
            throw Exception("Error del servidor: ${response.code()}")
        }
    }
}


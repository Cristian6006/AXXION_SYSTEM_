package com.example.axxionsystem.data.repository

import com.example.axxionsystem.data.api.ApiService
import com.example.axxionsystem.data.model.Alquiler.AlquilerItem
import com.example.axxionsystem.data.model.Alquiler.SolicitudCreateRequest
import com.example.axxionsystem.data.model.Alquiler.SolicitudResponse
import com.example.axxionsystem.data.model.Alquiler.RentaResponse
import com.example.axxionsystem.data.model.Alquiler.EntregaFirmaRequest
import com.example.axxionsystem.data.model.Alquiler.EntregaResponse
import com.example.axxionsystem.data.model.Alquiler.DevolucionFirmaRequest
import com.example.axxionsystem.data.model.Alquiler.DevolucionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio de Alquiler.
 *
 * Centraliza el acceso a [ApiService] para que el ViewModel consuma una API
 * simple y testable. Maneja las operaciones de:
 * - Consultar y crear solicitudes de alquiler
 * - Consultar rentas por cliente
 * - Firmar procesos de entrega y devolución
 *
 * Utiliza coroutines para operaciones asíncronas, moviendo el trabajo
 * fuera del hilo principal (IO dispatcher).
 *
 * @param apiService Instancia de Retrofit ApiService
 */
class AlquilerRepository(private val apiService: ApiService) {

    /**
     * Consulta la lista de solicitudes de alquiler.
     * @param estado Filtrar por estado (opcional)
     * @return Result con lista de [SolicitudResponse] o excepción
     */
    suspend fun getSolicitudes(estado: String? = null): Result<List<SolicitudResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.consultarSolicitudes(estado)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Crea una nueva solicitud de alquiler.
     * @param request Datos de la solicitud
     * @return Result con [SolicitudResponse] creada o excepción
     */
    suspend fun crearSolicitud(request: SolicitudCreateRequest): Result<SolicitudResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.crearSolicitud(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Consulta las rentas activas de un cliente.
     * @param clienteId ID del cliente
     * @return Result con lista de [RentaResponse] o excepción
     */
    suspend fun getRentasPorCliente(clienteId: Int): Result<List<RentaResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.rentasPorCliente(clienteId)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Firma la entrega de una renta (en dirección de obra).
     * @param request Datos de la firma de entrega
     * @return Result con [EntregaResponse] o excepción
     */
    suspend fun firmarEntrega(request: EntregaFirmaRequest): Result<EntregaResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.firmarEntrega(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Firma la devolución de una renta.
     * @param request Datos de la firma de devolución
     * @return Result con [DevolucionResponse] o excepción
     */
    suspend fun firmarDevolucion(request: DevolucionFirmaRequest): Result<DevolucionResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.firmarDevolucion(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Error de conexión: ${e.message}"))
            }
        }
    }

    /**
     * Convierte una lista de SolicitudResponse a items para el RecyclerView.
     */
    fun mapSolicitudesToItems(solicitudes: List<SolicitudResponse>): List<AlquilerItem> {
        return solicitudes.map { AlquilerItem.fromSolicitud(it) }
    }

    /**
     * Convierte una lista de RentaResponse a items para el RecyclerView.
     */
    fun mapRentasToItems(rentas: List<RentaResponse>): List<AlquilerItem> {
        return rentas.map { AlquilerItem.fromRenta(it) }
    }
}

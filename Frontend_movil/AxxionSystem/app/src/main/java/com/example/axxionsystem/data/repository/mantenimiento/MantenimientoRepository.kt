package com.example.axxionsystem.data.repository.mantenimiento

import com.example.axxionsystem.data.api.ApiService
import com.example.axxionsystem.data.model.mantenimiento.MantenimientoCreateRequest
import com.example.axxionsystem.data.model.mantenimiento.MantenimientoResponse
import com.example.axxionsystem.data.model.mantenimiento.MantenimientoUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para manejar las operaciones relacionadas con el mantenimiento.
 * Abstrae la fuente de datos (API) y proporciona una interfaz limpia para los ViewModels.
 *
 * @param apiService Instancia de ApiService para realizar llamadas a la API.
 */
class MantenimientoRepository(private val apiService: ApiService) {

    /**
     * Crea una nueva solicitud de mantenimiento.
     *
     * @param request Objeto MantenimientoCreateRequest con los datos del nuevo mantenimiento.
     * @return Un objeto Result que indica el éxito o fracaso de la operación.
     */
    suspend fun crearSolicitudMantenimiento(request: MantenimientoCreateRequest): Result<MantenimientoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.crearSolicitudMantenimiento(request)
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
     * Consulta las solicitudes de mantenimiento, opcionalmente filtradas por responsable.
     *
     * @param responsable (Opcional) Nombre del responsable para filtrar las solicitudes.
     * @return Un objeto Result que contiene una lista de MantenimientoResponse o un error.
     */
    suspend fun consultarSolicitudesMantenimiento(responsable: String? = null): Result<List<MantenimientoResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.consultarSolicitudesMantenimiento(responsable)
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
     * Actualiza una solicitud de mantenimiento existente.
     *
     * @param id El ID de la solicitud de mantenimiento a actualizar.
     * @param request Objeto MantenimientoUpdateRequest con los campos a actualizar.
     * @return Un objeto Result que indica el éxito o fracaso de la operación.
     */
    suspend fun actualizarSolicitudMantenimiento(id: Int, request: MantenimientoUpdateRequest): Result<MantenimientoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.actualizarSolicitudMantenimiento(id, request)
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
}

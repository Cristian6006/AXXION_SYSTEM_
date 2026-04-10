package com.example.axxionsystem.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Función de extensión para realizar llamadas seguras a la API.
 * Envuelve la llamada en un bloque try-catch y devuelve un objeto Result.
 *
 * @param dispatcher El CoroutineDispatcher en el que se ejecutará la llamada a la API.
 * @param call La función suspendida que realiza la llamada a la API y devuelve un Response.
 * @return Un objeto Result que contiene el éxito o el error de la llamada.
 */
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> Response<T>
): Result<T> {
    return withContext(dispatcher) {
        try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(IOException("API call successful but returned null body"))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody ?: "Error desconocido en la API"
                Result.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

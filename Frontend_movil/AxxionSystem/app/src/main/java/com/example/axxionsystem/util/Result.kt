package com.example.axxionsystem.util

/**
 * Una clase sellada que representa el resultado de una operación (generalmente de red o base de datos).
 * Puede ser un éxito con datos [Success] o un error con una excepción [Error].
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()

    /**
     * Ejecuta el bloque de código dado si el resultado es exitoso.
     * @param block El bloque de código a ejecutar con los datos.
     * @return El mismo objeto Result para encadenamiento.
     */
    inline fun onSuccess(block: (T) -> Unit): Result<T> {
        if (this is Success) {
            block(data)
        }
        return this
    }

    /**
     * Ejecuta el bloque de código dado si el resultado es un error.
     * @param block El bloque de código a ejecutar con la excepción.
     * @return El mismo objeto Result para encadenamiento.
     */
    inline fun onFailure(block: (Throwable) -> Unit): Result<T> {
        if (this is Error) {
            block(exception)
        }
        return this
    }
}

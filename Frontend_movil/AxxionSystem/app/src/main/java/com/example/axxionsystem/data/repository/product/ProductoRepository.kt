package com.example.axxionsystem.data.repository.product

import android.util.Log
import com.example.axxionsystem.data.api.ApiService
import com.example.axxionsystem.data.local.dao.ProductoDao
import com.example.axxionsystem.data.model.producto.ProductoEntity
import com.example.axxionsystem.data.model.producto.UpdateEstadoRequest
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class ProductoRepository(
    private val apiService: ApiService,
    private val productoDao: ProductoDao
) {
    fun getAllProductosFlow(): Flow<List<ProductoEntity>> {
        return productoDao.getAllProductos()
    }

    fun searchProductosFlow(query: String): Flow<List<ProductoEntity>> {
        return productoDao.searchProductos(query)
    }

    fun getProductoByIdFlow(id: Int): Flow<ProductoEntity?> {
        return productoDao.getProductoById(id)
    }

    suspend fun fetchProductosFromApi() {
        try {
            val response = apiService.getProductos()
            if (response.isSuccessful) {
                response.body()?.let { remoteProductos ->
                    productoDao.insertAllProductos(remoteProductos)
                }
            } else {
                Log.e("Repository", "Error API: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Repository", "Fallo de red, funcionando modo offline", e)
        }
    }

    suspend fun updateEstadoProducto(id: Int, estado: String, notas: String?): Result<Unit> {
        return try {
            val request = UpdateEstadoRequest(estado = estado, notas = notas)
            val response = apiService.updateEstadoProducto(id, request)

            if (response.isSuccessful) {
                response.body()?.let { productoActualizado ->
                    productoDao.updateProducto(productoActualizado)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al actualizar en el servidor: código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
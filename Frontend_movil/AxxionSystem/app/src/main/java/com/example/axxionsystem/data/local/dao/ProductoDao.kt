package com.example.axxionsystem.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.axxionsystem.data.model.producto.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAllProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :query || '%' OR marca LIKE '%' || :query || '%' OR modelo LIKE '%' || :query || '%'")
    fun searchProductos(query: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun getProductoById(id: Int): Flow<ProductoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProductos(productos: List<ProductoEntity>)

    @Update
    suspend fun updateProducto(producto: ProductoEntity)

    @Query("DELETE FROM productos")
    suspend fun deleteAll()
}
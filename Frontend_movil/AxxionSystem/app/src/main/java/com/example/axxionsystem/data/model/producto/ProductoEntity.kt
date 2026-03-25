package com.example.axxionsystem.data.model.producto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val marca: String,
    val modelo: String,
    val numeroSerie: String,
    val estado: String,
    val valorActual: Double,
    val notas: String?
)

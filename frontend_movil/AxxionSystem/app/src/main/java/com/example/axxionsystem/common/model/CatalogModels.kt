package com.example.axxionsystem.common.model

import java.math.BigDecimal

data class ProductoResponse(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val marca: String?,
    val modelo: String?,
    val precioAlquilerDia: BigDecimal?,
    val categoria: String?,
    val estado: String?
)

data class ClienteResponse(
    val id: Int,
    val nombre: String,
    val nombre2: String?,
    val apellido1: String,
    val apellido2: String?,
    val correoElectronico: String?,
    val telefonoPrincipal: String?
)

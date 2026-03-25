package com.example.axxionSystem.dto

import jakarta.validation.constraints.NotBlank

data class ActualizarEstadoRequest(
    @field:NotBlank(message = "El estado es obligatorio")
    val estado: String,
    val notas: String? = null
)

package com.example.axxionsystem.data.model.mantenimiento

import com.google.gson.annotations.SerializedName

/**
 * Data class que representa un objeto de Mantenimiento.
 */
data class Mantenimiento(
    @SerializedName("id") val id: Int,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("responsable") val responsable: String? = null
)

/**
 * Data class para la solicitud de creación de un nuevo mantenimiento.
 */
data class MantenimientoCreateRequest(
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("responsable") val responsable: String? = null
)

/**
 * Data class para la solicitud de actualización de un mantenimiento.
 */
data class MantenimientoUpdateRequest(
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("fecha") val fecha: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("responsable") val responsable: String? = null
)

/**
 * Data class para la respuesta de una operación de mantenimiento.
 */
data class MantenimientoResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("mantenimiento") val mantenimiento: Mantenimiento? = null
)

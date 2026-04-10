package com.example.axxionsystem.data.model.resumen

import com.google.gson.annotations.SerializedName

data class ResumenResponse(
    @SerializedName("totalProductos") val totalProductos: Int?,
    @SerializedName("totalAlquileres") val totalAlquileres: Int?,
    @SerializedName("totalMantenimientos") val totalMantenimientos: Int?
)
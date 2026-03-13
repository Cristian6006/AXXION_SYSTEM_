package com.example.axxionsystem.alquiler.model

/**
 * Modelo unificado para mostrar items de alquiler (solicitudes o rentas)
 * en el RecyclerView con diseño mejorado.
 */
data class AlquilerItem(
    val id: Int,
    val tipo: TipoItem,
    val estado: String,
    val clienteId: Int,
    // Para solicitudes
    val cantidad: Int? = null,
    val descripcion: String? = null,
    // Para rentas
    val fechaInicio: String? = null,
    val fechaFinPrevista: String? = null,
    val itemsCount: Int = 0,
    val mostrarAcciones: Boolean = false
) {
    enum class TipoItem {
        SOLICITUD,
        RENTA
    }

    companion object {
        fun fromSolicitud(response: SolicitudResponse): AlquilerItem {
            // Convertir enum a String
            val estadoStr = response.estado?.name?.replace("_", " ") ?: "SIN ESTADO"
            return AlquilerItem(
                id = response.id,
                tipo = TipoItem.SOLICITUD,
                estado = estadoStr,
                clienteId = response.clienteId,
                cantidad = response.cantidadSolicitada,
                descripcion = response.descripcionNecesidad,
                mostrarAcciones = false
            )
        }

        fun fromRenta(response: RentaResponse): AlquilerItem {
            // Convertir enum a String
            val estadoStr = response.estado?.name?.replace("_", " ") ?: "SIN ESTADO"
            return AlquilerItem(
                id = response.id,
                tipo = TipoItem.RENTA,
                estado = estadoStr,
                clienteId = response.clienteId,
                fechaInicio = response.fechaInicio?.substringBefore("T"),
                fechaFinPrevista = response.fechaFinPrevista?.substringBefore("T"),
                itemsCount = response.items.size,
                mostrarAcciones = true
            )
        }
    }
}

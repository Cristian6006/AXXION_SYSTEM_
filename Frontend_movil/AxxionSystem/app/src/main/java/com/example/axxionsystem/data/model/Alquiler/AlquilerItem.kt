package com.example.axxionsystem.data.model.Alquiler

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
    val nombreProducto: String? = null,
    val fechaReferencia: String? = null,
    // Para rentas
    val fechaInicio: String? = null,
    val fechaFinPrevista: String? = null,
    val itemsCount: Int = 0,
    val mostrarAcciones: Boolean = false
) {
    /**
     * Tipos de items que pueden mostrarse en la lista.
     */
    enum class TipoItem {
        SOLICITUD,
        RENTA
    }

    companion object {
        /**
         * Convierte un [SolicitudResponse] a [AlquilerItem] para visualización.
         */
        fun fromSolicitud(response: SolicitudResponse): AlquilerItem {
            val estadoStr = response.estado.name.replace("_", " ") ?: "SIN ESTADO"
            return AlquilerItem(
                id = response.id,
                tipo = TipoItem.SOLICITUD,
                estado = estadoStr,
                clienteId = response.clienteId,
                cantidad = response.cantidadSolicitada,
                descripcion = response.descripcionNecesidad,
                nombreProducto = response.nombreProductoAlternativo ?: "Producto genérico",
                fechaReferencia = response.fechaSolicitud?.substringBefore("T") ?: "",
                mostrarAcciones = false
            )
        }

        /**
         * Convierte un [RentaResponse] a [AlquilerItem] para visualización.
         */
        fun fromRenta(response: RentaResponse): AlquilerItem {
            val estadoStr = response.estado.name.replace("_", " ") ?: "SIN ESTADO"
            return AlquilerItem(
                id = response.id,
                tipo = TipoItem.RENTA,
                estado = estadoStr,
                clienteId = response.clienteId,
                fechaInicio = response.fechaInicio?.substringBefore("T"),
                fechaFinPrevista = response.fechaFinPrevista?.substringBefore("T"),
                fechaReferencia = response.fechaInicio?.substringBefore("T"),
                itemsCount = response.items.size,
                mostrarAcciones = true
            )
        }
    }
}

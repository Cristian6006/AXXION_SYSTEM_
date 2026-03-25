package com.example.axxionsystem.data.model.Alquiler

/**
 * Modelo unificado para mostrar items de alquiler (solicitudes o rentas)
 * en el RecyclerView con diseño mejorado.
 *
 * @param id ID único del item
 * @param tipo Indica si es SOLICITUD o RENTA
 * @param estado Estado en formato legible
 * @param clienteId ID del cliente asociado
 * @param cantidad Cantidad solicitada (para solicitudes)
 * @param descripcion Descripción de la necesidad (para solicitudes)
 * @param fechaInicio Fecha de inicio de la renta
 * @param fechaFinPrevista Fecha de fin prevista
 * @param itemsCount Cantidad de items en la renta
 * @param mostrarAcciones Si true, muestra botones de acción (firmas)
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

        /**
         * Convierte un [RentaResponse] a [AlquilerItem] para visualización.
         */
        fun fromRenta(response: RentaResponse): AlquilerItem {
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

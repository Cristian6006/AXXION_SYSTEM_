package com.example.axxionSystem.model

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class SolicitudProductoId(
    @Column(name = "solicitud_id")
    val solicitudId: Int = 0,
    @Column(name = "producto_id")
    val productoId: Int = 0
) : Serializable

@Entity
@Table(name = "solicitud_producto")
data class SolicitudProducto(
    @EmbeddedId
    val id: SolicitudProductoId,
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("solicitudId")
    @JoinColumn(name = "solicitud_id")
    val solicitud: Solicitud,
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productoId")
    @JoinColumn(name = "producto_id")
    val producto: Producto
)

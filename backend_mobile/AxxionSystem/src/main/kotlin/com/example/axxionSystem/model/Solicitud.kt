package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "solicitud")
data class Solicitud(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    val cliente: Cliente,
    @Column(name = "fecha_solicitud")
    val fechaSolicitud: Instant? = null,
    @Column(name = "nombre_producto_alternativo")
    val nombreProductoAlternativo: String? = null,
    @Column(name = "cantidad_solicitada")
    val cantidadSolicitada: Int? = 1,
    @Column(name = "descripcion_necesidad")
    val descripcionNecesidad: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud")
    var estadoSolicitud: EstadoSolicitud = EstadoSolicitud.Nueva,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

enum class EstadoSolicitud {
    Nueva,
    EnProceso,
    Atendida,
    Cancelada
}

package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "devolucion")
data class Devolucion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renta_id", nullable = false)
    val renta: Renta,
    @Column(name = "fecha_devolucion_programada")
    val fechaDevolucionProgramada: LocalDate? = null,
    @Column(name = "fecha_devolucion_real")
    val fechaDevolucionReal: LocalDateTime? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_devolucion")
    var estadoDevolucion: EstadoDevolucion = EstadoDevolucion.Pendiente,
    @Column(name = "persona_recibe")
    val personaRecibe: String? = null,
    @Column(name = "notas_generales")
    var notasGenerales: String? = null,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

enum class EstadoDevolucion {
    Pendiente,
    EnProcesoInspeccion,
    Completa,
    IncompletaConProblemas
}

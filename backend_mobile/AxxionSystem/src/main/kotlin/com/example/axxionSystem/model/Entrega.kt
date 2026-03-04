package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "entrega")
data class Entrega(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renta_id", nullable = false)
    val renta: Renta,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_id", nullable = false)
    val direccion: Direccion,
    @Column(name = "fecha_envio")
    val fechaEnvio: LocalDateTime? = null,
    @Column(name = "compania_envio")
    val companiaEnvio: String? = null,
    @Column(name = "numero_guia")
    val numeroGuia: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_entrega")
    var estadoEntrega: EstadoEntrega = EstadoEntrega.Programada,
    @Column(name = "notas")
    var notas: String? = null,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

enum class EstadoEntrega {
    Programada,
    EnTransito,
    Entregada,
    Fallida
}

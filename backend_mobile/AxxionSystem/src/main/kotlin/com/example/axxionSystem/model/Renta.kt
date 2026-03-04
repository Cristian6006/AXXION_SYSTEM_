package com.example.axxionSystem.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "renta")
data class Renta(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    val cliente: Cliente,
    @Column(name = "cotizacion_id")
    val cotizacionId: Int? = null,
    @Column(name = "fecha_inicio", nullable = false)
    val fechaInicio: LocalDateTime,
    @Column(name = "fecha_fin_prevista", nullable = false)
    val fechaFinPrevista: LocalDateTime,
    @Column(name = "fecha_devolucion_real")
    var fechaDevolucionReal: LocalDateTime? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_renta")
    var estadoRenta: EstadoRenta = EstadoRenta.Programada,
    @Column(name = "monto_total_renta")
    val montoTotalRenta: BigDecimal? = null,
    @Column(name = "deposito_garantia")
    val depositoGarantia: BigDecimal? = null,
    @Column(name = "notas")
    val notas: String? = null,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

enum class EstadoRenta {
    Programada,
    EnCurso,
    Finalizada,
    Retrasada,
    Cancelada
}

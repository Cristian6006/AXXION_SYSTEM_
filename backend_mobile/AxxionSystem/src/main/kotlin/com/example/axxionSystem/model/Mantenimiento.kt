package com.example.axxionSystem.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "mantenimiento")
data class Mantenimiento(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_item_id", nullable = false)
    val inventarioItem: InventarioItem,
    @Column(name = "fecha_inicio")
    val fechaInicio: LocalDateTime? = null,
    @Column(name = "fecha_fin_prevista")
    var fechaFinPrevista: LocalDate? = null,
    @Column(name = "fecha_fin_real")
    var fechaFinReal: LocalDate? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mantenimiento")
    var tipoMantenimiento: TipoMantenimiento = TipoMantenimiento.Correctivo,
    @Column(name = "descripcion_problema")
    var descripcionProblema: String? = null,
    @Column(name = "descripcion_trabajo_realizado")
    var descripcionTrabajoRealizado: String? = null,
    @Column(name = "costo_estimado")
    var costoEstimado: BigDecimal? = null,
    @Column(name = "costo_real")
    var costoReal: BigDecimal? = null,
    @Column(name = "estado_mantenimiento")
    var estadoMantenimiento: String? = null,
    @Column(name = "responsable")
    var responsable: String? = null,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

enum class TipoMantenimiento {
    Preventivo,
    Correctivo,
    Mejora
}

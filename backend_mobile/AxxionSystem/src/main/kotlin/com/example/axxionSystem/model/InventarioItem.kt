package com.example.axxionSystem.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "inventario_item")
data class InventarioItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    val producto: Producto,
    @Column(name = "numero_serie")
    val numeroSerie: String? = null,
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_item", nullable = false)
    var estadoItem: EstadoInventarioItem = EstadoInventarioItem.Disponible,
    @Column(name = "fecha_adquisicion")
    val fechaAdquisicion: LocalDate? = null,
    @Column(name = "costo_adquisicion")
    val costoAdquisicion: BigDecimal? = null,
    @Column(name = "ubicacion_fisica")
    val ubicacionFisica: String? = null,
    @Column(name = "notas")
    val notas: String? = null,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

enum class EstadoInventarioItem {
    Disponible,
    Rentado,
    EnMantenimiento,
    DeBaja
}

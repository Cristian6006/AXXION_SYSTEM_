package com.example.axxionSystem.model

import jakarta.persistence.*
import java.io.Serializable
import java.math.BigDecimal

@Embeddable
data class RentaInventarioItemId(
    @Column(name = "renta_id")
    val rentaId: Int = 0,
    @Column(name = "inventario_item_id")
    val inventarioItemId: Int = 0
) : Serializable

@Entity
@Table(name = "renta_inventario_item")
data class RentaInventarioItem(
    @EmbeddedId
    val id: RentaInventarioItemId,
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rentaId")
    @JoinColumn(name = "renta_id")
    val renta: Renta,
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("inventarioItemId")
    @JoinColumn(name = "inventario_item_id")
    val inventarioItem: InventarioItem,
    @Column(name = "precio_renta_item", nullable = false)
    val precioRentaItem: BigDecimal,
    @Column(name = "condicion_salida")
    var condicionSalida: String? = null,
    @Column(name = "condicion_regreso")
    var condicionRegreso: String? = null,
    @Column(name = "notas")
    var notas: String? = null
)

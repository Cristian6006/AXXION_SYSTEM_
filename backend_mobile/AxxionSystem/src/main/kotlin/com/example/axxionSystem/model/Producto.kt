package com.example.axxionSystem.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(name = "producto")
data class Producto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(name = "nombre", nullable = false)
    val nombre: String,
    @Column(name = "descripcion")
    val descripcion: String? = null,
    @Column(name = "marca")
    val marca: String? = null,
    @Column(name = "modelo")
    val modelo: String? = null,
    @Column(name = "precio_referencia_renta")
    val precioReferenciaRenta: BigDecimal? = null,
    @Column(name = "precio_alquiler_dia")
    val precioAlquilerDia: BigDecimal? = null,
    @Column(name = "precio_alquiler_semanal")
    val precioAlquilerSemanal: BigDecimal? = null,
    @Column(name = "precio_alquiler_mensual")
    val precioAlquilerMensual: BigDecimal? = null,
    @Column(name = "precio_compra")
    val precioCompra: BigDecimal? = null,
    @Column(name = "valor_actual")
    val valorActual: BigDecimal? = null,
    @Column(name = "fecha_compra")
    val fechaCompra: LocalDate? = null,
    @Column(name = "condicion")
    val condicion: String? = null,
    @Column(name = "ubicacion")
    val ubicacion: String? = null,
    @Column(name = "notas")
    val notas: String? = null,
    @Column(name = "sku")
    val sku: String? = null,
    @Column(name = "numero_serie")
    val numeroSerie: String? = null,
    @Column(name = "categoria")
    val categoria: String? = null,
    @Column(name = "especificaciones", columnDefinition = "longtext")
    val especificaciones: String? = null,
    @Column(name = "estado")
    val estado: String? = null,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "direccion")
data class Direccion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(name = "calle", nullable = false)
    val calle: String,
    @Column(name = "numero_exterior")
    val numeroExterior: String? = null,
    @Column(name = "numero_interior")
    val numeroInterior: String? = null,
    @Column(name = "colonia")
    val colonia: String? = null,
    @Column(name = "ciudad", nullable = false)
    val ciudad: String,
    @Column(name = "estado_provincia", nullable = false)
    val estadoProvincia: String,
    @Column(name = "codigo_postal", nullable = false)
    val codigoPostal: String,
    @Column(name = "pais", nullable = false)
    val pais: String,
    @Column(name = "referencias")
    val referencias: String? = null,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

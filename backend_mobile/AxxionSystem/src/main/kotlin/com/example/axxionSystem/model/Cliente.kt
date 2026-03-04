package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "cliente")
data class Cliente(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(name = "nombre", nullable = false)
    val nombre: String,
    @Column(name = "nombre2", nullable = false)
    val nombre2: String,
    @Column(name = "apellido1", nullable = false)
    val apellido1: String,
    @Column(name = "apellido2", nullable = false)
    val apellido2: String,
    @Column(name = "rfc")
    val rfc: String? = null,
    @Column(name = "telefono_principal")
    val telefonoPrincipal: String? = null,
    @Column(name = "correo_electronico")
    val correoElectronico: String? = null,
    @Column(name = "tipo_cliente")
    val tipoCliente: String? = null,
    @Column(name = "estado_cliente")
    val estadoCliente: String? = null,
    @Column(name = "created_at")
    val createdAt: Instant? = null,
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

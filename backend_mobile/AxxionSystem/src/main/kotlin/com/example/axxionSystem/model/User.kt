package com.example.axxionSystem.model

import jakarta.persistence.*

@Entity
@Table(name = "usuario")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(name = "usuario_nombre", nullable = false, unique = true)
    val userName: String,
    @Column(name = "nombre", nullable = true)
    val firstName: String,
    @Column(name = "nombre2", nullable = false)
    val secondName: String,
    @Column(name = "apellido1", nullable = false)
    val surName: String,
    @Column(name = "apellido2", nullable = false)
    val surName2: String,
    @Column(name = "password_hash", nullable = false)
    val password: String,
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(name = "telefono")
    val phone: String?,
    @Column(name = "departamento")
    val department: String?,
    @Column(name = "estado")
    val state: String
)

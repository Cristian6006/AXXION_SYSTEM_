package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.Instant

@Entity
data class Rol(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(name = "codigo", nullable = false, unique = true)
    val code: String,
    @Column(name = "nombre")
    val name: String,
    @Column(name = "descripcion", columnDefinition = "TEXT")
    val description: String?,
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now(),
    @Column(name = "updated_at")
    var updatedAt: Instant? = null
)

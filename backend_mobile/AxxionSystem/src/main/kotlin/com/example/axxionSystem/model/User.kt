package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "usuario")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    @Column(name = "nombre_usuario", nullable = false, unique = true)
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
    var password: String,
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(name = "telefono")
    val phone: String?,
    @Column(name = "departamento")
    val department: String?,
    @Column(name = "estado")
    val state: String?,
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now(),
    @Column(name = "updated_at")
    var updatedAt: Instant? = null,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_rol",
        joinColumns = [JoinColumn(name = "usuario_id")],
        inverseJoinColumns = [JoinColumn(name = "rol_id")]
    )
    var roles: MutableSet<Rol> = mutableSetOf()
)

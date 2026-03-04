package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class DispositivoBiometrico(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    val usuario: User,

    @Column(name = "device_id", nullable = false, unique = true)
    val deviceId: String,

    @Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
    var publicKey: String,

    @Column(name = "fecha_registro", nullable = false)
    val fechaRegistro: LocalDateTime = LocalDateTime.now()
)

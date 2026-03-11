package com.example.axxionSystem.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val usuario: User,
    @Column(name = "token_hash", nullable = false, unique = true)
    val tokenHash: String,
    @Column(name = "device_name")
    val deviceName: String? = null,
    @Column(name = "ip_address")
    val ipAddress: String? = null,
    @Column(name = "user_agent", columnDefinition = "TEXT")
    val userAgent: String? = null,
    @Column(name = "expires_at", nullable = false)
    val expiresAt: Instant,
    @Column(name = "last_used_at")
    val lastUsedAt: Instant? = null,
    @Column(name = "created_at")
    val createdAt: Instant = Instant.now(),
    @Column(name = "updated_at")
    var updatedAt: Instant? = null,
)

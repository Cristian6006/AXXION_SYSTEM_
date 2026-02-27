package com.example.axxionSystem.repository

import com.example.axxionSystem.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {

    fun findByTokenHash(tokenHash: String): Optional<RefreshToken>

    fun deleteByUsuario_Id(usuarioId: Int)
}
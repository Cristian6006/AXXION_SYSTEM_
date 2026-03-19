package com.example.axxionSystem.repository

import com.example.axxionSystem.model.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PasswordResetTokenRepository: JpaRepository<PasswordResetToken, Long> {

    fun findByToken(token: String): Optional<PasswordResetToken>

    fun deleteByUsuario_Id(usuarioId: Int)
}
package com.example.axxionSystem.service

import com.example.axxionSystem.dto.LoginRequest
import com.example.axxionSystem.dto.RegisterRequest
import com.example.axxionSystem.model.RefreshToken
import com.example.axxionSystem.model.User
import com.example.axxionSystem.repository.RefreshTokenRepository
import com.example.axxionSystem.repository.UserRepository
import com.example.axxionSystem.util.JwtUtil
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class AuthService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var refreshTokenRepository: RefreshTokenRepository
    @Autowired lateinit var passwordEncoder: PasswordEncoder
    @Autowired lateinit var jwtUtil: JwtUtil

    @Value("\${jwt.refresh-expiration}")
    var refreshExpirationMs: Long = 0

    fun register(request: RegisterRequest): User {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("El correo ya esta registrado")
        }
        val user = User(
            userName = request.nombreUsuario,
            firstName = request.nombre,
            secondName = request.nombre2,
            surName = request.apellido1,
            surName2 = request.apellido2,
            password = passwordEncoder.encode(request.password),
            email = request.email,
            phone = request.telefono,
            department = request.departamento,
            state = request.estado
        )
        return userRepository.save(user)
    }

    fun login(request: LoginRequest): AuthResponse {

        val user = userRepository.findByEmail(request.email).orElseThrow {IllegalArgumentException("Credenciales incorrectas")}

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Credenciales incorrectas")
        }

        val accessToken = jwtUtil.generateAccessToken(user.email)

        val refreshTokenString = UUID.randomUUID().toString()

        val refreshTokenEntity = RefreshToken(
            usuario = user,
            tokenHash = refreshTokenString,
            deviceName = request.deviceName,
            expiresAt = Instant.now().plusMillis(refreshExpirationMs),
            createdAt = Instant.now()
        )
        refreshTokenRepository.save(refreshTokenEntity)

        return AuthResponse(accessToken, refreshTokenString)
    }

    @Transactional
    fun logout(usuarioId: Int) {
        refreshTokenRepository.deleteByUsuario_Id(usuarioId)
    }
}

data class AuthResponse(val accessToken: String, val refreshToken: String)
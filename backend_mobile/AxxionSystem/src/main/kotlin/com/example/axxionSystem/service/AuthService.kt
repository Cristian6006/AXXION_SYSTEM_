package com.example.axxionSystem.service

import com.example.axxionSystem.dto.BiometricLoginRequest
import com.example.axxionSystem.dto.ForgotPasswordRequest
import com.example.axxionSystem.dto.LoginRequest
import com.example.axxionSystem.dto.ResetPasswordRequest
import com.example.axxionSystem.model.PasswordResetToken
import com.example.axxionSystem.model.RefreshToken
import com.example.axxionSystem.repository.*
import com.example.axxionSystem.util.CryptoUtil
import com.example.axxionSystem.util.JwtUtil
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.*
import kotlin.math.abs

@Service
class AuthService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var refreshTokenRepository: RefreshTokenRepository
    @Autowired lateinit var passwordEncoder: PasswordEncoder
    @Autowired lateinit var jwtUtil: JwtUtil
    @Autowired lateinit var emailService: EmailService
    @Autowired lateinit var passwordResetTokenRepository: PasswordResetTokenRepository
    @Autowired lateinit var cryptoUtil: CryptoUtil
    @Autowired lateinit var dispositivoRepository: DispositivoBiometricoRepository



    @Value("\${jwt.refresh-expiration}")
    var refreshExpirationMs: Long = 0

    @org.springframework.transaction.annotation.Transactional
    fun solicitarRecuperacionPassword(request: ForgotPasswordRequest)
    {
        val usuario = userRepository.findByEmail(request.email).orElse(null) ?: return

        passwordResetTokenRepository.deleteByUsuario_Id(usuario.id!!)

        val pinGenerado = generarPinSeguro()

        val resetToken = PasswordResetToken(
            token = pinGenerado,
            usuario = usuario,
            fechaExpiracion = java.time.LocalDateTime.now().plusMinutes(15)
        )
        passwordResetTokenRepository.save(resetToken)

        emailService.enviarCorreoRecuperacion(usuario.email, pinGenerado)
    }

    @org.springframework.transaction.annotation.Transactional
    fun restablecerPassword(request: ResetPasswordRequest) {
        val resetToken = passwordResetTokenRepository.findByToken(request.token)
            .orElseThrow { IllegalArgumentException("El código PIN es inválido o no existe.") }

        if (resetToken.fechaExpiracion.isBefore(java.time.LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken)
            throw IllegalArgumentException("El código PIN ha expirado. Solicita uno nuevo.")
        }

        val usuario = resetToken.usuario
        usuario.password = passwordEncoder.encode(request.nuevaPassword)
        userRepository.save(usuario)

        passwordResetTokenRepository.delete(resetToken)
    }

    @Transactional
    fun logout(usuarioId: Int) {
        refreshTokenRepository.deleteByUsuario_Id(usuarioId)
    }

    fun login(request: LoginRequest): AuthResponse {

        val user = userRepository.findByEmail(request.email).orElseThrow {IllegalArgumentException("Credenciales incorrectas")}

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Credenciales incorrectas")
        }

        val accessToken = jwtUtil.generateAccessToken(user.email)

        val refreshTokenString = UUID.randomUUID().toString()

        val hashedToken = hashToken(refreshTokenString)

        val refreshTokenEntity = RefreshToken(
            usuario = user,
            tokenHash = hashedToken,
            deviceName = request.deviceName,
            expiresAt = Instant.now().plusMillis(refreshExpirationMs),
            createdAt = Instant.now()
        )
        refreshTokenRepository.save(refreshTokenEntity)

        return AuthResponse(accessToken, refreshTokenString)
    }

    fun loginBiometrico(request: BiometricLoginRequest): AuthResponse {

        val dispositivo = dispositivoRepository.findByDeviceId(request.deviceId)
            .orElseThrow { IllegalArgumentException("Dispositivo no registrado para biometría.") }

        val ahora = System.currentTimeMillis()
        val diferenciaSegundos = abs(ahora - request.timestamp) / 1000

        if (diferenciaSegundos > 60) {
            throw IllegalArgumentException("La petición expiró. Posible ataque de repetición detectado.")
        }

        val payloadEsperado = "${request.deviceId}|${request.timestamp}"

        val firmaValida = cryptoUtil.verifySignature(
            publicKeyBase64 = dispositivo.publicKey,
            payload = payloadEsperado,
            signatureBase64 = request.signature
        )

        if (!firmaValida) {
            throw IllegalArgumentException("Firma biométrica inválida. Acceso denegado.")
        }

        val usuario = dispositivo.usuario
        val accessToken = jwtUtil.generateAccessToken(usuario.email)

        val refreshTokenString = UUID.randomUUID().toString()

        val hashedToken = hashToken(refreshTokenString)

        val refreshTokenEntity = RefreshToken(
            usuario = usuario,
            tokenHash = hashedToken,
            deviceName = request.deviceId,
            expiresAt = Instant.now().plusMillis(refreshExpirationMs),
            createdAt = Instant.now()
        )
        refreshTokenRepository.save(refreshTokenEntity)

        return AuthResponse(accessToken, refreshTokenString)
    }
    @org.springframework.transaction.annotation.Transactional
    fun refreshAccessToken(refreshToken: String): AuthResponse  {
        val hashedIncomingToken = hashToken(refreshToken)

        val tokenEntity = refreshTokenRepository.findByTokenHash(hashedIncomingToken).
                orElseThrow {IllegalArgumentException("Token de refresco inválido")}

        if (tokenEntity.expiresAt.isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity)
            throw IllegalArgumentException("La sesión ha expirado. Inicie sesión nuevamente.")
        }

        refreshTokenRepository.delete(tokenEntity)

        val nuevoAccessToken = jwtUtil.generateAccessToken(tokenEntity.usuario.email)

        val nuevoRefreshToken = UUID.randomUUID().toString()
        val nuevoHashedToken = hashToken(nuevoRefreshToken)

        val nuevoTokenEntity = RefreshToken(
            usuario = tokenEntity.usuario,
            tokenHash = nuevoHashedToken,
            deviceName = tokenEntity.deviceName,
            expiresAt = Instant.now().plusMillis(refreshExpirationMs),
            createdAt = Instant.now()
        )
        refreshTokenRepository.save(nuevoTokenEntity)

        return AuthResponse(nuevoAccessToken, nuevoRefreshToken)
    }

    private fun generarPinSeguro(): String {
        val random = java.security.SecureRandom()
        val pin = random.nextInt(999999)

        return String.format("%06d", pin)
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return hashBytes.joinToString("") {"%02x".format(it)}
    }

}

data class AuthResponse(val accessToken: String, val refreshToken: String)

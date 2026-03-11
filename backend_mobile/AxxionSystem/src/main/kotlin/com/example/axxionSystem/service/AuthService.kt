package com.example.axxionSystem.service

import com.example.axxionSystem.dto.BiometricLoginRequest
import com.example.axxionSystem.dto.ForgotPasswordRequest
import com.example.axxionSystem.dto.LoginRequest
import com.example.axxionSystem.dto.RegisterRequest
import com.example.axxionSystem.dto.ResetPasswordRequest
import com.example.axxionSystem.model.PasswordResetToken
import com.example.axxionSystem.model.RefreshToken
import com.example.axxionSystem.model.User
import com.example.axxionSystem.repository.DispositivoBiometricoRepository
import com.example.axxionSystem.repository.PasswordResetTokenRepository
import com.example.axxionSystem.repository.RefreshTokenRepository
import com.example.axxionSystem.repository.UserRepository
import com.example.axxionSystem.repository.RolRepository
import com.example.axxionSystem.util.CryptoUtil
import com.example.axxionSystem.util.JwtUtil
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import java.security.MessageDigest
import kotlin.math.abs

@Service
class AuthService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var refreshTokenRepository: RefreshTokenRepository
    @Autowired lateinit var rolRepository: RolRepository
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

        val rolBasico = rolRepository.findByCode("OPER")
            .orElseThrow {RuntimeException("Error Critico: El rol OPER no existe en la BD")}

        user.roles
            .add(rolBasico)

        return userRepository.save(user)
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

    fun refreshAccessToken(refreshToken: String): String {
        val hashedIncomingToken = hashToken(refreshToken)

        val tokenEntity = refreshTokenRepository.findByTokenHash(hashedIncomingToken).
                orElseThrow {IllegalArgumentException("Token de refresco inválido")}

        if (tokenEntity.expiresAt.isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity)
            throw IllegalArgumentException("Token de refresco expirado")
        }

        tokenEntity.lastUsedAt = Instant.now()
        refreshTokenRepository.save(tokenEntity)

        return jwtUtil.generateAccessToken(tokenEntity.usuario.email)
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

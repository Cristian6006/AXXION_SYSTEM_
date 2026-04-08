package com.example.axxionSystem.service

import com.example.axxionSystem.dto.BiometricDeviceStatusResponse
import com.example.axxionSystem.dto.BiometricRegisterRequest
import com.example.axxionSystem.model.DispositivoBiometrico
import com.example.axxionSystem.repository.DispositivoBiometricoRepository
import com.example.axxionSystem.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class BiometriaService {

    @Autowired lateinit var dispositivoRepository: DispositivoBiometricoRepository
    @Autowired lateinit var userRepository: UserRepository

    fun verificarDispositivoRegistrado(deviceId: String): BiometricDeviceStatusResponse {
        val registered = dispositivoRepository.existsByDeviceId(deviceId)
        return BiometricDeviceStatusResponse(
            registered = registered,
            deviceId = deviceId
        )
    }

    fun registrarDispositivo(request: BiometricRegisterRequest) {

        val emailAutenticado = SecurityContextHolder.getContext().authentication.name

        val usuario = userRepository.findByEmail(emailAutenticado)
            .orElseThrow { UsernameNotFoundException("Usuario no encontrado") }

        val dispositivoExistente = dispositivoRepository.findByDeviceId(request.deviceId).orElse(null)

        if (dispositivoExistente != null) {
            if (dispositivoExistente.usuario.id != usuario.id) {
                throw IllegalArgumentException("Este dispositivo ya está registrado a otro usuario.")
            }
            dispositivoExistente.publicKey = request.publicKey
            dispositivoRepository.save(dispositivoExistente)
        } else {
            val nuevoDispositivo = DispositivoBiometrico(
                usuario = usuario,
                deviceId = request.deviceId,
                publicKey = request.publicKey
            )
            dispositivoRepository.save(nuevoDispositivo)
        }
    }
}

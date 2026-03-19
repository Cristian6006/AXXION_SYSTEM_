package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.BiometricRegisterRequest
import com.example.axxionSystem.service.BiometriaService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/biometria")
class BiometriaController {

    @Autowired lateinit var biometriaService: BiometriaService

    @PostMapping("/registrar")
    fun registrarLlave(@Valid @RequestBody request: BiometricRegisterRequest): ResponseEntity<Any> {
        biometriaService.registrarDispositivo(request)
        return ResponseEntity.ok(mapOf("mensaje" to "Dispositivo biométrico registrado exitosamente para futuros inicios de sesión."))
    }
}
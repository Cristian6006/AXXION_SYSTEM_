package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.UserProfileResponse
import com.example.axxionSystem.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/usuario")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/perfil")
    fun verPerfil(): ResponseEntity<UserProfileResponse> {
        val perfil = userService.obtenerPerfilActual()
        return ResponseEntity.ok(perfil)
    }

}
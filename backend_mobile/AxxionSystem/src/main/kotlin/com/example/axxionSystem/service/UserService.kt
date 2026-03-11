package com.example.axxionSystem.service

import com.example.axxionSystem.dto.UserProfileResponse
import com.example.axxionSystem.repository.RefreshTokenRepository
import com.example.axxionSystem.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var refreshTokenRepository: RefreshTokenRepository

    fun obtenerPerfilActual(): UserProfileResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = authentication.name

        val usuario = userRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("Usuario no encontrado") }

        return UserProfileResponse(
            id = usuario.id,
            nombreUsuario = usuario.userName,
            nombre = usuario.firstName,
            nombre2 = usuario.secondName,
            apellido1 = usuario.surName,
            apellido2 = usuario.surName2,
            email = usuario.email,
            telefono = usuario.phone,
            departamento = usuario.department,
            estado = usuario.state,
            roles = usuario.roles.map { it.name }
        )
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }




}
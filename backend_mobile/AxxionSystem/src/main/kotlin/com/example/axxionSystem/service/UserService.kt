package com.example.axxionSystem.service

import com.example.axxionSystem.dto.RegisterRequest
import com.example.axxionSystem.dto.UserProfileResponse
import com.example.axxionSystem.repository.RefreshTokenRepository
import com.example.axxionSystem.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import com.example.axxionSystem.model.User
import com.example.axxionSystem.repository.RolRepository
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var rolRepository: RolRepository

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

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }




}
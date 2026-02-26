package com.example.axxionSystem.service

import com.example.axxionSystem.model.User
import com.example.axxionSystem.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun registerUser(user: User): User {
        val email = user.email

        if (userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("El correo ${user.email} ya esta registrado ")
        }

        return userRepository.save(user)
    }

    fun getUserById(id:Int): User? {
        return userRepository.findById(id).orElse(null)
    }




}
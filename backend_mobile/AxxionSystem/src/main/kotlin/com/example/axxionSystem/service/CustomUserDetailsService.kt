package com.example.axxionSystem.service

import com.example.axxionSystem.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService: UserDetailsService {

    @Autowired lateinit var userRepository: UserRepository

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            .orElseThrow {UsernameNotFoundException("Usuario no enccontrado con email: $email")}

        val autoridad = user.roles.map {
            SimpleGrantedAuthority(it.code)
        }

        return User(user.email, user.password, autoridad )
    }
}
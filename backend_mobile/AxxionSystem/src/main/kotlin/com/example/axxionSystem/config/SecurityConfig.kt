package com.example.axxionSystem.config

import com.example.axxionSystem.service.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired lateinit var jwtAuthFilter: JwtAuthenticationFilter
    @Autowired lateinit var userDetailsService: CustomUserDetailsService

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf {it.disable()} // Desactivar CSRF
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/auth/**").permitAll() // PERMITIR PÚBLICAMENTE: Login, Registro y Refresh
                auth.anyRequest().authenticated() // BLOQUEAR TODO LO DEMÁS
            }
            .sessionManagement { session -> // NO GUARDAR ESTADO (STATELESS) -> Usar JWT
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // AÑADIR NUESTRO FILTRO JWT ANTES DEL FILTRO DE SPRING
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    // Conecta BD y Encriptación
    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}
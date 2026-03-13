package com.example.axxionSystem.config

import com.example.axxionSystem.service.CustomUserDetailsService
import com.example.axxionSystem.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter: OncePerRequestFilter(){

    @Autowired lateinit var jwtUtil: JwtUtil
    @Autowired lateinit var userDetailsService: CustomUserDetailsService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ", ignoreCase = true)) {
            val token = authHeader.substring(7).trim()

            if(jwtUtil.validateToken(token)) {
                val email = jwtUtil.extractUsername(token)

                if(SecurityContextHolder.getContext(). authentication == null) {
                    val userDetails = userDetailsService.loadUserByUsername(email)
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authToken

                }
            } else {
                println("El token falló la validación (Expirado o Firma Inválida)")
            }
        }
        filterChain.doFilter(request, response)
    }
}

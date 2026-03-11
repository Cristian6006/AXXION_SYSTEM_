package com.example.axxionSystem.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService {

    @Autowired lateinit var mailSender: JavaMailSender

    fun enviarCorreoRecuperacion(destinatario: String, pin: String) {
        val mensaje = SimpleMailMessage()

        mensaje.from = "soporte@axxion.com"
        mensaje.setTo(destinatario)
        mensaje.subject = "Codigo de Recuperacion de Contraseña"
        mensaje.text = """
            Hola,
            
            Recibimos una solicitud para restablecer tu contraseña.
            Tu código de seguridad de 6 dígitos es:
            
            $pin
            
            Este código expirará en 15 minutos. Si no solicitaste este cambio, ignora este correo.
            
            Saludos,
            El equipo de Soporte.
        """.trimIndent()

        mailSender.send(mensaje)
    }
}
package com.example.axxionSystem.controller

import com.example.axxionSystem.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/usuarios")
class UsuariosController {
    @Autowired
    lateinit var userService: UserService

    @GetMapping
    fun getPersonas(): List<String> {
        // We will return a combined string or just the first name depending on what the app is showing.
        // The app concatenates: "Nombre " + persona.getNombre() + " Edad " + persona.getEdad()
        // Wait, the app calls `getPersonas` which expects a List<String>.
        // Let's return just names or something useful. Let's return the user's first name + " " + user's surname.
        return userService.getAllUsers().map { "${it.firstName} ${it.surName}" }
    }
}

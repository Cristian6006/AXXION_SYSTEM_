package com.example.axxionSystem.controller

import com.example.axxionSystem.model.User
import com.example.axxionSystem.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/usuario")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/registro")
    fun registrar(@RequestBody user: User): ResponseEntity<Any> {
        return try {
            val newUser = userService.registerUser(user)
            ResponseEntity.status(HttpStatus.CREATED).body(newUser)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/{id}")
    fun getId(@PathVariable id: Int): User? = userService.getUserById(id)
}
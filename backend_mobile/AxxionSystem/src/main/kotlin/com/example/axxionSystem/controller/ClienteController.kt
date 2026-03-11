package com.example.axxionSystem.controller

import com.example.axxionSystem.model.Cliente
import com.example.axxionSystem.service.ClienteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/clientes")
class ClienteController {

    @Autowired lateinit var clienteService: ClienteService

    @GetMapping
    fun getAll(): ResponseEntity<List<Cliente>> {
        return ResponseEntity.ok(clienteService.getAllClientes())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<Cliente> {
        val cliente = clienteService.getClienteById(id)
        return if (cliente != null) ResponseEntity.ok(cliente) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun create(@RequestBody cliente: Cliente): ResponseEntity<Cliente> {
        return ResponseEntity.ok(clienteService.saveCliente(cliente))
    }
}

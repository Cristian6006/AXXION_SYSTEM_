package com.example.axxionSystem.service

import com.example.axxionSystem.model.Cliente
import com.example.axxionSystem.repository.ClienteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClienteService {

    @Autowired lateinit var clienteRepository: ClienteRepository

    fun getAllClientes(): List<Cliente> {
        return clienteRepository.findAll()
    }

    fun getClienteById(id: Int): Cliente? {
        return clienteRepository.findById(id).orElse(null)
    }

    @Transactional
    fun saveCliente(cliente: Cliente): Cliente {
        return clienteRepository.save(cliente)
    }
}

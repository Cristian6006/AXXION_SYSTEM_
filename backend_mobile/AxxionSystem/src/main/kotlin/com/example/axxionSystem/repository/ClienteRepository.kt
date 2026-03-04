package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Cliente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClienteRepository : JpaRepository<Cliente, Int>

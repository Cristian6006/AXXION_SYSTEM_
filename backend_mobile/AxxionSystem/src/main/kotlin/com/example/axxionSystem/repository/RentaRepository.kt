package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Renta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RentaRepository : JpaRepository<Renta, Int> {
    fun findByClienteId(clienteId: Int): List<Renta>
}

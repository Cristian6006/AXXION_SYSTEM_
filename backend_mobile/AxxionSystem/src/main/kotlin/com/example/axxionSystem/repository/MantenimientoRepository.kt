package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Mantenimiento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MantenimientoRepository : JpaRepository<Mantenimiento, Int> {
    fun findByResponsable(responsable: String): List<Mantenimiento>
}

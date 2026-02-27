package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Rol
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RolRepository: JpaRepository<Rol, Int> {
    fun findByCode(code: String): Optional<Rol>
}
package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Direccion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DireccionRepository : JpaRepository<Direccion, Int>

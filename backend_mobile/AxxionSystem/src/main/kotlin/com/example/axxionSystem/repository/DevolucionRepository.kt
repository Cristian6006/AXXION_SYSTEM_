package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Devolucion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DevolucionRepository : JpaRepository<Devolucion, Int>

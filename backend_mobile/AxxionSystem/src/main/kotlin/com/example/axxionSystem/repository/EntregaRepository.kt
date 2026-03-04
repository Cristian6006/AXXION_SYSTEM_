package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Entrega
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EntregaRepository : JpaRepository<Entrega, Int>

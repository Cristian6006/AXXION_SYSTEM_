package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Producto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductoRepository : JpaRepository<Producto, Int>

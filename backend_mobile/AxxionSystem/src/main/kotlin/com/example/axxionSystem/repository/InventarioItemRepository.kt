package com.example.axxionSystem.repository

import com.example.axxionSystem.model.InventarioItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InventarioItemRepository : JpaRepository<InventarioItem, Int>

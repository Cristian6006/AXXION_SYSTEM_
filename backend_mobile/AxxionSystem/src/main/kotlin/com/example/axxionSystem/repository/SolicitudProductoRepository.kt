package com.example.axxionSystem.repository

import com.example.axxionSystem.model.SolicitudProducto
import com.example.axxionSystem.model.SolicitudProductoId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SolicitudProductoRepository : JpaRepository<SolicitudProducto, SolicitudProductoId> {
    fun findByIdSolicitudId(solicitudId: Int): List<SolicitudProducto>
}

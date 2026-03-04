package com.example.axxionSystem.repository

import com.example.axxionSystem.model.EstadoSolicitud
import com.example.axxionSystem.model.Solicitud
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface SolicitudRepository : JpaRepository<Solicitud, Int> {

    @Query("""
        select s from Solicitud s
        where (:estado is null or s.estadoSolicitud = :estado)
          and (:desde is null or s.fechaSolicitud >= :desde)
          and (:hasta is null or s.fechaSolicitud < :hasta)
        order by s.fechaSolicitud desc
    """)
    fun findByFiltro(
        @Param("estado") estado: EstadoSolicitud?,
        @Param("desde") desde: Instant?,
        @Param("hasta") hasta: Instant?
    ): List<Solicitud>
}

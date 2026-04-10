package com.example.axxionSystem.repository

import com.example.axxionSystem.model.Mantenimiento
import com.example.axxionSystem.model.TipoMantenimiento
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * Repositorio para gestionar las operaciones de persistencia de Mantenimiento.
 * Proporciona métodos de consulta personalizados para filtrar mantenimientos.
 */
@Repository
interface MantenimientoRepository : JpaRepository<Mantenimiento, Int> {

    /**
     * Busca mantenimientos por responsable.
     */
    fun findByResponsable(responsable: String): List<Mantenimiento>

    /**
     * Busca mantenimientos por estado.
     */
    fun findByEstadoMantenimiento(estado: String): List<Mantenimiento>

    /**
     * Busca mantenimientos por tipo.
     */
    fun findByTipoMantenimiento(tipo: TipoMantenimiento): List<Mantenimiento>

    /**
     * Busca mantenimientos por ID del item de inventario.
     */
    fun findByInventarioItemId(inventarioItemId: Int): List<Mantenimiento>

    /**
     * Consulta avanzado de mantenimientos con filtros opcionales.
     * @param estado Filtrar por estado (opcional)
     * @param responsable Filtrar por responsable (opcional)
     * @param fechaDesde Filtrar por fecha de inicio desde (opcional)
     * @param fechaHasta Filtrar por fecha de inicio hasta (opcional)
     * @param tipo Filtrar por tipo de mantenimiento (opcional)
     * @param pageable Paginación
     */
    @Query("""
        SELECT m FROM Mantenimiento m
        WHERE (:estado IS NULL OR LOWER(m.estadoMantenimiento) = LOWER(:estado))
        AND (:responsable IS NULL OR LOWER(m.responsable) LIKE LOWER(CONCAT('%', :responsable, '%')))
        AND (:fechaDesde IS NULL OR m.fechaInicio >= :fechaDesde)
        AND (:fechaHasta IS NULL OR m.fechaInicio <= :fechaHasta)
        AND (:tipo IS NULL OR m.tipoMantenimiento = :tipo)
    """)
    fun buscarConFiltros(
        @Param("estado") estado: String?,
        @Param("responsable") responsable: String?,
        @Param("fechaDesde") fechaDesde: LocalDate?,
        @Param("fechaHasta") fechaHasta: LocalDate?,
        @Param("tipo") tipo: TipoMantenimiento?,
        pageable: Pageable
    ): Page<Mantenimiento>

    /**
     * Cuenta mantenimientos por estado.
     */
    fun countByEstadoMantenimiento(estado: String): Long

    /**
     * Obtiene los mantenimientos pendientes (no finalizados ni cancelados).
     */
    @Query("""
        SELECT m FROM Mantenimiento m
        WHERE m.estadoMantenimiento NOT IN ('Finalizado', 'Cancelado')
    """)
    fun findPendientes(): List<Mantenimiento>
}

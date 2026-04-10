package com.example.axxionSystem.service

import com.example.axxionSystem.dto.MantenimientoCreateRequest
import com.example.axxionSystem.dto.MantenimientoFilterRequest
import com.example.axxionSystem.dto.MantenimientoPageResponse
import com.example.axxionSystem.dto.MantenimientoResponse
import com.example.axxionSystem.dto.MantenimientoUpdateRequest
import com.example.axxionSystem.model.EstadoInventarioItem
import com.example.axxionSystem.model.Mantenimiento
import com.example.axxionSystem.repository.InventarioItemRepository
import com.example.axxionSystem.repository.MantenimientoRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Servicio para gestionar las operaciones de mantenimiento de equipos.
 * 
 * Responsabilidades:
 * - Crear nuevas solicitudes de mantenimiento
 * - Consultar y filtrar mantenimientos existentes
 * - Actualizar el estado de mantenimientos
 * - Eliminar mantenimientos (cancelación lógica)
 * 
 * Este servicio sigue el principio de responsabilidad única (SRP) enfocándose
 * únicamente en la lógica de negocio de mantenimientos.
 */
@Service
class MantenimientoService {

    private val logger = LoggerFactory.getLogger(MantenimientoService::class.java)

    @Autowired lateinit var mantenimientoRepository: MantenimientoRepository
    @Autowired lateinit var inventarioItemRepository: InventarioItemRepository

    /**
     * Crea una nueva solicitud de mantenimiento.
     * 
     * Valida que el item de inventario exista y cambia su estado a "EnMantenimiento".
     * 
     * @param request Datos de la solicitud de mantenimiento
     * @return MantenimientoResponse con los datos del mantenimiento creado
     * @throws IllegalArgumentException si el item de inventario no existe
     */
    @Transactional
    fun crearSolicitud(request: MantenimientoCreateRequest): MantenimientoResponse {
        logger.info("Creando solicitud de mantenimiento para item: ${request.inventarioItemId}")
        
        // Validar que el inventario existe
        val inventario = inventarioItemRepository.findById(request.inventarioItemId)
            .orElseThrow { 
                logger.error("Inventario no encontrado: ${request.inventarioItemId}")
                IllegalArgumentException("Inventario no encontrado con ID: ${request.inventarioItemId}") 
            }

        // Validar el request
        request.validate()

        // Combinar descripción con evidencia si existe
        val descripcion = mergeEvidencia(request.descripcionProblema, request.evidenciaFoto)

        // Crear el mantenimiento
        val mantenimiento = mantenimientoRepository.save(
            Mantenimiento(
                inventarioItem = inventario,
                fechaInicio = LocalDateTime.now(),
                fechaFinPrevista = request.fechaFinPrevista,
                tipoMantenimiento = request.tipoMantenimiento ?: getDefaultTipo(),
                descripcionProblema = descripcion,
                costoEstimado = request.costoEstimado,
                estadoMantenimiento = "En revision",
                responsable = request.responsable
            )
        )

        // Actualizar el estado del inventario a "En Mantenimiento"
        inventario.estadoItem = EstadoInventarioItem.EnMantenimiento
        inventarioItemRepository.save(inventario)

        logger.info("Solicitud de mantenimiento creada exitosamente: ${mantenimiento.id}")
        return mapResponseCompleto(mantenimiento)
    }

    /**
     * Consulta todas las solicitudes de mantenimiento con filtros opcionales.
     * 
     * @param responsable Filtrar por responsable (opcional)
     * @return Lista de mantenimientos encontrados
     */
    fun consultarSolicitudes(responsable: String?): List<MantenimientoResponse> {
        logger.info("Consultando solicitudes de mantenimiento. Responsable: $responsable")
        
        val lista = if (responsable.isNullOrBlank()) {
            mantenimientoRepository.findAll()
        } else {
            mantenimientoRepository.findByResponsable(responsable)
        }
        
        return lista.map { mapResponseCompleto(it) }
    }

    /**
     * Consulta mantenimientos con paginación y filtros avanzados.
     * 
     * @param filterRequest Filtros de búsqueda
     * @param pagina Número de página (0-indexed)
     * @param tamanoPagina Tamaño de página
     * @return MantenimientoPageResponse con los resultados paginados
     */
    fun consultarConPaginacion(
        filterRequest: MantenimientoFilterRequest?,
        pagina: Int = 0,
        tamanoPagina: Int = 20
    ): MantenimientoPageResponse {
        logger.info("Consultando mantenimientos con paginación. Filtros: $filterRequest")
        
        val pageable: Pageable = PageRequest.of(
            pagina, 
            tamanoPagina, 
            Sort.by(Sort.Direction.DESC, "fechaInicio")
        )

        val page: Page<Mantenimiento> = if (filterRequest != null) {
            mantenimientoRepository.buscarConFiltros(
                estado = filterRequest.estado,
                responsable = filterRequest.responsable,
                fechaDesde = filterRequest.fechaInicioDesde,
                fechaHasta = filterRequest.fechaInicioHasta,
                tipo = filterRequest.tipoMantenimiento,
                pageable = pageable
            )
        } else {
            mantenimientoRepository.findAll(pageable)
        }

        val mantenimientos = page.content.map { mapResponseCompleto(it) }

        return MantenimientoPageResponse(
            mantenimientos = mantenimientos,
            total = page.totalElements.toInt(),
            paginaActual = page.number,
            totalPaginas = page.totalPages
        )
    }

    /**
     * Obtiene un mantenimiento específico por su ID.
     * 
     * @param id ID del mantenimiento
     * @return MantenimientoResponse con los datos completos
     * @throws IllegalArgumentException si no se encuentra el mantenimiento
     */
    fun obtenerPorId(id: Int): MantenimientoResponse {
        logger.info("Obteniendo mantenimiento por ID: $id")
        
        val mantenimiento = mantenimientoRepository.findById(id)
            .orElseThrow { 
                logger.error("Mantenimiento no encontrado: $id")
                IllegalArgumentException("Mantenimiento no encontrado con ID: $id") 
            }
        
        return mapResponseCompleto(mantenimiento)
    }

    /**
     * Obtiene un mantenimiento específico por su ID con detalles completos.
     *
     * @param id ID del mantenimiento
     * @return MantenimientoResponse con los datos completos
     * @throws IllegalArgumentException si no se encuentra el mantenimiento
     */
    fun obtenerDetallePorId(id: Int): MantenimientoResponse {
        logger.info("Obteniendo detalle de mantenimiento por ID: $id")

        val mantenimiento = mantenimientoRepository.findById(id)
            .orElseThrow {
                logger.error("Mantenimiento no encontrado: $id")
                IllegalArgumentException("Mantenimiento no encontrado con ID: $id")
            }
        return mapResponseCompleto(mantenimiento)
    }

    /**
     * Actualiza una solicitud de mantenimiento existente.
     * 
     * Maneja la transición de estado y actualiza el inventario cuando
     * el mantenimiento se marca como "Finalizado".
     * 
     * @param id ID del mantenimiento a actualizar
     * @param request Datos de actualización
     * @return MantenimientoResponse con los datos actualizados
     * @throws IllegalArgumentException si no se encuentra el mantenimiento
     */
    @Transactional
    fun actualizarSolicitud(id: Int, request: MantenimientoUpdateRequest): MantenimientoResponse {
        logger.info("Actualizando mantenimiento ID: $id")
        
        val mantenimiento = mantenimientoRepository.findById(id)
            .orElseThrow { 
                logger.error("Mantenimiento no encontrado: $id")
                IllegalArgumentException("Mantenimiento no encontrado con ID: $id") 
            }

        // Validar el request
        request.validate()

        // Actualizar campos
        if (!request.estadoMantenimiento.isNullOrBlank()) {
            mantenimiento.estadoMantenimiento = request.estadoMantenimiento
        }
        
        if (!request.descripcionTrabajoRealizado.isNullOrBlank() || !request.evidenciaFoto.isNullOrBlank()) {
            mantenimiento.descripcionTrabajoRealizado = mergeEvidencia(
                request.descripcionTrabajoRealizado,
                request.evidenciaFoto
            )
        }
        
        if (request.fechaFinReal != null) {
            mantenimiento.fechaFinReal = request.fechaFinReal
        }
        
        if (request.costoReal != null) {
            mantenimiento.costoReal = request.costoReal
        }
        
        if (!request.responsable.isNullOrBlank()) {
            mantenimiento.responsable = request.responsable
        }

        // Si se marca como Finalizado, cambiar el estado del inventario
        val estadoAnterior = mantenimiento.estadoMantenimiento
        val nuevoEstado = request.estadoMantenimiento ?: estadoAnterior
        
        if (!nuevoEstado.isNullOrBlank() && 
            nuevoEstado.equals("Finalizado", ignoreCase = true) &&
            !estadoAnterior.equals("Finalizado", ignoreCase = true)
        ) {
            logger.info("Mantenimiento finalizado. Actualizando estado del inventario")
            mantenimiento.inventarioItem.estadoItem = EstadoInventarioItem.Disponible
            inventarioItemRepository.save(mantenimiento.inventarioItem)
        }

        val actualizado = mantenimientoRepository.save(mantenimiento)
        logger.info("Mantenimiento actualizado exitosamente: $id")
        
        return mapResponseCompleto(actualizado)
    }

    /**
     * Cancela una solicitud de mantenimiento (eliminación lógica).
     * 
     * El mantenimiento no se elimina físicamente de la base de datos,
     * solo se cambia su estado a "Cancelado" y se libera el inventario.
     * 
     * @param id ID del mantenimiento a cancelar
     * @throws IllegalArgumentException si no se encuentra el mantenimiento
     */
    @Transactional
    fun cancelarSolicitud(id: Int) {
        logger.info("Cancelando mantenimiento ID: $id")
        
        val mantenimiento = mantenimientoRepository.findById(id)
            .orElseThrow { 
                logger.error("Mantenimiento no encontrado: $id")
                IllegalArgumentException("Mantenimiento no encontrado con ID: $id") 
            }

        // Solo permitir cancelación si no está ya finalizado
        if (mantenimiento.estadoMantenimiento.equals("Finalizado", ignoreCase = true)) {
            throw IllegalArgumentException("No se puede cancelar un mantenimiento ya finalizado")
        }

        // Cambiar estado a cancelado y liberar inventario
        mantenimiento.estadoMantenimiento = "Cancelado"
        mantenimiento.inventarioItem.estadoItem = EstadoInventarioItem.Disponible
        inventarioItemRepository.save(mantenimiento.inventarioItem)
        
        mantenimientoRepository.save(mantenimiento)
        
        logger.info("Mantenimiento cancelado exitosamente: $id")
    }

    /**
     * Obtiene estadísticas de mantenimientos por estado.
     * 
     * @return Mapa con el estado como clave y la cantidad como valor
     */
    fun obtenerEstadisticas(): Map<String, Long> {
        logger.info("Obteniendo estadísticas de mantenimientos")
        
        return listOf("En revision", "En proceso", "Finalizado", "Cancelado").associateWith { estado ->
            mantenimientoRepository.countByEstadoMantenimiento(estado)
        }
    }

    /**
     * Obtiene la lista de mantenimientos pendientes.
     * 
     * @return Lista de mantenimientos que no están finalizados ni cancelados
     */
    fun obtenerPendientes(): List<MantenimientoResponse> {
        logger.info("Obteniendo mantenimientos pendientes")
        
        return mantenimientoRepository.findPendientes().map { mapResponseCompleto(it) }
    }

    /**
     * Mapea la entidad Mantenimiento a un response completo con datos del inventario.
     */
    private fun mapResponseCompleto(mantenimiento: Mantenimiento): MantenimientoResponse {
        return MantenimientoResponse(
            id = mantenimiento.id!!,
            inventarioItemId = mantenimiento.inventarioItem.id!!,
            inventarioItemNombre = mantenimiento.inventarioItem.producto.nombre,
            fechaInicio = mantenimiento.fechaInicio,
            fechaFinPrevista = mantenimiento.fechaFinPrevista,
            fechaFinReal = mantenimiento.fechaFinReal,
            tipoMantenimiento = mantenimiento.tipoMantenimiento.name,
            estadoMantenimiento = mantenimiento.estadoMantenimiento,
            responsable = mantenimiento.responsable,
            descripcionProblema = mantenimiento.descripcionProblema,
            descripcionTrabajoRealizado = mantenimiento.descripcionTrabajoRealizado,
            costoEstimado = mantenimiento.costoEstimado,
            costoReal = mantenimiento.costoReal,
            evidenciaFoto = null, // No exponer la URL completa por seguridad
            createdAt = mantenimiento.createdAt?.let { 
                LocalDateTime.ofInstant(it, java.time.ZoneId.systemDefault()) 
            },
            updatedAt = mantenimiento.updatedAt?.let { 
                LocalDateTime.ofInstant(it, java.time.ZoneId.systemDefault()) 
            }
        )
    }

    /**
     * Combina la descripción con la evidencia de foto en un solo campo.
     */
    private fun mergeEvidencia(descripcion: String?, evidencia: String?): String? {
        if (evidencia.isNullOrBlank()) return descripcion
        val prefix = "EVIDENCIA_FOTO:"
        val evidenciaLinea = "$prefix$evidencia"
        return if (descripcion.isNullOrBlank()) evidenciaLinea else "$descripcion\n$evidenciaLinea"
    }

    /**
     * Obtiene el tipo de mantenimiento por defecto.
     */
    private fun getDefaultTipo() = com.example.axxionSystem.model.TipoMantenimiento.Correctivo
}

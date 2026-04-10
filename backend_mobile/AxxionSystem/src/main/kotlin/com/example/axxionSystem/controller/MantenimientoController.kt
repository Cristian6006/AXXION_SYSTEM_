package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.MantenimientoCreateRequest
import com.example.axxionSystem.dto.MantenimientoFilterRequest
import com.example.axxionSystem.dto.MantenimientoPageResponse
import com.example.axxionSystem.dto.MantenimientoResponse
import com.example.axxionSystem.dto.MantenimientoUpdateRequest
import com.example.axxionSystem.model.TipoMantenimiento
import com.example.axxionSystem.service.MantenimientoService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controlador REST para la gestión de solicitudes de mantenimiento.
 * 
 * Endpoints disponibles:
 * - POST /api/mantenimiento/solicitudes - Crear nueva solicitud
 * - GET /api/mantenimiento/solicitudes - Consultar todas las solicitudes
 * - GET /api/mantenimiento/solicitudes/{id} - Obtener una solicitud específica
 * - PUT /api/mantenimiento/solicitudes/{id} - Actualizar una solicitud
 * - DELETE /api/mantenimiento/solicitudes/{id} - Cancelar una solicitud
 * - GET /api/mantenimiento/paginado - Consultar con paginación
 * - GET /api/mantenimiento/estadisticas - Obtener estadísticas
 * - GET /api/mantenimiento/pendientes - Obtener mantenimientos pendientes
 */
@RestController
@RequestMapping("/api/mantenimiento")
class MantenimientoController {

    private val logger = LoggerFactory.getLogger(MantenimientoController::class.java)

    @Autowired lateinit var mantenimientoService: MantenimientoService

    /**
     * Crea una nueva solicitud de mantenimiento.
     * 
     * @param request Datos de la solicitud de mantenimiento
     * @return 201 Created con el mantenimiento creado, o 400 Bad Request si hay error de validación
     */
    @PostMapping("/solicitudes")
    fun crearSolicitud(@RequestBody request: MantenimientoCreateRequest): ResponseEntity<Any> {
        return try {
            logger.info("Recibida solicitud de mantenimiento para item: ${request.inventarioItemId}")
            val response = mantenimientoService.crearSolicitud(request)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: IllegalArgumentException) {
            logger.warn("Error al crear solicitud: ${e.message}")
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        } catch (e: Exception) {
            logger.error("Error inesperado al crear solicitud", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error interno al crear la solicitud"))
        }
    }

    /**
     * Consulta todas las solicitudes de mantenimiento.
     * 
     * @param responsable Filtrar por responsable (opcional)
     * @return 200 OK con la lista de mantenimientos
     */
    @GetMapping("/solicitudes")
    fun consultarSolicitudes(
        @RequestParam(required = false) responsable: String?
    ): ResponseEntity<List<MantenimientoResponse>> {
        logger.info("Consultando solicitudes. Responsable: $responsable")
        return ResponseEntity.ok(mantenimientoService.consultarSolicitudes(responsable))
    }

    /**
     * Obtiene una solicitud de mantenimiento específica por su ID.
     * 
     * @param id ID del mantenimiento
     * @return 200 OK con el mantenimiento, o 404 Not Found si no existe
     */
    @GetMapping("/solicitudes/{id}")
    fun obtenerPorId(@PathVariable id: Int): ResponseEntity<Any> {
        return try {
            logger.info("Obteniendo mantenimiento ID: $id")
            ResponseEntity.ok(mantenimientoService.obtenerPorId(id))
        } catch (e: IllegalArgumentException) {
            logger.warn("Mantenimiento no encontrado: $id")
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    /**
     * Actualiza una solicitud de mantenimiento existente.
     * 
     * @param id ID del mantenimiento a actualizar
     * @param request Datos de actualización
     * @return 200 OK con el mantenimiento actualizado, o 400 Bad Request si hay error
     */
    @PutMapping("/solicitudes/{id}")
    fun actualizarSolicitud(
        @PathVariable id: Int,
        @RequestBody request: MantenimientoUpdateRequest
    ): ResponseEntity<Any> {
        return try {
            logger.info("Actualizando mantenimiento ID: $id")
            val response = mantenimientoService.actualizarSolicitud(id, request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            logger.warn("Error al actualizar mantenimiento: ${e.message}")
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        } catch (e: Exception) {
            logger.error("Error inesperado al actualizar mantenimiento", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error interno al actualizar la solicitud"))
        }
    }

    /**
     * Cancela una solicitud de mantenimiento (eliminación lógica).
     * 
     * @param id ID del mantenimiento a cancelar
     * @return 200 OK si se canceló correctamente, o 400 Bad Request si hay error
     */
    @DeleteMapping("/solicitudes/{id}")
    fun cancelarSolicitud(@PathVariable id: Int): ResponseEntity<Any> {
        return try {
            logger.info("Cancelando mantenimiento ID: $id")
            mantenimientoService.cancelarSolicitud(id)
            ResponseEntity.ok(mapOf("mensaje" to "Solicitud de mantenimiento cancelada correctamente"))
        } catch (e: IllegalArgumentException) {
            logger.warn("Error al cancelar mantenimiento: ${e.message}")
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        } catch (e: Exception) {
            logger.error("Error inesperado al cancelar mantenimiento", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error interno al cancelar la solicitud"))
        }
    }

    /**
     * Consulta mantenimientos con paginación y filtros avanzados.
     * 
     * @param estado Filtrar por estado (opcional)
     * @param responsable Filtrar por responsable (opcional)
     * @param fechaDesde Filtrar por fecha de inicio desde (opcional)
     * @param fechaHasta Filtrar por fecha de inicio hasta (opcional)
     * @param tipo Filtrar por tipo de mantenimiento (opcional)
     * @param pagina Número de página (default: 0)
     * @param tamano Tamaño de página (default: 20)
     * @return 200 OK con los resultados paginados
     */
    @GetMapping("/paginado")
    fun consultarPaginado(
        @RequestParam(required = false) estado: String?,
        @RequestParam(required = false) responsable: String?,
        @RequestParam(required = false) fechaDesde: String?,
        @RequestParam(required = false) fechaHasta: String?,
        @RequestParam(required = false) tipo: TipoMantenimiento?,
        @RequestParam(defaultValue = "0") pagina: Int,
        @RequestParam(defaultValue = "20") tamano: Int
    ): ResponseEntity<MantenimientoPageResponse> {
        
        val filterRequest = if (estado != null || responsable != null || fechaDesde != null || fechaHasta != null || tipo != null) {
            MantenimientoFilterRequest(
                estado = estado,
                responsable = responsable,
                fechaInicioDesde = fechaDesde?.let { 
                    java.time.LocalDate.parse(it) 
                },
                fechaInicioHasta = fechaHasta?.let { 
                    java.time.LocalDate.parse(it) 
                },
                tipoMantenimiento = tipo
            )
        } else null

        logger.info("Consultando mantenimientos paginados. Filtros: $filterRequest, Página: $pagina, Tamaño: $tamano")
        return ResponseEntity.ok(mantenimientoService.consultarConPaginacion(filterRequest, pagina, tamano))
    }

    /**
     * Obtiene estadísticas de mantenimientos por estado.
     * 
     * @return 200 OK con el mapa de estadísticas
     */
    @GetMapping("/estadisticas")
    fun obtenerEstadisticas(): ResponseEntity<Map<String, Long>> {
        logger.info("Obteniendo estadísticas de mantenimientos")
        return ResponseEntity.ok(mantenimientoService.obtenerEstadisticas())
    }

    /**
     * Obtiene la lista de mantenimientos pendientes (no finalizados ni cancelados).
     * 
     * @return 200 OK con la lista de mantenimientos pendientes
     */
    @GetMapping("/pendientes")
    fun obtenerPendientes(): ResponseEntity<List<MantenimientoResponse>> {
        logger.info("Obteniendo mantenimientos pendientes")
        return ResponseEntity.ok(mantenimientoService.obtenerPendientes())
    }
}

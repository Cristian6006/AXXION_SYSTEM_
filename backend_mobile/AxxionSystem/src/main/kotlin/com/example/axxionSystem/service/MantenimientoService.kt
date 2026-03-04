package com.example.axxionSystem.service

import com.example.axxionSystem.dto.MantenimientoCreateRequest
import com.example.axxionSystem.dto.MantenimientoResponse
import com.example.axxionSystem.dto.MantenimientoUpdateRequest
import com.example.axxionSystem.model.EstadoInventarioItem
import com.example.axxionSystem.model.Mantenimiento
import com.example.axxionSystem.repository.InventarioItemRepository
import com.example.axxionSystem.repository.MantenimientoRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MantenimientoService {

    @Autowired lateinit var mantenimientoRepository: MantenimientoRepository
    @Autowired lateinit var inventarioItemRepository: InventarioItemRepository

    @Transactional
    fun crearSolicitud(request: MantenimientoCreateRequest): MantenimientoResponse {
        val inventario = inventarioItemRepository.findById(request.inventarioItemId)
            .orElseThrow { IllegalArgumentException("Inventario no encontrado") }

        val descripcion = mergeEvidencia(request.descripcionProblema, request.evidenciaFoto)

        val mantenimiento = mantenimientoRepository.save(
            Mantenimiento(
                inventarioItem = inventario,
                fechaInicio = LocalDateTime.now(),
                fechaFinPrevista = request.fechaFinPrevista,
                tipoMantenimiento = request.tipoMantenimiento ?: inventarioItemDefaultTipo(),
                descripcionProblema = descripcion,
                costoEstimado = request.costoEstimado,
                estadoMantenimiento = "En revision",
                responsable = request.responsable
            )
        )

        inventario.estadoItem = EstadoInventarioItem.EnMantenimiento

        return mapResponse(mantenimiento)
    }

    fun consultarSolicitudes(responsable: String?): List<MantenimientoResponse> {
        val lista = if (responsable.isNullOrBlank()) {
            mantenimientoRepository.findAll()
        } else {
            mantenimientoRepository.findByResponsable(responsable)
        }
        return lista.map { mapResponse(it) }
    }

    @Transactional
    fun actualizarSolicitud(id: Int, request: MantenimientoUpdateRequest): MantenimientoResponse {
        val mantenimiento = mantenimientoRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Mantenimiento no encontrado") }

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

        if (!mantenimiento.estadoMantenimiento.isNullOrBlank() &&
            mantenimiento.estadoMantenimiento!!.equals("Finalizado", ignoreCase = true)
        ) {
            mantenimiento.inventarioItem.estadoItem = EstadoInventarioItem.Disponible
        }

        return mapResponse(mantenimiento)
    }

    private fun mapResponse(mantenimiento: Mantenimiento): MantenimientoResponse {
        return MantenimientoResponse(
            id = mantenimiento.id!!,
            inventarioItemId = mantenimiento.inventarioItem.id!!,
            fechaInicio = mantenimiento.fechaInicio,
            estadoMantenimiento = mantenimiento.estadoMantenimiento,
            responsable = mantenimiento.responsable,
            descripcionProblema = mantenimiento.descripcionProblema,
            descripcionTrabajoRealizado = mantenimiento.descripcionTrabajoRealizado
        )
    }

    private fun mergeEvidencia(descripcion: String?, evidencia: String?): String? {
        if (evidencia.isNullOrBlank()) return descripcion
        val prefix = "EVIDENCIA_FOTO:"
        val evidenciaLinea = "$prefix$evidencia"
        return if (descripcion.isNullOrBlank()) evidenciaLinea else "$descripcion\n$evidenciaLinea"
    }

    private fun inventarioItemDefaultTipo() = com.example.axxionSystem.model.TipoMantenimiento.Correctivo
}

package com.example.axxionSystem.dto

import com.example.axxionSystem.model.TipoMantenimiento
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime


data class MantenimientoCreateRequest(
    val inventarioItemId: Int,
    val descripcionProblema: String? = null,
    val evidenciaFoto: String? = null,
    val tipoMantenimiento: TipoMantenimiento? = TipoMantenimiento.Correctivo,
    val responsable: String? = null,
    val fechaFinPrevista: LocalDate? = null,
    val costoEstimado: BigDecimal? = null
)

data class MantenimientoUpdateRequest(
    val estadoMantenimiento: String? = null,
    val descripcionTrabajoRealizado: String? = null,
    val evidenciaFoto: String? = null,
    val fechaFinReal: LocalDate? = null,
    val costoReal: BigDecimal? = null,
    val responsable: String? = null
)

data class MantenimientoResponse(
    val id: Int,
    val inventarioItemId: Int,
    val fechaInicio: LocalDateTime?,
    val estadoMantenimiento: String?,
    val responsable: String?,
    val descripcionProblema: String?,
    val descripcionTrabajoRealizado: String?
)

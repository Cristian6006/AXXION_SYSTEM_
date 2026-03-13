package com.example.axxionsystem.mantenimiento.model

import java.math.BigDecimal

// ─── Enum ────────────────────────────────────────────

enum class TipoMantenimiento { Preventivo, Correctivo, Mejora }

// ─── Requests ────────────────────────────────────────

data class MantenimientoCreateRequest(
    val inventarioItemId: Int,
    val descripcionProblema: String? = null,
    val evidenciaFoto: String? = null,
    val tipoMantenimiento: TipoMantenimiento? = TipoMantenimiento.Correctivo,
    val responsable: String? = null,
    val fechaFinPrevista: String? = null,    // ISO LocalDate: "2025-03-04"
    val costoEstimado: BigDecimal? = null
)

data class MantenimientoUpdateRequest(
    val estadoMantenimiento: String? = null,
    val descripcionTrabajoRealizado: String? = null,
    val evidenciaFoto: String? = null,
    val fechaFinReal: String? = null,        // ISO LocalDate
    val costoReal: BigDecimal? = null,
    val responsable: String? = null
)

// ─── Response ────────────────────────────────────────

data class MantenimientoResponse(
    val id: Int,
    val inventarioItemId: Int,
    val fechaInicio: String?,
    val estadoMantenimiento: String?,
    val responsable: String?,
    val descripcionProblema: String?,
    val descripcionTrabajoRealizado: String?
)

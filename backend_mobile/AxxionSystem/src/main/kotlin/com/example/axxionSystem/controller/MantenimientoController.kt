package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.MantenimientoCreateRequest
import com.example.axxionSystem.dto.MantenimientoResponse
import com.example.axxionSystem.dto.MantenimientoUpdateRequest
import com.example.axxionSystem.service.MantenimientoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mantenimiento")
class MantenimientoController {

    @Autowired lateinit var mantenimientoService: MantenimientoService

    @PostMapping("/solicitudes")
    fun crearSolicitud(@RequestBody request: MantenimientoCreateRequest): ResponseEntity<Any> {
        return try {
            val response = mantenimientoService.crearSolicitud(request)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/solicitudes")
    fun consultarSolicitudes(@RequestParam(required = false) responsable: String?): ResponseEntity<List<MantenimientoResponse>> {
        return ResponseEntity.ok(mantenimientoService.consultarSolicitudes(responsable))
    }

    @PutMapping("/solicitudes/{id}")
    fun actualizarSolicitud(
        @PathVariable id: Int,
        @RequestBody request: MantenimientoUpdateRequest
    ): ResponseEntity<Any> {
        return try {
            val response = mantenimientoService.actualizarSolicitud(id, request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}

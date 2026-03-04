package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.*
import com.example.axxionSystem.model.EstadoSolicitud
import com.example.axxionSystem.service.AlquilerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/alquiler")
class AlquilerController {

    @Autowired lateinit var alquilerService: AlquilerService

    @PostMapping("/solicitudes")
    fun crearSolicitud(@RequestBody request: SolicitudCreateRequest): ResponseEntity<Any> {
        return try {
            val response = alquilerService.crearSolicitud(request)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/solicitudes")
    fun consultarSolicitudes(
        @RequestParam(required = false) estado: EstadoSolicitud?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) fecha: LocalDate?
    ): ResponseEntity<List<SolicitudResponse>> {
        return ResponseEntity.ok(alquilerService.consultarSolicitudes(estado, fecha))
    }

    @PostMapping("/rentas")
    fun crearRenta(@RequestBody request: RentaCreateRequest): ResponseEntity<Any> {
        return try {
            val response = alquilerService.crearRenta(request)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/entregas/firmar")
    fun firmarEntrega(@RequestBody request: EntregaFirmaRequest): ResponseEntity<Any> {
        return try {
            val entrega = alquilerService.firmarEntrega(request)
            ResponseEntity.ok(entrega)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/devoluciones/firmar")
    fun firmarDevolucion(@RequestBody request: DevolucionFirmaRequest): ResponseEntity<Any> {
        return try {
            val devolucion = alquilerService.firmarDevolucion(request)
            ResponseEntity.ok(devolucion)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/rentas/cliente/{clienteId}")
    fun rentasCliente(@PathVariable clienteId: Int): ResponseEntity<List<RentaResponse>> {
        return ResponseEntity.ok(alquilerService.rentasPorCliente(clienteId))
    }
}

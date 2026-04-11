package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.ResumenResponse
import com.example.axxionSystem.repository.MantenimientoRepository
import com.example.axxionSystem.repository.ProductoRepository
import com.example.axxionSystem.repository.RentaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/resumen")
class ResumenController {

    @Autowired lateinit var productoRepository: ProductoRepository
    @Autowired lateinit var rentaRepository: RentaRepository
    @Autowired lateinit var mantenimientoRepository: MantenimientoRepository

    @GetMapping
    fun getResumen(): ResponseEntity<ResumenResponse> {
        val response = ResumenResponse(
            totalProductos = productoRepository.count(),
            totalAlquileres = rentaRepository.count(),
            totalMantenimientos = mantenimientoRepository.count()
        )
        return ResponseEntity.ok(response)
    }
}


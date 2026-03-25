package com.example.axxionSystem.controller

import com.example.axxionSystem.dto.ActualizarEstadoRequest
import com.example.axxionSystem.model.Producto
import com.example.axxionSystem.service.ProductoService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/productos")
class ProductoController {

    @Autowired lateinit var productoService: ProductoService

    @GetMapping
    fun getAll(): ResponseEntity<List<Producto>> {
        return ResponseEntity.ok(productoService.getAllProductos())
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<Producto> {
        val producto = productoService.getProductoById(id)
        return if (producto != null) ResponseEntity.ok(producto) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun create(@RequestBody producto: Producto): ResponseEntity<Producto> {
        return ResponseEntity.ok(productoService.saveProducto(producto))
    }

    @PatchMapping("/{id}/estado")
    fun actualizarEstado(
        @PathVariable id: Int,
        @Valid @RequestBody request: ActualizarEstadoRequest
    ): ResponseEntity<Producto> {

        val productoActualizado = productoService.actualizarEstado(id, request)

        return ResponseEntity.ok(productoActualizado)
    }
}

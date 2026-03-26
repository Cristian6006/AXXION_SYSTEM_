package com.example.axxionSystem.service

import com.example.axxionSystem.model.Producto
import com.example.axxionSystem.dto.ActualizarEstadoRequest
import com.example.axxionSystem.repository.ProductoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ProductoService {

    @Autowired lateinit var productoRepository: ProductoRepository

    fun getAllProductos(): List<Producto> {
        return productoRepository.findAll()
    }

    fun getProductoById(id: Int): Producto? {
        return productoRepository.findById(id).orElse(null)
    }

    @Transactional
    fun saveProducto(producto: Producto): Producto {
        return productoRepository.save(producto)
    }

    @Transactional
    fun deleteProducto(id: Int) {
        productoRepository.deleteById(id)
    }

    @Transactional
    fun actualizarEstado(id: Int, request: ActualizarEstadoRequest): Producto {
        val productoActual = productoRepository.findById(id)
            .orElseThrow { IllegalArgumentException("El producto con ID $id no existe.") }

        val productoActualizado = productoActual.copy(
            estado = request.estado,
            notas = request.notas ?: productoActual.notas,
            updatedAt = Instant.now()
        )

        return productoRepository.save(productoActualizado)
    }
}

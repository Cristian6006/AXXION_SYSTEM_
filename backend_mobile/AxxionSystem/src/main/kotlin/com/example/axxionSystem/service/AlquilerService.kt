package com.example.axxionSystem.service

import com.example.axxionSystem.dto.*
import com.example.axxionSystem.model.*
import com.example.axxionSystem.repository.*
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class AlquilerService {

    @Autowired lateinit var clienteRepository: ClienteRepository
    @Autowired lateinit var productoRepository: ProductoRepository
    @Autowired lateinit var inventarioItemRepository: InventarioItemRepository
    @Autowired lateinit var solicitudRepository: SolicitudRepository
    @Autowired lateinit var solicitudProductoRepository: SolicitudProductoRepository
    @Autowired lateinit var rentaRepository: RentaRepository
    @Autowired lateinit var rentaInventarioItemRepository: RentaInventarioItemRepository
    @Autowired lateinit var entregaRepository: EntregaRepository
    @Autowired lateinit var devolucionRepository: DevolucionRepository
    @Autowired lateinit var direccionRepository: DireccionRepository

    @Transactional
    fun crearSolicitud(request: SolicitudCreateRequest): SolicitudResponse {
        val cliente = clienteRepository.findById(request.clienteId)
            .orElseThrow { IllegalArgumentException("Cliente no encontrado") }

        val productosIds = request.productos ?: emptyList()
        if (productosIds.isEmpty() && request.nombreProductoAlternativo.isNullOrBlank()) {
            throw IllegalArgumentException("Debe indicar productos o un nombre alternativo")
        }

        val solicitud = solicitudRepository.save(
            Solicitud(
                cliente = cliente,
                fechaSolicitud = Instant.now(),
                nombreProductoAlternativo = request.nombreProductoAlternativo,
                cantidadSolicitada = request.cantidadSolicitada,
                descripcionNecesidad = request.descripcionNecesidad,
                estadoSolicitud = EstadoSolicitud.Nueva
            )
        )

        if (productosIds.isNotEmpty()) {
            val productos = productoRepository.findAllById(productosIds).associateBy { it.id }
            val faltantes = productosIds.filter { productos[it] == null }
            if (faltantes.isNotEmpty()) {
                throw IllegalArgumentException("Productos no encontrados: ${faltantes.joinToString(", ")}")
            }

            val relaciones = productosIds.map { id ->
                val producto = productos[id]!!
                SolicitudProducto(
                    id = SolicitudProductoId(solicitudId = solicitud.id!!, productoId = producto.id!!),
                    solicitud = solicitud,
                    producto = producto
                )
            }
            solicitudProductoRepository.saveAll(relaciones)
        }

        return SolicitudResponse(
            id = solicitud.id!!,
            clienteId = cliente.id!!,
            fechaSolicitud = solicitud.fechaSolicitud?.toString(),
            estado = solicitud.estadoSolicitud,
            productos = productosIds,
            nombreProductoAlternativo = solicitud.nombreProductoAlternativo,
            cantidadSolicitada = solicitud.cantidadSolicitada,
            descripcionNecesidad = solicitud.descripcionNecesidad
        )
    }

    fun consultarSolicitudes(estado: EstadoSolicitud?, fecha: LocalDate?): List<SolicitudResponse> {
        val desde = fecha?.atStartOfDay()?.toInstant(java.time.ZoneOffset.UTC)
        val hasta = fecha?.plusDays(1)?.atStartOfDay()?.toInstant(java.time.ZoneOffset.UTC)

        return solicitudRepository.findByFiltro(estado, desde, hasta).map { solicitud ->
            val productos = solicitudProductoRepository.findByIdSolicitudId(solicitud.id!!)
                .mapNotNull { it.producto.id }
            SolicitudResponse(
                id = solicitud.id!!,
                clienteId = solicitud.cliente.id!!,
                fechaSolicitud = solicitud.fechaSolicitud?.toString(),
                estado = solicitud.estadoSolicitud,
                productos = productos,
                nombreProductoAlternativo = solicitud.nombreProductoAlternativo,
                cantidadSolicitada = solicitud.cantidadSolicitada,
                descripcionNecesidad = solicitud.descripcionNecesidad
            )
        }
    }

    @Transactional
    fun crearRenta(request: RentaCreateRequest): RentaResponse {
        if (request.items.isEmpty()) {
            throw IllegalArgumentException("Debe indicar al menos un item de inventario")
        }
        val cliente = clienteRepository.findById(request.clienteId)
            .orElseThrow { IllegalArgumentException("Cliente no encontrado") }

        val renta = rentaRepository.save(
            Renta(
                cliente = cliente,
                cotizacionId = request.cotizacionId,
                fechaInicio = request.fechaInicio,
                fechaFinPrevista = request.fechaFinPrevista,
                montoTotalRenta = request.montoTotalRenta,
                depositoGarantia = request.depositoGarantia,
                notas = request.notas,
                estadoRenta = EstadoRenta.Programada
            )
        )

        val items = request.items.map { itemReq ->
            val inventario = inventarioItemRepository.findById(itemReq.inventarioItemId)
                .orElseThrow { IllegalArgumentException("Inventario no encontrado: ${itemReq.inventarioItemId}") }

            if (inventario.estadoItem != EstadoInventarioItem.Disponible) {
                throw IllegalArgumentException("Inventario no disponible: ${itemReq.inventarioItemId}")
            }

            inventario.estadoItem = EstadoInventarioItem.Rentado

            RentaInventarioItem(
                id = RentaInventarioItemId(rentaId = renta.id!!, inventarioItemId = inventario.id!!),
                renta = renta,
                inventarioItem = inventario,
                precioRentaItem = itemReq.precioRentaItem,
                condicionSalida = itemReq.condicionSalida,
                notas = itemReq.notas
            )
        }

        rentaInventarioItemRepository.saveAll(items)

        return mapRentaResponse(renta)
    }

    @Transactional
    fun firmarEntrega(request: EntregaFirmaRequest): Entrega {
        val renta = rentaRepository.findById(request.rentaId)
            .orElseThrow { IllegalArgumentException("Renta no encontrada") }
        val direccion = direccionRepository.findById(request.direccionId)
            .orElseThrow { IllegalArgumentException("Direccion no encontrada") }

        val notas = mergeFirma(request.notas, request.firmaDigital)

        val entrega = entregaRepository.save(
            Entrega(
                renta = renta,
                direccion = direccion,
                fechaEnvio = request.fechaEnvio ?: LocalDateTime.now(),
                companiaEnvio = request.companiaEnvio,
                numeroGuia = request.numeroGuia,
                estadoEntrega = request.estadoEntrega ?: EstadoEntrega.Entregada,
                notas = notas
            )
        )

        renta.estadoRenta = EstadoRenta.EnCurso

        actualizarCondicionesSalida(renta.id!!, request.condicionesSalida)

        return entrega
    }

    @Transactional
    fun firmarDevolucion(request: DevolucionFirmaRequest): Devolucion {
        val renta = rentaRepository.findById(request.rentaId)
            .orElseThrow { IllegalArgumentException("Renta no encontrada") }

        val notas = mergeFirma(request.notasGenerales, request.firmaDigital)

        val devolucion = devolucionRepository.save(
            Devolucion(
                renta = renta,
                fechaDevolucionProgramada = request.fechaDevolucionProgramada,
                fechaDevolucionReal = request.fechaDevolucionReal ?: LocalDateTime.now(),
                estadoDevolucion = request.estadoDevolucion ?: EstadoDevolucion.Completa,
                personaRecibe = request.personaRecibe,
                notasGenerales = notas
            )
        )

        val estadoFinal = devolucion.estadoDevolucion
        if (estadoFinal == EstadoDevolucion.Completa) {
            renta.estadoRenta = EstadoRenta.Finalizada
            renta.fechaDevolucionReal = devolucion.fechaDevolucionReal
            liberarInventario(renta.id!!)
        } else {
            renta.estadoRenta = EstadoRenta.EnCurso
        }

        actualizarCondicionesRegreso(renta.id!!, request.condicionesRegreso)

        return devolucion
    }

    fun rentasPorCliente(clienteId: Int): List<RentaResponse> {
        return rentaRepository.findByClienteId(clienteId).map { mapRentaResponse(it) }
    }

    private fun mapRentaResponse(renta: Renta): RentaResponse {
        val items = rentaInventarioItemRepository.findByIdRentaId(renta.id!!).map {
            RentaItemResponse(
                inventarioItemId = it.inventarioItem.id!!,
                productoId = it.inventarioItem.producto.id!!,
                precioRentaItem = it.precioRentaItem,
                condicionSalida = it.condicionSalida,
                condicionRegreso = it.condicionRegreso,
                notas = it.notas
            )
        }
        return RentaResponse(
            id = renta.id!!,
            clienteId = renta.cliente.id!!,
            estado = renta.estadoRenta,
            fechaInicio = renta.fechaInicio,
            fechaFinPrevista = renta.fechaFinPrevista,
            fechaDevolucionReal = renta.fechaDevolucionReal,
            items = items
        )
    }

    private fun actualizarCondicionesSalida(rentaId: Int, condiciones: List<CondicionItemRequest>?) {
        if (condiciones.isNullOrEmpty()) return
        val items = rentaInventarioItemRepository.findByIdRentaId(rentaId).associateBy { it.inventarioItem.id }
        condiciones.forEach { cond ->
            val item = items[cond.inventarioItemId] ?: return@forEach
            if (!cond.condicion.isNullOrBlank()) item.condicionSalida = cond.condicion
            if (!cond.notas.isNullOrBlank()) item.notas = appendNota(item.notas, cond.notas)
        }
    }

    private fun actualizarCondicionesRegreso(rentaId: Int, condiciones: List<CondicionItemRequest>?) {
        if (condiciones.isNullOrEmpty()) return
        val items = rentaInventarioItemRepository.findByIdRentaId(rentaId).associateBy { it.inventarioItem.id }
        condiciones.forEach { cond ->
            val item = items[cond.inventarioItemId] ?: return@forEach
            if (!cond.condicion.isNullOrBlank()) item.condicionRegreso = cond.condicion
            if (!cond.notas.isNullOrBlank()) item.notas = appendNota(item.notas, cond.notas)
        }
    }

    private fun liberarInventario(rentaId: Int) {
        val items = rentaInventarioItemRepository.findByIdRentaId(rentaId)
        items.forEach { it.inventarioItem.estadoItem = EstadoInventarioItem.Disponible }
    }

    private fun mergeFirma(notas: String?, firma: String?): String? {
        if (firma.isNullOrBlank()) return notas
        val prefix = "FIRMA_DIGITAL:"
        val firmaLinea = "$prefix$firma"
        return if (notas.isNullOrBlank()) firmaLinea else "$notas\n$firmaLinea"
    }

    private fun appendNota(actual: String?, extra: String): String {
        return if (actual.isNullOrBlank()) extra else "$actual\n$extra"
    }
}

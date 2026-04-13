package com.example.axxionsystem.ui.alquiler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.model.Alquiler.AlquilerItem
import com.example.axxionsystem.data.model.Alquiler.RentaResponse
import com.example.axxionsystem.data.model.Alquiler.SolicitudCreateRequest
import com.example.axxionsystem.data.model.Alquiler.EntregaFirmaRequest
import com.example.axxionsystem.data.model.Alquiler.DevolucionFirmaRequest
import com.example.axxionsystem.data.model.producto.ProductoEntity
import com.example.axxionsystem.data.repository.AlquilerRepository
import kotlinx.coroutines.launch

class AlquilerViewModel(private val repository: AlquilerRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _items = MutableLiveData<List<AlquilerItem>>()
    val items: LiveData<List<AlquilerItem>> = _items

    private val _productos = MutableLiveData<List<ProductoEntity>>()
    val productos: LiveData<List<ProductoEntity>> = _productos

    private var originalItems = listOf<AlquilerItem>()
    private var listaRentasCompleta = mutableListOf<RentaResponse>()

    private val _solicitudCreateResult = MutableLiveData<Result<Int>>()
    val solicitudCreateResult: LiveData<Result<Int>> = _solicitudCreateResult

    private val _entregaFirmaResult = MutableLiveData<Result<String>>()
    val entregaFirmaResult: LiveData<Result<String>> = _entregaFirmaResult

    private val _devolucionFirmaResult = MutableLiveData<Result<String>>()
    val devolucionFirmaResult: LiveData<Result<String>> = _devolucionFirmaResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _mostrandoRentas = MutableLiveData<Boolean>(false)
    val mostrandoRentas: LiveData<Boolean> = _mostrandoRentas

    private var clienteIdActual: Int = 1

    /**
     * Aplica filtros locales a la lista cargada actualmente.
     */
    fun aplicarFiltros(query: String?, fecha: String?) {
        var filtrados = originalItems

        if (!query.isNullOrBlank()) {
            val q = query.lowercase()
            filtrados = filtrados.filter {
                it.nombreProducto?.lowercase()?.contains(q) == true ||
                it.descripcion?.lowercase()?.contains(q) == true ||
                it.id.toString().contains(q)
            }
        }

        if (!fecha.isNullOrBlank()) {
            filtrados = filtrados.filter { it.fechaReferencia == fecha }
        }

        _items.value = filtrados
    }

    fun cargarSolicitudes(fecha: String? = null) {
        _mostrandoRentas.value = false
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.getSolicitudes()
            result.onSuccess { lista ->
                originalItems = repository.mapSolicitudesToItems(lista)
                _items.value = originalItems
            }.onFailure { exception ->
                _error.value = exception.message
            }
            _isLoading.value = false
        }
    }

    fun cargarProductos() {
        viewModelScope.launch {
            repository.getProductos().onSuccess {
                _productos.value = it
            }.onFailure {
                _error.value = "Error al cargar productos: ${it.message}"
            }
        }
    }

    fun cargarRentas(clienteId: Int) {
        clienteIdActual = clienteId
        _mostrandoRentas.value = true
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.getRentasPorCliente(clienteId)
            result.onSuccess { lista ->
                listaRentasCompleta = lista.toMutableList()
                originalItems = repository.mapRentasToItems(lista)
                _items.value = originalItems
            }.onFailure { exception ->
                _error.value = exception.message
            }
            _isLoading.value = false
        }
    }

    fun toggleVista(clienteId: Int = clienteIdActual) {
        if (_mostrandoRentas.value == true) {
            cargarSolicitudes()
        } else {
            cargarRentas(clienteId)
        }
    }

    fun crearSolicitudSimple(clienteId: Int, cantidad: Int, descripcion: String?, productoAlt: String?) {
        val request = SolicitudCreateRequest(
            clienteId = clienteId,
            cantidadSolicitada = cantidad,
            descripcionNecesidad = descripcion,
            nombreProductoAlternativo = productoAlt
        )
        _isLoading.value = true
        viewModelScope.launch {
            repository.crearSolicitud(request).onSuccess {
                _solicitudCreateResult.value = Result.success(it.id)
                cargarSolicitudes()
            }.onFailure { _solicitudCreateResult.value = Result.failure(it) }
            _isLoading.value = false
        }
    }

    fun firmarEntrega(rentaId: Int, direccionId: Int, firmaDigital: String, notas: String?) {
        _isLoading.value = true
        val request = EntregaFirmaRequest(rentaId = rentaId, direccionId = direccionId, firmaDigital = firmaDigital, notas = notas)
        viewModelScope.launch {
            repository.firmarEntrega(request).onSuccess {
                _entregaFirmaResult.value = Result.success("Entrega firmada")
                cargarRentas(clienteIdActual)
            }.onFailure { _entregaFirmaResult.value = Result.failure(it) }
            _isLoading.value = false
        }
    }

    fun firmarDevolucion(rentaId: Int, firmaDigital: String, personaRecibe: String?, notas: String?) {
        _isLoading.value = true
        val request = DevolucionFirmaRequest(rentaId = rentaId, firmaDigital = firmaDigital, personaRecibe = personaRecibe, notasGenerales = notas)
        viewModelScope.launch {
            repository.firmarDevolucion(request).onSuccess {
                _devolucionFirmaResult.value = Result.success("Devolución firmada")
                cargarRentas(clienteIdActual)
            }.onFailure { _devolucionFirmaResult.value = Result.failure(it) }
            _isLoading.value = false
        }
    }

    fun getRentaPorPosicion(position: Int): RentaResponse? {
        val currentItems = _items.value ?: return null
        if (position < 0 || position >= currentItems.size) return null
        val item = currentItems[position]
        return listaRentasCompleta.find { it.id == item.id }
    }

    fun clearError() { _error.value = null }
    fun clearCreateResult() { _solicitudCreateResult.value = Result.failure(Exception("")) }
    fun clearEntregaResult() { _entregaFirmaResult.value = Result.failure(Exception("")) }
    fun clearDevolucionResult() { _devolucionFirmaResult.value = Result.failure(Exception("")) }
}

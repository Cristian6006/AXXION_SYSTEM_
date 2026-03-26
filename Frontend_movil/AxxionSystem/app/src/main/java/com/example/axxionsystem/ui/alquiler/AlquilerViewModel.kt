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
import com.example.axxionsystem.data.repository.AlquilerRepository
import kotlinx.coroutines.launch

/**
 * ViewModel de Alquiler.
 *
 * Orquesta llamadas al [AlquilerRepository] usando coroutines, y expone resultados
 * via LiveData para que la UI (Fragment) reaccione a:
 * - Estados de carga (isLoading)
 * - Listas de solicitudes y rentas
 * - Resultados de operaciones (crear, firmar)
 * - Errores para mostrar al usuario
 *
 * Mantiene el estado de la vista independientes de la rotación de pantalla.
 */
class AlquilerViewModel(private val repository: AlquilerRepository) : ViewModel() {

    // ═══════════════════════════════════════════════════════
    // ESTADO DE CARGA
    // ═══════════════════════════════════════════════════════
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // ═══════════════════════════════════════════════════════
    // SOLICITUDES
    // ═══════════════════════════════════════════════════════
    private val _solicitudes = MutableLiveData<List<AlquilerItem>>()
    val solicitudes: LiveData<List<AlquilerItem>> = _solicitudes

    private val _solicitudCreateResult = MutableLiveData<Result<Int>>()
    val solicitudCreateResult: LiveData<Result<Int>> = _solicitudCreateResult

    // ═══════════════════════════════════════════════════════
    // RENTAS
    // ═══════════════════════════════════════════════════════
    private val _rentas = MutableLiveData<List<AlquilerItem>>()
    val rentas: LiveData<List<AlquilerItem>> = _rentas

    // Almacena las rentas originales para acceder a datos completos
    private var listaRentasCompleta = mutableListOf<RentaResponse>()

    // ═══════════════════════════════════════════════════════
    // FIRMAS (ENTREGA/DEVOLUCIÓN)
    // ═══════════════════════════════════════════════════════
    private val _entregaFirmaResult = MutableLiveData<Result<String>>()
    val entregaFirmaResult: LiveData<Result<String>> = _entregaFirmaResult

    private val _devolucionFirmaResult = MutableLiveData<Result<String>>()
    val devolucionFirmaResult: LiveData<Result<String>> = _devolucionFirmaResult

    // ═══════════════════════════════════════════════════════
    // ERRORES
    // ═══════════════════════════════════════════════════════
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // ═══════════════════════════════════════════════════════
    // MODO DE VISUALIZACIÓN
    // ═══════════════════════════════════════════════════════
    // true = mostrando Rentas, false = mostrando Solicitudes
    private val _mostrandoRentas = MutableLiveData<Boolean>(false)
    val mostrandoRentas: LiveData<Boolean> = _mostrandoRentas

    // ═══════════════════════════════════════════════════════
    // CLIENTE ID ACTUAL
    // ═══════════════════════════════════════════════════════
    private var clienteIdActual: Int = 1 // Por defecto

    /**
     * Carga las solicitudes de alquiler del servidor.
     */
    fun cargarSolicitudes() {
        _mostrandoRentas.value = false
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.getSolicitudes()
            result.onSuccess { lista ->
                _solicitudes.value = repository.mapSolicitudesToItems(lista)
            }.onFailure { exception ->
                _error.value = exception.message
            }
            _isLoading.value = false
        }
    }

    /**
     * Carga las rentas de un cliente específico.
     */
    fun cargarRentas(clienteId: Int) {
        clienteIdActual = clienteId
        _mostrandoRentas.value = true
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.getRentasPorCliente(clienteId)
            result.onSuccess { lista ->
                listaRentasCompleta = lista.toMutableList()
                _rentas.value = repository.mapRentasToItems(lista)
            }.onFailure { exception ->
                _error.value = exception.message
            }
            _isLoading.value = false
        }
    }

    /**
     * Alterna entre vista de solicitudes y vista de rentas.
     */
    fun toggleVista(clienteId: Int = clienteIdActual) {
        if (_mostrandoRentas.value == true) {
            cargarSolicitudes()
        } else {
            cargarRentas(clienteId)
        }
    }

    /**
     * Crea una nueva solicitud de alquiler.
     */
    fun crearSolicitud(request: SolicitudCreateRequest) {
        if (request.clienteId <= 0) {
            _solicitudCreateResult.value = Result.failure(Exception("El ID de cliente debe ser un número válido"))
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.crearSolicitud(request)
            result.onSuccess { response ->
                _solicitudCreateResult.value = Result.success(response.id)
                cargarSolicitudes()
            }.onFailure { exception ->
                _solicitudCreateResult.value = Result.failure(exception)
            }
            _isLoading.value = false
        }
    }

    /**
     * Crea una solicitud con parámetros simples.
     */
    fun crearSolicitudSimple(
        clienteId: Int,
        cantidad: Int,
        descripcion: String?,
        productoAlt: String?
    ) {
        val request = SolicitudCreateRequest(
            clienteId = clienteId,
            cantidadSolicitada = cantidad,
            descripcionNecesidad = descripcion,
            nombreProductoAlternativo = productoAlt
        )
        crearSolicitud(request)
    }

    /**
     * Firma la entrega de una renta.
     */
    fun firmarEntrega(rentaId: Int, direccionId: Int, firmaDigital: String, notas: String?) {
        _isLoading.value = true
        _error.value = null

        val request = EntregaFirmaRequest(
            rentaId = rentaId,
            direccionId = direccionId,
            firmaDigital = firmaDigital,
            notas = notas
        )

        viewModelScope.launch {
            val result = repository.firmarEntrega(request)
            result.onSuccess {
                _entregaFirmaResult.value = Result.success("Entrega firmada correctamente")
                cargarRentas(clienteIdActual)
            }.onFailure { exception ->
                _entregaFirmaResult.value = Result.failure(exception)
            }
            _isLoading.value = false
        }
    }

    /**
     * Firma la devolución de una renta.
     */
    fun firmarDevolucion(
        rentaId: Int,
        firmaDigital: String,
        personaRecibe: String?,
        notas: String?
    ) {
        _isLoading.value = true
        _error.value = null

        val request = DevolucionFirmaRequest(
            rentaId = rentaId,
            firmaDigital = firmaDigital,
            personaRecibe = personaRecibe,
            notasGenerales = notas
        )

        viewModelScope.launch {
            val result = repository.firmarDevolucion(request)
            result.onSuccess {
                _devolucionFirmaResult.value = Result.success("Devolución firmada correctamente")
                cargarRentas(clienteIdActual)
            }.onFailure { exception ->
                _devolucionFirmaResult.value = Result.failure(exception)
            }
            _isLoading.value = false
        }
    }

    /**
     * Obtiene los datos completos de una renta por posición.
     */
    fun getRentaPorPosicion(position: Int): RentaResponse? {
        return if (position >= 0 && position < listaRentasCompleta.size) {
            listaRentasCompleta[position]
        } else {
            null
        }
    }

    /**
     * Limpia el mensaje de error después de ser mostrado.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Limpia los resultados de operaciones.
     */
    fun clearCreateResult() {
        _solicitudCreateResult.value = Result.failure(Exception(""))
    }

    fun clearEntregaResult() {
        _entregaFirmaResult.value = Result.failure(Exception(""))
    }

    fun clearDevolucionResult() {
        _devolucionFirmaResult.value = Result.failure(Exception(""))
    }
}

package com.example.axxionsystem.ui.mantenimiento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.model.mantenimiento.MantenimientoCreateRequest
import com.example.axxionsystem.data.model.mantenimiento.MantenimientoResponse
import com.example.axxionsystem.data.model.mantenimiento.MantenimientoUpdateRequest
import com.example.axxionsystem.data.repository.mantenimiento.MantenimientoRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para el módulo de Mantenimiento.
 * Gestiona el estado de la UI y la comunicación con el Repositorio.
 *
 * @param repository El repositorio de mantenimiento.
 */
class MantenimientoViewModel(private val repository: MantenimientoRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _mantenimientos = MutableLiveData<List<MantenimientoResponse>>()
    val mantenimientos: LiveData<List<MantenimientoResponse>> = _mantenimientos

    private val _operacionResult = MutableLiveData<Result<MantenimientoResponse>>()
    val operacionResult: LiveData<Result<MantenimientoResponse>> = _operacionResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Carga la lista de solicitudes de mantenimiento.
     * @param responsable (Opcional) Responsable para filtrar.
     */
    fun cargarMantenimientos(responsable: String? = null) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = repository.consultarSolicitudesMantenimiento(responsable)
            result.onSuccess { lista ->
                _mantenimientos.value = lista
            }.onFailure { exception ->
                _error.value = exception.message
            }
            _isLoading.value = false
        }
    }

    /**
     * Crea una nueva solicitud de mantenimiento.
     */
    fun crearMantenimiento(descripcion: String, fecha: String, estado: String, responsable: String?) {
        if (descripcion.isBlank() || fecha.isBlank() || estado.isBlank()) {
            _error.value = "Todos los campos son obligatorios"
            return
        }

        val request = MantenimientoCreateRequest(descripcion, fecha, estado, responsable)
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.crearSolicitudMantenimiento(request)
            _operacionResult.value = result
            result.onSuccess {
                cargarMantenimientos() // Recargar lista tras creación exitosa
            }
            _isLoading.value = false
        }
    }

    /**
     * Actualiza una solicitud de mantenimiento existente.
     */
    fun actualizarMantenimiento(id: Int, descripcion: String?, fecha: String?, estado: String?, responsable: String?) {
        val request = MantenimientoUpdateRequest(descripcion, fecha, estado, responsable)
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.actualizarSolicitudMantenimiento(id, request)
            _operacionResult.value = result
            result.onSuccess {
                cargarMantenimientos() // Recargar lista tras actualización exitosa
            }
            _isLoading.value = false
        }
    }

    fun clearError() { _error.value = null }
}

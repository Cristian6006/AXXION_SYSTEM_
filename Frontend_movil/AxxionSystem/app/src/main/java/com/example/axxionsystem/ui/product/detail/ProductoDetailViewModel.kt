package com.example.axxionsystem.ui.product.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.repository.product.ProductoRepository
import com.example.axxionsystem.ui.product.ProductoDetailUiState
import com.example.axxionsystem.ui.product.UpdateActionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductoDetailViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductoDetailUiState>(ProductoDetailUiState.Loading)
    val uiState: StateFlow<ProductoDetailUiState> = _uiState

    private val _updateState = MutableStateFlow<UpdateActionState>(UpdateActionState.Idle)
    val updateState: StateFlow<UpdateActionState> = _updateState

    fun getProductoById(id: Int) {
        viewModelScope.launch {
            _uiState.value = ProductoDetailUiState.Loading
            repository.getProductoByIdFlow(id)
                .catch { _uiState.value = ProductoDetailUiState.Error("Error al cargar datos") }
                .collect { producto ->
                    if (producto != null) {
                        _uiState.value = ProductoDetailUiState.Success(producto)
                    } else {
                        _uiState.value = ProductoDetailUiState.Error("Producto no encontrado")
                    }
                }
        }
    }

    fun updateEstado(id: Int, nuevoEstado: String, notas: String?) {
        viewModelScope.launch {
            _updateState.value = UpdateActionState.Loading

            val result = repository.updateEstadoProducto(id, nuevoEstado, notas)

            result.onSuccess {
                _updateState.value = UpdateActionState.Success
            }.onFailure { exception ->
                _updateState.value = UpdateActionState.Error(exception.message ?: "Error de conexión")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = UpdateActionState.Idle
    }
}
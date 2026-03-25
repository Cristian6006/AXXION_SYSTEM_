package com.example.axxionsystem.ui.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.repository.product.ProductoRepository
import com.example.axxionsystem.ui.product.ProductosUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductosUiState>(ProductosUiState.Loading)
    val uiState: StateFlow<ProductosUiState> = _uiState

    private var searchJob: Job? = null

    init {
        loadProductos("")
        sincronizarConNube()
    }

    fun loadProductos(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _uiState.value = ProductosUiState.Loading

            val flow = if (query.isBlank()) {
                repository.getAllProductosFlow()
            } else {
                repository.searchProductosFlow(query)
            }

            flow.catch { exception ->
                _uiState.value = ProductosUiState.Error(exception.message ?: "Error desconocido")
            }.collect { lista ->
                _uiState.value = ProductosUiState.Success(lista)
            }
        }
    }

    private fun sincronizarConNube() {
        viewModelScope.launch {
            repository.fetchProductosFromApi()
        }
    }
}
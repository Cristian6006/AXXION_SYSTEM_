package com.example.axxionsystem.ui.product

import com.example.axxionsystem.data.model.producto.ProductoEntity

sealed class ProductosUiState {
    object Loading : ProductosUiState()
    data class Success(val productos: List<ProductoEntity>) : ProductosUiState()
    data class Error(val message: String) : ProductosUiState()
}

sealed class ProductoDetailUiState {
    object Loading : ProductoDetailUiState()
    data class Success(val producto: ProductoEntity) : ProductoDetailUiState()
    data class Error(val message: String) : ProductoDetailUiState()
}

sealed class UpdateActionState {
    object Idle : UpdateActionState()
    object Loading : UpdateActionState()
    object Success : UpdateActionState()
    data class Error(val message: String) : UpdateActionState()
}
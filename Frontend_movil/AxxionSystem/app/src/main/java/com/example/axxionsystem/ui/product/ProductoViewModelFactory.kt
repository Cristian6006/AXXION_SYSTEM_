package com.example.axxionsystem.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.axxionsystem.data.repository.product.ProductoRepository
import com.example.axxionsystem.ui.product.detail.ProductoDetailViewModel
import com.example.axxionsystem.ui.product.list.ProductosViewModel

class ProductoViewModelFactory(
    private val repository: ProductoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            return ProductosViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ProductoDetailViewModel::class.java)) {
            return ProductoDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
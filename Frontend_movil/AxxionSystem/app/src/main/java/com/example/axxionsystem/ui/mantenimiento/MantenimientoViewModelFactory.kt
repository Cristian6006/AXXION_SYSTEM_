package com.example.axxionsystem.ui.mantenimiento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.axxionsystem.data.repository.mantenimiento.MantenimientoRepository

/**
 * Factory para crear instancias de MantenimientoViewModel.
 * Permite la inyección de dependencias manual del repositorio.
 *
 * @param repository El repositorio de mantenimiento a inyectar.
 */
class MantenimientoViewModelFactory(private val repository: MantenimientoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MantenimientoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MantenimientoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.example.axxionsystem.ui.alquiler

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.axxionsystem.data.api.RetrofitClient
import com.example.axxionsystem.data.repository.AlquilerRepository

/**
 * Factory para crear [AlquilerViewModel] con dependencias.
 *
 * Se usa con [ViewModelProvider] para inyectar [AlquilerRepository] sin un DI framework.
 * Proporciona la instancia de ApiService necesaria para el repositorio.
 */
class AlquilerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    // Instancia del repositorio (se crea una vez y se reutiliza)
    private val repository: AlquilerRepository by lazy {
        val apiService = RetrofitClient.getApiService(context)
        AlquilerRepository(apiService)
    }

    /**
     * Crea una instancia del ViewModel solicitado.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlquilerViewModel::class.java)) {
            return AlquilerViewModel(repository) as T
        }
        throw IllegalArgumentException(
            "Clase ViewModel desconocida: ${modelClass.name}. " +
            "Solo se soporta AlquilerViewModel."
        )
    }
}

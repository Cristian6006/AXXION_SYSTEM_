package com.example.axxionsystem.ui.auth

/**
 * Factory para crear [AuthViewModel] con dependencias (repository).
 *
 * Se usa con [ViewModelProvider] para inyectar [AuthRepository] sin un DI
 * framework.
 */
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.axxionsystem.data.repository.auth.AuthRepository

class AuthViewModelFactory(private val repository: AuthRepository): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}

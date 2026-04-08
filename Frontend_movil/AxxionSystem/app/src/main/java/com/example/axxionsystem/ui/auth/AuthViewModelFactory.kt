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
import com.example.axxionsystem.data.security.CryptographyManager
import com.example.axxionsystem.util.SessionManager

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
    private val cryptographyManager: CryptographyManager
): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, sessionManager, cryptographyManager) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}

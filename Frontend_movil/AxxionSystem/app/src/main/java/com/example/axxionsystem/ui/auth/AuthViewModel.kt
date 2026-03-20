package com.example.axxionsystem.ui.auth

/**
 * ViewModel de autenticacion y perfil.
 *
 * Orquesta llamadas al [AuthRepository] usando coroutines, y expone resultados
 * via LiveData para que la UI (fragments) reaccione a:
 * - estado de carga
 * - resultado de login
 * - datos del perfil del usuario
 */
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.model.auth.LoginRequest
import com.example.axxionsystem.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle: LoginUiState()
    object Loading: LoginUiState()
    data class Success(val token: String): LoginUiState()
    data class Error(val message: String, val isNetworkError: Boolean): LoginUiState()
    data class ValidationError(val emailError: String?, val passwordError: String?): LoginUiState()
}

class AuthViewModel(private val repository: AuthRepository): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {

        val emailError = if (email.isBlank()) "El correo no puede estar vacio"
                            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Formato de correo invalido"
                                else null

        val passwordError = if (password.isBlank()) "La contraseña no puede estar vacia" else null

        if (emailError != null || passwordError != null) {
            _uiState.value = LoginUiState.ValidationError(emailError, passwordError)
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = repository.login(request)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = LoginUiState.Success(response.body()!!.accessToken)
                } else {
                    val code = response.code()
                    val msg = if (code == 401) "Credenciales incorrectas" else "Error del servidor ($code)"
                    _uiState.value = LoginUiState.Error(msg,  false)
                }
            } catch(e: Exception) {
                _uiState.value = LoginUiState.Error("Fallo de conexion. Verifica tu internet", true)
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    fun logoutBackend() {
        viewModelScope.launch {
            try {
                repository.logout()
            } catch (e: Exception) {
            }
        }
    }
}

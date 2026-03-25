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
import org.json.JSONObject

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

        val passwordError = when {
            password.isBlank() -> "La contraseña no puede estar vacia"
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            !password.any { it.isLowerCase() } -> "La contraseña debe tener al menos una minuscula"
            !password.any { it.isUpperCase() } -> "La contraseña debe tener al menos una mayuscula"
            else -> null
        }

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
                    val errorMsg = try {
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            JSONObject(errorBody).optString("error", "Error desconocido")
                        } else {
                            "Error del servidor (${response.code()})"
                        }
                    } catch (e: Exception) {
                        "Error del servidor (${response.code()})"
                    }
                    _uiState.value = LoginUiState.Error(errorMsg, false)
                }
            } catch(e: Exception) {
                _uiState.value = LoginUiState.Error("Fallo de conexion. Verifica tu internet", true)
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

}

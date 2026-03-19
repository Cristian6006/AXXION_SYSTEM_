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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.model.AuthResponse  
import com.example.axxionsystem.data.model.LoginRequest
import com.example.axxionsystem.data.model.UserProfileResponse
import com.example.axxionsystem.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(private val repository: AuthRepository): ViewModel() {

    private val _loginResult = MutableLiveData<Result<AuthResponse>>()
    val loginResult: LiveData<Result<AuthResponse>> = _loginResult

    private val _perfilResult = MutableLiveData<Result<UserProfileResponse>>()
    val perfilResult: LiveData<Result<UserProfileResponse>> = _perfilResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginResult.value = Result.failure(Exception("Campos vacios"))
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = LoginRequest(email, password)
                val response: Response<AuthResponse> = repository.login(request)

                if (response.isSuccessful && response.body() != null) {
                    _loginResult.value = Result.success(response.body()!!)
                } else {
                    _loginResult.value = Result.failure(Exception("Credenciales invalidas"))
                }
            } catch(e: Exception) {
                _loginResult.value = Result.failure(Exception("Error de conexion: ${e.message}"))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getPerfil()
                if (response.isSuccessful && response.body() != null) {
                    _perfilResult.value = Result.success(response.body()!!)
                }
            } catch(e: Exception) {
                _perfilResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
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

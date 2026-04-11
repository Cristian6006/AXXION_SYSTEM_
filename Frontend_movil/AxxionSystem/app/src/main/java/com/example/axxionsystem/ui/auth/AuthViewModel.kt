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
import com.example.axxionsystem.data.security.CryptographyManager
import com.example.axxionsystem.util.SessionManager
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

    data class BiometricDeviceChecked(val isRegistered: Boolean) : LoginUiState()
    data class BiometricPromptReady(val cryptoObject: androidx.biometric.BiometricPrompt.CryptoObject) : LoginUiState()
    object BiometricEnrolledSuccess : LoginUiState()
}

class AuthViewModel(private val repository: AuthRepository,
                    private val sessionManager: SessionManager,
                    private val cryptographyManager: CryptographyManager
): ViewModel() {

    private val KEY_ALIAS = "axxion_biometric_key_01"

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

    fun checkBiometricStatus(deviceId: String) {
        viewModelScope.launch {
            val result = repository.verifyBiometricStatus(deviceId)
            result.onSuccess { response ->
                _uiState.value = LoginUiState.BiometricDeviceChecked(response.registered)
            }.onFailure {
                _uiState.value = LoginUiState.BiometricDeviceChecked(false)
            }
        }
    }

    fun initiateBiometricLogin() {
        try {
            val signature = cryptographyManager.getInitializedSignature(KEY_ALIAS)

            if (signature != null) {
                val cryptoObject = androidx.biometric.BiometricPrompt.CryptoObject(signature)
                _uiState.value = LoginUiState.BiometricPromptReady(cryptoObject)
            } else {
                _uiState.value = LoginUiState.Error("Llave biométrica corrupta. Debes iniciar sesión con contraseña.", false)
            }
        } catch (e: Exception) {
            _uiState.value = LoginUiState.Error("Error al iniciar biometría: ${e.message}", true)
        }
    }

    fun executeBiometricLogin(deviceId: String, unlockedSignature: java.security.Signature) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading // Tu Loader

            val timestamp = System.currentTimeMillis()
            val payload = "$deviceId|$timestamp"

            try {
                val base64Signature = cryptographyManager.signData(unlockedSignature, payload)
                val result = repository.loginBiometric(deviceId, timestamp, base64Signature)

                result.onSuccess { response ->
                    if (response.isSuccessful && response.body() != null) {
                        sessionManager.setBiometricEnabled(true)
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
                }.onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "Error en login biométrico", false)
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Firma inválida. Intenta nuevamente.", false)
            }
        }
    }

    fun enrollBiometrics(deviceId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = LoginUiState.Loading

                cryptographyManager.generateBiometricKeyPair(KEY_ALIAS)

                val publicKeyBase64 = cryptographyManager.getPublicKeyBase64(KEY_ALIAS)
                    ?: throw Exception("No se pudo extraer llave pública")

                val result = repository.registerBiometric(deviceId, publicKeyBase64)

                result.onSuccess {
                    _uiState.value = LoginUiState.BiometricEnrolledSuccess
                }.onFailure {
                    _uiState.value = LoginUiState.Error("No pudimos asociar tu huella.", false)
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Error: ${e.message}", false)
            }
        }
    }
}

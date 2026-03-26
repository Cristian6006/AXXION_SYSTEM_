package com.example.axxionsystem.ui.auth.recovery

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecoverPasswordViewModel(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<RecoverPasswordUiState>(RecoverPasswordUiState.Idle)
    val uiState: StateFlow<RecoverPasswordUiState> = _uiState.asStateFlow()

    private val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z]).{8,}$")

    fun resetErrorState() {
        if (_uiState.value is RecoverPasswordUiState.Error) {
            _uiState.value = RecoverPasswordUiState.Idle
        }
    }

    fun requestRecoveryToken(email: String) {
        val emailTrimmed = email.trim()

        if (emailTrimmed.isEmpty()) {
            _uiState.value = RecoverPasswordUiState.Error("El correo no puede estar vacio")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches()) {
            _uiState.value = RecoverPasswordUiState.Error("Formato de correo invalido")
            return
        }

        _uiState.value = RecoverPasswordUiState.Loading
        viewModelScope.launch {
            val result = authRepository.requestPasswordRecoveryToken(emailTrimmed)

            result.fold(
                onSuccess = {
                    _uiState.value = RecoverPasswordUiState.TokenSentSuccess
                },
                onFailure = { exception ->
                    _uiState.value = RecoverPasswordUiState.Error(
                        exception.message ?: "Ocurrió un error inesperado al enviar el código."
                    )
                }
            )
        }
    }

    fun executePasswordReset(token: String, newPassword: String) {

        val cleanToken = token.trim()

        if (cleanToken.length != 6) {
            _uiState.value = RecoverPasswordUiState.Error("El codigo debe tener 6 digitos")
            return
        }

        if (!passwordRegex.matches(newPassword)) {
            _uiState.value = RecoverPasswordUiState.Error("La contraseña debe tener minimo 8 caracteres, una mayuscula y una minuscula.")
            return
        }

        _uiState.value = RecoverPasswordUiState.Loading

        viewModelScope.launch {
            val result = authRepository.executePasswordReset(cleanToken, newPassword)

            result.fold(
                onSuccess = {
                    _uiState.value = RecoverPasswordUiState.ResetSuccess
                },
                onFailure = { exception ->
                    _uiState.value = RecoverPasswordUiState.Error(
                        exception.message ?: "Ocurrió un error al actualizar la contraseña."
                    )
                }
            )
        }
    }
}
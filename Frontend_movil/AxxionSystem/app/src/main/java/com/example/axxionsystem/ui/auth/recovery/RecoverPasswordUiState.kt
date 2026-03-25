package com.example.axxionsystem.ui.auth.recovery

sealed class RecoverPasswordUiState {
    object Idle : RecoverPasswordUiState()

    object Loading : RecoverPasswordUiState()

    object TokenSentSuccess : RecoverPasswordUiState()

    object ResetSuccess : RecoverPasswordUiState()

    data class Error(val message: String) : RecoverPasswordUiState()
}
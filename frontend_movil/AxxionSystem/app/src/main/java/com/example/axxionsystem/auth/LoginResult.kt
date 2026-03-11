package com.example.axxionsystem.auth

sealed class LoginResult {
    data object Success : LoginResult()
    data class Error(val type: ErrorType, val message: String? = null) : LoginResult()

    enum class ErrorType {
        INVALID_CREDENTIALS,
        INVALID_RESPONSE,
        NETWORK
    }
}

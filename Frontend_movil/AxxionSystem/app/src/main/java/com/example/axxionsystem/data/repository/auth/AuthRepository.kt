package com.example.axxionsystem.data.repository.auth

import com.example.axxionsystem.data.api.ApiService
import com.example.axxionsystem.data.model.auth.AuthResponse
import com.example.axxionsystem.data.model.auth.BiometricLoginRequest
import com.example.axxionsystem.data.model.auth.BiometricRegisterRequest
import com.example.axxionsystem.data.model.auth.BiometricVerifyRequest
import com.example.axxionsystem.data.model.auth.BiometricVerifyResponse
import com.example.axxionsystem.data.model.auth.ForgotPasswordRequest
import com.example.axxionsystem.data.model.auth.LoginRequest
import com.example.axxionsystem.data.model.auth.ResetPasswordRequest
import com.example.axxionsystem.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

/**
 * Repositorio de autenticacion.
 *
 * Centraliza el acceso a [ApiService] para que el ViewModel consuma un API
 * simple y testable (login, perfil y logout).
 */

class AuthRepository(private val apiService: ApiService) {

    private lateinit var sessionManager: SessionManager

    suspend fun login(request: LoginRequest): Response<AuthResponse> {
        return apiService.login(request)
    }

    suspend fun getPerfil() = apiService.getPerfil()

    suspend fun logout() = apiService.logout()

    suspend fun requestPasswordRecoveryToken(email: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.requestPasswordRecovery(ForgotPasswordRequest(email))
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error al solicitar recuperacion: ${response.code()}"))
                }
            } catch(e: Exception) {
                Result.failure(Exception("Error de conexion. Verifica tu internet"))
            }
        }
    }

    suspend fun executePasswordReset(token: String, newPassword: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.resetPassword(ResetPasswordRequest(token, newPassword))
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Error al intentar cambiar la contraseña"))
                }
            } catch(e: IOException) {
                Result.failure(Exception("Sin conexion a internet"))
            } catch(e: Exception) {
                Result.failure(Exception("Ocurrio un error"))
            }
        }
    }

    suspend fun verifyBiometricStatus(deviceId: String): Result<BiometricVerifyResponse> {
        return try {
            val response = apiService.verifyDevice(BiometricVerifyRequest(deviceId))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerBiometric(deviceId: String, publicKey: String): Result<Unit> {
        return try {
            apiService.registerBiometric(BiometricRegisterRequest(deviceId, publicKey))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginBiometric(deviceId: String, timestamp: Long, signature: String): Result<Response<AuthResponse>> {
        return try {
            val response = apiService.loginBiometric(BiometricLoginRequest(deviceId, timestamp, signature))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
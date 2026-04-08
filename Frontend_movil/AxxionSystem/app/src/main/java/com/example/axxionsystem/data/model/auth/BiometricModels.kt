package com.example.axxionsystem.data.model.auth

data class BiometricVerifyRequest(val deviceId: String)
data class BiometricVerifyResponse(val registered: Boolean, val deviceId: String)

data class BiometricRegisterRequest(
    val deviceId: String,
    val publicKey: String
)

data class BiometricLoginRequest(
    val deviceId: String,
    val timestamp: Long,
    val signature: String
)
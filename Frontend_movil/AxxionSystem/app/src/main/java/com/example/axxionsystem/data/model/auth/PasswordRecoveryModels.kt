package com.example.axxionsystem.data.model.auth

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    @SerializedName("email") val email: String
)

data class ResetPasswordRequest(
    @SerializedName("token") val token: String,
    @SerializedName("nuevaPassword") val nuevaPassword: String
)


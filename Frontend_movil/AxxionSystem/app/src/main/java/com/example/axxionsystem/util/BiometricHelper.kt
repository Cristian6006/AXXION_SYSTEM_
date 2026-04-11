package com.example.axxionsystem.util

import android.content.Context
import androidx.biometric.BiometricManager

fun canUseBiometrics(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
}
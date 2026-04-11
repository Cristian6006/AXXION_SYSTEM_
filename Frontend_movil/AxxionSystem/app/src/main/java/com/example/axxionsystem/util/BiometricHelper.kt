package com.example.axxionsystem.util

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager

fun canUseBiometrics(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    val canAuthenticate = biometricManager.canAuthenticate(
        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
    )

    Log.d("BIOMETRIA", "Resultado canAuthenticate: $canAuthenticate")

    return canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS
}
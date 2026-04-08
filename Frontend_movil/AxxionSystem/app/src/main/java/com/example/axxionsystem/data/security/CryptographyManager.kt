package com.example.axxionsystem.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.RSAKeyGenParameterSpec

interface CryptographyManager {
    /**
     * Genera el par de llaves (RSA 2048) dentro del Android Keystore.
     * Configura la llave privada para que EXIJA la huella digital cada vez que se use.
     */
    fun generateBiometricKeyPair(keyAlias: String)

    /**
     * Extrae la Llave Pública generada para poder enviarla al backend
     * en el endpoint POST /api/biometria/registrar.
     * @return La llave pública en formato Base64.
     */
    fun getPublicKeyBase64(keyAlias: String): String?

    /**
     * Obtiene el motor de firmado (SHA256withRSA) y lo inicializa con nuestra llave privada.
     */
    fun getInitializedSignature(keyAlias: String): Signature?

    /**
     * Toma el objeto Signature (que el BiometricPrompt acaba de desbloquear al leer la huella)
     * y firma el payload ("deviceId|timestamp").
     * @return La firma criptográfica en formato Base64.
     */
    fun signData(signature: Signature, payload: String): String
}

class CryptographyManagerImpl : CryptographyManager {

    private val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA
    private val KEYSTORE_NAME = "AndroidKeyStore"

    override fun generateBiometricKeyPair(keyAlias: String) {
        val keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM, KEYSTORE_NAME)

        val spec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setAlgorithmParameterSpec(RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4))
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            .setUserAuthenticationRequired(true)
            .build()

        keyPairGenerator.initialize(spec)
        keyPairGenerator.generateKeyPair()
    }

    override fun getPublicKeyBase64(keyAlias: String): String? {
        val keyStore = KeyStore.getInstance(KEYSTORE_NAME).apply { load(null) }
        val publicKey = keyStore.getCertificate(keyAlias)?.publicKey ?: return null
        return Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
    }

    override fun getInitializedSignature(keyAlias: String): Signature? {
        val keyStore = KeyStore.getInstance(KEYSTORE_NAME).apply { load(null) }
        val privateKey = keyStore.getKey(keyAlias, null) as PrivateKey
        return Signature.getInstance("SHA256withRSA").apply {
            initSign(privateKey)
        }
    }

    override fun signData(signature: Signature, payload: String): String {
        signature.update(payload.toByteArray())
        return Base64.encodeToString(signature.sign(), Base64.NO_WRAP)
    }
}
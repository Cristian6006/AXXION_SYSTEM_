package com.example.axxionSystem.util

import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Component
class CryptoUtil {

    fun verifySignature(publicKeyBase64: String, payload: String, signatureBase64: String):Boolean {
        return try {
            val cleanKey = publicKeyBase64
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\\s+".toRegex(), "")

            val keyBytes = Base64.getDecoder().decode(cleanKey)
            val keySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey: PublicKey = keyFactory.generatePublic(keySpec)

            val signature = Signature.getInstance("SHA256withRSA")
            signature.initVerify(publicKey)

            signature.update(payload.toByteArray(Charsets.UTF_8))

            val signatureBytes = Base64.getDecoder().decode(signatureBase64)
            signature.verify(signatureBytes)
        } catch (e: Exception) {
            println("Error criptográfico: ${e.message}")
            false
        }
    }
}

fun main() {
    println("--- SIMULANDO CELULAR CREANDO HUELLA ---")

    // 1. El celular genera las llaves físicas (RSA 2048)
    val keyGen = KeyPairGenerator.getInstance("RSA")
    keyGen.initialize(2048)
    val pair = keyGen.generateKeyPair()

    val publicKeyBase64 = Base64.getEncoder().encodeToString(pair.public.encoded)
    val privateKey = pair.private

    // 2. Preparamos los datos del dispositivo
    val deviceId = "postman-device-001"
    val timestamp = System.currentTimeMillis()
    val payload = "$deviceId|$timestamp"

    // 3. El celular lee la huella y FIRMA el mensaje
    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(payload.toByteArray(Charsets.UTF_8))
    val signatureBase64 = Base64.getEncoder().encodeToString(signature.sign())

    println("\n PASO 1: JSON PARA REGISTRAR LA LLAVE (POST /api/biometria/registrar)")
    println("Necesitas tu JWT en el Header para esto.")
    println("""
        {
            "deviceId": "$deviceId",
            "publicKey": "$publicKeyBase64"
        }
    """.trimIndent())

    println("\n PASO 2: JSON PARA LOGIN BIOMÉTRICO (POST /api/auth/login-biometrico)")
    println("TIENES 60 SEGUNDOS PARA USAR ESTE JSON ANTES DE QUE EXPIRE EL TIMESTAMP ⚠️")
    println("""
        {
            "deviceId": "$deviceId",
            "timestamp": $timestamp,
            "signature": "$signatureBase64"
        }
    """.trimIndent())
}
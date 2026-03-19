<?php

/**
 * Script de prueba para verificar la autenticación JWT
 * Ejecutar desde la línea de comandos: php scripts/test_auth.php
 * 
 * ACTUALIZADO: Usa los nuevos endpoints /api/auth/*
 */

// Configuración
$baseUrl = 'http://localhost:8000/api';
$testEmail = 'admin@ejemplo.com'; // Cambiar por un email válido
$testPassword = 'admin123'; // Cambiar por una contraseña válida

echo "=== PRUEBA DE AUTENTICACIÓN JWT ===\n\n";

// 1. Prueba de Login
echo "1. Probando Login...\n";
$loginData = json_encode([
    'email' => $testEmail,
    'password' => $testPassword
]);

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $baseUrl . '/auth/login');
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, $loginData);
curl_setopt($ch, CURLOPT_HTTPHEADER, [
    'Content-Type: application/json',
    'Accept: application/json'
]);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HEADER, true);

$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
$headerSize = curl_getinfo($ch, CURLINFO_HEADER_SIZE);
$body = substr($response, $headerSize);
curl_close($ch);

echo "Código de respuesta: $httpCode\n";
echo "Respuesta: " . $body . "\n\n";

if ($httpCode === 200) {
    $loginResult = json_decode($body, true);
    $token = $loginResult['data']['access_token'] ?? null;

    if ($token) {
        echo "✅ Login exitoso! Token obtenido.\n\n";

        // 2. Prueba de API protegida (obtener usuario actual)
        echo "2. Probando API protegida (obtener usuario actual)...\n";

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $baseUrl . '/auth/me');
        curl_setopt($ch, CURLOPT_HTTPGET, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Authorization: Bearer ' . $token,
            'Accept: application/json'
        ]);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        echo "Código de respuesta: $httpCode\n";
        echo "Respuesta: " . $response . "\n\n";

        if ($httpCode === 200) {
            echo "✅ API protegida accesible con token!\n\n";
        } else {
            echo "❌ Error al acceder a API protegida\n\n";
        }

        // 3. Prueba de listar usuarios
        echo "3. Probando listar usuarios...\n";

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $baseUrl . '/usuarios');
        curl_setopt($ch, CURLOPT_HTTPGET, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Authorization: Bearer ' . $token,
            'Accept: application/json'
        ]);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        echo "Código de respuesta: $httpCode\n";
        echo "Respuesta (truncada): " . substr($response, 0, 500) . "...\n\n";

        if ($httpCode === 200) {
            echo "✅ Lista de usuarios obtenida!\n\n";
        } else {
            echo "❌ Error al listar usuarios\n\n";
        }

        // 4. Prueba de Logout
        echo "4. Probando Logout...\n";

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $baseUrl . '/auth/logout');
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Authorization: Bearer ' . $token,
            'Accept: application/json'
        ]);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        echo "Código de respuesta: $httpCode\n";
        echo "Respuesta: " . $response . "\n\n";

        if ($httpCode === 200) {
            echo "✅ Logout exitoso!\n\n";
        } else {
            echo "❌ Error en logout\n\n";
        }

    } else {
        echo "❌ Token no encontrado en la respuesta\n\n";
    }

} else {
    echo "❌ Login falló\n\n";
}

echo "=== FIN DE PRUEBAS ===\n";
echo "\nPara ejecutar este script:\n";
echo "1. Asegúrate de que el servidor Laravel esté corriendo (php artisan serve)\n";
echo "2. Modifica las credenciales de prueba en este script\n";
echo "3. Ejecuta: php scripts/test_auth.php\n";

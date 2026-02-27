<?php

namespace App\Http\Middleware;

use App\Models\Usuario;
use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class JwtMiddleware
{
    /**
     * Handle an incoming request.
     */
    public function handle(Request $request, Closure $next): Response
    {
        $token = $this->getTokenFromRequest($request);

        if (!$token) {
            return response()->json([
                'success' => false,
                'message' => 'Token no proporcionado',
            ], 401);
        }

        try {
            $payload = $this->validateToken($token);

            // Verificar expiración
            if (isset($payload['exp']) && $payload['exp'] < time()) {
                return response()->json([
                    'success' => false,
                    'message' => 'Token expirado',
                ], 401);
            }

            // Cargar usuario
            $user = Usuario::find($payload['sub']);

            if (!$user) {
                return response()->json([
                    'success' => false,
                    'message' => 'Usuario no encontrado',
                ], 401);
            }

            // Agregar usuario al request
            $request->merge(['auth_user' => $user]);
            auth()->setUser($user);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'Token inválido: ' . $e->getMessage(),
            ], 401);
        }

        return $next($request);
    }

    /**
     * Obtiene el token del header Authorization
     */
    private function getTokenFromRequest(Request $request): ?string
    {
        $header = $request->header('Authorization', '');

        if (preg_match('/Bearer\s+(.*)$/i', $header, $matches)) {
            return $matches[1];
        }

        return null;
    }

    /**
     * Valida y decodifica el JWT
     */
    private function validateToken(string $token): array
    {
        $parts = explode('.', $token);

        if (count($parts) !== 3) {
            throw new \Exception('Formato de token inválido');
        }

        [$base64UrlHeader, $base64UrlPayload, $base64UrlSignature] = $parts;

        // Verificar firma
        $signature = $this->base64UrlDecode($base64UrlSignature);
        $expectedSignature = hash_hmac(
            'sha256',
            $base64UrlHeader . "." . $base64UrlPayload,
            config('app.key'),
            true
        );

        if (!hash_equals($signature, $expectedSignature)) {
            throw new \Exception('Firma inválida');
        }

        // Decodificar payload
        $payload = json_decode($this->base64UrlDecode($base64UrlPayload), true);

        if (!$payload) {
            throw new \Exception('Payload inválido');
        }

        return $payload;
    }

    private function base64UrlDecode($data): string
    {
        return base64_decode(strtr($data, '-_', '+/'));
    }
}
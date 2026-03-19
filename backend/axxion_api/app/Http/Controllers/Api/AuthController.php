<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Services\AuthService;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    public function __construct(
        private AuthService $authService
    ) {}

    /**
     * Login del usuario
     */
    public function login(Request $request): JsonResponse
    {
        $request->validate([
            'email' => 'required|email',
            'password' => 'required|string',
            'device_name' => 'nullable|string',
        ]);

        try {
            $metadata = [
                'device_name' => $request->device_name ?? 'unknown',
                'ip_address' => $request->ip(),
                'user_agent' => $request->userAgent(),
            ];

            $result = $this->authService->login(
                $request->email,
                $request->password,
                $metadata
            );

            return response()->json([
                'success' => true,
                'data' => [
                    'access_token' => $result['access_token'],
                    'token_type' => $result['token_type'],
                    'expires_in' => $result['expires_in'],
                    'user' => $result['user'],
                ],
            ])->cookie($result['cookie']);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => $e->getMessage(),
            ], 401);
        }
    }

    /**
     * Refresca el Access Token
     */
    public function refresh(Request $request): JsonResponse
    {
        $refreshToken = $request->cookie('refresh_token');

        if (!$refreshToken) {
            return response()->json([
                'success' => false,
                'message' => 'Refresh token no encontrado',
            ], 401);
        }

        try {
        $result = $this->authService->refresh($refreshToken);
        
        // ¡Adjunta la nueva cookie a la respuesta!
        return response()->json([
            'access_token' => $result['access_token'],
            'user'         => $result['user'],
        ])->withCookie($result['cookie']);
        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => $e->getMessage(),
            ], 401);
        }
    }

    /**
     * Cierra sesión del usuario
     */
    public function logout(Request $request): JsonResponse
    {
        $refreshToken = $request->cookie('refresh_token');

        if ($refreshToken) {
            $this->authService->logout($refreshToken);
        }

        return response()->json([
            'success' => true,
            'message' => 'Sesión cerrada correctamente',
        ])->cookie(cookie()->forget('refresh_token'));
    }

    /**
     * Obtiene el usuario autenticado
     */
    public function me(Request $request): JsonResponse
    {
        return response()->json([
            'success' => true,
            'data' => $request->user(),
        ]);
    }
}

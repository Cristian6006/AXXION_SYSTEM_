<?php

namespace App\Services;

use App\Models\Usuario;
use App\Models\RefreshToken;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Cookie;
use Firebase\JWT\JWT;

class AuthService
{
    private const ACCESS_TOKEN_EXPIRATION = 15;
    private const REFRESH_TOKEN_EXPIRATION = 30;

    public function generateAccessToken(Usuario $user): array
    {
        $expirationTime = time() + (config('auth.access_token_expiration', 15) * 60);

        $payload = [
            'sub' => $user->id,
            'email' => $user->email,
            'nombre_usuario' => $user->nombre_usuario,
            'iat' => time(),
            'exp' => $expirationTime,
        ];

        $token = JWT::encode($payload, config('app.key'), 'HS256');

        return [
            'token' => $token,
            'expires_in' => $expirationTime - time(),
        ];
    }

    public function generateRefreshToken(): string
    {
        return Str::random(64);
    }

    public function login(string $email, string $password, array $metadata = []): array
    {
        $user = Usuario::where('email', $email)->first();

        if (!$user || !Hash::check($password, $user->password_hash)) {
            throw new \Exception('Credenciales invalidas');
        }
        $user->load('roles');
        $accessTokenData = $this->generateAccessToken($user);
        $refreshToken = $this->generateRefreshToken();

        RefreshToken::createForUser($user, $refreshToken, $metadata);

        $cookie = $this->createRefreshTokenCookie($refreshToken);

        return [
            'access_token' => $accessTokenData['token'],
            'token_type' => 'Bearer',
            'expires_in' => $accessTokenData['expires_in'],
            'user' => $user,
            'cookie' => $cookie,
        ];
    }

    public function refresh(string $refreshToken): array
    {
        $tokenHash = hash('sha256', $refreshToken);

        $storedToken = RefreshToken::where('token_hash', $tokenHash)->with('user')->first();

        if (!$storedToken || $storedToken->isExpired()) {
            throw new \Exception('Refresh token inválido o expirado');
        }
        $user = $storedToken->user;
        $user->load('roles');
        // Elimina el token antiguo para que no pueda ser reutilizado.
        $storedToken->delete();

        // Genera un NUEVO Access Token.
        $newAccessTokenData = $this->generateAccessToken($user);

        // Genera un NUEVO Refresh Token.
        $newRefreshToken = $this->generateRefreshToken();

        // Guarda el hash del NUEVO refresh token en la BBDD.
        RefreshToken::createForUser($user, $newRefreshToken, [
            'ip_address' => request()->ip(),
            'user_agent' => request()->userAgent()
        ]);

        // Crea la NUEVA cookie para el NUEVO refresh token.
        $newCookie = $this->createRefreshTokenCookie($newRefreshToken);

        // Devuelve todo junto.
        return [
            'access_token' => $newAccessTokenData['token'],
            'token_type' => 'Bearer',
            'expires_in' => $newAccessTokenData['expires_in'],
            'cookie' => $newCookie,
            'user' => $user,
        ];
    }

    public function logout(string $refreshToken): void
    {
        $tokenHash = hash('sha256', $refreshToken);
        RefreshToken::where('token_hash', $tokenHash)->delete();
    }

    private function createRefreshTokenCookie(string $token): \Symfony\Component\HttpFoundation\Cookie
    {
        return Cookie::make(
            'refresh_token',
            $token,
            self::REFRESH_TOKEN_EXPIRATION * 24 * 60, // minutos
            '/',
            null,
            true, // secure (solo HTTPS)
            true, // httpOnly
            false,
            'strict' // sameSite
        );
    }
}
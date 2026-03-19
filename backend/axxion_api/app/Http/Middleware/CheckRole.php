<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;
use Illuminate\Support\Facades\Auth; 

class CheckRole
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @param  string  ...$roles
     * @return \Symfony\Component\HttpFoundation\Response
     */
    public function handle(Request $request, Closure $next, ...$roles): Response
    {
        $user = Auth::user(); // Obtenemos el usuario ya autenticado

        // comprobar si hay un usuario autenticado
        if (!$user) {
            return response()->json(['success' => false, 'message' => 'No autenticado'], 401);
        }
        // lógica para comprobar los roles.
        foreach ($roles as $role) {
            if ($user->roles->contains('codigo', $role)) { // Compara por 'codigo' o 'nombre'
                return $next($request); // Si tiene el rol, permite el paso
            }
        }
        return response()->json([
            'success' => false, 
            'message' => 'Acceso denegado. No tienes los permisos necesarios.'
        ], 403);
    }
}

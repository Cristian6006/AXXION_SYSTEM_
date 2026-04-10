<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreUsuarioRequest;
use App\Http\Requests\UpdateUsuarioRequest;
use App\Http\Resources\UsuarioResource;
use App\Models\Usuario;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Tymon\JWTAuth\Facades\JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;


class UsuarioController extends Controller
{


    /**
     * Lista todos los usuarios del sistema con sus roles.
     */
    public function index()
    {
        $usuarios = Usuario::with('roles')->get();
        return UsuarioResource::collection($usuarios);
    }

    /**
     * Registro
     *
     * Crea un nuevo usuario en el sistema.
     *
     * @unauthenticated
     *
     * @bodyParam nombre            string required Nombre completo. Example: Juan Pérez
     * @bodyParam email             string required Email único. Example: juan@correo.com
     * @bodyParam password          string required Mínimo 8 caracteres. Example: secret123
     *
     * @response 201 {
     *   'message' => 'Usuario creado correctamente',
            'data' => $usuario
     * }
     */
    public function store(Request $request)
    {
        $validator = Validator :: make($request->all(), [
            'nombre_usuario' => 'required',
            'nombre' => 'required',
            // second name and second surname can be optional
            'nombre2' => 'nullable',
            'apellido1' => 'required',
            'apellido2' => 'nullable',
            'password' => 'required|min:8',
            'email' => 'required|email|unique:usuario,email',
            'telefono' => 'required',
            'departamento' => 'required',
            'estado' => 'required',
            'roles' => 'required|array'
            ]);

            if ($validator->fails()) {
            $data = [
            'message' => 'Error en la validacion de los datos',
            'errors' => $validator->errors(),
            'status' => 400
            ];

            return response()->json($data, 400);
        }

    $usuario = Usuario::create([
        'nombre_usuario' => $request->nombre_usuario,
        'nombre' => $request->nombre,
        'nombre2' => $request->nombre2,
        'apellido1' => $request->apellido1,
        'apellido2' => $request->apellido2,
        // store hashed password in password_hash column
        'password_hash' => Hash::make($request->password),
        'email' => $request->email,
        'telefono' => $request->telefono,
        'departamento' => $request->departamento,
        'estado' => $request->estado,
    ]);

        if ($request->has('roles')) {
            $usuario->roles()->sync($request->roles);
        }


        return response()->json([
            'message' => 'Usuario creado correctamente',
            'data' => $usuario
        ], 201);
    }

    /**
     * Muestra los detalles de un usuario específico.
     */
    public function show(string $id)
    {
        $usuario = Usuario::with('roles')->findOrFail($id);
        return new UsuarioResource($usuario);
    }


    /**
     * Autentica a un usuario y genera un token de acceso.
     *
     * ANALOGÍA: Esta función actúa como el guardia de seguridad en la entrada de un edificio exclusivo.
     * Verifica tu identificación (credenciales) y si es válida, te da un pase temporal (token)
     * para que puedas moverte por las instalaciones.
     */
    public function login(Request $request) {
        $validator = Validator::make($request->all(), [
            'email' => 'required|string|email',
            'password' => 'required|string'
        ]);

        if ($validator->fails()) {
            return response()->json(['error' => $validator->errors()], 422);
        }

        try {
            // Intentar autenticar con las credenciales
            if (!$token = auth('api')->attempt([
                'email' => $request->email,
                'password' => $request->password
            ])) {
                // Verificar si el usuario existe
                $user = Usuario::where('email', $request->email)->first();
                if (!$user) {
                    return response()->json(['error' => 'Usuario no encontrado'], 401);
                }

                return response()->json(['error' => 'Contraseña incorrecta'], 401);
            }

            // Si llegamos aquí, la autenticación fue exitosa
            $user = Usuario::with('roles')->findOrFail(auth('api')->id());

            return response()->json([
                'token' => $token,
                'user' => [
                    'id' => $user->id,
                    'nombre' => $user->nombre,
                    'nombre_usuario' => $user->nombre_usuario,
                    'email' => $user->email,
                    'roles' => $user->roles->map(function($role) {
                        return [
                            'id' => $role->id,
                            'codigo' => $role->codigo,
                            'nombre' => $role->nombre,
                            'descripcion' => $role->descripcion ?? ''
                        ];
                    })->toArray()
                ]
            ], 200);

        } catch (JWTException $e) {
            return response()->json([
                'error' => 'No se pudo crear el token',
                'message' => $e->getMessage()
            ], 500);
        }

    }

    /**
     * Update the specified resource in storage.
     */

    /**
     * Elimina un usuario del sistema.
     */
    public function destroy(string $id)
    {
        $usuario = Usuario::findOrFail($id);
        $usuario->roles()->detach();
        $usuario->delete();
        return response()->json(status: 204);
    }

    /**
     * Actualiza la información de un usuario.
     * Permite cambiar contraseña y roles.
     */
    public function update(Request $request, string $id)
    {
        $usuario = Usuario::findOrFail($id);
        $validator = Validator::make($request->all(), [
            'nombre_usuario' => 'required',
            'nombre' => 'required',
            // second name and second surname can be optional
            'nombre2' => 'nullable',
            'apellido1' => 'required',
            'apellido2' => 'nullable',
            'password' => 'nullable|min:6',
            // allow the same email for the current user
            'email' => 'required|email|unique:usuario,email,' . $id,
            'telefono' => 'required',
            'departamento' => 'required',
            'estado' => 'required',
            'roles' => 'sometimes|array'
        ]);

        if ($validator->fails()) {
            $data = [
                'message' => 'Error en la validacion de los datos',
                'errors' => $validator->errors(),
                'status' => 400
            ];

            return response()->json($data, 400);
        }

        // Update fields
        $usuario->nombre_usuario = $request->nombre_usuario;
        $usuario->nombre = $request->nombre;
        $usuario->nombre2 = $request->nombre2;
        $usuario->apellido1 = $request->apellido1;
        $usuario->apellido2 = $request->apellido2;

        // If password is provided, hash and update the password_hash column
        if ($request->filled('password')) {
            $usuario->password_hash = Hash::make($request->password);
        }

        $usuario->email = $request->email;
        $usuario->telefono = $request->telefono;
        $usuario->departamento = $request->departamento;
        $usuario->estado = $request->estado;

        $usuario->save();

        // Sync roles if provided
        if ($request->has('roles')) {
            $usuario->roles()->sync($request->roles);
        }

        return response()->json([
            'message' => 'Usuario actualizado correctamente',
            'data' => new UsuarioResource($usuario)
        ], 200);

    }

    /**
     * Logout
     *
     * Revoca el token actual del usuario autenticado.
     *
     * @response 200 {
     *   "message": "Logged out successfully"
     * }
     */
    public function logout(){
        JWTAuth::invalidate(JWTAuth::getToken());
        return response()->json(['message' => 'Logged out successfully'], 200);

    }
}

<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Cliente;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;

class clienteController extends Controller
    {
    /**
     * Lista todos los clientes con sus relaciones (direcciones, solicitudes, etc.).
     */
        public function index()
        {
            try{ 
                $cliente = Cliente::with('direcciones', 'solicitudes', 'cotizaciones', 'rentas')->get();
                $data = [
                    'cliente' => $cliente,
                    'status' => 200
                ];
                return response()->json($data, 200);
            } catch (Exception $e) {
                Log::error('Error al intentar listar clientes: '. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(), 
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Registra un nuevo cliente en el sistema.
     * 
     * ANALOGÍA: Esta función actúa como el recepcionista que registra a un nuevo huésped en el hotel. 
     * Pide sus datos personales, verifica que no esté ya registrado (validación) y le crea un perfil.
     */
        public function store(Request $request)
        {
            try {
                $validator = Validator::make($request->all(),[
                    'nombre' => ['required', 'string', 'max:200'],
                    'nombre2' => ['required', 'string', 'max:100'],
                    'apellido1' => ['required', 'string', 'max:100'],
                    'apellido2' => ['required', 'string', 'max:100'],
                    'rfc' => ['nullable', 'string', 'max:13', 'unique:cliente'],
                    'telefono_principal' => ['nullable', 'string', 'max:20'],
                    'correo_electronico' => ['nullable', 'string', 'max:100', 'unique:cliente'],
                    'tipo_cliente' => ['nullable', 'string', Rule::in(['Particular','Empresa'])],
                    'estado_cliente' => ['nullable', 'string', Rule::in(['Activo','Inactivo','Prospecto'])]
                ]);
                if($validator->fails()){
                    $data = [
                        'message' => 'Validation failed',
                        'errors' => $validator->errors(),
                        'status' => 422
                    ];
                    return response()->json($data, 422);
                }
                $cliente = Cliente::create([
                    'nombre' => $request->nombre,
                    'nombre2' => $request->nombre2,
                    'apellido1' => $request->apellido1,
                    'apellido2' => $request->apellido2,
                    'rfc' => $request->rfc,
                    'telefono_principal' => $request->telefono_principal,
                    'correo_electronico' => $request->correo_electronico,
                    'tipo_cliente' => $request->tipo_cliente,
                    'estado_cliente' => $request->estado_cliente
                ]);
                if(!$cliente){
                    $data = [
                        'message' => 'Error al crear cliente',
                        'status' => 500
                    ];
                    return response()->json($data, 500);
                }
                $data = [
                    'cliente' => $cliente,
                    'status' => 201
                ];
                return response()->json($data, 201);
            } catch (Exception $e) {
                Log::error('Error al intentar listar clientes: '. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(), 
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Muestra la información detallada de un cliente.
     */
        public function show($id)
        {
            try {
                $cliente = Cliente::find($id);
                if(!$cliente){
                    $data = [
                        'message' => 'Cliente no encontrado',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $data = [
                    'cliente' => $cliente,
                    'status' => 200
                ];
                return response()->json($data, 200);
            } catch (Exception $e) {
                Log::error('Error al intentar listar clientes: '. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(), 
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Elimina un cliente del sistema.
     */
        public function destroy($id){
            $cliente = Cliente::find($id);
            if(!$cliente){
                $data = [
                    'message' => 'Cliente no encontrado',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $cliente->delete();
            $data = [
                'status' => 204
            ];
            return response()->json(null, 204);
        }
    /**
     * Actualiza la información de un cliente existente.
     */
        public function update(Request $request, $id)
        {
            try {
                $cliente = Cliente::find($id);
                if(!$cliente){
                    $data = [
                        'message' => 'Cliente no encontrado',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $validator = Validator::make($request->all(), [
                    'nombre' => ['required', 'string', 'max:200'],
                    'nombre2' => ['required', 'string', 'max:100'],
                    'apellido1' => ['required', 'string', 'max:100'],
                    'apellido2' => ['required', 'string', 'max:100'],
                    'rfc' => ['nulleable', 'string', 'max:13', 'unique:cliente'],
                    'telefono_principal' => ['nulleable', 'string', 'max:20', 'unique:cliente'],
                    'correo_electronico' => ['nulleable', 'string', 'max:100'],
                    'tipo_cliente' => ['nulleable', Rule::in('Particular','Empresa')],
                    'estado_cliente' => ['nulleable', Rule::in('Activo','Inactivo','Prospecto')]
                ]);
                if($validator->fails()){
                    $data = [
                        'message' => 'Validator failed',
                        'errors' => $validator->errors(),
                        'status' => 422
                    ];
                    return response()->json($data, 422);
                }
                $cliente->nombre = $request->nombre;
                $cliente->nombre2 = $request->nombre2;
                $cliente->apellido1 = $request->apellido1;
                $cliente->apellido2 = $request->apellido2;
                $cliente->rfc = $request->rfc;
                $cliente->telefono_principal = $request->telefono_principal;
                $cliente->correo_electronico = $request->correo_electronico;
                $cliente->tipo_cliente = $request->tipo_cliente;
                $cliente->estado_cliente = $request->estado_cliente;
                $cliente->save();
                $data = [
                    'message' => 'cliente actualizado',
                    'cliente' => $cliente,
                    'status' => 200
                ];
                return response()->json($data, 200);
            } catch (Exception $e) {
                Log::error('Error al actualizar el cliente'. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Actualización parcial de un cliente.
     */
        public function updatePartial(Request $request, $id){
            $cliente = Cliente::find($id);
            if(!$cliente){
                $data = [
                    'message' => 'cliente no encontrado',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(),[
                'nombre' => 'string',
                'nombre2' => 'string',
                'apellido1' => 'string',
                'apellido2' => 'string',
                'rfc' => 'string',
                'telefono_principal' => 'string',
                'correo_electronico' => 'string',
                'tipo_cliente' => 'string',
                'estado_cliente' => 'string'
            ]);
            
            if($validator->fails()){
                $data = [
                    'message' => 'Error en la Validacion',
                    'errors' => $validator->errors(),
                    'status' => 400
                ];
                return response()->json($data, 400);
            }
            
            if($request->has('nombre')){
                $cliente->nombre = $request->nombre;
            }
            if($request->has('nombre2')){
                $cliente->nombre2 = $request->nombre2;
            }
            if($request->has('apellido1')){
                $cliente->apellido1 = $request->apellido1;
            }
            if($request->has('apellido2')){
                $cliente->apellido2 = $request->apellido2;
            }
            if($request->has('rfc')){
                $cliente->rfc = $request->rfc;
            }
            if($request->has('telefono_principal')){
                $cliente->telefono_principal = $request->telefono_principal;
            }
            if($request->has('correo_electronico')){
                $cliente->correo_electronico = $request->correo_electronico;
            }
            if($request->has('tipo_cliente')){
                $cliente->tipo_cliente = $request->tipo_cliente;
            }
            if($request->has('estado_cliente')){
                $cliente->estado_cliente = $request->estado_cliente;
            }
            $cliente->save();
            $data = [
                'message' => 'cliente actualizado',
                'cliente' => $cliente,
                'status' => 200
            ];
            return response()->json($data, 200);
        }
    }

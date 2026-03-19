<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Rol;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class rolController extends Controller
    {
    /**
     * Lista todos los roles disponibles en el sistema.
     */
        public function index() 
        {
            $rol = Rol::all();
            
            $data = [
                'rol' => $rol,
                'status' => 200
            ];
            
            return response()->json($data, 200);
        }

    /**
     * Crea un nuevo rol de usuario.
     * 
     * ANALOGÍA: Esta función es como crear un nuevo puesto de trabajo en una empresa. 
     * Se define el título (nombre), el código interno y las responsabilidades (descripción) 
     * que tendrá quien ocupe ese puesto.
     */
        public function store(Request $request){
            $validator = Validator::make($request->all(), [
            'codigo' => 'required',
            'nombre' => 'required',
            'descripcion' => 'required'
            ]);
            
            if ($validator->fails()) {
                $data = [
                'message' => 'Validation failed',
                'errors' => $validator->errors(),
                'status' => 400
                ];
            return response()->json($data, 400);
            }
            
            $rol = Rol::create([
                'codigo' => $request->codigo,
                'nombre' => $request->nombre,
                'descripcion' => $request->descripcion
            ]);
            
            if (!$rol){
                $data = [
                    'message' => 'Error al crear el rol',
                    'status' => 500
                ];
                return response()->json($data, 500);
            }
            $data = [
                'rol' => $rol,
                'status' => 200
            ];
            return response()->json($data,200);
        }

    /**
     * Muestra los detalles de un rol específico.
     */
        public function show($id)
        {
            $rol = Rol::find($id);
            
            if (!$rol){
                $data = [
                    'message' => 'Rol no encontrado',
                    'status' => 404                
                ];
                return response()->json($data,404);
            }
            $data = [
                'rol' => $rol,
                'status' => 200
            ];
            return response()->json($data,200);
        }

    /**
     * Elimina un rol del sistema.
     */
        public function destroy($id)
        {
            $rol = Rol::find($id);
            
            if(!$rol) {
                $data = [
                    'message' => 'Rol no encontrado',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $rol->delete();
                $data = [
                    'rol' => $rol,
                    'status' => 200
                ];
                return response()->json($data, 200);
        }

    /**
     * Actualiza la información de un rol.
     */
        public function update(Request $request, $id)
        {
            $rol = Rol::find($id);

            if(!$rol){
                $data = [
                    'message' => 'Rol no encontrado',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }

            $validator = Validator::make($request->all(), [
                'codigo' => 'required',
                'nombre' => 'required',
                'descripcion' => 'required'
            ]);

            if($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 400
                ];
                return response()->json($data, 400);
            }

            $rol->codigo = $request->codigo;
            $rol->nombre = $request->nombre;
            $rol->descripcion = $request->descripcion;

            $rol->save();

            $data = [
                'message' => 'Rol actualizado',
                'rol' => $rol,
                'status' => 200
            ];
            return response()->json($data, 200);
            
        }

    /**
     * Actualización parcial de un rol.
     */
        public function updatePartial(Request $request, $id)
        {
            $rol = Rol::find($id);

            if(!$rol){
                $data = [
                    'message' => 'Rol no encontrado',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }

            $validator = Validator::make($request->all(),[
                'codigo' => 'string',
                'nombre' => 'string',
                'descripcion' => 'string'
            ]);
            
            if($validator->fails()){
                $data = [
                    'message' => 'Error en la Validacion',
                    'errors' => $validator->errors(),
                    'status' => 400
                ];
                return response()->json($data, 400);
            }
            
            if($request->has('codigo')){
                $rol->codigo = $request->codigo;
            }
            if($request->has('nombre')){
                $rol->nombre = $request->nombre;
            }
            if($request->has('descripcion')){
                $rol->descripcion = $request->descripcion;
            }
            $rol->save();

            $data = [
                'message' => 'Rol actualizado',
                'rol' => $rol,
                'status' => 200
            ];
            return response()->json($data, 200);
        }
    }


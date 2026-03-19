<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Subcategoria;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;

class subcategoriaController extends Controller
    {
    /**
     * Lista todas las subcategorías.
     */
        public function index()
        {
            try {
            $subcategoria = Subcategoria::with('categorias')->get();
            $data = [
                'categoria' => $subcategoria,
                'status' => 200
            ];
            return response()->json($data, 200);
            } catch (Exception $e) {
                Log::error('Error al listar las subcategorías'. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Crea una nueva subcategoría.
     * 
     * ANALOGÍA: Esta función es como añadir un estante específico dentro de una sección de la biblioteca. 
     * Si la sección es 'Ciencia', aquí creamos el estante de 'Física Cuántica' y lo asociamos a esa sección.
     */
        public function store(Request $request)
        {
            try {
            $validator = Validator::make($request->all(),[
                'nombre' => ['required', 'string', 'unique:subcategoria', 'max:100'],
                'descripcion' => ['nullable', 'string'],
                'categorias' => ['array']
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            $subcategoria = Subcategoria::create([
                'nombre' => $request->nombre,
                'descripcion' => $request->descripcion
            ]);
            if(!$subcategoria){
                $data = [
                    'message' => 'Error al crear subcategoria',
                    'status' => 500
                ];
                return response()->json($data, 500);
            }
            if($request->has('categorias')){
                $subcategoria->categorias()->sync($request->categorias);
            }
            $data = [
                'subcategoria' => $subcategoria,
                'status' => 201
            ];
            return response()->json($data, 201);
            } catch (Exception $e) {
                Log::error('Error al crear Entrega: ' . $e->getMessage());
                return response()->json([
                    'message' => 'Error interno al crear la entrega',
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Muestra una subcategoría específica.
     */
        public function show($id)
        {
            try {
                $subcategoria = Subcategoria::with('categorias')->find($id);
                if(!$subcategoria){
                    $data = [
                        'message' => 'subcategoria no encontrada',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $data = [
                    'subcategoria' => $subcategoria,
                    'status' => 200
                ];
                return response()->json($data, 200);
            } catch (Exception $e) {
                Log::error('Error al listar la subcategoria'. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Elimina una subcategoría.
     */
        public function destroy($id)
        {
                try {
                $subcategoria = Subcategoria::with('categorias')->find($id);
                if(!$subcategoria){
                    $data = [
                        'message' => 'subCategoria no encontrada',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $subcategoria->categorias()->detach();
                $subcategoria->delete();
                $data = [
                    'subcategoria' => $subcategoria,
                    'status' => 204
                ];
                return response()->json(null, 204);
            } catch (Exception $e) {
                Log::error('Error al eliminar subcategoria'. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Actualiza una subcategoría existente.
     */
        public function update(Request $request, $id)
        {
            try {
            $subcategoria = Subcategoria::with('categorias')->find($id);
            if(!$subcategoria){
                $data = [
                    'message' => 'subcategoria no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(), [
                'nombre' => ['required', 'string'],
                'descripcion' => ['nullable', 'string'],
                'categorias' => ['array']
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Validator failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            $subcategoria->nombre = $request->nombre;
            $subcategoria->descripcion = $request->descripcion;
            if($request->has('categorias')){
                $subcategoria->categorias()->sync($request->categorias);
            }
            $subcategoria->save();
            $data = [
                'message' => 'subcategoria actualizada',
                'subcategoria' => $subcategoria,
                'status' => 200
            ];
            return response()->json($data, 200);
            } catch (Exception $e) {
                Log::error('Error al crear Entrega: ' . $e->getMessage());
                return response()->json([
                    'message' => 'Error interno al crear la entrega',
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Actualización parcial de una subcategoría.
     */
        public function updatePartial(Request $request, $id){
            try {
            $subcategoria = Subcategoria::with('categorias')->find($id);
            if(!$subcategoria){
                $data = [
                    'message' => 'subcategoria no encontrado',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(),[
                'nombre' => ['string'],
                'descripcion' => ['string'],
                'categorias' => ['array']
            ]);
            
            if($validator->fails()){
                $data = [
                    'message' => 'Error en la Validacion',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            
            if($request->has('nombre')){
                $subcategoria->nombre = $request->nombre;
            }
            if($request->has('descripcion')){
                $subcategoria->descripcion = $request->descripcion;
            }
            if($request->has('categorias')){
                $subcategoria->categorias()->sync($request->categorias);
            }
            $subcategoria->save();
            $data = [
                'message' => 'subcategoria actualizado',
                'subcategoria' => $subcategoria,
                'status' => 200
            ];
            return response()->json($data, 200);
            } catch (Exception $e) {
                Log::error('Error al crear subcategoria: ' . $e->getMessage());
                return response()->json([
                    'message' => 'Error interno al crear la subcategoria',
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    }
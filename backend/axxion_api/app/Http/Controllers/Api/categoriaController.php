<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Categoria;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;

class categoriaController extends Controller
    {
    /**
     * Lista todas las categorías con sus subcategorías.
     */
        public function index()
        {
            try {
                $categoria = Categoria::with('subcategorias')->get();
                $data = [
                    'categoria' => $categoria,
                    'status' => 200
                ];
                return response()->json($data, 200);
                } catch (Exception $e) {
                    Log::error('Error al obtener las categorias'. $e->getMessage());
                    return response()->json([
                        'errors' => $e->getMessage(),
                        'status' => 500
                    ], 500);
                }
        }
    /**
     * Crea una nueva categoría.
     * 
     * ANALOGÍA: Esta función es como crear una nueva sección en una biblioteca. 
     * Le pones un nombre (ej. 'Ciencia Ficción') y decides qué estantes (subcategorías) irán dentro.
     */
        public function store(Request $request)
        {
            try {
            $validator = Validator::make($request->all(),[
                'nombre' => ['required', 'string', 'unique:categoria', 'max:100'],
                'descripcion' => ['required', 'string'],
                'tipo_categoria' => ['required', 'string', 'max:50'],
                'subcategorias' => ['array'],
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            $categoria = Categoria::create([
                'nombre' => $request->nombre,
                'descripcion' => $request->descripcion,
                'tipo_categoria' => $request->tipo_categoria
            ]);
            if(!$categoria){
                $data = [
                    'message' => 'Error al crear categoria',
                    'status' => 500
                ];
                return response()->json($data, 500);
            }
            if($request->has('subcategorias')){
                $categoria->subcategorias()->sync($request->subcategorias);
            }
            $data = [
                'categoria' => $categoria,
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
     * Muestra una categoría específica.
     */
        public function show($id)
        {
            try {
                $categoria = Categoria::with('subcategorias')->find($id);
                if(!$categoria){
                    $data = [
                        'message' => 'Categoria no encontrada',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $data = [
                    'categoria' => $categoria,
                    'status' => 200
                ];
                return response()->json($data, 200);
            } catch (Exception $e) {
                Log::error('Error al obtener la categoria'. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Elimina una categoría.
     */
        public function destroy($id)
        {
            try {
                $categoria = Categoria::with('subcategorias')->find($id);
                if(!$categoria){
                    $data = [
                        'message' => 'Categoria no encontrada',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $categoria->subcategorias()->detach();
                $categoria->delete();
                $data = [
                    'status' => 204
                ];
                return response()->json(null, 204);
            } catch (Exception $e) {
                Log::error('Error al eliminar la categoria'. $e->getMessage());
                return response()->json([
                    'errors' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Actualiza una categoría existente.
     */
        public function update(Request $request, $id)
        {
            try {
            $categoria = Categoria::with('subcategorias')->find($id);
            if(!$categoria){
                $data = [
                    'message' => 'categoria no encontrada',
                    'status' => 200
                ];
                return response()->json($data, 200);
            }
            $validator = Validator::make($request->all(), [
                'nombre' => ['required', 'string'],
                'descripcion' => ['required', 'string'],
                'tipo_categoria' => ['required', 'string'],
                'subcategorias' => ['array'],
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Validator failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            $categoria->nombre = $request->nombre;
            $categoria->descripcion = $request->descripcion;
            $categoria->tipo_categoria = $request->tipo_categoria;
            if($request->has('subcategorias')){
                $categoria->subcategorias()->sync($request->subcategorias);
            }
            $categoria->save();
            $data = [
                'message' => 'categoria actualizada',
                'categoria' => $categoria,
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
     * Actualización parcial de una categoría.
     */
        public function updatePartial(Request $request, $id)
        {
            try {
            $categoria = Categoria::with('subcategorias')->find($id);
            if(!$categoria){
                $data = [
                    'message' => 'categoria no encontrado',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(),[
                'nombre' => ['string'],
                'descripcion' => ['string'],
                'tipo_categoria' => ['string'],
                'subcategorias' => ['array'],
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
                $categoria->nombre = $request->nombre;
            }
            if($request->has('descripcion')){
                $categoria->descripcion = $request->descripcion;
            }
            if($request->has('tipo_categoria')){
                $categoria->tipo_categoria = $request->tipo_categoria;
            }
            if($request->has('subcategorias')){
                $categoria->subcategorias()->sync($request->subcategorias);
            }
            $categoria->save();
            $data = [
                'message' => 'categoria actualizado',
                'categoria' => $categoria,
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
    }

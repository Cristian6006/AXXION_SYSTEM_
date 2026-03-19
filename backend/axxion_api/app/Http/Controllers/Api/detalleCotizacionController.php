<?php

namespace App\Http\Controllers\api;

use App\Http\Controllers\Controller;
use App\Models\Cotizacion;
use App\Models\DetalleCotizacion;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Log;

class detalleCotizacionController extends Controller
{
    /**
     * Lista todos los detalles de cotizaciones.
     */
    public function index()
    {
        try{
            $detalleCotizacion = DetalleCotizacion::with('cotizacion', 'producto')->get();
            $data = [
                'detalleCotizacion' => $detalleCotizacion,
                'status' => 200
            ];
            return response()->json($data, 200);
        } catch (\Exception $e) {
            Log::error('Error al listar Detalle cotizacion: ' . $e->getMessage());
            return response()->json([
                'error' => $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }
    /**
     * Agrega un item (detalle) a una cotización.
     * 
     * ANALOGÍA: Esta función es como agregar una línea específica al presupuesto: 
     * '5 Sillas Modelo X a $10 cada una'.
     */
    public function store(Request $request){
        $validator = Validator::make($request->all(),[
            'cotizacion_id' => ['required', Rule::exists('cotizacion','id')],
            'producto_id' => ['required', Rule::exists('producto','id')],
            'descripcion_item' => ['required', 'string'],
            'cantidad' => ['required', 'numeric'],
            'precio_unitario' => ['required', 'numeric'],
            'descuento_porcentaje' => ['required', 'numeric'],
            'impuestos_aplicables' => ['required', 'numeric'],
            'notas' => ['required', 'string'],
        ]);
        if($validator->fails()){
            $data = [
                'message' => 'Validation failed',
                'errors' => $validator->errors(),
                'status' => 422
            ];
            return response()->json($data, 422);
        }
        $detalleCotizacion = DetalleCotizacion::create([
            'cotizacion_id' => $request->cotizacion_id,
            'producto_id' => $request->producto_id,
            'descripcion_item' => $request->descripcion_item,
            'cantidad' => $request->cantidad,
            'precio_unitario' => $request->precio_unitario,
            'descuento_porcentaje' => $request->descuento_porcentaje,
            'impuestos_aplicables' => $request->impuestos_aplicables,
            'notas' => $request->notas,
        ]);
        if(!$detalleCotizacion){
            $data = [
                'message' => 'Error al crear detalle de cotizacion',
                'status' => 500
            ];
            return response()->json($data, 500);
        }
        $detalleCotizacion->load(['cotizacion', 'producto']);
        $data = [
            'detalleCotizacion' => $detalleCotizacion,
            'status' => 201
        ];
        return response()->json($data, 201);
    }
    /**
     * Muestra un detalle específico.
     */
    public function show($id){
        $detalleCotizacion = DetalleCotizacion::with('cotizacion', 'producto')->find($id);
        if(!$detalleCotizacion){
            $data = [
                'message' => 'Detalle de cotizacion no encontrado',
                'status' => 404
            ];
            return response()->json($data, 404);
        }
        $data = [
            'detalleCotizacion' => $detalleCotizacion,
            'status' => 200
        ];
        return response()->json($data, 200);
    }
    /**
     * Elimina un detalle de cotización.
     */
    public function destroy($id){
        $detalleCotizacion = DetalleCotizacion::find($id);
        if(!$detalleCotizacion){
            $data = [
                'message' => 'Detalle de cotizacion no encontrado',
                'status' => 404
            ];
            return response()->json($data, 404);
        }
        $detalleCotizacion->delete();
        $data = [
            'detalleCotizacion' => $detalleCotizacion,
            'status' => 204
        ];
        return response()->json(null, 204);
    }
    /**
     * Actualiza un detalle de cotización.
     */
    public function update(Request $request, $id)
    {
        try {
            // Buscar el registro
            $detalleCotizacion = DetalleCotizacion::find($id);
            if (!$detalleCotizacion) {
                return response()->json([
                    'message' => 'Detalle de cotización no encontrado',
                    'status' => 404
                ], 404);
            }
        // Validar los datos del request
            $validator = Validator::make($request->all(), [
                'cotizacion_id' => ['required', Rule::exists('cotizacion', 'id')],
                'producto_id' => ['required', Rule::exists('producto', 'id')],
                'descripcion_item' => ['required', 'string', 'max:255'],
                'cantidad' => ['required', 'integer', 'min:1'],
                'precio_unitario' => ['required', 'numeric'],
                'descuento_porcentaje' => ['required', 'numeric'],
                'impuestos_aplicables' => ['required', 'numeric'],
                'notas' => ['nullable', 'string', 'max:255'],
            ]);
            if ($validator->fails()) {
                return response()->json([
                    'message' => 'Validación fallida',
                    'errors' => $validator->errors(),
                    'status' => 422
                ], 422);
            }
            // Actualizar los campos
            $detalleCotizacion->cotizacion_id = $request->cotizacion_id;
            $detalleCotizacion->producto_id = $request->producto_id;
            $detalleCotizacion->descripcion_item = $request->descripcion_item;
            $detalleCotizacion->cantidad = $request->cantidad;
            $detalleCotizacion->precio_unitario = $request->precio_unitario;
            $detalleCotizacion->descuento_porcentaje = $request->descuento_porcentaje;
            $detalleCotizacion->impuestos_aplicables = $request->impuestos_aplicables;
            $detalleCotizacion->notas = $request->notas;

            $detalleCotizacion->save();
            $detalleCotizacion->load(['cotizacion', 'producto']);

            return response()->json([
                'message' => 'Detalle de cotización actualizado correctamente',
                'detalleCotizacion' => $detalleCotizacion,
                'status' => 200
            ], 200);
        } catch (\Exception $e) {
            Log::error('Error al actualizar DetalleCotizacion: ' . $e->getMessage());
            return response()->json([
                'message' => 'Error interno al actualizar el detalle de cotización',
                'error' => $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }
    /**
     * Actualización parcial de un detalle.
     */
    public function updatePartial(Request $request, $id)
    {
        try {
            $detalleCotizacion = DetalleCotizacion::find($id);
            if(!$detalleCotizacion){
                $data = [
                    'message' => 'detalle de cotizacion no encontrado',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(), [
                'cotizacion_id' => [Rule::exists('cotizacion', 'id')],
                'producto_id' => [Rule::exists('producto', 'id')],
                'descripcion_item' => ['string', 'max:255'],
                'cantidad' => ['integer', 'min:1'],
                'precio_unitario' => ['numeric'],
                'descuento_porcentaje' => ['numeric'],
                'impuestos_aplicables' => ['numeric'],
                'notas' => ['nullable', 'string', 'max:255'],
            ]);
            if($validator->fails()){
                $data = [
                    'message' => 'Error en la Validacion',
                    'errors' => $validator->errors(),
                    'status' => 400
                ];
                return response()->json($data, 400);
            }
            if($request->has('cotizacion_id')){
                $detalleCotizacion->cotizacion_id = $request->cotizacion_id;
            }
            if($request->has('producto_id')){
                $detalleCotizacion->producto_id = $request->producto_id;
            }
            if($request->has('descripcion_item')){
                $detalleCotizacion->descripcion_item = $request->descripcion_item;
            }
            if($request->has('cantidad')){
                $detalleCotizacion->cantidad = $request->cantidad;
            }
            if($request->has('precio_unitario')){
                $detalleCotizacion->precio_unitario = $request->precio_unitario;
            }
            if($request->has('descuento_porcentaje')){
                $detalleCotizacion->descuento_porcentaje = $request->descuento_porcentaje;
            }
            if($request->has('impuestos_aplicables')){
                $detalleCotizacion->impuestos_aplicables = $request->impuestos_aplicables;
            }
            if($request->has('notas')){
                $detalleCotizacion->notas = $request->notas;
            }
            $detalleCotizacion->save();
            $detalleCotizacion->load(['cotizacion', 'producto']);
            $data = [
                'message' => 'detalle de cotizacion actualizado',
                'detalleCotizacion' => $detalleCotizacion,
                'status' => 200
            ];
            return response()->json($data, 200);
        } catch (\Exception $e) {
            Log::error('Error al actualizar DetalleCotizacion: ' . $e->getMessage());
            return response()->json([
                'message' => 'Error interno al actualizar el detalle de cotización',
                'error' => $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }
}

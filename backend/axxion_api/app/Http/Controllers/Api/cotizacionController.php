<?php

namespace App\Http\Controllers\api;

use App\Http\Controllers\Controller;
use App\Models\Cotizacion;
use App\Models\Cliente;
use App\Models\Solicitud;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Ramsey\Uuid\Codec\CodecInterface;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Validation\Rule;

class cotizacionController extends Controller
{
    /**
     * Lista todas las cotizaciones con sus detalles.
     */
        public function index()
        {
            try {
                $cotizacion = Cotizacion::with('cliente', 'solicitud', 'detalles', 'renta')->get();
                $data = [
                    'cotizacion' => $cotizacion,
                    'status' => 200
                ];
                return response()->json($data, 200);
            } catch (\Exception $e) {
                Log::error('Error al listar Rentas: ' . $e->getMessage());
                return response()->json([
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Crea una nueva cotización y sus detalles.
     * 
     * ANALOGÍA: Esta función es como redactar un presupuesto formal. 
     * Se suman los precios de todos los productos que el cliente quiere, 
     * se aplican impuestos y descuentos, y se genera un documento con fecha de validez.
     */
        public function store(Request $request){
            $validator = Validator::make($request->all(),[
                'cliente_id' => ['required', Rule::exists('cliente','id')],
                'solicitud_id' => ['required', Rule::exists('solicitud','id')],
                'fecha_cotizacion' => ['required', 'date'],
                'fecha_validez' => ['required', 'date'],
                'monto_total' => ['required', 'numeric'],
                'estado_cotizacion' => ['required', 'string', Rule::in(['Borrador','Enviada','Aceptada','Rechazada','Vencida'])],
                'terminos_condiciones' => ['nullable', 'string'],
                'notas_internas' => ['nullable', 'string'],
                'detalles' => ['required', 'array'],
                'detalles.*.producto_id' => ['required', Rule::exists('producto', 'id')],
                'detalles.*.cantidad' => ['required', 'integer', 'min:1'],
                'detalles.*.precio_unitario' => ['required', 'numeric'],
            ]);

            if($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }

            DB::beginTransaction();
            try {
                $cotizacion = Cotizacion::create([
                    'cliente_id' => $request->cliente_id,
                    'solicitud_id' => $request->solicitud_id,
                    'fecha_cotizacion' => $request->fecha_cotizacion,
                    'fecha_validez' => $request->fecha_validez,
                    'monto_total' => $request->monto_total,
                    'estado_cotizacion' => $request->estado_cotizacion,
                    'terminos_condiciones' => $request->terminos_condiciones,
                    'notas_internas' => $request->notas_internas,
                ]);

                foreach ($request->detalles as $detalle) {
                    \App\Models\DetalleCotizacion::create([
                        'cotizacion_id' => $cotizacion->id,
                        'producto_id' => $detalle['producto_id'],
                        'descripcion_item' => $detalle['descripcion_item'] ?? null,
                        'cantidad' => $detalle['cantidad'],
                        'precio_unitario' => $detalle['precio_unitario'],
                    ]);
                }

                DB::commit();

                $cotizacion->load(['cliente', 'solicitud', 'detalles.producto']);
                
                $data = [
                    'cotizacion' => $cotizacion,
                    'status' => 201
                ];
                return response()->json($data, 201);

            } catch (\Exception $e) {
                DB::rollBack();
                Log::error('Error al crear cotizacion: ' . $e->getMessage());
                return response()->json([
                    'message' => 'Error al crear cotizacion',
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }

    /**
     * Muestra una cotización específica con todos sus items.
     */
        public function show($id)
        {
            try{
                $cotizacion = Cotizacion::with('cliente', 'solicitud', 'detalles.producto', 'renta' )->find($id);
                if(!$cotizacion){
                    $data = [
                        'message' => 'Cotizacion no encontrada',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $data = [
                    'cotizacion' => $cotizacion,
                    'status' => 200
                ];
                return response()->json($data, 200);
            } catch (\Exception $e) {
                Log::error('Error al listar Cotizacion: ' . $e->getMessage());
                return response()->json([
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Elimina una cotización.
     */
        public function destroy($id)
        {
            try {
                $cotizacion = Cotizacion::with('cliente', 'solicitud', 'detalles', 'renta')->find($id);
                if(!$cotizacion){
                    $data = [
                        'message' => 'Cotizacion no encontrada',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                
                $cotizacion->delete();
                $data = [
                    'cotizacion' => $cotizacion,
                    'status' => 204
                ];
                return response()->json(null, 204);
                } catch (\Exception $e) {
                Log::error('Error al listar la cotizacion: ' . $e->getMessage());
                return response()->json([
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
        }
    /**
     * Actualiza una cotización existente.
     */
        public function update(Request $request, $id)
            {
                // 1) Buscar
                $cotizacion = Cotizacion::find($id);
                if (!$cotizacion) {
                    return response()->json([
                        'message' => 'Cotización no encontrada',
                        'status' => 404
                    ], 404);
                }
                // 2) Reglas de validación más estrictas
                $validator = Validator::make($request->all(), [
                    'cliente_id'           => ['required', Rule::exists('cliente','id')],
                    'solicitud_id'         => ['required', Rule::exists('solicitud','id')],
                    'fecha_cotizacion'     => ['required','date'],
                    'fecha_validez'        => ['required','date'],
                    'monto_total'          => ['required','numeric'],
                    'estado_cotizacion'    => ['required','string', Rule::in(['Borrador','Enviada','Aceptada','Rechazada','Vencida'])],
                    'terminos_condiciones' => ['nullable','string'],
                    'notas_internas'       => ['nullable','string'],
                ]);
                if ($validator->fails()) {
                    return response()->json([
                        'message' => 'Validation failed',
                        'errors'  => $validator->errors(),
                        'status'  => 422
                    ], 422);
                }
                // 3) Intentar update dentro de transacción y captura de errores
                try {
                    DB::beginTransaction();
                    // fill + save es más limpio
                    $cotizacion->fill($validator->validated());
                    $cotizacion->save();
                    DB::commit();
                } catch (\Throwable $e) {
                    DB::rollBack();
                    // Loguear error completo
                    Log::error('Error al actualizar cotización', [
                        'cotizacion_id' => $id,
                        'exception'     => $e->getMessage(),
                        'trace'         => $e->getTraceAsString()
                    ]);
                    $resp = [
                        'message' => 'Error interno al actualizar la cotización',
                        'status'  => 500
                    ];
                    // En desarrollo añadir detalle del error (temporal)
                    if (config('app.debug')) {
                        $resp['error'] = $e->getMessage();
                    }
                    return response()->json($resp, 500);
                }
                // 4) Cargar relaciones sólo si existen (evita Call to undefined relationship)
                $relations = [];
                if (method_exists($cotizacion, 'cliente')) {
                    $relations[] = 'cliente';
                }
                if (method_exists($cotizacion, 'solicitud')) {
                    $relations[] = 'solicitud';
                }
                if (!empty($relations)) {
                    $cotizacion->load($relations);
                }
                return response()->json([
                    'message'    => 'Cotización actualizada exitosamente',
                    'cotizacion' => $cotizacion,
                    'status'     => 200
                ], 200);
            }
            /**
             * Actualización parcial de una cotización.
             */
            public function updatePartial(Request $request, $id){
                $cotizacion = Cotizacion::find($id);
                if(!$cotizacion){
                    $data = [
                        'message' => 'cotizacion no encontrada',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $validator = Validator::make($request->all(),[
                    'cliente_id'           => [ Rule::exists('cliente','id')],
                    'solicitud_id'        => [ Rule::exists('solicitud','id')],
                    'fecha_cotizacion'     => ['date'],
                    'fecha_validez'        => ['date'],
                    'monto_total'          => ['numeric'],
                    'estado_cotizacion'    => ['string', Rule::in(['Borrador','Enviada','Aceptada','Rechazada','Vencida'])],
                    'terminos_condiciones' => ['string'],
                    'notas_internas'       => ['string'],
                ]);

                if($validator->fails()){
                    $data = [
                        'message' => 'Error en la Validacion',
                        'errors' => $validator->errors(),
                        'status' => 400
                    ];
                    return response()->json($data, 400);
                }

                if($request->has('cliente_id')){
                    $cotizacion->cliente_id = $request->cliente_id;
                }
                if($request->has('solicitud_id')){
                    $cotizacion->solicitud_id = $request->solicitud_id;
                }
                if($request->has('fecha_cotizacion')){
                    $cotizacion->fecha_cotizacion = $request->fecha_cotizacion;
                }
                if($request->has('fecha_validez')){
                    $cotizacion->fecha_validez = $request->fecha_validez;
                }
                if($request->has('monto_total')){
                    $cotizacion->monto_total = $request->monto_total;
                }
                if($request->has('estado_cotizacion')){
                    $cotizacion->estado_cotizacion = $request->estado_cotizacion;
                }
                if($request->has('terminos_condiciones')){
                    $cotizacion->terminos_condiciones = $request->terminos_condiciones;
                }
                if($request->has('notas_internas')){
                    $cotizacion->notas_internas = $request->notas_internas;
                }
                $cotizacion->save();
                $cotizacion->load(['cliente', 'solicitud']);
                $data = [
                    'message' => 'cotizacion actualizado',
                    'cotizacion' => $cotizacion,
                    'status' => 200
                ];
                return response()->json($data, 200);
            }
}

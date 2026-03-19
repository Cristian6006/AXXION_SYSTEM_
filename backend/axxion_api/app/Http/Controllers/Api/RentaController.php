<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Renta;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;

class RentaController extends Controller
{
    /**
     * Lista todas las rentas con sus detalles completos (cliente, cotización, equipos, entrega, devolución).
     */
    public function index() 
    {
        try {
            $renta = Renta::with([
                'cliente', 
                'cotizacion', 
                'inventarioItems.producto',  // Agregar relación producto
                'entrega', 
                'devolucion'
            ])->get();
            $data = [
                'renta' => $renta,
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
     * Crea una nueva renta en el sistema.
     * 
     * ANALOGÍA: Es como firmar un contrato de alquiler. Se validan las fechas, el cliente, 
     * el dinero (depósito y total) y se entregan las llaves (equipos).
     */
    public function store(Request $request) 
    {
        try {
            $validator = Validator::make($request->all(), [
                'cliente_id' => ['required', Rule::exists('cliente', 'id')],
                'cotizacion_id' => ['nullable', Rule::exists('cotizacion', 'id')],
                'fecha_inicio'=> ['required', 'date'],
                'fecha_fin_prevista' => ['required', 'date'],
                'fecha_devolucion_real' => ['nullable', 'date'],
                'estado_renta' => ['required', 'string', Rule::in(['Programada','EnCurso','Finalizada','Retrasada','Cancelada'])],
                'monto_total_renta' => ['required', 'numeric'],
                'deposito_garantia' => ['required', 'numeric'],
                'notas' => ['nullable', 'string'],
                'inventarioItems' => ['array'],
                'entrega' => ['array'],
                'devolucion' => ['array'],
            ]);
            if ($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            $renta = Renta::create([
                'cliente_id' => $request->cliente_id,
                'cotizacion_id' => $request->cotizacion_id,
                'fecha_inicio' => $request->fecha_inicio,
                'fecha_fin_prevista' => $request->fecha_fin_prevista,
                'fecha_devolucion_real' => $request->fecha_devolucion_real,
                'estado_renta' => $request->estado_renta,
                'monto_total_renta' => $request->monto_total_renta,
                'deposito_garantia' => $request->deposito_garantia,
                'notas' => $request->notas
            ]);
            if (!$renta){
                $data = [
                    'message' => 'Error al crear renta',
                    'status' => 500
                ];
                return response()->json($data, 500);
            }
            $renta->load('cliente', 'cotizacion');
            $data = [
                'renta' => $renta,
                'status' => 200
            ];
            return response()->json($data, 200);
        } catch (\Exception $e) {
                Log::error('Error al crear Renta: ' . $e->getMessage());
                return response()->json([
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
    }
    /**
     * Muestra los detalles de una renta específica.
     */
    public function show($id)
    {
        try {
                $renta = Renta::with([
                    'cliente', 
                    'cotizacion', 
                    'inventarioItems.producto',  // Agregar relación producto
                    'entrega', 
                    'devolucion'
                ])->find($id);
                if (!$renta) {
                    $data = [
                        'message' => 'Renta no encontrada',
                        'status' => 404
                    ];
                    return response()->json($data, 404);
                }
                $data = [
                    'renta' => $renta,
                    'status' => 200
                ];
                return response()->json($data, 200);
            } catch (\Exception $e) {
                Log::error('Error al consultar Renta: ' . $e->getMessage());
                return response()->json([
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
    }
    /**
     * Elimina una renta y gestiona el estado de los equipos asociados.
     * 
     * ANALOGÍA: Esta función es como cancelar una reserva de hotel: no solo se borra la reserva, 
     * sino que la habitación (equipo) se envía a limpieza (mantenimiento) para asegurar que 
     * esté lista para el siguiente huésped.
     */
    public function destroy($id)
    {
        try {
            $renta = Renta::with('cliente', 'cotizacion', 'inventarioItems', 'entrega', 'devolucion')->find($id);
            if (!$renta){
                $data = [
                    'message' => 'Renta no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            
            DB::transaction(function () use ($renta) {
                // Obtener IDs de items antes de detach
                $inventarioItemIds = $renta->inventarioItems->pluck('id')->toArray();
                
                // Actualizar estado de items a EnMantenimiento al eliminar renta
                if (!empty($inventarioItemIds)) {
                    DB::table('inventario_item')
                        ->whereIn('id', $inventarioItemIds)
                        ->update([
                            'estado_item' => 'EnMantenimiento',
                            'updated_at' => now()
                        ]);
                }
                
                // Detach items de la renta
                $renta->inventarioItems()->detach();
                
                if ($renta->entrega) {
                    $renta->entrega()->delete();
                }
                if ($renta->devolucion) {
                    $renta->devolucion()->delete();
                }
                $renta->delete();
            });
            
            $data = [
                'message' => 'Renta eliminada y equipos enviados a mantenimiento',
                'renta' => $renta,
                'status' => 200
            ];
            return response()->json($data, 200);
        } catch (\Exception $e) {
                Log::error('Error al eliminar Renta: ' . $e->getMessage());
                return response()->json([
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
        }
    }
    /**
     * Actualiza una renta existente y sincroniza los equipos.
     * Maneja lógica compleja para cambiar el estado de los equipos según si la renta finaliza o se cancela.
     */
    public function update(Request $request, $id) 
    {
        try {
            $renta = Renta::with('cliente', 'cotizacion', 'inventarioItems', 'entrega', 'devolucion')->find($id);
            if (!$renta){
                $data = [
                    'message' => 'Renta no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(), [
                'cliente_id' => ['required', Rule::exists('cliente', 'id')],
                'cotizacion_id' => ['nullable', Rule::exists('cotizacion', 'id')],
                'fecha_inicio'=> ['required', 'date'],
                'fecha_fin_prevista' => ['required', 'date'],
                'fecha_devolucion_real' => ['nullable', 'date'],
                'estado_renta' => ['required', 'string', Rule::in(['Programada','EnCurso','Finalizada','Retrasada','Cancelada'])],
                'monto_total_renta' => ['required', 'numeric'],
                'deposito_garantia' => ['required', 'numeric'],
                'notas' => ['nullable', 'string'],
                'inventarioItems' => ['array'],
                'entrega' => ['array'],
                'devolucion' => ['array'],
            ]);
            if ($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            $renta->cliente_id = $request->cliente_id;
            $renta->cotizacion_id = $request->cotizacion_id;
            $renta->fecha_inicio = $request->fecha_inicio;
            $renta->fecha_fin_prevista = $request->fecha_fin_prevista;
            $renta->fecha_devolucion_real = $request->fecha_devolucion_real;
            $renta->estado_renta = $request->estado_renta;
            $renta->monto_total_renta = $request->monto_total_renta;
            $renta->deposito_garantia = $request->deposito_garantia;
            $renta->notas = $request->notas;
            if($request->has('inventarioItems')){
                $renta->inventarioItems()->sync($request->inventarioItems);
            }
            if($request->has('entrega')){
                $entregaData = $request->input('entrega');
                // Si existe entrega relacionada, actualizar; si no, crear y asociar
                if ($renta->entrega) {
                    $renta->entrega()->update($entregaData);
                } else {
                    $renta->entrega()->create($entregaData);
                }
            }

            if($request->has('devolucion')){
                $devolucionData = $request->input('devolucion');
                if ($renta->devolucion) {
                    $renta->devolucion()->update($devolucionData);
                } else {
                    $renta->devolucion()->create($devolucionData);
                }
            }
            $renta->save();

            // Actualizar estado de items según el estado de la renta
            $inventarioItemIds = $renta->inventarioItems->pluck('id')->toArray();

            if (!empty($inventarioItemIds)) {
                if ($request->estado_renta === 'Finalizada') {
                    // Si la renta finaliza, enviar equipos a mantenimiento
                    // Esto disparará el trigger trg_inventario_item_after_update
                    DB::table('inventario_item')
                        ->whereIn('id', $inventarioItemIds)
                        ->update([
                            'estado_item' => 'EnMantenimiento',
                            'updated_at' => now()
                        ]);
                } elseif ($request->estado_renta === 'Cancelada') {
                    // Si la renta se cancela, poner equipos disponibles
                    DB::table('inventario_item')
                        ->whereIn('id', $inventarioItemIds)
                        ->update([
                            'estado_item' => 'Disponible',
                            'updated_at' => now()
                        ]);
                }
            }

            $data = [
                'message' => 'renta actualizada',
                'renta' => $renta,
                'status' => 200
            ];
            return response()->json($data, 200);
        } catch (\Exception $e) {
                Log::error('Error al actualizar Renta: ' . $e->getMessage());
                return response()->json([
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
    }
    /**
     * Actualización parcial de una renta (PATCH).
     */
    public function updatePartial(Request $request, $id)
    {
        try {
            $renta = Renta::with('cliente', 'cotizacion')->find($id);
            if (!$renta){
                $data = [
                    'message' => 'Renta no encontrada',
                    'status' => 404
                ];
                return response()->json($data, 404);
            }
            $validator = Validator::make($request->all(), [
                'cliente_id' => [Rule::exists('cliente', 'id')],
                'cotizacion_id' => [Rule::exists('cotizacion', 'id')],
                'fecha_inicio'=> ['date'],
                'fecha_fin_prevista' => ['date'],
                'fecha_devolucion_real' => ['date'],
                'estado_renta' => ['string', Rule::in(['Programada','EnCurso','Finalizada','Retrasada','Cancelada'])],
                'monto_total_renta' => ['numeric'],
                'deposito_garantia' => ['numeric'],
                'notas'=> ['string']
            ]);
            if ($validator->fails()){
                $data = [
                    'message' => 'Validation failed',
                    'errors' => $validator->errors(),
                    'status' => 422
                ];
                return response()->json($data, 422);
            }
            if($request->has('cliente_id')){
                $renta->cliente_id = $request->cliente_id;
            }
            if($request->has('cotizacion_id')){
                $renta->cotizacion_id = $request->cotizacion_id;
            }
            if($request->has('fecha_inicio')){
                $renta->fecha_inicio = $request->fecha_inicio;
            }
            if($request->has('fecha_fin_prevista')){
                $renta->fecha_fin_prevista = $request->fecha_fin_prevista;
            }
            if($request->has('fecha_devolucion_real')){
                $renta->fecha_devolucion_real = $request->fecha_devolucion_real;
            }
            if($request->has('estado_renta')){
                $renta->estado_renta = $request->estado_renta;
            }
            if($request->has('monto_total_renta')){
                $renta->monto_total_renta = $request->monto_total_renta;
            }
            if($request->has('deposito_garantia')){
                $renta->deposito_garantia = $request->deposito_garantia;
            }
            if($request->has('notas')){
                $renta->notas = $request->notas;
            }
            $renta->save();
            $renta->load('cliente', 'cotizacion');
            $data = [
                'message' => 'renta actualizado',
                'renta' => $renta,
                'status' => 200
            ];
            return response()->json($data, 200);
        } catch (\Exception $e) {
                Log::error('Error al actualizar Renta: ' . $e->getMessage());
                return response()->json([
                    'error' => $e->getMessage(),
                    'status' => 500
                ], 500);
            }
    }
}

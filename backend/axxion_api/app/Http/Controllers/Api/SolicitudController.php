<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Solicitud;
use App\Models\Renta;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class SolicitudController extends Controller
{
    /**
     * Lista todas las solicitudes registradas con sus clientes y productos.
     */
    public function index()
    {
        try {
            $solicitudes = Solicitud::with('cliente', 'productos')->get();
            return response()->json([
                'solicitudes' => $solicitudes,
                'status' => 200
            ], 200);
        } catch (\Exception $e) {
            Log::error('Error al listar Solicitudes: ' . $e->getMessage());
            return response()->json([
                'error' => $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Crea una nueva solicitud de productos.
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'cliente_id' => 'required|exists:cliente,id',
            'fecha_solicitud' => 'required|date',
            'cantidad_solicitada' => 'required|integer',
            'descripcion_necesidad' => 'nullable|string',
            'estado_solicitud' => 'required|in:Nueva,EnProceso,Atendida,Cancelada',
            'productos' => 'nullable|array',
            'productos.*.producto_id' => 'required|exists:producto,id',
            'productos.*.cantidad' => 'nullable|integer'
        ]);

        if ($validator->fails()) {
            return response()->json([
                'message' => 'Validation failed',
                'errors' => $validator->errors(),
                'status' => 400
            ], 400);
        }

        try {
            DB::beginTransaction();

            $solicitud = Solicitud::create([
                'cliente_id' => $request->cliente_id,
                'fecha_solicitud' => $request->fecha_solicitud,
                'cantidad_solicitada' => $request->cantidad_solicitada,
                'descripcion_necesidad' => $request->descripcion_necesidad,
                'estado_solicitud' => $request->estado_solicitud,
            ]);

            if ($request->has('productos')) {
                foreach ($request->productos as $prod) {
                    DB::table('solicitud_producto')->insert([
                        'solicitud_id' => $solicitud->id,
                        'producto_id' => $prod['producto_id']
                    ]);
                }
            }

            DB::commit();

            return response()->json([
                'message' => 'Solicitud creada exitosamente',
                'solicitud' => $solicitud,
                'status' => 201
            ], 201);

        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error al crear Solicitud: ' . $e->getMessage());
            return response()->json([
                'error' => $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Convierte una solicitud en una renta activa.
     *
     * ANALOGÍA: Es como cuando una reservación de mesa se convierte en una cuenta activa
     * porque los comensales ya llegaron. Los productos solicitados se vinculan a la renta.
     */
    public function convertToRental(Request $request, $id)
    {
        $validator = Validator::make($request->all(), [
            'fecha_inicio' => 'required|date',
            'fecha_fin_prevista' => 'required|date|after:fecha_inicio',
            'monto_total_renta' => 'required|numeric|min:0',
            'deposito_garantia' => 'required|numeric|min:0',
            'inventario_items' => 'required|array|min:1',
            'inventario_items.*' => 'exists:inventario_item,id'
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        try {
            DB::beginTransaction();

            $solicitud = Solicitud::findOrFail($id);

            // 1. Crear la Renta
            $renta = Renta::create([
                'cliente_id' => $solicitud->cliente_id,
                'fecha_inicio' => $request->fecha_inicio,
                'fecha_fin_prevista' => $request->fecha_fin_prevista,
                'estado_renta' => 'Programada',
                'monto_total_renta' => $request->monto_total_renta,
                'deposito_garantia' => $request->deposito_garantia,
                'notas' => "Generada desde Solicitud #$id. " . ($request->notas ?? '')
            ]);

            // 2. Vincular Items de Inventario y cambiar su estado
            foreach ($request->inventario_items as $itemId) {
                // Vincular a la tabla pivote renta_inventario_item
                DB::table('renta_inventario_item')->insert([
                    'renta_id' => $renta->id,
                    'inventario_item_id' => $itemId
                ]);

                // Actualizar estado del item a 'Rentado'
                DB::table('inventario_item')->where('id', $itemId)->update([
                    'estado_item' => 'Rentado'
                ]);
            }

            // 3. Marcar Solicitud como Atendida
            $solicitud->update(['estado_solicitud' => 'Atendida']);

            DB::commit();

            return response()->json([
                'message' => 'Solicitud convertida a Renta exitosamente',
                'renta' => $renta,
                'status' => 201
            ], 201);

        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error al convertir Solicitud a Renta: ' . $e->getMessage());
            return response()->json(['error' => $e->getMessage()], 500);
        }
    }

    /**
     * Muestra los detalles de una solicitud específica.
     */
    public function show($id)
    {
        try {
            $solicitud = Solicitud::with('cliente', 'productos')->find($id);
            if (!$solicitud) {
                return response()->json([
                    'message' => 'Solicitud no encontrada',
                    'status' => 404
                ], 404);
            }
            return response()->json([
                'solicitud' => $solicitud,
                'status' => 200
            ], 200);
        } catch (\Exception $e) {
            return response()->json([
                'error' => $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }
}

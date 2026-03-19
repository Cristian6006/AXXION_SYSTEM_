<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Models\Solicitud;
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
     * 
     * ANALOGÍA: Esta función es como llenar un formulario de pedido en un restaurante: 
     * el cliente (usuario) dice qué quiere comer (productos) y el mesero (sistema) 
     * anota todo para pasarlo a la cocina.
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
                    // Assuming a pivot table or related table exists. 
                    // Based on SQL dump: solicitud_producto (solicitud_id, producto_id)
                    // Note: The SQL dump shows a table `solicitud_producto` with just IDs.
                    // If quantity is needed per product in the request, the table structure might need check.
                    // The SQL dump `solicitud_producto` has no extra columns.
                    // However, `detalle_cotizacion` has quantity.
                    // For now, we just attach the product.
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

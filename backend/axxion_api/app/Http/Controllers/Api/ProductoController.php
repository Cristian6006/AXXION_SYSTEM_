<?php

namespace App\Http\Controllers\api;

use App\Http\Controllers\Controller;
use App\Models\Producto;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class ProductoController extends Controller
{
    /**
     * Muestra el catálogo completo de productos.
     */
    public function index()
    {
        try {
            // Eager load inventory items, their active rentals, and the client associated with those rentals
            $productos = Producto::with(['inventarioItems.rentas' => function($query) {
                $query->whereIn('estado_renta', ['Programada', 'EnCurso', 'Activa'])
                      ->orderBy('fecha_inicio', 'desc');
            }, 'inventarioItems.rentas.cliente'])->get();

            // Process each product to attach 'renta_activa' if applicable
            // This mimics the structure expected by the frontend EquipmentCard
            $productos->each(function($producto) {
                $producto->renta_activa = null;
                
                // Check if any inventory item has an active rental
                foreach ($producto->inventarioItems as $item) {
                    Log::info('Checking inventory item:', [
                        'item_id' => $item->id,
                        'rentas_count' => $item->rentas->count()
                    ]);
                    
                    $activeRental = $item->rentas->first();
                    if ($activeRental) {
                        Log::info('Found active rental:', [
                            'rental_id' => $activeRental->id,
                            'estado' => $activeRental->estado_renta,
                            'cliente' => $activeRental->cliente ? $activeRental->cliente->nombre : 'null'
                        ]);
                        
                        $producto->renta_activa = [
                            'cliente_nombre' => $activeRental->cliente ? 
                                trim($activeRental->cliente->nombre . ' ' . ($activeRental->cliente->apellido1 ?? '')) : 
                                'Cliente Desconocido',
                            'fecha_fin_prevista' => $activeRental->fecha_fin_prevista,
                            'estado_renta' => $activeRental->estado_renta
                        ];
                        break; 
                    }
                }
                
                if ($producto->renta_activa) {
                    Log::info('Product with active rental:', [
                        'producto_id' => $producto->id,
                        'producto_nombre' => $producto->nombre,
                        'renta_activa' => $producto->renta_activa
                    ]);
                }
            });

            return response()->json($productos);
        } catch (\Exception $e) {
            Log::error('Error in ProductoController::index: ' . $e->getMessage());
            return response()->json(['error' => 'Error al obtener los productos: ' . $e->getMessage()], 500);
        }
    }

    /**
     * Registra un nuevo producto en el sistema.
     * 
     * ANALOGÍA: Esta función actúa como el encargado de almacén que recibe un nuevo producto, 
     * verifica que venga con toda su documentación (validación) y lo registra en el sistema 
     * asignándole un lugar (estado).
     */
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'nombre' => 'required|string|max:255',
            'descripcion' => 'nullable|string',
            'marca' => 'nullable|string|max:255',
            'modelo' => 'nullable|string|max:255',
            'precio_referencia_renta' => 'nullable|numeric|min:0',
            'sku' => 'nullable|string|max:255|unique:producto,sku',
            'numero_serie' => 'nullable|string|max:100|unique:producto,numero_serie',
            'categoria' => 'nullable|string|max:50',
            'especificaciones' => 'nullable|array',
            'precio_alquiler_dia' => 'nullable|numeric|min:0',
            'precio_alquiler_semanal' => 'nullable|numeric|min:0',
            'precio_alquiler_mensual' => 'nullable|numeric|min:0',
            'precio_compra' => 'nullable|numeric|min:0',
            'valor_actual' => 'nullable|numeric|min:0',
            'fecha_compra' => 'nullable|date',
            'condicion' => 'nullable|string|max:50',
            'ubicacion' => 'nullable|string|max:255',
            'notas' => 'nullable|string',
            'estado' => 'nullable|string|max:50',
        ]);

        if ($validator->fails()) {
            return response()->json(['errors' => $validator->errors()], 422);
        }

        try {
            $data = $request->all();
            // Agregar estado por defecto si no se proporciona
            if (!isset($data['estado'])) {
                $data['estado'] = 'disponible';
            }
            
            $producto = Producto::create($data);
            return response()->json($producto, 201);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al crear el producto: ' . $e->getMessage()], 500);
        }
    }

    /**
     * Muestra los detalles de un producto específico, incluyendo sus relaciones.
     */
    public function show(Producto $id)
    {
        try {
            $producto = Producto::with('subcategorias', 'inventarioItems', 'solicitudes', 'detallesCotizacion')->find($id);
            return response()->json($producto);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al obtener el producto: ' . $e->getMessage()], 500);
        }
    }

    /**
     * Actualiza la información de un producto existente.
     */
    public function update(Request $request, $id)
    {
        try {
            // Buscar el producto por ID
            $producto = Producto::find($id);
            
            if (!$producto) {
                return response()->json(['error' => 'Producto no encontrado'], 404);
            }
            
            // Log para debugging
            \Log::info('Actualizando producto ID: ' . $id);
            \Log::info('Datos recibidos: ' . json_encode($request->all()));
            
            $validator = Validator::make($request->all(), [
                'nombre' => 'sometimes|required|string|max:255',
                'descripcion' => 'nullable|string',
                'marca' => 'nullable|string|max:255',
                'modelo' => 'nullable|string|max:255',
                'precio_referencia_renta' => 'nullable|numeric|min:0',
                'sku' => 'nullable|string|max:255|unique:producto,sku,' . $id,
                'numero_serie' => 'nullable|string|max:100|unique:producto,numero_serie,' . $id,
                'categoria' => 'nullable|string|max:50',
                'especificaciones' => 'nullable|array',
                'precio_alquiler_dia' => 'nullable|numeric|min:0',
                'precio_alquiler_semanal' => 'nullable|numeric|min:0',
                'precio_alquiler_mensual' => 'nullable|numeric|min:0',
                'precio_compra' => 'nullable|numeric|min:0',
                'valor_actual' => 'nullable|numeric|min:0',
                'fecha_compra' => 'nullable|date',
                'condicion' => 'nullable|string|max:50',
                'ubicacion' => 'nullable|string|max:255',
                'notas' => 'nullable|string',
                'estado' => 'nullable|string|max:50',
            ]);

            if ($validator->fails()) {
                \Log::error('Validación falló: ' . json_encode($validator->errors()));
                return response()->json(['errors' => $validator->errors()], 422);
            }

            // Actualizar el producto
            $updated = $producto->update($request->all());
            \Log::info('Resultado de actualización: ' . ($updated ? 'exitoso' : 'falló'));
            
            // Recargar el producto para obtener los datos actualizados
            $producto->refresh();
            \Log::info('Producto actualizado: ' . json_encode($producto->toArray()));
            
            return response()->json($producto, 200);
            
        } catch (\Exception $e) {
            \Log::error('Error al actualizar producto: ' . $e->getMessage());
            \Log::error('Stack trace: ' . $e->getTraceAsString());
            return response()->json(['error' => 'Error al actualizar el producto: ' . $e->getMessage()], 500);
        }
    }

    /**
     * Elimina un producto del sistema.
     */
    public function destroy($id)
    {
        try {
            // Buscar el producto por ID
            $producto = Producto::find($id);
            
            if (!$producto) {
                return response()->json(['error' => 'Producto no encontrado'], 404);
            }
            
            // Log para debugging
            \Log::info('Intentando eliminar producto ID: ' . $id . ', Nombre: ' . $producto->nombre);
            
            // Eliminar directamente con query builder
            $deleted = DB::table('producto')->where('id', $id)->delete();
            
            \Log::info('Filas eliminadas: ' . $deleted);
            
            if ($deleted > 0) {
                return response()->json([
                    'message' => 'Producto eliminado exitosamente',
                    'deleted_id' => $id
                ], 200);
            } else {
                return response()->json(['error' => 'No se pudo eliminar el producto'], 500);
            }
            
        } catch (\Exception $e) {
            \Log::error('Error al eliminar producto: ' . $e->getMessage());
            \Log::error('Stack trace: ' . $e->getTraceAsString());
            return response()->json([
                'error' => 'Error al eliminar el producto: ' . $e->getMessage()
            ], 500);
        }
    }
}

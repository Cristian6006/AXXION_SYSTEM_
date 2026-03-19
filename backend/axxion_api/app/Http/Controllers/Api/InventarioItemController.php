<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\InventarioItem;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;

class InventarioItemController extends Controller
{
    private const VALIDATION_FAILED_MESSAGE  = 'Validation failed';
    private const ITEM_MISSING_MESSAGE = 'Item de inventario no encontrado';
    /**
     * Muestra el listado de todos los items del inventario.
     * Carga relaciones con producto, mantenimientos y rentas.
     */
    public function index()
    {
        try {
            $inventarioItems = InventarioItem::with('producto', 'mantenimientos', 'rentas')->get();
            return response()->json([
                'inventario_items' => $inventarioItems,
                'status' => 200
            ], 200);
        } catch (\Exception $e) {
            Log::error('Error al obtener inventario items: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al obtener los items de inventario: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Almacena un nuevo item de inventario en la base de datos.
     * Valida los datos de entrada antes de crear el registro.
     */
    public function store(Request $request)
    {
        try {
            $validator = Validator::make($request->all(), [
                'producto_id' => ['required', Rule::exists('producto', 'id')],
                'numero_serie' => ['nullable', 'string', 'max:100', 'unique:inventario_item,numero_serie'],
                'estado_item' => ['required', 'string', Rule::in(['Disponible', 'Rentado', 'EnMantenimiento', 'DeBaja'])],
                'fecha_adquisicion' => ['nullable', 'date'],
                'costo_adquisicion' => ['nullable', 'numeric', 'min:0'],
                'ubicacion_fisica' => ['nullable', 'string', 'max:100'],
                'notas' => ['nullable', 'string'],
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'message' => self::VALIDATION_FAILED_MESSAGE,
                    'errors' => $validator->errors(),
                    'status' => 422
                ], 422);
            }

            DB::beginTransaction();

            $inventarioItem = InventarioItem::create($request->all());
            $inventarioItem->load('producto');

            DB::commit();

            return response()->json([
                'message' => 'Item de inventario creado exitosamente',
                'inventario_item' => $inventarioItem,
                'status' => 201
            ], 201);

        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error al crear inventario item: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error interno al crear el item de inventario: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Muestra un item específico del inventario por su ID.
     */
    public function show($id)
    {
        try {
            $inventarioItem = InventarioItem::with('producto', 'mantenimientos', 'rentas')->find($id);

            if (!$inventarioItem) {
                return response()->json([
                    'message' => self::ITEM_MISSING_MESSAGE,
                    'status' => 404
                ], 404);
            }

            return response()->json([
                'inventario_item' => $inventarioItem,
                'status' => 200
            ], 200);

        } catch (\Exception $e) {
            Log::error('Error al obtener inventario item: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al obtener el item de inventario: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Actualiza un item de inventario existente.
     * Requiere validación completa de los campos enviados.
     */
    public function update(Request $request, $id)
    {
        try {
            $inventarioItem = InventarioItem::find($id);

            if (!$inventarioItem) {
                return response()->json([
                    'message' => self::ITEM_MISSING_MESSAGE,
                    'status' => 404
                ], 404);
            }

            $validator = Validator::make($request->all(), [
                'producto_id' => ['sometimes', 'required', Rule::exists('producto', 'id')],
                'numero_serie' => ['nullable', 'string', 'max:100', Rule::unique('inventario_item', 'numero_serie')->ignore($id)],
                'estado_item' => ['sometimes', 'required', 'string', Rule::in(['Disponible', 'Rentado', 'EnMantenimiento', 'DeBaja'])],
                'fecha_adquisicion' => ['nullable', 'date'],
                'costo_adquisicion' => ['nullable', 'numeric', 'min:0'],
                'ubicacion_fisica' => ['nullable', 'string', 'max:100'],
                'notas' => ['nullable', 'string'],
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'message' => self::VALIDATION_FAILED_MESSAGE,
                    'errors' => $validator->errors(),
                    'status' => 422
                ], 422);
            }

            DB::beginTransaction();

            $inventarioItem->update($request->all());
            $inventarioItem->load('producto');

            DB::commit();

            return response()->json([
                'message' => 'Item de inventario actualizado exitosamente',
                'inventario_item' => $inventarioItem,
                'status' => 200
            ], 200);

        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error al actualizar inventario item: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error interno al actualizar el item de inventario: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Actualización parcial de un item de inventario (PATCH).
     * Solo actualiza los campos presentes en la solicitud.
     */
    public function updatePartial(Request $request, $id)
    {
        try {
            $inventarioItem = InventarioItem::find($id);

            if (!$inventarioItem) {
                return response()->json([
                    'message' => self::ITEM_MISSING_MESSAGE,
                    'status' => 404
                ], 404);
            }

            $validator = Validator::make($request->all(), [
                'producto_id' => ['sometimes', Rule::exists('producto', 'id')],
                'numero_serie' => ['nullable', 'string', 'max:100', Rule::unique('inventario_item', 'numero_serie')->ignore($id)],
                'estado_item' => ['sometimes', 'string', Rule::in(['Disponible', 'Rentado', 'EnMantenimiento', 'DeBaja'])],
                'fecha_adquisicion' => ['nullable', 'date'],
                'costo_adquisicion' => ['nullable', 'numeric', 'min:0'],
                'ubicacion_fisica' => ['nullable', 'string', 'max:100'],
                'notas' => ['nullable', 'string'],
            ]);

            if ($validator->fails()) {
                return response()->json([
                    'message' => self::VALIDATION_FAILED_MESSAGE,
                    'errors' => $validator->errors(),
                    'status' => 422
                ], 422);
            }

            DB::beginTransaction();

            $inventarioItem->update($request->only(array_keys($request->all())));
            $inventarioItem->load('producto');

            DB::commit();

            return response()->json([
                'message' => 'Item de inventario actualizado parcialmente',
                'inventario_item' => $inventarioItem,
                'status' => 200
            ], 200);

        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error al actualizar inventario item: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error interno al actualizar el item de inventario: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Elimina un item del inventario.
     */
    public function destroy($id)
    {
        try {
            $inventarioItem = InventarioItem::find($id);

            if (!$inventarioItem) {
                return response()->json([
                    'message' => 'Item de inventario no encontrado',
                    'status' => 404
                ], 404);
            }

            DB::beginTransaction();

            $inventarioItem->delete();

            DB::commit();

            return response()->json([
                'message' => 'Item de inventario eliminado exitosamente',
                'status' => 200
            ], 200);

        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error al eliminar inventario item: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error interno al eliminar el item de inventario: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Obtiene todos los items de inventario asociados a un producto específico.
     */
    public function getByProducto($producto_id)
    {
        try {
            $inventarioItems = InventarioItem::where('producto_id', $producto_id)
                ->with('producto', 'mantenimientos', 'rentas')
                ->orderBy('created_at', 'desc')
                ->get();

            return response()->json([
                'inventario_items' => $inventarioItems,
                'status' => 200
            ], 200);

        } catch (\Exception $e) {
            Log::error('Error al obtener inventario items por producto: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al obtener los items de inventario del producto: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Filtra los items de inventario por su estado (ej. Disponible, Rentado).
     */
    public function getByEstado($estado)
    {
        try {
            $estados_validos = ['Disponible', 'Rentado', 'EnMantenimiento', 'DeBaja'];
            
            if (!in_array($estado, $estados_validos)) {
                return response()->json([
                    'error' => 'Estado no válido',
                    'status' => 400
                ], 400);
            }

            $inventarioItems = InventarioItem::where('estado_item', $estado)
                ->with('producto', 'mantenimientos', 'rentas')
                ->orderBy('created_at', 'desc')
                ->get();

            return response()->json([
                'inventario_items' => $inventarioItems,
                'status' => 200
            ], 200);

        } catch (\Exception $e) {
            Log::error('Error al obtener inventario items por estado: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al obtener los items de inventario por estado: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }

    /**
     * Obtiene items de inventario enriquecidos con el estado de su renta actual.
     * 
     * ANALOGÍA: Esta función es como un "Bibliotecario Omnisciente" que no solo sabe qué libros (items) 
     * hay en la biblioteca, sino que tiene una ficha pegada en cada uno diciendo exactamente 
     * quién lo tiene prestado ahora mismo y cuándo promete devolverlo.
     */
    public function getWithRentalStatus()
    {
        try {
            $inventarioItems = InventarioItem::with([
                'producto',
                'mantenimientos' => function($query) {
                    $query->whereIn('estado_mantenimiento', ['Programado', 'EnProceso'])
                          ->orderBy('fecha_inicio', 'desc');
                },
                'rentas' => function($query) {
                    // Incluir rentas activas: estados específicos O sin fecha de devolución
                    $query->where(function($q) {
                        $q->whereIn('estado_renta', ['Programada', 'EnCurso', 'Activa', 'Pendiente'])
                          ->orWhereNull('fecha_devolucion_real');
                    })
                    ->with('cliente')
                    ->orderBy('fecha_inicio', 'desc');
                }
            ])->get();

            // Enriquecer cada item con información de renta activa
            $inventarioItems->each(function($item) {
                Log::info('Processing inventory item:', [
                    'item_id' => $item->id,
                    'producto' => $item->producto ? $item->producto->nombre : 'null',
                    'rentas_count' => $item->rentas->count(),
                    'rentas' => $item->rentas->map(function($r) {
                        return [
                            'id' => $r->id,
                            'estado' => $r->estado_renta,
                            'cliente_id' => $r->cliente_id,
                            'fecha_inicio' => $r->fecha_inicio,
                            'fecha_fin' => $r->fecha_fin_prevista
                        ];
                    })
                ]);
                
                $rentaActiva = $item->rentas->first();
                
                if ($rentaActiva) {
                    Log::info('Found active rental for item ' . $item->id, [
                        'rental_id' => $rentaActiva->id,
                        'estado' => $rentaActiva->estado_renta,
                        'cliente' => $rentaActiva->cliente ? $rentaActiva->cliente->nombre : 'null'
                    ]);
                }
                
                $item->renta_activa = $rentaActiva ? [
                    'id' => $rentaActiva->id,
                    'cliente_nombre' => $rentaActiva->cliente ? 
                        trim($rentaActiva->cliente->nombre . ' ' . ($rentaActiva->cliente->nombre2 ?? '') . ' ' . 
                             ($rentaActiva->cliente->apellido1 ?? '') . ' ' . ($rentaActiva->cliente->apellido2 ?? '')) : 
                        'N/A',
                    'fecha_inicio' => $rentaActiva->fecha_inicio,
                    'fecha_fin_prevista' => $rentaActiva->fecha_fin_prevista,
                    'estado_renta' => $rentaActiva->estado_renta
                ] : null;
            });

            return response()->json([
                'inventario_items' => $inventarioItems,
                'status' => 200
            ], 200);

        } catch (\Exception $e) {
            Log::error('Error al obtener inventario items con estado de renta: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al obtener los items de inventario con estado de renta: ' . $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }
}

<?php

namespace App\Http\Controllers\api;

use App\Http\Controllers\Controller;
use App\Models\Mantenimiento;
use App\Models\InventarioItem;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\DB;

class MantenimientoController extends Controller
{
    private const INCORRECT_DATA_MESSAJE = 'Datos de validación incorrectos';
    private const MAINTENANCE_NOT_FOUND_MESSAJE = 'Mantenimiento no encontrado';
    /**
     * Lista todos los mantenimientos registrados.
     */
    public function index()
    {
        try {
            $mantenimientos = Mantenimiento::with('inventarioItem')->get();
            return response()->json($mantenimientos);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al obtener los mantenimientos: ' . $e->getMessage()], 500);
        }
    }

    /**
     * Registra un nuevo mantenimiento en el sistema.
     * 
     * ANALOGÍA: Es como abrir una "Orden de Trabajo" en un taller mecánico. 
     * Se anotan los datos del vehículo (item), qué se le va a hacer, quién lo hará y cuándo.
     */
    public function store(Request $request)
    {
        // Normalizar a mayúsculas para permitir flexibilidad en el input
        $data = $request->all();
        if (isset($data['tipo_mantenimiento'])) {
            $data['tipo_mantenimiento'] = strtoupper($data['tipo_mantenimiento']);
        }
        if (isset($data['estado_mantenimiento'])) {
            $data['estado_mantenimiento'] = strtoupper($data['estado_mantenimiento']);
        }

        $validator = Validator::make($data, [
            'inventario_item_id' => 'required|exists:inventario_item,id',
            'fecha_inicio' => 'required|date',
            'fecha_fin_prevista' => 'required|date|after_or_equal:fecha_inicio',
            'fecha_fin_real' => 'nullable|date',
            'tipo_mantenimiento' => 'required|string|in:PREVENTIVO,CORRECTIVO,MEJORA,PREDICTIVO,EMERGENCIA',
            'descripcion_problema' => 'required|string|max:1000',
            'descripcion_trabajo_realizado' => 'nullable|string|max:1000',
            'costo_estimado' => 'nullable|numeric|min:0',
            'costo_real' => 'nullable|numeric|min:0',
            'estado_mantenimiento' => 'required|string|in:PROGRAMADO,EN_PROCESO,FINALIZADO,COMPLETADO,CANCELADO,PAUSADO',
            'responsable' => 'required|string|max:255',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'error' => self::INCORRECT_DATA_MESSAJE,
                'messages' => $validator->errors()
            ], 422);
        }

        try {
            DB::beginTransaction();

            $mantenimiento = Mantenimiento::create($data);
            $mantenimiento->load('inventarioItem');

            DB::commit();

            return response()->json([
                'message' => 'Mantenimiento creado exitosamente',
                'mantenimiento' => $mantenimiento
            ], 201);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'error' => 'Error al crear el mantenimiento: ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * Muestra los detalles de un mantenimiento específico.
     */
    public function show($id)
    {
        try {
            $mantenimiento = Mantenimiento::with('inventarioItem')->find($id);
            if (!$mantenimiento) {
                return response()->json(['error' => self::MAINTENANCE_NOT_FOUND_MESSAJE], 404);
            }

            return response()->json($mantenimiento);

        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al obtener el mantenimiento: ' . $e->getMessage()], 500);
        }
    }

    /**
     * Actualiza la información de un mantenimiento completo.
     */
    public function update(Request $request, $id)
    {
        // Normalizar a mayúsculas para permitir flexibilidad en el input
        $data = $request->all();
        if (isset($data['tipo_mantenimiento'])) {
            $data['tipo_mantenimiento'] = strtoupper($data['tipo_mantenimiento']);
        }
        if (isset($data['estado_mantenimiento'])) {
            $data['estado_mantenimiento'] = strtoupper($data['estado_mantenimiento']);
        }

        $validator = Validator::make($data, [
            'inventario_item_id' => 'sometimes|required|exists:inventario_item,id',
            'fecha_inicio' => 'sometimes|required|date',
            'fecha_fin_prevista' => 'sometimes|required|date|after_or_equal:fecha_inicio',
            'fecha_fin_real' => 'nullable|date',
            'tipo_mantenimiento' => 'sometimes|required|string|in:PREVENTIVO,CORRECTIVO,MEJORA,PREDICTIVO,EMERGENCIA',
            'descripcion_problema' => 'sometimes|required|string|max:1000',
            'descripcion_trabajo_realizado' => 'nullable|string|max:1000',
            'costo_estimado' => 'nullable|numeric|min:0',
            'costo_real' => 'nullable|numeric|min:0',
            'estado_mantenimiento' => 'sometimes|required|string|in:PROGRAMADO,EN_PROCESO,FINALIZADO,COMPLETADO,CANCELADO,PAUSADO',
            'responsable' => 'sometimes|required|string|max:255',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'error' => self::INCORRECT_DATA_MESSAJE,
                'messages' => $validator->errors()
            ], 422);
        }

        try {
            $mantenimiento = Mantenimiento::find($id);

            if (!$mantenimiento) {
                return response()->json(['error' => self::MAINTENANCE_NOT_FOUND_MESSAJE], 404);
            }

            DB::beginTransaction();

            $mantenimiento->update($data);
            $mantenimiento->load('inventarioItem');

            DB::commit();

            return response()->json([
                'message' => 'Mantenimiento actualizado exitosamente',
                'mantenimiento' => $mantenimiento
            ]);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'error' => 'Error al actualizar el mantenimiento: ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * Actualiza parcialmente un registro de mantenimiento.
     * Útil para cambiar solo el estado o agregar costos finales sin re-enviar todo.
     */
    public function updatePartial(Request $request, $id)
    {
        $validator = Validator::make($request->all(), [
            'inventario_item_id' => 'sometimes|exists:inventario_item,id',
            'fecha_inicio' => 'sometimes|date',
            'fecha_fin_prevista' => 'sometimes|date',
            'fecha_fin_real' => 'nullable|date',
            'tipo_mantenimiento' => 'sometimes|string|in:PREVENTIVO,CORRECTIVO,PREDICTIVO,EMERGENCIA',
            'descripcion_problema' => 'sometimes|string|max:1000',
            'descripcion_trabajo_realizado' => 'nullable|string|max:1000',
            'costo_estimado' => 'nullable|numeric|min:0',
            'costo_real' => 'nullable|numeric|min:0',
            'estado_mantenimiento' => 'sometimes|string|in:PROGRAMADO,EN_PROCESO,COMPLETADO,CANCELADO,PAUSADO',
            'responsable' => 'sometimes|string|max:255',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'error' => self::INCORRECT_DATA_MESSAJE,
                'messages' => $validator->errors()
            ], 422);
        }

        try {
            $mantenimiento = Mantenimiento::find($id);

            if (!$mantenimiento) {
                return response()->json(['error' => self::MAINTENANCE_NOT_FOUND_MESSAJE], 404);
            }

            DB::beginTransaction();

            $mantenimiento->update($request->only(array_keys($request->all())));
            $mantenimiento->load('inventarioItem');

            DB::commit();

            return response()->json([
                'message' => 'Mantenimiento actualizado parcialmente',
                'mantenimiento' => $mantenimiento
            ]);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'error' => 'Error al actualizar el mantenimiento: ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * Elimina un registro de mantenimiento.
     */
    public function destroy($id)
    {
        try {
            $mantenimiento = Mantenimiento::find($id);

            if (!$mantenimiento) {
                return response()->json(['error' => self::MAINTENANCE_NOT_FOUND_MESSAJE], 404);
            }

            DB::beginTransaction();

            $mantenimiento->delete();

            DB::commit();

            return response()->json([
                'message' => 'Mantenimiento eliminado exitosamente'
            ]);

        } catch (\Exception $e) {
            DB::rollBack();
            return response()->json([
                'error' => 'Error al eliminar el mantenimiento: ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * Obtiene el historial de mantenimientos de un item específico.
     */
    public function getByInventarioItem($inventario_item_id)
    {
        try {
            $mantenimientos = Mantenimiento::where('inventario_item_id', $inventario_item_id)
                ->with('inventarioItem')
                ->orderBy('fecha_inicio', 'desc')
                ->get();

            return response()->json($mantenimientos);

        } catch (\Exception $e) {
            return response()->json([
                'error' => 'Error al obtener los mantenimientos del item: ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * Filtra mantenimientos por su estado actual (ej. PROGRAMADO, EN_PROCESO).
     */
    public function getByEstado($estado)
    {
        try {
            $estados_validos = ['PROGRAMADO', 'EN_PROCESO', 'COMPLETADO', 'CANCELADO', 'PAUSADO'];
            
            if (!in_array(strtoupper($estado), $estados_validos)) {
                return response()->json(['error' => 'Estado no válido'], 400);
            }

            $mantenimientos = Mantenimiento::where('estado_mantenimiento', strtoupper($estado))
                ->with('inventarioItem')
                ->orderBy('fecha_inicio', 'desc')
                ->get();

            return response()->json($mantenimientos);

        } catch (\Exception $e) {
            return response()->json([
                'error' => 'Error al obtener los mantenimientos por estado: ' . $e->getMessage()
            ], 500);
        }
    }

    /**
     * Filtra mantenimientos por tipo (ej. PREVENTIVO, CORRECTIVO).
     */
    public function getByTipo($tipo)
    {
        try {
            $tipos_validos = ['PREVENTIVO', 'CORRECTIVO', 'PREDICTIVO', 'EMERGENCIA'];
            
            if (!in_array(strtoupper($tipo), $tipos_validos)) {
                return response()->json(['error' => 'Tipo de mantenimiento no válido'], 400);
            }

            $mantenimientos = Mantenimiento::where('tipo_mantenimiento', strtoupper($tipo))
                ->with('inventarioItem')
                ->orderBy('fecha_inicio', 'desc')
                ->get();

            return response()->json($mantenimientos);

        } catch (\Exception $e) {
            return response()->json([
                'error' => 'Error al obtener los mantenimientos por tipo: ' . $e->getMessage()
            ], 500);
        }
    }
}

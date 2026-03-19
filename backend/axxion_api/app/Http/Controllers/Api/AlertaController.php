<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Mantenimiento;
use App\Models\Renta;
use App\Models\Producto;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class AlertaController extends Controller
{
    /**
     * Muestra una lista de alertas generadas por el sistema.
     * 
     * ANALOGÍA: Esta función actúa como una "Torre de Control" que escanea constantemente 
     * tres radares diferentes: Productos (equipos antiguos), Mantenimientos (pendientes/atrasados) 
     * y Rentas (programadas/atrasadas) para notificar al operador sobre cualquier anomalía.
     */
    public function index()
    {
        try {
            $hoy = now();
            // Busca productos comprados hace mucho tiempo (antigüedad)
            // Regla: Si es día 24 o más, da 15 días de margen, si no, 7 días.
            $producto = Producto::whereDate('fecha_compra', '<=', $hoy->copy()->addDays($hoy->day >= 24 ? 15 : 7))
                ->get()
                ->map(function($item) {
                    return [
                        'tipo' => 'Inventario',
                        'nivel' => 'Informativa',
                        'titulo' => "Antigüedad del equipo de {$item->nombre}",
                        'mensaje' => "El equipo {$item->nombre} ({$item->modelo}) tiene más de 1 año desde su compra.",
                        'referencia' => "Equipo ID: {$item->id}",
                        'fecha' => $item->fecha_compra,
                    ];
                });
            
            // Busca mantenimientos programados para hoy o antes, o en proceso pero atrasados
            $mantenimientos = Mantenimiento::where('estado_mantenimiento', 'Programado')
                ->whereDate('fecha_inicio', '<=', $hoy)
                ->orWhere(function($q) use ($hoy) {
                    $q->where('estado_mantenimiento', 'EnProceso')
                    ->whereDate('fecha_fin_prevista', '<', $hoy);
                })
                ->get()
                ->map(function($m) {
                    return [
                        'tipo' => 'Mantenimiento',
                        'nivel' => $m->estado_mantenimiento === 'Programado' ? 'Informativa' : 'Crítica',
                        'titulo' => "Mantenimiento {$m->tipo_mantenimiento} pendiente",
                        'mensaje' => $m->descripcion_problema ?? "Revisar estado del equipo",
                        'referencia' => "Equipo ID: {$m->inventario_item_id}",
                        'fecha' => $m->fecha_inicio,
                    ];
                });

            // Busca rentas programadas para hoy o antes, o en curso pero atrasadas
            $rentas = Renta::where('estado_renta', 'Programada')
                ->whereDate('fecha_inicio', '<=', $hoy)
                ->orWhere(function($q) use ($hoy) {
                    $q->where('estado_renta', 'EnCurso')
                    ->whereDate('fecha_fin_prevista', '<', $hoy);
                })
                ->get()
                ->map(function($r) {
                    return [
                        'tipo' => 'Alquiler',
                        'nivel' => $r->estado_renta === 'Programada' ? 'Advertencia' : 'Crítica',
                        'titulo' => "Renta #{$r->id} {$r->estado_renta}",
                        'mensaje' => $r->notas ?? 'Revisar estado de la renta',
                        'referencia' => "Cliente: {$r->cliente->nombre} {$r->cliente->apellido1}",
                        'fecha' => $r->fecha_inicio,
                    ];
                });                
            
            // Combina todas las alertas en una sola colección
            $alertas = $producto->merge($mantenimientos)->merge($rentas);
            return response()->json([
                'alertas' => $alertas,
                'total' => $alertas->count()
            ]);
        } catch (Exception $e) {
            Log::error('Error al obtener alertas'. $e->getMessage());
            return response()->json([
                'errors' => $e->getMessage(),
                'status' => 500
            ], 500);
        }
    }
}
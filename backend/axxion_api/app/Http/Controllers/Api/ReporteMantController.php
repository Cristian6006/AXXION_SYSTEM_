<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Mantenimiento;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;

class ReporteMantController extends Controller
{
    public function reporteEficiencia()
    {
        // Filtramos solo los mantenimientos terminados para tener datos reales
        $mantenimientos = Mantenimiento::whereNotNull('fecha_fin_real')
            ->whereNotNull('fecha_inicio')
            ->get();

        $datos = $mantenimientos->map(function ($item) {
            // Calculamos la diferencia en días
            $inicio = Carbon::parse($item->fecha_inicio);
            $finReal = Carbon::parse($item->fecha_fin_real);
            $finPrevista = Carbon::parse($item->fecha_fin_prevista);

            return [
                'id' => $item->id,
                'item' => $item->inventarioItem->nombre ?? 'Item #' . $item->inventario_item_id,
                'dias_reparacion' => intval(abs($finReal->diffInDays($inicio))),
                'retraso_dias' => abs($finReal->greaterThan($finPrevista))
                    ? abs($finReal->diffInDays($finPrevista))
                    : 0,
            ];
        });

        $totalRegistros = $datos->count();
        $cantidadAtrasos = $datos->where('retraso_dias', '>', 0)->count();

        $porcentajeAtrasos = $totalRegistros > 0
            ? ($cantidadAtrasos / $totalRegistros) * 100
            : 0;

        $promedioDiasReparacion = $datos->avg('dias_reparacion') ?? 0;

        return response()->json([
            'resumen' => [
                'tiempo_promedio_reparacion_dias' => intval(abs($promedioDiasReparacion), 2),
                'tasa_de_incumplimiento' => round($porcentajeAtrasos, 2) . '%',
                'total_analizados' => $totalRegistros,
            ],
            'detalle' => $datos
        ]);
    }

    /**
     * Análisis Financiero - Control de Costos
     * 
     * Calcula:
     * 1. Desviación Presupuestaria (costo_estimado vs costo_real)
     * 2. TCO (Total Cost of Ownership) por Activo
     * 
     * Detecta:
     * - Problemas de planificación presupuestaria
     * - "Activos vampiro" (equipos que cuestan más mantener que reemplazar)
     */
    public function analisisFinanciero()
    {
        try {
            // Obtenemos todos los mantenimientos con costos reales
            $mantenimientos = Mantenimiento::whereNotNull('costo_real')
                ->with('inventarioItem')
                ->get();

            // === 1. DESVIACIÓN PRESUPUESTARIA ===
            $desviaciones = $mantenimientos->map(function ($item) {
                $costoEstimado = $item->costo_estimado ?? 0;
                $costoReal = $item->costo_real ?? 0;
                $desviacion = $costoReal - $costoEstimado;
                $desviacionPorcentaje = $costoEstimado > 0
                    ? (($desviacion / $costoEstimado) * 100)
                    : 0;

                return [
                    'mantenimiento_id' => $item->id,
                    'item' => $item->inventarioItem->nombre ?? 'Item #' . $item->inventario_item_id,
                    'costo_estimado' => round($costoEstimado, 2),
                    'costo_real' => round($costoReal, 2),
                    'desviacion' => round($desviacion, 2),
                    'desviacion_porcentaje' => round($desviacionPorcentaje, 2),
                    'subestimado' => $desviacion > 0,
                ];
            });

            // Métricas de desviación presupuestaria
            $totalMantenimientos = $desviaciones->count();
            $subestimados = $desviaciones->where('subestimado', true)->count();
            $porcentajeSubestimacion = $totalMantenimientos > 0
                ? round(($subestimados / $totalMantenimientos) * 100, 2)
                : 0;

            $desviacionPromedioMonto = $desviaciones->avg('desviacion');
            $desviacionPromedioPorcentaje = $desviaciones->avg('desviacion_porcentaje');

            // === 2. TCO (Total Cost of Ownership) POR ACTIVO ===
            $tcosPorActivo = Mantenimiento::select(
                'inventario_item_id',
                DB::raw('SUM(costo_real) as tco_total'),                  
                DB::raw('COUNT(*) as cantidad_reparaciones'),       
                DB::raw('AVG(costo_real) as costo_promedio_reparacion') 
            )
            ->whereNotNull('costo_real')
            ->groupBy('inventario_item_id')
            ->with('inventarioItem:id,numero_serie') // Traemos solo ID y numero de serie 
            ->orderByDesc('tco_total') // Ordenamos en la BD
            ->get(); // Aquí ya traemos el resultado procesado.

            // Mapeamos solo para dar formato final 
            $tcosFormateados = $tcosPorActivo->map(function($item) {
                return [
                    'inventario_item_id' => $item->inventario_item_id,
                    'nombre_item' => $item->inventarioItem->numero_serie ?? 'Item #' . $item->inventario_item_id,
                    'tco_total' => round($item->tco_total, 2),
                    'cantidad_reparaciones' => $item->cantidad_reparaciones,
                    'costo_promedio_reparacion' => round($item->costo_promedio_reparacion, 2),
                ];
            });

            // Identificar "activos vampiro" (top 10 más costosos)
            $activosVampiro = $tcosFormateados->take(10);

            // === RESUMEN GENERAL ===
            $resumen = [
                // Desviación Presupuestaria
                'desviacion_presupuestaria' => [
                    'total_mantenimientos_analizados' => $totalMantenimientos,
                    'mantenimientos_subestimados' => $subestimados,
                    'porcentaje_subestimacion' => $porcentajeSubestimacion . '%',
                    'desviacion_promedio_monto' => round($desviacionPromedioMonto, 2),
                    'desviacion_promedio_porcentaje' => round($desviacionPromedioPorcentaje, 2) . '%',
                    'alerta_planificacion' => $porcentajeSubestimacion > 50
                        ? 'CRÍTICO: Más del 50% de los mantenimientos están subestimados'
                        : ($porcentajeSubestimacion > 30
                            ? 'ADVERTENCIA: Revisar proceso de estimación de costos'
                            : 'NORMAL: Estimaciones dentro del rango aceptable'),
                ],
                // TCO
                'tco_general' => [
                    'total_activos_analizados' => $tcosPorActivo->count(),
                    'tco_promedio_por_activo' => round($tcosPorActivo->avg('tco_total'), 2),
                    'tco_maximo' => round($tcosPorActivo->max('tco_total'), 2),
                    'activos_vampiro_detectados' => $activosVampiro->count(),
                    'recomendacion' => $activosVampiro->count() > 0
                        ? 'Revisar activos con TCO elevado para evaluar reemplazo'
                        : 'No se detectaron activos con TCO crítico',
                ],
            ];

            return response()->json([
                'resumen' => $resumen,
                'desviacion_presupuestaria' => [
                    'detalle' => $desviaciones,
                ],
                'tco_por_activo' => [
                    'todos' => $tcosPorActivo,
                    'activos_vampiro' => $activosVampiro,
                ],
            ]);

        } catch (\Exception $e) {
            Log::error('Error en análisis financiero: ' . $e->getMessage());
            return response()->json([
                'error' => 'Error al generar análisis financiero',
                'mensaje' => $e->getMessage()
            ], 500);
        }
    }
}

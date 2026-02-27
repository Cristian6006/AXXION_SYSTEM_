<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Renta;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;

class ReporteRentaController extends Controller
{
    // REPORTE: Tablero Principal
    public function resumenGeneral() 
    {
        try {
        // Calcular rentas activas
        $rentasActivas = Renta::where('estado_renta', 'EnCurso')->count();
        // Calcular rentas atrasadas
        $rentasAtrasadas = Renta::where('estado_renta', 'EnCurso')
        ->whereDate('fecha_fin_prevista', '<', Carbon::today())
        ->count();
        // Dinero total en garantía retenido
        $garantiasRetenidas = Renta::where('estado_renta', 'EnCurso')
        ->sum('deposito_garantia');
        
        return response()->json([
            'total_rentas_activas' => $rentasActivas,
            'alertas_atrasadas' => $rentasAtrasadas,
            'dinero_en_garantias' => $garantiasRetenidas,
        ]);
        } catch (\Exception $e) {
            return response()->json([
                'status' => 'error',
                'message' => 'Ocurrió un error en el servidor',
                'error_detail' => $e->getMessage(),
                'file' => $e->getFile(),
                'line' => $e->getLine()
            ], 500);
        }
    }
    // REPORTE: Operativo 
    public function obtenerRentasAtrasadas()
    {
        $atrasadas = Renta::where('estado_renta', 'EnCurso')
        ->whereDate('fecha_fin_prevista', '<', Carbon::today())
        ->with(['cliente:id,nombre,telefono_principal,correo_electronico'])
        ->get()
        ->map(function ($renta) {
            return [
                'folio' => $renta->id,
                'cliente' => $renta->cliente->nombre,
                'contacto' => $renta->cliente->telefono_principal,
                'fecha_debio_entregar'=> $renta->fecha_fin_prevista->format('d/m/Y'),
                'dias_retraso' => intval(abs(Carbon::now()->diffInDays($renta->fecha_fin_prevista))),
                'monto_pendiente' => $renta->monto_total_renta
            ];
        });
        return response()->json($atrasadas);
    }
    // REPORTE: Financiero - Ingresos por Mes
    public function ingresosPorMes()
    {
        try {
        // Usamos selectRaw para consultas de agregación
        $ingresos = Renta::selectRaw('
        YEAR(fecha_inicio) as año,
        MONTH(fecha_inicio) as mes,
        SUM(monto_total_renta) as total
        ')
        ->groupBy(DB::raw('YEAR(fecha_inicio)'), DB::raw('MONTH(fecha_inicio)'))
        ->orderBy(DB::raw('YEAR(fecha_inicio)'), 'desc')
        ->orderBy(DB::raw('MONTH(fecha_inicio)'), 'desc')
        ->limit(12)
        ->get();
        // Mapeamos para que el frontend
        $datosGrafico = $ingresos->map(function ($item) {
            return [
                'etiqueta' => $item->mes . '/' . $item->año,
                'valor' => (float)$item->total
            ];
        });
        return response()->json($datosGrafico);
        } catch (\Exception $e) {
            return response()->json([
                'status' => 'error',
                'message' => 'Ocurrió un error en el servidor',
                'error_detail' => $e->getMessage(),
                'file' => $e->getFile(),
                'line' => $e->getLine()
            ], 500);
        }
    }
    // REPORTE: Activos - Inventario más utilizado
    public function topEquiposRentados()
    {
        $ranking = DB::table('renta_inventario_item')
            ->join('inventario_item', 'renta_inventario_item.inventario_item_id', '=', 'inventario_item.id')
            ->join('producto', 'inventario_item.producto_id', '=', 'producto.id') 
            ->select(
                'producto.nombre', // Nombre legible del producto
                DB::raw('count(*) as total_rentas'), // Cuántas veces se ha alquilado
                DB::raw('SUM(renta_inventario_item.precio_renta_item) as ingresos_generados') // Cuánto dinero ha traído
            )
            ->groupBy('producto.id', 'producto.nombre')
            ->orderByDesc('total_rentas')
            ->limit(10)
            ->get();

        return response()->json($ranking);
    }
    // REPORTE: Disponibilidad del Inventario
    public function estadoInventario()
    {
        // Esto cuenta cuántos ítems hay en cada estado: Disponible, Rentado, etc.
        $estados = DB::table('inventario_item')
            ->select('estado_item', DB::raw('count(*) as total'))
            ->groupBy('estado_item')
            ->get();

        // Formateamos para un gráfico de pastel (Pie Chart)
        $data = $estados->map(function($item) {
            return [
                'estado' => $item->estado_item,
                'cantidad' => $item->total
            ];
        });

        return response()->json($data);
    }
    // REPORTE: ROI (Retorno de Inversión) por Ítem Específico
    public function roiPorEquipo()
    {
        $analisis = DB::table('inventario_item')
            ->join('renta_inventario_item', 'inventario_item.id', '=', 'renta_inventario_item.inventario_item_id')
            ->select(
                'inventario_item.numero_serie',
                'inventario_item.costo_adquisicion',
                DB::raw('SUM(renta_inventario_item.precio_renta_item) as total_ingresos')
            )
            //  En la base de datos, ignoramos costos 0 o negativos para no traer basura
            ->where('inventario_item.costo_adquisicion', '>', 0) 
            ->groupBy('inventario_item.id', 'inventario_item.numero_serie', 'inventario_item.costo_adquisicion')
            ->get()
            ->map(function ($item) {
                // Convertimos a float para asegurar operaciones matemáticas
                $costo = (float)$item->costo_adquisicion;
                $ingresos = (float)$item->total_ingresos;
                // VALIDACIÓN DE SEGURIDAD (Por si acaso se coló un 0.0001 o similar)
                if ($costo <= 0) {
                    $roi = 0;
                } else {
                    $roi = ($ingresos / $costo) * 100;
                }
                $gananciaNeta = $ingresos - $costo;
                return [
                    'serie' => $item->numero_serie,
                    'costo' => $costo,
                    'ingreso_acumulado' => $ingresos,
                    'es_rentable' => $gananciaNeta > 0,
                    'roi_porcentaje' => round($roi, 2) . '%'
                ];
            });
        return response()->json($analisis);
    }
}
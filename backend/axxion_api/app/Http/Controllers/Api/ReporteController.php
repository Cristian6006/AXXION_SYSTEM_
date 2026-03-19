<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Usuario;
use App\Models\Mantenimiento;
use App\Models\Renta;
use App\Models\Categoria;
use App\Models\Producto;
use Illuminate\Http\Request;

class ReporteController extends Controller
{
    /**
     * Genera un reporte simple del inventario (lista de productos).
     */
    public function inventario()
    {
        try {
            $productos = Producto::all();
            return response()->json($productos);
        } catch (\Exception $e) {
            return response()->json(['error' => 'Error al obtener los productos: ' . $e->getMessage()], 500);
        }
    }


    /**
     * Genera un reporte de usuarios y sus roles.
     */
    public function usuarios()
    {
        $usuarios = Usuario::with('roles')->get();
        return response()->json([
            'data' => $usuarios,
            'status' => 200
        ]);
    }

    /**
     * Genera un reporte de rentas con clientes y equipos.
     */
    public function rentas()
    {
        $rentas = Renta::with(['cliente','equipos'])->get();
        return response()->json([
            'data' => $rentas,
            'status' => 200
        ]);
    }

    /**
     * Calcula métricas generales del inventario.
     * 
     * ANALOGÍA: Esta función es como un contador rápido que hace un inventario visual 
     * para decirte cuántos equipos tienes y cuánto valen en total, sin entrar en detalles de cada uno.
     */
    public function metrics()
{
    $totalItems = Producto::count();
    $valorTotal = Producto::sum('valor_actual');
    $equiposDisponibles = Producto::where('condicion', 'Excelente')->count();
    
    return response()->json([
        'items_totales' => $totalItems,
        'valor_total' => $valorTotal,
        'equipos_disponibles' => $equiposDisponibles,
    ]);
}

    /**
     * Calcula métricas financieras de las rentas.
     */
    public function metricsAlq()
{
    $totalItems = Renta::count();
    $valorTotal = Renta::sum('monto_total_renta');
    return response()->json([
        'items_totales' => $totalItems,
        'valor_total' => $valorTotal
    ]);
}
}
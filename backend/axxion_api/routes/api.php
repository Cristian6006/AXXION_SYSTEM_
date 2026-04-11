<?php

use App\Http\Controllers\Api\categoriaController;
use App\Http\Controllers\Api\clienteController;
use App\Http\Controllers\Api\RentaController;
use App\Http\Controllers\Api\subcategoriaController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\UsuarioController;
use App\Http\Controllers\Api\ProductoController;
use App\Http\Controllers\Api\rolController;
use App\Http\Controllers\Api\cotizacionController;
use App\Http\Controllers\Api\detalleCotizacionController;
use App\Http\Controllers\Api\devolucionController;
use App\Http\Controllers\Api\direccionController;
use App\Http\Controllers\Api\entregaController;
use App\Http\Controllers\Api\MantenimientoController;
use App\Http\Controllers\Api\ReporteController;
use App\Http\Controllers\Api\ReporteRentaController;
use App\Http\Controllers\Api\AlertaController;
use App\Http\Controllers\Api\InventarioItemController;
use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\ReporteMantController;
use App\Http\Controllers\Api\ReportUserController;
use App\Http\Controllers\Api\SolicitudController;


/**
 * ANALOGÍA: Este archivo actúa como el 'Mapa de Rutas' o el 'Directorio Telefónico' de la API.
 * Define qué URLs están disponibles y a qué controlador (operador) se debe dirigir cada llamada.
 */

// ============================================
// RUTAS PÚBLICAS (sin autenticación)
// ============================================
// Permite a los usuarios iniciar sesión y obtener un token.
Route::prefix('auth')->group(function () {
    Route::post('/login', [AuthController::class, 'login']);
    Route::post('/refresh', [AuthController::class, 'refresh']);
});
Route::post('/usuarios', [UsuarioController::class, 'store']); // Registro de usuarios
// Permite registrar un nuevo usuario inicial (abierto temporalmente).


// ============================================
// RUTAS PROTEGIDAS (requieren autenticación)
// ============================================

// Rutas protegidass
Route::middleware(['jwt'])->group(function () {
    Route::prefix('auth')->group(function () {
        Route::post('/logout', [AuthController::class, 'logout']);
        Route::get('/me', [AuthController::class, 'me']);
    });
    // ============================================
    // GESTIÓN DE USUARIOS (requiere autenticación)
    // ============================================
    Route::get('/usuarios', [UsuarioController::class, 'index']);
    Route::get('/usuario/{id}', [UsuarioController::class, 'show']);
    Route::put('/usuario/{id}', [UsuarioController::class, 'update']);
    Route::patch('/usuario/{id}', [UsuarioController::class, 'update']);

    // Solo administradores pueden eliminar usuarios
    // Middleware 'check.role:ADMIN' asegura que solo usuarios con rol ADMIN accedan.
    Route::delete('/usuario/{id}', [UsuarioController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // PRODUCTOS (requiere autenticación)
    // ============================================
    Route::get('/producto', [ProductoController::class, 'index']);
    Route::get('/producto/{id}', [ProductoController::class, 'show']);

    // Solo usuarios autenticados pueden crear/modificar productos
    Route::post('/producto', [ProductoController::class, 'store']);
    Route::put('/producto/{id}', [ProductoController::class, 'update']);
    Route::patch('/producto/{id}', [ProductoController::class, 'updatePartial']);

    // Solo administradores pueden eliminar productos
    Route::delete('/producto/{id}', [ProductoController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // ROLES (solo administradores)
    // Gestión de los roles de usuario (ej. Admin, Vendedor).
    // ============================================
    Route::get('/rol', [rolController::class, 'index']);
    Route::post('/rol', [rolController::class, 'store'])->middleware('check.role:ADMIN');
    Route::get('/rol/{id}', [rolController::class, 'show'])->middleware('check.role:ADMIN');
    Route::delete('/rol/{id}', [rolController::class, 'destroy'])->middleware('check.role:ADMIN');
    Route::put('/rol/{id}', [rolController::class, 'update'])->middleware('check.role:ADMIN');
    Route::patch('/rol/{id}', [rolController::class, 'updatePartial'])->middleware('check.role:ADMIN');

    // ============================================
    // CATEGORÍAS (requiere autenticación)
    // Gestión de categorías de productos.
    // ============================================
    Route::get('/categoria', [categoriaController::class, 'index']);
    Route::get('/categoria/{id}', [categoriaController::class, 'show']);
    Route::post('/categoria', [categoriaController::class, 'store']);
    Route::put('/categoria/{id}', [categoriaController::class, 'update']);
    Route::patch('/categoria/{id}', [categoriaController::class, 'updatePartial']);

    // Solo administradores pueden eliminar categorías
    Route::delete('/categoria/{id}', [categoriaController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // SUBCATEGORÍAS (requiere autenticación)
    // Gestión de subcategorías para una clasificación más detallada.
    // ============================================
    Route::get('/subcategoria', [subcategoriaController::class, 'index']);
    Route::get('/subcategoria/{id}', [subcategoriaController::class, 'show']);
    Route::post('/subcategoria', [subcategoriaController::class, 'store']);
    Route::put('/subcategoria/{id}', [subcategoriaController::class, 'update']);
    Route::patch('/subcategoria/{id}', [subcategoriaController::class, 'updatePartial']);

    // Solo administradores pueden eliminar subcategorías
    Route::delete('/subcategoria/{id}', [subcategoriaController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // CLIENTES (requiere autenticación)
    // Gestión de la base de datos de clientes.
    // ============================================
    Route::get('/cliente', [clienteController::class, 'index']);
    Route::get('/cliente/{id}', [clienteController::class, 'show']);
    Route::post('/cliente', [clienteController::class, 'store']);
    Route::put('/cliente/{id}', [clienteController::class, 'update']);
    Route::patch('/cliente/{id}', [clienteController::class, 'updatePartial']);

    // Solo administradores pueden eliminar clientes
    Route::delete('/cliente/{id}', [clienteController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // COTIZACIONES (requiere autenticación)
    // Creación y gestión de presupuestos para clientes.
    // ============================================
    Route::get('/cotizacion', [cotizacionController::class, 'index']);
    Route::get('/cotizacion/{id}', [cotizacionController::class, 'show']);
    Route::post('/cotizacion', [cotizacionController::class, 'store']);
    Route::put('/cotizacion/{id}', [cotizacionController::class, 'update']);
    Route::patch('/cotizacion/{id}', [cotizacionController::class, 'updatePartial']);

    // Solo administradores pueden eliminar cotizaciones
    Route::delete('/cotizacion/{id}', [cotizacionController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // DETALLE COTIZACIÓN (requiere autenticación)
    // Gestión de los ítems individuales dentro de una cotización.
    // ============================================
    Route::get('/detalleCotizacion', [detalleCotizacionController::class, 'index']);
    Route::get('/detalleCotizacion/{id}', [detalleCotizacionController::class, 'show']);
    Route::post('/detalleCotizacion', [detalleCotizacionController::class, 'store']);
    Route::put('/detalleCotizacion/{id}', [detalleCotizacionController::class, 'update']);
    Route::patch('/detalleCotizacion/{id}', [detalleCotizacionController::class, 'updatePartial']);

    // Solo administradores pueden eliminar detalles de cotización
    Route::delete('/detalleCotizacion/{id}', [detalleCotizacionController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // DEVOLUCIONES (requiere autenticación)
    // Registro y gestión del retorno de equipos rentados.
    // ============================================
    Route::get('/devolucion', [devolucionController::class, 'index']);
    Route::get('/devolucion/{id}', [devolucionController::class, 'show']);
    Route::post('/devolucion', [devolucionController::class, 'store']);
    Route::put('/devolucion/{id}', [devolucionController::class, 'update']);
    Route::patch('/devolucion/{id}', [devolucionController::class, 'updatePartial']);

    // Solo administradores pueden eliminar devoluciones
    Route::delete('/devolucion/{id}', [devolucionController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // DIRECCIONES (requiere autenticación)
    // Gestión de direcciones de clientes y entregas.
    // ============================================
    Route::get('/direccion', [direccionController::class, 'index']);
    Route::get('/direccion/{id}', [direccionController::class, 'show']);
    Route::post('/direccion', [direccionController::class, 'store']);
    Route::put('/direccion/{id}', [direccionController::class, 'update']);
    Route::patch('/direccion/{id}', [direccionController::class, 'updatePartial']);

    // Solo administradores pueden eliminar direcciones
    Route::delete('/direccion/{id}', [direccionController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // ENTREGAS (requiere autenticación)
    // Gestión logística de envíos de equipos.
    // ============================================
    Route::get('/entrega', [entregaController::class, 'index']);
    Route::get('/entrega/{id}', [entregaController::class, 'show']);
    Route::post('/entrega', [entregaController::class, 'store']);
    Route::put('/entrega/{id}', [entregaController::class, 'update']);
    Route::patch('/entrega/{id}', [entregaController::class, 'updatePartial']);

    // Solo administradores pueden eliminar entregas
    Route::delete('/entrega/{id}', [entregaController::class, 'destroy'])->middleware('check.role:ADMIN');

    // ============================================
    // MANTENIMIENTOS (requiere autenticación)
    // Gestión del ciclo de vida de mantenimiento de los equipos.
    // ============================================
    Route::get('/mantenimiento', [MantenimientoController::class, 'index']);
    Route::get('/mantenimiento/{id}', [MantenimientoController::class, 'show']);
    Route::post('/mantenimiento', [MantenimientoController::class, 'store']);
    Route::put('/mantenimiento/{id}', [MantenimientoController::class, 'update']);
    Route::patch('/mantenimiento/{id}', [MantenimientoController::class, 'updatePartial']);

    // Solo administradores pueden eliminar mantenimientos
    Route::delete('/mantenimiento/{id}', [MantenimientoController::class, 'destroy'])->middleware('check.role:ADMIN');

    // Rutas adicionales para mantenimientos
    Route::get('/mantenimiento/inventario/{inventario_item_id}', [MantenimientoController::class, 'getByInventarioItem']);
    Route::get('/mantenimiento/estado/{estado}', [MantenimientoController::class, 'getByEstado']);
    Route::get('/mantenimiento/tipo/{tipo}', [MantenimientoController::class, 'getByTipo']);

    // ============================================
    // REPORTES (requiere autenticación)
    // Generación de reportes y métricas del sistema.
    // ============================================
    // Reportes Inventario
    Route::prefix('reportes')->group(function () {
        Route::get('/metrics', [ReporteController::class, 'metrics']);
        Route::get('/metricsAlq', [ReporteController::class, 'metricsAlq']);
        // Reportes Alquiler
        Route::get('/resumenGeneral', [ReporteRentaController::class, 'resumenGeneral']);
        Route::get('/obtenerRentasAtrasadas', [ReporteRentaController::class, 'obtenerRentasAtrasadas']);
        Route::get('/ingresosPorMes', [ReporteRentaController::class, 'ingresosPorMes']);
        Route::get('/topEquiposRentados', [ReporteRentaController::class, 'topEquiposRentados']);
        Route::get('/estadoInventario', [ReporteRentaController::class, 'estadoInventario']);
        Route::get('/roiPorEquipo', [ReporteRentaController::class, 'roiPorEquipo']);
        // Reportes Mantenimientos
        Route::get('/reporteEficiencia', [ReporteMantController::class, 'reporteEficiencia']);
        Route::get('/analisisFinanciero', [ReporteMantController::class, 'analisisFinanciero']);
        // Reportes Usuarios
        Route::get('/distribucionPorDepartamento', [ReportUserController::class, 'distribucionPorDepartamento']);
        Route::get('/seguridadYRoles', [ReportUserController::class, 'seguridadYRoles']);
        Route::get('/reporteCompleto', [ReportUserController::class, 'reporteCompleto']);
    });

    // ============================================
    // ALQUILER (requiere autenticación)
    // Gestión principal de contratos de renta.
    // ============================================

    Route::get('/renta', [RentaController::class, 'index']);
    Route::post('/renta', [RentaController::class, 'store']);
    Route::get('/renta/{id}', [RentaController::class, 'show']);
    Route::delete('/renta/{id}', [RentaController::class, 'destroy'])->middleware('check.role:ADMIN');
    Route::put('/renta/{id}', [RentaController::class, 'update']);
    Route::patch('/renta/{id}', [RentaController::class, 'updatePartial']);

    // ============================================
    // INVENTARIO ITEMS (requiere autenticación)
    // Gestión de unidades físicas individuales de productos.
    // ============================================
    Route::get('/inventario_item', [InventarioItemController::class, 'index']);
    Route::get('/inventario_item/{id}', [InventarioItemController::class, 'show']);
    Route::post('/inventario_item', [InventarioItemController::class, 'store']);
    Route::put('/inventario_item/{id}', [InventarioItemController::class, 'update']);
    Route::patch('/inventario_item/{id}', [InventarioItemController::class, 'updatePartial']);

    // Solo administradores pueden eliminar items de inventario
    Route::delete('/inventario_item/{id}', [InventarioItemController::class, 'destroy'])->middleware('check.role:ADMIN');

    // Rutas adicionales para inventario items
    Route::get('/inventario_item/producto/{producto_id}', [InventarioItemController::class, 'getByProducto']);
    Route::get('/inventario_item/estado/{estado}', [InventarioItemController::class, 'getByEstado']);
    Route::get('/inventario_item_with_rental_status', [InventarioItemController::class, 'getWithRentalStatus']);

    // ============================================
    // ALERTAS (requiere autenticación)
    // Sistema de notificaciones sobre estados críticos (mantenimientos, rentas vencidas).
    // ============================================
    Route::get('/alertas', [AlertaController::class, 'index']);
    // ============================================
    // SOLICITUDES (requiere autenticación)
    // Gestión de solicitudes iniciales de clientes.
    // ============================================
    Route::get('/solicitud', [SolicitudController::class, 'index']);
    Route::get('/solicitud/{id}', [SolicitudController::class, 'show']);
    Route::post('/solicitud', [SolicitudController::class, 'store']);
    Route::post('/solicitud/{id}/convert-to-rental', [SolicitudController::class, 'convertToRental']);


});






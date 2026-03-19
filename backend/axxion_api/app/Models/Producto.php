<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa un Producto en el catálogo.
 * 
 * Define las características generales de un tipo de equipo (ej. 'Taladro Percutor Modelo X').
 * No representa una unidad física específica (ver InventarioItem).
 */
class Producto extends Model
{
    use HasFactory;

    protected $table = 'producto';

    protected $fillable = [
        'nombre',
        'descripcion',
        'marca',
        'modelo',
        'precio_referencia_renta',
        'sku',
        'numero_serie',
        'categoria',
        'especificaciones',
        'precio_alquiler_dia',
        'precio_alquiler_semanal',
        'precio_alquiler_mensual',
        'precio_compra',
        'valor_actual',
        'fecha_compra',
        'condicion',
        'ubicacion',
        'notas',
        'estado',
    ];

    protected $casts = [
        'especificaciones' => 'array',
        'precio_referencia_renta' => 'decimal:2',
        'precio_alquiler_dia' => 'decimal:2',
        'precio_alquiler_semanal' => 'decimal:2',
        'precio_alquiler_mensual' => 'decimal:2',
        'precio_compra' => 'decimal:2',
        'valor_actual' => 'decimal:2',
        'fecha_compra' => 'date',
    ];

    /**
     * Relación: Un producto puede pertenecer a varias subcategorías.
     */
    public function subcategorias()
    {
        return $this->belongsToMany(Subcategoria::class, 'producto_subcategoria');
    }

    /**
     * Relación: Un producto tiene muchas unidades físicas (items) en inventario.
     */
    public function inventarioItems()
    {
        return $this->hasMany(InventarioItem::class);
    }

    /**
     * Relación: Un producto puede ser solicitado muchas veces.
     */
    public function solicitudes()
    {
        return $this->belongsToMany(Solicitud::class, 'solicitud_producto');
    }

    /**
     * Relación: Un producto puede aparecer en muchos detalles de cotización.
     */
    public function detallesCotizacion()
    {
        return $this->hasMany(DetalleCotizacion::class);
    }
}

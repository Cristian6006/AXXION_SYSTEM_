<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa un Detalle de Cotización.
 * 
 * Corresponde a cada línea o ítem dentro de una cotización (ej. '1 Taladro', '2 Escaleras').
 */
class DetalleCotizacion extends Model
{
    use HasFactory;

    protected $table = 'detalle_cotizacion';

    protected $fillable = [
        'cotizacion_id',
        'producto_id',
        'descripcion_item',
        'cantidad',
        'precio_unitario',
        'descuento_porcentaje',
        'impuestos_aplicables',
        'notas',
    ];

    /**
     * Relación: Este detalle pertenece a una cotización específica.
     */
    public function cotizacion()
    {
        return $this->belongsTo(Cotizacion::class);
    }

    /**
     * Relación: Este detalle hace referencia a un producto del inventario.
     */
    public function producto()
    {
        return $this->belongsTo(Producto::class);
    }
}

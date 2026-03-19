<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa una Solicitud.
 * 
 * Es el primer contacto donde un cliente expresa interés por ciertos productos.
 */
class Solicitud extends Model
{
    use HasFactory;

    protected $table = 'solicitud';

    protected $fillable = [
        'cliente_id',
        'fecha_solicitud',
        'nombre_producto_alternativo',
        'cantidad_solicitada',
        'descripcion_necesidad',
        'estado_solicitud',
    ];

    /**
     * Relación: La solicitud es realizada por un cliente.
     */
    public function cliente()
    {
        return $this->belongsTo(Cliente::class);
    }

    /**
     * Relación: En la solicitud se listan los productos de interés.
     */
    public function productos()
    {
        return $this->belongsToMany(Producto::class, 'solicitud_producto');
    }

    /**
     * Relación: Una solicitud puede generar varias cotizaciones (versiones).
     */
    public function cotizaciones()
    {
        return $this->hasMany(Cotizacion::class);
    }
}

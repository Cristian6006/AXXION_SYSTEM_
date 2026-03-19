<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa un Ítem de Inventario.
 * 
 * Es una instancia física y única de un producto (ej. 'Taladro #5' con serie 'XYZ-123').
 */
class InventarioItem extends Model
{
    use HasFactory;

    protected $table = 'inventario_item';

    protected $fillable = [
        'producto_id',
        'numero_serie',
        'estado_item',
        'fecha_adquisicion',
        'costo_adquisicion',
        'ubicacion_fisica',
        'notas',
    ];

    /**
     * Relación: Este ítem es una instancia de un producto del catálogo.
     */
    public function producto()
    {
        return $this->belongsTo(Producto::class);
    }

    /**
     * Relación: Este ítem puede tener un historial de mantenimientos.
     */
    public function mantenimientos()
    {
        return $this->hasMany(Mantenimiento::class);
    }

    /**
     * Relación: Este ítem puede haber sido rentado muchas veces.
     */
    public function rentas()
    {
        return $this->belongsToMany(Renta::class, 'renta_inventario_item')
            ->withPivot('precio_renta_item', 'condicion_salida', 'condicion_regreso', 'notas');
    }
}

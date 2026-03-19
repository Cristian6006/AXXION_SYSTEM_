<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa un registro de Mantenimiento.
 * 
 * Documenta las reparaciones o servicios realizados a un ítem del inventario.
 */
class Mantenimiento extends Model
{
    use HasFactory;

    protected $table = 'mantenimiento';

    protected $fillable = [
        'inventario_item_id',
        'fecha_inicio',
        'fecha_fin_prevista',
        'fecha_fin_real',
        'tipo_mantenimiento',
        'descripcion_problema',
        'descripcion_trabajo_realizado',
        'costo_estimado',
        'costo_real',
        'estado_mantenimiento',
        'responsable',
    ];

    protected $casts = [
        'fecha_inicio' => 'datetime',
        'fecha_fin_prevista' => 'date',
        'fecha_fin_real' => 'date',
        'costo_estimado' => 'decimal:2',
        'costo_real' => 'decimal:2',
    ];

    /**
     * Relación: El mantenimiento se realiza sobre un ítem específico del inventario.
     */
    public function inventarioItem()
    {
        return $this->belongsTo(InventarioItem::class);
    }
}

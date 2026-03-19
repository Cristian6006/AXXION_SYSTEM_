<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa una Renta.
 * 
 * Es el contrato o acuerdo principal donde se asignan equipos a un cliente por un tiempo determinado.
 */
class Renta extends Model
{
    use HasFactory;

    protected $table = 'renta';

    protected $fillable = [
        'cliente_id',
        'cotizacion_id',
        'fecha_inicio',
        'fecha_fin_prevista',
        'fecha_devolucion_real',
        'estado_renta',
        'monto_total_renta',
        'deposito_garantia',
        'notas',
    ];

    protected $casts = [
        'fecha_inicio' => 'datetime',
        'fecha_fin_prevista' => 'date',
        'fecha_devolucion_real' => 'date',
        'monto_total_renta' => 'decimal:2',
        'deposito_garantia' => 'decimal:2',
    ];

    /**
     * Relación: La renta pertenece a un cliente.
     */
    public function cliente()
    {
        return $this->belongsTo(Cliente::class);
    }

    /**
     * Relación: La renta puede originarse de una cotización aprobada.
     */
    public function cotizacion()
    {
        return $this->belongsTo(Cotizacion::class);
    }

    /**
     * Relación: Una renta incluye múltiples ítems de inventario específicos.
     */
    public function inventarioItems()
    {
        return $this->belongsToMany(InventarioItem::class, 'renta_inventario_item')
            ->withPivot('precio_renta_item', 'condicion_salida', 'condicion_regreso', 'notas');
    }

    /**
     * Relación: Una renta puede tener una entrega asociada.
     */
    public function entrega()
    {
        return $this->hasOne(Entrega::class);
    }

    /**
     * Relación: Una renta finaliza con una devolución.
     */
    public function devolucion()
    {
        return $this->hasOne(Devolucion::class);
    }
}

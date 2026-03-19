<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa una Entrega.
 * 
 * Gestiona la logística de envío de equipos rentados a una dirección específica.
 */
class Entrega extends Model
{
    use HasFactory;

    protected $table = 'entrega';

    protected $fillable = [
        'renta_id',
        'direccion_id',
        'fecha_envio',
        'compania_envio',
        'numero_guia',
        'estado_entrega',
        'notas',
    ];

    /**
     * Relación: La entrega corresponde a una renta específica.
     */
    public function renta()
    {
        return $this->belongsTo(Renta::class);
    }

    /**
     * Relación: La entrega se realiza en una dirección específica.
     */
    public function direccion()
    {
        return $this->belongsTo(Direccion::class);
    }
}

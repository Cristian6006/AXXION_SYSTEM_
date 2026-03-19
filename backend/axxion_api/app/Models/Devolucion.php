<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa una Devolución.
 * 
 * Registra el evento de retorno de los equipos rentados al finalizar el contrato.
 */
class Devolucion extends Model
{
    use HasFactory;

    protected $table = 'devolucion';

    protected $fillable = [
        'renta_id',
        'fecha_devolucion_programada',
        'fecha_devolucion_real',
        'estado_devolucion',
        'persona_recibe',
        'notas_generales',
    ];

    /**
     * Relación: La devolución está asociada a una renta específica.
     */
    public function renta()
    {
        return $this->belongsTo(Renta::class);
    }
}

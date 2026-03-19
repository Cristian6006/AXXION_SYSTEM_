<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa una Cotización.
 * 
 * Es el documento formal donde se valoran los productos solicitados antes de concretar una renta.
 */
class Cotizacion extends Model
{
    use HasFactory;

    protected $table = 'cotizacion';

    protected $fillable = [
        'cliente_id',
        'solicitud_id',
        'fecha_cotizacion',
        'fecha_validez',
        'monto_total',
        'estado_cotizacion',
        'terminos_condiciones',
        'notas_internas',
    ];

    /**
     * Relación: La cotización pertenece a un cliente específico.
     */
    public function cliente()
    {
        return $this->belongsTo(Cliente::class);
    }

    /**
     * Relación: La cotización puede derivar de una solicitud previa.
     */
    public function solicitud()
    {
        return $this->belongsTo(Solicitud::class);
    }

    /**
     * Relación: Una cotización tiene múltiples detalles (líneas de productos).
     */
    public function detalles()
    {
        return $this->hasMany(DetalleCotizacion::class);
    }

    /**
     * Relación: Una cotización aprobada puede convertirse en una renta.
     */
    public function renta()
    {
        return $this->hasOne(Renta::class);
    }
    
}

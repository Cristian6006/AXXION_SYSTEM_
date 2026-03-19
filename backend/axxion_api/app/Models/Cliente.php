<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa a un Cliente.
 * 
 * Almacena la información personal o empresarial de quienes solicitan rentas o servicios.
 */
class Cliente extends Model
{
    use HasFactory;

    protected $table = 'cliente';

    protected $fillable = [
        'nombre',
        'nombre2',
        'apellido1',
        'apellido2',
        'rfc',
        'telefono_principal',
        'correo_electronico',
        'tipo_cliente',
        'estado_cliente',
    ];

    /**
     * Relación: Un cliente puede tener múltiples direcciones registradas.
     */
    public function direcciones()
    {
        return $this->belongsToMany(Direccion::class, 'cliente_direccion')->withPivot('es_principal');
    }

    /**
     * Relación: Un cliente puede realizar muchas solicitudes.
     */
    public function solicitudes()
    {
        return $this->hasMany(Solicitud::class);
    }

    /**
     * Relación: Un cliente puede tener muchas cotizaciones asociadas.
     */
    public function cotizaciones()
    {
        return $this->hasMany(Cotizacion::class);
    }

    /**
     * Relación: Un cliente puede tener múltiples rentas históricas o activas.
     */
    public function rentas()
    {
        return $this->hasMany(Renta::class);
    }
}

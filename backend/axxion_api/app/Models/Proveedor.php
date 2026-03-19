<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa un Proveedor.
 * 
 * Entidad externa que suministra productos o servicios a la empresa.
 */
class Proveedor extends Model
{
    use HasFactory;

    protected $table = 'proveedor';

    protected $fillable = [
        'nombre_empresa',
        'rfc',
        'nombre_contacto',
        'telefono_contacto',
        'correo_contacto',
        'estado_proveedor',
    ];

    /**
     * Relación: Un proveedor puede tener múltiples direcciones (sucursales, almacenes).
     */
    public function direcciones()
    {
        return $this->belongsToMany(Direccion::class, 'proveedor_direccion')->withPivot('es_principal');
    }
}

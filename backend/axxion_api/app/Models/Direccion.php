<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa una Dirección física.
 * 
 * Almacena detalles de ubicación para clientes, proveedores y entregas.
 */
class Direccion extends Model
{
    use HasFactory;

    protected $table = 'direccion';

    protected $fillable = [
        'calle',
        'numero_exterior',
        'numero_interior',
        'colonia',
        'ciudad',
        'estado_provincia',
        'codigo_postal',
        'pais',
        'referencias',
    ];

    /**
     * Relación: Una dirección puede pertenecer a varios clientes (ej. oficinas compartidas).
     */
    public function clientes()
    {
        return $this->belongsToMany(Cliente::class, 'cliente_direccion');
    }

    /**
     * Relación: Una dirección puede pertenecer a varios proveedores.
     */
    public function proveedores()
    {
        return $this->belongsToMany(Proveedor::class, 'proveedor_direccion');
    }

    /**
     * Relación: Una dirección puede ser el destino de múltiples entregas.
     */
    public function entregas()
    {
        return $this->hasMany(Entrega::class);
    }
}

<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa un Rol de Usuario.
 * 
 * Define los permisos y niveles de acceso (ej. 'Administrador', 'Vendedor').
 */
class Rol extends Model
{
    use HasFactory;

    protected $table = 'rol';

    protected $fillable = [
        'codigo',
        'nombre',
        'descripcion',
    ];

    /**
     * Relación: Un rol puede ser asignado a múltiples usuarios.
     */
    public function usuarios()
    {
        return $this->belongsToMany(Usuario::class, 'usuario_rol');
    }
}

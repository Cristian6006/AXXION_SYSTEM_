<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Tymon\JWTAuth\Contracts\JWTSubject;



/**
 * Modelo que representa un Usuario del sistema.
 * 
 * Gestiona el acceso, autenticación (JWT) y perfil de los empleados o administradores.
 */
class Usuario extends Authenticatable implements JWTSubject
{
    use HasFactory;

    public function getJWTIdentifier()
    {
        return $this->getKey();
    }

    public function getJWTCustomClaims()
    {
        return [
            'email' => $this->email,
            'roles' => $this->roles()->pluck('codigo')->toArray()
        ];
    }
 
    protected $table = 'usuario';

    protected $fillable = [
        'id',
        'nombre_usuario',
        'nombre',
        'nombre2',
        'apellido1',
        'apellido2',
        'password_hash',
        'email',
        'telefono',
        'departamento',
        'estado',
    ];

    protected $hidden = [
        'password_hash',
    ];

    protected $casts = [
        'estado' => 'string',
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
    ];

    public function getAuthPassword()
    {
        return $this->password_hash;
    }

    /**
     * Relación: Un usuario puede tener múltiples roles asignados.
     */
    public function roles()
    {
        return $this->belongsToMany(Rol::class, 'usuario_rol');
    }

    public function refreshTokens()
{
    return $this->hasMany(RefreshToken::class);
}
}



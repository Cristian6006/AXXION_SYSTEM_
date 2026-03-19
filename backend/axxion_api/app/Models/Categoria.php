<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa una Categoría de productos.
 * 
 * Una categoría agrupa productos similares (ej. 'Maquinaria Pesada', 'Herramientas Manuales').
 */
class Categoria extends Model
{
    use HasFactory;

    protected $table = 'categoria';

    protected $fillable = [
        'nombre',
        'descripcion',
        'tipo_categoria',
    ];

    /**
     * Relación: Una categoría puede tener muchas subcategorías.
     */
    public function subcategorias()
    {
        return $this->belongsToMany(Subcategoria::class, 'categoria_subcategoria');
    }
}

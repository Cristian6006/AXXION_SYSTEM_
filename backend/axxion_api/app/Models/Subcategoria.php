<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

/**
 * Modelo que representa una Subcategoría.
 * 
 * Permite una clasificación más granular de los productos dentro de una categoría principal.
 */
class Subcategoria extends Model
{
    use HasFactory;

    protected $table = 'subcategoria';

    protected $fillable = [
        'nombre',
        'descripcion',
    ];

    /**
     * Relación: Una subcategoría pertenece a una o varias categorías.
     */
    public function categorias()
    {
        return $this->belongsToMany(Categoria::class, 'categoria_subcategoria');
    }

    /**
     * Relación: Una subcategoría agrupa múltiples productos.
     */
    public function productos()
    {
        return $this->belongsToMany(Producto::class, 'producto_subcategoria');
    }
}

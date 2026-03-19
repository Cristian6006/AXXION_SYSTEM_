<?php

namespace Tests\Feature;

use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class UsuarioTest extends TestCase
{
    use RefreshDatabase;

    /**
     * Test creating a new user.
     */
    public function test_create_usuario()
    {
        $data = [
            "nombre_usuario" => "CRISTIAN",
            "nombre" => "Cristian",
            "nombre2" => "Camilo",
            "apellido1" => "Cifuentes",
            "apellido2" => "Gaona",
            "email" => "c@example.com",
            "telefono" => "22161510",
            "departamento" => "Ventas",
            "estado" => "activo",
            "password" => "12345678",
            "roles" => [1]
        ];

        $response = $this->postJson('/api/usuarios', $data);

        $response->assertStatus(201)
                 ->assertJson([
                     'message' => 'Usuario creado correctamente',
                 ]);
    }
}

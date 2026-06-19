# AXXION SYSTEM — Backend API Context

> **Proyecto:** Sistema de gestión de inventario, alquileres y mantenimientos  
> **Stack:** Laravel 11 + PHP 8.2 + MySQL + JWT Auth  
> **Frontend:** Vue 3 (consume esta API)  
> **Servidor:** Apache / PHP Artisan Serve

---

## 1. Estructura del Proyecto

```
axxion_api/
├── app/
│   ├── Http/
│   │   ├── Controllers/
│   │   │   ├── AuthController.php
│   │   │   ├── UsuarioController.php
│   │   │   ├── ProductoController.php
│   │   │   ├── CategoriaController.php
│   │   │   ├── SubcategoriaController.php
│   │   │   ├── ClienteController.php
│   │   │   ├── RentaController.php
│   │   │   ├── CotizacionController.php
│   │   │   ├── DetalleCotizacionController.php
│   │   │   ├── InventarioItemController.php
│   │   │   ├── MantenimientoController.php
│   │   │   ├── DevolucionController.php
│   │   │   ├── EntregaController.php
│   │   │   ├── DireccionController.php
│   │   │   ├── RolController.php
│   │   │   ├── RoleUserController.php
│   │   │   ├── MenuController.php
│   │   │   ├── UnidadOperativaController.php
│   │   │   └── OpcionesMenuController.php
│   │   ├── Middleware/
│   │   │   ├── JwtMiddleware.php
│   │   │   └── CorsMiddleware.php
│   │   └── Requests/         (form requests de validación)
│   ├── Models/
│   │   ├── Usuario.php
│   │   ├── Producto.php
│   │   ├── Categoria.php
│   │   ├── Subcategoria.php
│   │   ├── Cliente.php
│   │   ├── Renta.php
│   │   ├── Cotizacion.php
│   │   ├── DetalleCotizacion.php
│   │   ├── InventarioItem.php
│   │   ├── Mantenimiento.php
│   │   ├── Devolucion.php
│   │   ├── Entrega.php
│   │   ├── Direccion.php
│   │   ├── Rol.php
│   │   ├── RoleUser.php
│   │   ├── Menu.php
│   │   ├── UnidadOperativa.php
│   │   └── OpcionesMenu.php
│   ├── Exceptions/
│   │   └── Handler.php
│   └── Providers/
│       └── AppServiceProvider.php
├── bootstrap/
├── config/
│   ├── app.php
│   ├── auth.php
│   ├── database.php
│   ├── cors.php
│   ├── jwt.php
│   └── ...
├── database/
│   ├── migrations/
│   │   └── 2025_11_24_181905_create_refresh_tokens_table.php
│   └── seeders/          (para datos de prueba)
├── routes/
│   └── api.php           (TODAS las rutas de la API)
├── storage/
├── tests/
├── .env                  (configuración de BD, JWT, etc.)
├── artisan
├── composer.json
└── README_API.md         (documentación de uso de la API)
```

---

## 2. Stack Tecnológico

| Tecnología       | Versión    | Propósito                          |
|------------------|------------|------------------------------------|
| PHP              | ^8.2       | Lenguaje backend                   |
| Laravel          | ^11.31     | Framework PHP                      |
| MySQL            | —          | Base de datos relacional           |
| JWT Auth         | tymon/jwt-auth ^2.2 | Autenticación basada en tokens JWT |
| firebase/php-jwt | ^7.0       | Librería JWT adicional             |
| lcobucci/jwt     | ^4.0       | Implementación JWT                 |
| Scribe           | ^5.9       | Generación de documentación API    |
| Predis           | ^3.4       | Cliente Redis (sesiones/cache)     |
| Guzzle           | ^7.10      | Cliente HTTP                       |
| PHPUnit          | ^11.0.1    | Testing                            |

---

## 3. Arquitectura General

```
[Cliente (Vue Frontend)] 
        ↓ HTTP (JSON)
[API Laravel:8000/api/*]
        ↓
[Middleware JWT → Controladores → Modelos → MySQL]
        ↓
[Respuesta JSON]
```

### Flujo de autenticación

1. Cliente envía `POST /api/auth/login` con email + password.
2. `AuthController@login` valida credenciales contra `usuarios` + `roles`.
3. Devuelve `{ token, user }` con datos del usuario y sus roles.
4. Cliente almacena token y lo envía en header `Authorization: Bearer <token>`.
5. `JwtMiddleware` verifica token en cada request protegido.
6. `AuthController@logout` invalida token.
7. `AuthController@refresh` renueva token vía cookie.

### Patrón de controladores

Cada controlador sigue un patrón RESTful consistente:

| Método HTTP | Endpoint               | Acción del Controlador | Descripción          |
|-------------|------------------------|------------------------|----------------------|
| GET         | `/api/recurso`         | `index()`              | Listar todos         |
| POST        | `/api/recurso`         | `store()`              | Crear nuevo          |
| GET         | `/api/recurso/{id}`    | `show()`               | Ver detalle          |
| PUT         | `/api/recurso/{id}`    | `update()`             | Actualizar completo  |
| PATCH       | `/api/recurso/{id}`    | `updatePartial()`      | Actualizar parcial   |
| DELETE      | `/api/recurso/{id}`    | `destroy()`            | Eliminar (solo admin)|

---

## 4. Base de Datos (MySQL)

**Base de datos:** `sistemarenta`  
**Host:** `127.0.0.1:3306` (local) o `100.113.218.94:3306` (remoto)

### Tablas

El esquema de base de datos **no fue creado mediante migraciones de Laravel** (solo existe una migración para `refresh_tokens`). Las tablas fueron creadas manualmente o importadas de un esquema existente.

#### `usuarios` — Usuarios del sistema
| Columna          | Tipo         | Descripción                         |
|------------------|--------------|-------------------------------------|
| id               | int (PK)     | ID único                            |
| nombre_usuario   | string       | Nombre de usuario (login)           |
| nombre           | string       | Primer nombre                       |
| nombre2          | string|null  | Segundo nombre                      |
| apellido1        | string       | Primer apellido                     |
| apellido2        | string|null  | Segundo apellido                    |
| password         | string       | Password hasheado (bcrypt)          |
| email            | string       | Correo electrónico                  |
| telefono         | string|null  | Teléfono                            |
| departamento     | string|null  | Departamento de trabajo             |
| estado           | enum         | `ACTIVO` / `INACTIVO`              |
| created_at       | timestamp    | Fecha de creación                   |
| updated_at       | timestamp    | Fecha de actualización              |

#### `roles` — Roles del sistema
| Columna | Tipo     | Descripción           |
|---------|----------|-----------------------|
| id      | int (PK) | ID único              |
| nombre  | string   | Nombre del rol (ej: ADMIN, TECNICO, AUXILIAR) |
| estado  | enum     | `ACTIVO` / `INACTIVO` |

#### `role_user` — Relación usuarios ↔ roles (N:M)
| Columna   | Tipo | Descripción               |
|-----------|------|---------------------------|
| usuario_id| int (FK) | FK → `usuarios.id`    |
| role_id   | int (FK) | FK → `roles.id`       |

#### `categorias` — Categorías de productos/equipos
| Columna     | Tipo         | Descripción           |
|-------------|--------------|-----------------------|
| id          | int (PK)     | ID único              |
| nombre      | string       | Nombre de categoría   |
| descripcion | text|null    | Descripción           |
| created_at  | timestamp    |                       |
| updated_at  | timestamp    |                       |

#### `subcategorias` — Subcategorías
| Columna      | Tipo         | Descripción            |
|--------------|--------------|------------------------|
| id           | int (PK)     | ID único               |
| categoria_id | int (FK)     | FK → `categorias.id`   |
| nombre       | string       | Nombre de subcategoría |
| descripcion  | text|null    | Descripción            |
| created_at   | timestamp    |                        |
| updated_at   | timestamp    |                        |

#### `inventario_items` — Equipos en inventario
| Columna              | Tipo         | Descripción                          |
|----------------------|--------------|--------------------------------------|
| id                   | int (PK)     | ID único                             |
| categoria_id         | int (FK)     | FK → `categorias.id`                 |
| subcategoria_id      | int (FK)null | FK → `subcategorias.id`              |
| nombre               | string       | Nombre del equipo                    |
| descripcion          | text|null    | Descripción                          |
| estado               | enum         | `disponible`, `alquilado`, `mantenimiento` |
| precio_alquiler_dia  | decimal      | Precio de alquiler por día           |
| precio_venta         | decimal|null | Precio de venta                      |
| cantidad_stock       | int          | Cantidad en stock                    |
| imagen               | string|null  | URL de imagen                        |
| created_at           | timestamp    |                                      |
| updated_at           | timestamp    |                                      |

#### `productos` — Productos (relacionados a inventario)
| Columna       | Tipo         | Descripción                     |
|---------------|--------------|---------------------------------|
| id            | int (PK)     | ID único                        |
| inventario_id | int (FK)null | FK → `inventario_items.id`      |
| nombre        | string       | Nombre del producto             |
| descripcion   | text|null    | Descripción                     |
| precio        | decimal      | Precio                          |
| created_at    | timestamp    |                                 |
| updated_at    | timestamp    |                                 |

#### `clientes` — Clientes
| Columna     | Tipo         | Descripción         |
|-------------|--------------|---------------------|
| id          | int (PK)     | ID único            |
| nombre      | string       | Nombre del cliente  |
| email       | string       | Correo              |
| telefono    | string       | Teléfono            |
| direccion   | text|null    | Dirección           |
| created_at  | timestamp    |                     |
| updated_at  | timestamp    |                     |

#### `direcciones` — Direcciones de clientes
| Columna     | Tipo         | Descripción           |
|-------------|--------------|-----------------------|
| id          | int (PK)     | ID único              |
| cliente_id  | int (FK)     | FK → `clientes.id`    |
| direccion   | string       | Dirección             |
| ciudad      | string|null  | Ciudad               |
| created_at  | timestamp    |                       |
| updated_at  | timestamp    |                       |

#### `rentas` — Alquileres
| Columna          | Tipo         | Descripción                      |
|------------------|--------------|----------------------------------|
| id               | int (PK)     | ID único                         |
| cliente_id       | int (FK)     | FK → `clientes.id`               |
| fecha_inicio     | date         | Fecha de inicio del alquiler     |
| fecha_fin        | date         | Fecha de fin del alquiler        |
| costo_total      | decimal      | Costo total                      |
| estado           | enum         | `pendiente`, `activo`, `completado`, `cancelado` |
| created_at       | timestamp    |                                  |
| updated_at       | timestamp    |                                  |

#### `cotizaciones` — Cotizaciones
| Columna          | Tipo         | Descripción            |
|------------------|--------------|------------------------|
| id               | int (PK)     | ID único               |
| cliente_id       | int (FK)     | FK → `clientes.id`     |
| total_estimado   | decimal      | Total estimado         |
| estado           | enum         | `pendiente`, `aprobada`, `rechazada` |
| created_at       | timestamp    |                        |
| updated_at       | timestamp    |                        |

#### `detalle_cotizacion` — Items de cotización
| Columna             | Tipo         | Descripción                      |
|---------------------|--------------|----------------------------------|
| id                  | int (PK)     | ID único                         |
| cotizacion_id       | int (FK)     | FK → `cotizaciones.id`           |
| inventario_item_id  | int (FK)     | FK → `inventario_items.id`       |
| cantidad            | int          | Cantidad                         |
| dias                | int          | Días de alquiler                 |
| precio_unitario     | decimal      | Precio unitario                  |
| subtotal            | decimal      | Subtotal                         |
| created_at          | timestamp    |                                  |
| updated_at          | timestamp    |                                  |

#### `mantenimientos` — Mantenimientos de equipos
| Columna             | Tipo         | Descripción                        |
|---------------------|--------------|------------------------------------|
| id                  | int (PK)     | ID único                           |
| inventario_item_id  | int (FK)     | FK → `inventario_items.id`         |
| tecnico             | string       | Nombre del técnico                 |
| fecha_inicio        | datetime     | Fecha de inicio                    |
| fecha_fin           | datetime|null| Fecha de fin                       |
| descripcion         | text         | Descripción del trabajo            |
| estado              | enum         | `pendiente`, `en_progreso`, `completado` |
| costo               | decimal|null | Costo del mantenimiento            |
| created_at          | timestamp    |                                    |
| updated_at          | timestamp    |                                    |

#### `entregas` — Entregas de equipos
| Columna     | Tipo         | Descripción            |
|-------------|--------------|------------------------|
| id          | int (PK)     | ID único               |
| renta_id    | int (FK)     | FK → `rentas.id`       |
| fecha       | datetime     | Fecha de entrega       |
| responsable | string       | Responsable            |
| observaciones| text|null   | Observaciones          |
| created_at  | timestamp    |                        |
| updated_at  | timestamp    |                        |

#### `devoluciones` — Devoluciones de equipos
| Columna      | Tipo         | Descripción            |
|--------------|--------------|------------------------|
| id           | int (PK)     | ID único               |
| renta_id     | int (FK)     | FK → `rentas.id`       |
| fecha        | datetime     | Fecha de devolución    |
| responsable  | string       | Responsable            |
| observaciones| text|null    | Observaciones          |
| created_at   | timestamp    |                        |
| updated_at   | timestamp    |                        |

#### `menu_principal` — Menú del sistema
| Columna | Tipo     | Descripción           |
|---------|----------|-----------------------|
| id      | int (PK) | ID único              |
| nombre  | string   | Nombre del menú       |
| url     | string   | URL del menú          |
| orden   | int      | Orden de visualización|
| icono   | string   | Clase del icono       |

#### `opciones_menu` — Sub-opciones de menú
| Columna      | Tipo     | Descripción             |
|--------------|----------|-------------------------|
| id           | int (PK) | ID único                |
| menu_id      | int (FK) | FK → `menu_principal.id`|
| nombre       | string   | Nombre de la opción     |
| url          | string   | URL                     |
| orden        | int      | Orden                   |

#### `unidades_operativas` — Unidades operativas
| Columna | Tipo     | Descripción         |
|---------|----------|---------------------|
| id      | int (PK) | ID único            |
| nombre  | string   | Nombre de la unidad |
| codigo  | string   | Código              |

#### `refresh_tokens` — Tokens de refresco (única migración)
| Columna    | Tipo         | Descripción                    |
|------------|--------------|--------------------------------|
| id         | int (PK)     | ID único                       |
| user_id    | int (FK)     | FK → `usuarios.id`             |
| token_hash | string (unique) | SHA-256 del refresh token    |
| device_name| string|null  | Nombre del dispositivo         |
| ip_address | string(45)null| Dirección IP                  |
| user_agent | text|null    | User-Agent                     |
| expires_at | timestamp    | Fecha de expiración            |
| last_used_at| timestamp|null | Último uso (rotación)        |
| created_at | timestamp    |                                |
| updated_at | timestamp    |                                |

---

## 5. Rutas de la API (`routes/api.php`)

### Rutas Públicas (sin autenticación)

| Método | Endpoint              | Controlador              | Descripción              |
|--------|-----------------------|--------------------------|--------------------------|
| POST   | `/api/auth/login`     | `AuthController@login`   | Iniciar sesión           |
| POST   | `/api/auth/refresh`   | `AuthController@refresh` | Refrescar token (cookie) |
| POST   | `/api/usuarios`       | `UsuarioController@store`| Registrar usuario        |

### Rutas Protegidas (con middleware JWT)

#### Autenticación
| Método | Endpoint              | Controlador               | Descripción       |
|--------|-----------------------|---------------------------|-------------------|
| POST   | `/api/auth/logout`    | `AuthController@logout`   | Cerrar sesión     |
| GET    | `/api/auth/me`        | `AuthController@me`       | Datos del usuario |
| PUT    | `/api/auth/password`  | `AuthController@updatePassword` | Cambiar password |

#### Usuarios
| Método | Endpoint                    | Acción           | Descripción              |
|--------|-----------------------------|------------------|--------------------------|
| GET    | `/api/usuarios`             | `index`          | Listar usuarios          |
| GET    | `/api/usuarios/{id}`        | `show`           | Ver usuario              |
| PUT    | `/api/usuarios/{id}`        | `update`         | Actualizar usuario       |
| PATCH  | `/api/usuarios/{id}`        | `updatePartial`  | Actualizar parcial       |
| DELETE | `/api/usuarios/{id}`        | `destroy`        | Eliminar usuario (admin) |

#### Roles
| Método | Endpoint              | Acción           | Descripción          |
|--------|-----------------------|------------------|----------------------|
| GET    | `/api/roles`          | `index`          | Listar roles         |
| POST   | `/api/roles`          | `store`          | Crear rol            |
| GET    | `/api/roles/{id}`     | `show`           | Ver rol              |
| PUT    | `/api/roles/{id}`     | `update`         | Actualizar rol       |
| PATCH  | `/api/roles/{id}`     | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/roles/{id}`     | `destroy`        | Eliminar rol (admin) |

#### Categorías
| Método | Endpoint                    | Acción           | Descripción          |
|--------|-----------------------------|------------------|----------------------|
| GET    | `/api/categorias`           | `index`          | Listar categorías    |
| POST   | `/api/categorias`           | `store`          | Crear categoría      |
| GET    | `/api/categorias/{id}`      | `show`           | Ver categoría        |
| PUT    | `/api/categorias/{id}`      | `update`         | Actualizar           |
| PATCH  | `/api/categorias/{id}`      | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/categorias/{id}`      | `destroy`        | Eliminar (admin)     |

#### Subcategorías
| Método | Endpoint                       | Acción           | Descripción           |
|--------|--------------------------------|------------------|-----------------------|
| GET    | `/api/subcategorias`           | `index`          | Listar subcategorías  |
| POST   | `/api/subcategorias`           | `store`          | Crear                 |
| GET    | `/api/subcategorias/{id}`      | `show`           | Ver                   |
| PUT    | `/api/subcategorias/{id}`      | `update`         | Actualizar            |
| PATCH  | `/api/subcategorias/{id}`      | `updatePartial`  | Actualizar parcial    |
| DELETE | `/api/subcategorias/{id}`      | `destroy`        | Eliminar (admin)      |

#### Productos
| Método | Endpoint                    | Acción           | Descripción          |
|--------|-----------------------------|------------------|----------------------|
| GET    | `/api/productos`            | `index`          | Listar productos     |
| POST   | `/api/productos`            | `store`          | Crear producto       |
| GET    | `/api/productos/{id}`       | `show`           | Ver producto         |
| PUT    | `/api/productos/{id}`       | `update`         | Actualizar           |
| PATCH  | `/api/productos/{id}`       | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/productos/{id}`       | `destroy`        | Eliminar (admin)     |

#### Inventario Items
| Método | Endpoint                        | Acción           | Descripción             |
|--------|---------------------------------|------------------|-------------------------|
| GET    | `/api/inventario-items`         | `index`          | Listar items            |
| POST   | `/api/inventario-items`         | `store`          | Crear item              |
| GET    | `/api/inventario-items/{id}`    | `show`           | Ver item                |
| PUT    | `/api/inventario-items/{id}`    | `update`         | Actualizar              |
| PATCH  | `/api/inventario-items/{id}`    | `updatePartial`  | Actualizar parcial      |
| DELETE | `/api/inventario-items/{id}`    | `destroy`        | Eliminar (admin)        |
| GET    | `/api/inventario-items/{id}/mantenimientos` | `mantenimientos` | Mantenimientos del item |

#### Clientes
| Método | Endpoint                    | Acción           | Descripción          |
|--------|-----------------------------|------------------|----------------------|
| GET    | `/api/clientes`             | `index`          | Listar clientes      |
| POST   | `/api/clientes`             | `store`          | Crear cliente        |
| GET    | `/api/clientes/{id}`        | `show`           | Ver cliente          |
| PUT    | `/api/clientes/{id}`        | `update`         | Actualizar           |
| PATCH  | `/api/clientes/{id}`        | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/clientes/{id}`        | `destroy`        | Eliminar (admin)     |

#### Rentas (Alquileres)
| Método | Endpoint                    | Acción           | Descripción          |
|--------|-----------------------------|------------------|----------------------|
| GET    | `/api/rentas`               | `index`          | Listar rentas        |
| POST   | `/api/rentas`               | `store`          | Crear renta          |
| GET    | `/api/rentas/{id}`          | `show`           | Ver renta            |
| PUT    | `/api/rentas/{id}`          | `update`         | Actualizar           |
| PATCH  | `/api/rentas/{id}`          | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/rentas/{id}`          | `destroy`        | Eliminar (admin)     |

#### Cotizaciones
| Método | Endpoint                        | Acción           | Descripción           |
|--------|---------------------------------|------------------|-----------------------|
| GET    | `/api/cotizaciones`             | `index`          | Listar cotizaciones   |
| POST   | `/api/cotizaciones`             | `store`          | Crear cotización      |
| GET    | `/api/cotizaciones/{id}`        | `show`           | Ver cotización        |
| PUT    | `/api/cotizaciones/{id}`        | `update`         | Actualizar            |
| PATCH  | `/api/cotizaciones/{id}`        | `updatePartial`  | Actualizar parcial    |
| DELETE | `/api/cotizaciones/{id}`        | `destroy`        | Eliminar (admin)      |

#### Detalle Cotización
| Método | Endpoint                            | Acción           | Descripción              |
|--------|-------------------------------------|------------------|--------------------------|
| GET    | `/api/detalle-cotizacion`           | `index`          | Listar detalles          |
| POST   | `/api/detalle-cotizacion`           | `store`          | Crear detalle            |
| GET    | `/api/detalle-cotizacion/{id}`      | `show`           | Ver detalle              |
| PUT    | `/api/detalle-cotizacion/{id}`      | `update`         | Actualizar               |
| PATCH  | `/api/detalle-cotizacion/{id}`      | `updatePartial`  | Actualizar parcial       |
| DELETE | `/api/detalle-cotizacion/{id}`      | `destroy`        | Eliminar (admin)         |

#### Mantenimientos
| Método | Endpoint                                    | Acción           | Descripción                |
|--------|---------------------------------------------|------------------|----------------------------|
| GET    | `/api/mantenimientos`                        | `index`          | Listar mantenimientos      |
| POST   | `/api/mantenimientos`                        | `store`          | Crear mantenimiento        |
| GET    | `/api/mantenimientos/{id}`                   | `show`           | Ver mantenimiento          |
| PUT    | `/api/mantenimientos/{id}`                   | `update`         | Actualizar                 |
| PATCH  | `/api/mantenimientos/{id}`                   | `updatePartial`  | Actualizar parcial         |
| DELETE | `/api/mantenimientos/{id}`                   | `destroy`        | Eliminar (admin)           |
| GET    | `/api/mantenimiento/inventario/{id}`         | `porInventario`  | Mantenimientos por equipo  |
| GET    | `/api/mantenimiento/estadisticas`            | `estadisticas`   | Estadísticas de mantenimiento |
| GET    | `/api/reporte-mantenimiento`                 | `reporte`        | Reporte de mantenimientos  |

#### Devoluciones
| Método | Endpoint                        | Acción           | Descripción            |
|--------|---------------------------------|------------------|------------------------|
| GET    | `/api/devoluciones`             | `index`          | Listar devoluciones    |
| POST   | `/api/devoluciones`             | `store`          | Crear devolución       |
| GET    | `/api/devoluciones/{id}`        | `show`           | Ver devolución         |
| PUT    | `/api/devoluciones/{id}`        | `update`         | Actualizar             |
| PATCH  | `/api/devoluciones/{id}`        | `updatePartial`  | Actualizar parcial     |
| DELETE | `/api/devoluciones/{id}`        | `destroy`        | Eliminar (admin)       |

#### Entregas
| Método | Endpoint                    | Acción           | Descripción          |
|--------|-----------------------------|------------------|----------------------|
| GET    | `/api/entregas`             | `index`          | Listar entregas      |
| POST   | `/api/entregas`             | `store`          | Crear entrega        |
| GET    | `/api/entregas/{id}`        | `show`           | Ver entrega          |
| PUT    | `/api/entregas/{id}`        | `update`         | Actualizar           |
| PATCH  | `/api/entregas/{id}`        | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/entregas/{id}`        | `destroy`        | Eliminar (admin)     |

#### Direcciones
| Método | Endpoint                    | Acción           | Descripción          |
|--------|-----------------------------|------------------|----------------------|
| GET    | `/api/direcciones`          | `index`          | Listar direcciones   |
| POST   | `/api/direcciones`          | `store`          | Crear dirección      |
| GET    | `/api/direcciones/{id}`     | `show`           | Ver dirección        |
| PUT    | `/api/direcciones/{id}`     | `update`         | Actualizar           |
| PATCH  | `/api/direcciones/{id}`     | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/direcciones/{id}`     | `destroy`        | Eliminar (admin)     |

#### Menú y Opciones
| Método | Endpoint                    | Acción           | Descripción          |
|--------|-----------------------------|------------------|----------------------|
| GET    | `/api/menu-principal`       | `index`          | Listar menú          |
| POST   | `/api/menu-principal`       | `store`          | Crear menú           |
| GET    | `/api/menu-principal/{id}`  | `show`           | Ver menú             |
| PUT    | `/api/menu-principal/{id}`  | `update`         | Actualizar           |
| PATCH  | `/api/menu-principal/{id}`  | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/menu-principal/{id}`  | `destroy`        | Eliminar (admin)     |
| GET    | `/api/opciones-menu`        | `index`          | Listar opciones      |
| POST   | `/api/opciones-menu`        | `store`          | Crear opción         |
| GET    | `/api/opciones-menu/{id}`   | `show`           | Ver opción           |
| PUT    | `/api/opciones-menu/{id}`   | `update`         | Actualizar           |
| PATCH  | `/api/opciones-menu/{id}`   | `updatePartial`  | Actualizar parcial   |
| DELETE | `/api/opciones-menu/{id}`   | `destroy`        | Eliminar (admin)     |

#### Unidades Operativas
| Método | Endpoint                          | Acción           | Descripción              |
|--------|-----------------------------------|------------------|--------------------------|
| GET    | `/api/unidades-operativas`        | `index`          | Listar unidades          |
| POST   | `/api/unidades-operativas`        | `store`          | Crear unidad             |
| GET    | `/api/unidades-operativas/{id}`   | `show`           | Ver unidad               |
| PUT    | `/api/unidades-operativas/{id}`   | `update`         | Actualizar               |
| PATCH  | `/api/unidades-operativas/{id}`   | `updatePartial`  | Actualizar parcial       |
| DELETE | `/api/unidades-operativas/{id}`   | `destroy`        | Eliminar (admin)         |

#### Reportes
| Método | Endpoint                    | Controlador              | Descripción             |
|--------|-----------------------------|--------------------------|-------------------------|
| GET    | `/api/reporte-renting`      | `ReporteRentingController@index` | Reporte de renting |
| GET    | `/api/reporte-usuarios`     | `ReporteUsuarioController@index` | Reporte de usuarios |
| GET    | `/api/reporte-mantenimiento`| `MantenimientoController@reporte` | Reporte de mantenimiento |

#### Alertas
| Método | Endpoint                    | Controlador              | Descripción             |
|--------|-----------------------------|--------------------------|-------------------------|
| GET    | `/api/alertas`              | `AlertaController@index` | Listar alertas          |

---

## 6. Modelos (Eloquent)

### Relaciones entre modelos

```
Usuario ──┬── Rol (N:M via role_user)
          └── RefreshToken (1:N)

Categoria ──┬── Subcategoria (1:N)
            └── InventarioItem (1:N)

Subcategoria ──┬── InventarioItem (1:N)

InventarioItem ──┬── Mantenimiento (1:N)
                 └── DetalleCotizacion (1:N)

Cliente ──┬── Direccion (1:N)
          ├── Renta (1:N)
          └── Cotizacion (1:N)

Renta ──┬── Entrega (1:N)
        └── Devolucion (1:N)

Cotizacion ──┬── DetalleCotizacion (1:N)
```

### Modelos principales

#### `Usuario.php`
- **Tabla:** `usuarios` (nombre personalizado, no `users`)
- **Relaciones:** `roles()` (N:M), `refreshTokens()` (1:N)
- **Implementa:** `JWTSubject` (interface de tymon/jwt-auth)
- **Métodos clave:**
  - `getJWTIdentifier()` → devuelve `$this->id`
  - `getJWTCustomClaims()` → devuelve array con roles del usuario
  - `hasRole($role)` → verifica si tiene un rol específico
- **Password:** hasheado con bcrypt en mutator `setPasswordAttribute()`
- **Hidden:** `password`

#### `Rol.php`
- **Tabla:** `roles`
- **Relaciones:** `usuarios()` (N:M)

#### `Categoria.php`
- **Tabla:** `categorias`
- **Relaciones:** `subcategorias()` (1:N), `inventarioItems()` (1:N)

#### `Subcategoria.php`
- **Tabla:** `subcategorias`
- **Relaciones:** `categoria()` (N:1), `inventarioItems()` (1:N)

#### `InventarioItem.php`
- **Tabla:** `inventario_items`
- **Relaciones:** `categoria()` (N:1), `subcategoria()` (N:1), `mantenimientos()` (1:N), `detallesCotizacion()` (1:N)

#### `Producto.php`
- **Tabla:** `productos`
- **Relaciones:** `inventario()` (N:1 → `InventarioItem`)

#### `Cliente.php`
- **Tabla:** `clientes`
- **Relaciones:** `direcciones()` (1:N), `rentas()` (1:N), `cotizaciones()` (1:N)

#### `Renta.php`
- **Tabla:** `rentas`
- **Relaciones:** `cliente()` (N:1), `entregas()` (1:N), `devoluciones()` (1:N)

#### `Cotizacion.php`
- **Tabla:** `cotizaciones`
- **Relaciones:** `cliente()` (N:1), `detalles()` (1:N)

#### `DetalleCotizacion.php`
- **Tabla:** `detalle_cotizacion`
- **Relaciones:** `cotizacion()` (N:1), `inventarioItem()` (N:1)

#### `Mantenimiento.php`
- **Tabla:** `mantenimientos`
- **Relaciones:** `inventarioItem()` (N:1)

#### `Entrega.php`
- **Tabla:** `entregas`
- **Relaciones:** `renta()` (N:1)

#### `Devolucion.php`
- **Tabla:** `devoluciones`
- **Relaciones:** `renta()` (N:1)

---

## 7. Autenticación JWT

### Configuración (`config/jwt.php`)

- **Provider:** `tymon/jwt-auth`
- **TTL:** 60 minutos (configurable)
- **Refresh TTL:** 20160 minutos (14 días)
- **Hash algorithm:** `HS256`
- **Secret key:** definido en `.env` como `JWT_SECRET`
- **User model:** `App\Models\Usuario`
- **Auth guard:** `api`

### Flujo de autenticación

```
Login → POST /api/auth/login
  ├── Valida email + password contra tabla `usuarios`
  ├── Genera JWT token con claims (user_id, roles)
  ├── Crea refresh_token en BD (hash SHA-256)
  ├── Devuelve { token, token_type, expires_in, user }
  └── También setea refresh_token en cookie HTTP-only

Logout → POST /api/auth/logout
  ├── Invalida JWT token actual
  └── Elimina refresh_token de BD

Refresh → POST /api/auth/refresh
  ├── Lee refresh_token de cookie
  ├── Verifica hash contra BD
  ├── Invalida token anterior (rotación)
  ├── Genera nuevo JWT + nuevo refresh_token
  └── Devuelve nuevo { token, ... }
```

### Middleware JWT (`app/Http/Middleware/JwtMiddleware.php`)

- Se aplica a todas las rutas protegidas en `routes/api.php`.
- Verifica token del header `Authorization: Bearer <token>`.
- Si el token es inválido o expiró → respuesta 401.
- Si es válido → continúa al controlador.

### Roles del sistema

| Rol       | Descripción                          |
|-----------|--------------------------------------|
| `ADMIN`   | Acceso completo a todas las funciones|
| `TECNICO` | Acceso a operaciones técnicas        |
| `AUXILIAR`| Acceso limitado a consultas          |

El control de roles se maneja en cada controlador mediante verificaciones como:
```php
if (!$user->hasRole('ADMIN')) {
    return response()->json(['error' => 'No autorizado'], 403);
}
```

---

## 8. Configuración CORS (`config/cors.php`)

```php
return [
    'paths' => ['api/*', 'sanctum/csrf-cookie'],
    'allowed_methods' => ['*'],
    'allowed_origins' => ['*'],  // Permitido cualquier origen
    'allowed_headers' => ['*'],
    'exposed_headers' => [],
    'max_age' => 0,
    'supports_credentials' => true,
];
```

**Middleware CORS:** `app/Http/Middleware/CorsMiddleware.php` — middleware adicional para manejar CORS (npm package `cors` también incluido en package.json).

---

## 9. Configuración de Base de Datos

### `.env` (local)
```
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=sistemarenta
DB_USERNAME=root
DB_PASSWORD=
```

### `.env` (remoto — comentado en .env actual)
```
DB_HOST=100.113.218.94
DB_DATABASE=sistemarenta
DB_USERNAME=root
DB_PASSWORD=vN3WpS5al5ZPqBi4
```

### Configuraciones adicionales en `.env`
- **Cache:** Database driver (`CACHE_STORE=database`)
- **Session:** Database driver (`SESSION_DRIVER=database`)
- **Queue:** Database connection (`QUEUE_CONNECTION=database`)
- **Redis:** Predis client (`REDIS_CLIENT=predis`)
- **JWT Secret:** `JWT_SECRET=7Syf3af4kSmvHksQH8ACwiC0t29YYnkncCVqeuucUjiVrLpBxIkPWQIMGh1ioO1C`

---

## 10. Middleware

| Middleware              | Archivo                          | Propósito                              |
|------------------------|----------------------------------|----------------------------------------|
| JWT Auth               | `JwtMiddleware.php`              | Verificar token JWT en cada request    |
| CORS                   | `CorsMiddleware.php`             | Manejar headers CORS                   |
| Laravel Default        | `bootstrap/app.php`              | EncryptCookies, TrimStrings, etc.      |

---

## 11. Manejo de Errores (`app/Exceptions/Handler.php`)

- Personalizado para devolver respuestas JSON consistentes.
- Maneja:
  - `AuthenticationException` → 401
  - `AuthorizationException` → 403
  - `ValidationException` → 422 con errores de validación
  - `ModelNotFoundException` → 404
  - `NotFoundHttpException` → 404
  - Errores generales → 500

### Formato de respuesta de error
```json
{
    "error": "mensaje de error",
    "message": "detalle adicional (en debug)",
    "code": 401
}
```

---

## 12. Documentación de la API (Scribe)

- **Paquete:** `knuckleswtf/scribe` ^5.9
- **Ruta de documentación:** `/docs` (generada por Scribe)
- **Auth key para docs:** `SCRIBE_AUTH_KEY=token_de_prueba_para_la_docs` (en `.env`)
- La documentación se genera automáticamente a partir de anotaciones en los controladores.

---

## 13. Códigos de Error HTTP

| Código | Significado                  | Causa                                  |
|--------|------------------------------|----------------------------------------|
| 200    | OK                           | Request exitoso                        |
| 201    | Created                      | Recurso creado exitosamente            |
| 400    | Bad Request                  | Datos inválidos                        |
| 401    | Unauthorized                 | Token inválido, expirado o no provisto |
| 403    | Forbidden                    | Sin permisos suficientes               |
| 404    | Not Found                    | Recurso no encontrado                  |
| 422    | Unprocessable Entity         | Error de validación                    |
| 500    | Internal Server Error        | Error del servidor                     |

---

## 14. Testing (PHPUnit)

- **Configuración:** `phpunit.xml`
- **Tests:** Unit + Feature en `tests/`
- **Entorno de testing:** APP_ENV=testing, DB sqlite en memoria (comentado)
- **Comando:** `php artisan test`

---

## 15. Comandos Útiles (Artisan)

| Comando                              | Descripción                          |
|--------------------------------------|--------------------------------------|
| `php artisan serve`                  | Iniciar servidor de desarrollo       |
| `php artisan route:list`             | Listar todas las rutas               |
| `php artisan make:controller`        | Crear controlador                    |
| `php artisan make:model`             | Crear modelo                         |
| `php artisan make:migration`         | Crear migración                      |
| `php artisan migrate`                | Ejecutar migraciones                 |
| `php artisan migrate:rollback`       | Revertir migración                   |
| `php artisan tinker`                 | Consola interactiva                  |
| `php artisan key:generate`           | Generar APP_KEY                      |
| `php artisan jwt:secret`             | Generar JWT_SECRET                   |
| `php artisan vendor:publish`         | Publicar assets de paquetes          |
| `php artisan scribe:generate`        | Generar documentación API            |
| `php artisan test`                   | Ejecutar tests                       |
| `npm run dev`                        | Iniciar Vite para assets frontend    |

---

## 16. Script `dev` de Composer

El script `dev` en `composer.json` ejecuta concurrentemente:
```bash
php artisan serve          # Servidor Laravel (puerto 8000)
php artisan queue:listen   # Cola de trabajos
php artisan pail           # Logs en tiempo real
npm run dev                # Vite para assets
```

---

## 17. Convenciones de Código

- **PSR-4:** Autoloading en `App\Models`, `App\Http\Controllers`, etc.
- **PSR-12:** Estilo de código (Laravel Pint configurado).
- **Controladores:** Nomenclatura RESTful (index, store, show, update, destroy).
- **Rutas:** Nombres en plural (`/api/usuarios`, `/api/categorias`).
- **Modelos:** Singular (`Usuario`, `Categoria`).
- **Migraciones:** Timestamp + nombre descriptivo.
- **Respuestas JSON:** Consistentes con códigos HTTP apropiados.

---

## 18. Flujo de Trabajo Típico

### Crear un nuevo equipo en inventario

1. Frontend envía `POST /api/inventario-items` con datos del equipo.
2. `JwtMiddleware` verifica token.
3. `InventarioItemController@store` valida datos.
4. Crea registro en `inventario_items`.
5. Devuelve `201 Created` con el item creado.

### Procesar un alquiler

1. Frontend envía `POST /api/rentas` con `{ cliente_id, fecha_inicio, fecha_fin, items }`.
2. `RentaController@store` crea la renta y actualiza stock.
3. Se crean registros en `rentas`.
4. El estado del equipo en `inventario_items` cambia a `alquilado`.
5. Opcional: se crea `entrega` asociada.

### Reporte de renting

1. Frontend solicita `GET /api/reporte-renting`.
2. `ReporteRentingController@index` consulta rentas con filtros (fechas, cliente, etc.).
3. Devuelve datos agregados para gráficos y tablas.

---

## 19. Notas de Desarrollo

- **Base de datos:** Las tablas no usan migraciones de Laravel (esquema preexistente). Solo `refresh_tokens` tiene migración.
- **JWT:** Usar `php artisan jwt:secret` para regenerar la clave secreta si es necesario.
- **CORS:** Configurado para aceptar cualquier origen (`*`). En producción, restringir.
- **Proxy:** El frontend Vue usa Vite proxy para `/api` hacia `localhost:8000`.
- **Redis:** Configurado para sesiones/cache pero puede no estar disponible localmente.
- **Refresh tokens:** Implementan rotación (al refrescar, el token anterior se invalida).
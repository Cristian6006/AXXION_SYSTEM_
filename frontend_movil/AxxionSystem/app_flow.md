# Documentación de la Aplicación

## Flujo de la Aplicación

### Navegación y Actividades
La aplicación sigue un flujo de navegación basado en autenticación y módulos funcionales:

1. **Inicio de la App**:
   - `AxxionSystemApp` (Application class) inicializa `TokenManager` al iniciar la app.
   - `MainActivity` es el punto de entrada (launcher).

2. **Autenticación**:
   - Si el usuario no está logueado (`TokenManager.isLoggedIn()` retorna false), se muestra la pantalla de login.
   - El login se maneja con `LoginFormController` y `LoginRepository`, usando `RetrofitInstance.publicApi.login()`.
   - Al loguearse exitosamente, se guarda el token y se navega a `DashboardActivity`.
   - Opción de registro: Navega a `RegisterActivity`, que usa `RetrofitInstance.publicApi.register()`.

3. **Dashboard**:
   - Pantalla principal post-login.
   - Botones para navegar a módulos:
     - **Productos**: `ProductosActivity` - Lista productos usando `RetrofitInstance.api.getProductos()`.
     - **Alquiler**: `AlquilerActivity` - Gestiona solicitudes, rentas, entregas y devoluciones.
     - **Mantenimiento**: `MantenimientoActivity` - Crea y actualiza solicitudes de mantenimiento.
     - **Clientes**: `ClientesActivity` - Lista clientes usando `RetrofitInstance.api.getClientes()`.
   - Botón de logout: Limpia token y regresa a `MainActivity`.

4. **Módulos Específicos**:
   - Cada actividad carga datos automáticamente al abrirse.
   - Manejo de errores: Si token expirado (401/403), se redirige a login.
   - Diálogos para crear/editar entidades (productos, solicitudes, etc.).

### Arquitectura del Proyecto
- **Organización por Features**:
  - `auth/`: Autenticación (RegisterActivity, LoginFormController, etc.).
  - `dashboard/`: DashboardActivity.
  - `alquiler/`: AlquilerActivity y modelos relacionados.
  - `clientes/`: ClientesActivity.
  - `mantenimiento/`: MantenimientoActivity y modelos.
  - `productos/`: ProductosActivity.
  - `main/`: MainActivity.
  - `common/`: Componentes compartidos (api/, adapters/, model/, ui/).
- **Modelos**: Agrupados por feature o en common/.
- **Adapters**: Reutilizables en common/adapters/.

## Flujo de Retrofit y API

### Configuración de Retrofit
La aplicación utiliza Retrofit para consumir APIs REST. La configuración se encuentra en `RetrofitInstance.kt`:

- **BASE_URL**: `http://10.0.2.2:8080/` (para emulador Android).
- **Gson**: Configurado como lenient y serializa nulos.
- **Logging**: Interceptor que loguea el body completo de las requests/responses.
- **Timeouts**: 30 segundos para connect, read y write.

### Clientes OkHttp
- **Cliente Autenticado (`authenticatedClient`)**: Incluye header `Authorization: Bearer {token}` y `Content-Type: application/json`. Usado para rutas protegidas.
- **Cliente Público (`publicClient`)**: Sin token, usado para login y registro.

### Instancias de API
- **`RetrofitInstance.api`**: Usa cliente autenticado. Para llamadas que requieren token.
- **`RetrofitInstance.publicApi`**: Usa cliente público. Para login y registro.

### Manejo de Tokens
- **TokenManager**: Almacena el token en SharedPreferences.
  - `saveToken(token)`: Guarda el token tras login exitoso.
  - `getToken()`: Recupera el token para incluir en headers.
  - `clearToken()`: Limpia el token en logout.
  - `isLoggedIn()`: Verifica si hay token válido.

### Endpoints Definidos (ApiServicesKotlin)
- **Autenticación**:
  - `POST /api/auth/login`: Login con email/password.
  - `POST /api/auth/registro`: Registro de usuario.
- **Usuarios**:
  - `GET /usuarios`: Obtener lista de personas.
- **Alquiler**:
  - Solicitudes: Crear y consultar solicitudes de alquiler.
  - Rentas: Crear rentas y consultar por cliente.
  - Entrega/Devolución: Firmar entregas y devoluciones.
- **Mantenimiento**:
  - Crear, consultar y actualizar solicitudes de mantenimiento.
- **Catálogos**:
  - `GET /api/productos`: Lista de productos.
  - `GET /api/clientes`: Lista de clientes.

### Flujo de Llamadas
1. **Login**: Usa `publicApi.login()`. Si éxito, guarda token y cambia a cliente autenticado.
2. **Llamadas Protegidas**: Usa `api.{endpoint}()`. Incluye automáticamente el token en headers.
3. **Manejo de Errores**: Verifica códigos 401/403 para redirigir a login si token expirado.
4. **Callbacks**: Todas las llamadas usan enqueue con Callback para manejar onResponse y onFailure.

### Ejemplo de Uso
En `DashboardActivity`:
```kotlin
RetrofitInstance.api.getPersonas().enqueue(object : Callback<DataResponse> {
    override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
        if (response.isSuccessful) {
            // Procesar datos
        } else if (response.code() == 401) {
            // Token expirado, redirigir a login
        }
    }
    override fun onFailure(call: Call<DataResponse>, t: Throwable) {
        // Error de conexión
    }
})
```

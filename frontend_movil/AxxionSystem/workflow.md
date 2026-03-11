# Flujo de Trabajo (Workflow) - AxxionSystem Android

Este documento describe el flujo de navegación y la arquitectura principal de la aplicación móvil AxxionSystem.

## 1. Inicio y Autenticación
La aplicación comienza su ejecución evaluando el estado de la sesión del usuario.
- **`MainActivity`**: Es el punto de entrada de la aplicación.
  - Al iniciar, inicializa el `TokenManager`.
  - Verifica si el usuario ya tiene una sesión activa (`TokenManager.isLoggedIn()`).
  - **Si hay sesión activa**: Redirige inmediatamente al `DashboardActivity`.
  - **Si NO hay sesión activa**: Muestra la pantalla de inicio de sesión (`R.layout.login`) y delega la lógica al `LoginFormController`.
- **`LoginFormController`**: Gestiona la interacción con la UI del inicio de sesión.
  - Captura las credenciales suministradas por el usuario.
  - Utiliza el `LoginRepository` para consumir el endpoint `/api/auth/login` (definido en `ApiServicesKotlin`).
  - Tras un inicio de sesión exitoso, notifica a la `MainActivity` para que proceda a navegar al Dashboard.

## 2. Panel Principal (Dashboard)
Una vez autenticado exitosamente, el usuario accede al núcleo interactivo de la aplicación.
- **`DashboardActivity`**: Actúa como el menú principal (hub) hacia los demás módulos del sistema.
  - Permite la navegación hacia las pantallas específicas de cada módulo operativo:
    - **Productos**: Redirige a `ProductosActivity`.
    - **Alquiler**: Redirige a `AlquilerActivity` (gestión de solicitudes, entregas, firmas y devoluciones).
    - **Mantenimiento**: Redirige a `MantenimientoActivity`.
    - **Clientes**: Redirige a `ClientesActivity`.
  - Dispone de una función para "Mostrar Usuarios", que consume el endpoint `/usuarios` por medio de `ApiServicesKotlin` y muestra el resultado en un `RecyclerView` usando `PersonaAdapter`.
  - Contiene la opción de "**Cerrar Sesión**", que limpia el token almacenado y devuelve al usuario a la `MainActivity`.

## 3. Módulos Operativos (Servicios API)
Toda solicitud de red dentro de la aplicación es manejada a través de `Retrofit` y la interfaz **`ApiServicesKotlin`**, la cual especifica todos los endpoints de la aplicación:
- **Catálogos**: Consulta de listado de productos (`/api/productos`) y clientes (`/api/clientes`).
- **Alquiler**:
  - Creación y consulta de solicitudes de alquiler (`/api/alquiler/solicitudes`).
  - Creación y consulta de rentas asignadas a clientes (`/api/alquiler/rentas`).
  - Firmas digitales de entregas y devoluciones.
- **Mantenimiento**: Creación, actualización y consulta de tickets/solicitudes de mantenimiento.

## Resumen del Flujo Gráfico
[App Iniciada] --> (Token válido?) 
   |-- [NO] --> (Pantalla Login) --> [Autenticación OK] --> (Guarda Token) --> [Dashboard]
   |-- [SI] --> [Dashboard]

[Dashboard] --> (Elige Módulo)
   |-- (Alquiler) --> AlquilerActivity
   |-- (Mantenimiento) --> MantenimientoActivity
   |-- (Productos) --> ProductosActivity
   |-- (Clientes) --> ClientesActivity
   |-- (Logout) --> Limpia Token --> [Pantalla Login]

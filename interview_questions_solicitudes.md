# Preguntas de Entrevista: Gestión de Solicitudes y Vue.js

Este documento contiene posibles preguntas de entrevista relacionadas con la implementación de la sección de solicitudes en el sistema AXXION.

## 1. Arquitectura y Vue.js

### ¿Cómo manejas la comunicación entre componentes en este módulo?
**Respuesta:** En este módulo se utiliza **Pinia** como gestor de estado centralizado (`solicitudStore`). Esto permite que la vista de `Solicitudes.vue` y cualquier otro componente futuro (como un dashboard) accedan a los datos de forma reactiva sin necesidad de pasar props a través de múltiples niveles (prop drilling).

### ¿Por qué elegiste usar `computed` para el filtrado de solicitudes en lugar de un `watch`?
**Respuesta:** Las propiedades computadas (`computed`) son ideales para el filtrado porque son declarativas y se cachean automáticamente. Vue solo recalcula el filtro si sus dependencias (la lista original de solicitudes o el texto de búsqueda) cambian, lo que optimiza el rendimiento frente a un `watch` que ejecutaría lógica imperativa en cada cambio.

### Explica el ciclo de vida de este componente (`onMounted`).
**Respuesta:** Se utiliza `onMounted` para realizar la petición inicial a la API a través del store. Es el momento adecuado porque el componente ya está en el DOM y podemos mostrar un estado de carga mientras recibimos los datos, evitando llamadas innecesarias si el componente no llega a montarse.

## 2. Integración con el Backend

### ¿Cómo manejas las relaciones de datos (Cliente y Productos) que vienen del backend?
**Respuesta:** En el backend de Laravel, el controlador usa `Solicitud::with('cliente', 'productos')->get()`. En el frontend, estas relaciones llegan como objetos anidados. En el componente, accedemos a ellas de forma segura usando el encadenamiento opcional (`solicitud.cliente?.nombre`) para evitar errores si alguna relación es nula.

### ¿Qué estrategia de manejo de errores implementaste para las peticiones HTTP?
**Respuesta:** Se utiliza un bloque `try-catch` dentro de las acciones de Pinia. Si la API devuelve un error (ej. 404 o 500), se captura y se guarda en una propiedad `error` del estado del store, permitiendo que la interfaz de usuario muestre un mensaje amigable al usuario en lugar de fallar silenciosamente.

## 3. Experiencia de Usuario (UX)

### ¿Cómo optimizaste la visualización de datos complejos en una pantalla pequeña?
**Respuesta:** Se implementó una tabla responsiva con `overflow-x-auto` y un diseño de tarjetas en el modal para que en dispositivos móviles la información se apile verticalmente de forma legible, utilizando clases de Tailwind CSS como `grid-cols-1 md:grid-cols-2`.

### ¿Qué ventajas ofrece el uso de "Status Badges" en la gestión de solicitudes?
**Respuesta:** Los badges de estado proporcionan un feedback visual inmediato (identificación por color: azul para nuevo, verde para atendido, rojo para cancelado). Esto reduce la carga cognitiva del usuario, permitiéndole escanear rápidamente la lista y priorizar tareas.

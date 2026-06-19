# AXXION SYSTEM — Frontend Context

> **Proyecto:** Sistema de gestión de inventario, alquileres y mantenimientos  
> **Stack:** Vue 3 + Pinia + Vue Router + Vite + Tailwind CSS v4  
> **Backend:** Laravel (API REST en `/api`)  
> **Entorno:** Node ^20.19.0 || >=22.12.0

---

## 1. Estructura del Proyecto

```
axxion_system/
├── src/
│   ├── App.vue                        # Componente raíz (<RouterView />)
│   ├── main.js                        # Entry point: createApp, Pinia, Router
│   ├── Style.css                      # Estilos globales (Tailwind base + custom)
│   ├── sw-register.js                 # Service Worker (PWA auto-update)
│   ├── types.ts                       # Interfaces TypeScript compartidas
│   ├── assets/                        # Logo, imágenes estáticas
│   ├── components/                    # Componentes reutilizables
│   │   ├── SideBar.vue                # Barra de navegación lateral
│   │   ├── headerP.vue                # Header superior (breadcrumb, usuario)
│   │   ├── Footer.vue                 # Pie de página
│   │   ├── Table.vue                  # Tabla genérica reutilizable
│   │   ├── modal.vue                  # Modal genérico reutilizable
│   │   ├── UserCard.vue / UserForm.vue
│   │   ├── EquipmentCard.vue / EquipmentForm.vue / EquipmentDetails.vue
│   │   ├── CategoryCard.vue / CategoryModal.vue / CategoryList.vue
│   │   ├── SubCategoryCard.vue / SubcategoryModal.vue / SubcategoryList.vue
│   │   ├── ClientForm.vue             # Formulario de cliente para alquileres
│   │   ├── RentalModal.vue            # Modal de gestión de alquiler
│   │   ├── CartDrawer.vue             # Carrito de cotizaciones
│   │   ├── MetricCard.vue             # Tarjeta de métrica reutilizable
│   │   ├── DashboardMetrics.vue       # Panel de métricas del dashboard
│   │   ├── BarChart.vue / LineBar.vue / PieGraph.vue  # Gráficos ECharts
│   │   ├── StatusBadge.vue / InfoRow.vue
│   │   ├── ExportWorksheet .vue       # Exportación a Excel
│   │   ├── WorksheetMant.vue / WorksheetUser.vue
│   │   ├── DebugUserInfo.vue          # Debug: info del usuario autenticado
│   │   ├── Graficos/                  # Componentes de gráficos adicionales
│   │   └── icons/                     # Componentes de íconos
│   ├── views/
│   │   ├── Home.vue                   # Dashboard principal
│   │   ├── login.vue                  # Página de inicio de sesión
│   │   ├── User.vue                   # Gestión de usuarios
│   │   ├── Category.vue               # Gestión de categorías
│   │   ├── SubCategory.vue            # Gestión de subcategorías
│   │   ├── Inventory.vue              # Gestión de inventario (equipos)
│   │   ├── Rental.vue                 # Gestión de alquileres
│   │   ├── Maintenace.vue             # Gestión de mantenimientos
│   │   ├── Reports.vue                # Reportes generales
│   │   ├── ReportAlquiler.vue         # Reportes de alquiler
│   │   ├── ReportMaintenances.vue     # Reportes de mantenimiento
│   │   ├── ReportUsers.vue            # Reportes de usuarios
│   │   ├── Solicitudes.vue            # Solicitudes de cotización
│   │   ├── QuotationDetails.vue       # Detalle de cotización
│   │   └── Alerts.vue                 # Alertas del sistema
│   ├── stores/                        # State management (Pinia)
│   │   ├── auth.js                    # Autenticación y sesión
│   │   ├── inventory.js               # Inventario (productos)
│   │   ├── inventarioItem.js          # Items de inventario
│   │   ├── category.js                # Categorías
│   │   ├── useSubcategoryStore.js     # Subcategorías
│   │   ├── user.js                    # Usuarios
│   │   ├── maintenance.js             # Mantenimientos
│   │   ├── rentalStore.js             # Alquileres
│   │   ├── CartStore.js               # Carrito de cotizaciones
│   │   ├── solicitudStore.js          # Solicitudes
│   │   └── alertStore.js             # Alertas
│   ├── services/                      # Capa de API (Axios)
│   │   ├── AuthService.js             # (si existe, autenticación)
│   │   ├── InventoryService.js        # CRUD inventario
│   │   ├── InventarioItemService.js   # Items de inventario
│   │   ├── CategoryService.js         # CRUD categorías
│   │   ├── SubCategoryService.js      # CRUD subcategorías
│   │   ├── UserService.js             # CRUD usuarios
│   │   ├── MaintenanceService.js      # CRUD mantenimientos
│   │   ├── RentalService.js           # CRUD alquileres
│   │   ├── ClienteService.js          # CRUD clientes
│   │   ├── SolicitudService.js        # Solicitudes
│   │   ├── CotizacionService.js       # Cotizaciones
│   │   ├── ReportService.js           # Reportes generales
│   │   ├── ReporteReService.js        # Reportes de renting
│   │   ├── ReportMantService.js       # Reportes de mantenimiento
│   │   ├── ReportUserService.js       # Reportes de usuarios
│   │   └── AlertService.js            # Alertas
│   ├── router/
│   │   └── index.js                   # Configuración de rutas + guardias
│   └── plugins/
│       └── axios.js                   # Instancia Axios (baseURL, interceptors)
├── index.html                         # HTML de entrada
├── vite.config.js                     # Vite config (alias, proxy, PWA, Tailwind)
├── package.json                       # Dependencias y scripts
├── postcss.config.js                  # PostCSS (Tailwind, autoprefixer)
├── tailwind.config.js                 # Tailwind CSS v4 (archivo legacy)
├── jsconfig.json                      # Alias @ → ./src
├── eslint.config.js                   # ESLint (flat config)
├── .prettierrc.json                   # Prettier (semicolons: false, singleQuote)
└── README.md                          # Template README
```

---

## 2. Stack Tecnológico

| Tecnología         | Versión    | Propósito                             |
|--------------------|------------|---------------------------------------|
| Vue 3              | ~3.5.18    | Framework frontend (Composition API)  |
| Pinia              | ~3.0.3     | State management                      |
| Vue Router         | ~4.5.1     | SPA routing                           |
| Vite               | ~7.0.6     | Build tool / dev server               |
| Tailwind CSS       | ~4.1.12    | CSS utility framework                 |
| Axios              | ~1.11.0    | HTTP client                           |
| ECharts            | ~5.6.0     | Charts / gráficos interactivos        |
| vue-echarts        | ~7.0.3     | Wrapper Vue para ECharts              |
| Flowbite           | ~3.1.2     | UI components (modals, alerts, etc.)  |
| flowbite-vue       | ~0.2.1     | Wrapper Vue de Flowbite               |
| FontAwesome        | ~7.0.0     | Iconos (free-solid, brands, regular)  |
| Heroicons          | ~2.2.0     | Iconos SVG para Vue                   |
| SweetAlert2        | ~11.26.18  | Alertas / modales de confirmación     |
| file-saver         | ~2.0.5     | Descarga de archivos                  |
| xlsx               | ~0.18.5    | Exportación a Excel                   |
| vite-plugin-pwa    | ~1.2.0     | PWA / Service Worker                  |

---

## 3. Arquitectura General

```
[Vue App] → [Router] → [View (página)]
                ↓
        [Componentes UI]
                ↓
        [Pinia Stores] ← → [Services (Axios)]
                ↓
        [Backend Laravel API /api]
```

### Flujo de datos

1. **Vistas** (`views/`) → cargan datos llamando a **stores** de Pinia.
2. **Stores** → usan **services** para hacer peticiones HTTP.
3. **Services** → usan la instancia de **Axios** configurada en `plugins/axios.js`.
4. Los componentes reciben datos reactivos de las stores mediante `storeToRefs()` o computed properties.
5. Las mutaciones se realizan a través de **acciones** de las stores que llaman a los services correspondientes.

---

## 4. Enrutamiento (`router/index.js`)

### Configuración

- **Modo:** `createWebHistory()` (URLs sin `#`).
- **Base:** `'/axxion'` (proxy reverso o contexto de Apache).
- **Guardia global:** `router.beforeEach` verifica:
  - Si hay token en `authStore.token` → permite navegación.
  - Si no hay token y la ruta requiere autenticación → redirige a `/login`.
  - Si hay token y la ruta es `/login` → redirige a `/home`.

### Tabla de Rutas

| Path                  | Name         | Componente         | Layout         |
|-----------------------|--------------|--------------------|----------------|
| `/login`              | `Login`      | `login.vue`        | Login (sin nav)|
| `/home`               | `Home`       | `Home.vue`         | SideBar + Main |
| `/User`               | `User`       | `User.vue`         | SideBar + Main |
| `/Category`           | `Category`   | `Category.vue`     | SideBar + Main |
| `/SubCategory`        | `SubCategory`| `SubCategory.vue`  | SideBar + Main |
| `/Inventory`          | `Inventory`  | `Inventory.vue`    | SideBar + Main |
| `/Rental`             | `Rental`     | `Rental.vue`       | SideBar + Main |
| `/Mantenace`          | `Mantenace`  | `Maintenace.vue`   | SideBar + Main |
| `/Reports`            | `Reports`    | `Reports.vue`      | SideBar + Main |
| `/ReportAlquiler`     | —            | `ReportAlquiler.vue`| SideBar + Main |
| `/ReportMaintenances` | —            | `ReportMaintenances.vue`| SideBar + Main |
| `/ReportUsers`        | —            | `ReportUsers.vue`  | SideBar + Main |
| `/Solicitudes`        | `Solicitudes`| `Solicitudes.vue`  | SideBar + Main |
| `/QuotationDetails`   | —            | `QuotationDetails.vue`| SideBar + Main |
| `/Alerts`             | —            | `Alerts.vue`       | SideBar + Main |

---

## 5. Stores (Pinia)

### `auth.js` — Autenticación

- **State:** `user` (objeto usuario), `token` (string JWT), `role` (string).
- **Actions:**
  - `login(credentials)` → POST `/api/login` → guarda token y usuario en `localStorage`.
  - `logout()` → limpia estado y `localStorage`.
  - `fetchUser()` → GET `/api/user` → actualiza datos del usuario.
- **Getters:** `isAuthenticated`, `userRole`, `userName`.

### `inventory.js` — Productos / Equipos

- **State:** `productList` (array), `categories` (array), `loading` (bool), `error` (string).
- **Actions:**
  - `fetchProducts()` → GET `/api/inventario-items`.
  - `fetchCategories()` → GET `/api/categorias`.
  - `addProduct(data)` → POST `/api/inventario-items`.
  - `updateProduct(id, data)` → PUT `/api/inventario-items/{id}`.
  - `deleteProduct(id)` → DELETE `/api/inventario-items/{id}`.
- **Getters:** `productsByCategory`, `availableProducts`, `rentedProducts`.

### `category.js` — Categorías

- **State:** `categories` (array), `loading` (bool).
- **Actions:** `fetchCategories()`, `addCategory()`, `updateCategory()`, `deleteCategory()`.

### `useSubcategoryStore.js` — Subcategorías

- **State:** `subcategories` (array), `loading` (bool).
- **Actions:** `fetchSubcategories()`, `addSubcategory()`, `updateSubcategory()`, `deleteSubcategory()`.

### `user.js` — Usuarios

- **State:** `users` (array), `loading` (bool).
- **Actions:** `fetchUsers()`, `createUser()`, `updateUser()`, `deleteUser()`.

### `maintenance.js` — Mantenimientos

- **State:** `maintenances` (array), `loading` (bool).
- **Actions:** `fetchMaintenances()`, `createMaintenance()`, `updateMaintenance()`, `deleteMaintenance()`.

### `rentalStore.js` — Alquileres

- **State:** `rentals` (array), `clients` (array), `loading` (bool).
- **Actions:** `fetchRentals()`, `createRental()`, `updateRental()`, `deleteRental()`, `fetchClients()`.

### `CartStore.js` — Carrito de Cotizaciones

- **State:** `cart` (array de items), `client` (objeto).
- **Actions:** `addItem()`, `removeItem()`, `updateQuantity()`, `clearCart()`, `setClient()`.
- **Getters:** `cartTotal`, `cartCount`.

### `solicitudStore.js` — Solicitudes

- **State:** `solicitudes` (array), `loading` (bool).
- **Actions:** `fetchSolicitudes()`, `createSolicitud()`, `updateStatus()`.

### `alertStore.js` — Alertas

- **State:** `alerts` (array), `loading` (bool).
- **Actions:** `fetchAlerts()`, `dismissAlert()`.

### `inventarioItem.js` — Items de Inventario (store adicional)

- **State:** `items` (array).
- **Actions:** CRUD items de inventario.

---

## 6. Servicios (API Calls)

Cada servicio encapsula las llamadas HTTP a un endpoint específico del backend. Usan la instancia de Axios configurada en `plugins/axios.js`.

### Patrón común de servicio

```javascript
import api from '@/plugins/axios'

export default {
  async getAll() {
    return await api.get('/api/recurso')
  },
  async getById(id) {
    return await api.get(`/api/recurso/${id}`)
  },
  async create(data) {
    return await api.post('/api/recurso', data)
  },
  async update(id, data) {
    return await api.put(`/api/recurso/${id}`, data)
  },
  async delete(id) {
    return await api.delete(`/api/recurso/${id}`)
  }
}
```

### Servicios disponibles

| Servicio                  | Endpoint base                   | Propósito                          |
|---------------------------|---------------------------------|------------------------------------|
| `InventoryService`        | `/api/inventario-items`         | CRUD de equipos                    |
| `InventarioItemService`   | `/api/inventario-items`         | CRUD items de inventario           |
| `CategoryService`         | `/api/categorias`               | CRUD categorías                    |
| `SubCategoryService`      | `/api/subcategorias`            | CRUD subcategorías                 |
| `UserService`             | `/api/users`                    | CRUD usuarios                      |
| `MaintenanceService`      | `/api/maintenances`             | CRUD mantenimientos                |
| `RentalService`           | `/api/rentals`                  | CRUD alquileres                    |
| `ClienteService`          | `/api/clientes`                 | CRUD clientes                      |
| `SolicitudService`        | `/api/solicitudes`              | CRUD solicitudes                   |
| `CotizacionService`       | `/api/cotizaciones`             | CRUD cotizaciones                  |
| `ReportService`           | `/api/reports`                  | Reportes generales                 |
| `ReporteReService`        | `/api/reporte-renting`          | Reportes de renting                |
| `ReportMantService`       | `/api/reporte-mantenimiento`    | Reportes de mantenimiento          |
| `ReportUserService`       | `/api/reporte-usuarios`         | Reportes de usuarios               |
| `AlertService`            | `/api/alerts`                   | Gestión de alertas                 |

---

## 7. Axios Config (`plugins/axios.js`)

```javascript
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
})

// Interceptor de solicitud: añade token Bearer
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Interceptor de respuesta: manejo de errores 401
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/axxion/login'
    }
    return Promise.reject(error)
  }
)

export default api
```

---

## 8. Vite Config (`vite.config.js`)

- **Plugins:** `vue()`, `vueJsx()`, `vueDevTools()`, `tailwindcss()`, `VitePWA()`.
- **Resolve alias:** `@` → `./src`.
- **Server proxy:** `/api` → `http://localhost:8000` (configurable via `VITE_API_PROXY_TARGET` env).
- **Server watch:**
  - Ignora `.git/`, `node_modules/`, `dist/`, `target/`, `.idea/`, `_docs/`, `backend/`.
  - `usePolling` activable via `VITE_USE_POLLING=1` para entornos con limitaciones de inotify (WSL, Docker).

---

## 9. Componentes Clave

### SideBar.vue
- Barra lateral de navegación responsive.
- **Modo colapsado:** En desktop se colapsa a iconos (`md:w-20`).
- **Modo móvil:** Sidebar deslizante fijo (toggled by hamburger).
- Dropdown de reportes con sub-rutas.
- Resalta ruta activa con clase `router-link-active` (color verde `#01995f`).
- Módulos: Home, Users, Categories, Inventory, Requests, Reports, Alerts, Maintenance, Rental.

### headerP.vue
- Breadcrumb dinámico (muestra la ruta actual).
- Botón de toggle de sidebar (móvil).
- Dropdown de usuario con opciones: Perfil, Cerrar sesión.

### Table.vue
- Tabla genérica reutilizable.
- Props: `columns` (array de config), `data` (array), `actions` (boolean).
- Soporta: sorting, filtros, acciones (editar/eliminar).
- Slots: celdas personalizadas, acciones.

### modal.vue
- Modal genérico reutilizable.
- Props: `show` (boolean), `title` (string), `size` (string).
- Slots: `default` (contenido), `footer` (acciones).
- Emite: `close`.

### CartDrawer.vue
- Drawer lateral para carrito de cotizaciones.
- Muestra items agregados, cantidades, subtotales.
- Botón para generar cotización / solicitud.

### MetricCard.vue
- Tarjeta de métrica estilo KPI.
- Props: `title`, `value`, `icon`, `color`, `trend`.

### BarChart.vue / LineBar.vue / PieGraph.vue
- Wrappers de ECharts para gráficos.
- Props: `data`, `options`, `height`, `width`.
- Usan `vue-echarts` internamente.

---

## 10. Vistas Principales

### Home.vue (Dashboard)
- Sección hero con saludo dinámico (mañana/tarde/noche).
- 4 tarjetas de métricas: Equipos Disponibles, Alquilados, En Mantenimiento, Ingresos del Mes.
- Grid de módulos (enlaces rápidos a categorías, inventario, usuarios, mantenimiento, reportes, proveedores).
- Sección de Actividad Reciente y Alertas del Sistema.
- Carga datos de inventario en `onMounted`.

### login.vue
- Layout de dos paneles: branding + formulario.
- Botones de autenticación rápida para 3 roles (admin, auxiliar, asesor).
- Redirección post-login a `{ name: 'Home' }` o a la ruta original (query param `redirect`).

### Inventory.vue
- CRUD completo de equipos/inventario.
- 3 tabs: Lista (tabla), Tarjetas (cards visuales), Métricas (dashboard).
- Modal de creación/edición de equipos.
- Filtros por categoría, estado, búsqueda.
- Alertas de mantenimiento programado.
- Exportación a Excel.

### Rental.vue
- CRUD de alquileres.
- Selección de cliente (autocompletado / búsqueda).
- Selección de equipos con disponibilidad en tiempo real.
- Cálculo automático de fechas y costos.
- Estados: pendiente, activo, completado, cancelado.

### Maintenace.vue
- CRUD de mantenimientos.
- Asignación de equipos y técnicos.
- Programación de fechas.
- Estados: pendiente, en progreso, completado.
- Historial de mantenimientos por equipo.

### Reports.vue
- Dashboard de reportes.
- Gráficos (barras, líneas, pastel) usando ECharts.
- Métricas clave: ingresos, equipment usage, maintenance frequency.
- Enlaces a reportes detallados (alquiler, mantenimiento, usuarios).

### User.vue
- CRUD de usuarios del sistema.
- Roles: admin, auxiliar, asesor.
- Tarjetas de usuario con información de contacto.
- Modal de creación/edición.

---

## 11. Modelo de Datos (TypeScript: `types.ts`)

```typescript
// Usuario
interface User {
  id: number
  name: string
  email: string
  role: 'admin' | 'auxiliar' | 'asesor'
  created_at: string
}

// Categoría
interface Category {
  id: number
  nombre: string
  descripcion?: string
  created_at: string
}

// Subcategoría
interface Subcategory {
  id: number
  nombre: string
  categoria_id: number
  descripcion?: string
}

// Producto / Equipo de inventario
interface InventoryItem {
  id: number
  nombre: string
  descripcion?: string
  categoria_id: number
  subcategoria_id?: number
  estado: 'disponible' | 'alquilado' | 'mantenimiento'
  precio_alquiler_dia: number
  precio_venta?: number
  cantidad_stock: number
  imagen?: string
  created_at: string
  updated_at: string
}

// Cliente
interface Client {
  id: number
  nombre: string
  email: string
  telefono: string
  direccion?: string
  created_at: string
}

// Alquiler
interface Rental {
  id: number
  cliente_id: number
  fecha_inicio: string
  fecha_fin: string
  costo_total: number
  estado: 'pendiente' | 'activo' | 'completado' | 'cancelado'
  items: RentalItem[]
  created_at: string
}

// Item de alquiler
interface RentalItem {
  id: number
  inventario_item_id: number
  cantidad: number
  precio_unitario: number
}

// Mantenimiento
interface Maintenance {
  id: number
  inventario_item_id: number
  tecnico: string
  fecha_inicio: string
  fecha_fin?: string
  descripcion: string
  estado: 'pendiente' | 'en_progreso' | 'completado'
  costo?: number
  created_at: string
}

// Solicitud / Cotización
interface Solicitud {
  id: number
  cliente_id: number
  items: SolicitudItem[]
  total_estimado: number
  estado: 'pendiente' | 'aprobada' | 'rechazada'
  created_at: string
}

interface SolicitudItem {
  inventario_item_id: number
  cantidad: number
  dias: number
}
```

---

## 12. Autenticación y Seguridad

- **Login:** POST `/api/login` → recibe `{ token, user }`.
- **Token:** Almacenado en `localStorage` con clave `'token'`.
- **Interceptor Axios:** Añade `Authorization: Bearer <token>` a cada request.
- **Interceptor de respuesta:** Si recibe 401, limpia token y redirige a `/login`.
- **Guardia de ruta:** `router.beforeEach` verifica existencia de token. Sin token → redirect a `/login`.
- **Roles:** El backend maneja autorización por rol (admin, auxiliar, asesor). El frontend puede mostrar/ocultar elementos según el rol.

---

## 13. Variables de Entorno

| Variable                 | Default               | Propósito                           |
|--------------------------|-----------------------|-------------------------------------|
| `VITE_API_URL`           | `/api`                | Base URL de la API                  |
| `VITE_API_PROXY_TARGET`  | `http://localhost:8000`| Target del proxy de Vite            |
| `VITE_USE_POLLING`       | —                     | Forzar polling para watch (WSL/Docker) |

---

## 14. Scripts Disponibles (package.json)

| Comando            | Descripción                                    |
|--------------------|------------------------------------------------|
| `npm run dev`      | Dev server con polling activado (WSL/Docker)   |
| `npm run dev:native`| Dev server sin polling (entornos nativos)      |
| `npm run build`    | Build para producción                          |
| `npm run preview`  | Vista previa del build                         |
| `npm run lint`     | ESLint con auto-fix                            |
| `npm run format`   | Prettier sobre `src/`                          |

---

## 15. Convenciones de Código

### Estilo
- **Sin punto y coma** (`semi: false`).
- **Comillas simples** (`singleQuote: true`).
- **Ancho máximo:** 100 caracteres.
- **Indentación:** 2 espacios.
- **Final de línea:** LF (Unix).

### Nombramiento
- **Archivos:** `PascalCase.vue` para componentes, `camelCase.js` para stores/services.
- **Componentes:** Multi-word (ej: `UserCard`, `EquipmentForm`).
- **Stores:** `camelCase` con sufijo descriptivo (`useXxxStore`).
- **Variables:** `camelCase`.
- **Rutas:** `PascalCase` para name, `kebab-case` para path.
- **Servicios:** `PascalCase` (ej: `InventoryService`, `CategoryService`).

### Patrones
- **Composition API** con `<script setup>` en todos los componentes.
- **Pinia** con sintaxis de setup stores (cuando aplica).
- **Servicios** como objetos con métodos async.
- **Props** tipadas con `defineProps` (sin TypeScript estricto).
- **Eventos** emitidos con `defineEmits`.

---

## 16. PWA (vite-plugin-pwa)

- **registerType:** `'autoUpdate'`.
- **Manifest:** nombre "AXXION SYSTEM", íconos 192x192 y 512x512.
- **Service Worker:** Generado automáticamente por Vite.
- **Máximo tamaño de cache:** 4 MB (`maximumFileSizeToCacheInBytes`).
- **Archivos precacheados:** `favicon.ico`, `apple-touch-icon.png`, `masked-icon.svg`.

---

## 17. Estilos Globales (`Style.css`)

```css
@import "tailwindcss";
@import "flowbite";
```

Tailwind CSS v4 se aplica globalmente. No se requiere import adicional. Los componentes usan clases utilitarias de Tailwind con estilos scoped cuando es necesario (SCSS con `<style scoped>`).

---

## 18. Dependencias de UI Externas

| Biblioteca       | Componentes usados típicamente                  |
|------------------|-------------------------------------------------|
| Flowbite         | `FwbAlert`, `FwbButton`, `FwbCard`, `FwbBadge`, `FwbModal`, `FwbTable` |
| flowbite-vue     | Wrappers de Flowbite para Vue 3                 |
| FontAwesome      | `<font-awesome-icon icon="fa-solid fa-xxx" />`  |
| Heroicons Vue    | Componentes SVG importados desde `@heroicons/vue` |
| SweetAlert2      | `Swal.fire()` para confirmaciones y alertas     |
| vue-echarts      | `<v-chart :option="..." />`                     |

---

## 19. Manejo de Errores

- **Services:** Capturan errores HTTP y los propagan o transforman.
- **Stores:** Manejan `try/catch` en acciones, actualizan estado `error`.
- **Vistas:** Muestran errores con alerts de Flowbite o SweetAlert2.
- **Interceptor Axios:** 401 → logout automático.
- **Red:** Errores de red no controlados se loguean en consola.

---

## 20. Flujo de Trabajo Típico

### Crear un nuevo recurso (ej: equipo)

1. Usuario llena formulario en `Inventory.vue`.
2. Se llama a `inventoryStore.addProduct(data)`.
3. La store llama a `InventoryService.create(data)`.
4. El service hace POST a `/api/inventario-items`.
5. Backend responde con el nuevo item + 201.
6. La store hace push al array `productList`.
7. La UI se actualiza reactivamente.

### Exportar datos a Excel

1. Usuario hace clic en "Exportar".
2. Componente `ExportWorksheet` recolecta datos de la store.
3. Usa la librería `xlsx` para crear workbook.
4. Usa `file-saver` para descargar el archivo `.xlsx`.

---

## 21. Notas de Desarrollo

- **Proxy de Vite:** Las peticiones a `/api/*` se redirigen automáticamente al backend Laravel (`localhost:8000` por defecto).
- **Polling:** En entornos WSL/Docker, usar `npm run dev` (habilita polling automáticamente via `VITE_USE_POLLING=1`).
- **Alias `@`:** En imports, usar `@/components/...` en lugar de rutas relativas.
- **Node engine:** El proyecto requiere Node 20.19+ o 22.12+.
- **Hot Module Replacement (HMR):** Vue DevTools habilitado automáticamente en desarrollo.
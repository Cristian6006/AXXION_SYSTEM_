<template>
  <div class="app flex min-h-screen bg-gray-50 ">
    <!-- SideBar -->
    <SideBar></SideBar>

    <!-- Main Content -->
    <RouterView></RouterView>

    <main class="flex-1 p-6 overflow-x-auto">
      <div class="max-w-7xl mx-auto space-y-6">
        <!-- Header -->
        <headerP></headerP>

        <!-- Welcome Hero Section -->
        <div class="bg-gradient-to-r from-blue-600 via-purple-600 to-blue-800 rounded-2xl p-8 text-white shadow-xl">
          <div class="flex flex-col lg:flex-row items-center justify-between">
            <div class="mb-6 lg:mb-0">
              <h1 class="text-4xl lg:text-5xl font-bold mb-4">
                ¡Bienvenido a <span class="text-yellow-300">AXION SYSTEM</span>!
              </h1>
              <p class="text-xl text-blue-100 mb-6 max-w-2xl">
                Gestiona eficientemente tu inventario de equipos, alquileres y mantenimientos
                con nuestro sistema integral de administración.
              </p>
              <div class="flex flex-wrap gap-4">
                <fwb-button gradient="yellow-blue" size="lg" @click="goToInventory">
                  <font-awesome-icon icon="fa-solid fa-boxes" class="mr-2"/>
                  Ver Inventario
                </fwb-button>
                <fwb-button gradient="purple-pink" size="lg" @click="goToReports">
                  <font-awesome-icon icon="fa-solid fa-chart-bar" class="mr-2"/>
                  Ver Reportes
                </fwb-button>
              </div>
            </div>
            <div class="hidden lg:block">
              <div class="w-32 h-32 bg-white/20 rounded-full flex items-center justify-center">
                <font-awesome-icon icon="fa-solid fa-cogs" class="text-6xl text-yellow-300"/>
              </div>
            </div>
          </div>
        </div>

        <!-- Statistics Dashboard -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <fwb-card class="bg-gradient-to-br from-green-500 to-green-600 text-white border-0 shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div class="p-6">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-green-100 text-sm font-medium mb-1">Equipos Disponibles</p>
                  <p class="text-3xl font-bold">{{ computedStats.available }}</p>
                  <p class="text-green-100 text-xs mt-1">Listos para alquiler</p>
                </div>
                <div class="p-3 bg-white/20 rounded-full">
                  <font-awesome-icon icon="fa-solid fa-check-circle" class="text-2xl"/>
                </div>
              </div>
            </div>
          </fwb-card>

          <fwb-card class="bg-gradient-to-br from-blue-500 to-blue-600 text-white border-0 shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div class="p-6">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-blue-100 text-sm font-medium mb-1">Equipos Alquilados</p>
                  <p class="text-3xl font-bold">{{ computedStats.rented }}</p>
                  <p class="text-blue-100 text-xs mt-1">En uso actualmente</p>
                </div>
                <div class="p-3 bg-white/20 rounded-full">
                  <font-awesome-icon icon="fa-solid fa-handshake" class="text-2xl"/>
                </div>
              </div>
            </div>
          </fwb-card>

          <fwb-card class="bg-gradient-to-br from-orange-500 to-orange-600 text-white border-0 shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div class="p-6">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-orange-100 text-sm font-medium mb-1">En Mantenimiento</p>
                  <p class="text-3xl font-bold">{{ computedStats.maintenance }}</p>
                  <p class="text-orange-100 text-xs mt-1">Equipos en reparación</p>
                </div>
                <div class="p-3 bg-white/20 rounded-full">
                  <font-awesome-icon icon="fa-solid fa-tools" class="text-2xl"/>
                </div>
              </div>
            </div>
          </fwb-card>

          <fwb-card class="bg-gradient-to-br from-purple-500 to-purple-600 text-white border-0 shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div class="p-6">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-purple-100 text-sm font-medium mb-1">Ingresos del Mes</p>
                  <p class="text-3xl font-bold">${{ computedStats.monthlyRevenue.toLocaleString() }}</p>
                  <p class="text-purple-100 text-xs mt-1">Generados este mes</p>
                </div>
                <div class="p-3 bg-white/20 rounded-full">
                  <font-awesome-icon icon="fa-solid fa-dollar-sign" class="text-2xl"/>
                </div>
              </div>
            </div>
          </fwb-card>
        </div>

        <!-- System Modules Grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <!-- Categories Module -->
          <RouterLink to="/category" class="group">
            <fwb-card class="h-full border-0 shadow-lg hover:shadow-xl transition-all duration-300 group-hover:-translate-y-1 bg-gradient-to-br from-blue-50 to-indigo-50 dark:from-gray-800 dark:to-gray-700">
              <div class="p-6 text-center">
                <div class="w-16 h-16 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
                  <font-awesome-icon icon="fa-solid fa-tags" class="text-2xl text-white"/>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 dark:text-white mb-2">Categorías</h3>
                <p class="text-gray-600 dark:text-gray-300 text-sm">Organiza y administra las categorías de tus equipos</p>
                <div class="mt-4">
                  <fwb-badge color="blue" class="text-xs">Activo</fwb-badge>
                </div>
              </div>
            </fwb-card>
          </RouterLink>

          <!-- Inventory Module -->
          <RouterLink to="/Inventory" class="group">
            <fwb-card class="h-full border-0 shadow-lg hover:shadow-xl transition-all duration-300 group-hover:-translate-y-1 bg-gradient-to-br from-green-50 to-emerald-50 dark:from-gray-800 dark:to-gray-700">
              <div class="p-6 text-center">
                <div class="w-16 h-16 bg-gradient-to-br from-green-500 to-emerald-600 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
                  <font-awesome-icon icon="fa-solid fa-boxes" class="text-2xl text-white"/>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 dark:text-white mb-2">Inventario</h3>
                <p class="text-gray-600 dark:text-gray-300 text-sm">Gestiona todo tu catálogo de equipos disponibles</p>
                <div class="mt-4">
                  <fwb-badge color="green" class="text-xs">{{ computedStats.available }} equipos</fwb-badge>
                </div>
              </div>
            </fwb-card>
          </RouterLink>

          <!-- Users Module -->
          <RouterLink to="/User" class="group">
            <fwb-card class="h-full border-0 shadow-lg hover:shadow-xl transition-all duration-300 group-hover:-translate-y-1 bg-gradient-to-br from-purple-50 to-violet-50 dark:from-gray-800 dark:to-gray-700">
              <div class="p-6 text-center">
                <div class="w-16 h-16 bg-gradient-to-br from-purple-500 to-violet-600 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
                  <font-awesome-icon icon="fa-solid fa-users" class="text-2xl text-white"/>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 dark:text-white mb-2">Usuarios</h3>
                <p class="text-gray-600 dark:text-gray-300 text-sm">Administra usuarios y permisos del sistema</p>
                <div class="mt-4">
                  <fwb-badge color="purple" class="text-xs">Sistema</fwb-badge>
                </div>
              </div>
            </fwb-card>
          </RouterLink>

          <!-- Maintenance Module -->
          <RouterLink to="/Mantenace" class="group">
            <fwb-card class="h-full border-0 shadow-lg hover:shadow-xl transition-all duration-300 group-hover:-translate-y-1 bg-gradient-to-br from-orange-50 to-red-50 dark:from-gray-800 dark:to-gray-700">
              <div class="p-6 text-center">
                <div class="w-16 h-16 bg-gradient-to-br from-orange-500 to-red-600 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
                  <font-awesome-icon icon="fa-solid fa-tools" class="text-2xl text-white"/>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 dark:text-white mb-2">Mantenimiento</h3>
                <p class="text-gray-600 dark:text-gray-300 text-sm">Programa y controla el mantenimiento de equipos</p>
                <div class="mt-4">
                  <fwb-badge color="orange" class="text-xs">{{ computedStats.maintenance }} pendientes</fwb-badge>
                </div>
              </div>
            </fwb-card>
          </RouterLink>

          <!-- Reports Module -->
          <RouterLink to="/Reports" class="group">
            <fwb-card class="h-full border-0 shadow-lg hover:shadow-xl transition-all duration-300 group-hover:-translate-y-1 bg-gradient-to-br from-cyan-50 to-blue-50 dark:from-gray-800 dark:to-gray-700">
              <div class="p-6 text-center">
                <div class="w-16 h-16 bg-gradient-to-br from-cyan-500 to-blue-600 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
                  <font-awesome-icon icon="fa-solid fa-chart-bar" class="text-2xl text-white"/>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 dark:text-white mb-2">Reportes</h3>
                <p class="text-gray-600 dark:text-gray-300 text-sm">Genera informes y estadísticas detalladas</p>
                <div class="mt-4">
                  <fwb-badge color="cyan" class="text-xs">Analytics</fwb-badge>
                </div>
              </div>
            </fwb-card>
          </RouterLink>

          <!-- Suppliers Module -->
          <a href="#" class="group">
            <fwb-card class="h-full border-0 shadow-lg hover:shadow-xl transition-all duration-300 group-hover:-translate-y-1 bg-gradient-to-br from-teal-50 to-green-50 dark:from-gray-800 dark:to-gray-700">
              <div class="p-6 text-center">
                <div class="w-16 h-16 bg-gradient-to-br from-teal-500 to-green-600 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:scale-110 transition-transform duration-300">
                  <font-awesome-icon icon="fa-solid fa-truck" class="text-2xl text-white"/>
                </div>
                <h3 class="text-xl font-semibold text-gray-900 dark:text-white mb-2">Proveedores</h3>
                <p class="text-gray-600 dark:text-gray-300 text-sm">Gestiona proveedores y adquisiciones</p>
                <div class="mt-4">
                  <fwb-badge color="teal" class="text-xs">Supply Chain</fwb-badge>
                </div>
              </div>
            </fwb-card>
          </a>
        </div>

        <!-- Recent Activity -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <fwb-card class="shadow-lg">
            <template #header>
              <div class="flex items-center">
                <font-awesome-icon icon="fa-solid fa-clock" class="text-gray-600 mr-3"/>
                <h3 class="text-xl font-semibold text-gray-900 dark:text-white">Actividad Reciente</h3>
              </div>
            </template>
            <div class="p-6">
              <div class="space-y-4">
                <div class="flex items-start space-x-3">
                  <div class="w-2 h-2 bg-green-500 rounded-full mt-2"></div>
                  <div class="flex-1">
                    <p class="text-sm text-gray-900 dark:text-white font-medium">Nuevo equipo agregado al inventario</p>
                    <p class="text-xs text-gray-500 dark:text-gray-400">Hace 2 horas</p>
                  </div>
                </div>
                <div class="flex items-start space-x-3">
                  <div class="w-2 h-2 bg-blue-500 rounded-full mt-2"></div>
                  <div class="flex-1">
                    <p class="text-sm text-gray-900 dark:text-white font-medium">Mantenimiento programado completado</p>
                    <p class="text-xs text-gray-500 dark:text-gray-400">Hace 4 horas</p>
                  </div>
                </div>
                <div class="flex items-start space-x-3">
                  <div class="w-2 h-2 bg-orange-500 rounded-full mt-2"></div>
                  <div class="flex-1">
                    <p class="text-sm text-gray-900 dark:text-white font-medium">Alerta: Equipo requiere mantenimiento</p>
                    <p class="text-xs text-gray-500 dark:text-gray-400">Hace 6 horas</p>
                  </div>
                </div>
              </div>
            </div>
          </fwb-card>

          <fwb-card class="shadow-lg">
            <template #header>
              <div class="flex items-center">
                <font-awesome-icon icon="fa-solid fa-bell" class="text-gray-600 mr-3"/>
                <h3 class="text-xl font-semibold text-gray-900 dark:text-white">Alertas del Sistema</h3>
              </div>
            </template>
            <div class="p-6">
              <div class="space-y-3">
                <fwb-alert type="warning" icon="true" closable>
                  <span class="font-medium">Mantenimiento Pendiente</span>
                  <p class="mt-1 text-sm">3 equipos requieren mantenimiento preventivo esta semana</p>
                </fwb-alert>
                <fwb-alert type="info" icon="true" closable>
                  <span class="font-medium">Devolución Programada</span>
                  <p class="mt-1 text-sm">2 equipos tienen devoluciones programadas para hoy</p>
                </fwb-alert>
              </div>
            </div>
          </fwb-card>
        </div>
      </div>
    </main>
  </div>


  <Footer />
  
  <!-- Componente de Debug -->
  <DebugUserInfo />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import SideBar from '@/components/SideBar.vue'
import headerP from '@/components/headerP.vue'
import Footer from '@/components/Footer.vue'
import DebugUserInfo from '@/components/DebugUserInfo.vue'
import { useInventoryStore } from '@/stores/inventory.js'
import {
  FwbAlert,
  FwbButton,
  FwbCard,
  FwbBadge,
} from 'flowbite-vue'

/**
 * Vista Home.
 * 
 * Página de inicio y dashboard principal del sistema.
 * Muestra:
 * - Resumen estadístico (KPIs) de equipos, rentas y finanzas.
 * - Accesos directos a los módulos principales (Categorías, Inventario, Usuarios, etc.).
 * - Actividad reciente y alertas del sistema.
 */

const router = useRouter()
const inventoryStore = useInventoryStore()

// Datos reactivos para las estadísticas del dashboard.
const stats = ref({
  available: 0,
  rented: 0,
  maintenance: 0,
  monthlyRevenue: 0
})

// Computed: Calcula estadísticas en tiempo real basadas en la lista de productos.
const products = computed(() => inventoryStore.productList)

const computedStats = computed(() => ({
  available: products.value.filter(p => p.estado === 'disponible').length,
  rented: products.value.filter(p => p.estado === 'alquilado').length,
  maintenance: products.value.filter(p => p.estado === 'mantenimiento').length,
  monthlyRevenue: products.value
    .filter(p => p.estado === 'alquilado')
    .reduce((sum, p) => sum + (p.precio_alquiler_dia * 30), 0)
}))

// Métodos de navegación a las diferentes secciones del sistema.
function goToInventory() {
  router.push('/Inventory')
}

function goToUsers() {
  router.push('/User') 
}

function goToMaintenance() {
  router.push('/Mantenace')
}

function goToReports() {
 router.push('/Reportes')
}

// Carga inicial de datos (productos y categorías) para alimentar el dashboard.
onMounted(async () => {
  try {
    await inventoryStore.fetchProducts()
    await inventoryStore.fetchCategories()
    // Update stats with real data
    stats.value = computedStats.value
  } catch (error) {
    console.error('Error loading data:', error)
  }
})
</script>
<style scoped>
/* Custom animations and styles */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-in-up {
  animation: fadeInUp 0.6s ease-out;
}

/* Hover effects for cards */
.module-card {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.module-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 10px 10px -5px rgb(0 0 0 / 0.04);
}

/* Gradient text effect */
.gradient-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%);
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .hero-section {
    padding: 2rem 1rem;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }

  .module-grid {
    grid-template-columns: repeat(1, 1fr);
  }
}

@media (max-width: 640px) {
  .stats-grid {
    grid-template-columns: repeat(1, 1fr);
  }

  .hero-title {
    font-size: 2.5rem;
  }
}

/* Loading animation for stats */
.stat-number {
  transition: all 0.3s ease;
}

.stat-number:hover {
  transform: scale(1.05);
}

/* Custom focus styles */
.fwb-button:focus {
  outline: 2px solid #667eea;
  outline-offset: 2px;
}

/* Dark mode adjustments */
.dark .module-card {
  background: linear-gradient(135deg, rgba(55, 65, 81, 0.8) 0%, rgba(31, 41, 55, 0.8) 100%);
}

.dark .gradient-text {
  background: linear-gradient(135deg, #a855f7 0%, #ec4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
</style>

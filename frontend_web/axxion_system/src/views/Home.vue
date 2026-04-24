<template>
  <div class="app flex min-h-screen bg-white text-gray-900 font-sans">
    <!-- SideBar -->
    <SideBar></SideBar>

    <!-- Main Content -->
    <RouterView></RouterView>

    <main class="flex-1 p-8 lg:p-12 overflow-x-auto">
      <div class="max-w-6xl mx-auto space-y-16">
        <!-- Header -->
        <headerP></headerP>

        <!-- Hero Section - SideBar Pattern -->
        <section class="py-12 px-8 bg-gray-800 rounded-xl shadow-md border-b border-gray-700 mb-8">
          <div class="max-w-3xl">
            <div class="inline-block px-3 py-1 mb-6 text-xs font-medium tracking-widest text-[#01995f] uppercase border border-[#01995f] rounded-full bg-[#01995f]/10">
              {{ currentDateFormatted }}
            </div>
            
            <h1 class="text-5xl font-light tracking-tight text-white mb-6">
              {{ greeting }},<br/>
              <span class="font-bold text-[#01995f]">AXXION SYSTEM.</span>
            </h1>
            
            <p class="text-lg text-gray-300 mb-10 leading-relaxed max-w-2xl">
              Panel centralizado de gestión de inventario, alquileres y mantenimientos.
              Monitorea tus operaciones con simplicidad y eficiencia.
            </p>
            
            <div class="flex flex-wrap gap-4">
              <button @click="goToInventory" class="px-6 py-3 bg-[#01995f] text-white text-sm font-medium rounded-lg hover:bg-[#017a4a] transition-colors shadow-lg">
                Ver Inventario
              </button>
              <button @click="goToReports" class="px-6 py-3 bg-transparent text-white text-sm font-medium rounded-lg border border-gray-500 hover:bg-gray-700 transition-colors">
                Ver Reportes
              </button>
            </div>
          </div>
        </section>

        <!-- Minimal Stats -->
        <section>
          <h2 class="text-sm font-medium text-gray-400 uppercase tracking-widest mb-6">Métricas Generales</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <div class="p-6 border-l-4 border-l-emerald-500 border-y border-r border-gray-200 rounded">
              <p class="text-sm text-gray-500 mb-2">Equipos Disponibles</p>
              <p class="text-4xl font-light text-emerald-600">{{ computedStats.available }}</p>
            </div>
            <div class="p-6 border-l-4 border-l-blue-500 border-y border-r border-gray-200 rounded">
              <p class="text-sm text-gray-500 mb-2">Equipos Alquilados</p>
              <p class="text-4xl font-light text-blue-600">{{ computedStats.rented }}</p>
            </div>
            <div class="p-6 border-l-4 border-l-orange-500 border-y border-r border-gray-200 rounded">
              <p class="text-sm text-gray-500 mb-2">En Mantenimiento</p>
              <p class="text-4xl font-light text-orange-600">{{ computedStats.maintenance }}</p>
            </div>
            <div class="p-6 border-l-4 border-l-purple-500 border-y border-r border-gray-200 rounded">
              <p class="text-sm text-gray-500 mb-2">Ingresos del Mes</p>
              <p class="text-4xl font-light text-purple-600">${{ computedStats.monthlyRevenue.toLocaleString() }}</p>
            </div>
          </div>
        </section>

        <!-- Minimal Modules Grid -->
        <section>
          <h2 class="text-sm font-medium text-gray-400 uppercase tracking-widest mb-6">Módulos</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <RouterLink to="/category" class="group block p-6 border border-gray-200 rounded hover:border-indigo-500 hover:bg-indigo-50/50 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center group-hover:bg-indigo-100 transition-colors">
                  <font-awesome-icon icon="fa-solid fa-tags" class="text-gray-400 group-hover:text-indigo-600 transition-colors" />
                </div>
                <span class="text-xs text-gray-400 group-hover:text-indigo-500 transition-colors">Categorías</span>
              </div>
              <h3 class="text-lg font-medium text-black group-hover:text-indigo-900 transition-colors">Organización</h3>
            </RouterLink>

            <RouterLink to="/Inventory" class="group block p-6 border border-gray-200 rounded hover:border-emerald-500 hover:bg-emerald-50/50 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center group-hover:bg-emerald-100 transition-colors">
                  <font-awesome-icon icon="fa-solid fa-boxes" class="text-gray-400 group-hover:text-emerald-600 transition-colors" />
                </div>
                <span class="text-xs text-gray-400 group-hover:text-emerald-500 transition-colors">Inventario</span>
              </div>
              <h3 class="text-lg font-medium text-black group-hover:text-emerald-900 transition-colors">Catálogo de equipos</h3>
            </RouterLink>

            <RouterLink to="/User" class="group block p-6 border border-gray-200 rounded hover:border-blue-500 hover:bg-blue-50/50 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center group-hover:bg-blue-100 transition-colors">
                  <font-awesome-icon icon="fa-solid fa-users" class="text-gray-400 group-hover:text-blue-600 transition-colors" />
                </div>
                <span class="text-xs text-gray-400 group-hover:text-blue-500 transition-colors">Usuarios</span>
              </div>
              <h3 class="text-lg font-medium text-black group-hover:text-blue-900 transition-colors">Administración</h3>
            </RouterLink>

            <RouterLink to="/Mantenace" class="group block p-6 border border-gray-200 rounded hover:border-orange-500 hover:bg-orange-50/50 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center group-hover:bg-orange-100 transition-colors">
                  <font-awesome-icon icon="fa-solid fa-tools" class="text-gray-400 group-hover:text-orange-600 transition-colors" />
                </div>
                <span class="text-xs text-gray-400 group-hover:text-orange-500 transition-colors">Mantenimiento</span>
              </div>
              <h3 class="text-lg font-medium text-black group-hover:text-orange-900 transition-colors">Control técnico</h3>
            </RouterLink>

            <RouterLink to="/Reports" class="group block p-6 border border-gray-200 rounded hover:border-cyan-500 hover:bg-cyan-50/50 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center group-hover:bg-cyan-100 transition-colors">
                  <font-awesome-icon icon="fa-solid fa-chart-bar" class="text-gray-400 group-hover:text-cyan-600 transition-colors" />
                </div>
                <span class="text-xs text-gray-400 group-hover:text-cyan-500 transition-colors">Reportes</span>
              </div>
              <h3 class="text-lg font-medium text-black group-hover:text-cyan-900 transition-colors">Informes detallados</h3>
            </RouterLink>

            <a href="#" class="group block p-6 border border-gray-200 rounded hover:border-teal-500 hover:bg-teal-50/50 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <div class="w-10 h-10 rounded-full bg-gray-50 flex items-center justify-center group-hover:bg-teal-100 transition-colors">
                  <font-awesome-icon icon="fa-solid fa-truck" class="text-gray-400 group-hover:text-teal-600 transition-colors" />
                </div>
                <span class="text-xs text-gray-400 group-hover:text-teal-500 transition-colors">Proveedores</span>
              </div>
              <h3 class="text-lg font-medium text-black group-hover:text-teal-900 transition-colors">Adquisiciones</h3>
            </a>
          </div>
        </section>

        <!-- Minimal Activity & Alerts -->
        <section class="grid grid-cols-1 lg:grid-cols-2 gap-12">
          <div>
            <h2 class="text-sm font-medium text-gray-400 uppercase tracking-widest mb-6">Actividad Reciente</h2>
            <div class="space-y-6">
              <div class="flex items-start gap-4">
                <div class="w-1.5 h-1.5 bg-black rounded-full mt-2 shrink-0"></div>
                <div>
                  <p class="text-sm font-medium text-gray-900">Nuevo equipo agregado al inventario</p>
                  <p class="text-xs text-gray-500 mt-1">Hace 2 horas</p>
                </div>
              </div>
              <div class="flex items-start gap-4">
                <div class="w-1.5 h-1.5 bg-gray-300 rounded-full mt-2 shrink-0"></div>
                <div>
                  <p class="text-sm font-medium text-gray-900">Mantenimiento programado completado</p>
                  <p class="text-xs text-gray-500 mt-1">Hace 4 horas</p>
                </div>
              </div>
              <div class="flex items-start gap-4">
                <div class="w-1.5 h-1.5 bg-gray-300 rounded-full mt-2 shrink-0"></div>
                <div>
                  <p class="text-sm font-medium text-gray-900">Alerta: Equipo requiere mantenimiento</p>
                  <p class="text-xs text-gray-500 mt-1">Hace 6 horas</p>
                </div>
              </div>
            </div>
          </div>

          <div>
            <h2 class="text-sm font-medium text-gray-400 uppercase tracking-widest mb-6">Alertas del Sistema</h2>
            <div class="space-y-4">
              <div class="p-4 border border-gray-200 rounded flex gap-4 items-start">
                <font-awesome-icon icon="fa-solid fa-circle-exclamation" class="text-black mt-0.5" />
                <div>
                  <p class="text-sm font-medium text-black">Mantenimiento Pendiente</p>
                  <p class="text-sm text-gray-500 mt-1">3 equipos requieren mantenimiento preventivo esta semana.</p>
                </div>
              </div>
              <div class="p-4 border border-gray-200 rounded flex gap-4 items-start">
                <font-awesome-icon icon="fa-solid fa-circle-info" class="text-gray-400 mt-0.5" />
                <div>
                  <p class="text-sm font-medium text-black">Devolución Programada</p>
                  <p class="text-sm text-gray-500 mt-1">2 equipos tienen devoluciones programadas para hoy.</p>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </main>
  </div>

  <Footer />
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
 */

const router = useRouter()
const inventoryStore = useInventoryStore()

const stats = ref({
  available: 0,
  rented: 0,
  maintenance: 0,
  monthlyRevenue: 0
})

const products = computed(() => inventoryStore.productList)

const computedStats = computed(() => ({
  available: products.value.filter(p => p.estado === 'disponible').length,
  rented: products.value.filter(p => p.estado === 'alquilado').length,
  maintenance: products.value.filter(p => p.estado === 'mantenimiento').length,
  monthlyRevenue: products.value
    .filter(p => p.estado === 'alquilado')
    .reduce((sum, p) => sum + (p.precio_alquiler_dia * 30), 0)
}))

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return 'Buenos días'
  if (hour < 18) return 'Buenas tardes'
  return 'Buenas noches'
})

const currentDateFormatted = computed(() => {
  return new Date().toLocaleDateString('es-ES', {
    weekday: 'long',
    day: 'numeric',
    month: 'long'
  })
})

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

onMounted(async () => {
  try {
    await inventoryStore.fetchProducts()
    await inventoryStore.fetchCategories()
    stats.value = computedStats.value
  } catch (error) {
    console.error('Error loading data:', error)
  }
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500&display=swap');

.font-sans {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
}

/* Base minimal styles */
::selection {
  background: black;
  color: white;
}

/* Custom minimal scrollbar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: #e5e7eb;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #d1d5db;
}
</style>

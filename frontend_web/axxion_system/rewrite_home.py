import os

filepath = "/etc/apache2/sites-available/axxion/AXXION_SYSTEM_/frontend_web/axxion_system/src/views/Home.vue"

new_content = """<template>
  <div class="app flex min-h-screen bg-white text-gray-900 font-sans">
    <!-- SideBar -->
    <SideBar></SideBar>

    <!-- Main Content -->
    <RouterView></RouterView>

    <main class="flex-1 p-8 lg:p-12 overflow-x-auto">
      <div class="max-w-6xl mx-auto space-y-16">
        <!-- Header -->
        <headerP></headerP>

        <!-- Minimal Hero Section -->
        <section class="py-12 border-b border-gray-100">
          <div class="max-w-3xl">
            <div class="inline-block px-3 py-1 mb-6 text-xs font-medium tracking-widest text-gray-500 uppercase border border-gray-200 rounded-full">
              {{ currentDateFormatted }}
            </div>
            
            <h1 class="text-5xl font-light tracking-tight text-gray-900 mb-6">
              {{ greeting }},<br/>
              <span class="font-medium">AXXION SYSTEM.</span>
            </h1>
            
            <p class="text-lg text-gray-500 mb-10 leading-relaxed max-w-2xl">
              Panel centralizado de gestión de inventario, alquileres y mantenimientos.
              Monitorea tus operaciones con simplicidad y eficiencia.
            </p>
            
            <div class="flex flex-wrap gap-4">
              <button @click="goToInventory" class="px-6 py-3 bg-black text-white text-sm font-medium rounded hover:bg-gray-800 transition-colors">
                Ver Inventario
              </button>
              <button @click="goToReports" class="px-6 py-3 bg-white text-black text-sm font-medium rounded border border-gray-300 hover:bg-gray-50 transition-colors">
                Ver Reportes
              </button>
            </div>
          </div>
        </section>

        <!-- Minimal Stats -->
        <section>
          <h2 class="text-sm font-medium text-gray-400 uppercase tracking-widest mb-6">Métricas Generales</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <div class="p-6 border border-gray-200 rounded">
              <p class="text-sm text-gray-500 mb-2">Equipos Disponibles</p>
              <p class="text-4xl font-light text-black">{{ computedStats.available }}</p>
            </div>
            <div class="p-6 border border-gray-200 rounded">
              <p class="text-sm text-gray-500 mb-2">Equipos Alquilados</p>
              <p class="text-4xl font-light text-black">{{ computedStats.rented }}</p>
            </div>
            <div class="p-6 border border-gray-200 rounded">
              <p class="text-sm text-gray-500 mb-2">En Mantenimiento</p>
              <p class="text-4xl font-light text-black">{{ computedStats.maintenance }}</p>
            </div>
            <div class="p-6 border border-gray-200 rounded">
              <p class="text-sm text-gray-500 mb-2">Ingresos del Mes</p>
              <p class="text-4xl font-light text-black">${{ computedStats.monthlyRevenue.toLocaleString() }}</p>
            </div>
          </div>
        </section>

        <!-- Minimal Modules Grid -->
        <section>
          <h2 class="text-sm font-medium text-gray-400 uppercase tracking-widest mb-6">Módulos</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <RouterLink to="/category" class="group block p-6 border border-gray-200 rounded hover:border-gray-900 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <font-awesome-icon icon="fa-solid fa-tags" class="text-gray-400 group-hover:text-black transition-colors" />
                <span class="text-xs text-gray-400">Categorías</span>
              </div>
              <h3 class="text-lg font-medium text-black">Organización</h3>
            </RouterLink>

            <RouterLink to="/Inventory" class="group block p-6 border border-gray-200 rounded hover:border-gray-900 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <font-awesome-icon icon="fa-solid fa-boxes" class="text-gray-400 group-hover:text-black transition-colors" />
                <span class="text-xs text-gray-400">Inventario</span>
              </div>
              <h3 class="text-lg font-medium text-black">Catálogo de equipos</h3>
            </RouterLink>

            <RouterLink to="/User" class="group block p-6 border border-gray-200 rounded hover:border-gray-900 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <font-awesome-icon icon="fa-solid fa-users" class="text-gray-400 group-hover:text-black transition-colors" />
                <span class="text-xs text-gray-400">Usuarios</span>
              </div>
              <h3 class="text-lg font-medium text-black">Administración</h3>
            </RouterLink>

            <RouterLink to="/Mantenace" class="group block p-6 border border-gray-200 rounded hover:border-gray-900 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <font-awesome-icon icon="fa-solid fa-tools" class="text-gray-400 group-hover:text-black transition-colors" />
                <span class="text-xs text-gray-400">Mantenimiento</span>
              </div>
              <h3 class="text-lg font-medium text-black">Control técnico</h3>
            </RouterLink>

            <RouterLink to="/Reports" class="group block p-6 border border-gray-200 rounded hover:border-gray-900 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <font-awesome-icon icon="fa-solid fa-chart-bar" class="text-gray-400 group-hover:text-black transition-colors" />
                <span class="text-xs text-gray-400">Reportes</span>
              </div>
              <h3 class="text-lg font-medium text-black">Informes detallados</h3>
            </RouterLink>

            <a href="#" class="group block p-6 border border-gray-200 rounded hover:border-gray-900 transition-colors">
              <div class="flex items-center justify-between mb-4">
                <font-awesome-icon icon="fa-solid fa-truck" class="text-gray-400 group-hover:text-black transition-colors" />
                <span class="text-xs text-gray-400">Proveedores</span>
              </div>
              <h3 class="text-lg font-medium text-black">Adquisiciones</h3>
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
"""

with open(filepath, "w", encoding="utf-8") as f:
    f.write(new_content)

print("SUCCESS")

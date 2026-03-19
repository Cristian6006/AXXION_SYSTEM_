<template>
  <div class="space-y-6">
    <!-- Métricas Principales -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <MetricCard
        v-for="metric in mainMetrics"
        :key="metric.id"
        :metric="metric"
        :loading="isLoading"
      />
    </div>

    <!-- Gráficos y Análisis -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Gráfico de Utilización -->
      <fwb-card>
        <template #header>
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-chart-pie" class="mr-2 text-blue-500"/>
              <h3 class="text-lg font-semibold">Utilización por Estado</h3>
            </div>
            <fwb-button size="sm" gradient="blue" @click="refreshUtilizationChart">
              <font-awesome-icon icon="fa-solid fa-refresh" class="mr-1"/>
            </fwb-button>
          </div>
        </template>
        <div class="h-64 flex items-center justify-center">
          <div v-if="isLoading" class="text-center">
            <font-awesome-icon icon="fa-solid fa-spinner" class="animate-spin text-4xl text-gray-400 mb-2"/>
            <p class="text-gray-500">Cargando gráfico...</p>
          </div>
          <div v-else class="w-full">
            <!-- Aquí iría un gráfico real como Chart.js o similar -->
            <div class="grid grid-cols-2 gap-4">
              <div class="text-center">
                <div class="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-2">
                  <span class="text-2xl font-bold text-green-600">{{ utilizationData.available }}%</span>
                </div>
                <p class="text-sm text-gray-600">Disponible</p>
              </div>
              <div class="text-center">
                <div class="w-20 h-20 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-2">
                  <span class="text-2xl font-bold text-blue-600">{{ utilizationData.rented }}%</span>
                </div>
                <p class="text-sm text-gray-600">Alquilado</p>
              </div>
              <div class="text-center">
                <div class="w-20 h-20 bg-yellow-100 rounded-full flex items-center justify-center mx-auto mb-2">
                  <span class="text-2xl font-bold text-yellow-600">{{ utilizationData.maintenance }}%</span>
                </div>
                <p class="text-sm text-gray-600">Mantenimiento</p>
              </div>
              <div class="text-center">
                <div class="w-20 h-20 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-2">
                  <span class="text-2xl font-bold text-red-600">{{ utilizationData.outOfService }}%</span>
                </div>
                <p class="text-sm text-gray-600">Fuera de Servicio</p>
              </div>
            </div>
          </div>
        </div>
      </fwb-card>

      <!-- Ingresos por Mes -->
      <fwb-card>
        <template #header>
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-chart-line" class="mr-2 text-green-500"/>
              <h3 class="text-lg font-semibold">Ingresos por Mes</h3>
            </div>
            <fwb-button size="sm" gradient="green" @click="refreshRevenueChart">
              <font-awesome-icon icon="fa-solid fa-refresh" class="mr-1"/>
            </fwb-button>
          </div>
        </template>
        <div class="h-64 flex items-center justify-center">
          <div v-if="isLoading" class="text-center flex flex-col items-center">
            <div class="loader mb-2"></div>
            <p class="text-gray-500">Cargando datos...</p>
          </div>
          <div v-else class="w-full">
            <!-- Gráfico de barras simple -->
            <div class="space-y-3">
              <div 
                v-for="(month, index) in revenueData" 
                :key="index"
                class="flex items-center justify-between"
              >
                <span class="text-sm text-gray-600 w-16">{{ month.name }}</span>
                <div class="flex-1 mx-4">
                  <div class="bg-gray-200 rounded-full h-2">
                    <div 
                      class="bg-green-500 h-2 rounded-full transition-all duration-500"
                      :style="{ width: `${(month.amount / maxRevenue) * 100}%` }"
                    ></div>
                  </div>
                </div>
                <span class="text-sm font-medium w-20 text-right">${{ month.amount.toLocaleString() }}</span>
              </div>
            </div>
          </div>
        </div>
      </fwb-card>
    </div>

    <!-- Equipos Más Rentables -->
    <fwb-card>
      <template #header>
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <font-awesome-icon icon="fa-solid fa-trophy" class="mr-2 text-yellow-500"/>
            <h3 class="text-lg font-semibold">Equipos Más Rentables</h3>
          </div>
          <fwb-button size="sm" gradient="yellow" @click="refreshTopEquipment">
            <font-awesome-icon icon="fa-solid fa-refresh" class="mr-1"/>
          </fwb-button>
        </div>
      </template>
      <div class="overflow-x-auto">
        <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
          <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th scope="col" class="px-6 py-3">Equipo</th>
              <th scope="col" class="px-6 py-3">Categoría</th>
              <th scope="col" class="px-6 py-3">Alquileres</th>
              <th scope="col" class="px-6 py-3">Ingresos</th>
              <th scope="col" class="px-6 py-3">ROI</th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="(equipment, index) in topEquipment" 
              :key="equipment.id"
              class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600"
            >
              <td class="px-6 py-4 font-medium text-gray-900 dark:text-white">
                <div class="flex items-center">
                  <span class="w-8 h-8 bg-yellow-100 text-yellow-800 rounded-full flex items-center justify-center text-sm font-bold mr-3">
                    {{ index + 1 }}
                  </span>
                  {{ equipment.name }}
                </div>
              </td>
              <td class="px-6 py-4">
                <span class="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded-full">
                  {{ getCategoryLabel(equipment.category) }}
                </span>
              </td>
              <td class="px-6 py-4">{{ equipment.rentalCount }}</td>
              <td class="px-6 py-4 font-medium text-green-600">
                ${{ equipment.totalRevenue.toLocaleString() }}
              </td>
              <td class="px-6 py-4">
                <span class="font-medium" :class="getROIClass(equipment.roi)">
                  {{ equipment.roi }}%
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </fwb-card>

    <!-- Alertas y Notificaciones -->
    <fwb-card>
      <template #header>
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <font-awesome-icon icon="fa-solid fa-bell" class="mr-2 text-orange-500"/>
            <h3 class="text-lg font-semibold">Alertas del Sistema</h3>
          </div>
          <fwb-button size="sm" gradient="orange" @click="refreshAlerts">
            <font-awesome-icon icon="fa-solid fa-refresh" class="mr-1"/>
          </fwb-button>
        </div>
      </template>
      <div class="space-y-3">
        <fwb-alert 
          v-for="alert in systemAlerts" 
          :key="alert.id"
          :type="alert.type"
          :icon="alert.icon"
          closable
          @close="removeAlert(alert.id)"
        >
          <span class="font-medium">{{ alert.title }}</span>
          <p class="mt-1">{{ alert.message }}</p>
        </fwb-alert>
        
        <div v-if="systemAlerts.length === 0" class="text-center py-8 text-gray-500">
          <font-awesome-icon icon="fa-solid fa-check-circle" class="text-4xl mb-2 text-green-500"/>
          <p>No hay alertas pendientes</p>
        </div>
      </div>
    </fwb-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useInventoryStore } from '@/stores/inventory.js';
import { FwbCard, FwbButton, FwbAlert } from 'flowbite-vue';
import MetricCard from './MetricCard.vue';

/**
 * Componente DashboardMetrics.
 * 
 * Este componente visualiza las métricas clave del sistema en el panel de control.
 * Muestra tarjetas con contadores (disponibles, alquilados, mantenimiento, ingresos),
 * gráficos de utilización e ingresos, tabla de equipos más rentables y alertas del sistema.
 * 
 * Se conecta al store de inventario para obtener los datos en tiempo real y calcular
 * las estadísticas mostradas.
 */

// Store
const inventoryStore = useInventoryStore();

// Estado
const isLoading = ref(false);
const systemAlerts = ref([]);

// Computed
const equipment = computed(() => inventoryStore.equipment);
const totalEquipment = computed(() => equipment.value.length);

/**
 * Calcula las métricas principales para las tarjetas superiores.
 * - Disponibles: Equipos listos para alquilar.
 * - Alquilados: Equipos actualmente en renta.
 * - Mantenimiento: Equipos en reparación.
 * - Ingresos: Estimación de ingresos mensuales basados en equipos alquilados.
 */
const mainMetrics = computed(() => [
  {
    id: 'available',
    title: 'Equipos Disponibles',
    value: equipment.value.filter(e => e.status === 'available').length,
    total: totalEquipment.value,
    icon: 'fa-solid fa-check-circle',
    color: 'blue',
    trend: '+12%',
    trendDirection: 'up'
  },
  {
    id: 'rented',
    title: 'Equipos Alquilados',
    value: equipment.value.filter(e => e.status === 'rented').length,
    total: totalEquipment.value,
    icon: 'fa-solid fa-handshake',
    color: 'green',
    trend: '+8%',
    trendDirection: 'up'
  },
  {
    id: 'maintenance',
    title: 'En Mantenimiento',
    value: equipment.value.filter(e => e.status === 'maintenance').length,
    total: totalEquipment.value,
    icon: 'fa-solid fa-tools',
    color: 'yellow',
    trend: '-3%',
    trendDirection: 'down'
  },
  {
    id: 'revenue',
    title: 'Ingresos del Mes',
    value: equipment.value
      .filter(e => e.status === 'rented')
      .reduce((sum, e) => sum + (e.dailyRate * 30), 0),
    total: null,
    icon: 'fa-solid fa-dollar-sign',
    color: 'purple',
    trend: '+15%',
    trendDirection: 'up',
    isCurrency: true
  }
]);

/**
 * Calcula los porcentajes de utilización por estado para el gráfico circular.
 * Evita divisiones por cero si no hay equipos.
 */
const utilizationData = computed(() => {
  const total = totalEquipment.value;
  if (total === 0) return { available: 0, rented: 0, maintenance: 0, outOfService: 0 };
  
  return {
    available: Math.round((equipment.value.filter(e => e.status === 'available').length / total) * 100),
    rented: Math.round((equipment.value.filter(e => e.status === 'rented').length / total) * 100),
    maintenance: Math.round((equipment.value.filter(e => e.status === 'maintenance').length / total) * 100),
    outOfService: Math.round((equipment.value.filter(e => e.status === 'out_of_service').length / total) * 100)
  };
});

/**
 * Datos simulados para el gráfico de ingresos mensuales.
 * En una implementación real, esto vendría del backend.
 */
const revenueData = computed(() => [
  { name: 'Ene', amount: 12500 },
  { name: 'Feb', amount: 15200 },
  { name: 'Mar', amount: 13800 },
  { name: 'Abr', amount: 16800 },
  { name: 'May', amount: 14200 },
  { name: 'Jun', amount: 18900 }
]);

const maxRevenue = computed(() => Math.max(...revenueData.value.map(m => m.amount)));

/**
 * Identifica los 5 equipos más rentables basándose en sus ingresos totales.
 * Calcula el ROI (Retorno de Inversión) para cada equipo.
 */
const topEquipment = computed(() => {
  return equipment.value
    .filter(e => e.rentalCount > 0)
    .map(e => ({
      ...e,
      roi: e.purchasePrice > 0 ? Math.round(((e.totalRevenue - e.purchasePrice) / e.purchasePrice) * 100) : 0
    }))
    .sort((a, b) => b.totalRevenue - a.totalRevenue)
    .slice(0, 5);
});

// Métodos

/**
 * Traduce el código de categoría a una etiqueta legible.
 * @param {string} category - Código de la categoría.
 * @returns {string} Etiqueta formateada.
 */
const getCategoryLabel = (category) => {
  const labels = {
    laptop: 'Laptop',
    desktop: 'Desktop',
    projector: 'Proyector',
    monitor: 'Monitor',
    accessory: 'Accesorio'
  };
  return labels[category] || category;
};

/**
 * Determina la clase CSS para el color del ROI según su valor.
 * - Verde: > 50%
 * - Amarillo: > 20%
 * - Rojo: <= 20%
 */
const getROIClass = (roi) => {
  if (roi > 50) return 'text-green-600';
  if (roi > 20) return 'text-yellow-600';
  return 'text-red-600';
};

const refreshUtilizationChart = () => {
  // Implementar actualización del gráfico
  console.log('Actualizando gráfico de utilización');
};

const refreshRevenueChart = () => {
  // Implementar actualización del gráfico de ingresos
  console.log('Actualizando gráfico de ingresos');
};

const refreshTopEquipment = () => {
  // Implementar actualización de equipos más rentables
  console.log('Actualizando equipos más rentables');
};

const refreshAlerts = () => {
  // Implementar actualización de alertas
  console.log('Actualizando alertas');
};

const removeAlert = (alertId) => {
  systemAlerts.value = systemAlerts.value.filter(alert => alert.id !== alertId);
};

/**
 * Carga alertas simuladas del sistema.
 * Estas alertas notifican sobre mantenimientos, devoluciones, etc.
 */
const loadSystemAlerts = () => {
  systemAlerts.value = [
    {
      id: 1,
      type: 'warning',
      icon: 'fa-solid fa-exclamation-triangle',
      title: 'Mantenimiento Programado',
      message: '3 equipos requieren mantenimiento preventivo esta semana'
    },
    {
      id: 2,
      type: 'info',
      icon: 'fa-solid fa-info-circle',
      title: 'Devolución Pendiente',
      message: '2 equipos tienen devoluciones programadas para hoy'
    },
    {
      id: 3,
      type: 'success',
      icon: 'fa-solid fa-check-circle',
      title: 'Mantenimiento Completado',
      message: 'El mantenimiento del Proyector Epson se completó exitosamente'
    }
  ];
};

// Lifecycle
onMounted(() => {
  loadSystemAlerts();
});
</script>

<style scoped>
/* Estilos adicionales si son necesarios */
</style>


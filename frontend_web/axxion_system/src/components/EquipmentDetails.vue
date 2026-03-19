<template>
  <div v-if="equipment" class="space-y-6">
    <!-- Header con información principal -->
    <div class="bg-gradient-to-r from-blue-500 to-blue-600 text-white p-6 rounded-lg">
      <div class="flex items-center justify-between">
        <div>
          <h2 class="text-2xl font-bold mb-2">{{ equipment.name }}</h2>
          <p class="text-blue-100">{{ equipment.brand }} {{ equipment.model }}</p>
          <p class="text-blue-200 text-sm">Serie: {{ equipment.serialNumber }}</p>
        </div>
        <div class="text-right">
          <StatusBadge :status="equipment.status" class="mb-2" />
          <p class="text-2xl font-bold">${{ equipment.dailyRate }}/día</p>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Columna principal -->
      <div class="lg:col-span-2 space-y-6">
        <!-- Especificaciones Técnicas -->
        <fwb-card>
          <template #header>
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-cogs" class="mr-2 text-blue-500"/>
              <h3 class="text-lg font-semibold">Especificaciones Técnicas</h3>
            </div>
          </template>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div v-if="equipment.specifications?.processor" class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-microchip" class="w-5 h-5 mr-3 text-blue-500"/>
              <div>
                <p class="text-sm text-gray-500">Procesador</p>
                <p class="font-medium">{{ equipment.specifications.processor }}</p>
              </div>
            </div>
            <div v-if="equipment.specifications?.ram" class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-memory" class="w-5 h-5 mr-3 text-green-500"/>
              <div>
                <p class="text-sm text-gray-500">Memoria RAM</p>
                <p class="font-medium">{{ equipment.specifications.ram }}</p>
              </div>
            </div>
            <div v-if="equipment.specifications?.storage" class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-hard-drive" class="w-5 h-5 mr-3 text-purple-500"/>
              <div>
                <p class="text-sm text-gray-500">Almacenamiento</p>
                <p class="font-medium">{{ equipment.specifications.storage }}</p>
              </div>
            </div>
            <div v-if="equipment.specifications?.graphics" class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-video" class="w-5 h-5 mr-3 text-red-500"/>
              <div>
                <p class="text-sm text-gray-500">Gráficos</p>
                <p class="font-medium">{{ equipment.specifications.graphics }}</p>
              </div>
            </div>
            <div v-if="equipment.specifications?.screen" class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-desktop" class="w-5 h-5 mr-3 text-yellow-500"/>
              <div>
                <p class="text-sm text-gray-500">Pantalla</p>
                <p class="font-medium">{{ equipment.specifications.screen }}</p>
              </div>
            </div>
            <div v-if="equipment.specifications?.os" class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-laptop-code" class="w-5 h-5 mr-3 text-indigo-500"/>
              <div>
                <p class="text-sm text-gray-500">Sistema Operativo</p>
                <p class="font-medium">{{ equipment.specifications.os }}</p>
              </div>
            </div>
          </div>
        </fwb-card>

        <!-- Historial de Alquileres -->
        <fwb-card>
          <template #header>
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <font-awesome-icon icon="fa-solid fa-history" class="mr-2 text-green-500"/>
                <h3 class="text-lg font-semibold">Historial de Alquileres</h3>
              </div>
              <fwb-button size="sm" gradient="blue" @click="loadRentalHistory">
                <font-awesome-icon icon="fa-solid fa-refresh" class="mr-1"/>
                Actualizar
              </fwb-button>
            </div>
          </template>
          <div v-if="rentalHistory.length > 0" class="space-y-3">
            <div 
              v-for="rental in rentalHistory" 
              :key="rental.id"
              class="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-700 rounded-lg"
            >
              <div>
                <p class="font-medium">{{ rental.clientName }}</p>
                <p class="text-sm text-gray-600 dark:text-gray-400">
                  {{ formatDate(rental.startDate) }} - {{ formatDate(rental.endDate) }}
                </p>
              </div>
              <div class="text-right">
                <p class="font-bold text-green-600">${{ rental.totalAmount }}</p>
                <StatusBadge :status="rental.status" />
              </div>
            </div>
          </div>
          <div v-else class="text-center py-8 text-gray-500">
            <font-awesome-icon icon="fa-solid fa-inbox" class="text-4xl mb-2"/>
            <p>No hay historial de alquileres</p>
          </div>
        </fwb-card>

        <!-- Historial de Mantenimiento -->
        <fwb-card>
          <template #header>
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <font-awesome-icon icon="fa-solid fa-tools" class="mr-2 text-yellow-500"/>
                <h3 class="text-lg font-semibold">Historial de Mantenimiento</h3>
              </div>
              <fwb-button size="sm" gradient="yellow" @click="loadMaintenanceHistory">
                <font-awesome-icon icon="fa-solid fa-refresh" class="mr-1"/>
                Actualizar
              </fwb-button>
            </div>
          </template>
          <div v-if="maintenanceHistory.length > 0" class="space-y-3">
            <div 
              v-for="maintenance in maintenanceHistory" 
              :key="maintenance.id"
              class="p-3 bg-gray-50 dark:bg-gray-700 rounded-lg"
            >
              <div class="flex items-center justify-between mb-2">
                <span class="font-medium">{{ maintenance.type }}</span>
                <span class="text-sm text-gray-600 dark:text-gray-400">
                  {{ formatDate(maintenance.date) }}
                </span>
              </div>
              <p class="text-sm text-gray-600 dark:text-gray-400 mb-2">
                {{ maintenance.description }}
              </p>
              <div class="flex items-center justify-between">
                <span class="text-sm">Técnico: {{ maintenance.technician }}</span>
                <span class="text-sm font-medium">${{ maintenance.cost }}</span>
              </div>
            </div>
          </div>
          <div v-else class="text-center py-8 text-gray-500">
            <font-awesome-icon icon="fa-solid fa-tools" class="text-4xl mb-2"/>
            <p>No hay historial de mantenimiento</p>
          </div>
        </fwb-card>
      </div>

      <!-- Columna lateral -->
      <div class="space-y-6">
        <!-- Información Financiera -->
        <fwb-card>
          <template #header>
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-dollar-sign" class="mr-2 text-green-500"/>
              <h3 class="text-lg font-semibold">Información Financiera</h3>
            </div>
          </template>
          <div class="space-y-4">
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">Tarifa Diaria</span>
              <span class="font-bold">${{ equipment.dailyRate }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">Tarifa Semanal</span>
              <span class="font-bold">${{ equipment.weeklyRate || 'N/A' }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">Tarifa Mensual</span>
              <span class="font-bold">${{ equipment.monthlyRate || 'N/A' }}</span>
            </div>
            <hr class="border-gray-200 dark:border-gray-600">
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">Precio de Compra</span>
              <span class="font-bold">${{ equipment.purchasePrice || 'N/A' }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">Valor Actual</span>
              <span class="font-bold">${{ equipment.currentValue || 'N/A' }}</span>
            </div>
            <hr class="border-gray-200 dark:border-gray-600">
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">Total Alquileres</span>
              <span class="font-bold">{{ equipment.rentalCount || 0 }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">Ingresos Totales</span>
              <span class="font-bold text-green-600">${{ (equipment.totalRevenue || 0).toLocaleString() }}</span>
            </div>
          </div>
        </fwb-card>

        <!-- Información del Equipo -->
        <fwb-card>
          <template #header>
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-info-circle" class="mr-2 text-blue-500"/>
              <h3 class="text-lg font-semibold">Información del Equipo</h3>
            </div>
          </template>
          <div class="space-y-4">
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Categoría</span>
              <span class="font-medium">{{ getCategoryLabel(equipment.category) }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Condición</span>
              <span class="font-medium">{{ getConditionLabel(equipment.condition) }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Ubicación</span>
              <span class="font-medium">{{ equipment.location || 'No especificada' }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Fecha de Compra</span>
              <span class="font-medium">{{ formatDate(equipment.purchaseDate) || 'No especificada' }}</span>
            </div>
            <div v-if="equipment.lastMaintenance">
              <span class="text-gray-600 dark:text-gray-400 block">Último Mantenimiento</span>
              <span class="font-medium">{{ formatDate(equipment.lastMaintenance) }}</span>
            </div>
            <div v-if="equipment.nextMaintenance">
              <span class="text-gray-600 dark:text-gray-400 block">Próximo Mantenimiento</span>
              <span class="font-medium">{{ formatDate(equipment.nextMaintenance) }}</span>
            </div>
          </div>
        </fwb-card>

        <!-- Alquiler Actual -->
        <fwb-card v-if="equipment.status === 'rented' && equipment.currentRental">
          <template #header>
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-handshake" class="mr-2 text-green-500"/>
              <h3 class="text-lg font-semibold">Alquiler Actual</h3>
            </div>
          </template>
          <div class="space-y-4">
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Cliente</span>
              <span class="font-medium">{{ equipment.currentRental.clientName }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Fecha de Inicio</span>
              <span class="font-medium">{{ formatDate(equipment.currentRental.startDate) }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Fecha de Fin</span>
              <span class="font-medium">{{ formatDate(equipment.currentRental.endDate) }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Monto Total</span>
              <span class="font-bold text-green-600">${{ equipment.currentRental.totalAmount }}</span>
            </div>
          </div>
        </fwb-card>

        <!-- Mantenimiento Programado -->
        <fwb-card v-if="equipment.status === 'maintenance' && equipment.maintenanceSchedule">
          <template #header>
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-tools" class="mr-2 text-yellow-500"/>
              <h3 class="text-lg font-semibold">Mantenimiento Programado</h3>
            </div>
          </template>
          <div class="space-y-4">
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Tipo</span>
              <span class="font-medium">{{ equipment.maintenanceSchedule.type }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Fecha Programada</span>
              <span class="font-medium">{{ formatDate(equipment.maintenanceSchedule.scheduledDate) }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Descripción</span>
              <span class="font-medium">{{ equipment.maintenanceSchedule.description }}</span>
            </div>
            <div>
              <span class="text-gray-600 dark:text-gray-400 block">Técnico</span>
              <span class="font-medium">{{ equipment.maintenanceSchedule.technician }}</span>
            </div>
          </div>
        </fwb-card>

        <!-- Notas -->
        <fwb-card v-if="equipment.notes">
          <template #header>
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-sticky-note" class="mr-2 text-orange-500"/>
              <h3 class="text-lg font-semibold">Notas</h3>
            </div>
          </template>
          <p class="text-gray-700 dark:text-gray-300">{{ equipment.notes }}</p>
        </fwb-card>

        <!-- Botones de Acción -->
        <fwb-card>
          <template #header>
            <div class="flex items-center">
              <font-awesome-icon icon="fa-solid fa-cogs" class="mr-2 text-purple-500"/>
              <h3 class="text-lg font-semibold">Acciones</h3>
            </div>
          </template>
          <div class="space-y-3">
            <fwb-button 
              gradient="blue" 
              class="w-full"
              @click="$emit('edit', equipment)"
            >
              <font-awesome-icon icon="fa-solid fa-edit" class="mr-2"/>
              Editar Equipo
            </fwb-button>
            
            <fwb-button 
              v-if="equipment.status === 'available'"
              gradient="green" 
              class="w-full"
              @click="rentEquipment"
            >
              <font-awesome-icon icon="fa-solid fa-handshake" class="mr-2"/>
              Alquilar Equipo
            </fwb-button>
            
            <fwb-button 
              gradient="yellow" 
              class="w-full"
              @click="scheduleMaintenance"
            >
              <font-awesome-icon icon="fa-solid fa-tools" class="mr-2"/>
              Programar Mantenimiento
            </fwb-button>
            
            <fwb-button 
              gradient="purple" 
              class="w-full"
              @click="generateReport"
            >
              <font-awesome-icon icon="fa-solid fa-file-export" class="mr-2"/>
              Generar Reporte
            </fwb-button>
          </div>
        </fwb-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useInventoryStore } from '@/stores/inventory.js';
import { FwbCard, FwbButton } from 'flowbite-vue';
import StatusBadge from './StatusBadge.vue';

/**
 * Componente EquipmentDetails.
 * 
 * Muestra la información detallada de un equipo específico seleccionado.
 * Incluye:
 * - Información básica y estado actual.
 * - Especificaciones técnicas completas.
 * - Historial de alquileres y mantenimientos.
 * - Información financiera y ROI.
 * - Acciones disponibles (editar, alquilar, mantener, reportar).
 */

// Props
// equipment: Objeto con todos los datos del equipo a visualizar.
const props = defineProps({
  equipment: {
    type: Object,
    required: true
  }
});

// Emits
const emit = defineEmits(['close', 'edit']);

// Store
const inventoryStore = useInventoryStore();

// Estado
const rentalHistory = ref([]);
const maintenanceHistory = ref([]);

// Métodos

/**
 * Obtiene la etiqueta legible para una categoría.
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

const getConditionLabel = (condition) => {
  const labels = {
    excellent: 'Excelente',
    good: 'Buena',
    fair: 'Regular',
    poor: 'Mala'
  };
  return labels[condition] || condition;
};

/**
 * Formatea una fecha ISO a un formato local legible (ej. "15 de octubre de 2023").
 */
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

/**
 * Carga el historial de alquileres del equipo desde el store.
 * Si falla, usa datos de ejemplo para visualización.
 */
const loadRentalHistory = async () => {
  try {
    rentalHistory.value = await inventoryStore.getRentalHistory(props.equipment.id);
  } catch (error) {
    console.error('Error al cargar historial de alquileres:', error);
    // Datos de ejemplo para desarrollo
    rentalHistory.value = [
      {
        id: 1,
        clientName: 'María González',
        startDate: '2023-12-01',
        endDate: '2023-12-15',
        totalAmount: 375.00,
        status: 'completed'
      },
      {
        id: 2,
        clientName: 'Carlos Rodríguez',
        startDate: '2023-11-10',
        endDate: '2023-11-25',
        totalAmount: 450.00,
        status: 'completed'
      }
    ];
  }
};

/**
 * Carga el historial de mantenimientos del equipo.
 */
const loadMaintenanceHistory = async () => {
  try {
    maintenanceHistory.value = await inventoryStore.getMaintenanceHistory(props.equipment.id);
  } catch (error) {
    console.error('Error al cargar historial de mantenimiento:', error);
    // Datos de ejemplo para desarrollo
    maintenanceHistory.value = [
      {
        id: 1,
        type: 'Preventivo',
        date: '2023-10-15',
        description: 'Limpieza general y actualización de software',
        technician: 'Juan Pérez',
        cost: 50.00
      },
      {
        id: 2,
        type: 'Correctivo',
        date: '2023-08-20',
        description: 'Reparación de teclado y reemplazo de batería',
        technician: 'Ana López',
        cost: 120.00
      }
    ];
  }
};

const rentEquipment = () => {
  // Implementar lógica de alquiler
  console.log('Alquilar equipo:', props.equipment);
};

const scheduleMaintenance = () => {
  // Implementar lógica de programación de mantenimiento
  console.log('Programar mantenimiento para:', props.equipment);
};

const generateReport = () => {
  // Implementar lógica de generación de reportes
  console.log('Generar reporte para:', props.equipment);
};

// Lifecycle
onMounted(() => {
  loadRentalHistory();
  loadMaintenanceHistory();
});
</script>

<style scoped>
/* Estilos adicionales si son necesarios */
</style>


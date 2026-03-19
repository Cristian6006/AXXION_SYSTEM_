<template>
  <fwb-card class="equipment-card hover:shadow-lg transition-shadow duration-300">
    <!-- Imagen del equipo -->
    <div class="relative">
      <img 
        :src="equipmentImage" 
        :alt="equipment.nombre || equipment.name"
        class="w-full h-48 object-cover rounded-t-lg"
        @error="handleImageError"
      />
      
      <!-- Badge de estado -->
      <div class="absolute top-3 right-3">
        <StatusBadge :status="getMappedStatus(equipment)" />
      </div>
      
      <!-- Badge de categoría -->
      <div class="absolute top-3 left-3">
        <span class="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded-full">
          {{ getCategoryLabel(equipment.categoria || equipment.category) }}
        </span>
      </div>
    </div>

    <!-- Contenido de la tarjeta -->
    <div class="p-6">
      <!-- Información principal -->
      <div class="mb-4">
        <h3 class="text-lg font-semibold text-gray-900 dark:text-white mb-1">
          {{ equipment.nombre || equipment.name }}
        </h3>
        <p class="text-sm text-gray-600 dark:text-gray-400 mb-2">
          {{ equipment.marca || equipment.brand }} {{ equipment.modelo || equipment.model }}
        </p>
        <p class="text-xs text-gray-500 dark:text-gray-500">
          Serie: {{ equipment.numero_serie || equipment.serialNumber }}
        </p>
        <p v-if="equipment.ubicacion_fisica" class="text-xs text-gray-500 dark:text-gray-500">
          Ubicación: {{ equipment.ubicacion_fisica }}
        </p>
      </div>

      <!-- Especificaciones principales -->
      <div class="mb-4 space-y-2">
        <div v-if="getSpecifications?.processor" class="flex items-center text-sm text-gray-600 dark:text-gray-400">
          <font-awesome-icon icon="fa-solid fa-microchip" class="w-4 h-4 mr-2 text-blue-500"/>
          {{ getSpecifications.processor }}
        </div>
        <div v-if="getSpecifications?.ram" class="flex items-center text-sm text-gray-600 dark:text-gray-400">
          <font-awesome-icon icon="fa-solid fa-memory" class="w-4 h-4 mr-2 text-green-500"/>
          {{ getSpecifications.ram }}
        </div>
        <div v-if="getSpecifications?.storage" class="flex items-center text-sm text-gray-600 dark:text-gray-400">
          <font-awesome-icon icon="fa-solid fa-hard-drive" class="w-4 h-4 mr-2 text-purple-500"/>
          {{ getSpecifications.storage }}
        </div>
      </div>

      <!-- Información de alquiler -->
      <div class="mb-4 p-3 bg-gray-50 dark:bg-gray-700 rounded-lg">
        <div class="flex justify-between items-center mb-2">
          <span class="text-sm font-medium text-gray-700 dark:text-gray-300">Tarifa Diaria</span>
          <span class="text-lg font-bold text-green-600">${{ equipment.precio_alquiler_dia || equipment.dailyRate || 0 }}</span>
        </div>
        <div class="flex justify-between items-center text-sm text-gray-600 dark:text-gray-400">
          <span>Semanal: ${{ equipment.precio_alquiler_semanal || equipment.weeklyRate || 0 }}</span>
          <span>Mensual: ${{ equipment.precio_alquiler_mensual || equipment.monthlyRate || 0 }}</span>
        </div>
        <div class="flex justify-between items-center text-sm text-gray-600 dark:text-gray-400">
          <span>Ingresos:</span>
          <span>${{ (equipment.totalRevenue || 0).toLocaleString() }}</span>
        </div>
      </div>

      <!-- Información de alquiler actual -->
      <div v-if="getCurrentRental" class="mb-4 p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg">
        <div class="flex items-center mb-2">
          <font-awesome-icon icon="fa-solid fa-user" class="w-4 h-4 mr-2 text-blue-500"/>
          <span class="text-sm font-medium text-blue-700 dark:text-blue-300">Cliente Actual</span>
        </div>
        <p class="text-sm text-blue-600 dark:text-blue-400">{{ getCurrentRental.clientName || getCurrentRental.cliente?.nombre || 'N/A' }}</p>
        <p class="text-xs text-blue-500 dark:text-blue-500">
          Hasta: {{ formatDate(getCurrentRental.fecha_fin || getCurrentRental.endDate) }}
        </p>
      </div>

      <!-- Información de mantenimiento -->
      <div v-if="(equipment.estado === 'mantenimiento' || equipment.status === 'maintenance') && getCurrentMaintenance" class="mb-4 p-3 bg-yellow-50 dark:bg-yellow-900/20 rounded-lg">
        <div class="flex items-center mb-2">
          <font-awesome-icon icon="fa-solid fa-tools" class="w-4 h-4 mr-2 text-yellow-500"/>
          <span class="text-sm font-medium text-yellow-700 dark:text-yellow-300">En Mantenimiento</span>
        </div>
        <p class="text-sm text-yellow-600 dark:text-yellow-400">{{ getCurrentMaintenance.descripcion || getCurrentMaintenance.description || 'Mantenimiento programado' }}</p>
        <p class="text-xs text-yellow-500 dark:text-yellow-500">
          Técnico: {{ getCurrentMaintenance.tecnico_asignado || getCurrentMaintenance.technician || 'N/A' }}
        </p>
        <p v-if="getCurrentMaintenance.fecha_fin" class="text-xs text-yellow-500 dark:text-yellow-500">
          Hasta: {{ formatDate(getCurrentMaintenance.fecha_fin) }}
        </p>
      </div>

      <!-- Botones de acción -->
      <div class="flex flex-wrap gap-2">
        <fwb-button 
          size="sm" 
          gradient="blue" 
          @click="emit('view-details', equipment)"
          class="flex-1"
        >
          <font-awesome-icon icon="fa-solid fa-eye" class="mr-1"/>
          Ver
        </fwb-button>
        
        <fwb-button 
          v-if="getMappedStatus(equipment) === 'disponible'"
          size="sm" 
          gradient="green" 
          @click="emit('rent-equipment', equipment)"
          class="flex-1"
        >
          <font-awesome-icon icon="fa-solid fa-handshake" class="mr-1"/>
          Alquilar
        </fwb-button>
        
        <fwb-button 
          v-if="getMappedStatus(equipment) === 'disponible'"
          size="sm" 
          gradient="yellow" 
          @click="emit('maintenance-equipment', equipment)"
          class="flex-1"
        >
          <font-awesome-icon icon="fa-solid fa-tools" class="mr-1"/>
          Mantener
        </fwb-button>
        
        <fwb-button 
          size="sm" 
          gradient="purple" 
          @click="emit('edit-equipment', equipment)"
          class="flex-1"
        >
          <font-awesome-icon icon="fa-solid fa-edit" class="mr-1"/>
          Editar
        </fwb-button>
        
        <fwb-button 
          size="sm" 
          gradient="red" 
          @click="emit('delete-equipment', equipment)"
          class="flex-1"
        >
          <font-awesome-icon icon="fa-solid fa-trash" class="mr-1"/>
          Eliminar
        </fwb-button>
      </div>
    </div>
  </fwb-card>
</template>

<script setup>
import { computed } from 'vue';
import { FwbCard, FwbButton } from 'flowbite-vue';
import StatusBadge from './StatusBadge.vue';

/**
 * Componente EquipmentCard.
 * 
 * Tarjeta rica en información para mostrar un equipo individual en el inventario.
 * Muestra:
 * - Imagen y estado (badge).
 * - Detalles técnicos (procesador, RAM, etc.).
 * - Información financiera y de rentas activas.
 * - Botones de acción (alquilar, mantener, editar, eliminar).
 */

// Props
const props = defineProps({
  equipment: {
    type: Object,
    required: true
  }
});

// Emits
// Emits: Eventos que dispara la tarjeta hacia el componente padre
const emit = defineEmits([
  'view-details',        // Ver detalles completos
  'edit-equipment',      // Abrir modal de edición
  'delete-equipment',    // Solicitar eliminación
  'rent-equipment',      // Iniciar flujo de renta
  'maintenance-equipment' // Iniciar flujo de mantenimiento
]);

// Computed: Gestiona la imagen del equipo.
// Si no hay imagen cargada, devuelve una por defecto basada en la categoría.
const equipmentImage = computed(() => {
  if (props.equipment.images && props.equipment.images.length > 0) {
    return props.equipment.images[0];
  }
  
  // Usar imagen por defecto desde assets
  return new URL('@/assets/img/items.png', import.meta.url).href;
});

const getSpecifications = computed(() => {
  return props.equipment.especificaciones || props.equipment.specifications || {};
});

// Obtener alquiler actual
// Computed: Identifica si el equipo está actualmente alquilado.
// Intenta obtener la información de 'renta_activa' (backend) o buscar en el array de rentas.
const getCurrentRental = computed(() => {
  // Debug logging
  console.log('Equipment data:', props.equipment);
  console.log('renta_activa:', props.equipment.renta_activa);
  
  // Primero verificar si viene renta_activa del backend
  if (props.equipment.renta_activa) {
    console.log('Found renta_activa:', props.equipment.renta_activa);
    return {
      clientName: props.equipment.renta_activa.cliente_nombre,
      fecha_fin: props.equipment.renta_activa.fecha_fin_prevista,
      endDate: props.equipment.renta_activa.fecha_fin_prevista,
      estado: props.equipment.renta_activa.estado_renta
    };
  }
  
  // Fallback al método anterior
  if (props.equipment.rentas && Array.isArray(props.equipment.rentas)) {
    const rentaActiva = props.equipment.rentas.find(renta => 
      renta.estado === 'Activa' || renta.estado === 'active' || !renta.fecha_fin
    );
    if (rentaActiva) return rentaActiva;
  }
  
  return props.equipment.currentRental || null;
});

// Obtener mantenimiento actual
const getCurrentMaintenance = computed(() => {
  if (props.equipment.mantenimientos && Array.isArray(props.equipment.mantenimientos)) {
    return props.equipment.mantenimientos.find(m => 
      ['PROGRAMADO', 'EN_PROCESO'].includes(m.estado_mantenimiento || m.estado)
    ) || null;
  }
  return props.equipment.maintenanceSchedule || null;
});

// Métodos
const getCategoryLabel = (category) => {
  const labels = {
    laptop: 'Laptop',
    desktop: 'Desktop',
    projector: 'Proyector',
    monitor: 'Monitor',
    accessory: 'Accesorio',
    'Iluminación': 'Iluminación',
    'Equipos de Sonido': 'Equipos de Sonido'
  };
  return labels[category] || category;
};

// Mapear estado para compatibilidad
// Normaliza el estado del equipo para que coincida con los valores esperados por StatusBadge.
// Prioriza 'renta_activa', luego 'estado_item' y finalmente 'estado'/'status'.
const getMappedStatus = (equipment) => {
  // Verificar primero si hay renta activa
  if (equipment.renta_activa) {
    return 'alquilado';
  }
  
  // Si tiene estado_item, mapearlo
  if (equipment.estado_item) {
    const estadoMap = {
      'Disponible': 'disponible',
      'Rentado': 'alquilado',
      'EnMantenimiento': 'mantenimiento',
      'DeBaja': 'de_baja'
    };
    return estadoMap[equipment.estado_item] || equipment.estado_item.toLowerCase();
  }
  
  // Usar estado normal si existe
  return equipment.estado || equipment.status || 'disponible';
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

const handleImageError = (event) => {
  // Si la imagen falla, usar imagen por defecto
  event.target.src = '/images/defaults/equipment.jpg';
};
</script>

<style scoped>
.equipment-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.equipment-card .p-6 {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.equipment-card .flex.flex-wrap.gap-2 {
  margin-top: auto;
}
</style>


<template>
  <span 
    :class="badgeClasses"
    class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
  >
    <font-awesome-icon 
      :icon="statusIcon" 
      class="w-3 h-3 mr-1"
    />
    {{ statusLabel }}
  </span>
</template>

<script setup>
import { computed } from 'vue';

/**
 * Componente StatusBadge.
 * 
 * Muestra una etiqueta (badge) con color e icono según el estado proporcionado.
 * Normaliza estados en inglés y español para mantener consistencia visual.
 * Soporta estados de inventario, alquiler, mantenimiento y usuarios.
 */

// Props
// Props
// status: El string de estado a visualizar (ej. 'available', 'disponible', 'active').
// size: Tamaño del badge ('sm', 'md', 'lg').
const props = defineProps({
  status: {
    type: String,
    required: true
  },
  size: {
    type: String,
    default: 'sm',
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  }
});

// Computed
// Computed: Configuración centralizada de estados.
// Mapea cada estado posible a su etiqueta legible, icono y clases de color.
const statusConfig = computed(() => {
  const configs = {
    // Estados de inventario (inglés - compatibilidad)
    available: {
      label: 'Disponible',
      icon: 'fa-solid fa-check-circle',
      classes: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300'
    },
    rented: {
      label: 'Alquilado',
      icon: 'fa-solid fa-handshake',
      classes: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300'
    },
    maintenance: {
      label: 'Mantenimiento',
      icon: 'fa-solid fa-tools',
      classes: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300'
    },
    out_of_service: {
      label: 'Fuera de Servicio',
      icon: 'fa-solid fa-times-circle',
      classes: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300'
    },
    
    // Estados de inventario (español - nuevos)
    disponible: {
      label: 'Disponible',
      icon: 'fa-solid fa-check-circle',
      classes: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300'
    },
    alquilado: {
      label: 'Alquilado',
      icon: 'fa-solid fa-handshake',
      classes: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300'
    },
    mantenimiento: {
      label: 'Mantenimiento',
      icon: 'fa-solid fa-tools',
      classes: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300'
    },
    fuera_de_servicio: {
      label: 'Fuera de Servicio',
      icon: 'fa-solid fa-times-circle',
      classes: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300'
    },
    
    // Estados de alquiler
    active: {
      label: 'Activo',
      icon: 'fa-solid fa-play-circle',
      classes: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300'
    },
    completed: {
      label: 'Completado',
      icon: 'fa-solid fa-check-circle',
      classes: 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300'
    },
    cancelled: {
      label: 'Cancelado',
      icon: 'fa-solid fa-times-circle',
      classes: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300'
    },
    overdue: {
      label: 'Vencido',
      icon: 'fa-solid fa-exclamation-triangle',
      classes: 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-300'
    },
    
    // Estados de mantenimiento
    scheduled: {
      label: 'Programado',
      icon: 'fa-solid fa-calendar',
      classes: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300'
    },
    in_progress: {
      label: 'En Progreso',
      icon: 'fa-solid fa-spinner',
      classes: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300'
    },
    completed_maintenance: {
      label: 'Completado',
      icon: 'fa-solid fa-check-circle',
      classes: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300'
    },
    
    // Estados de usuario
    active_user: {
      label: 'Activo',
      icon: 'fa-solid fa-user-check',
      classes: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300'
    },
    inactive_user: {
      label: 'Inactivo',
      icon: 'fa-solid fa-user-times',
      classes: 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300'
    },
    pending_user: {
      label: 'Pendiente',
      icon: 'fa-solid fa-user-clock',
      classes: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300'
    }
  };
  
  return configs[props.status] || {
    label: props.status,
    icon: 'fa-solid fa-question-circle',
    classes: 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300'
  };
});

const statusLabel = computed(() => statusConfig.value.label);
const statusIcon = computed(() => statusConfig.value.icon);

// Computed: Combina las clases base de color con las clases de tamaño.
const badgeClasses = computed(() => {
  const baseClasses = statusConfig.value.classes;
  const sizeClasses = {
    sm: 'px-2.5 py-0.5 text-xs',
    md: 'px-3 py-1 text-sm',
    lg: 'px-4 py-1.5 text-base'
  };
  
  return `${baseClasses} ${sizeClasses[props.size]}`;
});
</script>

<style scoped>
/* Estilos adicionales si son necesarios */
</style>


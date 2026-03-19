<template>
  <fwb-card 
    :class="cardClasses"
    class="transition-all duration-300 hover:shadow-lg"
  >
    <div class="p-6">
      <div class="flex items-center justify-between">
        <div class="flex-1">
          <p class="text-sm font-medium opacity-90 mb-1">
            {{ metric.title }}
          </p>
          <div class="flex items-baseline">
            <p class="text-3xl font-bold">
              {{ formattedValue }}
            </p>
            <p v-if="metric.total" class="text-sm opacity-75 ml-2">
              / {{ metric.total }}
            </p>
          </div>
          <div v-if="metric.trend" class="flex items-center mt-2">
            <font-awesome-icon 
              :icon="trendIcon" 
              :class="trendIconClasses"
              class="w-4 h-4 mr-1"
            />
            <span :class="trendTextClasses" class="text-sm font-medium">
              {{ metric.trend }}
            </span>
            <span class="text-sm opacity-75 ml-1">vs mes anterior</span>
          </div>
        </div>
        <div class="ml-4">
          <div :class="iconContainerClasses" class="w-16 h-16 rounded-full flex items-center justify-center">
            <font-awesome-icon 
              :icon="metric.icon" 
              class="text-2xl"
            />
          </div>
        </div>
      </div>
      
      <!-- Barra de progreso si hay total -->
      <div v-if="metric.total && metric.total > 0" class="mt-4">
        <div class="bg-white bg-opacity-20 rounded-full h-2">
          <div 
            :class="progressBarClasses"
            class="h-2 rounded-full transition-all duration-500"
            :style="{ width: `${progressPercentage}%` }"
          ></div>
        </div>
      </div>
    </div>
  </fwb-card>
</template>

<script setup>
import { computed } from 'vue';
import { FwbCard } from 'flowbite-vue';

/**
 * Componente MetricCard.
 * 
 * Tarjeta para visualizar una métrica individual en el dashboard.
 * Soporta:
 * - Título y valor principal.
 * - Comparación con un total (barra de progreso).
 * - Indicador de tendencia (subida/bajada).
 * - Estilos dinámicos basados en color.
 */

// Props
// Props
// metric: Objeto con los datos de la métrica (title, value, total, color, icon, trend, etc.).
// loading: Estado de carga (actualmente no utilizado en el template, pero disponible).
const props = defineProps({
  metric: {
    type: Object,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  }
});

// Computed
// Computed: Genera las clases CSS para el fondo de la tarjeta según el color configurado.
const cardClasses = computed(() => {
  const colorMap = {
    blue: 'bg-gradient-to-r from-blue-500 to-blue-600 text-white',
    green: 'bg-gradient-to-r from-green-500 to-green-600 text-white',
    yellow: 'bg-gradient-to-r from-yellow-500 to-yellow-600 text-white',
    purple: 'bg-gradient-to-r from-purple-500 to-purple-600 text-white',
    red: 'bg-gradient-to-r from-red-500 to-red-600 text-white',
    indigo: 'bg-gradient-to-r from-indigo-500 to-indigo-600 text-white',
    pink: 'bg-gradient-to-r from-pink-500 to-pink-600 text-white',
    gray: 'bg-gradient-to-r from-gray-500 to-gray-600 text-white'
  };
  
  return colorMap[props.metric.color] || colorMap.blue;
});

const iconContainerClasses = computed(() => {
  const colorMap = {
    blue: 'bg-blue-100 bg-opacity-20',
    green: 'bg-green-100 bg-opacity-20',
    yellow: 'bg-yellow-100 bg-opacity-20',
    purple: 'bg-purple-100 bg-opacity-20',
    red: 'bg-red-100 bg-opacity-20',
    indigo: 'bg-indigo-100 bg-opacity-20',
    pink: 'bg-pink-100 bg-opacity-20',
    gray: 'bg-gray-100 bg-opacity-20'
  };
  
  return colorMap[props.metric.color] || colorMap.blue;
});

const progressBarClasses = computed(() => {
  const colorMap = {
    blue: 'bg-blue-200',
    green: 'bg-green-200',
    yellow: 'bg-yellow-200',
    purple: 'bg-purple-200',
    red: 'bg-red-200',
    indigo: 'bg-indigo-200',
    pink: 'bg-pink-200',
    gray: 'bg-gray-200'
  };
  
  return colorMap[props.metric.color] || colorMap.blue;
});

// Computed: Formatea el valor numérico, añadiendo símbolo de moneda si es necesario.
const formattedValue = computed(() => {
  if (props.metric.isCurrency) {
    return `$${props.metric.value.toLocaleString()}`;
  }
  return props.metric.value.toLocaleString();
});

const progressPercentage = computed(() => {
  if (!props.metric.total || props.metric.total === 0) return 0;
  return Math.round((props.metric.value / props.metric.total) * 100);
});

// Computed: Determina el icono de tendencia (flecha arriba/abajo/guion).
const trendIcon = computed(() => {
  if (props.metric.trendDirection === 'up') {
    return 'fa-solid fa-arrow-up';
  } else if (props.metric.trendDirection === 'down') {
    return 'fa-solid fa-arrow-down';
  }
  return 'fa-solid fa-minus';
});

const trendIconClasses = computed(() => {
  if (props.metric.trendDirection === 'up') {
    return 'text-green-200';
  } else if (props.metric.trendDirection === 'down') {
    return 'text-red-200';
  }
  return 'text-gray-200';
});

const trendTextClasses = computed(() => {
  if (props.metric.trendDirection === 'up') {
    return 'text-green-200';
  } else if (props.metric.trendDirection === 'down') {
    return 'text-red-200';
  }
  return 'text-gray-200';
});
</script>

<style scoped>
/* Estilos adicionales si son necesarios */
</style>


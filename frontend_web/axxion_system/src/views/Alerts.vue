<template>
  <div class="App flex">
    <SideBar />
    <main class="container h-screen p-4 flex-1 overflow-y-auto text-white">
      <headerP />
      <h1 class="text-3xl font-bold mb-6">Alertas del Sistema</h1>
      <div class="flex gap-4 mb-6">
        <button
          v-for="filtro in alertStore.filtros"
          :key="filtro"
          @click="alertStore.setFiltroActivo(filtro)"
          :class="['px-4 py-2 rounded', alertStore.filtroActivo === filtro ? 'bg-blue-600' : 'bg-gray-700']"
        >
          {{ filtro }}
        </button>
      </div>
      <div v-if="alertStore.isLoading">Cargando alertas...</div>
      <div v-else-if="alertStore.error" class="text-red-400">{{ alertStore.error }}</div>
      <div v-else class="space-y-4">
        <div
          v-for="(alerta, index) in alertStore.alertasFiltradas"
          :key="alerta.id || index" 
          class="p-4 rounded-md"
          :class="{
            'bg-red-900': alerta.nivel === 'Crítica',
            'bg-yellow-800': alerta.nivel === 'Advertencia',
            'bg-blue-800': alerta.nivel === 'Informativa'
          }"
        >
          <div class="flex justify-between items-center mb-2">
            <span class="font-bold">{{ alerta.nivel }}</span>
            <span class="text-sm text-gray-300">{{ formatDate(alerta.fecha) }}</span>
          </div>
          <h2 class="text-xl font-semibold">{{ alerta.titulo }}</h2>
          <p class="text-gray-200">{{ alerta.mensaje }}</p>
          <div class="mt-2 text-sm bg-gray-700 px-2 py-1 rounded inline-block">
            {{ alerta.referencia }}
          </div>
        </div>
        <div v-if="!alertStore.alertasFiltradas.length" class="text-gray-400">
            No hay alertas para mostrar en esta categoría.
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useAlertStore } from "@/stores/alertStore";

import SideBar from "@/components/SideBar.vue";
import headerP from "@/components/headerP.vue";

/**
 * Vista Alerts.
 * 
 * Muestra el panel de alertas del sistema.
 * Permite filtrar las alertas por nivel (Crítica, Advertencia, Informativa)
 * y visualizarlas con códigos de colores correspondientes.
 */

const alertStore = useAlertStore();

// Formatea la fecha de la alerta para mostrarla en formato local.
const formatDate = (date) => new Date(date).toLocaleString();

onMounted(() => {
  // Carga las alertas al montar la vista.
  alertStore.fetchAlerts();
});
</script>

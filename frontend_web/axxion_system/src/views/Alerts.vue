<template>
  <div class="App flex min-h-screen bg-slate-50 overflow-hidden font-sans">
    <SideBar />

    <main class="container mx-auto h-screen p-6 md:p-8 flex-1 overflow-y-auto text-slate-800 relative z-0">
      <div class="absolute top-0 left-1/4 w-96 h-96 bg-blue-300/30 rounded-full mix-blend-multiply filter blur-3xl opacity-60 animate-pulse pointer-events-none -z-10"></div>
      <div class="absolute bottom-1/4 right-1/4 w-96 h-96 bg-indigo-300/30 rounded-full mix-blend-multiply filter blur-3xl opacity-60 pointer-events-none -z-10"></div>

      <headerP class="mb-4 relative z-10" />

      <div class="max-w-6xl mx-auto relative z-10">
        <!-- Header Section -->
        <header class="mb-10 flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200/80 pb-6">
          <div class="flex items-center gap-4">
            <div class="p-3 bg-white rounded-2xl border border-slate-200 shadow-sm backdrop-blur-md">
              <BellAlertIcon class="w-8 h-8 text-emerald-500/90" />
            </div>
            <div>
              <h1 class="text-4xl font-extrabold tracking-tight text-transparent bg-clip-text bg-gradient-to-br from-slate-900 to-slate-600">
                Alertas del Sistema
              </h1>
              <p class="text-sm text-slate-500 mt-1 font-medium">Monitoreo y notificaciones en tiempo real</p>
            </div>
          </div>

          <div class="flex flex-wrap lg:inline-flex bg-white/80 backdrop-blur-md p-1.5 rounded-xl border border-slate-200 shadow-sm gap-1">
            <button
              v-for="filtro in alertStore.filtros"
              :key="filtro"
              @click="alertStore.setFiltroActivo(filtro)"
              class="relative px-4 py-2 rounded-lg text-sm font-semibold transition-all duration-300 ease-out focus:outline-none focus-visible:ring-2 focus-visible:ring-emerald-500/90"
              :class="alertStore.filtroActivo === filtro
                ? 'text-white'
                : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100'"
            >
              <span v-if="alertStore.filtroActivo === filtro" class="absolute inset-0 bg-emerald-500/90 rounded-lg -z-10 shadow-md shadow-blue-500/30"></span>
              {{ filtro }}
            </button>
          </div>
        </header>

        <div class="min-h-[400px]">
          <div v-if="alertStore.isLoading" class="flex flex-col items-center justify-center h-64 space-y-4">
            <ArrowPathIcon class="w-10 h-10 text-emerald-500/90 animate-spin" />
            <p class="text-slate-500 font-medium animate-pulse">Sincronizando alertas...</p>
          </div>

          <div v-else-if="alertStore.error" class="bg-red-50 border border-red-200 rounded-2xl p-6 flex items-start gap-4 backdrop-blur-sm">
            <ExclamationTriangleIcon class="w-8 h-8 text-red-600 shrink-0" />
            <div>
              <h3 class="text-red-800 font-bold text-lg">Error de conexión</h3>
              <p class="text-red-700/80 mt-1">{{ alertStore.error }}</p>
            </div>
          </div>

          <!-- Alerts List -->
          <TransitionGroup
            v-else
            name="list"
            tag="div"
            class="grid grid-cols-1 md:grid-cols-2 gap-6"
          >
            <div
              v-for="alerta in alertStore.alertasFiltradas"
              :key="alerta.id || alerta.titulo"
              class="group relative bg-slate-900 backdrop-blur-xl border border-slate-200/60 rounded-2xl p-6 transition-all duration-300 hover:-translate-y-1 hover:shadow-xl hover:shadow-slate-200 hover:bg-white overflow-hidden shadow-sm"
            >
              <!-- Colored lateral border indicator -->
              <div
                class="absolute left-0 top-0 bottom-0 w-1.5 transition-all duration-300 group-hover:w-2"
                :class="{
                  'bg-gradient-to-b from-red-500 to-red-600 shadow-[2px_0_8px_rgba(239,68,68,0.25)]': alerta.nivel === 'Crítica',
                  'bg-gradient-to-b from-amber-400 to-orange-500 shadow-[2px_0_8px_rgba(245,158,11,0.25)]': alerta.nivel === 'Advertencia',
                  'bg-gradient-to-b from-emerald-500/65 to-emerald-500/90 shadow-[2px_0_8px_rgba(59,130,246,0.25)]': alerta.nivel === 'Informativa'
                }"
              ></div>

              <!-- Header row of the card -->
              <div class="flex justify-between items-start mb-4 pl-2">
                <div class="flex items-center gap-2">
                  <component
                    :is="getIconForLevel(alerta.nivel)"
                    class="w-5 h-5"
                    :class="{
                      'text-red-500 animate-[pulse_2s_ease-in-out_infinite]': alerta.nivel === 'Crítica',
                      'text-amber-500': alerta.nivel === 'Advertencia',
                      'text-emerald-500/90': alerta.nivel === 'Informativa'
                    }"
                  />
                  <span
                    class="text-xs font-bold uppercase tracking-wider px-2.5 py-1 rounded-full bg-slate-100 border border-slate-200/60"
                    :class="{
                      'text-red-600': alerta.nivel === 'Crítica',
                      'text-amber-600': alerta.nivel === 'Advertencia',
                      'text-emerald-500/90': alerta.nivel === 'Informativa'
                    }"
                  >
                    {{ alerta.nivel }}
                  </span>
                </div>
                <div class="flex items-center gap-1.5 text-xs font-medium text-slate-700 bg-slate-100/50 px-2.5 py-1 rounded-md border border-slate-200/40">
                  <ClockIcon class="w-3.5 h-3.5 opacity-70" />
                  {{ formatDate(alerta.fecha) }}
                </div>
              </div>

              <!-- Content -->
              <div class="pl-2">
                <h2 class="text-xl font-bold text-slate-50 mb-2 leading-tight group-hover:text-slate-800 transition-colors">
                  {{ alerta.titulo }}
                </h2>
                <p class="text-slate-500 text-sm leading-relaxed mb-5 line-clamp-2">
                  {{ alerta.mensaje }}
                </p>
              </div>

              <!-- Footer info -->
              <div class="mt-auto flex items-center justify-between text-xs border-t border-slate-100 pt-4 pl-2">
                <div class="flex items-center gap-1.5 text-slate-500">
                  <HashtagIcon class="w-4 h-4" />
                  <span class="font-mono bg-slate-100 px-2 py-0.5 rounded border border-slate-200/60 text-slate-600">{{ alerta.referencia }}</span>
                </div>
                <!-- Action button (Interactive feel) -->
                <button class="opacity-0 group-hover:opacity-100 focus:opacity-100 transition-opacity duration-300 flex items-center gap-1 text-slate-800 hover:text-slate-100 font-semibold cursor-pointer">
                  Detalles <ChevronRightIcon class="w-4 h-4" />
                </button>
              </div>
            </div>
          </TransitionGroup>

          <!-- Empty State -->
          <div v-if="!alertStore.isLoading && !alertStore.error && alertStore.alertasFiltradas.length === 0"
                class="flex flex-col items-center justify-center p-12 text-center bg-white/50 rounded-3xl border border-dashed border-slate-300 shadow-sm mt-8">
            <div class="p-5 bg-white rounded-full mb-4 ring-1 ring-slate-200 shadow-sm">
              <CheckBadgeIcon class="w-12 h-12 text-emerald-500/90" />
            </div>
            <h3 class="text-xl font-bold text-slate-800 mb-2">Todo en orden</h3>
            <p class="text-slate-500 max-w-md">No hay alertas activas para la categoría seleccionada en este momento. El sistema está funcionando correctamente.</p>
          </div>
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

// Icons importing
import {
  BellAlertIcon,
  ExclamationTriangleIcon,
  ExclamationCircleIcon,
  InformationCircleIcon,
  ArrowPathIcon,
  ClockIcon,
  HashtagIcon,
  ChevronRightIcon,
  CheckBadgeIcon
} from '@heroicons/vue/24/solid';

const alertStore = useAlertStore();

const formatDate = (date) => {
  if (!date) return 'Sin fecha';
  try {
    const d = new Date(date);
    return new Intl.DateTimeFormat('es-ES', {
      day: '2-digit',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit'
    }).format(d);
  } catch (e) {
    return date;
  }
};

const getIconForLevel = (nivel) => {
  switch (nivel) {
    case 'Crítica': return ExclamationTriangleIcon;
    case 'Advertencia': return ExclamationCircleIcon;
    case 'Informativa': return InformationCircleIcon;
    default: return InformationCircleIcon;
  }
};

onMounted(() => {
  alertStore.fetchAlerts();
});
</script>

<style scoped>
/* Transiciones fluidas para la lista de alertas */
.list-enter-active,
.list-leave-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.95);
}
.list-leave-active {
  position: absolute;
}
</style>

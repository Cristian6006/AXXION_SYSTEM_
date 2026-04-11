<template>
  <div class="app flex">
    <SideBar />
    <main class="container h-screen p-4 flex-1 overflow-y-auto">
      <div class="rounded-lg flex-col">
        <headerP />

        <!-- Encabezado de la Sección -->
        <div class="mb-8">
          <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">
            <font-awesome-icon icon="fa-solid fa-file-contract" class="mr-3 text-[#01995f]" />
            Gestión de Solicitudes
          </h1>
          <p class="text-gray-600 dark:text-gray-400">
            Convierte requerimientos en rentas activas y gestiona el ciclo de vida de cada solicitud.
          </p>
        </div>

        <!-- Dashboard de Resumen -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <fwb-card class="bg-white dark:bg-gray-800 border-l-4 border-blue-500 hover:shadow-lg transition-shadow">
            <div class="p-5 flex items-center justify-between">
              <div>
                <p class="text-sm font-medium text-gray-500 dark:text-gray-400">Total Solicitudes</p>
                <p class="text-2xl font-bold text-gray-900 dark:text-white">{{ solicitudesStore.totalSolicitudes }}</p>
              </div>
              <div class="bg-blue-100 p-3 rounded-full">
                <font-awesome-icon icon="fa-solid fa-clipboard-list" class="text-blue-600" />
              </div>
            </div>
          </fwb-card>

          <fwb-card class="bg-white dark:bg-gray-800 border-l-4 border-yellow-500 hover:shadow-lg transition-shadow">
            <div class="p-5 flex items-center justify-between">
              <div>
                <p class="text-sm font-medium text-gray-500 dark:text-gray-400">Pendientes</p>
                <p class="text-2xl font-bold text-gray-900 dark:text-white">{{ activeSolicitudesCount }}</p>
              </div>
              <div class="bg-yellow-100 p-3 rounded-full">
                <font-awesome-icon icon="fa-solid fa-clock" class="text-yellow-600" />
              </div>
            </div>
          </fwb-card>

          <fwb-card class="bg-white dark:bg-gray-800 border-l-4 border-green-500 hover:shadow-lg transition-shadow">
            <div class="p-5 flex items-center justify-between">
              <div>
                <p class="text-sm font-medium text-gray-500 dark:text-gray-400">Atendidas (Con Renta)</p>
                <p class="text-2xl font-bold text-gray-900 dark:text-white">{{ attendedSolicitudesCount }}</p>
              </div>
              <div class="bg-green-100 p-3 rounded-full">
                <font-awesome-icon icon="fa-solid fa-check-double" class="text-green-600" />
              </div>
            </div>
          </fwb-card>
        </div>

        <!-- Tabla de Solicitudes -->
        <div class="bg-white dark:bg-gray-800 shadow-xl rounded-2xl overflow-hidden border border-gray-100 dark:border-gray-700">
          <div class="p-6 border-b border-gray-100 dark:border-gray-700 flex flex-col md:flex-row justify-between items-center gap-4">
             <div class="relative w-full max-w-md">
                <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
                    <font-awesome-icon icon="fa-solid fa-search" class="text-gray-400" />
                </div>
                <input
                    v-model="searchQuery"
                    type="text"
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-xl focus:ring-[#01995f] focus:border-[#01995f] block w-full pl-10 p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white"
                    placeholder="Buscar por cliente o folio..."
                >
             </div>
             <div class="flex gap-2">
                <fwb-button @click="solicitudesStore.fetchSolicitudes" color="alternative">
                    <font-awesome-icon icon="fa-solid fa-sync" :class="{ 'fa-spin': solicitudesStore.loading }" class="mr-2" />
                    Actualizar
                </fwb-button>
             </div>
          </div>

          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
              <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  <th scope="col" class="px-6 py-4">Folio / Fecha</th>
                  <th scope="col" class="px-6 py-4">Cliente</th>
                  <th scope="col" class="px-6 py-4">Productos Solicitados</th>
                  <th scope="col" class="px-6 py-4">Estado</th>
                  <th scope="col" class="px-6 py-4 text-center">Gestión</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="solicitudesStore.loading" class="bg-white dark:bg-gray-800">
                  <td colspan="5" class="px-6 py-10 text-center">
                    <div class="flex justify-center items-center space-x-2">
                        <div class="w-4 h-4 bg-[#01995f] rounded-full animate-bounce"></div>
                        <div class="w-4 h-4 bg-[#01995f] rounded-full animate-bounce [animation-delay:-.3s]"></div>
                        <div class="w-4 h-4 bg-[#01995f] rounded-full animate-bounce [animation-delay:-.5s]"></div>
                    </div>
                  </td>
                </tr>
                <tr v-else-if="filteredSolicitudes.length === 0" class="bg-white dark:bg-gray-800">
                  <td colspan="5" class="px-6 py-10 text-center text-gray-500 italic">
                    No se encontraron registros.
                  </td>
                </tr>
                <tr
                  v-for="solicitud in filteredSolicitudes"
                  :key="solicitud.id"
                  class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                >
                  <td class="px-6 py-4">
                    <div class="flex flex-col">
                        <span class="font-bold text-gray-900 dark:text-white">#{{ solicitud.id.toString().padStart(5, '0') }}</span>
                        <span class="text-[10px] text-gray-500 uppercase tracking-widest">{{ formatDate(solicitud.fecha_solicitud) }}</span>
                    </div>
                  </td>
                  <td class="px-6 py-4">
                    <div class="flex items-center">
                      <div class="w-9 h-9 rounded-full bg-gradient-to-br from-[#01995f] to-green-400 flex items-center justify-center text-white font-bold mr-3 text-xs shadow-sm">
                        {{ solicitud.cliente?.nombre?.charAt(0) || 'C' }}
                      </div>
                      <div class="flex flex-col">
                        <span class="font-medium text-gray-900 dark:text-white leading-tight">
                          {{ solicitud.cliente?.nombre || 'Cliente no asignado' }}
                        </span>
                        <span class="text-[10px] text-gray-500">{{ solicitud.cliente?.email || 'Sin correo' }}</span>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4">
                    <div class="flex flex-wrap gap-1">
                      <span
                        v-for="prod in solicitud.productos"
                        :key="prod.id"
                        class="bg-gray-100 text-gray-700 text-[10px] font-bold px-2 py-0.5 rounded-md dark:bg-gray-700 dark:text-gray-300 border border-gray-200 dark:border-gray-600"
                      >
                        {{ prod.nombre }}
                      </span>
                      <span v-if="!solicitud.productos || solicitud.productos.length === 0" class="text-gray-400 italic text-xs">
                        Sin productos vinculados
                      </span>
                    </div>
                  </td>
                  <td class="px-6 py-4">
                    <div class="flex flex-col gap-1.5">
                        <span :class="getStatusBadgeClass(solicitud.estado_solicitud)">
                            {{ solicitud.estado_solicitud }}
                        </span>
                        <!-- Indicador visual de Renta -->
                        <div v-if="solicitud.estado_solicitud === 'Atendida'" class="flex items-center text-[9px] text-green-600 font-bold uppercase tracking-tighter">
                            <font-awesome-icon icon="fa-solid fa-check-circle" class="mr-1" />
                            Renta Activa
                        </div>
                        <div v-else class="flex items-center text-[9px] text-gray-400 font-medium uppercase tracking-tighter">
                            <font-awesome-icon icon="fa-solid fa-clock" class="mr-1" />
                            Pendiente
                        </div>
                    </div>
                  </td>
                  <td class="px-6 py-4">
                    <div class="flex justify-center items-center gap-2">
                        <fwb-button
                            @click="viewDetails(solicitud)"
                            color="light"
                            size="sm"
                            class="rounded-lg hover:text-[#01995f] hover:bg-green-50"
                            title="Ver Detalles"
                        >
                            <font-awesome-icon icon="fa-solid fa-eye" />
                        </fwb-button>

                        <!-- Botón para Convertir a Renta -->
                        <fwb-button
                            v-if="solicitud.estado_solicitud !== 'Atendida' && solicitud.estado_solicitud !== 'Cancelada'"
                            @click="openRentalConversion(solicitud)"
                            gradient="green-blue"
                            size="sm"
                            class="rounded-lg"
                            title="Formalizar Renta"
                        >
                            <font-awesome-icon icon="fa-solid fa-handshake" class="mr-1" />
                            Rentará
                        </fwb-button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </main>

    <!-- Modal de Conversión a Renta -->
    <fwb-modal v-if="conversionModal" @close="conversionModal = false" size="5xl">
        <template #header>
            <div class="flex items-center">
                <div class="bg-green-100 p-2 rounded-lg mr-3">
                    <font-awesome-icon icon="fa-solid fa-file-invoice-dollar" class="text-[#01995f]" />
                </div>
                <div>
                    <h3 class="text-xl font-bold text-gray-900 dark:text-white">Generar Contrato de Renta</h3>
                    <p class="text-xs text-gray-500 font-normal">Solicitud #{{ currentConversionSolicitud.id }} — Cliente: {{ currentConversionSolicitud.cliente?.nombre }}</p>
                </div>
            </div>
        </template>
        <template #body>
            <form @submit.prevent="submitRentalConversion" class="space-y-6">
                <!-- Cronograma -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div class="space-y-2">
                        <label class="block text-sm font-bold text-gray-700 dark:text-gray-300">Fecha de Inicio</label>
                        <input type="date" v-model="rentalForm.fecha_inicio" required
                            class="w-full bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-xl focus:ring-[#01995f] focus:border-[#01995f] p-3 dark:bg-gray-700 dark:border-gray-600 dark:text-white">
                    </div>
                    <div class="space-y-2">
                        <label class="block text-sm font-bold text-gray-700 dark:text-gray-300">Fecha de Devolución Estimada</label>
                        <input type="date" v-model="rentalForm.fecha_fin_prevista" required
                            class="w-full bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-xl focus:ring-[#01995f] focus:border-[#01995f] p-3 dark:bg-gray-700 dark:border-gray-600 dark:text-white">
                    </div>
                </div>

                <!-- Selección de Items de Inventario -->
                <div class="space-y-3">
                    <label class="block text-sm font-bold text-gray-700 dark:text-gray-300">
                        Seleccionar Equipos Físicos (Items Disponibles)
                    </label>
                    <div class="bg-gray-50 dark:bg-gray-800 p-4 rounded-2xl border border-gray-200 dark:border-gray-700">
                        <div v-if="availableInventoryItems.length === 0" class="text-center py-6 text-gray-500 italic">
                            No hay unidades físicas disponibles en inventario para estos productos.
                        </div>
                        <div v-else class="grid grid-cols-1 sm:grid-cols-2 gap-3 max-h-48 overflow-y-auto pr-2">
                            <div v-for="item in availableInventoryItems" :key="item.id"
                                @click="toggleItemSelection(item.id)"
                                :class="[
                                    'cursor-pointer p-3 rounded-xl border-2 transition-all flex items-center justify-between',
                                    rentalForm.inventario_items.includes(item.id)
                                        ? 'border-[#01995f] bg-green-50 dark:bg-green-900/20 shadow-sm'
                                        : 'border-white dark:border-gray-700 bg-white dark:bg-gray-800 hover:border-gray-200'
                                ]"
                            >
                                <div class="flex items-center">
                                    <div class="w-8 h-8 rounded-lg bg-gray-100 dark:bg-gray-700 flex items-center justify-center mr-3">
                                        <font-awesome-icon icon="fa-solid fa-laptop" class="text-xs text-gray-400" />
                                    </div>
                                    <div class="flex flex-col">
                                        <span class="text-xs font-bold">{{ item.producto?.nombre }}</span>
                                        <span class="text-[10px] text-gray-500">S/N: {{ item.numero_serie }}</span>
                                    </div>
                                </div>
                                <font-awesome-icon v-if="rentalForm.inventario_items.includes(item.id)"
                                    icon="fa-solid fa-check-circle" class="text-[#01995f]" />
                            </div>
                        </div>
                    </div>
                    <p class="text-[10px] text-blue-600 font-medium italic">
                        * Debes seleccionar los equipos físicos reales para poder formalizar la renta.
                    </p>
                </div>

                <!-- Finanzas -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div class="space-y-2">
                        <label class="block text-sm font-bold text-gray-700 dark:text-gray-300">Monto Total ($)</label>
                        <input type="number" step="0.01" v-model="rentalForm.monto_total_renta" required
                            class="w-full bg-white border border-gray-300 text-gray-900 text-sm rounded-xl focus:ring-[#01995f] focus:border-[#01995f] p-3 dark:bg-gray-700 dark:border-gray-600 dark:text-white font-mono">
                    </div>
                    <div class="space-y-2">
                        <label class="block text-sm font-bold text-gray-700 dark:text-gray-300">Depósito Garantía ($)</label>
                        <input type="number" step="0.01" v-model="rentalForm.deposito_garantia" required
                            class="w-full bg-white border border-gray-300 text-gray-900 text-sm rounded-xl focus:ring-[#01995f] focus:border-[#01995f] p-3 dark:bg-gray-700 dark:border-gray-600 dark:text-white font-mono">
                    </div>
                </div>
            </form>
        </template>
        <template #footer>
            <div class="flex justify-end gap-3 w-full">
                <fwb-button @click="conversionModal = false" color="alternative">Cancelar</fwb-button>
                <fwb-button
                    @click="submitRentalConversion"
                    :disabled="rentalForm.inventario_items.length === 0 || isSubmitting"
                    gradient="green-blue"
                >
                    <font-awesome-icon v-if="isSubmitting" icon="fa-solid fa-spinner" class="fa-spin mr-2" />
                    Finalizar y Generar Renta
                </fwb-button>
            </div>
        </template>
    </fwb-modal>

    <!-- Modal de Detalles -->
    <fwb-modal v-if="selectedSolicitud" @close="selectedSolicitud = null" size="4xl">
      <template #header>
        <div class="flex items-center text-lg font-bold">Resumen de Solicitud #{{ selectedSolicitud.id }}</div>
      </template>
      <template #body>
         <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div class="bg-gray-50 dark:bg-gray-700/50 p-6 rounded-2xl border border-gray-100 dark:border-gray-600">
                <h4 class="text-xs font-bold text-gray-400 uppercase tracking-widest mb-4">Información del Cliente</h4>
                <div class="space-y-3">
                  <div class="flex justify-between border-b dark:border-gray-600 pb-2">
                    <span class="text-gray-500 text-sm">Nombre:</span>
                    <span class="font-bold text-gray-900 dark:text-white">{{ selectedSolicitud.cliente?.nombre }}</span>
                  </div>
                  <div class="flex justify-between border-b dark:border-gray-600 pb-2">
                    <span class="text-gray-500 text-sm">Teléfono:</span>
                    <span class="font-medium text-gray-900 dark:text-white">{{ selectedSolicitud.cliente?.telefono || 'N/A' }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-gray-500 text-sm">Correo:</span>
                    <span class="font-medium text-blue-600 dark:text-blue-400">{{ selectedSolicitud.cliente?.email }}</span>
                  </div>
                </div>
            </div>

            <div class="bg-gray-50 dark:bg-gray-700/50 p-6 rounded-2xl border border-gray-100 dark:border-gray-600">
                <h4 class="text-xs font-bold text-gray-400 uppercase tracking-widest mb-4">Estado y Solicitud</h4>
                <div class="space-y-3">
                  <div class="flex justify-between border-b dark:border-gray-600 pb-2">
                    <span class="text-gray-500 text-sm">Estado Actual:</span>
                    <span :class="getStatusBadgeClass(selectedSolicitud.estado_solicitud)">{{ selectedSolicitud.estado_solicitud }}</span>
                  </div>
                  <div class="flex justify-between">
                    <span class="text-gray-500 text-sm">Fecha Solicitud:</span>
                    <span class="font-medium text-gray-900 dark:text-white">{{ formatDate(selectedSolicitud.fecha_solicitud) }}</span>
                  </div>
                </div>
            </div>

            <div class="md:col-span-2 space-y-3">
                <h4 class="text-xs font-bold text-gray-400 uppercase tracking-widest">Descripción de Necesidad</h4>
                <div class="bg-white dark:bg-gray-800 p-4 rounded-xl text-sm italic border dark:border-gray-600 shadow-inner">
                  "{{ selectedSolicitud.descripcion_necesidad || 'Sin descripción adicional.' }}"
                </div>
            </div>

            <div class="md:col-span-2 space-y-4">
                <h4 class="text-xs font-bold text-gray-400 uppercase tracking-widest">Productos Vinculados ({{ selectedSolicitud.productos?.length || 0 }})</h4>
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                   <div v-for="prod in selectedSolicitud.productos" :key="prod.id"
                    class="flex items-center p-3 bg-white dark:bg-gray-800 rounded-xl shadow-sm border dark:border-gray-700">
                      <div class="w-10 h-10 rounded-lg bg-green-50 flex items-center justify-center text-[#01995f] mr-3">
                        <font-awesome-icon icon="fa-solid fa-box-open" />
                      </div>
                      <div class="flex flex-col">
                        <span class="text-xs font-bold text-gray-900 dark:text-white">{{ prod.nombre }}</span>
                        <span class="text-[9px] text-gray-500 uppercase tracking-tighter">{{ prod.marca }} | {{ prod.modelo }}</span>
                      </div>
                   </div>
                </div>
            </div>
         </div>
      </template>
      <template #footer>
        <div class="flex justify-between w-full">
            <fwb-button
                v-if="selectedSolicitud.estado_solicitud !== 'Atendida' && selectedSolicitud.estado_solicitud !== 'Cancelada'"
                @click="openRentalConversion(selectedSolicitud)"
                gradient="green-blue"
            >
                Convertir a Renta
            </fwb-button>
            <fwb-button @click="selectedSolicitud = null" color="alternative" class="ml-auto">Cerrar</fwb-button>
        </div>
      </template>
    </fwb-modal>
  </div>
  <Footer />
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useSolicitudStore } from '@/stores/solicitudStore';
import { useInventarioItemStore } from '@/stores/inventarioItem';
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import Footer from '@/components/Footer.vue';
import Swal from 'sweetalert2';
import { FwbButton, FwbModal, FwbCard } from 'flowbite-vue';

// Stores
const solicitudesStore = useSolicitudStore();
const inventoryItemStore = useInventarioItemStore();

// UI State
const searchQuery = ref('');
const selectedSolicitud = ref(null);
const isSubmitting = ref(false);

// Conversion State
const conversionModal = ref(false);
const currentConversionSolicitud = ref({});
const rentalForm = ref({
    fecha_inicio: '',
    fecha_fin_prevista: '',
    monto_total_renta: 0,
    deposito_garantia: 0,
    inventario_items: [],
    notas: ''
});

// Lifecycle
onMounted(async () => {
    await Promise.all([
        solicitudesStore.fetchSolicitudes(),
        inventoryItemStore.fetchInventarioItems()
    ]);
});

// Computed
const filteredSolicitudes = computed(() => {
    if (!searchQuery.value) return solicitudesStore.allSolicitudes;
    const query = searchQuery.value.toLowerCase();
    return solicitudesStore.allSolicitudes.filter(s =>
        s.cliente?.nombre?.toLowerCase().includes(query) ||
        s.id.toString().includes(query)
    );
});

const activeSolicitudesCount = computed(() => {
    return solicitudesStore.allSolicitudes.filter(s =>
        s.estado_solicitud === 'Nueva' || s.estado_solicitud === 'EnProceso'
    ).length;
});

const attendedSolicitudesCount = computed(() => {
    return solicitudesStore.allSolicitudes.filter(s => s.estado_solicitud === 'Atendida').length;
});

const availableInventoryItems = computed(() => {
    if (!currentConversionSolicitud.value.productos) return [];
    const productIds = currentConversionSolicitud.value.productos.map(p => p.id);
    return inventoryItemStore.inventarioItems.filter(item =>
        productIds.includes(item.producto_id) &&
        (item.estado_item === 'Disponible' || item.estado_item === 'disponible')
    );
});

// Methods
const viewDetails = (solicitud) => {
    selectedSolicitud.value = solicitud;
};

const openRentalConversion = (solicitud) => {
    currentConversionSolicitud.value = solicitud;
    selectedSolicitud.value = null;

    const today = new Date().toISOString().split('T')[0];
    const nextMonth = new Date();
    nextMonth.setMonth(nextMonth.getMonth() + 1);

    rentalForm.value = {
        fecha_inicio: today,
        fecha_fin_prevista: nextMonth.toISOString().split('T')[0],
        monto_total_renta: 0,
        deposito_garantia: 0,
        inventario_items: [],
        notas: `Formalización de solicitud #${solicitud.id}`
    };

    conversionModal.value = true;
};

const toggleItemSelection = (itemId) => {
    const idx = rentalForm.value.inventario_items.indexOf(itemId);
    if (idx > -1) rentalForm.value.inventario_items.splice(idx, 1);
    else rentalForm.value.inventario_items.push(itemId);
};

const submitRentalConversion = async () => {
    if (rentalForm.value.inventario_items.length === 0) return;

    isSubmitting.value = true;
    try {
        await solicitudesStore.convertToRental(currentConversionSolicitud.value.id, rentalForm.value);
        conversionModal.value = false;

        Swal.fire({
            title: '¡Renta Generada!',
            text: 'La solicitud ha sido procesada y el contrato de renta ha sido creado exitosamente.',
            icon: 'success',
            confirmButtonColor: '#01995f'
        });

        await inventoryItemStore.fetchInventarioItems();
    } catch (error) {
        Swal.fire({ title: 'Error', text: error.message || 'Error al procesar la renta.', icon: 'error' });
    } finally {
        isSubmitting.value = false;
    }
};

const formatDate = (date) => date ? new Date(date).toLocaleDateString() : 'N/A';

const getStatusBadgeClass = (status) => {
    const base = 'text-[10px] font-bold px-2 py-0.5 rounded-lg inline-flex items-center uppercase tracking-widest';
    if (status === 'Nueva') return `${base} bg-blue-50 text-blue-700 border border-blue-100`;
    if (status === 'Atendida') return `${base} bg-green-50 text-green-700 border border-green-100`;
    if (status === 'Cancelada') return `${base} bg-red-50 text-red-700 border border-red-100`;
    return `${base} bg-yellow-50 text-yellow-700 border border-yellow-100`;
};

defineOptions({ name: 'SolicitudesView' });
</script>

<style scoped>
.app { display: flex; }
</style>

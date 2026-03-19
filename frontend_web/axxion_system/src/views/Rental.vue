<template>
  <div class="app flex">
    <SideBar />
    <main class="container h-screen p-4 flex-1 overflow-y-auto">
      <headerP />
      <section class="flex flex-col">
        <div v-if="rentalStore.loading" class="p-4 justify-items-center h-screen text-white">
          <span class="loader"></span>
        </div>
        <div v-else-if="rentalStore.error" class="mb-4">
          <fwb-alert type="danger" icon>
            {{ rentalStore.error }}
          </fwb-alert>
        </div>
        
        <div v-else>
          <!-- Filtros -->
          <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 mb-6 mx-4">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Buscar
                </label>
                <fwb-input 
                  v-model="searchQuery"
                  placeholder="Cliente o Equipo..."
                  icon="fa-solid fa-search"
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Fecha de Renta
                </label>
                <input 
                  type="date" 
                  v-model="dateFilter"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                />
              </div>
              
              <div class="flex items-end">
                <fwb-button 
                  gradient="red-yellow" 
                  size="lg" 
                  @click="clearFilters"
                  class="w-full"
                >
                  <font-awesome-icon icon="fa-solid fa-times" class="mr-2"/>
                  Limpiar Filtros
                </fwb-button>
              </div>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 p-4">
          <!-- Tarjeta de Renta Moderna -->
          <div 
            v-for="r in filteredRentals" 
            :key="r.id" 
            class="rental-card bg-gradient-to-br from-gray-800 to-gray-900 rounded-2xl shadow-2xl overflow-hidden transform transition-all duration-300 hover:scale-105 hover:shadow-3xl border border-gray-700"
          >
            <!-- Header con gradiente -->
            <div class="rental-header bg-gradient-to-r from-blue-600 to-purple-600 p-4 relative">
              <div class="flex justify-between items-start">
                <div>
                  <h3 class="text-2xl font-bold text-white mb-1">Renta #{{ r.id }}</h3>
                  <p class="text-blue-100 text-sm">{{ formatDate(r.fecha_inicio) }}</p>
                </div>
                <span 
                  :class="getStatusClass(r.estado_renta)"
                  class="px-3 py-1 rounded-full text-xs font-semibold shadow-lg"
                >
                  {{ r.estado_renta }}
                </span>
              </div>
            </div>

            <!-- Contenido de la tarjeta -->
            <div class="p-6 space-y-4">
              <!-- Cliente -->
              <div class="flex items-center space-x-3 p-3 bg-gray-700/50 rounded-lg">
                <font-awesome-icon icon="fa-solid fa-user" class="text-blue-400 text-xl" />
                <div class="flex-1">
                  <p class="text-xs text-gray-400 uppercase tracking-wide">Cliente</p>
                  <p class="text-white font-medium">
                    {{ (r.cliente?.nombre || '') + ' ' + (r.cliente?.nombre2 ? r.cliente.nombre2 + ' ' : '') + (r.cliente?.apellido1 || '') + (r.cliente?.apellido2 ? ' ' + r.cliente.apellido2 : '') }}
                  </p>
                </div>
              </div>

              <!-- Fechas -->
              <div class="grid grid-cols-2 gap-3">
                <div class="p-3 bg-gray-700/50 rounded-lg">
                  <p class="text-xs text-gray-400 uppercase tracking-wide mb-1">Inicio</p>
                  <p class="text-white text-sm font-medium">{{ formatDateShort(r.fecha_inicio) }}</p>
                </div>
                <div class="p-3 bg-gray-700/50 rounded-lg">
                  <p class="text-xs text-gray-400 uppercase tracking-wide mb-1">Fin Prevista</p>
                  <p class="text-white text-sm font-medium">{{ formatDateShort(r.fecha_fin_prevista) }}</p>
                </div>
              </div>

              <!-- Montos -->
              <div class="p-4 bg-gradient-to-r from-green-900/30 to-emerald-900/30 rounded-lg border border-green-700/30">
                <div class="flex justify-between items-center mb-2">
                  <span class="text-gray-300 text-sm">Monto Total</span>
                  <span class="text-2xl font-bold text-green-400">{{ formatCurrency(r.monto_total_renta) }}</span>
                </div>
                <div class="flex justify-between items-center text-sm">
                  <span class="text-gray-400">Depósito</span>
                  <span class="text-green-300">{{ formatCurrency(r.deposito_garantia) }}</span>
                </div>
              </div>

              <!-- Equipos -->
              <div class="space-y-2">
                <div class="flex items-center space-x-2">
                  <font-awesome-icon icon="fa-solid fa-box" class="text-purple-400" />
                  <p class="text-sm font-semibold text-gray-300">Equipos Rentados</p>
                  <span class="bg-purple-600 text-white text-xs px-2 py-0.5 rounded-full">
                    {{ (r.inventario_items || []).length }}
                  </span>
                </div>
                <div class="max-h-32 overflow-y-auto space-y-2 custom-scrollbar">
                  <div 
                    v-for="item in r.inventario_items || []" 
                    :key="item.id"
                    class="p-2 bg-gray-700/30 rounded text-xs text-gray-300 border-l-2 border-purple-500"
                  >
                    <div class="space-y-1">
                      <div class="flex justify-between items-start">
                        <div class="flex-1">
                          <p class="font-semibold text-white">{{ item.producto?.nombre || 'Producto sin nombre' }}</p>
                          <p class="text-gray-400">Serie: {{ item.numero_serie || item.producto_id }}</p>
                        </div>
                        <span class="text-purple-400 font-medium">{{ formatCurrency(item.pivot?.precio_renta_item) }}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Notas -->
              <div v-if="r.notas" class="p-3 bg-gray-700/30 rounded-lg border-l-4 border-blue-500">
                <p class="text-xs text-gray-400 uppercase tracking-wide mb-1">Notas</p>
                <p class="text-sm text-gray-300">{{ r.notas }}</p>
              </div>

              <!-- Botones de acción -->
              <div class="flex gap-3 pt-4 border-t border-gray-700">
                <button 
                  @click="rentalStore.openEditModal(r)" 
                  class="flex-1 flex items-center justify-center gap-2 px-4 py-2.5 bg-gradient-to-r from-yellow-500 to-orange-500 hover:from-yellow-600 hover:to-orange-600 text-white rounded-lg font-medium transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
                >
                  <font-awesome-icon icon="fa-solid fa-pen-to-square" />
                  <span>Editar</span>
                </button>
                <button 
                  @click="r.estado_renta === 'Finalizada' ? rentalStore.openDeleteModal(r) : null" 
                  :disabled="r.estado_renta !== 'Finalizada'"
                  :class="[
                    'flex-1 flex items-center justify-center gap-2 px-4 py-2.5 rounded-lg font-medium transition-all duration-200 shadow-lg',
                    r.estado_renta === 'Finalizada' 
                      ? 'bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 text-white hover:shadow-xl transform hover:-translate-y-0.5 cursor-pointer' 
                      : 'bg-gray-600 text-gray-400 cursor-not-allowed opacity-50'
                  ]"
                  :title="r.estado_renta !== 'Finalizada' ? 'Solo se pueden eliminar rentas finalizadas' : 'Eliminar renta'"
                >
                  <font-awesome-icon icon="fa-solid fa-trash" />
                  <span>Eliminar</span>
                </button>
              </div>
            </div>
          </div>
        </div>

        </div>

        <RentalModal
          :show="rentalStore.showModal"
          :mode="rentalStore.modalMode"
          :payload="rentalStore.selectedRental"
          :cliente-options="rentalStore.clienteOptions"
          @close="rentalStore.closeModal"
          @save="handleSaveRental"
          @delete="rentalStore.deleteRental"
        />
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useRentalStore } from '@/stores/rentalStore';
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import RentalModal from '@/components/RentalModal.vue';
import { FwbInput, FwbButton, FwbAlert } from 'flowbite-vue';

/**
 * Vista Rental.
 * 
 * Gestión de alquileres (Rentas).
 * Funcionalidades:
 * - Listado de rentas con tarjetas detalladas (cliente, equipos, fechas, montos).
 * - Filtrado por búsqueda (cliente/equipo) y fecha.
 * - Creación y edición de rentas mediante modal.
 * - Finalización de rentas con redirección automática a mantenimiento si es necesario.
 * - Visualización de estado y detalles financieros de cada renta.
 */

const rentalStore = useRentalStore();
const router = useRouter();

const searchQuery = ref('');
const dateFilter = ref('');

// Carga inicial de rentas y clientes.
onMounted(() => {
  rentalStore.fetchRentals();
  rentalStore.fetchClientes();
});

// Filtra las rentas según la búsqueda y la fecha seleccionada.
const filteredRentals = computed(() => {
  let rentals = rentalStore.rentals;

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    rentals = rentals.filter(r => {
      const clientName = `${r.cliente?.nombre || ''} ${r.cliente?.nombre2 || ''} ${r.cliente?.apellido1 || ''} ${r.cliente?.apellido2 || ''}`.toLowerCase();
      const equipmentMatch = r.inventario_items?.some(item => 
        (item.producto?.nombre || '').toLowerCase().includes(query) || 
        (item.numero_serie || '').toLowerCase().includes(query)
      );
      return clientName.includes(query) || equipmentMatch;
    });
  }

  if (dateFilter.value) {
    rentals = rentals.filter(r => {
      if (!r.fecha_inicio) return false;
      return r.fecha_inicio.startsWith(dateFilter.value);
    });
  }

  return rentals;
});

const clearFilters = () => {
  searchQuery.value = '';
  dateFilter.value = '';
};

// Maneja el guardado de una renta.
// Si la renta se finaliza, redirige automáticamente a la vista de mantenimiento
// preseleccionando los equipos devueltos para su revisión.
const handleSaveRental = async (payload) => {
    console.log('handleSaveRental called with payload:', payload);
    console.log('Estado de la renta:', payload.estado_renta);
    
    await rentalStore.saveRental(payload);
    
    console.log('After saveRental, error:', rentalStore.error);
    
    // Si la renta se guardó exitosamente y el estado es 'Finalizada', redirigir a mantenimiento
    if (!rentalStore.error && payload.estado_renta === 'Finalizada') {
        console.log('Condición cumplida, iniciando redirección...');
        
        // Esperar un momento para que el trigger cree el mantenimiento
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        // Recargar las rentas para obtener los datos actualizados con inventario_items
        await rentalStore.fetchRentals();
        
        console.log('Rentas recargadas:', rentalStore.rentals.length);
        
        // Buscar la renta actualizada
        const rentalId = rentalStore.selectedRental?.id || payload.id;
        console.log('Buscando renta con ID:', rentalId);
        
        const updatedRental = rentalStore.rentals.find(r => r.id === rentalId);
        console.log('Renta encontrada:', updatedRental);
        
        if (updatedRental && updatedRental.inventario_items) {
            console.log('Items de inventario:', updatedRental.inventario_items);
            
            if (updatedRental.inventario_items.length > 0) {
                const itemIds = updatedRental.inventario_items.map(i => i.id).join(',');
                console.log('IDs de items para redirección:', itemIds);
                
                // Redirigir a mantenimiento pasando los IDs de los items para abrir el modal automáticamente
                router.push({ path: '/Mantenace', query: { auto_open_items: itemIds } });
            } else {
                console.warn('No hay items de inventario en la renta');
            }
        } else {
            console.warn('No se encontró la renta actualizada o no tiene inventario_items');
        }
    } else {
        console.log('No se cumplió la condición para redirección');
    }
};

const formatDate = (iso) => iso ? new Date(iso).toLocaleString('es-CO', { 
  year: 'numeric', 
  month: 'long', 
  day: 'numeric',
  hour: '2-digit',
  minute: '2-digit'
}) : '-';

const formatDateShort = (iso) => iso ? new Date(iso).toLocaleDateString('es-CO', { 
  month: 'short', 
  day: 'numeric'
}) : '-';

const formatCurrency = (val) => (val === null || val === undefined) ? '-' : Number(val).toLocaleString('es-CO', { style: 'currency', currency: 'COP' });

const getStatusClass = (status) => {
  const classes = {
    'Programada': 'bg-blue-500 text-white',
    'EnCurso': 'bg-green-500 text-white',
    'Finalizada': 'bg-gray-500 text-white',
    'Retrasada': 'bg-orange-500 text-white',
    'Cancelada': 'bg-red-500 text-white'
  };
  return classes[status] || 'bg-gray-500 text-white';
};
</script>

<style scoped>


.rental-card {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.rental-card:hover {
  transform: translateY(-8px);
}

.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: rgba(75, 85, 99, 0.3);
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: rgba(139, 92, 246, 0.5);
  border-radius: 10px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: rgba(139, 92, 246, 0.7);
}
</style>

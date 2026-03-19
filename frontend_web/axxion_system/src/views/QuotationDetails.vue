<template>
  <div class="app flex">
    <SideBar />
    <main class="container h-screen p-4 flex-1 overflow-y-auto">
      <headerP />
      
      <div v-if="loading" class="flex justify-center items-center h-64">
        <div class="loader"></div>
      </div>

      <div v-else-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
        <strong class="font-bold">Error: </strong>
        <span class="block sm:inline">{{ error }}</span>
      </div>

      <div v-else-if="cotizacion" class="max-w-4xl mx-auto bg-white dark:bg-gray-800 rounded-lg shadow-lg p-6">
        <!-- Alert Component -->
        <div v-if="alertState.show" class="mb-4">
          <fwb-alert 
            :type="alertState.type" 
            closable 
            @close="alertState.show = false"
            :icon="true"
          >
            {{ alertState.message }}
          </fwb-alert>
        </div>

        <!-- Header -->
        <div class="flex justify-between items-center mb-6 border-b pb-4">
          <div>
            <h1 class="text-2xl font-bold text-gray-900 dark:text-white">
              Cotización #{{ cotizacion.id }}
            </h1>
            <p class="text-sm text-gray-500 dark:text-gray-400">
              Generada el: {{ formatDate(cotizacion.fecha_cotizacion) }}
            </p>
          </div>
          <div class="flex items-center space-x-2">
            <span :class="getStatusClass(cotizacion.estado_cotizacion)" class="px-3 py-1 rounded-full text-sm font-medium">
              {{ cotizacion.estado_cotizacion }}
            </span>
          </div>
        </div>

        <!-- Client Info -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <div class="bg-gray-50 dark:bg-gray-700 p-4 rounded-lg">
            <h3 class="text-lg font-semibold mb-3 text-gray-800 dark:text-white">
              <font-awesome-icon icon="fa-solid fa-user" class="mr-2 text-blue-500"/>
              Información del Cliente
            </h3>
            <div v-if="cotizacion.cliente">
              <p class="font-medium text-gray-900 dark:text-white text-lg">
                {{ cotizacion.cliente.nombre }} {{ cotizacion.cliente.nombre2 }} {{ cotizacion.cliente.apellido1 }} {{ cotizacion.cliente.apellido2 }}
              </p>
              <div class="mt-2 space-y-1">
                <p class="text-gray-600 dark:text-gray-300 text-sm">
                  <span class="font-semibold">RFC:</span> {{ cotizacion.cliente.rfc || 'N/A' }}
                </p>
                <p class="text-gray-600 dark:text-gray-300 text-sm">
                  <span class="font-semibold">Email:</span> {{ cotizacion.cliente.correo_electronico || cotizacion.cliente.email }}
                </p>
                <p class="text-gray-600 dark:text-gray-300 text-sm">
                  <span class="font-semibold">Teléfono:</span> {{ cotizacion.cliente.telefono_principal || cotizacion.cliente.telefono }}
                </p>
                <p class="text-gray-600 dark:text-gray-300 text-sm">
                  <span class="font-semibold">Tipo:</span> {{ cotizacion.cliente.tipo_cliente || 'N/A' }}
                </p>
                <p class="text-gray-600 dark:text-gray-300 text-sm">
                  <span class="font-semibold">Estado:</span> 
                  <span :class="{'text-green-600': cotizacion.cliente.estado_cliente === 'Activo', 'text-red-600': cotizacion.cliente.estado_cliente !== 'Activo'}">
                    {{ cotizacion.cliente.estado_cliente || 'N/A' }}
                  </span>
                </p>
              </div>
            </div>
          </div>

          <div class="bg-gray-50 dark:bg-gray-700 p-4 rounded-lg">
            <h3 class="text-lg font-semibold mb-3 text-gray-800 dark:text-white">
              <font-awesome-icon icon="fa-solid fa-calendar" class="mr-2 text-green-500"/>
              Detalles de Validez
            </h3>
            <p class="text-gray-600 dark:text-gray-300">
              <span class="font-medium">Válida hasta:</span> {{ formatDate(cotizacion.fecha_validez) }}
            </p>
            <p v-if="cotizacion.solicitud" class="text-gray-600 dark:text-gray-300 mt-2">
              <span class="font-medium">Solicitud Relacionada:</span> #{{ cotizacion.solicitud.id }}
            </p>
          </div>
        </div>

        <!-- Items Table -->
        <div class="mb-8">
          <h3 class="text-lg font-semibold mb-4 text-gray-800 dark:text-white">Items Cotizados</h3>
          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
              <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  <th scope="col" class="px-6 py-3">Producto</th>
                  <th scope="col" class="px-6 py-3 text-center">Cantidad</th>
                  <th scope="col" class="px-6 py-3 text-right">Precio Unitario</th>
                  <th scope="col" class="px-6 py-3 text-right">Subtotal</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in cotizacion.detalles" :key="item.id" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                  <td class="px-6 py-4 font-medium text-gray-900 dark:text-white">
                    {{ item.descripcion_item || item.producto?.nombre || 'Producto #' + item.producto_id }}
                  </td>
                  <td class="px-6 py-4 text-center">
                    {{ item.cantidad }}
                  </td>
                  <td class="px-6 py-4 text-right">
                    {{ formatCurrency(item.precio_unitario) }}
                  </td>
                  <td class="px-6 py-4 text-right">
                    {{ formatCurrency(item.precio_unitario * item.cantidad) }}
                  </td>
                </tr>
              </tbody>
              <tfoot>
                <tr class="font-semibold text-gray-900 dark:text-white bg-gray-50 dark:bg-gray-700">
                  <td colspan="3" class="px-6 py-4 text-right">Total</td>
                  <td class="px-6 py-4 text-right">{{ formatCurrency(cotizacion.monto_total) }}</td>
                </tr>
              </tfoot>
            </table>
          </div>
        </div>

        <!-- Actions -->
        <div class="flex justify-end space-x-4 border-t pt-6">
          <fwb-button color="alternative" @click="$router.push('/Inventory')">
            Volver al Inventario
          </fwb-button>
          
          <fwb-button 
            gradient="green-blue" 
            @click="convertToRental"
            :disabled="isConverting"
          >
            <font-awesome-icon v-if="isConverting" icon="fa-solid fa-spinner" class="animate-spin mr-2"/>
            <font-awesome-icon v-else icon="fa-solid fa-check" class="mr-2"/>
            Convertir a Renta
          </fwb-button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import CotizacionService from '@/services/CotizacionService';
import RentalService from '@/services/RentalService';
import { FwbButton, FwbAlert } from 'flowbite-vue';

/**
 * Vista QuotationDetails.
 * 
 * Muestra los detalles completos de una cotización específica.
 * Permite visualizar:
 * - Información del cliente.
 * - Items cotizados con precios y subtotales.
 * - Estado actual de la cotización.
 * - Opción para convertir la cotización en una renta (si es aplicable).
 */

const route = useRoute();
const router = useRouter();
const cotizacion = ref(null);
const loading = ref(true);
const error = ref(null);
const isConverting = ref(false);

// Estado para la alerta
const alertState = ref({
  show: false,
  type: 'info',
  message: ''
});

// Función helper para mostrar alertas
const showAlert = (type, message) => {
  alertState.value = {
    show: true,
    type,
    message
  };
  // Auto-ocultar después de 5 segundos si es éxito
  if (type === 'success') {
    setTimeout(() => {
      alertState.value.show = false;
    }, 5000);
  }
};

// Carga los detalles de la cotización basada en el ID de la URL.
const loadCotizacion = async () => {
  try {
    const id = route.params.id;
    const response = await CotizacionService.getCotizacion(id);
    // Handle response structure { cotizacion: {...}, status: 200 } or direct object
    cotizacion.value = response.cotizacion || response;
    console.log('Cotizacion loaded:', cotizacion.value);
  } catch (err) {
    console.error('Error loading cotizacion:', err);
    error.value = 'No se pudo cargar la información de la cotización.';
  } finally {
    loading.value = false;
  }
};

// Convierte la cotización actual en una nueva renta.
// Prepara los datos iniciales y redirige al módulo de rentas tras la creación.
const convertToRental = async () => {
  if (!cotizacion.value) return;
  
  isConverting.value = true;
  alertState.value.show = false; // Ocultar alertas previas

  try {
    // Prepare rental data from quotation
    // Assuming default dates or prompting user would be better, but for now using defaults/current
    // Ideally, we should open a modal to confirm dates, but per requirement "pass to rental part"
    
    const rentalData = {
      cliente_id: cotizacion.value.cliente_id,
      cotizacion_id: cotizacion.value.id,
      fecha_inicio: new Date().toISOString().slice(0, 19).replace('T', ' '), // Default to now
      fecha_fin_prevista: cotizacion.value.fecha_validez 
          ? new Date(cotizacion.value.fecha_validez).toISOString().slice(0, 19).replace('T', ' ') 
          : new Date(Date.now() + 86400000).toISOString().slice(0, 19).replace('T', ' '), // Default +1 day
      estado_renta: 'Programada',
      monto_total_renta: cotizacion.value.monto_total,
      deposito_garantia: 0, // Default
      notas: `Generado desde Cotización #${cotizacion.value.id}`,
      // Include items if the backend supports creating rental items directly
      // Otherwise they might need to be added separately
    };

    console.log('Creating rental with data:', rentalData);
    await RentalService.createRental(rentalData);
    
    showAlert('success', 'Renta creada exitosamente! Redirigiendo...');
    
    // Pequeña pausa para que el usuario vea el mensaje antes de redirigir
    setTimeout(() => {
      router.push('/Rental');
    }, 1500);
    
  } catch (err) {
    console.error('Error converting to rental:', err);
    showAlert('danger', 'Error al convertir a renta: ' + (err.response?.data?.message || err.message));
  } finally {
    isConverting.value = false;
  }
};

const formatDate = (dateString) => {
  if (!dateString) return '-';
  return new Date(dateString).toLocaleDateString('es-CO', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const formatCurrency = (value) => {
  return new Intl.NumberFormat('es-CO', {
    style: 'currency',
    currency: 'COP',
    minimumFractionDigits: 0
  }).format(value);
};

const getStatusClass = (status) => {
  const classes = {
    'Borrador': 'bg-gray-100 text-gray-800',
    'Enviada': 'bg-blue-100 text-blue-800',
    'Aceptada': 'bg-green-100 text-green-800',
    'Rechazada': 'bg-red-100 text-red-800',
    'Vencida': 'bg-yellow-100 text-yellow-800'
  };
  return classes[status] || 'bg-gray-100 text-gray-800';
};

onMounted(() => {
  loadCotizacion();
});
</script>

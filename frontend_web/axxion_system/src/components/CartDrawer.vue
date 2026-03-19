<template>
  <div v-if="cartStore.isOpen" class="relative z-50" aria-labelledby="slide-over-title" role="dialog" aria-modal="true">
    <!-- Background backdrop, show/hide based on slide-over state. -->
    <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity backdrop-blur-sm" @click="cartStore.closeCart"></div>

    <div class="fixed inset-0 overflow-hidden">
      <div class="absolute inset-0 overflow-hidden">
        <div class="pointer-events-none fixed inset-y-0 right-0 flex max-w-full pl-10">
          <div class="pointer-events-auto w-screen max-w-md">
            <div class="flex h-full flex-col overflow-y-scroll bg-white shadow-xl">
              <div class="flex-1 overflow-y-auto px-4 py-6 sm:px-6">
                <div class="flex items-start justify-between">
                  <h2 class="text-lg font-medium text-gray-900" id="slide-over-title">Carrito de Cotización</h2>
                  <div class="ml-3 flex h-7 items-center">
                    <button type="button" class="relative -m-2 p-2 text-gray-400 hover:text-gray-500" @click="cartStore.closeCart">
                      <span class="absolute -inset-0.5"></span>
                      <span class="sr-only">Cerrar panel</span>
                      <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
                      </svg>
                    </button>
                  </div>
                </div>

                <!-- Alert Component -->
                <div v-if="alertState.show" class="mt-4">
                  <fwb-alert 
                    :type="alertState.type" 
                    closable 
                    @close="alertState.show = false"
                    :icon="true"
                  >
                    {{ alertState.message }}
                  </fwb-alert>
                </div>

                <div class="mt-8">
                  <div class="flow-root">
                    <ul role="list" class="-my-6 divide-y divide-gray-200">
                      <li v-for="item in cartStore.items" :key="item.id" class="flex py-6">
                        <div class="h-24 w-24 flex-shrink-0 overflow-hidden rounded-md border border-gray-200">
                          <img :src="item.imagen || 'https://via.placeholder.com/150'" :alt="item.nombre" class="h-full w-full object-cover object-center">
                        </div>

                        <div class="ml-4 flex flex-1 flex-col">
                          <div>
                            <div class="flex justify-between text-base font-medium text-gray-900">
                              <h3>
                                <a href="#">{{ item.nombre }}</a>
                              </h3>
                              <p class="ml-4">{{ formatCurrency(item.precio_alquiler_dia || item.precio_referencia_renta) }}</p>
                            </div>
                            <p class="mt-1 text-sm text-gray-500">{{ item.marca }} - {{ item.modelo }}</p>
                          </div>
                          <div class="flex flex-1 items-end justify-between text-sm">
                            <div class="flex items-center border border-gray-300 rounded">
                                <button @click="decrementQuantity(item)" class="px-2 py-1 hover:bg-gray-100">-</button>
                                <p class="px-2 text-gray-500">{{ item.quantity }}</p>
                                <button @click="incrementQuantity(item)" class="px-2 py-1 hover:bg-gray-100">+</button>
                            </div>

                            <div class="flex">
                              <button type="button" @click="cartStore.removeFromCart(item.id)" class="font-medium text-indigo-600 hover:text-indigo-500">Eliminar</button>
                            </div>
                          </div>
                        </div>
                      </li>
                      <li v-if="cartStore.items.length === 0" class="py-6 text-center text-gray-500">
                        No hay items en el carrito.
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <div class="border-t border-gray-200 px-4 py-6 sm:px-6">
                <div class="flex justify-between text-base font-medium text-gray-900">
                  <p>Subtotal (Estimado)</p>
                  <p>{{ formatCurrency(cartStore.totalPrice) }}</p>
                </div>
                <p class="mt-0.5 text-sm text-gray-500">El precio final se calculará en la cotización.</p>
                
                <!-- Checkout Form -->
                <div v-if="cartStore.items.length > 0" class="mt-6 space-y-4">
                    <div>
                        <label for="cliente" class="block text-sm font-medium text-gray-700">Cliente</label>
                        <div class="flex gap-2">
                            <select id="cliente" v-model="selectedClientId" class="mt-1 block w-full rounded-md border-gray-300 py-2 pl-3 pr-10 text-base focus:border-indigo-500 focus:outline-none focus:ring-indigo-500 sm:text-sm">
                                <option value="" disabled>Seleccione un cliente</option>
                                <option v-for="client in clients" :key="client.id" :value="client.id">
                                    {{ client.nombre }} {{ client.apellido1 }}
                                </option>
                            </select>
                            <button 
                                @click="openClientModal"
                                class="mt-1 inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
                                title="Agregar Nuevo Cliente"
                            >
                                <font-awesome-icon icon="fa-solid fa-plus" />
                            </button>
                        </div>
                    </div>
                     <div>
                        <label for="fecha_inicio" class="block text-sm font-medium text-gray-700">Fecha Inicio</label>
                        <input type="datetime-local" id="fecha_inicio" v-model="fechaInicio" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm">
                    </div>
                     <div>
                        <label for="fecha_fin" class="block text-sm font-medium text-gray-700">Fecha Fin</label>
                        <input type="datetime-local" id="fecha_fin" v-model="fechaFin" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm">
                    </div>
                </div>

                <div class="mt-6">
                  <button 
                    @click="checkout" 
                    :disabled="isProcessing || cartStore.items.length === 0 || !selectedClientId || !fechaInicio || !fechaFin"
                    class="flex w-full items-center justify-center rounded-md border border-transparent bg-indigo-600 px-6 py-3 text-base font-medium text-white shadow-sm hover:bg-indigo-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                  >
                    {{ isProcessing ? 'Procesando...' : 'Generar Cotización' }}
                  </button>
                </div>
                <div class="mt-6 flex justify-center text-center text-sm text-gray-500">
                  <p>
                    o
                    <button type="button" class="font-medium text-indigo-600 hover:text-indigo-500" @click="cartStore.closeCart">
                      Continuar Explorando
                      <span aria-hidden="true"> &rarr;</span>
                    </button>
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- Client Creation Modal -->
  <fwb-modal v-if="showClientModal" @close="closeClientModal" size="4xl">
    <template #header>
      <div class="flex items-center text-lg font-semibold text-gray-900 dark:text-white">
        <font-awesome-icon icon="fa-solid fa-user-plus" class="mr-2 text-green-600" />
        Agregar Nuevo Cliente
      </div>
    </template>
    <template #body>
      <ClientForm 
        mode="add"
        @success="handleClientSuccess"
        @cancel="closeClientModal"
      />
    </template>
  </fwb-modal>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useCartStore } from '@/stores/CartStore';
import SolicitudService from '@/services/SolicitudService';
import CotizacionService from '@/services/CotizacionService';
import ClienteService from '@/services/ClienteService';
import ClientForm from '@/components/ClientForm.vue'; // Import ClientForm
import { FwbAlert, FwbModal } from 'flowbite-vue'; // Import FwbModal

/**
 * Componente CartDrawer.
 * 
 * Panel deslizante (drawer) que muestra el contenido del carrito de cotización.
 * Permite al usuario:
 * - Ver los items seleccionados.
 * - Ajustar cantidades o eliminar items.
 * - Seleccionar un cliente y fechas para la cotización.
 * - Finalizar el proceso generando una Solicitud y una Cotización.
 */

const cartStore = useCartStore();
const router = useRouter();
const clients = ref([]);
const selectedClientId = ref('');
const fechaInicio = ref('');
const fechaFin = ref('');
const isProcessing = ref(false);
const showClientModal = ref(false); // State for modal

const openClientModal = () => {
    showClientModal.value = true;
};

const closeClientModal = () => {
    showClientModal.value = false;
};

const handleClientSuccess = async (newClient) => {
    try {
        // Reload clients to include the new one
        const response = await ClienteService.getAll();
        clients.value = response.cliente || response.data || response;
        
        // Auto-select the new client
        if (newClient && newClient.id) {
            selectedClientId.value = newClient.id;
        }
        
        showAlert('success', 'Cliente creado y seleccionado correctamente.');
        closeClientModal();
    } catch (error) {
        console.error("Error reloading clients", error);
        showAlert('warning', 'Cliente creado, pero hubo un error al recargar la lista.');
    }
};

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
};

// Carga la lista de clientes al montar el componente para el selector del checkout.
onMounted(async () => {
    try {
        const response = await ClienteService.getAll();
        // Response structure is { cliente: [...], status: 200 }
        clients.value = response.cliente || response.data || response; 
        console.log('Clients loaded:', clients.value);
    } catch (error) {
        console.error("Error loading clients", error);
    }
});

/**
 * Formatea un valor numérico a moneda colombiana (COP).
 * @param {number} value - Valor a formatear.
 * @returns {string} Valor formateado (ej. $1.000.000 COP).
 */
const formatCurrency = (value) => {
    if (!value) return '$0 COP';
    return new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP' }).format(value);
};

// Métodos para manipular la cantidad de items en el carrito
const incrementQuantity = (item) => {
    cartStore.updateQuantity(item.id, (item.quantity || 1) + 1);
};

const decrementQuantity = (item) => {
    cartStore.updateQuantity(item.id, (item.quantity || 1) - 1);
};

/**
 * Procesa el checkout (finalización de la cotización).
 * 1. Crea una Solicitud con los items del carrito.
 * 2. Crea una Cotización asociada a esa solicitud.
 * 3. Limpia el carrito y redirige al detalle de la cotización creada.
 */
const checkout = async () => {
    if (!selectedClientId.value || !fechaInicio.value || !fechaFin.value) return;
    
    isProcessing.value = true;
    alertState.value.show = false; // Ocultar alertas previas

    try {
        // 1. Crear Solicitud
        const solicitudData = {
            cliente_id: selectedClientId.value,
            fecha_solicitud: new Date().toISOString().slice(0, 19).replace('T', ' '),
            cantidad_solicitada: cartStore.itemCount,
            descripcion_necesidad: `Solicitud generada desde carrito web. Fechas: ${fechaInicio.value} a ${fechaFin.value}`,
            estado_solicitud: 'Nueva',
            productos: cartStore.items.map(item => ({
                producto_id: item.producto_id,
                cantidad: item.quantity
            }))
        };
        
        const solicitudResponse = await SolicitudService.create(solicitudData);
        const solicitudId = solicitudResponse.solicitud ? solicitudResponse.solicitud.id : solicitudResponse.id;

        // 2. Crear Cotización
        const cotizacionData = {
            cliente_id: selectedClientId.value,
            solicitud_id: solicitudId,
            fecha_cotizacion: new Date().toISOString().slice(0, 19).replace('T', ' '),
            fecha_validez: new Date(new Date().setDate(new Date().getDate() + 15)).toISOString().slice(0, 10), // Válido por 15 días
            monto_total: cartStore.totalPrice,
            estado_cotizacion: 'Borrador',
            terminos_condiciones: 'Términos estándar de alquiler.',
            notas_internas: 'Generado automáticamente.',
            detalles: cartStore.items.map(item => ({
                producto_id: item.producto_id,
                cantidad: item.quantity,
                precio_unitario: item.precio_alquiler_dia || item.precio_referencia_renta || 0,
                descripcion_item: item.nombre
            }))
        };

        const response = await CotizacionService.createCotizacion(cotizacionData);

        // Mostrar éxito
        showAlert('success', 'Cotización creada exitosamente! Redirigiendo...');

        // Esperar un momento antes de cerrar y redirigir
        setTimeout(() => {
            // 3. Limpiar carrito y redirigir
            cartStore.clearCart();
            cartStore.closeCart();
            
            // Redirigir a la vista de detalle de cotización
            const cotizacionId = response.cotizacion ? response.cotizacion.id : response.id;
            router.push(`/quotation/${cotizacionId}`);
        }, 1500);
        
    } catch (error) {
        console.error("Error during checkout", error);
        showAlert('danger', 'Error al procesar la solicitud. Por favor intente nuevamente.');
    } finally {
        isProcessing.value = false;
    }
};
</script>

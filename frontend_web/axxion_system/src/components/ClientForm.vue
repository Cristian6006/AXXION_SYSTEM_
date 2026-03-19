<template>
  <form @submit.prevent="handleSubmit" class="space-y-6">
    <!-- Mensaje de error -->
    <fwb-alert v-if="error" type="danger" closable @close="clearError">
      {{ error }}
    </fwb-alert>

    <!-- Mensaje de éxito -->
    <fwb-alert v-if="successMessage" type="success" closable @close="successMessage = ''">
      {{ successMessage }}
    </fwb-alert>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <!-- Nombre -->
      <div>
        <fwb-label for="nombre" value="Nombre *" />
        <fwb-input
          id="nombre"
          v-model="formData.nombre"
          type="text"
          placeholder="Ingrese el nombre"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Segundo Nombre -->
      <div>
        <fwb-label for="nombre2" value="Segundo Nombre *" />
        <fwb-input
          id="nombre2"
          v-model="formData.nombre2"
          type="text"
          placeholder="Ingrese el segundo nombre"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Primer Apellido -->
      <div>
        <fwb-label for="apellido1" value="Primer Apellido *" />
        <fwb-input
          id="apellido1"
          v-model="formData.apellido1"
          type="text"
          placeholder="Ingrese el primer apellido"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Segundo Apellido -->
      <div>
        <fwb-label for="apellido2" value="Segundo Apellido *" />
        <fwb-input
          id="apellido2"
          v-model="formData.apellido2"
          type="text"
          placeholder="Ingrese el segundo apellido"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- RFC -->
      <div>
        <fwb-label for="rfc" value="RFC / Documento" />
        <fwb-input
          id="rfc"
          v-model="formData.rfc"
          type="text"
          placeholder="Ingrese RFC o Documento"
          :disabled="isLoading"
        />
      </div>

      <!-- Teléfono -->
      <div>
        <fwb-label for="telefono_principal" value="Teléfono Principal" />
        <fwb-input
          id="telefono_principal"
          v-model="formData.telefono_principal"
          type="tel"
          placeholder="Ingrese teléfono"
          :disabled="isLoading"
        />
      </div>

      <!-- Email -->
      <div>
        <fwb-label for="correo_electronico" value="Correo Electrónico" />
        <fwb-input
          id="correo_electronico"
          v-model="formData.correo_electronico"
          type="email"
          placeholder="cliente@ejemplo.com"
          :disabled="isLoading"
        />
      </div>

      <!-- Tipo de Cliente -->
      <div>
        <label for="tipo_cliente" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
          Tipo de Cliente
        </label>
        <select
          id="tipo_cliente"
          v-model="formData.tipo_cliente"
          :disabled="isLoading"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <option value="Particular">Particular</option>
          <option value="Empresa">Empresa</option>
        </select>
      </div>

      <!-- Estado Cliente -->
      <div>
        <label for="estado_cliente" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
          Estado
        </label>
        <select
          id="estado_cliente"
          v-model="formData.estado_cliente"
          :disabled="isLoading"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <option value="Activo">Activo</option>
          <option value="Inactivo">Inactivo</option>
          <option value="Prospecto">Prospecto</option>
        </select>
      </div>
    </div>

    <!-- Botones de acción -->
    <div class="flex justify-end space-x-4 pt-6 border-t">
      <fwb-button
        type="button"
        color="alternative"
        @click="$emit('cancel')"
        :disabled="isLoading"
      >
        Cancelar
      </fwb-button>
      
      <fwb-button
        type="submit"
        color="green"
        :disabled="isLoading || !isFormValid"
      >
        <span v-if="isLoading" class="flex items-center">
          <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          Guardando...
        </span>
        <span v-else>Guardar Cliente</span>
      </fwb-button>
    </div>
  </form>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import ClienteService from '@/services/ClienteService';
import {
  FwbAlert,
  FwbButton,
  FwbInput,
  
} from 'flowbite-vue';

const props = defineProps({
  mode: {
    type: String,
    default: 'add'
  },
  selectedClient: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['success', 'cancel']);

// Estado del formulario
const formData = ref({
  nombre: '',
  nombre2: '',
  apellido1: '',
  apellido2: '',
  rfc: '',
  telefono_principal: '',
  correo_electronico: '',
  tipo_cliente: 'Particular',
  estado_cliente: 'Activo'
});

const isLoading = ref(false);
const error = ref('');
const successMessage = ref('');

// Validación básica
const isFormValid = computed(() => {
  return formData.value.nombre &&
         formData.value.nombre2 &&
         formData.value.apellido1 &&
         formData.value.apellido2;
});

const clearError = () => {
  error.value = '';
};

const resetForm = () => {
  formData.value = {
    nombre: '',
    nombre2: '',
    apellido1: '',
    apellido2: '',
    rfc: '',
    telefono_principal: '',
    correo_electronico: '',
    tipo_cliente: 'Particular',
    estado_cliente: 'Activo'
  };
  error.value = '';
  successMessage.value = '';
};

const handleSubmit = async () => {
  try {
    isLoading.value = true;
    error.value = '';
    successMessage.value = '';

    if (props.mode === 'add') {
      const response = await ClienteService.create(formData.value);
      successMessage.value = 'Cliente creado exitosamente';
      
      // Emitir evento de éxito con los datos del nuevo cliente
      // Asumiendo que la respuesta trae el cliente creado en response.cliente
      const newClient = response.cliente || response.data || response;
      emit('success', newClient);
      
      setTimeout(() => {
        resetForm();
      }, 1500);
    } 
    // Implementar update si fuera necesario en el futuro
    
  } catch (err) {
    console.error(err);
    error.value = err.response?.data?.message || err.message || 'Error al guardar cliente';
    if (err.response?.data?.errors) {
        // Si hay errores de validación detallados, mostrarlos
        const details = Object.values(err.response.data.errors).flat().join(', ');
        error.value += ': ' + details;
    }
  } finally {
    isLoading.value = false;
  }
};

// Cargar datos si es edición (por ahora solo 'add' es prioritario para el requerimiento)
watch(() => props.selectedClient, (newClient) => {
  if (newClient && props.mode === 'update') {
    formData.value = { ...newClient };
  }
}, { immediate: true });

</script>

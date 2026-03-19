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

    <!-- Formulario de Agregar Usuario -->
    <div v-if="props.mode === 'add'" class="grid grid-cols-1 md:grid-cols-2 gap-6">
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
        <fwb-label for="nombre2" value="Segundo Nombre" />
        <fwb-input
          id="nombre2"
          v-model="formData.nombre2"
          type="text"
          placeholder="Ingrese el segundo nombre (opcional)"
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
        <fwb-label for="apellido2" value="Segundo Apellido" />
        <fwb-input
          id="apellido2"
          v-model="formData.apellido2"
          type="text"
          placeholder="Ingrese el segundo apellido (opcional)"
          :disabled="isLoading"
        />
      </div>

      <!-- Nombre de Usuario -->
      <div>
        <fwb-label for="nombre_usuario" value="Nombre de Usuario *" />
        <fwb-input
          id="nombre_usuario"
          v-model="formData.nombre_usuario"
          type="text"
          placeholder="Ingrese el nombre de usuario"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Email -->
      <div>
        <fwb-label for="email" value="Email *" />
        <fwb-input
          id="email"
          v-model="formData.email"
          type="email"
          placeholder="usuario@empresa.com"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Teléfono -->
      <div>
        <fwb-label for="telefono" value="Teléfono *" />
        <fwb-input
          id="telefono"
          v-model="formData.telefono"
          type="tel"
          placeholder="Telefono"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Departamento -->
      <div>
        <label for="departamento" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
          Departamento *
        </label>
        <select
          id="departamento"
          v-model="formData.departamento"
          required
          :disabled="isLoading"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <option value="">Seleccione un departamento</option>
          <option value="Tecnología">Tecnología</option>
          <option value="Ventas">Ventas</option>
          <option value="Marketing">Marketing</option>
          <option value="Recursos Humanos">Recursos Humanos</option>
          <option value="Contabilidad">Contabilidad</option>
          <option value="Administración">Administración</option>
        </select>
      </div>

      <!-- Estado -->
      <div>
        <label for="estado" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
          Estado *
        </label>
        <select
          id="estado"
          v-model="formData.estado"
          required
          :disabled="isLoading"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <option value="">Seleccione un estado</option>
          <option value="activo">Activo</option>
          <option value="inactivo">Inactivo</option>
        </select>
      </div>

      <!-- Contraseña -->
      <div>
        <fwb-label for="password" value="Contraseña *" />
        <fwb-input
          id="password"
          v-model="formData.password"
          type="password"
          placeholder="Ingrese la contraseña"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Roles -->
      <div>
        <label class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
          Roles *
        </label>
        <div class="space-y-2 p-3 border border-gray-300 rounded-lg bg-gray-50 dark:bg-gray-700 dark:border-gray-600">
          <div v-for="role in availableRoles" :key="role.id" class="flex items-center">
            <input
              :id="`role-${role.id}`"
              type="checkbox"
              :value="role.id"
              v-model="formData.roles"
              :disabled="isLoading"
              class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600 disabled:opacity-50"
            />
            <label :for="`role-${role.id}`" class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">
              {{ role.nombre }}
            </label>
          </div>
        </div>
        <p class="text-sm text-gray-500 mt-1">Selecciona uno o más roles para el usuario</p>
      </div>
    </div>

    <!-- Formulario de Actualizar Usuario -->
    <div v-else-if="props.mode === 'update'" class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <!-- Nombre -->
      <div>
        <fwb-label for="nombre_update" value="Nombre *" />
        <fwb-input
          id="nombre_update"
          v-model="formData.nombre"
          type="text"
          placeholder="Ingrese el nombre"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Segundo Nombre -->
      <div>
        <fwb-label for="nombre2_update" value="Segundo Nombre" />
        <fwb-input
          id="nombre2_update"
          v-model="formData.nombre2"
          type="text"
          placeholder="Ingrese el segundo nombre (opcional)"
          :disabled="isLoading"
        />
      </div>

      <!-- Primer Apellido -->
      <div>
        <fwb-label for="apellido1_update" value="Primer Apellido *" />
        <fwb-input
          id="apellido1_update"
          v-model="formData.apellido1"
          type="text"
          placeholder="Ingrese el primer apellido"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Segundo Apellido -->
      <div>
        <fwb-label for="apellido2_update" value="Segundo Apellido" />
        <fwb-input
          id="apellido2_update"
          v-model="formData.apellido2"
          type="text"
          placeholder="Ingrese el segundo apellido (opcional)"
          :disabled="isLoading"
        />
      </div>

      <!-- Nombre de Usuario -->
      <div>
        <fwb-label for="nombre_usuario_update" value="Nombre de Usuario *" />
        <fwb-input
          id="nombre_usuario_update"
          v-model="formData.nombre_usuario"
          type="text"
          placeholder="Ingrese el nombre de usuario"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Email -->
      <div>
        <fwb-label for="email_update" value="Email *" />
        <fwb-input
          id="email_update"
          v-model="formData.email"
          type="email"
          placeholder="usuario@empresa.com"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Teléfono -->
      <div>
        <fwb-label for="telefono_update" value="Teléfono *" />
        <fwb-input
          id="telefono_update"
          v-model="formData.telefono"
          type="tel"
          placeholder="Telefono"
          required
          :disabled="isLoading"
        />
      </div>

      <!-- Departamento -->
      <div>
        <label for="departamento_update" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
          Departamento *
        </label>
        <select
          id="departamento_update"
          v-model="formData.departamento"
          required
          :disabled="isLoading"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <option value="">Seleccione un departamento</option>
          <option value="Tecnología">Tecnología</option>
          <option value="Ventas">Ventas</option>
          <option value="Marketing">Marketing</option>
          <option value="Recursos Humanos">Recursos Humanos</option>
          <option value="Contabilidad">Contabilidad</option>
          <option value="Administración">Administración</option>
        </select>
      </div>

      <!-- Estado -->
      <div>
        <label for="estado_update" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
          Estado *
        </label>
        <select
          id="estado_update"
          v-model="formData.estado"
          required
          :disabled="isLoading"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <option value="">Seleccione un estado</option>
          <option value="activo">Activo</option>
          <option value="inactivo">Inactivo</option>
        </select>
      </div>

      <!-- Contraseña (opcional para actualización) -->
      <div>
        <fwb-label for="password_update" value="Nueva Contraseña (opcional)" />
        <fwb-input
          id="password_update"
          v-model="formData.password"
          type="password"
          placeholder="Dejar vacío para mantener la actual"
          :disabled="isLoading"
        />
        <p class="text-sm text-gray-500 mt-1">Dejar vacío si no desea cambiar la contraseña</p>
      </div>

      <!-- Roles -->
      <div>
        <label class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
          Roles *
        </label>
        <div class="space-y-2 p-3 border border-gray-300 rounded-lg bg-gray-50 dark:bg-gray-700 dark:border-gray-600">
          <div v-for="role in availableRoles" :key="role.id" class="flex items-center">
            <input
              :id="`role-update-${role.id}`"
              type="checkbox"
              :value="role.id"
              v-model="formData.roles"
              :disabled="isLoading"
              class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600 disabled:opacity-50"
            />
            <label :for="`role-update-${role.id}`" class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">
              {{ role.nombre }}
            </label>
          </div>
        </div>
        <p class="text-sm text-gray-500 mt-1">Selecciona uno o más roles para el usuario</p>
      </div>
    </div>

    <!-- Formulario de Eliminar Usuario -->
    <div v-else-if="props.mode === 'delete'" class="space-y-6">
      <div class="bg-red-50 border border-red-200 rounded-lg p-6">
        <div class="flex items-center mb-4">
          <svg class="w-8 h-8 text-red-600 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.732-.833-2.5 0L4.268 19.5c-.77.833.192 2.5 1.732 2.5z"></path>
          </svg>
          <h3 class="text-lg font-semibold text-red-800">Confirmar Eliminación</h3>
        </div>
        
        <div v-if="props.selectedUser" class="bg-white border border-red-200 rounded-lg p-4 mb-4">
          <h4 class="font-medium text-gray-900 mb-2">Usuario a eliminar:</h4>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
            <div>
              <span class="font-medium text-gray-700">Nombre:</span>
              <span class="ml-2 text-gray-900">{{ getFullName(props.selectedUser) }}</span>
            </div>
            <div>
              <span class="font-medium text-gray-700">Usuario:</span>
              <span class="ml-2 text-gray-900">@{{ props.selectedUser.nombre_usuario }}</span>
            </div>
            <div>
              <span class="font-medium text-gray-700">Email:</span>
              <span class="ml-2 text-gray-900">{{ props.selectedUser.email }}</span>
            </div>
            <div>
              <span class="font-medium text-gray-700">Departamento:</span>
              <span class="ml-2 text-gray-900">{{ props.selectedUser.departamento }}</span>
            </div>
          </div>
        </div>

        <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
          <div class="flex">
            <svg class="w-5 h-5 text-yellow-600 mr-2 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
            <div>
              <h4 class="font-medium text-yellow-800 mb-1">Advertencia</h4>
              <p class="text-sm text-yellow-700">
                Esta acción no se puede deshacer. El usuario será eliminado permanentemente del sistema.
              </p>
            </div>
          </div>
        </div>

        <div class="mt-4">
          <label class="flex items-center">
            <input
              type="checkbox"
              v-model="confirmDelete"
              :disabled="isLoading"
              class="w-4 h-4 text-red-600 bg-gray-100 border-gray-300 rounded focus:ring-red-500 dark:focus:ring-red-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
            />
            <span class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">
              Confirmo que quiero eliminar este usuario permanentemente
            </span>
          </label>
        </div>
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
      
      <!-- Botón para modo Agregar -->
      <fwb-button
        v-if="props.mode === 'add'"
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
        <span v-else>Guardar Usuario</span>
      </fwb-button>

      <!-- Botón para modo Actualizar -->
      <fwb-button
        v-else-if="props.mode === 'update'"
        type="submit"
        color="blue"
        :disabled="isLoading || !isFormValid"
      >
        <span v-if="isLoading" class="flex items-center">
          <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          Actualizando...
        </span>
        <span v-else>Actualizar Usuario</span>
      </fwb-button>

      <!-- Botón para modo Eliminar -->
      <fwb-button
        v-else-if="props.mode === 'delete'"
        type="submit"
        color="red"
        :disabled="isLoading || !confirmDelete"
      >
        <span v-if="isLoading" class="flex items-center">
          <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          Eliminando...
        </span>
        <span v-else>Eliminar Usuario</span>
      </fwb-button>
    </div>
  </form>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useUserStore } from '@/stores/user.js';
import {
  FwbAlert,
  FwbButton,
  FwbInput
} from 'flowbite-vue';

/**
 * Componente UserForm.
 * 
 * Este componente gestiona el formulario para la administración de usuarios del sistema.
 * Permite crear nuevos usuarios, actualizar información existente y eliminar usuarios.
 * Maneja la asignación de roles, validación de campos y confirmación de acciones destructivas.
 * 
 * Modos soportados:
 * - 'add': Formulario para crear un nuevo usuario.
 * - 'update': Formulario para editar un usuario existente.
 * - 'delete': Vista de confirmación para eliminar un usuario.
 */

// Props y Emits
// mode: Define la operación actual ('add', 'update', 'delete').
// selectedUser: Objeto con los datos del usuario a editar o eliminar.
const props = defineProps({
  mode: {
    type: String,
    default: 'add', // 'add', 'update' o 'delete'
    validator: (value) => ['add', 'update', 'delete'].includes(value)
  },
  selectedUser: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['success', 'cancel']);

// Store
const userStore = useUserStore();

// Estado del formulario
const formData = ref({
  nombre: '',
  nombre2: '',
  apellido1: '',
  apellido2: '',
  nombre_usuario: '',
  email: '',
  telefono: '',
  departamento: '',
  estado: 'activo',
  password: '',
  roles: []
});

// Estado del componente
const isLoading = ref(false);
const error = ref('');
const successMessage = ref('');
const availableRoles = ref([]);
const confirmDelete = ref(false);

// Computed
/**
 * Verifica si el formulario es válido basándose en el modo actual.
 * - En 'add': Todos los campos obligatorios deben estar llenos.
 * - En 'update': Similar a 'add', pero la contraseña es opcional.
 * - En 'delete': Se requiere confirmación explícita.
 * @returns {boolean} True si es válido.
 */
const isFormValid = computed(() => {
  if (props.mode === 'add') {
    return formData.value.nombre &&
           formData.value.apellido1 &&
           formData.value.nombre_usuario &&
           formData.value.email &&
           formData.value.telefono &&
           formData.value.departamento &&
           formData.value.estado &&
           formData.value.password &&
           formData.value.roles.length > 0;
  } else if (props.mode === 'update') {
    return formData.value.nombre &&
           formData.value.apellido1 &&
           formData.value.nombre_usuario &&
           formData.value.email &&
           formData.value.telefono &&
           formData.value.departamento &&
           formData.value.estado &&
           formData.value.roles.length > 0;
  } else if (props.mode === 'delete') {
    return confirmDelete.value && props.selectedUser;
  }
  return false;
});

// Función para obtener el nombre completo
/**
 * Construye el nombre completo del usuario concatenando sus nombres y apellidos.
 * Filtra valores nulos o vacíos.
 */
const getFullName = (user) => {
  if (!user) return '';
  return [user.nombre, user.nombre2, user.apellido1, user.apellido2]
    .filter(Boolean)
    .join(' ');
};

// Métodos
const clearError = () => {
  error.value = '';
};

const resetForm = () => {
  formData.value = {
    nombre: '',
    nombre2: '',
    apellido1: '',
    apellido2: '',
    nombre_usuario: '',
    email: '',
    telefono: '',
    departamento: '',
    estado: 'activo',
    password: '',
    roles: []
  };
  confirmDelete.value = false;
  error.value = '';
  successMessage.value = '';
};

/**
 * Maneja el envío del formulario según el modo (crear, actualizar, eliminar).
 * Realiza llamadas al store correspondiente y gestiona mensajes de éxito/error.
 */
const handleSubmit = async () => {
  try {
    isLoading.value = true;
    error.value = '';
    successMessage.value = '';

    if (props.mode === 'add') {
      // Preparar datos para envío
      const userData = {
        ...formData.value,
        roles: Array.isArray(formData.value.roles) ? formData.value.roles : [formData.value.roles]
      };

      // Crear usuario
      await userStore.createUser(userData);
      successMessage.value = 'Usuario creado exitosamente';
      
    } else if (props.mode === 'update' && props.selectedUser) {
      // Preparar datos para actualización
      const updateData = {
        ...formData.value,
        roles: Array.isArray(formData.value.roles) ? formData.value.roles : [formData.value.roles]
      };
      
      // Si no se proporciona contraseña, eliminarla del objeto
      if (!updateData.password || updateData.password.trim() === '') {
        delete updateData.password;
      }
      
      // Actualizar usuario
      await userStore.updateUser(props.selectedUser.id, updateData);
      successMessage.value = 'Usuario actualizado exitosamente';
      
    } else if (props.mode === 'delete' && props.selectedUser) {
      // Eliminar usuario
      await userStore.deleteUser(props.selectedUser.id);
      successMessage.value = 'Usuario eliminado exitosamente';
    }
    
    // Emitir evento de éxito
    emit('success');
    
    // Resetear formulario después de un breve delay
    setTimeout(() => {
      resetForm();
    }, 2000);

  } catch (err) {
    const errorMessage = props.mode === 'add' 
      ? 'Error al crear el usuario' 
      : props.mode === 'update'
      ? 'Error al actualizar el usuario'
      : 'Error al eliminar el usuario';
    error.value = err.response?.data?.message || err.message || errorMessage;
  } finally {
    isLoading.value = false;
  }
};

// Cargar roles al montar el componente
/**
 * Carga la lista de roles disponibles desde el backend.
 * Si falla, carga roles por defecto para evitar bloqueo de la UI.
 */
onMounted(async () => {
  try {
    await userStore.fetchRoles();
    availableRoles.value = userStore.roles;
    console.log('Available roles after fetch:', availableRoles.value);
  } catch (err) {
    console.error('Error al cargar roles:', err);
    // Roles por defecto
    availableRoles.value = [
      { id: 1, nombre: 'Administrador' },
      { id: 2, nombre: 'Auxiliar' },
      { id: 3, nombre: 'Técnico' }
    ];
  }
});

// Watch para poblar el formulario cuando se selecciona un usuario para actualizar
/**
 * Observa cambios en `selectedUser` para rellenar el formulario cuando se entra en modo edición.
 * Mapea los datos del usuario seleccionado a la estructura del formulario.
 */
watch(() => props.selectedUser, (newUser) => {
  if (newUser && props.mode === 'update') {
    console.log('Selected user for update:', newUser);
    console.log('User roles:', newUser.roles);
    
    // Mapear roles correctamente - pueden venir como objetos o como IDs
    let userRoles = [];
    if (newUser.roles && Array.isArray(newUser.roles)) {
      userRoles = newUser.roles.map(role => {
        // Si el rol es un objeto, extraer el ID
        if (typeof role === 'object' && role.id) {
          return role.id;
        }
        // Si es un número, usarlo directamente
        return role;
      });
    }
    
    console.log('Mapped roles:', userRoles);
    
    formData.value = {
      nombre: newUser.nombre || '',
      nombre2: newUser.nombre2 || '',
      apellido1: newUser.apellido1 || '',
      apellido2: newUser.apellido2 || '',
      nombre_usuario: newUser.nombre_usuario || '',
      email: newUser.email || '',
      telefono: newUser.telefono || '',
      departamento: newUser.departamento || '',
      estado: newUser.estado || 'activo',
      password: '', // Siempre vacío para actualización
      roles: userRoles
    };
    
    console.log('Form data after update:', formData.value);
  }
}, { immediate: true });

// Watch para limpiar mensajes cuando cambie el formulario
watch(formData, () => {
  if (error.value) error.value = '';
  if (successMessage.value) successMessage.value = '';
}, { deep: true });
</script>

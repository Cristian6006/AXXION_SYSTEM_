<script setup lang="ts">
import { computed } from 'vue';

interface User {
  id: number;
  nombre: string;
  nombre2?: string;
  apellido1: string;
  apellido2?: string;
  nombre_usuario: string;
  departamento: string;
  email: string;
  telefono: string;
  estado: string;
};

/**
 * Componente UserCard.
 * 
 * Tarjeta de presentación para un usuario del sistema.
 * Muestra:
 * - Avatar y estado (activo/inactivo).
 * - Información personal y de contacto.
 * - Botones de acción rápida (editar, eliminar).
 */




const test = () => {
  console.log();
};

// Props
// user: Objeto con la información completa del usuario.
const props = defineProps<{
  user: User;
}>();

// Emits
// deleteUser: Evento para solicitar la eliminación del usuario.
// updateUser: Evento para solicitar la actualización del usuario.
const emit = defineEmits<{
  deleteUser: [user: User];
  updateUser: [user: User];
}>();

// Computed: Concatena los nombres y apellidos para mostrar el nombre completo.
const fullName = computed(() => {
  return [props.user.nombre, props.user.nombre2, props.user.apellido1, props.user.apellido2]
    .filter(Boolean)
    .join(' ');
});
</script>

<template>
  <div class="w-80 h-96 bg-white shadow-xl rounded-xl overflow-hidden my-4 border border-gray-100 hover:shadow-2xl transition-all duration-300 flex flex-col">
    <!-- Header con imagen y estado -->
    <div class="relative">
      <img 
        class="w-full h-48 object-cover object-center" 
        src="https://images.unsplash.com/photo-1517841905240-472988babdf9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=334&q=80" 
        alt="avatar"
      >
      <!-- Badge de estado -->
      <div class="absolute top-3 right-3">
        <span 
          :class="[
            'px-3 py-1 rounded-full text-xs font-semibold text-white',
            props.user.estado === 'activo' ? 'bg-green-500' : 'bg-red-500'
          ]"
        >
          {{ props.user.estado }}
        </span>
      </div>
      
      <div class="absolute top-3 left-3 flex gap-2">
        <!-- Botón eliminar -->
        <button
          @click="emit('deleteUser', props.user)"
          class="bg-red-500 hover:bg-red-600 text-white p-2 rounded-full transition-colors duration-200 shadow-lg"
          title="Eliminar usuario"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
          </svg>
        </button>

        <!-- Botón actualizar -->
        <button
          @click="emit('updateUser', props.user)"
          class="bg-blue-500 hover:bg-blue-600 text-white p-2 rounded-full transition-colors duration-200 shadow-lg"
          title="Actualizar usuario"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.5L15.232 5.232z"></path>
          </svg>
        </button>
      </div>
    </div>

    <!-- Información del usuario -->
    <div class="p-6 flex-1 flex flex-col justify-between">
      <!-- Nombre completo -->
      <h1 class="text-2xl font-bold text-gray-800 mb-2">{{ fullName }}</h1>
      
      <!-- Nombre de usuario -->
      <p class="text-sm text-gray-500 mb-4">@{{ props.user.nombre_usuario }}</p>

      <!-- Departamento -->
      <div class="flex items-center mb-4 p-3 bg-blue-50 rounded-lg">
        <svg class="h-5 w-5 text-blue-600 fill-current mr-3" viewBox="0 0 512 512">
          <path d="M256 48C150 48 64 136.2 64 245.1v153.3c0 36.3 28.6 65.7 64 65.7h64V288h-85.3v-42.9c0-84.7 66.8-153.3 149.3-153.3s149.3 68.5 149.3 153.3V288H320v176h64c35.4 0 64-29.3 64-65.7V245.1C448 136.2 362 48 256 48z"/>
        </svg>
        <span class="text-blue-800 font-medium">{{ props.user.departamento }}</span>
      </div>

      <!-- Información de contacto -->
      <div class="space-y-3 flex-1">
        <!-- Email -->
        <div class="flex items-center text-gray-700">
          <svg class="h-5 w-5 text-gray-500 fill-current mr-3 flex-shrink-0" viewBox="0 0 512 512">
            <path d="M437.332 80H74.668C51.199 80 32 99.198 32 122.667v266.666C32 412.802 51.199 432 74.668 432h362.664C460.801 432 480 412.802 480 389.333V122.667C480 99.198 460.801 80 437.332 80zM432 170.667L256 288 80 170.667V128l176 117.333L432 128v42.667z"/>
          </svg>
          <span class="text-sm ">{{ props.user.email }}</span>
        </div>

        <!-- Teléfono -->
        <div class="flex items-center text-gray-700">
          <svg class="h-5 w-5 text-gray-500 fill-current mr-3 flex-shrink-0" viewBox="0 0 512 512">
            <path d="M493.4 24.6l-104-24c-11.3-2.6-22.9 3.3-27.5 13.9l-48 112c-4.2 9.8-1.4 21.3 6.9 28l60.6 49.6c-36 76.7-98.9 140.5-177.2 177.2l-49.6-60.6c-6.8-8.3-18.2-11.1-28-6.9l-112 48C3.9 366.5-2 378.1.6 389.4l24 104C27.1 504.2 36.7 512 48 512c256.1 0 464-207.5 464-464 0-11.2-7.8-20.9-18.6-23.4z"/>
          </svg>
          <span class="text-sm">{{ props.user.telefono }}</span>
        </div>
      </div>

      <!-- Botón de acción -->
      <button class="w-full mt-4 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors duration-200">
        Ver Perfil
      </button>
    </div>
  </div>
</template>

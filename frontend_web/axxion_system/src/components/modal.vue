<template>
  <transition name="modal-fade">
    <div v-if="show" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50" @click.self="close">
      <transition name="modal-inner">
        <div v-if="show" class="relative w-full max-w-lg p-6 mx-4 bg-white rounded-lg shadow-lg">
          <div class="flex items-start justify-between pb-4 border-b rounded-t">
            <h3 class="text-xl font-semibold text-gray-900">
              <slot name="header">
                Modal Title
              </slot>
            </h3>
            <button type="button" class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center" @click="close">
              <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"></path></svg>
            </button>
          </div>
          <div class="py-6 space-y-6">
            <slot name="body">
              <p class="text-base leading-relaxed text-gray-500">
                This is the default modal body.
              </p>
            </slot>
          </div>
          <div class="flex items-center justify-end pt-4 space-x-2 border-t rounded-b">
            <slot name="footer">
              <button @click="close" type="button" class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">Accept</button>
            </slot>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

/**
 * Componente Modal.
 * 
 * Componente modal genérico y reutilizable.
 * Proporciona slots para header, body y footer, permitiendo una personalización completa.
 * Incluye transiciones de entrada/salida para una mejor experiencia de usuario.
 */

// Props
// show: Controla la visibilidad del modal.
const props = defineProps({
  show: {
    type: Boolean,
    required: true,
  },
});

// Emits
// close: Evento emitido cuando se debe cerrar el modal (clic en fondo o botón cerrar).
const emit = defineEmits(['close']);

const close = () => {
  emit('close');
};
</script>

<style scoped>
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.3s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-inner-enter-active {
  transition: all 0.3s ease-out;
}

.modal-inner-leave-active {
  transition: all 0.3s ease-in;
}

.modal-inner-enter-from {
  transform: translateY(-50px);
  opacity: 0;
}

.modal-inner-leave-to {
  transform: translateY(-50px);
  opacity: 0;
}
</style>

<template>
  <!-- Saca este html del componente y lo pone al final del body -->
  <Teleport to="body">
    <!-- Para que el fondo y la caja aparezcan suavemente -->
    <Transition
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <!-- Capa de fondo -->
      <div
        v-if="show"
        class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4"
        @click="emit('close')"
      >
        <!-- Contenido del modal -->
        <!-- @click.stop evita que al dar click dentro del modal, se cierre -->
        <div
          class="bg-white rounded-xl shadow-xl w-full max-w-lg overflow-hidden transform transition-all"
          @click.stop
        >
          <!-- Header -->
          <div v-if="$slots.header" class="bg-gray-100 px-6 py-4 border-b border-gray-200 font-bold text-gray-700">
            <slot name="header"></slot>
          </div>
          <!-- Body -->
          <div class="p-6">
            <slot></slot>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
// Recibimos la prop para saber si se muestra o no
  defineProps({
    show: Boolean
  });
  // Definimos eventos para decirle al padre que cierre
  const emit = defineEmits(['close']);
</script>


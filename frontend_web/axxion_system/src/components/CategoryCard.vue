<template>
  <FwbCard
    class="w-sm"
    img-alt="Categoria"
    img-src="https://flowbite.com/docs/images/blog/image-4.jpg"
    variant="horizontal"
  >
    <div class="p-5">
      <div class="flex gap-2">
        <!-- Botones editar / eliminar -->
        <FwbButton class=" transition  duration-300 ease-in-out hover:-translate-y-1 hover:scale-110"
          size="xs"
          color="yellow"
          @click="$emit('edit', category)"
        >
          <font-awesome-icon icon="fa-solid fa-pen-to-square" />
        </FwbButton>
        <FwbButton class=" transition  duration-300 ease-in-out hover:-translate-y-1 hover:scale-110"
          size="xs"
          color="red"
          @click="$emit('delete', category)"
        >
          <font-awesome-icon icon="fa-solid fa-trash" />
        </FwbButton>
      </div>

      <br />
      <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
        {{ category.nombre }}
      </h5>
      <p class="font-normal text-gray-700 dark:text-gray-400">
        {{ category.descripcion }}
        <br />
        <span class="text-sm text-blue-600">Tipo: {{ category.tipo_categoria }}</span>
      </p>
    </div>
  </FwbCard>
</template>

<script setup>
import { FwbButton, FwbCard } from "flowbite-vue";

/**
 * Componente CategoryCard.
 * 
 * Tarjeta visual para mostrar los detalles de una categoría individual.
 * Incluye botones de acción rápida para editar y eliminar.
 */

// Props: recibimos la categoría a mostrar
// Props: recibimos la categoría a mostrar
// category: Objeto con los datos de la categoría (nombre, descripción, tipo).
const props = defineProps({
  category: {
    type: Object,
    required: true,
  },
});

// Emitimos eventos al padre
// Emitimos eventos al padre para manejar las acciones
const emit = defineEmits(["edit", "delete"]);
</script>

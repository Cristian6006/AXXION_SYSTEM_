<template>
  <article class="basis-3/5 bg-black text-amber-50 rounded-md p-4">
    <h2 class="font-bold text-2xl mb-4">Subcategorias</h2>
    <div v-if="categoria">
      <h3 class="text-xl font-semibold mb-2">{{ categoria.nombre }}</h3>
      <p class="mb-2">{{ categoria.descripcion }}</p>
      <p class="italic text-sm text-gray-400 mb-4">Tipo: {{ categoria.tipo_categoria }}</p>

      <h4 class="text-lg font-semibold">Lista de subcategorías:</h4>
      <ul>
        <li v-for="sub in categoria.subcategorias" :key="sub.id"
            class="bg-gray-800 p-2 rounded-md mb-2 flex items-center justify-between">
          {{ sub.nombre }} - <span class="text-gray-400">{{ sub.descripcion }}</span>
          <div>
            <fwb-button gradient="blue" @click="$emit('edit-sub', sub)" pill square>
              <font-awesome-icon icon="fa-solid fa-pen-to-square" />
            </fwb-button>
            <fwb-button gradient="red" @click="$emit('delete-sub', sub)" pill square>
              <font-awesome-icon icon="fa-solid fa-trash" />
            </fwb-button>
          </div>
        </li>
        
      </ul>
    </div>
    <div v-else>
      <p>Selecciona una categoría para ver sus detalles y subcategorías.</p>
    </div>
  </article>
</template>

<script setup>
import { FwbButton } from 'flowbite-vue'
import { useSubcategoryStore } from '@/stores/useSubcategoryStore'

/**
 * Componente SubcategoryList.
 * 
 * Muestra la lista de subcategorías asociadas a una categoría seleccionada.
 * Permite editar y eliminar subcategorías mediante eventos emitidos al padre.
 */

// Props
// categoria: Objeto categoría seleccionado, que contiene sus subcategorías.
defineProps({
  categoria: {
    type: Object,
    default: null
  }
})
</script>


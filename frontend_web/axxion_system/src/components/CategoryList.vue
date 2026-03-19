<template>
  <article class="basis-2/5 bg-black text-amber-50 rounded-md p-4">
    <h2 class="font-bold text-2xl mb-4">Categorias</h2>
    <div v-if="categorias.length">
      <ul>
        <li v-for="cat in categorias" 
            :key="cat.id"
            @click="$emit('select', cat)"
            class="cursor-pointer bg-gray-800 p-2 rounded-md mb-2 hover:bg-indigo-700">
          {{ cat.nombre }}
          <fwb-button gradient="blue" @click.stop="$emit('edit', cat)" pill square>
            <font-awesome-icon icon="fa-solid fa-pen-to-square" />
          </fwb-button>
          <fwb-button gradient="red" @click.stop="$emit('delete', cat)" pill square>
            <font-awesome-icon icon="fa-solid fa-trash" />
          </fwb-button>
        </li>
      </ul>
    </div>
      <div v-if="categorias && categorias.length">
    <ul>
      
    </ul>
  </div>
  <div v-else>
    <p>No hay categorías registradas</p>
  </div>
  </article>

</template>

<script setup>
import { FwbButton } from 'flowbite-vue'

/**
 * Componente CategoryList.
 * 
 * Muestra una lista simple de categorías.
 * Permite seleccionar, editar y eliminar categorías mediante eventos.
 */
// Props
// categorias: Array de objetos categoría a listar.
defineProps({
  categorias: {
    type: Array,
    default: () => []   
  }
})
</script>


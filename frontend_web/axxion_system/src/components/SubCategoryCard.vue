<template>
  <div>
        <button 
          class="w-full text-left px-4 py-2 bg-gray-700 text-white rounded hover:bg-gray-600"
          @click="toggleCategoria(categoria.id)"
        >
          {{ categoria.nombre }}
        </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { FwbButton } from 'flowbite-vue'
import SubCategoryService from '@/services/SubCategoryService';
import CategoryService from '@/services/CategoryService'; // Asumiendo que existe, dado el uso en el código original

/**
 * Componente SubCategoryCard.
 * 
 * Botón/Tarjeta para seleccionar o alternar una categoría.
 * (Nota: El nombre sugiere 'SubCategory' pero parece manejar categorías principales).
 */
// Carga las categorías disponibles al montar el componente.
const fetchCategorias = async () => {
  try {
    const response = await CategoryService.getCategory();
    categorias.value = response.categoria;
  } catch (error) {
    console.error("Error al cargar categorías:", error);
  }
};

const categorias = ref([]);
const selectedCategoria = ref(null);

// Alterna la selección de una categoría.
// Si se hace clic en la misma, se deselecciona.
const toggleCategoria = (id) => {
  if (selectedCategoria.value?.id === id) {
    selectedCategoria.value = null;
  } else {
    selectedCategoria.value = categorias.value.find(cat => cat.id === id);
  }
};


onMounted(fetchCategorias);
</script>

<style scoped>

</style>>


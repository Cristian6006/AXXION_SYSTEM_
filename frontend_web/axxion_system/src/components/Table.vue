<template>
    <div class="container mx-auto px-4 sm:px-8 max-w-full">
        <div class="py-8">
            <div>
                <h2 class="text-2xl font-bold leading-tight text-gray-900 dark:text-white">Data</h2>
            </div>
            <div class="-mx-4 sm:-mx-8 px-4 sm:px-8 py-4 overflow-x-auto">
                <div class="inline-block min-w-full shadow-md rounded-lg overflow-hidden">
                    <table class="min-w-full leading-normal">
                        <thead class="bg-gray-50 dark:bg-gray-700">
                            <tr>
                                <th v-for="header in headers" :key="header"
                                    scope="col"
                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">
                                    {{ header }}
                                </th>
                            </tr>
                        </thead>
                        <tbody class="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
                            <tr v-if="items.length === 0">
                                <td :colspan="headers.length" class="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-500 dark:text-gray-400">
                                    No data available
                                </td>
                            </tr>
                            <tr v-for="(item, index) in items" :key="index" class="hover:bg-gray-100 dark:hover:bg-gray-600" :class="{ 'bg-gray-50 dark:bg-gray-700': index % 2 !== 0 }">
                                <td v-for="header in headers" :key="header" class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">
                                    {{ item[header] }}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'

/**
 * Componente Table.
 * 
 * Tabla de datos reutilizable y responsiva.
 * Genera automáticamente las cabeceras basándose en las claves del primer objeto de datos.
 * Soporta modo oscuro y estados vacíos.
 */

const items = ref([
  // Example of how data could look. Kept empty as requested.
  { 'Song': 'The Sliding Mr. Bones', 'Artist': 'Malcolm Lockyer', 'Year': 1961 },
  { 'Song': 'Witchy Woman', 'Artist': 'The Eagles', 'Year': 1972 },
]);

// Computed: Genera las cabeceras de la tabla dinámicamente.
// Si no hay datos, muestra cabeceras por defecto.
const headers = computed(() => {
    if (items.value.length === 0) {
        return ['Column 1', 'Column 2', 'Column 3']; // Default headers
    }
    return Object.keys(items.value[0]);
});
</script>

<style scoped>
</style>

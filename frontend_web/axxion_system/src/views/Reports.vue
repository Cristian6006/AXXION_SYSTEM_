<template>
  <div class="app flex">
    <SideBar/>
    <RouterView></RouterView>
    <main class="container h-screen p-4 flex-1 overflow-y-auto">
      <headerP />
      <section class="flex flex-col">
        <h1><strong>Reporte de inventario</strong></h1>
        <br>
        <Graphs/>
        <br>
      </section>
              <section>
                <div class="flex justify-end mb-4">
                    <ExportWorksheet
                    filename="inventario.xlsx"
                    sheetName="Inventario"
                    :data="inventario"
                    :columns="[
                    { key: 'id', label: 'ID' },
                    { key: 'nombre', label: 'Nombre' },
                    { key: 'marca', label: 'Marca' },
                    { key: 'modelo', label: 'Modelo' },
                    { key: 'estado', label: 'Estado' },
                    { key: 'ubicacion', label: 'Ubicación' },
                    ]"
                />
                </div>
            </section>
        <section class="bg-white rounded-md shadow-md p-4">
            <h2 class="text-xl font-bold mb-4">Detalle inventarios</h2>
            <div class="overflow-x-auto overflow-y-auto">
                <table class="w-full text-sm text-left text-gray-700 border">
                    <thead class="bg-gray-200 text-gray-900">
                        <tr>
                            <th class="p-2 border">Nombre</th>
                            <th class="p-2 border">Descripcion</th>
                            <th class="p-2 border">Marca</th>
                            <th class="p-2 border">Modelo</th>
                            <th class="p-2 border">Precio Referencia Renta</th>
                            <th class="p-2 border">Precio de alquiler diario</th>
                            <th class="p-2 border">Precio de alquiler semanal</th>
                            <th class="p-2 border">Precio de alquiler mensual</th>
                            <th class="p-2 border">Precio de compra</th>
                            <th class="p-2 border">Valor actual</th>
                            <th class="p-2 border">Fecha de compra</th>
                            <th class="p-2 border">Condicion</th>
                            <th class="p-2 border">Ubicacion</th>
                            <th class="p-2 border">Notas</th>
                            <th class="p-2 border">SKU</th>
                            <th class="p-2 border">Numero de serie</th>
                            <th class="p-2 border">Categoria</th>
                            <th class="p-2 border">Especificaciones</th>
                            <th class="p-2 border">Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="p in products" :key="p.id" class="hover:bg-gray-100">
                            <td class="p-2 border">{{ p.nombre }}</td>
                            <td class="p-2 border">{{ p.descripcion }}</td>
                            <td class="p-2 border">{{ p.marca }}</td>
                            <td class="p-2 border">{{ p.modelo }}</td>
                            <td class="p-2 border">{{ p.precio_referencia_renta || '-' }}</td>
                            <td class="p-2 border">{{ p.precio_alquiler_dia }}</td>
                            <td class="p-2 border">{{ p.precio_alquiler_semanal }}</td>
                            <td class="p-2 border">{{ p.precio_alquiler_mensual }}</td>
                            <td class="p-2 border">{{ p.precio_compra }}</td>
                            <td class="p-2 border">{{ p.valor_actual }}</td>
                            <td class="p-2 border">{{ p.fecha_compra }}</td>
                            <td class="p-2 border">
                              <span :class="{
                                'text-green-600 font-bold': p.condicion === 'Excelente',
                                'text-yellow-600 font-bold': p.condicion === 'Bueno'
                                }">
                                {{ p.condicion }}
                              </span>
                            </td>
                            <td class="p-2 border">{{ p.ubicacion }}</td>
                            <td class="p-2 border">{{ p.notas }}</td>
                            <td class="p-2 border">{{ p.sku }}</td>
                            <td class="p-2 border">{{ p.numero_serie }}</td>
                            <td class="p-2 border">{{ p.categoria }}</td>
                            <td class="p-2 border">{{ p.especificaciones }}</td>
                            <td class="p-2 border">{{p.estado}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import Graphs from '@/components/Graficos/Graphs.vue';
import InventoryService from '@/services/InventoryService';
import ExportWorksheet from '@/components/ExportWorksheet .vue'

/**
 * Vista Reports.
 * 
 * Reporte general de inventario.
 * Funcionalidades:
 * - Visualización de gráficos generales de inventario.
 * - Tabla detallada de todos los productos en inventario con información completa (precios, estado, ubicación, etc.).
 * - Exportación del inventario completo a Excel.
 */

const products = ref([]);
const inventario = ref([]);

onMounted(async () => {
  try {
    const data = await InventoryService.getProducts();
    products.value = Array.isArray(data) ? data : data.data || [];
    console.log('Inventario cargado:', products.value);
  } catch (error) {
    console.error('Error al cargar inventario:', error);
  }
});
</script>

<style scoped>

</style>





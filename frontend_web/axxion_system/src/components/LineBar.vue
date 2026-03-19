<template>
    <section class="flex flex-wrap gap-6">
        <!-- Gráfico por Categoría -->
        <article class="bg-gray-800 shadow-xl/30 text-amber-50 rounded-md pl-4 p-4 flex-1 min-w-[450px]">
            <h2 class="font-bold text-2xl mb-4">Equipos por Categoría</h2>
            <v-chart :option="chartByCategory" style="height: 450px;" autoresize />
        </article>
        <!-- Gráfico por Valor -->
        <article class="bg-gray-800 shadow-xl/30 text-amber-50 rounded-md pl-4 p-4 flex-1 min-w-[450px]">
            <h2 class="font-bold text-2xl mb-4">Valor Total por Categoría</h2>
            <v-chart :option="chartByValue" style="height: 450px;" autoresize />
        </article>
        <!-- Gráfico por Condición -->
        <article class="bg-gray-800 shadow-xl/30 text-amber-50 rounded-md pl-4 p-4 flex-1 min-w-[300px]">
            <h2 class="font-bold text-2xl mb-4">Equipos por Condición</h2>
            <v-chart :option="chartByCondition" style="height: 300px;" autoresize />
        </article>
        
    </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart, LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import InventoryService from '@/services/InventoryService'
import ReportService from '@/services/ReportService'
import { text } from '@fortawesome/fontawesome-svg-core'
import LineBar from '@/components/LineBar.vue'

/**
 * Componente LineBar.
 * 
 * Visualiza estadísticas de inventario utilizando gráficos de líneas (LineChart) de ECharts.
 * Similar a BarChart, pero enfocado en tendencias o visualización lineal.
 * Muestra:
 * - Equipos por Categoría.
 * - Valor Total por Categoría.
 * - Equipos por Condición.
 */


// registrar echarts
use([
  CanvasRenderer,
  PieChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  LineChart
])

// opciones reactivas
// Estado reactivo para los datos de los gráficos
const chartByCategory = ref({})
const chartByValue = ref({})
const chartByCondition = ref({})

// Métricas generales (aunque no se usan directamente en el template actual, se cargan)
const itemsTotales = ref(0)
const valorTotal = ref(0)
const equiposDisponibles = ref(0)
// cargar datos
// Cargar datos al montar el componente
onMounted(async () => {
    try {
        const data = await ReportService.getMetrics()
        itemsTotales.value = data.items_totales
        valorTotal.value = data.valor_total
        equiposDisponibles.value = data.equipos_disponibles
    } catch (error) {
        console.error('Error cargando métricas:', error)
    }


  try {
    const items = await InventoryService.getProducts() 
    console.log('Inventario (productos):', items)

    // Procesar items para generar datos de los gráficos
    const categorias = {}
    const valores = {}
    const condiciones = {}

    items.forEach(item => {
        const categoria = item.categoria || 'Sin categoría'
        categorias[categoria] = (categorias[categoria] || 0) + 1
        valores[categoria] = (valores[categoria] || 0) + parseFloat(item.valor_actual || 0)
        const condicion = item.condicion || 'No definida'
        condiciones[condicion] = (condiciones[condicion] || 0) + 1
    })

    // Configuración de los gráficos
    chartByCategory.value = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: Object.keys(categorias) },
      yAxis: { type: 'value' },
      series: [
        {
          data: Object.values(categorias),
          type: 'line',
          itemStyle: { color: '#1E90FF' }
        }
      ]
    }
    chartByValue.value = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: Object.keys(valores) },
      yAxis: { type: 'value' },
      series: [
        {
          data: Object.values(valores),
          type: 'line',
          itemStyle: { color: '#1E90FF' }
        }
      ]
    }
    chartByCondition.value = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: Object.keys(condiciones) },
      yAxis: { type: 'value' },
      series: [
        {
          data: Object.values(condiciones),
          type: 'line',
          itemStyle: { color: '#1E90FF' }
        }
      ]
    }
  } catch (error) {
    console.error('Error cargando inventario:', error)
  }
})
</script>
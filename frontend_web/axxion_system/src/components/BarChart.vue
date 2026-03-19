<template>
    <section class="flex flex-wrap gap-6">
        <!-- Gráfico por Categoría -->
        <article v-if="props.mode === 'all' || props.mode === 'category'" class="bg-gray-800 shadow-xl/30 text-amber-50 rounded-md pl-4 p-4 flex-1 min-w-[450px]">
            <h2 class="font-bold text-2xl mb-4">Equipos por Categoría</h2>
            <v-chart :option="chartByCategory" style="height: 450px;" autoresize />
        </article>
        <!-- Gráfico por Valor -->
        <article v-if="props.mode === 'all' || props.mode === 'value'" class="bg-gray-800 shadow-xl/30 text-amber-50 rounded-md pl-4 p-4 flex-1 min-w-[450px] ">
            <h2 class="font-bold text-2xl mb-4">Valor Total por Categoría</h2>
            <v-chart :option="chartByValue" style="height: 450px;" autoresize />
        </article>
        <!-- Gráfico por Condición -->
        <article v-if="props.mode === 'all' || props.mode === 'condition'" class="bg-gray-800 shadow-xl/30 text-amber-50 rounded-md pl-4 p-4 flex-1 min-w-[300px]">
            <h2 class="font-bold text-2xl mb-4">Equipos por Condición</h2>
            <v-chart :option="chartByCondition" style="height: 300px;" autoresize />
        </article>
        
    </section>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart, LineChart } from 'echarts/charts'
import { FwbCard } from 'flowbite-vue'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { UniversalTransition } from 'echarts/features';
import InventoryService from '@/services/InventoryService'
import ReportService from '@/services/ReportService'
import { text } from '@fortawesome/fontawesome-svg-core'
import { defineProps } from 'vue'

/**
 * Componente BarChart.
 * 
 * Visualiza estadísticas de inventario utilizando gráficos de ECharts.
 * Muestra tres tipos de gráficos según el modo seleccionado:
 * - Por Categoría: Cantidad de equipos por categoría.
 * - Por Valor: Valor monetario total por categoría.
 * - Por Condición: Estado físico de los equipos.
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
    LineChart,
    UniversalTransition
])

// opciones reactivas
const chartByCategory = ref({})
const chartByValue = ref({})
const chartByCondition = ref({})

const itemsTotales = ref(0)
const valorTotal = ref(0)
const equiposDisponibles = ref(0)

// Props
// items: Lista de equipos para generar los gráficos.
// metrics: Métricas pre-calculadas (opcional).
// mode: Filtro de visualización ('all', 'category', 'value', 'condition').
const props = defineProps({
    items: { type: Array, default: null },
    metrics: { type: Object, default: null },
    mode: { type: String, default: 'all' } 
})

/**
 * Procesa la lista de items para generar los datos necesarios para los gráficos.
 * Agrupa por categoría y condición, y suma los valores monetarios.
 * @param {Array} items - Lista de equipos del inventario.
 */
function buildChartsFromItems(items) {
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

    chartByCategory.value = {
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: Object.keys(categorias) },
        yAxis: { type: 'value' },
        series: [
            {
                data: Object.values(categorias),
                type: 'bar',
                itemStyle: { color: '#3b82f6' }
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
                type: 'bar',
                itemStyle: { color: '#3b82f6' }
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
                type: 'bar',
                itemStyle: { color: '#3b82f6' }
            }
        ]
    }
}

// Observa cambios en la prop 'items' para regenerar los gráficos automáticamente.
watch(() => props.items, (newItems) => {
    if (newItems && Array.isArray(newItems)) buildChartsFromItems(newItems)
})

// Al montar, carga métricas y datos si no se proporcionan vía props.
// Esto permite que el componente funcione de forma autónoma o controlada por el padre.
onMounted(async () => {
    if (props.metrics) {
        itemsTotales.value = props.metrics.items_totales || 0
        valorTotal.value = props.metrics.valor_total || 0
        equiposDisponibles.value = props.metrics.equipos_disponibles || 0
    } else {
        try {
            const data = await ReportService.getMetrics()
            itemsTotales.value = data.items_totales
            valorTotal.value = data.valor_total
            equiposDisponibles.value = data.equipos_disponibles
        } catch (error) {
            console.error('Error cargando métricas:', error)
        }
    }

    if (props.items && Array.isArray(props.items)) {
        buildChartsFromItems(props.items)
        return
    }

    try {
        const items = await InventoryService.getProducts()
        buildChartsFromItems(items)
    } catch (error) {
        console.error('Error cargando inventario:', error)
    }
})
</script>

<style>

</style>
<template>
    <div>
        <section class="flex flex-wrap gap-6">
            <fwb-card class="w-sm flex-1 min-w-[300px] bg-linear-to-br from-[#00c54e] to-[#01a93f]">
                <div class="p-5">
                    <h5 class="mb-2 text-lg font-bold tracking-tight text-gray-900 dark:text-white">Items Totales</h5>
                    <p class="text-4xl font-extrabold text-white">{{ itemsTotales }}</p>
                </div>
            </fwb-card>
            <fwb-card class="w-sm flex-1 min-w-[300px] bg-linear-to-br from-[#9914fb] to-[#ac45ff]">
                <div class="p-5">
                    <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900  dark:text-white">
                        Valor Total del Inventario
                    </h5>
                    <p class="text-3xl font-extrabold text-white flex justify-between">
                        <b>
                            ${{ valorTotal.toLocaleString() }}
                        </b>
                        <b class="bg-[#b254ff] rounded-full p-2">
                            <font-awesome-icon icon="fa-solid fa-dollar-sign" />
                        </b>
                    </p>
                </div>
            </fwb-card>
            <fwb-card class="w-sm flex-1 min-w-[300px] bg-linear-to-br from-[#f44a01] to-[#fe6900]">
                <div class="p-5">
                    <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900  dark:text-white">
                        Equipos disponibles
                    </h5>
                    <div class="text-4xl font-extrabold text-white flex justify-between" >
                        <div>
                            {{ equiposDisponibles }}
                        </div>
                        <div class="bg-[#fb7532] rounded-full p-2">
                            <font-awesome-icon icon="fa-solid fa-screwdriver-wrench" />
                        </div>
                    </div>
                </div>
            </fwb-card>
        </section>
        <br><br>
        <!-- SELECTOR -->
        <h4>Selecciona un gráfico</h4>
        <select v-model="selectedType" class="bg-gray-800 rounded-xl text-amber-50 container p-2">
            <option disabled value="">Selecciona un gráfico</option>
            <option value="line">Lineal</option>
            <option value="bar">Barra</option>
            <option value="pie">Pastel</option>
        </select>
        
        <br><br>
        <section class="flex flex-wrap gap-6">
            
            <!-- Gráfico 1: Categoría -->
            <article class="bg-gray-800 shadow-xl/30 text-white rounded-md p-4 flex-1 min-w-[450px]">
                <h2 class="font-bold text-2xl mb-4">Equipos por Categoría</h2>
                <v-chart :option="chartOptionsCategory" style="height: 300px;" autoresize />
            </article>

            <!-- Gráfico 2: Valor -->
            <article class="bg-gray-800 shadow-xl/30 text-white rounded-md p-4 flex-1 min-w-[450px]">
                <h2 class="font-bold text-2xl mb-4">Valor Total por Categoría</h2>
                <v-chart :option="chartOptionsValue" style="height: 300px;" autoresize />
            </article>

            <!-- Gráfico 3: Condición -->
            <article class="bg-gray-800 shadow-xl/30 text-white rounded-md p-4 flex-1 min-w-[450px]">
                <h2 class="font-bold text-2xl mb-4">Equipos por Condición</h2>
                <v-chart :option="chartOptionsCondition" style="height: 300px;" autoresize />
            </article>    
        </section>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import InventoryService from '@/services/InventoryService'
import ReportService from '@/services/ReportService'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, BarChart, LineChart } from 'echarts/charts'
import { FwbCard } from 'flowbite-vue'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'

use([CanvasRenderer, PieChart, BarChart, LineChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent])

// ESTADO: Variables simples para datos 
const itemsTotales = ref(0)
const valorTotal = ref(0)
const equiposDisponibles = ref(0)
const selectedType = ref('line') // 'line', 'bar', 'pie'

// Guardamos solo los DATOS PROCESADOS
const rawData = ref({
    categories: {}, 
    values: {},     
    conditions: {}  
})

// Esta función crea la configuración de ECharts dinámicamente
const getChartConfig = (type, dataObj, colorHex) => {
    const labels = Object.keys(dataObj);
    const values = Object.values(dataObj);
    
    // Configuración base común (estilos de texto, tooltip)
    const baseConfig = {
        tooltip: { trigger: type === 'pie' ? 'item' : 'axis' },
        legend: { textStyle: { color: "#fff" }, top: '5%' },
        grid: { containLabel: true, left: '3%', right: '4%', bottom: '3%' },
    };

    // Configuración específica según el tipo seleccionado
    if (type === 'pie') {
        return {
            ...baseConfig,
            series: [{
                type: 'pie',
                radius: ['40%', '70%'],
                itemStyle: { borderRadius: 10, borderColor: '#121212', borderWidth: 1 },
                label: { show: false, position: 'center' },
                emphasis: { label: { show: true, fontSize: 20, fontWeight: 'bold' } },
                // Para pastel necesitamos transformar los datos a [{name: 'A', value: 10}]
                data: labels.map((label, index) => ({ name: label, value: values[index] }))
            }]
        };
    } else {
        // Configuración para Linea y Barra (comparten ejes X/Y)
        return {
            ...baseConfig,
            xAxis: { 
                type: 'category', 
                data: labels,
                axisLabel: { color: "#fff" },
                axisLine: { lineStyle: { color: "#fff" } }
            },
            yAxis: { 
                type: 'value',
                axisLabel: { color: "#fff" },
                splitLine: { lineStyle: { color: "rgba(255, 255, 255, 0.2)" } }
            },
            series: [{
                data: values,
                type: type, // 'line' o 'bar'
                smooth: true, // Hace la línea curva si es lineal
                areaStyle: type === 'line' ? { opacity: 0.3 } : null,
                itemStyle: { color: colorHex },
                color: colorHex // Color principal
            }]
        };
    }
}

// Cada vez que 'selectedType' o 'rawData' cambien, estas variables se recalculan solas.

const chartOptionsCategory = computed(() => {
    return getChartConfig(selectedType.value, rawData.value.categories, '#FA15DF');
});

const chartOptionsValue = computed(() => {
    return getChartConfig(selectedType.value, rawData.value.values, '#6317FA');
});

const chartOptionsCondition = computed(() => {
    return getChartConfig(selectedType.value, rawData.value.conditions, '#3DFA17');
});

onMounted(async () => {
    try {
        // Carga métricas rápidas
        const metrics = await ReportService.getMetrics();
        itemsTotales.value = metrics.items_totales;
        valorTotal.value = metrics.valor_total;
        equiposDisponibles.value = metrics.equipos_disponibles;

        // Carga inventario completo
        const items = await InventoryService.getProducts();
        
        // Procesamos los datos y los guardamos en rawData
        const cats = {};
        const vals = {};
        const conds = {};

        items.forEach(item => {
            const cat = item.categoria || 'Sin categoría';
            cats[cat] = (cats[cat] || 0) + 1;
            vals[cat] = (vals[cat] || 0) + parseFloat(item.valor_actual || 0);
            
            const cond = item.condicion || 'No definida';
            conds[cond] = (conds[cond] || 0) + 1;
        });

        // Actualizamos la referencia reactiva una sola vez
        rawData.value = {
            categories: cats,
            values: vals,
            conditions: conds
        };

    } catch (error) {
        console.error('Error cargando datos:', error);
    }
});
</script>
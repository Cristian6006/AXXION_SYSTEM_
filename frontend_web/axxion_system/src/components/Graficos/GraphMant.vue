<template>
    <div>
        <section class="flex flex-wrap justify-center gap-12">
            <fwb-card class="relative w-sm flex-1 min-w-[300px] bg-linear-to-br from-[#336bbd] to-[#0046ad]">
                <div class="p-5 relative z-10">
                    <h5 class="mb-2 text-lg font-bold tracking-tight text-white">
                        Tiempo Promedio de reparacion
                    </h5>
                    <p class="text-2xl font-bold text-white">
                        {{ tiempoPromedioReparacion }} Dias
                    </p>
                    <div v-if="isLoading" class="absolute inset-y-3 right-8 p-10">
                        <span class="loader border-blue-600"></span>
                    </div>
                </div>
                <span class="absolute -inset-y-4 -right-3 opacity-20 pr-3">
                    <svg 
                        xmlns="http://www.w3.org/2000/svg"
                        class="w-35 h-35"
                        viewBox="0 -960 960 960"
                        fill="#FFFFFF">
                        <path d="M729.44-129.83v-255.26l212.43 127.35-212.43 127.91Zm-213.01 0v-255.26l213.01 127.35-213.01 127.91ZM480-480ZM375.56-65.87 355-196.39q-15.61-5.31-34.07-15.89-18.45-10.59-31.63-21.33l-121.95 56.26-106-187.17 111.39-81.26q-1.44-7.31-1.94-17.11-.5-9.81-.5-17.11 0-7.3.5-17.11.5-9.8 1.94-17.11L61.35-596.04l106-186.05 123.08 55.7q12.61-10.18 30.79-20.48 18.17-10.3 33.78-15.17l20.56-132.66h208.88L605-763.04q15.61 5.87 34.28 15.67 18.68 9.8 31.42 20.98l122.52-55.7 105.43 186.05-111.95 79.82q.43 7.74 1.21 17.83.79 10.09.79 16.82v18.24q0 8.37.56 18.24h-79.22q-.56-9.87-1.13-18.24-.56-8.37-.56-18.24.56-20.56-2-36.34-2.57-15.79-6.57-31.35l93.44-67.87-35.48-64.65-105.44 46q-23-27.13-53.13-45.48-30.13-18.35-65.69-22.78l-14-110.87h-78.96l-13.43 110.87q-35.13 6.43-65.76 24.28-30.63 17.85-54.2 43.98l-104.87-46-35.48 64.65 92.31 67.3q-4 17.57-6.79 34.92-2.78 17.34-2.78 34.91 0 17.57 2.78 35.2 2.79 17.63 6.79 34.63l-92.31 67.3 35.48 64.65 104.87-46q25.44 26.57 58.65 44.35 33.22 17.78 69.65 26.35v187.65h-59.87Zm61.01-291.56v-74.79q-8.61-8.87-13.63-20.95-5.03-12.09-5.03-26.83 0-25.61 17.68-43.28 17.67-17.68 43.28-17.68 25.04 0 42.72 17.68 17.67 17.67 17.67 43.28 0 9.87-2.72 18.96-2.71 9.08-7.58 15.95H604q2.43-8.87 3.65-17.24 1.22-8.37 1.22-17.67 0-54-38-92t-92-38q-54 0-92 38t-38 92q0 43.57 24.56 76.35 24.57 32.78 63.14 46.22Z"/>
                    </svg>
                </span>
            </fwb-card>
            <fwb-card class="relative w-sm flex-1 min-w-[300px]" :class='porcentaje'>
                <div class="p-5 relative z-10">
                    <h5 class="mb-2 text-lg font-bold tracking-tight text-white">
                        Tasa de incumplimiento
                    </h5>
                    <p class="text-2xl font-bold text-white">
                        {{ tasaIncumplimiento }}
                    </p>
                    <div v-if="isLoading" class="absolute inset-y-3 right-8 p-10">
                        <span class="loader border-blue-600"></span>
                    </div>
                </div>
                <span class="absolute -inset-y-6 -right-15 opacity-20 pr-3">
                    <svg 
                        xmlns="http://www.w3.org/2000/svg"
                        class="w-50 h-50"
                        viewBox="0 -960 960 960"
                        fill="#FFFFFF">
                        <path d="m343-283 137-104 137 104-55-173 126-82H537l-57-173-57 173H272l126 82-55 173ZM218.87-98.52l98.09-321.52L55.69-607.35h321.87L480-946l102.22 338.65h322.65L642.9-420.18l99.04 321.66-261.25-198.93L218.87-98.52ZM480-497Z"/>
                    </svg>
                </span>
            </fwb-card>
            <fwb-card class="relative w-sm flex-1 min-w-[300px] bg-linear-to-br from-[#bf04d4] to-[#8a00ad]">
                <div class="p-5 relative z-10">
                    <h5 class="mb-2 text-lg font-bold tracking-tight text-white">
                        Mantenimientos activos
                    </h5>
                    <p class="text-2xl font-bold text-white">
                        {{ totalAnalizados }}
                    </p>
                    <div v-if="isLoading" class="absolute inset-y-3 right-8 p-10">
                        <span class="loader border-blue-600"></span>
                    </div>
                </div>
                <span class="absolute -inset-y-4 -right-3 opacity-20 pr-3">
                    <svg 
                        xmlns="http://www.w3.org/2000/svg"
                        class="w-35 h-35"
                        viewBox="0 -960 960 960"
                        fill="#FFFFFF">
                        <path d="M446.96-279.78h66.13l11.28-55.82q17.89-5.39 30.89-13.79 13-8.39 23.87-19.26l64.26 19 31.09-62.48-49.26-31.7q3.43-19.3 3.43-36.17t-3.43-36.74l49.26-31.13-31.09-63.04-64.26 19.56q-10.87-10.87-23.87-19.26-13-8.4-30.89-13.79l-11.28-55.82h-66.13l-10.61 55.7q-18.87 5.43-31.74 13.87-12.87 8.43-23.74 19.3l-64.26-19.56-31.09 63.04 49.26 31.13q-3.43 19.87-3.43 36.74t3.43 36.17l-49.26 31.7 31.09 62.48 64.26-19q10.87 10.87 23.74 19.3 12.87 8.44 31.74 13.87l10.61 55.7Zm32.85-118.61q-34.68 0-58.05-23.56-23.37-23.56-23.37-58.24 0-34.68 23.56-58.05 23.56-23.37 58.24-23.37 34.68 0 58.05 23.56 23.37 23.56 23.37 58.24 0 34.68-23.56 58.05-23.56 23.37-58.24 23.37ZM185.09-105.87q-32.51 0-55.87-23.35-23.35-23.36-23.35-55.87v-589.82q0-32.74 23.35-56.26 23.36-23.53 55.87-23.53h589.82q32.74 0 56.26 23.53 23.53 23.52 23.53 56.26v589.82q0 32.51-23.53 55.87-23.52 23.35-56.26 23.35H185.09Zm0-79.22h589.82v-589.82H185.09v589.82Zm0-589.82v589.82-589.82Z"/>
                    </svg>
                </span>
            </fwb-card>
        </section>
        <br><br>
        <section class="bg-white rounded-md shadow-md p-4">
            <h2 class="text-xl font-bold mb-4 text-black">
                Resumen de Mantenimientos
            </h2>
            <!-- Estado de carga -->
            <div v-if="isLoading" class="flex justify-center p-4">
                <span class="loader"></span>
            </div>
            <!-- Tabla de datos -->
            <div class="overflow-x-auto overflow-y-auto">
                <table v-if="mantenimiento.length > 0" class="w-full text-sm text-left text-gray-700 border">
                    <thead class="bg-gray-200 text-gray-900">
                        <tr>
                            <th class="p-2 border">Id mantenimiento</th>
                            <th class="p-2 border">Id Equipo</th>
                            <th class="p-2 border">Dias de reparacion</th>
                            <th class="p-2 border">Dias de retraso</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="m in mantenimiento" :key="m.id" class="hover:bg-red-50 border-b">
                            <td class="p-2 border font-bold">{{ m.id }}</td>
                            <td class="p-2 border">{{ m.item }}</td>
                            <td class="p-2 border font-bold text-center">{{ m.dias_reparacion }}</td>
                            <td class="p-2 border font-bold bg-red-100 text-red-800 rounded">{{ m.retraso_dias }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </section>
        <br><br>
        <section>
            <article class="bg-gray-800 shadow-xl/30 text-white rounded-md p-4 flex-1 min-w-[900px]">
                <h2 class="font-bold text-2xl mb-4">costo total de propiedad</h2>
                <v-chart :option="chartByTCO" style="height: 300px;" autoresize />
            </article>
            <br><br>
            <!-- Gráfico de Dona: Estado del Inventario -->
            <article class="md:col-span-1 bg-white shadow-xl rounded-md p-4 border border-gray-200">
                <h2 class="font-bold text-xl mb-4 text-gray-700 flex items-center gap-2">
                    <font-awesome-icon icon="fa-solid fa-chart-pie" class="text-purple-500"/>
                    Disponibilidad de Equipos
                </h2>
                
                <div v-if="isLoading" class="flex justify-center items-center h-[300px]">
                    <span class="loader border-purple-500"></span>
                </div>
                <v-chart v-else :option="chartByResume" style="height: 350px;" autoresize />
            </article>
        </section>
    </div>
</template>

<script setup>
import { FwbCard } from 'flowbite-vue';
import ReportMantService from '@/services/ReportMantService';
import { ref, onMounted, computed, watch } from 'vue';

// Importaciones de ECharts
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

use([
    CanvasRenderer,
    PieChart,
    BarChart,
    LineChart,
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent
])

// --- VARIABLES REACTIVAS ---
const tiempoPromedioReparacion = ref(0)
const tasaIncumplimiento = ref(0)
const totalAnalizados = ref(0)
const isLoading = ref(false)
const porcentaje = ref('')
const mantenimiento = ref([])
const vampiroData = ref([])
const desviacionData = ref([])

// -- Gráficos --
const chartByTCO = ref(null)
const chartByResume = ref(null)

watch (tasaIncumplimiento, (newValue) => {
    if(newValue <= 30){
        // Verde
        porcentaje.value = 'bg-linear-to-br from-[#00c54e] to-[#019239]'
    } else if (newValue <= 70) {
        // Amarillo
        porcentaje.value = 'bg-linear-to-br from-[#f59e0b] to-[#d97706]'
    } else {
        // Rojo
        porcentaje.value = 'bg-linear-to-br from-[#ef4444] to-[#dc2626]'
    }
});

onMounted(async () => {
    isLoading.value = true;
    try {
        // Pedir datos al Backend
        const [resumenData, analisisFinanciero] = await Promise.all([
            ReportMantService.getReporteEficiencia(),
            ReportMantService.getanalisisFinanciero()
        ]);

        // Llenar los KPIs (Tarjetas)
        tiempoPromedioReparacion.value = resumenData.resumen.tiempo_promedio_reparacion_dias || 0;
        tasaIncumplimiento.value = resumenData.resumen.tasa_de_incumplimiento || 0;
        totalAnalizados.value = resumenData.resumen.total_analizados || 0;

        // Datos Tabla
        mantenimiento.value = resumenData.detalle || [];
        console.log('Mantenimientos cargado: ', mantenimiento.value);
        
        // DEBUG: Ver estructura completa de analisisFinanciero
        console.log('analisisFinanciero completo:', analisisFinanciero);
        console.log('analisisFinanciero.resumen:', analisisFinanciero.resumen);
        console.log('analisisFinanciero.desviacion_presupuestaria:', analisisFinanciero.desviacion_presupuestaria);

        // --- CARGA DE GRÁFICOS ---
        await cargarGraficoBarras(analisisFinanciero.tco_por_activo.activos_vampiro)
        await cargarGraficoDona(analisisFinanciero.resumen.desviacion_presupuestaria)

    } catch(error) {
        console.log('Error al cargar el dashboard:', error);
    } finally {
        isLoading.value = false;
    }
});

const cargarGraficoBarras = async (data) => {
    try {
        // Asignar los datos recibidos al ref
        vampiroData.value = data || [];
        
        // Validar que haya datos
        if(!vampiroData.value || vampiroData.value.length === 0) {
            console.warn('No hay datos de activos vampiro para mostrar');
            return;
        }
        
        const etiquetas = vampiroData.value.map(item => item.nombre_item);
        const valores = vampiroData.value.map(item => item.tco_total);
        
        chartByTCO.value = {
            textStyle: { fontFamily: 'Inter, sans-serif' },
            tooltip: { 
                trigger: 'axis', 
                axisPointer: { type: 'shadow' },
                backgroundColor: 'rgba(255, 255, 255, 0.95)',
                textStyle: { color: '#121212' }
            },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: [{
                type: 'category',
                data: etiquetas,
                axisLine: { lineStyle: { color: '#6b7280' } },
                axisLabel: { color: '#e5e7eb' }
            }],
            yAxis: [{
                type: 'value',
                splitLine: { lineStyle: { color: '#374151' } },
                axisLabel: { color: '#e5e7eb', formatter: (val) => `$${val}` }
            }],
            series: [{
                name: 'Ingresos',
                type: 'bar',
                barWidth: '50%',
                data: valores,
                itemStyle: { color: '#34d399', borderRadius: [4, 4, 0, 0] }
            }]
        }
    } catch(e) {
        console.error("Error en grafico de barras", e);
    }
}

const cargarGraficoDona = async (data) => {
    try {
        // Asignar los datos recibidos al ref
        desviacionData.value = data || {};
        
        // Validar que haya datos
        if (!data || !data.total_mantenimientos_analizados) {
            console.warn('No hay datos de desviación presupuestaria para mostrar');
            return;
        }

        const total = data.total_mantenimientos_analizados;
        const malos = data.mantenimientos_subestimados || 0;
        const buenos = total - malos;

        // Mapa de colores para el gráfico
        const colorMap = {
            'Presupuestado': '#3b82f6',  // Azul
            'Ejecutado': '#10b981'        // Verde
        };

        const pieData = [
            {
                value: parseFloat(buenos),
                name: 'Presupuestado',
                itemStyle: { color: colorMap['Presupuestado'] }
            },
            {
                value: parseFloat(malos),
                name: 'Ejecutado',
                itemStyle: { color: colorMap['Ejecutado'] }
            }
        ];

        chartByResume.value = {
            textStyle: { fontFamily: 'Inter, sans-serif' },
            tooltip: {
                trigger: 'item',
                formatter: '<b>{b}</b><br/> ${c} ({d}%)'
            },
            legend: {
                bottom: '0%',
                left: 'center',
                icon: 'circle'
            },
            series: [
                {
                    name: 'Presupuesto',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: 20,
                            fontWeight: 'bold'
                        }
                    },
                    labelLine: {
                        show: false
                    },
                    data: pieData
                }
            ]
        };
    } catch (e) {
        console.error("Error en gráfico de dona", e);
    }
}


</script>

<style scoped>
.loader {
    width: 48px;
    height: 48px;
    border: 5px solid #FFF;
    border-bottom-color: #3B82F6; 
    border-radius: 50%;
    display: inline-block;
    box-sizing: border-box;
    animation: rotation 1s linear infinite;
}

@keyframes rotation {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
</style>
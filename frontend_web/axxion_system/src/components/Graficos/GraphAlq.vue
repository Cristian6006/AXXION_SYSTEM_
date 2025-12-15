<template>
    <div>
        <section class="flex flex-wrap justify-center gap-12">
            <fwb-card class="relative w-sm flex-1 min-w-[300px] bg-linear-to-br from-[#00c54e] to-[#019239] overflow-hidden">
                <div class="p-5 relative z-10">
                    <h5 class="mb-2 text-lg font-bold tracking-tight text-white">
                        Total Rentas Activas
                    </h5>
                    <p class="text-4xl font-bold text-white">
                        {{ totalRentasActivas }}
                    </p>
                </div>
                <span class="absolute -inset-y-6 -right-3 opacity-20 pr-3">
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        class="w-50 h-50"
                        viewBox="0 -960 960 960"
                        fill="#FFFFFF">
                        <path d="m122.43-227.56-56.56-56.57 308.39-306.83 167 167 219.52-220.65H638.87v-79.78H894.7v255.83h-78.79v-118.48L540.26-310.83l-167-167-250.83 250.27Z"/>
                    </svg>
                </span>
            </fwb-card>
            <fwb-card class="relative w-sm flex-1 min-w-[300px] bg-linear-to-br from-[#ff2626] to-[#920103]">
                <div class="p-5 relative z-10">
                    <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900  dark:text-white">
                        Rentas Atrasadas
                    </h5>
                    <p class="text-3xl font-extrabold text-white flex justify-between">
                        {{ alertasAtrasadas }}
                    </p>
                </div>
                <span class="absolute -inset-y-2 -right-8 opacity-20 pr-3">
                    <svg 
                        xmlns="http://www.w3.org/2000/svg" 
                        class="w-45 h-45"
                        viewBox="0 -960 960 960"
                        fill="#FFFFFF">
                        <path d="M446.78-273.78h72.44V-520h-72.44v246.22Zm33.25-311.74q17.64 0 29.55-11.64 11.9-11.64 11.9-28.84 0-18.69-11.92-30.58-11.92-11.9-29.54-11.9-18.26 0-29.88 11.9-11.62 11.89-11.62 30.3 0 17.55 11.94 29.16 11.93 11.6 29.57 11.6Zm.31 519.65q-86.2 0-161.5-32.39-75.3-32.4-131.74-88.84-56.44-56.44-88.84-131.73-32.39-75.3-32.39-161.59t32.39-161.67q32.4-75.37 88.75-131.34t131.69-88.62q75.34-32.65 161.67-32.65 86.34 0 161.78 32.61 75.45 32.6 131.37 88.5 55.93 55.89 88.55 131.45 32.63 75.56 32.63 161.87 0 86.29-32.65 161.58t-88.62 131.48q-55.97 56.18-131.42 88.76-75.46 32.58-161.67 32.58Zm.16-79.22q139.24 0 236.83-97.73 97.58-97.73 97.58-237.68 0-139.24-97.4-236.83-97.4-97.58-237.79-97.58-139.02 0-236.83 97.4-97.8 97.4-97.8 237.79 0 139.02 97.73 236.83 97.73 97.8 237.68 97.8ZM480-480Z"/>
                    </svg>
                </span>
            </fwb-card>
            <fwb-card class="relative w-sm flex-1 min-w-[300px] bg-linear-to-br from-[#336bbd] to-[#0046ad]">
                <div class="p-5 relative z-10">
                    <h5 class="mb-2 text-2xl font-bold tracking-tight text-gray-900  dark:text-white">
                        Dinero en Garantias
                    </h5>
                    <p class="text-3xl font-extrabold text-white flex justify-between">
                        ${{ dineroEnGarantias.toLocaleString() }}
                    </p>
                    <span class="absolute -inset-y-3.5 -right-11 opacity-20 pr-3">
                        <svg 
                            xmlns="http://www.w3.org/2000/svg" 
                            viewBox="0 -960 960 960" 
                            class="w-36 h-36"
                            fill="#FFFFFF">
                            <path d="M448.74-113.78v-85.13q-60.39-10.57-98.02-46.33-37.63-35.76-53.07-90.72l68.44-26.95q15.87 50.26 47.87 72.63 32 22.37 74.17 22.37 46.3 0 74.76-22.03 28.46-22.02 28.46-60.06 0-41.17-27.79-64.89-27.78-23.72-103.21-47.59-70.87-21.87-106.94-61.85-36.06-39.97-36.06-98.67 0-53.3 34.08-92.28 34.09-38.98 97.31-46.81v-84.13h66.22v84.13q46.13 7.26 80.32 32.89 34.2 25.63 51.76 67.59l-67.3 27.96q-14-30.87-37.5-45.94-23.5-15.06-60.37-15.06-43.17 0-67.35 18.45-24.17 18.46-24.17 51.07 0 34.61 26.61 56.98 26.61 22.37 109.87 47.93 69.13 22.13 103.32 62.76 34.2 40.63 34.2 103.46 0 64.7-37.85 104.61t-111.54 51.48v84.13h-66.22Z"/>
                        </svg>
                    </span>
                </div>
            </fwb-card>
        </section>
        <br><br>
        <section class="bg-white rounded-md shadow-md p-4">
            <h2 class="text-xl font-bold mb-4 text-red-600">
                Rentas Atrasadas
            </h2>
            <!-- Estado de Carga -->
            <div v-if="isLoading" class="flex justify-center p-4">
                <span class="loader"></span>
            </div>
            <!-- Tabla de Datos -->
            <div v-else class="overflow-x-auto overflow-y-auto">
                <table v-if="rentals.length > 0" class="w-full text-sm text-left text-gray-700 border">
                    <thead class="bg-gray-200 text-gray-900">
                        <tr>
                            <th class="p-2 border">Renta</th> 
                            <th class="p-2 border">Cliente</th>
                            <th class="p-2 border">Contacto</th>
                            <th class="p-2 border">Fecha Límite</th>
                            <th class="p-2 border">Días Retraso</th>
                            <th class="p-2 border">Monto Pendiente</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="r in rentals" :key="r.folio" class="hover:bg-red-50 border-b">
                            <td class="p-2 border font-bold">#{{ r.folio }}</td>
                            <td class="p-2 border">{{ r.cliente }}</td>
                            <td class="p-2 border">{{ r.contacto }}</td>
                            <td class="p-2 border text-red-600">{{ r.fecha_debio_entregar }}</td>
                            <td class="p-2 border font-bold text-center bg-red-100 text-red-800 rounded">
                                {{ r.dias_retraso }} días
                            </td>
                            <!-- Usamos toLocaleString para formato moneda -->
                            <td class="p-2 border font-mono">
                                ${{ Number(r.monto_pendiente).toLocaleString() }}
                            </td>
                        </tr>
                    </tbody>
                </table>
                <!-- Mensaje si no hay datos -->
                <div v-else class="text-center p-8 bg-gray-50 rounded border border-dashed border-gray-300">
                    <p class="text-green-600 font-bold text-lg">
                        <font-awesome-icon icon="fa-solid fa-check-circle" />
                        ¡Todo en orden!
                    </p>
                    <p class="text-gray-500">No hay rentas atrasadas por el momento.</p>
                </div>
            </div>
        </section>
        <br><br>
        
        <section>
            <!-- Gráfico de Barra: Ingresos mensuales -->
            <article class="bg-gray-800 shadow-xl/30 text-white rounded-md p-4 flex-1 min-w-[900px]">
                <h2 class="font-bold text-2xl mb-4">Ingresos por mes</h2>
                <v-chart :option="chartByIncome" style="height: 300px;" autoresize />
            </article>
            <br><br>
            <!-- Gráfico de Barra Horizontal: Top Equipos Más Rentados -->
            <article class="bg-gray-800 shadow-xl/30 text-white rounded-md p-4 border border-gray-200">
                <h2 class="font-bold text-2xl mb-4 text-white flex items-center gap-2">
                    Top Equipos Más Rentados
                </h2>
                <div v-if="isLoading" class="flex justify-center items-center h-[300px]">
                    <span class="loader border-blue-500"></span>
                </div>
                <v-chart v-else :option="chartByTopProducts" style="height: 350px;" autoresize />
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
                <v-chart v-else :option="chartInventoryStatus" style="height: 350px;" autoresize />
            </article>
            <br><br>

            <!-- Tabla ROI: Análisis Financiero -->
            <article class="md:col-span-2 bg-white shadow-xl rounded-md p-4 border border-gray-200 flex flex-col">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="font-bold text-xl text-gray-700 flex items-center gap-2">
                        <font-awesome-icon icon="fa-solid fa-magnifying-glass-dollar" class="text-blue-600"/>
                        Análisis de Rentabilidad (ROI)
                    </h2>
                    <!-- Pequeña leyenda -->
                    <div class="text-xs flex gap-3">
                        <span class="flex items-center gap-1"><div class="w-3 h-3 rounded-full bg-green-100 border border-green-500"></div> Rentable</span>
                        <span class="flex items-center gap-1"><div class="w-3 h-3 rounded-full bg-red-50 border border-red-500"></div> En Pérdida</span>
                    </div>
                </div>

                <div v-if="isLoading" class="flex justify-center p-10">
                    <span class="loader border-blue-600"></span>
                </div>

                <!-- Tabla -->
                <div v-else class="overflow-x-auto overflow-y-auto max-h-[350px] border rounded-lg">
                    <table class="w-full text-sm text-left text-gray-700">
                        <thead class="text-xs text-gray-700 uppercase bg-gray-50 sticky top-0 z-10 shadow-sm">
                            <tr>
                                <th class="px-4 py-3">Serie</th>
                                <th class="px-4 py-3 text-right">Costo Compra</th>
                                <th class="px-4 py-3 text-right">Ingreso Total</th>
                                <th class="px-4 py-3 text-center">ROI %</th>
                                <th class="px-4 py-3 text-center">Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in roiData" :key="item.serie" class="border-b hover:bg-gray-50 transition-colors">
                                <td class="px-4 py-3 font-medium text-gray-900 whitespace-nowrap">
                                    {{ item.serie }}
                                </td>
                                <td class="px-4 py-3 text-right font-mono text-gray-500">
                                    ${{ Number(item.costo).toLocaleString() }}
                                </td>
                                <td class="px-4 py-3 text-right font-mono font-bold text-blue-600">
                                    ${{ Number(item.ingreso_acumulado).toLocaleString() }}
                                </td>
                                <td class="px-4 py-3 text-center font-bold">
                                    <!-- Barra de progreso mini visual -->
                                    <div class="flex items-center gap-2 justify-end">
                                        <span :class="item.es_rentable ? 'text-green-600' : 'text-red-500'">
                                            {{ item.roi_porcentaje }}
                                        </span>
                                    </div>
                                </td>
                                <td class="px-4 py-3 text-center">
                                    <span v-if="item.es_rentable" class="bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded border border-green-400">
                                        Ganancia
                                    </span>
                                    <span v-else class="bg-red-100 text-red-800 text-xs font-medium px-2.5 py-0.5 rounded border border-red-400">
                                        Recuperando
                                    </span>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    
                    <!-- Estado Vacío -->
                    <div v-if="roiData.length === 0" class="text-center p-8 text-gray-400">
                        No hay datos de costos registrados para calcular el ROI.
                    </div>
                </div>
            </article>
        </section>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'; 
import { FwbCard } from 'flowbite-vue'
import ReporteReService from '@/services/ReporteReService';

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
const totalRentasActivas = ref(0)
const alertasAtrasadas = ref(0)
const dineroEnGarantias = ref(0)
const isLoading = ref(false) 
const rentals = ref([])
const roiData = ref([])

// Gráficos
const chartByIncome = ref(null)
const chartByTopProducts = ref(null)
const chartInventoryStatus = ref(null)

const colorMap = {
    'Disponible': '#019239',      // Green-500
    'Rentado': '#0046ad',         // Blue-500
    'EnMantenimiento': '#920103', // Orange-500
    'DeBaja': '#64748b'           // Slate-500
};

// --- CICLO DE VIDA ---
onMounted(async () => {
    isLoading.value = true;
    try {
        // --- CARGA DE KPIS Y RENTAS ---
        // Usamos Promise.all para cargar todo en paralelo )
        const [resumenData, atrasadasData] = await Promise.all([
            ReporteReService.getResumenGeneral(),
            ReporteReService.getObtenerRentasAtrasadas()
        ]);

        // Asignación de KPIs
        totalRentasActivas.value = resumenData.total_rentas_activas || 0;
        alertasAtrasadas.value = resumenData.alertas_atrasadas || 0;
        dineroEnGarantias.value = resumenData.dinero_en_garantias || 0;

        // Asignación de Tabla
        rentals.value = Array.isArray(atrasadasData) ? atrasadasData : atrasadasData.data || [];
        console.log('Inventario cargado:', rentals.value);

        // --- CARGA DE GRÁFICOS ---
        await cargarGraficoIngresos();
        await cargarGraficoTopProductos();
        await cargarGraficoInventario();
        await cargarAnalisisROI();

    } catch (error) {
        console.error('Error general cargando dashboard:', error);
    } finally {
        isLoading.value = false;
    }
})

// --- FUNCIONES AUXILIARES  ---

const cargarGraficoIngresos = async () => {
    try {
        const ingresosData = await ReporteReService.getIngresosPorMes();
        
        if(!ingresosData) return;

        const etiquetas = ingresosData.map(item => item.etiqueta);
        const valores = ingresosData.map(item => item.valor);

        chartByIncome.value = {
            textStyle: { fontFamily: 'Inter, sans-serif' },
            tooltip: { 
                trigger: 'axis', 
                axisPointer: { type: 'shadow' },
                backgroundColor: 'rgba(255, 255, 255, 0.95)',
                textStyle: { color: '#e5e7eb' }
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
        };
    } catch (e) {
        console.error("Error en gráfico ingresos", e);
    }
}

const cargarGraficoTopProductos = async () => {
    try {
        const topData = await ReporteReService.getTopEquiposRentados();
        
        if(!topData) return;

        const dataOrdenada = [...topData].reverse();
        const nombres = dataOrdenada.map(item => item.nombre);
        const cantidades = dataOrdenada.map(item => item.total_rentas);
        const ingresos = dataOrdenada.map(item => item.ingresos_generados);

        chartByTopProducts.value = {
            tooltip: {
                trigger: 'axis',
                axisPointer: { type: 'shadow' },
                formatter: (params) => {
                    const i = params[0].dataIndex;
                    return `<b>${nombres[i]}</b><br/> ${cantidades[i]} rentas<br/> $${Number(ingresos[i]).toLocaleString()}`;
                }
            },
            grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
            xAxis: { type: 'value', boundaryGap: [0, 0.01] },
            yAxis: { 
                type: 'category', 
                data: nombres,
                axisLabel: { width: 130, overflow: 'truncate' } 
            },
            series: [{
                name: 'Veces Rentado',
                type: 'bar',
                data: cantidades,
                itemStyle: {
                    color: {
                        type: 'linear', x: 0, y: 0, x2: 1, y2: 0,
                        colorStops: [{ offset: 0, color: '#3b82f6' }, { offset: 1, color: '#2563eb' }]
                    },
                    borderRadius: [0, 4, 4, 0]
                },
                label: { show: true, position: 'right', fontWeight: 'bold' }
            }]
        };
    } catch (e) {
        console.error("Error en gráfico top productos", e);
    }
}
const cargarGraficoInventario = async () => {
    try {
        const data = await ReporteReService.getEstadoInventario();

        if (!data) return;

        const pieData = data.map(item => ({
            value: item.cantidad,
            name: item.estado,
            itemStyle: {
                color: colorMap[item.estado] || '#cbd5e1' 
            }
        }));

        chartInventoryStatus.value = {
            tooltip: {
                trigger: 'item',
                formatter: '<b>{b}</b><br/> {c} equipos ({d}%)' // Muestra cantidad y porcentaje
            },
            legend: {
                bottom: '0%',
                left: 'center',
                icon: 'circle' // Iconos redondos en la leyenda
            },
            series: [
                {
                    name: 'Estado Inventario',
                    type: 'pie',
                    radius: ['40%', '70%'], // Esto crea el efecto "Dona" (Agujero en medio)
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: false, // Ocultamos etiquetas en el gráfico para que no se vea sucio
                        position: 'center'
                    },
                    emphasis: { // Efecto al pasar el mouse
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
        console.error("Error en gráfico inventario", e);
    }
}
const cargarAnalisisROI = async () => {
    try {
        const data = await ReporteReService.getRoiPorEquipo();
        
        roiData.value = data || [];
        
    } catch (e) {
        console.error("Error en tabla ROI", e);
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

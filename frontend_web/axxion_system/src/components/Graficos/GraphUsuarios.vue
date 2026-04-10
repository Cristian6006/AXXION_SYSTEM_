<template>
    <div class="w-full">
        <div v-if="loading" class="flex flex-col items-center justify-center py-20">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>
            <p class="text-gray-500 font-medium">Preparando reporte...</p>
        </div>
        
        <!-- Estado de Error -->
        <div v-else-if="error" class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 rounded mb-6">
            <p class="font-bold">Error de Conexión</p>
            <p>{{ error }}</p>
        </div>
        
        <!-- Contenido del Reporte -->
        <div v-else-if="reportData" class="flex flex-col gap-6">
            <p class="text-sm text-gray-500 text-right">
                Última actualización: {{ reportData.resumen_general.fecha_reporte }}
            </p>
            
            <!-- 1. Tarjetas KPI -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <!-- KPI Usuarios -->
                <fwb-card class="p-6 bg-white shadow-md w-full hover:shadow-lg transition">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-semibold text-gray-500 uppercase">Usuarios Totales</p>
                            <h3 class="text-3xl font-bold text-white">{{ reportData.resumen_general.usuarios.total }}</h3>
                            <p class="text-sm mt-2 text-green-600">
                                <i class="fa-solid fa-user-check mr-1"></i> {{ reportData.resumen_general.usuarios.activos }} Activos
                                <span class="text-gray-400 mx-1">|</span>
                                <span class="text-red-500"><i class="fa-solid fa-user-xmark mr-1"></i> {{ reportData.resumen_general.usuarios.inactivos }} Inactivos</span>
                            </p>
                        </div>
                        <div class="p-4 bg-blue-100 rounded-full text-blue-600">
                            <i class="fa-solid fa-users fa-2x"></i>
                        </div>
                    </div>
                </fwb-card>
                
                <!-- KPI Departamentos -->
                <fwb-card class="p-6 bg-white shadow-md w-full hover:shadow-lg transition">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-semibold text-gray-500 uppercase">Departamentos</p>
                            <h3 class="text-3xl font-bold text-white">{{ reportData.resumen_general.departamentos.total }}</h3>
                            <p class="text-sm mt-2 text-gray-600">
                                <i class="fa-solid fa-fire text-orange-500 mr-1"></i> Mayor demanda: 
                                <span class="font-semibold">{{ reportData.resumen_general.departamentos.mayor_demanda }}</span>
                            </p>
                        </div>
                        <div class="p-4 bg-purple-100 rounded-full text-purple-600">
                            <i class="fa-solid fa-building fa-2x"></i>
                        </div>
                    </div>
                </fwb-card>
                
                <!-- KPI Seguridad -->
                <fwb-card class="p-6 bg-white shadow-md w-full hover:shadow-lg transition">
                    <div class="flex items-center justify-between">
                        <div>
                        <p class="text-sm font-semibold text-gray-500 uppercase">Riesgo de Seguridad</p>
                        <h3 :class="['text-3xl font-bold', riskColorText]">{{ reportData.resumen_general.seguridad.nivel_riesgo }}</h3>
                        <p class="text-sm mt-2 text-gray-600">
                            <i class="fa-solid fa-triangle-exclamation text-yellow-500 mr-1"></i> 
                            {{ reportData.resumen_general.seguridad.administradores_inactivos }} Admins inactivos
                        </p>
                        </div>
                        <div :class="['p-4 rounded-full', riskColorBg]">
                            <i class="fa-solid fa-shield-halved fa-2x"></i>
                        </div>
                    </div>
                </fwb-card>
            </div>
            
            <!-- 2. Gráficos Visuales -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h4 class="text-lg font-bold text-gray-700 mb-4 border-b pb-2">Distribución por Departamento</h4>
                    <!-- Wrapper importante para dar altura a eCharts -->
                    <div style="height: 350px;">
                        <v-chart :option="deptPieOptions" autoresize />
                    </div>
                </div>
                
                <div class="bg-white p-6 rounded-lg shadow-md">
                    <h4 class="text-lg font-bold text-gray-700 mb-4 border-b pb-2">Salud de Roles (Activos vs Inactivos)</h4>
                <div style="height: 350px;">
                    <v-chart :option="rolesBarOptions" autoresize />
                </div>
            </div>
        </div>
        
        <!-- 3. Tabla de Detalles Críticos -->
        <div v-if="adminAlerts.length > 0" class="bg-white p-6 rounded-lg shadow-md border-l-4 border-red-500 mt-4">
            <div class="flex items-center mb-4 text-red-600">
                <i class="fa-solid fa-circle-exclamation fa-lg mr-2"></i>
                <h4 class="text-lg font-bold">Alerta: Administradores Inactivos</h4>
            </div>
            <p class="text-sm text-gray-600 mb-4">
                Los siguientes usuarios poseen el rol de Administrador pero se encuentran inactivos en el sistema. Se sugiere revisión.
            </p>
            <div class="overflow-x-auto rounded-lg">
                <table class="w-full text-sm text-left text-gray-500">
                    <thead class="text-xs text-red-700 uppercase bg-red-50">
                        <tr>
                            <th scope="col" class="px-6 py-3">ID</th>
                            <th scope="col" class="px-6 py-3">Usuario</th>
                            <th scope="col" class="px-6 py-3">Nombre Completo</th>
                            <th scope="col" class="px-6 py-3">Email</th>
                            <th scope="col" class="px-6 py-3">Departamento</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="admin in adminAlerts" :key="admin.id" class="bg-white border-b hover:bg-gray-50">
                            <td class="px-6 py-4 font-medium text-gray-900">{{ admin.id }}</td>
                            <td class="px-6 py-4">{{ admin.nombre_usuario }}</td>
                            <td class="px-6 py-4">{{ admin.nombre }} {{ admin.apellido1 }}</td>
                            <td class="px-6 py-4">{{ admin.email }}</td>
                            <td class="px-6 py-4"><span class="bg-red-100 text-red-800 text-xs font-medium px-2.5 py-0.5 rounded">{{ admin.departamento }}</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</template>

<script setup>
    import { ref, computed, onMounted } from 'vue';
    import { FwbCard } from 'flowbite-vue';
    import { getReporteUsuario } from '@/services/ReportUserService'; 
    import { use } from 'echarts';
    import { CanvasRenderer } from 'echarts/renderers';
    import { PieChart, BarChart } from 'echarts/charts';
    import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components';
    import VChart from 'vue-echarts';
    
    use([CanvasRenderer, PieChart, BarChart, TitleComponent, TooltipComponent, LegendComponent, GridComponent]);
    
    // Estados Reactivos
    const loading = ref(true);
    const error = ref(null);
    const reportData = ref(null);
    
    const deptPieOptions = ref({});
    const rolesBarOptions = ref({});
    
    const adminAlerts = computed(() => {
        return reportData.value?.alertas_seguridad?.administradores_inactivos || [];
    });
    
    const riskColorText = computed(() => {
        const level = reportData.value?.resumen_general?.seguridad?.nivel_riesgo;
        if (level === 'BAJO') return 'text-green-600';
        if (level === 'MEDIO') return 'text-yellow-500';
        return 'text-red-600'
    });
    
    const riskColorBg = computed(() => {
        const level = reportData.value?.resumen_general?.seguridad?.nivel_riesgo;
        if (level === 'BAJO') return 'bg-green-100 text-green-600';
        if (level === 'MEDIO') return 'bg-yellow-100 text-yellow-600';
        return 'bg-red-100 text-red-600';
    });
    
    onMounted(async () => {
        try {
            loading.value = true;
            error.value = null;
            const response = await getReporteUsuario();
            reportData.value = response;
            
            construirGraficoDepartamentos(response.distribucion_departamentos);
            construirGraficoRoles(response.distribucion_roles);
        } catch (err) {
            error.value = "No se pudo cargar el reporte. Verifica la conexion del servidor."
        } finally {
            loading.value = false;
        }
    });
    
    // Funciones de ECharts
    const construirGraficoDepartamentos = (data) => {
        const formatedData = data.map(item => ({
            name: item.departamento,
            value: item.cantidad_usuarios
        }));
        
        deptPieOptions.value = {
            tooltip: { trigger: 'item', formatter: '{b}: {c} usuarios ({d}%)' },
            legend: { bottom: '0%', left: 'center' },
            color:['#3b82f6', '#8b5cf6', '#10b981', '#f59e0b', '#ef4444'],
            series: [
                {
                    name: 'Departamentos',
                    type: 'pie',
                    radius:['40%', '70%'], 
                    avoidLabelOverlap: false,
                    itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
                    label: { show: false },
                    data: formatedData
                }
            ]
        };
    };
    
    const construirGraficoRoles = (data) => {
        const labelsX = data.map(item => item.rol_nombre);
        const dataActivos = data.map(item => item.activos);
        const dataInactivos = data.map(item => item.inactivos);
        
        rolesBarOptions.value = {
            tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
            legend: { bottom: '0%', left: 'center' },
            grid: { left: '3%', right: '4%', bottom: '15%', top: '5%', containLabel: true },
            xAxis:[ { type: 'category', data: labelsX } ],
            yAxis: [ { type: 'value' } ],
            series:[
                {
                    name: 'Activos',
                    type: 'bar',
                    stack: 'total',
                    emphasis: { focus: 'series' },
                    itemStyle: { color: '#10b981' }, 
                    data: dataActivos
                },
                {
                    name: 'Inactivos',
                    type: 'bar',
                    stack: 'total', 
                    emphasis: { focus: 'series' },
                    itemStyle: { color: '#ef4444' },  
                    data: dataInactivos
                }
            ]
        };
    };
</script>
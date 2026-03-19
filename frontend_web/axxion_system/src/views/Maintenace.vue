<template>
  <div class="maintenance-view">
    <div class="app flex">
      <SideBar />
      <RouterView />

    <main class="container h-screen">
      <div class="rounded-lg flex-col">
        <headerP />

        <!-- Main Content -->
        <div class="p-4 sm:p-6 lg:p-8 bg-gray-50 dark:bg-gray-800 rounded-lg h-full">
          <div class="mb-8">
            <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">
              <font-awesome-icon icon="fa-solid fa-tools" class="mr-3 text-yellow-500" />
              Gestión de Mantenimiento
            </h1>
            <p class="text-gray-600 dark:text-gray-400">
              Planifica, rastrea y gestiona todas las tareas de mantenimiento de equipos.
            </p>
          </div>

          <!-- Notifications -->
          <div v-if="notifications.length > 0" class="mb-6 space-y-2">
            <div
              v-for="notification in notifications"
              :key="notification.id"
              :class="[
                'p-4 rounded-lg flex justify-between items-center',
                notification.type === 'success'
                  ? 'bg-green-100 border border-green-500 text-green-700 dark:bg-green-800 dark:text-green-300'
                  : 'bg-red-100 border border-red-500 text-red-700 dark:bg-red-800 dark:text-red-300'
              ]"
            >
              <span>{{ notification.message }}</span>
              <button
                @click="removeNotification(notification.id)"
                class="ml-4 text-gray-400 hover:text-gray-600 dark:text-gray-300 dark:hover:text-gray-100"
              >
                <font-awesome-icon icon="fa-solid fa-times" />
              </button>
            </div>
          </div>

          <!-- Action Controls -->
          <!-- <div class="flex flex-wrap gap-4 mb-6">
            <fwb-button gradient="green-blue" size="lg" @click="showAddModal">
              <font-awesome-icon icon="fa-solid fa-plus" class="mr-2" />
              Programar Tarea
            </fwb-button>
          </div> -->

          <!-- Filters and Search -->
          <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 mb-6">
            <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Buscar Tarea
                </label>
                <fwb-input v-model="searchQuery" placeholder="Buscar por equipo, tarea..." />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Estado
                </label>
                <select 
                  v-model="statusFilter"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                >
                  <option value="">Todos</option>
                  <option v-for="status in statusOptions" :key="status.value" :value="status.value">
                    {{ status.name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Técnico Asignado
                </label>
                <select 
                  v-model="technicianFilter"
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                >
                  <option value="">Todos</option>
                  <option v-for="technician in technicianOptions" :key="technician.value" :value="technician.value">
                    {{ technician.name }}
                  </option>
                </select>
              </div>
              <div class="flex items-end">
                <fwb-button gradient="red-yellow" size="lg" @click="clearFilters" class="w-full">
                  <font-awesome-icon icon="fa-solid fa-times" class="mr-2" />
                  Limpiar
                </fwb-button>
              </div>
            </div>
          </div>

          <!-- Maintenance Table -->
          <div class="overflow-x-auto bg-white dark:bg-gray-800 rounded-lg shadow">
            <fwb-table>
              <fwb-table-head>
                <fwb-table-head-cell>Equipo</fwb-table-head-cell>
                <fwb-table-head-cell>Tarea</fwb-table-head-cell>
                <fwb-table-head-cell>Fecha Programada</fwb-table-head-cell>
                <fwb-table-head-cell>Técnico</fwb-table-head-cell>
                <fwb-table-head-cell>Estado</fwb-table-head-cell>
                <fwb-table-head-cell>
                  <span class="sr-only">Acciones</span>
                </fwb-table-head-cell>
              </fwb-table-head>
              <fwb-table-body>
                <fwb-table-row v-for="task in filteredTasks" :key="task.id">
                   <fwb-table-cell>
                     <div>{{ getEquipmentName(task.inventario_item_id) }}</div>
                     <div class="text-xs text-gray-500">ID: {{ task.inventario_item_id }}</div>
                   </fwb-table-cell>
                   <fwb-table-cell>{{ task.descripcion_problema }}</fwb-table-cell>
                   <fwb-table-cell>{{ formatDate(task.fecha_inicio) }}</fwb-table-cell>
                   <fwb-table-cell>{{ getTechnicianName(task.responsable) }}</fwb-table-cell>
                   <fwb-table-cell>
                     <span :class="getStatusClass(task.estado_mantenimiento)">
                       {{ getStatusDisplayName(task.estado_mantenimiento) }}
                     </span>
                   </fwb-table-cell>
                  <fwb-table-cell class="flex gap-2">
                    <fwb-button size="xs" gradient="blue" @click="editTask(task)">
                      Editar
                    </fwb-button>
                    <fwb-button size="xs" gradient="red" @click="showDeleteModal(task)">
                      Eliminar
                    </fwb-button>
                  </fwb-table-cell>
                </fwb-table-row>
                <fwb-table-row v-if="filteredTasks.length === 0">
                   <fwb-table-cell colspan="6" class="text-center py-8 text-gray-500">
                     <div v-if="isLoading" class="flex items-center justify-center flex-col">
                       <span class="loader mb-2"></span>
                       Cargando mantenimientos...
                     </div>
                     <div v-else>
                       No hay mantenimientos disponibles
                       <div class="text-xs mt-2 text-gray-400">
                         Total en store: {{ maintenances?.length || 0 }}
                         <br>Store maintenances: {{ JSON.stringify(maintenances) }}
                       </div>
                     </div>
                   </fwb-table-cell>
                 </fwb-table-row>
              </fwb-table-body>
            </fwb-table>
          </div>
        </div>
      </div>
    </main>

    <!-- Add/Edit Modal -->
    <fwb-modal v-if="isShowModal" @close="closeModal" size="2xl">
      <template #header>
        <h3 class="text-xl font-semibold text-gray-900 dark:text-white">
          {{ isEditMode ? 'Editar Tarea' : 'Programar Nueva Tarea' }}
        </h3>
      </template>
      <template #body>
        <form @submit.prevent="saveTask" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Equipo *
            </label>
            <fwb-select v-model="taskForm.inventario_item_id" required :class="{ 'border-red-500': formErrors.inventario_item_id }" :disabled="isEditMode">
              <option value="">Seleccionar equipo</option>
              <option v-for="item in inventoryOptions" :key="item.value" :value="item.value">
                {{ item.name }}
              </option>
            </fwb-select>
            <p v-if="formErrors.inventario_item_id" class="mt-1 text-sm text-red-600 dark:text-red-400">
              {{ formErrors.inventario_item_id }}
            </p>
          </div>
          <div>
            <fwb-textarea
              v-model="taskForm.descripcion_problema"
              label="Descripción del Problema"
              placeholder="Ej: Limpieza interna y cambio de pasta térmica"
              required
              :class="{ 'border-red-500': formErrors.descripcion_problema }"
            />
            <p v-if="formErrors.descripcion_problema" class="mt-1 text-sm text-red-600 dark:text-red-400">
              {{ formErrors.descripcion_problema }}
            </p>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <fwb-input
                    v-model="taskForm.fecha_inicio"
                    label="Fecha de Inicio"
                    type="date"
                    required
                    :disabled="isEditMode"
                />
            </div>
            <div>
                <fwb-input
                    v-model="taskForm.fecha_fin_prevista"
                    label="Fecha de Fin Prevista"
                    type="date"
                />
            </div>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Responsable *
                </label>
                <select 
                  v-model="taskForm.responsable" 
                  required
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                >
                  <option value="">Seleccionar responsable</option>
                  <option v-for="technician in technicianOptions" :key="technician.value" :value="technician.value">
                    {{ technician.name }}
                  </option>
                </select>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Estado *
                </label>
                <select 
                  v-model="taskForm.estado_mantenimiento" 
                  required
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                >
                  <option v-for="status in statusOptions" :key="status.value" :value="status.value">
                    {{ status.name }}
                  </option>
                </select>
            </div>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Tipo de Mantenimiento *
                </label>
                <select 
                  v-model="taskForm.tipo_mantenimiento" 
                  required
                  class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                >
                  <option v-for="type in typeOptions" :key="type.value" :value="type.value">
                    {{ type.name }}
                  </option>
                </select>
            </div>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <fwb-input
                    v-model.number="taskForm.costo_estimado"
                    label="Costo Estimado"
                    type="number"
                    step="0.01"
                    min="0"
                />
            </div>
            <div>
                <fwb-input
                    v-model.number="taskForm.costo_real"
                    label="Costo Real"
                    type="number"
                    step="0.01"
                    min="0"
                />
            </div>
          </div>
          <div>
            <fwb-textarea
              v-model="taskForm.descripcion_trabajo_realizado"
              label="Descripción del Trabajo Realizado"
              placeholder="Descripción detallada del trabajo realizado"
            />
          </div>
        </form>
      </template>
      <template #footer>
        <div class="flex justify-end gap-2">
          <fwb-button @click="closeModal" color="alternative">
            Cancelar
          </fwb-button>
          <fwb-button @click="saveTask" color="green" :disabled="isSaving">
            <font-awesome-icon v-if="isSaving" icon="fa-solid fa-spinner" class="mr-2 animate-spin" />
            {{ isEditMode ? 'Guardar Cambios' : 'Guardar' }}
          </fwb-button>
        </div>
      </template>
    </fwb-modal>

    <!-- Delete Confirmation Modal -->
    <fwb-modal v-if="isShowDeleteModal" @close="closeDeleteModal">
      <template #body>
        <div class="text-center p-4">
            <font-awesome-icon icon="fa-solid fa-triangle-exclamation" class="text-5xl text-red-500 mb-4"/>
            <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">
                ¿Está seguro de que desea eliminar esta tarea?
            </h3>
            <div class="flex justify-center gap-4">
                <fwb-button @click="deleteTask" color="red" :disabled="isDeleting">
                  <font-awesome-icon v-if="isDeleting" icon="fa-solid fa-spinner" class="mr-2 animate-spin" />
                  Sí, estoy seguro
                </fwb-button>
                <fwb-button @click="closeDeleteModal" color="alternative">No, cancelar</fwb-button>
            </div>
        </div>
      </template>
    </fwb-modal>

    </div>
    
    <Footer />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import Footer from '@/components/Footer.vue';
import { useMaintenanceStore } from '@/stores/maintenance.js';
import { useUserStore } from '@/stores/user.js';
import { useInventoryStore } from '@/stores/inventory.js';
import {
  FwbButton,
  FwbTable,
  FwbTableBody,
  FwbTableCell,
  FwbTableHead,
  FwbTableHeadCell,
  FwbTableRow,
  FwbModal,
  FwbInput,
  FwbSelect,
  FwbTextarea
} from 'flowbite-vue';


// Stores
const maintenanceStore = useMaintenanceStore();
const userStore = useUserStore();
const inventoryStore = useInventoryStore();

/**
 * Vista Maintenance.
 * 
 * Gestión centralizada de tareas de mantenimiento.
 * Funcionalidades:
 * - Tablero de control de mantenimientos (programados, en proceso, completados).
 * - Filtrado por estado, técnico responsable y búsqueda general.
 * - CRUD de tareas de mantenimiento (Crear, Editar, Eliminar).
 * - Asignación de técnicos y equipos a tareas.
 * - Registro de costos estimados vs reales y descripción del trabajo realizado.
 * - Detección automática de mantenimientos desde otras vistas (ej. al finalizar renta).
 */

// Estado reactivo de los stores usando storeToRefs para mantener reactividad
const { maintenances, isLoading } = storeToRefs(maintenanceStore);
const { users } = storeToRefs(userStore);

// Form and Modal State
const isShowModal = ref(false);
const isShowDeleteModal = ref(false);
const isEditMode = ref(false);
const selectedTask = ref(null);
const taskForm = ref({});

// Loading states
const isSaving = ref(false);
const isDeleting = ref(false);

// Form validation
const formErrors = ref({});

const defaultFormState = {
    inventario_item_id: '',
    descripcion_problema: '',
    fecha_inicio: '',
    fecha_fin_prevista: '',
    responsable: '',
    estado_mantenimiento: 'PROGRAMADO',
    tipo_mantenimiento: 'PREVENTIVO',
    costo_estimado: 0,
    costo_real: 0,
    descripcion_trabajo_realizado: ''
};

// Options for Selects - Computed para reactividad
// Genera opciones para los selectores de técnicos (filtrados por rol), estados e inventario.
const technicianOptions = computed(() => {
  return users.value
    .filter(user => user.rol && (user.rol.nombre === 'Técnico' || user.rol.nombre === 'Administrador'))
    .map(user => ({ 
      value: user.id, 
      name: `${user.nombre} ${user.apellido}` 
    }));
});

const statusOptions = computed(() => maintenanceStore.getAvailableStatuses());
const typeOptions = computed(() => maintenanceStore.getAvailableTypes());

const inventoryOptions = computed(() => {
  // Obtener opciones de inventario del store de inventario
  return inventoryStore.productList.map(item => ({
    value: item.id,
    name: `${item.numero_serie} - ${item.estado}`
  }));
});

// Notification state
const notifications = ref([]);


// Filter State
const searchQuery = ref('');
const statusFilter = ref('');
const technicianFilter = ref('');

// Filtra las tareas de mantenimiento según los criterios seleccionados por el usuario.
const filteredTasks = computed(() => {
  console.log('filteredTasks computed - maintenances:', maintenances.value);
  console.log('filteredTasks computed - maintenances length:', maintenances.value?.length);
  
  if (!maintenances.value || maintenances.value.length === 0) {
    console.log('filteredTasks: No maintenances available');
    return [];
  }

  const filtered = maintenanceStore.filterMaintenances({
    search: searchQuery.value,
    estado: statusFilter.value,
    responsable: technicianFilter.value
  });
  
  console.log('filteredTasks: Filtered result:', filtered);
  return filtered;
});

// Helper Functions
function getEquipmentName(inventarioItemId) {
  console.log('getEquipmentName called with:', inventarioItemId);
  console.log('Available maintenances:', maintenances.value);

  if (!maintenances.value || !Array.isArray(maintenances.value)) {
    console.log('Maintenances not loaded or not an array');
    return 'Datos no cargados';
  }

  // Find the maintenance record with the matching inventory item ID
  const maintenance = maintenances.value.find(m => m.inventario_item_id === inventarioItemId);

  if (maintenance && maintenance.inventario_item) {
    const result = `${maintenance.inventario_item.numero_serie} - ${maintenance.inventario_item.estado_item}`;
    console.log('getEquipmentName result:', result);
    return result;
  }

  console.log('Equipment not found for ID:', inventarioItemId);
  return 'Equipo no encontrado';
}

function formatDate(dateString) {
  if (!dateString) return 'Sin fecha';
  const date = new Date(dateString);
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

function getTechnicianName(responsableId) {
  if (!responsableId) return 'Sin asignar';
  if (!users.value || !Array.isArray(users.value)) return 'Usuarios no cargados';
  const technician = users.value.find(user => user.id === responsableId);
  return technician ? `${technician.nombre} ${technician.apellido}` : 'Técnico no encontrado';
}

function getStatusClass(status) {
  const statusClasses = {
    'PROGRAMADO': 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300',
    'EN_PROCESO': 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300',
    'COMPLETADO': 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300',
    'CANCELADO': 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300',
    'PAUSADO': 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300'
  };
  return statusClasses[status] || 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-300';
}

function getStatusDisplayName(status) {
  const statusNames = {
    'PROGRAMADO': 'Programado',
    'EN_PROCESO': 'En Proceso',
    'COMPLETADO': 'Completado', // Coincide con trigger
    'Finalizado': 'Completado', // Alias para compatibilidad
    'CANCELADO': 'Cancelado',
    'PAUSADO': 'Pausado'
  };
  return statusNames[status] || status || 'Sin estado';
}

function clearFilters() {
  searchQuery.value = '';
  statusFilter.value = '';
  technicianFilter.value = '';
}

// Notification methods
function addNotification(message, type = 'success') {
  const notification = {
    id: Date.now(),
    message,
    type,
    timestamp: new Date()
  };

  notifications.value.push(notification);

  // Auto-remove after 5 seconds
  setTimeout(() => {
    removeNotification(notification.id);
  }, 5000);
}

function removeNotification(id) {
  const index = notifications.value.findIndex(n => n.id === id);
  if (index > -1) {
    notifications.value.splice(index, 1);
  }
}

function validateForm() {
  formErrors.value = {};
  let isValid = true;

  if (!taskForm.value.inventario_item_id) {
    formErrors.value.inventario_item_id = 'Debe seleccionar un equipo';
    isValid = false;
  }

  if (!taskForm.value.descripcion_problema?.trim()) {
    formErrors.value.descripcion_problema = 'La descripción del problema es requerida';
    isValid = false;
  }

  if (!taskForm.value.fecha_inicio) {
    formErrors.value.fecha_inicio = 'La fecha de inicio es requerida';
    isValid = false;
  }

  if (!taskForm.value.responsable) {
    formErrors.value.responsable = 'Debe seleccionar un responsable';
    isValid = false;
  }

  if (!taskForm.value.estado_mantenimiento) {
    formErrors.value.estado_mantenimiento = 'Debe seleccionar un estado';
    isValid = false;
  }

  if (!taskForm.value.tipo_mantenimiento) {
    formErrors.value.tipo_mantenimiento = 'Debe seleccionar un tipo de mantenimiento';
    isValid = false;
  }

  return isValid;
}

// Lifecycle
// Carga inicial de datos y manejo de apertura automática de modal si hay parámetros en la URL.
onMounted(async () => {
  try {
    console.log('Starting to load maintenance data...');
    
    // Cargar datos secuencialmente
    await userStore.fetchUsers();
    await inventoryStore.fetchProducts();
    await maintenanceStore.fetchMaintenances();
    
    console.log('All data loaded successfully');
    addNotification('Datos cargados exitosamente', 'success');

    // Verificar si hay parámetros en la URL para abrir mantenimiento automáticamente
    const urlParams = new URLSearchParams(window.location.search);
    const autoOpenItems = urlParams.get('auto_open_items');
    
    if (autoOpenItems) {
        const itemIds = autoOpenItems.split(',');
        console.log('Auto-opening maintenance for items:', itemIds);
        
        // Esperar un poco más para asegurar que los datos se cargaron
        await new Promise(resolve => setTimeout(resolve, 500));
        
        console.log('Maintenances disponibles:', maintenances.value);
        
        // Buscar el primer mantenimiento programado para estos items (el más reciente)
        const targetMaintenance = maintenances.value.find(m => 
            itemIds.includes(String(m.inventario_item_id)) && 
            (m.estado_mantenimiento === 'PROGRAMADO' || m.estado_mantenimiento === 'Programado')
        );

        if (targetMaintenance) {
            console.log('Found target maintenance to auto-open:', targetMaintenance);
            console.log('Inventario item en mantenimiento:', targetMaintenance.inventario_item);
            
            // Esperar un tick para que las opciones se rendericen
            await new Promise(resolve => setTimeout(resolve, 100));
            
            editTask(targetMaintenance);
            addNotification('Mantenimiento automático detectado. Por favor complete los detalles.', 'success');
        } else {
            console.log('No matching maintenance found for auto-open items');
            console.log('Available maintenances:', maintenances.value.map(m => ({
                id: m.id,
                inventario_item_id: m.inventario_item_id,
                estado: m.estado_mantenimiento
            })));
        }
    }

  } catch (error) {
    console.error('Error loading initial data:', error);
    addNotification('Error al cargar los datos iniciales: ' + error.message, 'error');
  }
});

// Modal Functions
function showAddModal() {
  // Ya no se usa manualmente, pero por si acaso
  isEditMode.value = false;
  taskForm.value = { ...defaultFormState };
  selectedTask.value = null;
  isShowModal.value = true;
}

function editTask(task) {
  console.log('editTask called with task:', task);
  
  isEditMode.value = true;
  taskForm.value = { ...task };
  
  console.log('taskForm after copy:', taskForm.value);
  
  // Asegurar fechas en formato correcto para inputs
  if(taskForm.value.fecha_inicio) {
    taskForm.value.fecha_inicio = taskForm.value.fecha_inicio.split('T')[0];
  }
  if(taskForm.value.fecha_fin_prevista) {
    taskForm.value.fecha_fin_prevista = taskForm.value.fecha_fin_prevista.split('T')[0];
  }
  
  // Asegurar que estado_mantenimiento tenga un valor
  if (!taskForm.value.estado_mantenimiento) {
    taskForm.value.estado_mantenimiento = 'PROGRAMADO';
  }
  
  // Asegurar que tipo_mantenimiento tenga un valor
  if (!taskForm.value.tipo_mantenimiento) {
    taskForm.value.tipo_mantenimiento = 'CORRECTIVO';
  }
  
  // Asegurar que responsable tenga un valor
  if (!taskForm.value.responsable) {
    taskForm.value.responsable = 'Sistema';
  }
  
  console.log('taskForm final:', taskForm.value);
  console.log('Available users:', users.value);
  console.log('Available statuses:', statusOptions.value);
  console.log('Available types:', typeOptions.value);
  
  selectedTask.value = task;
  isShowModal.value = true;
}

function closeModal() {
  isShowModal.value = false;
  // Limpiar query params si existen para evitar que se reabra al recargar
  if (window.history.pushState) {
      const newurl = window.location.protocol + "//" + window.location.host + window.location.pathname;
      window.history.pushState({path:newurl},'',newurl);
  }
}

// Guarda (crea o actualiza) una tarea de mantenimiento.
async function saveTask() {
    if (isSaving.value) return;

    // Validate form
    if (!validateForm()) {
        addNotification('Por favor complete todos los campos requeridos', 'error');
        return;
    }

    try {
        isSaving.value = true;
        
        // Asegurar que los campos enum estén en mayúsculas para la API
        const payload = { ...taskForm.value };
        if (payload.tipo_mantenimiento) payload.tipo_mantenimiento = payload.tipo_mantenimiento.toUpperCase();
        if (payload.estado_mantenimiento) payload.estado_mantenimiento = payload.estado_mantenimiento.toUpperCase();
        
        if (isEditMode.value && selectedTask.value) {
            // Update existing task
            await maintenanceStore.updateMaintenance(selectedTask.value.id, payload);
            addNotification('Mantenimiento actualizado exitosamente', 'success');
        } else {
            // Add new task
            await maintenanceStore.createMaintenance(payload);
            addNotification('Mantenimiento creado exitosamente', 'success');
        }
        closeModal();
    } catch (error) {
        console.error('Error al guardar mantenimiento:', error);
        addNotification('Error al guardar mantenimiento: ' + (error.message || 'Error desconocido'), 'error');
    } finally {
        isSaving.value = false;
    }
}

function showDeleteModal(task) {
    selectedTask.value = task;
    isShowDeleteModal.value = true;
}

function closeDeleteModal() {
    isShowDeleteModal.value = false;
    selectedTask.value = null;
}

async function deleteTask() {
    if (isDeleting.value) return;

    try {
        isDeleting.value = true;
        if(selectedTask.value) {
            await maintenanceStore.deleteMaintenance(selectedTask.value.id);
            addNotification('Mantenimiento eliminado exitosamente', 'success');
        }
        closeDeleteModal();
    } catch (error) {
        console.error('Error al eliminar mantenimiento:', error);
        addNotification('Error al eliminar mantenimiento: ' + (error.message || 'Error desconocido'), 'error');
        closeDeleteModal();
    } finally {
        isDeleting.value = false;
    }
}
</script>

<style scoped>
/* Add any specific styles for this view here */
</style>

<template>
  <form @submit.prevent="handleSubmit" class="space-y-6">
    <!-- Alert Component -->
    <div v-if="alertState.show" class="mb-4">
      <fwb-alert 
        :type="alertState.type" 
        closable 
        @close="alertState.show = false"
        :icon="true"
      >
        {{ alertState.message }}
      </fwb-alert>
    </div>

    <!-- Información Básica -->
    <div class="bg-gray-50 dark:bg-gray-700 p-6 rounded-lg">
      <h3 class="text-lg font-semibold text-gray-900 dark:text-white mb-4">
        <font-awesome-icon icon="fa-solid fa-info-circle" class="mr-2 text-blue-500"/>
        Información Básica
      </h3>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Nombre del Equipo *
          </label>
          <fwb-input 
            v-model="form.name"
            placeholder="Ej: Laptop Dell Inspiron 15"
            :error="errors.name"
            required
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Marca *
          </label>
          <fwb-input 
            v-model="form.brand"
            placeholder="Ej: Dell, HP, Apple"
            :error="errors.brand"
            required
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Modelo *
          </label>
          <fwb-input 
            v-model="form.model"
            placeholder="Ej: Inspiron 15 3000"
            :error="errors.model"
            required
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Número de Serie *
          </label>
          <fwb-input 
            v-model="form.serialNumber"
            placeholder="Ej: DL001234567"
            :error="errors.serialNumber"
            required
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Categoría *
          </label>
          <select 
            v-model="form.category" 
            :class="[
              'bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5',
              'dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500',
              errors.category ? 'border-red-500' : ''
            ]"
            required
          >
            <option value="">Seleccionar categoría</option>
            <option v-if="loadingCategories" disabled>Cargando categorías...</option>
            <option v-if="!loadingCategories && categories.length === 0" disabled>No hay categorías disponibles</option>
            <option 
              v-for="category in categories" 
              :key="category.id" 
              :value="category.nombre"
            >
              {{ category.nombre }}
            </option>
          </select>
          <!-- Debug info -->
          <div v-if="categories.length > 0" class="text-xs text-green-600 mt-1">
            {{ categories.length }} categorías cargadas
          </div>
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Estado *
          </label>
          <select 
            v-model="form.status" 
            :class="[
              'bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5',
              'dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500',
              errors.status ? 'border-red-500' : ''
            ]"
            required
          >
            <option value="">Seleccionar estado</option>
            <option 
              v-for="state in availableStates" 
              :key="state.value" 
              :value="state.value"
            >
              {{ state.label }}
            </option>
          </select>
          <!-- Debug info -->
          <div v-if="availableStates.length > 0" class="text-xs text-blue-600 mt-1">
            {{ availableStates.length }} estados disponibles
          </div>
        </div>
      </div>
    </div>

    <!-- Especificaciones Técnicas -->
    <div class="bg-gray-50 dark:bg-gray-700 p-6 rounded-lg">
      <h3 class="text-lg font-semibold text-gray-900 dark:text-white mb-4">
        <font-awesome-icon icon="fa-solid fa-cogs" class="mr-2 text-green-500"/>
        Especificaciones Técnicas
      </h3>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Procesador
          </label>
          <fwb-input 
            v-model="form.specifications.processor"
            placeholder="Ej: Intel Core i5-1135G7"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Memoria RAM
          </label>
          <fwb-input 
            v-model="form.specifications.ram"
            placeholder="Ej: 8GB DDR4"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Almacenamiento
          </label>
          <fwb-input 
            v-model="form.specifications.storage"
            placeholder="Ej: 256GB SSD"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Tarjeta Gráfica
          </label>
          <fwb-input 
            v-model="form.specifications.graphics"
            placeholder="Ej: Intel Iris Xe"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Pantalla
          </label>
          <fwb-input 
            v-model="form.specifications.screen"
            placeholder="Ej: 15.6\"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Sistema Operativo
          </label>
          <fwb-input 
            v-model="form.specifications.os"
            placeholder="Ej: Windows 11 Home"
          />
        </div>
      </div>
    </div>

    <!-- Información Financiera -->
    <div class="bg-gray-50 dark:bg-gray-700 p-6 rounded-lg">
      <h3 class="text-lg font-semibold text-gray-900 dark:text-white mb-4">
        <font-awesome-icon icon="fa-solid fa-dollar-sign" class="mr-2 text-purple-500"/>
        Información Financiera
      </h3>
      
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Tarifa Diaria *
          </label>
          <fwb-input 
            v-model.number="form.dailyRate"
            type="number"
            step="0.01"
            min="0"
            placeholder="25.00"
            :error="errors.dailyRate"
            required
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Tarifa Semanal
          </label>
          <fwb-input 
            v-model.number="form.weeklyRate"
            type="number"
            step="0.01"
            min="0"
            placeholder="150.00"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Tarifa Mensual
          </label>
          <fwb-input 
            v-model.number="form.monthlyRate"
            type="number"
            step="0.01"
            min="0"
            placeholder="500.00"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Precio de Compra
          </label>
          <fwb-input 
            v-model.number="form.purchasePrice"
            type="number"
            step="0.01"
            min="0"
            placeholder="899.99"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Valor Actual
          </label>
          <fwb-input 
            v-model.number="form.currentValue"
            type="number"
            step="0.01"
            min="0"
            placeholder="650.00"
          />
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Fecha de Compra
          </label>
          <fwb-input 
            v-model="form.purchaseDate"
            type="date"
          />
        </div>
      </div>
    </div>

    <!-- Información Adicional -->
    <div class="bg-gray-50 dark:bg-gray-700 p-6 rounded-lg">
      <h3 class="text-lg font-semibold text-gray-900 dark:text-white mb-4">
        <font-awesome-icon icon="fa-solid fa-info" class="mr-2 text-orange-500"/>
        Información Adicional
      </h3>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Condición
          </label>
          <select 
            v-model="form.condition"
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
          >
            <option value="">Seleccionar condición</option>
            <option value="excelente">Excelente</option>
            <option value="buena">Buena</option>
            <option value="regular">Regular</option>
            <option value="mala">Mala</option>
          </select>
        </div>
        
        <div>
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Ubicación
          </label>
          <fwb-input 
            v-model="form.location"
            placeholder="Ej: Almacén Principal"
          />
        </div>
        
        <div class="md:col-span-2">
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
            Notas
          </label>
          <fwb-textarea 
            v-model="form.notes"
            placeholder="Información adicional sobre el equipo..."
            rows="3"
          />
        </div>
      </div>
    </div>

    <!-- Botones de Acción -->
    <div class="flex justify-end gap-4 pt-6 border-t border-gray-200 dark:border-gray-600">
      <fwb-button 
        gradient="gray" 
        @click="$emit('cancel')"
        type="button"
      >
        Cancelar
      </fwb-button>
      
      <fwb-button 
        gradient="blue" 
        type="submit"
        :disabled="isSubmitting"
      >
        <div v-if="isSubmitting" class="loader mr-2 w-4 h-4 border-2"></div>
        {{ mode === 'add' ? 'Agregar Equipo' : 'Actualizar Equipo' }}
      </fwb-button>
    </div>
  </form>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue';
import { useInventoryStore } from '@/stores/inventory.js';
import { useInventarioItemStore } from '@/stores/inventarioItem.js';
import CategoryService from '@/services/CategoryService.js';
import { 
  FwbInput, 
  FwbTextarea, 
  FwbButton,
  FwbAlert
} from 'flowbite-vue';

/**
 * Componente EquipmentForm.
 * 
 * Este componente proporciona un formulario completo para crear y editar equipos en el inventario.
 * Maneja la validación de datos, la carga de categorías y estados, y la comunicación con
 * los stores de Pinia para persistir los cambios.
 * 
 * Funcionalidades principales:
 * - Creación y edición de equipos.
 * - Validación de campos obligatorios.
 * - Carga dinámica de categorías desde el backend.
 * - Mapeo de datos para coincidir con la estructura de la base de datos.
 */

// Props
// mode: Determina si el formulario está en modo 'add' (agregar) o 'edit' (editar).
// selectedEquipment: Objeto con los datos del equipo a editar (solo en modo 'edit').
const props = defineProps({
  mode: {
    type: String,
    default: 'add',
    validator: (value) => ['add', 'edit'].includes(value)
  },
  selectedEquipment: {
    type: Object,
    default: null
  }
});

// Emits
const emit = defineEmits(['success', 'cancel']);

// Stores
const inventoryStore = useInventoryStore();
const inventarioItemStore = useInventarioItemStore();

// Estado
const isSubmitting = ref(false);
const errors = ref({});
const categories = ref([]);
const loadingCategories = ref(false);

// Estado para la alerta
const alertState = ref({
  show: false,
  type: 'info',
  message: ''
});

// Estados disponibles
const availableStates = ref([
  { value: 'disponible', label: 'Disponible' },
  { value: 'alquilado', label: 'Alquilado' },
  { value: 'mantenimiento', label: 'Mantenimiento' },
  { value: 'fuera_de_servicio', label: 'Fuera de Servicio' }
]);

// Inicializar categorías con datos de prueba para asegurar que funcione
const testCategories = [
  { id: 1, nombre: 'Sonido' },
  { id: 2, nombre: 'Video' },
  { id: 3, nombre: 'Iluminación' },
  { id: 4, nombre: 'Mobiliario' },
  { id: 5, nombre: 'Estructuras' }
];

// Formulario
const form = reactive({
  name: '',
  brand: '',
  model: '',
  serialNumber: '',
  category: '',
  status: 'disponible',
  dailyRate: 0,
  weeklyRate: 0,
  monthlyRate: 0,
  purchasePrice: 0,
  currentValue: 0,
  purchaseDate: '',
  condition: 'excelente',
  location: '',
  notes: '',
  specifications: {
    processor: '',
    ram: '',
    storage: '',
    graphics: '',
    screen: '',
    os: ''
  }
});

// Métodos

/**
 * Valida los campos del formulario antes del envío.
 * Verifica que los campos obligatorios (nombre, marca, modelo, serie, categoría, estado, tarifa) no estén vacíos.
 * @returns {boolean} True si el formulario es válido, False si hay errores.
 */
const validateForm = () => {
  errors.value = {};
  
  if (!form.name.trim()) {
    errors.value.name = 'El nombre es requerido';
  }
  
  if (!form.brand.trim()) {
    errors.value.brand = 'La marca es requerida';
  }
  
  if (!form.model.trim()) {
    errors.value.model = 'El modelo es requerido';
  }
  
  if (!form.serialNumber.trim()) {
    errors.value.serialNumber = 'El número de serie es requerido';
  }
  
  if (!form.category) {
    errors.value.category = 'La categoría es requerida';
  }
  
  if (!form.status) {
    errors.value.status = 'El estado es requerido';
  }
  
  if (!form.dailyRate || form.dailyRate <= 0) {
    errors.value.dailyRate = 'La tarifa diaria debe ser mayor a 0';
  }
  
  return Object.keys(errors.value).length === 0;
};

/**
 * Maneja el envío del formulario.
 * Realiza la validación, mapea los datos al formato esperado por el backend
 * y llama a la acción correspondiente del store (createProduct o updateProduct).
 */
const handleSubmit = async () => {
  console.log('handleSubmit iniciado, modo:', props.mode);
  console.log('Datos del formulario:', form);
  console.log('Equipo seleccionado:', props.selectedEquipment);
  
  alertState.value.show = false; // Ocultar alertas previas

  if (!validateForm()) {
    console.log('Validación falló, errores:', errors.value);
    return;
  }
  
  isSubmitting.value = true;
  
  try {
    // Mapear campos del formulario a los campos del backend
    const productData = {
      nombre: form.name,
      descripcion: form.notes || '',
      marca: form.brand,
      modelo: form.model,
      numero_serie: form.serialNumber,
      categoria: form.category,
      estado: form.status,
      especificaciones: form.specifications,
      precio_alquiler_dia: form.dailyRate,
      precio_alquiler_semanal: form.weeklyRate,
      precio_alquiler_mensual: form.monthlyRate,
      precio_compra: form.purchasePrice,
      valor_actual: form.currentValue,
      fecha_compra: form.purchaseDate,
      condicion: form.condition,
      ubicacion: form.location,
      notas: form.notes
    };

    console.log('Datos mapeados para enviar:', productData);

    if (props.mode === 'add') {
      console.log('Creando nuevo producto...');
      await inventoryStore.createProduct(productData);
    } else {
      // Obtener el producto_id del equipo seleccionado
      // El selectedEquipment viene del computed products que tiene producto_id
      let productoId = null;
      
      if (props.selectedEquipment) {
        // Intentar obtener producto_id de diferentes formas
        productoId = props.selectedEquipment.producto_id || 
                    props.selectedEquipment.producto?.id || 
                    (props.selectedEquipment.id && props.selectedEquipment.inventario_item_id ? null : props.selectedEquipment.id);
        
        console.log('Intentando obtener producto_id:');
        console.log('- producto_id directo:', props.selectedEquipment.producto_id);
        console.log('- producto.id:', props.selectedEquipment.producto?.id);
        console.log('- id (puede ser inventario_item_id):', props.selectedEquipment.id);
        console.log('- inventario_item_id:', props.selectedEquipment.inventario_item_id);
        
        // Si no se encontró producto_id pero hay inventario_item_id, 
        // necesitamos obtener el producto desde el inventarioItem
        if (!productoId && (props.selectedEquipment.inventario_item_id || props.selectedEquipment.id)) {
          console.log('No se encontró producto_id, buscando en inventarioItem...');
          // Buscar el inventarioItem en el store para obtener el producto_id
          const inventarioItem = inventarioItemStore.inventarioItems.find(
            item => item.id === props.selectedEquipment.inventario_item_id || 
                    item.id === props.selectedEquipment.id ||
                    (item.producto_id && item.producto_id === props.selectedEquipment.producto_id)
          );
          
          if (inventarioItem) {
            if (inventarioItem.producto) {
              productoId = inventarioItem.producto.id || inventarioItem.producto_id;
              console.log('producto_id encontrado en inventarioItem.producto:', productoId);
            } else if (inventarioItem.producto_id) {
              productoId = inventarioItem.producto_id;
              console.log('producto_id encontrado en inventarioItem.producto_id:', productoId);
            }
          }
        }
      }
      
      console.log('Producto ID final para actualizar:', productoId);
      
      if (!productoId) {
        throw new Error('No se encontró el ID del producto para actualizar. Por favor, recarga la página e intenta nuevamente.');
      }
      
      console.log('Actualizando producto ID:', productoId);
      await inventoryStore.updateProduct(productoId, productData);
    }
    
    console.log('Operación exitosa, emitiendo success...');
    emit('success');
  } catch (error) {
    console.error('Error al guardar producto:', error);
    const errorMessage = error.response?.data?.message || error.message || 'Error desconocido';
    alertState.value = {
      show: true,
      type: 'danger',
      message: 'Error al guardar el producto: ' + errorMessage
    };
  } finally {
    isSubmitting.value = false;
  }
};

/**
 * Carga los datos del equipo seleccionado en el formulario para su edición.
 * Mapea los campos del objeto `selectedEquipment` a las propiedades reactivas del formulario.
 * Maneja la normalización de datos como fechas y estados.
 */
const loadEquipmentData = () => {
  console.log('loadEquipmentData llamado con:', props.selectedEquipment);
  console.log('Modo actual:', props.mode);
  
  if (props.selectedEquipment) {
    const equipment = props.selectedEquipment;
    console.log('Datos del equipo a cargar:', equipment);
    
    // Obtener datos del producto (puede estar directamente o anidado)
    const producto = equipment.producto || equipment;
    
    // Asignar datos básicos - priorizar datos del producto anidado si existe
    form.name = producto.nombre || equipment.nombre || '';
    form.brand = producto.marca || equipment.marca || '';
    form.model = producto.modelo || equipment.modelo || '';
    form.serialNumber = producto.numero_serie || equipment.numero_serie || '';
    form.category = producto.categoria || equipment.categoria || '';
    
    // Mapear estado correctamente
    const estadoMap = {
      'Disponible': 'disponible',
      'Rentado': 'alquilado',
      'EnMantenimiento': 'mantenimiento',
      'DeBaja': 'de_baja',
      'disponible': 'disponible',
      'alquilado': 'alquilado',
      'mantenimiento': 'mantenimiento',
      'de_baja': 'de_baja'
    };
    const estadoEquipo = producto.estado || equipment.estado || equipment.estado_item || 'disponible';
    form.status = estadoMap[estadoEquipo] || estadoEquipo.toLowerCase() || 'disponible';
    
    form.dailyRate = parseFloat(producto.precio_alquiler_dia || equipment.precio_alquiler_dia || 0);
    form.weeklyRate = parseFloat(producto.precio_alquiler_semanal || equipment.precio_alquiler_semanal || 0);
    form.monthlyRate = parseFloat(producto.precio_alquiler_mensual || equipment.precio_alquiler_mensual || 0);
    form.purchasePrice = parseFloat(producto.precio_compra || equipment.precio_compra || 0);
    form.currentValue = parseFloat(producto.valor_actual || equipment.valor_actual || 0);
    
    // Formatear fecha de compra si existe
    let fechaCompra = producto.fecha_compra || equipment.fecha_compra || '';
    if (fechaCompra) {
      // Si la fecha viene en formato diferente, convertirla
      const date = new Date(fechaCompra);
      if (!isNaN(date.getTime())) {
        fechaCompra = date.toISOString().split('T')[0];
      }
    }
    form.purchaseDate = fechaCompra;
    
    form.condition = producto.condicion || equipment.condicion || 'excelente';
    form.location = producto.ubicacion || equipment.ubicacion || equipment.ubicacion_fisica || '';
    form.notes = producto.notas || equipment.notas || '';
    
    // Asignar especificaciones
    const especificaciones = producto.especificaciones || equipment.especificaciones || {};
    if (especificaciones && typeof especificaciones === 'object') {
      form.specifications.processor = especificaciones.procesador || especificaciones.processor || '';
      form.specifications.ram = especificaciones.memoria_ram || especificaciones.ram || '';
      form.specifications.storage = especificaciones.almacenamiento || especificaciones.storage || '';
      form.specifications.graphics = especificaciones.tarjeta_grafica || especificaciones.graphics || '';
      form.specifications.screen = especificaciones.pantalla || especificaciones.screen || '';
      form.specifications.os = especificaciones.sistema_operativo || especificaciones.os || '';
    }
    
    console.log('Formulario después de cargar datos:', form);
  } else {
    console.log('No hay selectedEquipment para cargar');
  }
};

/**
 * Restablece el formulario a su estado inicial (limpio).
 * Se utiliza al cambiar de modo o al finalizar una operación.
 */
const resetForm = () => {
  Object.assign(form, {
    name: '',
    brand: '',
    model: '',
    serialNumber: '',
    category: '',
    status: 'disponible',
    dailyRate: 0,
    weeklyRate: 0,
    monthlyRate: 0,
    purchasePrice: 0,
    currentValue: 0,
    purchaseDate: '',
    condition: 'excelente',
    location: '',
    notes: '',
    specifications: {
      processor: '',
      ram: '',
      storage: '',
      graphics: '',
      screen: '',
      os: ''
    }
  });
  errors.value = {};
};

// Watchers
watch(() => props.selectedEquipment, () => {
  if (props.mode === 'edit' && props.selectedEquipment) {
    loadEquipmentData();
  } else {
    resetForm();
  }
}, { immediate: true });

// Watcher para categorías del store
watch(() => inventoryStore.categoryList, (newCategories) => {
  if (newCategories && newCategories.length > 0) {
    categories.value = newCategories;
    console.log('Categorías actualizadas en el formulario:', categories.value);
  }
}, { immediate: true, deep: true });

// Función para cargar categorías
/**
 * Carga la lista de categorías disponibles desde el store o la API.
 * Utiliza datos de prueba como respaldo si la carga falla, asegurando que el formulario sea funcional.
 */
const loadCategories = async () => {
  loadingCategories.value = true;
  
  // Primero, cargar categorías de prueba para garantizar que funcione
  categories.value = testCategories;
  console.log('Categorías de prueba cargadas:', categories.value);
  
  try {
    // Intentar cargar desde el store
    await inventoryStore.fetchCategories();
    
    if (inventoryStore.categoryList && inventoryStore.categoryList.length > 0) {
      categories.value = inventoryStore.categoryList;
      console.log('Categorías actualizadas desde store:', categories.value);
    } else {
      // Si el store no tiene categorías, cargar directamente desde el servicio
      console.log('Store vacío, intentando cargar desde API...');
      const response = await CategoryService.getCategories();
      console.log('Respuesta directa del API:', response);
      
      if (response && response.categoria && Array.isArray(response.categoria)) {
        categories.value = response.categoria;
        console.log('Categorías actualizadas desde API:', categories.value);
      } else {
        console.log('Manteniendo categorías de prueba');
      }
    }
  } catch (error) {
    console.error('Error al cargar categorías, usando categorías de prueba:', error);
    // Mantener las categorías de prueba en caso de error
  } finally {
    loadingCategories.value = false;
  }
};

// Lifecycle
onMounted(async () => {
  console.log('EquipmentForm mounted, loading categories...');
  
  // Cargar categorías
  await loadCategories();
  
  // Debug: verificar estado después de cargar
  console.log('Categories after load:', categories.value);
  console.log('Store categoryList:', inventoryStore.categoryList);
  
  if (props.mode === 'edit' && props.selectedEquipment) {
    loadEquipmentData();
  }
});
</script>

<style scoped>
/* Estilos adicionales si son necesarios */
</style>

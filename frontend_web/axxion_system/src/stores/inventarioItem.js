import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import InventarioItemService from '@/services/InventarioItemService';

export const useInventarioItemStore = defineStore('inventarioItem', () => {
  // ===== ESTADO =====
  const inventarioItems = ref([]);
  const currentInventarioItem = ref(null);
  const loading = ref(false);
  const error = ref(null);
  const selectedInventarioItem = ref(null);
  
  // Filtros y paginación
  const filters = ref({
    estado: '',
    producto_id: '',
    ubicacion: '',
    search: ''
  });
  
  const pagination = ref({
    currentPage: 1,
    totalPages: 1,
    totalItems: 0,
    itemsPerPage: 10
  });
  
  const sorting = ref({
    field: 'created_at',
    direction: 'desc'
  });

  // ===== GETTERS =====
  const getInventarioItems = computed(() => inventarioItems.value);
  const getCurrentInventarioItem = computed(() => currentInventarioItem.value);
  const getSelectedInventarioItem = computed(() => selectedInventarioItem.value);
  const isLoading = computed(() => loading.value);
  const getError = computed(() => error.value);
  const getFilters = computed(() => filters.value);
  const getPagination = computed(() => pagination.value);
  const getSorting = computed(() => sorting.value);

  // Inventario items filtrados
  const filteredInventarioItems = computed(() => {
    let filtered = inventarioItems.value;

    if (filters.value.estado) {
      filtered = filtered.filter(item => item.estado_item === filters.value.estado);
    }

    if (filters.value.producto_id) {
      filtered = filtered.filter(item => item.producto_id === parseInt(filters.value.producto_id));
    }

    if (filters.value.ubicacion) {
      filtered = filtered.filter(item => 
        item.ubicacion_fisica && 
        item.ubicacion_fisica.toLowerCase().includes(filters.value.ubicacion.toLowerCase())
      );
    }

    if (filters.value.search) {
      const searchTerm = filters.value.search.toLowerCase();
      filtered = filtered.filter(item => 
        item.numero_serie?.toLowerCase().includes(searchTerm) ||
        item.producto?.nombre?.toLowerCase().includes(searchTerm) ||
        item.notas?.toLowerCase().includes(searchTerm)
      );
    }

    return filtered;
  });

  // Estadísticas
  const estadisticas = computed(() => {
    const total = inventarioItems.value.length;
    const disponibles = inventarioItems.value.filter(item => item.estado_item === 'Disponible').length;
    const rentados = inventarioItems.value.filter(item => item.estado_item === 'Rentado').length;
    const enMantenimiento = inventarioItems.value.filter(item => item.estado_item === 'EnMantenimiento').length;
    const deBaja = inventarioItems.value.filter(item => item.estado_item === 'DeBaja').length;

    return {
      total,
      disponibles,
      rentados,
      enMantenimiento,
      deBaja,
      porcentajeDisponibles: total > 0 ? Math.round((disponibles / total) * 100) : 0
    };
  });

  // ===== ACCIONES =====
  
  /**
   * Obtener todos los inventario items
   */
  const fetchInventarioItems = async () => {
    try {
      loading.value = true;
      error.value = null;
      
      const service = new InventarioItemService();
      // Usar el endpoint que incluye renta_activa
      const response = await service.getItemsWithRentalStatus();
      
      inventarioItems.value = response.inventario_items || response;
      console.log('Inventario items cargados con estado de renta:', inventarioItems.value);
      
    } catch (err) {
      error.value = 'Error al cargar los items de inventario: ' + (err.response?.data?.error || err.message);
      console.error('Error al cargar inventario items:', err);
    } finally {
      loading.value = false;
    }
  };

  /**
   * Obtener inventario item por ID
   */
  const fetchInventarioItemById = async (id) => {
    try {
      loading.value = true;
      error.value = null;
      
      const service = new InventarioItemService();
      const response = await service.getInventarioItemById(id);
      
      currentInventarioItem.value = response.inventario_item || response;
      return currentInventarioItem.value;
      
    } catch (err) {
      error.value = 'Error al cargar el item de inventario: ' + (err.response?.data?.error || err.message);
      console.error('Error al cargar inventario item:', err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * Crear nuevo inventario item
   */
  const createInventarioItem = async (inventarioItemData) => {
    try {
      loading.value = true;
      error.value = null;
      
      const service = new InventarioItemService();
      const response = await service.createInventarioItem(inventarioItemData);
      
      const newItem = response.inventario_item || response;
      inventarioItems.value.unshift(newItem);
      
      console.log('Inventario item creado:', newItem);
      return newItem;
      
    } catch (err) {
      error.value = 'Error al crear el item de inventario: ' + (err.response?.data?.error || err.message);
      console.error('Error al crear inventario item:', err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * Actualizar inventario item
   */
  const updateInventarioItem = async (id, inventarioItemData) => {
    try {
      loading.value = true;
      error.value = null;
      
      const service = new InventarioItemService();
      const response = await service.updateInventarioItem(id, inventarioItemData);
      
      const updatedItem = response.inventario_item || response;
      const index = inventarioItems.value.findIndex(item => item.id === id);
      
      if (index !== -1) {
        inventarioItems.value[index] = updatedItem;
      }
      
      if (currentInventarioItem.value?.id === id) {
        currentInventarioItem.value = updatedItem;
      }
      
      console.log('Inventario item actualizado:', updatedItem);
      return updatedItem;
      
    } catch (err) {
      error.value = 'Error al actualizar el item de inventario: ' + (err.response?.data?.error || err.message);
      console.error('Error al actualizar inventario item:', err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * Actualizar parcialmente inventario item
   */
  const updateInventarioItemPartial = async (id, inventarioItemData) => {
    try {
      loading.value = true;
      error.value = null;
      
      const service = new InventarioItemService();
      const response = await service.updateInventarioItemPartial(id, inventarioItemData);
      
      const updatedItem = response.inventario_item || response;
      const index = inventarioItems.value.findIndex(item => item.id === id);
      
      if (index !== -1) {
        inventarioItems.value[index] = { ...inventarioItems.value[index], ...updatedItem };
      }
      
      if (currentInventarioItem.value?.id === id) {
        currentInventarioItem.value = { ...currentInventarioItem.value, ...updatedItem };
      }
      
      console.log('Inventario item actualizado parcialmente:', updatedItem);
      return updatedItem;
      
    } catch (err) {
      error.value = 'Error al actualizar el item de inventario: ' + (err.response?.data?.error || err.message);
      console.error('Error al actualizar inventario item:', err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * Eliminar inventario item
   */
  const deleteInventarioItem = async (id) => {
    try {
      loading.value = true;
      error.value = null;
      
      const service = new InventarioItemService();
      await service.deleteInventarioItem(id);
      
      inventarioItems.value = inventarioItems.value.filter(item => item.id !== id);
      
      if (currentInventarioItem.value?.id === id) {
        currentInventarioItem.value = null;
      }
      
      if (selectedInventarioItem.value?.id === id) {
        selectedInventarioItem.value = null;
      }
      
      console.log('Inventario item eliminado:', id);
      
    } catch (err) {
      error.value = 'Error al eliminar el item de inventario: ' + (err.response?.data?.error || err.message);
      console.error('Error al eliminar inventario item:', err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * Obtener inventario items por producto
   */
  const fetchInventarioItemsByProducto = async (productoId) => {
    try {
      loading.value = true;
      error.value = null;
      
      const service = new InventarioItemService();
      const response = await service.getInventarioItemsByProducto(productoId);
      
      return response.inventario_items || response;
      
    } catch (err) {
      error.value = 'Error al cargar items por producto: ' + (err.response?.data?.error || err.message);
      console.error('Error al cargar inventario items por producto:', err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * Obtener inventario items por estado
   */
  const fetchInventarioItemsByEstado = async (estado) => {
    try {
      loading.value = true;
      error.value = null;
      
      const service = new InventarioItemService();
      const response = await service.getInventarioItemsByEstado(estado);
      
      return response.inventario_items || response;
      
    } catch (err) {
      error.value = 'Error al cargar items por estado: ' + (err.response?.data?.error || err.message);
      console.error('Error al cargar inventario items por estado:', err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  // ===== MÉTODOS DE UTILIDAD =====
  
  /**
   * Seleccionar inventario item
   */
  const selectInventarioItem = (inventarioItem) => {
    selectedInventarioItem.value = inventarioItem;
  };

  /**
   * Limpiar inventario item seleccionado
   */
  const clearSelectedInventarioItem = () => {
    selectedInventarioItem.value = null;
  };

  /**
   * Establecer filtros
   */
  const setFilters = (newFilters) => {
    filters.value = { ...filters.value, ...newFilters };
  };

  /**
   * Limpiar filtros
   */
  const clearFilters = () => {
    filters.value = {
      estado: '',
      producto_id: '',
      ubicacion: '',
      search: ''
    };
  };

  /**
   * Establecer página actual
   */
  const setCurrentPage = (page) => {
    pagination.value.currentPage = page;
  };

  /**
   * Establecer ordenamiento
   */
  const setSorting = (field, direction) => {
    sorting.value = { field, direction };
  };

  /**
   * Limpiar error
   */
  const clearError = () => {
    error.value = null;
  };

  /**
   * Limpiar inventario item actual
   */
  const clearCurrentInventarioItem = () => {
    currentInventarioItem.value = null;
  };

  /**
   * Resetear estado
   */
  const resetState = () => {
    inventarioItems.value = [];
    currentInventarioItem.value = null;
    selectedInventarioItem.value = null;
    loading.value = false;
    error.value = null;
    clearFilters();
    pagination.value = {
      currentPage: 1,
      totalPages: 1,
      totalItems: 0,
      itemsPerPage: 10
    };
    sorting.value = {
      field: 'created_at',
      direction: 'desc'
    };
  };

  /**
   * Obtener estados disponibles
   */
  const getAvailableEstados = () => {
    const service = new InventarioItemService();
    return service.getAvailableEstados();
  };

  return {
    // Estado
    inventarioItems,
    currentInventarioItem,
    selectedInventarioItem,
    loading,
    error,
    filters,
    pagination,
    sorting,
    
    // Getters
    getInventarioItems,
    getCurrentInventarioItem,
    getSelectedInventarioItem,
    isLoading,
    getError,
    getFilters,
    getPagination,
    getSorting,
    filteredInventarioItems,
    estadisticas,
    
    // Acciones
    fetchInventarioItems,
    fetchInventarioItemById,
    createInventarioItem,
    updateInventarioItem,
    updateInventarioItemPartial,
    deleteInventarioItem,
    fetchInventarioItemsByProducto,
    fetchInventarioItemsByEstado,
    
    // Métodos de utilidad
    selectInventarioItem,
    clearSelectedInventarioItem,
    setFilters,
    clearFilters,
    setCurrentPage,
    setSorting,
    clearError,
    clearCurrentInventarioItem,
    resetState,
    getAvailableEstados
  };
});



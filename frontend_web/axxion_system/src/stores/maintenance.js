import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import MaintenanceService from '@/services/MaintenanceService.js';

export const useMaintenanceStore = defineStore('maintenance', () => {
  // Estado
  const maintenances = ref([]);
  const currentMaintenance = ref(null);
  const isLoading = ref(false);
  const error = ref(null);

  // Getters computados
   const maintenancesByStatus = computed(() => {
     const statusGroups = {};
     maintenances.value.forEach(maintenance => {
       const status = maintenance.estado_mantenimiento || 'SIN_ESTADO';
       if (!statusGroups[status]) {
         statusGroups[status] = [];
       }
       statusGroups[status].push(maintenance);
     });
     return statusGroups;
   });

   const maintenancesByType = computed(() => {
     const typeGroups = {};
     maintenances.value.forEach(maintenance => {
       const type = maintenance.tipo_mantenimiento || 'SIN_TIPO';
       if (!typeGroups[type]) {
         typeGroups[type] = [];
       }
       typeGroups[type].push(maintenance);
     });
     return typeGroups;
   });

  const maintenancesByPriority = computed(() => {
    const priorityGroups = {};
    maintenances.value.forEach(maintenance => {
      const priority = maintenance.prioridad || 'SIN_PRIORIDAD';
      if (!priorityGroups[priority]) {
        priorityGroups[priority] = [];
      }
      priorityGroups[priority].push(maintenance);
    });
    return priorityGroups;
  });

  const upcomingMaintenances = computed(() => {
    const today = new Date();
    const nextWeek = new Date(today.getTime() + 7 * 24 * 60 * 60 * 1000);

    return maintenances.value.filter(maintenance => {
      if (!maintenance.fecha_inicio) return false;
      const maintenanceDate = new Date(maintenance.fecha_inicio);
      return maintenanceDate >= today && maintenanceDate <= nextWeek &&
             (maintenance.estado_mantenimiento === 'Programado' || maintenance.estado_mantenimiento === 'EnProceso');
    });
  });

  const overdueMaintenances = computed(() => {
    const today = new Date();
    return maintenances.value.filter(maintenance => {
      if (!maintenance.fecha_fin_prevista) return false;
      const endDate = new Date(maintenance.fecha_fin_prevista);
      return endDate < today && maintenance.estado_mantenimiento !== 'Finalizado' && maintenance.estado_mantenimiento !== 'Cancelado';
    });
  });

  // Actions
  const fetchMaintenances = async () => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.getMaintenances();
      console.log('Store received response:', response);
      console.log('Store response type:', typeof response);
      console.log('Store response is array:', Array.isArray(response));
      
      // La respuesta viene directamente como array según los logs de consola
      maintenances.value = Array.isArray(response) ? response : (response.data || response.mantenimiento || []);
      console.log('Store set maintenances to:', maintenances.value);
      console.log('Store maintenances length:', maintenances.value.length);
      return response;
    } catch (err) {
      error.value = err.message || 'Error al cargar mantenimientos';
      console.error('Error fetching maintenances:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  const fetchMaintenanceById = async (id) => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.getMaintenanceById(id);
      currentMaintenance.value = response.data || response.mantenimiento;
      return response;
    } catch (err) {
      error.value = err.message || 'Error al cargar mantenimiento';
      console.error('Error fetching maintenance by ID:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  const createMaintenance = async (maintenanceData) => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.createMaintenance(maintenanceData);
      
      // Agregar el nuevo mantenimiento al estado local
      const newMaintenance = response.data || response.mantenimiento;
      if (newMaintenance) {
        maintenances.value.push(newMaintenance);
      }
      
      return response;
    } catch (err) {
      error.value = err.message || 'Error al crear mantenimiento';
      console.error('Error creating maintenance:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  const updateMaintenance = async (id, maintenanceData) => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.updateMaintenance(id, maintenanceData);
      
      // Actualizar el mantenimiento en el estado local
      const updatedMaintenance = response.data || response.mantenimiento;
      if (updatedMaintenance) {
        const index = maintenances.value.findIndex(maintenance => maintenance.id === id);
        if (index !== -1) {
          maintenances.value[index] = updatedMaintenance;
        }
        
        // Actualizar currentMaintenance si es el mismo
        if (currentMaintenance.value && currentMaintenance.value.id === id) {
          currentMaintenance.value = updatedMaintenance;
        }
      }
      
      return response;
    } catch (err) {
      error.value = err.message || 'Error al actualizar mantenimiento';
      console.error('Error updating maintenance:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  const updateMaintenancePartial = async (id, maintenanceData) => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.updateMaintenancePartial(id, maintenanceData);
      
      // Actualizar el mantenimiento en el estado local
      const updatedMaintenance = response.data || response.mantenimiento;
      if (updatedMaintenance) {
        const index = maintenances.value.findIndex(maintenance => maintenance.id === id);
        if (index !== -1) {
          maintenances.value[index] = { ...maintenances.value[index], ...updatedMaintenance };
        }
      }
      
      return response;
    } catch (err) {
      error.value = err.message || 'Error al actualizar mantenimiento parcialmente';
      console.error('Error partially updating maintenance:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  const deleteMaintenance = async (id) => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.deleteMaintenance(id);
      
      // Remover el mantenimiento del estado local
      const index = maintenances.value.findIndex(maintenance => maintenance.id === id);
      if (index !== -1) {
        maintenances.value.splice(index, 1);
      }
      
      // Limpiar currentMaintenance si es el mismo
      if (currentMaintenance.value && currentMaintenance.value.id === id) {
        currentMaintenance.value = null;
      }
      
      return response;
    } catch (err) {
      error.value = err.message || 'Error al eliminar mantenimiento';
      console.error('Error deleting maintenance:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  const fetchMaintenancesByInventoryItem = async (inventarioItemId) => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.getMaintenancesByInventoryItem(inventarioItemId);
      return response.data || response.mantenimiento || [];
    } catch (err) {
      error.value = err.message || 'Error al cargar mantenimientos por item de inventario';
      console.error('Error fetching maintenances by inventory item:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  const fetchMaintenancesByStatus = async (estado) => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.getMaintenancesByStatus(estado);
      return response.data || response.mantenimiento || [];
    } catch (err) {
      error.value = err.message || 'Error al cargar mantenimientos por estado';
      console.error('Error fetching maintenances by status:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  const fetchMaintenancesByType = async (tipo) => {
    try {
      isLoading.value = true;
      error.value = null;
      const response = await MaintenanceService.getMaintenancesByType(tipo);
      return response.data || response.mantenimiento || [];
    } catch (err) {
      error.value = err.message || 'Error al cargar mantenimientos por tipo';
      console.error('Error fetching maintenances by type:', err);
      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  // Métodos de utilidad
  const getAvailableStatuses = () => {
    return MaintenanceService.getAvailableStatuses();
  };

  const getAvailableTypes = () => {
    return MaintenanceService.getAvailableTypes();
  };

  const getAvailablePriorities = () => {
    return MaintenanceService.getAvailablePriorities();
  };

  const clearError = () => {
    error.value = null;
  };

  const clearCurrentMaintenance = () => {
    currentMaintenance.value = null;
  };

  const resetState = () => {
    maintenances.value = [];
    currentMaintenance.value = null;
    isLoading.value = false;
    error.value = null;
  };

  // Filtros locales
  const filterMaintenances = (filters) => {
    let filtered = [...maintenances.value];

    if (filters.search) {
      const searchLower = filters.search.toLowerCase();
      filtered = filtered.filter(maintenance =>
        (maintenance.descripcion_problema && maintenance.descripcion_problema.toLowerCase().includes(searchLower)) ||
        (maintenance.descripcion_trabajo_realizado && maintenance.descripcion_trabajo_realizado.toLowerCase().includes(searchLower)) ||
        (maintenance.inventario_item && maintenance.inventario_item.numero_serie &&
         maintenance.inventario_item.numero_serie.toLowerCase().includes(searchLower))
      );
    }

    if (filters.estado) {
      filtered = filtered.filter(maintenance => 
        maintenance.estado_mantenimiento && 
        maintenance.estado_mantenimiento.toUpperCase() === filters.estado.toUpperCase()
      );
    }

    if (filters.tipo) {
      filtered = filtered.filter(maintenance => 
        maintenance.tipo_mantenimiento && 
        maintenance.tipo_mantenimiento.toUpperCase() === filters.tipo.toUpperCase()
      );
    }

    if (filters.responsable) {
      // Comparación flexible para manejar IDs numéricos y strings
      filtered = filtered.filter(maintenance => 
        maintenance.responsable == filters.responsable
      );
    }

    if (filters.fecha_inicio) {
      filtered = filtered.filter(maintenance => {
        if (!maintenance.fecha_inicio) return false;
        const maintenanceDate = new Date(maintenance.fecha_inicio);
        const filterDate = new Date(filters.fecha_inicio);
        return maintenanceDate >= filterDate;
      });
    }

    if (filters.fecha_fin) {
      filtered = filtered.filter(maintenance => {
        if (!maintenance.fecha_fin_prevista) return false;
        const maintenanceDate = new Date(maintenance.fecha_fin_prevista);
        const filterDate = new Date(filters.fecha_fin);
        return maintenanceDate <= filterDate;
      });
    }

    return filtered;
  };

  return {
    // Estado
    maintenances,
    currentMaintenance,
    isLoading,
    error,
    
    // Getters
    maintenancesByStatus,
    maintenancesByType,
    maintenancesByPriority,
    upcomingMaintenances,
    overdueMaintenances,
    
    // Actions
    fetchMaintenances,
    fetchMaintenanceById,
    createMaintenance,
    updateMaintenance,
    updateMaintenancePartial,
    deleteMaintenance,
    fetchMaintenancesByInventoryItem,
    fetchMaintenancesByStatus,
    fetchMaintenancesByType,
    
    // Utilidades
    getAvailableStatuses,
    getAvailableTypes,
    getAvailablePriorities,
    clearError,
    clearCurrentMaintenance,
    resetState,
    filterMaintenances
  };
});

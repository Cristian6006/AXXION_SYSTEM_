import apiClient from '@/plugins/axios';

class MaintenanceService {
  constructor() {
    this.api = apiClient;
  }

  // Obtener todos los mantenimientos
  async getMaintenances() {
    try {
      const response = await this.api.get('/mantenimiento');
      console.log('MaintenanceService - Raw API response:', response);
      console.log('MaintenanceService - Response data:', response.data);
      console.log('MaintenanceService - Response data type:', typeof response.data);
      console.log('MaintenanceService - Response data length:', response.data?.length);
      return response.data;
    } catch (error) {
      console.error('Error al obtener mantenimientos:', error);
      throw error;
    }
  }

  // Obtener un mantenimiento específico
  async getMaintenanceById(id) {
    try {
      const response = await this.api.get(`/mantenimiento/${id}`);
      console.log('Maintenance by ID API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener mantenimiento:', error);
      throw error;
    }
  }

  // Crear un nuevo mantenimiento
  async createMaintenance(maintenanceData) {
    try {
      const response = await this.api.post('/mantenimiento', maintenanceData);
      console.log('Create maintenance API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al crear mantenimiento:', error);
      throw error;
    }
  }

  // Actualizar un mantenimiento completo
  async updateMaintenance(id, maintenanceData) {
    try {
      const response = await this.api.put(`/mantenimiento/${id}`, maintenanceData);
      console.log('Update maintenance API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al actualizar mantenimiento:', error);
      throw error;
    }
  }

  // Actualización parcial de un mantenimiento
  async updateMaintenancePartial(id, maintenanceData) {
    try {
      const response = await this.api.patch(`/mantenimiento/${id}`, maintenanceData);
      console.log('Partial update maintenance API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al actualizar parcialmente mantenimiento:', error);
      throw error;
    }
  }

  // Eliminar un mantenimiento (solo ADMIN)
  async deleteMaintenance(id) {
    try {
      const response = await this.api.delete(`/mantenimiento/${id}`);
      console.log('Delete maintenance API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al eliminar mantenimiento:', error);
      throw error;
    }
  }

  // Obtener mantenimientos por item de inventario
  async getMaintenancesByInventoryItem(inventarioItemId) {
    try {
      const response = await this.api.get(`/mantenimiento/inventario/${inventarioItemId}`);
      console.log('Maintenances by inventory item API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener mantenimientos por item de inventario:', error);
      throw error;
    }
  }

  // Obtener mantenimientos por estado
  async getMaintenancesByStatus(estado) {
    try {
      const response = await this.api.get(`/mantenimiento/estado/${estado}`);
      console.log('Maintenances by status API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener mantenimientos por estado:', error);
      throw error;
    }
  }

  // Obtener mantenimientos por tipo
  async getMaintenancesByType(tipo) {
    try {
      const response = await this.api.get(`/mantenimiento/tipo/${tipo}`);
      console.log('Maintenances by type API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener mantenimientos por tipo:', error);
      throw error;
    }
  }

  // Obtener estados disponibles
  getAvailableStatuses() {
    return [
      { value: 'PROGRAMADO', name: 'Programado' },
      { value: 'EN_PROCESO', name: 'En Proceso' },
      { value: 'COMPLETADO', name: 'Completado' },
      { value: 'CANCELADO', name: 'Cancelado' },
      { value: 'PAUSADO', name: 'Pausado' }
    ];
  }

  // Obtener tipos disponibles
  getAvailableTypes() {
    return [
      { value: 'PREVENTIVO', name: 'Preventivo' },
      { value: 'CORRECTIVO', name: 'Correctivo' },
      { value: 'PREDICTIVO', name: 'Predictivo' },
      { value: 'EMERGENCIA', name: 'Emergencia' }
    ];
  }

  // Obtener prioridades disponibles
  getAvailablePriorities() {
    return [
      { value: 'BAJA', name: 'Baja' },
      { value: 'MEDIA', name: 'Media' },
      { value: 'ALTA', name: 'Alta' },
      { value: 'CRITICA', name: 'Crítica' }
    ];
  }
}

export default new MaintenanceService();

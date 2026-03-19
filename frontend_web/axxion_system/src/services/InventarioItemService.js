import apiClient from '@/plugins/axios';

class InventarioItemService {
  constructor() {
    this.api = apiClient; // Usando apiClient configurado con interceptores
  }

  // ===== GESTIÓN DE INVENTARIO ITEMS =====
  
  /**
   * Obtener todos los inventario items
   */
  async getInventarioItems() {
    try {
      const response = await this.api.get('/inventario_item');
      console.log('Inventario Items API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener inventario items:', error);
      throw error;
    }
  }

  /**
   * Obtener un inventario item por ID
   */
  async getInventarioItemById(id) {
    try {
      const response = await this.api.get(`/inventario_item/${id}`);
      console.log('Inventario Item API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener inventario item:', error);
      throw error;
    }
  }

  /**
   * Crear un nuevo inventario item
   */
  async createInventarioItem(inventarioItemData) {
    try {
      const response = await this.api.post('/inventario_item', inventarioItemData);
      console.log('Inventario Item creado:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al crear inventario item:', error);
      throw error;
    }
  }

  /**
   * Actualizar un inventario item
   */
  async updateInventarioItem(id, inventarioItemData) {
    try {
      const response = await this.api.put(`/inventario_item/${id}`, inventarioItemData);
      console.log('Inventario Item actualizado:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al actualizar inventario item:', error);
      throw error;
    }
  }

  /**
   * Actualizar parcialmente un inventario item
   */
  async updateInventarioItemPartial(id, inventarioItemData) {
    try {
      const response = await this.api.patch(`/inventario_item/${id}`, inventarioItemData);
      console.log('Inventario Item actualizado parcialmente:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al actualizar inventario item:', error);
      throw error;
    }
  }

  /**
   * Eliminar un inventario item
   */
  async deleteInventarioItem(id) {
    try {
      const response = await this.api.delete(`/inventario_item/${id}`);
      console.log('Inventario Item eliminado:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al eliminar inventario item:', error);
      throw error;
    }
  }

  /**
   * Obtener inventario items por producto
   */
  async getInventarioItemsByProducto(productoId) {
    try {
      const response = await this.api.get(`/inventario_item/producto/${productoId}`);
      console.log('Inventario Items por producto:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener inventario items por producto:', error);
      throw error;
    }
  }

  /**
   * Obtener inventario items por estado
   */
  async getInventarioItemsByEstado(estado) {
    try {
      const response = await this.api.get(`/inventario_item/estado/${estado}`);
      console.log('Inventario Items por estado:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener inventario items por estado:', error);
      throw error;
    }
  }

  /**
   * Get inventario items with rental status
   */
  async getItemsWithRentalStatus() {
    try {
      console.log('InventarioItemService: Obteniendo items con estado de renta');
      const response = await this.api.get('/inventario_item_with_rental_status');
      console.log('InventarioItemService: Respuesta de items con renta:', response.data);
      return response.data;
    } catch (error) {
      console.error('InventarioItemService: Error al obtener items con estado de renta:', error.response?.data || error.message);
      throw error;
    }
  }

  // ===== MÉTODOS AUXILIARES =====

  /**
   * Obtener estados disponibles para inventario items
   */
  getAvailableEstados() {
    return [
      { value: 'Disponible', label: 'Disponible' },
      { value: 'Rentado', label: 'Rentado' },
      { value: 'EnMantenimiento', label: 'En Mantenimiento' },
      { value: 'DeBaja', label: 'De Baja' }
    ];
  }

  /**
   * Validar datos de inventario item
   */
  validateInventarioItemData(data) {
    const errors = [];

    if (!data.producto_id) {
      errors.push('El producto es requerido');
    }

    if (!data.estado_item) {
      errors.push('El estado del item es requerido');
    } else {
      const estadosValidos = ['Disponible', 'Rentado', 'EnMantenimiento', 'DeBaja'];
      if (!estadosValidos.includes(data.estado_item)) {
        errors.push('El estado del item no es válido');
      }
    }

    if (data.costo_adquisicion && data.costo_adquisicion < 0) {
      errors.push('El costo de adquisición no puede ser negativo');
    }

    return errors;
  }

  /**
   * Formatear datos para envío a la API
   */
  formatInventarioItemData(data) {
    return {
      producto_id: data.producto_id,
      numero_serie: data.numero_serie || null,
      estado_item: data.estado_item,
      fecha_adquisicion: data.fecha_adquisicion || null,
      costo_adquisicion: data.costo_adquisicion || null,
      ubicacion_fisica: data.ubicacion_fisica || null,
      notas: data.notas || null
    };
  }
}

export default InventarioItemService;

import apiClient from '@/plugins/axios';

class InventoryService {
  constructor() {
    this.api = apiClient; // Usando apiClient configurado con interceptores
  }

  // ===== GESTIÓN DE PRODUCTOS =====
  
  /**
   * Obtener todos los productos
   */
  async getProducts() {
    try {
      const response = await this.api.get('/producto');
      console.log('Products API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener productos:', error);
      throw error;
    }
  }

  /**
   * Obtener un producto por ID
   */
  async getProductById(id) {
    try {
      const response = await this.api.get(`/producto/${id}`);
      return response.data;
    } catch (error) {
      console.error('Error al obtener producto:', error);
      throw error;
    }
  }

  /**
   * Crear nuevo producto
   */
  async createProduct(productData) {
    try {
      const response = await this.api.post('/producto', productData);
      return response.data;
    } catch (error) {
      console.error('Error al crear producto:', error);
      throw error;
    }
  }

  /**
   * Actualizar producto existente
   */
  async updateProduct(id, productData) {
    try {
      console.log('InventoryService: Actualizando producto ID:', id);
      console.log('InventoryService: Datos enviados:', productData);
      
      const response = await this.api.put(`/producto/${id}`, productData);
      console.log('InventoryService: Respuesta de actualización:', response.data);
      
      return response.data;
    } catch (error) {
      console.error('InventoryService: Error al actualizar producto:', error.response?.data || error.message);
      throw error;
    }
  }

  /**
   * Eliminar producto
   */
  async deleteProduct(id) {
    try {
      console.log('InventoryService: Eliminando producto ID:', id);
      const response = await this.api.delete(`/producto/${id}`);
      console.log('InventoryService: Respuesta de eliminación:', response.data);
      return response.data;
    } catch (error) {
      console.error('InventoryService: Error al eliminar producto:', error.response?.data || error.message);
      throw error;
    }
  }
}

export default new InventoryService();


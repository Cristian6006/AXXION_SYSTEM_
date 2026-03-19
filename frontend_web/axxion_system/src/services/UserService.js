import apiClient from '@/plugins/axios';

class UserService {
  constructor() {
    this.api = apiClient;
  }

  // Obtener todos los usuarios
  async getUsers() {
    try {
      const response = await this.api.get('/usuarios');
      console.log('Users API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener usuarios:', error);
      throw error;
    }
  }

  // Crear un nuevo usuario
  async createUser(userData) {
    try {
      const response = await this.api.post('/usuarios', userData);
      return response.data;
    } catch (error) {
      console.error('Error al crear usuario:', error);
      throw error;
    }
  }

  // Actualizar un usuario
  async updateUser(id, userData) {
    try {
      const response = await this.api.put(`/usuario/${id}`, userData);
      return response.data;
    } catch (error) {
      console.error('Error al actualizar usuario:', error);
      throw error;
    }
  }

  // Eliminar un usuario
  async deleteUser(id) {
    try {
      const response = await this.api.delete(`/usuario/${id}`);
      return response.data;
    } catch (error) {
      console.error('Error al eliminar usuario:', error);
      throw error;
    }
  }

  // Obtener roles disponibles
  async getRoles() {
    try {
      const response = await this.api.get('/rol');
      console.log('Roles API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error al obtener roles:', error);
      // Retornar roles por defecto si la API no está disponible
      return {
        data: [
          { id: 1, nombre: 'Administrador' },
          { id: 2, nombre: 'Auxiliar' },
          { id: 3, nombre: 'Técnico' }
        ]
      };
    }
  }
}

export default new UserService();

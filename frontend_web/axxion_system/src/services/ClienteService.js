import apiClient from '@/plugins/axios';

class ClienteService {
    constructor() {
        this.api = apiClient;
    }

    // Obtener todos los clientes
    async getAll() {
        try {
            const response = await this.api.get('/cliente');
            console.log('Cliente API response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error al obtener clientes:', error);
            throw error;
        }
    }

    // Obtener cliente por ID
    async getById(id) {
        try {
            const response = await this.api.get(`/cliente/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error al obtener cliente:', error);
            throw error;
        }
    }

    // Crear cliente
    async create(clienteData) {
        try {
            const response = await this.api.post('/cliente', clienteData);
            return response.data;
        } catch (error) {
            console.error('Error al crear cliente:', error.response?.data || error.message);
            throw error;
        }
    }

    // Actualizar cliente
    async update(id, clienteData) {
        try {
            console.log('ClienteService: Actualizando cliente ID:', id);
            console.log('ClienteService: Datos enviados:', clienteData);
            const response = await this.api.put(`/cliente/${id}`, clienteData);
            console.log('ClienteService: Respuesta de actualización:', response.data);
            return response.data;
        } catch (error) {
            console.error('ClienteService: Error al actualizar cliente:', error.response?.data || error.message);
            throw error;
        }
    }

    // Eliminar cliente
    async delete(id) {
        try {
            console.log('ClienteService: Eliminando cliente ID:', id);
            const response = await this.api.delete(`/cliente/${id}`);
            console.log('ClienteService: Respuesta de eliminación:', response.data);
            return response.data;
        } catch (error) {
            console.error('ClienteService: Error al eliminar cliente:', error.response?.data || error.message);
            throw error;
        }
    }
}

export default new ClienteService();

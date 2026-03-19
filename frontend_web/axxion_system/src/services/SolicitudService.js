import apiClient from '@/plugins/axios';

class SolicitudService {
    constructor() {
        this.api = apiClient;
    }

    // Obtener todas las solicitudes
    async getAll() {
        try {
            const response = await this.api.get('/solicitud');
            return response.data;
        } catch (error) {
            console.error('Error fetching solicitudes:', error);
            throw error;
        }
    }

    // Obtener solicitud por ID
    async getById(id) {
        try {
            const response = await this.api.get(`/solicitud/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching solicitud:', error);
            throw error;
        }
    }

    // Crear solicitud
    async create(data) {
        try {
            const response = await this.api.post('/solicitud', data);
            return response.data;
        } catch (error) {
            console.error('Error creating solicitud:', error.response?.data || error.message);
            throw error;
        }
    }
}

export default new SolicitudService();

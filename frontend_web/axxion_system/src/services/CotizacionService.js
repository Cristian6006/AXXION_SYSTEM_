import apiClient from '@/plugins/axios';

class CotizacionService {
    constructor() {
        this.api = apiClient;
    }

    async createCotizacion(data) {
        try {
            const response = await this.api.post('/cotizacion', data);
            return response.data;
        } catch (error) {
            console.error('Error creating cotizacion:', error.response?.data || error.message);
            throw error;
        }
    }

    async getCotizaciones() {
        try {
            const response = await this.api.get('/cotizacion');
            return response.data;
        } catch (error) {
            console.error('Error fetching cotizaciones:', error.response?.data || error.message);
            throw error;
        }
    }

    async getCotizacion(id) {
        try {
            const response = await this.api.get(`/cotizacion/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching cotizacion:', error.response?.data || error.message);
            throw error;
        }
    }
}

export default new CotizacionService();

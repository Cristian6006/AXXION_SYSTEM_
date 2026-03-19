import apiClient from '@/plugins/axios';

class AlertService {
    constructor() {
        this.api = apiClient;
    }
    async getAlerts() {
        try {
            const response = await this.api.get("/alertas");
            console.log("Alertas API response:", response.data);
            return response.data.alertas || [];
        } catch (error) {
            console.error("Error al obtener alertas:", error.response?.data || error.message);
            throw error;
        }
    }
    
}

export default new AlertService();

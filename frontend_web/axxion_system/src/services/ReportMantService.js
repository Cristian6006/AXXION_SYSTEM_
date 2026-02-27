import apiClient from "@/plugins/axios";

class ReportMantService {
    constructor (){
        this.api = apiClient;
    }
    async getReporteEficiencia(){
        try {
            const response = await this.api.get('/reportes/reporteEficiencia')
            console.log('Report API response: ', response.data)
            return response.data
        } catch(error) {
            console.error('Error al obtener metricas:', error)
            throw error
        }
    }
    
    async getanalisisFinanciero(){
        try {
            const response = await this.api.get('/reportes/analisisFinanciero')
            console.log('Report API response: ', response.data)
            return response.data
        } catch(error) {
            console.log('Report API response: ', error)
            throw error
        }
    }
}

export default new ReportMantService();
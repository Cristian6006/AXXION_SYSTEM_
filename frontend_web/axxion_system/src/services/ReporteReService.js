import apiClient from "@/plugins/axios";

class ReporteReService {

    async getResumenGeneral() {
        try {
            const response = await apiClient.get('/reportes/resumenGeneral')
            return response.data
        } catch (error) {
            console.error('Error al obtener resumen:', error)
            throw error
        } 
    }
    async getObtenerRentasAtrasadas() {
        try {
            const response = await apiClient.get('/reportes/obtenerRentasAtrasadas')
            return response.data
        } catch (error) {
            console.error('Error al obtener rentas atrasadas:', error)
            throw error
        } 
    }
    async getIngresosPorMes() {
        try {
            const response = await apiClient.get('/reportes/ingresosPorMes')
            return response.data
        } catch (error) {
            console.error('Error al obtener Ingresos mensuales:', error)
            throw error
        } 
    }
    async getTopEquiposRentados() {
        try {
            const response = await apiClient.get('/reportes/topEquiposRentados')
            return response.data
        } catch (error) {
            console.error('Error al obtener métricas:', error)
            throw error
        } 
    }
    async getEstadoInventario() {
        try {
            const response = await apiClient.get('/reportes/estadoInventario')
            return response.data
        } catch (error) {
            console.error('Error al obtener métricas:', error)
            throw error
        } 
    }
    async getRoiPorEquipo() {
        try {
            const response = await apiClient.get('/reportes/roiPorEquipo')
            return response.data
        } catch (error) {
            console.error('Error al obtener métricas:', error)
            throw error
        } 
    }
}

export default new ReporteReService();
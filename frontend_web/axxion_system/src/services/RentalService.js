import apiClient from '@/plugins/axios';  

class RentalService {
    constructor (){
        this.api = apiClient;
    }
    async getRental(){
        try {
            const response = await this.api.get('/renta')
            console.log('Rental API response: ', response.data)
            return response.data
        } catch (error) {
            console.error('Error al obtener rentas:', error);
            throw error;
        }
    }
    async getRentalById(id) {
        try {
            const response = await this.api.get(`/renta/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error al obtener renta:', error);
            throw error;
        }
    }
    async createRental(rentalData) {
        try {
            const response = await this.api.post('/renta', rentalData);
            return response.data;
        } catch (error) {
            console.error('Error al crear renta:', error.response?.data || error.message);
            throw error;
        }
    }
    async updateRental(id, rentalData) {
        try {
            console.log('InventoryService: Actualizando renta ID:', id);
            console.log('InventoryService: Datos enviados:', rentalData);
            const response = await this.api.put(`/renta/${id}`, rentalData);
            console.log('InventoryService: Respuesta de actualización:', response.data);
            return response.data;
        } catch (error) {
            console.error('InventoryService: Error al actualizar renta:', error.response?.data || error.message);
            throw error;
        }
    }
    async deleteRental(id) {
        try {
            console.log('InventoryService: Eliminando renta ID:', id);
            const response = await this.api.delete(`/renta/${id}`);
            console.log('InventoryService: Respuesta de eliminación:', response.data);
            return response.data;
        } catch (error) {
            console.error('InventoryService: Error al eliminar renta:', error.response?.data || error.message);
            throw error;
        }
    }
}


export default new RentalService();
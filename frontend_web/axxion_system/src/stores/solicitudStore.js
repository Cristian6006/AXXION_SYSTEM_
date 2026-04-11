import { defineStore } from 'pinia';
import SolicitudService from '@/services/SolicitudService';

export const useSolicitudStore = defineStore('solicitud', {
    state: () => ({
        solicitudes: [],
        loading: false,
        error: null,
        currentSolicitud: null
    }),

    getters: {
        allSolicitudes: (state) => state.solicitudes,
        getSolicitudById: (state) => (id) => state.solicitudes.find(s => s.id === id),
        totalSolicitudes: (state) => state.solicitudes.length
    },

    actions: {
        async fetchSolicitudes() {
            this.loading = true;
            this.error = null;
            try {
                const response = await SolicitudService.getAll();
                this.solicitudes = response.solicitudes || [];
            } catch (err) {
                this.error = err.response?.data?.error || 'Error al cargar las solicitudes';
                console.error('Error fetching solicitudes:', err);
            } finally {
                this.loading = false;
            }
        },

        async fetchSolicitudById(id) {
            this.loading = true;
            try {
                const response = await SolicitudService.getById(id);
                this.currentSolicitud = response.solicitud;
                return response.solicitud;
            } catch (err) {
                this.error = err.response?.data?.error || 'Error al obtener la solicitud';
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async createSolicitud(solicitudData) {
            this.loading = true;
            try {
                const response = await SolicitudService.create(solicitudData);
                await this.fetchSolicitudes();
                return response;
            } catch (err) {
                this.error = err.response?.data?.errors || 'Error al crear la solicitud';
                throw err;
            } finally {
                this.loading = false;
            }
        },

        async convertToRental(id, rentalData) {
            this.loading = true;
            try {
                const response = await SolicitudService.convertToRental(id, rentalData);
                await this.fetchSolicitudes(); // Refrescar para ver el cambio de estado
                return response;
            } catch (err) {
                this.error = err.response?.data?.errors || err.response?.data?.error || 'Error al convertir a renta';
                throw err;
            } finally {
                this.loading = false;
            }
        }
    }
});

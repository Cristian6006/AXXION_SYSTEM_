import { defineStore } from 'pinia';
import RentalService from '@/services/RentalService';
import ClienteService from '@/services/ClienteService';


export const useRentalStore = defineStore('rentals', {
    state: () => ({
        rentals: [],
        clientes: [],
        loading: false,
        error: null,
        showModal: false,
        modalMode: 'add',
        selectedRental: null, 
    }),

    getters: {
        clienteOptions: (state) => {
            return state.clientes.map(c => ({
                value: c.id,
                label: `${c.nombre} ${c.nombre2 || ''} ${c.apellido1 || ''} ${c.apellido2 || ''}`.trim(),
            }));
        },
    },

actions: {
    async fetchRentals() {
        this.loading = true;
        this.error = null;
        try {
            const data = await RentalService.getRental();
            const extractArrayFromResponse = (obj) => {
                if (Array.isArray(obj)) return obj;
                if (!obj || typeof obj !== 'object') return [];
                for (const key of Object.keys(obj)) {
                    if (Array.isArray(obj[key])) return obj[key];
                }
                if ('id' in obj && 'cliente_id' in obj) return [obj];
                for (const key of Object.keys(obj)) {
                    const val = obj[key];
                    if (val && typeof val === 'object') {
                        for (const subKey of Object.keys(val)) {
                            if (Array.isArray(val[subKey])) return val[subKey];
                        }
                    }
                }
            return [];
            };
        this.rentals = extractArrayFromResponse(data);
        } catch (err) {
            console.error('Error al cargar rentas:', err);
            this.error = 'No se pudieron cargar las rentas.';
        } finally {
            this.loading = false;
        }
    },
    async fetchClientes() {
        try {
            const data = await ClienteService.getAll();
            this.clientes = Array.isArray(data) ? data : data.data || [];
        } catch (error) {
            console.error('Error cargando clientes:', error);
        }
    },
    openAddModal() {
        this.modalMode = 'add';
        this.selectedRental = {
            cliente_id: '', estado_renta: 'Programada', fecha_inicio: '',
            fecha_fin_prevista: '', monto_total_renta: '', deposito_garantia: '', notas: '',
        };
        this.showModal = true;
    },
    openEditModal(rental) {
        this.modalMode = 'edit';
        this.selectedRental = { 
            ...rental,
            fecha_inicio: rental.fecha_inicio ? rental.fecha_inicio.replace(' ', 'T') : '',
            fecha_fin_prevista: rental.fecha_fin_prevista ? rental.fecha_fin_prevista.replace(' ', 'T') : '',
            fecha_devolucion_real: rental.fecha_devolucion_real ? rental.fecha_devolucion_real.replace(' ', 'T') : '',
        };
        this.showModal = true;
    },
    openDeleteModal(rental) {
        this.modalMode = 'delete';
        this.selectedRental = rental;
        this.showModal = true;
    },
    closeModal() {
        this.showModal = false;
        this.selectedRental = null;
    },
    async saveRental(payloadFromModal) {
        const normalizePayloadForAPI = (payload) => ({
            cliente_id: payload.cliente_id ? Number(payload.cliente_id) : null,
            fecha_inicio: payload.fecha_inicio ? payload.fecha_inicio.replace('T', ' ') + ':00' : null,
            fecha_fin_prevista: payload.fecha_fin_prevista ? payload.fecha_fin_prevista.replace('T', ' ') + ':00' : null,
            estado_renta: payload.estado_renta || 'Programada',
            monto_total_renta: payload.monto_total_renta ? Number(payload.monto_total_renta) : 0,
            deposito_garantia: payload.deposito_garantia ? Number(payload.deposito_garantia) : 0,
            notas: payload.notas || '',
        });
    
        const normalized = normalizePayloadForAPI(payloadFromModal);
    
        try {
        if (this.modalMode === 'add') {
            await RentalService.createRental(normalized);
        } else if (this.modalMode === 'edit') {
            await RentalService.updateRental(this.selectedRental.id, normalized);
        }
        await this.fetchRentals();
        this.closeModal();
        } catch (err) {
            console.error('Error guardando la renta:', err);
            alert('No se pudo guardar la renta.');
        }
    },
    async deleteRental() {
        if (this.modalMode !== 'delete' || !this.selectedRental) return;
        try {
            await RentalService.deleteRental(this.selectedRental.id);
            this.rentals = this.rentals.filter(r => r.id !== this.selectedRental.id);
            this.closeModal();
        } catch (err) {
            console.error('Error eliminando la renta:', err);
            alert('No se pudo eliminar la renta.');
        }
    },
    },
});
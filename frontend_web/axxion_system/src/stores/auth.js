// stores/auth.js
import { defineStore } from 'pinia';
import axios from '@/plugins/axios';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
    user: null,
    isRefreshing: false,
    refreshSubscribers: [],
    authReady: false,
  }),
  getters: {
    isAuthenticated: (state) => !!state.accessToken,
  },
  actions: {
    async tryToRefresh() {
      if (this.authReady) return;
      try {
        await this.refreshAccessToken();
      } catch (error) {
        this.accessToken = null;
      } finally {
        this.authReady = true;
      }
    },

    async login(email, password) {
      try {
        const response = await axios.post('/auth/login', { email, password });
        const authData = response.data.data;
        // Usamos el objeto authData para asignar los valores
        this.accessToken = authData.access_token;
        this.user = authData.user;

      } catch (error) {
        console.log(`[Store Action - UID: ${this.$id}] Limpiando estado por error.`);
        this.accessToken = null;
        this.user = null;
        throw error;
      }
    },


    async logout() {
      try {
        await axios.post('/auth/logout');
      } catch (error) {
        console.error('El logout en el servidor falló, pero se limpiará la sesión localmente.', error);
      } finally {
        this.accessToken = null;
        this.user = null;
        this.authReady = false;
      }
    },

    async refreshAccessToken() {
      if (this.isRefreshing) {
        return new Promise((resolve) => {
          this.refreshSubscribers.push((token) => resolve(token));
        });
      }
      this.isRefreshing = true;

      try {
        const response = await axios.post('/auth/refresh');
        const authData = response.data.data || response.data;

        this.accessToken = authData.access_token;
        // Actualizamos el usuario con los datos que nos llegan.
        this.user = authData.user;

        this.refreshSubscribers.forEach(callback => callback(this.accessToken));
        return this.accessToken;

      } catch (error) {

        this.accessToken = null;
        this.user = null;
        this.authReady = false;
        throw new Error('No se pudo refrescar el token');

      } finally {
        this.isRefreshing = false;
        this.refreshSubscribers = [];
      }
    },
  },
});

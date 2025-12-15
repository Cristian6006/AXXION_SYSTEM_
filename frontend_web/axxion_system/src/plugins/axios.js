// plugins/axios.js
import axios from 'axios'
import { useAuth } from '@/composables/useAuth'
import { useAuthStore } from '@/stores/auth';

// Configuración base
const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8000/api', 
  withCredentials: true,
  headers: {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
  }
});

/**
 * Interceptor de Request - Añade el Access Token a nuestra instancia local
 */
apiClient.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore();
    if (authStore.accessToken) {
      config.headers.Authorization = `Bearer ${authStore.accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

/**
 * Interceptor de Response - Maneja errores 401 en nuestra instancia local
 */
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const authStore = useAuthStore();

    if (error.response.status === 401 && !originalRequest._retry) {
      if (originalRequest.url.includes('/auth/login') || originalRequest.url.includes('/auth/refresh')) {
        throw error;
      }
      
      originalRequest._retry = true;

      try {
        console.log('Interceptor: Access token expirado. Intentando refrescar...');
        await authStore.refreshAccessToken(); 

        originalRequest.headers.Authorization = `Bearer ${authStore.accessToken}`;
        
        // Usamos la misma instancia 'apiClient' que creó el interceptor.
        return apiClient(originalRequest);

      } catch (refreshError) {
        console.log('Interceptor: Refresh token inválido. Deslogueando...');
        await authStore.logout();
        window.location.href = '/login'; 
        throw error;
      }
    }
    
    throw error;
  }
);

export default apiClient;
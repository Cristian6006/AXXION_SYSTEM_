<template>
  <div v-if="showDebug" class="debug-panel">
    <div class="debug-header">
      <h4>🔍 Debug - Información del Usuario</h4>
      <button @click="toggleDebug" class="close-btn">✕</button>
    </div>
    <div class="debug-content">
      <div class="debug-section">
        <strong>Autenticado:</strong> {{ authStore.isAuthenticated }}
      </div>
      <div class="debug-section">
        <strong>Token:</strong> {{ authStore.token ? 'Presente' : 'No presente' }}
      </div>
      <div class="debug-section">
        <strong>Usuario:</strong>
        <pre>{{ JSON.stringify(authStore.user, null, 2) }}</pre>
      </div>
      <div class="debug-section">
        <strong>Roles:</strong>
        <ul v-if="authStore.user?.roles">
          <li v-for="role in authStore.user.roles" :key="role.id || role">
            {{ typeof role === 'string' ? role : `${role.codigo} - ${role.nombre}` }}
          </li>
        </ul>
        <span v-else>No hay roles</span>
      </div>
      <div class="debug-section">
        <strong>Verificaciones de rol:</strong>
        <ul>
          <li>hasRole('ADMIN'): {{ authStore.hasRole('ADMIN') }}</li>
          <li>hasRole('TECNICO'): {{ authStore.hasRole('TECNICO') }}</li>
          <li>hasRole('AUXILIAR'): {{ authStore.hasRole('AUXILIAR') }}</li>
        </ul>
      </div>
      <div class="debug-actions">
        <button @click="refreshAuth" class="refresh-btn">🔄 Refrescar Auth</button>
        <button @click="clearAuth" class="clear-btn">🗑️ Limpiar Auth</button>
      </div>
    </div>
  </div>
  <button v-else @click="toggleDebug" class="debug-toggle">🔍 Debug</button>
</template>

<script>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

export default {
  /**
   * Componente DebugUserInfo.
   * 
   * Panel flotante para desarrolladores que muestra el estado actual de la autenticación.
   * Útil para verificar roles, tokens y datos del usuario en tiempo real.
   * Solo visible si se activa manualmente (o configurado para dev).
   */
  name: 'DebugUserInfo',
  setup() {
    const authStore = useAuthStore();
    const showDebug = ref(false);

    // Alternar visibilidad del panel
    const toggleDebug = () => {
      showDebug.value = !showDebug.value;
    };

    // Forzar verificación de autenticación con el backend
    const refreshAuth = () => {
      authStore.checkAuth();
    };

    // Cerrar sesión y limpiar estado local
    const clearAuth = () => {
      authStore.logout();
    };

    return {
      authStore,
      showDebug,
      toggleDebug,
      refreshAuth,
      clearAuth
    };
  }
};
</script>

<style scoped>
.debug-panel {
  position: fixed;
  top: 10px;
  right: 10px;
  width: 400px;
  max-height: 80vh;
  background: white;
  border: 2px solid #007bff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  z-index: 10000;
  font-family: monospace;
  font-size: 12px;
  overflow-y: auto;
}

.debug-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #007bff;
  color: white;
  padding: 8px 12px;
  border-radius: 6px 6px 0 0;
}

.debug-header h4 {
  margin: 0;
  font-size: 14px;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 16px;
  padding: 0;
  width: 20px;
  height: 20px;
}

.debug-content {
  padding: 12px;
}

.debug-section {
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.debug-section:last-child {
  border-bottom: none;
}

.debug-section strong {
  color: #007bff;
  display: block;
  margin-bottom: 4px;
}

.debug-section pre {
  background: #f8f9fa;
  padding: 8px;
  border-radius: 4px;
  overflow-x: auto;
  margin: 0;
  font-size: 11px;
}

.debug-section ul {
  margin: 4px 0;
  padding-left: 16px;
}

.debug-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

.refresh-btn, .clear-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 11px;
}

.refresh-btn {
  background: #28a745;
  color: white;
}

.clear-btn {
  background: #dc3545;
  color: white;
}

.debug-toggle {
  position: fixed;
  top: 10px;
  right: 10px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 12px;
  z-index: 9999;
}

.debug-toggle:hover {
  background: #0056b3;
}
</style>

<template>
<div>
<header class="flex items-center justify-between border-b pb-4 mb-6 flex-shrink-0">
    <h1 class="text-3xl font-semibold text-gray-800">AXXION SYSTEM</h1>
    <div class="flex items-center space-x-4">
        <div v-if="authStore.isAuthenticated">
            <span class="text-lg font-medium text-gray-700">{{ userRoleName }}</span>
            <font-awesome-icon icon="fa-solid fa-user-tie" fade size="xl" style="color: #63E6BE;" />
        </div>
        <div v-else>
            <span class="text-lg font-medium text-gray-700">no hay roles</span>
        </div>
            <svg @click="logout" class="h-8 w-8 text-red-500 cursor-pointer" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg>
        </div>


</header>
</div>

</template>
<script setup>
import { ref, onMounted, computed } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';


/**
 * Componente headerP.
 * 
 * Encabezado principal de la aplicación (versión alternativa o específica).
 * Muestra el título del sistema, el rol del usuario actual y el botón de cerrar sesión.
 */

const router = useRouter();
const authStore = useAuthStore();

const user = computed(() => authStore.user);

// Esto hace la plantilla más limpia y maneja el caso de que no haya usuario o roles.
const userRoleName = computed(() => {
  // Primero, nos aseguramos de que 'user' y 'user.roles' existan y que 'roles' sea un array con al menos un elemento.
    if (user.value && user.value.roles && user.value.roles.length > 0) {
    // Si todo está bien, devolvemos el nombre del primer rol.
        return user.value.roles[0].nombre;
    }
  // Si algo falla, devolvemos un texto por defecto.
    return 'Sin rol asignado';
});

const logout = async () => {
    try {
        // Espera a que la acción de logout del store se complete.
        await authStore.logout();
        
        // Una vez completado el logout, redirige al usuario a la página de login.
        router.push({ name: 'Login' }); 
    } catch (error) {
        console.error("Ocurrió un error durante el logout:", error);
        router.push({ name: 'Login' });
    }
    // Cierra la sesión del usuario y redirige al login.
    const logout = () => {
        authStore.logout();
        router.push('/login');
    };



onMounted(() => {
    user.value = getuserdata();
    console.log('User data:', user.value);
});

}

</script>


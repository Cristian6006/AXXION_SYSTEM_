<template>
  <div>
    <div class="app flex h-screen">
      <SideBar/>
      <RouterView></RouterView>
      <main class="container h-screen p-4 flex-1 overflow-y-auto">
        <div class="rounded-lg flex-col">
          <headerP></headerP>
          <div class="p-4 sm:p-6 lg:p-8 bg-white dark:bg-gray-800 rounded-lg shadow-sm">
            <div class="mb-4">
              <h1 class="text-2xl font-bold text-gray-900 dark:text-white">Gestión de Usuarios</h1>
              <p class="text-gray-500 dark:text-gray-400">Administra los usuarios del sistema</p>
              <br>
              <!--CONTROLES DE BÚSQUEDA Y FILTRO -->
              <div class="flex flex-col md:flex-row gap-4 w-full">
                <!-- Barra de Búsqueda -->
                <div class="relative w-full md:w-64">
                  <div class="absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none">
                    <!-- Icono de Lupa Agregado -->
                    <font-awesome-icon icon="fa-solid fa-magnifying-glass" class="text-gray-400" />
                  </div>
                  <input 
                    v-model="searchQuery" 
                    type="text" 
                    class="block w-full p-2 ps-10 text-sm text-gray-900 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white" 
                    placeholder="Buscar por nombre o email..." 
                  />
                </div>
                <!-- Select de Rol -->
                <div class="float-right">
                  <select 
                    v-model="selectedRole" 
                    class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full md:w-auto p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white"
                  >
                    <option value="">Todos los roles</option>
                    <option 
                      v-for="rol in userStore.roles" 
                      :key="rol.id" 
                      :value="rol.nombre"
                    >
                      {{ rol.nombre }}
                    </option>
                  </select>
                </div>
              </div>
            </div>
            <!-- Alertas de Error -->
            <div class="flex mb-4" v-if="userStore.error">
              <FwbAlert :color="'red'" :dismissible="true" class="w-full">
                {{ userStore.error }}
              </FwbAlert>
            </div>
            
            <br>
            
            <!-- Botones de Acción -->
            <div class="flex gap-4 mb-6">
              <fwb-button gradient="green-blue" outline size="lg" hover:bg-sky-700 @click="showAddModal">
                <font-awesome-icon icon="fa-solid fa-user-plus" class="mr-2" style="color: #63E6BE;" />
                Agregar Usuario
              </fwb-button>
              <fwb-button gradient="red-yellow" outline size="lg" @click="showDeleteModal(null)">
                <font-awesome-icon icon="fa-solid fa-user-minus" class="mr-2" style="color: #d60000;"/>
                Eliminar Usuario
              </fwb-button>
            </div>
          </div> 
          <br>
            <!-- Loader -->
            <div v-if="isLoading" class="flex justify-center py-10">
              <span class="loader" aria-hidden="true"></span> 
            </div>
            
            <!-- LISTA DE USUARIOS  -->
            <div v-else class="flex flex-wrap gap-4">
                <UserCard 
                  v-for="user in filteredUsers" 
                  :key="user.id" 
                  :user="user" 
                  @delete-user="showDeleteModal"
                  @update-user="showUpdateModal"
                />

                <!-- Mensaje si no hay resultados -->
                <div v-if="filteredUsers.length === 0" class="w-full text-center py-10 text-gray-500">
                  No se encontraron usuarios con esos criterios.
                </div>
            </div>
        </div> 
      </main>
    <!-- MODAL -->
      <fwb-modal v-if="isShowModal" @close="closeModal" size="4xl">
        <template #header>
          <div class="flex items-center text-lg font-semibold text-gray-900 dark:text-white">
            <font-awesome-icon 
              :icon="modalMode === 'add' ? 'fa-solid fa-user-plus' : (modalMode === 'update' ? 'fa-solid fa-user-pen' : 'fa-solid fa-user-minus')" 
              :class="modalMode === 'add' ? 'mr-2 text-green-600' : (modalMode === 'update' ? 'mr-2 text-blue-600' : 'mr-2 text-red-600')"
            />
            {{ modalMode === 'add' ? 'Agregar Nuevo Usuario' : (modalMode === 'update' ? 'Actualizar Usuario' : 'Eliminar Usuario') }}
          </div>
        </template>
        <template #body>
          <UserForm 
            :mode="modalMode"
            :selectedUser="selectedUser"
            @success="handleUserSuccess"
            @cancel="closeModal"
          />
        </template>
      </fwb-modal>
    </div>
  <Footer/>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useUserStore } from '@/stores/user.js';
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import Footer from '@/components/Footer.vue';
import UserCard from '@/components/UserCard.vue';
import UserForm from '@/components/UserForm.vue';
import { FwbAlert, FwbButton, FwbModal } from 'flowbite-vue';

// Store
const userStore = useUserStore();

//  ESTADO DEL FILTRO
const searchQuery = ref('');
const selectedRole = ref('');

// Estado del Modal
const isShowModal = ref(false);
const modalMode = ref('add'); 
const selectedUser = ref(null);

// Computed directos del store
const users = computed(() => userStore.users);
const isLoading = computed(() => userStore.isLoading);

// Esta variable observa 'users', 'searchQuery' y 'selectedRole'.
// Si cualquiera cambia, se actualiza automáticamente.
const filteredUsers = computed(() => {
  // Obtenemos la lista base (asegurando que sea un array)
  let result = users.value || [];

  // Filtro: Texto (Búsqueda)
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    result = result.filter(user => 
      (user.nombre && user.nombre.toLowerCase().includes(query)) || 
      (user.nombre2 && user.nombre2.toLowerCase().includes(query)) || 
      (user.apellido1 && user.apellido1.toLowerCase().includes(query)) || 
      (user.apellido2 && user.apellido2.toLowerCase().includes(query)) || 
      (user.email && user.email.toLowerCase().includes(query))
    );
  }

  // Filtro: Rol (Dropdown)
  if (selectedRole.value) {
    result = result.filter(user => {
      // Verificamos si el usuario tiene roles. 
      // NOTA: Revisa si tu API devuelve 'user.roles' o 'user.rol'.
      // Pondré 'roles' que es el estándar de Laravel/bancos de datos relacionales.
      const userRoles = user.roles || user.rol || [];
      
      if (!Array.isArray(userRoles) || userRoles.length === 0) return false;

      // Usamos .some() buscando la propiedad 'nombre'
      return userRoles.some(r => r.nombre === selectedRole.value);
    });
  }

  return result;
});

// --- Funciones del Modal ---
function closeModal () {
  isShowModal.value = false;
  modalMode.value = 'add';
  selectedUser.value = null;
}

function showAddModal () {
  modalMode.value = 'add';
  selectedUser.value = null;
  isShowModal.value = true;
}

function showDeleteModal (user) {
  modalMode.value = 'delete';
  selectedUser.value = user;
  isShowModal.value = true;
}

function showUpdateModal (user) {
  modalMode.value = 'update';
  selectedUser.value = user;
  isShowModal.value = true;
}

//  CARGA DE DATOS 
const getuserdata = async () => {
  try {
    // Simplemente cargamos los datos al store.
    await userStore.fetchUsers(),
      userStore.fetchRoles()

  } catch (e) {
    console.error('Error al cargar usuarios:', e);
    // Asegúrate de que tu store maneje el error, o asígnalo aquí si es necesario
    userStore.error = e.message || 'Error al cargar usuarios';
  }
}

const handleUserSuccess = () => {
  closeModal();
  getuserdata(); 
};

onMounted(() => {
  getuserdata();
});
</script>

<style scoped>
.loader {
  width: 48px;
  height: 48px;
  border: 5px solid #FFF;
  border-bottom-color: #3B82F6; 
  border-radius: 50%;
  display: inline-block;
  box-sizing: border-box;
  animation: rotation 1s linear infinite;
}

@keyframes rotation {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>

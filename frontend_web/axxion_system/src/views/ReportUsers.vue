<template>
  <div class="App flex">
    <SideBar />
    <main class="container h-screen p-4 flex-1 overflow-y-auto">
      <headerP />
      <div class="flex flex-row justify-between">
        <h1 class="text-3xl font-bold mb-6 text-black">Reportes de Usuarios</h1>
        <section>
          <div class="flex justify-end mb-4">
            <WorksheetUser
            filename="usuario.xlsx"
            sheetName="usuario"
            :data="usuario"
            :columns="[
              {key: 'id', label: 'ID'},
              {key: 'email', label: 'Correo'},
              {key: 'telefono', label: 'Telefono'},
              {key: 'departamento', label: 'Departamento'},
              {key: 'estado', label: 'Estado'}
            ]"
            />
          </div>
        </section>
      </div>
      <!-- Tarjetas -->
      <section class="flex flex-wrap gap-6 mb-6">
        <fwb-card class="w-sm flex-1 min-w-[300px]">
          <div class="p-5">
            <h5 class="mb-2 text-2xl font-bold text-gray-900 dark:text-white">
              Empleados Totales
            </h5>
            <p class="text-4xl font-extrabold text-emerald-600 dark:text-emerald-400">{{ empleadosTotales }}</p>
          </div>
        </fwb-card>

        <fwb-card class="w-sm flex-1 min-w-[300px]">
          <div class="p-5">
            <h5 class="mb-2 text-2xl font-bold text-gray-900 dark:text-white">
              Estado
            </h5>
            <ul>
              <li class="text-4xl font-extrabold text-blue-600 dark:text-blue-400" v-for="(cantidad, estado) in empleadosPorEstado" :key="estado">
                <span class="font-medium">{{ estado }}:</span> {{ cantidad }}
              </li>
            </ul>
          </div>
        </fwb-card>
      </section>
    <br><br>
      <!-- Tabla -->
      <section class="bg-white shadow rounded-lg overflow-x-auto">
        <table class="min-w-full text-sm text-left text-gray-700">
          <thead class="bg-gray-200 text-gray-900">
            <tr>
              <th class="px-4 py-2">Usuario</th>
              <th class="px-4 py-2">Nombre</th>
              <th class="px-4 py-2">Segundo Nombre</th>
              <th class="px-4 py-2">Apellido 1</th>
              <th class="px-4 py-2">Apellido 2</th>
              <th class="px-4 py-2">Email</th>
              <th class="px-4 py-2">Teléfono</th>
              <th class="px-4 py-2">Departamento</th>
              <th class="px-4 py-2">Estado</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="user in usuarios"
              :key="user.id"
              class="border-b hover:bg-gray-100"
            >
              <td class="px-4 py-2">{{ user.nombre_usuario }}</td>
              <td class="px-4 py-2">{{ user.nombre }}</td>
              <td class="px-4 py-2">{{ user.nombre2 }}</td>
              <td class="px-4 py-2">{{ user.apellido1 }}</td>
              <td class="px-4 py-2">{{ user.apellido2 }}</td>
              <td class="px-4 py-2">{{ user.email }}</td>
              <td class="px-4 py-2">{{ user.telefono }}</td>
              <td class="px-4 py-2">{{ user.departamento }}</td>
              <td class="px-4 py-2">
                <span
                  :class="{
                    'text-green-600 font-bold': user.estado === 'Activo',
                    'text-red-600 font-bold': user.estado === 'Inactivo',
                    'text-yellow-600 font-bold': user.estado === 'Bloqueado'
                  }"
                >
                  {{ user.estado }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import SideBar from '@/components/SideBar.vue'
import headerP from '@/components/headerP.vue'
import UserService from '@/services/UserService'
import { FwbCard } from 'flowbite-vue'
import WorksheetUser from '@/components/WorksheetUser.vue'

/**
 * Vista ReportUsers.
 * 
 * Genera reportes sobre los usuarios del sistema.
 * Funcionalidades:
 * - KPIs de empleados totales.
 * - Desglose de empleados por estado (Activo, Inactivo, Bloqueado).
 * - Tabla detallada de usuarios con información de contacto y departamento.
 * - Exportación de la lista de usuarios a Excel mediante WorksheetUser.
 */

const usuario = ref([])
const empleadosTotales = ref(0)
const empleadosPorEstado = ref({})
const usuarios = ref([])

onMounted(async () => {
  try {
    const response = await UserService.getUsers()
    const data = response.data  
    usuarios.value = data
    empleadosTotales.value = data.length
    empleadosPorEstado.value = data.reduce((acc, user) => {
      acc[user.estado] = (acc[user.estado] || 0) + 1
      return acc
    }, {})
  } catch (error) {
    console.error('Error cargando usuarios:', error)
  }
})

</script>

<template>
  <div>
    <div class="app flex">
      <SideBar/>
      <RouterView></RouterView>
      <main class="container h-screen p-4 flex-1 overflow-y-auto">
        <headerP/>
        <section class="grid grid-cols-2 gap-5 justify-between">
          <div>
            <h1 class="text-3xl font-extrabold text-gray-900 tracking-tight">Categorias</h1>
            <p class="text-sm text-slate-500 mt-1 font-medium">Organiza tus categorías en un solo lugar</p>
          </div>
          <fwb-button
            @click="openCreate"
            class="w-auto h-10 col-start-3 cursor-pointer transition delay-150 duration-300 ease-in-out hover:-translate-y-1 hover:scale-110" gradient="green-blue" outline
          >
            <font-awesome-icon icon="fa-solid fa-plus" />
            Agregar Categoria
          </fwb-button>
        </section>
        <br>
        <section class="mb-6 flex gap-4 p-8 rounded-4xl h-auto  bg-[#1e2838]">
          <!-- Input buscador -->
          <div class=" relative flex-1">
            <MagnifyingGlassIcon class="size-6 text-gray-600 absolute left-3 top-1/2 -translate-y-1/2 pointer-events-none"/>
            <input
              v-model="busqueda"
              type="text"
              placeholder="Buscar Categorias"
              class="w-full pl-12 pr-4 py-2 bg-[#364153] text-[#828983] rounded-lg border-[#465263] focus:outline-none focus:border-emerald-500 transition-colors placeholder-gray-500"
            />
          </div>
          <!-- Filtro de Fecha -->
          <div class="relative flex items-center bg-[#364153] border border-gray-200  rounded-lg shadow-sm focus-within:border-emerald-500 focus-within:ring-1 focus-within:ring-emerald-500 transition-colors">
            <input
              v-model="fechaFiltro"
              type="date"
              class="px-4 py-2 bg-transparent text-[#828983]  outline-none w-full md:w-auto"
            />
            <button v-if="fechaFiltro" @click="fechaFiltro = ''" class="pr-3 text-gray-400 hover:text-gray-600" title="Limpiar fecha">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
            </button>
          </div>>
        </section>
        <br>
        <!-- Contenedor de lista vertical -->
        <section class="p-8 bg-[#364153] rounded-4xl h-auto">
          <div class="space-y-4">
            <div
              v-for="cat in listaFiltrada"
              :key="cat?.id"
              class="bg-[#1e2838] rounded-xl shadow-sm border border-gray-200 overflow-hidden grid"
            >
            <!-- Cabecera -->
              <div
                @click="toggleAcordeon(cat.id)"
                class="p-4 flex items-center justify-between cursor-pointer hover:bg-[#364153] transition-colors"
              >
              <!-- Lado Izquierdo Icono + Titulos -->
                <div class="flex items-center gap-4">
                  <!-- Icono  -->
                  <div class="text-gray-400 transition-transform duration-500" :class="{'rotate-180': abiertos.has(cat.id)}">
                    <ChevronDownIcon class="w-4 h-4 text-gray-600"/>
                  </div>
                  <!-- Caja icono  -->
                  <div class="w-12 h-12 bg-[#364153] text-emerald-500 rounded-lg flex items-center justify-center font-bold text-lg">
                    <TagIcon class="size-6" />
                  </div>
                  <div>
                    <h3 class="font-bold text-gray-100 text-lg flex items-center gap-2">
                      {{ cat.nombre }}
                      <!-- insignia gris pequeña -->
                      <span class="text-xs bg-gray-100 text-gray-500 px-2 py-0.5 rounded-full border border-gray-200 font-normal">
                        {{ cat.tipo_categoria || 'Tipo de categoria' }}
                      </span>
                    </h3>
                    <p class="text-gray-500 text-sm mt-0.5">
                      {{ cat.descripcion || 'Descripcion corta de la categoria' }}
                    </p>
                  </div>
                </div>
                <!-- Lado derecho: Metadatos y acciones -->
                <div class="flex items-center gap-6">
                  <!-- Info extra -->
                  <div class="text-right hidden md:block">
                    <div class="text-xs text-gray-400 flex items-center gap-1 justify-end">
                        <HashtagIcon class="size-3 text-gray-600"/>{{ cat.subcategorias?.length || 0 }} subcategorias
                    </div>
                    <div class="text-xs text-gray-400 mt-1 flex items-center gap-1 justify-end">
                      {{ new Date(cat.created_at).toLocaleDateString() }}
                    </div>
                  </div>
                  <!-- Botones CRUD -->
                  <div class="flex items-center gap-2">
                    <button @click.stop="openEdit(cat)" class="p-2 text-gray-400 hover:bg-blue-50 rounded-lg transition">
                      <PencilIcon class="w-4 h-4 text-white hover:text-emerald-500" />
                    </button>
                    <button @click.stop="openDelete(cat)" class="p-2 text-gray-400 hover:bg-red-50 rounded-lg transition">
                      <TrashIcon class="w-4 h-4 text-white hover:text-red-600" />
                    </button>
                  </div>
                </div>
              </div>
              <!-- Cuerpo desplegable (subcategorias) -->
              <transition
                enter-active-class="transition-all duration-900 ease-out overflow-hidden"
                enter-from-class="transform opacity-0 -translate-y-2 max-h-0"
                enter-to-class="transform opacity-100 translate-y-0 max-h-[1000px]"

                leave-active-class="transition-all duration-200 ease-in overflow-hidden"
                leave-from-class="transform opacity-100 translate-y-0 max-h-[1000px]"
                leave-to-class="transform opacity-0 -translate-y-2 max-h-0"
              >
                <div
                  v-if="abiertos.has(cat.id)"
                  class="bg-[#1e2838] border-t border-gray-100 p-6"
                >
                  <h4 class="text-xs font-bold text-white uppercase tracking-wider mb-3">
                    Subcategorias
                  </h4>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                      <div
                        v-for="sub in (cat.subcategorias)"
                        :key="sub.id"
                        class="bg-[#364153] border border-gray-200 p-3 rounded-lg flex flex-col md:flex-row items-center gap-3 shadow-sm justify-between"
                      >
                        <div class="w-2 h-2 rounded-full bg-emerald-500"></div>
                        <div class="flex-1">
                          <span class="text-white font-medium">{{ sub.nombre }}</span>
                          <p class="text-gray-400 text-sm mt-0.5">{{ sub.descripcion }}</p>
                        </div>
                        <div class="flex items-center gap-2">
                          <button @click.stop="openEditSub(cat.id, sub)" class="p-2 text-gray-400 hover:bg-blue-50 rounded-lg transition">
                            <PencilIcon class="size-4 text-white hover:text-emerald-500"/>
                          </button>
                          <button @click.stop="openDeleteSub(cat.id, sub)" class="p-2 text-gray-400 hover:bg-red-50 rounded-lg transition">
                            <TrashIcon class="size-4 text-white hover:text-red-600"/>
                          </button>
                        </div>
                      </div>
                    </div>
                    <!-- Mensaje si esta vacio -->
                    <p v-if="!cat.subcategorias?.length" class="text-sm text-gray-300 italic">
                      No hay categorias registradas.
                    </p>
                    <br>
                    <button
                      @click="openCreateSub(cat.id)"
                      class="text-xs bg-[#364153] text-white px-2 py-1 rounded hover:bg-indigo-100 hover:text-black transition"
                    >
                      + Añadir Nueva
                    </button>
                  </div>
                </transition>
              </div>
            </div>
        </section>
        <!-- componente modal -->
          <CategoryModal :show="modal.open" @close="modal.open = false">
            <template #header>
              <!-- TITULOS DINAMICOS -->
              <span v-if="modal.type === 'category'">
                Categoria ({{ modal.mode === 'create' ? 'Nueva Categoria' : 'Gestion Categoria' }})
              </span>
              <span v-else class="text-black">
                Subcategoria ({{ modal.mode === 'create' ? 'Crear' : 'Editar' }})
              </span>
            </template>
            <!-- CONTENIDO DINAMICO -->

            <!-- Formulario (Para Crear o Editar) -->
            <div v-if="modal.mode !== 'delete'" class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">Nombre</label>
                <input
                  v-model="modal.data.nombre"
                  type="text"
                  class="mt-1 w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                />
              </div>

              <div v-if="modal.type === 'category'" class="space-y-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700">Tipo de categoria</label>
                    <input
                      v-model="modal.data.tipo_categoria"
                      type="text"
                      class="mt-1 w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                    />
                  </div>
              </div>

                <div>
                  <label class="block text-sm font-medium text-gray-700">Descripción</label>
                  <textarea
                  v-model="modal.data.descripcion"
                  class="mt-1 w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                ></textarea>
                </div>
              </div>

              <!-- Confirmación de Borrado -->
              <div v-else class="text-center">
                <div class="text-5xl mb-4 justify-items-center-safe"><TrashIcon class="size-14 text-black"/></div>
                <h3 class="text-lg font-medium text-gray-900">¿Estás seguro?</h3>
                <p class="text-sm text-gray-500 mt-2">
                  Vas a eliminar la categoría <strong>"{{ modal.data.nombre }}"</strong>.
                  Esta acción no se puede deshacer.
                </p>
              </div>

              <!-- FOOTER CON BOTONES -->
              <div class="mt-6 flex justify-end gap-3">
                <button
                  @click="modal.open = false"
                  class="px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg"
                  :disabled="isSaving"
                >
                  Cancelar
                </button>

                <button
                  @click="handleAction"
                  class="px-4 py-2 text-white rounded-lg shadow-sm"
                  :class="modal.mode === 'delete' ? 'bg-red-600 hover:bg-red-700' : 'bg-emerald-500 hover:bg-emerald-700'"
                  :disabled="isSaving"
                >
                  <span v-if="isSaving" class="animate-spin h-4 w-4 border-2 border-white border-t-transparent rounded-full"></span>
                  <span v-if="isSaving">Procesando...</span>
                  <span v-else>
                    {{ modal.mode === 'create' ? 'Crear' : modal.mode === 'edit' ? 'Guardar Cambios' : 'Sí, Eliminar' }}
                  </span>
                </button>
              </div>
          </CategoryModal>
      </main>
    </div>
    <Footer/>
  </div>
</template>


<script setup>
import SideBar from '@/components/SideBar.vue';
import Footer from '@/components/Footer.vue';
import { FwbButton } from 'flowbite-vue';
import { onMounted } from 'vue';
import { useCategoryStore } from '@/stores/category.js';
import HeaderP from '@/components/headerP.vue';
import { storeToRefs } from 'pinia';
import { ref, computed, reactive } from 'vue';
import { PencilIcon, TrashIcon, ChevronDownIcon, HashtagIcon, MagnifyingGlassIcon, TagIcon } from '@heroicons/vue/24/outline'
import CategoryModal from '@/components/CategoryModal.vue';
import Swal from 'sweetalert2';


const store = useCategoryStore();
const { categories } = storeToRefs(store);
const isSaving = ref(false);

// Control de acordeon
const busqueda = ref('');
const fechaFiltro = ref('');
const abiertos = ref(new Set());

const toggleAcordeon = (id) => {
  if (abiertos.value.has(id)) {
    abiertos.value.delete(id);
  } else {
    abiertos.value.add(id);
  }
}

const formatearFecha = (fecha) => {
  const d = new Date (fecha)
  return d.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// Filtrado
const listaFiltrada = computed(() => {
  let filtradas = categories.value || [];

  // Filtro por de búsqueda de texto
  if (busqueda.value) {
    const texto = busqueda.value.toLowerCase();
    filtradas = filtradas.filter(cat => {
      const nombreMatch = cat.nombre ? cat.nombre.toLowerCase().includes(texto) : false;
      const descMatch = cat.descripcion ? cat.descripcion.toLowerCase().includes(texto) : false;
      return nombreMatch || descMatch;
    });
  }

  // Filtro por fecha seleccionada
  if (fechaFiltro.value) {
    filtradas = filtradas.filter(cat => {
      if (!cat.created_at) return false;
      const d = new Date(cat.created_at);
      const localDateFormated = d.getFullYear() + "-" + String(d.getMonth() + 1).padStart(2, '0') + "-" + String(d.getDate()).padStart(2, '0');

      return localDateFormated === fechaFiltro.value;
    });
  }

  return filtradas;
});

// Estado del modal
const modal = reactive({
  open: false,
  mode: 'create',
  type: 'category',
  parentId: null,
  data: null
});

// Funciones para abrir el modal en diferentes modos

// MODO CREAR CATEGORIA
const openCreate = () => {
  modal.mode = 'create';
  modal.data = {nombre: '', descripcion: '', tipo_categoria: ''};
  modal.open = true;
};

// MODO EDITAR CATEGORIA
const openEdit = (categoria) => {
  modal.mode = 'edit';
  modal.data = { ...categoria };
  modal.open = true;
};

// MODO BORRAR CATEGORIA
const openDelete = (categoria) => {
  modal.mode = 'delete';
  modal.data = { ...categoria };
  modal.open = true;
};

// MODO CREAR SUBCATEGORIA
const openCreateSub = (catId) => {
  modal.mode = 'create';
  modal.type = 'subcategory';
  modal.parentId = catId;
  modal.data = {nombre: '', descripcion: ''}
  modal.open = true;
};

// MODO EDITAR SUBCATEGORIA
const openEditSub = (catId, sub) => {
  modal.mode = 'edit';
  modal.type = 'subcategory';
  modal.parentId = catId;
  modal.data = {...sub};
  modal.open = true;
};

// MODO ELIMINAR SUBCATEGORIA
const openDeleteSub = (catId, sub) => {
  modal.mode = 'delete';
  modal.type = 'subcategory';
  modal.parentId = catId;
  modal.data = {...sub};
  modal.open = true;
}

// Guardar o borrar
const handleAction = async () => {
  isSaving.value = true;
  try {
    // SUBCATEGORIAS
    if (modal.type === 'subcategory') {
      if (modal.mode === 'create') {
        await store.addSubcategory(modal.parentId, {nombre: modal.data.nombre, descripcion: modal.data.descripcion});
      }
      else if (modal.mode === 'edit') {
        const payload = {
          nombre: modal.data.nombre,
          descripcion: modal.data.descripcion
        };
        await store.updateSubcategory(modal.parentId, modal.data.id, payload);
      }
      else if (modal.mode === 'delete') {
        await store.removeSubcategory(modal.parentId, modal.data.id);
      }
    }
    else {
      const payload = {
        nombre: modal.data.nombre,
        descripcion: modal.data.descripcion,
        tipo_categoria: modal.data.tipo_categoria
      };
      if (modal.mode === 'delete') {
        store.deleteCategory(modal.data.id)

      } else if (modal.mode === 'create'){
        await store.addCategory(payload);

      } else if (modal.mode == 'edit') {
        await store.updateCategory(modal.data.id, payload);

      }
    }
    Swal.fire({
      icon: 'success',
      title: '¡Guardado!',
      showConfirmButton: false,
      timer: 2000
    });
    modal.open = false;
  } catch (error) {
      console.error("Fallo la operacion: ", error);
      if (error.response && error.response.status === 422) {
        const errors = error.response.data.errors;
        const primerMensaje = Object.values(errors)[0][0];
        Swal.fire('Faltan datos', primerMensaje, 'warning');
      } else {
        Swal.fire('Error')
      }
      alert("hubo un error al guardar");
    } finally {
      isSaving.value = false;
    }
};

onMounted(() => {
  store.fetchCategories();
});
</script>

<style scoped>
.loader {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    max-width: 6rem;
    margin-top: 3rem;
    margin-bottom: 3rem;
  }
  .loader:before,
  .loader:after {
    content: "";
    position: absolute;
    border-radius: 50%;
    animation: pulsOut 1.8s ease-in-out infinite;
    filter: drop-shadow(0 0 1rem rgba(255, 255, 255, 0.75));
  }
  .loader:before {
    width: 100%;
    padding-bottom: 100%;
    box-shadow: inset 0 0 0 1rem #fff;
    animation-name: pulsIn;
  }
  .loader:after {
    width: calc(100% - 2rem);
    padding-bottom: calc(100% - 2rem);
    box-shadow: 0 0 0 0 #fff;
  }

  @keyframes pulsIn {
    0% {
      box-shadow: inset 0 0 0 1rem #fff;
      opacity: 1;
    }
    50%, 100% {
      box-shadow: inset 0 0 0 0 #fff;
      opacity: 0;
    }
  }

  @keyframes pulsOut {
    0%, 50% {
      box-shadow: 0 0 0 0 #fff;
      opacity: 0;
    }
    100% {
      box-shadow: 0 0 0 1rem #fff;
      opacity: 1;
    }
  }

</style>








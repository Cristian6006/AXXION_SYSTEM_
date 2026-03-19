<template>
    <div>
        <div class="app flex">
            <SideBar />
            <RouterView />
            <main class="container h-screen p-4 flex-1 overflow-y-auto">
                <headerP />
                <!-- Botón agregar -->
                <section class="my-4">
                    <FwbButton class=" transition  duration-300 ease-in-out hover:-translate-y-1 hover:scale-110" @click="showAddModal" color="dark" gradient="green-blue" outline >
                        <font-awesome-icon class="mr-1" icon="fa-solid fa-plus" size="xl" style="color: #ffffff;" /> Agregar Categoría
                    </FwbButton>
                </section>
                    <!-- Lista de Categorías -->
                    <article class="flex flex-wrap flex-row justify-center gap-10">
                        <CategoryCard class=" transition  duration-300 ease-in-out hover:-translate-y-0.5 hover:scale-100 flex-basis-1/30 m-2"
                            v-for="cat in displayedCategories"
                            :key="cat.id"
                            :category="cat"
                            @edit="showEditModal"
                            @delete="showDeleteModal"
                        />
                    </article>
                    <!-- Modal (Agregar/Editar/Eliminar) -->
                    <FwbModal v-if="isShowModal" @close="closeModal">
                        <template #header>
                            <div class="flex items-center text-lg text-amber-50">
                                {{ modalMode === 'add' ? 'Agregar Categoría' :
                                modalMode === 'edit' ? 'Editar Categoría' :
                                'Eliminar Categoría' }}
                            </div>
                        </template>
                        <template #body>
                            <!-- Formulario -->
                            <div v-if="modalMode !== 'delete'" class="text-amber-50">
                                <fwb-input
                                    v-model="form.nombre"
                                    label="Nombre"
                                    placeholder="Ej: Monitores"
                                    required
                                />
                                <br />
                                <fwb-input
                                    v-model="form.descripcion"
                                    label="Descripción"
                                    placeholder="Ej: Pantallas de 24'' y 27''"
                                    required
                                    />
                                    <br />
                                    <fwb-input
                                    v-model="form.tipo_categoria"
                                    label="Tipo de Categoría"
                                    placeholder="Ej: Hardware"
                                    required
                                />
                            </div>
                            <!-- Confirmación Eliminar -->
                            <div v-else>
                                <p class="text-amber-50">
                                    ¿Seguro que deseas eliminar la categoría <b>{{ selectedCategory?.nombre }}</b>?
                                </p>
                            </div>
                        </template>
                        <template #footer>
                            <div class="flex justify-between w-full">
                                <FwbButton @click="closeModal" color="alternative">Cancelar</FwbButton>
                                <FwbButton
                                    v-if="modalMode === 'add' || modalMode === 'edit'"
                                    @click="saveCategory"
                                    color="green"
                                    >Guardar
                                </FwbButton>
                                <FwbButton
                                    v-if="modalMode === 'delete'"
                                    @click="deleteCategory"
                                    color="red"
                                    >Eliminar
                                </FwbButton>
                            </div>
                        </template>
                    </FwbModal>
            </main>
        </div>
        </div>
        
        <Footer />
    
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import Footer from '@/components/Footer.vue';
import CategoryService from '@/services/CategoryService';
import { FwbButton, FwbModal, FwbAlert, FwbInput, FwbCard } from 'flowbite-vue';
import CategoryCard from "@/components/CategoryCard.vue";

// Estado local
const displayedCategories = ref([]);
const isShowModal = ref(false);
const modalMode = ref('add'); // 'add' | 'delete' | 'edit'
const selectedCategory = ref(null);

// Formulario
const nombre = ref('');
const descripcion = ref('');
const tipo_categoria = ref('');

// Estado general
const categorias = ref([]);
const isLoading = ref(false);

// Objeto del formulario
const form = ref({
  nombre: '',
  descripcion: '',
  tipo_categoria: ''
})

// ---- Funciones Modal ----
function closeModal() {
  isShowModal.value = false
  modalMode.value = 'add'
  selectedCategory.value = null
  form.value = { nombre: '', descripcion: '', tipo_categoria: '' }
}

function showAddModal() {
  modalMode.value = 'add'
  form.value = { nombre: '', descripcion: '', tipo_categoria: '' }
  isShowModal.value = true
}

function showEditModal(categoria) {
  modalMode.value = 'edit'
  selectedCategory.value = categoria
  form.value = { ...categoria } // clona los datos
  isShowModal.value = true
}

function showDeleteModal(categoria) {
  modalMode.value = 'delete'
  selectedCategory.value = categoria
  isShowModal.value = true
}

// ---- CRUD ----
async function fetchCategorias() {
  try {
    isLoading.value = true;
    const data = await CategoryService.getCategory();
    categorias.value = data.data ?? data;

    // Animación para mostrarlas poco a poco
    displayedCategories.value = [];
    const delayPerCard = 500;
    for (let i = 0; i < categorias.value.length; i++) {
      await new Promise(resolve => setTimeout(resolve, delayPerCard));
      displayedCategories.value.push(categorias.value[i]);
    }
  } catch (error) {
    console.error('Error al cargar categorías:', error);
  } finally {
    isLoading.value = false;
  }
}

async function saveCategory() {
  try {
    if (modalMode.value === 'add') {
      await CategoryService.createCategory(form.value)
      console.log('Categoría creada:', form.value)
    } else if (modalMode.value === 'edit') {
      await CategoryService.updateCategory(selectedCategory.value.id, form.value)
      console.log('Categoría actualizada:', form.value)
    }
    closeModal()
  } catch (error) {
    console.error('Error en saveCategory:', error)
  }
}

async function deleteCategory() {
  try {
    if (selectedCategory.value) {
      await CategoryService.deleteCategory(selectedCategory.value.id);
      closeModal();
      fetchCategorias();
    }
  } catch (error) {
    console.error('Error al eliminar categoría:', error);
  }
}

// ---- Ciclo de vida ----
onMounted(() => {
  fetchCategorias();
});

</script>



<style scoped>

/* overlay to center the loader */
.overlay {
  position: relative;
   top:100px; right:0; bottom:0; left:0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255,255,255,0.7);
  z-index: 10;
}
</style>
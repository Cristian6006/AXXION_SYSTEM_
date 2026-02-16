import { defineStore } from 'pinia';
import CategoryService from '@/services/CategoryService';

export const useCategoryStore = defineStore('category', {
  state: () => ({
    categories: [],
    loading: false,
    error: null
  }), 

  actions: {
    async fetchCategories() {
      this.loading = true;
      this.error = null;
      try {
        const response = await CategoryService.getAll();
        this.categories = response;
      } catch (err) {
        console.error('Error al cargar categorias:', err);
        this.error = 'Hubo un problema al cargar el catalogo.';
      } finally {
        this.loading = false;
      }
    },
    
    async addCategory(nuevoDato) {
      try {
        const response = await CategoryService.createCategory(nuevoDato);
        
        const nuevoRegistro = response.data.categoria; 
        
        if (nuevoRegistro && nuevoRegistro.id) {
            this.categories.push(nuevoRegistro);
        } else {
            await this.fetchCategories();
        }
      } catch (error) {
        throw error;
      }
    },
    
    async updateCategory(id, datosEditados) {
      await CategoryService.updateCategory(id, datosEditados);
      
      const index = this.categories.findIndex(c => c.id === id);
      if (index !== -1) {
        this.categories[index] = {...this.categories[index], ...datosEditados };
      }
    },
    
    async deleteCategory(id) {
      await CategoryService.deleteCategory(id);
      this.categories = this.categories.filter(c => c.id !== id);
    },
    
    async addSubcategory(parentId, datosSub) {
  try {
    const payload = {
      nombre: datosSub.nombre,
      descripcion: datosSub.descripcion, 
      categorias: [parentId] 
    };

    const response = await CategoryService.createSubcategory(payload);
    
    const nuevaSub = response.data.subcategoria; 

    const padre = this.categories.find(c => c.id === parentId);

    if (padre && nuevaSub && nuevaSub.id) {
        
        if (!padre.subcategorias) padre.subcategorias = [];

        padre.subcategorias.push(nuevaSub);

    } else {
      await this.fetchCategories();
    }

  } catch (error) {
    throw error;
  }
},
    async removeSubcategory(parentId, subId) {
      await CategoryService.deleteSubcategory(subId);
      
      const padre = this.categories.find(c => c.id === parentId);
      if(padre && padre.subcategorias) {
        padre.subcategorias = padre.subcategorias.filter(s = s.id !== subId);
      }
    },
    async updadateSubcategory(parentId, subId, datos) {
      await CategoryService.updateSubcategory(subId, datos);
      
      const padre = this.categories.find(c => c.id === parentId);
      if (padre && padre.subcategorias) {
        const index = padre.subcategorias.findIndex(s => s.id === parentId);
        if (index !== -1) {
          padre.subcategorias[index] = {... padre.subcategorias[index], ...datos};
        }
      }
    }
  }
});

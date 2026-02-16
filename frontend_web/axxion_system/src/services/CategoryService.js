import apiClient from '@/plugins/axios';

export default {
  // CATEGORIAS
  async getAll() {
    const res = await apiClient.get('/categoria');
    return res.data?.categoria ?? res.data;
  },
  async getId() {
    return await apiClient.get(`/categoria/${id}`)
  },
  async createCategory(datos) {
    return await apiClient.post('/categoria', datos); 
  },
  async updateCategory(id, payload) {
    const res = await apiClient.put(`/categoria/${id}`, payload);
    return res.data;
  },
  async deleteCategory(id) {
    const res = await apiClient.delete(`/categoria/${id}`);
    return res.data;
  },
  // SUBCATEGORIAS
  async createSubcategory(datos) {
    return await apiClient.post(`/subcategoria`, datos);
  },
  async updateSubcategory(id, datos) {
    return await apiClient.put(`/subcategoria/${id}`, datos);
  },
  async deleteSubcategory(id) {
    return await apiClient.delete(`/subcategoria/${id}`);
  }
};


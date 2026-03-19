import apiClient from '@/plugins/axios';

export default {
  async create(payload) {              
    const res = await apiClient.post('/subcategoria', payload);
    return res.data;
  },
  async update(id, payload) {
    const res = await apiClient.put(`/subcategoria/${id}`, payload);
    return res.data;
  },
  async delete(id) {
    const res = await apiClient.delete(`/subcategoria/${id}`);
    return res.data;
  },
  async getAll() {
    const res = await apiClient.get('/subcategoria');
    return res.data?.subcategoria ?? res.data;
  }
};




import api from './api';

export const skillService = {
  getAll: (params) => api.get('/skills', { params }),
  getById: (id) => api.get(`/skills/${id}`),
  create: (data) => api.post('/skills', data),
  update: (id, data) => api.put(`/skills/${id}`, data),
  delete: (id) => api.delete(`/skills/${id}`),
  getMySkills: () => api.get('/skills/my-skills'),
};
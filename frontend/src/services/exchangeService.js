import api from './api';

export const exchangeService = {
  create: (data) => api.post('/exchanges', data),
  getReceived: () => api.get('/exchanges/received'),
  getSent: () => api.get('/exchanges/sent'),
  getById: (id) => api.get(`/exchanges/${id}`),
  accept: (id) => api.put(`/exchanges/${id}/accept`),
  refuse: (id) => api.put(`/exchanges/${id}/refuse`),
  complete: (id) => api.put(`/exchanges/${id}/complete`),
  cancel: (id) => api.put(`/exchanges/${id}/cancel`),
};
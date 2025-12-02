import api from './api';

export const messageService = {
  send: (data) => api.post('/messages', data),
  getConversation: (exchangeId) => api.get(`/messages/conversation/${exchangeId}`),
  markAsRead: (id) => api.put(`/messages/${id}/read`),
};
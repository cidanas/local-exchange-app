import api from './api';

export const uploadService = {
  upload: async (files) => {
    try {
      const formData = new FormData();
      for (const f of files) {
        formData.append('files', f);
      }
      
      const res = await api.post('/uploads', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      
      if (!res.data || !Array.isArray(res.data)) {
        throw new Error('Invalid response format from server');
      }
      
      // Construct absolute URLs for returned file paths
      const baseUrl = api.defaults.baseURL; // e.g., http://localhost:8080/api
      const apiBase = baseUrl.replace(/\/api\/?$/, ''); // e.g., http://localhost:8080
      
      return res.data.map((filepath) => {
        if (filepath.startsWith('http')) {
          return filepath;
        }
        // Ensure proper URL format
        if (!filepath.startsWith('/')) {
          filepath = '/' + filepath;
        }
        return apiBase + filepath;
      });
    } catch (error) {
      console.error('Upload service error:', error);
      throw new Error(error.response?.data?.message || error.message || 'Upload failed');
    }
  },
};

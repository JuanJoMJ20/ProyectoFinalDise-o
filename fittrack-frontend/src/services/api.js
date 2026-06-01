import axios from 'axios';

// Usar variable de entorno si existe, sino usar el default de desarrollo
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Interceptor para capturar errores de red o respuestas de error del backend
api.interceptors.response.use(
  (response) => {
    // Si la petición es exitosa, devolvemos la respuesta tal cual
    return response;
  },
  (error) => {
    // Si el backend envía un error estructurado
    if (error.response) {
      console.error(
        `[API Error] ${error.response.status} - ${error.config.url}:`, 
        error.response.data
      );
    } else if (error.request) {
      // Si la petición fue enviada pero no hubo respuesta (ej. servidor apagado)
      console.error('[API Network Error] No se pudo conectar con el servidor.');
    } else {
      // Si hubo un error al configurar la petición
      console.error('[API Configuration Error]', error.message);
    }
    return Promise.reject(error);
  }
);

export default api;

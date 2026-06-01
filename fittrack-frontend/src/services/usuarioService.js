import api from './api';

export const obtenerUsuarios = async () => {
  const response = await api.get('/usuarios');
  return response.data;
};

export const registrarUsuario = async (usuarioData) => {
  const response = await api.post('/usuarios', usuarioData);
  return response.data;
};

export const actualizarProgreso = async (usuarioId, pesoActual) => {
  const response = await api.patch(`/usuarios/${usuarioId}/progreso`, { pesoActual });
  return response.data;
};

export const asignarRutina = async (usuarioId, planId) => {
  const response = await api.post(`/usuarios/${usuarioId}/rutinas`, { planId });
  return response.data;
};

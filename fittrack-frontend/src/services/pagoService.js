import api from './api';

export const procesarPago = async (pagoData) => {
  const response = await api.post('/pagos/procesar', pagoData);
  return response.data;
};

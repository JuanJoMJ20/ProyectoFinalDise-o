import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { procesarPago } from '../services/pagoService';
import { obtenerUsuarios } from '../services/usuarioService';

const Pagos = () => {
  const navigate = useNavigate();
  const [usuarios, setUsuarios] = useState([]);
  const [formData, setFormData] = useState({
    usuarioId: '',
    monto: '29.99',
    metodoPago: 'Tarjeta de Credito'
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const cargarUsuarios = async () => {
    try {
      const data = await obtenerUsuarios();
      setUsuarios(data);
      if (data.length > 0) {
        setFormData(prev => ({ ...prev, usuarioId: data[0].id }));
      }
    } catch (err) {
      console.error("Error al cargar usuarios", err);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const transaccionId = await procesarPago({
        usuarioId: parseInt(formData.usuarioId),
        monto: parseFloat(formData.monto),
        metodoPago: formData.metodoPago
      });
      
      setSuccess(`¡Pago procesado exitosamente! Transacción ID: ${transaccionId}`);
      // Limpiar un poco o navegar
      setTimeout(() => navigate('/'), 3000);
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Error al procesar el pago. Verifica los datos.');
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 flex flex-col justify-center py-12 sm:px-6 lg:px-8 font-sans">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h2 className="mt-6 text-center text-3xl font-extrabold text-emerald-400">
          Renovar Suscripción
        </h2>
        <p className="mt-2 text-center text-sm text-slate-400">
          Módulo de Pagos FitTrack Pro
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-slate-800 py-8 px-4 shadow sm:rounded-lg sm:px-10 border border-slate-700">
          <form className="space-y-6" onSubmit={handleSubmit}>
            {error && (
              <div className="bg-red-500/20 text-red-400 p-3 rounded-md text-sm text-center font-bold">
                {typeof error === 'string' ? error : 'Error de validación (400)'}
              </div>
            )}
            {success && (
              <div className="bg-emerald-500/20 text-emerald-400 p-3 rounded-md text-sm text-center font-bold">
                {success}
              </div>
            )}

            <div>
              <label htmlFor="usuarioId" className="block text-sm font-medium text-slate-300">
                Seleccionar Atleta
              </label>
              <div className="mt-1">
                <select
                  id="usuarioId"
                  name="usuarioId"
                  value={formData.usuarioId}
                  onChange={handleChange}
                  className="block w-full px-3 py-2 border border-slate-600 rounded-md shadow-sm focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm bg-slate-900 text-white"
                >
                  {usuarios.map(u => (
                    <option key={u.id} value={u.id}>{u.nombre} - Suscripción: {u.suscripcionActiva ? 'Activa' : 'Vencida'}</option>
                  ))}
                </select>
              </div>
            </div>

            <div>
              <label htmlFor="monto" className="block text-sm font-medium text-slate-300">
                Monto (USD)
              </label>
              <div className="mt-1">
                <input
                  id="monto"
                  name="monto"
                  type="number"
                  step="0.01"
                  value={formData.monto}
                  onChange={handleChange}
                  className="appearance-none block w-full px-3 py-2 border border-slate-600 rounded-md shadow-sm focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm bg-slate-900 text-white"
                />
              </div>
            </div>

            <div>
              <label htmlFor="metodoPago" className="block text-sm font-medium text-slate-300">
                Pasarela de Pago
              </label>
              <div className="mt-1">
                <select
                  id="metodoPago"
                  name="metodoPago"
                  value={formData.metodoPago}
                  onChange={handleChange}
                  className="block w-full px-3 py-2 border border-slate-600 rounded-md shadow-sm focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm bg-slate-900 text-white"
                >
                  <option value="Tarjeta de Credito">Stripe (Tarjeta Crédito/Débito)</option>
                  <option value="Nequi">Nequi</option>
                  <option value="Nu Bank">Nu Bank</option>
                </select>
              </div>
            </div>

            <div className="pt-2 flex gap-4">
              <button
                type="button"
                onClick={() => navigate('/')}
                className="w-full flex justify-center py-2 px-4 border border-slate-600 rounded-md shadow-sm text-sm font-medium text-slate-300 bg-transparent hover:bg-slate-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-500 transition-colors"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-slate-900 bg-emerald-400 hover:bg-emerald-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500 transition-colors"
              >
                Procesar Pago
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Pagos;

import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registrarUsuario } from '../services/usuarioService';

const Registro = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nombre: '',
    pesoActual: '',
    metaPeso: '',
    fechaNacimiento: ''
  });
  const [errores, setErrores] = useState({});
  const [generalError, setGeneralError] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    // Limpiar el error del campo cuando el usuario empiece a escribir de nuevo
    if (errores[e.target.name]) {
      setErrores({
        ...errores,
        [e.target.name]: null
      });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrores({});
    setGeneralError('');

    try {
      // Intentar convertir pesos a flotantes para evitar errores de tipo si es necesario, 
      // aunque el backend puede manejar la conversión de string a float en el DTO
      await registrarUsuario({
        ...formData,
        pesoActual: formData.pesoActual ? parseFloat(formData.pesoActual) : null,
        metaPeso: formData.metaPeso ? parseFloat(formData.metaPeso) : null
      });
      // Si el registro es exitoso, volvemos al dashboard
      navigate('/');
    } catch (error) {
      if (error.response && error.response.status === 400) {
        // Capturar los errores de validación de Jakarta (@Valid)
        setErrores(error.response.data);
      } else {
        setGeneralError('Ha ocurrido un error inesperado. Inténtalo de nuevo.');
      }
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 flex flex-col justify-center py-12 sm:px-6 lg:px-8 font-sans">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h2 className="mt-6 text-center text-3xl font-extrabold text-emerald-400">
          Registrar Nuevo Atleta
        </h2>
        <p className="mt-2 text-center text-sm text-slate-400">
          Únete a FitTrack Pro y comienza tu transformación
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-slate-800 py-8 px-4 shadow sm:rounded-lg sm:px-10 border border-slate-700">
          <form className="space-y-6" onSubmit={handleSubmit}>
            {generalError && (
              <div className="bg-red-500/20 text-red-400 p-3 rounded-md text-sm text-center font-bold">
                {generalError}
              </div>
            )}

            <div>
              <label htmlFor="nombre" className="block text-sm font-medium text-slate-300">
                Nombre Completo
              </label>
              <div className="mt-1">
                <input
                  id="nombre"
                  name="nombre"
                  type="text"
                  placeholder="Ej. Juan Pérez"
                  value={formData.nombre}
                  onChange={handleChange}
                  className={`appearance-none block w-full px-3 py-2 border rounded-md shadow-sm placeholder-slate-500 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm bg-slate-900 text-white ${errores.nombre ? 'border-red-500' : 'border-slate-600'}`}
                />
              </div>
              {errores.nombre && <p className="mt-2 text-sm text-red-400">{errores.nombre}</p>}
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label htmlFor="pesoActual" className="block text-sm font-medium text-slate-300">
                  Peso Actual (kg)
                </label>
                <div className="mt-1">
                  <input
                    id="pesoActual"
                    name="pesoActual"
                    type="number"
                    step="0.1"
                    placeholder="Ej. 75.5"
                    value={formData.pesoActual}
                    onChange={handleChange}
                    className={`appearance-none block w-full px-3 py-2 border rounded-md shadow-sm placeholder-slate-500 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm bg-slate-900 text-white ${errores.pesoActual ? 'border-red-500' : 'border-slate-600'}`}
                  />
                </div>
                {errores.pesoActual && <p className="mt-2 text-sm text-red-400">{errores.pesoActual}</p>}
              </div>

              <div>
                <label htmlFor="metaPeso" className="block text-sm font-medium text-slate-300">
                  Meta de Peso (kg)
                </label>
                <div className="mt-1">
                  <input
                    id="metaPeso"
                    name="metaPeso"
                    type="number"
                    step="0.1"
                    placeholder="Ej. 80.0"
                    value={formData.metaPeso}
                    onChange={handleChange}
                    className={`appearance-none block w-full px-3 py-2 border rounded-md shadow-sm placeholder-slate-500 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm bg-slate-900 text-white ${errores.metaPeso ? 'border-red-500' : 'border-slate-600'}`}
                  />
                </div>
                {errores.metaPeso && <p className="mt-2 text-sm text-red-400">{errores.metaPeso}</p>}
              </div>
            </div>

            <div>
              <label htmlFor="fechaNacimiento" className="block text-sm font-medium text-slate-300">
                Fecha de Nacimiento
              </label>
              <div className="mt-1">
                <input
                  id="fechaNacimiento"
                  name="fechaNacimiento"
                  type="date"
                  value={formData.fechaNacimiento}
                  onChange={handleChange}
                  className={`appearance-none block w-full px-3 py-2 border rounded-md shadow-sm placeholder-slate-500 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm bg-slate-900 text-white ${errores.fechaNacimiento ? 'border-red-500' : 'border-slate-600'}`}
                />
              </div>
              {errores.fechaNacimiento && <p className="mt-2 text-sm text-red-400">{errores.fechaNacimiento}</p>}
            </div>

            <div className="pt-2 flex gap-4">
              <button
                type="button"
                onClick={() => navigate('/')}
                className="w-full flex justify-center py-2 px-4 border border-slate-600 rounded-md shadow-sm text-sm font-medium text-slate-300 bg-transparent hover:bg-slate-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-500 focus:ring-offset-slate-900 transition-colors"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-slate-900 bg-emerald-400 hover:bg-emerald-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500 focus:ring-offset-slate-900 transition-colors"
              >
                Registrar Atleta
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Registro;

import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { obtenerUsuarios, actualizarProgreso, asignarRutina } from '../services/usuarioService';

const Dashboard = () => {
  const navigate = useNavigate();
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const cargarDatos = async () => {
    try {
      const data = await obtenerUsuarios();
      setUsuarios(data);
    } catch (err) {
      setError('No se pudo conectar con el servidor. Verifica que el backend esté corriendo.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarDatos();
  }, []);

  const handleActualizarPeso = async (id, pesoActual) => {
    const nuevoPeso = window.prompt("Ingresa el nuevo peso actual en kg (Ej: 71.5):", pesoActual);
    if (nuevoPeso && !isNaN(parseFloat(nuevoPeso))) {
      try {
        await actualizarProgreso(id, parseFloat(nuevoPeso));
        cargarDatos(); // Recargar la lista
      } catch (err) {
        alert("Hubo un error al actualizar el peso");
      }
    }
  };

  const handleAsignarRutina = async (id) => {
    // 1: Principiante, 2: Avanzado, 3: Hipertrofia
    const planId = window.prompt("ID del Plan (1: Principiante, 2: Avanzado, 3: Hipertrofia):", "3");
    if (planId && !isNaN(parseInt(planId))) {
      try {
        await asignarRutina(id, parseInt(planId));
        alert("¡Plan de entrenamiento asignado exitosamente! Revisa los logs del backend para ver el evento asíncrono.");
      } catch (err) {
        alert("Hubo un error al asignar el plan");
      }
    }
  };

  if (loading) return <div className="text-white text-center mt-20 text-xl">Cargando tu progreso...</div>;
  if (error) return <div className="text-red-500 text-center mt-20 text-xl font-bold">{error}</div>;

  return (
    <div className="min-h-screen bg-slate-900 text-white p-8 font-sans">
      <header className="mb-10 border-b border-slate-700 pb-4 flex justify-between items-end">
        <div>
          <h1 className="text-4xl font-extrabold text-emerald-400 tracking-tight">FitTrack Pro</h1>
          <p className="text-slate-400 mt-2">Panel de Control de Atletas</p>
        </div>
        <div className="flex gap-4">
          <button 
            onClick={() => navigate('/pagos')}
            className="bg-slate-700 hover:bg-slate-600 text-emerald-400 border border-emerald-400/30 font-bold py-2 px-6 rounded-lg transition-colors shadow-lg"
          >
            Renovar Suscripción
          </button>
          <button 
            onClick={() => navigate('/registro')}
            className="bg-emerald-500 hover:bg-emerald-400 text-slate-900 font-bold py-2 px-6 rounded-lg transition-colors shadow-lg"
          >
            + Registrar Nuevo Atleta
          </button>
        </div>
      </header>

      <main>
        <h2 className="text-2xl font-bold mb-6">Atletas Activos</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {usuarios.map((usuario) => (
            <div key={usuario.id} className="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700 hover:border-emerald-500 transition-colors flex flex-col justify-between">
              <div>
                <div className="flex justify-between items-start mb-4">
                  <h3 className="text-xl font-bold">{usuario.nombre}</h3>
                  <span className={`px-3 py-1 rounded-full text-xs font-bold ${usuario.suscripcionActiva ? 'bg-emerald-500/20 text-emerald-400' : 'bg-red-500/20 text-red-400'}`}>
                    {usuario.suscripcionActiva ? 'PRO' : 'VENCIDO'}
                  </span>
                </div>
                
                <div className="space-y-2 text-sm text-slate-300">
                  <p><span className="font-semibold text-slate-400">Peso Actual:</span> {usuario.pesoActual} kg</p>
                  <p><span className="font-semibold text-slate-400">Meta:</span> {usuario.metaPeso} kg</p>
                  
                  <div className="w-full bg-slate-700 rounded-full h-2.5 mt-4">
                    <div 
                      className="bg-emerald-500 h-2.5 rounded-full" 
                      style={{ width: `${Math.min((usuario.pesoActual / usuario.metaPeso) * 100, 100)}%` }}
                    ></div>
                  </div>
                </div>
              </div>

              <div className="mt-6 pt-4 border-t border-slate-700 flex gap-2">
                <button 
                  onClick={() => handleActualizarPeso(usuario.id, usuario.pesoActual)}
                  className="flex-1 bg-slate-700 hover:bg-slate-600 text-xs py-2 rounded text-slate-200 transition-colors"
                >
                  Modificar Peso
                </button>
                <button 
                  onClick={() => handleAsignarRutina(usuario.id)}
                  className="flex-1 bg-slate-700 hover:bg-slate-600 text-xs py-2 rounded text-slate-200 transition-colors"
                >
                  Asignar Rutina
                </button>
              </div>
            </div>
          ))}
          
          {usuarios.length === 0 && (
            <p className="text-slate-400 col-span-full">No hay atletas registrados en el sistema.</p>
          )}
        </div>
      </main>
    </div>
  );
};

export default Dashboard;

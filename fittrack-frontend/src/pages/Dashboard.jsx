import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { obtenerUsuarios, actualizarProgreso, asignarRutina, eliminarUsuario, obtenerPlanes } from '../services/usuarioService';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';

const Dashboard = () => {
  const navigate = useNavigate();
  const [usuarios, setUsuarios] = useState([]);
  const [planes, setPlanes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Estados para Modales
  const [pesoModal, setPesoModal] = useState({ show: false, userId: null, currentPeso: '' });
  const [rutinaModal, setRutinaModal] = useState({ show: false, userId: null, planId: '3' });

  const cargarDatos = async () => {
    try {
      const [dataUsuarios, dataPlanes] = await Promise.all([
        obtenerUsuarios(),
        obtenerPlanes()
      ]);
      setUsuarios(dataUsuarios);
      
      // Filtrar para que solo queden planes únicos, por si acaso hay duplicados de seeders
      const uniquePlanes = Array.from(new Map(dataPlanes.map(p => [p.nivel, p])).values());
      setPlanes(uniquePlanes);
      
      // Preseleccionar el primer plan disponible si no hay seleccionado
      if (uniquePlanes.length > 0) {
        setRutinaModal(prev => ({ ...prev, planId: uniquePlanes[0].id.toString() }));
      }
    } catch (err) {
      setError('No se pudo conectar con el servidor. Verifica que el backend esté corriendo.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarDatos();
  }, []);

  const handleGuardarPeso = async () => {
    if (pesoModal.currentPeso && !isNaN(parseFloat(pesoModal.currentPeso))) {
      try {
        await actualizarProgreso(pesoModal.userId, parseFloat(pesoModal.currentPeso));
        setPesoModal({ show: false, userId: null, currentPeso: '' });
        cargarDatos();
      } catch (err) {
        alert("Hubo un error al actualizar el peso");
      }
    }
  };

  const handleGuardarRutina = async () => {
    if (rutinaModal.planId) {
      try {
        await asignarRutina(rutinaModal.userId, parseInt(rutinaModal.planId));
        setRutinaModal({ show: false, userId: null, planId: planes.length > 0 ? planes[0].id.toString() : '' });
        alert("¡Plan asignado exitosamente!");
        cargarDatos();
      } catch (err) {
        alert("Hubo un error al asignar el plan. Es posible que el ID no exista en la Base de Datos.");
      }
    }
  };

  const handleEliminar = async (id) => {
    const confirm = window.confirm("¿Estás seguro que deseas eliminar este atleta? Esta acción no se puede deshacer.");
    if (confirm) {
      try {
        await eliminarUsuario(id);
        cargarDatos();
      } catch (err) {
        alert("Error al eliminar el usuario");
      }
    }
  };

  const exportarPDF = (usuario) => {
    const doc = new jsPDF();
    doc.setFillColor(15, 23, 42); // Fondo
    doc.rect(0, 0, 210, 297, 'F');
    
    doc.setTextColor(52, 211, 153); // Titulo
    doc.setFontSize(22);
    doc.text(`FitTrack Pro - Reporte Deportivo`, 20, 20);
    
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(14);
    doc.text(`Atleta: ${usuario.nombre}`, 20, 40);
    doc.text(`Suscripción: ${usuario.suscripcionActiva ? 'PRO Activa' : 'Vencida'}`, 20, 50);
    doc.text(`Peso Actual: ${usuario.pesoActual} kg`, 20, 60);
    doc.text(`Meta de Peso: ${usuario.metaPeso} kg`, 20, 70);

    // Forma correcta para Vite y jsPDF autotable
    autoTable(doc, {
      startY: 90,
      head: [['Día', 'Ejercicio', 'Series', 'Repeticiones']],
      body: [
        ['Lunes', 'Press de Banca', '4', '10-12'],
        ['Martes', 'Sentadilla Libre', '4', '8-10'],
        ['Miércoles', 'Descanso Activo', '-', '-'],
        ['Jueves', 'Dominadas', '4', 'Al fallo'],
        ['Viernes', 'Peso Muerto', '3', '6-8']
      ],
      headStyles: { fillColor: [16, 185, 129] },
      theme: 'grid'
    });

    doc.save(`Rutina_${usuario.nombre.replace(/\s+/g, '_')}.pdf`);
  };

  if (loading) return <div className="text-white text-center mt-20 text-xl">Cargando tu progreso...</div>;
  if (error) return <div className="text-red-500 text-center mt-20 text-xl font-bold">{error}</div>;

  return (
    <div className="min-h-screen bg-slate-900 text-white p-8 font-sans relative">
      
      {/* MODAL PARA PESO */}
      {pesoModal.show && (
        <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50">
          <div className="bg-slate-800 p-6 rounded-xl border border-slate-600 shadow-2xl w-96">
            <h3 className="text-xl font-bold text-emerald-400 mb-4">Actualizar Peso Actual</h3>
            <label className="block text-sm text-slate-300 mb-2">Nuevo Peso (kg)</label>
            <input 
              type="number" 
              step="0.1"
              value={pesoModal.currentPeso}
              onChange={(e) => setPesoModal({...pesoModal, currentPeso: e.target.value})}
              className="w-full bg-slate-900 border border-slate-600 rounded p-2 text-white focus:outline-none focus:border-emerald-500 mb-6"
            />
            <div className="flex justify-end gap-2">
              <button onClick={() => setPesoModal({show: false})} className="px-4 py-2 bg-slate-700 hover:bg-slate-600 rounded">Cancelar</button>
              <button onClick={handleGuardarPeso} className="px-4 py-2 bg-emerald-500 text-slate-900 font-bold rounded hover:bg-emerald-400">Guardar</button>
            </div>
          </div>
        </div>
      )}

      {/* MODAL PARA RUTINA */}
      {rutinaModal.show && (
        <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50">
          <div className="bg-slate-800 p-6 rounded-xl border border-slate-600 shadow-2xl w-96">
            <h3 className="text-xl font-bold text-emerald-400 mb-4">Asignar Plan de Entrenamiento</h3>
            <label className="block text-sm text-slate-300 mb-2">Selecciona un Plan</label>
            <select 
              value={rutinaModal.planId}
              onChange={(e) => setRutinaModal({...rutinaModal, planId: e.target.value})}
              className="w-full bg-slate-900 border border-slate-600 rounded p-2 text-white focus:outline-none focus:border-emerald-500 mb-6"
            >
              {planes.map(plan => (
                <option key={plan.id} value={plan.id}>
                  {plan.nivel} - {plan.enfoque}
                </option>
              ))}
              {planes.length === 0 && <option value="">No hay planes disponibles</option>}
            </select>
            <div className="flex justify-end gap-2">
              <button onClick={() => setRutinaModal({show: false})} className="px-4 py-2 bg-slate-700 hover:bg-slate-600 rounded">Cancelar</button>
              <button onClick={handleGuardarRutina} className="px-4 py-2 bg-emerald-500 text-slate-900 font-bold rounded hover:bg-emerald-400">Asignar</button>
            </div>
          </div>
        </div>
      )}

      <header className="mb-10 border-b border-slate-700 pb-4 flex justify-between items-end">
        <div>
          <h1 className="text-4xl font-extrabold text-emerald-400 tracking-tight">FitTrack Pro</h1>
          <p className="text-slate-400 mt-2">Panel de Control de Atletas</p>
        </div>
        <div className="flex gap-4">
          <button onClick={() => navigate('/pagos')} className="bg-slate-700 hover:bg-slate-600 text-emerald-400 border border-emerald-400/30 font-bold py-2 px-6 rounded-lg transition-colors shadow-lg">
            Renovar Suscripción
          </button>
          <button onClick={() => navigate('/registro')} className="bg-emerald-500 hover:bg-emerald-400 text-slate-900 font-bold py-2 px-6 rounded-lg transition-colors shadow-lg">
            + Registrar Nuevo Atleta
          </button>
        </div>
      </header>

      <main>
        {usuarios.length > 0 && (
          <div className="bg-slate-800 p-6 rounded-xl shadow-lg border border-slate-700 mb-8 h-[350px]">
            <h3 className="text-xl font-bold text-emerald-400 mb-6">Progreso Global de Atletas (Peso Actual vs Meta)</h3>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={usuarios}>
                <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                <XAxis dataKey="nombre" stroke="#94a3b8" />
                <YAxis stroke="#94a3b8" />
                <Tooltip contentStyle={{ backgroundColor: '#1e293b', borderColor: '#334155', color: '#fff' }} />
                <Legend />
                <Bar dataKey="pesoActual" name="Peso Actual (kg)" fill="#34d399" />
                <Bar dataKey="metaPeso" name="Meta (kg)" fill="#94a3b8" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        )}

        <h2 className="text-2xl font-bold mb-6">Atletas Activos</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {usuarios.map((usuario) => (
            <div key={usuario.id} className="bg-slate-800 rounded-xl p-6 shadow-lg border border-slate-700 hover:border-emerald-500 transition-colors flex flex-col justify-between">
              <div>
                <div className="flex justify-between items-start mb-4">
                  <h3 className="text-xl font-bold">{usuario.nombre}</h3>
                  <div className="flex gap-2 items-center">
                    <span className={`px-3 py-1 rounded-full text-xs font-bold ${usuario.suscripcionActiva ? 'bg-emerald-500/20 text-emerald-400' : 'bg-red-500/20 text-red-400'}`}>
                      {usuario.suscripcionActiva ? 'PRO' : 'VENCIDO'}
                    </span>
                    <button onClick={() => handleEliminar(usuario.id)} className="text-red-400 hover:text-red-300 font-bold ml-2 text-xl" title="Eliminar Atleta">
                      ×
                    </button>
                  </div>
                </div>
                
                <div className="space-y-2 text-sm text-slate-300">
                  <p><span className="font-semibold text-slate-400">Rutina Actual:</span> {usuario.rutinaActual || "Sin rutina asignada"}</p>
                  <p><span className="font-semibold text-slate-400">Peso Actual:</span> {usuario.pesoActual} kg</p>
                  <p><span className="font-semibold text-slate-400">Meta:</span> {usuario.metaPeso} kg</p>
                  
                  <div className="w-full bg-slate-700 rounded-full h-2.5 mt-4">
                    <div className="bg-emerald-500 h-2.5 rounded-full" style={{ width: `${Math.min((usuario.pesoActual / usuario.metaPeso) * 100, 100)}%` }}></div>
                  </div>
                </div>
              </div>

              <div className="mt-6 pt-4 border-t border-slate-700 flex flex-col gap-2">
                <div className="flex gap-2">
                  <button 
                    onClick={() => {
                      if (usuario.suscripcionActiva) setPesoModal({ show: true, userId: usuario.id, currentPeso: usuario.pesoActual });
                    }}
                    className={`flex-1 text-xs py-2 rounded font-bold transition-colors ${usuario.suscripcionActiva ? 'bg-slate-700 hover:bg-slate-600 text-slate-200' : 'bg-slate-800 text-slate-500 cursor-not-allowed border border-slate-700'}`}
                    disabled={!usuario.suscripcionActiva}
                    title={!usuario.suscripcionActiva ? 'Renueva la suscripción para modificar el peso' : ''}
                  >
                    Modificar Peso
                  </button>
                  <button 
                    onClick={() => {
                      if (usuario.suscripcionActiva) {
                        const planEncontrado = planes.find(p => p.nivel === usuario.rutinaActual);
                        const defaultPlanId = planEncontrado ? planEncontrado.id.toString() : (planes.length > 0 ? planes[0].id.toString() : '');
                        setRutinaModal({ show: true, userId: usuario.id, planId: defaultPlanId });
                      }
                    }}
                    className={`flex-1 text-xs py-2 rounded font-bold transition-colors ${usuario.suscripcionActiva ? 'bg-slate-700 hover:bg-slate-600 text-slate-200' : 'bg-slate-800 text-slate-500 cursor-not-allowed border border-slate-700'}`}
                    disabled={!usuario.suscripcionActiva}
                    title={!usuario.suscripcionActiva ? 'Renueva la suscripción para asignar rutinas' : ''}
                  >
                    Asignar Rutina
                  </button>
                </div>
                <button 
                  onClick={() => exportarPDF(usuario)}
                  className="w-full bg-emerald-500/20 hover:bg-emerald-500/40 text-emerald-400 border border-emerald-500/50 text-xs py-2 rounded transition-colors mt-2 font-bold"
                >
                  📄 Descargar Rutina (PDF)
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

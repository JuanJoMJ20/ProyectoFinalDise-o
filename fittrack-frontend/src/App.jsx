import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import Registro from './pages/Registro';
import Pagos from './pages/Pagos';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/registro" element={<Registro />} />
        <Route path="/pagos" element={<Pagos />} />
      </Routes>
    </Router>
  );
}

export default App;

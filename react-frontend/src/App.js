import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navigation from './components/Navigation';
import Home from './components/Home';
import Chambres from './components/Chambres';
import Clients from './components/Clients';
import Reservations from './components/Reservations';
import Statistiques from './components/Statistiques';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Navigation />
        <div className="container mt-4">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/chambres" element={<Chambres />} />
            <Route path="/clients" element={<Clients />} />
            <Route path="/reservations" element={<Reservations />} />
            <Route path="/statistiques" element={<Statistiques />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;





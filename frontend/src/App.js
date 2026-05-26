import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navigation from './components/Navigation';
import Login from './pages/Login';
import Register from './pages/Register';
import JobList from './pages/JobList';
import JobDetails from './pages/JobDetails';
import Dashboard from './pages/Dashboard';
import RecruiterDashboard from './pages/RecruiterDashboard';
import './App.css';

function App() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');
    if (token && userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const handleLogin = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };

  return (
    <Router>
      <div className="App">
        <Navigation user={user} onLogout={handleLogout} />
        <Routes>
          <Route path="/login" element={<Login onLogin={handleLogin} />} />
          <Route path="/register" element={<Register />} />
          <Route path="/jobs" element={user ? <JobList /> : <Navigate to="/login" />} />
          <Route path="/jobs/:id" element={user ? <JobDetails /> : <Navigate to="/login" />} />
          <Route path="/dashboard" element={
            user ? (user.role === 'RECRUITER' ? <RecruiterDashboard /> : <Dashboard />) : <Navigate to="/login" />
          } />
          <Route path="/" element={<Navigate to="/jobs" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;

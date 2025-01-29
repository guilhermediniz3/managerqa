// src/main.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route,Navigate  } from 'react-router-dom';
import './index.css';
import Login from './shared/pages/login/Login';
import ForgotPassword from './shared/pages/login/forgot-password/ForgotPassword';
import Register from './shared/pages/login/register/Register';
import Dashboard from './shared/pages/login/dashboard/dashboard';
import 'bootstrap/dist/css/bootstrap.min.css';
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Dashboard />} /> {/* Página que vai após login */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  </React.StrictMode>
);

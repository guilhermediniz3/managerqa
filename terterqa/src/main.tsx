import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import './index.css';
import Login from './shared/pages/login/Login';
import ForgotPassword from './shared/pages/login/forgot-password/ForgotPassword';
import Register from './shared/pages/login/register/Register';
import Dashboard from './shared/pages/login/dashboard/Dashboard';

import 'bootstrap/dist/css/bootstrap.min.css';
import ProtectedRoute from './shared/Routes/ProtectedRoute';



ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <Router>
      <Routes>
        {/* Rotas públicas */}
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/register" element={<Register />} />

        {/* Rotas protegidas (todas as rotas dentro deste Route serão protegidas) */}
        <Route element={<ProtectedRoute />}>
          <Route path="/dashboard" element={<Dashboard />} />
           {/* Adicione outras rotas protegidas aqui */}
        </Route>

        {/* Rota padrão (redireciona para o login) */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  </React.StrictMode>
);
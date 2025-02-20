import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import './index.css';
import Login from './shared/pages/login/Login';
import ForgotPassword from './shared/pages/login/forgot-password/ForgotPassword';
import Register from './shared/pages/login/register/Register';
import Dashboard from './shared/pages/login/dashboard/Dashboard';
import ListaUsuarios from './shared/pages/usuarios/Listagem/ListaUsuarios';
import 'bootstrap/dist/css/bootstrap.min.css';
import ProtectedRoute from './shared/Routes/ProtectedRoute';
import { TokenProvider } from './contexts/TokenContext ';
import EditarUsuarioPage from './shared/pages/usuarios/Editar/EditarUsuarioPage';
import CadastrarDesenvolvedor from './shared/pages/desenvolvedores/cadastrar/CadastrarDesenvolvedor';
import EditarDesenvolvedor from './shared/pages/desenvolvedores/editar/EditarDesenvolvedor';
import ListaDesenvolvedores from './shared/pages/desenvolvedores/listagem/ListaDesenvolvedores';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
        <TokenProvider>
    <Router>
      <Routes>
        {/* Rotas públicas */}
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/register" element={<Register />} />


        {/* Rotas protegidas (todas as rotas dentro deste Route serão protegidas) */}
        <Route element={<ProtectedRoute />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/usuarios" element={<ListaUsuarios/>} />
          <Route path="/usuarios/editar/:id" element={<EditarUsuarioPage />} />
          <Route path="/usuarios/register" element={<Register />} />
          <Route path="/desenvolvedores" element={<CadastrarDesenvolvedor/>} />
          <Route path="/desenvolvedores/editar/:id" element={<EditarDesenvolvedor/>} />
          <Route path="/desenvolvedores/listagem" element={<ListaDesenvolvedores/>} />
   
        </Route>

        {/* Rota padrão (redireciona para o login) */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
    </TokenProvider>
  </React.StrictMode>
);

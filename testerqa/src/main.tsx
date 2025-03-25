import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
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
import ListaTester from './shared/pages/tester/listagem/ListaTester';
import CadastrarTester from './shared/pages/tester/cadastrar/CadastrarTester';
import EditarTester from './shared/pages/tester/editar/EditarTester';
import ListaModulo from './shared/pages/modulo/listagem/ListaModulo';
import CadastrarModulo from './shared/pages/modulo/cadastrar/CadastrarModulo';
import EditarModulo from './shared/pages/modulo/editar/EditarModulo';
import ListaTecnologia from './shared/pages/tecnologia/listagem/ListaTecnologia';
import CadastrarTecnologia from './shared/pages/tecnologia/cadastrar/CadastrarTecnologia';
import EditarTecnologia from './shared/pages/tecnologia/editar/EditarTecnologia';
import ListaPlanoTeste from './shared/pages/planoTeste/listagem/ListaPlanoTeste'
import CadastrarPlanoTeste from'./shared/pages/planoTeste/cadastrar/CadastrarPlanoTeste';
import EditarPlanoTeste from './shared/pages/planoTeste/editar/EditarPlanoTeste';
import Case from './shared/pages/cases/Case';
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
          <Route path="/desenvolvedores/cadastrar" element={<CadastrarDesenvolvedor/>} />
          <Route path="/desenvolvedores/editar/:id" element={<EditarDesenvolvedor/>} />
          <Route path="/desenvolvedores/listagem" element={<ListaDesenvolvedores/>} />
          <Route path="/tester/listagem" element={<ListaTester/>} />
          <Route path="/tester/cadastrar" element={<CadastrarTester/>} />
          <Route path="/tester/editar/:id" element={<EditarTester/>} />
          <Route path="/modulo/listagem" element={<ListaModulo/>} />
          <Route path="/modulo/cadastrar" element={<CadastrarModulo/>} />
          <Route path="/modulo/editar/:id" element={<EditarModulo/>} />
          <Route path="/tecnologia/listagem" element={<ListaTecnologia/>} />
          <Route path="/tecnologia/cadastrar" element={<CadastrarTecnologia/>} />
          <Route path="/tecnologia/editar/:id" element={<EditarTecnologia/>} />
          <Route path="/planoTeste/listagem" element={<ListaPlanoTeste/>} />
          <Route path="/planoTeste/cadastrar" element={<CadastrarPlanoTeste/>} />
          <Route path="/planoTeste/cadastrar/:id" element={<CadastrarPlanoTeste/>} />
          <Route path="/planoTeste/editar/:id" element={<EditarPlanoTeste/>} />
          <Route path="/plan/:testPlanId/suite/:testSuiteId" element={<Case />} />
    
   
        </Route>

        {/* Rota padrão (redireciona para o login) */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
    </TokenProvider>
  
  </React.StrictMode>
);

import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
  exp: number;
  userId: string;
  username: string;
}

const ProtectedRoute: React.FC = () => {
  const location = useLocation();

  // Pega o token do localStorage e do state
  const tokenFromLocalStorage = localStorage.getItem('token');
  const tokenFromBack = location.state?.token;

  console.log('Token do backend (state):', tokenFromBack);
  console.log('Token do localStorage:', tokenFromLocalStorage);

  // Verifica se há um token válido (localStorage ou state)
  const token = tokenFromBack || tokenFromLocalStorage;

  // Se não houver token, redireciona para o login
  if (!token) {
    console.log('Nenhum token encontrado. Redirecionando para o login...');
    return <Navigate to="/login" replace />;
  }

  // Verifica se o token do state é diferente do token no localStorage
  if (tokenFromBack && tokenFromBack !== tokenFromLocalStorage) {
    console.log('Token do state diferente do localStorage. Redirecionando para o login...');
    return <Navigate to="/login" replace />;
  }

  // Verifica a validade do token
  try {
    const decodedToken = jwtDecode<DecodedToken>(token);
    const currentTime = Date.now() / 1000;

    // Se o token estiver expirado, remove o token do localStorage e redireciona para o login
    if (decodedToken.exp < currentTime) {
      console.log('Token expirado. Redirecionando para o login...');
      localStorage.removeItem('token');
      return <Navigate to="/login" replace />;
    }
  } catch (error) {
    console.error('Erro ao decodificar o token:', error);
    return <Navigate to="/login" replace />;
  }

  // Se o token for válido, renderiza os componentes filhos (rotas protegidas)
  console.log('Token válido. Permitindo acesso à rota protegida...');
  return <Outlet />;
};

export default ProtectedRoute;
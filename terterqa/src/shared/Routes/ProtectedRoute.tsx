import React from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

interface DecodedToken {
  exp: number;
  userId: string;
  username: string;
}

const ProtectedRoute: React.FC = () => {
  const location = useLocation();

  // Pega o token do localStorage
  const tokenFromLocalStorage = localStorage.getItem("token");
  const tokenFromBack = location.state?.token;

  console.log("Token do backend (state):", tokenFromBack);
  console.log("Token do localStorage:", tokenFromLocalStorage);

  // Usa o token do localStorage como principal
  const token = tokenFromLocalStorage || tokenFromBack;

  if (!token) {
    console.log("Nenhum token encontrado. Redirecionando para o login...");
    return <Navigate to="/login" replace />;
  }

  try {
    const decodedToken = jwtDecode<DecodedToken>(token);
    const currentTime = Date.now() / 1000;

    if (decodedToken.exp < currentTime) {
      console.log("Token expirado. Redirecionando para o login...");
      localStorage.removeItem("token");
      return <Navigate to="/login" replace />;
    }
  } catch (error) {
    console.error("Erro ao decodificar o token:", error);
    localStorage.removeItem("token");
    return <Navigate to="/login" replace />;
  }

  console.log("Token válido. Permitindo acesso à rota protegida...");
  return <Outlet />;
};

export default ProtectedRoute;

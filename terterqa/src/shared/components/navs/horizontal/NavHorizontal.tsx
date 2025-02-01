import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import './stlyless.css';
import NavVertical from '../vertical/NavVertical';

interface DecodedToken {
  username: string;
}

const NavHorizontal: React.FC = () => {
  const [username, setUsername] = useState<string>('');

  // Recupera o nome do usuário do token
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decodedToken = jwtDecode<DecodedToken>(token);
        setUsername(decodedToken.username);
      } catch (error) {
        console.error('Erro ao decodificar o token:', error);
      }
    }
  }, []);

  // Função para alternar entre temas dark e light
  const toggleTheme = () => {
    const htmlElement = document.documentElement;
    const currentTheme = htmlElement.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    htmlElement.setAttribute('data-theme', newTheme);
  };

  return (
    <>
      <nav className="navHorizontal">
        {/* Botão para alternar o tema */}
        <button onClick={toggleTheme} className="themeButton">
          Alternar Tema
        </button>

        {/* Nome do usuário */}
        <div className="userInfo">
          <span>{username}</span>
        </div>
      </nav>

      {/* Renderiza o NavVertical que gerencia seu próprio estado */}
      <NavVertical />
    </>
  );
};

export default NavHorizontal;

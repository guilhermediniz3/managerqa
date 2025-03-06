import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
import './stlyless.css';
import NavVertical from '../vertical/NavVertical';
import ToggleThemeButton from '../../Buttons/ToggleThemeButton';

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

  
  return (
    <>
      <nav className="navHorizontal">
      
        <div className="text-center mt-3">
                <ToggleThemeButton />
              </div>
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

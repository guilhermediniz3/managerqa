import React, { useState, useEffect } from 'react';
import { FaSun, FaMoon } from 'react-icons/fa';

const ThemeToggle: React.FC = () => {
  const [isDarkMode, setIsDarkMode] = useState<boolean>(true);  // Começa com o modo escuro

  useEffect(() => {
    // Definir o tema de acordo com o estado
    if (isDarkMode) {
      document.documentElement.setAttribute('data-bs-theme', 'dark');
    } else {
      document.documentElement.setAttribute('data-bs-theme', 'light');
    }
  }, [isDarkMode]);  // Atualiza sempre que `isDarkMode` mudar

  return (
    <button
      className="btn btn-link"
      onClick={() => setIsDarkMode(!isDarkMode)}  // Alterna entre os temas
    >
      {isDarkMode ? <FaSun /> : <FaMoon />}
    </button>
  );
};

export default ThemeToggle;

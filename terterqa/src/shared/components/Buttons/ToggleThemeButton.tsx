import React, { useState, useEffect } from "react";
import { FaSun, FaMoon } from "react-icons/fa";

const ThemeToggle: React.FC = () => {
  // Carrega o tema salvo no localStorage ou define 'dark' como padrão
  const [isDarkMode, setIsDarkMode] = useState<boolean>(
    localStorage.getItem("theme") === "dark"
  );

  // Define o tema no HTML e no localStorage sempre que `isDarkMode` mudar
  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.setAttribute("data-bs-theme", "dark");
      localStorage.setItem("theme", "dark");
    } else {
      document.documentElement.setAttribute("data-bs-theme", "light");
      localStorage.setItem("theme", "light");
    }
  }, [isDarkMode]);

  return (
    <button
      className="btn btn-link"
      onClick={() => setIsDarkMode(!isDarkMode)} // Alterna entre os temas
    >
      {isDarkMode ? <FaSun /> : <FaMoon />}
    </button>
  );
};

export default ThemeToggle;
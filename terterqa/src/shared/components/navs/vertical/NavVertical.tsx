import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Importe o useNavigate
import { FaBars, FaHome, FaUser, FaCog, FaPalette, FaUserPlus, FaRobot } from 'react-icons/fa';
import './styless.css';

const NavVertical: React.FC = () => {
  const navigate = useNavigate(); // Hook para navegação programática
  const [isExpanded, setIsExpanded] = useState<boolean>(false);
  const [isColorPickerVisible, setIsColorPickerVisible] = useState<boolean>(false);
  const [selectedColor, setSelectedColor] = useState<string>(
    localStorage.getItem('selectedColor') || '#333333' // Recupera a cor salva ou usa a cor padrão
  );
  const [isSubmenuOpen, setIsSubmenuOpen] = useState<boolean>(false); // Estado para controlar a visibilidade do submenu
  
  const toggleExpanded = () => {
    setIsExpanded((prev) => !prev);
  };

  const toggleColorPicker = () => {
    setIsColorPickerVisible((prev) => !prev);
  };

  const toggleSubmenu = () => {
    setIsSubmenuOpen((prev) => !prev); // Alterna a visibilidade do submenu
  };

  const handleColorChange = (color: string) => {
    setSelectedColor(color);
    localStorage.setItem('selectedColor', color); // Salva a cor no localStorage
    setIsColorPickerVisible(false);
  };

  // Função para navegar para uma rota específica
  const handleNavigation = (path: string) => {
    navigate(path); // Navega para a rota especificada
  };

  return (
    <nav
      className={`navVertical ${isExpanded ? 'expanded' : 'collapsed'}`}
      style={{ background: selectedColor }}
    >
      {/* Botão de expansão */}
      <div className="verticalHeader">
        <button onClick={toggleExpanded} className="expandButton">
          <FaBars className="navIcon" />
        </button>
      </div>

      {/* Lista de navegação */}
      <ul className="navList">
        <li className="navItem">
          <a href="#" className="navLink" onClick={() => handleNavigation('/dashboard')}>
            <FaHome className="navIcon" />
            {isExpanded && <span className="navText">Início</span>}
          </a>
        </li>
        <li className="navItem">
          <a href="#" className="navLink" onClick={() => handleNavigation('/perfil')}>
            <FaUser className="navIcon" />
            {isExpanded && <span className="navText">Perfil</span>}
          </a>
        </li>
        <li className="navItem">
          <a href="#" className="navLink" onClick={toggleSubmenu}>
            <FaUserPlus className="navIcon" />
            {isExpanded && <span className="navText">Cadastro</span>}
          </a>
          {isSubmenuOpen && (
            <ul className="submenu">
              <li className="submenuItem"><a href="#" onClick={() => handleNavigation('/register')}>Usuário</a></li>
              <li className="submenuItem"><a href="#" onClick={() => handleNavigation('/tester')}>Tester</a></li>
              <li className="submenuItem"><a href="#" onClick={() => handleNavigation('/desenvolvedor')}>Desenvolvedor</a></li>
              <li className="submenuItem"><a href="#" onClick={() => handleNavigation('/tecnologias')}>Tecnologias</a></li>
              <li className="submenuItem"><a href="#" onClick={() => handleNavigation('/modulos')}>Módulos</a></li>
            </ul>
          )}
        </li>
        <li className="navItem">
          <a href="#" className="navLink" onClick={() => handleNavigation('/configuracoes')}>
            <FaCog className="navIcon" />
            {isExpanded && <span className="navText">Configurações</span>}
          </a>
        </li>
        <li className="navItem">
          <a href="#" className="navLink" onClick={() => handleNavigation('/chatgpt')}>
            <FaRobot className="navIcon" />
            {isExpanded && <span className="navText">ChatGPT</span>}
          </a>
        </li>
      </ul>

      {/* Seletor de cor */}
      <div className="colorPickerContainer">
        <button onClick={toggleColorPicker} className="colorPickerButton">
          <FaPalette className="navIcon" />
        </button>
        {isColorPickerVisible && (
          <div className="colorOptions">
            {["#32cd32", "#333333", "#2E5CE6", "#2ED0E6", "#812EE6"].map((color, index) => (
              <div
                key={index}
                className="colorOption"
                style={{ background: color }}
                onClick={() => handleColorChange(color)}
              >
              </div>
            ))}
          </div>
        )}
      </div>
    </nav>
  );
};

export default NavVertical;

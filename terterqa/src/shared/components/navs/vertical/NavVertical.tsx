import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Importe o useNavigate
import { FaBars, FaHome, FaUser, FaCog, FaPalette, FaUserPlus } from 'react-icons/fa';
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

  // Lista de cores
  const colors = [
    '#32cd32', '#333333', '#2E5CE6',
    '#2ED0E6', '#812EE6', 'linear-gradient(to right, #5A48F9, #312787)',
    'radial-gradient(circle at 50% -20.71%, #ff57d2 0%, #ff4edb 12.5%, #f046e3 25%, #d63feb 37.5%, #b53cf2 50%, #8c3df9 62.5%, #4f40ff 75%, #0046ff 87.5%, #004bff 100%)',
    'radial-gradient(circle at 37.72% 119.64%, #ff40ff 0, #ff41ff 16.67%, #e240ff 33.33%, #b53cf2 50%, #8837df 66.67%, #5c33cc 83.33%, #252fbb 100%)',
    'radial-gradient(circle at 43.84% 120.44%, #8fffff 0, #75ffff 10%, #5dffff 20%, #49ffff 30%, #3effff 40%, #3cedf2 50%, #3fd2dd 60%, #44baca 70%, #47a5b8 80%, #4a94a8 90%, #4b869b 100%)',
    'radial-gradient(circle at 37.72% 119.64%, #ffff52 0, #e8ff56 10%, #cbfa5c 20%, #acf162 30%, #8de668 40%, #6cd86c 50%, #49c970 60%, #1abd74 70%, #00b279 80%, #00a97e 90%, #00a184 100%)',
    'linear-gradient(40deg, #b9f27d 0, #a4ec79 16.67%, #8be373 33.33%, #6cd86c 50%, #47cd66 66.67%, #00c464 83.33%, #00bd65 100%);'
  ];

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
          <a
            href="#"
            className="navLink"
            onClick={(e) => {
              e.preventDefault(); // Evita o comportamento padrão do link
              handleNavigation('/dashboard'); // Navega para a rota inicial
            }}
          >
            <FaHome className="navIcon" />
            {isExpanded && <span className="navText">Início</span>}
          </a>
        </li>
        <li className="navItem">
          <a
            href="#"
            className="navLink"
            onClick={(e) => {
              e.preventDefault(); // Evita o comportamento padrão do link
              handleNavigation('/perfil'); // Navega para a rota de perfil
            }}
          >
            <FaUser className="navIcon" />
            {isExpanded && <span className="navText">Perfil</span>}
          </a>
        </li>

        <li className="navItem">
          <a
            href="#"
            className="navLink"
            onClick={(e) => {
              e.preventDefault(); // Evita o comportamento padrão do link
              toggleSubmenu(); // Alterna a visibilidade do submenu
            }}
          >
            <FaUserPlus className="navIcon" />
            {isExpanded && <span className="navText">Cadastro</span>}
          </a>
          {/* Submenu */}
          {isSubmenuOpen && (
            <ul className="submenu">
              <li className="submenuItem">
                <a
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    handleNavigation('/register'); // Navega para a rota de usuário
                  }}
                >
                  Usuário
                </a>
              </li>
              <li className="submenuItem">
                <a
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    handleNavigation('/tester'); // Navega para a rota de tester
                  }}
                >
                  Tester
                </a>
              </li>
              <li className="submenuItem">
                <a
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    handleNavigation('/desenvolvedor'); // Navega para a rota de desenvolvedor
                  }}
                >
                  Desenvolvedor
                </a>
              </li>
              <li className="submenuItem">
                <a
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    handleNavigation('/tecnologias'); // Navega para a rota de tecnologias
                  }}
                >
                  Tecnologias
                </a>
              </li>
              <li className="submenuItem">
                <a
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    handleNavigation('/modulos'); // Navega para a rota de módulos
                  }}
                >
                  Módulos
                </a>
              </li>
            </ul>
          )}
        </li>

        <li className="navItem">
          <a
            href="#"
            className="navLink"
            onClick={(e) => {
              e.preventDefault(); // Evita o comportamento padrão do link
              handleNavigation('/configuracoes'); // Navega para a rota de configurações
            }}
          >
            <FaCog className="navIcon" />
            {isExpanded && <span className="navText">Configurações</span>}
          </a>
        </li>
      </ul>

      {/* Seletor de cor */}
      <div className="colorPickerContainer">
        <button onClick={toggleColorPicker} className="colorPickerButton">
          <FaPalette className="navIcon" />
          {isExpanded && <span className="navText"></span>}
        </button>
        {isColorPickerVisible && (
          <div className="colorOptions">
            {colors.map((color, index) => (
              <div
                key={index}
                className="colorOption"
                style={{ background: color }}
                onClick={() => handleColorChange(color)}
              >
                &nbsp; {/* Espaço para garantir que o div seja renderizado */}
              </div>
            ))}
          </div>
        )}
      </div>
    </nav>
  );
};

export default NavVertical;
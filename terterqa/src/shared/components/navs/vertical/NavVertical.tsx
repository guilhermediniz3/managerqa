import React, { useState } from 'react';
import { FaBars, FaHome, FaUser, FaCog, FaPalette } from 'react-icons/fa';
import './styless.css';

const NavVertical: React.FC = () => {
  const [isExpanded, setIsExpanded] = useState<boolean>(false);
  const [isColorPickerVisible, setIsColorPickerVisible] = useState<boolean>(false);
  const [selectedColor, setSelectedColor] = useState<string>('#333333'); // Cor inicial

  const toggleExpanded = () => {
    setIsExpanded((prev) => !prev);
  };

  const toggleColorPicker = () => {
    setIsColorPickerVisible((prev) => !prev);
  };

  const handleColorChange = (color: string) => {
    setSelectedColor(color);
    setIsColorPickerVisible(false);
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
          <a href="#" className="navLink">
            <FaHome className="navIcon" />
            {isExpanded && <span className="navText">Início</span>}
          </a>
        </li>
        <li className="navItem">
          <a href="#" className="navLink">
            <FaUser className="navIcon" />
            {isExpanded && <span className="navText">Perfil</span>}
          </a>
        </li>
        <li className="navItem">
          <a href="#" className="navLink">
            <FaCog className="navIcon" />
            {isExpanded && <span className="navText">Configurações</span>}
          </a>
        </li>
      </ul>

      {/* Seletor de cor */}
      <div className="colorPickerContainer">
        <button onClick={toggleColorPicker} className="colorPickerButton">
          <FaPalette className="navIcon" />
          {isExpanded && <span className="navText">Escolher Cor</span>}
        </button>
        {isColorPickerVisible && (
          <div className="colorOptions">
            <div
              className="colorOption"
              style={{ backgroundColor: '#333333' }}
              onClick={() => handleColorChange('#333333')}
            />
            <div
              className="colorOption"
              style={{ backgroundColor: '#FF0000' }}
              onClick={() => handleColorChange('#FF0000')}
            />
            <div
              className="colorOption"
              style={{ backgroundColor: '#32cd32' }}
              onClick={() => handleColorChange('#32cd32')}
            />
            <div
              className="colorOption"
              style={{ backgroundColor: '#2E5CE6' }}
              onClick={() => handleColorChange('#2E5CE6')}
            />
            <div
              className="colorOption"
              style={{ backgroundColor: '#2ED0E6' }}
              onClick={() => handleColorChange('#2ED0E6')}
            />
            <div
              className="colorOption"
              style={{ backgroundColor: '#812EE6' }}
              onClick={() => handleColorChange('#812EE6')}
            />
            <div
              className="colorOption"
              style={{
                background: 'linear-gradient(to right, #5A48F9, #312787)',
              }}
              onClick={() =>
                handleColorChange('linear-gradient(to right, #5A48F9, #312787)')
              }
            />
            {/* Adicione mais opções de cores conforme necessário */}
          </div>
        )}
      </div>
    </nav>
  );
};

export default NavVertical;

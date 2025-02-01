import React, { useState } from 'react';
import { FaBars, FaHome, FaUser, FaCog, FaPalette } from 'react-icons/fa';
import './styless.css';

const NavVertical: React.FC = () => {
  const [isExpanded, setIsExpanded] = useState<boolean>(false);
  const [isColorPickerVisible, setIsColorPickerVisible] = useState<boolean>(false);
  const [selectedColor, setSelectedColor] = useState<string>('#333333'); // Cor inicial

  const toggleExpanded = () => {
    setIsExpanded(prev => !prev);
  };

  const toggleColorPicker = () => {
    setIsColorPickerVisible(prev => !prev);
  };

  const handleColorChange = (color: string) => {
    setSelectedColor(color);
    setIsColorPickerVisible(false);
  };

  return (
    <nav className={`navVertical ${isExpanded ? 'expanded' : 'collapsed'}`} style={{ backgroundColor: selectedColor }}>
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
              style={{ backgroundColor: '#86168a' }}
              onClick={() => handleColorChange('#86168a')}
            />
            <div
              className="colorOption"
              style={{ backgroundColor: '#ff6347' }}
              onClick={() => handleColorChange('#ff6347')}
            />      
                          
                       <div
              className="colorOption"
              style={{ backgroundColor: '#333333' }}
              onClick={() => handleColorChange('#333333')}  
            />
                       <div
              className="colorOption"
              style={{ backgroundColor: '#4682B4' }}
              onClick={() => handleColorChange('#4682B4')}
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
              style={{ backgroundColor: '#708090' }}
              onClick={() => handleColorChange('#708090')}
            />
            
            <div
              className="colorOption"
              style={{ backgroundColor: '#' }}
              onClick={() => handleColorChange('#FFD700')}
            />
      
            {/* Adicione mais opções de cores conforme necessário */}
          </div>
        )}
      </div>
    </nav>
  );
};

export default NavVertical;

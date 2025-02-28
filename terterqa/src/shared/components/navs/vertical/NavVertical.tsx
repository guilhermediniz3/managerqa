import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaBars, FaHome, FaUser, FaCog, FaPalette, FaUserPlus } from "react-icons/fa";
import { BiTransfer } from "react-icons/bi";
import Form from 'react-bootstrap/Form';
import "./styless.css";

const NavVertical: React.FC = () => {
  const navigate = useNavigate();
  const [isExpanded, setIsExpanded] = useState(false);
  const [isColorPickerVisible, setIsColorPickerVisible] = useState(false);
  const [selectedColor, setSelectedColor] = useState(
    localStorage.getItem("selectedColor") || "#333333"
  );
  const [isCadastroSubmenuOpen, setIsCadastroSubmenuOpen] = useState(false);
  const [isMovimentacaoSubmenuOpen, setIsMovimentacaoSubmenuOpen] = useState(false);
  const [isCustomColorPickerOpen, setIsCustomColorPickerOpen] = useState(false);

  const toggleExpanded = () => setIsExpanded((prev) => !prev);
  const toggleColorPicker = () => setIsColorPickerVisible((prev) => !prev);
  const toggleCadastroSubmenu = () => setIsCadastroSubmenuOpen((prev) => !prev);
  const toggleMovimentacaoSubmenu = () => setIsMovimentacaoSubmenuOpen((prev) => !prev);
  const toggleCustomColorPicker = () => setIsCustomColorPickerOpen((prev) => !prev);

  const handleColorChange = (color: string) => {
    setSelectedColor(color);
    localStorage.setItem("selectedColor", color);
    setIsColorPickerVisible(false);
  };

  const handleCustomColorChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const color = event.target.value;
    handleColorChange(color);
  };

  const handleNavigation = (path: string, event: React.MouseEvent) => {
    event.preventDefault();
    navigate(path);
  };

  return (
    <nav
      className={`navVertical ${isExpanded ? "expanded" : "collapsed"}`}
      style={{ background: selectedColor }}
    >
      <div className="verticalHeader">
        <button onClick={toggleExpanded} className="expandButton">
          <FaBars className="navIcon" />
        </button>
      </div>

      <ul className="navList">
        <li className="navItem">
          <a href="#" className="navLink" onClick={(e) => handleNavigation("/dashboard", e)}>
            <FaHome className="navIcon" />
            {isExpanded && <span className="navText">Início</span>}
          </a>
        </li>
        <li className="navItem">
          <a href="#" className="navLink" onClick={(e) => handleNavigation("/perfil", e)}>
            <FaUser className="navIcon" />
            {isExpanded && <span className="navText">Perfil</span>}
          </a>
        </li>
        <li className="navItem">
          <a href="#" className="navLink" onClick={toggleCadastroSubmenu}>
            <FaUserPlus className="navIcon" />
            {isExpanded && <span className="navText">Cadastro</span>}
          </a>
          {isCadastroSubmenuOpen && (
            <ul className="submenu">
              <li className="submenuItem">
                <a href="#" onClick={(e) => handleNavigation("/usuarios", e)}>Usuário</a>
              </li>
              <li className="submenuItem">
                <a href="#" onClick={(e) => handleNavigation("/tester/listagem", e)}>Tester</a>
              </li>
              <li className="submenuItem">
                <a href="#" onClick={(e) => handleNavigation("/desenvolvedores/listagem", e)}>Desenvolvedor</a>
              </li>
              <li className="submenuItem">
                <a href="#" onClick={(e) => handleNavigation("/tecnologia/listagem", e)}>Tecnologias</a>
              </li>
              <li className="submenuItem">
                <a href="#" onClick={(e) => handleNavigation("/modulo/listagem", e)}>Módulos</a>
              </li>
            </ul>
          )}
        </li>
        <li className="navItem">
          <a href="#" className="navLink" onClick={toggleMovimentacaoSubmenu}>
            <BiTransfer className="navIcon" />
            {isExpanded && <span className="navText">Movimentação</span>}
          </a>
          {isMovimentacaoSubmenuOpen && (
            <ul className="submenu">
              <li className="submenuItem">
                <a href="#" onClick={(e) => handleNavigation("/planoTeste/listagem", e)}>Testes</a>
              </li>
              <li className="submenuItem">
                <a href="#" onClick={(e) => handleNavigation("/planoTeste/cadastrar", e)}>UL</a>
              </li>
            </ul>
            
          )}
        </li>
        <li className="navItem">
          <a href="#" className="navLink" onClick={(e) => handleNavigation("/configuracoes", e)}>
            <FaCog className="navIcon" />
            {isExpanded && <span className="navText">Configurações</span>}
          </a>
        </li>
      </ul>

      <div className="colorPickerContainer">
        <button onClick={toggleColorPicker} className="colorPickerButton">
          <FaPalette className="navIcon" />
        </button>
        {isColorPickerVisible && (
          <div className="colorOptions">
            {["#32cd32", "#333333", "#2E5CE6", "#2ED0E6", "#812EE6", "linear-gradient(355deg, #da60fd 0%, #b249f0 25%, #802ddf 50%, #3e14ce 75%, #000bc1 100%)", "radial-gradient(circle at 56.16% 120.44%, #ff2dca 0%, #ff26d5 10%, #ff26dc 20%, #ff28df 30%, #ff2bdd 40%, #df2dd6 50%, #b72ecc 60%, #9130c3 70%, #6c31ba 80%, #4432b2 90%, #0033ab 100%)", "radial-gradient(circle at 56.16% 120.44%, #ff64db 0%, #ff57dd 16.67%, #fe45db 33.33%, #df2dd6 50%, #be12d1 66.67%, #9d03d0 83.33%, #7c0dd1 100%)"].map((color, index) => (
              <div
                key={index}
                className="colorOption"
                style={{
                  background: color.includes("gradient") ? undefined : color,
                  backgroundImage: color.includes("gradient") ? color : undefined,
                  cursor: "pointer",
                }}
                onClick={() => handleColorChange(color)}
              />
            ))}
            <div
              className="colorOption"
              style={{ background: "#ffffff", cursor: "pointer", display: "flex", alignItems: "center", justifyContent: "center" }}
              onClick={toggleCustomColorPicker}
            >
              🎨
            </div>
          </div>
        )}
        {isCustomColorPickerOpen && (
          <Form.Group controlId="customColorInput">
            <Form.Label>Escolha sua cor</Form.Label>
            <Form.Control
              type="color"
              defaultValue={selectedColor}
              title="Escolha uma cor"
              onChange={handleCustomColorChange}
            />
          </Form.Group>
        )}
      </div>
    </nav>
  );
};

export default NavVertical;

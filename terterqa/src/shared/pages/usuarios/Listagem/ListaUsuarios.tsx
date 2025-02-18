import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaEdit, FaUserPlus, FaToggleOn, FaToggleOff } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import NavVertical from "../../../components/navs/vertical/NavVertical";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import "../Listagem/styless.css";

interface Usuario {
  id: number;
  name: string;
  email: string;
  active: boolean;
}

const ListagemUsuariosPage = () => {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [termoPesquisa, setTermoPesquisa] = useState(""); // Estado para o termo de pesquisa
  const navigate = useNavigate();

  // Buscar usuários da API
  const buscarUsuarios = async () => {
    try {
      const response = await axios.get("http://localhost:8081/api/users");
      setUsuarios(response.data);
      setCarregando(false);
    } catch (error) {
      setErro("Erro ao carregar usuários.");
      setCarregando(false);
    }
  };

  // Filtrar usuários com base no termo de pesquisa
  const usuariosFiltrados = usuarios.filter((usuario) => {
    // Divide o termo de pesquisa em palavras separadas por vírgula
    const termos = termoPesquisa
      .toLowerCase()
      .split(",")
      .map((termo) => termo.trim()) // Remove espaços extras
      .filter((termo) => termo.length > 0); // Remove termos vazios

    // Verifica se todas as palavras estão presentes no nome ou email do usuário
    return termos.every((termo) =>
      usuario.name.toLowerCase().includes(termo) ||
      usuario.email.toLowerCase().includes(termo)||
      (termoPesquisa.toLowerCase() === "ativo" && usuario.active) ||
      (termoPesquisa.toLowerCase() === "inativo" && !usuario.active)
    );
  });

  useEffect(() => {
    buscarUsuarios();
  }, []);

  const inativarUsuario = async (id: number, active: boolean) => {
    if (!id || id <= 0) {
      alert("ID inválido para inativação.");
      return;
    }
    if (window.confirm("Tem certeza que deseja alterar o status deste usuário?")) {
      try {
        const novoStatus = !active;
        const response = await axios.patch(`http://localhost:8081/api/users/${id}`, { active: novoStatus });
        console.log("Resposta da inativação:", response);
        setUsuarios((prevUsuarios) =>
          prevUsuarios.map((usuario) =>
            usuario.id === id ? { ...usuario, active: novoStatus } : usuario
          )
        );
        alert(`Usuário ${novoStatus ? "ativado" : "inativado"} com sucesso.`);
      } catch (error) {
        console.error("Erro ao alterar o status do usuário:", error);
        alert("Erro ao alterar o status do usuário.");
      }
    }
  };

  const editarUsuario = (id: number) => {
    navigate(`/usuarios/editar/${id}`);
  };

  if (carregando) return <div>Carregando...</div>;
  if (erro) return <div>{erro}</div>;

  return (
    <>
      <NavHorizontal />
      <NavVertical />
      <br/>
      <br/>
      <br/>
      <div className="container mt-4">
        <h1> Usuários</h1>
        {/* Campo de Pesquisa */}
        <input
          type="text"
          placeholder="Pesquisar por nome, email ou partes separadas por vírgula..."
          value={termoPesquisa}
          onChange={(e) => setTermoPesquisa(e.target.value)}
          className="search-bar"
        />
        {/* Botão Novo Usuário */}
        <button className="btn btn-primary mb-3 btn-novo-usuario" onClick={() => navigate("/usuarios/register")}>
          <FaUserPlus /> Novo Usuário
        </button>
        {/* Tabela de Usuários */}
        <table className="table table-striped">
          <thead>
            <tr>
              <th>Usuário</th>
              <th>Email</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {usuariosFiltrados.map((usuario) => (
              <tr key={usuario.id}>
                <td>{usuario.name}</td>
                <td>{usuario.email}</td>
                <td>{usuario.active ? "Ativo" : "Inativo"}</td>
                <td>
                  <div className="action-icons">
                    <FaEdit
                      className="edit-icon"
                      onClick={() => editarUsuario(usuario.id)}
                    />
                    {usuario.active ? (
                      <FaToggleOn
                        className="active-icon"
                        onClick={() => inativarUsuario(usuario.id, usuario.active)}
                      />
                    ) : (
                      <FaToggleOff
                        className="inactive-icon"
                        onClick={() => inativarUsuario(usuario.id, usuario.active)}
                      />
                    )}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
};

export default ListagemUsuariosPage;
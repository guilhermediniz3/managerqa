import React, { useEffect, useState } from "react";
import axios, { AxiosError } from "axios"; // Import AxiosError
import { FaEdit, FaUserPlus, FaToggleOn, FaToggleOff } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import NavVertical from "../../../components/navs/vertical/NavVertical";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import "../listagem/styless.css";

interface Module {
  id: number;
  name: string;
  active: boolean;
}

const ListagemtecnologiaPage = () => {
  const [tecnologias, settecnologias] = useState<Module[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [termoPesquisa, setTermoPesquisa] = useState("");
  const navigate = useNavigate();

  // Buscar módulos da API
  const buscartecnologias = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        setErro("Token de autenticação não encontrado.");
        setCarregando(false);
        return;
      }
      const response = await axios.get("http://localhost:8081/technologies", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.status === 200 && Array.isArray(response.data)) {
        settecnologias(response.data); // Define a lista de módulos
      } else {
        settecnologias([]); // Se não for array, define lista vazia
      }
    } catch (error) {
      console.error("Erro ao carregar tecnologias:", error);
      setErro("Erro ao carregar tecnologias. Verifique o console para mais detalhes.");
    } finally {
      setCarregando(false); // Finaliza carregamento mesmo em caso de erro
    }
  };

  // Filtrar módulos com base no termo de pesquisa
  const tecnologiasFiltrados = tecnologias.filter((tecnologia) => {
    const termos = termoPesquisa
      .toLowerCase()
      .split(",")
      .map((termo) => termo.trim())
      .filter((termo) => termo.length > 0);
    return termos.every(
      (termo) =>
        tecnologia.name.toLowerCase().includes(termo) ||
        String(tecnologia.id).includes(termo) ||
        (termo === "ativo" && tecnologia.active) ||
        (termo === "inativo" && !tecnologia.active)
    );
  });

  useEffect(() => {
    buscartecnologias();
  }, []);

  // Função para ativar/inativar módulo
  const inativartecnologia = async (id: number, active: boolean) => {
    if (!id || id <= 0) {
      alert("ID inválido para alteração de status.");
      return;
    }
    if (window.confirm("Tem certeza que deseja alterar o status deste módulo?")) {
      try {
        const novoStatus = !active;
        // Configura o token no cabeçalho da requisição
        const token = localStorage.getItem("token");
        if (!token) {
          alert("Token de autenticação não encontrado.");
          return;
        }
        // Envia a requisição PATCH para o backend
        const response = await axios.patch(
          `http://localhost:8081/technologies/${id}`,
          { active: novoStatus }, // Corpo da requisição com o novo status
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log("Resposta da alteração de status:", response.data);
        // Atualiza o estado dos módulos localmente
        settecnologias((prevtecnologias) =>
          prevtecnologias.map((tecnologia) =>
            tecnologia.id === id ? { ...tecnologia, active: novoStatus } : tecnologia
          )
        );
        alert(`tecnologia ${novoStatus ? "ativado" : "inativado"} com sucesso.`);
      } catch (error) {
        // Tratamento de erro com verificação de tipo
        if (axios.isAxiosError(error)) {
          const axiosError = error as AxiosError; // Converte para AxiosError
          if (axiosError.response) {
            const responseData = axiosError.response.data;
            // Verifica se responseData é um objeto com a propriedade message
            const errorMessage =
              typeof responseData === "object" &&
              responseData !== null &&
              "message" in responseData
                ? (responseData as { message: string }).message
                : "Erro ao alterar o status da tecnologia.";
            console.error("Erro na resposta do servidor:", responseData);
            alert(`Erro: ${errorMessage}`);
          } else if (axiosError.request) {
            console.error("Erro na requisição:", axiosError.request);
            alert("Erro na requisição. Verifique sua conexão com o servidor.");
          } else {
            console.error("Erro ao configurar a requisição:", axiosError.message);
            alert("Erro ao configurar a requisição.");
          }
        } else {
          console.error("Erro desconhecido:", error);
          alert("Ocorreu um erro desconhecido.");
        }
      }
    }
  };

  // Função para editar módulo
  const editartecnologia = (id: number) => {
    navigate(`/tecnologia/editar/${id}`);
  };

  if (carregando) return <div>Carregando...</div>;
  if (erro) return <div>{erro}</div>;

  return (
    <>
      <NavHorizontal />
      <NavVertical />
      <br />
      <br />
      <br />
      <div className="container mt-4">
        <h1>Tecnologias</h1>
        {/* Campo de Pesquisa */}
        <input
          type="text"
          placeholder="Pesquisar por nome ou status (ativo/inativo)"
          value={termoPesquisa}
          onChange={(e) => setTermoPesquisa(e.target.value)}
          className="search-bar"
        />
        {/* Botão Novo Módulo */}
        <button
          className="btn btn-primary mb-3 btn-novo-usuario"
          onClick={() => navigate("/tecnologia/cadastrar")}
        >
          <FaUserPlus /> Nova tecnologia
        </button>
        {/* Tabela de Módulos */}
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {tecnologiasFiltrados.length === 0 ? (
              <tr>
                <td colSpan={4} className="text-center">
                  Nenhuma tecnologia encontrado.
                </td>
              </tr>
            ) : (
              tecnologiasFiltrados.map((tecnologia) => (
                <tr key={tecnologia.id}>
                  <td>{tecnologia.id}</td>
                  <td>{tecnologia.name}</td>
                  <td>{tecnologia.active ? "Ativo" : "Inativo"}</td>
                  <td>
                    <div className="action-icons">
                      <FaEdit
                        className="edit-icon"
                        onClick={() => editartecnologia(tecnologia.id)}
                      />
                      {tecnologia.active ? (
                        <FaToggleOn
                          className="active-icon"
                          onClick={() => inativartecnologia(tecnologia.id, tecnologia.active)}
                        />
                      ) : (
                        <FaToggleOff
                          className="inactive-icon"
                          onClick={() => inativartecnologia(tecnologia.id, tecnologia.active)}
                        />
                      )}
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </>
  );
};

export default ListagemtecnologiaPage;
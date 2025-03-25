import React, { useEffect, useState } from "react";
import axios, { AxiosError } from "axios"; // Import AxiosError
import { FaEdit, FaUserPlus, FaToggleOn, FaToggleOff } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import NavVertical from "../../../components/navs/vertical/NavVertical";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import "../Listagem/styless.css";

interface Tester {
  id: number;
  name: string;
  active: boolean;
}

const ListagemTesterPage = () => {
  const [testers, setTesters] = useState<Tester[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [termoPesquisa, setTermoPesquisa] = useState("");
  const navigate = useNavigate();

  // Buscar testers da API
  const buscarTesters = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        setErro("Token de autenticação não encontrado.");
        setCarregando(false);
        return;
      }
      const response = await axios.get("http://localhost:8081/testers", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.status === 200 && Array.isArray(response.data)) {
        setTesters(response.data); // Define a lista de testers
      } else {
        setTesters([]); // Se não for array, define lista vazia
      }
    } catch (error) {
      console.error("Erro ao carregar testers:", error);
      setErro("Erro ao carregar testers. Verifique o console para mais detalhes.");
    } finally {
      setCarregando(false); // Finaliza carregamento mesmo em caso de erro
    }
  };

  // Filtrar testers com base no termo de pesquisa
  const testersFiltrados = testers.filter((tester) => {
    const termos = termoPesquisa
      .toLowerCase()
      .split(",")
      .map((termo) => termo.trim())
      .filter((termo) => termo.length > 0);
    return termos.every(
      (termo) =>
        tester.name.toLowerCase().includes(termo) ||
      String(tester.id).includes(termo) ||
        (termo === "ativo" && tester.active) ||
        (termo === "inativo" && !tester.active)
    );
  });

  useEffect(() => {
    buscarTesters();
  }, []);

  // Função para ativar/inativar tester
  const inativarTester = async (id: number, active: boolean) => {
    if (!id || id <= 0) {
      alert("ID inválido para alteração de status.");
      return;
    }

    if (window.confirm("Tem certeza que deseja alterar o status deste tester?")) {
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
          `http://localhost:8081/testers/${id}`,
          { active: novoStatus }, // Corpo da requisição com o novo status
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        console.log("Resposta da alteração de status:", response.data);

        // Atualiza o estado dos testers localmente
        setTesters((prevTesters) =>
          prevTesters.map((tester) =>
            tester.id === id ? { ...tester, active: novoStatus } : tester
          )
        );

        alert(`Tester ${novoStatus ? "ativado" : "inativado"} com sucesso.`);
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
                : "Erro ao alterar o status do tester.";

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

  // Função para editar tester
  const editarTester = (id: number) => {
    navigate(`/tester/editar/${id}`);
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
        <h1>Testers</h1>
        {/* Campo de Pesquisa */}
        <input
          type="text"
          placeholder="Pesquisar por nome ou status (ativo/inativo)"
          value={termoPesquisa}
          onChange={(e) => setTermoPesquisa(e.target.value)}
          className="search-bar"
        />
        {/* Botão Novo Tester */}
        <button
          className="btn btn-primary mb-3 btn-novo-usuario"
          onClick={() => navigate("/tester/cadastrar")}
        >
          <FaUserPlus /> Novo Tester
        </button>
        {/* Tabela de Testers */}
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
            {testersFiltrados.length === 0 ? (
              <tr>
                <td colSpan={4} className="text-center">
                  Nenhum tester encontrado.
                </td>
              </tr>
            ) : (
              testersFiltrados.map((tester) => (
                <tr key={tester.id}>
                  <td>{tester.id}</td>
                  <td>{tester.name}</td>
                  <td>{tester.active ? "Ativo" : "Inativo"}</td>
                  <td>
                    <div className="action-icons">
                      <FaEdit
                        className="edit-icon"
                        onClick={() => editarTester(tester.id)}
                      />
                      {tester.active ? (
                        <FaToggleOn
                          className="active-icon"
                          onClick={() => inativarTester(tester.id, tester.active)}
                        />
                      ) : (
                        <FaToggleOff
                          className="inactive-icon"
                          onClick={() => inativarTester(tester.id, tester.active)}
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

export default ListagemTesterPage;
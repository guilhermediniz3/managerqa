import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaEdit, FaUserPlus, FaToggleOn, FaToggleOff } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import NavVertical from "../../../components/navs/vertical/NavVertical";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import "../listagem/styless.css";

interface Desenvolvedor {
  id: number;
  name: string;
  active: boolean;
  technologyIds: number[];
}

interface Tecnologia {
  id: number;
  name: string;
}

const ListagemDesenvolvedoresPage = () => {
  const [desenvolvedores, setDesenvolvedores] = useState<Desenvolvedor[]>([]);
  const [tecnologias, setTecnologias] = useState<Tecnologia[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [termoPesquisa, setTermoPesquisa] = useState("");
  const navigate = useNavigate();

  // Buscar tecnologias disponíveis
  const buscarTecnologias = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8081/technologies", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setTecnologias(response.data);
    } catch (error) {
      console.error("Erro ao carregar tecnologias:", error);
    }
  };

  // Buscar desenvolvedores da API
  const buscarDesenvolvedores = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8081/developers", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setDesenvolvedores(response.data);
      setCarregando(false);
    } catch (error) {
      setErro("Erro ao carregar desenvolvedores.");
      setCarregando(false);
    }
  };

  useEffect(() => {
    buscarTecnologias();
    buscarDesenvolvedores();
  }, []);

  // Função para obter os nomes das tecnologias com base nos IDs
  const getNomesTecnologias = (ids: number[]) => {
    return ids
      .map((id) => tecnologias.find((tec) => tec.id === id)?.name)
      .filter(Boolean) // Remove valores undefined
      .join(", ");
  };

  const inativarDesenvolvedor = async (id: number, active: boolean) => {
    if (window.confirm("Tem certeza que deseja alterar o status deste desenvolvedor?")) {
      try {
        const novoStatus = !active;
        // Procura o desenvolvedor atual para obter o nome e tecnologias
        const currentDev = desenvolvedores.find(dev => dev.id === id);
        await axios.patch(`http://localhost:8081/developers/${id}`, { 
          name: currentDev?.name, 
          active: novoStatus,
          technologyIds: currentDev?.technologyIds
        });
        setDesenvolvedores((prevDesenvolvedores) =>
          prevDesenvolvedores.map((desenvolvedor) =>
            desenvolvedor.id === id ? { ...desenvolvedor, active: novoStatus } : desenvolvedor
          )
        );
        alert(`Desenvolvedor ${novoStatus ? "ativado" : "inativado"} com sucesso.`);
      } catch (error) {
        alert("Erro ao alterar o status do desenvolvedor.");
      }
    }
  };

  const editarDesenvolvedor = (id: number) => {
    navigate(`/desenvolvedores/editar/${id}`);
  };

  // Função de filtro de desenvolvedores com base nos critérios de pesquisa
  const filtrarDesenvolvedores = () => {
    const termos = termoPesquisa.split(",").map((term) => term.trim().toLowerCase());
    
    return desenvolvedores.filter((desenvolvedor) => {
      // Verificar se o nome do desenvolvedor contém algum dos termos
      const nomeMatch = termos.some((term) => desenvolvedor.name.toLowerCase().includes(term));

      // Verificar se alguma tecnologia do desenvolvedor contém algum dos termos
      const tecnologiasMatch = termos.some((term) =>
        desenvolvedor.technologyIds
          .map((id) => tecnologias.find((tec) => tec.id === id)?.name?.toLowerCase())
          .some((tecName) => tecName?.includes(term))
      );

      // Verificar se o status do desenvolvedor corresponde ao termo (ativo ou inativo)
      const statusMatch = termos.some((term) => {
        return term === "ativo" && desenvolvedor.active || term === "inativo" && !desenvolvedor.active;
      });

      return nomeMatch || tecnologiasMatch || statusMatch;
    });
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
        <h1> Desenvolvedores</h1>
        <input
          type="text"
          placeholder="Pesquisar por nome, tecnologias ou status (ativo, inativo)..."
          value={termoPesquisa}
          onChange={(e) => setTermoPesquisa(e.target.value)}
          className="search-bar"
        />
        <button className="btn btn-primary mb-3 btn-novo-usuario" onClick={() => navigate("/desenvolvedores/cadastrar")}>
          <FaUserPlus /> Novo Desenvolvedor
        </button>
        <table className="table table-striped">
          <thead>
            <tr>
              <th>Desenvolvedor</th>
              <th>Tecnologias</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {filtrarDesenvolvedores().map((desenvolvedor) => (
              <tr key={desenvolvedor.id}>
                <td>{desenvolvedor.name}</td>
                <td>{getNomesTecnologias(desenvolvedor.technologyIds)}</td>
                <td>{desenvolvedor.active ? "Ativo" : "Inativo"}</td>
                <td>
                  <div className="action-icons">
                    <FaEdit className="edit-icon" onClick={() => editarDesenvolvedor(desenvolvedor.id)} />
                    {desenvolvedor.active ? (
                      <FaToggleOn
                        className="active-icon"
                        onClick={() => inativarDesenvolvedor(desenvolvedor.id, desenvolvedor.active)}
                      />
                    ) : (
                      <FaToggleOff
                        className="inactive-icon"
                        onClick={() => inativarDesenvolvedor(desenvolvedor.id, desenvolvedor.active)}
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

export default ListagemDesenvolvedoresPage;

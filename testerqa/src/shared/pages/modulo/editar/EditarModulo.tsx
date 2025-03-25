import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import { FaToggleOn, FaToggleOff } from "react-icons/fa";
import "./styless.css"; // Importando o CSS
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import NavVertical from "../../../components/navs/vertical/NavVertical";

interface Modulo {
  id: number;
  name: string;
  email: string;
  active: boolean;
}

const EditarModuloPage = () => {
  const { id } = useParams<{ id: string }>(); // Obtém o ID do módulo da URL
  const navigate = useNavigate();
  const [modulo, setModulo] = useState<Modulo | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [mensagemSucesso, setMensagemSucesso] = useState<string | null>(null); // Estado para mensagem de sucesso

  // Buscar os dados do módulo ao carregar o componente
  useEffect(() => {
    const buscarModulo = async () => {
      try {
        const response = await axios.get(`http://localhost:8081/modules/${id}`);
        setModulo(response.data);
        setCarregando(false);
      } catch (error) {
        setErro("Erro ao carregar módulo.");
        setCarregando(false);
      }
    };
    buscarModulo();
  }, [id]);

  // Função para salvar as alterações usando PATCH
  const salvarAlteracoes = async () => {
    if (!modulo) return;
    try {
      await axios.patch(`http://localhost:8081/modules/${id}`, modulo); // Usa PATCH em vez de PUT
      setMensagemSucesso("Módulo atualizado com sucesso!"); // Exibe mensagem de sucesso
    } catch (error) {
      console.error("Erro ao atualizar módulo:", error);
      alert("Erro ao atualizar módulo.");
    }
  };

  // Função para voltar à listagem
  const voltarParaListagem = () => {
    navigate("/modulo/listagem"); // Redireciona para a listagem de módulos
  };

  // Renderização condicional
  if (carregando) return <div>Carregando...</div>;
  if (erro) return <div>{erro}</div>;
  if (!modulo) return <div>Módulo não encontrado.</div>;

  return (
    <>
      <NavHorizontal />
      <NavVertical />
      <br />
      <br />
      <br />

      <div className="container mt-4">
        <h1>Editar Módulo</h1>

        {/* Mensagem de Sucesso */}
        {mensagemSucesso && (
          <div className="alert alert-success" role="alert">
            {mensagemSucesso}
          </div>
        )}

        {/* Formulário de Edição */}
        <form
          onSubmit={(e) => {
            e.preventDefault();
            salvarAlteracoes();
          }}
        >
          <div className="mb-3">
            <label htmlFor="name" className="form-label">Nome</label>
            <input
              type="text"
              className="form-control"
              id="name"
              value={modulo.name}
              onChange={(e) => setModulo({ ...modulo, name: e.target.value })}
            />
          </div>

          <div className="mb-3 form-check status-icons">
            <label className="form-check-label me-2">Status:</label>
            {modulo.active ? (
              <FaToggleOn
                style={{ color: "green", cursor: "pointer", fontSize: "24px" }}
                onClick={() => setModulo({ ...modulo, active: false })}
              />
            ) : (
              <FaToggleOff
                style={{ color: "gray", cursor: "pointer", fontSize: "24px" }}
                onClick={() => setModulo({ ...modulo, active: true })}
              />
            )}
          </div>

          {/* Botões Salvar e Voltar */}
          <div className="d-flex justify-content-end gap-2">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={voltarParaListagem}
            >
              Voltar
            </button>
            <button type="submit" className="btn btn-primary">
              Salvar
            </button>
          </div>
        </form>
      </div>
    </>
  );
};

export default EditarModuloPage;
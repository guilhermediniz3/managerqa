import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Toast, ToastContainer } from "react-bootstrap";
import "./styless.css";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import NavVertical from "../../../components/navs/vertical/NavVertical";

interface Module {
  id?: number;
  name: string;
  active: boolean;
}

const CriarModulo = () => {
  const navigate = useNavigate();
  const [modulo, setModulo] = useState<Module>({ name: "", active: true });
  const [showSuccessToast, setShowSuccessToast] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  // Função para criar módulo
  const criarModulo = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      // Envia uma requisição POST para o backend
      await axios.post("http://localhost:8081/modules", modulo);
      setSuccessMessage("Módulo criado com sucesso!");
      setShowSuccessToast(true);
      setTimeout(() => navigate("/modulo/cadastrar"), 2000); // Redireciona após 2 segundos
    } catch (error) {
      setErrorMessage("Erro ao criar módulo.");
      setShowErrorToast(true);
    }
  };

  return (
    <>
      <NavHorizontal />
      <NavVertical />
      <br />
      <br />
      <br />
      <div className="container mt-4">
        <h1>Cadastrar Módulo</h1>
        <form onSubmit={criarModulo}>
          {/* Campo de Nome */}
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

          {/* Checkbox Ativo */}
          <div className="mb-3 form-check">
            <input
              type="checkbox"
              className="form-check-input"
              id="active"
              checked={modulo.active}
              onChange={(e) => setModulo({ ...modulo, active: e.target.checked })}
            />
            <label className="form-check-label" htmlFor="active">Ativo</label>
          </div>

          {/* Botões de Ação */}
          <div className="d-flex justify-content-end gap-2">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => navigate("/modulo/listagem")}
            >
              Voltar
            </button>
            <button type="submit" className="btn btn-primary">Criar</button>
          </div>
        </form>

        {/* Toast de Sucesso */}
        <ToastContainer position="top-end" className="p-3">
          <Toast
            onClose={() => setShowSuccessToast(false)}
            show={showSuccessToast}
            delay={3000}
            autohide
            bg="success"
          >
            <Toast.Header>
              <strong className="me-auto">Sucesso</strong>
              <small>Agora</small>
            </Toast.Header>
            <Toast.Body>{successMessage}</Toast.Body>
          </Toast>
        </ToastContainer>

        {/* Toast de Erro */}
        <ToastContainer position="top-end" className="p-3">
          <Toast
            onClose={() => setShowErrorToast(false)}
            show={showErrorToast}
            delay={3000}
            autohide
            bg="danger"
          >
            <Toast.Header>
              <strong className="me-auto">Erro</strong>
              <small>Agora</small>
            </Toast.Header>
            <Toast.Body>{errorMessage}</Toast.Body>
          </Toast>
        </ToastContainer>
      </div>
    </>
  );
};

export default CriarModulo;
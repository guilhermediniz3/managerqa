import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Toast, ToastContainer } from "react-bootstrap";
import "./styless.css";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import NavVertical from "../../../components/navs/vertical/NavVertical";

interface Developer {
  id?: number;
  name: string;
  active: boolean;
}

const Criartester = () => {
  const navigate = useNavigate();
  const [tester, setTester] = useState<Developer>({ name: "", active: true });
  const [showSuccessToast, setShowSuccessToast] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  // Criar tester
  const criartester = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8081/testers", tester);
      setSuccessMessage("Tester criado com sucesso!");
      setShowSuccessToast(true);
      setTimeout(() => navigate("/tester/cadastrar"), 2000);
    } catch (error) {
      setErrorMessage("Erro ao criar tester.");
      setShowErrorToast(true);
    }
  };

  return (
    <>
      <NavHorizontal />
      <NavVertical />
      <br/>
      <br/>
      <br/>

      <div className="container mt-4">
        <h1>Cadastrar Tester</h1>
        <form onSubmit={criartester}>
          <div className="mb-3">
            <label htmlFor="name" className="form-label">Nome</label>
            <input
              type="text"
              className="form-control"
              id="name"
              value={tester.name}
              onChange={(e) => setTester({ ...tester, name: e.target.value })}
            />
          </div>

          <div className="mb-3 form-check">
            <input
              type="checkbox"
              className="form-check-input"
              id="active"
              checked={tester.active}
              onChange={(e) => setTester({ ...tester, active: e.target.checked })}
            />
            <label className="form-check-label" htmlFor="active">Ativo</label>
          </div>

          <div className="d-flex justify-content-end gap-2">
            <button type="button" className="btn btn-secondary" onClick={() => navigate("/tester/listagem")}>
              Voltar
            </button>
            <button type="submit" className="btn btn-primary">Criar</button>
          </div>
        </form>

        {/* Toast de Sucesso */}
        <ToastContainer position="top-end" className="p-3">
          <Toast onClose={() => setShowSuccessToast(false)} show={showSuccessToast} delay={3000} autohide bg="success">
            <Toast.Header>
              <strong className="me-auto">Sucesso</strong>
              <small>Agora</small>
            </Toast.Header>
            <Toast.Body>{successMessage}</Toast.Body>
          </Toast>
        </ToastContainer>

        {/* Toast de Erro */}
        <ToastContainer position="top-end" className="p-3">
          <Toast onClose={() => setShowErrorToast(false)} show={showErrorToast} delay={3000} autohide bg="danger">
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

export default Criartester;

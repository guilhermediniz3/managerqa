import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Toast, ToastContainer } from "react-bootstrap";
import "./styless.css";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import NavVertical from "../../../components/navs/vertical/NavVertical";

interface Technology {
  id: number;
  name: string;
  active?: boolean; // Pode ser undefined caso venha null do backend
}

interface Developer {
  id?: number;
  name: string;
  active?: boolean;
  technologyIds: number[];
}

const CriarDesenvolvedor = () => {
  const navigate = useNavigate();
  const [developer, setDeveloper] = useState<Developer>({ name: "", active: true, technologyIds: [] });
  const [technologies, setTechnologies] = useState<Technology[]>([]);
  const [showSuccessToast, setShowSuccessToast] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  // Buscar tecnologias disponíveis
  useEffect(() => {
    const fetchTechnologies = async () => {
      try {
        const response = await axios.get("http://localhost:8081/technologies");
        setTechnologies(response.data);
      } catch (error) {
        console.error("Erro ao buscar tecnologias:", error);
      }
    };
    fetchTechnologies();
  }, []);

  // Adicionar ou remover tecnologia do desenvolvedor
  const toggleTechnology = (tech: Technology) => {
    const isSelected = developer.technologyIds.includes(tech.id);
    setDeveloper({
      ...developer,
      technologyIds: isSelected
        ? developer.technologyIds.filter((id) => id !== tech.id)
        : [...developer.technologyIds, tech.id],
    });
  };

  // Criar desenvolvedor
  const criarDesenvolvedor = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8081/developers", developer);
      setSuccessMessage("Desenvolvedor criado com sucesso!");
      setShowSuccessToast(true);
      setTimeout(() => navigate("/desenvolvedores"), 2000);
    } catch (error) {
      setErrorMessage("Erro ao criar Desenvolvedor.");
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
        <h1>Criar Desenvolvedor</h1>
        <form onSubmit={criarDesenvolvedor}>
          <div className="mb-3">
            <label htmlFor="name" className="form-label">Nome</label>
            <input
              type="text"
              className="form-control"
              id="name"
              value={developer.name}
              onChange={(e) => setDeveloper({ ...developer, name: e.target.value })}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Tecnologias</label>
            <div className="technology-list">
              {technologies.map((tech) => (
                <button
                  key={tech.id}
                  type="button"
                  className={`technology-item ${
                    developer.technologyIds.includes(tech.id) ? "selected" : ""
                  } ${tech.active === false ? "disabled" : ""}`}
                  onClick={() => tech.active !== false && toggleTechnology(tech)}
                  disabled={tech.active === false} // Evita clique se estiver inativa
                >
                  {tech.name}
                </button>
              ))}
            </div>
          </div>
          <div className="d-flex justify-content-end gap-2">
            <button type="button" className="btn btn-secondary" onClick={() => navigate("/desenvolvedores/listagem")}>
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

export default CriarDesenvolvedor;

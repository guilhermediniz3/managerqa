import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { Toast, ToastContainer } from "react-bootstrap";
import "./styless.css";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import NavVertical from "../../../components/navs/vertical/NavVertical";

interface Technology {
  id: number;
  name: string;
  active?: boolean;
}

interface Developer {
  id?: number;
  name: string;
  active?: boolean;
  technologyIds: number[];
}

const EditarDesenvolvedor = () => {
  const { id } = useParams(); // Pega o ID do desenvolvedor da URL
  const navigate = useNavigate();
  const [developer, setDeveloper] = useState<Developer>({
    name: "",
    active: true,
    technologyIds: [],
  });
  const [technologies, setTechnologies] = useState<Technology[]>([]);
  const [showSuccessToast, setShowSuccessToast] = useState(false);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  // Buscar dados do desenvolvedor e tecnologias
  useEffect(() => {
    const fetchTechnologiesAndDeveloper = async () => {
      try {
        const techResponse = await axios.get("http://localhost:8081/technologies");
        setTechnologies(techResponse.data);

        const devResponse = await axios.get(`http://localhost:8081/developers/${id}`);
        const devData = devResponse.data;
        // Se o backend retornar null para o nome, usa string vazia para o input
        const nameValue = devData.name !== null ? devData.name : "";
        setDeveloper({ ...devData, name: nameValue });
      } catch (error) {
        console.error("Erro ao buscar dados:", error);
      }
    };
    fetchTechnologiesAndDeveloper();
  }, [id]);

  // Alterna a seleção de tecnologias
  const toggleTechnology = (tech: Technology) => {
    const isSelected = developer.technologyIds.includes(tech.id);
    setDeveloper({
      ...developer,
      technologyIds: isSelected
        ? developer.technologyIds.filter(tid => tid !== tech.id)
        : [...developer.technologyIds, tech.id],
    });
  };

  // Atualizar desenvolvedor (envia somente os campos necessários)
  const updateDeveloper = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    // Constrói o payload dinamicamente
    const payload: Partial<Developer> = {
      active: developer.active,
      technologyIds: developer.technologyIds,
    };
    if (developer.name && developer.name.trim() !== "") {
      payload.name = developer.name;
    }

    try {
      await axios.patch(`http://localhost:8081/developers/${id}`, payload);
      setSuccessMessage("Desenvolvedor atualizado com sucesso!");
      setShowSuccessToast(true);
    } catch (error) {
      setErrorMessage("Erro ao atualizar Desenvolvedor.");
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
        <h1>Editar Desenvolvedor</h1>
        <form onSubmit={updateDeveloper}>
          <div className="mb-3">
            <label htmlFor="name" className="form-label">Nome</label>
            <input
              type="text"
              className="form-control"
              id="name"
              value={developer.name || ""}
              onChange={(e) => setDeveloper({ ...developer, name: e.target.value })}
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Status</label>
            <div className="form-check">
              <input
                type="checkbox"
                className="form-check-input"
                id="active"
                checked={developer.active || false}
                onChange={(e) => setDeveloper({ ...developer, active: e.target.checked })}
              />
              <label className="form-check-label" htmlFor="active">
                Ativo
              </label>
            </div>
          </div>
          <div className="mb-3">
            <label className="form-label">Tecnologias</label>
            <div className="technology-list">
              {technologies.map((tech) => (
                <button
                  key={tech.id}
                  type="button"
                  className={`technology-item ${developer.technologyIds.includes(tech.id) ? "selected" : ""} ${tech.active === false ? "disabled" : ""}`}
                  onClick={() => tech.active !== false && toggleTechnology(tech)}
                  disabled={tech.active === false}
                >
                  {tech.name}
                </button>
              ))}
            </div>
          </div>
          <div className="d-flex justify-content-between gap-2">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => navigate("/desenvolvedores/listagem")}
            >
              Voltar
            </button>
            <button
              type="submit"
              className="btn btn-success"
              style={{ backgroundColor: "#28a745", borderColor: "#28a745" }}
            >
              Salvar
            </button>
          </div>
        </form>
        {/* Toasts */}
        <ToastContainer position="top-end" className="p-3">
          <Toast onClose={() => setShowSuccessToast(false)} show={showSuccessToast} delay={3000} autohide bg="success">
            <Toast.Header>
              <strong className="me-auto">Sucesso</strong>
              <small>Agora</small>
            </Toast.Header>
            <Toast.Body>{successMessage}</Toast.Body>
          </Toast>
        </ToastContainer>
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

export default EditarDesenvolvedor;

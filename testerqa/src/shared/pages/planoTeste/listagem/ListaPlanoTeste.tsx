  import React, { useState, useEffect } from "react";
  import axios from "axios";
  import Button from "react-bootstrap/Button";
  import Table from "react-bootstrap/Table";
  import Pagination from "react-bootstrap/Pagination";
  import "bootstrap/dist/css/bootstrap.min.css";
  import { FaEdit } from "react-icons/fa";
  import "./styles.css"; // Importe o arquivo CSS personalizado
  import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
  import NavVertical from "../../../components/navs/vertical/NavVertical";
  import { useNavigate } from "react-router-dom";
  import ReportModal from "../../../components/Modal/ReportModal";

  // Interface para um TestPlan
  interface TestPlan {
    id: number;
    name: string;
    data: string;
    deliveryData: string;
    status: string;
    testerQAName: string;
    developerName: string;
    jira: string;
    callNumber: string;
    systemModuleName: string;
  }

  const TestPlanPage = () => {
    const navigate = useNavigate();
    const [results, setResults] = useState<TestPlan[]>([]);
    const [loading, setLoading] = useState(true);
    const [totalPages, setTotalPages] = useState(0);
    const [error, setError] = useState<string | null>(null);
    const [showReportModal, setShowReportModal] = useState(false);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(50);
    const [searchValue, setSearchValue] = useState("");

    // Buscar todos os planos de teste
    const fetchTestPlans = async () => {
      setLoading(true);
      setError(null);
      try {
        const token = localStorage.getItem("token");

        // Monta a URL corretamente com os parâmetros
          const endpoint = searchValue
            ? `http://localhost:8081/testplans/search?searchValue=${searchValue}&page=${page}&size=${size}`
            : `http://localhost:8081/testplans/all?page=${page}&size=${size}`;

        const response = await axios.get(endpoint, {
          headers: { Authorization: `Bearer ${token}` },
        });

        setResults(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
      } catch (error) {
        console.error("Erro ao buscar os planos de teste:", error);
        setError("Erro ao carregar os dados.");
      } finally {
        setLoading(false);
      }
    };

    // Lida com a mudança do campo de busca
    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const { value } = e.target;
      setSearchValue(value);
      setPage(0); // Reseta a página para a primeira ao pesquisar
    };

    // Muda a página na paginação
    const handlePageChange = (newPage: number) => {
      setPage(newPage);
    };

    // Efeito para buscar os planos de teste quando a página ou o searchValue muda
    useEffect(() => {
      fetchTestPlans();
    }, [page, searchValue]);

    return (

      
      <div className="container">
        <NavHorizontal />
        <NavVertical />

        <br />
        <br />
        <br />

        {/* Campo de Pesquisa */}
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <input
            type="text"
            placeholder="Pesquisar por Jira ou Chamada..."
            className="search-bar"
            value={searchValue}
            onChange={handleSearchChange}
          />
        </div>

        {/* Botão Salvar */}
        <div className="button-container">
          <Button
            type="submit"
            className="btn btn-primary"
            onClick={() => navigate("/planoTeste/cadastrar")}
          >
            Nova UL
          </Button>
          <Button
            className="btn btn-success"
            onClick={() => setShowReportModal(true)}
          >
            Report
          </Button>
        </div>

        {/* Modal de Relatório */}
        <ReportModal show={showReportModal} onHide={() => setShowReportModal(false)} />

        {/* Tabela de Resultados */}
        <Table striped bordered hover className="table-custom">
          <thead>
            <tr>
              <th>UL</th>
              <th>Bitrix</th>
              <th>Descrição</th>
              <th>Data</th>
              <th>Sprint</th>
              <th>Tester</th>
              <th>Desenvolvedor</th>
              <th>Módulo</th>
              <th>Status</th>
              <th>Ações</th>
            </tr>
          </thead>
          <tbody>
            {results.map((item) => (
              <tr key={item.id}>
                <td>{item.jira}</td>
                <td>{item.callNumber}</td>
                <td>{item.name}</td>
                <td>{item.data}</td>
                <td>{item.deliveryData}</td>
                <td>{item.testerQAName}</td>
                <td>{item.developerName}</td>
                <td>{item.systemModuleName}</td>
                <td>
                  <Button variant={getStatusColor(item.status)} className="status-button-fixed">
                    {item.status}
                  </Button>
                </td>
                <td>
                  <FaEdit
                    className="icon-edit"
                    style={{ color: "#0d6efd", cursor: "pointer", marginRight: "15px" }}
                    onClick={() => navigate(`/planoTeste/editar/${item.id}`)}
                  />
                </td>
              </tr>
            ))}
          </tbody>
        </Table>

        {/* Paginação */}
        <Pagination className="pagination-custom">
          <Pagination.First onClick={() => handlePageChange(0)} disabled={page === 0} />
          <Pagination.Prev onClick={() => handlePageChange(page - 1)} disabled={page === 0} />
          {Array.from({ length: totalPages }, (_, index) => (
            <Pagination.Item key={index} active={index === page} onClick={() => handlePageChange(index)}>
              {index + 1}
            </Pagination.Item>
          ))}
          <Pagination.Next onClick={() => handlePageChange(page + 1)} disabled={page === totalPages - 1} />
          <Pagination.Last onClick={() => handlePageChange(totalPages - 1)} disabled={page === totalPages - 1} />
        </Pagination>

        {/* Mensagem de Erro ou Carregamento */}
        {error && <p>{error}</p>}
        {loading && <p>Carregando...</p>}
      </div>
    );
  };

  // Função para definir a cor do botão conforme o status
  const getStatusColor = (status: string) => {
    switch (status) {
      case "EM_PROGRESSO":
        return "outline-primary";
      case "CRIADA":
        return "outline-secondary";
      case "CONCLUIDA":
        return "outline-success";
      case "IMPEDIMENTO":
        return "outline-warning";
      case "RETORNO":
        return "outline-danger";
      default:
        return "outline-dark";
    }
  };

  export default TestPlanPage;
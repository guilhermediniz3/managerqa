import React, { useState, useEffect } from "react";
import axios from "axios";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Table from "react-bootstrap/Table";
import Pagination from "react-bootstrap/Pagination";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { FaFilter, FaTimes, FaEdit } from "react-icons/fa";
import { HiOutlineDuplicate } from "react-icons/hi";
import "./styles.css"; // Importe o arquivo CSS personalizado
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import NavVertical from "../../../components/navs/vertical/NavVertical";
import { useNavigate } from 'react-router-dom';




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

// Interface para os filtros
interface Filters {
  name: string;
  jira: string;
  callNumber: string;
  testerName: string;
  developerName: string;
  systemModuleName: string;
  status: string;
  dataInicio: Date | null;
  dataFim: Date | null;
  deliveryDataInicio: Date | null;
  deliveryDataFim: Date | null;
  page: number;
  size: number;
}

// Interface para um Tester (para preencher o dropdown)
interface Tester {
  id: number;
  name: string;
}

const TestPlanPage = () => {
  // Estados necessários para carregar a lista
  const navigate = useNavigate(); // ✅ Chamado dentro do componente funcional
  const [filters, setFilters] = useState<Filters>({
    name: "",
    jira: "",
    callNumber: "",
    testerName: "",
    developerName: "",
    systemModuleName: "",
    status: "",
    dataInicio: null,
    dataFim: null,
    deliveryDataInicio: null,
    deliveryDataFim: null,
    page: 0,
    size: 50,
  });
  const [results, setResults] = useState<TestPlan[]>([]);
  const [loading, setLoading] = useState(true);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState<string | null>(null);
  const [showFilters, setShowFilters] = useState(false);
  const [testers, setTesters] = useState<Tester[]>([]);

  // Lista de status para o select
  const statusList = [
    { value: "EM_PROGRESSO", label: "Em Progresso" },
    { value: "CRIADA", label: "Criada" },
    { value: "CONCLUIDA", label: "Concluída" },
    { value: "IMPEDIMENTO", label: "Impedimento" },
    { value: "RETORNO", label: "Retorno" },
  ];

  // Buscar a lista de testers (usado no dropdown)
  const fetchTesters = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8081/testers", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setTesters(response.data);
    } catch (error) {
      console.error("Erro ao buscar a lista de testers:", error);
      setError("Erro ao carregar a lista de testers.");
    }
  };

  // Buscar todos os planos de teste (sem filtros)
  const fetchAllTestPlans = async () => {
    setLoading(true);
    setError(null);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8081/testplans/all", {
        params: { page: filters.page, size: filters.size },
        headers: { Authorization: `Bearer ${token}` },
      });
      setResults(response.data.content || []);
      setTotalPages(response.data.totalPages || 0);
    } catch (error) {
      console.error("Erro ao buscar todos os planos de teste:", error);
      setError("Erro ao carregar os dados.");
    } finally {
      setLoading(false);
    }
  };

  // Buscar planos de teste filtrados (envia somente os parâmetros com valor definido)
  const fetchFilteredTestPlans = async () => {
    setLoading(true);
    setError(null);
    try {
      const token = localStorage.getItem("token");
      const params: Record<string, any> = { page: filters.page, size: filters.size };
      if (filters.name) params.name = filters.name;
      if (filters.jira) params.jira = filters.jira;
      if (filters.callNumber) params.callNumber = filters.callNumber;
      if (filters.testerName) params.testerName = filters.testerName;
      if (filters.developerName) params.developerName = filters.developerName;
      if (filters.systemModuleName) params.systemModuleName = filters.systemModuleName;
      if (filters.status) params.status = filters.status;
      if (filters.dataInicio) params.dataInicio = filters.dataInicio.toISOString();
      if (filters.dataFim) params.dataFim = filters.dataFim.toISOString();
      if (filters.deliveryDataInicio) params.deliveryDataInicio = filters.deliveryDataInicio.toISOString();
      if (filters.deliveryDataFim) params.deliveryDataFim = filters.deliveryDataFim.toISOString();

      const response = await axios.get("http://localhost:8081/testplans/all", {
        params,
        headers: { Authorization: `Bearer ${token}` },
      });
      setResults(response.data.content || []);
      setTotalPages(response.data.totalPages || 0);
    } catch (error) {
      console.error("Erro ao buscar os planos de teste filtrados:", error);
      setError("Erro ao carregar os dados filtrados.");
    } finally {
      setLoading(false);
    }
  };

  // Alterna a visibilidade dos filtros extras
  const toggleFilters = () => {
    setShowFilters(!showFilters);
  };

  // Limpa todos os filtros e recarrega os planos sem filtro
  const clearFilters = () => {
    setFilters({
      name: "",
      jira: "",
      callNumber: "",
      testerName: "",
      developerName: "",
      systemModuleName: "",
      status: "",
      dataInicio: null,
      dataFim: null,
      deliveryDataInicio: null,
      deliveryDataFim: null,
      page: 0,
      size: 10,
    });
    fetchAllTestPlans();
  };

  // Lida com a mudança do campo de busca (aplica o mesmo valor a vários filtros)
  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setFilters((prev) => ({
      ...prev,
      name: value,
      jira: value,
      callNumber: value,
      testerName: value,
      developerName: value,
      systemModuleName: value,
      page: 0,
    }));
  };

  // Lida com a mudança dos filtros individuais
  const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  // Lida com a mudança das datas
  const handleDateChange = (date: Date | null, field: string) => {
    setFilters((prev) => ({ ...prev, [field]: date }));
  };

  // Define a cor do botão conforme o status
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

  // Muda a página na paginação
  const handlePageChange = (page: number) => {
    setFilters((prev) => ({ ...prev, page }));
  };

  // Efeito para buscar os planos de teste quando os filtros mudam
  useEffect(() => {
    if (
      filters.name ||
      filters.jira ||
      filters.callNumber ||
      filters.testerName ||
      filters.developerName ||
      filters.systemModuleName ||
      filters.status ||
      filters.dataInicio ||
      filters.dataFim ||
      filters.deliveryDataInicio ||
      filters.deliveryDataFim
    ) {
      fetchFilteredTestPlans();
    } else {
      fetchAllTestPlans();
    }
  }, [filters]);

  // Efeito para buscar a lista de testers
  useEffect(() => {
    fetchTesters();
  }, []);

  


  return (
    <div className="container">
      <NavHorizontal />
      <NavVertical />
      

      {/* Campo de Pesquisa e Ícone de Filtro */}
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <input
          type="text"
          placeholder="Pesquisar por nome, Jira, chamada, tester, desenvolvedor ou módulo..."
          className="search-bar"
          value={filters.name}
          onChange={handleSearchChange}
        />
        <FaFilter className="filter-icon" onClick={toggleFilters} />
      </div>

      {/* Filtros extras */}
      {showFilters && (
        <div className="filters-visible">
          {/* Filtros de Data */}
          <div className="filter-group">
            <DatePicker
              selected={filters.dataInicio}
              onChange={(date) => handleDateChange(date, "dataInicio")}
              dateFormat="dd/MM/yyyy"
              className="form-control"
              placeholderText="Data de"
            />
            <DatePicker
              selected={filters.dataFim}
              onChange={(date) => handleDateChange(date, "dataFim")}
              dateFormat="dd/MM/yyyy"
              className="form-control"
              placeholderText="Até"
            />
            {(filters.dataInicio || filters.dataFim) && (
              <FaTimes
                className="clear-icon"
                onClick={() => setFilters((prev) => ({ ...prev, dataInicio: null, dataFim: null }))}
              />
            )}
          </div>

          {/* Filtros de Data de Entrega */}
          <div className="filter-group">
            <DatePicker
              selected={filters.deliveryDataInicio}
              onChange={(date) => handleDateChange(date, "deliveryDataInicio")}
              dateFormat="dd/MM/yyyy"
              className="form-control"
              placeholderText="Data de entrega de"
            />
            <DatePicker
              selected={filters.deliveryDataFim}
              onChange={(date) => handleDateChange(date, "deliveryDataFim")}
              dateFormat="dd/MM/yyyy"
              className="form-control"
              placeholderText="Até"
            />
            {(filters.deliveryDataInicio || filters.deliveryDataFim) && (
              <FaTimes
                className="clear-icon"
                onClick={() =>
                  setFilters((prev) => ({ ...prev, deliveryDataInicio: null, deliveryDataFim: null }))
                }
              />
            )}
          </div>

          {/* Filtros de Tester e Status */}
          <div className="filter-group" style={{ display: "flex", gap: "10px" }}>
            <Form.Select
              name="testerName"
              value={filters.testerName}
              onChange={handleFilterChange}
              className="form-control"
            >
              <option value="">Selecione um tester</option>
              {testers.map((tester) => (
                <option key={tester.id} value={tester.name}>
                  {tester.name}
                </option>
              ))}
            </Form.Select>

            <Form.Select
              name="status"
              value={filters.status}
              onChange={handleFilterChange}
              className="form-control"
            >
              <option value="">Selecione um status</option>
              {statusList.map((status) => (
                <option key={status.value} value={status.value}>
                  {status.label}
                </option>
              ))}
            </Form.Select>
          </div>

          {/* Botões de Aplicar e Limpar Filtros */}
          <Button className="custom-button" onClick={fetchFilteredTestPlans}>
            <FaFilter style={{ marginRight: "8px" }} />
            Aplicar Filtros
          </Button>
          <Button className="custom-button clear-filters-button" onClick={clearFilters}>
            <FaTimes style={{ marginRight: "8px" }} />
            Limpar Filtros
          </Button>
        </div>
      )}

       {/* Botão Salvar */}
       <div style={{ textAlign: 'right', marginTop: '20px' }}>
              <Button type="submit" variant="primary"  style={{ borderRadius: '25px' }}onClick={() => navigate('/planoTeste/cadastrar')} >
                Nova UL
              </Button>
            </div>
          

      {/* Tabela de Resultados */}
      <Table striped bordered hover className="table-custom">
        <thead>
          <tr>
            <th>UL</th>
            <th>Bitrix</th>
            <th>Descrição</th>
            <th>Data</th>
            <th>Data de Entrega</th>
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
                <FaEdit className="icon-edit" style={{ color: "#0d6efd", cursor: "pointer", marginRight: "15px" }} onClick={() => navigate(`/planoTeste/editar/${item.id}`)} />            
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Paginação */}
      <Pagination className="pagination-custom">
        <Pagination.First onClick={() => handlePageChange(0)} disabled={filters.page === 0} />
        <Pagination.Prev onClick={() => handlePageChange(filters.page - 1)} disabled={filters.page === 0} />
        {Array.from({ length: totalPages }, (_, index) => (
          <Pagination.Item key={index} active={index === filters.page} onClick={() => handlePageChange(index)}>
            {index + 1}
          </Pagination.Item>
        ))}
        <Pagination.Next onClick={() => handlePageChange(filters.page + 1)} disabled={filters.page === totalPages - 1} />
        <Pagination.Last onClick={() => handlePageChange(totalPages - 1)} disabled={filters.page === totalPages - 1} />
      </Pagination>

      {/* Mensagem de Erro ou Carregamento */}
      {error && <p>{error}</p>}
      {loading && <p>Carregando...</p>}
    </div>
  );
};

export default TestPlanPage;
import React, { useState } from "react";
import axios from "axios";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { FaFilter, FaTimes } from "react-icons/fa";

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
}

interface SearchComponentProps {
  onSearch: (filters: Filters) => void;
  onClear: () => void;
  testers: { id: number; name: string }[];
  statusList: { value: string; label: string }[];
}

const SearchComponent: React.FC<SearchComponentProps> = ({
  onSearch,
  onClear,
  testers,
  statusList,
}) => {
  const [showFilters, setShowFilters] = useState(false);
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
  });

  const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleDateChange = (date: Date | null, field: string) => {
    setFilters((prev) => ({ ...prev, [field]: date }));
  };

  const handleSearch = () => {
    onSearch(filters);
  };

  const handleClear = () => {
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
    });
    onClear();
  };

  return (
    <div>
      {/* Campo de Pesquisa e Ícone de Filtro */}
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <input
          type="text"
          placeholder="Pesquisar por nome, Jira, chamada, tester, desenvolvedor ou módulo..."
          className="search-bar"
          value={filters.name}
          onChange={(e) => setFilters((prev) => ({ ...prev, name: e.target.value }))}
        />
        <FaFilter className="filter-icon" onClick={() => setShowFilters(!showFilters)} />
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
          <Button className="custom-button" onClick={handleSearch}>
            <FaFilter style={{ marginRight: "8px" }} />
            Aplicar Filtros
          </Button>
          <Button className="custom-button clear-filters-button" onClick={handleClear}>
            <FaTimes style={{ marginRight: "8px" }} />
            Limpar Filtros
          </Button>
        </div>
      )}
    </div>
  );
};

export default SearchComponent;
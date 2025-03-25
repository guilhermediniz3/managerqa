import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { FaFileExcel, FaFilePdf } from "react-icons/fa";
import axios from "axios";

interface ReportModalProps {
  show: boolean;
  onHide: () => void;
}

const ReportModal: React.FC<ReportModalProps> = ({ show, onHide }) => {
  const [reportDate, setReportDate] = useState<Date | null>(null);
  const [selectedTester, setSelectedTester] = useState<string>("");
  const [testers, setTesters] = useState<{ id: number; name: string }[]>([]);

  // Busca a lista de testers ao abrir o modal
  useEffect(() => {
    const fetchTesters = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8081/testers", {
          headers: { Authorization: `Bearer ${token}` },
        });
        setTesters(response.data);
      } catch (error) {
        console.error("Erro ao buscar a lista de testers:", error);
      }
    };
    if (show) {
      fetchTesters();
    }
  }, [show]);

  // Gera o relatório Excel
  const handleGenerateExcel = async () => {
    try {
      const token = localStorage.getItem("token");
      const params = {
        data: reportDate?.toISOString().split('T')[0], // Formato YYYY-MM-DD
        testerName: selectedTester || undefined, // Passa undefined se não houver tester selecionado
      };
      const response = await axios.get("http://localhost:8081/relatorios/excel", {
        params,
        headers: { Authorization: `Bearer ${token}` },
        responseType: "blob", // Para receber o arquivo binário
      });

      // Cria um link para download do arquivo
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "relatorio.xlsx");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("Erro ao gerar o relatório Excel:", error);
    }
  };

  // Gera o relatório PDF
  const handleGeneratePdf = async () => {
    try {
      const token = localStorage.getItem("token");
      const params = {
        data: reportDate?.toISOString().split('T')[0], // Formato YYYY-MM-DD
        testerName: selectedTester || undefined, // Passa undefined se não houver tester selecionado
      };
      const response = await axios.get("http://localhost:8081/relatorios/pdf", {
        params,
        headers: { Authorization: `Bearer ${token}` },
        responseType: "blob", // Para receber o arquivo binário
      });

      // Cria um link para download do arquivo
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "relatorio.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("Erro ao gerar o relatório PDF:", error);
    }
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>Gerar Relatório</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          {/* Campo de Data */}
          <Form.Group className="mb-3">
            <Form.Label>Data</Form.Label>
            <DatePicker
              selected={reportDate}
              onChange={(date) => setReportDate(date)}
              dateFormat="dd/MM/yyyy"
              className="form-control"
              placeholderText="Selecione a data"
            />
          </Form.Group>

          {/* Select de Testers */}
          <Form.Group className="mb-3">
            <Form.Label>Tester</Form.Label>
            <Form.Select
              value={selectedTester}
              onChange={(e) => setSelectedTester(e.target.value)}
            >
              <option value="">Todos os Testers</option>
              {testers.map((tester) => (
                <option key={tester.id} value={tester.name}>
                  {tester.name}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        {/* Botão para gerar Excel */}
        <Button variant="success" onClick={handleGenerateExcel}>
          <FaFileExcel style={{ marginRight: "8px" }} />
          Gerar Excel
        </Button>

        {/* Botão para gerar PDF */}
        <Button variant="danger" onClick={handleGeneratePdf}>
          <FaFilePdf style={{ marginRight: "8px" }} />
          Gerar PDF
        </Button>

        {/* Botão para fechar o modal */}
        <Button variant="secondary" onClick={onHide}>
          Fechar
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ReportModal;
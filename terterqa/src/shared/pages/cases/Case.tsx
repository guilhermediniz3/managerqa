import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Button from "react-bootstrap/Button";
import ListGroup from "react-bootstrap/ListGroup";
import Form from "react-bootstrap/Form";
import Modal from "react-bootstrap/Modal";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import axios from "axios";

// Interface para representar um TestCase
interface TestCase {
  id: number;
  codeCase: number;
  scenario: string;
  expectedResult: string;
  obtainedResult: string;
  videoEvidence: string; // Link do Vimeo (ex.: https://vimeo.com/manage/videos/983128257/a66f38d337)
  status: "EM_PROGRESSO" | "CONCLUIDA" | "IMPEDIMENTO";
  data: Date;
}

// Função para formatar a URL do Vimeo
function formatVimeoUrl(url: string): string {
  // Verifica se a URL corresponde ao formato https://vimeo.com/manage/videos/{id}/{token}
  const matchManage = url.match(/vimeo\.com\/manage\/videos\/(\d+)\/([^?]+)/);
  if (matchManage) {
    const videoId = matchManage[1];
    const token = matchManage[2];
    return `https://player.vimeo.com/video/${videoId}?h=${token}`;
  }
  
  // Verifica se a URL corresponde ao formato https://vimeo.com/{id}/{token}
  const matchRegular = url.match(/vimeo\.com\/(\d+)\/([^?]+)/);
  if (matchRegular) {
    const videoId = matchRegular[1];
    const token = matchRegular[2];
    return `https://player.vimeo.com/video/${videoId}?h=${token}`;
  }

  return url; // Retorna a URL original se não corresponder a nenhum dos formatos
}

const Case = () => {
  // Recupera os IDs da URL
  const { testPlanId, testSuiteId } = useParams<{ testPlanId: string; testSuiteId: string }>();

  // Estado para armazenar a lista de casos
  const [testCases, setTestCases] = useState<TestCase[]>([]);
  const [nextCode, setNextCode] = useState(1); // Próximo código incremental
  const [showVideoModal, setShowVideoModal] = useState(false);
  const [selectedVideoUrl, setSelectedVideoUrl] = useState("");

  // Função para buscar os casos da API
  const fetchTestCases = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8081/testcases/plan/${testPlanId}/suite/${testSuiteId}`
      );
      const data = response.data;

      // Mapear os dados da API para o formato esperado pelo componente
      const mappedTestCases = data.map((item: any) => ({
        id: item.id,
        codeCase: item.codeCase || 0, // Use 0 como valor padrão se codeCase for null
        scenario: item.scenario,
        expectedResult: item.expectedResult,
        obtainedResult: item.obtainedResult,
        videoEvidence: item.videoEvidence,
        status: item.status,
        data: new Date(item.data),
      }));

      setTestCases(mappedTestCases);
    } catch (error) {
      console.error("Erro ao buscar os casos:", error);
    }
  };

  // Carregar os casos ao montar o componente
  useEffect(() => {
    if (testPlanId && testSuiteId) {
      fetchTestCases();
    }
  }, [testPlanId, testSuiteId]);

  // Adicionar um novo TestCase
  const handleCreateCase = () => {
    const newCase: TestCase = {
      id: Date.now(), // ID único baseado no timestamp
      codeCase: nextCode,
      scenario: "",
      expectedResult: "",
      obtainedResult: "",
      videoEvidence: "",
      status: "EM_PROGRESSO",
      data: new Date(),
    };
    setTestCases([...testCases, newCase]);
    setNextCode(nextCode + 1); // Incrementa o próximo código
  };

  // Atualizar um campo específico de um TestCase
  const handleUpdateCase = (id: number, field: string, value: any) => {
    setTestCases((prev) =>
      prev.map((testCase) =>
        testCase.id === id ? { ...testCase, [field]: value } : testCase
      )
    );
  };

  // Salvar um TestCase no backend
  const handleSaveCase = async (id: number) => {
    try {
      const testCase = testCases.find((tc) => tc.id === id);
      if (!testCase) {
        alert("TestCase não encontrado.");
        return;
      }

      // Monta o DTO para enviar ao backend
      const testCaseDTO = {
        id: testCase.id,
        codeCase: testCase.codeCase,
        scenario: testCase.scenario,
        expectedResult: testCase.expectedResult,
        obtainedResult: testCase.obtainedResult,
        videoEvidence: testCase.videoEvidence,
        status: testCase.status,
        data: testCase.data.toISOString(), // Converte a data para ISO
      };

      // Envia a requisição PUT para o backend
      const response = await axios.put(
        `http://localhost:8081/testcases/${id}`,
        testCaseDTO
      );

      console.log("TestCase atualizado com sucesso:", response.data);

      // Atualiza a lista de TestCases após salvar
      fetchTestCases();

      alert("TestCase salvo com sucesso!");
    } catch (error) {
      console.error("Erro ao salvar TestCase:", error);
      alert("Erro ao salvar TestCase. Verifique o console para mais detalhes.");
    }
  };

  // Abrir modal de vídeo
  const handleVideoClick = (url: string) => {
    if (url) {
      setSelectedVideoUrl(formatVimeoUrl(url)); // Usa a URL formatada
      setShowVideoModal(true);
    } else {
      alert("URL do vídeo não disponível.");
    }
  };

  // Fechar modal de vídeo
  const handleCloseVideoModal = () => {
    setShowVideoModal(false);
    setSelectedVideoUrl("");
  };

  return (
    <div style={{ margin: "20px" }}>
      {/* Botão para criar um novo TestCase */}
      <Button onClick={handleCreateCase} variant="success" style={{ marginBottom: "20px" }}>
        Novo Case
      </Button>

      {/* Lista de TestCases */}
      <ListGroup>
        {testCases.map((testCase) => {
          return (
            <ListGroup.Item key={testCase.id}>
              <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                {/* Campos Editáveis */}
                <Form.Group controlId={`formScenario-${testCase.id}`}>
                  <Form.Label>Cenário</Form.Label>
                  <Form.Control
                    type="text"
                    value={testCase.scenario}
                    onChange={(e) => handleUpdateCase(testCase.id, "scenario", e.target.value)}
                    placeholder="Digite o cenário"
                  />
                </Form.Group>

                <Form.Group controlId={`formExpectedResult-${testCase.id}`}>
                  <Form.Label>Resultado Esperado</Form.Label>
                  <Form.Control
                    type="text"
                    value={testCase.expectedResult}
                    onChange={(e) => handleUpdateCase(testCase.id, "expectedResult", e.target.value)}
                    placeholder="Digite o resultado esperado"
                  />
                </Form.Group>

                <Form.Group controlId={`formObtainedResult-${testCase.id}`}>
                  <Form.Label>Resultado Obtido</Form.Label>
                  <Form.Control
                    type="text"
                    value={testCase.obtainedResult}
                    onChange={(e) => handleUpdateCase(testCase.id, "obtainedResult", e.target.value)}
                    placeholder="Digite o resultado obtido"
                  />
                </Form.Group>

                <Form.Group controlId={`formVideoEvidence-${testCase.id}`}>
                  <Form.Label>Vídeo (URL)</Form.Label>
                  <Form.Control
                    type="text"
                    value={testCase.videoEvidence}
                    onChange={(e) => handleUpdateCase(testCase.id, "videoEvidence", e.target.value)}
                    placeholder="Insira o link do vídeo do Vimeo"
                  />
                </Form.Group>

                <Form.Group controlId={`formStatus-${testCase.id}`}>
                  <Form.Label>Status</Form.Label>
                  <Form.Select
                    value={testCase.status}
                    onChange={(e) => handleUpdateCase(testCase.id, "status", e.target.value)}
                  >
                    <option value="EM_PROGRESSO">Em Progresso</option>
                    <option value="CONCLUIDA">Concluída</option>
                    <option value="IMPEDIMENTO">Impedimento</option>
                  </Form.Select>
                </Form.Group>

                <Form.Group controlId={`formData-${testCase.id}`}>
                  <Form.Label>Data</Form.Label>
                  <DatePicker
                    selected={testCase.data}
                    onChange={(date) => handleUpdateCase(testCase.id, "data", date || new Date())}
                    dateFormat="dd/MM/yyyy"
                    className="form-control"
                  />
                </Form.Group>

                {/* Botão Salvar */}
                <Button
                  variant="primary"
                  onClick={() => handleSaveCase(testCase.id)}
                  style={{ width: "100%" }}
                >
                  Salvar
                </Button>

                {/* Miniatura do Vídeo */}
                <div
                  style={{
                    width: "100px",
                    height: "100px",
                    border: "1px solid #ccc",
                    borderRadius: "8px",
                    cursor: "pointer",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    overflow: "hidden",
                  }}
                  onClick={() => handleVideoClick(testCase.videoEvidence)}
                >
                  {testCase.videoEvidence ? (
                    <img
                      src={`https://vumbnail.com/${testCase.videoEvidence.split("/").pop()}.jpg`}
                      alt="Thumbnail"
                      style={{ width: "100%", height: "auto" }}
                      onError={(e) => {
                        // Caso a miniatura não carregue, exibe "Sem vídeo"
                        e.currentTarget.style.display = "none";
                      }}
                    />
                  ) : (
                    <span>Sem vídeo</span>
                  )}
                </div>
              </div>
            </ListGroup.Item>
          );
        })}
      </ListGroup>

      {/* Modal de Vídeo */}
      <Modal show={showVideoModal} onHide={handleCloseVideoModal} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Vídeo</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedVideoUrl ? (
            <iframe
              src={selectedVideoUrl}
              width="100%"
              height="400"
              frameBorder="0"
              allow="autoplay; fullscreen"
              allowFullScreen
            ></iframe>
          ) : (
            <p>Carregando vídeo...</p>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseVideoModal}>
            Fechar
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default Case;

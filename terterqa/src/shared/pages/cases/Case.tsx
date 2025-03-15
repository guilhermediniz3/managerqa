import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Modal from "react-bootstrap/Modal";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import axios, { AxiosResponse } from "axios";
import { ListGroup, Form, Button, Row, Col, Spinner } from "react-bootstrap";

// Interface para representar um TestCase
interface TestCase {
  id?: number;
  codeCase: number | null;
  scenario: string;
  expectedResult: string;
  obtainedResult: string;
  videoEvidence: string;
  status: "EM_PROGRESSO" | "CONCLUIDA" | "RETORNO";
  data: string; // A data é uma string no formato "dd/MM/yyyy"
  testSuiteId: number;
  testPlanId: number;
}

// Função para formatar a URL do Vimeo
function formatVimeoUrl(url: string): string {
  const matchManage = url.match(/vimeo\.com\/manage\/videos\/(\d+)\/([^?]+)/);
  if (matchManage) {
    const videoId = matchManage[1];
    const token = matchManage[2];
    return `https://player.vimeo.com/video/${videoId}?h=${token}`;
  }

  const matchRegular = url.match(/vimeo\.com\/(\d+)\/([^?]+)/);
  if (matchRegular) {
    const videoId = matchRegular[1];
    const token = matchRegular[2];
    return `https://player.vimeo.com/video/${videoId}?h=${token}`;
  }

  return url;
}

// Função para converter uma string no formato "dd/MM/yyyy" para um objeto Date
function parseDate(dateString: string): Date {
  const [day, month, year] = dateString.split("/");
  return new Date(`${year}-${month}-${day}`);
}

const Case = () => {
  const { testPlanId, testSuiteId } = useParams<{ testPlanId: string; testSuiteId: string }>();
  const [testCases, setTestCases] = useState<TestCase[]>([]);
  const [nextCode, setNextCode] = useState(1);
  const [nextTempId, setNextTempId] = useState(-1); // ID temporário para novos casos
  const [showVideoModal, setShowVideoModal] = useState(false);
  const [selectedVideoUrl, setSelectedVideoUrl] = useState("");
  const [loading, setLoading] = useState(false); // Estado de carregamento

  // Busca os TestCases do backend
  const fetchTestCases = async () => {
    if (!testPlanId || !testSuiteId) return; // Verifica se os IDs estão definidos

    setLoading(true); // Ativa o estado de carregamento
    try {
      const response: AxiosResponse<TestCase[]> = await axios.get(
        `http://localhost:8081/testcases/plan/${testPlanId}/suite/${testSuiteId}`
      );
      const data = response.data;

      const mappedTestCases = data.map((item: TestCase) => ({
        ...item,
        data: item.data, // Mantém a data como string
      }));

      setTestCases(mappedTestCases);
    } catch (error) {
      console.error("Erro ao buscar os casos:", error);
      alert("Erro ao buscar os casos. Verifique o console para mais detalhes.");
    } finally {
      setLoading(false); // Desativa o estado de carregamento
    }
  };

  useEffect(() => {
    fetchTestCases(); // Busca os casos ao carregar o componente
  }, [testPlanId, testSuiteId]); // Executa quando testPlanId ou testSuiteId mudam

  // Adiciona um novo TestCase em modo de edição
  const handleCreateCase = () => {
    const newCase: TestCase = {
      id: nextTempId, // Usa um ID temporário
      codeCase: nextCode,
      scenario: "",
      expectedResult: "",
      obtainedResult: "",
      videoEvidence: "",
      status: "EM_PROGRESSO",
      data: new Date().toLocaleDateString("pt-BR"), // Formata a data como "dd/MM/yyyy"
      testSuiteId: parseInt(testSuiteId),
      testPlanId: parseInt(testPlanId),
    };
    setTestCases([...testCases, newCase]);
    setNextCode(nextCode + 1);
    setNextTempId(nextTempId - 1); // Decrementa o ID temporário para o próximo caso
  };

  // Atualiza um campo específico de um TestCase
  const handleUpdateCase = (id: number, field: string, value: any) => {
    setTestCases((prev) =>
      prev.map((testCase) =>
        testCase.id === id ? { ...testCase, [field]: value } : testCase
      )
    );
  };

  // Salva um TestCase (POST para novos casos, PUT para existentes)
  const handleSaveCase = async (id: number) => {
    try {
      const testCase = testCases.find((tc) => tc.id === id);
      if (!testCase) {
        alert("TestCase não encontrado.");
        return;
      }

      // Monta o DTO para enviar ao backend
      const testCaseDTO = {
        ...testCase,
        data: testCase.data, // Mantém a data como string
      };

      let response: AxiosResponse<TestCase>;

      if (testCase.id && testCase.id < 0) {
        // Se o TestCase tem um ID temporário, é um novo caso (POST)
        response = await axios.post(
          `http://localhost:8081/testcases/plan/${testPlanId}/suite/${testSuiteId}`,
          testCaseDTO
        );

        // Atualiza o estado com o novo TestCase retornado pelo backend
        setTestCases((prev) =>
          prev.map((tc) =>
            tc.id === id ? { ...tc, id: response.data.id } : tc
          )
        );
      } else {
        // Se o TestCase tem um ID real, é uma atualização (PUT)
        response = await axios.put(
          `http://localhost:8081/testcases/${id}`,
          testCaseDTO
        );

        // Atualiza o estado com o TestCase atualizado
        setTestCases((prev) =>
          prev.map((tc) => (tc.id === id ? response.data : tc))
        );
      }

      console.log("TestCase salvo com sucesso:", response.data);
      alert("TestCase salvo com sucesso!");
    } catch (error) {
      console.error("Erro ao salvar TestCase:", error);
      alert("Erro ao salvar TestCase. Verifique o console para mais detalhes.");
    }
  };

  // Abre o modal de vídeo
  const handleVideoClick = (url: string) => {
    if (url) {
      setSelectedVideoUrl(formatVimeoUrl(url));
      setShowVideoModal(true);
    } else {
      alert("URL do vídeo não disponível.");
    }
  };

  // Fecha o modal de vídeo
  const handleCloseVideoModal = () => {
    setShowVideoModal(false);
    setSelectedVideoUrl("");
  };

  // Mostra um spinner enquanto os dados estão sendo carregados
  if (loading) {
    return (
      <div style={{ display: "flex", justifyContent: "center", marginTop: "20px" }}>
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Carregando...</span>
        </Spinner>
      </div>
    );
  }

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
              <Row>
                {/* Coluna 1: Cenário, Resultado Esperado, Resultado Obtido */}
                <Col md={6}>
                  <div style={{ display: "flex", flexDirection: "column", gap: "5px" }}>
                    <Form.Group controlId={`formScenario-${testCase.id}`}>
                      <Form.Label>Cenário</Form.Label>
                      <Form.Control
                        as="textarea"
                        rows={1}
                        value={testCase.scenario}
                        onChange={(e) => handleUpdateCase(testCase.id!, "scenario", e.target.value)}
                        placeholder="Digite o cenário"
                        style={{ width: '100%' }}
                      />
                    </Form.Group>

                    <Form.Group controlId={`formExpectedResult-${testCase.id}`}>
                      <Form.Label>Resultado Esperado</Form.Label>
                      <Form.Control
                        as="textarea"
                        rows={1}
                        value={testCase.expectedResult}
                        onChange={(e) => handleUpdateCase(testCase.id!, "expectedResult", e.target.value)}
                        placeholder="Digite o resultado esperado"
                        style={{ width: '100%' }}
                      />
                    </Form.Group>

                    <Form.Group controlId={`formObtainedResult-${testCase.id}`}>
                      <Form.Label>Resultado Obtido</Form.Label>
                      <Form.Control
                        as="textarea"
                        rows={1}
                        value={testCase.obtainedResult}
                        onChange={(e) => handleUpdateCase(testCase.id!, "obtainedResult", e.target.value)}
                        placeholder="Digite o resultado obtido"
                        style={{ width: '100%' }}
                      />
                    </Form.Group>
                  </div>
                </Col>

                {/* Coluna 2: Vídeo, Status, Data, Botão Salvar */}
                <Col md={4}>
                  <div style={{ display: "flex", flexDirection: "column", gap: "5px" }}>
                    <Form.Group controlId={`formVideoEvidence-${testCase.id}`}>
                      <Form.Label>Vídeo (URL)</Form.Label>
                      <div style={{ display: "flex", flexDirection: "row", alignItems: "center", gap: "1px" }}>
                        <Form.Control
                          type="text"
                          value={testCase.videoEvidence}
                          onChange={(e) => handleUpdateCase(testCase.id!, "videoEvidence", e.target.value)}
                          placeholder="Insira o link do vídeo do Vimeo"
                          style={{ width: '70%', marginTop: "-45px" }}
                        />

                        <div
                          style={{
                            width: "90px",
                            height: "90px",
                            borderRadius: "8px",
                            cursor: "pointer",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            overflow: "hidden",
                            position: "relative",
                          }}
                          onClick={() => handleVideoClick(testCase.videoEvidence)}
                        >
                          {testCase.videoEvidence ? (
                            <img
                              src={`https://vumbnail.com/${testCase.videoEvidence.split("/").pop()}.jpg`}
                              alt="Thumbnail"
                              style={{
                                width: "60px",
                                height: "44px",
                                position: "absolute",
                                top: "50%",
                                left: "40%",
                                transform: "translate(-50%, -100%)",
                              }}
                              onError={(e) => {
                                e.currentTarget.style.display = "none";
                              }}
                            />
                          ) : (
                            <span>Sem vídeo</span>
                          )}
                        </div>
                      </div>
                    </Form.Group>

                    <div style={{ display: "flex", flexDirection: "row", gap: "5px" }}>
                      <Form.Group controlId={`formStatus-${testCase.id}`} style={{ flex: 1 }}>
                        <Form.Label>Status</Form.Label>
                        <Form.Select
                          value={testCase.status}
                          onChange={(e) => handleUpdateCase(testCase.id!, "status", e.target.value)}
                          style={{ width: '75%', height: "46px" }}
                        >
                          <option value="EM_PROGRESSO">Em Progresso</option>
                          <option value="CONCLUIDA">Concluída</option>
                          <option value="RETORNO">Retorno</option>
                        </Form.Select>
                      </Form.Group>

                      <Form.Group controlId={`formData-${testCase.id}`} style={{ flex: 1 }}>
                        <Form.Label>Data</Form.Label>
                        <div className="custom-datepicker">
                          <DatePicker
                            selected={parseDate(testCase.data)} // Converte a string para Date
                            onChange={(date) => handleUpdateCase(testCase.id!, "data", date?.toLocaleDateString("pt-BR") || "")}
                            dateFormat="dd/MM/yyyy"
                            className="form-control"
                          />
                        </div>
                      </Form.Group>
                    </div>

                    <div style={{ display: "flex", justifyContent: "flex-end" }}>
                      <Button
                        variant="primary"
                        onClick={() => handleSaveCase(testCase.id!)}
                        style={{ width: "30%" }}
                      >
                        Salvar
                      </Button>
                    </div>
                  </div>
                </Col>
              </Row>
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
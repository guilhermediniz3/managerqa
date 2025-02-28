import React, { useState, useEffect } from 'react';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import ListGroup from 'react-bootstrap/ListGroup';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function CreateTestPlan() {
  const [startDate, setStartDate] = useState(new Date());
  const [deliveryDate, setDeliveryDate] = useState(new Date());
  const [selectedDeveloper, setSelectedDeveloper] = useState(2);
  const [selectedSystemModule, setSelectedSystemModule] = useState(1);
  const [selectedStatus, setSelectedStatus] = useState("EM_PROGRESSO");
  const [selectedTaskStatus, setSelectedTaskStatus] = useState("in_progress");
  const [selectedTester, setSelectedTester] = useState(null);

  // Estado para armazenar os dados dos testadores vindos do endpoint
  const [testers, setTesters] = useState([]);

  // Estado para armazenar as suites criadas
  const [suites, setSuites] = useState([]);

  const developers = [
    { id: 1, name: "Developer 1" },
    { id: 2, name: "Developer 2" },
    { id: 3, name: "Developer 3" }
  ];

  const systemModules = [
    { id: 1, name: "Module 1" },
    { id: 2, name: "Module 2" },
    { id: 3, name: "Module 3" }
  ];

  const statusOptions = [
    { value: "EM_PROGRESSO", label: "Em Progresso" },
    { value: "CONCLUIDO", label: "Concluído" },
    { value: "PENDENTE", label: "Pendente" }
  ];

  const taskStatusOptions = [
    { value: "in_progress", label: "In Progress" },
    { value: "completed", label: "Completed" },
    { value: "pending", label: "Pending" }
  ];

  const handleSubmit = (e) => {
    e.preventDefault();
    // Lógica para enviar os dados do formulário
    console.log({
      startDate,
      deliveryDate,
      selectedDeveloper,
      selectedSystemModule,
      selectedTester,
      selectedStatus,
      selectedTaskStatus,
    });
  };

  // Função para buscar os dados dos testadores ao carregar o componente
  useEffect(() => {
    axios.get('http://localhost:8081/testers')
      .then((response) => {
        setTesters(response.data); // Atualiza o estado com os dados recebidos
      })
      .catch((error) => {
        console.error("Erro ao buscar testadores:", error);
      });
  }, []);

  // Função para criar uma nova suite via endpoint Axios
  const handleCreateSuite = () => {
    const newSuite = {
      id: suites.length + 1, // Simulação de ID incremental
      status: "EM_PROGRESSO",
      data: new Date().toLocaleDateString('pt-BR'), // Data no formato DD/MM/YYYY
    };

    // Simulação de chamada Axios
    axios.post('/api/suites', newSuite)
      .then((response) => {
        setSuites([...suites, response.data]); // Atualiza a lista de suites
      })
      .catch((error) => {
        console.error("Erro ao criar suite:", error);
      });
  };

  const navigate = useNavigate();

  return (
    <Container style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
      <div style={{ maxWidth: '900px', width: '100%', padding: '20px', border: '1px solid #ddd', borderRadius: '8px' }}>
        <Tabs defaultActiveKey="create-test-plan" id="fill-tab-example" className="mb-3" fill>
          {/* Primeira Aba - Criar Plano de Teste */}
          <Tab eventKey="create-test-plan" title="Criar Plano de Teste">
            <Form onSubmit={handleSubmit}>
              {/* Linha 1: Data de Entrega e Data */}
              <Row>
                <Col md={6}>
                  <Form.Group controlId="formDeliveryData">
                    <Form.Label>Data de Entrega</Form.Label>
                    <DatePicker
                      selected={deliveryDate}
                      onChange={(date) => setDeliveryDate(date || new Date())}
                      dateFormat="dd/MM/yyyy"
                      className="form-control"
                    />
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId="formData">
                    <Form.Label>Data</Form.Label>
                    <DatePicker
                      selected={startDate}
                      onChange={(date) => setStartDate(date || new Date())}
                      dateFormat="dd/MM/yyyy"
                      className="form-control"
                    />
                  </Form.Group>
                </Col>
              </Row>

              {/* Linha 2: Nome de Usuário e Senha */}
              <Row>
                <Col md={6}>
                  <Form.Group controlId="formUserName">
                    <Form.Label>Nome de Usuário</Form.Label>
                    <Form.Control type="text" placeholder="user_name" />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId="formPassword">
                    <Form.Label>Senha</Form.Label>
                    <Form.Control type="password" placeholder="password123" />
                  </Form.Group>
                </Col>
              </Row>

              {/* Linha 3: Nome, Jira e Número de Chamada */}
              <Row>
                <Col md={4}>
                  <Form.Group controlId="formName">
                    <Form.Label>Nome</Form.Label>
                    <Form.Control type="text" placeholder="Nome do teste" />
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId="formJira">
                    <Form.Label>JIRA</Form.Label>
                    <Form.Control type="text" placeholder="JIRA-123" />
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId="formCallNumber">
                    <Form.Label>Número de Chamada</Form.Label>
                    <Form.Control type="text" placeholder="12345" />
                  </Form.Group>
                </Col>
              </Row>

              {/* Linha 4: Testador, Desenvolvedor e Módulo do Sistema */}
              <Row>
                <Col md={4}>
                  <Form.Group controlId="formTester">
                    <Form.Label>Testador</Form.Label>
                    <Form.Control
                      as="select"
                      value={selectedTester || ""}
                      onChange={(e) => setSelectedTester(Number(e.target.value))}
                    >
                      <option value="">Selecione um testador</option>
                      {testers.map((tester) => (
                        <option key={tester.id} value={tester.id}>
                          {tester.name}
                        </option>
                      ))}
                    </Form.Control>
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId="formDeveloper">
                    <Form.Label>Desenvolvedor</Form.Label>
                    <Form.Control
                      as="select"
                      value={selectedDeveloper}
                      onChange={(e) => setSelectedDeveloper(Number(e.target.value))}
                    >
                      {developers.map((developer) => (
                        <option key={developer.id} value={developer.id}>
                          {developer.name}
                        </option>
                      ))}
                    </Form.Control>
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId="formSystemModule">
                    <Form.Label>Módulo do Sistema</Form.Label>
                    <Form.Control
                      as="select"
                      value={selectedSystemModule}
                      onChange={(e) => setSelectedSystemModule(Number(e.target.value))}
                    >
                      {systemModules.map((module) => (
                        <option key={module.id} value={module.id}>
                          {module.name}
                        </option>
                      ))}
                    </Form.Control>
                  </Form.Group>
                </Col>
              </Row>

              {/* Linha 5: Status e Status da Tarefa */}
              <Row>
                <Col md={4}>
                  <Form.Group controlId="formStatus">
                    <Form.Label>Status</Form.Label>
                    <Form.Control
                      as="select"
                      value={selectedStatus}
                      onChange={(e) => setSelectedStatus(e.target.value)}
                    >
                      {statusOptions.map((option) => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </Form.Control>
                  </Form.Group>
                </Col>
                <Col md={4}>
                  <Form.Group controlId="formTaskStatus">
                    <Form.Label>Status da Tarefa</Form.Label>
                    <Form.Control
                      as="select"
                      value={selectedTaskStatus}
                      onChange={(e) => setSelectedTaskStatus(e.target.value)}
                    >
                      {taskStatusOptions.map((option) => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </Form.Control>
                  </Form.Group>
                </Col>
              </Row>

              {/* Linha 6: Observações */}
              <Row>
                <Col md={12}>
                  <Form.Group controlId="formObservation">
                    <Form.Label>Observações</Form.Label>
                    <Form.Control as="textarea" rows={3} placeholder="Algumas observações" />
                  </Form.Group>
                </Col>
              </Row>

              {/* Botão Salvar */}
              <div style={{ textAlign: 'right', marginTop: '20px' }}>
                <Button variant="primary" type="submit" style={{ marginRight: '20px' }}>
                  Salvar
                </Button>
              </div>
            </Form>
          </Tab>

          {/* Segunda Aba - Outra Aba */}
          <Tab eventKey="other-tab" title="Outra Aba">
            <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '20px' }}>
              <Button variant="success" onClick={handleCreateSuite}>
                Nova Suite
              </Button>
            </div>
            <ListGroup as="ol" numbered>
              {suites.map((suite) => (
                <ListGroup.Item
                  key={suite.id}
                  as="li"
                  className="d-flex justify-content-between align-items-start"
                  style={{ margin: '10px', padding: '15px', border: '1px solid #ddd', borderRadius: '8px' }}
                >
                  <div className="ms-2 me-auto">
                    <div className="fw-bold">Suite #{suite.id}</div>
                    Data: {suite.data}
                  </div>
                  <div>
                    {/* Status como botão arredondado */}
                    <Button
                      variant="outline-primary"
                      style={{ borderRadius: '20px', marginRight: '10px' }}
                    >
                      {suite.status === "EM_PROGRESSO" ? "Em Progresso" : suite.status}
                    </Button>
                    {/* Botão Editar */}
                    <Button
                      variant="primary"
                      style={{ borderRadius: '5px' }}
                      onClick={() => navigate(`/edit-suite/${suite.id}`)} // Navegação para edição
                    >
                      Editar
                    </Button>
                  </div>
                </ListGroup.Item>
              ))}
            </ListGroup>
          </Tab>
        </Tabs>
      </div>
    </Container>
  );
}

export default CreateTestPlan;
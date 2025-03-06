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
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import NavVertical from "../../../components/navs/vertical/NavVertical";
import { useNavigate, useParams } from 'react-router-dom';

function EditTestPlan() {
  const { id } = useParams(); // Obtém o ID do plano de teste da URL
  const [startDate, setStartDate] = useState(new Date());
  const [deliveryDate, setDeliveryDate] = useState(new Date());
  const [selectedStatus, setSelectedStatus] = useState("EM_PROGRESSO");
  const [selectedTaskStatus, setSelectedTaskStatus] = useState("in_progress");
  const [isTaskCreated, setIsTaskCreated] = useState(false);
  const [matriz, setMatriz] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [jira, setJira] = useState("");
  const [callNumber, setCallNumber] = useState("");
  const [observations, setObservations] = useState("");
  const [suites, setSuites] = useState([]);

  interface Modulo {
    id: number;
    name: string;
    active: boolean;
  }
  const [modules, setModules] = useState<Modulo[]>([]);
  const [selectedModulo, setSelectedModulo] = useState<string | null>(null);

  interface Tester {
    id: number;
    name: string;
    active: boolean;
  }
  const [testers, setTesters] = useState<Tester[]>([]);
  const [selectedTester, setSelectedTester] = useState<string | null>(null);

  interface Developer {
    id: number;
    name: string;
    active: boolean;
    technologyIds: number[];
  }
  const [developers, setDevelopers] = useState<Developer[]>([]);
  const [selectedDeveloper, setSelectedDeveloper] = useState<string | null>(null);

  useEffect(() => {
    // Carrega os dados do plano de teste existente
    axios.get(`http://localhost:8081/testplans/${id}`)
      .then((response) => {
        const testPlan = response.data;
        setStartDate(new Date(testPlan.data));
        setDeliveryDate(new Date(testPlan.deliveryData));
        setSelectedStatus(testPlan.status);
        setIsTaskCreated(testPlan.created);
        setMatriz(testPlan.matriz);
        setUsername(testPlan.userName);
        setPassword(testPlan.password);
        setName(testPlan.name);
        setJira(testPlan.jira);
        setCallNumber(testPlan.callNumber);
        setObservations(testPlan.observation);
        setSelectedDeveloper(testPlan.developerId);
        setSelectedModulo(testPlan.systemModuleId);
        setSelectedTester(testPlan.testerId);
      })
      .catch((error) => console.error("Erro ao buscar Plano de Teste:", error));

    // Carrega os módulos, testers e desenvolvedores
    axios.get<Modulo[]>("http://localhost:8081/modules")
      .then((response) => {
        const activeModulo = response.data.filter((modulo) => modulo.active);
        setModules(activeModulo);
      })
      .catch((error) => console.error("Erro ao buscar Módulos:", error));

    axios.get<Tester[]>("http://localhost:8081/testers")
      .then((response) => {
        const activeTester = response.data.filter((teste) => teste.active);
        setTesters(activeTester);
      })
      .catch((error) => console.error("Erro ao buscar testadores:", error));

    axios.get<Developer[]>('http://localhost:8081/developers')
      .then((response) => {
        const activeDevelopers = response.data.filter((dev) => dev.active);
        setDevelopers(activeDevelopers);
      })
      .catch((error) => console.error("Erro ao buscar desenvolvedores:", error));
  }, [id]);

  const handleSubmit = (e) => {
    e.preventDefault();

    const formattedStartDate = startDate.toLocaleDateString('pt-BR');
    const formattedDeliveryDate = deliveryDate.toLocaleDateString('pt-BR');

    const testPlanData = {
      name: name || "Sem nome",
      created: isTaskCreated,
      observation: observations || "Sem observações",
      status: selectedStatus || "EM_PROGRESSO",
      jira: jira || "Sem JIRA",
      data: formattedStartDate,
      deliveryData: formattedDeliveryDate,
      matriz: matriz || "Sem matriz",
      userName: username || "Sem usuário",
      callNumber: callNumber || "Sem número",
      developerId: selectedDeveloper || 1,
      systemModuleId: selectedModulo || 1,
      testerId: selectedTester || 1,
      password: password || "Sem senha",
      testeSuiteId: [],
    };

    axios.put(`http://localhost:8081/testplans/${id}`, testPlanData)
      .then((response) => {
        console.log("Plano de Teste Atualizado:", response.data);
        alert("Plano de Teste atualizado com sucesso!");
      })
      .catch((error) => {
        console.error("Erro ao atualizar Plano de Teste:", error);
        alert("Erro ao atualizar Plano de Teste. Verifique o console para mais detalhes.");
      });
  };

  const handleCreateSuite = () => {
    const newSuite = {
      id: suites.length + 1,
      status: "EM_PROGRESSO",
      data: new Date().toLocaleDateString('pt-BR'),
    };

    axios.post('/api/suites', newSuite)
      .then((response) => {
        setSuites([...suites, response.data]);
      })
      .catch((error) => {
        console.error("Erro ao criar suite:", error);
      });
  };

  const navigate = useNavigate();

  return (
    <Container style={{ marginTop: '20px', marginBottom: '20px' }}>
      <NavHorizontal />
      <NavVertical />
      <br />
      <br />
      <br />

      <Tabs defaultActiveKey="edit-test-plan" id="fill-tab-example" className="mb-3" fill>
        <Tab eventKey="edit-test-plan" title="Editar Plano de Teste">
          <Form onSubmit={handleSubmit} style={{ margin: '20px', padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
            <Row>
              <Col md={6}>
                <Form.Group controlId="formDeliveryDate">
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

            <Row>
              <Col md={4}>
                <Form.Group controlId="formUserName">
                  <Form.Label>Matriz</Form.Label>
                  <Form.Control
                    type="text"
                    value={matriz}
                    onChange={(e) => setMatriz(e.target.value)}
                    placeholder="50000101 "
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId="formUserName">
                  <Form.Label>Usuário</Form.Label>
                  <Form.Control
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Usuário"
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId="formPassword">
                  <Form.Label>Senha</Form.Label>
                  <Form.Control
                    type="text"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Senha"
                  />
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col md={4}>
                <Form.Group controlId="formName">
                  <Form.Label>Descrição UL</Form.Label>
                  <Form.Control
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Título da UL"
                    required
                    onInvalid={(e) => {
                      const target = e.target as HTMLInputElement;
                      target.setCustomValidity("Por favor, Informe a descrição da UL.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLInputElement;
                      target.setCustomValidity("");
                    }}
                    isInvalid={!name}
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId="formJira">
                  <Form.Label>JIRA</Form.Label>
                  <Form.Control
                    type="text"
                    value={jira}
                    onChange={(e) => setJira(e.target.value)}
                    placeholder="Número da UL-"
                    required
                    onInvalid={(e) => {
                      const target = e.target as HTMLInputElement;
                      target.setCustomValidity("Por favor, Informe o número da UL.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLInputElement;
                      target.setCustomValidity("");
                    }}
                    isInvalid={!jira}
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId="formCallNumber">
                  <Form.Label>Número do chamado</Form.Label>
                  <Form.Control
                    type="text"
                    value={callNumber}
                    onChange={(e) => setCallNumber(e.target.value)}
                    placeholder="Bitrix..."
                  />
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col md={4}>
                <Form.Group controlId="formTester">
                  <Form.Label>Tester(QA)</Form.Label>
                  <Form.Control
                    as="select"
                    value={selectedTester || ""}
                    onChange={(e) => setSelectedTester(e.target.value)}
                    required
                    onInvalid={(e) => {
                      const target = e.target as HTMLSelectElement;
                      target.setCustomValidity("Por favor, selecione um tester antes de continuar.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLSelectElement;
                      target.setCustomValidity("");
                    }}
                    isInvalid={!selectedTester}
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
                    value={selectedDeveloper || ""}
                    onChange={(e) => setSelectedDeveloper(e.target.value)}
                    required
                    onInvalid={(e) => {
                      const target = e.target as HTMLSelectElement;
                      target.setCustomValidity("Por favor, selecione um desenvolvedor antes de continuar.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLSelectElement;
                      target.setCustomValidity("");
                    }}
                    isInvalid={!selectedDeveloper}
                  >
                    <option value="">Selecione um desenvolvedor</option>
                    {developers.map((dev) => (
                      <option key={dev.id} value={dev.id}>
                        {dev.name}
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
                    value={selectedModulo || ""}
                    onChange={(e) => setSelectedModulo(e.target.value)}
                    required
                    onInvalid={(e) => {
                      const target = e.target as HTMLSelectElement;
                      target.setCustomValidity("Por favor, selecione um Módulo antes de continuar.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLSelectElement;
                      target.setCustomValidity("");
                    }}
                    isInvalid={!selectedModulo}
                  >
                    <option value="">Selecione um módulo</option>
                    {modules.map((modulo) => (
                      <option key={modulo.id} value={modulo.id}>
                        {modulo.name}
                      </option>
                    ))}
                  </Form.Control>
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col md={4}>
                <Form.Group controlId="formStatus">
                  <Form.Label>Status</Form.Label>
                  <Form.Control
                    as="select"
                    value={selectedStatus}
                    onChange={(e) => setSelectedStatus(e.target.value)}
                  >
                    <option value="EM_PROGRESSO">Em Progresso</option>
                    <option value="CRIADA">Criada</option>
                    <option value="CONCLUIDA">Concluída</option>
                    <option value="IMPEDIMENTO">Impedimento</option>
                    <option value="RETORNO">Retorno</option>
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
                    <option value="in_progress">In Progress</option>
                    <option value="completed">Completed</option>
                    <option value="pending">Pending</option>
                  </Form.Control>
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId="formTaskSwitch">
                  <Form.Label>Criada</Form.Label>
                  <Form.Check
                    type="switch"
                    id="custom-switch"
                    label=""
                    checked={isTaskCreated}
                    onChange={(e) => setIsTaskCreated(e.target.checked)}
                  />
                </Form.Group>
              </Col>
            </Row>

            <Row>
              <Col md={12}>
                <Form.Group controlId="formObservation">
                  <Form.Label>Observações</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={observations}
                    onChange={(e) => setObservations(e.target.value)}
                    placeholder="Algumas observações"
                  />
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={10}>
                <div style={{ textAlign: 'right', marginTop: '20px' }}>
                  <Button type="button" variant="secondary" onClick={() => navigate('/planoTeste/listagem')}>
                    Voltar
                  </Button>
                </div>
              </Col>
              <Col md={2}>
                <div style={{ textAlign: 'right', marginTop: '20px' }}>
                  <Button type="submit" variant="primary">
                    Salvar
                  </Button>
                </div>
              </Col>
            </Row>
          </Form>
        </Tab>

        <Tab eventKey="other-tab" title="Outra Aba">
          <div style={{ margin: '20px' }}>
            <Button onClick={handleCreateSuite} variant="success" style={{ marginBottom: '20px' }}>
              Nova Suite
            </Button>
            <ListGroup>
              {suites.map((suite) => (
                <ListGroup.Item key={suite.id}>
                  <strong>Suite #{suite.id}</strong> - Data: {suite.data}
                  <Button
                    variant="info"
                    size="sm"
                    style={{ marginLeft: '10px' }}
                    onClick={() => navigate(`/edit-suite/${suite.id}`)}
                  >
                    Editar
                  </Button>
                </ListGroup.Item>
              ))}
            </ListGroup>
          </div>
        </Tab>
      </Tabs>
    </Container>
  );
}

export default EditTestPlan;
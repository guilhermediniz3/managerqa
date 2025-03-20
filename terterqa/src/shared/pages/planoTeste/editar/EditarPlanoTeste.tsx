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
import { FaEdit } from 'react-icons/fa';
import { HiOutlineDuplicate } from 'react-icons/hi';
import './styless.css';

function EditTestPlan() {
  const { id } = useParams(); // Obtém o ID do plano de teste da URL
  const [startDate, setStartDate] = useState(null);
  const [deliveryDate, setDeliveryDate] = useState(null);
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
  const [suites, setSuites] = useState([]); // Lista de suites
  const [nextCode, setNextCode] = useState(1); // Estado para o próximo código incremental
  const [loading, setLoading] = useState(true); // Estado para indicar carregamento
  const [error, setError] = useState(null); // Estado para tratar erros

  interface Modulo {
    id: number;
    name: string;
    active: boolean;
  }
  const [modules, setModules] = useState<Modulo[]>([]);
  const [selectedModulo, setSelectedModulo] = useState<number | null>(null);

  interface Tester {
    id: number;
    name: string;
    active: boolean;
  }
  const [testers, setTesters] = useState<Tester[]>([]);
  const [selectedTester, setSelectedTester] = useState<number | null>(null);

  interface Developer {
    id: number;
    name: string;
    active: boolean;
    technologyIds: number[];
  }
  const [developers, setDevelopers] = useState<Developer[]>([]);
  const [selectedDeveloper, setSelectedDeveloper] = useState<number | null>(null);

  const navigate = useNavigate();

  // Função para converter datas no formato DD/MM/YYYY para Date
  const parseDate = (dateString) => {
    if (!dateString) return null;
    const [day, month, year] = dateString.split('/');
    return new Date(`${year}-${month}-${day}`);
  };

  // Função para buscar o último codeSuite
  const fetchLastCodeSuite = async () => {
    try {
      const lastCodeSuiteResponse = await axios.get(`http://localhost:8081/testplans/${id}/last-code-suite`);
      const { codeSuite, testPlanId } = lastCodeSuiteResponse.data;

      if (testPlanId === parseInt(id) && codeSuite !== null && codeSuite !== 0) {
        return codeSuite + 1;
      } else {
        console.warn("O codeSuite retornado é inválido. Usando valor padrão para nextCode.");
        return 1;
      }
    } catch (error) {
      console.warn("Erro ao buscar lastCodeSuite. Usando valor padrão para nextCode:", error);
      return 1;
    }
  };

  // Carrega os dados do plano de teste e as suites existentes
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        // Carrega os dados do plano de teste
        const testPlanResponse = await axios.get(`http://localhost:8081/testplans/${id}`);
        const testPlan = testPlanResponse.data;
        console.log('Dados do plano de teste:', testPlan);

        if (!testPlan) {
          throw new Error("TestPlan não encontrado.");
        }

        // Atualiza as datas
        if (testPlan.data) setStartDate(parseDate(testPlan.data));
        if (testPlan.deliveryData) setDeliveryDate(parseDate(testPlan.deliveryData));

        // Atualiza os outros campos
        setSelectedStatus(testPlan.status || "EM_PROGRESSO");
        setIsTaskCreated(testPlan.created || false);
        setMatriz(testPlan.matriz || "Sem matriz");
        setUsername(testPlan.userName || "Sem usuário");
        setPassword(testPlan.password || "Sem senha");
        setName(testPlan.name || "Sem nome");
        setJira(testPlan.jira || "Sem JIRA");
        setCallNumber(testPlan.callNumber || "Sem número");
        setObservations(testPlan.observation || "Sem observações");
        setSelectedDeveloper(testPlan.developerId || null);
        setSelectedModulo(testPlan.systemModuleId || null);
        setSelectedTester(testPlan.testerId || null);

        // Carrega as suites existentes
        const suitesResponse = await axios.get(`http://localhost:8081/test-suites/testplan/${id}`);
        console.log('Suites carregadas:', suitesResponse.data);
        setSuites(suitesResponse.data);

        // Carrega módulos, testers e desenvolvedores
        const modulesResponse = await axios.get<Modulo[]>("http://localhost:8081/modules");
        const activeModulo = modulesResponse.data.filter((modulo) => modulo.active);
        setModules(activeModulo);

        const testersResponse = await axios.get<Tester[]>("http://localhost:8081/testers");
        const activeTester = testersResponse.data.filter((teste) => teste.active);
        setTesters(activeTester);

        const developersResponse = await axios.get<Developer[]>('http://localhost:8081/developers');
        const activeDevelopers = developersResponse.data.filter((dev) => dev.active);
        setDevelopers(activeDevelopers);

        setLoading(false);
      } catch (error) {
        console.error("Erro ao carregar dados:", error);
        setError("Erro ao carregar dados. Tente novamente mais tarde.");
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  // Função para formatar a data no formato DD/MM/YYYY
  const formatDateToDDMMYYYY = (date) => {
    if (!date) return "";
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  };

  // Função para enviar os dados do formulário
  const handleSubmit = (e) => {
    e.preventDefault();
    const formattedStartDate = formatDateToDDMMYYYY(startDate);
    const formattedDeliveryDate = formatDateToDDMMYYYY(deliveryDate);
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
      testeSuiteId: suites.map((suite) => suite.id),
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

  // Função para criar uma nova suite
  const handleCreateSuite = async () => {
    const nextCodeSuite = await fetchLastCodeSuite();
    const newSuite = {
      status: "EM_PROGRESSO",
      data: formatDateToDDMMYYYY(new Date()),
      testPlanId: id,
      codeSuite: nextCodeSuite,
    };

    try {
      const response = await axios.post('http://localhost:8081/test-suites', newSuite);
      console.log('Suite criada com sucesso:', response.data);
      setSuites([...suites, response.data]);
      setNextCode(nextCodeSuite + 1);
    } catch (error) {
      console.error('Erro ao criar suite:', error);
    }
  };

  if (loading) {
    return <div>Carregando...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  if (!startDate || !deliveryDate) {
    return <div>Datas não carregadas.</div>;
  }

  return (
    <Container style={{ marginTop: '20px', marginBottom: '20px' }}>
      <NavHorizontal />
      <NavVertical />
      <br />
      <br />
      <br />
      <Tabs defaultActiveKey="edit-test-plan" id="fill-tab-example" className="mb-3" fill>
        {/* Primeira Aba - Editar Plano de Teste */}
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
              <Col md={6}>
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
                <Form.Group controlId="formMatriz">
                  <Form.Label>Matriz</Form.Label>
                  <Form.Control
                    type="text"
                    value={matriz}
                    onChange={(e) => setMatriz(e.target.value)}
                    placeholder="50000101"
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

        {/* Segunda Aba - Suites */}
        <Tab eventKey="suites-tab" title="Suites">
          <div style={{ margin: '20px' }}>
            <Button onClick={handleCreateSuite} variant="success" style={{ marginBottom: '20px' }}>
              Nova Suite
            </Button>
            <ListGroup>
              {suites.map((suite) => (
                <ListGroup.Item key={suite.id}>
                  <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
                    <div>
                      <strong>Suite #{suite.codeSuite}</strong> - Data: {suite.data}
                    </div>
                    <div>
                      <FaEdit
                        style={{ color: "#0d6efd", cursor: "pointer", marginRight: "15px" }}
                        onClick={() => navigate(`/plan/${id}/suite/${suite.id}`)}
                      />
                      <HiOutlineDuplicate
                        style={{ color: "#6c757d", cursor: "pointer" }}
                        onClick={() => alert(`Duplicar suite ${suite.codeSuite}`)}
                      />
                    </div>
                  </div>
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
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
import { useNavigate } from 'react-router-dom';

function CreateTestPlan() {
  const [startDate, setStartDate] = useState(new Date());
  const [deliveryDate, setDeliveryDate] = useState(new Date());



  const [selectedStatus, setSelectedStatus] = useState("EM_PROGRESSO");
  const [selectedTaskStatus, setSelectedTaskStatus] = useState("in_progress");
  const [isTaskCreated, setIsTaskCreated] = useState(false); // Estado para o switch
  const [matriz, setMatriz] = useState("");
  const [username, setUsername] = useState(""); // Nome de Usuário
  const [password, setPassword] = useState(""); // Senha
  const [name, setName] = useState(""); // Nome
  const [jira, setJira] = useState(""); // JIRA
  const [callNumber, setCallNumber] = useState(""); // Número de Chamada
  const [observations, setObservations] = useState(""); // Observações


  // Estados para armazenar dados vindos do backend



  // Estado para armazenar as suites criadas
  const [suites, setSuites] = useState([]);


  interface Modulo {

    id: number;
    name: string;
    active: boolean;
  }
  const [modules, setModules] = useState<Modulo[]>([]);
  const [selectedModulo, setSelectedModulo] = useState<string | null>(null);

  useEffect(() => {
    axios.get<Modulo[]>("http://localhost:8081/modules")
      .then((response) => {
        const activeModulo = response.data.filter((modulo) => modulo.active);
        setModules(activeModulo);
      })
      .catch((error) => console.error("Erro ao buscar Módulos:", error));
  }, []);

  interface Tester {
    id: number;
    name: string;
    active: boolean;
  }
  const [testers, setTesters] = useState<Tester[]>([]);
  const [selectedTester, setSelectedTester] = useState<string | null>(null);

  useEffect(() => {
    axios.get<Tester[]>("http://localhost:8081/testers")
      .then((response) => {

        const activeTester = response.data.filter((teste) => teste.active);
        setTesters(activeTester);
      })
      .catch((error) => console.error("Erro ao buscar testadores:", error));
  }, []);


  interface Developer {
    id: number;
    name: string;
    active: boolean;
    technologyIds: number[];
  }

  const [developers, setDevelopers] = useState<Developer[]>([]);
  const [selectedDeveloper, setSelectedDeveloper] = useState<string | null>(null);

  useEffect(() => {
    axios.get<Developer[]>('http://localhost:8081/developers')
      .then((response) => {
        const activeDevelopers = response.data.filter((dev) => dev.active);
        setDevelopers(activeDevelopers); // Armazena os objetos inteiros
      })
      .catch((error) => console.error("Erro ao buscar desenvolvedores:", error));
  }, []);

  // Função para enviar os dados do formulário para o backend
  const handleSubmit = (e) => {
    e.preventDefault();



    const formattedStartDate = startDate.toLocaleDateString('pt-BR'); // Formata a data como DD/MM/YYYY
    const formattedDeliveryDate = deliveryDate.toLocaleDateString('pt-BR'); // Formata a data como DD/MM/YYYY

    const testPlanData = {
      name: name || "Sem nome", // Garante que o campo não seja null
      created: isTaskCreated,
      observation: observations || "Sem observações", // Garante que o campo não seja null
      status: selectedStatus || "EM_PROGRESSO", // Garante que o campo não seja null
      jira: jira || "Sem JIRA", // Garante que o campo não seja null
      data: formattedStartDate, // Data formatada
      deliveryData: formattedDeliveryDate, // Data de entrega formatada
      matriz: matriz || "Sem matriz",
      userName: username || "Sem usuário", // Garante que o campo não seja null
      callNumber: callNumber || "Sem número", // Garante que o campo não seja null
      developerId: selectedDeveloper || 1, // Garante que o campo não seja null (valor padrão 1)
      systemModuleId: selectedModulo || 1, // Ajustado para seguir a mesma lógica dos outros selects
      testerId: selectedTester || 1, // Garante que o campo não seja null (valor padrão 1)
      password: password || "Sem senha", // Garante que o campo não seja null
      testeSuiteId: [], // Array vazio por padrão
    };

    axios.post('http://localhost:8081/testplans', testPlanData)
      .then((response) => {
        console.log("Plano de Teste Criado:", response.data);
        alert("Plano de Teste criado com sucesso!");
      })
      .catch((error) => {
        console.error("Erro ao criar Plano de Teste:", error);
        alert("Erro ao criar Plano de Teste. Verifique o console para mais detalhes.");
      });
  };

  // Função para criar uma nova suite via endpoint Axios
  const handleCreateSuite = () => {
    const newSuite = {
      id: suites.length + 1, // Simulação de ID incremental
      status: "EM_PROGRESSO",
      data: new Date().toLocaleDateString('pt-BR'), // Data no formato DD/MM/YYYY
    };

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
    <Container style={{ marginTop: '20px', marginBottom: '20px' }}>
      {/* Menus */}
      <NavHorizontal />
      <NavVertical />
      <br />
      <br />
      <br />

      {/* Tabs */}
      <Tabs defaultActiveKey="create-test-plan" id="fill-tab-example" className="mb-3" fill>
        {/* Primeira Aba - Criar Plano de Teste */}
        <Tab eventKey="create-test-plan" title="Criar Plano de Teste">
          <Form onSubmit={handleSubmit} style={{ margin: '20px', padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
            {/* Linha 1: Data de Entrega e Data */}
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

            {/* Linha 2: Nome de Usuário e Senha */}
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

            {/* Linha 3: Nome, Jira e Número de Chamada */}
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
                      const target = e.target as HTMLInputElement; // Cast para HTMLInputElement
                      target.setCustomValidity("Por favor, Informe a descrição da UL.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLInputElement; // Cast para HTMLInputElement
                      target.setCustomValidity(""); // Reseta a mensagem de erro
                    }}
                    isInvalid={!name} // Valida se o campo está vazio, para mostrar o erro
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
                      const target = e.target as HTMLInputElement; // Cast para HTMLInputElement
                      target.setCustomValidity("Por favor, Informe o número da UL.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLInputElement; // Cast para HTMLInputElement
                      target.setCustomValidity(""); // Reseta a mensagem de erro
                    }}
                    isInvalid={!jira} // Valida se o campo está vazio, para mostrar o erro
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

            {/* Linha 4: Testador, Desenvolvedor e Módulo do Sistema */}
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
                      const target = e.target as HTMLSelectElement; // Cast para HTMLSelectElement
                      target.setCustomValidity("Por favor, selecione um tester antes de continuar.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLSelectElement; // Cast para HTMLSelectElement
                      target.setCustomValidity(""); // Reseta a mensagem de erro
                    }}
                    isInvalid={!selectedTester} // Define como inválido se não houver tester selecionado
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
                      const target = e.target as HTMLSelectElement; // Cast para HTMLSelectElement
                      target.setCustomValidity("Por favor, selecione um desenvolvedor antes de continuar.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLSelectElement; // Cast para HTMLSelectElement
                      target.setCustomValidity(""); // Reseta a mensagem de erro
                    }}
                    isInvalid={!selectedDeveloper} // Define como inválido se não houver tester selecionado
                  >
                    <option value="">Selecione um desenvolvedor</option>
                    {developers.map((dev) => (
                      <option key={dev.id} value={dev.id}>
                        {dev.name} {/* Exibe apenas o nome */}
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
                      const target = e.target as HTMLSelectElement; // Cast para HTMLSelectElement
                      target.setCustomValidity("Por favor, selecione um Módulo antes de continuar.");
                    }}
                    onInput={(e) => {
                      const target = e.target as HTMLSelectElement; // Cast para HTMLSelectElement
                      target.setCustomValidity(""); // Reseta a mensagem de erro
                    }}
                    isInvalid={!selectedModulo} // Define como inválido se não houver tester selecionado
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

            {/* Linha 6: Observações */}
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
                {/* Botão Salvar */}
                <div style={{ textAlign: 'right', marginTop: '20px' }}>
                  <Button type="submit" variant="secondary" onClick={() => navigate('/planoTeste/listagem')} >
                    Voltar
                  </Button>
                </div>
              </Col>
              <Col md={2}>
                {/* Botão Salvar */}
                <div style={{ textAlign: 'right', marginTop: '20px' }}>
                  <Button type="submit" variant="primary">
                    Salvar
                  </Button>
                </div>
              </Col>
            </Row>
          </Form>
        </Tab>

        {/* Segunda Aba - Outra Aba */}
        <Tab eventKey="other-tab" title="Outra Aba">
          <div style={{ margin: '20px' }}>
            <Button onClick={handleCreateSuite} variant="success" style={{ marginBottom: '20px' }}>
              Nova Suite
            </Button>

            <Container fluid="md">
              <Row>
                <Col>1 of 1 teste teste  teste teste </Col>
              </Row>
              <Row>
                <Col>1 of 1 teste teste  teste teste </Col>
              </Row>
            </Container>
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

export default CreateTestPlan;
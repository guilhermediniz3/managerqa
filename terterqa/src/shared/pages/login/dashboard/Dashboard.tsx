import React, { useState, useEffect } from 'react';
import { Form, ProgressBar, Row, Col, Button } from 'react-bootstrap';
import { PieChart, Pie, Cell, Tooltip, Legend } from 'recharts';
import { RiFileExcel2Line, RiCloseCircleFill } from 'react-icons/ri'; // Ícone do Excel e "X"
import { ImFilePdf } from 'react-icons/im'; // Ícone do PDF
import NavHorizontal from '../../../components/navs/horizontal/NavHorizontal';
import axios from 'axios'; // Importando Axios
import './styless.css';

const Dashboard: React.FC = () => {
  // Estados para os inputs de data
  const [dataDe, setDataDe] = useState<string>('');
  const [dataAte, setDataAte] = useState<string>('');

  // Estados para os dados do gráfico de pizza e barra de tarefas
  const [dataPizza, setDataPizza] = useState([
    { name: 'CRIADAS', value: 0 },
    { name: 'RETORNO', value: 0 },
    { name: 'CONCLUÍDA', value: 0 },
  ]);

  const [progressoStatus, setProgressoStatus] = useState({
    EM_PROGRESS: 0,
    IMPEDIMENTO: 0,
    RETORNO: 0,
    CONCLUIDA: 0,
  });

  // Cores para cada status no gráfico de pizza
  const coresPorStatus = {
    CRIADAS: '#00BFFF', // Azul
    RETORNO: '#FF4444', // Vermelho
    CONCLUÍDA: '#00C49F', // Verde
  };

  // Função para calcular a porcentagem de cada item
  const calcularPorcentagem = (value: number, total: number) => {
    return ((value / total) * 100).toFixed(2) + '%';
  };

  // Total dos valores para cálculo da porcentagem
  const totalValores = dataPizza.reduce((acc, item) => acc + item.value, 0);

  // Funções para exportar Excel e PDF (a serem implementadas)
  const handleExportExcel = () => {
    console.log('Exportar para Excel');
    // Aqui você pode chamar o endpoint para exportar Excel
  };

  const handleExportPdf = () => {
    console.log('Exportar para PDF');
    // Aqui você pode chamar o endpoint para exportar PDF
  };

  // Função para buscar dados dos endpoints usando Axios
  const fetchData = async () => {
    try {
      // Formata as datas para o formato ISO (yyyy-MM-dd)
      const dataDeFormatada = new Date(dataDe).toISOString().split('T')[0];
      const dataAteFormatada = new Date(dataAte).toISOString().split('T')[0];

      console.log('Fazendo requisições com datas:', { dataDeFormatada, dataAteFormatada });

      // Faz as chamadas aos endpoints usando Axios (com a porta 8081)
      const [concluidas, retorno, emProgresso, impedimento, criadas] = await Promise.all([
        axios.get(`http://localhost:8081/testplans/count-concluidas?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
        axios.get(`http://localhost:8081/testplans/count-retorno?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
        axios.get(`http://localhost:8081/testplans/count-em-progresso?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
        axios.get(`http://localhost:8081/testplans/count-impedimento?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
        axios.get(`http://localhost:8081/testplans/count-created-true?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
      ]);

      console.log('Dados retornados:', { concluidas, retorno, emProgresso, impedimento, criadas });

      // Filtra os dados para o gráfico de pizza, removendo status com valor 0
      const dadosFiltrados = [
        { name: 'CRIADAS', value: criadas.data },
        { name: 'RETORNO', value: retorno.data },
        { name: 'CONCLUÍDA', value: concluidas.data },
      ].filter((item) => item.value > 0); // Remove itens com valor 0

      // Atualiza os dados do gráfico de pizza
      setDataPizza(dadosFiltrados);

      // Filtra os dados da barra de progresso, removendo status com valor 0
      const dadosBarraProgresso = {
        EM_PROGRESS: emProgresso.data,
        IMPEDIMENTO: impedimento.data,
        RETORNO: retorno.data,
        CONCLUIDA: concluidas.data,
      };

      // Calcula o total para a barra de progresso apenas com valores maiores que 0
      const totalProgresso = Object.values(dadosBarraProgresso).reduce((acc, value) => acc + value, 0);

      // Atualiza os dados da barra de tarefas (EM PROGRESSO, IMPEDIMENTO, RETORNO, CONCLUIDA)
      setProgressoStatus({
        EM_PROGRESS: totalProgresso > 0 ? (emProgresso.data / totalProgresso) * 100 : 0,
        IMPEDIMENTO: totalProgresso > 0 ? (impedimento.data / totalProgresso) * 100 : 0,
        RETORNO: totalProgresso > 0 ? (retorno.data / totalProgresso) * 100 : 0,
        CONCLUIDA: totalProgresso > 0 ? (concluidas.data / totalProgresso) * 100 : 0,
      });
    } catch (error) {
      console.error('Erro ao buscar dados:', error);
      if (error.response) {
        // Erro retornado pelo backend
        console.error('Resposta do erro:', error.response.data);
        console.error('Status do erro:', error.response.status);
      } else if (error.request) {
        // A requisição foi feita, mas não houve resposta
        console.error('Sem resposta do servidor:', error.request);
      } else {
        // Outros erros
        console.error('Erro ao configurar a requisição:', error.message);
      }
    }
  };

  // Função para limpar os inputs de data
  const limparInput = (campo: 'dataDe' | 'dataAte') => {
    if (campo === 'dataDe') {
      setDataDe('');
    } else {
      setDataAte('');
    }
  };

  // Executa a busca dos dados quando as datas mudam
  useEffect(() => {
    console.log('useEffect acionado:', { dataDe, dataAte }); // Verifique se isso aparece no console
    if (dataDe && dataAte) {
      fetchData();
    }
  }, [dataDe, dataAte]); // Dependências do useEffect

  // Define a data atual como valor padrão ao carregar o componente
  useEffect(() => {
    const dataAtual = new Date().toISOString().split('T')[0];
    setDataDe(dataAtual);
    setDataAte(dataAtual);
  }, []);

  return (
    <div className="dashboardContainer">
      <NavHorizontal />
      <div className="dashboardContent">
        <h1>Bem-vindo ao Dashboard</h1>

        {/* Inputs de Data */}
        <Row className="mb-4">
          <Col md={3}>
            <Form.Group>
              <Form.Label>De</Form.Label>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <Form.Control
                  type="date"
                  value={dataDe}
                  onChange={(e) => {
                    console.log('Data De alterada:', e.target.value); // Verifique no console
                    setDataDe(e.target.value);
                  }}
                  placeholder="De"
                />
                <RiCloseCircleFill
                  style={{ marginLeft: '5px', cursor: 'pointer', color: '#dc3545' }}
                  onClick={() => limparInput('dataDe')}
                  title="Limpar data"
                />
              </div>
            </Form.Group>
          </Col>
          <Col md={3}>
            <Form.Group>
              <Form.Label>Até</Form.Label>
              <div style={{ display: 'flex', alignItems: 'center' }}>
                <Form.Control
                  type="date"
                  value={dataAte}
                  onChange={(e) => {
                    console.log('Data Até alterada:', e.target.value); // Verifique no console
                    setDataAte(e.target.value);
                  }}
                  placeholder="Até"
                />
                <RiCloseCircleFill
                  style={{ marginLeft: '5px', cursor: 'pointer', color: '#dc3545' }}
                  onClick={() => limparInput('dataAte')}
                  title="Limpar data"
                />
              </div>
            </Form.Group>
          </Col>
        </Row>

        {/* Gráfico de Pizza com Porcentagens */}
        <Row className="mb-4">
          <Col md={6} style={{ marginLeft: '60px' }}> {/* Aumentei ainda mais a margem esquerda */}
            <h4>Status das Tarefas</h4>
            <PieChart width={320} height={300}> {/* Reduzi o tamanho do gráfico */}
              <Pie
                data={dataPizza}
                cx="50%"
                cy="50%"
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
                label={({ value }) => calcularPorcentagem(value, totalValores)}
                labelLine={false} // Remove as linhas dos rótulos
              >
                {dataPizza.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={coresPorStatus[entry.name]} />
                ))}
              </Pie>
              <Tooltip formatter={(value) => calcularPorcentagem(value, totalValores)} />
              <Legend />
              
            </PieChart>
          </Col>
        </Row>

        {/* Barra de Status (sem espaço vazio) */}
        <Row className="mb-4">
          <Col md={6}>
            <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
              <h4>Progresso da Sprint</h4>
              <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                <RiFileExcel2Line
                  style={{ color: "#198754", cursor: "pointer" }}
                  onClick={handleExportExcel}
                  title="Exportar para Excel"
                />
                <ImFilePdf
                  style={{ color: "#dc3545", cursor: "pointer" }}
                  onClick={handleExportPdf}
                  title="Exportar para PDF"
                />
              </div>
            </div>
            <div>
              <ProgressBar>
                {progressoStatus.EM_PROGRESS > 0 && (
                  <ProgressBar
                    striped
                    variant="primary" // Azul
                    now={progressoStatus.EM_PROGRESS}
                    key={1}
                    label={`${progressoStatus.EM_PROGRESS.toFixed(2)}%`}
                    title="EM PROGRESSO" // Tooltip ao passar o mouse
                  />
                )}
                {progressoStatus.IMPEDIMENTO > 0 && (
                  <ProgressBar
                    striped
                    variant="warning" // Amarelo
                    now={progressoStatus.IMPEDIMENTO}
                    key={2}
                    label={`${progressoStatus.IMPEDIMENTO.toFixed(2)}%`}
                    title="IMPEDIMENTO" // Tooltip ao passar o mouse
                  />
                )}
                {progressoStatus.RETORNO > 0 && (
                  <ProgressBar
                    striped
                    variant="danger" // Vermelho
                    now={progressoStatus.RETORNO}
                    key={3}
                    label={`${progressoStatus.RETORNO.toFixed(2)}%`}
                    title="RETORNO" // Tooltip ao passar o mouse
                  />
                )}
                {progressoStatus.CONCLUIDA > 0 && (
                  <ProgressBar
                    striped
                    variant="success" // Verde
                    now={progressoStatus.CONCLUIDA}
                    key={4}
                    label={`${progressoStatus.CONCLUIDA.toFixed(2)}%`}
                    title="CONCLUÍDA" // Tooltip ao passar o mouse
                  />
                )}
              </ProgressBar>
            </div>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default Dashboard;
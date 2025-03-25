import React, { useState, useEffect } from 'react';
import { Form, ProgressBar, Row, Col } from 'react-bootstrap';
import { PieChart, Pie, Cell, Tooltip, Legend } from 'recharts';
import { RiFileExcel2Line, RiCloseCircleFill } from 'react-icons/ri';
import { ImFilePdf } from 'react-icons/im';
import NavHorizontal from '../../../components/navs/horizontal/NavHorizontal';
import axios from 'axios';
import './styless.css';

const API_BASE_URL = 'http://localhost:8081';

const Dashboard: React.FC = () => {
  // Função para obter a data atual no formato YYYY-MM-DD (local)
  const getCurrentLocalDate = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  // Estados para os inputs de data
  const [dataDe, setDataDe] = useState<string>(getCurrentLocalDate());
  const [dataAte, setDataAte] = useState<string>(getCurrentLocalDate());
  const [loading, setLoading] = useState<boolean>(false);

  // Estados para os dados
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

  // Cores para o gráfico
  const coresPorStatus = {
    CRIADAS: '#00BFFF',
    RETORNO: '#FF4444',
    CONCLUÍDA: '#00C49F',
  };

  // Funções de utilidade
  const calcularPorcentagem = (value: number, total: number) => {
    return total > 0 ? ((value / total) * 100).toFixed(2) + '%' : '0%';
  };

  const totalValores = dataPizza.reduce((acc, item) => acc + item.value, 0);

  // Funções de exportação
  const handleExportExcel = async () => {
    try {
      if (!dataDe || !dataAte) {
        alert('Selecione as datas primeiro');
        return;
      }

      setLoading(true);
      const dataDeFormatada = new Date(dataDe).toISOString().split('T')[0];
      const dataAteFormatada = new Date(dataAte).toISOString().split('T')[0];

      const response = await axios.get(`${API_BASE_URL}/relatorios/sprint-excel?dataInicio=${dataDeFormatada}&dataFim=${dataAteFormatada}`, {
        responseType: 'blob'
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `sprints_${dataDeFormatada}_a_${dataAteFormatada}.xlsx`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);

    } catch (error) {
      console.error('Erro ao exportar Excel:', error);
      alert('Erro ao exportar para Excel');
    } finally {
      setLoading(false);
    }
  };

  const handleExportPdf = async () => {
    try {
      if (!dataDe || !dataAte) {
        alert('Selecione as datas primeiro');
        return;
      }

      setLoading(true);
      const dataDeFormatada = new Date(dataDe).toISOString().split('T')[0];
      const dataAteFormatada = new Date(dataAte).toISOString().split('T')[0];

      const response = await axios.get(`${API_BASE_URL}/relatorios/sprint-pdf?dataInicio=${dataDeFormatada}&dataFim=${dataAteFormatada}`, {
        responseType: 'blob'
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `sprints_${dataDeFormatada}_a_${dataAteFormatada}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);

    } catch (error) {
      console.error('Erro ao exportar PDF:', error);
      alert('Erro ao exportar para PDF');
    } finally {
      setLoading(false);
    }
  };

  // Busca dos dados
  const fetchData = async () => {
    try {
      if (!dataDe || !dataAte) return;

      setLoading(true);
      const dataDeFormatada = new Date(dataDe).toISOString().split('T')[0];
      const dataAteFormatada = new Date(dataAte).toISOString().split('T')[0];

      const [concluidas, retorno, emProgresso, impedimento, criadas] = await Promise.all([
        axios.get(`${API_BASE_URL}/testplans/count-concluidas?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
        axios.get(`${API_BASE_URL}/testplans/count-retorno?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
        axios.get(`${API_BASE_URL}/testplans/count-em-progresso?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
        axios.get(`${API_BASE_URL}/testplans/count-impedimento?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
        axios.get(`${API_BASE_URL}/testplans/count-created-true?dataDe=${dataDeFormatada}&dataAte=${dataAteFormatada}`),
      ]);

      const dadosFiltrados = [
        { name: 'CRIADAS', value: criadas.data },
        { name: 'RETORNO', value: retorno.data },
        { name: 'CONCLUÍDA', value: concluidas.data },
      ].filter(item => item.value > 0);

      setDataPizza(dadosFiltrados.length > 0 ? dadosFiltrados : [
        { name: 'CRIADAS', value: 0 },
        { name: 'RETORNO', value: 0 },
        { name: 'CONCLUÍDA', value: 0 },
      ]);

      const totalProgresso = emProgresso.data + impedimento.data + retorno.data + concluidas.data;

      setProgressoStatus({
        EM_PROGRESS: totalProgresso > 0 ? (emProgresso.data / totalProgresso) * 100 : 0,
        IMPEDIMENTO: totalProgresso > 0 ? (impedimento.data / totalProgresso) * 100 : 0,
        RETORNO: totalProgresso > 0 ? (retorno.data / totalProgresso) * 100 : 0,
        CONCLUIDA: totalProgresso > 0 ? (concluidas.data / totalProgresso) * 100 : 0,
      });

    } catch (error) {
      console.error('Erro ao buscar dados:', error);
    } finally {
      setLoading(false);
    }
  };

  // Limpar inputs
  const limparInput = (campo: 'dataDe' | 'dataAte') => {
    if (campo === 'dataDe') setDataDe('');
    else setDataAte('');
  };

  // Efeitos
  useEffect(() => {
    fetchData();
  }, [dataDe, dataAte]);

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
                  onChange={(e) => setDataDe(e.target.value)}
                  disabled={loading}
                />
                <RiCloseCircleFill
                  style={{ marginLeft: '5px', cursor: 'pointer', color: '#dc3545' }}
                  onClick={() => !loading && limparInput('dataDe')}
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
                  onChange={(e) => setDataAte(e.target.value)}
                  disabled={loading}
                />
                <RiCloseCircleFill
                  style={{ marginLeft: '5px', cursor: 'pointer', color: '#dc3545' }}
                  onClick={() => !loading && limparInput('dataAte')}
                />
              </div>
            </Form.Group>
          </Col>
        </Row>

        {/* Gráfico e Exportação */}
        <Row className="mb-4">
          <Col md={6} style={{ marginLeft: '60px' }}>
            <h4>Status das Tarefas</h4>
            {loading ? (
              <p>Carregando...</p>
            ) : (
              <PieChart width={320} height={300}>
                <Pie
                  data={dataPizza}
                  cx="50%"
                  cy="50%"
                  outerRadius={80}
                  dataKey="value"
                  label={({ value }) => calcularPorcentagem(value, totalValores)}
                >
                  {dataPizza.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={coresPorStatus[entry.name]} />
                  ))}
                </Pie>
                <Tooltip formatter={(value) => calcularPorcentagem(value, totalValores)} />
                <Legend />
              </PieChart>
            )}
          </Col>
        </Row>

        <Row className="mb-4">
          <Col md={6}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <h4>Progresso da Sprint</h4>
              <div>
                <RiFileExcel2Line
                  style={{ color: '#198754', cursor: 'pointer', marginRight: '10px' }}
                  onClick={handleExportExcel}
                  title="Exportar Excel"
                />
                <ImFilePdf
                  style={{ color: '#dc3545', cursor: 'pointer' }}
                  onClick={handleExportPdf}
                  title="Exportar PDF"
                />
              </div>
            </div>
            {loading ? (
              <p>Carregando...</p>
            ) : (
              <ProgressBar>
                {progressoStatus.EM_PROGRESS > 0 && (
                  <ProgressBar
                    striped
                    variant="primary"
                    now={progressoStatus.EM_PROGRESS}
                    label={`${progressoStatus.EM_PROGRESS.toFixed(2)}%`}
                  />
                )}
                {progressoStatus.IMPEDIMENTO > 0 && (
                  <ProgressBar
                    striped
                    variant="warning"
                    now={progressoStatus.IMPEDIMENTO}
                    label={`${progressoStatus.IMPEDIMENTO.toFixed(2)}%`}
                  />
                )}
                {progressoStatus.RETORNO > 0 && (
                  <ProgressBar
                    striped
                    variant="danger"
                    now={progressoStatus.RETORNO}
                    label={`${progressoStatus.RETORNO.toFixed(2)}%`}
                  />
                )}
                {progressoStatus.CONCLUIDA > 0 && (
                  <ProgressBar
                    striped
                    variant="success"
                    now={progressoStatus.CONCLUIDA}
                    label={`${progressoStatus.CONCLUIDA.toFixed(2)}%`}
                  />
                )}
              </ProgressBar>
            )}
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default Dashboard;
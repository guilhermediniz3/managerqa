// src/shared/pages/dashboard/Dashboard.tsx
import React from 'react';
import { Button, Container, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const Dashboard: React.FC = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Aqui você pode implementar a lógica de logout
    navigate('/'); // Redireciona de volta para a página de login
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <Row className="w-100">
        <Col xs={12} className="text-center">
          <h2>Bem-vindo ao seu Dashboard!</h2>
          <Button variant="danger" onClick={handleLogout}>
            Logout
          </Button>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard;

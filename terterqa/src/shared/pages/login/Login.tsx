import React, { useState } from 'react';
import { Button, Form, Container, Row, Col, Card } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import ToggleThemeButton from '../../components/Buttons/ToggleThemeButton';
import loginImage from '../../../assets/login.png';
import './Login.css';  // Importe o arquivo CSS

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Aqui você pode implementar a lógica de autenticação
    console.log('Email:', email);
    console.log('Password:', password);
    navigate('/dashboard'); // Redireciona para o Dashboard após login
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <Row className="w-100">
        <Col xs={12} md={6} lg={4} className="image-container">
          <Card className="shadow-lg rounded">
            <Card.Body>
              <h2 className="text-center mb-4">Login</h2>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <Form.Label>Email address</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                  />
                </Form.Group>
                <Button variant="primary" type="submit" className="w-100 mb-3">
                  Entrar
                </Button>
              </Form>
              <div className="text-center">
                <Link to="/forgot-password">Esqueceu a senha?</Link>
              </div>
              <div className="text-center mt-2">
                <Link to="/register">Ainda não tem uma conta? Registre-se</Link>
              </div>
              {/* Aqui você insere o ToggleThemeButton */}
              <div className="text-center mt-3">
                <ToggleThemeButton />
              </div>
            </Card.Body>
          </Card>
        </Col>
        {/* Coloque a imagem do lado direito */}
        <Col xs={12} md={6} className="d-none d-md-block">
          <div className="image-wrapper">
            <img src={loginImage} alt="Login" className="img-fluid" />
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;

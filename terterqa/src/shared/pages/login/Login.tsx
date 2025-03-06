import React, { useState } from 'react';
import { Button, Form, Container, Row, Col, Card } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import ToggleThemeButton from '../../components/Buttons/ToggleThemeButton';
import loginImage from '../../../assets/login.png';
import './Login.css';

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8081/auth/login', {
        email,
        password,
      });

      console.log('Resposta do backend:', response.data);

      if (response.status === 200) {
        const { token, user } = response.data;

        // Armazena o token no localStorage
        localStorage.setItem('token', token);
        console.log('Token armazenado no localStorage:', token);

        // Armazena os dados do usuário no localStorage (opcional)
        if (user) {
          localStorage.setItem('user', JSON.stringify(user));
          console.log('Dados do usuário armazenados no localStorage:', user);
        }

        // Envia o token para a ProtectedRoute e navega para o dashboard
        navigate('/dashboard', { state: { token } });
      }
    } catch (error) {
      setErrorMessage('Falha na autenticação. Verifique suas credenciais e tente novamente.');
      console.error('Erro ao fazer login:', error);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <Row className="w-100">
        <Col xs={12} md={6} lg={4} className="image-container">
          <Card className="shadow-lg rounded">
            <Card.Body>
              <h2 className="text-center mb-4">Login</h2>
              {errorMessage && <div className="alert alert-danger text-center">{errorMessage}</div>}
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
              <div className="text-center mt-3">
                <ToggleThemeButton />
              </div>
            </Card.Body>
          </Card>
        </Col>
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
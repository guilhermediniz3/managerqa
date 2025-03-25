import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom"; // Importe o hook useNavigate
import Toast from "react-bootstrap/Toast";
import ToastContainer from "react-bootstrap/ToastContainer";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import NavHorizontal from "../../../components/navs/horizontal/NavHorizontal";
import NavVertical from "../../../components/navs/vertical/NavVertical";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [showSuccessToast, setShowSuccessToast] = useState(false);
  const navigate = useNavigate(); // Hook para navegação

  // Função para voltar à listagem de usuários
  const handleBack = () => {
    navigate("/usuarios"); // Redireciona para a página de listagem
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const payload = {
      name: name,
      email: email,
      password: password,
    };
    try {
      const response = await axios.post("http://localhost:8081/auth/register", payload, {
        headers: {
          "Content-Type": "application/json",
        },
        withCredentials: true,
      });
      if (response.status === 200) {
        setSuccess("Usuário registrado com sucesso!");
        setShowSuccessToast(true);
        setShowErrorToast(false);
        console.log(response.data);
      }
    } catch (error) {
      setError("Usuário já existe na base de dados.");
      setShowErrorToast(true);
      setShowSuccessToast(false);
      console.error(error);
    }
  };

  return (
    <>
      <NavHorizontal /> {/* NavHorizontal no topo da página */}
      <div style={{ display: "flex" }}>
        <NavVertical /> {/* NavVertical no lado esquerdo */}
        <Container className="d-flex justify-content-center align-items-center vh-100">
          <Row>
            <Col>
              <h2 className="text-center mb-4">Register</h2>
              <Form onSubmit={handleSubmit} className="p-4 border rounded shadow" style={{ maxWidth: "400px", width: "100%" }}>
                <Form.Group controlId="formName" className="mb-3">
                  <Form.Label>Name</Form.Label>
                  <Form.Control
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                  />
                </Form.Group>
                <Form.Group controlId="formEmail" className="mb-3">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </Form.Group>
                <Form.Group controlId="formPassword" className="mb-3">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </Form.Group>
                <div className="d-grid gap-2">
                  <Button variant="success" type="submit">
                    Salvar
                  </Button>
                  <Button variant="secondary" onClick={handleBack}>
                    Voltar
                  </Button>
                </div>
              </Form>
            </Col>
          </Row>
          {/* Toast de Sucesso */}
          <ToastContainer position="top-end" className="p-3">
            <Toast
              onClose={() => setShowSuccessToast(false)}
              show={showSuccessToast}
              delay={3000}
              autohide
              bg="success"
            >
              <Toast.Header>
                <strong className="me-auto">Success</strong>
                <small>Now</small>
              </Toast.Header>
              <Toast.Body>{success}</Toast.Body>
            </Toast>
          </ToastContainer>
          {/* Toast de Erro */}
          <ToastContainer position="top-end" className="p-3">
            <Toast
              onClose={() => setShowErrorToast(false)}
              show={showErrorToast}
              delay={3000}
              autohide
              bg="danger"
            >
              <Toast.Header>
                <strong className="me-auto">Error</strong>
                <small>Now</small>
              </Toast.Header>
              <Toast.Body>{error}</Toast.Body>
            </Toast>
          </ToastContainer>
        </Container>
      </div>
    </>
  );
}

export default Register;
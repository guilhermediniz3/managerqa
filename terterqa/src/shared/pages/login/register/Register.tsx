import axios from "axios";
import { useState } from "react";
import Toast from "react-bootstrap/Toast";
import ToastContainer from "react-bootstrap/ToastContainer";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [showErrorToast, setShowErrorToast] = useState(false); // Estado para controlar o Toast de erro
  const [showSuccessToast, setShowSuccessToast] = useState(false); // Estado para controlar o Toast de sucesso

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
        setShowSuccessToast(true); // Mostra o Toast de sucesso
        setShowErrorToast(false); // Esconde o Toast de erro, se estiver visível
        console.log(response.data);
      }
    } catch (error) {
      setError("Usuário já existe na base de dados.");
      setShowErrorToast(true); // Mostra o Toast de erro
      setShowSuccessToast(false); // Esconde o Toast de sucesso, se estiver visível
      console.error(error);
    }
  };

  return (
    <div>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Register</button>
      </form>

      {/* Toast de Sucesso */}
      <ToastContainer position="top-end" className="p-3">
        <Toast
          onClose={() => setShowSuccessToast(false)} // Fecha o Toast de sucesso
          show={showSuccessToast}
          delay={3000} // Fecha automaticamente após 3 segundos
          autohide
          bg="success" // Cor de fundo do Toast (verde)
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
          onClose={() => setShowErrorToast(false)} // Fecha o Toast de erro
          show={showErrorToast}
          delay={3000} // Fecha automaticamente após 3 segundos
          autohide
          bg="danger" // Cor de fundo do Toast (vermelho)
        >
          <Toast.Header>
            <strong className="me-auto">Error</strong>
            <small>Now</small>
          </Toast.Header>
          <Toast.Body>{error}</Toast.Body>
        </Toast>
      </ToastContainer>
    </div>
  );
}

export default Register;
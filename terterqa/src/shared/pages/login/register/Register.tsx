import axios from "axios";
import { useState } from "react";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const payload = {
      name: name,
      email: email,
      password: password,
    };

    try {
      const response = await axios.post("http://localhost:9090/auth/register", payload, {
        headers: {
          "Content-Type": "application/json", // Cabeçalho correto para enviar dados em JSON
        },
        withCredentials: true, // Permite enviar cookies ou credenciais, se necessário
      });

      if (response.status === 200) {
        setSuccess("User registered successfully!");
        console.log(response.data); // Log do token ou outros dados
      }
    } catch (error) {
      setError("Error registering user. Please try again.");
      console.error(error);
    }
  };

  return (
    <div>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Name</label>
          <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
        </div>
        <div>
          <label>Email</label>
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </div>
        <div>
          <label>Password</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        </div>
        <button type="submit">Register</button>
      </form>

      {error && <p style={{ color: "red" }}>{error}</p>}
      {success && <p style={{ color: "green" }}>{success}</p>}
    </div>
  );
}

export default Register;

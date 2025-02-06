import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const ChatGPTPage: React.FC = () => {
  const navigate = useNavigate();
  const [token, setToken] = useState<string | null>(null);
  const [messages, setMessages] = useState<{ user: string, bot: string }[]>([]);
  const [userMessage, setUserMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    // Verifica se o usuário está autenticado
    const storedToken = localStorage.getItem('token');
    if (!storedToken) {
      // Se não estiver autenticado, redireciona para o login
      navigate('/login');
    } else {
      setToken(storedToken); // Define o token no estado
    }
  }, [navigate]);

  const handleMessageSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    if (userMessage.trim() === '') return;

    // Adiciona a mensagem do usuário à lista de mensagens
    setMessages([...messages, { user: userMessage, bot: '' }]);
    setLoading(true);
    setUserMessage('');

    try {
      // Simulação de chamada à API do ChatGPT
      const botResponse = await getChatGPTResponse(userMessage);
      setMessages((prevMessages) => [
        ...prevMessages,
        { user: userMessage, bot: botResponse }
      ]);
    } catch (error) {
      console.error('Erro ao enviar mensagem:', error);
      setMessages((prevMessages) => [
        ...prevMessages,
        { user: userMessage, bot: 'Ocorreu um erro ao enviar a mensagem. Tente novamente.' }
      ]);
    } finally {
      setLoading(false);
    }
  };

  // Simula a chamada à API do ChatGPT (substitua com a API real)
  const getChatGPTResponse = async (message: string) => {
    return new Promise<string>((resolve) => {
      setTimeout(() => {
        resolve(`Resposta do ChatGPT para: "${message}"`);
      }, 1000); // Simula um atraso de 1 segundo
    });
  };

  return (
    <div className="chatgpt-container">
      <h1>ChatGPT</h1>
      <div className="chat-window">
        {messages.length === 0 && <p>Comece uma conversa!</p>}
        <div className="messages">
          {messages.map((msg, index) => (
            <div key={index} className="message">
              <div className="user-message">{msg.user}</div>
              {msg.bot && <div className="bot-message">{msg.bot}</div>}
            </div>
          ))}
        </div>
        <form onSubmit={handleMessageSubmit} className="chat-form">
          <input
            type="text"
            value={userMessage}
            onChange={(e) => setUserMessage(e.target.value)}
            placeholder="Digite sua mensagem"
            disabled={loading}
            className="message-input"
          />
          <button type="submit" disabled={loading} className="send-button">
            {loading ? 'Enviando...' : 'Enviar'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default ChatGPTPage;

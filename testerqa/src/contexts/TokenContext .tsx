import React, { createContext, useState, useContext } from 'react';

// Define o tipo do contexto
interface TokenContextType {
  token: string | null;
  setToken: (token: string | null) => void;
}

// Cria o contexto
const TokenContext = createContext<TokenContextType>({
  token: null,
  setToken: () => {},
});

// Provedor do contexto
export const TokenProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'));

  return (
    <TokenContext.Provider value={{ token, setToken }}>
      {children}
    </TokenContext.Provider>
  );
};

// Hook personalizado para usar o contexto
export const useToken = () => useContext(TokenContext);
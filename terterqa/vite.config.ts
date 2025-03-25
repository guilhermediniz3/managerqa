// terterqa/vite.config.ts
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Configuração para redirecionar TUDO para o Spring Boot
      '^/(?!(assets|src|node_modules|@vite)).*': {
        target: 'http://localhost:8081', // Porta do Spring
        changeOrigin: true,
      }
    }
  },
  build: {
    outDir: '../back-end/src/main/resources/static'
  }
})
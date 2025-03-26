import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'
import { fileURLToPath } from 'url'

// Resolve __dirname equivalente em ES Modules
const __dirname = path.dirname(fileURLToPath(import.meta.url))

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      '~bootstrap': path.resolve(__dirname, 'node_modules/bootstrap')
    }
  },
  build: {
    outDir: '../backend/src/main/resources/static',
    emptyOutDir: true,
    rollupOptions: {
      onwarn(warning, warn) {
        // Tratamento especial para erros de importação CSS
        if (warning.code === 'UNRESOLVED_IMPORT' && warning.message.includes('.css')) {
          throw new Error(
            `Erro de importação CSS: ${warning.message}\n` +
            `Verifique o caminho em: ${warning.id}`
          )
        }
        warn(warning)
      }
    }
  },
  server: {
    port: 5173,
    strictPort: true
  },
  preview: {
    port: 5173,
    strictPort: true
  }
})
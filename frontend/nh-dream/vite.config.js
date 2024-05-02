import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    {
      name: 'vite-plugin-splashscreen',
      config: {
        // Optional splashscreen options
      }
    },
  ],
})

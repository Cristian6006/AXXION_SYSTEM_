import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'
import tailwindcss from '@tailwindcss/vite'
import { VitePWA } from 'vite-plugin-pwa'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    vueDevTools(),
    tailwindcss(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['favicon.ico', 'apple-touch-icon.png', 'masked-icon.svg'],
      manifest: {
        name: 'AXXION SYSTEM',
        short_name: 'AXXION',
        description: 'AXXION SYSTEM Application',
        theme_color: '#ffffff',
        icons: [
          {
            src: 'pwa-192x192.png',
            sizes: '192x192',
            type: 'image/png'
          },
          {
            src: 'pwa-512x512.png',
            sizes: '512x512',
            type: 'image/png'
          }
        ]
      },
      workbox: {
        maximumFileSizeToCacheInBytes: 4 * 1024 * 1024
      }
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
    proxy: {
      // Si una petición empieza con '/api', Vite la interceptará.
      '/api': {
        // Y la reenviará a nuestro backend de Laravel.
        target: 'http://127.0.0.1:8000',

        // Esto es importante para evitar problemas con Virtual Hosts.
        changeOrigin: true,

        // Esto es opcional y generalmente no es necesario,
        // pero no hace daño tenerlo para claridad.
        // Le dice a Vite que no modifique la ruta,
        // así que /api/auth/login se reenvía como /api/auth/login.
        rewrite: (path) => path.replace(/^\/api/, '/api'),
      },
    },
  },
})

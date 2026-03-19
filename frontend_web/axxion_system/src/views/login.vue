<template>
  <div class="login-container">
    <div class="background-pattern"></div>
    <div class="login-card">
      <div class="branding-panel">
        <div class="logo-container">
          <div class="logo-shape logo-shape-1"></div>
          <div class="logo-shape logo-shape-2"></div>
          <div class="logo-shape logo-shape-3"></div>
        </div>
        <h1 class="title">AXXION SYSTEM</h1>
        <p class="subtitle">Plataforma de Gestión de Equipos</p>
      </div>
      <div class="form-panel">
        <h2 class="form-title">Iniciar Sesión</h2>
        <!-- El mensaje de error solo se muestra si la variable 'error' tiene contenido -->
        <div v-if="error" class="error-message">{{ error }}</div>
        
        <form @submit.prevent="handleLogin">
          <div class="input-group">
            <label for="email">Email</label>
            <!-- Conectamos el input a nuestra variable reactiva 'email' -->
            <input id="email" type="email" v-model="email" required autocomplete="username">
          </div>
          <div class="input-group">
            <label for="password">Password</label>
            <!-- Conectamos el input a nuestra variable reactiva 'password' -->
            <input id="password" type="password" v-model="password" required autocomplete="current-password">
          </div>
          <!-- El botón se deshabilita mientras 'loading' sea true -->
          <button type="submit" class="login-button" :disabled="loading">
            <span v-if="!loading">Acceder</span>
            <span v-else>Accediendo...</span>
          </button>
        </form>
         <div class="demo-users">
          <p @click="fillDemoCredentials('admin@demo.com', 'admin123')" class="clickable">admin@demo.com / admin123</p>
          <p @click="fillDemoCredentials('aux@demo.com', 'aux123')" class="clickable">aux@demo.com / aux123</p>
          <p @click="fillDemoCredentials('asesor@demo.com', 'asesor123')" class="clickable">asesor@demo.com / asesor123</p>
      </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth'; 
import { useRouter, useRoute } from 'vue-router';

// 1. Hooks de Vue y Pinia
const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();

// 2. Estado del componente (reactivo)
const email = ref('');
const password = ref('');
const error = ref('');
const loading = ref(false);

// 3. Función que maneja el envío del formulario
const handleLogin = async () => {
  // Inicia el estado de carga y limpia errores previos
  loading.value = true;
  error.value = '';

  try {
    // Llama a la acción 'login' de nuestro store de Pinia
    await authStore.login(email.value, password.value);

    // Si la acción anterior tiene éxito (no lanza error), procedemos a redirigir

    const redirectPath = route.query.redirect || { name: 'Home' }; 


    router.push(redirectPath);

  } catch (err) {


    error.value = err.response?.data?.message || 'Las credenciales son incorrectas.';
  } finally {

    loading.value = false;
  }
};


const fillDemoCredentials = (demoEmail, demoPassword) => {
  email.value = demoEmail;
  password.value = demoPassword;
};
</script>



<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&display=swap');

:root {
  --primary-color: #007BFF; /* Professional Blue */
  --dark-grey: #2C3E50;     /* Dark Cool Grey */
  --medium-grey: #8492A6;    /* Medium Cool Grey */
  --light-grey: #003f9c9f;    /* Light Cool Grey */
  --white: #0f4b63c4;
}

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--primary-color);
  font-family: 'Montserrat', sans-serif;
  position: relative;
  overflow: hidden;
}

.background-pattern {
  position: absolute;
  top: 0;
  left: 0;
  width: 200%;
  height: 200%;
  background-image: 
    repeating-linear-gradient(45deg, #2188c450, #c7e4f8 1px, transparent 1px, transparent 20px);
  animation: slide 60s linear infinite;
}

@keyframes slide {
  from { transform: translate(0, 0); }
  to { transform: translate(-50%, -50%); }
}

.login-card {
  display: grid;
  grid-template-columns: 1fr;
  width: 100%;
  max-width: 800px;
  margin: 20px;
  background-color: var(--primary-color);
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  z-index: 1;
}

@media (min-width: 768px) {
  .login-card {
    grid-template-columns: 40% 60%;
  }
}

.branding-panel {
  background-color: var(--dark-grey);
  color: var(--white);
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.logo-container {
  position: relative;
  width: 80px;
  height: 80px;
  margin-bottom: 20px;
  animation: rotate-logo 15s linear infinite;
}

@keyframes rotate-logo {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.logo-shape {
  position: absolute;
  width: 50px;
  height: 50px;
  background-color: var(--primary-color);
}

.logo-shape-1 {
  top: 0; left: 15px;
  clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
}

.logo-shape-2 {
  bottom: 15px; left: 0;
  clip-path: polygon(0 0, 100% 50%, 0 100%);
}

.logo-shape-3 {
  bottom: 15px; right: 0;
  clip-path: polygon(100% 0, 0 50%, 100% 100%);
}

.title {
  font-size: 1.5rem;
  font-weight: 700;
  letter-spacing: 1px;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 0.9rem;
  color: var(--light-grey);
}

.form-panel {
  padding: 40px;
}

.form-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--dark-grey);
  margin-bottom: 30px;
}

.input-group {
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  font-size: 0.9rem;
  color: var(--medium-grey);
  margin-bottom: 5px;
}

.input-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.input-group input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}

.login-button {
  width: 100%;
  padding: 15px;
  background-color: var(--primary-color);
  border: none;
  border-radius: 5px;
  color: var(--white);
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.login-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.login-button:active {
  transform: translateY(0);
}

.demo-users {
  margin-top: 20px;
  font-size: 0.8rem;
  color: #888;
  text-align: center;
}

.demo-users p {
    margin-bottom: 5px;
}

.error-message {
  background-color: #ffdddd;
  border: 1px solid #ff8888;
  color: #d8000c;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 20px;
  text-align: center;
}

</style> 
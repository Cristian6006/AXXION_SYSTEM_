<template>
  <aside :class="`${is_expanded && 'is-expanded'}`">
    <div class="logo">
      <img src="../assets/gallo.png" alt="vue" />
    </div>

    <div class="menu-toggle-wrap">
      <button class="menu-toggle" @click="ToggleMenu">
        <span class="material-icons">keyboard_double_arrow_right</span>
      </button>
    </div>
    <h3>Menu</h3>
    <div class="menu">
      <router-link class="button" to="/Home">
        <span class="material-icons">home</span>
        <span class="text">Inicio</span>
      </router-link>
      <router-link class="button" to="/category">
        <span class="material-icons">category</span>
        <span class="text">Categorias</span>
      </router-link>
      <router-link class="button" to="/typechicken">
        <span class="material-icons">kitchen</span>
        <span class="text">Tipo pollo</span>
      </router-link>
      <router-link class="button" to="/city">
        <span class="material-icons">apartment</span>
        <span class="text">Ciudades</span>
      </router-link>
      <router-link class="button" to="/unit">
        <span class="material-icons">scale</span>
        <span class="text">Unidades</span>
      </router-link>
      <router-link class="button" to="/product">
        <span class="material-icons">store_mall_directory</span>
        <span class="text">Productos</span>
      </router-link>
      <router-link class="button" to="/supplier">
        <span class="material-icons">group</span>
        <span class="text">Proveedores</span>
      </router-link>
      <router-link class="button" to="/storage">
        <span class="material-icons">inventory_2</span>
        <span class="text">Almacemiento</span>
      </router-link>
      <router-link class="button" to="/statistics">
        <span class="material-icons">dashboard</span>
        <span class="text">Estadísticas</span>
      </router-link>
      <router-link class="button" to="/register">
        <span class="material-icons">how_to_reg</span>
        <span class="text">Registrar</span>
      </router-link>
      <router-link class="button" to="/newpassword">
        <span class="material-icons">account_circle</span>
        <span class="text">Nueva Contraseña</span>
      </router-link>
    </div>

    <div class="flex"></div>

    <div class="menu">
      <router-link class="button" to="/settings">
        <span class="material-icons">settings</span>
        <span class="text">Configuración</span>
      </router-link>
    </div>
  </aside>
</template>

<script setup>
import { ref } from "vue";

// Estado reactivo para controlar si la barra lateral está expandida o colapsada.
// Se inicializa leyendo el valor guardado en localStorage.
const is_expanded = ref(localStorage.getItem("is_expanded") === "true");

/**
 * Alterna el estado de expansión del menú y guarda la preferencia en localStorage.
 */
const ToggleMenu = () => {
  is_expanded.value = !is_expanded.value;

  localStorage.setItem("is_expanded", is_expanded.value);
};
</script>

<style lang="scss" scoped>
aside {
  display: flex;
  flex-direction: column;

  background-color: var(--dark);
  color: var(--ligth);

  width: calc(2rem + 32px);
  overflow: hidden;
  min-height: 100vh;
  padding: 1rem;
  z-index: 1000;
  transition: 0.2s ease-out;

  .flex {
    flex: 1 1 0;
  }

  .logo {
    margin-bottom: 1rem;

    img {
      width: 2rem;
    }
  }

  .menu-toggle-wrap {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 1rem;

    position: relative;
    top: 0;
    transition: 0.2s ease-in-out;

    .menu-toggle {
      transition: 0.2s ease-in-out;

      .material-icons {
        font-size: 2rem;
        color: var(--light);
        transition: 0.2s ease-out;
      }

      &:hover {
        .material-icons {
          color: var(--primary);
          transform: translateX(0.5rem);
        }
      }
    }
  }

  h3,
  .button .text {
    opacity: 0;
    transition: 0.3s ease-in-out;
  }

  h3 {
    color: var(--grey);
    font-size: 0.875rem;
    margin-bottom: 0.5rem;
    text-transform: uppercase;
  }

  .menu {
    margin: 0 -1rem;

    .button {
      display: flex;
      align-items: center;
      text-decoration: none;

      transition: 0.2 ease-in-out;
      padding: 0.5rem 1rem;

      .material-icons {
        font-size: 2rem;
        color: var(--light);
        transition: 0.2s ease-in-out;
      }

      .text {
        color: var(--light);
        transition: 0.2s ease-in-out;
      }

      &:hover,
      &.router-link-exact-active {
        background-color: var(--dark-alt);

        .material-icons,
        .text {
          color: var(--primary);
        }
      }

      &.router-link-exact-active {
        border-right: 5px solid var(--primary);
      }
    }
  }

  &.is-expanded {
    width: var(--sidebar-width);

    .menu-toggle-wrap {
      top: -3rem;

      .menu-toggle {
        transform: rotate(-180deg);
      }
    }

    h3,
    .button .text {
      opacity: 1;
    }

    .button {
      .material-icons {
        margin-right: 1rem;
      }
    }
  }

  @media (max-width: 768px) {
    position: fixed;
    z-index: 99;
  }
}
</style>


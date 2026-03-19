<template>
  <footer>
    <fwb-footer class="mt-auto">
      <fwb-footer-copyright
        by="AXION SYSTEM™"
        href="#"
        copyright-message="Todos los derechos reservados."
      />
      <fwb-footer-link-group>
        <fwb-footer-link href="#">
          Acerca de
        </fwb-footer-link>
        <fwb-footer-link href="#">
          Política de Privacidad
        </fwb-footer-link>
        <fwb-footer-link href="#">
          Licencias
        </fwb-footer-link>
        <fwb-footer-link href="#">
          Contacto
        </fwb-footer-link>
      </fwb-footer-link-group>
    </fwb-footer>
  </footer>
</template>

<script setup>
import {
  FwbFooter,
  FwbFooterCopyright,
  FwbFooterLink,
  FwbFooterLinkGroup,
} from 'flowbite-vue';

/**
 * Componente Footer.
 * 
 * Footer reutilizable para todas las vistas del sistema.
 * Incluye:
 * - Copyright de AXION SYSTEM
 * - Enlaces a páginas informativas (Acerca de, Privacidad, Licencias, Contacto)
 */
</script>

<style scoped>
/* Estilos adicionales si son necesarios */
</style>

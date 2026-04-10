<template>
  <div>
    <div class="app flex">
      <SideBar/>
      <RouterView></RouterView>
      <main class="container h-screen p-4 flex-1 overflow-y-auto">
        <HeaderP/>
        <Section class="flex-col">
          <div class="mb-8">
          <h1 class="text-3xl font-extrabold text-gray-900 tracking-tight">Reporte de Usuarios y Seguridad</h1>
          <p class="text-gray-500 mt-2 text-lg">Módulo analítico sobre accesos, roles y alertas del sistema.</p>
        </div>
        <GraphUsuarios/> 
        </Section>
      </main>
    </div>
    <Footer/>
  </div>
</template>

<script setup>
  import SideBar from '@/components/SideBar.vue';
  import Footer from '@/components/Footer.vue';
  import HeaderP from '@/components/headerP.vue';
  import GraphUsuarios from '@/components/Graficos/GraphUsuarios.vue';
</script>

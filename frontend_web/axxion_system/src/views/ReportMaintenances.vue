<template>
  <div>
    <div class="app flex">
      <SideBar/>
      <RouterView></RouterView>
      <main class="container h-screen p-4 flex-1 overflow-y-auto">
        <headerP/>
        <section class="flex flex-col">
          <h1 class="text-2xl"><strong>Reportes de Mantenimientos</strong></h1>
          <br>
          <GraphMant/>
        </section>
      </main>
    </div>
    <Footer/>
  </div>
</template>

<script setup>
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import Footer from '@/components/Footer.vue';
import GraphMant from '@/components/Graficos/GraphMant.vue';
</script>


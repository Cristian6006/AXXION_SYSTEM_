<template>
    <div>
        <div class="app flex">
            <SideBar/>
            <RouterView></RouterView>
            <main class="container h-screen p-4 flex-1 overflow-y-auto">
                <headerP/>
                <section class="flex flex-col">
                    <h1 class="text-2xl"><strong>Reportes de alquiler</strong></h1>
                    <br>
                    <GraphAlq/>
                </section>
            </main>
        </div>
        <Footer/>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import SideBar from '@/components/SideBar.vue';
import headerP from '@/components/headerP.vue';
import GraphAlq from '@/components/Graficos/GraphAlq.vue';
import Footer from '@/components/Footer.vue';

/**
 * Vista ReportAlquiler.
 * 
 * Muestra reportes y gráficos específicos relacionados con los alquileres.
 * Utiliza el componente GraphAlq para visualizar la información.
 */
</script>

<style>

</style>


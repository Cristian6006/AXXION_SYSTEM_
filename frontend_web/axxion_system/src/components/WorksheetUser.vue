<template>
    <section>
        <div class="flex justify-end mb-4 font-bold">
            <fwb-Button
                gradient="green"
                @click="exportUsuarioExcel"
                class="px-4 py-2"
            >
                <font-awesome-icon icon="fa-solid fa-file-export" style="color: #ffffff;" class="mr-2"/>
                Exportar a Excel
            </fwb-Button>
        </div>
    </section>
</template>

<script setup>
    import * as XLSX from "xlsx";
    import { saveAs } from "file-saver";
    import UserService from "@/services/UserService";
    import { ref, onMounted} from 'vue';
    import { FwbButton } from "flowbite-vue";

    /**
     * Componente WorksheetUser.
     * 
     * Permite exportar el listado de usuarios a un archivo Excel (.xlsx).
     * Utiliza 'xlsx' para la generación del archivo y 'file-saver' para la descarga.
     */

    const usuario = ref([]);
    // Carga la lista de usuarios al montar el componente.
    const loadUsuario = async () => {
        try {
            const data = await UserService.getUsers();
            usuario.value = Array.isArray(data) ? data : data.data || [];
        } catch(err) {
            console.error("Error al cargar usuarios:", err);
        }
    };
    /**
     * Genera y descarga el reporte de usuarios en Excel.
     * Mapea los campos a encabezados legibles en español.
     */
    const exportUsuarioExcel = () => {
        if (!usuario.value.length) {
            alert("No hay datos en usuarios");
            return;
        }
        const exportData = usuario.value.map(user => ({
            ID: user.id,
            "Nombre del usuario": user.nombre_usuario,
            "Primer nombre": user.nombre,
            "segundo nombre": user.nombre2,
            "Primer apellido": user.apellido1,
            "Segundo apellido": user.apellido2,
            Correo: user.email,
            Telefono: user.telefono,
            Departamento: user.departamento,
            Estado: user.estado
        }));
        const worksheet = XLSX.utils.json_to_sheet(exportData);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "usuario");
        const excelBuffer = XLSX.write(workbook, { bookType: "xlsx", type: "array"});
        const blob = new Blob([excelBuffer], {type: "application/octet-stream"});
        saveAs(blob, "reporte_usuarios.xlsx");
    };
    onMounted(() => {
        loadUsuario();
    });
</script>

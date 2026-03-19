<template>
    <section>
        <div class="flex justify-end mb-4 font-bold">
            <fwb-Button
                gradient="green"
                @click="exportMantenimientoExcel"
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
    import MaintenaceService from "@/services/MaintenanceService";
    import { ref, onMounted} from 'vue';
    import { FwbButton } from "flowbite-vue";

    /**
     * Componente WorksheetMant.
     * 
     * Permite exportar el registro de mantenimientos a un archivo Excel (.xlsx).
     * Utiliza 'xlsx' para la generación del archivo y 'file-saver' para la descarga.
     */

    const mantenimiento = ref([]);
    // Carga la lista de mantenimientos al montar el componente.
    const loadMantenimiento = async () => {
        try {
            const data = await MaintenaceService.getMaintenances();
            mantenimiento.value = Array.isArray(data) ? data : data.data || [];
        } catch(err) {
            console.error("Error al cargar mantenimientos:", err);
        }
    };
    /**
     * Genera y descarga el reporte de mantenimientos en Excel.
     * Mapea los campos a encabezados legibles en español.
     */
    const exportMantenimientoExcel = () => {
        if (!mantenimiento.value.length) {
            alert("No hay datos en mantenimientos");
            return;
        }
        const exportData = mantenimiento.value.map(maintenance => ({
            ID: maintenance.id,
            "ID del item": maintenance.inventario_item_id,
            "Fecha de inicio": maintenance.fecha_inicio,
            "Fecha de fin prevista": maintenance.fecha_fin_prevista,
            "Fecha de fin real": maintenance.fecha_fin_real,
            "Tipo de mantenimiento": maintenance.tipo_mantenimiento,
            "Descripcion del problema": maintenance.descripcion_problema,
            "Descripcion del trabajo realizado": maintenance.descripcion_trabajo_realizado,
            "Costo estimado": maintenance.costo_estimado,
            "Costo real": maintenance.costo_real,
            "Estado mantenimiento": maintenance.estado_mantenimiento,
            Responsable: maintenance.responsable,
        }));
        const worksheet = XLSX.utils.json_to_sheet(exportData);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "mantenimiento");
        const excelBuffer = XLSX.write(workbook, { bookType: "xlsx", type: "array"});
        const blob = new Blob([excelBuffer], {type: "application/octet-stream"});
        saveAs(blob, "reporte_mantenimientos.xlsx");
    };
    onMounted(() => {
        loadMantenimiento();
    });
</script>
<template>
    <section>
        <div class="flex justify-end mb-4 font-bold">
            <fwb-Button
                gradient="green"
                @click="exportInventarioExcel"
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
    import InventoryService from "@/services/InventoryService";
    import { ref, onMounted} from 'vue';
    import { FwbButton } from 'flowbite-vue'

    /**
     * Componente ExportWorksheet.
     * 
     * Permite exportar el inventario completo a un archivo Excel (.xlsx).
     * Utiliza las librerías 'xlsx' para generar el archivo y 'file-saver' para descargarlo.
     */

    const inventario = ref([]);

    // Carga los datos del inventario al montar el componente.
    const loadinventario = async () => {
        try {
            const data = await InventoryService.getProducts();
            inventario.value = Array.isArray(data) ? data : data.data || [];
        } catch(err) {
            console.error("Error al cargar inventarios:", err);
        }
    };
    /**
     * Genera y descarga el archivo Excel con los datos del inventario.
     * Mapea los campos a nombres de columnas legibles en español.
     */
    const exportInventarioExcel = () => {
        if (!inventario.value.length) {
            alert("No hay datos en inventarios");
            return;
        }
        const exportData = inventario.value.map(product => ({
            ID: product.id,
            Nombre: product.nombre,
            Descripcion: product.descripcion,
            Marca: product.marca,
            Modelo: product.modelo,
            "Precio de referencia de la renta": product.precio_referencia_renta,
            "Precio de alquiler diario": product.precio_alquiler_dia,
            "Precio de alquiler semanal": product.precio_alquiler_semanal,
            "Precio de alquiler mensual": product.precio_alquiler_mensual,
            "Precio de compra": product.precio_compra,
            "valor actual": product.valor_actual,
            "Fecha de compra": product.fecha_compra,
            Condicion: product.condicion,
            Ubicacion: product.ubicacion,
            Notas: product.notas,
            SKU: product.sku,
            "Numero de serie": product.numero_serie,
            Categoria: product.categoria,
            Especificaciones: product.especificaciones,
            Estado: product.estado
        }));
        const worksheet = XLSX.utils.json_to_sheet(exportData);
        const workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "inventario");
        const excelBuffer = XLSX.write(workbook, { bookType: "xlsx", type: "array"});
        const blob = new Blob([excelBuffer], {type: "application/octet-stream"});
        saveAs(blob, "reporte_inventarios.xlsx");
    };
    onMounted(() => {
        loadinventario();
    });
</script>




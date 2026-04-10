import apiClient from "@/plugins/axios";

/**
 * Servicio para obtener el reporte completo de Usuarios y Seguridad.
 */

export const getReporteUsuario = async () => {
    try {
        const response = await apiClient.get('/reportes/reporteCompleto');
        return response.data;
    } catch(error) {
        console.error("Error al obtener el reporte de usuarios: ", error);
        throw error;
    }
}

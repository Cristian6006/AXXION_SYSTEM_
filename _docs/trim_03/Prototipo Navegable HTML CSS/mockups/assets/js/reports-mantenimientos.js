// Función general para controlar la visibilidad de los modales
function toggleModal(modalId, show) {
    document.getElementById(modalId).classList.toggle('hidden', !show);
}

// CONTROLADOR DE MODALES DE DESCARGA (REUTILIZADO)
const downloadPdfBtn = document.getElementById('download-pdf-btn');
const downloadExcelBtn = document.getElementById('download-excel-btn');
const downloadWordBtn = document.getElementById('download-word-btn');
const downloadReportModal = document.getElementById('download-report-modal');
const closeDownloadModalBtn = document.getElementById('close-download-modal-btn');
const downloadReportAction = document.getElementById('download-report-action');
const cancelDownloadReportAction = document.getElementById('cancel-download-report-action');
const confirmDownloadModal = document.getElementById('confirm-download-modal');
const confirmDownloadActionFinal = document.getElementById('confirm-download-action-final');
const cancelDownloadActionFinal = document.getElementById('cancel-download-action-final');

// Abre el modal de descarga al hacer clic en cualquier botón de descarga
[downloadPdfBtn, downloadExcelBtn, downloadWordBtn].forEach(btn => {
    btn.addEventListener('click', () => toggleModal('download-report-modal', true));
});
closeDownloadModalBtn.addEventListener('click', () => toggleModal('download-report-modal', false));
cancelDownloadReportAction.addEventListener('click', () => toggleModal('download-report-modal', false));

downloadReportAction.addEventListener('click', () => {
    toggleModal('download-report-modal', false);
    toggleModal('confirm-download-modal', true);
});
confirmDownloadActionFinal.addEventListener('click', () => {
    alert('Reporte descargado! (Simulación de la descarga)');
    toggleModal('confirm-download-modal', false);
});
cancelDownloadActionFinal.addEventListener('click', () => {
    toggleModal('confirm-download-modal', false);
    toggleModal('download-report-modal', true);
});

// CONTROLADOR DE MODAL DE EDICIÓN DE REPORTE MANTENIMIENTOS
const editMaintenanceReportBtn = document.getElementById('edit-maintenance-report-btn');
const editMaintenanceReportModal = document.getElementById('edit-maintenance-report-modal');
const closeEditMaintenanceModalBtn = document.getElementById('close-edit-maintenance-modal-btn');
const saveEditedMaintenanceReportBtn = document.getElementById('save-edited-maintenance-report-btn');
const cancelEditMaintenanceReportBtn = document.getElementById('cancel-edit-maintenance-report-btn');
const confirmEditMaintenanceReportModal = document.getElementById('confirm-edit-maintenance-report-modal');
const confirmEditMaintenanceActionFinal = document.getElementById('confirm-edit-maintenance-action-final');
const cancelEditMaintenanceActionFinal = document.getElementById('cancel-edit-maintenance-action-final');

editMaintenanceReportBtn.addEventListener('click', () => toggleModal('edit-maintenance-report-modal', true));
closeEditMaintenanceModalBtn.addEventListener('click', () => toggleModal('edit-maintenance-report-modal', false));
cancelEditMaintenanceReportBtn.addEventListener('click', () => toggleModal('edit-maintenance-report-modal', false));

saveEditedMaintenanceReportBtn.addEventListener('click', () => {
    toggleModal('edit-maintenance-report-modal', false);
    toggleModal('confirm-edit-maintenance-report-modal', true);
});
confirmEditMaintenanceActionFinal.addEventListener('click', () => {
    alert('Registro de mantenimiento modificado! (Simulación)');
    toggleModal('confirm-edit-maintenance-report-modal', false);
});
cancelEditMaintenanceActionFinal.addEventListener('click', () => {
    toggleModal('confirm-edit-maintenance-report-modal', false);
    toggleModal('edit-maintenance-report-modal', true);
});

// CONTROLADOR DE MODAL DE ELIMINAR REPORTE MANTENIMIENTOS
const deleteMaintenanceReportBtn = document.getElementById('delete-maintenance-report-btn');
const removeMaintenanceReportModal = document.getElementById('remove-maintenance-report-modal');
const confirmRemoveMaintenanceReportAction = document.getElementById('confirm-remove-maintenance-report-action');
const cancelRemoveMaintenanceReportAction = document.getElementById('cancel-remove-maintenance-report-action');

deleteMaintenanceReportBtn.addEventListener('click', () => toggleModal('remove-maintenance-report-modal', true));
confirmRemoveMaintenanceReportAction.addEventListener('click', () => {
    alert('Registro de mantenimiento eliminado! (Simulación)');
    toggleModal('remove-maintenance-report-modal', false);
});
cancelRemoveMaintenanceReportAction.addEventListener('click', () => toggleModal('remove-maintenance-report-modal', false));

// CONTROLADOR DE MODAL DE DAR DE BAJA REPORTE MANTENIMIENTOS
const deactivateMaintenanceReportBtn = document.getElementById('deactivate-maintenance-report-btn');
const deactivateMaintenanceReportModal = document.getElementById('deactivate-maintenance-report-modal');
const confirmDeactivateMaintenanceReportAction = document.getElementById('confirm-deactivate-maintenance-report-action');
const cancelDeactivateMaintenanceReportAction = document.getElementById('cancel-deactivate-maintenance-report-action');

deactivateMaintenanceReportBtn.addEventListener('click', () => toggleModal('deactivate-maintenance-report-modal', true));
confirmDeactivateMaintenanceReportAction.addEventListener('click', () => {
    alert('Registro de mantenimiento dado de baja! (Simulación)');
    toggleModal('deactivate-maintenance-report-modal', false);
});
cancelDeactivateMaintenanceReportAction.addEventListener('click', () => toggleModal('deactivate-maintenance-report-modal', false));

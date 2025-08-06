// Script extraído de reports-inventario.html
// Función general para controlar la visibilidad de los modales
function toggleModal(modalId, show) {
    document.getElementById(modalId).classList.toggle('hidden', !show);
}

// CONTROLADOR DE MODALES DE DESCARGA
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

// CONTROLADOR DE MODAL DE EDICIÓN DE REPORTE (para inventario)
const editReportBtn = document.getElementById('edit-report-btn');
const editReportModal = document.getElementById('edit-report-modal');
const closeEditModalBtn = document.getElementById('close-edit-modal-btn');
const saveEditedReportBtn = document.getElementById('save-edited-report-btn');
const cancelEditReportBtn = document.getElementById('cancel-edit-report-btn');
const confirmEditReportModal = document.getElementById('confirm-edit-report-modal');
const confirmEditReportActionFinal = document.getElementById('confirm-edit-report-action-final');
const cancelEditReportActionFinal = document.getElementById('cancel-edit-report-action-final');

editReportBtn.addEventListener('click', () => toggleModal('edit-report-modal', true));
closeEditModalBtn.addEventListener('click', () => toggleModal('edit-report-modal', false));
cancelEditReportBtn.addEventListener('click', () => toggleModal('edit-report-modal', false));

saveEditedReportBtn.addEventListener('click', () => {
    toggleModal('edit-report-modal', false);
    toggleModal('confirm-edit-report-modal', true);
});
confirmEditReportActionFinal.addEventListener('click', () => {
    alert('Reporte (datos) modificados! (Simulación)');
    toggleModal('confirm-edit-report-modal', false);
});
cancelEditReportActionFinal.addEventListener('click', () => {
    toggleModal('confirm-edit-report-modal', false);
    toggleModal('edit-report-modal', true);
});

// CONTROLADOR DE MODAL DE ELIMINAR REPORTE (de inventario)
const deleteReportBtn = document.getElementById('delete-report-btn');
const removeReportModal = document.getElementById('remove-report-modal');
const confirmRemoveReportAction = document.getElementById('confirm-remove-report-action');
const cancelRemoveReportAction = document.getElementById('cancel-remove-report-action');

deleteReportBtn.addEventListener('click', () => toggleModal('remove-report-modal', true));
confirmRemoveReportAction.addEventListener('click', () => {
    alert('Reporte eliminado! (Simulación de la eliminación de datos)');
    toggleModal('remove-report-modal', false);
});
cancelRemoveReportAction.addEventListener('click', () => toggleModal('remove-report-modal', false));

// CONTROLADOR DE MODAL DE DAR DE BAJA REPORTE (de inventario)
const deactivateReportBtn = document.getElementById('deactivate-report-btn');
const deactivateReportModal = document.getElementById('deactivate-report-modal');
const confirmDeactivateReportAction = document.getElementById('confirm-deactivate-report-action');
const cancelDeactivateReportAction = document.getElementById('cancel-deactivate-report-action');

deactivateReportBtn.addEventListener('click', () => toggleModal('deactivate-report-modal', true));
confirmDeactivateReportAction.addEventListener('click', () => {
    alert('Reporte dado de baja! (Simulación de baja de datos)');
    toggleModal('deactivate-report-modal', false);
});
cancelDeactivateReportAction.addEventListener('click', () => toggleModal('deactivate-report-modal', false));

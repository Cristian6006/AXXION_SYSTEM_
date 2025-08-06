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

// CONTROLADOR DE MODAL DE EDICIÓN DE REPORTE PROVEEDORES
const editProviderReportBtn = document.getElementById('edit-provider-report-btn');
const editProviderReportModal = document.getElementById('edit-provider-report-modal');
const closeEditProviderModalBtn = document.getElementById('close-edit-provider-modal-btn');
const saveEditedProviderReportBtn = document.getElementById('save-edited-provider-report-btn');
const cancelEditProviderReportBtn = document.getElementById('cancel-edit-provider-report-btn');
const confirmEditProviderReportModal = document.getElementById('confirm-edit-provider-report-modal');
const confirmEditProviderActionFinal = document.getElementById('confirm-edit-provider-action-final');
const cancelEditProviderActionFinal = document.getElementById('cancel-edit-provider-action-final');

editProviderReportBtn.addEventListener('click', () => toggleModal('edit-provider-report-modal', true));
closeEditProviderModalBtn.addEventListener('click', () => toggleModal('edit-provider-report-modal', false));
cancelEditProviderReportBtn.addEventListener('click', () => toggleModal('edit-provider-report-modal', false));

saveEditedProviderReportBtn.addEventListener('click', () => {
    toggleModal('edit-provider-report-modal', false);
    toggleModal('confirm-edit-provider-report-modal', true);
});
confirmEditProviderActionFinal.addEventListener('click', () => {
    alert('Datos del proveedor modificados! (Simulación)');
    toggleModal('confirm-edit-provider-report-modal', false);
});
cancelEditProviderActionFinal.addEventListener('click', () => {
    toggleModal('confirm-edit-provider-report-modal', false);
    toggleModal('edit-provider-report-modal', true);
});

// CONTROLADOR DE MODAL DE ELIMINAR REPORTE PROVEEDORES
const deleteProviderReportBtn = document.getElementById('delete-provider-report-btn');
const removeProviderReportModal = document.getElementById('remove-provider-report-modal'); // Primer modal para input de nombre
const closeRemoveProviderModalBtn = document.getElementById('close-remove-provider-modal-btn');
const performRemoveProviderBtn = document.getElementById('perform-remove-provider-btn'); // Botón Aceptar en primer modal
const cancelRemoveProviderBtn = document.getElementById('cancel-remove-provider-btn');
const confirmRemoveProviderModal = document.getElementById('confirm-remove-provider-modal'); // Segundo modal de confirmación
const confirmRemoveProviderActionFinal = document.getElementById('confirm-remove-provider-action-final'); // Botón Aceptar en segundo modal
const cancelRemoveProviderActionFinal = document.getElementById('cancel-remove-provider-action-final'); // Botón Cancelar en segundo modal

deleteProviderReportBtn.addEventListener('click', () => toggleModal('remove-provider-report-modal', true));
closeRemoveProviderModalBtn.addEventListener('click', () => toggleModal('remove-provider-report-modal', false));
cancelRemoveProviderBtn.addEventListener('click', () => toggleModal('remove-provider-report-modal', false));

performRemoveProviderBtn.addEventListener('click', () => {
    toggleModal('remove-provider-report-modal', false); // Cierra el primer modal
    toggleModal('confirm-remove-provider-modal', true); // Abre el modal de confirmación
});
confirmRemoveProviderActionFinal.addEventListener('click', () => {
    alert('Proveedor eliminado! (Simulación)');
    toggleModal('confirm-remove-provider-modal', false);
});
cancelRemoveProviderActionFinal.addEventListener('click', () => {
    toggleModal('confirm-remove-provider-modal', false);
    toggleModal('remove-provider-report-modal', true); // Vuelve al modal de nombre si se cancela la confirmación final
});

// CONTROLADOR DE MODAL DE DAR DE BAJA REPORTE PROVEEDORES
const deactivateProviderReportBtn = document.getElementById('deactivate-provider-report-btn');
const deactivateProviderReportModal = document.getElementById('deactivate-provider-report-modal'); // Primer modal para input de nombre
const closeDeactivateProviderModalBtn = document.getElementById('close-deactivate-provider-modal-btn');
const performDeactivateProviderBtn = document.getElementById('perform-deactivate-provider-btn'); // Botón Aceptar en primer modal
const cancelDeactivateProviderBtn = document.getElementById('cancel-deactivate-provider-btn');
const confirmDeactivateProviderModal = document.getElementById('confirm-deactivate-provider-modal'); // Segundo modal de confirmación
const confirmDeactivateProviderActionFinal = document.getElementById('confirm-deactivate-provider-action-final'); // Botón Aceptar en segundo modal
const cancelDeactivateProviderActionFinal = document.getElementById('cancel-deactivate-provider-action-final'); // Botón Cancelar en segundo modal

deactivateProviderReportBtn.addEventListener('click', () => toggleModal('deactivate-provider-report-modal', true));
closeDeactivateProviderModalBtn.addEventListener('click', () => toggleModal('deactivate-provider-report-modal', false));
cancelDeactivateProviderBtn.addEventListener('click', () => toggleModal('deactivate-provider-report-modal', false));

performDeactivateProviderBtn.addEventListener('click', () => {
    toggleModal('deactivate-provider-report-modal', false);
    toggleModal('confirm-deactivate-provider-modal', true);
});
confirmDeactivateProviderActionFinal.addEventListener('click', () => {
    alert('Proveedor dado de baja! (Simulación)');
    toggleModal('confirm-deactivate-provider-modal', false);
});
cancelDeactivateProviderActionFinal.addEventListener('click', () => {
    toggleModal('confirm-deactivate-provider-modal', false);
    toggleModal('deactivate-provider-report-modal', true);
});

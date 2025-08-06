// Script para reports-empleados.html
function toggleModal(modalId, show) {
    document.getElementById(modalId).classList.toggle('hidden', !show);
}

// Referencia a la tabla de empleados
const employeesTableBody = document.getElementById('employees-table-body');

// Variables para el formulario de edición
const editEmployeeNameSelect = document.getElementById('edit-employee-name');
const editEmployeeIdentificationInput = document.getElementById('edit-employee-identification');
const editEmployeeHiredateInput = document.getElementById('edit-employee-hiredate');
const editEmployeeRoleSelect = document.getElementById('edit-employee-role');
const editEmployeePhoneInput = document.getElementById('edit-employee-phone');
let tempEditEmployee = null;

// Utilidad para obtener el siguiente número de empleado
function getNextEmployeeNumber() {
    return employeesTableBody.querySelectorAll('tr').length + 1;
}

// Función para actualizar el selector de nombres en el modal de edición
function updateEmployeeNameSelector() {
    // Limpiar opciones
    editEmployeeNameSelect.innerHTML = '<option value="">Seleccione nombre</option>';
    // Agregar opciones dinámicamente
    Array.from(employeesTableBody.querySelectorAll('tr')).forEach(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length > 0) {
            const nombre = tds[0].textContent.trim();
            const opt = document.createElement('option');
            opt.value = nombre.toLowerCase().replace(/ /g, '-');
            opt.textContent = nombre;
            editEmployeeNameSelect.appendChild(opt);
        }
    });
}

// AGREGAR EMPLEADO
// (En este contexto, se agregan empleados solo desde JS, pero puedes adaptar para un formulario si lo deseas)
window.addEmployee = function({nombre, identificacion, fechaIngreso, cargo, telefono, fechas}) {
    const tr = document.createElement('tr');
    tr.className = 'hover:bg-gray-50';
    tr.innerHTML = `
        <td class="py-3 px-4 whitespace-nowrap">${nombre}</td>
        <td class="py-3 px-4 whitespace-nowrap">${identificacion}</td>
        <td class="py-3 px-4 whitespace-nowrap">${fechaIngreso}</td>
        <td class="py-3 px-4 whitespace-nowrap">${cargo}</td>
        <td class="py-3 px-4 whitespace-nowrap">${telefono}</td>
        <td class="py-3 px-4 whitespace-nowrap">${fechas}</td>
    `;
    employeesTableBody.appendChild(tr);
    updateEmployeeNameSelector();
};

// EDITAR EMPLEADO
const editEmployeeReportBtn = document.getElementById('edit-employee-report-btn');
const editEmployeeReportModal = document.getElementById('edit-employee-report-modal');
const closeEditEmployeeModalBtn = document.getElementById('close-edit-employee-modal-btn');
const saveEditedEmployeeReportBtn = document.getElementById('save-edited-employee-report-btn');
const cancelEditEmployeeReportBtn = document.getElementById('cancel-edit-employee-report-btn');
const confirmEditEmployeeReportModal = document.getElementById('confirm-edit-employee-report-modal');
const confirmEditEmployeeActionFinal = document.getElementById('confirm-edit-employee-action-final');
const cancelEditEmployeeActionFinal = document.getElementById('cancel-edit-employee-action-final');

editEmployeeReportBtn.addEventListener('click', () => {
    updateEmployeeNameSelector();
    toggleModal('edit-employee-report-modal', true);
});
closeEditEmployeeModalBtn.addEventListener('click', () => toggleModal('edit-employee-report-modal', false));
cancelEditEmployeeReportBtn.addEventListener('click', () => toggleModal('edit-employee-report-modal', false));

editEmployeeNameSelect.addEventListener('change', () => {
    const nombre = editEmployeeNameSelect.options[editEmployeeNameSelect.selectedIndex].text;
    // Buscar la fila correspondiente
    const fila = Array.from(employeesTableBody.querySelectorAll('tr')).find(tr => tr.querySelector('td') && tr.querySelector('td').textContent.trim() === nombre);
    if (fila) {
        const tds = fila.querySelectorAll('td');
        editEmployeeIdentificationInput.value = tds[1].textContent.trim();
        editEmployeeHiredateInput.value = tds[2].textContent.trim().split('/').reverse().join('-');
        editEmployeeRoleSelect.value = tds[3].textContent.trim().toLowerCase();
        editEmployeePhoneInput.value = tds[4].textContent.trim();
        window.currentEditRow = fila;
    } else {
        editEmployeeIdentificationInput.value = '';
        editEmployeeHiredateInput.value = '';
        editEmployeeRoleSelect.value = '';
        editEmployeePhoneInput.value = '';
        window.currentEditRow = null;
    }
});

saveEditedEmployeeReportBtn.addEventListener('click', () => {
    // Validar campos
    const nombre = editEmployeeNameSelect.options[editEmployeeNameSelect.selectedIndex].text;
    const identificacion = editEmployeeIdentificationInput.value.trim();
    const fechaIngreso = editEmployeeHiredateInput.value.trim();
    const cargo = editEmployeeRoleSelect.value.trim();
    const telefono = editEmployeePhoneInput.value.trim();
    if (!nombre || !identificacion || !fechaIngreso || !cargo || !telefono) {
        alert('Por favor, complete todos los campos.');
        return;
    }
    tempEditEmployee = { nombre, identificacion, fechaIngreso, cargo, telefono };
    toggleModal('edit-employee-report-modal', false);
    toggleModal('confirm-edit-employee-report-modal', true);
});

confirmEditEmployeeActionFinal.addEventListener('click', () => {
    if (!tempEditEmployee || !window.currentEditRow) {
        toggleModal('confirm-edit-employee-report-modal', false);
        return;
    }
    const tds = window.currentEditRow.querySelectorAll('td');
    tds[0].textContent = tempEditEmployee.nombre;
    tds[1].textContent = tempEditEmployee.identificacion;
    tds[2].textContent = tempEditEmployee.fechaIngreso.split('-').reverse().join('/');
    tds[3].textContent = tempEditEmployee.cargo.charAt(0).toUpperCase() + tempEditEmployee.cargo.slice(1);
    tds[4].textContent = tempEditEmployee.telefono;
    tempEditEmployee = null;
    window.currentEditRow = null;
    updateEmployeeNameSelector();
    toggleModal('confirm-edit-employee-report-modal', false);
    alert('¡Empleado editado exitosamente!');
});
cancelEditEmployeeActionFinal.addEventListener('click', () => {
    toggleModal('confirm-edit-employee-report-modal', false);
    toggleModal('edit-employee-report-modal', true);
});

// ELIMINAR EMPLEADO
const deleteEmployeeReportBtn = document.getElementById('delete-employee-report-btn');
const removeEmployeeReportModal = document.getElementById('remove-employee-report-modal');
const confirmRemoveEmployeeReportAction = document.getElementById('confirm-remove-employee-report-action');
const cancelRemoveEmployeeReportAction = document.getElementById('cancel-remove-employee-report-action');
let tempDeleteEmployee = null;

deleteEmployeeReportBtn.addEventListener('click', () => {
    updateEmployeeNameSelector();
    toggleModal('remove-employee-report-modal', true);
});
confirmRemoveEmployeeReportAction.addEventListener('click', () => {
    // Eliminar el empleado seleccionado en el selector de edición
    const nombre = editEmployeeNameSelect.options[editEmployeeNameSelect.selectedIndex].text;
    if (!nombre || nombre === 'Seleccione nombre') {
        alert('Seleccione un empleado para eliminar.');
        return;
    }
    // Buscar la fila correspondiente
    const fila = Array.from(employeesTableBody.querySelectorAll('tr')).find(tr => tr.querySelector('td') && tr.querySelector('td').textContent.trim() === nombre);
    if (fila) {
        fila.remove();
        updateEmployeeNameSelector();
        alert('¡Empleado eliminado exitosamente!');
    } else {
        alert('No se encontró el empleado.');
    }
    toggleModal('remove-employee-report-modal', false);
});
cancelRemoveEmployeeReportAction.addEventListener('click', () => {
    toggleModal('remove-employee-report-modal', false);
});

// DAR DE BAJA EMPLEADO
const deactivateEmployeeReportBtn = document.getElementById('deactivate-employee-report-btn');
const deactivateEmployeeReportModal = document.getElementById('deactivate-employee-report-modal');
const confirmDeactivateEmployeeReportAction = document.getElementById('confirm-deactivate-employee-report-action');
const cancelDeactivateEmployeeReportAction = document.getElementById('cancel-deactivate-employee-report-action');
let tempDeactivateEmployee = null;

deactivateEmployeeReportBtn.addEventListener('click', () => {
    updateEmployeeNameSelector();
    toggleModal('deactivate-employee-report-modal', true);
});
confirmDeactivateEmployeeReportAction.addEventListener('click', () => {
    // Dar de baja el empleado seleccionado en el selector de edición
    const nombre = editEmployeeNameSelect.options[editEmployeeNameSelect.selectedIndex].text;
    if (!nombre || nombre === 'Seleccione nombre') {
        alert('Seleccione un empleado para dar de baja.');
        return;
    }
    // Buscar la fila correspondiente
    const fila = Array.from(employeesTableBody.querySelectorAll('tr')).find(tr => tr.querySelector('td') && tr.querySelector('td').textContent.trim() === nombre);
    if (fila) {
        fila.style.opacity = '0.5';
        fila.style.textDecoration = 'line-through';
        alert('¡Empleado dado de baja exitosamente!');
    } else {
        alert('No se encontró el empleado.');
    }
    toggleModal('deactivate-employee-report-modal', false);
});
cancelDeactivateEmployeeReportAction.addEventListener('click', () => {
    toggleModal('deactivate-employee-report-modal', false);
});

// --- AGREGAR EMPLEADO ---
const addEmployeeReportBtn = document.getElementById('add-employee-report-btn');
const addEmployeeReportModal = document.getElementById('add-employee-report-modal');
const closeAddEmployeeModalBtn = document.getElementById('close-add-employee-modal-btn');
const saveAddEmployeeBtn = document.getElementById('save-add-employee-btn');
const cancelAddEmployeeBtn = document.getElementById('cancel-add-employee-btn');
const confirmAddEmployeeModal = document.getElementById('confirm-add-employee-modal');
const confirmAddEmployeeAction = document.getElementById('confirm-add-employee-action');
const cancelAddEmployeeActionFinal = document.getElementById('cancel-add-employee-action-final');

const addEmployeeNameInput = document.getElementById('add-employee-name');
const addEmployeeIdentificationInput = document.getElementById('add-employee-identification');
const addEmployeeHiredateInput = document.getElementById('add-employee-hiredate');
const addEmployeeRoleSelect = document.getElementById('add-employee-role');
const addEmployeePhoneInput = document.getElementById('add-employee-phone');
const addEmployeeFechasInput = document.getElementById('add-employee-fechas');
let tempNewEmployee = null;

addEmployeeReportBtn.addEventListener('click', () => {
    // Limpiar formulario
    addEmployeeNameInput.value = '';
    addEmployeeIdentificationInput.value = '';
    addEmployeeHiredateInput.value = '';
    addEmployeeRoleSelect.value = '';
    addEmployeePhoneInput.value = '';
    addEmployeeFechasInput.value = '';
    toggleModal('add-employee-report-modal', true);
});
closeAddEmployeeModalBtn.addEventListener('click', () => toggleModal('add-employee-report-modal', false));
cancelAddEmployeeBtn.addEventListener('click', () => toggleModal('add-employee-report-modal', false));

saveAddEmployeeBtn.addEventListener('click', () => {
    // Validar campos
    const nombre = addEmployeeNameInput.value.trim();
    const identificacion = addEmployeeIdentificationInput.value.trim();
    const fechaIngreso = addEmployeeHiredateInput.value.trim();
    const cargo = addEmployeeRoleSelect.value.trim();
    const telefono = addEmployeePhoneInput.value.trim();
    const fechas = addEmployeeFechasInput.value.trim();
    if (!nombre || !identificacion || !fechaIngreso || !cargo || !telefono || !fechas) {
        alert('Por favor, complete todos los campos.');
        return;
    }
    tempNewEmployee = { nombre, identificacion, fechaIngreso, cargo, telefono, fechas };
    toggleModal('add-employee-report-modal', false);
    toggleModal('confirm-add-employee-modal', true);
});

confirmAddEmployeeAction.addEventListener('click', () => {
    if (!tempNewEmployee) {
        toggleModal('confirm-add-employee-modal', false);
        return;
    }
    // Formatear fecha de ingreso a dd/mm/yyyy
    const fechaIngreso = tempNewEmployee.fechaIngreso.split('-').reverse().join('/');
    window.addEmployee({
        nombre: tempNewEmployee.nombre,
        identificacion: tempNewEmployee.identificacion,
        fechaIngreso: fechaIngreso,
        cargo: tempNewEmployee.cargo.charAt(0).toUpperCase() + tempNewEmployee.cargo.slice(1),
        telefono: tempNewEmployee.telefono,
        fechas: tempNewEmployee.fechas
    });
    tempNewEmployee = null;
    toggleModal('confirm-add-employee-modal', false);
    alert('¡Empleado agregado exitosamente!');
});

cancelAddEmployeeActionFinal.addEventListener('click', () => {
    tempNewEmployee = null;
    toggleModal('confirm-add-employee-modal', false);
    toggleModal('add-employee-report-modal', true);
});

// Inicialización: limpiar y preparar selectores
updateEmployeeNameSelector();

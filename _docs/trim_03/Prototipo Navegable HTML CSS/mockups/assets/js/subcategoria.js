// Script extraído de subcategoria.html
function toggleModal(modalId, show) {
    document.getElementById(modalId).classList.toggle('hidden', !show);
}

// AGREGAR SUBCATEGORÍA
const addSubcategoryBtn = document.getElementById('add-subcategory-btn');
const addSubcategoryModal = document.getElementById('add-subcategory-modal');
const closeAddModalBtn = document.getElementById('close-add-modal-btn');
const saveSubcategoryBtn = document.getElementById('save-subcategory-btn');
const cancelAddSubcategoryBtn = document.getElementById('cancel-add-subcategory-btn');
const addSubcategoryConfirmModal = document.getElementById('add-subcategory-confirm-modal');
const confirmAddSubcategoryAction = document.getElementById('confirm-add-subcategory-action');
const cancelAddSubcategoryAction = document.getElementById('cancel-add-subcategory-action');

addSubcategoryBtn.addEventListener('click', () => toggleModal('add-subcategory-modal', true));
closeAddModalBtn.addEventListener('click', () => toggleModal('add-subcategory-modal', false));
cancelAddSubcategoryBtn.addEventListener('click', () => toggleModal('add-subcategory-modal', false));

// --- INICIO LÓGICA AGREGAR SUBCATEGORÍA REAL ---
// Referencias a los campos del formulario de agregar
const categoryParentSelect = document.getElementById('category-parent-select');
const subcategoryNameInput = document.getElementById('subcategory-name');
// Referencia a la tabla de subcategorías
const subcategoriesTableBody = document.querySelector('table tbody');
// Variable para llevar el conteo de subcategorías (basado en las filas actuales)
function getNextSubcategoryNumber() {
    // Cuenta solo las filas visibles (por si hay filtros en el futuro)
    return subcategoriesTableBody.querySelectorAll('tr').length + 1;
}
// Guardar datos temporalmente antes de confirmar
let tempNewSubcategory = null;

saveSubcategoryBtn.addEventListener('click', () => {
    // Validar campos
    const categoria = categoryParentSelect.value;
    const subcategoria = subcategoryNameInput.value.trim();
    if (!categoria || !subcategoria) {
        alert('Por favor, complete todos los campos.');
        return;
    }
    // Guardar datos temporalmente
    tempNewSubcategory = { categoria, subcategoria };
    toggleModal('add-subcategory-modal', false);
    toggleModal('add-subcategory-confirm-modal', true);
});

confirmAddSubcategoryAction.addEventListener('click', () => {
    if (!tempNewSubcategory) {
        toggleModal('add-subcategory-confirm-modal', false);
        return;
    }
    // Crear nueva fila
    const tr = document.createElement('tr');
    tr.className = 'hover:bg-gray-50';
    const num = getNextSubcategoryNumber();
    tr.innerHTML = `
        <td class="py-3 px-4">${num}</td>
        <td class="py-3 px-4">${tempNewSubcategory.categoria.charAt(0).toUpperCase() + tempNewSubcategory.categoria.slice(1)}</td>
        <td class="py-3 px-4 max-w-lg">${tempNewSubcategory.subcategoria}</td>
        <td class="py-3 px-4 flex justify-center items-center">
            <button class="edit-subcategory-btn text-gray-600 hover:text-green-600" disabled title="Solo mockup">
                <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.928a2 2 0 010 2.828L14.828 15H12v-2.828l6.707-6.707a2 2 0 012.828 0z"></path></svg>
            </button>
        </td>
    `;
    subcategoriesTableBody.appendChild(tr);
    // Limpiar formulario
    categoryParentSelect.value = '';
    subcategoryNameInput.value = '';
    tempNewSubcategory = null;
    toggleModal('add-subcategory-confirm-modal', false);
    // Mensaje de éxito
    alert('¡Subcategoría agregada exitosamente!');
});
// --- FIN LÓGICA AGREGAR SUBCATEGORÍA REAL ---

// ELIMINAR SUBCATEGORÍA
const deleteSubcategoryBtn = document.getElementById('delete-subcategory-btn');
const deleteSubcategoryModal = document.getElementById('delete-subcategory-modal');
const closeDeleteModalBtn = document.getElementById('close-delete-modal-btn');
const performDeleteBtn = document.getElementById('perform-delete-btn');
const cancelDeleteSubcategoryBtn = document.getElementById('cancel-delete-subcategory-btn');
const deleteSubcategoryConfirmModal = document.getElementById('delete-subcategory-confirm-modal');
const confirmDeleteSubcategoryAction = document.getElementById('confirm-delete-subcategory-action');
const cancelDeleteSubcategoryActionFinal = document.getElementById('cancel-delete-subcategory-action-final');

deleteSubcategoryBtn.addEventListener('click', () => toggleModal('delete-subcategory-modal', true));
closeDeleteModalBtn.addEventListener('click', () => toggleModal('delete-subcategory-modal', false));
cancelDeleteSubcategoryBtn.addEventListener('click', () => toggleModal('delete-subcategory-modal', false));

// --- INICIO LÓGICA ELIMINAR SUBCATEGORÍA REAL ---
const selectDeleteCategoryParent = document.getElementById('select-delete-category-parent');
const selectDeleteSubcategory = document.getElementById('select-delete-subcategory');

let tempDeleteSubcategory = null;

performDeleteBtn.addEventListener('click', () => {
    const categoria = selectDeleteCategoryParent.value;
    const subcategoria = selectDeleteSubcategory.value.trim() || selectDeleteSubcategory.value || selectDeleteSubcategory.placeholder;
    const subcatInput = selectDeleteSubcategory.value.trim();
    if (!categoria || !subcatInput) {
        alert('Por favor, complete todos los campos.');
        return;
    }
    // Buscar la fila correspondiente (insensible a mayúsculas/minúsculas)
    const filas = Array.from(subcategoriesTableBody.querySelectorAll('tr'));
    const filaEncontrada = filas.find(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length < 3) return false;
        const cat = tds[1].textContent.trim().toLowerCase();
        const subcat = tds[2].textContent.trim().toLowerCase();
        return cat === categoria.toLowerCase() && subcat === subcatInput.toLowerCase();
    });
    if (!filaEncontrada) {
        alert('No se encontró la subcategoría con esos datos.');
        return;
    }
    tempDeleteSubcategory = filaEncontrada;
    toggleModal('delete-subcategory-modal', false);
    toggleModal('delete-subcategory-confirm-modal', true);
});

confirmDeleteSubcategoryAction.addEventListener('click', () => {
    if (!tempDeleteSubcategory) {
        toggleModal('delete-subcategory-confirm-modal', false);
        return;
    }
    // Eliminar la fila
    tempDeleteSubcategory.remove();
    tempDeleteSubcategory = null;
    // Reenumerar las filas
    Array.from(subcategoriesTableBody.querySelectorAll('tr')).forEach((tr, idx) => {
        const tdNum = tr.querySelector('td');
        if (tdNum) tdNum.textContent = idx + 1;
    });
    // Limpiar formulario
    selectDeleteCategoryParent.value = '';
    selectDeleteSubcategory.value = '';
    toggleModal('delete-subcategory-confirm-modal', false);
    alert('¡Subcategoría eliminada exitosamente!');
});
// --- FIN LÓGICA ELIMINAR SUBCATEGORÍA REAL ---

// DAR DE BAJA SUBCATEGORÍA
const deactivateSubcategoryBtn = document.getElementById('deactivate-subcategory-btn');
const deactivateSubcategoryModal = document.getElementById('deactivate-subcategory-modal');
const closeDeactivateModalBtn = document.getElementById('close-deactivate-modal-btn');
const performDeactivateBtn = document.getElementById('perform-deactivate-btn');
const cancelDeactivateSubcategoryBtn = document.getElementById('cancel-deactivate-subcategory-btn');
const deactivateSubcategoryConfirmModal = document.getElementById('deactivate-subcategory-confirm-modal');
const confirmDeactivateSubcategoryAction = document.getElementById('confirm-deactivate-subcategory-action');
const cancelDeactivateSubcategoryActionFinal = document.getElementById('cancel-deactivate-subcategory-action-final');

deactivateSubcategoryBtn.addEventListener('click', () => toggleModal('deactivate-subcategory-modal', true));
closeDeactivateModalBtn.addEventListener('click', () => toggleModal('deactivate-subcategory-modal', false));
cancelDeactivateSubcategoryBtn.addEventListener('click', () => toggleModal('deactivate-subcategory-modal', false));

performDeactivateBtn.addEventListener('click', () => {
    toggleModal('deactivate-subcategory-modal', false);
    toggleModal('deactivate-subcategory-confirm-modal', true);
});
confirmDeactivateSubcategoryAction.addEventListener('click', () => {
    alert('Subcategoría dada de baja! (Simulación)');
    toggleModal('deactivate-subcategory-confirm-modal', false);
});
cancelDeactivateSubcategoryActionFinal.addEventListener('click', () => {
    toggleModal('deactivate-subcategory-confirm-modal', false);
    toggleModal('deactivate-subcategory-modal', true);
});

// MODIFICAR SUBCATEGORÍA
const editSubcategoryBtns = document.querySelectorAll('.edit-subcategory-btn');
const modifySubcategoryModal = document.getElementById('modify-subcategory-modal');
const closeModifyModalBtn = document.getElementById('close-modify-modal-btn');
const saveModifiedSubcategoryBtn = document.getElementById('save-modified-subcategory-btn');
const cancelModifySubcategoryBtn = document.getElementById('cancel-modify-subcategory-btn');
const modifySubcategoryConfirmModal = document.getElementById('modify-subcategory-confirm-modal');
const confirmModifySubcategoryAction = document.getElementById('confirm-modify-subcategory-action');
const cancelModifySubcategoryActionFinal = document.getElementById('cancel-modify-subcategory-action-final');

editSubcategoryBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        toggleModal('modify-subcategory-modal', true);
    });
});
closeModifyModalBtn.addEventListener('click', () => toggleModal('modify-subcategory-modal', false));
cancelModifySubcategoryBtn.addEventListener('click', () => toggleModal('modify-subcategory-modal', false));

saveModifiedSubcategoryBtn.addEventListener('click', () => {
    toggleModal('modify-subcategory-modal', false);
    toggleModal('modify-subcategory-confirm-modal', true);
});
confirmModifySubcategoryAction.addEventListener('click', () => {
    alert('Subcategoría modificada! (Simulación)');
    toggleModal('modify-subcategory-confirm-modal', false);
});
cancelModifySubcategoryActionFinal.addEventListener('click', () => {
    toggleModal('modify-subcategory-confirm-modal', false);
    toggleModal('modify-subcategory-modal', true);
});

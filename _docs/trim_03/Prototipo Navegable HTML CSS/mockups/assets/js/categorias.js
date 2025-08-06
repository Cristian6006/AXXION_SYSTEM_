// Script para categorias.html
function toggleModal(modalId, show) {
    document.getElementById(modalId).classList.toggle('hidden', !show);
}

// Referencia a la tabla de categorías
const categoriesTableBody = document.querySelector('table tbody');

// Función para actualizar los selectores de categorías
function updateCategorySelectors() {
    const selectDeleteCategory = document.getElementById('select-delete-category');
    const selectModifyCategory = document.getElementById('select-modify-category');
    
    // Obtener todas las categorías de la tabla
    const categorias = Array.from(categoriesTableBody.querySelectorAll('tr')).map(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length >= 2) {
            return tds[1].textContent.trim();
        }
        return null;
    }).filter(cat => cat !== null);
    
    // Actualizar selector de eliminar
    if (selectDeleteCategory) {
        const currentValue = selectDeleteCategory.value;
        selectDeleteCategory.innerHTML = '<option value="">Seleccione una categoría</option>';
        categorias.forEach(categoria => {
            const option = document.createElement('option');
            option.value = categoria.toLowerCase();
            option.textContent = categoria;
            selectDeleteCategory.appendChild(option);
        });
        selectDeleteCategory.value = currentValue;
    }
    
    // Actualizar selector de modificar
    if (selectModifyCategory) {
        const currentValue = selectModifyCategory.value;
        selectModifyCategory.innerHTML = '<option value="">Seleccione una categoría</option>';
        categorias.forEach(categoria => {
            const option = document.createElement('option');
            option.value = categoria.toLowerCase();
            option.textContent = categoria;
            selectModifyCategory.appendChild(option);
        });
        selectModifyCategory.value = currentValue;
    }
}

// --- INICIO LÓGICA AGREGAR CATEGORÍA REAL ---
const categoryNameInput = document.getElementById('category-name');
let tempNewCategory = null;

function getNextCategoryNumber() {
    return categoriesTableBody.querySelectorAll('tr').length + 1;
}

// AGREGAR CATEGORÍA
const addCategoryBtn = document.getElementById('add-category-btn');
const addCategoryModal = document.getElementById('add-category-modal');
const closeAddModalBtn = document.getElementById('close-add-modal-btn');
const saveCategoryBtn = document.getElementById('save-category-btn');
const cancelAddCategoryBtn = document.getElementById('cancel-add-category-btn');
const addCategoryConfirmModal = document.getElementById('add-category-confirm-modal');
const confirmAddCategoryAction = document.getElementById('confirm-add-category-action');
const cancelAddCategoryAction = document.getElementById('cancel-add-category-action');

addCategoryBtn.addEventListener('click', () => toggleModal('add-category-modal', true));
closeAddModalBtn.addEventListener('click', () => toggleModal('add-category-modal', false));
cancelAddCategoryBtn.addEventListener('click', () => toggleModal('add-category-modal', false));

saveCategoryBtn.addEventListener('click', () => {
    const categoria = categoryNameInput.value.trim();
    if (!categoria) {
        alert('Por favor, complete el campo de categoría.');
        return;
    }
    tempNewCategory = { categoria };
    toggleModal('add-category-modal', false);
    toggleModal('add-category-confirm-modal', true);
});

confirmAddCategoryAction.addEventListener('click', () => {
    if (!tempNewCategory) {
        toggleModal('add-category-confirm-modal', false);
        return;
    }
    // Crear nueva fila
    const tr = document.createElement('tr');
    tr.className = 'hover:bg-gray-50';
    const num = getNextCategoryNumber();
    tr.innerHTML = `
        <td class="py-3 px-4 whitespace-nowrap">${num}</td>
        <td class="py-3 px-4 whitespace-nowrap">${tempNewCategory.categoria.charAt(0).toUpperCase() + tempNewCategory.categoria.slice(1)}</td>
        <td class="py-3 px-4 flex justify-center items-center">
            <button class="edit-category-btn text-gray-600 hover:text-green-600">
                <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.928a2 2 0 010 2.828L14.828 15H12v-2.828l6.707-6.707a2 2 0 012.828 0z"></path></svg>
            </button>
        </td>
    `;
    categoriesTableBody.appendChild(tr);
    
    // Agregar evento al nuevo botón de editar
    const newEditBtn = tr.querySelector('.edit-category-btn');
    newEditBtn.addEventListener('click', () => {
        const categoria = tr.querySelector('td:nth-child(2)').textContent.trim();
        selectModifyCategory.value = categoria.toLowerCase();
        newCategoryNameInput.value = categoria;
        tempModifyCategory = tr;
        toggleModal('modify-category-modal', true);
    });
    
    // Actualizar selectores
    updateCategorySelectors();
    
    // Limpiar formulario
    categoryNameInput.value = '';
    tempNewCategory = null;
    toggleModal('add-category-confirm-modal', false);
    alert('¡Categoría agregada exitosamente!');
});
// --- FIN LÓGICA AGREGAR CATEGORÍA REAL ---

// --- INICIO LÓGICA ELIMINAR CATEGORÍA REAL ---
const selectDeleteCategory = document.getElementById('select-delete-category');
let tempDeleteCategory = null;

// ELIMINAR CATEGORÍA
const deleteCategoryBtn = document.getElementById('delete-category-btn');
const deleteCategoryModal = document.getElementById('delete-category-modal');
const closeDeleteModalBtn = document.getElementById('close-delete-modal-btn');
const performDeleteBtn = document.getElementById('perform-delete-btn');
const cancelDeleteCategoryBtn = document.getElementById('cancel-delete-category-btn');
const deleteCategoryConfirmModal = document.getElementById('delete-category-confirm-modal');
const confirmDeleteCategoryAction = document.getElementById('confirm-delete-category-action');
const cancelDeleteCategoryActionFinal = document.getElementById('cancel-delete-category-action-final');

deleteCategoryBtn.addEventListener('click', () => {
    updateCategorySelectors();
    toggleModal('delete-category-modal', true);
});
closeDeleteModalBtn.addEventListener('click', () => toggleModal('delete-category-modal', false));
cancelDeleteCategoryBtn.addEventListener('click', () => toggleModal('delete-category-modal', false));

performDeleteBtn.addEventListener('click', () => {
    const categoria = selectDeleteCategory.value;
    if (!categoria) {
        alert('Por favor, seleccione una categoría.');
        return;
    }
    // Buscar la fila correspondiente
    const filas = Array.from(categoriesTableBody.querySelectorAll('tr'));
    const filaEncontrada = filas.find(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length < 2) return false;
        const cat = tds[1].textContent.trim().toLowerCase();
        return cat === categoria.toLowerCase();
    });
    if (!filaEncontrada) {
        alert('No se encontró la categoría seleccionada.');
        return;
    }
    tempDeleteCategory = filaEncontrada;
    toggleModal('delete-category-modal', false);
    toggleModal('delete-category-confirm-modal', true);
});

confirmDeleteCategoryAction.addEventListener('click', () => {
    if (!tempDeleteCategory) {
        toggleModal('delete-category-confirm-modal', false);
        return;
    }
    // Eliminar la fila
    tempDeleteCategory.remove();
    tempDeleteCategory = null;
    // Reenumerar las filas
    Array.from(categoriesTableBody.querySelectorAll('tr')).forEach((tr, idx) => {
        const tdNum = tr.querySelector('td');
        if (tdNum) tdNum.textContent = idx + 1;
    });
    // Actualizar selectores
    updateCategorySelectors();
    // Limpiar formulario
    selectDeleteCategory.value = '';
    toggleModal('delete-category-confirm-modal', false);
    alert('¡Categoría eliminada exitosamente!');
});
// --- FIN LÓGICA ELIMINAR CATEGORÍA REAL ---

// --- INICIO LÓGICA DAR DE BAJA CATEGORÍA REAL ---
const selectDeactivateCategory = document.getElementById('select-deactivate-category');
let tempDeactivateCategory = null;

// DAR DE BAJA CATEGORÍA
const deactivateCategoryBtn = document.getElementById('deactivate-category-btn');
const deactivateCategoryModal = document.getElementById('deactivate-category-modal');
const closeDeactivateModalBtn = document.getElementById('close-deactivate-modal-btn');
const performDeactivateBtn = document.getElementById('perform-deactivate-btn');
const cancelDeactivateCategoryBtn = document.getElementById('cancel-deactivate-category-btn');
const deactivateCategoryConfirmModal = document.getElementById('deactivate-category-confirm-modal');
const confirmDeactivateCategoryAction = document.getElementById('confirm-deactivate-category-action');
const cancelDeactivateCategoryActionFinal = document.getElementById('cancel-deactivate-category-action-final');

deactivateCategoryBtn.addEventListener('click', () => toggleModal('deactivate-category-modal', true));
closeDeactivateModalBtn.addEventListener('click', () => toggleModal('deactivate-category-modal', false));
cancelDeactivateCategoryBtn.addEventListener('click', () => toggleModal('deactivate-category-modal', false));

performDeactivateBtn.addEventListener('click', () => {
    const categoria = selectDeactivateCategory.value.trim();
    if (!categoria) {
        alert('Por favor, ingrese el nombre de la categoría.');
        return;
    }
    // Buscar la fila correspondiente
    const filas = Array.from(categoriesTableBody.querySelectorAll('tr'));
    const filaEncontrada = filas.find(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length < 2) return false;
        const cat = tds[1].textContent.trim().toLowerCase();
        return cat === categoria.toLowerCase();
    });
    if (!filaEncontrada) {
        alert('No se encontró la categoría ingresada.');
        return;
    }
    tempDeactivateCategory = filaEncontrada;
    toggleModal('deactivate-category-modal', false);
    toggleModal('deactivate-category-confirm-modal', true);
});

confirmDeactivateCategoryAction.addEventListener('click', () => {
    if (!tempDeactivateCategory) {
        toggleModal('deactivate-category-confirm-modal', false);
        return;
    }
    // Cambiar el estilo de la fila para indicar que está de baja
    tempDeactivateCategory.style.opacity = '0.5';
    tempDeactivateCategory.style.textDecoration = 'line-through';
    tempDeactivateCategory = null;
    // Limpiar formulario
    selectDeactivateCategory.value = '';
    toggleModal('deactivate-category-confirm-modal', false);
    alert('¡Categoría dada de baja exitosamente!');
});
// --- FIN LÓGICA DAR DE BAJA CATEGORÍA REAL ---

// --- INICIO LÓGICA MODIFICAR CATEGORÍA REAL ---
const selectModifyCategory = document.getElementById('select-modify-category');
const newCategoryNameInput = document.getElementById('new-category-name');
let tempModifyCategory = null;

// MODIFICAR CATEGORÍA
const editCategoryBtns = document.querySelectorAll('.edit-category-btn');
const modifyCategoryModal = document.getElementById('modify-category-modal');
const closeModifyModalBtn = document.getElementById('close-modify-modal-btn');
const saveModifiedCategoryBtn = document.getElementById('save-modified-category-btn');
const cancelModifyCategoryBtn = document.getElementById('cancel-modify-category-btn');
const modifyCategoryConfirmModal = document.getElementById('modify-category-confirm-modal');
const confirmModifyCategoryAction = document.getElementById('confirm-modify-category-action');
const cancelModifyCategoryActionFinal = document.getElementById('cancel-modify-category-action-final');

editCategoryBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        const tr = btn.closest('tr');
        const categoria = tr.querySelector('td:nth-child(2)').textContent.trim();
        selectModifyCategory.value = categoria.toLowerCase();
        newCategoryNameInput.value = categoria;
        tempModifyCategory = tr;
        toggleModal('modify-category-modal', true);
    });
});

closeModifyModalBtn.addEventListener('click', () => toggleModal('modify-category-modal', false));
cancelModifyCategoryBtn.addEventListener('click', () => toggleModal('modify-category-modal', false));

saveModifiedCategoryBtn.addEventListener('click', () => {
    const nuevaCategoria = newCategoryNameInput.value.trim();
    if (!nuevaCategoria) {
        alert('Por favor, ingrese el nuevo nombre de la categoría.');
        return;
    }
    if (!tempModifyCategory) {
        toggleModal('modify-category-modal', false);
        return;
    }
    toggleModal('modify-category-modal', false);
    toggleModal('modify-category-confirm-modal', true);
});

confirmModifyCategoryAction.addEventListener('click', () => {
    if (!tempModifyCategory) {
        toggleModal('modify-category-confirm-modal', false);
        return;
    }
    const nuevaCategoria = newCategoryNameInput.value.trim();
    // Modificar la fila
    const tdCategoria = tempModifyCategory.querySelector('td:nth-child(2)');
    tdCategoria.textContent = nuevaCategoria.charAt(0).toUpperCase() + nuevaCategoria.slice(1);
    tempModifyCategory = null;
    // Actualizar selectores
    updateCategorySelectors();
    // Limpiar formulario
    selectModifyCategory.value = '';
    newCategoryNameInput.value = '';
    toggleModal('modify-category-confirm-modal', false);
    alert('¡Categoría modificada exitosamente!');
});
// --- FIN LÓGICA MODIFICAR CATEGORÍA REAL ---

// Manejo de cancelaciones
cancelAddCategoryAction.addEventListener('click', () => {
    toggleModal('add-category-confirm-modal', false);
    toggleModal('add-category-modal', true);
});

cancelDeleteCategoryActionFinal.addEventListener('click', () => {
    toggleModal('delete-category-confirm-modal', false);
    toggleModal('delete-category-modal', true);
});

cancelDeactivateCategoryActionFinal.addEventListener('click', () => {
    toggleModal('deactivate-category-confirm-modal', false);
    toggleModal('deactivate-category-modal', true);
});

cancelModifyCategoryActionFinal.addEventListener('click', () => {
    toggleModal('modify-category-confirm-modal', false);
    toggleModal('modify-category-modal', true);
});

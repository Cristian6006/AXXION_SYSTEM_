// Script para inventario.html
function toggleModal(modalId, show) {
    document.getElementById(modalId).classList.toggle('hidden', !show);
}

// Referencia a la tabla de inventario
const inventoryTableBody = document.getElementById('inventory-table-body');

// Variables para el formulario de agregar
const equipmentNameInput = document.getElementById('new-equipment-name');
const addCategorySelect = document.getElementById('add-category-select');
const addSubcategorySelect = document.getElementById('add-subcategory-select');
const addProviderSelect = document.getElementById('add-provider-select');
const addRouteSelect = document.getElementById('add-route-select');
const addDescriptionInput = document.getElementById('add-description');
let tempNewInventory = null;

// Variables para el formulario de editar
const editEquipmentNameInput = document.getElementById('edit-equipment-name');
const editCategorySelect = document.getElementById('edit-category-select');
const editSubcategorySelect = document.getElementById('edit-subcategory-select');
const editProviderSelect = document.getElementById('edit-provider-select');
const editRouteSelect = document.getElementById('edit-route-select');
const editDescriptionInput = document.getElementById('edit-description');
let tempEditInventory = null;

function getNextInventoryNumber() {
    return inventoryTableBody.querySelectorAll('tr').length + 1;
}

// AGREGAR INVENTARIO
const addInventoryBtn = document.getElementById('add-inventory-btn');
const addInventoryModal = document.getElementById('add-inventory-modal');
const closeAddModalBtn = document.getElementById('close-add-modal-btn');
const saveInventoryBtn = document.getElementById('save-inventory-btn');
const cancelAddInventoryBtn = document.getElementById('cancel-add-inventory-btn');
const addInventoryConfirmModal = document.getElementById('add-inventory-confirm-modal');
const confirmAddInventoryAction = document.getElementById('confirm-add-inventory-action');
const cancelAddInventoryActionFinal = document.getElementById('cancel-add-inventory-action-final');

addInventoryBtn.addEventListener('click', () => {
    // Limpiar formulario
    equipmentNameInput.value = '';
    addCategorySelect.value = '';
    addSubcategorySelect.value = '';
    addProviderSelect.value = '';
    addRouteSelect.value = '';
    addDescriptionInput.value = '';
    toggleModal('add-inventory-modal', true);
});

closeAddModalBtn.addEventListener('click', () => toggleModal('add-inventory-modal', false));
cancelAddInventoryBtn.addEventListener('click', () => toggleModal('add-inventory-modal', false));

saveInventoryBtn.addEventListener('click', () => {
    // Validar campos
    const equipmentName = equipmentNameInput.value.trim();
    const category = addCategorySelect.value.trim();
    const subcategory = addSubcategorySelect.value.trim();
    const provider = addProviderSelect.value.trim();
    const route = addRouteSelect.value.trim();
    const description = addDescriptionInput.value.trim();
    
    if (!equipmentName || !category || !subcategory || !provider || !route) {
        alert('Por favor, complete todos los campos obligatorios.');
        return;
    }
    
    tempNewInventory = { equipmentName, category, subcategory, provider, route, description };
    toggleModal('add-inventory-modal', false);
    toggleModal('add-inventory-confirm-modal', true);
});

confirmAddInventoryAction.addEventListener('click', () => {
    if (!tempNewInventory) {
        toggleModal('add-inventory-confirm-modal', false);
        return;
    }
    
    // Crear nueva fila en la tabla
    const tr = document.createElement('tr');
    tr.className = 'hover:bg-gray-50';
    const num = getNextInventoryNumber();
    
    // Capitalizar nombres
    const capitalizedCategory = tempNewInventory.category.charAt(0).toUpperCase() + tempNewInventory.category.slice(1);
    const capitalizedSubcategory = tempNewInventory.subcategory.charAt(0).toUpperCase() + tempNewInventory.subcategory.slice(1);
    const capitalizedProvider = tempNewInventory.provider.charAt(0).toUpperCase() + tempNewInventory.provider.slice(1);
    
    tr.innerHTML = `
        <td class="py-3 px-4">${num}</td>
        <td class="py-3 px-4">${capitalizedCategory}</td>
        <td class="py-3 px-4 table-cell-subcategory">${capitalizedSubcategory}</td>
        <td class="py-3 px-4">${capitalizedProvider}</td>
        <td class="py-3 px-4">${tempNewInventory.route}</td>
        <td class="py-3 px-4">3</td>
        <td class="py-3 px-4">1</td>
        <td class="py-3 px-4">2</td>
        <td class="py-3 px-4 flex justify-center items-center">
            <button class="edit-inventory-btn text-gray-600 hover:text-green-600">
                <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.928a2 2 0 010 2.828L14.828 15H12v-2.828l6.707-6.707a2 2 0 012.828 0z"></path></svg>
            </button>
        </td>
    `;
    
    inventoryTableBody.appendChild(tr);
    
    // Agregar evento al nuevo botón de editar
    const newEditBtn = tr.querySelector('.edit-inventory-btn');
    newEditBtn.addEventListener('click', () => {
        // Llenar el formulario de edición con los datos del inventario
        editEquipmentNameInput.value = tempNewInventory.equipmentName;
        editCategorySelect.value = tempNewInventory.category;
        editSubcategorySelect.value = tempNewInventory.subcategory;
        editProviderSelect.value = tempNewInventory.provider;
        editRouteSelect.value = tempNewInventory.route;
        editDescriptionInput.value = tempNewInventory.description;
        
        // Guardar referencia a la fila para actualizar
        window.currentEditRow = tr;
        window.currentEditInventory = { ...tempNewInventory };
        
        toggleModal('edit-inventory-modal', true);
    });
    
    // Limpiar variables
    tempNewInventory = null;
    toggleModal('add-inventory-confirm-modal', false);
    alert('¡Elemento de inventario agregado exitosamente!');
});

cancelAddInventoryActionFinal.addEventListener('click', () => {
    tempNewInventory = null;
    toggleModal('add-inventory-confirm-modal', false);
    toggleModal('add-inventory-modal', true);
});

// ELIMINAR INVENTARIO
const deleteInventoryBtn = document.getElementById('delete-inventory-btn');
const deleteInventoryModal = document.getElementById('delete-inventory-modal');
const closeDeleteModalBtn = document.getElementById('close-delete-modal-btn');
const performDeleteBtn = document.getElementById('perform-delete-btn');
const cancelDeleteInventoryBtn = document.getElementById('cancel-delete-inventory-btn');
const deleteInventoryConfirmModal = document.getElementById('delete-inventory-confirm-modal');
const confirmDeleteInventoryAction = document.getElementById('confirm-delete-inventory-action');
const cancelDeleteInventoryActionFinal = document.getElementById('cancel-delete-inventory-action-final');
const deleteCategoryInput = document.getElementById('delete-category-input');
const deleteSubcategoryInput = document.getElementById('delete-subcategory-input');
let tempDeleteInventory = null;

deleteInventoryBtn.addEventListener('click', () => toggleModal('delete-inventory-modal', true));
closeDeleteModalBtn.addEventListener('click', () => toggleModal('delete-inventory-modal', false));
cancelDeleteInventoryBtn.addEventListener('click', () => toggleModal('delete-inventory-modal', false));

performDeleteBtn.addEventListener('click', () => {
    const category = deleteCategoryInput.value.trim();
    const subcategory = deleteSubcategoryInput.value.trim();
    
    if (!category || !subcategory) {
        alert('Por favor, complete todos los campos.');
        return;
    }
    
    // Buscar la fila correspondiente
    const filas = Array.from(inventoryTableBody.querySelectorAll('tr'));
    const filaEncontrada = filas.find(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length < 3) return false;
        const cat = tds[1].textContent.trim().toLowerCase();
        const subcat = tds[2].textContent.trim().toLowerCase();
        return cat === category.toLowerCase() && subcat === subcategory.toLowerCase();
    });
    
    if (!filaEncontrada) {
        alert('No se encontró el elemento de inventario especificado.');
        return;
    }
    
    tempDeleteInventory = filaEncontrada;
    toggleModal('delete-inventory-modal', false);
    toggleModal('delete-inventory-confirm-modal', true);
});

confirmDeleteInventoryAction.addEventListener('click', () => {
    if (!tempDeleteInventory) {
        toggleModal('delete-inventory-confirm-modal', false);
        return;
    }
    
    // Eliminar la fila
    tempDeleteInventory.remove();
    tempDeleteInventory = null;
    
    // Reenumerar las filas
    Array.from(inventoryTableBody.querySelectorAll('tr')).forEach((tr, idx) => {
        const tdNum = tr.querySelector('td');
        if (tdNum) tdNum.textContent = idx + 1;
    });
    
    // Limpiar formulario
    deleteCategoryInput.value = '';
    deleteSubcategoryInput.value = '';
    
    toggleModal('delete-inventory-confirm-modal', false);
    alert('¡Elemento de inventario eliminado exitosamente!');
});

// DAR DE BAJA INVENTARIO
const deactivateInventoryBtn = document.getElementById('deactivate-inventory-btn');
const deactivateInventoryModal = document.getElementById('deactivate-inventory-modal');
const closeDeactivateModalBtn = document.getElementById('close-deactivate-modal-btn');
const performDeactivateBtn = document.getElementById('perform-deactivate-btn');
const cancelDeactivateInventoryBtn = document.getElementById('cancel-deactivate-inventory-btn');
const deactivateInventoryConfirmModal = document.getElementById('deactivate-inventory-confirm-modal');
const confirmDeactivateInventoryAction = document.getElementById('confirm-deactivate-inventory-action');
const cancelDeactivateInventoryActionFinal = document.getElementById('cancel-deactivate-inventory-action-final');
const deactivateCategoryInput = document.getElementById('deactivate-category-input');
const deactivateSubcategoryInput = document.getElementById('deactivate-subcategory-input');
let tempDeactivateInventory = null;

deactivateInventoryBtn.addEventListener('click', () => toggleModal('deactivate-inventory-modal', true));
closeDeactivateModalBtn.addEventListener('click', () => toggleModal('deactivate-inventory-modal', false));
cancelDeactivateInventoryBtn.addEventListener('click', () => toggleModal('deactivate-inventory-modal', false));

performDeactivateBtn.addEventListener('click', () => {
    const category = deactivateCategoryInput.value.trim();
    const subcategory = deactivateSubcategoryInput.value.trim();
    
    if (!category || !subcategory) {
        alert('Por favor, complete todos los campos.');
        return;
    }
    
    // Buscar la fila correspondiente
    const filas = Array.from(inventoryTableBody.querySelectorAll('tr'));
    const filaEncontrada = filas.find(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length < 3) return false;
        const cat = tds[1].textContent.trim().toLowerCase();
        const subcat = tds[2].textContent.trim().toLowerCase();
        return cat === category.toLowerCase() && subcat === subcategory.toLowerCase();
    });
    
    if (!filaEncontrada) {
        alert('No se encontró el elemento de inventario especificado.');
        return;
    }
    
    tempDeactivateInventory = filaEncontrada;
    toggleModal('deactivate-inventory-modal', false);
    toggleModal('deactivate-inventory-confirm-modal', true);
});

confirmDeactivateInventoryAction.addEventListener('click', () => {
    if (!tempDeactivateInventory) {
        toggleModal('deactivate-inventory-confirm-modal', false);
        return;
    }
    
    // Cambiar el estilo de la fila para indicar que está de baja
    tempDeactivateInventory.style.opacity = '0.5';
    tempDeactivateInventory.style.textDecoration = 'line-through';
    tempDeactivateInventory = null;
    
    // Limpiar formulario
    deactivateCategoryInput.value = '';
    deactivateSubcategoryInput.value = '';
    
    toggleModal('deactivate-inventory-confirm-modal', false);
    alert('¡Elemento de inventario dado de baja exitosamente!');
});

// MODIFICAR INVENTARIO
const editInventoryBtns = document.querySelectorAll('.edit-inventory-btn');
const editInventoryModal = document.getElementById('edit-inventory-modal');
const closeEditModalBtn = document.getElementById('close-edit-modal-btn');
const saveEditedInventoryBtn = document.getElementById('save-edited-inventory-btn');
const cancelEditInventoryBtn = document.getElementById('cancel-edit-inventory-btn');
const editInventoryConfirmModal = document.getElementById('edit-inventory-confirm-modal');
const confirmEditInventoryAction = document.getElementById('confirm-edit-inventory-action');
const cancelEditInventoryActionFinal = document.getElementById('cancel-edit-inventory-action-final');

editInventoryBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        toggleModal('edit-inventory-modal', true);
    });
});

closeEditModalBtn.addEventListener('click', () => toggleModal('edit-inventory-modal', false));
cancelEditInventoryBtn.addEventListener('click', () => toggleModal('edit-inventory-modal', false));

saveEditedInventoryBtn.addEventListener('click', () => {
    // Validar campos
    const equipmentName = editEquipmentNameInput.value.trim();
    const category = editCategorySelect.value.trim();
    const subcategory = editSubcategorySelect.value.trim();
    const provider = editProviderSelect.value.trim();
    const route = editRouteSelect.value.trim();
    const description = editDescriptionInput.value.trim();
    
    if (!equipmentName || !category || !subcategory || !provider || !route) {
        alert('Por favor, complete todos los campos obligatorios.');
        return;
    }
    
    tempEditInventory = { equipmentName, category, subcategory, provider, route, description };
    toggleModal('edit-inventory-modal', false);
    toggleModal('edit-inventory-confirm-modal', true);
});

confirmEditInventoryAction.addEventListener('click', () => {
    if (!tempEditInventory || !window.currentEditRow) {
        toggleModal('edit-inventory-confirm-modal', false);
        return;
    }
    
    // Actualizar la fila
    const tds = window.currentEditRow.querySelectorAll('td');
    const capitalizedCategory = tempEditInventory.category.charAt(0).toUpperCase() + tempEditInventory.category.slice(1);
    const capitalizedSubcategory = tempEditInventory.subcategory.charAt(0).toUpperCase() + tempEditInventory.subcategory.slice(1);
    const capitalizedProvider = tempEditInventory.provider.charAt(0).toUpperCase() + tempEditInventory.provider.slice(1);
    
    tds[1].textContent = capitalizedCategory;
    tds[2].textContent = capitalizedSubcategory;
    tds[3].textContent = capitalizedProvider;
    tds[4].textContent = tempEditInventory.route;
    
    // Actualizar eventos del botón de editar
    const editBtn = tds[8].querySelector('.edit-inventory-btn');
    editBtn.addEventListener('click', () => {
        // Llenar el formulario de edición con los datos actualizados
        editEquipmentNameInput.value = tempEditInventory.equipmentName;
        editCategorySelect.value = tempEditInventory.category;
        editSubcategorySelect.value = tempEditInventory.subcategory;
        editProviderSelect.value = tempEditInventory.provider;
        editRouteSelect.value = tempEditInventory.route;
        editDescriptionInput.value = tempEditInventory.description;
        
        // Guardar referencia a la fila para actualizar
        window.currentEditRow = window.currentEditRow;
        window.currentEditInventory = { ...tempEditInventory };
        
        toggleModal('edit-inventory-modal', true);
    });
    
    // Limpiar variables
    tempEditInventory = null;
    window.currentEditRow = null;
    window.currentEditInventory = null;
    
    // Limpiar formulario
    editEquipmentNameInput.value = '';
    editCategorySelect.value = '';
    editSubcategorySelect.value = '';
    editProviderSelect.value = '';
    editRouteSelect.value = '';
    editDescriptionInput.value = '';
    
    toggleModal('edit-inventory-confirm-modal', false);
    alert('¡Elemento de inventario modificado exitosamente!');
});

// Manejo de cancelaciones
cancelDeleteInventoryActionFinal.addEventListener('click', () => {
    toggleModal('delete-inventory-confirm-modal', false);
    toggleModal('delete-inventory-modal', true);
});

cancelDeactivateInventoryActionFinal.addEventListener('click', () => {
    toggleModal('deactivate-inventory-confirm-modal', false);
    toggleModal('deactivate-inventory-modal', true);
});

cancelEditInventoryActionFinal.addEventListener('click', () => {
    toggleModal('edit-inventory-confirm-modal', false);
    toggleModal('edit-inventory-modal', true);
});

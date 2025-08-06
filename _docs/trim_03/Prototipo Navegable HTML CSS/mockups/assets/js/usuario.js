// Script para usuario.html
function toggleModal(modalId, show) {
    document.getElementById(modalId).classList.toggle('hidden', !show);
}

// Referencia a la tabla de usuarios
const usersTableBody = document.getElementById('users-table-body');

// Función para actualizar los selectores de usuarios
function updateUserSelectors() {
    const selectDeleteUser = document.getElementById('select-delete-user');
    
    // Obtener todas las usuarios de la tabla
    const usuarios = Array.from(usersTableBody.querySelectorAll('tr')).map(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length >= 4) {
            const nombre = tds[0].textContent.split('\n')[0].trim();
            const identificacion = tds[1].textContent.trim();
            return { nombre, identificacion };
        }
        return null;
    }).filter(user => user !== null);
    
    // Actualizar selector de eliminar
    if (selectDeleteUser) {
        const currentValue = selectDeleteUser.value;
        selectDeleteUser.innerHTML = '<option value="">Seleccione un usuario</option>';
        usuarios.forEach(usuario => {
            const option = document.createElement('option');
            option.value = usuario.identificacion;
            option.textContent = `${usuario.nombre} (${usuario.identificacion})`;
            selectDeleteUser.appendChild(option);
        });
        selectDeleteUser.value = currentValue;
    }
}

// Variables para el formulario
const nameInput = document.getElementById('name');
const phoneInput = document.getElementById('phone');
const identificationInput = document.getElementById('identification');
const emailInput = document.getElementById('email');
const roleInput = document.getElementById('role');
let tempNewUser = null;

// AGREGAR USUARIO
const addUserBtn = document.getElementById('add-user-btn');
const addUserModal = document.getElementById('add-user-modal');
const closeModalBtn = document.getElementById('close-modal-btn');
const confirmAddBtn = document.getElementById('confirm-add-btn');
const cancelAddBtn = document.getElementById('cancel-add-btn');
const addConfirmationModal = document.getElementById('add-confirmation-modal');
const finalAcceptBtn = document.getElementById('final-accept-btn');
const finalCancelBtn = document.getElementById('final-cancel-btn');

// Mostrar el modal de agregar usuario
addUserBtn.addEventListener('click', () => {
    // Limpiar formulario
    nameInput.value = '';
    phoneInput.value = '';
    identificationInput.value = '';
    emailInput.value = '';
    roleInput.value = '';
    toggleModal('add-user-modal', true);
});

// Cerrar el modal de agregar usuario desde el botón X o Cancelar
closeModalBtn.addEventListener('click', () => {
    toggleModal('add-user-modal', false);
});

cancelAddBtn.addEventListener('click', () => {
    toggleModal('add-user-modal', false);
});

// Mostrar el modal de confirmación después de Aceptar en el modal de agregar
confirmAddBtn.addEventListener('click', () => {
    // Validar campos
    const name = nameInput.value.trim();
    const phone = phoneInput.value.trim();
    const identification = identificationInput.value.trim();
    const email = emailInput.value.trim();
    const role = roleInput.value.trim();
    
    if (!name || !phone || !identification || !email || !role) {
        alert('Por favor, complete todos los campos.');
        return;
    }
    
    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('Por favor, ingrese un email válido.');
        return;
    }
    
    tempNewUser = { name, phone, identification, email, role };
    toggleModal('add-user-modal', false);
    toggleModal('add-confirmation-modal', true);
});

// Manejar el modal de confirmación
finalAcceptBtn.addEventListener('click', () => {
    if (!tempNewUser) {
        toggleModal('add-confirmation-modal', false);
        return;
    }
    
    // Crear nueva fila en la tabla
    const tr = document.createElement('tr');
    tr.className = 'hover:bg-gray-50';
    
    // Capitalizar el nombre y el rol
    const capitalizedName = tempNewUser.name.charAt(0).toUpperCase() + tempNewUser.name.slice(1);
    const capitalizedRole = tempNewUser.role.charAt(0).toUpperCase() + tempNewUser.role.slice(1);
    
    tr.innerHTML = `
        <td class="py-3 px-4 whitespace-nowrap">${capitalizedName}<br><span class="text-sm text-gray-500">${capitalizedRole}</span></td>
        <td class="py-3 px-4 whitespace-nowrap">${tempNewUser.identification}</td>
        <td class="py-3 px-4 whitespace-nowrap">${tempNewUser.email}</td>
        <td class="py-3 px-4 whitespace-nowrap">${tempNewUser.phone}</td>
        <td class="py-3 px-4 flex justify-center items-center space-x-2">
            <button class="view-user-btn text-gray-600 hover:text-blue-600" title="Ver detalles">
                <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path></svg>
            </button>
            <button class="edit-user-btn text-gray-600 hover:text-green-600" title="Editar usuario">
                <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.928a2 2 0 010 2.828L14.828 15H12v-2.828l6.707-6.707a2 2 0 012.828 0z"></path></svg>
            </button>
        </td>
    `;
    
    usersTableBody.appendChild(tr);
    
    // Agregar eventos a los nuevos botones
    const newViewBtn = tr.querySelector('.view-user-btn');
    const newEditBtn = tr.querySelector('.edit-user-btn');
    
    newViewBtn.addEventListener('click', () => {
        alert(`Detalles del usuario:\nNombre: ${tempNewUser.name}\nTeléfono: ${tempNewUser.phone}\nIdentificación: ${tempNewUser.identification}\nEmail: ${tempNewUser.email}\nRol: ${tempNewUser.role}`);
    });
    
    newEditBtn.addEventListener('click', () => {
        // Llenar el formulario de actualización con los datos del usuario
        document.getElementById('update-name').value = tempNewUser.name;
        document.getElementById('update-phone').value = tempNewUser.phone;
        document.getElementById('update-identification').value = tempNewUser.identification;
        document.getElementById('update-email').value = tempNewUser.email;
        document.getElementById('update-role').value = tempNewUser.role;
        
        // Guardar referencia a la fila para actualizar
        window.currentEditRow = tr;
        window.currentEditUser = { ...tempNewUser };
        
        toggleModal('update-user-modal', true);
    });
    
    // Actualizar selectores
    updateUserSelectors();
    
    // Limpiar variables
    tempNewUser = null;
    toggleModal('add-confirmation-modal', false);
    alert('¡Usuario agregado exitosamente!');
});

finalCancelBtn.addEventListener('click', () => {
    tempNewUser = null;
    toggleModal('add-confirmation-modal', false);
    toggleModal('add-user-modal', true);
});

// --- INICIO LÓGICA ELIMINAR USUARIO ---
const selectDeleteUser = document.getElementById('select-delete-user');
let tempDeleteUser = null;

// ELIMINAR USUARIO
const deleteUserBtn = document.getElementById('delete-user-btn');
const deleteUserModal = document.getElementById('delete-user-modal');
const closeDeleteModalBtn = document.getElementById('close-delete-modal-btn');
const performDeleteUserBtn = document.getElementById('perform-delete-user-btn');
const cancelDeleteUserBtn = document.getElementById('cancel-delete-user-btn');
const deleteUserConfirmModal = document.getElementById('delete-user-confirm-modal');
const confirmDeleteUserAction = document.getElementById('confirm-delete-user-action');
const cancelDeleteUserActionFinal = document.getElementById('cancel-delete-user-action-final');

deleteUserBtn.addEventListener('click', () => {
    updateUserSelectors();
    toggleModal('delete-user-modal', true);
});

closeDeleteModalBtn.addEventListener('click', () => toggleModal('delete-user-modal', false));
cancelDeleteUserBtn.addEventListener('click', () => toggleModal('delete-user-modal', false));

performDeleteUserBtn.addEventListener('click', () => {
    const identificacion = selectDeleteUser.value;
    if (!identificacion) {
        alert('Por favor, seleccione un usuario.');
        return;
    }
    // Buscar la fila correspondiente
    const filas = Array.from(usersTableBody.querySelectorAll('tr'));
    const filaEncontrada = filas.find(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length < 2) return false;
        const id = tds[1].textContent.trim();
        return id === identificacion;
    });
    if (!filaEncontrada) {
        alert('No se encontró el usuario seleccionado.');
        return;
    }
    tempDeleteUser = filaEncontrada;
    toggleModal('delete-user-modal', false);
    toggleModal('delete-user-confirm-modal', true);
});

confirmDeleteUserAction.addEventListener('click', () => {
    if (!tempDeleteUser) {
        toggleModal('delete-user-confirm-modal', false);
        return;
    }
    // Eliminar la fila
    tempDeleteUser.remove();
    tempDeleteUser = null;
    // Actualizar selectores
    updateUserSelectors();
    // Limpiar formulario
    selectDeleteUser.value = '';
    toggleModal('delete-user-confirm-modal', false);
    alert('¡Usuario eliminado exitosamente!');
});
// --- FIN LÓGICA ELIMINAR USUARIO ---

// --- INICIO LÓGICA ACTUALIZAR USUARIO ---
let tempUpdateUser = null;

// ACTUALIZAR USUARIO
const updateUserModal = document.getElementById('update-user-modal');
const closeUpdateModalBtn = document.getElementById('close-update-modal-btn');
const saveUpdatedUserBtn = document.getElementById('save-updated-user-btn');
const cancelUpdateUserBtn = document.getElementById('cancel-update-user-btn');
const updateUserConfirmModal = document.getElementById('update-user-confirm-modal');
const confirmUpdateUserAction = document.getElementById('confirm-update-user-action');
const cancelUpdateUserActionFinal = document.getElementById('cancel-update-user-action-final');

closeUpdateModalBtn.addEventListener('click', () => toggleModal('update-user-modal', false));
cancelUpdateUserBtn.addEventListener('click', () => toggleModal('update-user-modal', false));

saveUpdatedUserBtn.addEventListener('click', () => {
    // Validar campos
    const name = document.getElementById('update-name').value.trim();
    const phone = document.getElementById('update-phone').value.trim();
    const identification = document.getElementById('update-identification').value.trim();
    const email = document.getElementById('update-email').value.trim();
    const role = document.getElementById('update-role').value.trim();
    
    if (!name || !phone || !identification || !email || !role) {
        alert('Por favor, complete todos los campos.');
        return;
    }
    
    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('Por favor, ingrese un email válido.');
        return;
    }
    
    tempUpdateUser = { name, phone, identification, email, role };
    toggleModal('update-user-modal', false);
    toggleModal('update-user-confirm-modal', true);
});

confirmUpdateUserAction.addEventListener('click', () => {
    if (!tempUpdateUser || !window.currentEditRow) {
        toggleModal('update-user-confirm-modal', false);
        return;
    }
    
    // Actualizar la fila
    const tds = window.currentEditRow.querySelectorAll('td');
    const capitalizedName = tempUpdateUser.name.charAt(0).toUpperCase() + tempUpdateUser.name.slice(1);
    const capitalizedRole = tempUpdateUser.role.charAt(0).toUpperCase() + tempUpdateUser.role.slice(1);
    
    tds[0].innerHTML = `${capitalizedName}<br><span class="text-sm text-gray-500">${capitalizedRole}</span>`;
    tds[1].textContent = tempUpdateUser.identification;
    tds[2].textContent = tempUpdateUser.email;
    tds[3].textContent = tempUpdateUser.phone;
    
    // Actualizar eventos de los botones
    const viewBtn = tds[4].querySelector('.view-user-btn');
    const editBtn = tds[4].querySelector('.edit-user-btn');
    
    viewBtn.addEventListener('click', () => {
        alert(`Detalles del usuario:\nNombre: ${tempUpdateUser.name}\nTeléfono: ${tempUpdateUser.phone}\nIdentificación: ${tempUpdateUser.identification}\nEmail: ${tempUpdateUser.email}\nRol: ${tempUpdateUser.role}`);
    });
    
    editBtn.addEventListener('click', () => {
        // Llenar el formulario de actualización con los datos actualizados
        document.getElementById('update-name').value = tempUpdateUser.name;
        document.getElementById('update-phone').value = tempUpdateUser.phone;
        document.getElementById('update-identification').value = tempUpdateUser.identification;
        document.getElementById('update-email').value = tempUpdateUser.email;
        document.getElementById('update-role').value = tempUpdateUser.role;
        
        // Guardar referencia a la fila para actualizar
        window.currentEditRow = window.currentEditRow;
        window.currentEditUser = { ...tempUpdateUser };
        
        toggleModal('update-user-modal', true);
    });
    
    // Actualizar selectores
    updateUserSelectors();
    
    // Limpiar variables
    tempUpdateUser = null;
    window.currentEditRow = null;
    window.currentEditUser = null;
    
    // Limpiar formulario
    document.getElementById('update-name').value = '';
    document.getElementById('update-phone').value = '';
    document.getElementById('update-identification').value = '';
    document.getElementById('update-email').value = '';
    document.getElementById('update-role').value = '';
    
    toggleModal('update-user-confirm-modal', false);
    alert('¡Usuario actualizado exitosamente!');
});
// --- FIN LÓGICA ACTUALIZAR USUARIO ---

// Manejo de cancelaciones
cancelDeleteUserActionFinal.addEventListener('click', () => {
    toggleModal('delete-user-confirm-modal', false);
    toggleModal('delete-user-modal', true);
});

cancelUpdateUserActionFinal.addEventListener('click', () => {
    toggleModal('update-user-confirm-modal', false);
    toggleModal('update-user-modal', true);
});

// assets/js/alquiler.js
// CRUD para alquileres usando un array en memoria y control de modales

let rentals = [];
let rentalToDelete = null;
let rentalToDeactivate = null;

// Utilidades para mostrar/ocultar modales
function openModal(id) {
    document.getElementById(id).classList.remove('hidden');
}
function closeModal(id) {
    document.getElementById(id).classList.add('hidden');
}

// Renderizar la tabla de alquileres
function renderRentalTable() {
    const tbody = document.getElementById('rental-table-body');
    tbody.innerHTML = '';
    if (rentals.length === 0) {
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        td.colSpan = 7;
        td.className = 'text-center text-gray-400 py-4';
        td.textContent = 'No hay alquileres registrados.';
        tr.appendChild(td);
        tbody.appendChild(tr);
        return;
    }
    rentals.forEach((rental, idx) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="px-4 py-2">${rental.id}</td>
            <td class="px-4 py-2">${rental.client}</td>
            <td class="px-4 py-2">${rental.equipment}</td>
            <td class="px-4 py-2">${rental.startDate}</td>
            <td class="px-4 py-2">${rental.endDate}</td>
            <td class="px-4 py-2">${rental.status}</td>
            <td class="px-4 py-2 flex flex-wrap gap-1">
                <button class="edit-btn px-2 py-1 bg-yellow-400 text-white rounded" data-idx="${idx}">Editar</button>
                <button class="delete-btn px-2 py-1 bg-red-500 text-white rounded" data-idx="${idx}">Eliminar</button>
                <button class="deactivate-btn px-2 py-1 bg-purple-600 text-white rounded" data-idx="${idx}" ${rental.status === 'Cancelado' ? 'disabled' : ''}>Dar de Baja</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// Limpiar formulario de alquiler
function clearRentalForm() {
    document.getElementById('rental-form').reset();
    document.getElementById('rental-index').value = '';
}

// Abrir modal para agregar alquiler
function openAddRentalModal() {
    clearRentalForm();
    document.getElementById('rental-modal-title').textContent = 'Agregar Alquiler';
    openModal('rental-modal');
}

// Abrir modal para editar alquiler
function openEditRentalModal(idx) {
    const rental = rentals[idx];
    document.getElementById('rental-index').value = idx;
    document.getElementById('rental-id').value = rental.id;
    document.getElementById('rental-client').value = rental.client;
    document.getElementById('rental-equipment').value = rental.equipment;
    document.getElementById('rental-start-date').value = rental.startDate;
    document.getElementById('rental-end-date').value = rental.endDate;
    document.getElementById('rental-status').value = rental.status;
    document.getElementById('rental-modal-title').textContent = 'Editar Alquiler';
    openModal('rental-modal');
}

// Guardar alquiler (agregar o editar)
document.getElementById('rental-form').addEventListener('submit', function(e) {
    e.preventDefault();
    const idx = document.getElementById('rental-index').value;
    const rental = {
        id: document.getElementById('rental-id').value.trim(),
        client: document.getElementById('rental-client').value.trim(),
        equipment: document.getElementById('rental-equipment').value.trim(),
        startDate: document.getElementById('rental-start-date').value,
        endDate: document.getElementById('rental-end-date').value,
        status: document.getElementById('rental-status').value
    };
    if (idx === '') {
        rentals.push(rental);
    } else {
        rentals[idx] = rental;
    }
    closeModal('rental-modal');
    renderRentalTable();
});

document.getElementById('cancel-rental-btn').onclick = function() {
    closeModal('rental-modal');
};
document.getElementById('close-rental-modal').onclick = function() {
    closeModal('rental-modal');
};

// Delegación de eventos para editar, eliminar y dar de baja

document.getElementById('rental-table-body').addEventListener('click', function(e) {
    if (e.target.classList.contains('edit-btn')) {
        openEditRentalModal(e.target.dataset.idx);
    } else if (e.target.classList.contains('delete-btn')) {
        rentalToDelete = e.target.dataset.idx;
        openModal('delete-rental-modal');
    } else if (e.target.classList.contains('deactivate-btn')) {
        rentalToDeactivate = e.target.dataset.idx;
        openModal('deactivate-rental-modal');
    }
});

// Eliminar alquiler
document.getElementById('confirm-delete-rental-btn').onclick = function() {
    if (rentalToDelete !== null) {
        rentals.splice(rentalToDelete, 1);
        rentalToDelete = null;
        renderRentalTable();
    }
    closeModal('delete-rental-modal');
};
document.getElementById('cancel-delete-rental-btn').onclick = function() {
    rentalToDelete = null;
    closeModal('delete-rental-modal');
};
document.getElementById('close-delete-rental-modal').onclick = function() {
    rentalToDelete = null;
    closeModal('delete-rental-modal');
};

// Dar de baja alquiler
document.getElementById('confirm-deactivate-rental-btn').onclick = function() {
    if (rentalToDeactivate !== null) {
        rentals[rentalToDeactivate].status = 'Cancelado';
        rentalToDeactivate = null;
        renderRentalTable();
    }
    closeModal('deactivate-rental-modal');
};
document.getElementById('cancel-deactivate-rental-btn').onclick = function() {
    rentalToDeactivate = null;
    closeModal('deactivate-rental-modal');
};
document.getElementById('close-deactivate-rental-modal').onclick = function() {
    rentalToDeactivate = null;
    closeModal('deactivate-rental-modal');
};

// Inicializar tabla vacía y botón para agregar
renderRentalTable();

// Si tienes un botón para abrir el modal de agregar, enlázalo aquí:
const addRentalBtn = document.getElementById('add-rental-btn');
if (addRentalBtn) {
    addRentalBtn.onclick = openAddRentalModal;
}

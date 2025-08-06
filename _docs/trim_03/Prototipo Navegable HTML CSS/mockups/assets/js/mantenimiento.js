// --- CRUD de Mantenimientos con Array ---
let mantenimientos = [];

function renderMantenimientos() {
    const tbody = document.querySelector('table tbody');
    tbody.innerHTML = '';
    let idx = 1;
    mantenimientos.forEach(m => {
        if (m.activo) {
            const estadoClass = m.estado === 'Finalizado' ? 'status-finalizado' : m.estado === 'En proceso' ? 'status-en-proceso' : 'status-pendiente';
            const tr = document.createElement('tr');
            tr.className = 'hover:bg-gray-50';
            tr.innerHTML = `
                <td class="py-3 px-4">${idx++}</td>
                <td class="py-3 px-4">${m.id}</td>
                <td class="py-3 px-4">${m.equipo}</td>
                <td class="py-3 px-4">${m.tipo}</td>
                <td class="py-3 px-4">${m.ubicacion}</td>
                <td class="py-3 px-4">${m.tecnico}</td>
                <td class="py-3 px-4"><span class="status-pill ${estadoClass}">${m.estado}</span></td>
                <td class="py-3 px-4 flex justify-center items-center space-x-2">
                    <a href="mantenimiento-detail.html" class="text-gray-600 hover:text-blue-600">
                        <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path></svg>
                    </a>
                    <button class="edit-maintenance-btn text-gray-600 hover:text-green-600" data-id="${m.id}">
                        <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.928a2 2 0 010 2.828L14.828 15H12v-2.828l6.707-6.707a2 2 0 012.828 0z"></path></svg>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        }
    });
    attachEditEvents();
}

function agregarMantenimiento(m) {
    mantenimientos.push(m);
    renderMantenimientos();
}

function eliminarMantenimiento(id) {
    const idx = mantenimientos.findIndex(m => m.id === id);
    if (idx !== -1) mantenimientos.splice(idx, 1);
    renderMantenimientos();
}

function darDeBajaMantenimiento(id) {
    const idx = mantenimientos.findIndex(m => m.id === id);
    if (idx !== -1) mantenimientos[idx].activo = false;
    renderMantenimientos();
}

function modificarMantenimiento(id, data) {
    const idx = mantenimientos.findIndex(m => m.id === id);
    if (idx !== -1) {
        mantenimientos[idx] = { ...mantenimientos[idx], ...data };
    }
    renderMantenimientos();
}

function attachEditEvents() {
    document.querySelectorAll('.edit-maintenance-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const id = parseInt(this.getAttribute('data-id'));
            const m = mantenimientos.find(x => x.id === id);
            if (m) openEditModal(m);
        });
    });
}

// Utilidades para mostrar/ocultar modales
function toggleModal(id, show) {
    document.getElementById(id).classList.toggle('hidden', !show);
}
// Renderiza selects de ID para eliminar/dar de baja
function renderIdSelects() {
    const del = document.getElementById('delete-maintenance-id');
    const baja = document.getElementById('deactivate-maintenance-id');
    if (!del || !baja) return;
    del.innerHTML = '<option value="">Seleccione un ID</option>';
    baja.innerHTML = '<option value="">Seleccione un ID</option>';
    mantenimientos.forEach(m => {
        if (m.activo) {
            del.innerHTML += `<option value="${m.id}">${m.id} - ${m.equipo}</option>`;
            baja.innerHTML += `<option value="${m.id}">${m.id} - ${m.equipo}</option>`;
        }
    });
}
// Botones principales
window.addEventListener('DOMContentLoaded', () => {
    renderMantenimientos();
    document.getElementById('add-equipment-btn').onclick = () => { toggleModal('add-maintenance-modal', true); };
    document.getElementById('delete-equipment-btn').onclick = () => { renderIdSelects(); toggleModal('delete-maintenance-modal', true); };
    document.getElementById('deactivate-equipment-btn').onclick = () => { renderIdSelects(); toggleModal('deactivate-maintenance-modal', true); };
    // Cerrar/cancelar modales
    document.getElementById('close-add-maintenance-modal').onclick = () => toggleModal('add-maintenance-modal', false);
    document.getElementById('cancel-add-maintenance').onclick = () => toggleModal('add-maintenance-modal', false);
    document.getElementById('close-delete-maintenance-modal').onclick = () => toggleModal('delete-maintenance-modal', false);
    document.getElementById('cancel-delete-maintenance').onclick = () => toggleModal('delete-maintenance-modal', false);
    document.getElementById('close-deactivate-maintenance-modal').onclick = () => toggleModal('deactivate-maintenance-modal', false);
    document.getElementById('cancel-deactivate-maintenance').onclick = () => toggleModal('deactivate-maintenance-modal', false);
    // Agregar
    document.getElementById('add-maintenance-form').onsubmit = function(e) {
        e.preventDefault();
        agregarMantenimiento({
            id: parseInt(document.getElementById('add-id').value),
            equipo: document.getElementById('add-equipo').value,
            tipo: document.getElementById('add-tipo').value,
            ubicacion: document.getElementById('add-ubicacion').value,
            tecnico: document.getElementById('add-tecnico').value,
            estado: document.getElementById('add-estado').value,
            activo: true
        });
        toggleModal('add-maintenance-modal', false);
        this.reset();
    };
    // Eliminar
    document.getElementById('delete-maintenance-form').onsubmit = function(e) {
        e.preventDefault();
        const id = parseInt(document.getElementById('delete-maintenance-id').value);
        eliminarMantenimiento(id);
        toggleModal('delete-maintenance-modal', false);
    };
    // Dar de baja
    document.getElementById('deactivate-maintenance-form').onsubmit = function(e) {
        e.preventDefault();
        const id = parseInt(document.getElementById('deactivate-maintenance-id').value);
        darDeBajaMantenimiento(id);
        toggleModal('deactivate-maintenance-modal', false);
    };
});

// assets/js/login-fallback.js
// Fallback login for static/demo mode (no backend)
document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form[action="login.php"]');
    if (!form) return;

    // Demo users
    const users = [
        { email: 'admin@demo.com', password: 'admin123' },
        { email: 'aux@demo.com', password: 'aux123' },
        { email: 'asesor@demo.com', password: 'asesor123' }
    ];

    // Error message container
    let errorDiv = form.querySelector('.login-error-fallback');
    if (!errorDiv) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'login-error-fallback bg-red-500 text-white p-2 rounded mb-2 text-center';
        form.insertBefore(errorDiv, form.firstChild.nextSibling); // after h2/header
    }
    errorDiv.style.display = 'none';

    form.addEventListener('submit', function (e) {
        // Try to detect if backend is available (optional, fallback always for demo)
        e.preventDefault();
        const email = form.email.value.trim();
        const password = form.password.value;
        const found = users.some(u => u.email === email && u.password === password);
        if (found) {
            window.location.href = 'inicio.html';
        } else {
            errorDiv.textContent = 'Usuario o contraseña incorrectos (demo JS)';
            errorDiv.style.display = 'block';
        }
    });
});

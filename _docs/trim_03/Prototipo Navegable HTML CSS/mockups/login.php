<?php
// Simulación de usuarios
$users = [
    ["email" => "admin@demo.com", "password" => "admin123"],
    ["email" => "aux@demo.com", "password" => "aux123"],
    ["email" => "asesor@demo.com", "password" => "asesor123"]
];

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $email = $_POST["email"] ?? "";
    $password = $_POST["password"] ?? "";
    $found = false;
    foreach ($users as $user) {
        if ($user["email"] === $email && $user["password"] === $password) {
            $found = true;
            break;
        }
    }
    if ($found) {
        header("Location: inicio.html");
        exit();
    } else {
        $error = "Usuario o contraseña incorrectos";
    }
} else {
    $error = "";
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-900 flex items-center justify-center min-h-screen">
    <form method="POST" action="login.php" class="bg-slate-800 p-8 rounded-lg shadow-lg w-full max-w-sm space-y-6">
        <h2 class="text-2xl font-bold text-white mb-4">Iniciar Sesión</h2>
        <?php if (!empty($error)): ?>
            <div class="bg-red-500 text-white p-2 rounded mb-2 text-center"><?php echo $error; ?></div>
        <?php endif; ?>
        <div>
            <label class="block text-gray-300 text-sm font-medium mb-2">E-MAIL</label>
            <input type="email" name="email" required placeholder="Correo electrónico" class="input-field w-full px-4 py-3 rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-0">
        </div>
        <div>
            <label class="block text-gray-300 text-sm font-medium mb-2">PASSWORD</label>
            <input type="password" name="password" required placeholder="Contraseña" class="input-field w-full px-4 py-3 rounded-lg text-white placeholder-gray-400 focus:outline-none focus:ring-0">
        </div>
        <button type="submit" class="btn-gradient w-full py-3 px-6 rounded-lg text-white font-semibold focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:ring-offset-2 focus:ring-offset-slate-800">Entrar</button>
        <div class="text-gray-400 text-xs mt-2">
            <b>Usuarios de prueba:</b><br>
            admin@demo.com / admin123<br>
            aux@demo.com / aux123<br>
            asesor@demo.com / asesor123
        </div>
    </form>
</body>
</html>

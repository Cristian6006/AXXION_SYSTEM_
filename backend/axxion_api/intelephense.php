<?php
// Intelephense configuration file
// This helps with IDE analysis for Laravel framework

// Ensure the Laravel autoloader is loaded
$autoloadPath = __DIR__ . '/vendor/autoload.php';
if (file_exists($autoloadPath)) {
    require_once $autoloadPath;
}

// Provide type hints for Laravel classes
if (!function_exists('app')) {
    function app($abstract = null, array $parameters = []) {
        return null;
    }
}

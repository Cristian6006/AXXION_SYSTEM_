-- =====================================================
-- FIX: Crear tabla auditoria_inventario si no existe
-- =====================================================
-- Este script corrige el error 500 al actualizar productos
-- causado por la falta de la tabla auditoria_inventario
-- =====================================================

USE sistemarenta;

-- Crear tabla de auditoría de inventario si no existe
CREATE TABLE IF NOT EXISTS `auditoria_inventario` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `inventario_item_id` INT,
    `accion` VARCHAR(50),
    `campos_cambiados` TEXT,
    `valores_anteriores` TEXT,
    `valores_nuevos` TEXT,
    `usuario` VARCHAR(100),
    `fecha_cambio` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_inventario_item_id` (`inventario_item_id`),
    INDEX `idx_fecha_cambio` (`fecha_cambio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Verificar que la tabla se creó correctamente
SELECT 'Tabla auditoria_inventario creada exitosamente' AS mensaje;


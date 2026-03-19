USE sistemarenta;

DROP TRIGGER IF EXISTS `trg_inventario_item_after_update`;
DROP TRIGGER IF EXISTS `trg_mantenimiento_after_update`;

DELIMITER //

-- Trigger: Al cambiar item a EnMantenimiento -> Crear registro en mantenimiento
CREATE TRIGGER `trg_inventario_item_after_update` AFTER UPDATE ON `inventario_item` FOR EACH ROW BEGIN
    IF NEW.estado_item = 'EnMantenimiento' AND OLD.estado_item != 'EnMantenimiento' THEN
        INSERT INTO `mantenimiento` (
            `inventario_item_id`,
            `fecha_inicio`,
            `fecha_fin_prevista`,
            `tipo_mantenimiento`,
            `descripcion_problema`,
            `estado_mantenimiento`,
            `responsable`,
            `created_at`,
            `updated_at`
        ) VALUES (
            NEW.id,
            NOW(),
            DATE_ADD(NOW(), INTERVAL 3 DAY),
            'Correctivo',
            'Mantenimiento generado automáticamente por cambio de estado',
            'PROGRAMADO', -- Usar mayúsculas consistente con frontend
            'Sistema',
            NOW(),
            NOW()
        );
    END IF;
END//

-- Trigger: Al finalizar mantenimiento -> Liberar item a Disponible
CREATE TRIGGER `trg_mantenimiento_after_update` AFTER UPDATE ON `mantenimiento` FOR EACH ROW BEGIN
    -- Aceptar tanto 'Finalizado' como 'COMPLETADO' para robustez
    IF (NEW.estado_mantenimiento = 'Finalizado' OR NEW.estado_mantenimiento = 'COMPLETADO') 
       AND (OLD.estado_mantenimiento != 'Finalizado' AND OLD.estado_mantenimiento != 'COMPLETADO') THEN
        UPDATE `inventario_item`
        SET `estado_item` = 'Disponible'
        WHERE `id` = NEW.inventario_item_id;
    END IF;
END//

DELIMITER ;

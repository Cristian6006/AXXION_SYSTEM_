USE sistemarenta;

DROP TRIGGER IF EXISTS `trg_inventario_item_after_update`;

DELIMITER //
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
            'Programado',
            'Sistema',
            NOW(),
            NOW()
        );
    END IF;
END//
DELIMITER ;

USE sistemarenta;

DROP TRIGGER IF EXISTS `trg_mantenimiento_after_update`;

DELIMITER //
CREATE TRIGGER `trg_mantenimiento_after_update` AFTER UPDATE ON `mantenimiento` FOR EACH ROW BEGIN
    -- Si cambia a Finalizado, poner item como Disponible
    IF NEW.estado_mantenimiento = 'Finalizado' AND OLD.estado_mantenimiento != 'Finalizado' THEN
        UPDATE `inventario_item`
        SET `estado_item` = 'Disponible'
        WHERE `id` = NEW.inventario_item_id;
    END IF;
END//
DELIMITER ;

USE sistemarenta;

DROP TRIGGER IF EXISTS `trg_mantenimiento_after_delete`;

DELIMITER //

-- Trigger: Al eliminar mantenimiento -> Liberar item a Disponible
CREATE TRIGGER `trg_mantenimiento_after_delete` AFTER DELETE ON `mantenimiento` FOR EACH ROW BEGIN
    -- Al eliminar un mantenimiento, devolver el item a Disponible
    UPDATE `inventario_item`
    SET `estado_item` = 'Disponible'
    WHERE `id` = OLD.inventario_item_id;
END//

DELIMITER ;

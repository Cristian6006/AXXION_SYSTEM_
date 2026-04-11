DELIMITER //

CREATE TRIGGER `trg_inventario_item_after_update`
AFTER UPDATE ON `inventario_item`
FOR EACH ROW
BEGIN
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
            'CORRECTIVO',
            'Mantenimiento generado automáticamente por cambio de estado',
            'PROGRAMADO',
            'Sistema',
            NOW(),
            NOW()
        );
    END IF;
END//

CREATE TRIGGER `trg_mantenimiento_after_delete`
AFTER DELETE ON `mantenimiento`
FOR EACH ROW
BEGIN
    UPDATE `inventario_item`
    SET `estado_item` = 'Disponible'
    WHERE `id` = OLD.inventario_item_id;
END//

CREATE TRIGGER `trg_mantenimiento_after_update`
AFTER UPDATE ON `mantenimiento`
FOR EACH ROW
BEGIN
    IF (NEW.estado_mantenimiento = 'Finalizado' OR NEW.estado_mantenimiento = 'COMPLETADO') 
       AND (OLD.estado_mantenimiento != 'Finalizado' AND OLD.estado_mantenimiento != 'COMPLETADO') THEN
        UPDATE `inventario_item`
        SET `estado_item` = 'Disponible'
        WHERE `id` = NEW.inventario_item_id;
    END IF;
END//

CREATE TRIGGER `trg_producto_after_insert`
AFTER INSERT ON `producto`
FOR EACH ROW
BEGIN
    INSERT INTO `inventario_item` (
        `producto_id`,
        `numero_serie`,
        `estado_item`,
        `fecha_adquisicion`,
        `costo_adquisicion`,
        `ubicacion_fisica`,
        `notas`,
        `created_at`,
        `updated_at`
    ) VALUES (
        NEW.id,
        NEW.numero_serie,
        CASE 
            WHEN NEW.estado = 'disponible' THEN 'Disponible'
            WHEN NEW.estado = 'rentado' THEN 'Rentado'
            WHEN NEW.estado = 'mantenimiento' THEN 'EnMantenimiento'
            WHEN NEW.estado = 'baja' THEN 'DeBaja'
            ELSE 'Disponible'
        END,
        NEW.fecha_compra,
        NEW.precio_compra,
        NEW.ubicacion,
        CONCAT('Item creado automáticamente desde producto: ', NEW.nombre),
        NOW(),
        NOW()
    );
END//

CREATE TRIGGER `trg_producto_after_update`
AFTER UPDATE ON `producto`
FOR EACH ROW
BEGIN
    DECLARE cambios_detectados TINYINT DEFAULT 0;

    IF OLD.estado != NEW.estado OR 
       OLD.ubicacion != NEW.ubicacion OR 
       OLD.numero_serie != NEW.numero_serie OR
       OLD.precio_compra != NEW.precio_compra OR
       OLD.fecha_compra != NEW.fecha_compra THEN
        SET cambios_detectados = 1;
    END IF;

    IF cambios_detectados THEN
        UPDATE `inventario_item`
        SET 
            `numero_serie` = NEW.numero_serie,
            `estado_item` = CASE 
                WHEN NEW.estado = 'disponible' THEN 'Disponible'
                WHEN NEW.estado = 'rentado' THEN 'Rentado'
                WHEN NEW.estado = 'mantenimiento' THEN 'EnMantenimiento'
                WHEN NEW.estado = 'baja' THEN 'DeBaja'
                ELSE `estado_item`
            END,
            `fecha_adquisicion` = NEW.fecha_compra,
            `costo_adquisicion` = NEW.precio_compra,
            `ubicacion_fisica` = NEW.ubicacion,
            `notas` = CONCAT('Actualizado desde producto: ', NEW.nombre, ' - ', NOW()),
            `updated_at` = NOW()
        WHERE `producto_id` = NEW.id;
    END IF;
END//

CREATE TRIGGER `trg_renta_after_insert`
AFTER INSERT ON `renta`
FOR EACH ROW
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_producto_id INT;
    DECLARE v_precio_renta DECIMAL(10,2);
    DECLARE v_cantidad INT;

    DECLARE cur_productos CURSOR FOR
        SELECT dc.producto_id, dc.cantidad, dc.precio_unitario
        FROM detalle_cotizacion dc
        WHERE dc.cotizacion_id = NEW.cotizacion_id;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    IF NEW.cotizacion_id IS NOT NULL THEN
        OPEN cur_productos;

        read_loop: LOOP
            FETCH cur_productos INTO v_producto_id, v_cantidad, v_precio_renta;
            IF done THEN
                LEAVE read_loop;
            END IF;

            INSERT INTO renta_inventario_item (
                renta_id,
                inventario_item_id,
                precio_renta_item,
                condicion_salida,
                notas
            )
            SELECT 
                NEW.id,
                ii.id,
                v_precio_renta,
                'Buena',
                CONCAT('Asignado automáticamente desde renta ID: ', NEW.id)
            FROM inventario_item ii
            WHERE ii.producto_id = v_producto_id
              AND ii.estado_item = 'Disponible'
            LIMIT v_cantidad;

            UPDATE inventario_item ii
            SET ii.estado_item = 'Rentado',
                ii.updated_at = NOW()
            WHERE ii.id IN (
                SELECT rii.inventario_item_id
                FROM renta_inventario_item rii
                WHERE rii.renta_id = NEW.id
            );

        END LOOP;

        CLOSE cur_productos;
    END IF;
END//

CREATE TRIGGER `trg_renta_after_update`
AFTER UPDATE ON `renta`
FOR EACH ROW
BEGIN
    IF (NEW.estado_renta IN ('Finalizada', 'Cancelada')) AND 
       (OLD.estado_renta NOT IN ('Finalizada', 'Cancelada')) THEN
        
        UPDATE inventario_item ii
        SET ii.estado_item = 'Disponible',
            ii.updated_at = NOW()
        WHERE ii.id IN (
            SELECT rii.inventario_item_id
            FROM renta_inventario_item rii
            WHERE rii.renta_id = NEW.id
        );
    END IF;

    IF (NEW.estado_renta = 'EnCurso') AND (OLD.estado_renta = 'Programada') THEN
        UPDATE inventario_item ii
        SET ii.estado_item = 'Rentado',
            ii.updated_at = NOW()
        WHERE ii.id IN (
            SELECT rii.inventario_item_id
            FROM renta_inventario_item rii
            WHERE rii.renta_id = NEW.id
        );
    END IF;
END//

DELIMITER ;
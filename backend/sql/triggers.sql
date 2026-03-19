-- =====================================================
-- SISTEMA DE RENTAS - TRIGGERS DE BASE DE DATOS
-- =====================================================
-- Este archivo contiene triggers para automatizar
-- procesos y mantener la integridad de datos
-- =====================================================

USE sistemarenta;

-- =====================================================
-- PRIMERO: CREAR TABLAS DE AUDITORÍA
-- =====================================================

-- Tabla para auditar cambios en clientes
CREATE TABLE IF NOT EXISTS auditoria_cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    accion VARCHAR(10),
    campos_cambiados TEXT,
    valores_anteriores TEXT,
    valores_nuevos TEXT,
    usuario VARCHAR(100),
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla para auditar cambios en rentas
CREATE TABLE IF NOT EXISTS auditoria_renta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    renta_id INT,
    accion VARCHAR(10),
    campos_cambiados TEXT,
    valores_anteriores TEXT,
    valores_nuevos TEXT,
    usuario VARCHAR(100),
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla para auditar cambios en inventario
CREATE TABLE IF NOT EXISTS auditoria_inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inventario_item_id INT,
    accion VARCHAR(10),
    campos_cambiados TEXT,
    valores_anteriores TEXT,
    valores_nuevos TEXT,
    usuario VARCHAR(100),
    fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TRIGGERS PARA AUDITORÍA DE CLIENTES
-- =====================================================

-- Trigger para INSERT en cliente
DELIMITER //
CREATE TRIGGER trg_auditoria_cliente_insert
AFTER INSERT ON cliente
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_cliente (cliente_id, accion, campos_cambiados, valores_nuevos, usuario)
    VALUES (NEW.id, 'INSERT', 'Todos', CONCAT('Nombre: ', NEW.nombre, ', Email: ', NEW.correo_electronico), USER());
END//
DELIMITER ;

-- Trigger para UPDATE en cliente
DELIMITER //
CREATE TRIGGER trg_auditoria_cliente_update
AFTER UPDATE ON cliente
FOR EACH ROW
BEGIN
    DECLARE cambios TEXT DEFAULT '';
    DECLARE valores_ant TEXT DEFAULT '';
    DECLARE valores_nue TEXT DEFAULT '';

    IF OLD.nombre != NEW.nombre THEN
        SET cambios = CONCAT(cambios, 'nombre, ');
        SET valores_ant = CONCAT(valores_ant, 'nombre: ', OLD.nombre, '; ');
        SET valores_nue = CONCAT(valores_nue, 'nombre: ', NEW.nombre, '; ');
    END IF;

    IF OLD.correo_electronico != NEW.correo_electronico THEN
        SET cambios = CONCAT(cambios, 'correo_electronico, ');
        SET valores_ant = CONCAT(valores_ant, 'correo: ', OLD.correo_electronico, '; ');
        SET valores_nue = CONCAT(valores_nue, 'correo: ', NEW.correo_electronico, '; ');
    END IF;

    IF OLD.estado_cliente != NEW.estado_cliente THEN
        SET cambios = CONCAT(cambios, 'estado_cliente, ');
        SET valores_ant = CONCAT(valores_ant, 'estado: ', OLD.estado_cliente, '; ');
        SET valores_nue = CONCAT(valores_nue, 'estado: ', NEW.estado_cliente, '; ');
    END IF;

    IF LENGTH(cambios) > 0 THEN
        SET cambios = LEFT(cambios, LENGTH(cambios) - 2);
        INSERT INTO auditoria_cliente (cliente_id, accion, campos_cambiados, valores_anteriores, valores_nuevos, usuario)
        VALUES (NEW.id, 'UPDATE', cambios, valores_ant, valores_nue, USER());
    END IF;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA AUDITORÍA DE RENTAS
-- =====================================================

-- Trigger para INSERT en renta
DELIMITER //
CREATE TRIGGER trg_auditoria_renta_insert
AFTER INSERT ON renta
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_renta (renta_id, accion, campos_cambiados, valores_nuevos, usuario)
    VALUES (NEW.id, 'INSERT', 'Nueva renta', CONCAT('Cliente: ', NEW.cliente_id, ', Monto: ', NEW.monto_total_renta), USER());
END//
DELIMITER ;

-- Trigger para UPDATE en renta
DELIMITER //
CREATE TRIGGER trg_auditoria_renta_update
AFTER UPDATE ON renta
FOR EACH ROW
BEGIN
    DECLARE cambios TEXT DEFAULT '';
    DECLARE valores_ant TEXT DEFAULT '';
    DECLARE valores_nue TEXT DEFAULT '';

    IF OLD.estado_renta != NEW.estado_renta THEN
        SET cambios = CONCAT(cambios, 'estado_renta, ');
        SET valores_ant = CONCAT(valores_ant, 'estado: ', OLD.estado_renta, '; ');
        SET valores_nue = CONCAT(valores_nue, 'estado: ', NEW.estado_renta, '; ');
    END IF;

    IF OLD.fecha_devolucion_real != NEW.fecha_devolucion_real THEN
        SET cambios = CONCAT(cambios, 'fecha_devolucion_real, ');
        SET valores_ant = CONCAT(valores_ant, 'devolucion: ', OLD.fecha_devolucion_real, '; ');
        SET valores_nue = CONCAT(valores_nue, 'devolucion: ', NEW.fecha_devolucion_real, '; ');
    END IF;

    IF LENGTH(cambios) > 0 THEN
        SET cambios = LEFT(cambios, LENGTH(cambios) - 2);
        INSERT INTO auditoria_renta (renta_id, accion, campos_cambiados, valores_anteriores, valores_nuevos, usuario)
        VALUES (NEW.id, 'UPDATE', cambios, valores_ant, valores_nue, USER());
    END IF;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA AUDITORÍA DE INVENTARIO
-- =====================================================

-- Trigger para UPDATE en inventario_item
DELIMITER //
CREATE TRIGGER trg_auditoria_inventario_update
AFTER UPDATE ON inventario_item
FOR EACH ROW
BEGIN
    DECLARE cambios TEXT DEFAULT '';
    DECLARE valores_ant TEXT DEFAULT '';
    DECLARE valores_nue TEXT DEFAULT '';

    IF OLD.estado_item != NEW.estado_item THEN
        SET cambios = CONCAT(cambios, 'estado_item, ');
        SET valores_ant = CONCAT(valores_ant, 'estado: ', OLD.estado_item, '; ');
        SET valores_nue = CONCAT(valores_nue, 'estado: ', NEW.estado_item, '; ');
    END IF;

    IF OLD.ubicacion_fisica != NEW.ubicacion_fisica THEN
        SET cambios = CONCAT(cambios, 'ubicacion_fisica, ');
        SET valores_ant = CONCAT(valores_ant, 'ubicacion: ', OLD.ubicacion_fisica, '; ');
        SET valores_nue = CONCAT(valores_nue, 'ubicacion: ', NEW.ubicacion_fisica, '; ');
    END IF;

    IF LENGTH(cambios) > 0 THEN
        SET cambios = LEFT(cambios, LENGTH(cambios) - 2);
        INSERT INTO auditoria_inventario (inventario_item_id, accion, campos_cambiados, valores_anteriores, valores_nuevos, usuario)
        VALUES (NEW.id, 'UPDATE', cambios, valores_ant, valores_nue, USER());
    END IF;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA VALIDACIÓN Y REGLAS DE NEGOCIO
-- =====================================================

-- Trigger para validar fecha de fin antes de fecha de inicio en rentas
DELIMITER //
CREATE TRIGGER trg_validar_fechas_renta
BEFORE INSERT ON renta
FOR EACH ROW
BEGIN
    IF NEW.fecha_fin_prevista <= NEW.fecha_inicio THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La fecha de fin prevista debe ser posterior a la fecha de inicio';
    END IF;
END//
DELIMITER ;

-- Trigger para validar que no se pueda rentar un item que no esté disponible
DELIMITER //
CREATE TRIGGER trg_validar_disponibilidad_item
BEFORE INSERT ON renta_inventario_item
FOR EACH ROW
BEGIN
    DECLARE estado_actual ENUM('Disponible','Rentado','EnMantenimiento','DeBaja');

    SELECT estado_item INTO estado_actual
    FROM inventario_item
    WHERE id = NEW.inventario_item_id;

    IF estado_actual != 'Disponible' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede rentar un item que no está disponible';
    END IF;
END//
DELIMITER ;

-- Trigger para validar que el cliente esté activo
DELIMITER //
CREATE TRIGGER trg_validar_cliente_activo
BEFORE INSERT ON renta
FOR EACH ROW
BEGIN
    DECLARE estado_cliente_actual ENUM('Activo','Inactivo','Prospecto');

    SELECT estado_cliente INTO estado_cliente_actual
    FROM cliente
    WHERE id = NEW.cliente_id;

    IF estado_cliente_actual = 'Inactivo' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede crear una renta para un cliente inactivo';
    END IF;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA ACTUALIZACIONES AUTOMÁTICAS
-- =====================================================

-- Trigger para actualizar estado de item cuando se crea una renta
DELIMITER //
CREATE TRIGGER trg_actualizar_estado_item_renta
AFTER INSERT ON renta_inventario_item
FOR EACH ROW
BEGIN
    UPDATE inventario_item
    SET estado_item = 'Rentado',
        ubicacion_fisica = CONCAT('Rentado - Renta #', NEW.renta_id),
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.inventario_item_id;
END//
DELIMITER ;

-- Trigger para actualizar estado de item cuando se devuelve
DELIMITER //
CREATE TRIGGER trg_actualizar_estado_item_devolucion
AFTER UPDATE ON renta
FOR EACH ROW
BEGIN
    IF OLD.estado_renta != 'Finalizada' AND NEW.estado_renta = 'Finalizada' THEN
        UPDATE inventario_item
        SET estado_item = 'Disponible',
            ubicacion_fisica = 'Almacén Central',
            updated_at = CURRENT_TIMESTAMP
        WHERE id IN (
            SELECT inventario_item_id
            FROM renta_inventario_item
            WHERE renta_id = NEW.id
        );
    END IF;
END//
DELIMITER ;

-- Trigger para marcar rentas como retrasadas automáticamente
DELIMITER //
CREATE TRIGGER trg_marcar_rentas_retrasadas
AFTER UPDATE ON renta
FOR EACH ROW
BEGIN
    IF NEW.estado_renta = 'EnCurso' AND NEW.fecha_fin_prevista < CURRENT_DATE THEN
        UPDATE renta
        SET estado_renta = 'Retrasada',
            updated_at = CURRENT_TIMESTAMP
        WHERE id = NEW.id;
    END IF;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA CÁLCULOS AUTOMÁTICOS
-- =====================================================

-- Trigger para calcular monto total de cotización automáticamente
DELIMITER //
CREATE TRIGGER trg_calcular_total_cotizacion
AFTER INSERT ON detalle_cotizacion
FOR EACH ROW
BEGIN
    UPDATE cotizacion
    SET monto_total = (
        SELECT SUM(subtotal)
        FROM detalle_cotizacion
        WHERE cotizacion_id = NEW.cotizacion_id
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.cotizacion_id;
END//
DELIMITER ;

-- Trigger para actualizar total de cotización al modificar detalles
DELIMITER //
CREATE TRIGGER trg_actualizar_total_cotizacion
AFTER UPDATE ON detalle_cotizacion
FOR EACH ROW
BEGIN
    UPDATE cotizacion
    SET monto_total = (
        SELECT SUM(subtotal)
        FROM detalle_cotizacion
        WHERE cotizacion_id = NEW.cotizacion_id
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.cotizacion_id;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA MANTENIMIENTO DE INVENTARIO
-- =====================================================

-- Trigger para crear registro de mantenimiento preventivo automático
DELIMITER //
CREATE TRIGGER trg_mantenimiento_preventivo_auto
AFTER UPDATE ON inventario_item
FOR EACH ROW
BEGIN
    IF OLD.estado_item = 'Rentado' AND NEW.estado_item = 'Disponible' THEN
        -- Verificar si han pasado más de 90 días desde el último mantenimiento
        IF NOT EXISTS (
            SELECT 1 FROM mantenimiento
            WHERE inventario_item_id = NEW.id
            AND fecha_fin_real >= DATE_SUB(CURRENT_DATE, INTERVAL 90 DAY)
        ) THEN
            INSERT INTO mantenimiento (
                inventario_item_id,
                tipo_mantenimiento,
                descripcion_problema,
                estado_mantenimiento,
                responsable
            ) VALUES (
                NEW.id,
                'Preventivo',
                'Mantenimiento preventivo programado después de renta',
                'Programado',
                'Sistema Automático'
            );
        END IF;
    END IF;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA ALERTAS Y NOTIFICACIONES
-- =====================================================

-- Tabla para almacenar alertas del sistema
CREATE TABLE IF NOT EXISTS alertas_sistema (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_alerta VARCHAR(50),
    descripcion TEXT,
    tabla_afectada VARCHAR(50),
    registro_id INT,
    severidad ENUM('baja', 'media', 'alta', 'critica'),
    estado ENUM('activa', 'atendida', 'descartada') DEFAULT 'activa',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_atencion TIMESTAMP NULL
);

-- Trigger para alertar sobre rentas próximas a vencer
DELIMITER //
CREATE TRIGGER trg_alerta_rentas_por_vencer
AFTER UPDATE ON renta
FOR EACH ROW
BEGIN
    IF NEW.estado_renta = 'EnCurso' AND NEW.fecha_fin_prevista BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY) THEN
        INSERT INTO alertas_sistema (tipo_alerta, descripcion, tabla_afectada, registro_id, severidad)
        VALUES (
            'Renta por vencer',
            CONCAT('La renta #', NEW.id, ' vence el ', NEW.fecha_fin_prevista),
            'renta',
            NEW.id,
            'media'
        );
    END IF;
END//
DELIMITER ;

-- Trigger para alertar sobre items con mucho tiempo sin mantenimiento
DELIMITER //
CREATE TRIGGER trg_alerta_mantenimiento_pendiente
AFTER INSERT ON mantenimiento
FOR EACH ROW
BEGIN
    -- Si no hay mantenimientos recientes para este item
    IF NOT EXISTS (
        SELECT 1 FROM mantenimiento
        WHERE inventario_item_id = NEW.inventario_item_id
        AND id != NEW.id
        AND fecha_fin_real >= DATE_SUB(CURRENT_DATE, INTERVAL 180 DAY)
    ) THEN
        INSERT INTO alertas_sistema (tipo_alerta, descripcion, tabla_afectada, registro_id, severidad)
        VALUES (
            'Mantenimiento pendiente',
            CONCAT('El item #', NEW.inventario_item_id, ' requiere mantenimiento urgente'),
            'inventario_item',
            NEW.inventario_item_id,
            'alta'
        );
    END IF;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA INTEGRIDAD REFERENCIAL
-- =====================================================

-- Trigger para evitar eliminación de clientes con rentas activas
DELIMITER //
CREATE TRIGGER trg_prevenir_eliminacion_cliente_con_rentas
BEFORE DELETE ON cliente
FOR EACH ROW
BEGIN
    IF EXISTS (SELECT 1 FROM renta WHERE cliente_id = OLD.id AND estado_renta IN ('Programada', 'EnCurso')) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar un cliente con rentas activas';
    END IF;
END//
DELIMITER ;

-- Trigger para evitar eliminación de productos con items en renta
DELIMITER //
CREATE TRIGGER trg_prevenir_eliminacion_producto_con_rentas
BEFORE DELETE ON producto
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1 FROM inventario_item ii
        INNER JOIN renta_inventario_item rii ON ii.id = rii.inventario_item_id
        INNER JOIN renta r ON rii.renta_id = r.id
        WHERE ii.producto_id = OLD.id AND r.estado_renta IN ('Programada', 'EnCurso')
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No se puede eliminar un producto con items en renta activa';
    END IF;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA ESTADÍSTICAS AUTOMÁTICAS
-- =====================================================

-- Tabla para estadísticas diarias
CREATE TABLE IF NOT EXISTS estadisticas_diarias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    rentas_nuevas INT DEFAULT 0,
    rentas_finalizadas INT DEFAULT 0,
    ingresos_totales DECIMAL(12,2) DEFAULT 0,
    clientes_nuevos INT DEFAULT 0,
    UNIQUE KEY unique_fecha (fecha)
);

-- Trigger para actualizar estadísticas diarias al finalizar una renta
DELIMITER //
CREATE TRIGGER trg_actualizar_estadisticas_renta_finalizada
AFTER UPDATE ON renta
FOR EACH ROW
BEGIN
    IF OLD.estado_renta != 'Finalizada' AND NEW.estado_renta = 'Finalizada' THEN
        INSERT INTO estadisticas_diarias (fecha, rentas_finalizadas, ingresos_totales)
        VALUES (CURRENT_DATE, 1, NEW.monto_total_renta)
        ON DUPLICATE KEY UPDATE
            rentas_finalizadas = rentas_finalizadas + 1,
            ingresos_totales = ingresos_totales + NEW.monto_total_renta;
    END IF;
END//
DELIMITER ;

-- Trigger para actualizar estadísticas diarias al crear una renta
DELIMITER //
CREATE TRIGGER trg_actualizar_estadisticas_renta_nueva
AFTER INSERT ON renta
FOR EACH ROW
BEGIN
    INSERT INTO estadisticas_diarias (fecha, rentas_nuevas)
    VALUES (CURRENT_DATE, 1)
    ON DUPLICATE KEY UPDATE
        rentas_nuevas = rentas_nuevas + 1;
END//
DELIMITER ;

-- Trigger para actualizar estadísticas diarias al crear un cliente
DELIMITER //
CREATE TRIGGER trg_actualizar_estadisticas_cliente_nuevo
AFTER INSERT ON cliente
FOR EACH ROW
BEGIN
    INSERT INTO estadisticas_diarias (fecha, clientes_nuevos)
    VALUES (CURRENT_DATE, 1)
    ON DUPLICATE KEY UPDATE
        clientes_nuevos = clientes_nuevos + 1;
END//
DELIMITER ;

-- =====================================================
-- TRIGGERS PARA LIMPIEZA AUTOMÁTICA
-- =====================================================

-- Trigger para limpiar alertas atendidas después de 30 días
DELIMITER //
CREATE TRIGGER trg_limpiar_alertas_antiguas
AFTER UPDATE ON alertas_sistema
FOR EACH ROW
BEGIN
    IF OLD.estado = 'activa' AND NEW.estado = 'atendida' THEN
        -- Programar eliminación automática (esto sería mejor con un evento programado)
        -- Por ahora solo marcamos la fecha de atención
        UPDATE alertas_sistema
        SET fecha_atencion = CURRENT_TIMESTAMP
        WHERE id = NEW.id;
    END IF;
END//
DELIMITER ;

-- =====================================================
-- EVENTO PARA LIMPIEZA AUTOMÁTICA DE ALERTAS
-- =====================================================

-- Crear evento para limpiar alertas atendidas después de 30 días
DELIMITER //
CREATE EVENT IF NOT EXISTS evt_limpiar_alertas_antiguas
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
    DELETE FROM alertas_sistema
    WHERE estado = 'atendida'
    AND fecha_atencion < DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY);
END//
DELIMITER ;

-- Asegurarse de que el programador de eventos esté habilitado
SET GLOBAL event_scheduler = ON;

-- =====================================================
-- TRIGGERS PARA SINCRONIZACIÓN PRODUCTO-INVENTARIO
-- =====================================================

-- Trigger para INSERT en producto - Crear item de inventario automáticamente
DELIMITER $$
CREATE TRIGGER `trg_producto_after_insert` 
AFTER INSERT ON `producto`
FOR EACH ROW
BEGIN
    -- Insertar automáticamente un item en inventario_item por cada producto nuevo
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
END$$
DELIMITER ;

-- Trigger para UPDATE en producto - Sincronizar cambios con inventario_item
DELIMITER $$
CREATE TRIGGER `trg_producto_after_update` 
AFTER UPDATE ON `producto`
FOR EACH ROW
BEGIN
    DECLARE cambios_detectados BOOLEAN DEFAULT FALSE;
    
    -- Verificar si hay cambios que requieren sincronización
    IF OLD.estado != NEW.estado OR 
       OLD.ubicacion != NEW.ubicacion OR 
       OLD.numero_serie != NEW.numero_serie OR
       OLD.precio_compra != NEW.precio_compra OR
       OLD.fecha_compra != NEW.fecha_compra THEN
        SET cambios_detectados = TRUE;
    END IF;
    
    -- Si hay cambios, actualizar el item de inventario correspondiente
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
        
        -- Registrar en auditoría (solo si la tabla existe)
        BEGIN
            DECLARE CONTINUE HANDLER FOR SQLSTATE '42S02' BEGIN END;
            INSERT INTO `auditoria_inventario` (
                `inventario_item_id`, 
                `accion`, 
                `campos_cambiados`, 
                `valores_anteriores`, 
                `valores_nuevos`, 
                `usuario`
            ) VALUES (
                (SELECT id FROM inventario_item WHERE producto_id = NEW.id LIMIT 1),
                'UPDATE_SYNC',
                'Sincronización desde producto',
                CONCAT('Estado: ', OLD.estado, ', Ubicación: ', OLD.ubicacion),
                CONCAT('Estado: ', NEW.estado, ', Ubicación: ', NEW.ubicacion),
                USER()
            );
        END;
    END IF;
END$$
DELIMITER ;

-- Trigger para DELETE en producto - Marcar items de inventario como eliminados
DELIMITER $$
CREATE TRIGGER `trg_producto_after_delete` 
AFTER DELETE ON `producto`
FOR EACH ROW
BEGIN
    -- Marcar items de inventario como eliminados (soft delete)
    UPDATE `inventario_item`
    SET 
        `estado_item` = 'DeBaja',
        `ubicacion_fisica` = 'Producto Eliminado',
        `notas` = CONCAT('Producto eliminado: ', OLD.nombre, ' - ', NOW()),
        `updated_at` = NOW()
    WHERE `producto_id` = OLD.id;
    
    -- Registrar en auditoría (solo si la tabla existe)
    BEGIN
        DECLARE CONTINUE HANDLER FOR SQLSTATE '42S02' BEGIN END;
        INSERT INTO `auditoria_inventario` (
            `inventario_item_id`, 
            `accion`, 
            `campos_cambiados`, 
            `valores_anteriores`, 
            `valores_nuevos`, 
            `usuario`
        ) VALUES (
            (SELECT id FROM inventario_item WHERE producto_id = OLD.id LIMIT 1),
            'DELETE_SYNC',
            'Producto eliminado',
            CONCAT('Producto: ', OLD.nombre),
            'Producto eliminado del sistema',
            USER()
        );
    END;
END$$
DELIMITER ;
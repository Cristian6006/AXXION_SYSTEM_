-- ====================================================================
-- SCRIPT DE PRUEBA PARA BASE DE DATOS: renta_equipo_db
-- Incluye datos de ejemplo y consultas de verificación
-- ====================================================================

-- ====================================================================
-- SECCIÓN 1: INSERTAR DATOS DE PRUEBA
-- ====================================================================

-- 1.1 Insertar direcciones
INSERT INTO direccion (calle, numero_exterior, numero_interior, colonia, ciudad, estado_provincia, codigo_postal, pais, referencias) VALUES
('Av. Insurgentes Sur', '1234', 'Depto 5', 'Roma Norte', 'Ciudad de México', 'CDMX', '06700', 'México', 'Edificio azul, portón negro'),
('Calle Revolución', '567', NULL, 'San Ángel', 'Ciudad de México', 'CDMX', '01000', 'México', 'Casa con jardín frontal'),
('Av. Universidad', '890', 'Local 3', 'Copilco', 'Ciudad de México', 'CDMX', '04360', 'México', 'Plaza comercial'),
('Carrera 7', '123-45', 'Of 201', 'La Candelaria', 'Bogotá', 'Cundinamarca', '110311', 'Colombia', 'Edificio colonial'),
('Calle 93', '456', NULL, 'Chapinero', 'Bogotá', 'Cundinamarca', '110221', 'Colombia', 'Torre empresarial');

-- 1.2 Insertar categorías
INSERT INTO categoria (nombre, descripcion, tipo_categoria) VALUES
('Audio y Video', 'Equipos de sonido, micrófonos, pantallas y proyectores', 'Tecnológico'),
('Iluminación', 'Luces, reflectores y equipos de iluminación profesional', 'Tecnológico'),
('Mobiliario', 'Sillas, mesas, carpas y estructuras para eventos', 'Físico'),
('Catering', 'Equipos de cocina, refrigeración y servicio', 'Servicio'),
('Decoración', 'Elementos decorativos, flores y ambientación', 'Estético');

-- 1.3 Insertar subcategorías
INSERT INTO subcategoria (nombre, descripcion) VALUES
('Microfonía', 'Micrófonos inalámbricos, de mano, corbateros'),
('Proyección', 'Proyectores, pantallas de proyección'),
('Sonido', 'Bocinas, amplificadores, mezcladores'),
('Luces LED', 'Iluminación LED de colores y efectos'),
('Luces Tradicionales', 'Reflectores halógenos y tungsteno'),
('Mesas Redondas', 'Mesas circulares para eventos'),
('Sillas Chiavari', 'Sillas elegantes para eventos'),
('Carpas', 'Estructuras temporales para exteriores'),
('Refrigeración', 'Neveras y equipos de frío'),
('Decoración Floral', 'Arreglos y centros de mesa');

-- 1.4 Insertar productos
INSERT INTO producto (nombre, descripcion, marca, modelo, precio_referencia_renta, sku) VALUES
('Micrófono Inalámbrico', 'Micrófono inalámbrico profesional con receptor', 'Shure', 'SM58', 150.00, 'MIC-SM58-001'),
('Proyector Full HD', 'Proyector 1920x1080 3000 lúmenes', 'Epson', 'EB-X41', 300.00, 'PROJ-EBX41-001'),
('Bocina Activa 15"', 'Bocina autoamplificada 400W', 'JBL', 'PRX815W', 200.00, 'BOC-PRX815-001'),
('Luz LED Par 64', 'Reflector LED RGB 180W', 'Chauvet', 'SlimPAR T12BT', 80.00, 'LED-SLIMPAR-001'),
('Mesa Redonda 1.5m', 'Mesa redonda para 8 personas', 'Generic', 'MR150', 25.00, 'MES-RD150-001'),
('Silla Chiavari Dorada', 'Silla elegante color dorado', 'Chiavari', 'CH-GOLD', 8.00, 'SIL-CHGOLD-001'),
('Carpa 6x6 metros', 'Carpa blanca para exteriores', 'Eureka', 'E66W', 120.00, 'CAR-E66W-001'),
('Nevera Exhibidora', 'Refrigerador vertical 400L', 'Imbera', 'VR-08', 180.00, 'NEV-VR08-001'),
('Centro de Mesa Premium', 'Arreglo floral con rosas y lirios', 'FloralArt', 'CMP-001', 45.00, 'DEC-CMP001-001'),
('Pantalla de Proyección', 'Pantalla retráctil 2x1.5 metros', 'Da-Lite', 'DL-150', 50.00, 'PAN-DL150-001');

-- 1.5 Insertar clientes
INSERT INTO cliente (nombre, rfc, telefono_principal, correo_electronico, tipo_cliente, estado_cliente) VALUES
('Empresas Globales S.A.', 'EGL891203AB1', '+52-55-1234-5678', 'contacto@empresasglobales.com', 'Empresa', 'Activo'),
('María González López', 'GOLM850615MH2', '+52-55-9876-5432', 'maria.gonzalez@email.com', 'Particular', 'Activo'),
('Hotel Royal Plaza', 'HRP920308CD3', '+52-55-5555-1234', 'eventos@hotelroyal.com', 'Empresa', 'Activo'),
('Carlos Mendoza Torres', 'METC780923EF4', '+57-1-234-5678', 'carlos.mendoza@email.com', 'Particular', 'Prospecto'),
('Eventos Corporativos Ltda', 'ECL030415GH5', '+57-1-987-6543', 'info@eventoscorp.com', 'Empresa', 'Activo');

-- 1.6 Insertar proveedores
INSERT INTO proveedor (nombre_empresa, rfc, nombre_contacto, telefono_contacto, correo_contacto, estado_proveedor) VALUES
('Audio Pro Rental', 'APR850612IJ6', 'Juan Pérez', '+52-55-1111-2222', 'juan@audiopro.com', 'Activo'),
('Luces y Efectos SA', 'LEF920715KL7', 'Ana Rodríguez', '+52-55-3333-4444', 'ana@lucesyefectos.com', 'Activo'),
('Mobiliario Elite', 'MEL880923MN8', 'Pedro Sánchez', '+57-1-555-6666', 'pedro@mobiliarioelite.com', 'Activo'),
('Catering Solutions', 'CSL910304OP9', 'Laura Martínez', '+57-1-777-8888', 'laura@cateringsol.com', 'Inactivo'),
('Decoraciones Finas', 'DFI860108QR0', 'Miguel Torres', '+52-55-9999-0000', 'miguel@decoracionesfinas.com', 'Activo');

-- 1.7 Insertar mantenimientos
INSERT INTO mantenimiento (fecha_inicio, fecha_fin_prevista, tipo_mantenimiento, descripcion_problema, estado_mantenimiento, responsable) VALUES
('2024-01-15 09:00:00', '2024-01-15', 'Preventivo', 'Calibración y limpieza general', 'Finalizado', 'Técnico González'),
('2024-02-20 14:00:00', '2024-02-21', 'Correctivo', 'Reparación de conexión XLR', 'Finalizado', 'Técnico López'),
('2024-03-10 10:00:00', '2024-03-12', 'Preventivo', 'Revisión de componentes internos', 'EnProceso', 'Técnico Martínez'),
('2024-03-25 08:00:00', '2024-03-26', 'Correctivo', 'Sustitución de lámpara', 'Programado', 'Técnico Rodríguez'),
('2024-04-05 11:00:00', '2024-04-06', 'Mejora', 'Actualización de firmware', 'Programado', 'Técnico Silva');

-- 1.8 Insertar roles
INSERT INTO rol (codigo, nombre, descripcion) VALUES
('ADMIN', 'Administrador', 'Acceso total al sistema'),
('VENTAS', 'Vendedor', 'Gestión de clientes y cotizaciones'),
('INVENTARIO', 'Encargado de Inventario', 'Control de productos y equipos'),
('TECNICO', 'Técnico', 'Mantenimiento y soporte técnico'),
('GERENTE', 'Gerente', 'Supervisión y reportes ejecutivos');

-- 1.9 Insertar usuarios
INSERT INTO usuario (nombre_usuario, password_hash, nombre_completo, email, telefono, departamento, estado) VALUES
('admin', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrador del Sistema', 'admin@rentaequipos.com', '+52-55-1000-0001', 'Sistemas', 'Activo'),
('jperez', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Juan Pérez Vendedor', 'juan.perez@rentaequipos.com', '+52-55-1000-0002', 'Ventas', 'Activo'),
('mgonzalez', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'María González Inventario', 'maria.gonzalez@rentaequipos.com', '+52-55-1000-0003', 'Inventario', 'Activo'),
('ctecnico', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Carlos Técnico López', 'carlos.tecnico@rentaequipos.com', '+52-55-1000-0004', 'Técnico', 'Activo'),
('agerente', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Ana Gerente Silva', 'ana.gerente@rentaequipos.com', '+52-55-1000-0005', 'Gerencia', 'Inactivo');

-- 1.10 Insertar items de inventario
INSERT INTO inventario_item (producto_id, numero_serie, estado_item, fecha_adquisicion, costo_adquisicion, ubicacion_fisica, notas) VALUES
(1, 'SH-SM58-001', 'Disponible', '2023-01-15', 2500.00, 'Bodega A-1', 'Micrófono en excelente estado'),
(1, 'SH-SM58-002', 'Rentado', '2023-01-15', 2500.00, 'Bodega A-1', 'Micrófono con estuche original'),
(2, 'EP-EBX41-001', 'Disponible', '2023-02-20', 8000.00, 'Bodega B-2', 'Proyector con cables incluidos'),
(3, 'JB-PRX815-001', 'EnMantenimiento', '2023-03-10', 12000.00, 'Taller', 'Requiere revisión de componentes'),
(4, 'CH-SLIMPAR-001', 'Disponible', '2023-04-05', 3500.00, 'Bodega C-3', 'Luz LED con control remoto'),
(5, 'GE-MR150-001', 'Disponible', '2023-01-10', 800.00, 'Bodega D-4', 'Mesa con mantel incluido'),
(6, 'CH-CHGOLD-001', 'Disponible', '2023-02-15', 150.00, 'Bodega D-4', 'Silla con cojín'),
(7, 'EU-E66W-001', 'Rentado', '2023-03-20', 5000.00, 'Bodega E-5', 'Carpa con anclajes'),
(8, 'IM-VR08-001', 'DeBaja', '2022-12-01', 15000.00, 'Almacén', 'Equipo obsoleto para reparación'),
(9, 'FL-CMP001-001', 'Disponible', '2024-01-05', 200.00, 'Refrigerador F-6', 'Arreglo temporal, requiere cuidado especial');

-- ====================================================================
-- SECCIÓN 2: POBLAR TABLAS DE RELACIÓN
-- ====================================================================

-- 2.1 Relaciones categoria-subcategoria
INSERT INTO categoria_subcategoria (categoria_id, subcategoria_id) VALUES
(1, 1), (1, 2), (1, 3),  -- Audio y Video
(2, 4), (2, 5),          -- Iluminación
(3, 6), (3, 7), (3, 8),  -- Mobiliario
(4, 9),                  -- Catering
(5, 10);                 -- Decoración

-- 2.2 Relaciones producto-subcategoria
INSERT INTO producto_subcategoria (producto_id, subcategoria_id) VALUES
(1, 1),   -- Micrófono -> Microfonía
(2, 2),   -- Proyector -> Proyección
(3, 3),   -- Bocina -> Sonido
(4, 4),   -- Luz LED -> Luces LED
(5, 6),   -- Mesa -> Mesas Redondas
(6, 7),   -- Silla -> Sillas Chiavari
(7, 8),   -- Carpa -> Carpas
(8, 9),   -- Nevera -> Refrigeración
(9, 10),  -- Centro de Mesa -> Decoración Floral
(10, 2);  -- Pantalla -> Proyección

-- 2.3 Relaciones cliente-direccion
INSERT INTO cliente_direccion (cliente_id, direccion_id, es_principal) VALUES
(1, 1, 1), (1, 3, 0),  -- Empresas Globales con 2 direcciones
(2, 2, 1),             -- María González
(3, 1, 1),             -- Hotel Royal Plaza
(4, 4, 1),             -- Carlos Mendoza
(5, 5, 1);             -- Eventos Corporativos

-- 2.4 Relaciones usuario-rol
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES
(1, 1),  -- admin -> ADMIN
(2, 2),  -- jperez -> VENTAS
(3, 3),  -- mgonzalez -> INVENTARIO
(4, 4),  -- ctecnico -> TECNICO
(5, 5);  -- agerente -> GERENTE

-- 2.5 Relaciones inventario_item-mantenimiento
INSERT INTO inventario_item_mantenimiento (inventario_item_id, mantenimiento_id) VALUES
(1, 1),  -- Micrófono 1 -> Mantenimiento preventivo
(3, 2),  -- Proyector -> Reparación correctiva
(4, 3),  -- Bocina -> Revisión en proceso
(2, 4),  -- Micrófono 2 -> Programado
(5, 5);  -- Luz LED -> Mejora programada

-- ====================================================================
-- SECCIÓN 3: CREAR PROCESO DE NEGOCIO COMPLETO
-- ====================================================================

-- 3.1 Crear solicitudes de clientes
INSERT INTO solicitud (cliente_id, fecha_solicitud, nombre_producto_alternativo, cantidad_solicitada, descripcion_necesidad, estado_solicitud) VALUES
(1, '2024-03-01 10:00:00', 'Sistema de sonido completo', 1, 'Evento corporativo para 200 personas', 'Atendida'),
(2, '2024-03-05 14:30:00', 'Iluminación para boda', 3, 'Boda en jardín, necesito luces cálidas', 'EnProceso'),
(3, '2024-03-10 09:15:00', NULL, 50, 'Mesas y sillas para evento de hotel', 'Nueva'),
(4, '2024-03-12 16:45:00', 'Proyector y pantalla', 1, 'Presentación empresarial', 'EnProceso'),
(5, '2024-03-15 11:20:00', 'Decoración completa', 1, 'Evento de fin de año corporativo', 'Nueva');

-- 3.2 Crear cotizaciones basadas en solicitudes
INSERT INTO cotizacion (cliente_id, proveedor_id, solicitud_id, fecha_cotizacion, fecha_validez, monto_total, estado_cotizacion, terminos_condiciones, notas_internas) VALUES
(1, 1, 1, '2024-03-02 09:00:00', '2024-03-16', 1200.00, 'Aceptada', 'Pago 50% anticipo, 50% contra entrega. Incluye transporte.', 'Cliente frecuente, aplicar descuento 5%'),
(2, 2, 2, '2024-03-06 11:00:00', '2024-03-20', 450.00, 'Enviada', 'Pago contra entrega. No incluye instalación.', 'Primera cotización para cliente nuevo'),
(3, 3, 3, '2024-03-11 15:00:00', '2024-03-25', 2100.00, 'Borrador', 'Términos por definir', 'Cotización grande, revisar disponibilidad'),
(4, 1, 4, '2024-03-13 08:30:00', '2024-03-27', 380.00, 'Enviada', 'Pago anticipado requerido', 'Cliente de Colombia, confirmar logística'),
(1, 2, NULL, '2024-03-16 10:00:00', '2024-03-30', 850.00, 'Borrador', NULL, 'Cotización adicional sin solicitud previa');

-- 3.3 Crear detalles de cotizaciones
INSERT INTO detalle_cotizacion (cotizacion_id, producto_id, descripcion_item, cantidad, precio_unitario, descuento_porcentaje, impuestos_aplicables, notas) VALUES
-- Cotización 1 (Sistema de sonido completo)
(1, 1, 'Micrófono inalámbrico profesional', 2, 150.00, 5.00, 48.00, 'Incluye receptor y baterías'),
(1, 3, 'Bocina activa para evento', 2, 200.00, 5.00, 64.00, 'Con trípodes incluidos'),
(1, 10, 'Pantalla de proyección', 1, 50.00, 0.00, 8.00, 'Pantalla retráctil'),

-- Cotización 2 (Iluminación para boda)
(2, 4, 'Luz LED Par 64 RGB', 3, 80.00, 0.00, 38.40, 'Con controlador DMX'),
(2, 4, 'Servicios de instalación', 1, 150.00, 0.00, 24.00, 'Técnico especializado'),

-- Cotización 3 (Mesas y sillas para hotel)
(3, 5, 'Mesa redonda 1.5m', 15, 25.00, 10.00, 52.50, 'Con manteles blancos'),
(3, 6, 'Silla Chiavari dorada', 50, 8.00, 10.00, 56.00, 'Incluye cojines'),
(3, 7, 'Carpa 6x6 metros', 2, 120.00, 0.00, 38.40, 'Para área externa'),

-- Cotización 4 (Proyector y pantalla)
(4, 2, 'Proyector Full HD', 1, 300.00, 0.00, 48.00, 'Con cables VGA y HDMI'),
(4, 10, 'Pantalla de proyección', 1, 50.00, 0.00, 8.00, 'Trípode incluido'),

-- Cotización 5 (Cotización adicional)
(5, 9, 'Centro de Mesa Premium', 5, 45.00, 0.00, 36.00, 'Arreglos personalizados'),
(5, 4, 'Iluminación ambiental LED', 4, 80.00, 0.00, 51.20, 'Luces de colores cálidos');

-- 3.4 Crear rentas basadas en cotizaciones aceptadas
INSERT INTO renta (cliente_id, cotizacion_id, fecha_inicio, fecha_fin_prevista, estado_renta, monto_total_renta, deposito_garantia, notas) VALUES
(1, 1, '2024-03-20 08:00:00', '2024-03-22 18:00:00', 'Finalizada', 1200.00, 500.00, 'Evento exitoso, equipo devuelto en perfecto estado'),
(2, 2, '2024-03-25 15:00:00', '2024-03-26 02:00:00', 'EnCurso', 450.00, 200.00, 'Boda en progreso, monitoreo continuo'),
(4, 4, '2024-03-28 09:00:00', '2024-03-28 17:00:00', 'Programada', 380.00, 150.00, 'Presentación de un día, cliente de Colombia');

-- 3.5 Crear detalles de renta
INSERT INTO detalle_renta (renta_id, precio_renta_item, condicion_salida, condicion_regreso, notas) VALUES
(1, 570.00, 'Equipos en perfecto estado, calibrados', 'Equipos devueltos limpios y funcionales', 'Sin incidencias'),
(1, 630.00, 'Bocinas y pantalla verificadas', 'Todo en orden', 'Cliente satisfecho'),
(2, 450.00, 'Luces LED verificadas y programadas', NULL, 'Evento en curso'),
(3, 380.00, 'Proyector y pantalla listos', NULL, 'Pendiente de entrega');

-- 3.6 Asignar items específicos a detalles de renta
INSERT INTO detalle_renta_inventario_item (detalle_renta_id, inventario_item_id) VALUES
(1, 1), (1, 2),  -- Micrófonos para renta 1
(2, 3), (2, 10), -- Proyector y pantalla para renta 1
(3, 4), (3, 5),  -- Luces LED para renta 2
(4, 3), (4, 10); -- Proyector y pantalla para renta 3

-- 3.7 Crear entregas
INSERT INTO entrega (renta_id, fecha_envio, compania_envio, numero_guia, estado_entrega, notas) VALUES
(1, '2024-03-19 14:00:00', 'LogiRental Express', 'LRE-2024-001', 'Entregada', 'Entrega exitosa, cliente firmó recibido'),
(2, '2024-03-24 12:00:00', 'LogiRental Express', 'LRE-2024-002', 'Entregada', 'Entregado en venue de la boda'),
(3, '2024-03-27 10:00:00', 'International Shipping', 'IS-COL-003', 'EnTransito', 'Envío internacional a Colombia');

-- 3.8 Asignar direcciones a entregas
INSERT INTO entrega_direccion (entrega_id, direccion_id) VALUES
(1, 1),  -- Entrega 1 a dirección de Empresas Globales
(2, 2),  -- Entrega 2 a dirección de María González
(3, 4);  -- Entrega 3 a dirección de Carlos Mendoza

-- 3.9 Asignar items a entregas
INSERT INTO entrega_inventario_item (entrega_id, inventario_item_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 10),  -- Items para entrega 1
(2, 4), (2, 5),                   -- Items para entrega 2
(3, 3), (3, 10);                  -- Items para entrega 3

-- 3.10 Crear devoluciones
INSERT INTO devolucion (renta_id, fecha_devolucion_programada, fecha_devolucion_real, estado_devolucion, persona_recibe, notas_generales) VALUES
(1, '2024-03-22', '2024-03-22 19:30:00', 'Completa', 'María González - Inventario', 'Todos los equipos devueltos en perfecto estado'),
(2, '2024-03-26', NULL, 'Pendiente', NULL, 'Devolución programada post-evento'),
(3, '2024-03-28', NULL, 'Pendiente', NULL, 'Devolución pendiente - evento programado');

-- 3.11 Asignar items a devoluciones
INSERT INTO devolucion_inventario_item (devolucion_id, inventario_item_id, condicion_al_regresar, requiere_mantenimiento) VALUES
(1, 1, 'Excelente estado, sin daños', 0),
(1, 2, 'Excelente estado, sin daños', 0),
(1, 3, 'Buen estado, requiere limpieza menor', 0),
(1, 10, 'Perfecto estado', 0);

-- ====================================================================
-- SECCIÓN 4: CONSULTAS DE VERIFICACIÓN Y PRUEBA
-- ====================================================================

-- 4.1 VERIFICAR ESTRUCTURA DE DATOS
SELECT '=== RESUMEN DE DATOS INSERTADOS ===' as INFO;

SELECT 'Direcciones' as Tabla, COUNT(*) as Total FROM direccion
UNION ALL
SELECT 'Categorías', COUNT(*) FROM categoria
UNION ALL
SELECT 'Subcategorías', COUNT(*) FROM subcategoria
UNION ALL
SELECT 'Productos', COUNT(*) FROM producto
UNION ALL
SELECT 'Clientes', COUNT(*) FROM cliente
UNION ALL
SELECT 'Proveedores', COUNT(*) FROM proveedor
UNION ALL
SELECT 'Items Inventario', COUNT(*) FROM inventario_item
UNION ALL
SELECT 'Usuarios', COUNT(*) FROM usuario
UNION ALL
SELECT 'Roles', COUNT(*) FROM rol
UNION ALL
SELECT 'Solicitudes', COUNT(*) FROM solicitud
UNION ALL
SELECT 'Cotizaciones', COUNT(*) FROM cotizacion
UNION ALL
SELECT 'Detalles Cotización', COUNT(*) FROM detalle_cotizacion
UNION ALL
SELECT 'Rentas', COUNT(*) FROM renta
UNION ALL
SELECT 'Detalles Renta', COUNT(*) FROM detalle_renta
UNION ALL
SELECT 'Entregas', COUNT(*) FROM entrega
UNION ALL
SELECT 'Devoluciones', COUNT(*) FROM devolucion;

-- 4.2 CONSULTAS DE PRUEBA FUNCIONAL

-- Consulta 1: Productos más rentados
SELECT '=== PRODUCTOS MÁS RENTADOS ===' as INFO;
SELECT 
    p.nombre as Producto,
    p.marca,
    p.modelo,
    COUNT(drii.inventario_item_id) as Veces_Rentado,
    AVG(dr.precio_renta_item) as Precio_Promedio_Renta
FROM producto p
LEFT JOIN inventario_item ii ON p.id = ii.producto_id
LEFT JOIN detalle_renta_inventario_item drii ON ii.id = drii.inventario_item_id
LEFT JOIN detalle_renta dr ON drii.detalle_renta_id = dr.id
WHERE ii.id IS NOT NULL
GROUP BY p.id, p.nombre, p.marca, p.modelo
ORDER BY Veces_Rentado DESC;

-- Consulta 2: Estado actual del inventario
SELECT '=== ESTADO DEL INVENTARIO ===' as INFO;
SELECT 
    estado_item as Estado,
    COUNT(*) as Cantidad_Items,
    SUM(costo_adquisicion) as Valor_Total
FROM inventario_item
GROUP BY estado_item
ORDER BY Cantidad_Items DESC;

-- Consulta 3: Clientes más activos
SELECT '=== CLIENTES MÁS ACTIVOS ===' as INFO;
SELECT 
    c.nombre as Cliente,
    c.tipo_cliente,
    c.estado_cliente,
    COUNT(DISTINCT r.id) as Total_Rentas,
    COUNT(DISTINCT cot.id) as Total_Cotizaciones,
    COALESCE(SUM(r.monto_total_renta), 0) as Ingresos_Generados
FROM cliente c
LEFT JOIN renta r ON c.id = r.cliente_id
LEFT JOIN cotizacion cot ON c.id = cot.cliente_id
GROUP BY c.id, c.nombre, c.tipo_cliente, c.estado_cliente
ORDER BY Ingresos_Generados DESC;

-- Consulta 4: Rentabilidad por categoría
SELECT '=== RENTABILIDAD POR CATEGORÍA ===' as INFO;
SELECT 
    cat.nombre as Categoria,
    COUNT(DISTINCT p.id) as Productos_Disponibles,
    COUNT(DISTINCT ii.id) as Items_Inventario,
    COUNT(DISTINCT r.id) as Rentas_Realizadas,
    COALESCE(SUM(r.monto_total_renta), 0) as Ingresos_Total,
    COALESCE(AVG(r.monto_total_renta), 0) as Ingreso_Promedio_Renta
FROM categoria cat
LEFT JOIN categoria_subcategoria cs ON cat.id = cs.categoria_id
LEFT JOIN producto_subcategoria ps ON cs.subcategoria_id = ps.subcategoria_id
LEFT JOIN producto p ON ps.producto_id = p.id
LEFT JOIN inventario_item ii ON p.id = ii.producto_id
LEFT JOIN detalle_renta_inventario_item drii ON ii.id = drii.inventario_item_id
LEFT JOIN detalle_renta dr ON drii.detalle_renta_id = dr.id
LEFT JOIN renta r ON dr.renta_id = r.id
WHERE r.estado_renta IN ('Finalizada', 'EnCurso')
GROUP BY cat.id, cat.nombre
ORDER BY Ingresos_Total DESC;

-- Consulta 5: Proveedores y su actividad
SELECT '=== ACTIVIDAD DE PROVEEDORES ===' as INFO;
SELECT 
    pr.nombre_empresa as Proveedor,
    pr.nombre_contacto,
    pr.estado_proveedor,
    COUNT(DISTINCT cot.id) as Cotizaciones_Generadas,
    COUNT(DISTINCT CASE WHEN cot.estado_cotizacion = 'Aceptada' THEN cot.id END) as Cotizaciones_Aceptadas,
    COALESCE(SUM(CASE WHEN cot.estado_cotizacion = 'Aceptada' THEN cot.monto_total END), 0) as Monto_Total_Aceptado
FROM proveedor pr
LEFT JOIN cotizacion cot ON pr.id = cot.proveedor_id
GROUP BY pr.id, pr.nombre_empresa, pr.nombre_contacto, pr.estado_proveedor
ORDER BY Cotizaciones_Aceptadas DESC, Monto_Total_Aceptado DESC;

-- Consulta 6: Seguimiento de mantenimientos
SELECT '=== SEGUIMIENTO DE MANTENIMIENTOS ===' as INFO;
SELECT 
    m.id as Mantenimiento_ID,
    m.tipo_mantenimiento,
    m.estado_mantenimiento,
    m.fecha_inicio,
    m.fecha_fin_prevista,
    m.responsable,
    COUNT(iim.inventario_item_id) as Items_Afectados,
    m.costo_estimado,
    m.costo_real,
    CASE 
        WHEN m.fecha_fin_real IS NULL AND m.fecha_fin_prevista < CURRENT_DATE 
        THEN 'ATRASADO'
        WHEN m.estado_mantenimiento = 'Finalizado' 
        THEN 'COMPLETADO'
        ELSE 'EN TIEMPO'
    END as Estado_Tiempo
FROM mantenimiento m
LEFT JOIN inventario_item_mantenimiento iim ON m.id = iim.mantenimiento_id
GROUP BY m.id
ORDER BY m.fecha_inicio DESC;

-- Consulta 7: Items que requieren atención
SELECT '=== ITEMS QUE REQUIEREN ATENCIÓN ===' as INFO;
SELECT 
    ii.id as Item_ID,
    p.nombre as Producto,
    ii.numero_serie,
    ii.estado_item,
    ii.ubicacion_fisica,
    CASE 
        WHEN ii.estado_item = 'EnMantenimiento' THEN 'En mantenimiento'
        WHEN ii.estado_item = 'DeBaja' THEN 'Requiere evaluación'
        WHEN dii.requiere_mantenimiento = 1 THEN 'Requiere mantenimiento post-devolución'
        ELSE 'Sin problemas'
    END as Accion_Requerida,
    ii.notas
FROM inventario_item ii
LEFT JOIN producto p ON ii.producto_id = p.id
LEFT JOIN devolucion_inventario_item dii ON ii.id = dii.inventario_item_id
WHERE ii.estado_item IN ('EnMantenimiento', 'DeBaja') 
   OR dii.requiere_mantenimiento = 1
GROUP BY ii.id
ORDER BY ii.estado_item, p.nombre;

-- Consulta 8: Análisis de entregas y logística
SELECT '=== ANÁLISIS DE ENTREGAS Y LOGÍSTICA ===' as INFO;
SELECT 
    e.estado_entrega,
    COUNT(*) as Total_Entregas,
    e.compania_envio,
    COUNT(DISTINCT e.renta_id) as Rentas_Afectadas,
    AVG(DATEDIFF(r.fecha_inicio, e.fecha_envio)) as Dias_Anticipacion_Promedio
FROM entrega e
LEFT JOIN renta r ON e.renta_id = r.id
GROUP BY e.estado_entrega, e.compania_envio
ORDER BY Total_Entregas DESC;

-- Consulta 9: Flujo de cotizaciones y conversión
SELECT '=== ANÁLISIS DE CONVERSIÓN DE COTIZACIONES ===' as INFO;
SELECT 
    estado_cotizacion as Estado,
    COUNT(*) as Total_Cotizaciones,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM cotizacion), 2) as Porcentaje,
    COALESCE(SUM(monto_total), 0) as Monto_Total,
    COALESCE(AVG(monto_total), 0) as Monto_Promedio
FROM cotizacion
GROUP BY estado_cotizacion
ORDER BY 
    CASE estado_cotizacion
        WHEN 'Aceptada' THEN 1
        WHEN 'Enviada' THEN 2
        WHEN 'Borrador' THEN 3
        WHEN 'Rechazada' THEN 4
        WHEN 'Vencida' THEN 5
        ELSE 6
    END;

-- Consulta 10: Análisis temporal de rentas
SELECT '=== ANÁLISIS TEMPORAL DE RENTAS ===' as INFO;
SELECT 
    DATE_FORMAT(r.fecha_inicio, '%Y-%m') as Mes,
    COUNT(*) as Total_Rentas,
    COUNT(CASE WHEN r.estado_renta = 'Finalizada' THEN 1 END) as Rentas_Finalizadas,
    COUNT(CASE WHEN r.estado_renta = 'EnCurso' THEN 1 END) as Rentas_EnCurso,
    COUNT(CASE WHEN r.estado_renta = 'Retrasada' THEN 1 END) as Rentas_Retrasadas,
    COALESCE(SUM(r.monto_total_renta), 0) as Ingresos_Mes,
    COALESCE(AVG(r.monto_total_renta), 0) as Ingreso_Promedio
FROM renta r
GROUP BY DATE_FORMAT(r.fecha_inicio, '%Y-%m')
ORDER BY Mes DESC;

-- ====================================================================
-- SECCIÓN 5: CONSULTAS AVANZADAS DE ANÁLISIS DE NEGOCIO
-- ====================================================================

-- Consulta 11: Productos con mejor ROI (Return on Investment)
SELECT '=== ANÁLISIS ROI DE PRODUCTOS ===' as INFO;
SELECT 
    p.nombre as Producto,
    p.marca,
    COUNT(DISTINCT ii.id) as Items_Disponibles,
    COALESCE(SUM(ii.costo_adquisicion), 0) as Inversion_Total,
    COUNT(DISTINCT dr.id) as Veces_Rentado,
    COALESCE(SUM(dr.precio_renta_item), 0) as Ingresos_Generados,
    CASE 
        WHEN SUM(ii.costo_adquisicion) > 0 THEN
            ROUND((COALESCE(SUM(dr.precio_renta_item), 0) / SUM(ii.costo_adquisicion)) * 100, 2)
        ELSE 0
    END as ROI_Porcentaje,
    CASE 
        WHEN COUNT(DISTINCT dr.id) > 0 THEN
            ROUND(COALESCE(SUM(dr.precio_renta_item), 0) / COUNT(DISTINCT dr.id), 2)
        ELSE 0
    END as Ingreso_Promedio_Por_Renta
FROM producto p
LEFT JOIN inventario_item ii ON p.id = ii.producto_id
LEFT JOIN detalle_renta_inventario_item drii ON ii.id = drii.inventario_item_id
LEFT JOIN detalle_renta dr ON drii.detalle_renta_id = dr.id
LEFT JOIN renta r ON dr.renta_id = r.id AND r.estado_renta = 'Finalizada'
WHERE ii.id IS NOT NULL
GROUP BY p.id, p.nombre, p.marca
HAVING Inversion_Total > 0
ORDER BY ROI_Porcentaje DESC;

-- Consulta 12: Análisis de satisfacción del cliente (basado en devoluciones)
SELECT '=== ANÁLISIS DE SATISFACCIÓN DEL CLIENTE ===' as INFO;
SELECT 
    c.nombre as Cliente,
    c.tipo_cliente,
    COUNT(DISTINCT r.id) as Total_Rentas,
    COUNT(DISTINCT d.id) as Total_Devoluciones,
    COUNT(CASE WHEN d.estado_devolucion = 'Completa' THEN 1 END) as Devoluciones_Completas,
    COUNT(CASE WHEN d.estado_devolucion = 'IncompletaConProblemas' THEN 1 END) as Devoluciones_Problematicas,
    CASE 
        WHEN COUNT(DISTINCT d.id) > 0 THEN
            ROUND((COUNT(CASE WHEN d.estado_devolucion = 'Completa' THEN 1 END) * 100.0) / COUNT(DISTINCT d.id), 2)
        ELSE 100.0
    END as Porcentaje_Satisfaccion,
    COALESCE(SUM(r.monto_total_renta), 0) as Valor_Total_Cliente
FROM cliente c
LEFT JOIN renta r ON c.id = r.cliente_id
LEFT JOIN devolucion d ON r.id = d.renta_id
GROUP BY c.id, c.nombre, c.tipo_cliente
HAVING Total_Rentas > 0
ORDER BY Valor_Total_Cliente DESC, Porcentaje_Satisfaccion DESC;

-- Consulta 13: Predicción de mantenimiento preventivo
SELECT '=== ITEMS QUE REQUIEREN MANTENIMIENTO PREVENTIVO ===' as INFO;
SELECT 
    ii.id as Item_ID,
    p.nombre as Producto,
    ii.numero_serie,
    ii.fecha_adquisicion,
    DATEDIFF(CURRENT_DATE, ii.fecha_adquisicion) as Dias_Desde_Adquisicion,
    COUNT(DISTINCT drii.detalle_renta_id) as Veces_Usado,
    ii.estado_item,
    CASE 
        WHEN DATEDIFF(CURRENT_DATE, ii.fecha_adquisicion) > 365 AND COUNT(DISTINCT drii.detalle_renta_id) > 5 
        THEN 'ALTO - Requiere mantenimiento inmediato'
        WHEN DATEDIFF(CURRENT_DATE, ii.fecha_adquisicion) > 180 AND COUNT(DISTINCT drii.detalle_renta_id) > 3 
        THEN 'MEDIO - Programar mantenimiento'
        WHEN COUNT(DISTINCT drii.detalle_renta_id) > 8 
        THEN 'MEDIO - Alto uso, revisar estado'
        ELSE 'BAJO - Sin necesidad inmediata'
    END as Prioridad_Mantenimiento,
    ii.notas
FROM inventario_item ii
LEFT JOIN producto p ON ii.producto_id = p.id
LEFT JOIN detalle_renta_inventario_item drii ON ii.id = drii.inventario_item_id
WHERE ii.estado_item = 'Disponible'
GROUP BY ii.id, p.nombre, ii.numero_serie, ii.fecha_adquisicion, ii.estado_item, ii.notas
ORDER BY 
    CASE 
        WHEN DATEDIFF(CURRENT_DATE, ii.fecha_adquisicion) > 365 AND COUNT(DISTINCT drii.detalle_renta_id) > 5 THEN 1
        WHEN DATEDIFF(CURRENT_DATE, ii.fecha_adquisicion) > 180 AND COUNT(DISTINCT drii.detalle_renta_id) > 3 THEN 2
        WHEN COUNT(DISTINCT drii.detalle_renta_id) > 8 THEN 3
        ELSE 4
    END,
    Dias_Desde_Adquisicion DESC;

-- ====================================================================
-- SECCIÓN 6: CONSULTAS DE INTEGRIDAD Y VERIFICACIÓN
-- ====================================================================

-- Verificación 1: Consistencia de estados
SELECT '=== VERIFICACIÓN DE CONSISTENCIA DE ESTADOS ===' as INFO;

-- Items rentados que deberían estar en estado 'Rentado'
SELECT 'Items en renta activa pero con estado incorrecto' as Verificacion,
       COUNT(*) as Problemas_Detectados
FROM inventario_item ii
JOIN detalle_renta_inventario_item drii ON ii.id = drii.inventario_item_id
JOIN detalle_renta dr ON drii.detalle_renta_id = dr.id
JOIN renta r ON dr.renta_id = r.id
WHERE r.estado_renta IN ('EnCurso', 'Programada') 
  AND ii.estado_item != 'Rentado'

UNION ALL

-- Items que no están rentados pero tienen estado 'Rentado'
SELECT 'Items marcados como rentados sin renta activa',
       COUNT(*)
FROM inventario_item ii
LEFT JOIN detalle_renta_inventario_item drii ON ii.id = drii.inventario_item_id
LEFT JOIN detalle_renta dr ON drii.detalle_renta_id = dr.id
LEFT JOIN renta r ON dr.renta_id = r.id AND r.estado_renta IN ('EnCurso', 'Programada')
WHERE ii.estado_item = 'Rentado' 
  AND r.id IS NULL;

-- Verificación 2: Validación de fechas
SELECT '=== VERIFICACIÓN DE FECHAS LÓGICAS ===' as INFO;
SELECT 'Rentas con fecha fin anterior a fecha inicio' as Problema,
       COUNT(*) as Casos_Detectados
FROM renta
WHERE fecha_fin_prevista <= fecha_inicio

UNION ALL

SELECT 'Mantenimientos con fecha fin anterior a fecha inicio',
       COUNT(*)
FROM mantenimiento
WHERE fecha_fin_real IS NOT NULL 
  AND fecha_fin_real < fecha_inicio

UNION ALL

SELECT 'Cotizaciones con fecha validez anterior a fecha cotización',
       COUNT(*)
FROM cotizacion
WHERE fecha_validez IS NOT NULL 
  AND fecha_validez < DATE(fecha_cotizacion);

-- ====================================================================
-- SECCIÓN 7: REPORTES EJECUTIVOS
-- ====================================================================

-- Reporte Ejecutivo 1: Dashboard principal
SELECT '=== DASHBOARD EJECUTIVO - INDICADORES CLAVE ===' as INFO;

SELECT 'Total Clientes Activos' as Indicador, 
       COUNT(*) as Valor, 
       NULL as Porcentaje
FROM cliente 
WHERE estado_cliente = 'Activo'

UNION ALL

SELECT 'Ingresos Totales (Rentas Finalizadas)',
       COALESCE(SUM(monto_total_renta), 0),
       NULL
FROM renta 
WHERE estado_renta = 'Finalizada'

UNION ALL

SELECT 'Items Disponibles para Renta',
       COUNT(*),
       ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM inventario_item WHERE estado_item != 'DeBaja'), 2)
FROM inventario_item 
WHERE estado_item = 'Disponible'

UNION ALL

SELECT 'Tasa Conversión Cotizaciones',
       COUNT(CASE WHEN estado_cotizacion = 'Aceptada' THEN 1 END),
       ROUND(COUNT(CASE WHEN estado_cotizacion = 'Aceptada' THEN 1 END) * 100.0 / COUNT(*), 2)
FROM cotizacion

UNION ALL

SELECT 'Rentas en Curso',
       COUNT(*),
       NULL
FROM renta 
WHERE estado_renta = 'EnCurso'

UNION ALL

SELECT 'Items en Mantenimiento',
       COUNT(*),
       ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM inventario_item), 2)
FROM inventario_item 
WHERE estado_item = 'EnMantenimiento';

-- ====================================================================
-- FINALIZACIÓN DEL SCRIPT
-- ====================================================================

SELECT '=== SCRIPT DE PRUEBA COMPLETADO EXITOSAMENTE ===' as RESULTADO;
SELECT CONCAT('Fecha de ejecución: ', NOW()) as TIMESTAMP;
SELECT 'Base de datos lista para uso en producción' as ESTADO;
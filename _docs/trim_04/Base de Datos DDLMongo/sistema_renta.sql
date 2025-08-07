-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 07-08-2025 a las 02:01:39
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `sistemarenta`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE `categoria` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `tipo_categoria` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`id`, `nombre`, `descripcion`, `tipo_categoria`, `created_at`, `updated_at`) VALUES
(1, 'Audio y Video', 'Equipos de sonido, micrófonos, pantallas y proyectores', 'Tecnológico', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 'Iluminación', 'Luces, reflectores y equipos de iluminación profesional', 'Tecnológico', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 'Mobiliario', 'Sillas, mesas, carpas y estructuras para eventos', 'Físico', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 'Catering', 'Equipos de cocina, refrigeración y servicio', 'Servicio', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 'Decoración', 'Elementos decorativos, flores y ambientación', 'Estético', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_subcategoria`
--

CREATE TABLE `categoria_subcategoria` (
  `categoria_id` int(11) NOT NULL,
  `subcategoria_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id` int(11) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `rfc` varchar(13) DEFAULT NULL,
  `telefono_principal` varchar(20) DEFAULT NULL,
  `correo_electronico` varchar(100) DEFAULT NULL,
  `tipo_cliente` enum('Particular','Empresa') DEFAULT 'Particular',
  `estado_cliente` enum('Activo','Inactivo','Prospecto') DEFAULT 'Prospecto',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id`, `nombre`, `rfc`, `telefono_principal`, `correo_electronico`, `tipo_cliente`, `estado_cliente`, `created_at`, `updated_at`) VALUES
(1, 'Empresas Globales S.A.', 'EGL891203AB1', '+52-55-1234-5678', 'contacto@empresasglobales.com', 'Empresa', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 'María González López', 'GOLM850615MH2', '+52-55-9876-5432', 'maria.gonzalez@email.com', 'Particular', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 'Hotel Royal Plaza', 'HRP920308CD3', '+52-55-5555-1234', 'eventos@hotelroyal.com', 'Empresa', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 'Carlos Mendoza Torres', 'METC780923EF4', '+57-1-234-5678', 'carlos.mendoza@email.com', 'Particular', 'Prospecto', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 'Eventos Corporativos Ltda', 'ECL030415GH5', '+57-1-987-6543', 'info@eventoscorp.com', 'Empresa', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente_direccion`
--

CREATE TABLE `cliente_direccion` (
  `cliente_id` int(11) NOT NULL,
  `direccion_id` int(11) NOT NULL,
  `es_principal` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cotizacion`
--

CREATE TABLE `cotizacion` (
  `id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `proveedor_id` int(11) DEFAULT NULL,
  `solicitud_id` int(11) DEFAULT NULL,
  `fecha_cotizacion` datetime DEFAULT current_timestamp(),
  `fecha_validez` date DEFAULT NULL,
  `monto_total` decimal(12,2) DEFAULT NULL,
  `estado_cotizacion` enum('Borrador','Enviada','Aceptada','Rechazada','Vencida') DEFAULT 'Borrador',
  `terminos_condiciones` text DEFAULT NULL,
  `notas_internas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_cotizacion`
--

CREATE TABLE `detalle_cotizacion` (
  `id` int(11) NOT NULL,
  `cotizacion_id` int(11) NOT NULL,
  `producto_id` int(11) DEFAULT NULL,
  `descripcion_item` varchar(255) DEFAULT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) GENERATED ALWAYS AS (`cantidad` * `precio_unitario`) STORED,
  `descuento_porcentaje` decimal(5,2) DEFAULT 0.00,
  `impuestos_aplicables` decimal(10,2) DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_renta`
--

CREATE TABLE `detalle_renta` (
  `id` int(11) NOT NULL,
  `renta_id` int(11) NOT NULL,
  `precio_renta_item` decimal(10,2) NOT NULL,
  `condicion_salida` text DEFAULT NULL,
  `condicion_regreso` text DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_renta_inventario_item`
--

CREATE TABLE `detalle_renta_inventario_item` (
  `detalle_renta_id` int(11) NOT NULL,
  `inventario_item_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `devolucion`
--

CREATE TABLE `devolucion` (
  `id` int(11) NOT NULL,
  `renta_id` int(11) DEFAULT NULL,
  `fecha_devolucion_programada` date DEFAULT NULL,
  `fecha_devolucion_real` datetime DEFAULT NULL,
  `estado_devolucion` enum('Pendiente','EnProcesoInspeccion','Completa','IncompletaConProblemas') DEFAULT 'Pendiente',
  `persona_recibe` varchar(150) DEFAULT NULL,
  `notas_generales` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `devolucion_inventario_item`
--

CREATE TABLE `devolucion_inventario_item` (
  `devolucion_id` int(11) NOT NULL,
  `inventario_item_id` int(11) NOT NULL,
  `condicion_al_regresar` text DEFAULT NULL,
  `requiere_mantenimiento` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `devolucion_item_mantenimiento`
--

CREATE TABLE `devolucion_item_mantenimiento` (
  `devolucion_id` int(11) NOT NULL,
  `inventario_item_id` int(11) NOT NULL,
  `mantenimiento_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `direccion`
--

CREATE TABLE `direccion` (
  `id` int(11) NOT NULL,
  `calle` varchar(255) NOT NULL,
  `numero_exterior` varchar(20) DEFAULT NULL,
  `numero_interior` varchar(20) DEFAULT NULL,
  `colonia` varchar(100) DEFAULT NULL,
  `ciudad` varchar(100) NOT NULL,
  `estado_provincia` varchar(100) NOT NULL,
  `codigo_postal` varchar(10) NOT NULL,
  `pais` varchar(100) NOT NULL,
  `referencias` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `direccion`
--

INSERT INTO `direccion` (`id`, `calle`, `numero_exterior`, `numero_interior`, `colonia`, `ciudad`, `estado_provincia`, `codigo_postal`, `pais`, `referencias`, `created_at`, `updated_at`) VALUES
(1, 'Av. Insurgentes Sur', '1234', 'Depto 5', 'Roma Norte', 'Ciudad de México', 'CDMX', '06700', 'México', 'Edificio azul, portón negro', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 'Calle Revolución', '567', NULL, 'San Ángel', 'Ciudad de México', 'CDMX', '01000', 'México', 'Casa con jardín frontal', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 'Av. Universidad', '890', 'Local 3', 'Copilco', 'Ciudad de México', 'CDMX', '04360', 'México', 'Plaza comercial', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 'Carrera 7', '123-45', 'Of 201', 'La Candelaria', 'Bogotá', 'Cundinamarca', '110311', 'Colombia', 'Edificio colonial', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 'Calle 93', '456', NULL, 'Chapinero', 'Bogotá', 'Cundinamarca', '110221', 'Colombia', 'Torre empresarial', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrega`
--

CREATE TABLE `entrega` (
  `id` int(11) NOT NULL,
  `renta_id` int(11) DEFAULT NULL,
  `fecha_envio` datetime DEFAULT NULL,
  `compania_envio` varchar(100) DEFAULT NULL,
  `numero_guia` varchar(100) DEFAULT NULL,
  `estado_entrega` enum('Programada','EnTransito','Entregada','Fallida') DEFAULT 'Programada',
  `notas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrega_direccion`
--

CREATE TABLE `entrega_direccion` (
  `entrega_id` int(11) NOT NULL,
  `direccion_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrega_inventario_item`
--

CREATE TABLE `entrega_inventario_item` (
  `entrega_id` int(11) NOT NULL,
  `inventario_item_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario_item`
--

CREATE TABLE `inventario_item` (
  `id` int(11) NOT NULL,
  `producto_id` int(11) DEFAULT NULL,
  `numero_serie` varchar(100) DEFAULT NULL,
  `estado_item` enum('Disponible','Rentado','EnMantenimiento','DeBaja') NOT NULL DEFAULT 'Disponible',
  `fecha_adquisicion` date DEFAULT NULL,
  `costo_adquisicion` decimal(10,2) DEFAULT NULL,
  `ubicacion_fisica` varchar(100) DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `inventario_item`
--

INSERT INTO `inventario_item` (`id`, `producto_id`, `numero_serie`, `estado_item`, `fecha_adquisicion`, `costo_adquisicion`, `ubicacion_fisica`, `notas`, `created_at`, `updated_at`) VALUES
(1, 1, 'SH-SM58-001', 'Disponible', '2023-01-15', 2500.00, 'Bodega A-1', 'Micrófono en excelente estado', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 1, 'SH-SM58-002', 'Rentado', '2023-01-15', 2500.00, 'Bodega A-1', 'Micrófono con estuche original', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 2, 'EP-EBX41-001', 'Disponible', '2023-02-20', 8000.00, 'Bodega B-2', 'Proyector con cables incluidos', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 3, 'JB-PRX815-001', 'EnMantenimiento', '2023-03-10', 12000.00, 'Taller', 'Requiere revisión de componentes', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 4, 'CH-SLIMPAR-001', 'Disponible', '2023-04-05', 3500.00, 'Bodega C-3', 'Luz LED con control remoto', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(6, 5, 'GE-MR150-001', 'Disponible', '2023-01-10', 800.00, 'Bodega D-4', 'Mesa con mantel incluido', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(7, 6, 'CH-CHGOLD-001', 'Disponible', '2023-02-15', 150.00, 'Bodega D-4', 'Silla con cojín', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(8, 7, 'EU-E66W-001', 'Rentado', '2023-03-20', 5000.00, 'Bodega E-5', 'Carpa con anclajes', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(9, 8, 'IM-VR08-001', 'DeBaja', '2022-12-01', 15000.00, 'Almacén', 'Equipo obsoleto para reparación', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(10, 9, 'FL-CMP001-001', 'Disponible', '2024-01-05', 200.00, 'Refrigerador F-6', 'Arreglo temporal, requiere cuidado especial', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario_item_mantenimiento`
--

CREATE TABLE `inventario_item_mantenimiento` (
  `inventario_item_id` int(11) NOT NULL,
  `mantenimiento_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mantenimiento`
--

CREATE TABLE `mantenimiento` (
  `id` int(11) NOT NULL,
  `fecha_inicio` datetime DEFAULT current_timestamp(),
  `fecha_fin_prevista` date DEFAULT NULL,
  `fecha_fin_real` date DEFAULT NULL,
  `tipo_mantenimiento` enum('Preventivo','Correctivo','Mejora') DEFAULT 'Correctivo',
  `descripcion_problema` text DEFAULT NULL,
  `descripcion_trabajo_realizado` text DEFAULT NULL,
  `costo_estimado` decimal(10,2) DEFAULT NULL,
  `costo_real` decimal(10,2) DEFAULT NULL,
  `estado_mantenimiento` enum('Programado','EnProceso','Finalizado','Cancelado') DEFAULT 'Programado',
  `responsable` varchar(150) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `mantenimiento`
--

INSERT INTO `mantenimiento` (`id`, `fecha_inicio`, `fecha_fin_prevista`, `fecha_fin_real`, `tipo_mantenimiento`, `descripcion_problema`, `descripcion_trabajo_realizado`, `costo_estimado`, `costo_real`, `estado_mantenimiento`, `responsable`, `created_at`, `updated_at`) VALUES
(1, '2024-01-15 09:00:00', '2024-01-15', NULL, 'Preventivo', 'Calibración y limpieza general', NULL, NULL, NULL, 'Finalizado', 'Técnico González', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, '2024-02-20 14:00:00', '2024-02-21', NULL, 'Correctivo', 'Reparación de conexión XLR', NULL, NULL, NULL, 'Finalizado', 'Técnico López', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, '2024-03-10 10:00:00', '2024-03-12', NULL, 'Preventivo', 'Revisión de componentes internos', NULL, NULL, NULL, 'EnProceso', 'Técnico Martínez', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, '2024-03-25 08:00:00', '2024-03-26', NULL, 'Correctivo', 'Sustitución de lámpara', NULL, NULL, NULL, 'Programado', 'Técnico Rodríguez', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, '2024-04-05 11:00:00', '2024-04-06', NULL, 'Mejora', 'Actualización de firmware', NULL, NULL, NULL, 'Programado', 'Técnico Silva', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `id` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `modelo` varchar(100) DEFAULT NULL,
  `precio_referencia_renta` decimal(10,2) DEFAULT NULL,
  `sku` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`id`, `nombre`, `descripcion`, `marca`, `modelo`, `precio_referencia_renta`, `sku`, `created_at`, `updated_at`) VALUES
(1, 'Micrófono Inalámbrico', 'Micrófono inalámbrico profesional con receptor', 'Shure', 'SM58', 150.00, 'MIC-SM58-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 'Proyector Full HD', 'Proyector 1920x1080 3000 lúmenes', 'Epson', 'EB-X41', 300.00, 'PROJ-EBX41-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 'Bocina Activa 15\"', 'Bocina autoamplificada 400W', 'JBL', 'PRX815W', 200.00, 'BOC-PRX815-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 'Luz LED Par 64', 'Reflector LED RGB 180W', 'Chauvet', 'SlimPAR T12BT', 80.00, 'LED-SLIMPAR-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 'Mesa Redonda 1.5m', 'Mesa redonda para 8 personas', 'Generic', 'MR150', 25.00, 'MES-RD150-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(6, 'Silla Chiavari Dorada', 'Silla elegante color dorado', 'Chiavari', 'CH-GOLD', 8.00, 'SIL-CHGOLD-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(7, 'Carpa 6x6 metros', 'Carpa blanca para exteriores', 'Eureka', 'E66W', 120.00, 'CAR-E66W-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(8, 'Nevera Exhibidora', 'Refrigerador vertical 400L', 'Imbera', 'VR-08', 180.00, 'NEV-VR08-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(9, 'Centro de Mesa Premium', 'Arreglo floral con rosas y lirios', 'FloralArt', 'CMP-001', 45.00, 'DEC-CMP001-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(10, 'Pantalla de Proyección', 'Pantalla retráctil 2x1.5 metros', 'Da-Lite', 'DL-150', 50.00, 'PAN-DL150-001', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_subcategoria`
--

CREATE TABLE `producto_subcategoria` (
  `producto_id` int(11) NOT NULL,
  `subcategoria_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

CREATE TABLE `proveedor` (
  `id` int(11) NOT NULL,
  `nombre_empresa` varchar(200) NOT NULL,
  `rfc` varchar(13) DEFAULT NULL,
  `nombre_contacto` varchar(150) DEFAULT NULL,
  `telefono_contacto` varchar(20) DEFAULT NULL,
  `correo_contacto` varchar(100) DEFAULT NULL,
  `estado_proveedor` enum('Activo','Inactivo') DEFAULT 'Activo',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`id`, `nombre_empresa`, `rfc`, `nombre_contacto`, `telefono_contacto`, `correo_contacto`, `estado_proveedor`, `created_at`, `updated_at`) VALUES
(1, 'Audio Pro Rental', 'APR850612IJ6', 'Juan Pérez', '+52-55-1111-2222', 'juan@audiopro.com', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 'Luces y Efectos SA', 'LEF920715KL7', 'Ana Rodríguez', '+52-55-3333-4444', 'ana@lucesyefectos.com', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 'Mobiliario Elite', 'MEL880923MN8', 'Pedro Sánchez', '+57-1-555-6666', 'pedro@mobiliarioelite.com', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 'Catering Solutions', 'CSL910304OP9', 'Laura Martínez', '+57-1-777-8888', 'laura@cateringsol.com', 'Inactivo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 'Decoraciones Finas', 'DFI860108QR0', 'Miguel Torres', '+52-55-9999-0000', 'miguel@decoracionesfinas.com', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `renta`
--

CREATE TABLE `renta` (
  `id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `cotizacion_id` int(11) DEFAULT NULL,
  `fecha_inicio` datetime NOT NULL,
  `fecha_fin_prevista` datetime NOT NULL,
  `fecha_devolucion_real` datetime DEFAULT NULL,
  `estado_renta` enum('Programada','EnCurso','Finalizada','Retrasada','Cancelada') DEFAULT 'Programada',
  `monto_total_renta` decimal(12,2) DEFAULT NULL,
  `deposito_garantia` decimal(10,2) DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `id` int(11) NOT NULL,
  `codigo` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id`, `codigo`, `nombre`, `descripcion`, `created_at`, `updated_at`) VALUES
(1, 'ADMIN', 'Administrador', 'Acceso total al sistema', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 'VENTAS', 'Vendedor', 'Gestión de clientes y cotizaciones', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 'INVENTARIO', 'Encargado de Inventario', 'Control de productos y equipos', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 'TECNICO', 'Técnico', 'Mantenimiento y soporte técnico', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 'GERENTE', 'Gerente', 'Supervisión y reportes ejecutivos', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `solicitud`
--

CREATE TABLE `solicitud` (
  `id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `fecha_solicitud` datetime DEFAULT current_timestamp(),
  `nombre_producto_alternativo` varchar(200) DEFAULT NULL,
  `cantidad_solicitada` int(11) DEFAULT 1,
  `descripcion_necesidad` text DEFAULT NULL,
  `estado_solicitud` enum('Nueva','EnProceso','Atendida','Cancelada') DEFAULT 'Nueva',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subcategoria`
--

CREATE TABLE `subcategoria` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `subcategoria`
--

INSERT INTO `subcategoria` (`id`, `nombre`, `descripcion`, `created_at`, `updated_at`) VALUES
(1, 'Microfonía', 'Micrófonos inalámbricos, de mano, corbateros', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 'Proyección', 'Proyectores, pantallas de proyección', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 'Sonido', 'Bocinas, amplificadores, mezcladores', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 'Luces LED', 'Iluminación LED de colores y efectos', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 'Luces Tradicionales', 'Reflectores halógenos y tungsteno', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(6, 'Mesas Redondas', 'Mesas circulares para eventos', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(7, 'Sillas Chiavari', 'Sillas elegantes para eventos', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(8, 'Carpas', 'Estructuras temporales para exteriores', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(9, 'Refrigeración', 'Neveras y equipos de frío', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(10, 'Decoración Floral', 'Arreglos y centros de mesa', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nombre_usuario` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `nombre_completo` varchar(200) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `departamento` varchar(100) DEFAULT NULL,
  `estado` enum('Activo','Inactivo','Bloqueado') DEFAULT 'Activo',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `nombre_usuario`, `password_hash`, `nombre_completo`, `email`, `telefono`, `departamento`, `estado`, `created_at`, `updated_at`) VALUES
(1, 'admin', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrador del Sistema', 'admin@rentaequipos.com', '+52-55-1000-0001', 'Sistemas', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(2, 'jperez', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Juan Pérez Vendedor', 'juan.perez@rentaequipos.com', '+52-55-1000-0002', 'Ventas', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(3, 'mgonzalez', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'María González Inventario', 'maria.gonzalez@rentaequipos.com', '+52-55-1000-0003', 'Inventario', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(4, 'ctecnico', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Carlos Técnico López', 'carlos.tecnico@rentaequipos.com', '+52-55-1000-0004', 'Técnico', 'Activo', '2025-08-06 23:49:22', '2025-08-06 23:49:22'),
(5, 'agerente', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Ana Gerente Silva', 'ana.gerente@rentaequipos.com', '+52-55-1000-0005', 'Gerencia', 'Inactivo', '2025-08-06 23:49:22', '2025-08-06 23:49:22');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_rol`
--

CREATE TABLE `usuario_rol` (
  `usuario_id` int(11) NOT NULL,
  `rol_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria`
--
ALTER TABLE `categoria`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `categoria_subcategoria`
--
ALTER TABLE `categoria_subcategoria`
  ADD PRIMARY KEY (`categoria_id`,`subcategoria_id`),
  ADD KEY `subcategoria_id` (`subcategoria_id`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `rfc` (`rfc`),
  ADD UNIQUE KEY `correo_electronico` (`correo_electronico`),
  ADD KEY `idx_estado_cliente` (`estado_cliente`),
  ADD KEY `idx_tipo_cliente` (`tipo_cliente`);

--
-- Indices de la tabla `cliente_direccion`
--
ALTER TABLE `cliente_direccion`
  ADD PRIMARY KEY (`cliente_id`,`direccion_id`),
  ADD KEY `direccion_id` (`direccion_id`);

--
-- Indices de la tabla `cotizacion`
--
ALTER TABLE `cotizacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_cliente_id` (`cliente_id`),
  ADD KEY `idx_estado_cotizacion` (`estado_cotizacion`),
  ADD KEY `idx_fecha_cotizacion` (`fecha_cotizacion`),
  ADD KEY `fk_cotizacion_proveedor` (`proveedor_id`),
  ADD KEY `fk_cotizacion_solicitud` (`solicitud_id`);

--
-- Indices de la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_cotizacion_id` (`cotizacion_id`),
  ADD KEY `fk_detalle_cotizacion_producto` (`producto_id`);

--
-- Indices de la tabla `detalle_renta`
--
ALTER TABLE `detalle_renta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_renta_id` (`renta_id`);

--
-- Indices de la tabla `detalle_renta_inventario_item`
--
ALTER TABLE `detalle_renta_inventario_item`
  ADD PRIMARY KEY (`detalle_renta_id`,`inventario_item_id`),
  ADD KEY `inventario_item_id` (`inventario_item_id`);

--
-- Indices de la tabla `devolucion`
--
ALTER TABLE `devolucion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_renta_id` (`renta_id`),
  ADD KEY `idx_estado_devolucion` (`estado_devolucion`);

--
-- Indices de la tabla `devolucion_inventario_item`
--
ALTER TABLE `devolucion_inventario_item`
  ADD PRIMARY KEY (`devolucion_id`,`inventario_item_id`),
  ADD KEY `inventario_item_id` (`inventario_item_id`);

--
-- Indices de la tabla `devolucion_item_mantenimiento`
--
ALTER TABLE `devolucion_item_mantenimiento`
  ADD PRIMARY KEY (`devolucion_id`,`inventario_item_id`,`mantenimiento_id`),
  ADD KEY `inventario_item_id` (`inventario_item_id`),
  ADD KEY `mantenimiento_id` (`mantenimiento_id`);

--
-- Indices de la tabla `direccion`
--
ALTER TABLE `direccion`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `entrega`
--
ALTER TABLE `entrega`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_renta_id` (`renta_id`),
  ADD KEY `idx_estado_entrega` (`estado_entrega`);

--
-- Indices de la tabla `entrega_direccion`
--
ALTER TABLE `entrega_direccion`
  ADD PRIMARY KEY (`entrega_id`,`direccion_id`),
  ADD KEY `direccion_id` (`direccion_id`);

--
-- Indices de la tabla `entrega_inventario_item`
--
ALTER TABLE `entrega_inventario_item`
  ADD PRIMARY KEY (`entrega_id`,`inventario_item_id`),
  ADD KEY `inventario_item_id` (`inventario_item_id`);

--
-- Indices de la tabla `inventario_item`
--
ALTER TABLE `inventario_item`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero_serie` (`numero_serie`),
  ADD KEY `idx_estado_item` (`estado_item`),
  ADD KEY `idx_producto_id` (`producto_id`);

--
-- Indices de la tabla `inventario_item_mantenimiento`
--
ALTER TABLE `inventario_item_mantenimiento`
  ADD PRIMARY KEY (`inventario_item_id`,`mantenimiento_id`),
  ADD KEY `mantenimiento_id` (`mantenimiento_id`);

--
-- Indices de la tabla `mantenimiento`
--
ALTER TABLE `mantenimiento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_estado_mantenimiento` (`estado_mantenimiento`),
  ADD KEY `idx_tipo_mantenimiento` (`tipo_mantenimiento`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sku` (`sku`),
  ADD KEY `idx_nombre` (`nombre`);

--
-- Indices de la tabla `producto_subcategoria`
--
ALTER TABLE `producto_subcategoria`
  ADD PRIMARY KEY (`producto_id`,`subcategoria_id`),
  ADD KEY `subcategoria_id` (`subcategoria_id`);

--
-- Indices de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre_empresa` (`nombre_empresa`),
  ADD UNIQUE KEY `rfc` (`rfc`);

--
-- Indices de la tabla `renta`
--
ALTER TABLE `renta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_cliente_id` (`cliente_id`),
  ADD KEY `idx_estado_renta` (`estado_renta`),
  ADD KEY `idx_fecha_inicio` (`fecha_inicio`),
  ADD KEY `fk_renta_cotizacion` (`cotizacion_id`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `solicitud`
--
ALTER TABLE `solicitud`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_cliente_id` (`cliente_id`),
  ADD KEY `idx_estado_solicitud` (`estado_solicitud`),
  ADD KEY `idx_fecha_solicitud` (`fecha_solicitud`);

--
-- Indices de la tabla `subcategoria`
--
ALTER TABLE `subcategoria`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_estado` (`estado`);

--
-- Indices de la tabla `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD PRIMARY KEY (`usuario_id`,`rol_id`),
  ADD KEY `rol_id` (`rol_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categoria`
--
ALTER TABLE `categoria`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `cotizacion`
--
ALTER TABLE `cotizacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_renta`
--
ALTER TABLE `detalle_renta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `devolucion`
--
ALTER TABLE `devolucion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `direccion`
--
ALTER TABLE `direccion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `entrega`
--
ALTER TABLE `entrega`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `inventario_item`
--
ALTER TABLE `inventario_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `mantenimiento`
--
ALTER TABLE `mantenimiento`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `renta`
--
ALTER TABLE `renta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `solicitud`
--
ALTER TABLE `solicitud`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `subcategoria`
--
ALTER TABLE `subcategoria`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `categoria_subcategoria`
--
ALTER TABLE `categoria_subcategoria`
  ADD CONSTRAINT `fk_categoria_subcategoria_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_categoria_subcategoria_subcategoria` FOREIGN KEY (`subcategoria_id`) REFERENCES `subcategoria` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `cliente_direccion`
--
ALTER TABLE `cliente_direccion`
  ADD CONSTRAINT `fk_cliente_direccion_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_cliente_direccion_direccion` FOREIGN KEY (`direccion_id`) REFERENCES `direccion` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `cotizacion`
--
ALTER TABLE `cotizacion`
  ADD CONSTRAINT `fk_cotizacion_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  ADD CONSTRAINT `fk_cotizacion_proveedor` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedor` (`id`),
  ADD CONSTRAINT `fk_cotizacion_solicitud` FOREIGN KEY (`solicitud_id`) REFERENCES `solicitud` (`id`);

--
-- Filtros para la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  ADD CONSTRAINT `fk_detalle_cotizacion_cotizacion` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizacion` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_detalle_cotizacion_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`);

--
-- Filtros para la tabla `detalle_renta`
--
ALTER TABLE `detalle_renta`
  ADD CONSTRAINT `fk_detalle_renta_renta` FOREIGN KEY (`renta_id`) REFERENCES `renta` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `detalle_renta_inventario_item`
--
ALTER TABLE `detalle_renta_inventario_item`
  ADD CONSTRAINT `fk_dr_ii_detalle_renta` FOREIGN KEY (`detalle_renta_id`) REFERENCES `detalle_renta` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_dr_ii_inventario_item` FOREIGN KEY (`inventario_item_id`) REFERENCES `inventario_item` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `devolucion`
--
ALTER TABLE `devolucion`
  ADD CONSTRAINT `fk_devolucion_renta` FOREIGN KEY (`renta_id`) REFERENCES `renta` (`id`);

--
-- Filtros para la tabla `devolucion_inventario_item`
--
ALTER TABLE `devolucion_inventario_item`
  ADD CONSTRAINT `fk_d_ii_devolucion` FOREIGN KEY (`devolucion_id`) REFERENCES `devolucion` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_d_ii_inventario_item` FOREIGN KEY (`inventario_item_id`) REFERENCES `inventario_item` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `devolucion_item_mantenimiento`
--
ALTER TABLE `devolucion_item_mantenimiento`
  ADD CONSTRAINT `fk_di_m_devolucion` FOREIGN KEY (`devolucion_id`) REFERENCES `devolucion` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_di_m_inventario_item` FOREIGN KEY (`inventario_item_id`) REFERENCES `inventario_item` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_di_m_mantenimiento` FOREIGN KEY (`mantenimiento_id`) REFERENCES `mantenimiento` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `entrega`
--
ALTER TABLE `entrega`
  ADD CONSTRAINT `fk_entrega_renta` FOREIGN KEY (`renta_id`) REFERENCES `renta` (`id`);

--
-- Filtros para la tabla `entrega_direccion`
--
ALTER TABLE `entrega_direccion`
  ADD CONSTRAINT `fk_entrega_direccion_direccion` FOREIGN KEY (`direccion_id`) REFERENCES `direccion` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_entrega_direccion_entrega` FOREIGN KEY (`entrega_id`) REFERENCES `entrega` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `entrega_inventario_item`
--
ALTER TABLE `entrega_inventario_item`
  ADD CONSTRAINT `fk_e_ii_entrega` FOREIGN KEY (`entrega_id`) REFERENCES `entrega` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_e_ii_inventario_item` FOREIGN KEY (`inventario_item_id`) REFERENCES `inventario_item` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `inventario_item`
--
ALTER TABLE `inventario_item`
  ADD CONSTRAINT `fk_inventario_item_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`);

--
-- Filtros para la tabla `inventario_item_mantenimiento`
--
ALTER TABLE `inventario_item_mantenimiento`
  ADD CONSTRAINT `fk_ii_m_inventario_item` FOREIGN KEY (`inventario_item_id`) REFERENCES `inventario_item` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_ii_m_mantenimiento` FOREIGN KEY (`mantenimiento_id`) REFERENCES `mantenimiento` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `producto_subcategoria`
--
ALTER TABLE `producto_subcategoria`
  ADD CONSTRAINT `fk_producto_subcategoria_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_producto_subcategoria_subcategoria` FOREIGN KEY (`subcategoria_id`) REFERENCES `subcategoria` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `renta`
--
ALTER TABLE `renta`
  ADD CONSTRAINT `fk_renta_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  ADD CONSTRAINT `fk_renta_cotizacion` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizacion` (`id`);

--
-- Filtros para la tabla `solicitud`
--
ALTER TABLE `solicitud`
  ADD CONSTRAINT `fk_solicitud_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`);

--
-- Filtros para la tabla `usuario_rol`
--
ALTER TABLE `usuario_rol`
  ADD CONSTRAINT `fk_usuario_rol_rol` FOREIGN KEY (`rol_id`) REFERENCES `rol` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_usuario_rol_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

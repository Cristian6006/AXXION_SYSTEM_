-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 11-11-2025 a las 15:09:20
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `categoria`
--

INSERT INTO `categoria` (`id`, `nombre`, `descripcion`, `tipo_categoria`, `created_at`, `updated_at`) VALUES
(1, 'Equipos de Sonido', 'Altavoces, consolas, micrófonos y más.', 'Equipos', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 'Iluminación', 'Luces para eventos, escenarios y fiestas.', 'Equipos', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 'Video', 'Pantallas, proyectores y equipo de video.', 'Equipos', '2025-09-19 05:24:18', '2025-09-19 05:24:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_subcategoria`
--

CREATE TABLE `categoria_subcategoria` (
  `categoria_id` int(11) NOT NULL,
  `subcategoria_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `categoria_subcategoria`
--

INSERT INTO `categoria_subcategoria` (`categoria_id`, `subcategoria_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 4),
(3, 5),
(3, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id` int(11) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `nombre2` varchar(100) NOT NULL,
  `apellido1` varchar(100) NOT NULL,
  `apellido2` varchar(100) NOT NULL,
  `rfc` varchar(13) DEFAULT NULL,
  `telefono_principal` varchar(20) DEFAULT NULL,
  `correo_electronico` varchar(100) DEFAULT NULL,
  `tipo_cliente` enum('Particular','Empresa') DEFAULT 'Particular',
  `estado_cliente` enum('Activo','Inactivo','Prospecto') DEFAULT 'Prospecto',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id`, `nombre`, `nombre2`, `apellido1`, `apellido2`, `rfc`, `telefono_principal`, `correo_electronico`, `tipo_cliente`, `estado_cliente`, `created_at`, `updated_at`) VALUES
(1, 'Carlos', 'Alberto', 'Sanchez', 'Ruiz', 'SARC850312XYZ', '5512345678', 'carlos.sanchez@email.com', 'Particular', 'Activo', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 'Eventos', 'Grandes', 'SA de CV', '', 'EGR990115ABC', '5587654321', 'contacto@eventosgrandes.com', 'Empresa', 'Activo', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 'Maria', 'Elena', 'García', 'Pérez', 'GAPM780520DEF', '5555667788', 'maria.garcia@email.com', 'Particular', 'Prospecto', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(4, 'Producciones', 'Audiovisuales', 'SA de CV', '', 'PAV120815GHI', '5544332211', 'contacto@prodav.com', 'Empresa', 'Activo', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(5, 'Luis', 'Fernando', 'Rodriguez', 'Morales', 'ROML850720JKL', '5599887766', 'luis.rodriguez@email.com', 'Particular', 'Activo', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(6, 'Eventos', 'Corporativos', 'Plus SA', '', 'ECP030910MNO', '5533445566', 'ventas@eventoscorp.com', 'Empresa', 'Prospecto', '2025-09-19 05:24:18', '2025-09-19 05:24:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente_direccion`
--

CREATE TABLE `cliente_direccion` (
  `cliente_id` int(11) NOT NULL,
  `direccion_id` int(11) NOT NULL,
  `es_principal` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cliente_direccion`
--

INSERT INTO `cliente_direccion` (`cliente_id`, `direccion_id`, `es_principal`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 6, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cotizacion`
--

CREATE TABLE `cotizacion` (
  `id` int(11) NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `solicitud_id` int(11) DEFAULT NULL,
  `fecha_cotizacion` datetime DEFAULT current_timestamp(),
  `fecha_validez` date DEFAULT NULL,
  `monto_total` decimal(12,2) DEFAULT NULL,
  `estado_cotizacion` enum('Borrador','Enviada','Aceptada','Rechazada','Vencida') DEFAULT 'Borrador',
  `terminos_condiciones` text DEFAULT NULL,
  `notas_internas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cotizacion`
--

INSERT INTO `cotizacion` (`id`, `cliente_id`, `solicitud_id`, `fecha_cotizacion`, `fecha_validez`, `monto_total`, `estado_cotizacion`, `terminos_condiciones`, `notas_internas`, `created_at`, `updated_at`) VALUES
(1, 1, 1, '2025-09-19 00:24:18', '2025-10-19', 1600.00, 'Enviada', 'Pago del 50% para reservar. No incluye IVA.', NULL, '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 2, 2, '2025-09-19 00:24:18', '2025-10-15', 2500.00, 'Borrador', 'Sujeto a disponibilidad de equipo.', NULL, '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 4, 3, '2025-09-18 15:00:00', '2025-10-18', 8500.00, 'Enviada', 'Pago 50% anticipo, 50% contra entrega. Incluye instalación y operador.', 'Cliente corporativo, descuento aplicado', '2025-09-19 05:24:19', '2025-09-19 05:24:19'),
(4, 5, 4, '2025-09-17 11:00:00', '2025-10-17', 12000.00, 'Borrador', 'Precios sujetos a confirmación de fechas. Incluye montaje.', 'Evento en exterior, considerar carpa adicional', '2025-09-19 05:24:19', '2025-09-19 05:24:19'),
(5, 1, 5, '2025-09-16 17:00:00', '2025-10-16', 2400.00, 'Aceptada', 'Pago de contado con 10% descuento.', 'Cliente frecuente', '2025-09-19 05:24:19', '2025-09-19 05:24:19');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_cotizacion`
--

CREATE TABLE `detalle_cotizacion` (
  `id` int(11) NOT NULL,
  `cotizacion_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `descripcion_item` varchar(255) DEFAULT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) GENERATED ALWAYS AS (`cantidad` * `precio_unitario`) STORED,
  `descuento_porcentaje` decimal(5,2) DEFAULT 0.00,
  `impuestos_aplicables` decimal(10,2) DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `detalle_cotizacion`
--

INSERT INTO `detalle_cotizacion` (`id`, `cotizacion_id`, `producto_id`, `descripcion_item`, `cantidad`, `precio_unitario`, `descuento_porcentaje`, `impuestos_aplicables`, `notas`, `created_at`, `updated_at`) VALUES
(1, 1, 1, 'Altavoz autoamplificado 15\" QSC K12.2', 2, 800.00, 0.00, NULL, 'Para evento de 100 personas', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 1, 3, 'Micrófono inalámbrico Shure QLX-D', 2, 600.00, 5.00, NULL, 'Descuento por cantidad', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 2, 4, 'Proyector 5000 Lúmenes Epson PowerLite L510U', 1, 2000.00, 0.00, NULL, 'Para conferencia empresarial', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(4, 2, 8, 'Pantalla de proyección frontal 2x3m', 1, 500.00, 0.00, NULL, 'Incluye trípodes de soporte', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(5, 3, 1, 'Altavoz autoamplificado 15\" QSC K12.2', 4, 800.00, 10.00, NULL, 'Descuento por volumen', '2025-09-19 05:24:19', '2025-09-19 05:24:19'),
(6, 3, 2, 'Consola de mezclas digital Behringer X32', 1, 1500.00, 10.00, NULL, 'Incluye operador técnico', '2025-09-19 05:24:19', '2025-09-19 05:24:19'),
(7, 3, 10, 'Subwoofer QSC KS118', 2, 1000.00, 10.00, NULL, 'Para refuerzo de graves', '2025-09-19 05:24:19', '2025-09-19 05:24:19'),
(8, 4, 6, 'Mesa redonda 1.8m', 15, 150.00, 5.00, NULL, 'Con manteles blancos', '2025-09-19 05:24:19', '2025-09-19 05:24:19'),
(9, 4, 7, 'Silla Tiffany dorada', 150, 50.00, 8.00, NULL, 'Descuento por cantidad', '2025-09-19 05:24:19', '2025-09-19 05:24:19'),
(10, 5, 5, 'Luz LED Par 64', 8, 300.00, 0.00, NULL, 'Colores personalizables', '2025-09-19 05:24:19', '2025-09-19 05:24:19');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `devolucion`
--

CREATE TABLE `devolucion` (
  `id` int(11) NOT NULL,
  `renta_id` int(11) NOT NULL,
  `fecha_devolucion_programada` date DEFAULT NULL,
  `fecha_devolucion_real` datetime DEFAULT NULL,
  `estado_devolucion` enum('Pendiente','EnProcesoInspeccion','Completa','IncompletaConProblemas') DEFAULT 'Pendiente',
  `persona_recibe` varchar(150) DEFAULT NULL,
  `notas_generales` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `devolucion`
--

INSERT INTO `devolucion` (`id`, `renta_id`, `fecha_devolucion_programada`, `fecha_devolucion_real`, `estado_devolucion`, `persona_recibe`, `notas_generales`, `created_at`, `updated_at`) VALUES
(1, 1, '2025-09-22', NULL, 'Pendiente', NULL, NULL, '2025-09-19 05:24:18', '2025-09-19 05:24:18');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `direccion`
--

INSERT INTO `direccion` (`id`, `calle`, `numero_exterior`, `numero_interior`, `colonia`, `ciudad`, `estado_provincia`, `codigo_postal`, `pais`, `referencias`, `created_at`, `updated_at`) VALUES
(1, 'Av. Siempre Viva', '742', NULL, 'Springfield', 'Ciudad de México', 'CDMX', '01234', 'México', 'Frente al Kwik-E-Mart', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 'Calle Falsa', '123', NULL, 'Centro', 'Guadalajara', 'Jalisco', '44100', 'México', 'Edificio de ladrillo rojo', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 'Blvd. de los Sueños Rotos', '45', NULL, 'Zona Hotelera', 'Cancún', 'Quintana Roo', '77500', 'México', 'Cerca de la playa', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(4, 'Av. Insurgentes Sur', '1234', 'Piso 5', 'Del Valle', 'Ciudad de México', 'CDMX', '03100', 'México', 'Torre empresarial, entrada por estacionamiento', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(5, 'Calle Morelos', '567', NULL, 'Centro Histórico', 'Puebla', 'Puebla', '72000', 'México', 'Edificio colonial, portón verde', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(6, 'Blvd. Kukulcán', '89', 'Local 12', 'Zona Hotelera', 'Cancún', 'Quintana Roo', '77500', 'México', 'Plaza comercial, segundo nivel', '2025-09-19 05:24:18', '2025-09-19 05:24:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrega`
--

CREATE TABLE `entrega` (
  `id` int(11) NOT NULL,
  `renta_id` int(11) NOT NULL,
  `direccion_id` int(11) NOT NULL,
  `fecha_envio` datetime DEFAULT NULL,
  `compania_envio` varchar(100) DEFAULT NULL,
  `numero_guia` varchar(100) DEFAULT NULL,
  `estado_entrega` enum('Programada','EnTransito','Entregada','Fallida') DEFAULT 'Programada',
  `notas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `entrega`
--

INSERT INTO `entrega` (`id`, `renta_id`, `direccion_id`, `fecha_envio`, `compania_envio`, `numero_guia`, `estado_entrega`, `notas`, `created_at`, `updated_at`) VALUES
(1, 1, 2, '2025-09-20 08:00:00', NULL, NULL, 'Programada', NULL, '2025-09-19 05:24:18', '2025-09-19 05:24:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario_item`
--

CREATE TABLE `inventario_item` (
  `id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `numero_serie` varchar(100) DEFAULT NULL,
  `estado_item` enum('Disponible','Rentado','EnMantenimiento','DeBaja') NOT NULL DEFAULT 'Disponible',
  `fecha_adquisicion` date DEFAULT NULL,
  `costo_adquisicion` decimal(10,2) DEFAULT NULL,
  `ubicacion_fisica` varchar(100) DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `inventario_item`
--

INSERT INTO `inventario_item` (`id`, `producto_id`, `numero_serie`, `estado_item`, `fecha_adquisicion`, `costo_adquisicion`, `ubicacion_fisica`, `notas`, `created_at`, `updated_at`) VALUES
(19, 13, 'test', 'Disponible', '0001-01-01', 1.00, NULL, 'Item creado automáticamente desde producto: test', '2025-10-17 02:11:16', '2025-10-17 02:11:16'),
(20, 14, '11', 'EnMantenimiento', NULL, 0.00, NULL, 'Item creado automáticamente desde producto: TEST2', '2025-10-23 02:15:37', '2025-10-23 02:15:37');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mantenimiento`
--

CREATE TABLE `mantenimiento` (
  `id` int(11) NOT NULL,
  `inventario_item_id` int(11) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `precio_alquiler_dia` decimal(10,2) DEFAULT NULL,
  `precio_alquiler_semanal` decimal(10,2) DEFAULT NULL,
  `precio_alquiler_mensual` decimal(10,2) DEFAULT NULL,
  `precio_compra` decimal(10,2) DEFAULT NULL,
  `valor_actual` decimal(10,2) DEFAULT NULL,
  `fecha_compra` date DEFAULT NULL,
  `condicion` varchar(50) DEFAULT NULL,
  `ubicacion` varchar(255) DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `sku` varchar(50) DEFAULT NULL,
  `numero_serie` varchar(100) DEFAULT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `especificaciones` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`especificaciones`)),
  `estado` varchar(50) DEFAULT 'disponible',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`id`, `nombre`, `descripcion`, `marca`, `modelo`, `precio_referencia_renta`, `precio_alquiler_dia`, `precio_alquiler_semanal`, `precio_alquiler_mensual`, `precio_compra`, `valor_actual`, `fecha_compra`, `condicion`, `ubicacion`, `notas`, `sku`, `numero_serie`, `categoria`, `especificaciones`, `estado`, `created_at`, `updated_at`) VALUES
(1, 'Altavoz autoamplificado 15\"', NULL, 'QSC', 'K12.2', NULL, 800.00, 4800.00, 18000.00, 25000.00, 22000.00, '2024-01-15', 'excelente', 'Bodega A-1', NULL, 'QSC-K122-001', 'QSCK122001', 'Iluminación', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'mantenimiento', '2025-09-19 05:24:18', '2025-10-11 06:49:55'),
(2, 'Consola de mezclas digital', NULL, 'Behringer', 'X32', NULL, 1500.00, 9000.00, 33000.00, 60000.00, 55000.00, '2023-11-20', 'Excelente', 'Bodega B-2', NULL, 'BEH-X32-001', 'BEHX32001', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'mantenimiento', '2025-09-19 05:24:18', '2025-10-23 05:02:22'),
(3, 'Micrófono inalámbrico de mano', NULL, 'Shure', 'QLX-D', NULL, 600.00, 3600.00, 13500.00, 18000.00, 16000.00, '2024-03-10', 'regular', 'Taller', NULL, 'SHU-QLXD-001', 'SHUQLXD001', 'Iluminación', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'mantenimiento', '2025-09-19 05:24:18', '2025-09-25 05:09:16'),
(4, 'Proyector 5000 Lúmenes', 'Proyector láser de alta luminosidad.', 'Epson', 'PowerLite L510U', NULL, 2000.00, 12000.00, 45000.00, 85000.00, 80000.00, '2024-02-01', 'Excelente', 'Bodega C-1', NULL, 'EPS-L510U-001', 'EPSL510U001', 'Video', '{\"lumenes\": 5000, \"resolucion\": \"WUXGA\", \"tecnologia\": \"Láser\", \"vida_util\": \"20000h\"}', 'disponible', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(5, 'Luz LED Par 64', 'Reflector LED RGBW de 180W.', 'Chauvet', 'SlimPAR Pro H USB', NULL, 300.00, 1800.00, 6750.00, 8500.00, 7500.00, '2024-01-20', 'Excelente', 'Bodega D-1', NULL, 'CHA-SLIMPAR-001', 'CHASLIM001', 'Iluminación', '{\"potencia\": \"180W\", \"colores\": \"RGBW\", \"control\": \"DMX512\"}', 'disponible', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(6, 'Mesa redonda 1.8m', 'Mesa redonda para 10 personas.', 'Eventos Pro', 'MR-180', NULL, 150.00, 900.00, 3375.00, 3500.00, 3000.00, '2023-12-15', 'Bueno', 'Bodega E-1', NULL, 'EVP-MR180-001', 'EVPMR180001', 'Mobiliario', '{\"diametro\": \"1.8m\", \"capacidad\": \"10 personas\", \"material\": \"Madera\"}', 'disponible', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(7, 'Silla Tiffany dorada', 'Silla elegante para eventos.', 'Eventos Pro', 'ST-DORADA', NULL, 50.00, 300.00, 1125.00, 1200.00, 1000.00, '2023-10-10', 'Bueno', 'Bodega E-2', NULL, 'EVP-STDOR-001', 'EVPSTDOR001', 'Mobiliario', '{\"material\": \"Resina\", \"color\": \"Dorado\", \"apilable\": \"Sí\"}', 'disponible', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(8, 'Pantalla de proyección 2x3m', 'Pantalla frontal para proyector.', 'Da-Lite', 'Fast-Fold Deluxe', NULL, 500.00, 3000.00, 11250.00, 15000.00, 13500.00, '2024-01-25', 'Excelente', 'Bodega C-2', NULL, 'DAL-FFD23-001', 'DALFFD23001', 'Video', '{\"tamaño\": \"2x3m\", \"tipo\": \"Frontal\", \"superficie\": \"Matte White\"}', 'disponible', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(9, 'Carpa 6x6m', 'Carpa blanca para eventos exteriores.', 'Tent Pro', 'TP-66', NULL, 1200.00, 7200.00, 27000.00, 35000.00, 32000.00, '2023-09-05', 'Bueno', 'Patio exterior', NULL, 'TNT-TP66-001', 'TNTTP66001', 'Mobiliario', '{\"tamaño\": \"6x6m\", \"color\": \"Blanco\", \"resistente_agua\": \"Sí\"}', 'disponible', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(10, 'Altavoz subwoofer 18\"', 'Subwoofer activo de 18 pulgadas.', 'QSC', 'KS118', NULL, 1000.00, 6000.00, 22500.00, 35000.00, 32000.00, '2024-02-10', 'Excelente', 'Bodega A-2', NULL, 'QSC-KS118-001', 'QSCKS118001', 'Audio', '{\"potencia\": \"3600W\", \"pulgadas\": 18, \"tipo\": \"Subwoofer activo\"}', 'disponible', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(12, 'ejemplo', NULL, 'APPLE', 'E5000', NULL, 1.00, 1.00, 1.00, 1.00, 1.00, '2025-10-16', 'mala', 'unate', NULL, NULL, '34506ll', 'Equipos de Sonido', '{\"processor\":\"we\",\"ram\":\"we\",\"storage\":\"wewee\",\"graphics\":\"rtt\",\"screen\":\"yu\",\"os\":\"uuu\"}', 'disponible', '2025-10-17 07:00:02', '2025-10-17 07:00:02'),
(13, 'test', NULL, 'TEST', 'test', NULL, 1.00, 1.00, 1.00, 1.00, 1.00, '0001-01-01', 'excelente', NULL, NULL, NULL, 'test', 'Iluminación', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-10-17 07:11:16', '2025-10-23 07:11:05'),
(14, 'TEST2', NULL, 'TEST2', 'TEST', NULL, 1.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, NULL, NULL, '11', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'alquilado', '2025-10-23 07:15:37', '2025-10-23 07:17:32');

--
-- Disparadores `producto`
--
DELIMITER $$
CREATE TRIGGER `trg_producto_after_insert` AFTER INSERT ON `producto` FOR EACH ROW BEGIN
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
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_producto_after_update` AFTER UPDATE ON `producto` FOR EACH ROW BEGIN
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
        
        -- Registrar en auditoría
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
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_subcategoria`
--

CREATE TABLE `producto_subcategoria` (
  `producto_id` int(11) NOT NULL,
  `subcategoria_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `producto_subcategoria`
--

INSERT INTO `producto_subcategoria` (`producto_id`, `subcategoria_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 5),
(5, 4),
(6, 7),
(7, 6),
(8, 8),
(9, 7),
(10, 1);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`id`, `nombre_empresa`, `rfc`, `nombre_contacto`, `telefono_contacto`, `correo_contacto`, `estado_proveedor`, `created_at`, `updated_at`) VALUES
(1, 'Audio Pro México', 'APM010203XYZ', 'Pedro Ramirez', '3312345678', 'pedro@audiopro.mx', 'Activo', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 'Iluminación Espectacular', 'IES050607ABC', 'Laura Juarez', '8187654321', 'laura@iluminacion.com', 'Activo', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 'Mobiliario Eventos MX', 'MEM080912PQR', 'Carmen Vega', '5577889900', 'carmen@mobiliariomx.com', 'Activo', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(4, 'Tech Video Solutions', 'TVS151218STU', 'Miguel Torres', '8199001122', 'miguel@techvideo.com', 'Activo', '2025-09-19 05:24:18', '2025-09-19 05:24:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor_direccion`
--

CREATE TABLE `proveedor_direccion` (
  `proveedor_id` int(11) NOT NULL,
  `direccion_id` int(11) NOT NULL,
  `es_principal` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `proveedor_direccion`
--

INSERT INTO `proveedor_direccion` (`proveedor_id`, `direccion_id`, `es_principal`) VALUES
(1, 2, 1),
(2, 1, 1),
(3, 4, 1),
(4, 5, 1);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `renta`
--

INSERT INTO `renta` (`id`, `cliente_id`, `cotizacion_id`, `fecha_inicio`, `fecha_fin_prevista`, `fecha_devolucion_real`, `estado_renta`, `monto_total_renta`, `deposito_garantia`, `notas`, `created_at`, `updated_at`) VALUES
(1, 2, NULL, '2025-09-20 10:00:00', '2025-09-22 18:00:00', NULL, 'Programada', 5000.00, 1000.00, NULL, '2025-09-19 05:24:18', '2025-09-19 05:24:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `renta_inventario_item`
--

CREATE TABLE `renta_inventario_item` (
  `renta_id` int(11) NOT NULL,
  `inventario_item_id` int(11) NOT NULL,
  `precio_renta_item` decimal(10,2) NOT NULL,
  `condicion_salida` text DEFAULT NULL,
  `condicion_regreso` text DEFAULT NULL,
  `notas` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id`, `codigo`, `nombre`, `descripcion`, `created_at`, `updated_at`) VALUES
(1, 'ADMIN', 'Administrador', 'Rol con todos los permisos en el sistema.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 'OPER', 'Operador', 'Rol para la gestión de inventario y rentas.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 'VENT', 'Ventas', 'Rol para la gestión de clientes y cotizaciones.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(4, 'CLI', 'Cliente', 'Rol para clientes que acceden al portal.', '2025-09-19 05:24:18', '2025-09-19 05:24:18');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `solicitud`
--

INSERT INTO `solicitud` (`id`, `cliente_id`, `fecha_solicitud`, `nombre_producto_alternativo`, `cantidad_solicitada`, `descripcion_necesidad`, `estado_solicitud`, `created_at`, `updated_at`) VALUES
(1, 1, '2025-09-19 00:24:18', NULL, 1, 'Necesito cotización para un sistema de sonido para una fiesta de 100 personas.', 'Nueva', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 2, '2025-09-19 00:24:18', NULL, 1, 'Requiero equipo de video para una conferencia, incluyendo pantalla y proyector.', 'EnProceso', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 4, '2025-09-18 14:30:00', 'Sistema de sonido completo', 1, 'Necesito un sistema completo para una presentación de producto con 200 asistentes', 'Nueva', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(4, 5, '2025-09-17 10:15:00', 'Mobiliario para boda', 50, 'Requiero mesas y sillas para una boda de 150 personas en jardín', 'EnProceso', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(5, 1, '2025-09-16 16:45:00', 'Iluminación LED', 20, 'Luces de colores para fiesta de cumpleaños en casa', 'Atendida', '2025-09-19 05:24:18', '2025-09-19 05:24:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `solicitud_producto`
--

CREATE TABLE `solicitud_producto` (
  `solicitud_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `solicitud_producto`
--

INSERT INTO `solicitud_producto` (`solicitud_id`, `producto_id`) VALUES
(1, 1),
(2, 4),
(3, 1),
(3, 2),
(3, 10),
(4, 6),
(4, 7),
(5, 5);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `subcategoria`
--

INSERT INTO `subcategoria` (`id`, `nombre`, `descripcion`, `created_at`, `updated_at`) VALUES
(1, 'Altavoces Activos', 'Altavoces con amplificador integrado.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(2, 'Consolas Digitales', 'Mesas de mezcla de audio digitales.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(3, 'Micrófonos Inalámbricos', 'Micrófonos sin cables para mayor movilidad.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(4, 'Luces LED', 'Sistemas de iluminación base LED.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(5, 'Proyectores HD', 'Proyectores de alta definición.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(6, 'Sillas Plegables', 'Sillas para todo tipo de evento.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(7, 'Mesas Redondas', 'Mesas para banquetes y eventos.', '2025-09-19 05:24:18', '2025-09-19 05:24:18'),
(8, 'Pantallas LED', 'Pantallas modulares para video en gran formato.', '2025-09-19 05:24:18', '2025-09-19 05:24:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nombre_usuario` varchar(100) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `nombre2` varchar(100) NOT NULL,
  `apellido1` varchar(100) NOT NULL,
  `apellido2` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `departamento` varchar(100) DEFAULT NULL,
  `estado` enum('Activo','Inactivo','Bloqueado') DEFAULT 'Activo',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuario`
--
-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_rol`
--

CREATE TABLE `usuario_rol` (
  `usuario_id` int(11) NOT NULL,
  `rol_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuario_rol`
--

INSERT INTO `usuario_rol` (`usuario_id`, `rol_id`) VALUES
(1, 1),
(5, 1),
(6, 2),
(7, 1);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_clientes_completa`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_clientes_completa` (
`id` int(11)
,`nombre` varchar(200)
,`nombre2` varchar(100)
,`apellido1` varchar(100)
,`apellido2` varchar(100)
,`rfc` varchar(13)
,`telefono_principal` varchar(20)
,`correo_electronico` varchar(100)
,`tipo_cliente` enum('Particular','Empresa')
,`estado_cliente` enum('Activo','Inactivo','Prospecto')
,`calle` varchar(255)
,`numero_exterior` varchar(20)
,`numero_interior` varchar(20)
,`colonia` varchar(100)
,`ciudad` varchar(100)
,`estado_provincia` varchar(100)
,`codigo_postal` varchar(10)
,`pais` varchar(100)
,`referencias` text
,`created_at` timestamp
,`updated_at` timestamp
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_clientes_completa`
--
DROP TABLE IF EXISTS `vista_clientes_completa`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_clientes_completa`  AS SELECT `c`.`id` AS `id`, `c`.`nombre` AS `nombre`, `c`.`nombre2` AS `nombre2`, `c`.`apellido1` AS `apellido1`, `c`.`apellido2` AS `apellido2`, `c`.`rfc` AS `rfc`, `c`.`telefono_principal` AS `telefono_principal`, `c`.`correo_electronico` AS `correo_electronico`, `c`.`tipo_cliente` AS `tipo_cliente`, `c`.`estado_cliente` AS `estado_cliente`, `d`.`calle` AS `calle`, `d`.`numero_exterior` AS `numero_exterior`, `d`.`numero_interior` AS `numero_interior`, `d`.`colonia` AS `colonia`, `d`.`ciudad` AS `ciudad`, `d`.`estado_provincia` AS `estado_provincia`, `d`.`codigo_postal` AS `codigo_postal`, `d`.`pais` AS `pais`, `d`.`referencias` AS `referencias`, `c`.`created_at` AS `created_at`, `c`.`updated_at` AS `updated_at` FROM ((`cliente` `c` left join `cliente_direccion` `cd` on(`c`.`id` = `cd`.`cliente_id` and `cd`.`es_principal` = 1)) left join `direccion` `d` on(`cd`.`direccion_id` = `d`.`id`)) ;

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
  ADD UNIQUE KEY `correo_electronico` (`correo_electronico`);

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
  ADD KEY `fk_cotizacion_solicitud` (`solicitud_id`);

--
-- Indices de la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_cotizacion_id` (`cotizacion_id`),
  ADD KEY `fk_detalle_cotizacion_producto` (`producto_id`);

--
-- Indices de la tabla `devolucion`
--
ALTER TABLE `devolucion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_renta_id` (`renta_id`);

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
  ADD KEY `idx_direccion_id` (`direccion_id`);

--
-- Indices de la tabla `inventario_item`
--
ALTER TABLE `inventario_item`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero_serie` (`numero_serie`),
  ADD KEY `idx_producto_id` (`producto_id`);

--
-- Indices de la tabla `mantenimiento`
--
ALTER TABLE `mantenimiento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_inventario_item_id` (`inventario_item_id`);

--
-- Indices de la tabla `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sku` (`sku`),
  ADD UNIQUE KEY `numero_serie` (`numero_serie`);

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
-- Indices de la tabla `proveedor_direccion`
--
ALTER TABLE `proveedor_direccion`
  ADD PRIMARY KEY (`proveedor_id`,`direccion_id`),
  ADD KEY `direccion_id` (`direccion_id`);

--
-- Indices de la tabla `renta`
--
ALTER TABLE `renta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_cliente_id` (`cliente_id`),
  ADD KEY `fk_renta_cotizacion` (`cotizacion_id`);

--
-- Indices de la tabla `renta_inventario_item`
--
ALTER TABLE `renta_inventario_item`
  ADD PRIMARY KEY (`renta_id`,`inventario_item_id`),
  ADD KEY `inventario_item_id` (`inventario_item_id`);

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
  ADD KEY `idx_cliente_id` (`cliente_id`);

--
-- Indices de la tabla `solicitud_producto`
--
ALTER TABLE `solicitud_producto`
  ADD PRIMARY KEY (`solicitud_id`,`producto_id`),
  ADD KEY `producto_id` (`producto_id`);

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
  ADD UNIQUE KEY `email` (`email`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `cotizacion`
--
ALTER TABLE `cotizacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `devolucion`
--
ALTER TABLE `devolucion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `direccion`
--
ALTER TABLE `direccion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `entrega`
--
ALTER TABLE `entrega`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `inventario_item`
--
ALTER TABLE `inventario_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `mantenimiento`
--
ALTER TABLE `mantenimiento`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `renta`
--
ALTER TABLE `renta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `solicitud`
--
ALTER TABLE `solicitud`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `subcategoria`
--
ALTER TABLE `subcategoria`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

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
  ADD CONSTRAINT `fk_cotizacion_solicitud` FOREIGN KEY (`solicitud_id`) REFERENCES `solicitud` (`id`);

--
-- Filtros para la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  ADD CONSTRAINT `fk_detalle_cotizacion_cotizacion` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizacion` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_detalle_cotizacion_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`);

--
-- Filtros para la tabla `devolucion`
--
ALTER TABLE `devolucion`
  ADD CONSTRAINT `fk_devolucion_renta` FOREIGN KEY (`renta_id`) REFERENCES `renta` (`id`);

--
-- Filtros para la tabla `entrega`
--
ALTER TABLE `entrega`
  ADD CONSTRAINT `fk_entrega_direccion` FOREIGN KEY (`direccion_id`) REFERENCES `direccion` (`id`),
  ADD CONSTRAINT `fk_entrega_renta` FOREIGN KEY (`renta_id`) REFERENCES `renta` (`id`);

--
-- Filtros para la tabla `inventario_item`
--
ALTER TABLE `inventario_item`
  ADD CONSTRAINT `fk_inventario_item_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`);

--
-- Filtros para la tabla `mantenimiento`
--
ALTER TABLE `mantenimiento`
  ADD CONSTRAINT `fk_mantenimiento_inventario_item` FOREIGN KEY (`inventario_item_id`) REFERENCES `inventario_item` (`id`);

--
-- Filtros para la tabla `producto_subcategoria`
--
ALTER TABLE `producto_subcategoria`
  ADD CONSTRAINT `fk_producto_subcategoria_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_producto_subcategoria_subcategoria` FOREIGN KEY (`subcategoria_id`) REFERENCES `subcategoria` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `proveedor_direccion`
--
ALTER TABLE `proveedor_direccion`
  ADD CONSTRAINT `fk_proveedor_direccion_direccion` FOREIGN KEY (`direccion_id`) REFERENCES `direccion` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_proveedor_direccion_proveedor` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedor` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `renta`
--
ALTER TABLE `renta`
  ADD CONSTRAINT `fk_renta_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`),
  ADD CONSTRAINT `fk_renta_cotizacion` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizacion` (`id`);

--
-- Filtros para la tabla `renta_inventario_item`
--
ALTER TABLE `renta_inventario_item`
  ADD CONSTRAINT `fk_rii_inventario_item` FOREIGN KEY (`inventario_item_id`) REFERENCES `inventario_item` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rii_renta` FOREIGN KEY (`renta_id`) REFERENCES `renta` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `solicitud`
--
ALTER TABLE `solicitud`
  ADD CONSTRAINT `fk_solicitud_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `cliente` (`id`);

--
-- Filtros para la tabla `solicitud_producto`
--
ALTER TABLE `solicitud_producto`
  ADD CONSTRAINT `fk_solicitud_producto_producto` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_solicitud_producto_solicitud` FOREIGN KEY (`solicitud_id`) REFERENCES `solicitud` (`id`) ON DELETE CASCADE;

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

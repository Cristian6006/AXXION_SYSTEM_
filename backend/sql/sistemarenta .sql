-- phpMyAdmin SQL Dump- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-11-2025 a las 22:02:15
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
-- Estructura de tabla para la tabla `auditoria_inventario`
--


--
-- Volcado de datos para la tabla `auditoria_inventario`
--







DROP DATABASE IF EXISTS sistemarenta;

CREATE DATABASE IF NOT EXISTS `sistemarenta` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `sistemarenta`;


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria`
--

CREATE TABLE  IF NOT EXISTS `categoria` (
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
(1, 'Equipos de Sonido', 'Altavoces, consolas, micrófonos y más.', 'Equipos', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(2, 'Iluminación', 'Luces para eventos, escenarios y fiestas.', 'Equipos', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(3, 'Video', 'Pantallas, proyectores y equipo de video.', 'Equipos', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(5, 'Herramientas Test', 'Categoría para pruebas', 'Producto', '2025-11-23 21:05:50', '2025-11-23 21:05:50'),
(6, 'w', 'w', 'w', '2025-11-24 00:00:54', '2025-11-24 00:00:54');

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
(1, 10),
(2, 4),
(3, 5),
(3, 8),
(5, 9);

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
(1, 'Carlos', 'Alberto', 'Sanchez', 'Ruiz', 'SARC850312XYZ', '5512345678', 'carlos.sanchez@email.com', 'Particular', 'Activo', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(2, 'Eventos', 'Grandes', 'SA de CV', '', 'EGR990115ABC', '5587654321', 'contacto@eventosgrandes.com', 'Empresa', 'Activo', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(3, 'Maria', 'Elena', 'García', 'Pérez', 'GAPM780520DEF', '5555667788', 'maria.garcia@email.com', 'Particular', 'Prospecto', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(4, 'Producciones', 'Audiovisuales', 'SA de CV', '', 'PAV120815GHI', '5544332211', 'contacto@prodav.com', 'Empresa', 'Activo', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(5, 'Luis', 'Fernando', 'Rodriguez', 'Morales', 'ROML850720JKL', '5599887766', 'luis.rodriguez@email.com', 'Particular', 'Activo', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(6, 'Eventos', 'Corporativos', 'Plus SA', '', 'ECP030910MNO', '5533445566', 'ventas@eventoscorp.com', 'Empresa', 'Prospecto', '2025-09-19 10:24:18', '2025-09-19 10:24:18');

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
(3, 1, 4, '2025-11-22 19:37:25', '2025-12-07', 1.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 00:37:26', '2025-11-23 00:37:26'),
(4, 1, 5, '2025-11-22 20:37:53', '2025-12-07', 1.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 01:37:54', '2025-11-23 01:37:54'),
(6, 3, 6, '2025-11-22 21:22:03', '2025-12-07', 3.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 02:22:04', '2025-11-23 02:22:04'),
(7, 1, 7, '2025-11-22 23:01:25', '2025-12-07', 30.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 04:01:26', '2025-11-23 04:01:26'),
(8, 1, 8, '2025-11-23 14:35:32', '2025-12-08', 50000.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 19:35:33', '2025-11-23 19:35:33'),
(9, 1, 9, '2025-11-23 15:00:50', '2025-12-08', 10000.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 20:00:51', '2025-11-23 20:00:51'),
(10, 1, 10, '2025-11-23 15:28:25', '2025-12-08', 1.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 20:28:26', '2025-11-23 20:28:26'),
(11, 2, 11, '2025-11-23 16:14:41', '2025-12-08', 1.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 21:14:42', '2025-11-23 21:14:42'),
(12, 2, 12, '2025-11-23 16:25:59', '2025-12-08', 2.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 21:26:00', '2025-11-23 21:26:00'),
(13, 1, 13, '2025-11-23 16:31:49', '2025-12-08', 33.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 21:31:51', '2025-11-23 21:31:51'),
(14, 3, 14, '2025-11-23 16:38:26', '2025-12-08', 4.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 21:38:27', '2025-11-23 21:38:27'),
(15, 1, 15, '2025-11-23 16:47:57', '2025-12-08', 11.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 21:47:58', '2025-11-23 21:47:58'),
(16, 2, 16, '2025-11-23 17:00:48', '2025-12-08', 4.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 22:00:49', '2025-11-23 22:00:49'),
(17, 3, 17, '2025-11-23 17:05:49', '2025-12-08', 15.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 22:05:50', '2025-11-23 22:05:50'),
(18, 1, 18, '2025-11-23 17:11:20', '2025-12-08', 11.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 22:11:21', '2025-11-23 22:11:21'),
(19, 1, 19, '2025-11-23 17:29:00', '2025-12-08', 4.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 22:29:01', '2025-11-23 22:29:01'),
(20, 2, 20, '2025-11-23 17:35:06', '2025-12-08', 4.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 22:35:07', '2025-11-23 22:35:07'),
(21, 2, 21, '2025-11-23 17:43:41', '2025-12-08', 11.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 22:43:43', '2025-11-23 22:43:43'),
(22, 1, 22, '2025-11-23 18:59:24', '2025-12-08', 4.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-23 23:59:25', '2025-11-23 23:59:25'),
(23, 5, 23, '2025-11-24 00:50:52', '2025-12-09', 11.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-24 05:50:53', '2025-11-24 05:50:53'),
(24, 1, 24, '2025-11-24 18:25:07', '2025-12-09', 15000.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-24 23:25:08', '2025-11-24 23:25:08'),
(25, 2, 25, '2025-11-24 18:41:42', '2025-12-09', 92000.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-24 23:41:43', '2025-11-24 23:41:43'),
(26, 2, 26, '2025-11-24 18:44:26', '2025-12-09', 22000.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-24 23:44:27', '2025-11-24 23:44:27'),
(27, 1, 27, '2025-11-24 19:27:58', '2025-12-09', 42000.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-25 00:27:59', '2025-11-25 00:27:59'),
(28, 2, 28, '2025-11-24 19:43:57', '2025-12-09', 28000.00, 'Borrador', 'Términos estándar de alquiler.', 'Generado automáticamente.', '2025-11-25 00:43:59', '2025-11-25 00:43:59');

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
(1, 3, 2, 't', 1, 1.00, 0.00, NULL, NULL, '2025-11-23 00:37:26', '2025-11-23 00:37:26'),
(2, 4, 2, 't', 1, 1.00, 0.00, NULL, NULL, '2025-11-23 01:37:54', '2025-11-23 01:37:54'),
(11, 6, 19, '23', 1, 3.00, 0.00, NULL, NULL, '2025-11-23 02:22:04', '2025-11-23 02:22:04'),
(12, 7, 20, 'Laptop Test', 1, 30.00, 0.00, NULL, NULL, '2025-11-23 04:01:26', '2025-11-23 04:01:26'),
(13, 8, 21, 'Laptop Dell Inspiron 15 3000', 1, 50000.00, 0.00, NULL, NULL, '2025-11-23 19:35:33', '2025-11-23 19:35:33'),
(14, 9, 22, 'test 1', 1, 10000.00, 0.00, NULL, NULL, '2025-11-23 20:00:51', '2025-11-23 20:00:51'),
(15, 10, 23, 'test', 1, 1.00, 0.00, NULL, NULL, '2025-11-23 20:28:26', '2025-11-23 20:28:26'),
(16, 11, 25, '1', 1, 1.00, 0.00, NULL, NULL, '2025-11-23 21:14:42', '2025-11-23 21:14:42'),
(17, 12, 26, '2', 1, 2.00, 0.00, NULL, NULL, '2025-11-23 21:26:00', '2025-11-23 21:26:00'),
(18, 13, 27, '33', 1, 33.00, 0.00, NULL, NULL, '2025-11-23 21:31:51', '2025-11-23 21:31:51'),
(19, 14, 28, '4', 1, 4.00, 0.00, NULL, NULL, '2025-11-23 21:38:27', '2025-11-23 21:38:27'),
(20, 15, 29, '123', 1, 11.00, 0.00, NULL, NULL, '2025-11-23 21:47:58', '2025-11-23 21:47:58'),
(21, 16, 28, '4', 1, 4.00, 0.00, NULL, NULL, '2025-11-23 22:00:49', '2025-11-23 22:00:49'),
(22, 17, 28, '4', 1, 4.00, 0.00, NULL, NULL, '2025-11-23 22:05:50', '2025-11-23 22:05:50'),
(23, 17, 29, '123', 1, 11.00, 0.00, NULL, NULL, '2025-11-23 22:05:50', '2025-11-23 22:05:50'),
(24, 18, 29, '123', 1, 11.00, 0.00, NULL, NULL, '2025-11-23 22:11:21', '2025-11-23 22:11:21'),
(25, 19, 28, '4', 1, 4.00, 0.00, NULL, NULL, '2025-11-23 22:29:01', '2025-11-23 22:29:01'),
(26, 20, 28, '4', 1, 4.00, 0.00, NULL, NULL, '2025-11-23 22:35:07', '2025-11-23 22:35:07'),
(27, 21, 29, '123', 1, 11.00, 0.00, NULL, NULL, '2025-11-23 22:43:43', '2025-11-23 22:43:43'),
(28, 22, 28, '4', 1, 4.00, 0.00, NULL, NULL, '2025-11-23 23:59:25', '2025-11-23 23:59:25'),
(29, 23, 29, '123', 1, 11.00, 0.00, NULL, NULL, '2025-11-24 05:50:53', '2025-11-24 05:50:53'),
(30, 24, 32, 'Escalera Telescópica Aluminio 5.8m', 1, 15000.00, 0.00, NULL, NULL, '2025-11-24 23:25:08', '2025-11-24 23:25:08'),
(31, 25, 33, 'Sierra Circular DeWalt DWE575', 1, 22000.00, 0.00, NULL, NULL, '2025-11-24 23:41:43', '2025-11-24 23:41:43'),
(32, 25, 34, 'Martillo Demoledor Hilti TE 500-AVR', 1, 42000.00, 0.00, NULL, NULL, '2025-11-24 23:41:43', '2025-11-24 23:41:43'),
(33, 25, 35, 'Compresor de Aire Stanley D200/10/24', 1, 28000.00, 0.00, NULL, NULL, '2025-11-24 23:41:43', '2025-11-24 23:41:43'),
(34, 26, 33, 'Sierra Circular DeWalt DWE575', 1, 22000.00, 0.00, NULL, NULL, '2025-11-24 23:44:27', '2025-11-24 23:44:27'),
(35, 27, 34, 'Martillo Demoledor Hilti TE 500-AVR', 1, 42000.00, 0.00, NULL, NULL, '2025-11-25 00:27:59', '2025-11-25 00:27:59'),
(36, 28, 35, 'Compresor de Aire Stanley D200/10/24', 1, 28000.00, 0.00, NULL, NULL, '2025-11-25 00:43:59', '2025-11-25 00:43:59');

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
(1, 'Av. Siempre Viva', '742', NULL, 'Springfield', 'Ciudad de México', 'CDMX', '01234', 'México', 'Frente al Kwik-E-Mart', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(2, 'Calle Falsa', '123', NULL, 'Centro', 'Guadalajara', 'Jalisco', '44100', 'México', 'Edificio de ladrillo rojo', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(3, 'Blvd. de los Sueños Rotos', '45', NULL, 'Zona Hotelera', 'Cancún', 'Quintana Roo', '77500', 'México', 'Cerca de la playa', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(4, 'Av. Insurgentes Sur', '1234', 'Piso 5', 'Del Valle', 'Ciudad de México', 'CDMX', '03100', 'México', 'Torre empresarial, entrada por estacionamiento', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(5, 'Calle Morelos', '567', NULL, 'Centro Histórico', 'Puebla', 'Puebla', '72000', 'México', 'Edificio colonial, portón verde', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(6, 'Blvd. Kukulcán', '89', 'Local 12', 'Zona Hotelera', 'Cancún', 'Quintana Roo', '77500', 'México', 'Plaza comercial, segundo nivel', '2025-09-19 10:24:18', '2025-09-19 10:24:18');

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
(36, 29, 'xxxxxxxxxxxxxx', 'Rentado', NULL, 0.00, NULL, 'Item creado automáticamente desde producto: 123', '2025-11-23 16:47:34', '2025-11-24 19:29:05'),
(39, 32, 'PM1122334455', 'EnMantenimiento', '2023-06-12', 620000.00, 'Bodega Central - Área F', 'Item creado automáticamente desde producto: Escalera Telescópica Aluminio 5.8m', '2025-11-24 01:28:41', '2025-11-25 00:29:19'),
(40, 33, 'DW5544332211', 'Rentado', '2023-04-15', 650000.00, 'Bodega Central - Estante B2', 'Item creado automáticamente desde producto: Sierra Circular DeWalt DWE575', '2025-11-24 01:29:19', '2025-11-24 18:44:52'),
(41, 34, 'HT7788990011', 'EnMantenimiento', '2023-01-10', 1850000.00, 'Bodega Central - Estante C1', 'Item creado automáticamente desde producto: Martillo Demoledor Hilti TE 500-AVR', '2025-11-24 01:29:57', '2025-11-25 00:44:42'),
(42, 35, 'ST2233445566', 'Rentado', '2023-03-22', 850000.00, 'Bodega Central - Área D', 'Item creado automáticamente desde producto: Compresor de Aire Stanley D200/10/24', '2025-11-24 01:30:27', '2025-11-24 19:44:10');

--
-- Disparadores `inventario_item`
--
DELIMITER $$
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
            'CORRECTIVO',  
            'Mantenimiento generado autom├íticamente por cambio de estado',
            'PROGRAMADO',  
            'Sistema',
            NOW(),
            NOW()
        );
    END IF;
END
$$
DELIMITER ;

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
  `estado_mantenimiento` varchar(20) DEFAULT NULL,
  `responsable` varchar(150) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `mantenimiento`
--

INSERT INTO `mantenimiento` (`id`, `inventario_item_id`, `fecha_inicio`, `fecha_fin_prevista`, `fecha_fin_real`, `tipo_mantenimiento`, `descripcion_problema`, `descripcion_trabajo_realizado`, `costo_estimado`, `costo_real`, `estado_mantenimiento`, `responsable`, `created_at`, `updated_at`) VALUES
(28, 39, '2025-11-24 00:00:00', '2025-11-27', NULL, 'Preventivo', 'Mantenimiento generado autom├íticamente por cambio de estado', NULL, NULL, NULL, 'PROGRAMADO', 'Sistema', '2025-11-24 19:29:19', '2025-11-25 00:29:50'),
(29, 41, '2025-11-24 00:00:00', '2025-11-27', NULL, 'Preventivo', 'Mantenimiento generado autom├íticamente por cambio de estado', NULL, NULL, NULL, 'EN_PROCESO', 'Sistema', '2025-11-24 19:44:42', '2025-11-25 00:45:12');

--
-- Disparadores `mantenimiento`
--
DELIMITER $$
CREATE TRIGGER `trg_mantenimiento_after_delete` AFTER DELETE ON `mantenimiento` FOR EACH ROW BEGIN
    
    UPDATE `inventario_item`
    SET `estado_item` = 'Disponible'
    WHERE `id` = OLD.inventario_item_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_mantenimiento_after_update` AFTER UPDATE ON `mantenimiento` FOR EACH ROW BEGIN
    
    IF (NEW.estado_mantenimiento = 'Finalizado' OR NEW.estado_mantenimiento = 'COMPLETADO') 
       AND (OLD.estado_mantenimiento != 'Finalizado' AND OLD.estado_mantenimiento != 'COMPLETADO') THEN
        UPDATE `inventario_item`
        SET `estado_item` = 'Disponible'
        WHERE `id` = NEW.inventario_item_id;
    END IF;
END
$$
DELIMITER ;

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
(2, 't', 'Item creado automáticamente desde producto: t', 't', 't', NULL, 1.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, 'Item creado automáticamente desde producto: t', NULL, 't', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 00:26:27', '2025-11-23 01:39:40'),
(19, '23', NULL, '3', '3', NULL, 3.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, NULL, NULL, '3', 'Equipos de Sonido', '{\"processor\":\"3\",\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 02:21:40', '2025-11-23 02:21:40'),
(20, 'Laptop Test', 'Item creado automáticamente desde producto: Laptop Test', 'TestBrand', 'TestModel X1', NULL, 30.00, 180.00, 600.00, 0.00, 0.00, NULL, 'excelente', NULL, 'Item creado automáticamente desde producto: Laptop Test', NULL, 'SN-TEST-001', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 03:32:10', '2025-11-23 19:32:07'),
(21, 'Laptop Dell Inspiron 15 3000', 'Actualizado desde producto: Laptop Dell Inspiron 15 3000 - 2025-11-23 09:24:51', 'Dell', 'Inspiron 15 3000', NULL, 50000.00, 300000.00, 0.00, 0.00, 0.00, NULL, 'excelente', 'Almacén Principal', 'Actualizado desde producto: Laptop Dell Inspiron 15 3000 - 2025-11-23 09:24:51', NULL, 'DL001234567', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 04:00:53', '2025-11-23 19:33:15'),
(22, 'test 1', 'Item creado automáticamente desde producto: test 1', 'test 1', 'test 1', NULL, 10000.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, 'Item creado automáticamente desde producto: test 1', NULL, 'test 1', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 20:00:12', '2025-11-23 20:01:40'),
(23, 'test', NULL, 'test', 'test', NULL, 1.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, NULL, NULL, 'test', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 20:27:53', '2025-11-23 20:27:53'),
(24, 'Producto Prueba Flujo', NULL, 'Marca Test', 'Modelo Test', NULL, 100.00, 0.00, 0.00, 1000.00, 0.00, '2025-11-23', 'excelente', 'Bodega', NULL, NULL, 'SERIE-TEST-FLOW-001', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 20:58:27', '2025-11-23 20:58:27'),
(25, '1', 'Item creado automáticamente desde producto: 1', '1', '1', NULL, 1.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, 'Item creado automáticamente desde producto: 1', NULL, '1', 'Herramientas Test', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 21:14:16', '2025-11-23 21:18:11'),
(26, '2', NULL, '2', '2', NULL, 2.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, NULL, NULL, '2', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 21:25:34', '2025-11-23 21:25:34'),
(27, '33', NULL, '33', '33', NULL, 33.00, 3.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, NULL, NULL, '33', 'Equipos de Sonido', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 21:31:31', '2025-11-23 21:31:31'),
(28, '4', 'Actualizado desde producto: 4 - 2025-11-23 19:36:40', '4', '4', NULL, 4.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, 'Actualizado desde producto: 4 - 2025-11-23 19:36:40', NULL, '4', 'Iluminación', '{\"processor\":\"4\",\"ram\":\"4\",\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 21:38:02', '2025-11-24 06:09:59'),
(29, '123', NULL, '123', 'xxxxxx', NULL, 11.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, NULL, NULL, 'xxxxxxxxxxxxxx', 'Herramientas Test', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 21:47:34', '2025-11-23 21:47:34'),
(30, '123', NULL, '134', '32', NULL, 2.00, 0.00, 0.00, 0.00, 0.00, NULL, 'excelente', NULL, NULL, NULL, '33223', 'Herramientas Test', '{\"processor\":null,\"ram\":null,\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-23 23:58:32', '2025-11-23 23:58:32'),
(31, 't44', NULL, 't44', 't44', NULL, 444.00, 4444.00, 44.00, 0.00, 0.00, NULL, 'excelente', NULL, NULL, NULL, 't44', 'w', '{\"processor\":\"t\",\"ram\":\"t\",\"storage\":null,\"graphics\":null,\"screen\":null,\"os\":null}', 'disponible', '2025-11-24 05:34:50', '2025-11-24 05:34:50'),
(32, 'Escalera Telescópica Aluminio 5.8m', 'Escalera telescópica multiposición en aluminio, se extiende hasta 5.8m.', 'Promart', 'ETL-58', 12000.00, 15000.00, 78000.00, 265000.00, 620000.00, 550000.00, '2023-06-12', 'Usado - Buen estado', 'Bodega Central - Área F', 'Verificar seguro de peldaños. Capacidad 150kg.', 'ESC-PROMART-001', 'PM1122334455', 'Equipos de Acceso', '[\"Altura m\\u00e1xima: 5.8m, Material: Aluminio, Capacidad: 150kg\"]', 'disponible', '2025-11-24 06:28:41', '2025-11-24 06:28:41'),
(33, 'Sierra Circular DeWalt DWE575', 'Sierra circular de 7.25 pulgadas con motor de 1950W para cortes precisos.', 'DeWalt', 'DWE575', 18000.00, 22000.00, 115000.00, 390000.00, 650000.00, 550000.00, '2023-04-15', 'Usado - Excelente estado', 'Bodega Central - Estante B2', 'Incluye disco de corte premium y guía paralela.', 'SRC-DEWALT-001', 'DW5544332211', 'Herramientas Eléctricas', '[\"Potencia: 1950W, Disco: 7.25 pulgadas, Profundidad de corte: 65mm\"]', 'disponible', '2025-11-24 06:29:19', '2025-11-24 06:29:19'),
(34, 'Martillo Demoledor Hilti TE 500-AVR', 'Martillo demoledor profesional de 1100W con sistema de reducción de vibración.', 'Hilti', 'TE 500-AVR', 35000.00, 42000.00, 220000.00, 750000.00, 1850000.00, 1600000.00, '2023-01-10', 'Usado - Buen estado', 'Bodega Central - Estante C1', 'Sistema anti-vibración. Incluye 3 cinceles y maletín.', 'MRT-HILTI-001', 'HT7788990011', 'Herramientas Eléctricas', '[\"Potencia: 1100W, Impactos: 2900 ipm, Peso: 5.8kg\"]', 'disponible', '2025-11-24 06:29:57', '2025-11-24 06:29:57'),
(35, 'Compresor de Aire Stanley D200/10/24', 'Compresor portátil de 24 litros, ideal para herramientas neumáticas.', 'Stanley', 'D200/10/24', 22000.00, 28000.00, 145000.00, 490000.00, 850000.00, 720000.00, '2023-03-22', 'Usado - Buen estado', 'Bodega Central - Área D', 'Revisar nivel de aceite antes de cada uso. Incluye manguera.', 'CMP-STANLEY-001', 'ST2233445566', 'Equipos de Aire', '[\"Capacidad: 24L, Presi\\u00f3n: 8 bar, Potencia: 1.5HP\"]', 'disponible', '2025-11-24 06:30:27', '2025-11-24 06:30:27');

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
(1, 'Audio Pro México', 'APM010203XYZ', 'Pedro Ramirez', '3312345678', 'pedro@audiopro.mx', 'Activo', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(2, 'Iluminación Espectacular', 'IES050607ABC', 'Laura Juarez', '8187654321', 'laura@iluminacion.com', 'Activo', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(3, 'Mobiliario Eventos MX', 'MEM080912PQR', 'Carmen Vega', '5577889900', 'carmen@mobiliariomx.com', 'Activo', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(4, 'Tech Video Solutions', 'TVS151218STU', 'Miguel Torres', '8199001122', 'miguel@techvideo.com', 'Activo', '2025-09-19 10:24:18', '2025-09-19 10:24:18');

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
(24, 5, NULL, '2025-11-24 00:51:00', '2025-12-09 00:00:00', NULL, 'EnCurso', 11.00, 0.00, 'Generado desde Cotización #23', '2025-11-24 05:51:01', '2025-11-25 00:29:05'),
(25, 1, NULL, '2025-11-24 18:26:00', '2025-12-09 00:00:00', NULL, 'Finalizada', 15000.00, 0.00, 'Generado desde Cotización #24', '2025-11-24 23:26:48', '2025-11-25 00:29:19'),
(26, 2, 26, '2025-11-24 18:44:51', '2025-12-09 00:00:00', NULL, 'Programada', 22000.00, 0.00, 'Generado desde Cotización #26', '2025-11-24 23:44:52', '2025-11-24 23:44:52'),
(27, 1, NULL, '2025-11-24 19:28:00', '2025-12-09 00:00:00', NULL, 'Finalizada', 42000.00, 0.00, 'Generado desde Cotización #27', '2025-11-25 00:28:26', '2025-11-25 00:44:42'),
(28, 2, 28, '2025-11-24 19:44:09', '2025-12-09 00:00:00', NULL, 'Programada', 28000.00, 0.00, 'Generado desde Cotización #28', '2025-11-25 00:44:10', '2025-11-25 00:44:10');

--
-- Disparadores `renta`
--
DELIMITER $$
CREATE TRIGGER `trg_renta_after_insert` AFTER INSERT ON `renta` FOR EACH ROW BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_producto_id INT;
    DECLARE v_inventario_item_id INT;
    DECLARE v_precio_renta DECIMAL(10,2);
    DECLARE v_cantidad INT;
    
    -- Cursor para obtener productos de la cotización
    DECLARE cur_productos CURSOR FOR
        SELECT dc.producto_id, dc.cantidad, dc.precio_unitario
        FROM detalle_cotizacion dc
        WHERE dc.cotizacion_id = NEW.cotizacion_id;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- Solo procesar si hay cotización asociada
    IF NEW.cotizacion_id IS NOT NULL THEN
        OPEN cur_productos;
        
        read_loop: LOOP
            FETCH cur_productos INTO v_producto_id, v_cantidad, v_precio_renta;
            IF done THEN
                LEAVE read_loop;
            END IF;
            
            -- Buscar items de inventario disponibles para este producto
            -- Insertar tantos items como cantidad solicitada
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
            
            -- Actualizar estado de los items asignados a Rentado
            UPDATE inventario_item ii
            SET ii.estado_item = 'Rentado',
                ii.updated_at = NOW()
            WHERE ii.id IN (
                SELECT rii.inventario_item_id
                FROM renta_inventario_item rii
                WHERE rii.renta_id = NEW.id
                    AND rii.inventario_item_id IN (
                        SELECT id FROM inventario_item
                        WHERE producto_id = v_producto_id
                    )
            );
            
        END LOOP;
        
        CLOSE cur_productos;
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_renta_after_update` AFTER UPDATE ON `renta` FOR EACH ROW BEGIN
    -- Si la renta cambia a estado Finalizada o Cancelada, liberar los items
    IF (NEW.estado_renta IN ('Finalizada', 'Cancelada')) AND 
       (OLD.estado_renta NOT IN ('Finalizada', 'Cancelada')) THEN
        
        -- Actualizar estado de items a Disponible
        UPDATE inventario_item ii
        SET ii.estado_item = 'Disponible',
            ii.updated_at = NOW()
        WHERE ii.id IN (
            SELECT rii.inventario_item_id
            FROM renta_inventario_item rii
            WHERE rii.renta_id = NEW.id
        );
    END IF;
    
    -- Si la renta cambia de Programada a EnCurso, asegurar que items estén Rentado
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
END
$$
DELIMITER ;

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

--
-- Volcado de datos para la tabla `renta_inventario_item`
--

INSERT INTO `renta_inventario_item` (`renta_id`, `inventario_item_id`, `precio_renta_item`, `condicion_salida`, `condicion_regreso`, `notas`) VALUES
(24, 36, 11.00, 'Buena', NULL, 'Asignado automáticamente desde renta ID: 24'),
(25, 39, 15000.00, 'Buena', NULL, 'Asignado automáticamente desde renta ID: 25'),
(26, 40, 22000.00, 'Buena', NULL, 'Asignado automáticamente desde renta ID: 26'),
(27, 41, 42000.00, 'Buena', NULL, 'Asignado automáticamente desde renta ID: 27'),
(28, 42, 28000.00, 'Buena', NULL, 'Asignado automáticamente desde renta ID: 28');

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
(1, 'ADMIN', 'Administrador', 'Rol con todos los permisos en el sistema.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(2, 'OPER', 'Operador', 'Rol para la gestión de inventario y rentas.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(3, 'VENT', 'Ventas', 'Rol para la gestión de clientes y cotizaciones.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(4, 'CLI', 'Cliente', 'Rol para clientes que acceden al portal.', '2025-09-19 10:24:18', '2025-09-19 10:24:18');

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
(1, 1, '2025-11-22 19:31:46', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-27T14:27 a 2025-11-27T14:27', 'Nueva', '2025-11-23 00:31:47', '2025-11-23 00:31:47'),
(2, 1, '2025-11-22 19:34:08', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-27T14:27 a 2025-11-27T14:27', 'Nueva', '2025-11-23 00:34:09', '2025-11-23 00:34:09'),
(3, 1, '2025-11-22 19:36:04', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-22T14:35 a 2025-11-22T18:36', 'Nueva', '2025-11-23 00:36:05', '2025-11-23 00:36:05'),
(4, 1, '2025-11-22 19:37:24', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-22T14:35 a 2025-11-22T18:36', 'Nueva', '2025-11-23 00:37:25', '2025-11-23 00:37:25'),
(5, 1, '2025-11-22 20:37:52', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-28T15:37 a 2025-11-28T15:37', 'Nueva', '2025-11-23 01:37:53', '2025-11-23 01:37:53'),
(6, 3, '2025-11-22 21:22:02', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-28T16:21 a 2025-11-27T16:21', 'Nueva', '2025-11-23 02:22:03', '2025-11-23 02:22:03'),
(7, 1, '2025-11-22 23:01:23', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-01-20T10:00 a 2025-01-25T18:00', 'Nueva', '2025-11-23 04:01:25', '2025-11-23 04:01:25'),
(8, 1, '2025-11-23 14:35:31', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T09:35 a 2025-12-01T09:35', 'Nueva', '2025-11-23 19:35:32', '2025-11-23 19:35:32'),
(9, 1, '2025-11-23 15:00:49', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-24T10:00 a 2025-11-26T10:00', 'Nueva', '2025-11-23 20:00:50', '2025-11-23 20:00:50'),
(10, 1, '2025-11-23 15:28:24', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T10:28 a 2025-11-23T10:28', 'Nueva', '2025-11-23 20:28:25', '2025-11-23 20:28:25'),
(11, 2, '2025-11-23 16:14:40', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-21T11:14 a 2025-11-22T11:14', 'Nueva', '2025-11-23 21:14:41', '2025-11-23 21:14:41'),
(12, 2, '2025-11-23 16:25:58', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T11:25 a 2025-12-01T11:25', 'Nueva', '2025-11-23 21:25:59', '2025-11-23 21:25:59'),
(13, 1, '2025-11-23 16:31:49', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-25T11:31 a 2025-11-19T11:31', 'Nueva', '2025-11-23 21:31:49', '2025-11-23 21:31:49'),
(14, 3, '2025-11-23 16:38:25', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T11:38 a 2025-11-25T05:38', 'Nueva', '2025-11-23 21:38:26', '2025-11-23 21:38:26'),
(15, 1, '2025-11-23 16:47:56', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-24T11:47 a 2025-11-24T11:47', 'Nueva', '2025-11-23 21:47:57', '2025-11-23 21:47:57'),
(16, 2, '2025-11-23 17:00:47', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-24T12:00 a 2025-11-26T12:00', 'Nueva', '2025-11-23 22:00:48', '2025-11-23 22:00:48'),
(17, 3, '2025-11-23 17:05:48', NULL, 2, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T18:05 a 2025-11-24T12:05', 'Nueva', '2025-11-23 22:05:49', '2025-11-23 22:05:49'),
(18, 1, '2025-11-23 17:11:19', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T12:11 a 2025-11-24T12:11', 'Nueva', '2025-11-23 22:11:20', '2025-11-23 22:11:20'),
(19, 1, '2025-11-23 17:28:59', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T12:28 a 2025-11-17T12:28', 'Nueva', '2025-11-23 22:29:00', '2025-11-23 22:29:00'),
(20, 2, '2025-11-23 17:35:05', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T12:34 a 2025-11-23T12:35', 'Nueva', '2025-11-23 22:35:06', '2025-11-23 22:35:06'),
(21, 2, '2025-11-23 17:43:40', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-24T12:43 a 2025-11-24T12:43', 'Nueva', '2025-11-23 22:43:41', '2025-11-23 22:43:41'),
(22, 1, '2025-11-23 18:59:23', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-23T03:02 a 2025-11-24T13:59', 'Nueva', '2025-11-23 23:59:24', '2025-11-23 23:59:24'),
(23, 5, '2025-11-24 00:50:51', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-26T19:50 a 2025-12-01T19:50', 'Nueva', '2025-11-24 05:50:52', '2025-11-24 05:50:52'),
(24, 1, '2025-11-24 18:25:06', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-24T13:24 a 2025-12-03T13:25', 'Nueva', '2025-11-24 23:25:07', '2025-11-24 23:25:07'),
(25, 2, '2025-11-24 18:41:41', NULL, 3, 'Solicitud generada desde carrito web. Fechas: 2025-11-24T13:41 a 2025-11-26T13:41', 'Nueva', '2025-11-24 23:41:42', '2025-11-24 23:41:42'),
(26, 2, '2025-11-24 18:44:25', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-24T13:44 a 2025-11-24T13:44', 'Nueva', '2025-11-24 23:44:26', '2025-11-24 23:44:26'),
(27, 1, '2025-11-24 19:27:57', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-25T14:27 a 2025-11-25T14:27', 'Nueva', '2025-11-25 00:27:58', '2025-11-25 00:27:58'),
(28, 2, '2025-11-24 19:43:57', NULL, 1, 'Solicitud generada desde carrito web. Fechas: 2025-11-26T14:43 a 2025-11-25T14:43', 'Nueva', '2025-11-25 00:43:57', '2025-11-25 00:43:57');

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
(1, 2),
(2, 2),
(3, 2),
(4, 2),
(5, 2),
(6, 19),
(7, 20),
(8, 21),
(9, 22),
(10, 23),
(11, 25),
(12, 26),
(13, 27),
(14, 28),
(15, 29),
(16, 28),
(17, 28),
(17, 29),
(18, 29),
(19, 28),
(20, 28),
(21, 29),
(22, 28),
(23, 29),
(24, 32),
(25, 33),
(25, 34),
(25, 35),
(26, 33),
(27, 34),
(28, 35);

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
(1, 'Altavoces Activos', 'Altavoces con amplificador integrado.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(2, 'Consolas Digitales', 'Mesas de mezcla de audio digitales.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(3, 'Micrófonos Inalámbricos', 'Micrófonos sin cables para mayor movilidad.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(4, 'Luces LED', 'Sistemas de iluminación base LED.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(5, 'Proyectores HD', 'Proyectores de alta definición.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(6, 'Sillas Plegables', 'Sillas para todo tipo de evento.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(7, 'Mesas Redondas', 'Mesas para banquetes y eventos.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(8, 'Pantallas LED', 'Pantallas modulares para video en gran formato.', '2025-09-19 10:24:18', '2025-09-19 10:24:18'),
(9, 'Taladros', 'Subcategoría para taladros', '2025-11-23 21:06:39', '2025-11-23 21:06:39'),
(10, 'w', NULL, '2025-11-24 00:00:44', '2025-11-24 00:00:44');

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

INSERT INTO `usuario` (`id`, `nombre_usuario`, `nombre`, `nombre2`, `apellido1`, `apellido2`, `password_hash`, `email`, `telefono`, `departamento`, `estado`, `created_at`, `updated_at`) VALUES
(8, 'JULIAN', 'JULIAN', 'ANDRES', 'VARGAS', 'Gaona', '$2y$12$qRKCJCNc4N0cFNvkpUkAieNtjyZsQPeH3nBM.6fTaX/3qPYomJxOm', 'J@example.com', '22161510', 'Ventas', 'Activo', '2025-11-23 22:26:50', '2025-11-23 22:26:50'),
(11, 'c', 'c', 'c', 'c', 'c', '$2y$12$UQIK5gx8wZt9W3zrj21KVOti/A/RHRaPlAyGd6M3LJIlMKWd3g3ge', 'c@c.com', 'c', 'Tecnología', 'Activo', '2025-11-24 06:08:57', '2025-11-24 06:08:57');

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
(8, 1),
(11, 1);

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
CREATE TABLE `auditoria_inventario` (
  `id` int(11) NOT NULL,
  `inventario_item_id` int(11) DEFAULT NULL,
  `accion` varchar(50) DEFAULT NULL,
  `campos_cambiados` text DEFAULT NULL,
  `valores_anteriores` text DEFAULT NULL,
  `valores_nuevos` text DEFAULT NULL,
  `usuario` varchar(100) DEFAULT NULL,
  `fecha_cambio` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
--
-- Estructura para la vista `vista_clientes_completa`
--
DROP TABLE IF EXISTS `vista_clientes_completa`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_clientes_completa`  AS SELECT `c`.`id` AS `id`, `c`.`nombre` AS `nombre`, `c`.`nombre2` AS `nombre2`, `c`.`apellido1` AS `apellido1`, `c`.`apellido2` AS `apellido2`, `c`.`rfc` AS `rfc`, `c`.`telefono_principal` AS `telefono_principal`, `c`.`correo_electronico` AS `correo_electronico`, `c`.`tipo_cliente` AS `tipo_cliente`, `c`.`estado_cliente` AS `estado_cliente`, `d`.`calle` AS `calle`, `d`.`numero_exterior` AS `numero_exterior`, `d`.`numero_interior` AS `numero_interior`, `d`.`colonia` AS `colonia`, `d`.`ciudad` AS `ciudad`, `d`.`estado_provincia` AS `estado_provincia`, `d`.`codigo_postal` AS `codigo_postal`, `d`.`pais` AS `pais`, `d`.`referencias` AS `referencias`, `c`.`created_at` AS `created_at`, `c`.`updated_at` AS `updated_at` FROM ((`cliente` `c` left join `cliente_direccion` `cd` on(`c`.`id` = `cd`.`cliente_id` and `cd`.`es_principal` = 1)) left join `direccion` `d` on(`cd`.`direccion_id` = `d`.`id`)) ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `auditoria_inventario`
--
ALTER TABLE `auditoria_inventario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_inventario_item_id` (`inventario_item_id`),
  ADD KEY `idx_fecha_cambio` (`fecha_cambio`);

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
  ADD KEY `fk_cotizacion_cliente` (`cliente_id`),
  ADD KEY `fk_cotizacion_solicitud` (`solicitud_id`);

--
-- Indices de la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_detalle_cotizacion_cotizacion` (`cotizacion_id`),
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
  ADD PRIMARY KEY (`id`);

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
  ADD KEY `fk_renta_cliente` (`cliente_id`),
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
  ADD KEY `fk_solicitud_cliente` (`cliente_id`);

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
-- AUTO_INCREMENT de la tabla `auditoria_inventario`
--
ALTER TABLE `auditoria_inventario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `categoria`
--
ALTER TABLE `categoria`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `cotizacion`
--
ALTER TABLE `cotizacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT de la tabla `mantenimiento`
--
ALTER TABLE `mantenimiento`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT de la tabla `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT de la tabla `proveedor`
--
ALTER TABLE `proveedor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `renta`
--
ALTER TABLE `renta`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `rol`
--
ALTER TABLE `rol`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `solicitud`
--
ALTER TABLE `solicitud`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `subcategoria`
--
ALTER TABLE `subcategoria`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

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

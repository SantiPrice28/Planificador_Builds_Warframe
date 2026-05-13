-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.44 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.13.0.7147
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para planificador_warframe
CREATE DATABASE IF NOT EXISTS `planificador_warframe` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `planificador_warframe`;

-- Volcando estructura para tabla planificador_warframe.arcano
CREATE TABLE IF NOT EXISTS `arcano` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `rango_max` int DEFAULT NULL,
  `efecto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.arcano: ~0 rows (aproximadamente)

-- Volcando estructura para tabla planificador_warframe.arma
CREATE TABLE IF NOT EXISTS `arma` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `id_tipo_arma` int NOT NULL,
  `dano_impacto` decimal(10,2) DEFAULT NULL,
  `dano_perforante` decimal(10,2) DEFAULT NULL,
  `dano_cortante` decimal(10,2) DEFAULT NULL,
  `dano_frio` decimal(10,2) DEFAULT NULL,
  `dano_electrico` decimal(10,2) DEFAULT NULL,
  `dano_calor` decimal(10,2) DEFAULT NULL,
  `dano_toxina` decimal(10,2) DEFAULT NULL,
  `critico` decimal(4,2) DEFAULT NULL,
  `mult_critico` decimal(4,2) DEFAULT '1.50',
  `estado` decimal(4,2) DEFAULT NULL,
  `precision` decimal(6,3) DEFAULT '1.000',
  `cadencia` decimal(6,3) DEFAULT '1.000',
  `descripcion` text,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_tipo_arma` (`id_tipo_arma`),
  CONSTRAINT `arma_ibfk_1` FOREIGN KEY (`id_tipo_arma`) REFERENCES `tipo_arma` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.arma: ~8 rows (aproximadamente)
REPLACE INTO `arma` (`id`, `nombre`, `id_tipo_arma`, `dano_impacto`, `dano_perforante`, `dano_cortante`, `dano_frio`, `dano_electrico`, `dano_calor`, `dano_toxina`, `critico`, `mult_critico`, `estado`, `precision`, `cadencia`, `descripcion`) VALUES
	(1, 'Tigris', 2, 21.00, 21.00, 168.00, 0.00, 0.00, 0.00, 0.00, 0.10, 2.00, 0.28, 1.000, 1.000, 'La escopeta de doble cañon Tigris dispara dos rafagas en una sucesion rapida, derribando con facilidad a la presa mas dura.'),
	(2, 'Braton', 1, 7.00, 7.00, 8.00, 0.00, 0.00, 0.00, 0.00, 0.12, 1.60, 0.08, 1.000, 9.500, 'La alta cadencia de fuego y precision del Braton lo convierte en el favorito entre los tenno.'),
	(3, 'Lato', 4, 10.00, 10.00, 20.00, 0.00, 0.00, 0.00, 0.00, 0.10, 1.80, 0.06, 1.000, 5.000, 'La Lato es una pistola altamente precisa usada por los tenno en todas partes.'),
	(4, 'Skana', 5, 11.25, 11.25, 67.50, 0.00, 0.00, 0.00, 0.00, 0.10, 1.50, 0.10, 1.000, 1.000, 'Una espada tradicional Tenno. El arma blanca basica pero eficaz.'),
	(5, 'Orthos', 5, 16.00, 16.00, 128.00, 0.00, 0.00, 0.00, 0.00, 0.05, 1.50, 0.15, 1.000, 1.000, 'Una lanza de doble filo con gran alcance y velocidad de ataque.'),
	(6, 'Galatine', 5, 52.50, 52.50, 395.00, 0.00, 0.00, 0.00, 0.00, 0.20, 2.00, 0.20, 1.000, 0.917, 'Una espada pesada de dos manos que inflige un daño masivo con cada golpe.'),
	(7, 'Dual Kamas', 5, 8.75, 8.75, 52.50, 0.00, 0.00, 0.00, 0.00, 0.15, 1.70, 0.20, 1.000, 1.083, 'Un par de hoces ceremoniales letales. Veloces y mortales.'),
	(8, 'Nikana', 5, 14.00, 14.00, 112.00, 0.00, 0.00, 0.00, 0.00, 0.28, 2.20, 0.14, 1.000, 1.000, 'Una katana ceremonial Tenno de gran precision y letalidad.');

-- Volcando estructura para tabla planificador_warframe.build
CREATE TABLE IF NOT EXISTS `build` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo` enum('Warframe','Arma') NOT NULL,
  `id_warframe` int DEFAULT NULL,
  `id_arma` int DEFAULT NULL,
  `descripcion` text,
  `mod_1_id` int DEFAULT NULL,
  `mod_2_id` int DEFAULT NULL,
  `mod_3_id` int DEFAULT NULL,
  `mod_4_id` int DEFAULT NULL,
  `mod_5_id` int DEFAULT NULL,
  `mod_6_id` int DEFAULT NULL,
  `mod_7_id` int DEFAULT NULL,
  `mod_8_id` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_usuario` (`id_usuario`),
  KEY `id_warframe` (`id_warframe`),
  KEY `id_arma` (`id_arma`),
  CONSTRAINT `build_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`),
  CONSTRAINT `build_ibfk_2` FOREIGN KEY (`id_warframe`) REFERENCES `warframe` (`id`),
  CONSTRAINT `build_ibfk_3` FOREIGN KEY (`id_arma`) REFERENCES `arma` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.build: ~11 rows (aproximadamente)
REPLACE INTO `build` (`id`, `id_usuario`, `nombre`, `tipo`, `id_warframe`, `id_arma`, `descripcion`, `mod_1_id`, `mod_2_id`, `mod_3_id`, `mod_4_id`, `mod_5_id`, `mod_6_id`, `mod_7_id`, `mod_8_id`) VALUES
	(1, 1, 'Build 1', 'Warframe', 1, NULL, 'Build de mag', 34, 35, 36, 37, 38, 39, 40, 41),
	(5, 1, 'Tusuuusu', 'Arma', NULL, 1, 'sdadsa', 18, 16, 15, 28, 29, 26, 33, 2),
	(6, 1, 'dsadasdasdsa', 'Arma', NULL, 1, 'dsadasd', 18, 16, 15, 28, 29, 26, 33, NULL),
	(7, 1, 'fgsds', 'Warframe', 2, NULL, 'dsffsd', NULL, 50, NULL, NULL, NULL, 52, NULL, 45),
	(8, 1, 'Prueba vacia', 'Warframe', 1, NULL, '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(9, 1, 'Prueba vacia 2', 'Arma', NULL, 8, 'sadcsda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
	(11, 1, 'Build rhino', 'Warframe', 2, NULL, '', NULL, 45, NULL, NULL, NULL, NULL, NULL, NULL),
	(12, 1, 'dsadasdadsa', 'Warframe', 1, NULL, 'asdadasd', NULL, 56, NULL, 45, NULL, 38, NULL, NULL),
	(13, 1, 'Prueba xd', 'Arma', NULL, 1, 'sadasd', NULL, 15, NULL, 27, NULL, NULL, NULL, NULL),
	(14, 1, 'Prueba xd', 'Warframe', 1, NULL, 'sdadsd', NULL, 50, NULL, NULL, NULL, NULL, NULL, NULL),
	(16, 1, 'Build braton ejemplo', 'Arma', NULL, 2, '', 4, 10, 20, 7, 31, NULL, NULL, NULL);

-- Volcando estructura para tabla planificador_warframe.build_arcano
CREATE TABLE IF NOT EXISTS `build_arcano` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_build` int NOT NULL,
  `id_arcano` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_build` (`id_build`),
  KEY `id_arcano` (`id_arcano`),
  CONSTRAINT `build_arcano_ibfk_1` FOREIGN KEY (`id_build`) REFERENCES `build` (`id`),
  CONSTRAINT `build_arcano_ibfk_2` FOREIGN KEY (`id_arcano`) REFERENCES `arcano` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.build_arcano: ~0 rows (aproximadamente)

-- Volcando estructura para tabla planificador_warframe.build_mod
CREATE TABLE IF NOT EXISTS `build_mod` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_build` int NOT NULL,
  `id_mod` int NOT NULL,
  `ranura` int DEFAULT NULL,
  `rango` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_build_mod` (`id_build`,`id_mod`),
  KEY `idx_id_mod` (`id_mod`),
  CONSTRAINT `build_mod_fk_build` FOREIGN KEY (`id_build`) REFERENCES `build` (`id`),
  CONSTRAINT `build_mod_fk_mod` FOREIGN KEY (`id_mod`) REFERENCES `mod` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.build_mod: ~0 rows (aproximadamente)

-- Volcando estructura para tabla planificador_warframe.habilidad
CREATE TABLE IF NOT EXISTS `habilidad` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_warframe` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `tipo` enum('Activa','Pasiva') DEFAULT 'Activa',
  `costo_energia` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_warframe` (`id_warframe`),
  CONSTRAINT `habilidad_ibfk_1` FOREIGN KEY (`id_warframe`) REFERENCES `warframe` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.habilidad: ~0 rows (aproximadamente)

-- Volcando estructura para tabla planificador_warframe.inventario
CREATE TABLE IF NOT EXISTS `inventario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_warframe` int DEFAULT NULL,
  `id_arma` int DEFAULT NULL,
  `id_arcano` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_usuario` (`id_usuario`),
  KEY `id_warframe` (`id_warframe`),
  KEY `id_arma` (`id_arma`),
  KEY `id_arcano` (`id_arcano`),
  CONSTRAINT `inventario_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`),
  CONSTRAINT `inventario_ibfk_2` FOREIGN KEY (`id_warframe`) REFERENCES `warframe` (`id`),
  CONSTRAINT `inventario_ibfk_3` FOREIGN KEY (`id_arma`) REFERENCES `arma` (`id`),
  CONSTRAINT `inventario_ibfk_4` FOREIGN KEY (`id_arcano`) REFERENCES `arcano` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.inventario: ~0 rows (aproximadamente)

-- Volcando estructura para tabla planificador_warframe.mod
CREATE TABLE IF NOT EXISTS `mod` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `tipo_objeto` enum('Warframe','Arma','Arcano') NOT NULL,
  `categoria` enum('Primaria','Secundaria','Melee','Warframe','Universal') DEFAULT 'Universal',
  `id_tipo_arma` int DEFAULT NULL,
  `efecto` varchar(255) DEFAULT NULL,
  `rareza` enum('Común','Poco común','Raro','Legendario') DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_tipo_arma` (`id_tipo_arma`),
  CONSTRAINT `mod_ibfk_1` FOREIGN KEY (`id_tipo_arma`) REFERENCES `tipo_arma` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.mod: ~80 rows (aproximadamente)
REPLACE INTO `mod` (`id`, `nombre`, `tipo_objeto`, `categoria`, `id_tipo_arma`, `efecto`, `rareza`) VALUES
	(1, 'Serration', 'Arma', 'Primaria', 1, '+165% Daño', 'Raro'),
	(2, 'Point Blank', 'Arma', 'Primaria', 2, '+165% Daño', 'Raro'),
	(4, 'Heavy Caliber', 'Arma', 'Primaria', 1, '+165% Daño / -55% Precisión', 'Raro'),
	(5, 'Vile Precision', 'Arma', 'Primaria', 1, '-90% Retroceso / -15% Cadencia', 'Raro'),
	(6, 'Anemic Agility', 'Arma', 'Secundaria', 4, '+90% Cadencia / -15% Daño', 'Raro'),
	(7, 'Hellfire', 'Arma', 'Primaria', 1, '+90% Calor', 'Común'),
	(8, 'Stormbringer', 'Arma', 'Secundaria', 4, '+90% Eléctrico', 'Común'),
	(9, 'High Voltage', 'Arma', 'Primaria', 1, '+60% Eléctrico / +60% Prob Estado', 'Poco común'),
	(10, 'Cryo Rounds', 'Arma', 'Primaria', 1, '+90% Frío', 'Común'),
	(11, 'Infected Clip', 'Arma', 'Primaria', 1, '+90% Toxina', 'Común'),
	(12, 'Chilling Grasp', 'Arma', 'Secundaria', 4, '+90% Frío', 'Común'),
	(13, 'Pathogen Rounds', 'Arma', 'Secundaria', 4, '+90% Toxina', 'Común'),
	(14, 'Heated Charge', 'Arma', 'Secundaria', 4, '+90% Calor', 'Común'),
	(15, 'Incendiary Coat', 'Arma', 'Primaria', 2, '+90% Calor', 'Común'),
	(16, 'Contagious Spread', 'Arma', 'Primaria', 2, '+90% Toxina', 'Común'),
	(17, 'Chilling Reload', 'Arma', 'Primaria', 2, '+90% Frío', 'Común'),
	(18, 'Charged Shell', 'Arma', 'Primaria', 2, '+90% Eléctrico', 'Común'),
	(19, 'Rime Rounds', 'Arma', 'Primaria', 1, '+60% Frío / +60% Estado', 'Poco común'),
	(20, 'Malignant Force', 'Arma', 'Primaria', 1, '+60% Toxina / +60% Estado', 'Poco común'),
	(21, 'Thermite Rounds', 'Arma', 'Primaria', 1, '+60% Calor / +60% Estado', 'Poco común'),
	(22, 'Pistol Pestilence', 'Arma', 'Secundaria', 4, '+60% Toxina / +60% Estado', 'Poco común'),
	(23, 'Deep Freeze', 'Arma', 'Secundaria', 4, '+60% Frío / +60% Estado', 'Poco común'),
	(24, 'Scorch', 'Arma', 'Secundaria', 4, '+60% Calor / +60% Estado', 'Poco común'),
	(25, 'Jolt', 'Arma', 'Secundaria', 4, '+60% Eléctrico / +60% Estado', 'Poco común'),
	(26, 'Toxic Barrage', 'Arma', 'Primaria', 2, '+60% Toxina / +60% Estado', 'Poco común'),
	(27, 'Frigid Blast', 'Arma', 'Primaria', 2, '+60% Frío / +60% Estado', 'Poco común'),
	(28, 'Scattering Inferno', 'Arma', 'Primaria', 2, '+60% Calor / +60% Estado', 'Poco común'),
	(29, 'Shell Shock', 'Arma', 'Primaria', 2, '+60% Eléctrico / +60% Estado', 'Poco común'),
	(30, 'Hornet Strike', 'Arma', 'Secundaria', 4, '+220% Daño', 'Raro'),
	(31, 'Split Chamber', 'Arma', 'Primaria', 1, '+90% Multishot', 'Raro'),
	(32, 'Barrel Diffusion', 'Arma', 'Secundaria', 4, '+120% Multishot', 'Raro'),
	(33, 'Hell\'s Chamber', 'Arma', 'Primaria', 2, '+120% Multishot', 'Raro'),
	(34, 'Vitality', 'Warframe', 'Warframe', NULL, '+100% Salud', 'Raro'),
	(35, 'Vigor', 'Warframe', 'Warframe', NULL, '+80% Salud / +80% Escudo', 'Poco común'),
	(36, 'Physique', 'Warframe', 'Warframe', NULL, '+20% Salud', 'Común'),
	(37, 'Redirection', 'Warframe', 'Warframe', NULL, '+100% Escudo', 'Raro'),
	(38, 'Fast Deflection', 'Warframe', 'Warframe', NULL, '+90% Recarga Escudo', 'Común'),
	(39, 'Steel Fiber', 'Warframe', 'Warframe', NULL, '+100% Armadura', 'Raro'),
	(40, 'Armored Agility', 'Warframe', 'Warframe', NULL, '+40% Armadura / +15% Velocidad', 'Poco común'),
	(41, 'Flow', 'Warframe', 'Warframe', NULL, '+100% Energía', 'Poco común'),
	(42, 'Primed Flow', 'Warframe', 'Warframe', NULL, '+275% Energía', 'Legendario'),
	(43, 'Preparation', 'Warframe', 'Warframe', NULL, '+100% Energía Inicial', 'Común'),
	(44, 'Intensify', 'Warframe', 'Warframe', NULL, '+30% Fuerza', 'Raro'),
	(45, 'Blind Rage', 'Warframe', 'Warframe', NULL, '+99% Fuerza / -55% Eficiencia', 'Raro'),
	(46, 'Transient Fortitude', 'Warframe', 'Warframe', NULL, '+55% Fuerza / -27.5% Duración', 'Raro'),
	(47, 'Power Drift', 'Warframe', 'Warframe', NULL, '+15% Fuerza', 'Poco común'),
	(48, 'Streamline', 'Warframe', 'Warframe', NULL, '+30% Eficiencia', 'Poco común'),
	(49, 'Fleeting Expertise', 'Warframe', 'Warframe', NULL, '+60% Eficiencia / -60% Duración', 'Raro'),
	(50, 'Continuity', 'Warframe', 'Warframe', NULL, '+30% Duración', 'Poco común'),
	(51, 'Primed Continuity', 'Warframe', 'Warframe', NULL, '+55% Duración', 'Legendario'),
	(52, 'Constitution', 'Warframe', 'Warframe', NULL, '+28% Duración', 'Poco común'),
	(53, 'Narrow Minded', 'Warframe', 'Warframe', NULL, '+99% Duración / -66% Rango', 'Raro'),
	(54, 'Stretch', 'Warframe', 'Warframe', NULL, '+45% Rango', 'Poco común'),
	(55, 'Overextended', 'Warframe', 'Warframe', NULL, '+90% Rango / -60% Fuerza', 'Raro'),
	(56, 'Augur Reach', 'Warframe', 'Warframe', NULL, '+30% Rango', 'Poco común'),
	(57, 'Cunning Drift', 'Warframe', 'Warframe', NULL, '+15% Rango', 'Poco común'),
	(58, 'Pressure Point', 'Arma', 'Melee', 5, '+120% Daño', 'Raro'),
	(59, 'Primed Pressure Point', 'Arma', 'Melee', 5, '+165% Daño', 'Legendario'),
	(60, 'Spoiled Strike', 'Arma', 'Melee', 5, '+100% Daño / -20% Velocidad', 'Raro'),
	(61, 'Heavy Trauma', 'Arma', 'Melee', 5, '+90% Daño Impacto', 'Poco común'),
	(62, 'Auger Strike', 'Arma', 'Melee', 5, '+90% Daño Perforante', 'Poco común'),
	(63, 'Buzz Kill', 'Arma', 'Melee', 5, '+120% Daño Cortante', 'Raro'),
	(64, 'True Steel', 'Arma', 'Melee', 5, '+60% Crítico', 'Poco común'),
	(65, 'Organ Shatter', 'Arma', 'Melee', 5, '+90% Daño Crítico', 'Raro'),
	(66, 'Blood Rush', 'Arma', 'Melee', 5, '+165% Crítico en Combo', 'Raro'),
	(67, 'Weeping Wounds', 'Arma', 'Melee', 5, '+40% Estado por Combo', 'Raro'),
	(68, 'Drifting Contact', 'Arma', 'Melee', 5, '+40% Estado / +10s Duración Combo', 'Poco común'),
	(69, 'Fury', 'Arma', 'Melee', 5, '+30% Velocidad Ataque', 'Común'),
	(70, 'Berserker Fury', 'Arma', 'Melee', 5, '+75% Velocidad con Críticos', 'Raro'),
	(71, 'Primed Fury', 'Arma', 'Melee', 5, '+55% Velocidad Ataque', 'Legendario'),
	(72, 'Reach', 'Arma', 'Melee', 5, '+60% Alcance', 'Poco común'),
	(73, 'Primed Reach', 'Arma', 'Melee', 5, '+110% Alcance', 'Legendario'),
	(74, 'North Wind', 'Arma', 'Melee', 5, '+90% Frío', 'Común'),
	(75, 'Molten Impact', 'Arma', 'Melee', 5, '+90% Calor', 'Común'),
	(76, 'Shocking Touch', 'Arma', 'Melee', 5, '+90% Eléctrico', 'Común'),
	(77, 'Fever Strike', 'Arma', 'Melee', 5, '+90% Toxina', 'Común'),
	(78, 'Vicious Frost', 'Arma', 'Melee', 5, '+60% Frío / +60% Estado', 'Poco común'),
	(79, 'Volcanic Edge', 'Arma', 'Melee', 5, '+60% Calor / +60% Estado', 'Poco común'),
	(80, 'Voltaic Strike', 'Arma', 'Melee', 5, '+60% Eléctrico / +60% Estado', 'Poco común'),
	(81, 'Virulent Scourge', 'Arma', 'Melee', 5, '+60% Toxina / +60% Estado', 'Poco común');

-- Volcando estructura para tabla planificador_warframe.mod_efecto
CREATE TABLE IF NOT EXISTS `mod_efecto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_mod` int NOT NULL,
  `objetivo` enum('arma','warframe','habilidad','global') NOT NULL,
  `campo` varchar(50) NOT NULL,
  `operacion` enum('add','sub') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `valor` decimal(10,6) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `atributo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id_mod` (`id_mod`),
  CONSTRAINT `mod_efecto_ibfk_1` FOREIGN KEY (`id_mod`) REFERENCES `mod` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.mod_efecto: ~118 rows (aproximadamente)
REPLACE INTO `mod_efecto` (`id`, `id_mod`, `objetivo`, `campo`, `operacion`, `valor`, `descripcion`, `atributo`) VALUES
	(1, 1, 'arma', 'daño', 'add', 1.650000, '+165% Daño', 'daño'),
	(2, 2, 'arma', 'daño', 'add', 1.650000, '+165% Daño', 'daño'),
	(3, 4, 'arma', 'daño', 'add', 1.650000, '+165% Daño', 'daño'),
	(4, 4, 'arma', 'precision', 'sub', -0.550000, '-55% Precisión', 'precision'),
	(5, 5, 'arma', 'recoil', 'sub', -0.900000, '-90% Retroceso', 'recoil'),
	(6, 5, 'arma', 'cadencia', 'sub', -0.150000, '-15% Cadencia', 'cadencia'),
	(7, 6, 'arma', 'cadencia', 'add', 0.900000, '+90% Cadencia de fuego', 'cadencia'),
	(8, 6, 'arma', 'daño', 'sub', -0.150000, '-15% Daño', 'daño'),
	(9, 7, 'arma', 'daño', 'add', 0.900000, '+90% Calor', 'calor'),
	(10, 10, 'arma', 'daño', 'add', 0.900000, '+90% Frío', 'frio'),
	(11, 11, 'arma', 'daño', 'add', 0.900000, '+90% Toxina', 'toxina'),
	(12, 8, 'arma', 'daño', 'add', 0.900000, '+90% Eléctrico', 'electrico'),
	(13, 12, 'arma', 'daño', 'add', 0.900000, '+90% Frío', 'frio'),
	(14, 13, 'arma', 'daño', 'add', 0.900000, '+90% Toxina', 'toxina'),
	(15, 14, 'arma', 'daño', 'add', 0.900000, '+90% Calor', 'calor'),
	(16, 9, 'arma', 'daño', 'add', 0.600000, '+60% Eléctrico', 'electrico'),
	(17, 9, 'arma', 'prob_estado', 'add', 0.600000, '+60% Prob Estado', 'prob_estado'),
	(18, 19, 'arma', 'daño', 'add', 0.600000, '+60% Frío', 'frio'),
	(19, 19, 'arma', 'prob_estado', 'add', 0.600000, '+60% Estado', 'prob_estado'),
	(20, 20, 'arma', 'daño', 'add', 0.600000, '+60% Toxina', 'toxina'),
	(21, 20, 'arma', 'prob_estado', 'add', 0.600000, '+60% Estado', 'prob_estado'),
	(22, 21, 'arma', 'daño', 'add', 0.600000, '+60% Calor', 'calor'),
	(23, 21, 'arma', 'prob_estado', 'add', 0.600000, '+60% Estado', 'prob_estado'),
	(24, 22, 'arma', 'daño', 'add', 0.600000, '+60% Toxina', 'toxina'),
	(25, 22, 'arma', 'prob_estado', 'add', 0.600000, '+60% Estado', 'prob_estado'),
	(26, 31, 'arma', 'multishot', 'add', 0.900000, '+90% Multishot', 'multishot'),
	(27, 32, 'arma', 'multishot', 'add', 1.200000, '+120% Multishot', 'multishot'),
	(28, 33, 'arma', 'multishot', 'add', 1.200000, '+120% Multishot', 'multishot'),
	(29, 30, 'arma', 'daño', 'add', 2.200000, '+220% Daño', 'daño'),
	(30, 34, 'warframe', 'salud', 'add', 1.000000, '+100% Salud', 'salud'),
	(31, 35, 'warframe', 'salud', 'add', 0.800000, '+80% Salud', 'salud'),
	(32, 35, 'warframe', 'escudo', 'add', 0.800000, '+80% Escudo', 'escudo'),
	(33, 36, 'warframe', 'salud', 'add', 0.200000, '+20% Salud', 'salud'),
	(34, 37, 'warframe', 'escudo', 'add', 1.000000, '+100% Escudo', 'escudo'),
	(35, 38, 'warframe', 'escudo_delay', 'add', 0.900000, '+90% Escudo Recarga', 'escudo_delay'),
	(36, 39, 'warframe', 'armadura', 'add', 1.000000, '+100% Armadura', 'armadura'),
	(37, 40, 'warframe', 'armadura', 'add', 0.400000, '+40% Armadura', 'armadura'),
	(38, 40, 'warframe', 'velocidad', 'add', 0.150000, '+15% Velocidad', 'velocidad'),
	(39, 41, 'warframe', 'energia', 'add', 1.000000, '+100% Energía', 'energia'),
	(40, 42, 'warframe', 'energia', 'add', 2.750000, '+275% Energía', 'energia'),
	(41, 43, 'warframe', 'energia_inicial', 'add', 1.000000, '+100% Energía inicial', 'energia_inicial'),
	(42, 44, 'warframe', 'fuerza', 'add', 0.300000, '+30% Fuerza', 'fuerza'),
	(43, 45, 'warframe', 'fuerza', 'add', 0.990000, '+99% Fuerza', 'fuerza'),
	(44, 45, 'warframe', 'eficiencia', 'sub', -0.550000, '-55% Eficiencia', 'eficiencia'),
	(45, 46, 'warframe', 'fuerza', 'add', 0.550000, '+55% Fuerza', 'fuerza'),
	(46, 46, 'warframe', 'duracion', 'sub', -0.275000, '-27.5% Duración', 'duracion'),
	(47, 47, 'warframe', 'fuerza', 'add', 0.150000, '+15% Fuerza', 'fuerza'),
	(48, 48, 'warframe', 'eficiencia', 'add', 0.300000, '+30% Eficiencia', 'eficiencia'),
	(49, 49, 'warframe', 'eficiencia', 'add', 0.600000, '+60% Eficiencia', 'eficiencia'),
	(50, 49, 'warframe', 'duracion', 'sub', -0.600000, '-60% Duración', 'duracion'),
	(51, 50, 'warframe', 'duracion', 'add', 0.300000, '+30% Duración', 'duracion'),
	(52, 51, 'warframe', 'duracion', 'add', 0.550000, '+55% Duración', 'duracion'),
	(53, 52, 'warframe', 'duracion', 'add', 0.280000, '+28% Duración', 'duracion'),
	(54, 53, 'warframe', 'duracion', 'add', 0.990000, '+99% Duración', 'duracion'),
	(55, 53, 'warframe', 'rango', 'sub', -0.660000, '-66% Rango', 'rango'),
	(56, 54, 'warframe', 'rango', 'add', 0.450000, '+45% Rango', 'rango'),
	(57, 55, 'warframe', 'rango', 'add', 0.900000, '+90% Rango', 'rango'),
	(58, 55, 'warframe', 'fuerza', 'sub', -0.600000, '-60% Fuerza', 'fuerza'),
	(59, 56, 'warframe', 'rango', 'add', 0.300000, '+30% Rango', 'rango'),
	(60, 57, 'warframe', 'rango', 'add', 0.150000, '+15% Rango', 'rango'),
	(61, 36, 'warframe', 'salud', 'add', 0.900000, '+90% Salud', 'salud'),
	(62, 37, 'warframe', 'escudo', 'add', 4.400000, '+440% Escudo', 'escudo'),
	(63, 38, 'warframe', 'escudo_delay', 'add', 0.900000, '+90% Velocidad de Recarga', 'escudo_delay'),
	(64, 39, 'warframe', 'armadura', 'add', 1.100000, '+110% Armadura', 'armadura'),
	(65, 40, 'warframe', 'armadura', 'add', 0.450000, '+45% Armadura', 'armadura'),
	(66, 40, 'warframe', 'velocidad', 'add', 0.150000, '+15% Velocidad', 'velocidad'),
	(67, 41, 'warframe', 'energia', 'add', 1.500000, '+150% Energía', 'energia'),
	(68, 42, 'warframe', 'energia', 'add', 2.750000, '+275% Energía', 'energia'),
	(69, 43, 'warframe', 'energia_inicial', 'add', 1.000000, '+100% Energía Inicial', 'energia_inicial'),
	(70, 44, 'warframe', 'fuerza', 'add', 0.300000, '+30% Fuerza', 'fuerza'),
	(71, 45, 'warframe', 'fuerza', 'add', 0.990000, '+99% Fuerza', 'fuerza'),
	(72, 45, 'warframe', 'eficiencia', 'sub', -0.550000, '-55% Eficiencia', 'eficiencia'),
	(73, 46, 'warframe', 'fuerza', 'add', 0.550000, '+55% Fuerza', 'fuerza'),
	(74, 46, 'warframe', 'duracion', 'sub', -0.275000, '-27.5% Duración', 'duracion'),
	(75, 47, 'warframe', 'fuerza', 'add', 0.150000, '+15% Fuerza', 'fuerza'),
	(76, 48, 'warframe', 'eficiencia', 'add', 0.300000, '+30% Eficiencia', 'eficiencia'),
	(77, 49, 'warframe', 'eficiencia', 'add', 0.600000, '+60% Eficiencia', 'eficiencia'),
	(78, 49, 'warframe', 'duracion', 'sub', -0.600000, '-60% Duración', 'duracion'),
	(79, 50, 'warframe', 'duracion', 'add', 0.300000, '+30% Duración', 'duracion'),
	(80, 51, 'warframe', 'duracion', 'add', 0.550000, '+55% Duración', 'duracion'),
	(81, 52, 'warframe', 'duracion', 'add', 0.280000, '+28% Duración', 'duracion'),
	(82, 53, 'warframe', 'duracion', 'add', 0.990000, '+99% Duración', 'duracion'),
	(83, 53, 'warframe', 'rango', 'sub', -0.660000, '-66% Rango', 'rango'),
	(84, 54, 'warframe', 'rango', 'add', 0.450000, '+45% Rango', 'rango'),
	(85, 55, 'warframe', 'rango', 'add', 0.900000, '+90% Rango', 'rango'),
	(86, 55, 'warframe', 'fuerza', 'sub', -0.600000, '-60% Fuerza', 'fuerza'),
	(87, 56, 'warframe', 'rango', 'add', 0.300000, '+30% Rango', 'rango'),
	(88, 57, 'warframe', 'rango', 'add', 0.150000, '+15% Rango', 'rango'),
	(89, 58, 'arma', 'daño', 'add', 1.200000, '+120% Daño', 'daño'),
	(90, 59, 'arma', 'daño', 'add', 1.650000, '+165% Daño', 'daño'),
	(91, 60, 'arma', 'daño', 'add', 1.000000, '+100% Daño', 'daño'),
	(92, 60, 'arma', 'cadencia', 'sub', -0.200000, '-20% Velocidad', 'cadencia'),
	(93, 61, 'arma', 'daño', 'add', 0.900000, '+90% Daño Impacto', 'impacto'),
	(94, 62, 'arma', 'daño', 'add', 0.900000, '+90% Daño Perforante', 'perforante'),
	(95, 63, 'arma', 'daño', 'add', 1.200000, '+120% Daño Cortante', 'cortante'),
	(96, 64, 'arma', 'critico', 'add', 0.600000, '+60% Crítico', 'critico'),
	(97, 65, 'arma', 'mult_critico', 'add', 0.900000, '+90% Mult. Crítico', 'mult_critico'),
	(98, 66, 'arma', 'critico_combo', 'add', 1.650000, '+165% Crítico en Combo', 'critico'),
	(99, 67, 'arma', 'estado_combo', 'add', 0.400000, '+40% Estado por Combo', 'estado'),
	(100, 68, 'arma', 'prob_estado', 'add', 0.400000, '+40% Estado', NULL),
	(101, 68, 'arma', 'duracion_combo', 'add', 10.000000, '+10s Duración Combo', NULL),
	(102, 69, 'arma', 'cadencia', 'add', 0.300000, '+30% Velocidad', 'cadencia'),
	(103, 70, 'arma', 'velocidad_critico', 'add', 0.750000, '+75% Velocidad con Críticos', 'cadencia'),
	(104, 71, 'arma', 'cadencia', 'add', 0.550000, '+55% Velocidad', 'cadencia'),
	(105, 72, 'arma', 'alcance', 'add', 0.600000, '+60% Alcance', NULL),
	(106, 73, 'arma', 'alcance', 'add', 1.100000, '+110% Alcance', NULL),
	(107, 74, 'arma', 'daño', 'add', 0.900000, '+90% Frío', 'frio'),
	(108, 75, 'arma', 'daño', 'add', 0.900000, '+90% Calor', 'calor'),
	(109, 76, 'arma', 'daño', 'add', 0.900000, '+90% Eléctrico', 'electrico'),
	(110, 77, 'arma', 'daño', 'add', 0.900000, '+90% Toxina', 'toxina'),
	(111, 78, 'arma', 'daño', 'add', 0.600000, '+60% Frío', 'frio'),
	(112, 78, 'arma', 'prob_estado', 'add', 0.600000, '+60% Estado', NULL),
	(113, 79, 'arma', 'daño', 'add', 0.600000, '+60% Calor', 'calor'),
	(114, 79, 'arma', 'prob_estado', 'add', 0.600000, '+60% Estado', NULL),
	(115, 80, 'arma', 'daño', 'add', 0.600000, '+60% Eléctrico', 'electrico'),
	(116, 80, 'arma', 'prob_estado', 'add', 0.600000, '+60% Estado', NULL),
	(117, 81, 'arma', 'daño', 'add', 0.600000, '+60% Toxina', 'toxina'),
	(118, 81, 'arma', 'prob_estado', 'add', 0.600000, '+60% Estado', NULL);

-- Volcando estructura para tabla planificador_warframe.mod_incompatible
CREATE TABLE IF NOT EXISTS `mod_incompatible` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_mod_1` int NOT NULL,
  `id_mod_2` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_pair` (`id_mod_1`,`id_mod_2`),
  KEY `id_mod_2` (`id_mod_2`),
  CONSTRAINT `mod_incompatible_ibfk_1` FOREIGN KEY (`id_mod_1`) REFERENCES `mod` (`id`) ON DELETE CASCADE,
  CONSTRAINT `mod_incompatible_ibfk_2` FOREIGN KEY (`id_mod_2`) REFERENCES `mod` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.mod_incompatible: ~10 rows (aproximadamente)
REPLACE INTO `mod_incompatible` (`id`, `id_mod_1`, `id_mod_2`) VALUES
	(1, 58, 59),
	(2, 59, 58),
	(3, 69, 71),
	(4, 71, 69),
	(5, 72, 73),
	(6, 73, 72),
	(7, 41, 42),
	(8, 42, 41),
	(9, 50, 51),
	(10, 51, 50);

-- Volcando estructura para tabla planificador_warframe.tipo_arma
CREATE TABLE IF NOT EXISTS `tipo_arma` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `categoria` enum('Primaria','Secundaria','Melee') NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.tipo_arma: ~5 rows (aproximadamente)
REPLACE INTO `tipo_arma` (`id`, `nombre`, `categoria`) VALUES
	(1, 'Rifle', 'Primaria'),
	(2, 'Escopeta', 'Primaria'),
	(3, 'Arco', 'Primaria'),
	(4, 'Pistola', 'Secundaria'),
	(5, 'Melee', 'Melee');

-- Volcando estructura para tabla planificador_warframe.usuario
CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(50) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `nombre_usuario` (`nombre_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.usuario: ~3 rows (aproximadamente)
REPLACE INTO `usuario` (`id`, `nombre_usuario`, `contraseña`) VALUES
	(1, 'Santi', '$2a$12$NkrVWzJDQ4Io4NX/wNOJMuopnK6ZycObSXEqBhWHC.nZiN9gyxa0a'),
	(5, 'Paco', '$2a$12$K3DFJKme2AfB.jsSx74pZOkEWIP9rHajEF5dswjejYVpuj8ixvO/G'),
	(6, 'Paco123', '$2a$12$iVkbqzedZ44Hw82hBgdyYehNvHjqDqzQWktPABKUzUkak4lIEQnQS');

-- Volcando estructura para tabla planificador_warframe.warframe
CREATE TABLE IF NOT EXISTS `warframe` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `salud_base` int DEFAULT NULL,
  `escudo_base` int DEFAULT NULL,
  `armadura_base` int DEFAULT NULL,
  `energia_base` int DEFAULT NULL,
  `descripcion` text,
  `duracion` decimal(20,6) NOT NULL,
  `eficiencia` decimal(20,6) NOT NULL,
  `fuerza` decimal(20,6) NOT NULL,
  `rango` decimal(20,6) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla planificador_warframe.warframe: ~6 rows (aproximadamente)
REPLACE INTO `warframe` (`id`, `nombre`, `salud_base`, `escudo_base`, `armadura_base`, `energia_base`, `descripcion`, `duracion`, `eficiencia`, `fuerza`, `rango`) VALUES
	(1, 'Mag', 280, 555, 105, 190, 'Le gustan los imanes', 1.000000, 1.000000, 1.000000, 1.000000),
	(2, 'Rhino', 370, 555, 240, 150, 'El warframe mas duro de la galaxia', 1.000000, 1.000000, 1.000000, 1.000000),
	(3, 'Atlas Prime', 650, 655, 500, 265, 'El creador de las montañas', 1.000000, 1.000000, 1.000000, 1.000000),
	(4, 'Inaros Prime', 2415, 0, 240, 140, 'El señor del desierto', 1.000000, 1.000000, 1.000000, 1.000000),
	(5, 'Gyre', 370, 550, 105, 190, 'Aniquila enemigos con la conductividad de bobinas y transmisores', 1.000000, 1.000000, 1.000000, 1.000000),
	(6, 'Hildryn Prime', 370, 1380, 315, 0, 'Utiliza la fuerza de sus escudos para lanzar habilidades', 1.000000, 1.000000, 1.000000, 1.000000);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

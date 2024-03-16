-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Generation Time: Mar 16, 2024 at 03:15 AM
-- Server version: 11.3.0-MariaDB
-- PHP Version: 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sycomore_dev`
--

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `recording_date`, `updating_date`, `label`) VALUES
(1, '2024-03-16 05:07:26', NULL, 'A'),
(2, '2024-03-16 05:07:30', NULL, 'B'),
(3, '2024-03-16 05:07:36', NULL, 'C'),
(4, '2024-03-16 05:07:42', NULL, 'D'),
(5, '2024-03-16 05:07:47', NULL, 'E');

-- --------------------------------------------------------

--
-- Table structure for table `classifiable_level`
--

DROP TABLE IF EXISTS `classifiable_level`;
CREATE TABLE IF NOT EXISTS `classifiable_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `full_name` varchar(255) NOT NULL,
  `short_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `classifiable_level`
--

INSERT INTO `classifiable_level` (`id`, `recording_date`, `updating_date`, `full_name`, `short_name`) VALUES
(1, '2024-03-16 05:04:33', NULL, 'Première', '1èr'),
(2, '2024-03-16 05:04:53', '2024-03-16 05:07:10', 'Deuxième', '2émè'),
(3, '2024-03-16 05:05:21', '2024-03-16 05:10:18', 'Troixième', '3émè'),
(4, '2024-03-16 05:05:43', NULL, 'Quantrième', '4émè'),
(5, '2024-03-16 05:06:14', NULL, 'Cinquème', '5émè'),
(6, '2024-03-16 05:06:33', NULL, 'Sixème', '6émè'),
(7, '2024-03-16 05:06:47', NULL, 'Septième', '7émè'),
(8, '2024-03-16 05:07:02', NULL, 'Huitième', '8émè');

-- --------------------------------------------------------

--
-- Table structure for table `classifiable_option`
--

DROP TABLE IF EXISTS `classifiable_option`;
CREATE TABLE IF NOT EXISTS `classifiable_option` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `full_name` varchar(255) NOT NULL,
  `short_name` varchar(255) NOT NULL,
  `section_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnmeokqcbj83ygt5ilg2tdxq8p` (`section_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `classifiable_option`
--

INSERT INTO `classifiable_option` (`id`, `recording_date`, `updating_date`, `full_name`, `short_name`, `section_id`) VALUES
(1, '2024-03-16 05:03:11', NULL, 'Electronique generale', 'Etro', 1),
(2, '2024-03-16 05:03:31', NULL, 'Electricité générale', 'Elect', 1);

-- --------------------------------------------------------

--
-- Table structure for table `classifiable_section`
--

DROP TABLE IF EXISTS `classifiable_section`;
CREATE TABLE IF NOT EXISTS `classifiable_section` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `full_name` varchar(255) NOT NULL,
  `short_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `classifiable_section`
--

INSERT INTO `classifiable_section` (`id`, `recording_date`, `updating_date`, `full_name`, `short_name`) VALUES
(1, '2024-03-16 05:02:46', NULL, 'Section Technique', 'ST');

-- --------------------------------------------------------

--
-- Table structure for table `inscription`
--

DROP TABLE IF EXISTS `inscription`;
CREATE TABLE IF NOT EXISTS `inscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `total_paid_cash` DECIMAL(16,2) DEFAULT NULL,
  `total_related_fees` DECIMAL(16,2) DEFAULT NULL,
  `promotion_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3r03n89wnptxyb9944hwtqys8` (`promotion_id`),
  KEY `FK7rmfqaksvrei8ecot3b73ovqj` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- --------------------------------------------------------

--
-- Table structure for table `inscription_related_fees`
--

DROP TABLE IF EXISTS `inscription_related_fees`;
CREATE TABLE IF NOT EXISTS `inscription_related_fees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `config_id` int(11) NOT NULL,
  `inscription_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKs88ljnm0ex1h8j43x2prtamvy` (`config_id`),
  KEY `FKpu3opfdyqo92a0559lpfgtrf7` (`inscription_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
CREATE TABLE IF NOT EXISTS `payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `amount` double NOT NULL,
  `day_date` date NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `inscription_id` int(11) NOT NULL,
  `relatedConfig_id` int(11) DEFAULT NULL,
  `studyConfig_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa73ilxsmox5fa2f23wmgoq68j` (`inscription_id`),
  KEY `FKl0psxddsppe3op16mepaqbsxb` (`relatedConfig_id`),
  KEY `FKmd7kmc5x3c4tbxfy02lb1frek` (`studyConfig_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
CREATE TABLE IF NOT EXISTS `promotion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `total_study_fees` DECIMAL(16,2) NULL DEFAULT NULL,
  `inscriptions_count` INT(11) NULL DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `level_id` int(11) NOT NULL,
  `option_id` int(11) DEFAULT NULL,
  `school_id` int(11) NOT NULL,
  `year_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKok7am2wl7u75y5ssfbcmwcs16` (`category_id`),
  KEY `FKps3sri7g4fx4h9g8urjbd39i8` (`level_id`),
  KEY `FKras0ewm14cdgg425x5mf819b` (`option_id`),
  KEY `FKi6l70rrq70kc468fmjfretl3q` (`school_id`),
  KEY `FKi7n7erwvy5ioa3ag4c1iv73lj` (`year_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `promotion`
--

INSERT INTO `promotion` (`id`, `recording_date`, `updating_date`, `category_id`, `level_id`, `option_id`, `school_id`, `year_id`) VALUES
(1, '2024-03-16 05:09:18', NULL, NULL, 1, NULL, 1, 1),
(2, '2024-03-16 05:09:18', NULL, NULL, 2, NULL, 1, 1),
(3, '2024-03-16 05:10:00', NULL, NULL, 1, NULL, 2, 1),
(4, '2024-03-16 05:10:00', NULL, NULL, 2, NULL, 2, 1),
(5, '2024-03-16 05:10:00', NULL, NULL, 3, NULL, 2, 1),
(6, '2024-03-16 05:10:00', NULL, NULL, 4, NULL, 2, 1),
(7, '2024-03-16 05:10:00', NULL, NULL, 5, NULL, 2, 1),
(8, '2024-03-16 05:10:00', NULL, NULL, 6, NULL, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `promotion_study_fees`
--

DROP TABLE IF EXISTS `promotion_study_fees`;
CREATE TABLE IF NOT EXISTS `promotion_study_fees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `config_id` int(11) NOT NULL,
  `promotion_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhj4kph1quqnfsvo237mto7ot8` (`config_id`),
  KEY `FKj22ajw101r1k86ykmyr8qukuh` (`promotion_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `related_fees_config`
--

DROP TABLE IF EXISTS `related_fees_config`;
CREATE TABLE IF NOT EXISTS `related_fees_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `amount` double NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `end_at` date DEFAULT NULL,
  `start_at` date DEFAULT NULL,
  `year_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk5o82c81gth7dtrf8jt7yeuem` (`year_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `school`
--

DROP TABLE IF EXISTS `school`;
CREATE TABLE IF NOT EXISTS `school` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `school`
--

INSERT INTO `school` (`id`, `recording_date`, `updating_date`, `name`) VALUES
(1, '2024-03-16 05:01:48', NULL, 'Maternel'),
(2, '2024-03-16 05:02:17', NULL, 'Primaire'),
(3, '2024-03-16 05:02:25', NULL, 'Secondaire');

-- --------------------------------------------------------

--
-- Table structure for table `school_year`
--

DROP TABLE IF EXISTS `school_year`;
CREATE TABLE IF NOT EXISTS `school_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `archived` bit(1) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `school_year`
--

INSERT INTO `school_year` (`id`, `recording_date`, `updating_date`, `archived`, `label`) VALUES
(1, '2024-03-16 02:53:53', NULL, b'0', '2023-2024');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `names` varchar(255) NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `registration_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `study_fees_config`
--

DROP TABLE IF EXISTS `study_fees_config`;
CREATE TABLE IF NOT EXISTS `study_fees_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `recording_date` datetime NOT NULL,
  `updating_date` datetime DEFAULT NULL,
  `amount` double NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `end_at` date DEFAULT NULL,
  `start_at` date DEFAULT NULL,
  `year_id` int(11) NOT NULL,
  `school_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh0ov4aax6181dhl51it1e9uhk` (`year_id`),
  KEY `FKberxdtqae81beah9i7mg62234` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `classifiable_option`
--
ALTER TABLE `classifiable_option`
  ADD CONSTRAINT `FKnmeokqcbj83ygt5ilg2tdxq8p` FOREIGN KEY (`section_id`) REFERENCES `classifiable_section` (`id`);

--
-- Constraints for table `inscription`
--
ALTER TABLE `inscription`
  ADD CONSTRAINT `FK3r03n89wnptxyb9944hwtqys8` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`),
  ADD CONSTRAINT `FK7rmfqaksvrei8ecot3b73ovqj` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`);

--
-- Constraints for table `inscription_related_fees`
--
ALTER TABLE `inscription_related_fees`
  ADD CONSTRAINT `FKpu3opfdyqo92a0559lpfgtrf7` FOREIGN KEY (`inscription_id`) REFERENCES `inscription` (`id`),
  ADD CONSTRAINT `FKs88ljnm0ex1h8j43x2prtamvy` FOREIGN KEY (`config_id`) REFERENCES `related_fees_config` (`id`);

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `FKa73ilxsmox5fa2f23wmgoq68j` FOREIGN KEY (`inscription_id`) REFERENCES `inscription` (`id`),
  ADD CONSTRAINT `FKl0psxddsppe3op16mepaqbsxb` FOREIGN KEY (`relatedConfig_id`) REFERENCES `related_fees_config` (`id`),
  ADD CONSTRAINT `FKmd7kmc5x3c4tbxfy02lb1frek` FOREIGN KEY (`studyConfig_id`) REFERENCES `study_fees_config` (`id`);

--
-- Constraints for table `promotion`
--
ALTER TABLE `promotion`
  ADD CONSTRAINT `FKi6l70rrq70kc468fmjfretl3q` FOREIGN KEY (`school_id`) REFERENCES `school` (`id`),
  ADD CONSTRAINT `FKi7n7erwvy5ioa3ag4c1iv73lj` FOREIGN KEY (`year_id`) REFERENCES `school_year` (`id`),
  ADD CONSTRAINT `FKok7am2wl7u75y5ssfbcmwcs16` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  ADD CONSTRAINT `FKps3sri7g4fx4h9g8urjbd39i8` FOREIGN KEY (`level_id`) REFERENCES `classifiable_level` (`id`),
  ADD CONSTRAINT `FKras0ewm14cdgg425x5mf819b` FOREIGN KEY (`option_id`) REFERENCES `classifiable_option` (`id`);

--
-- Constraints for table `promotion_study_fees`
--
ALTER TABLE `promotion_study_fees`
  ADD CONSTRAINT `FKhj4kph1quqnfsvo237mto7ot8` FOREIGN KEY (`config_id`) REFERENCES `study_fees_config` (`id`),
  ADD CONSTRAINT `FKj22ajw101r1k86ykmyr8qukuh` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`);

--
-- Constraints for table `related_fees_config`
--
ALTER TABLE `related_fees_config`
  ADD CONSTRAINT `FKk5o82c81gth7dtrf8jt7yeuem` FOREIGN KEY (`year_id`) REFERENCES `school_year` (`id`);

--
-- Constraints for table `study_fees_config`
--
ALTER TABLE `study_fees_config`
  ADD CONSTRAINT `FKberxdtqae81beah9i7mg62234` FOREIGN KEY (`school_id`) REFERENCES `school` (`id`),
  ADD CONSTRAINT `FKh0ov4aax6181dhl51it1e9uhk` FOREIGN KEY (`year_id`) REFERENCES `school_year` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

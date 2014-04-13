-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 18. Mrz 2014 um 08:18
-- Server Version: 5.5.35-2
-- PHP-Version: 5.5.9-1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `dmdb`
--
CREATE DATABASE IF NOT EXISTS `dmdb` DEFAULT CHARACTER SET latin1 COLLATE latin1_general_ci;
USE `dmdb`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `CaseNote`
--

CREATE TABLE IF NOT EXISTS `CaseNote` (
  `CaseNoteId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CaseId` int(10) unsigned NOT NULL,
  `Note` mediumtext COLLATE latin1_general_ci NOT NULL,
  `UserId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`CaseNoteId`),
  KEY `CaseId` (`CaseId`),
  KEY `UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Case`
--

CREATE TABLE IF NOT EXISTS `Case` (
  `CaseId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `CrimeId` int(10) unsigned NULL,
  `Status` enum('open','closed') COLLATE latin1_general_ci NOT NULL,
  `Date` date NULL,
  `Time` time NULL,
  `Location` varchar(50) COLLATE latin1_general_ci NULL,
  PRIMARY KEY (`CaseId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

--
-- Trigger `Case`
--
DROP TRIGGER IF EXISTS `NoCaseUpdateIfClosed`;
DELIMITER //
CREATE TRIGGER `NoCaseUpdateIfClosed` BEFORE UPDATE ON `Case`
 FOR EACH ROW BEGIN
    DECLARE msg VARCHAR(255);
    IF (old.status = "closed" AND new.status="closed") THEN
        set msg = "Cannot modify Cases if case is closed";
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;

END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Convicted`
--

CREATE TABLE IF NOT EXISTS `Convicted` (
  `PoIId` int(10) unsigned NOT NULL,
  `CaseId` int(10) unsigned NOT NULL,
  `CrimeId` int(10) unsigned NOT NULL,
  `Date` date NOT NULL,
  `Sentence` varchar(50) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`PoIId`,`CaseId`,`CrimeId`),
  KEY `CaseId` (`CaseId`),
  KEY `CrimeId` (`CrimeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Trigger `Convicted`
--
DROP TRIGGER IF EXISTS `NoDeleteIfClosed`;
DELIMITER //
CREATE TRIGGER `NoDeleteIfClosed` BEFORE DELETE ON `Convicted`
 FOR EACH ROW BEGIN
    DECLARE msg VARCHAR(255);
    IF ((SELECT status from `Case` where CaseId = Old.CaseId) = "closed") THEN
        set msg = "Cannot delete Convicted entry for closed case";
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;

END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `NoInsertIfClosed`;
DELIMITER //
CREATE TRIGGER `NoInsertIfClosed` BEFORE INSERT ON `Convicted`
 FOR EACH ROW BEGIN
    DECLARE msg VARCHAR(255);
    IF ((SELECT status from `Case` where CaseId = NEW.CaseId) = "closed") THEN
        set msg = "Cannot insert Convicted for closed case";
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;

END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `NoUpdateIfClosed`;
DELIMITER //
CREATE TRIGGER `NoUpdateIfClosed` BEFORE UPDATE ON `Convicted`
 FOR EACH ROW BEGIN
    DECLARE msg VARCHAR(255);
    IF ((SELECT status from `Case` where CaseId = Old.CaseId) = "closed") THEN
        set msg = "Cannot modify Convicted for closed case";
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;

END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Crime`
--

CREATE TABLE IF NOT EXISTS `Crime` (
  `CrimeId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Crime` varchar(50) COLLATE latin1_general_ci NOT NULL,
  PRIMARY KEY (`CrimeId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `PoI`
--

CREATE TABLE IF NOT EXISTS `PoI` (
  `PoIId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) COLLATE latin1_general_ci NOT NULL,
  `Birthdate` date NOT NULL,
  `UserId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`PoIId`),
  UNIQUE KEY `Name` (`Name`,`Birthdate`),
  KEY `UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `PoINote`
--

CREATE TABLE IF NOT EXISTS `PoINote` (
  `PoINoteId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PoIId` int(10) unsigned NOT NULL,
  `Note` mediumtext COLLATE latin1_general_ci NOT NULL,
  `UserId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`PoINoteId`),
  KEY `PoIId` (`PoIId`),
  KEY `UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Suspect`
--

CREATE TABLE IF NOT EXISTS `Suspect` (
  `PoIId` int(10) unsigned NOT NULL,
  `CaseId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`PoIId`,`CaseId`),
  KEY `CaseId` (`CaseId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `UserId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) COLLATE latin1_general_ci NOT NULL,
  `Password` varchar(32) COLLATE latin1_general_ci NOT NULL,
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `CaseNote`
--
ALTER TABLE `CaseNote`
  ADD CONSTRAINT `CaseNote_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`) ON DELETE SET NULL ON UPDATE SET NULL,
  ADD CONSTRAINT `CaseNote_ibfk_2` FOREIGN KEY (`CaseId`) REFERENCES `Case` (`CaseId`) ON DELETE CASCADE ON UPDATE CASCADE;
  
ALTER TABLE `Case`
  ADD CONSTRAINT `Case_ibfk_1` FOREIGN KEY (`CrimeId`) REFERENCES `Crime` (`CrimeId`) ON DELETE SET NULL;

--
-- Constraints der Tabelle `Convicted`
--
ALTER TABLE `Convicted`
  ADD CONSTRAINT `Convicted_ibfk_1` FOREIGN KEY (`PoIId`) REFERENCES `PoI` (`PoIId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Convicted_ibfk_2` FOREIGN KEY (`CaseId`) REFERENCES `Case` (`CaseId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Convicted_ibfk_3` FOREIGN KEY (`CrimeId`) REFERENCES `Crime` (`CrimeId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `PoI`
--
ALTER TABLE `PoI`
  ADD CONSTRAINT `PoI_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Constraints der Tabelle `PoINote`
--
ALTER TABLE `PoINote`
  ADD CONSTRAINT `PoINote_ibfk_1` FOREIGN KEY (`PoIId`) REFERENCES `PoI` (`PoIId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PoINote_ibfk_2` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Constraints der Tabelle `Suspect`
--
ALTER TABLE `Suspect`
  ADD CONSTRAINT `Suspect_ibfk_1` FOREIGN KEY (`CaseId`) REFERENCES `Case` (`CaseId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Suspect_ibfk_2` FOREIGN KEY (`PoIId`) REFERENCES `PoI` (`PoIId`) ON DELETE CASCADE ON UPDATE CASCADE;
  
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

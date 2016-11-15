-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema cognitiveagent
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `cognitiveagent` ;

-- -----------------------------------------------------
-- Schema cognitiveagent
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cognitiveagent` DEFAULT CHARACTER SET latin1 ;
USE `cognitiveagent` ;

-- -----------------------------------------------------
-- Table `cognitiveagent`.`categoriaoferta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cognitiveagent`.`categoriaoferta` ;

CREATE TABLE IF NOT EXISTS `cognitiveagent`.`categoriaoferta` (
  `idCategoriaOferta` INT(11) NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idCategoriaOferta`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cognitiveagent`.`oferta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cognitiveagent`.`oferta` ;

CREATE TABLE IF NOT EXISTS `cognitiveagent`.`oferta` (
  `idOferta` int(11) NOT NULL AUTO_INCREMENT,
  `tituloDeOferta` varchar(80) DEFAULT NULL,
  `comercio` varchar(45) DEFAULT NULL,
  `descripcion` varchar(80) DEFAULT NULL,
  `categoria` int(11) NOT NULL,
  `ciudad` varchar(45) DEFAULT NULL,
  `estado` bit(1) DEFAULT NULL,
  `restricciones` varchar(255) DEFAULT NULL,
  `vigenciaDesde` date DEFAULT NULL,
  `vigenciaHasta` date DEFAULT NULL,
  `imagenComercioPath` varchar(255) DEFAULT NULL,
  `imagenPublicidadPath` varchar(255) DEFAULT NULL,
  `fechaHoraRegistro` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `eliminada` bit(1) DEFAULT b'0',
  PRIMARY KEY (`idOferta`, `categoria`),
  INDEX `fk_oferta_categoriaoferta_idx` (`categoria` ASC),
  CONSTRAINT `fk_oferta_categoriaoferta`
    FOREIGN KEY (`categoria`)
    REFERENCES `cognitiveagent`.`categoriaoferta` (`idCategoriaOferta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = latin1;


use cognitiveagent;

DROP TABLE IF EXISTS `cognitiveagent`.`spring_session` ;

CREATE TABLE cognitiveagent.SPRING_SESSION (
        SESSION_ID CHAR(36),
        CREATION_TIME BIGINT NOT NULL,
        LAST_ACCESS_TIME BIGINT NOT NULL,
        MAX_INACTIVE_INTERVAL INT NOT NULL,
        PRINCIPAL_NAME VARCHAR(100),
        CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (SESSION_ID)
) ENGINE=InnoDB;

CREATE INDEX SPRING_SESSION_IX1 ON cognitiveagent.SPRING_SESSION (LAST_ACCESS_TIME);


DROP TABLE IF EXISTS `cognitiveagent`.`SPRING_SESSION_ATTRIBUTES` ;

CREATE TABLE cognitiveagent.SPRING_SESSION_ATTRIBUTES (
        SESSION_ID CHAR(36),
        ATTRIBUTE_NAME VARCHAR(200),
        ATTRIBUTE_BYTES BLOB,
        CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_ID, ATTRIBUTE_NAME),
        CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_ID) REFERENCES SPRING_SESSION(SESSION_ID) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX SPRING_SESSION_ATTRIBUTES_IX1 ON cognitiveagent.SPRING_SESSION_ATTRIBUTES (SESSION_ID);






SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
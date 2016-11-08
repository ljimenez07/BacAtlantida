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
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `cognitiveagent`.`oferta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cognitiveagent`.`oferta` ;

CREATE TABLE IF NOT EXISTS `cognitiveagent`.`oferta` (
  `idOferta` INT(11) NOT NULL AUTO_INCREMENT,
  `tituloDeOferta` VARCHAR(80) NULL DEFAULT NULL,
  `comercio` VARCHAR(45) NULL DEFAULT NULL,
  `descripcion` VARCHAR(80) NULL DEFAULT NULL,
  `ciudad` VARCHAR(45) NULL DEFAULT NULL,
  `estado` BIT(1) NULL DEFAULT NULL,
  `restricciones` VARCHAR(255) NULL DEFAULT NULL,
  `vigenciaDesde` DATE NULL DEFAULT NULL,
  `vigenciaHasta` DATE NULL DEFAULT NULL,
  `imagenComercioPath` VARCHAR(255) NULL DEFAULT NULL,
  `imagenPublicidadPath` VARCHAR(255) NULL DEFAULT NULL,
  `categoria` INT(11) NOT NULL,
  PRIMARY KEY (`idOferta`, `categoriaoferta_idCategoriaOferta`),
  INDEX `fk_oferta_categoriaoferta_idx` (`categoriaoferta_idCategoriaOferta` ASC),
  CONSTRAINT `fk_oferta_categoriaoferta`
    FOREIGN KEY (`categoriaoferta_idCategoriaOferta`)
    REFERENCES `cognitiveagent`.`categoriaoferta` (`idCategoriaOferta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

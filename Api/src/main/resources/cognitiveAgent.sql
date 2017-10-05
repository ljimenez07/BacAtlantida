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
  `descripcion` varchar(255) DEFAULT NULL,
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




CREATE TABLE `cognitiveagent`.`categoriadeoferta` (
  `id` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));
  
INSERT INTO `cognitiveagent`.`categoriadeoferta` (`id`, `nombre`) VALUES ('1', 'Restaurante');
INSERT INTO `cognitiveagent`.`categoriadeoferta` (`id`, `nombre`) VALUES ('2', 'Hoteles');
INSERT INTO `cognitiveagent`.`categoriadeoferta` (`id`, `nombre`) VALUES ('3', 'Belleza');

ALTER TABLE `cognitiveagent`.`oferta` 
DROP FOREIGN KEY `fk_oferta_categoriaoferta`;
ALTER TABLE `cognitiveagent`.`oferta` 
DROP COLUMN `categoria`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`idOferta`),
DROP INDEX `fk_oferta_categoriaoferta_idx` ;

DROP TABLE cognitiveagent.categoriaoferta;


CREATE TABLE `cognitiveagent`.`categoria_con_oferta_y_peso` (
  `idCategoria` INT NOT NULL,
  `idOferta` INT NOT NULL,
  `peso` INT NOT NULL,
  INDEX `fk_categoria_idx` (`idCategoria` ASC),
  INDEX `fk_oferta_idx` (`idOferta` ASC),
  CONSTRAINT `fk_categoria`
    FOREIGN KEY (`idCategoria`)
    REFERENCES `cognitiveagent`.`categoriadeoferta` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_oferta`
    FOREIGN KEY (`idOferta`)
    REFERENCES `cognitiveagent`.`oferta` (`idOferta`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


ALTER TABLE `cognitiveagent`.`categoria_con_oferta_y_peso` 
CHANGE COLUMN `peso` `peso` DOUBLE NOT NULL ;

CREATE TABLE `cognitiveagent`.`categoria_usuario_peso` (
  `idUsuarioenBA` VARCHAR(45) NOT NULL,
  `idCategoria` INT NOT NULL,
  `peso` DOUBLE NOT NULL,
  INDEX `fk_cat_usu_idx` (`idCategoria` ASC),
  CONSTRAINT `fk_cat_usu`
    FOREIGN KEY (`idCategoria`)
    REFERENCES `cognitiveagent`.`categoriadeoferta` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

    
    ALTER TABLE `cognitiveagent`.`categoria_usuario_peso` 
ADD PRIMARY KEY (`idUsuarioenBA`, `idCategoria`);


CREATE TABLE `cognitiveagent`.`popups_vistos_por_usuario` (
  `usuario` VARCHAR(45) NOT NULL,
  `nuevasOfertas` TINYINT NULL,
  PRIMARY KEY (`usuario`));
  
DROP TABLE IF EXISTS `bitacora_de_conversaciones`;
CREATE TABLE `bitacora_de_conversaciones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_sesion` varchar(100) NOT NULL,
  `id_usuario` varchar(100) DEFAULT NULL,
  `fecha` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `conversacion` blob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `estadistica_tema`;
CREATE TABLE `estadistica_tema` (
  `idTema` varchar(55) NOT NULL,
  `fecha` date NOT NULL,
  `vecesConsultado` int(11) DEFAULT '0',
  PRIMARY KEY (`idTema`,`fecha`)
) ENGINE=InnoDB;

--
-- Table structure for table `contenedor_info_agencias_autobancos`
--

DROP TABLE IF EXISTS `contenedor_info_agencias_autobancos`;
CREATE TABLE `contenedor_info_agencias_autobancos` (
  `idContenedorDeInfoDeAgenciasYAutobancos` int(11) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) DEFAULT NULL,
  `tipo` varchar(50) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `departamento` varchar(45) DEFAULT NULL,
  `ciudad` varchar(45) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `horarioLV` varchar(45) NOT NULL,
  `horarioSabado` varchar(45) DEFAULT NULL,
  `horarioDomingo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idContenedorDeInfoDeAgenciasYAutobancos`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contenedor_info_agencias_autobancos`
--

LOCK TABLES `contenedor_info_agencias_autobancos` WRITE;
INSERT INTO `contenedor_info_agencias_autobancos` VALUES (1,'111','Autobanco','Autbanco Plaza Bancatlan','2280-0000 ext.2362','Francisco Morazán','Tegucigalpa','Blvd. Centroamerica, Tegucigalpa','09:00 - 19:00','08:30 - 17:00',NULL),(2,'102','Autobanco','Autobanco Segunda Ave','2280-0000 ext6120','Francisco Morazán','Comayaguela','Calle Real, Comayaguela','09:00 - 18:00','08:30 - 15:00',NULL),(3,'112','Autobanco','Autobanco Colón','2280-0000 ext6235','Francisco Morazán','Comayaguela','Blvd. Com. Económica Europea, cont. Gas. Texaco','09:00 - 18:00','08:30 - 15:00',NULL),(4,'127','Autobanco','Autobanco Almendros','2280-0000 ext6093','Francisco Morazán','Tegucigalpa','Blvd. Morazán, Frente a Esc. Federico Froebel, Tegucigalpa ','09:00 - 19:00','08:30 - 15:00',NULL),(5,'130','Autobanco','Autobanco Trapiche','2280-0000 ext6772','Francisco Morazán','Tegucigalpa','Boulevard Suyapa, Tegucigalpa MDC.','09:00 - 19:00','08:30 - 17:00',NULL),(6,'138','Autobanco','Autobanco Kennedy','2280-0000 ext6222','Francisco Morazán','Tegucigalpa','blvd centroamerica col kennedy Contiguo a SISTEC','09:00 - 19:00','08:30 - 17:00',NULL),(7,'204','Autobanco','Autobanco 10ma Calle','2580-0000 ext5853','Cortes','San Pedro Sula','3a. Avenida 10a. Calle Barrio lempira, San Pedro Sula','09:00 - 16:00','08:30 - 12:00',NULL),(8,'208','Autobanco','Autobanco La Lima','2668-2263','Cortes','La Lima','Lima Nueva Cortés. Frente a Oficina Tella Railroad Co.','09:00 - 17:00','09:00 - 16:00',NULL),(9,'213','Autobanco','Autobanco Ag Norte','2580-0000 ext5863','Cortes','San Pedro Sula','Boulevard del Norte, Salida a Puerto Cortés.','09:00 - 19:00','08:30 - 16:00',NULL),(10,'215','Autobanco','Autobanco Choloma','2669-0503','Cortes','Choloma','Edif. Miguel Hasbrun, Ave. Los Proceres, No. 224,  Choloma, Cortés.','',NULL,NULL),(11,'220','Autobanco','Autobanco La Arboleda',NULL,'Cortes','San Pedro Sula','1a. calle 7a.y 8a. Avenida,  Bo. Santa Anita, San Pedro Sula.','09:00 - 16:00','08:30 - 12:00',NULL),(12,'222','Autobanco','Autobanco El Roble','25512860','Cortes','San Pedro Sula','Col. El Roble, Autopista a Puerto Cortés, San Pedro Sula.','09:00 - 16:00','08:30 - 12:00',NULL),(13,'225','Autobanco','Autobanco Ofi Prin SPS','2580-0000 ext3392','Cortes','San Pedro Sula','Edif. Bancatlán, Frente a Parque Central, San Pedro Sula.','09:00 - 19:00','08:30 - 16:00',NULL),(14,'227','Autobanco','Autobanco Ag. Monumento a la Madre','2580-0000 ext5861','Cortes','San Pedro Sula','Col. El Roble, Autopista a Puerto Cortés, San Pedro Sula.','09:00 - 19:00','08:30 - 16:00',NULL),(15,'241','Autobanco','Autobanco Búfalo','2580-0000 ext6089','Cortes','Búfalo','Bufalo Plaza, Zona indutrial Bufalo, Villanueva','09:00 - 16:00','08:30 - 12:00','10am-2pm'),(16,'301','Autobanco','Autobanco Las palmas','2480-0000 ext4176','Atlántida','La Ceiba','Col. El Sauce, Edif. Las Palmas, La Ceiba.','09:00 - 16:00','08:30 - 12:00',NULL),(17,'318','Autobanco','Autobanco Mall Roatán','24805316, 5317. ','Islas de Bahía ','Roatán','Agencia Mall, French Harbour Calle principal.','09:00 - 17:00','09:00 - 16:00',NULL),(18,'401','Autobanco','Autobanco Puerto Cortés','2665-0145/ 2013','Cortes','Puerto Cortes','Barrio Copén 2da avenida 12 calle ','09:00 - 16:00','08:30 - 12:00',NULL),(19,'601','Autobanco','Autobanco Progreso','2648-1495','Cortes','Progreso','Col. Montevideo 10 calle, 2 y 3 avenida Norte, El Progreso, Yoro.','09:00 - 17:00','08:30 - 12:00',NULL),(20,'701','Autobanco','Autobanco Choluteca','27820121','Choluteca','Choluteca','Oficina Principal Choluteca','',NULL,NULL),(21,'1001','Autobanco','Autobanco Danli','27632818','El Paraíso','Danli','Barrio El Centro, Danlí, El Paraíso','',NULL,NULL),(22,'1201','Autobanco','Autobanco Comayagua','27721502','Comayagua','Comayagua','Boulevard 4to. Centenario, Comayagua','09:00 - 16:00','08:30 - 11:30',NULL),(23,'1301','Autobanco','Autobanco Siguatepeque','27730133','Comayagua','Siguatepeque','Ave. Francisco Morazán, Frente a Plaza San Pablo','09:00 - 17:00','08:30 - 14:00',NULL),(24,'1305','Autobanco','Autobanco San Juan',NULL,'Comayagua','Siguatepeque','Ave. Francisco Morazán, Frente a la reserva san juan Siguatepeque, Comayagua','09:00 - 17:00','08:30 - 14:00',NULL),(25,'154','Agencia','AGENCIA DIGITAL MALL MULTIPLAZA',NULL,NULL,'MALL MULTIPLAZA',NULL,'10:00 - 18:00','10:00 - 14:00',NULL),(26,'234','Agencia','AGENCIA DIGITAL MALL ALTARA',NULL,NULL,'MALL ALTARA',NULL,'10:00 - 18:00','10:00 - 14:00',NULL),(27,'245','Agencia','AGENCIA DIGITAL MALL GALERÍAS DEL VALLE',NULL,NULL,'MALL GALERÍAS DEL VALLE',NULL,'10:00 - 19:00','10:00 - 14:00',NULL);
UNLOCK TABLES;


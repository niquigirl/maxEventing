SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `MaxEventing` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `MaxEventing` ;

-- -----------------------------------------------------
-- Table `MaxEventing`.`task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `MaxEventing`.`taskTemplate` ;
DROP TABLE IF EXISTS `MaxEventing`.`associateTask` ;
DROP TABLE IF EXISTS `MaxEventing`.`autoTaskFlow` ;
DROP TABLE IF EXISTS `MaxEventing`.`prospect` ;
DROP TABLE IF EXISTS `MaxEventing`.`prospectContact` ;
DROP TABLE IF EXISTS `MaxEventing`.`prospectProperty` ;
DROP TABLE IF EXISTS `MaxEventing`.`rating` ;
DROP TABLE IF EXISTS `MaxEventing`.`notification` ;

-- -----------------------------------------------------
-- Table `MaxEventing`.`taskTemplate`
-- -----------------------------------------------------

CREATE  TABLE IF NOT EXISTS `MaxEventing`.`taskTemplate` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `descriptionKey` VARCHAR(200) NULL , -- internationalization
  `url` VARCHAR(2084) NULL , -- navigate to complete
  `detailUrl` VARCHAR(2084) NULL ,
  `formUrl` VARCHAR(2084) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `id` (`id` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `MaxEventing`.`associateTask`
-- -----------------------------------------------------

CREATE  TABLE IF NOT EXISTS `MaxEventing`.`associateTask` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `associateId` BIGINT NOT NULL ,
  `taskTemplateId` BIGINT NOT NULL ,
  `createdDate` DATETIME NULL ,
  `dueDate` DATETIME NULL ,
  `ignored` BIT(1) NULL,
   PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) ,
  INDEX `idx_AssociateTask_TaskTemplate` (`taskTemplateId` ASC) ,
  INDEX `idx_AssociateTask_AssociateId` (`associateId` ASC) ,
  CONSTRAINT `AssociateTask_TaskTemplate_fk`
    FOREIGN KEY (`taskTemplateId` )
    REFERENCES `MaxEventing`.`taskTemplate` (`id`)
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MaxEventing`.`autoTaskFlow`
-- -----------------------------------------------------

CREATE  TABLE IF NOT EXISTS `MaxEventing`.`autoTaskFlow` (
  `id` BIGINT NOT NULL AUTO_INCREMENT ,
  `taskTemplateId` BIGINT NOT NULL ,
  `eventName` VARCHAR(80) NULL ,
  `dependentTaskTemplateId` BIGINT NULL ,
  `numberCompleted` INT NULL ,
  `autoDueDateNumDays` INT NULL ,
  `assigneeType` VARCHAR(80) NULL ,
   PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_AutoTaskFlow_TaskTemplate`
    FOREIGN KEY (`taskTemplateId` )
    REFERENCES `MaxEventing`.`taskTemplate` (`id` )
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MaxEventing`.`prospect`
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS `MaxEventing`.`prospect` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL ,
  `cell` VARCHAR(45) NULL ,
  `home` VARCHAR(45) NULL ,
  `work` VARCHAR(45) NULL ,
  `secondary` VARCHAR(45) NULL ,
  `addressLine1` VARCHAR(200) NULL ,
  `addressLine2` VARCHAR(200) NULL ,
  `city` VARCHAR(45) NULL ,
  `state` VARCHAR(45) NULL ,
  `zip` VARCHAR(12) NULL ,
  `ownerId` BIGINT NULL , -- associate ID
  `dateCreated` DATETIME NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_Prospect_Name_idx` (`name` ASC),
  INDEX `idx_Prospect_OwnerId` (`ownerId` ASC)
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `MaxEventing`.`prospectContact`
-- ------------------------------------------------------

CREATE TABLE IF NOT EXISTS `MaxEventing`.`prospectContact` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `prospectId` BIGINT NULL ,
  `date` DATE NULL ,
  `contactTypeDescriptionKey` VARCHAR(200) NULL ,
  `artifactDescriptionKey` VARCHAR(200) NULL , -- need to internationalize
  `artifactUrl` VARCHAR(2084) NULL ,
  `notes` LONGTEXT NULL ,
  `createdDate` DATETIME NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_ProspectContact_Prospect_idx` (`prospectId` ASC) ,
  CONSTRAINT `ProspectContactProspectId`
    FOREIGN KEY (`prospectId` )
    REFERENCES `MaxEventing`.`prospect` (`id` )
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `MaxEventing`.`rating`
-- ------------------------------------------------------

CREATE TABLE IF NOT EXISTS `MaxEventing`.`rating` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ratingSetId` BIGINT NULL , -- for grouping
  `ratingValue` INT NULL ,
  `displayOrder` INT NULL ,
  `descriptionKey` VARCHAR(200) NULL ,
  `image` VARCHAR(2084) NULL ,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `MaxEventing`.`prospectProperty`
-- ------------------------------------------------------

CREATE TABLE IF NOT EXISTS `MaxEventing`.`prospectProperty` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `prospectId` BIGINT NULL ,
  `propertyNameKey` VARCHAR(200) NULL ,
  `ratingResponseId` BIGINT NULL ,
  `ratingSetId` BIGINT NULL ,
  `textResponse` LONGTEXT ,
  `notes` LONGTEXT NULL ,
  `dateResponse` DATETIME NULL ,
  `reminder` DATETIME NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_ProspectProperty_Prospect_idx` (`prospectId` ASC) ,
  INDEX `fk_ProspectProperty_Reminder_idx` (`reminder` DESC) ,
  CONSTRAINT `ProspectPropertyProspectId`
    FOREIGN KEY (`prospectId` )
    REFERENCES `MaxEventing`.`prospect` (`id` )
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `MaxEventing`.`notificationTemplate`
-- ------------------------------------------------------

CREATE TABLE IF NOT EXISTS `MaxEventing`.`notificationTemplate` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `eventName` VARCHAR(200) NULL ,
  `recipientType` VARCHAR(80) NULL ,
  `emailTemplateUrl` VARCHAR(2084) NULL ,
  `smsTemplateUrl` VARCHAR(2084) NULL ,
  `pushTemplateUrl` VARCHAR(2084) NULL ,
  PRIMARY KEY (`id`)
)
ENGINE = InnoDB;




SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
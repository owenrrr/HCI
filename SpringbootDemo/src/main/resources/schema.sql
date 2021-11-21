DROP DATABASE IF EXISTS seiii;
CREATE DATABASE seiii DEFAULT CHARACTER SET utf8;
USE seiii;

CREATE TABLE `relation`(
    `rid` INT AUTO_INCREMENT,
    `source_id` varchar(255) NOT NULL,
    `target_id` varchar(255) NOT NULL,
    `source` varchar(255) NOT NULL,
    `target` varchar(255) NOT NULL,
    `relation` varchar(255) NOT NULL,
    `type` varchar(255),
    `hash_id` varchar(255) NOT NULL,
    PRIMARY KEY (`rid`)
)ENGINE=InnoDB;

CREATE TABLE `entity`(
     `eid` varchar(255) NOT NULL,
     `name` varchar(45) NOT NULL,
     `type` varchar(255) NOT NULL,
     `property` LONGTEXT,
     PRIMARY KEY (`eid`)
)ENGINE=InnoDB;

CREATE TABLE `position`(
    `id` varchar(255) NOT NULL,
    `x` double NOT NULL,
    `y` double NOT NULL,
    PRIMARY KEY(`id`)
)
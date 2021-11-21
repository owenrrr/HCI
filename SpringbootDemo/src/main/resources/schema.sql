DROP DATABASE IF EXISTS seiii;
CREATE DATABASE seiii DEFAULT CHARACTER SET utf8;
USE seiii;

CREATE TABLE `relation`(
    `rid` INT AUTO_INCREMENT,
    `pid` INT NOT NULL default 1,
    `source_id` varchar(255) NOT NULL,
    `target_id` varchar(255) NOT NULL,
    `source` varchar(255) NOT NULL,
    `target` varchar(255) NOT NULL,
    `relation` varchar(255) NOT NULL,
    `type` varchar(255),
    `hash_id` varchar(255) NOT NULL,
    PRIMARY KEY (`rid`, `pid`)
)ENGINE=InnoDB;

CREATE TABLE `entity`(
     `eid` varchar(255) NOT NULL,
     `pid` INT NOT NULL default 1,
     `name` varchar(45) NOT NULL,
     `type` varchar(255) NOT NULL,
     `property` LONGTEXT,
     PRIMARY KEY (`eid`, `pid`)
)ENGINE=InnoDB;

CREATE TABLE `position`(
    `id` varchar(255) NOT NULL,
    `pid` INT NOT NULL default 1,
    `x` double NOT NULL,
    `y` double NOT NULL,
    PRIMARY KEY(`id`, `pid`)
)ENGINE=InnoDB;

CREATE TABLE `project`(
    `id` INT AUTO_INCREMENT,
    `uid` INT NOT NULL,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY(`id`)
)ENGINE=InnoDB;

CREATE TABLE `user`(
    `id` INT AUTO_INCREMENT,
    `password` varchar(255) NOT NULL,
    `mail` varchar(255) NOT NULL,
    PRIMARY KEY(`id`)
)ENGINE=InnoDB;
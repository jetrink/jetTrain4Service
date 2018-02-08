SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @`OLD_CHARACTER_SET_CLIENT` = @@`CHARACTER_SET_CLIENT` */;
/*!40101 SET @`OLD_CHARACTER_SET_RESULTS` = @@`CHARACTER_SET_RESULTS` */;
/*!40101 SET @`OLD_COLLATION_CONNECTION` = @@`COLLATION_CONNECTION` */;
/*!40101 SET NAMES utf8mb4 */;

CREATE TABLE `Persons` (
  `id`            INT(11)                      NOT NULL,
  `firstName`     VARCHAR(500)
                  COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `lastName`      VARCHAR(500)
                  COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `dateOfBirth`   DATE                         NOT NULL,
  `adressId`      INT(11)                      NOT NULL,
  `createdOn`     DATETIME                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `streetAddress` VARCHAR(500)
                  COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `city`          VARCHAR(100)
                  COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `state`         VARCHAR(100)
                  COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `country`       VARCHAR(100)
                  COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `postalCode`    VARCHAR(10)
                  COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `lastModified`  TIMESTAMP                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;

CREATE TABLE `Students` (
  `id`           INT(11)   NOT NULL,
  `personId`     INT(11)   NOT NULL,
  `gradeLevel`   INT(11)   NOT NULL,
  `managerId`    INT(11)   NOT NULL,
  `createdOn`    DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;

CREATE TABLE `Subjects` (
  `id`          INT(11)                      NOT NULL,
  `name`        VARCHAR(150)
                COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `field`       ENUM ('MATH', 'SCIENCE', 'LANGUAGE', 'ART', 'SOCIAL', 'OTHER')
                COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `description` VARCHAR(500)
                COLLATE `utf8mb4_unicode_ci` NOT NULL DEFAULT '',
  `createdOn`   DATETIME                     NOT NULL DEFAULT CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;

CREATE TABLE `TutorExpertise` (
  `id`        INT(11)  NOT NULL,
  `tutorId`   INT(11)  NOT NULL,
  `subjectId` INT(11)  NOT NULL,
  `rating`    INT(11)  NOT NULL DEFAULT '1'
  COMMENT '1-100',
  `createdOn` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;

CREATE TABLE `Tutors` (
  `id`           INT(10)    NOT NULL,
  `personId`     INT(11)    NOT NULL,
  `approved`     TINYINT(1) NOT NULL DEFAULT '0',
  `expYears`     INT(10)    NOT NULL DEFAULT '0',
  `createdOn`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;

CREATE TABLE `Users` (
  `id`               INT(11)                      NOT NULL,
  `email`            VARCHAR(150)
                     COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `password`         CHAR(60)
                     CHARACTER SET `utf8mb4`
                     COLLATE `utf8mb4_bin`        NOT NULL,
  `status`           ENUM ('ACTIVE', 'SUSPENDED', 'DISABLED')
                     COLLATE `utf8mb4_unicode_ci` NOT NULL DEFAULT 'ACTIVE',
  `isStudentManager` TINYINT(1)                   NOT NULL DEFAULT '0',
  `tutorId`          INT(11)                               DEFAULT NULL,
  `studentId`        INT(11)                               DEFAULT NULL,
  `lastActive`       DATETIME                              DEFAULT NULL,
  `lastIpAddress`    VARCHAR(50)
                     COLLATE `utf8mb4_unicode_ci`          DEFAULT NULL,
  `createdOn`        DATETIME                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified`     TIMESTAMP                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;

ALTER TABLE `Persons`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `Students`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_Students_Persons` (`personId`),
  ADD KEY `FK_Students_Users` (`managerId`);

ALTER TABLE `Subjects`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

ALTER TABLE `TutorExpertise`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `tutorId` (`tutorId`, `subjectId`),
  ADD KEY `FK_Expertise_Subjects` (`subjectId`);

ALTER TABLE `Tutors`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_Tutors_Persons` (`personId`);

ALTER TABLE `Users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `FK_Users_Students` (`studentId`),
  ADD KEY `FK_Users_Tutors` (`tutorId`);


ALTER TABLE `Persons`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Students`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Subjects`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `TutorExpertise`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Tutors`
  MODIFY `id` INT(10) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Users`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;


ALTER TABLE `Students`
  ADD CONSTRAINT `FK_Students_Persons` FOREIGN KEY (`personId`) REFERENCES `Persons` (`id`),
  ADD CONSTRAINT `FK_Students_Users` FOREIGN KEY (`managerId`) REFERENCES `Users` (`id`);

ALTER TABLE `TutorExpertise`
  ADD CONSTRAINT `FK_Expertise_Subjects` FOREIGN KEY (`subjectId`) REFERENCES `Subjects` (`id`)
  ON DELETE CASCADE,
  ADD CONSTRAINT `FK_Expertise_Tutors` FOREIGN KEY (`tutorId`) REFERENCES `Tutors` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `Tutors`
  ADD CONSTRAINT `FK_Tutors_Persons` FOREIGN KEY (`personId`) REFERENCES `Persons` (`id`);

ALTER TABLE `Users`
  ADD CONSTRAINT `FK_Users_Students` FOREIGN KEY (`studentId`) REFERENCES `Students` (`id`)
  ON DELETE SET NULL,
  ADD CONSTRAINT `FK_Users_Tutors` FOREIGN KEY (`tutorId`) REFERENCES `Tutors` (`id`)
  ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @`OLD_CHARACTER_SET_CLIENT` */;
/*!40101 SET CHARACTER_SET_RESULTS = @`OLD_CHARACTER_SET_RESULTS` */;
/*!40101 SET COLLATION_CONNECTION = @`OLD_COLLATION_CONNECTION` */;

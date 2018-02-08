CREATE TABLE `Sessions` (
  `id`            INT(11)                      NOT NULL,
  `studentId`   INT(11)  NOT NULL,
  `tutorId`   INT(11)  NOT NULL,
  `subjectId` INT(11)  NOT NULL,
   `description` VARCHAR(500)
                COLLATE `utf8mb4_unicode_ci`  NULL DEFAULT '',

   `startDateTime`   DATETIME                         NOT NULL,
   `endDateTime`   DATETIME                          NULL,
  `status` VARCHAR(100)                   NOT NULL,
  `createdOn`        DATETIME                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified`  TIMESTAMP                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;


ALTER TABLE `Sessions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_Sessions_Student` (`studentId`),
  ADD KEY `FK_Sessions_Tutor` (`tutorId`),
  ADD KEY `FK_Sessions_Subject` (`subjectId`);

ALTER TABLE `Sessions`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Sessions`
  ADD CONSTRAINT `FK_Sessions_Student` FOREIGN KEY (`studentId`) REFERENCES `Students` (`id`),
  ADD CONSTRAINT `FK_Sessions_Tutor` FOREIGN KEY (`tutorId`) REFERENCES `Tutors` (`id`),
  ADD CONSTRAINT `FK_Sessions_Subject` FOREIGN KEY (`subjectId`) REFERENCES `Subjects` (`id`);
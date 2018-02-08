CREATE TABLE `Schedules` (
  `id`            INT(11)                      NOT NULL,
  `tutorId`   INT(11)  NOT NULL,
  `dayAvailable`           ENUM ('MONDAY', 'TUESDAY', 'WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY')
                     COLLATE `utf8mb4_unicode_ci` NOT NULL,
                     `timeAvailableDescription` VARCHAR(100)
                COLLATE `utf8mb4_unicode_ci` NOT NULL DEFAULT 'All day',
  `createdOn`    DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified`  TIMESTAMP                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;


ALTER TABLE `Schedules`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_Schedules_Tutor` (`tutorId`);

ALTER TABLE `Schedules`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `Schedules`
  ADD CONSTRAINT `FK_Schedules_Tutor` FOREIGN KEY (`tutorId`) REFERENCES `Tutors` (`id`);
CREATE TABLE `SessionRequests` (
  `id`            INT(11)                      NOT NULL,
  `requestedFor`   INT(11)  NOT NULL,
  `requestedTo`   INT(11)  NOT NULL,
  `subjectId` INT(11)  NOT NULL,

   `sessionDateTime`   DATETIME                         NOT NULL,
  `status`        VARCHAR(10)
                COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `createdOn`    DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified`  TIMESTAMP                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;


ALTER TABLE `SessionRequests`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_SessionRequests_User` (`requestedFor`),
  ADD KEY `FK_SessionRequests_Tutor` (`requestedTo`),
  ADD KEY `FK_SessionRequests_Subject` (`subjectId`);

ALTER TABLE `SessionRequests`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `SessionRequests`
  ADD CONSTRAINT `FK_SessionRequests_User` FOREIGN KEY (`requestedFor`) REFERENCES `Users` (`id`),
  ADD CONSTRAINT `FK_SessionRequests_Tutor` FOREIGN KEY (`requestedTo`) REFERENCES `Tutors` (`id`),
  ADD CONSTRAINT `FK_SessionRequests_Subject` FOREIGN KEY (`subjectId`) REFERENCES `Subjects` (`id`);
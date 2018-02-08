CREATE TABLE `StudentReview` (
  `id`              INT(11)   NOT NULL,
  `tutorId`         INT(11)   NOT NULL,
  `studentId`       INT(11)   NOT NULL,
  `participation`   INT(11)   NOT NULL,
  `effort`          INT(11)   NOT NULL,
  `respectfullness` INT(11)   NOT NULL,
  `comprehension`   INT(11)   NOT NULL,
  `preperation`     INT(11)   NOT NULL,
  `createdOn`       DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;


ALTER TABLE `StudentReview`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_StudentReview_Tutor` (`tutorId`),
  ADD KEY `FK_StudentReview_Student` (`studentId`);

ALTER TABLE `StudentReview`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `StudentReview`
  ADD CONSTRAINT `FK_StudentReview_Tutor` FOREIGN KEY (`tutorId`) REFERENCES `Tutors` (`id`),
  ADD CONSTRAINT `FK_StudentReview_Student` FOREIGN KEY (`studentId`) REFERENCES `Students` (`id`),
  ADD CONSTRAINT `participationCK` CHECK (`participation` >= 0 AND `participation` <= 5),
  ADD CONSTRAINT `effortCK` CHECK (`effort` >= 0 AND `effort` <= 5),
  ADD CONSTRAINT `respectfullnessCK` CHECK (`respectfullness` >= 0 AND `respectfullness` <= 5),
  ADD CONSTRAINT `comprehensionCK` CHECK (`comprehension` >= 0 AND `comprehension` <= 5),
  ADD CONSTRAINT `preperationCK` CHECK (`preperation` >= 0 AND `preperation` <= 5);

CREATE TABLE `TutorReview` (
  `id`           INT(11)   NOT NULL,
  `tutorId`      INT(11)   NOT NULL,
  `studentId`    INT(11)   NOT NULL,
  `knowledge`    INT(11)   NOT NULL,
  `helpful`      INT(11)   NOT NULL,
  `friendlyness` INT(11)   NOT NULL,
  `prepared`     INT(11)   NOT NULL,
  `createdOn`    DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_unicode_ci`;


ALTER TABLE `TutorReview`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_TutorReview_Tutor` (`tutorId`),
  ADD KEY `FK_TutorReview_Student` (`studentId`);

ALTER TABLE `TutorReview`
  MODIFY `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `TutorReview`
  ADD CONSTRAINT `FK_TutorReview_Tutor` FOREIGN KEY (`tutorId`) REFERENCES `Tutors` (`id`),
  ADD CONSTRAINT `FK_tutorReview_Student` FOREIGN KEY (`studentId`) REFERENCES `Students` (`id`),
  ADD CONSTRAINT `knowledgeCK` CHECK (`knowledge` >= 0 AND `knowledge` <= 5),
  ADD CONSTRAINT `helpfulCK` CHECK (`helpful` >= 0 AND `helpful` <= 5),
  ADD CONSTRAINT `friendlynessCK` CHECK (`friendlyness` >= 0 AND `friendlyness` <= 5),
  ADD CONSTRAINT `preparedCK` CHECK (`prepared` >= 0 AND `prepared` <= 5);

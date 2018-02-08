ALTER TABLE `SessionRequests`
DROP FOREIGN KEY `FK_SessionRequests_User`;
ALTER TABLE `SessionRequests`
DROP INDEX `FK_SessionRequests_User` ;
ALTER TABLE `SessionRequests`
ADD CONSTRAINT `FK_SessionRequests_Student`
  FOREIGN KEY (`requestedFor`)
  REFERENCES `Students` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

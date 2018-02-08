ALTER TABLE `Users`
ADD `managerPersonId` INT(11)        DEFAULT NULL;

ALTER TABLE `Users`
  ADD KEY `FK_Users_Managers` (`managerPersonId`);

ALTER TABLE `Users`
  ADD CONSTRAINT `FK_Users_Managers` FOREIGN KEY (`managerPersonId`) REFERENCES `Persons` (`id`)
  ON DELETE SET NULL;
# delete addressId column in persons table
ALTER TABLE `Persons`
  DROP COLUMN `adressId`;

# make managerId nullable
ALTER TABLE `Students`
  CHANGE `managerId` `managerId` INT(11) NULL DEFAULT NULL;
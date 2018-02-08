# add a username column
ALTER TABLE `Users`
  ADD `username` VARCHAR(100) NOT NULL
  AFTER `id`,
  ADD UNIQUE (`username`);

# add a salt for the password

# change the statistical columns so we dont have to hit the DB every query
ALTER TABLE `Users`
  CHANGE `lastActive` `lastLogin` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CHANGE `lastIpAddress` `lastLoginIp` VARCHAR(50)
CHARACTER SET `utf8mb4`
COLLATE `utf8mb4_unicode_ci` NOT NULL;


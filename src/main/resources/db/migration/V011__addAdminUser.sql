# create the AdminUsers table
CREATE TABLE `AdminUsers` (
  `id`           INT(11)                                  NOT NULL AUTO_INCREMENT,
  `email`        VARCHAR(150)
                 COLLATE `utf8mb4_unicode_ci`             NOT NULL,
  `username`     VARCHAR(100)
                 COLLATE `utf8mb4_unicode_ci`             NOT NULL,
  `password`     CHAR(60)
                 CHARACTER SET `utf8mb4`
                 COLLATE `utf8mb4_bin`                    NOT NULL,
  `status`       ENUM ('ACTIVE', 'SUSPENDED', 'DISABLED')
                 COLLATE `utf8mb4_unicode_ci`             NOT NULL DEFAULT 'ACTIVE',
  `lastLogin`    DATETIME                                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastLoginIp`  VARCHAR(50)                              NOT NULL,
  `createdOn`    DATETIME                                 NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastModified` TIMESTAMP ON UPDATE CURRENT_TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE (`username`)
)
  ENGINE = InnoDB;

# add the config-file based System admin user
INSERT INTO `AdminUsers` (`id`, `username`, `email`, `password`, `lastLoginIp`)
VALUES (1, 'System', 'system@localhost', '', '127.0.0.1');

SET foreign_key_checks = 0;
ALTER TABLE `tweet`
MODIFY COLUMN `id` VARCHAR(36) PRIMARY KEY;
SET foreign_key_checks = 1;
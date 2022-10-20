
SET foreign_key_checks = 0;
ALTER TABLE `tweet`
    MODIFY COLUMN `user_id` VARCHAR(36);
SET foreign_key_checks = 1;
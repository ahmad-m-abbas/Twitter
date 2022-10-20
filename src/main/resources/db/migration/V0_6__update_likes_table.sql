SET foreign_key_checks = 0;
ALTER TABLE `likes`
MODIFY COLUMN `tweet_id` BIGINT NOT NULL ,
MODIFY COLUMN `user_id` VARCHAR(36);
SET foreign_key_checks = 1;

SET foreign_key_checks = 0;
ALTER TABLE `tweet`
    MODIFY COLUMN `id` SERIAL;
ALTER TABLE `likes`
    MODIFY COLUMN `tweet_id` SERIAL;
SET foreign_key_checks = 1;
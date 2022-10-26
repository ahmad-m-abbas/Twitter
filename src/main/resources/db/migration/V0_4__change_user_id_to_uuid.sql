ALTER TABLE `likes`
    DROP FOREIGN KEY likes_ibfk_2;
ALTER TABLE `tweet`
    DROP FOREIGN KEY tweet_ibfk_1;
ALTER TABLE `user`
    MODIFY COLUMN `id` VARCHAR(36) PRIMARY KEY;
ALTER TABLE `tweet`
    MODIFY COLUMN `user_id` VARCHAR(36),
    ADD FOREIGN KEY fk_tweet_user_id (`user_id`) REFERENCES `user` (`id`);
ALTER TABLE `likes`
    MODIFY COLUMN `user_id` VARCHAR(36),
    ADD FOREIGN KEY fk_likes_user_id (`user_id`) REFERENCES `user` (`id`);
ALTER TABLE `likes`
    DROP FOREIGN KEY likes_ibfk_2;
ALTER TABLE `tweet`
    DROP FOREIGN KEY tweet_ibfk_1;
ALTER TABLE `user`
    MODIFY COLUMN `id` SERIAL;
ALTER TABLE `tweet`
    MODIFY COLUMN `user_id` BIGINT UNSIGNED NOT NULL,
    ADD FOREIGN KEY fk_tweet_user_id (`user_id`) REFERENCES `user` (`id`);
ALTER TABLE `likes`
    MODIFY COLUMN `user_id` BIGINT UNSIGNED NOT NULL,
    ADD FOREIGN KEY fk_likes_user_id (`user_id`) REFERENCES `user` (`id`);
ALTER TABLE `likes`
    DROP FOREIGN KEY fk_likes_user_id ;
ALTER TABLE `likes`
    ADD FOREIGN KEY likes_ibfk_1 (`tweet_id`) REFERENCES `tweet` (`id`);
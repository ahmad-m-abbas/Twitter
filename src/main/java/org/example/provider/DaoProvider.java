package org.example.provider;

import org.example.dao.LikesDao;
import org.example.dao.TweetDao;
import org.example.dao.UserDao;

import java.util.function.Consumer;
import java.util.function.Function;

public class DaoProvider {


    private static DaoProvider instance = instance();

    private DaoProvider() {

    }

    public static final DaoProvider instance() {
        if (instance == null) {
            instance = new DaoProvider();
        }
        return instance;
    }

    public static final DaoProvider daos() {
        return instance();
    }
    public void useUserDao(Consumer<UserDao> consumer) {
        JdbiProvider.instance().jdbi().useExtension(UserDao.class, (userDao) -> {
            consumer.accept(userDao);
        });
    }

    public <T> T withUserDao(Function<UserDao, T> function) {
        return JdbiProvider.instance().jdbi().withExtension(UserDao.class, (userDao) -> {
            return function.apply(userDao);
        });
    }
    public void useTweetDao(Consumer<TweetDao> consumer) {
        JdbiProvider.instance().jdbi().useExtension(TweetDao.class, (tweetDao) -> {
            consumer.accept(tweetDao);
        });
    }
    public <T> T withTweetDao(Function<TweetDao, T> function) {
        return JdbiProvider.instance().jdbi().withExtension(TweetDao.class, (tweetDao) -> {
            return function.apply(tweetDao);
        });
    }
    public void useLikesDao(Consumer<LikesDao> consumer) {
        JdbiProvider.instance().jdbi().useExtension(LikesDao.class, (likesDao) -> {
            consumer.accept(likesDao);
        });
    }
    public <T> T withLikesDao(Function<LikesDao, T> function) {
        return JdbiProvider.instance().jdbi().withExtension(LikesDao.class, (likesDao) -> {
            return function.apply(likesDao);
        });
    }

}

package org.example.repository;


import org.example.dao.UserDao;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.jdbi.v3.sqlobject.customizer.Bind;

import java.util.List;

import static org.example.provider.DaoProvider.daos;

public class UserRepository {

    private static UserRepository instance = null;

    private UserRepository() {
    }


    public static UserRepository instance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    public UserDto get(String id) {

        return daos().withUserDao((userDao) -> userDao.getUser(id));
    }
    public void update(String id, UserDto userDto) {
        daos().useUserDao((userDao) -> userDao.update(id, userDto));
    }

    public long insert(UserDto userDto) {
        return daos().withUserDao((userDao) -> userDao.insert(userDto));
    }
    public List<TweetDto> getUserTweets(String userId){
        return daos().withUserDao((userDao) -> userDao.getUserTweets(userId));
    }


    public List<UserDto> list(String name) {
        return daos().withUserDao(userDao -> userDao.list(name));
    }
    public List<UserDto> getUserByName(String name) {
        return daos().withUserDao(userDao -> userDao.getUserByName(name));
    }
}

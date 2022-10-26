package org.example.service;


import org.example.dao.query.SearchFriendQuery;
import org.example.dto.FriendsDto;
import org.example.exceptions.UsedEmailException;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.exceptions.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.provider.DaoProvider.daos;

public class UserService {

    private static UserService instance = null;

    private UserService() {
    }

    public static UserService instance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }
    public UserDto findUser(String id) {

        return daos().withUserDao((userDao) -> userDao.getUser(id));
    }

    public void updateUser(String id, UserDto userDto) throws UsedEmailException {
        List<UserDto> list = listUsers("");
        boolean exist = list.stream().noneMatch(user -> user.getEmail()
                .equals(userDto.getEmail()));
        if (!exist) {
            throw new UsedEmailException(userDto.getEmail());
        }
        daos().useUserDao((userDao) -> userDao.update(id, userDto));
    }

    public long addUser(UserDto userDto) throws UsedEmailException {
        List<UserDto> list = listUsers("");
        boolean exist = list.stream().filter(user -> user.getEmail()
                        .equals(userDto.getEmail()))
                .collect(Collectors.toList()).isEmpty();
        if (!exist) {
            throw new UsedEmailException(userDto.getEmail());
        }
        return daos().withUserDao((userDao) -> userDao.insert(userDto));
    }

    public List<TweetDto> getUserTweets(String userId) {
        return daos().withUserDao((userDao) -> userDao.getUserTweets(userId));
    }

    public List<UserDto> listUsers(String name) {
        return daos().withUserDao(userDao -> userDao.list(name));
    }

    public List<UserDto> getUserByName(String name) {
        return daos().withUserDao(userDao -> userDao.getUserByName(name));
    }

    public List<UserDto> getUsersLikedTweet(String tweetId) {
        return daos().withLikesDao(likesDao -> likesDao.getUsers(tweetId));
    }

    public List<TweetDto> getFriendsTweets(String userId) {
        return daos().withFriendsDao(friendsDao -> friendsDao.getFriendsTweets(userId));
    }

    public List<UserDto> getFriends(String userId) {
        return daos().withFriendsDao(friendsDao -> friendsDao.getFriends(userId));
    }

    public List<UserDto> search(String userId, SearchFriendQuery searchFriendQuery) {
        return daos().withFriendsDao(friendsDao -> friendsDao.search(userId, searchFriendQuery));
    }

    public void addFriends(FriendsDto friendsDto) throws UserNotFoundException {
        UserDto userDto1 = UserService.instance().findUser(friendsDto.getFirstUser());
        UserDto userDto2 = UserService.instance().findUser(friendsDto.getSecondUser());
        if (userDto1 == null) {
            throw new UserNotFoundException(friendsDto.getFirstUser());
        }
        if (userDto2 == null) {
            throw new UserNotFoundException(friendsDto.getSecondUser());
        }
        daos().useFriendsDao(friendsDao -> friendsDao.addFriends(friendsDto));
    }

    public void delete(FriendsDto friendsDto) {
        daos().useFriendsDao(friendsDao -> friendsDao.delete(friendsDto));
    }

}

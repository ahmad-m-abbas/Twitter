package org.example.service;

import exceptions.UserNotFoundException;
import org.example.dao.query.SearchFriendQuery;
import org.example.dto.FriendsDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.repository.FriendsRepository;
import org.example.repository.LikesRepository;

import java.util.List;

import static org.example.provider.DaoProvider.daos;

public class FriendsService {

    private static FriendsService instance = null;
    private final FriendsRepository friendsRepository;

    private FriendsService() {
        friendsRepository = FriendsRepository.instance();
    }

    public static FriendsService instance() {
        if (instance == null) {
            instance = new FriendsService();
        }
        return instance;
    }

    public List<TweetDto> getFriendsTweets(String userId) {
        return friendsRepository.getFriendsTweets(userId);
    }

    public List<UserDto> getFriends(String userId) {
        return friendsRepository.getFriends(userId);
    }

    public List<UserDto> search(String userId, SearchFriendQuery searchFriendQuery) {
        return friendsRepository.search(userId, searchFriendQuery);
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
        friendsRepository.addFriends(friendsDto);
    }

    public void delete(FriendsDto friendsDto) {
        friendsRepository.delete(friendsDto);
    }
}

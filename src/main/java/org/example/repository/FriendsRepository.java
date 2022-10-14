package org.example.repository;

import org.example.dto.FriendsDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;

import java.util.List;

import static org.example.provider.DaoProvider.daos;

public class FriendsRepository {

    private static FriendsRepository friendsRepository = null;
    public FriendsRepository(){

    }


    public static FriendsRepository instance() {
        if (friendsRepository == null) {
            friendsRepository = new FriendsRepository();
        }
        return friendsRepository;
    }

    public List<TweetDto> getFriendsTweets(String userId){
        return daos().withFriendsDao(friendsDao -> friendsDao.getFriendsTweets(userId));
    }

    public List<UserDto> getFriends(String userId){
        return daos().withFriendsDao(friendsDao -> friendsDao.getFriends(userId));
    }

    public void addFriends( FriendsDto friendsDto){
        daos().useFriendsDao(friendsDao -> friendsDao.addFriends(friendsDto));
    }
}
package org.example.repository;

import org.example.dto.LikesDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;

import java.util.List;

import static org.example.provider.DaoProvider.daos;

public class LikesRepository {
    private static LikesRepository likesRepository = null;

    public LikesRepository(){

    }


    public static LikesRepository instance() {
        if (likesRepository == null) {
            likesRepository = new LikesRepository();
        }
        return likesRepository;
    }
    public List<UserDto> getUsers(String tweetId){
        return daos().withLikesDao(likesDao -> likesDao.getUsers(tweetId));
    }
    public List<TweetDto> getTweets(String userId){
        return daos().withLikesDao(likesDao -> likesDao.getTweets(userId));
    }

    public void addLike(LikesDto likesDto){
        daos().useLikesDao(likesDao -> likesDao.insert(likesDto));
    }
    public void unlike(LikesDto likesDto){
        daos().useLikesDao(likesDao -> likesDao.unlike(likesDto));
    }
}
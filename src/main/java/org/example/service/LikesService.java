package org.example.service;

import org.example.dto.LikesDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.repository.LikesRepository;


import java.util.List;

import static org.example.provider.DaoProvider.daos;

public class LikesService {
    private static LikesService instance = null;
    private final LikesRepository likesRepository;

    private LikesService(){
        likesRepository=LikesRepository.instance();
    }
    public static LikesService instance() {
        if (instance == null) {
            instance = new LikesService();
        }
        return instance;
    }
    public List<UserDto> getUsers(String tweetId){
        return likesRepository.getUsers(tweetId);
    }
    public List<TweetDto> getTweets(String userId){
        return likesRepository.getTweets(userId);
    }
    public void add(LikesDto likesDto){
        likesRepository.addLike(likesDto);
    }
    public void unlike(LikesDto likesDto){
        likesRepository.unlike(likesDto);
    }
}
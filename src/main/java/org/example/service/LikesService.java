package org.example.service;

import org.example.exceptions.TweetNotFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.dto.LikesDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.repository.LikesRepository;


import java.util.List;

public class LikesService {
    private static LikesService instance = null;
    private final LikesRepository likesRepository;

    private LikesService() {
        likesRepository = LikesRepository.instance();
    }

    public static LikesService instance() {
        if (instance == null) {
            instance = new LikesService();
        }
        return instance;
    }

    public List<UserDto> getUsers(String tweetId) {
        return likesRepository.getUsers(tweetId);
    }

    public List<TweetDto> getTweets(String userId) {
        return likesRepository.getTweets(userId);
    }

    public void add(LikesDto likesDto) throws UserNotFoundException, TweetNotFoundException {
        UserDto userDto = UserService.instance().findUser(likesDto.getUserId());
        TweetDto tweetDto = TweetService.instance().getTweet(likesDto.getTweetId());
        if (userDto == null) {
            throw new UserNotFoundException(likesDto.getUserId());
        }
        if (tweetDto == null) {
            throw new TweetNotFoundException(likesDto.getTweetId());
        }
        likesRepository.addLike(likesDto);
    }

    public void unlike(LikesDto likesDto) {
        likesRepository.unlike(likesDto);
    }
}
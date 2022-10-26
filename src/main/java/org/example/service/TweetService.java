package org.example.service;

import org.example.exceptions.TextOverflowException;
import org.example.exceptions.UserNotFoundException;
import org.example.dao.query.SearchTweetQuery;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.repository.TweetRepository;

import java.util.List;

public class TweetService {

    private static TweetService instance = null;
    private final TweetRepository tweetRepository;

    private TweetService() {
        tweetRepository = TweetRepository.instance();
    }

    public static TweetService instance() {
        if (instance == null) {
            instance = new TweetService();
        }

        return instance;
    }

    public List<TweetDto> list() {
        return tweetRepository.list();
    }

    public TweetDto getTweet(String id) {
        return tweetRepository.getTweet(id);
    }

    public void insert(TweetDto tweetDto) throws UserNotFoundException, TextOverflowException {
        UserDto userDto = UserService.instance().findUser(
                tweetDto.getUserId()
        );
        if (userDto == null) {
            throw new UserNotFoundException(tweetDto.getUserId());
        }
        if (tweetDto.getText().length() > 280) {
            throw new TextOverflowException("");
        }
        tweetRepository.insert(tweetDto);
    }

    public void delete(String id) throws UserNotFoundException, TextOverflowException {
        TweetDto tweetDto = TweetService.instance().getTweet(id);
        if (tweetDto == null) {
            throw new UserNotFoundException(id);
        }

        tweetRepository.delete(id);
    }

    public void update(TweetDto tweetDto) throws TextOverflowException {
        if (tweetDto.getText().length() > 280) {
            throw new TextOverflowException("");
        }
        tweetRepository.update(tweetDto);
    }

    public List<TweetDto> search(SearchTweetQuery searchTweetQuery) {
        return tweetRepository.search(searchTweetQuery);
    }
}
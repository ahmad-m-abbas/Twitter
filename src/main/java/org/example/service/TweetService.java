package org.example.service;

import org.example.dao.TweetDao;
import org.example.dto.LikesDto;
import org.example.exceptions.TextOverflowException;
import org.example.exceptions.TweetNotFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.dao.query.SearchTweetQuery;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;

import java.util.List;

import static org.example.provider.DaoProvider.daos;

public class TweetService {

    private static TweetService instance = null;

    private TweetService() {


    }

    public static TweetService instance() {
        if (instance == null) {
            instance = new TweetService();
        }

        return instance;
    }

    public List<TweetDto> list() {
        return daos().withTweetDao(TweetDao::list);
    }
    public TweetDto getTweet(String id) {
        return daos().withTweetDao((tweetDao) -> tweetDao.getTweet(id));
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
        daos().useTweetDao((tweetDao -> tweetDao.insert(tweetDto)));
    }

    public void delete(String id) throws UserNotFoundException, TextOverflowException {
        TweetDto tweetDto = TweetService.instance().getTweet(id);
        if (tweetDto == null) {
            throw new UserNotFoundException(id);
        }

        daos().useTweetDao(tweetDao -> tweetDao.delete(id));
    }

    public void update(TweetDto tweetDto) throws TextOverflowException {
        if (tweetDto.getText().length() > 280) {
            throw new TextOverflowException("");
        }
        daos().useTweetDao(tweetDao -> tweetDao.update(tweetDto));
    }

    public List<TweetDto> search(SearchTweetQuery searchTweetQuery) {
        return daos().withTweetDao(tweetDao -> tweetDao.search(searchTweetQuery));
    }

    public List<TweetDto> getTweetsLikedByUser(String userId) {
        return daos().withLikesDao(likesDao -> likesDao.getTweets(userId));
    }
    public void addLike(LikesDto likesDto) throws UserNotFoundException, TweetNotFoundException {
        UserDto userDto = UserService.instance().findUser(likesDto.getUserId());
        TweetDto tweetDto = TweetService.instance().getTweet(likesDto.getTweetId());
        if (userDto == null) {
            throw new UserNotFoundException(likesDto.getUserId());
        }
        if (tweetDto == null) {
            throw new TweetNotFoundException(likesDto.getTweetId());
        }
        daos().useLikesDao(likesDao -> likesDao.insert(likesDto));
    }

    public void unlike(LikesDto likesDto) {
        daos().useLikesDao(likesDao -> likesDao.unlike(likesDto));
    }

}

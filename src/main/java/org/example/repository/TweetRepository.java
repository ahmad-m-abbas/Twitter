package org.example.repository;

import org.example.dao.TweetDao;
import org.example.dao.query.SearchTweetQuery;
import org.example.dto.TweetDto;

import java.util.List;

import static org.example.provider.DaoProvider.daos;

public class TweetRepository {
    private static TweetRepository tweetRepository = null;

    public TweetRepository() {

    }

    public static TweetRepository instance() {
        if (tweetRepository == null) {
            tweetRepository = new TweetRepository();
        }
        return tweetRepository;
    }

    public List<TweetDto> list() {
        return daos().withTweetDao(TweetDao::list);
    }

    public TweetDto getTweet(String id) {
        return daos().withTweetDao((tweetDao) -> tweetDao.getTweet(id));
    }

    public void insert(TweetDto tweetDto) {
        daos().useTweetDao((tweetDao -> tweetDao.insert(tweetDto)));
    }

    public void delete(String id) {
        daos().useTweetDao(tweetDao -> tweetDao.delete(id));
    }

    public void update(TweetDto tweetDto) {
        daos().useTweetDao(tweetDao -> tweetDao.update(tweetDto));
    }

    public List<TweetDto> search(SearchTweetQuery searchTweetQuery) {
        return daos().withTweetDao(tweetDao -> tweetDao.search(searchTweetQuery));
    }
}
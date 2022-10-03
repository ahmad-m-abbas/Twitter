package org.example.repository;

import org.example.dao.TweetDao;
import org.example.dao.query.SearchTweetQuery;
import org.example.dto.TweetDto;

import java.util.List;

import static org.example.provider.DaoProvider.daos;

public class TweetRepository {
    private static TweetRepository tweetRepository = null;

    public TweetRepository(){

    }

    public static TweetRepository instance() {
        if (tweetRepository == null) {
            tweetRepository = new TweetRepository();
        }
        return tweetRepository;
    }

    public List<TweetDto> list(){
        return daos().withTweetDao(TweetDao::list);
    }
    public TweetDto getTweet(String id){
        return daos().withTweetDao((tweetDao) -> tweetDao.getTweet(id));
    }

    public long insert(TweetDto tweetDto){
        return daos().withTweetDao((tweetDao -> tweetDao.insert(tweetDto)));
    }

    public void delete(String id){
        daos().useTweetDao(tweetDao -> tweetDao.delete(id));
    }

    public void update(String id, TweetDto tweetDto){
        daos().useTweetDao(tweetDao -> tweetDao.update(id, tweetDto));
    }

    public List<TweetDto> search(String name ,SearchTweetQuery searchTweetQuery){
        return daos().withTweetDao(tweetDao -> tweetDao.search(name , searchTweetQuery));
    }
}
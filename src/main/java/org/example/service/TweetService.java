package org.example.service;

import org.example.dao.query.SearchTweetQuery;
import org.example.dto.TweetDto;
import org.example.repository.TweetRepository;

import java.util.List;

public class TweetService {

    private static TweetService instance = null;
    private final TweetRepository tweetRepository;

    private TweetService(){
        tweetRepository=TweetRepository.instance();
    }
    public static TweetService instance() {
        if (instance == null) {
            instance = new TweetService();
        }

        return instance;
    }

    public List<TweetDto> list(){
        return tweetRepository.list();
    }
    public TweetDto getTweet(String id){
        return tweetRepository.getTweet(id);
    }
    public long insert(TweetDto tweetDto){
        return tweetRepository.insert(tweetDto);
    }
    public void delete(String id){
        tweetRepository.delete(id);
    }
    public void update(String id,TweetDto tweetDto){
        tweetRepository.update(id,tweetDto);
    }
    public List<TweetDto> search(String name , SearchTweetQuery searchTweetQuery){
        return tweetRepository.search(name,searchTweetQuery);
    }
}
package org.example.test.fixture;

import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.exceptions.TextOverflowException;
import org.example.exceptions.UsedEmailException;
import org.example.exceptions.UserNotFoundException;
import org.example.service.TweetService;
import org.example.service.UserService;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TweetFixture {
    public static List<TweetDto> create(int count,String userId,int start) throws UserNotFoundException, TextOverflowException {
        List<TweetDto> tweets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TweetDto tweetDto = make(userId);
            tweetDto.setId(""+(i+start+1));
            TweetService.instance().insert(tweetDto);
            tweets.add(tweetDto);
        }

        return tweets;
    }

    public static TweetDto make(String userId) {
        TweetDto tweetDto = new TweetDto();
        tweetDto.setUserId(userId);
        tweetDto.setText(RandomStringUtils.randomAlphabetic(280));
        tweetDto.setCreated_on(new Date(ThreadLocalRandom.current().nextInt() * 1000L));

        return tweetDto;
    }
}

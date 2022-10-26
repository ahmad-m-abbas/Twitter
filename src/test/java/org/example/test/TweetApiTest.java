package org.example.test;

import org.example.Role;
import org.example.dto.LikesDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.test.fixture.TweetFixture;
import org.junit.Test;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class TweetApiTest extends TwitterTest{

    @Test
    public void get_all_tweets_test(){
        startActor((server,client)->{
            UserDto firstUser = new UserDto();
            firstUser.setId("auth0|Ahmad");
            firstUser.setName("Ahmad Abbas");
            firstUser.setEmail("ahmad@test.ps");
            client.post("/api/user",firstUser);

            UserDto secondUser = new UserDto();
            secondUser.setId("auth0|Adam");
            secondUser.setName("Adam Abbas");
            secondUser.setEmail("adam@test.ps");
            client.post("/api/user",secondUser);

            List<TweetDto> all = TweetFixture.create(20,firstUser.getId(),0);
            all.addAll(TweetFixture.create(20,secondUser.getId(),20));

            List<TweetDto> allApi = Lists.newArrayList(parseBody(client.get("/api/tweet"),TweetDto[].class));
            allApi = allApi.stream().filter(all::contains).collect(Collectors.toList());
            assert(allApi.isEmpty());

            int deleteID = ThreadLocalRandom.current().nextInt(1, 40 + 1);
            client.delete(String.format("/api/tweet/%d",deleteID));
            int updateId = ThreadLocalRandom.current().nextInt(1, 40 + 1);
            while (updateId==deleteID)updateId = ThreadLocalRandom.current().nextInt(1, 40 + 1);
            TweetDto toUpdate = parseBody(client.get(String.format("/api/tweet/%d",updateId)),TweetDto.class);
            client.put(String.format("/api/tweet/%d",updateId),toUpdate);
            all.set(updateId-1,toUpdate);
            for (int i = 0; i < 40; i++) {
                System.out.println();
                System.out.println(i+" "+deleteID);
                System.out.println(i+" "+updateId);
                if(i+1==deleteID){
                    try {
                        TweetDto getById = parseBody(client.get(String.format("/api/tweet/%d",(i+1))),TweetDto.class);
                        assert false;
                    }
                    catch (Exception e) {
                        assert true;
                    }
                    continue;
                }
                TweetDto getById = parseBody(client.get(String.format("/api/tweet/%d",(i+1))),TweetDto.class);
                assert(all.get(i).getUserId().equals(getById.getUserId()));
                assert(all.get(i).getId().equals(getById.getId()));
                assert(all.get(i).getText().equals(getById.getText()));
            }
        },"ahmad","ahmad@test.ps", String.valueOf(Role.USER));
    }

    @Test
    public void likes_test(){
        startActor((server,client)->{
            UserDto firstUser = new UserDto();
            firstUser.setId("auth0|Ahmad");
            firstUser.setName("Ahmad Abbas");
            firstUser.setEmail("ahmad@test.ps");
            client.post("/api/user",firstUser);

            UserDto secondUser = new UserDto();
            secondUser.setId("auth0|Adam");
            secondUser.setName("Adam Abbas");
            secondUser.setEmail("adam@test.ps");
            client.post("/api/user",secondUser);

            List<TweetDto> firstUserTweets = TweetFixture.create(20,firstUser.getId(),0);
            List<TweetDto> secondUserTweets = TweetFixture.create(20,secondUser.getId(),20);

            for (int i = 0; i < 20; i++) {
                client.post("/api/tweet/like",new LikesDto(firstUserTweets.get(i).getId(),secondUser.getId()));
                client.post("/api/tweet/like",new LikesDto(secondUserTweets.get(i).getId(),firstUser.getId()));
            }

            List<TweetDto> firstUserLikes = Lists.newArrayList(parseBody(client.get(String.format("/api/user/%s/likes",firstUser.getId())),TweetDto[].class));
            List<TweetDto> secondUserLikes = Lists.newArrayList(parseBody(client.get(String.format("/api/user/%s/likes",secondUser.getId())),TweetDto[].class));
            secondUserLikes = secondUserLikes.stream().filter(firstUserTweets::contains).collect(Collectors.toList());
            firstUserLikes = firstUserLikes.stream().filter(secondUserTweets::contains).collect(Collectors.toList());
            assert firstUserLikes.isEmpty();
            assert secondUserLikes.isEmpty();

            for (int i = 0; i < 20; i++) {
                client.delete("/api/tweet/unlike",new LikesDto(firstUserTweets.get(i).getId(),secondUser.getId()));
                client.delete("/api/tweet/unlike",new LikesDto(secondUserTweets.get(i).getId(),firstUser.getId()));
            }
            firstUserLikes = Lists.newArrayList(parseBody(client.get(String.format("/api/user/%s/likes",firstUser.getId())),TweetDto[].class));
            secondUserLikes = Lists.newArrayList(parseBody(client.get(String.format("/api/user/%s/likes",secondUser.getId())),TweetDto[].class));

            assert firstUserLikes.isEmpty();
            assert secondUserLikes.isEmpty();
        },"ahmad","ahmad@test.ps", String.valueOf(Role.USER));
    }

    @Test
    public void search_tweet_test(){
        startActor((server,client)->{
            UserDto firstUser = new UserDto();
            firstUser.setId("auth0|Ahmad");
            firstUser.setName("Ahmad Abbas");
            firstUser.setEmail("ahmad@test.ps");
            client.post("/api/user",firstUser);

            TweetDto tweetDto = new TweetDto();
            tweetDto.setId("1");
            tweetDto.setText("This tweet contains FOOTHILL");
            tweetDto.setUserId(firstUser.getId());
            tweetDto.setCreated_on(new Date());
            client.post("/api/tweet",tweetDto);
            List<TweetDto> containsFoothill = Lists.newArrayList(parseBody(client.get("/api/tweet/contains?name=&toBeIn=FOOT"),TweetDto[].class));
            assert !containsFoothill.isEmpty();

            containsFoothill = Lists.newArrayList(parseBody(client.get("/api/tweet/contains?name=&toBeIn=foot"),TweetDto[].class));
            assert !containsFoothill.isEmpty();

            containsFoothill = Lists.newArrayList(parseBody(client.get("/api/tweet/contains?name=&toBeIn=x"),TweetDto[].class));
            assert containsFoothill.isEmpty();

            containsFoothill = Lists.newArrayList(parseBody(client.get("/api/tweet/contains?name=Ahmad"),TweetDto[].class));
            assert !containsFoothill.isEmpty();
        },"ahmad","ahmad@test.ps", String.valueOf(Role.USER));
    }
}

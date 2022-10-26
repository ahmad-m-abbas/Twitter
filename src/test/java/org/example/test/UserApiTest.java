package org.example.test;

import org.example.Role;
import org.example.dto.FriendsDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.test.fixture.TweetFixture;
import org.example.test.fixture.UserFixture;
import org.junit.Test;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserApiTest extends TwitterTest {

    @Test
    public void create_user_test() {
        startActor((server, client) -> {
            UserDto userToCreate = new UserDto();
            userToCreate.setId("auth0|ahmad123s");
            userToCreate.setName("Ahmad Abbas");
            userToCreate.setEmail("ahmad@test.ps");

            var res = client.post("/api/user/", userToCreate);
            assertEquals(res.code(), 200);

            UserDto createdUser = parseBody(
                    client.get(String.format("/api/user/%s", "auth0|ahmad123s")),
                    UserDto.class
            );

            assertEquals(userToCreate.getName(), createdUser.getName());
            assertEquals(userToCreate.getEmail(), createdUser.getEmail());


        }, "ahmad", "ahmad@gmail.com", String.valueOf(Role.USER));

    }

    @Test
    public void list_user_test() {
        startActor((server, client) -> {
            UserFixture.create(15);
            List<UserDto> users = Lists.newArrayList(parseBody(
                    client.get("/api/user/"),
                    UserDto[].class
            ));
            assertEquals(15 + 1, users.size());
        }, "ahmad", "ahmad@gmail.com", String.valueOf(Role.USER));
    }

    @Test
    public void used_email_test() {
        startActor((server, client) -> {
            UserDto userToCreate = new UserDto();
            userToCreate.setId("auth0|ahmad123s");
            userToCreate.setName("Ahmad Abbas");
            userToCreate.setEmail("ahmad@test.ps");
            var res = client.post("/api/user/", userToCreate);
            assertEquals(200, res.code());

            userToCreate.setId("newauth0");
            res = client.post("/api/user/", userToCreate);
            assertNotEquals(200, res.code());

            userToCreate.setId("auth0|ahmad123s");
            res = client.post("/api/user/", userToCreate);
            assertNotEquals(200, res.code());

        }, "ahmad", "ahmad@gmail.com", String.valueOf(Role.USER));
    }

    @Test
    public void update_user_test() {
        startActor((server, client) -> {
            UserDto userToCreate = new UserDto();
            userToCreate.setId("auth0|ahmad123s");
            userToCreate.setName("Ahmad Abbas");
            userToCreate.setEmail("ahmad@test.ps");
            var res = client.post("/api/user/", userToCreate);
            assertEquals(200, res.code());


            userToCreate.setName("Adam Abbas");
            userToCreate.setEmail("adam@test.ps");
            client.put(String.format("/api/user/%s", userToCreate.getId()), userToCreate);
            UserDto updatedUser = parseBody(client.get(String.format("/api/user/%s", userToCreate.getId())), UserDto.class);

            assertEquals("adam@test.ps", updatedUser.getEmail());
            assertEquals("Adam Abbas", updatedUser.getName());

            userToCreate = new UserDto();
            userToCreate.setId("auth0|adam");
            userToCreate.setName("Adam Abbas");
            userToCreate.setEmail("adam@test.ps");
            res = client.post("/api/user/", userToCreate);
            assertNotEquals(200, res.code());


            userToCreate = new UserDto();
            userToCreate.setId("auth0|ahmad");
            userToCreate.setName("Ahmad Abbas");
            userToCreate.setEmail("ahmad@test.ps");
            res = client.post("/api/user/", userToCreate);
            assertEquals(200, res.code());

        }, "ahmad", "ahmad@gmail.com", String.valueOf(Role.USER));
    }

    @Test
    public void get_user_tweets_test() {
        startActor((server, client) -> {
            UserDto userToCreate = new UserDto();
            userToCreate.setId("auth0|ahmad123s");
            userToCreate.setName("Ahmad Abbas");
            userToCreate.setEmail("ahmad@test.ps");
            var res = client.post("/api/user/", userToCreate);
            assertEquals(res.code(), 200);

            List<TweetDto> tweetDtos = TweetFixture.create(15, userToCreate.getId(), 0);
            List<TweetDto> userTweets = Lists.newArrayList(parseBody(
                    client.get(String.format("/api/user/%s/tweets", userToCreate.getId())),
                    TweetDto[].class
            ));
            boolean oneUSer = tweetDtos.stream()
                    .filter(tweetDto -> tweetDto.getUserId().equals("auth0|ahmad123s")).count() == 15;
            tweetDtos = tweetDtos.stream().filter(userTweets::contains).collect(Collectors.toList());

            assertEquals(15, userTweets.size());
            assert (tweetDtos.isEmpty());
            assert (oneUSer);
        }, "ahmad", "ahmad@gmail.com", String.valueOf(Role.USER));
    }

    @Test
    public void add_friend_test() {
        startActor((server, client) -> {

            UserDto firstUser = new UserDto();
            firstUser.setId("auth0|ahmad");
            firstUser.setName("Ahmad Abbas");
            firstUser.setEmail("ahmad@test.ps");

            UserDto secondUser = new UserDto();
            secondUser.setId("auth0|adam");
            secondUser.setName("Adam Abbas");
            secondUser.setEmail("Adam@test.ps");

            FriendsDto friendsDto = new FriendsDto(firstUser.getId(), secondUser.getId(), new Date());
            var res = client.post("/api/user/friends", friendsDto);
            assertNotEquals(200, res.code());

            client.post("/api/user", firstUser);
            res = client.post("/api/user/friends", friendsDto);
            assertNotEquals(200, res.code());

            client.post("/api/user", secondUser);
            res = client.post("/api/user/friends", friendsDto);
            assertEquals(200, res.code());

            res = client.get(String.format("/api/user/%s/friends", secondUser.getId()));
            List<UserDto> friends = Lists.newArrayList(parseBody(res, UserDto[].class));
            assertEquals(friends.get(0).getId(), firstUser.getId());

            List<TweetDto> firstUserTweets = TweetFixture.create(15, firstUser.getId(), 0);

            List<TweetDto> secondUserFriendsTweets = Lists.newArrayList(parseBody(
                    client.get(String.format("/api/user/%s/friends/tweets", firstUser.getId())),
                    TweetDto[].class
            ));

            firstUserTweets = firstUserTweets.stream()
                    .filter(secondUserFriendsTweets::contains)
                    .collect(Collectors.toList());
            assert (firstUserTweets.isEmpty());

            List<UserDto> searchedUser = Lists.newArrayList(parseBody(
                    client.get(String.format("/api/user/%s/search?name=adam", firstUser.getId())),
                    UserDto[].class
            ));
            assert (!searchedUser.isEmpty());
            assertEquals(searchedUser.get(0).getId(), secondUser.getId());
            res = client.delete("/api/user/friends", friendsDto);
            assertEquals(200, res.code());

            res = client.get(String.format("/api/user/%s/friends", secondUser.getId()));
            friends = Lists.newArrayList(parseBody(res, UserDto[].class));
            assert (friends.isEmpty());
        }, "ahmad", "ahmad@gmail.com", String.valueOf(Role.USER));
    }

}

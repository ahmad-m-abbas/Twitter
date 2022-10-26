package org.example.controller;

import io.javalin.http.Context;
import org.example.dao.query.SearchFriendQuery;
import org.example.dto.FriendsDto;
import org.example.dto.UserDto;
import org.example.service.UserService;

public class UserController {

    private static final UserService userService = UserService.instance();

    public static void listUsers(Context ctx) {
        ctx.json(userService.listUsers(ctx.queryParamAsClass("name", String.class).getOrDefault("")));
    }

    public static void getUserById(Context ctx) {
        UserDto userDto = userService.findUser(ctx.pathParam("userId"));
        if (userDto == null) {
            ctx.status(404);
        } else {
            ctx.json(userDto);
        }
    }

    public static void addUser(Context ctx) {
        try {
            UserDto user = ctx.bodyAsClass(UserDto.class);
            userService.addUser(user);
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void updateUser(Context ctx) {
        try {
            UserDto userDto = ctx.bodyAsClass(UserDto.class);
            String userId = ctx.pathParam("userId");
            userService.updateUser(userId, userDto);
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void getUserTweets(Context ctx) {
        ctx.json(userService.getUserTweets(ctx.pathParam("userId")));
    }


    public static void addFriends(Context ctx) {
        try {
            userService.addFriends(ctx.bodyAsClass(FriendsDto.class));
            ctx.status(200);
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void delete(Context ctx) {
        try {
            userService.delete(ctx.bodyAsClass(FriendsDto.class));
            ctx.status(200);
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void getFriendsTweets(Context ctx) {
        ctx.json(userService.getFriendsTweets(ctx.pathParam("userId")));
    }

    public static void getFriends(Context ctx) {
        ctx.json(userService.getFriends(ctx.pathParam("userId")));
    }

    public static void search(Context ctx) {
        SearchFriendQuery searchFriendQuery = new SearchFriendQuery(
                ctx.queryParamAsClass("name", String.class).getOrDefault(""),
                ctx.queryParamAsClass("orderBy", String.class).getOrDefault(null),
                ctx.queryParamAsClass("order", String.class).getOrDefault(null)
        );
        ctx.json(userService.search(ctx.pathParam("userId"), searchFriendQuery));
    }
    public static void getUsersLikedTweet(Context ctx) {
        ctx.json(userService.getUsersLikedTweet(ctx.pathParam("tweetId")));
    }
}

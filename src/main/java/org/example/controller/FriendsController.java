package org.example.controller;

import io.javalin.http.Context;
import org.example.dao.query.SearchFriendQuery;
import org.example.dto.FriendsDto;
import org.example.service.FriendsService;

public class FriendsController {
    private static final FriendsService friendsService = FriendsService.instance();

    public static void getFriendsTweets(Context ctx) {
        ctx.json(friendsService.getFriendsTweets(ctx.pathParam("userId")));
    }

    public static void getFriends(Context ctx) {
        ctx.json(friendsService.getFriends(ctx.pathParam("userId")));
    }

    public static void search(Context ctx) {
        SearchFriendQuery searchFriendQuery = new SearchFriendQuery(
                ctx.queryParamAsClass("name", String.class).getOrDefault(""),
                ctx.queryParamAsClass("orderBy", String.class).getOrDefault(null),
                ctx.queryParamAsClass("order", String.class).getOrDefault(null)
        );
        ctx.json(friendsService.search(ctx.pathParam("userId"), searchFriendQuery));
    }

    public static void add(Context ctx) {
        try {
            friendsService.addFriends(ctx.bodyAsClass(FriendsDto.class));
            ctx.status(200);
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void delete(Context ctx) {
        try {
            friendsService.delete(ctx.bodyAsClass(FriendsDto.class));
            ctx.status(200);
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }
}

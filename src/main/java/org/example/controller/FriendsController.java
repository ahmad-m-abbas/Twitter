package org.example.controller;

import io.javalin.http.Context;
import org.example.dto.FriendsDto;
import org.example.dto.LikesDto;
import org.example.service.FriendsService;
import org.example.service.LikesService;

public class FriendsController {
    private static final FriendsService friendsService =FriendsService.instance();

    public static void getFriendsTweets(Context ctx){
        ctx.json(friendsService.getFriendsTweets(ctx.pathParam("userId")));
    }
    public static void getFriends(Context ctx){
        System.out.println(ctx.pathParam("userId"));
        ctx.json(friendsService.getFriends(ctx.pathParam("userId")));
    }
    public static void add(Context ctx ){
        friendsService.addFriends(ctx.bodyAsClass(FriendsDto.class));
        ctx.status(200);
    }
}

package org.example.controller;

import io.javalin.http.Context;
import org.example.dto.LikesDto;
import org.example.service.LikesService;

public class LikesController {

    private static final LikesService likeService =LikesService.instance();

    public static void getUsers(Context ctx){
        ctx.json(likeService.getUsers(ctx.pathParam("tweetId")));
    }
    public static void getTweets(Context ctx){
        ctx.json(likeService.getTweets(ctx.pathParam("userId")));
    }
    public static void add(Context ctx ){
        likeService.add(ctx.bodyAsClass(LikesDto.class));
    }
    public static void unlike(Context ctx ){
        likeService.unlike(ctx.bodyAsClass(LikesDto.class));
    }
}

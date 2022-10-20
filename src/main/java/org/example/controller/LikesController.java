package org.example.controller;

import io.javalin.http.Context;
import org.example.dto.LikesDto;
import org.example.service.LikesService;

public class LikesController {

    private static final LikesService likeService = LikesService.instance();

    public static void getUsers(Context ctx) {
        ctx.json(likeService.getUsers(ctx.pathParam("tweetId")));
    }

    public static void getTweets(Context ctx) {
        ctx.json(likeService.getTweets(ctx.pathParam("userId")));
    }

    public static void add(Context ctx) {
        try {
            likeService.add(ctx.bodyAsClass(LikesDto.class));
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void unlike(Context ctx) {
        try {
            likeService.unlike(ctx.bodyAsClass(LikesDto.class));
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }
}

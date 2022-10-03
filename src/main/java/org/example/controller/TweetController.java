package org.example.controller;

import io.javalin.http.Context;
import org.example.dao.query.SearchTweetQuery;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.service.TweetService;
import org.example.service.UserService;

public class TweetController {

    private static final TweetService tweetService = TweetService.instance();


    public static void list(Context ctx){
        ctx.json(tweetService.list());
    }
    public static void getTweetById(Context ctx) {
        TweetDto tweetDto = tweetService.getTweet(ctx.pathParam("tweetId"));
        if (tweetDto == null) {
            ctx.status(404);
        } else {
            ctx.json(tweetDto);
        }
    }

    public static void addTweet(Context ctx) {
        TweetDto tweet = ctx.bodyAsClass(TweetDto.class);
        tweetService.insert(tweet);
    }

    public static void updateTweet(Context ctx) {
        TweetDto tweet = ctx.bodyAsClass(TweetDto.class);
        String tweetId = ctx.pathParam("tweetId");
        tweetService.update(tweetId, tweet);
    }

    public static void deleteTweet(Context ctx){
        tweetService.delete(ctx.pathParam("tweetId"));
    }
    public static void search(Context ctx){
        SearchTweetQuery query = new SearchTweetQuery(
                ctx.queryParamAsClass("toBeIn",String.class).getOrDefault(null),
                ctx.queryParamAsClass("orderBy",String.class).getOrDefault(null)
        );
        String name = ctx.pathParam("name");
        ctx.json(tweetService.search(name,query));
    }
}

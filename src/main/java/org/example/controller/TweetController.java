package org.example.controller;

import io.javalin.http.Context;
import org.example.dao.query.SearchTweetQuery;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.service.TweetService;
import org.example.service.UserService;

public class TweetController {

    private static final TweetService tweetService = TweetService.instance();


    public static void list(Context ctx) {
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
        try {
            TweetDto tweet = ctx.bodyAsClass(TweetDto.class);
            tweetService.insert(tweet);
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void updateTweet(Context ctx) {
        try {
            TweetDto tweet = ctx.bodyAsClass(TweetDto.class);
            tweetService.update(tweet);
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void deleteTweet(Context ctx) {
        try {
            tweetService.delete(ctx.pathParam("tweetId"));
        } catch (Exception e) {
            ctx.json(e).status(400);
        }
    }

    public static void search(Context ctx) {
        SearchTweetQuery query = new SearchTweetQuery(
                ctx.pathParam("name"),
                ctx.queryParamAsClass("toBeIn", String.class).getOrDefault(null),
                ctx.queryParamAsClass("orderBy", String.class).getOrDefault(null)
        );
        ctx.json(tweetService.search(query));
    }
}
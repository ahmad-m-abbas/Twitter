package org.example;

import io.javalin.apibuilder.EndpointGroup;
import org.example.controller.LikesController;
import org.example.controller.TweetController;
import org.example.controller.UserController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes implements EndpointGroup {
    @Override
    public void addEndpoints() {
        path("/",()->{
        });


        path("api/user", () -> {
            get("", UserController::listUsers);
            get("{userId}", UserController::getUserById);
            get("{userId}/like", LikesController::getTweets);
            post( UserController::addUser);
            post("{userId}/like", LikesController::add);
            put("{userId}", UserController::updateUser);
        });
        path("api/tweet", () -> {
            get("", TweetController::list);
            get("{tweetId}", TweetController::getTweetById);
            get("{tweetId}/like", LikesController::getUsers);
            post( TweetController::addTweet);
            post("/like", LikesController::add);
            put("{tweetId}", TweetController::updateTweet);
        });
    }
}

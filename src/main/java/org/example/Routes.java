package org.example;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.plugin.rendering.vue.VueComponent;
import org.example.controller.TweetController;
import org.example.controller.UserController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes implements EndpointGroup {
    @Override
    public void addEndpoints() {
        path("/", () -> {
            get(new VueComponent("home"));
            get("users", new VueComponent("users"));
            get("tweets", new VueComponent("tweets"));
        });

        path("users", () -> {
            get("/{userId}", new VueComponent("user-profile-layout"));
            get("/{userId}/info", new VueComponent("user-profile-layout"));
            get("/{userId}/friends", new VueComponent("user-profile-layout"));
            get("/{userId}/tweets", new VueComponent("user-profile-layout"));
        });

        path("api/user", () -> {
            get("", UserController::listUsers);
            get("{userId}", UserController::getUserById);
            get("{userId}/likes", TweetController::getTweetsLikedByUser);
            get("{userId}/tweets", UserController::getUserTweets);

            post(UserController::addUser);
            post("{userId}/like", TweetController::addLike);
            put("{userId}", UserController::updateUser);

            get("{userId}/friends/tweets", UserController::getFriendsTweets);
            get("{userId}/friends", UserController::getFriends);
            get("{userId}/search", UserController::search);
            post("/friends", UserController::addFriends);
            delete("/friends", UserController::delete);
        });
        path("api/tweet", () -> {
            get("/contains", TweetController::search);
            get("", TweetController::list);
            get("{tweetId}", TweetController::getTweetById);
            get("{tweetId}/like", UserController::getUsersLikedTweet);
            delete("/unlike", TweetController::unlike);
            post(TweetController::addTweet);
            post("/like", TweetController::addLike);
            put("{tweetId}", TweetController::updateTweet);
            delete("{tweetId}", TweetController::deleteTweet);
        });

    }
}

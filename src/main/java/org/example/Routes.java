package org.example;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.plugin.rendering.vue.VueComponent;
import org.example.controller.FriendsController;
import org.example.controller.LikesController;
import org.example.controller.TweetController;
import org.example.controller.UserController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes implements EndpointGroup {
    @Override
    public void addEndpoints() {
        path("/",()->{
            get(new VueComponent("home", null, new Include()));
            get("users",new VueComponent("users"));
            get("tweets",new VueComponent("tweets"));
        });

        path("users",()->{
            get("{userId}/profile",new VueComponent("user-profile-layout"));
            get("{userId}/info",new VueComponent("user-profile-layout"));
            get("{userId}/tweets",new VueComponent("user-profile-layout"));
            get("{userId}/friends",new VueComponent("user-profile-layout"));
            get("{userId}/likes",new VueComponent("user-profile-layout"));
        });
        path("tweets",()->{
            get("{tweetId}",new VueComponent("tweet-layout"));
            get("{tweetId}/likes",new VueComponent("tweet-layout"));
        });


        path("api/user", () -> {
            get("", UserController::listUsers);
            get("{userId}", UserController::getUserById);
            get("{userId}/likes", LikesController::getTweets);
            get("{userId}/tweets", UserController::getUserTweets);

            post( UserController::addUser);
            post("{userId}/like", LikesController::add);
            delete("{userId}/like", LikesController::unlike);
            put("{userId}", UserController::updateUser);
        });
        path("api/tweet", () -> {
            get("/search/{name}",TweetController::search);
            get("", TweetController::list);
            get("{tweetId}", TweetController::getTweetById);
            get("{tweetId}/like", LikesController::getUsers);
            post( TweetController::addTweet);
            post("/like", LikesController::add);
            put("{tweetId}", TweetController::updateTweet);
            patch("{tweetId}", TweetController::deleteTweet);
        });

        path("api/friends",()->{
            get("{userId}/tweets", FriendsController::getFriendsTweets);
            get("{userId}", FriendsController::getFriends);
            get("{userId}/search", FriendsController::search);
            post("", FriendsController::add);
            delete("",FriendsController::delete);

        });
    }
}

package org.example.controller;

import io.javalin.http.Context;
import org.example.dto.UserDto;
import org.example.service.UserService;

public class UserController {

    private static final UserService userService = UserService.instance();

    public static void listUsers(Context ctx) {
        ctx.json(userService.listUsers());
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
        UserDto user = ctx.bodyAsClass(UserDto.class);
        userService.addUser(user);
    }

    public static void updateUser(Context ctx) {
        UserDto userDto = ctx.bodyAsClass(UserDto.class);
        String userId = ctx.pathParam("userId");
        userService.updateUser(userId, userDto);
    }

    public static void getUserTweets(Context ctx){
        ctx.json(userService.getUserTweets(ctx.pathParam("userId")));
    }
}

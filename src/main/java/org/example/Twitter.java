package org.example;

import io.javalin.Javalin;

public class Twitter {
    public static void main(String[] args) {
        App app =  new TwitterApp();
        app.init(8081);
        System.out.println("A");
        app.start();

    }
}
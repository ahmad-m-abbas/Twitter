package org.example;

import org.example.infra.App;

public class Twitter {
    public static void main(String[] args) {
        App app = new TwitterApp();
        app.init(8081);
        app.start();
    }
}
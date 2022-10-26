package org.example.infra;

import io.javalin.Javalin;

public interface App {
    public default void init() {
        init(8080);
    }

    public void init(int port);

    public void start();

    public Javalin getJavalin();
}

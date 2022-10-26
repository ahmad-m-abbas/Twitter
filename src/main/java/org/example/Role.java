package org.example;

import io.javalin.core.security.RouteRole;

public enum Role implements RouteRole {
    USER;

    @Override
    public String toString() {
        return super.toString();
    }
}

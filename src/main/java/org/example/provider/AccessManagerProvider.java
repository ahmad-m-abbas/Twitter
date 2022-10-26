package org.example;

import io.javalin.core.security.AccessManager;

public class AccessManagerProvider {
    private static AccessManagerProvider instance = instance();
    private AccessManager accessManager = null;

    public static AccessManagerProvider instance() {
        if (instance == null) {
            instance = new AccessManagerProvider();
        }
        return instance;
    }

    public AccessManager accessManager() {
        return accessManager;
    }

    public void accessManager(AccessManager accessManager) {
        this.accessManager = accessManager;
    }

}
package com.example.hellokeb_bi.pattern.singleton;

public class Authorization {
    private static Authorization authorization = null;
    private String token = "";

    private Authorization() {
    }

    public static Authorization getInstance() {
        if (authorization == null) {
            synchronized (Authorization.class) {
                if (authorization == null) {
                    authorization = new Authorization();
                }
            }
        }
        return authorization;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package com.example.firebasetest;

public class UserManager {
    private static UserManager instance;
    private String userUid;

    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUserUid(String uid) {
        userUid = uid;
    }
    public String getUserUid() {
        return userUid;
    }
    public void clearUserUid() {
        userUid = null;
    }
}

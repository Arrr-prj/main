package com.example.firebasetest;

public class UserManager {
    private static UserManager instance;
    private String userUid;
    private String userEmail;

    private UserManager() {
    }
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }
    public void clearUserEmail(){userEmail = null;}

    public void setUserUid(String uid) {
        userUid = uid;
    }
    public String getUserUid() {
        return userUid;
    }
    public void clearUserUid() {userUid = null;}
}

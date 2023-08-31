package com.example.firebasetest;

public class UserManager {
    private static UserManager instance;
    private String userUid;
    private String userEmail;
    private String userNickName;
    private Integer cancleCnt;

    private UserManager() {
    }
    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public Integer getCancleCnt() {
        return cancleCnt;
    }

    public void setCancleCnt(Integer cancleCnt) {
        this.cancleCnt = cancleCnt;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String nickname) {
        this.userNickName = userNickName;
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
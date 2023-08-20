package com.example.firebasetest;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UserDataHolderAnnouncements {
    public static ArrayList<Announcement> announcements = new ArrayList<>();
    public static void loadAnnouncement(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Announcement")
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String content = document.getString("content");
                            String uploadMillis = document.getString("uploadMillis");
                            String differenceMillis = document.getString("differenceMillis");
                            // Item 생성자에 맞게 데이터 추가
                            Announcement write = new Announcement(title, content, uploadMillis, differenceMillis);
                            announcements.add(write);
                        }
                    } else {
                        // 실패했을 때
                    }
                });
    }
}

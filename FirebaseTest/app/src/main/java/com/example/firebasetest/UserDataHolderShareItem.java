package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UserDataHolderShareItem {
    public static ArrayList<Item> shareItemList = new ArrayList<>();
    public static void loadShareItems(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ShareItem")
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String id = document.getString("id");
                            String info = document.getString("info");
                            String category = document.getString("category");
                            String seller = document.getString("seller");
                            String imgUrl = document.getString("imgUrl");
                            String futureDate = document.getString("futureDate");
                            String futureMillis = document.getString("futureMillis");

                            // Item 생성자에 맞게 데이터 추가
                            Item item = new Item(title, imgUrl, id, category, info, seller, futureDate, futureMillis);
                            shareItemList.add(item);
                        }
                    } else {
                        // 실패했을 때
                    }
                });
    }
}
package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class UserDataHolderEventItems {
    public static ArrayList<Item> eventItemList = new ArrayList<>();
    public static void loadEventItems(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("EventItem")
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String id = document.getString("id");
                            String info = document.getString("info");
                            String category = document.getString("category");
                            String seller = document.getString("seller");
                            String buyer = document.getString("buyer");
                            String imgUrl = document.getString("imgUrl");
                            String price = document.getString("price");
                            String endPrice = document.getString("endPrice");
                            String futureMillis = document.getString("futureMillis");
                            String futureDate = document.getString("futureDate");
                            Boolean confirm = document.getBoolean("confirm");
                            String itemType = document.getString("itemType");
                            Integer views = Objects.requireNonNull(document.getLong("views")).intValue();
                            String differenceDays = document.getString("differenceDays");
                            String uploadMillis = document.getString("uploadMillis");
                            // Item 생성자에 맞게 데이터 추가
                            Item item = new Item(title, imgUrl, id, price, endPrice, category, info, seller, buyer, futureMillis, futureDate, uploadMillis, differenceDays, confirm, itemType, views);
                            eventItemList.add(item);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}

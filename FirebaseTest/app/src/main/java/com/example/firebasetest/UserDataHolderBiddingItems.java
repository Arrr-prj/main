package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UserDataHolderBiddingItems {
    public static ArrayList<Item> biddingItemList = new ArrayList<>();
    public static void loadBiddingItems(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("BiddingItem")
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
                            String price = document.getString("price");
                            String futureMillis = document.getString("futureMillis");
                            String futureDate = document.getString("futureDate");

                            // Item 생성자에 맞게 데이터 추가
                            Item item = new Item(title, imgUrl, id, price, category, info, seller,futureMillis, futureDate);
                            biddingItemList.add(item);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}

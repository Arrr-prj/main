package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class UserDataHolderOpenItems {
    public static ArrayList<Item> openItemList = new ArrayList<>();
    public static void loadOpenItems(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OpenItem")
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
                            String imgUrl1 = document.getString("imgUrl1");
                            String imgUrl2 = document.getString("imgUrl2");
                            String imgUrl3 = document.getString("imgUrl3");
                            String imgUrl4 = document.getString("imgUrl4");
                            String imgUrl5 = document.getString("imgUrl5");
                            String imgUrl6 = document.getString("imgUrl6");
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
                            Item item = new Item(title,  imgUrl1, imgUrl2, imgUrl3, imgUrl4, imgUrl5, imgUrl6, id, price, endPrice, category, info, seller, buyer, futureMillis, futureDate, uploadMillis, differenceDays, confirm, itemType, views);
                            openItemList.add(item);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}

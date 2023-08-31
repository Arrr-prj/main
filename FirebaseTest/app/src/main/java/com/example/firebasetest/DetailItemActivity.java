package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailItemActivity extends AppCompatActivity {

    private Button btnEdit, btnDelete;
    private TextView itemId, startPrice, endPrice, itemInfo, seller, category;

    private ImageView imgUrl;

    BiddingItem item;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        // 아이템 정보들
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        imgUrl = findViewById(R.id.imgUrl);
        category = findViewById(R.id.category);


        // 수정 or 삭제
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);

        getSelectbItem();
        getSelectoItem();

        // 삭제 버튼 클릭 시 아이템 삭제 작업 추가
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "delete에 접근했습니다.");
                // docRef.getID() 하면 문서 ID 가져올 수 있음
                // Firestore에서 해당 아이템 삭제 작업
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                DocumentReference docRef = firestore.collection("BiddingItem").document();
                CollectionReference collectionReference = firestore.collection("BiddingItem");

                Log.d(TAG, ""+docRef.getId());
                for(BiddingItem item: BiddingActivity.biddingItemList){
                    if(item.getId().equals(id)){
                        Log.d(TAG, ""+item);
                        docRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // 삭제 성공 시 실행되는 코드
                                        Log.d(TAG, "Document successfully deleted!");

                                        // 아이템 삭제 후 이전 화면으로 돌아가기 등의 작업 수행
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // 삭제 실패 시 실행되는 코드
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });

                        break;
                    }

                }
                CollectionReference collectionoReference = firestore.collection("OpenItem");
                for(Item item: OpenAuctionActivity.openItemList) {
                    if (item.getId().equals(id)) {
                        Log.d(TAG, "" + item);

                        // 해당 문서 ID를 가져와서 삭제
                        String documentId = item.getId();
                        docRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // 삭제 성공 시 실행되는 코드
                                        Log.d(TAG, "Document successfully deleted!");

                                        // 아이템 삭제 후 이전 화면으로 돌아가기 등의 작업 수행
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // 삭제 실패 시 실행되는 코드
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });

                        break;
                    }

                }

                // DocumentReference를 이용하여 해당 문서 삭제

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    private  void setbValues(BiddingItem selectedItem){
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText("0");
        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        Glide.with(this)
                .load(selectedItem.getImageUrl())
                .into(imgUrl);
    }
    private  void setoValues(Item selectedItem){
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText(String.valueOf(selectedItem.getPrice()));
        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        Glide.with(this)
                .load(selectedItem.getImageUrl())
                .into(imgUrl);
    }

    // bidding Item 클릭 시 이벤트
    private void getSelectbItem(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        BiddingItem selectedItem = null;
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        for(BiddingItem item: BiddingActivity.biddingItemList){
            if(item.getId().equals(id)){
                selectedItem = item;
                break;
            }
        }
        if(selectedItem != null){
            // selectedItem 사용하기
            setbValues(selectedItem);
        }
        else{
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }
    private void getSelectoItem(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        final Item[] selectedItem = new Item[1];
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        for(Item item: OpenAuctionActivity.openItemList){
            if(item.getId().equals(id)){
                selectedItem[0] = item;
                break;
            }
        }
        if(selectedItem[0] != null){
            setoValues(selectedItem[0]);
            // 삭제 버튼 클릭 시 아이템 삭제 작업 추가
            Button deleteButton = findViewById(R.id.btn_delete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "delete에 접근했습니다.");
                    // Firestore에서 해당 아이템 삭제 작업
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    String collectionName = "OpenItem"; // 컬렉션 이름
                    String itemId = selectedItem[0].getId(); // 아이템 문서 ID

                        // DocumentReference를 이용하여 해당 문서 삭제
                        DocumentReference docRef = firestore.collection(collectionName).document(itemId);
                        docRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // 삭제 성공 시 실행되는 코드
                                        Log.d(TAG, "Document successfully deleted!");

                                        // 아이템 삭제 후 이전 화면으로 돌아가기 등의 작업 수행
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // 삭제 실패 시 실행되는 코드
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                });
            }
        else{
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }


}
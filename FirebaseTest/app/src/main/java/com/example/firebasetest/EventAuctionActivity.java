package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EventAuctionActivity extends AppCompatActivity {
    private Button btnRegistItem;
    ListView listView;
    private Button btnbck;
    private FirebaseFirestore db;
    public static ArrayList<Item> EventAuctionItemList = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_auction);

        listView = (ListView)findViewById(R.id.listView);

        btnbck = findViewById(R.id.btn_back);

        this.InitializeOpenItem();

        btnRegistItem = findViewById(R.id.btn_registItem);


        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
        db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("User").document(uid);

        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Boolean adminValue = documentSnapshot.getBoolean("admin");
                        if (adminValue != null && adminValue) { // admin 계정일 때
                            btnRegistItem.setVisibility(View.VISIBLE);// 버튼 활성화
                            btnRegistItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {// 이벤트 경매 등록
                                    Intent intent = new Intent(EventAuctionActivity.this, EventAuctionRegisterItemActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else { // admin 계정이 아닐 때
                            btnRegistItem.setVisibility(View.GONE); // 버튼 비활성화
                        }
                    }
                });

        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventAuctionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        // DetailPage
        setUpOnClickListener();
    }
    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Item item = (Item) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), EventAuctionDetailItemActivity.class);
                showDetail.putExtra("id", item.getId());
                showDetail.putExtra("title", item.getTitle());
                showDetail.putExtra("seller", item.getSeller());
                startActivity(showDetail);
            }
        });
    }
    public void InitializeOpenItem(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // 기존 아이템 리스트 비워주기
        EventAuctionItemList.clear();
        db.collection("EventItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            EventAuctionItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            String.valueOf(document.getData().get("imgUrl")),
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("price")),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")),
                                            String.valueOf(document.getData().get("seller")),
                                            String.valueOf(document.getData().get("futureMillis")),
                                            String.valueOf(document.getData().get("futureDate"))
                                    )
                            );
                        }
                        EventItemAdapter eventItemAdapter = new EventItemAdapter(this, EventAuctionItemList);
                        listView.setAdapter(eventItemAdapter);
                    }
                });
    }
}
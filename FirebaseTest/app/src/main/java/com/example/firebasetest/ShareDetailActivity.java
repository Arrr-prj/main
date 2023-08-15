package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ShareDetailActivity extends AppCompatActivity {
    private TextView itemId, startPrice, endPrice, itemInfo, seller, category;
    private ImageView imgUrl;
    private EditText mETBidPrice; // 입찰가
    private Button mBtnBidButton; // 입찰하기 버튼
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);
        // 아이템 정보들
        itemId = findViewById(R.id.itemId);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        imgUrl = findViewById(R.id.imgUrl);
        category = findViewById(R.id.category);
        database = FirebaseFirestore.getInstance();

        // 입력받은 입찰 가격
        mBtnBidButton = findViewById(R.id.btn_apply); // 입찰하기 버튼

        getSelectoItem();

        // 신청하기를 눌렀을 때
        mBtnBidButton.setOnClickListener(new View.OnClickListener() {

            Intent intent = getIntent();
            String title = intent.getStringExtra("title");
            String seller = intent.getStringExtra("seller");
            String documentId = title+ seller;
            private String document;

            @Override
            public void onClick(View view) {
                String uid = UserManager.getInstance().getUserUid();
                DocumentReference userDocRef = database.collection("User").document(uid); // 유저의 이름 받아오기
                Intent intent = getIntent();
                String strTitle = intent.getStringExtra("title");

                DocumentReference auctionDocRef = database.collection("ShareInProgress").document(documentId); // 공개 경매 중인 것들 참조하기


                // 이미 해당 사용자의 입찰 정보가 있는지 확인
                auctionDocRef.collection("Share").document(uid) // 경매중인 것들을 Bids라는 컬렉션을 새로 만들어서 유저들을 넣어주기
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(ShareDetailActivity.this, "이미 나눔에 참여하셨습니다.", Toast.LENGTH_SHORT).show();
                                }
                                // 해당 사용자의 입찰 정보가 없을 때
                                else {
                                    // 경매 문서의 하위 컬렉션에 입찰 정보 저장
                                    Map<String, Object> bidData = new HashMap<>();
                                    bidData.put("입찰자 아이디", userDocRef.getId());
                                    bidData.put("나눔 정보", documentId);

                                    auctionDocRef.collection("Share").document(UserManager.getInstance().getUserUid())
                                            .set(bidData)
                                            .addOnSuccessListener(aVoid -> {
                                                // 성공적으로 데이터가 저장된 후 수행할 작업을 여기에 추가
                                                auctionDocRef.collection("Shares");
                                            });
                                }
                            }
                        });
            }
        });
    }
    private void setValues(Item selectedItem) {
        database.collection("ShareItemInProgress")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    itemId.setText(selectedItem.getId());
                    itemInfo.setText(selectedItem.getInfo());
                    category.setText(selectedItem.getCategory());
                    seller.setText(selectedItem.getSeller());
                    Glide.with(this)
                            .load(selectedItem.getImageUrl())
                            .into(imgUrl);
                });
    }
    private void getSelectoItem() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Item selectedItem = null;
        for (Item item : UserDataHolderShareItem.shareItemList) {
            if(item.getId() != null){
                if (item.getId().equals(id)) {

                    selectedItem = item;
                    break;
                }
            }
        }

        if (selectedItem != null) {
            // selectedItem 사용하기
            setValues(selectedItem);
        } else {
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }
}
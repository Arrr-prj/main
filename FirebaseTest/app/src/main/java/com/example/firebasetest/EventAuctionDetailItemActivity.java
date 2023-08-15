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

public class EventAuctionDetailItemActivity extends AppCompatActivity {
    private TextView itemId, startPrice, endPrice, itemInfo, seller, category;
    private ImageView imgUrl;
    private EditText mETBidPrice; // 입찰가
    private Button mBtnBidButton; // 입찰하기 버튼
    String document;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_auction_detail_item);
        // 아이템 정보들
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        imgUrl = findViewById(R.id.imgUrl);
        category = findViewById(R.id.category);
        database = FirebaseFirestore.getInstance();
        // 입력받은 입찰 가격
        mBtnBidButton = findViewById(R.id.btn_bidbutton); // 입찰하기 버튼

        /*
        30초가 지났을 때 경매 종료 메서드
         */

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Toast.makeText(EventAuctionDetailItemActivity.this, id, Toast.LENGTH_SHORT).show();
        String title = intent.getStringExtra("title");
        String seller = intent.getStringExtra("seller");

        String documentId = title + seller;
        Toast.makeText(EventAuctionDetailItemActivity.this, documentId, Toast.LENGTH_SHORT).show();
        this.document = documentId;

        getSelectoItem(); // 최고가를 업데이트하도록 수정한 부분

        mBtnBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = UserManager.getInstance().getUserUid();
                mETBidPrice = findViewById(R.id.et_bid); // 경매 가격 받기
                String bidPrice = mETBidPrice.getText().toString(); // 경매 가격 초기화하기
                DocumentReference userDocRef = database.collection("EventItem").document(uid); // 유저의 이름 받아오기

                DocumentReference auctionDocRef = database.collection("EventAuctionInProgress").document(documentId); // 공개 경매 중인 것들 참조하기

                // 이미 해당 사용자의 입찰 정보가 있는지 확인
                auctionDocRef.collection("Bids").document(uid) // 경매중인 것들을 Bids라는 컬렉션을 새로 만들어서 유저들을 넣어주기
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                if (document.exists()) {
                                    Map<String, Object> existingBidData = document.getData();
                                    String existingBidPriceStr = (String) existingBidData.get("입찰 가격"); // 입력된 입찰 가격을 초기화시켜줌
                                    if (existingBidPriceStr != null) {
                                        try {
                                            int existingBidPrice = Integer.parseInt(existingBidPriceStr); // 입력된 입찰 가격을 int 형태로 바꿔줌
                                            int newBidPrice = Integer.parseInt(bidPrice); // 입력받은 입찰가격을 초기화해줌
                                            if (newBidPrice > existingBidPrice) { // 입력받은 가격이 이 전 가격보다 더 작으면
                                                // 새로운 입찰 정보 업데이트
                                                Map<String, Object> bidData = new HashMap<>();
                                                bidData.put("입찰자 아이디", userDocRef.getId());
                                                bidData.put("입찰 가격", bidPrice);
                                                bidData.put("경매 정보", documentId);

                                                auctionDocRef.collection("Bids").document(uid)
                                                        .set(bidData)
                                                        .addOnSuccessListener(aVoid -> {
                                                            updateHighestBidListener(auctionDocRef.collection("Bids"));
                                                        });
                                            }
                                            else {// 현재 입찰가보다 작은 가격이 입력되었을 때
                                                Toast.makeText(EventAuctionDetailItemActivity.this, "전 입찰가보다 높은 가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                // 또는 전 입찰가보다 낮은 가격으로 입찰을 하고싶은지 물어보고 등록해주기
                                            }
                                        } catch (NumberFormatException e) {// 숫자로 변환할 수 없는 경우
                                            Toast.makeText(EventAuctionDetailItemActivity.this, "가격을 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                // 해당 사용자의 입찰 정보가 없을 때
                                else {
                                    // 경매 문서의 하위 컬렉션에 입찰 정보 저장
                                    Map<String, Object> bidData = new HashMap<>();
                                    bidData.put("입찰자 아이디", userDocRef.getId());
                                    bidData.put("입찰 가격", bidPrice);
                                    bidData.put("경매 정보", documentId);

                                    auctionDocRef.collection("Bids").document(uid)
                                            .set(bidData)
                                            .addOnSuccessListener(aVoid -> {
                                                updateHighestBidListener(auctionDocRef.collection("Bids"));
                                            });
                                }
                            }
                        });
            }
        });
    }

    private void setoValues(Item selectedItem) {
        database.collection("EventAuctionInProgress")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    // 가장 큰 입찰가 찾기
                    int highestBid = 0;
                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot doc : value) {
                            Map<String, Object> bidData = doc.getData();
                            String bidPriceStr = (String) bidData.get("입찰 가격");
                            if (bidPriceStr != null) {
                                try {
                                    int bidPrice = Integer.parseInt(bidPriceStr);
                                    highestBid = Math.max(highestBid, bidPrice);
                                } catch (NumberFormatException e) {
                                    // 경매에 참가한 사람이 한명도 없을 때 캐치해줌
                                    highestBid = 0;
                                }
                            }
                        }
                    }
                    itemId.setText(selectedItem.getId());
                    itemInfo.setText(selectedItem.getInfo());
                    category.setText(selectedItem.getCategory());
                    startPrice.setText(String.valueOf(selectedItem.getPrice()));
                    endPrice.setText(String.valueOf(highestBid));
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
        for (Item item : EventAuctionActivity.EventAuctionItemList) {
            if (item.getId().equals(id)) {
                selectedItem = item;
                break;
            }
        }
        if (selectedItem != null) {
            // selectedItem 사용하기
            setoValues(selectedItem);
        } else {
            // 해당 id와 일치하는 아이템이 없는 경우
        }

        // 최고가 업데이트를 위해 호출
        if (selectedItem != null) {
            updateHighestBidListener(database.collection("EventAuctionInProgress")
                    .document(document)
                    .collection("Bids"));
        }
    }

    // 공개 경매 컬랙션에서 가장 큰 입찰가 찾고 업로드
    private void updateHighestBidListener(CollectionReference bidsCollectionRef) {
        bidsCollectionRef
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    // 가장 큰 입찰가 찾기
                    int highestBid = 0;
                    boolean hasBids = false; // 입찰자가 있는지
                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot doc : value) {
                            Map<String, Object> bidData = doc.getData();
                            String bidPriceStr = (String) bidData.get("입찰 가격");
                            if (bidPriceStr != null) {
                                try {
                                    int bidPrice = Integer.parseInt(bidPriceStr);
                                    highestBid = Math.max(highestBid, bidPrice);
                                    hasBids = true;
                                } catch (NumberFormatException e) {
                                    // 유효하지 않은 입찰 가격 캐치
                                }
                            }
                        }
                    }
                    if (hasBids) {
                        endPrice.setText(String.valueOf(highestBid));
                    } else {
                        endPrice.setText("경매에 참여한 사람이 없습니다.");
                    }
                });
    }
}
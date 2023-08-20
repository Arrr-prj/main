package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailBiddingItemActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private long remainingTimeMillis; // 전역 변수로 추가
    private Button btnEdit, btnDelete;
    private Button btnBid;
    private EditText bidText;
    private TextView itmeTitle, itemId, startPrice, endPrice, itemInfo, seller, category, timeinfo, futureMillis;
    private ImageView imgUrl;
    private LinearLayout bidPopupLayout;
    private FirebaseFirestore db;
    private String sellerName;

    Item item;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bidding_item);

        // 아이템 정보들
        itmeTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
//        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        imgUrl = findViewById(R.id.imgUrl);
        category = findViewById(R.id.category);
        timeinfo = findViewById(R.id.endTime);
        futureMillis = findViewById(R.id.futureMillis);

        //입찰하기 버튼
        btnBid = findViewById(R.id.biddng_bid);
        bidPopupLayout = findViewById(R.id.bidPopupLayout);
        bidText = findViewById(R.id.bidAmountEditText);

        db = FirebaseFirestore.getInstance();

        Toast.makeText(DetailBiddingItemActivity.this, String.valueOf(UserDataHolderBiddingItems.biddingItemList.size()), Toast.LENGTH_SHORT).show();

        getSelectbItem();

        // 입찰하기 버튼을 눌렀을 때 -> dialog 로 입력받을 수 있는 팝업창 띄움
        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidPopupLayout.setVisibility(View.VISIBLE);
            }
        });
        Button confirmBidButton = findViewById(R.id.confirmBidButton);
        Button cancelBidButton = findViewById(R.id.cancelBidButton);

        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidPopupLayout.setVisibility(View.VISIBLE);
            }
        });

        confirmBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = getIntent().getStringExtra("id");
                String bidAmount = bidText.getText().toString();
                if (!bidAmount.isEmpty()) {
                    // 컬렉션만들고 로직만들자리
                    long userBidAmount = Long.parseLong(bidAmount);
                    long currentHighestBid = Long.parseLong(startPrice.getText().toString());

                    if (userBidAmount > currentHighestBid) {
                        // 비딩금액이 컬렉션에 저장된금액보다 크면 데이터베이스 업데이트
                        updateBidData(userBidAmount);
                        // 경매타이머 설정후에 performAuctionEnd()메소드로 낙찰기능
                        //performAuctionEnd(itemId);
                    }
                    else {
                        Toast.makeText(DetailBiddingItemActivity.this,
                                "입찰이 성공적으로 처리되었습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                bidPopupLayout.setVisibility(View.GONE);
            }
        });

        cancelBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidPopupLayout.setVisibility(View.GONE);
            }
        });
    }

    private void updateEndTime(String documentId) {
        // 현재 시간을 기준으로 종료 시간 업데이트
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1); // 1분 후 종료 시간으로 설정

        String futureMillis = String.valueOf(calendar.getTimeInMillis());

        // "yyyy-MM-dd HH:mm:ss" 포맷으로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(calendar.getTime());

        Map<String, Object> data = new HashMap<>();
        data.put("futureMillis", futureMillis);
        data.put("futureDate", formattedDate);

        DocumentReference userDocRef = db.collection("OpenItem").document(documentId);
        userDocRef.update(data)
                .addOnSuccessListener(aVoid -> {
                    // 등록된 리스트 새로 갱신
                    UserDataHolderOpenItems.loadOpenItems();
                    Toast.makeText(DetailBiddingItemActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "종료 시간 업데이트에 실패했습니다." + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void setbValues(Item selectedItem) {
        itmeTitle.setText(selectedItem.getTitle());
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText("0");
//        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        timeinfo.setText(selectedItem.getFutureDate());

        long currentTimeMillis = System.currentTimeMillis();
        String aa = selectedItem.getFutureMillis();
        Long futureMillisValue = Long.valueOf(aa); // 형변환
        remainingTimeMillis = futureMillisValue - currentTimeMillis;
        String remainingTime = formatRemainingTime(remainingTimeMillis);
        if (remainingTimeMillis <= 0) {
            bidend();
        }
        futureMillis.setText(remainingTime);
        Glide.with(this)
                .load(selectedItem.getImageUrl())
                .into(imgUrl);
        startCountdownTimer(remainingTimeMillis);
    }
    private void bidend(){
        btnBid.setEnabled(false);
    }
    private String formatRemainingTime(long remainingTimeMillis) {
        if (remainingTimeMillis <= 0) {
            return "경매가 종료되었습니다.";
        }

        long diffSeconds = remainingTimeMillis / 1000;
        long diffMinutes = diffSeconds / 60;
        long diffHours = diffMinutes / 60;
        long diffDays = diffHours / 24;

        diffHours = diffHours % 24;
        diffMinutes = diffMinutes % 60;
        diffSeconds = diffSeconds % 60;

        return String.format("남은 시간: %d일 %02d시간 %02d분 %02d초", diffDays, diffHours, diffMinutes, diffSeconds);
    }

    private void startCountdownTimer(long futureTimeMillis) {
        CountDownTimer timer = new CountDownTimer(futureTimeMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis = millisUntilFinished;
                updateCountdownText();

            }

            @Override
            public void onFinish() {
                // 타이머가 종료될 때의 동작을 여기에 추가하면 됩니다.
                // 예를 들어, 경매 종료 처리 등을 수행할 수 있습니다.
            }
        };

        timer.start();
    }
    private void updateCountdownText() {
        String remainingTime = formatRemainingTime(remainingTimeMillis);
        futureMillis.setText(remainingTime);
    }

    // bidding Item 클릭 시 이벤트
    private void getSelectbItem() {
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
//        Intent intent = getIntent();
//        String title = intent.getStringExtra("title");
//        String seller = intent.getStringExtra("seller");
//        String documentId = title+ seller;
//        Log.d(TAG, ""+documentId);
        Item selectedItem = null;
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        for (Item item : UserDataHolderBiddingItems.biddingItemList) {
            if (documentId.equals(item.getTitle() + item.getSeller())) {
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
    private void performAuctionEnd(String auctionItemId) {
        String itemId = getIntent().getStringExtra("id");
        String seller = sellerName;

        // 해당 물품의 Bids 컬렉션에 저장된 입찰 정보 가져오기
        db.collection("BiddingAuctionItems").document(auctionItemId)
                .collection("Bids")
                .orderBy("비딩금액", Query.Direction.DESCENDING) // 내림차순으로 입찰금액 정렬
                .limit(1) // 가장높은 금액 입찰자 1명 가져오기
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot highestBidDoc = task.getResult().getDocuments().get(0);
                        String winningUserId = highestBidDoc.getId();
                        long winningBidAmount = highestBidDoc.getLong("비딩금액");

                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("winningUser", winningUserId);
                        updateData.put("winningBid", winningBidAmount);

                        db.collection("BiddingItem").document(itemId+seller)
                                .update(updateData)
                                .addOnSuccessListener(aVoid -> {
                                    // 업데이트 성공 시의 처리
                                    Toast.makeText(DetailBiddingItemActivity.this,
                                            itemId+" 경매가 종료되었습니다.\n낙찰자: " + winningUserId + "\n입찰금액: " + winningBidAmount,
                                            Toast.LENGTH_LONG).show();
                                    // remainingTimeMillis 값이 음수인 경우 입찰하기 버튼 비활성화
                                    if (remainingTimeMillis <= 0) {
                                        btnBid.setEnabled(false);
                                        btnBid.setText("경매 종료");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // 업데이트 실패 시의 처리
                                    Toast.makeText(DetailBiddingItemActivity.this,
                                            "낙찰자를 결정하는 중에 오류가 발생했습니다.",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, itemId+seller);
                                });
                    } else {
                        Toast.makeText(DetailBiddingItemActivity.this,
                                "낙찰자를 결정하는 중에 오류가 발생했습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateBidData(double newBidAmount) {
        String userId = UserManager.getInstance().getUserUid();

        String documentId = getIntent().getStringExtra("documentId"); // 현재 아이템의 ID 가져오기

        // BiddingAuctionProgress 컬렉션 안에 itemId로 문서 생성 또는 업데이트
        Map<String, Object> bidData = new HashMap<>();
        bidData.put("비딩금액", newBidAmount);
        bidData.put("사용자아이디", userId);

        db.collection("BiddingAuctionItems").document(documentId)

                .collection("Bids")
                .document(userId) // 사용자 ID로 문서 생성
                .set(bidData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DetailBiddingItemActivity.this,
                            "입찰이 성공적으로 처리되었습니다.",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DetailBiddingItemActivity.this,
                            "입찰을 처리하는 중에 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT).show();
                });
    }
}
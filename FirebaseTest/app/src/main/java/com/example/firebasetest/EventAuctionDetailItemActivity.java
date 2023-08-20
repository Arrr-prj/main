package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EventAuctionDetailItemActivity extends AppCompatActivity {
    private long remainingTimeMillis;
    private CountDownTimer countDownTimer;
    private TextView itmeTitle, itemId, startPrice, endPrice, itemInfo, seller, category, timeinfo, futureMillis;
    private ImageView imgUrl1, imgUrl2, imgUrl3, imgUrl4, imgUrl5, imgUrl6;
    private EditText mETBidPrice; // 입찰가
    private Button mBtnBidButton, mBtnBuy, mBtnConfirm, mBtnBidEnd, mBtnBigButton; // 입찰하기 버튼

    private String sellerName ,bidAmount;
    private ViewPager2 sliderViewPager;
    private PhotoView photoViewSlider;
    private LinearLayout layoutIndicator;
    private String[] images = new String[6];
    String buyer, document;

    boolean membershipValue;
    FirebaseFirestore database;
    String imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String userEmail; // 현재 유저의 이메일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_auction_detail_item);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        sliderViewPager.setOffscreenPageLimit(1);

        // 아이템 정보들
        itmeTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);

        category = findViewById(R.id.category);
        timeinfo = findViewById(R.id.timeInfoinfo);
        futureMillis = findViewById(R.id.futureMillis);
        mBtnBidButton = findViewById(R.id.btn_bidbutton); // 입찰하기 버튼
        mBtnBidEnd = findViewById(R.id.btn_bidEnd);       // 경매 종료
        mBtnBuy = findViewById(R.id.btn_buy);            // 구매하기
        mBtnConfirm = findViewById(R.id.btn_buyConfirm); // 구매 완료
        mETBidPrice = findViewById(R.id.et_bid); // 경매 가격 받기
        mBtnBigButton = findViewById(R.id.btn_bigbutton);
        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
        database = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = database.collection("User").document(uid);

        database = FirebaseFirestore.getInstance();

        buyer = " ";

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String seller = intent.getStringExtra("seller");
        String buyerUid = intent.getStringExtra("buyer");
        // 두번째 매개변수는 값을 잘 불러오지 못했을때 할당해줄 기본 값
        String confirm = intent.getStringExtra("confirm");
        String futureMillis = intent.getStringExtra("futureMillis");
        Toast.makeText(this, "buyer"+buyerUid, Toast.LENGTH_SHORT).show();

        String documentId = title + seller;
        this.document = documentId;
        if (user != null) {
            userEmail = user.getEmail(); // 현재 사용자의 이메일
            UserManager.getInstance().setUserEmail(userEmail); // UserManager에 이메일 저장
        }
        getSelectoItem(); // 최고가를 업데이트하도록 수정한 부분
// 이미지 URL을 한국 관련 이미지 URL로 대체하거나 이미지 URL이 있는 경우에만 추가
        if (imageUrl1 != null && !imageUrl1.isEmpty()) {
            images[0] = imageUrl1;
        }
        if (imageUrl2 != null && !imageUrl2.isEmpty()) {
            images[1] = imageUrl2;
        }
        if (imageUrl3 != null && !imageUrl3.isEmpty()) {
            images[2] = imageUrl3;
        }
        if (imageUrl4 != null && !imageUrl4.isEmpty()) {
            images[3] = imageUrl4;
        }
        if (imageUrl5 != null && !imageUrl5.isEmpty()) {
            images[4] = imageUrl5;
        }
        if (imageUrl6 != null && !imageUrl6.isEmpty()) {
            images[5] = imageUrl6;
        }
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, images));
        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        membershipValue = documentSnapshot.getBoolean("membership");
                    }
                });

        Calendar calendar = Calendar.getInstance();
        Long nowMillis = calendar.getTimeInMillis();
        Long llfutureMillis = Long.valueOf(intent.getStringExtra("futureMillis"));
        Long isEnd = llfutureMillis - nowMillis;

        // 이미 구매가 완료된 경우
        if(buyerUid.equals(uid) && confirm.equals("true")){
            Log.d(TAG, "uid값 : "+uid+"confirm 값 : "+ confirm);
            // 구매 완료 버튼만 보이게
            mBtnConfirm.setVisibility(View.VISIBLE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            // 낙찰자가 생겼지만 구매가 완료되지 않은 경우
        }else if(buyerUid.equals(uid) && confirm.equals("false") && isEnd <= 0){
            // 구매 버튼만 보이게
            mBtnBuy.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);

            // 누군가 구매를 해서 경매가 종료되었을 때
        }else if(confirm.equals("true") && isEnd <= 0){
            mBtnBidEnd.setVisibility(View.VISIBLE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
        }else{
            // 경매중인 경우
            // 입찰 버튼만 보이게
            mBtnBidButton.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
        }
// 만약 판매자의 이메일과 현재 로그인한 사용자의 이메일이 같다면, 신청하기 버튼을 숨김
        if (seller.equals(userEmail)) {
            mBtnBidButton.setVisibility(View.GONE);
        } else {
            mBtnBidButton.setVisibility(View.VISIBLE);
        }
        mBtnBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventAuctionDetailItemActivity.this, LargeImageActivity.class);
                intent.putExtra("images", images); // 이미지 URL 배열 전달
                startActivity(intent);
            }
        });

        // 구매 버튼 클릭 시 이벤트
        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 문서의 참조 가져오기
                DocumentReference docRef = database.collection("OpenItem").document(documentId);

                Map<String, Object> updates = new HashMap<>();
                updates.put("confirm", true);
                // 문서 수정
                docRef.update(updates);
                // 수정된 내용 갱신
                UserDataHolderOpenItems.loadOpenItems();
                Toast.makeText(EventAuctionDetailItemActivity.this, "구매가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EventAuctionDetailItemActivity.this, BiddingActivity.class));
                finish();
            }
        });


        mBtnBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!membershipValue) {
                    Toast.makeText(getApplicationContext(), "이벤트 경매는 멤버십 회원만 참여할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    return;

                } else {
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
                                                } else {// 현재 입찰가보다 작은 가격이 입력되었을 때
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
            }
        });
    }

    private void setoValues(Item selectedItem) {
        database.collection("EventAuctionInProgress")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    // 조회수 카운팅해주기
                    selectedItem.setViews(selectedItem.getViews()+1);

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
                    itmeTitle.setText(selectedItem.getTitle());
                    itemId.setText(selectedItem.getId());
                    itemInfo.setText(selectedItem.getInfo());
                    category.setText(selectedItem.getCategory());
                    startPrice.setText(String.valueOf(selectedItem.getPrice()));
                    endPrice.setText(String.valueOf(highestBid));
                    seller.setText(selectedItem.getSeller());
                    timeinfo.setText(selectedItem.getFutureDate());

                    long currentTimeMillis = System.currentTimeMillis();
                    String futureMillisStr = selectedItem.getFutureMillis();
                    Long futureMillisValue = Long.valueOf(futureMillisStr);
                    long futureTimeMillis = futureMillisValue - currentTimeMillis;
                    String remainingTime = formatRemainingTime(futureTimeMillis);
                    if (futureTimeMillis <= 0) {
                        btnBid();
                        performAuctionEnd(document);
                    }
                    futureMillis.setText(remainingTime);

                    startCountdownTimer(futureTimeMillis);
                });
    }

    private void updateCountdownText() {
        String remainingTime = formatRemainingTime(remainingTimeMillis);
        futureMillis.setText(remainingTime);
    }

    private void btnBid() {
        mBtnBidButton.setVisibility(View.GONE);
        mBtnBidEnd.setVisibility(View.VISIBLE);
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
                if (millisUntilFinished <= 0) {
                    btnBid();
                }
            }

            @Override
            public void onFinish() {
                // 타이머가 종료될 때의 동작을 여기에 추가하면 됩니다.
                // 예를 들어, 경매 종료 처리 등을 수행할 수 있습니다.
            }
        };

        timer.start();
    }

    private void getSelectoItem() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Item selectedItem = null;
        for (Item item : UserDataHolderEventItems.eventItemList) {
            if (item.getId().equals(id)) {
                selectedItem = item;
                break;
            }
        }
        if (selectedItem != null) {
            // selectedItem 사용하기
            setoValues(selectedItem);
            imageUrl1 = selectedItem.getImageUrl1();
            imageUrl2 = selectedItem.getImageUrl2();
            imageUrl3 = selectedItem.getImageUrl3();
            imageUrl4 = selectedItem.getImageUrl4();
            imageUrl5 = selectedItem.getImageUrl5();
            imageUrl6 = selectedItem.getImageUrl6();
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

//    private void performAuctionEnd(String auctionItemId) {
//
//
//        // 해당 물품의 Bids 컬렉션에 저장된 입찰 정보 가져오기
//        database.collection("EventAuctionInProgress").document(auctionItemId)
//                .collection("Bids")
//                .orderBy("입찰 가격", Query.Direction.DESCENDING) // 내림차순으로 입찰금액 정렬
//                .limit(1) // 가장높은 금액 입찰자 1명 가져오기
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                        DocumentSnapshot highestBidDoc = task.getResult().getDocuments().get(0);
//                        String winningUserId = highestBidDoc.getId();
//                        String winningBidAmount = highestBidDoc.getString("입찰 가격");
//
//                        Map<String, Object> updateData = new HashMap<>();
////                        updateData.put("경매 정보", document);
//
//                        updateData.put("buyer", winningUserId);
//                        updateData.put("price", winningBidAmount);
//
//                        database.collection("EventItem").document(document)
//                                .update(updateData)
//                                .addOnSuccessListener(aVoid -> {
//                                    // 업데이트 성공 시의 처리
//                                    Toast.makeText(EventAuctionDetailItemActivity.this,
//                                            itemId+" 경매가 종료되었습니다.\n낙찰자: " + winningUserId + "\n입찰금액: " + winningBidAmount,
//                                            Toast.LENGTH_LONG).show();
//                                    // remainingTimeMillis 값이 음수인 경우 입찰하기 버튼 비활성화
//                                    if (remainingTimeMillis <= 0) {
//                                        mBtnBidButton.setEnabled(false);
//                                        mBtnBidButton.setText("경매 종료");
//                                    }
//                                })
//                                .addOnFailureListener(e -> {
//                                    // 업데이트 실패 시의 처리
//                                    Toast.makeText(EventAuctionDetailItemActivity.this,
//                                            "낙찰자를 결정하는 중에 오류가 발생했습니다.",
//                                            Toast.LENGTH_SHORT).show();
//                                    Log.d(TAG, document);
//                                });
//                    } else {
//                        Toast.makeText(EventAuctionDetailItemActivity.this,
//                                "낙찰자를 결정하는 중에 오류가 발생했습니다.",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
private void performAuctionEnd(String auctionItemId) {

    // 해당 물품의 Bids 컬렉션에 저장된 입찰 정보 가져오기
    database.collection("EventAuctionInProgress").document(auctionItemId)
            .collection("Bids")
            .orderBy("입찰 가격", Query.Direction.DESCENDING) // 내림차순으로 입찰금액 정렬
            .limit(1) // 가장높은 금액 입찰자 1명 가져오기
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    DocumentSnapshot highestBidDoc = task.getResult().getDocuments().get(0);
                    String winningUserId = highestBidDoc.getId();
                    String winningBidAmount = highestBidDoc.getString("입찰 가격");

                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("buyer", winningUserId);
                    updateData.put("endPrice", winningBidAmount);

                    database.collection("EventItem").document(document)
                            .update(updateData)
                            .addOnSuccessListener(aVoid -> {


                                database.collection("User")
                                        .document(winningUserId)
                                        .get()
                                        .addOnSuccessListener(userDocument -> {
                                            if (userDocument.exists()) {
                                                String winningUserEmail = userDocument.getString("email");
                                                Toast.makeText(EventAuctionDetailItemActivity.this,
                                                        "경매가 종료되었습니다.\n낙찰자: " + winningUserEmail + "\n입찰금액: " + winningBidAmount,
                                                        Toast.LENGTH_LONG).show();

                                                // Rest of the code...
                                            } else {
                                                // Handle the case where the user document doesn't exist
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle the failure to fetch user document
                                        });
                                // remainingTimeMillis 값이 음수인 경우 입찰하기 버튼 비활성화
                                if (remainingTimeMillis <= 0) {
                                    mBtnBidButton.setEnabled(false);
                                    mBtnBidButton.setText("경매 종료");
                                }
                            })
                            // 추가: 업데이트 실패 시 처리
                            .addOnFailureListener(e -> {
                                Toast.makeText(EventAuctionDetailItemActivity.this,
                                        "낙찰자 업데이트 실패: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "낙찰자 업데이트 실패", e);
                            });
                } else {
                    Toast.makeText(EventAuctionDetailItemActivity.this,
                            "낙찰자를 결정하는 중에 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT).show();
                    // 추가: 업데이트 실패 시 처리
                    Log.e(TAG, "낙찰자를 결정하는 중에 오류가 발생했습니다.");
                }
            })
            // 추가: 업데이트 실패 시 처리
            .addOnFailureListener(e -> {
                Toast.makeText(EventAuctionDetailItemActivity.this,
                        "입찰 정보 가져오기 실패: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "입찰 정보 가져오기 실패", e);
            });
    }
    @Override
    public void onBackPressed(){
//        startActivity(new Intent(EventAuctionDetailItemActivity.this, EventAuctionActivity.class));
        finish();
    }
}
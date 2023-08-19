package com.example.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OpenDetailItemtestActivity extends AppCompatActivity implements ImageSliderAdapter.OnImageClickListener {
    private long remainingTimeMillis;
    private CountDownTimer countDownTimer;
    private TextView itmeTitle, itemId, startPrice, endPrice, itemInfo, seller, category, timeinfo, futureMillis;
    private ImageView imgUrl1, imgUrl2, imgUrl3, imgUrl4, imgUrl5, imgUrl6;
    private EditText mETBidPrice;
    private Button mBtnBidButton;

    private ViewPager2 sliderViewPager;
    private PhotoView photoViewSlider;
    private LinearLayout layoutIndicator;

    private String[] images = new String[6];

    String imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6;
    String document, buyer;
    FirebaseFirestore database;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_detail_itemtest);
        sliderViewPager = findViewById(R.id.sliderViewPager);


        sliderViewPager.setOffscreenPageLimit(1);
//        photoViewSlider = findViewById(R.id.sliderViewPager);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this, images);
        imageSliderAdapter.setImageClickListener(position -> {
            // 이미지 클릭 이벤트 처리
            Toast.makeText(this, "Image clicked at position: " + position, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FullScreenImageActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("images", images);
            startActivity(intent);
        });

        sliderViewPager.setAdapter(imageSliderAdapter);
//
//        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//            }
//        });

        itmeTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);

        category = findViewById(R.id.category);
        timeinfo = findViewById(R.id.timeInfoinfo);
        futureMillis = findViewById(R.id.futureMillis);
        Itemtest selectedItem = null;
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String sellerName = intent.getStringExtra("seller");

        String documentId = title + sellerName;
        this.document = documentId;
        // 문서의 참조 가져오기
        database = FirebaseFirestore.getInstance();
        DocumentReference docRef = database.collection("OpenItem").document(documentId);
        getSelectedItem();

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
        remainingTimeMillis = 0;

        buyer = " ";

        mBtnBidButton = findViewById(R.id.btn_bidbutton);

//        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(Itemtest.getImageUrls());
//        viewPager.setAdapter(imageSliderAdapter);




        sliderViewPager.setAdapter(new ImageSliderAdapter(this, images));
        mBtnBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mETBidPrice = findViewById(R.id.et_bid); // 경매 가격 받기
                String bidPrice = mETBidPrice.getText().toString(); // 경매 가격 초기화하기



                // 문서 읽기
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String price = document.getString("price");
                            if (price != null) {
                                int priceValue = Integer.parseInt(price);
                                int enterPrice = Integer.valueOf(bidPrice);

                                if (enterPrice < priceValue) {
                                    Toast.makeText(OpenDetailItemtestActivity.this, "시작 가격보다 높은 금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    updateUserinfo(bidPrice);
                                    // buyer값 업데이트
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("buyer", buyer);

                                    DocumentReference userDocRef = db.collection("OpenItem").document(documentId);
                                    userDocRef.update(data)
                                            .addOnSuccessListener(aVoid -> {
                                                // 등록된 리스트 새로 갱신
                                                UserDataHolderOpenItems.loadOpenItems();
                                                Toast.makeText(OpenDetailItemtestActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(getApplicationContext(), "종료 시간 업데이트에 실패했습니다." + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                // price 필드가 null인 경우
                            }
                        } else {
                            // 해당 문서가 존재하지 않을 경우
                        }
                    } else {
                        // 작업 실패
                    }
                });
            }
        });

        // 이하 내용은 코드 조각입니다. (updateEndTime 및 getEndTime 메소드 호출 등)
    }

    @Override
    public void onImageClick(int position) {
        // 이미지 클릭 시 실행할 동작을 여기에 구현
        Toast.makeText(this, "Image clicked at position: " + position, Toast.LENGTH_SHORT).show();
    }

    private void updateEndTime(String documentId) {
        // 현재 시간을 기준으로 종료 시간 업데이트
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 1); // 1분 후 종료 시간으로 설정

        String upfutureMillis = String.valueOf(calendar.getTimeInMillis());

        // "yyyy-MM-dd HH:mm:ss" 포맷으로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(calendar.getTime());

        Map<String, Object> data = new HashMap<>();
        data.put("futureMillis", upfutureMillis);
        data.put("futureDate", formattedDate);

        DocumentReference userDocRef = db.collection("OpenItem").document(documentId);
        userDocRef.update(data)
                .addOnSuccessListener(aVoid -> {
                    // 등록된 리스트 새로 갱신
                    UserDataHolderOpenItems.loadOpenItems();
                    Toast.makeText(OpenDetailItemtestActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "종료 시간 업데이트에 실패했습니다." + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
        getEndTime(documentId);
    }

    private void getEndTime(String documentId) {
        DocumentReference userDocRef = db.collection("OpenItem").document(documentId);

        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String futureMillisStr = documentSnapshot.getString("futureMillis");
                        String futureDate = documentSnapshot.getString("futureDate");

                        if (futureMillisStr != null && futureDate != null) {
                            // 업데이트된 값을 화면에 반영
                            timeinfo.setText(futureDate);
                            long currentTimeMillis = System.currentTimeMillis();
                            Long futureMillisValue = Long.valueOf(futureMillisStr);
                            long futureTimeMillis = futureMillisValue - currentTimeMillis;
                            futureMillis.setText(formatRemainingTime(futureTimeMillis));

                            startCountdownTimer(futureTimeMillis, 1000);
                            // 기존 시간 텍스트를 숨김 처리
                            //futureMillis.setVisibility(View.GONE);

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "데이터 가져오기에 실패했습니다." + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void setItemValues(Itemtest selectedItem) {
        database.collection("OpenAuctionInProgress")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
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
                    }

                    futureMillis.setText(remainingTime);

                    startCountdownTimer(futureTimeMillis, 1000);
                });
    }

    private void updateCountdownText() {
        String remainingTime = formatRemainingTime(remainingTimeMillis);
        futureMillis.setText(remainingTime);
    }

    private void btnBid() {
        mBtnBidButton.setEnabled(false);
        mBtnBidButton.setText("경매 종료");
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

    private void stopCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void startCountdownTimer(long futureTimeMillis, int a) {
        stopCountdownTimer(); // 기존 타이머가 실행 중이라면 중지합니다.

        CountDownTimer timer = new CountDownTimer(futureTimeMillis, a) {
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
        countDownTimer = timer;
        countDownTimer.start(); // 썪쎾쓰
    }

    private void getSelectedItem() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Itemtest selectedItem = null;
        for (Itemtest item : OpenAuctiontestActivity.openItemList) {
            if (item.getId().equals(id)) {
                selectedItem = item;
                break;
            }
        }
        if (selectedItem != null) {
            setItemValues(selectedItem);
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
            updateHighestBidListener(database.collection("OpenAuctionInProgress")
                    .document(document)
                    .collection("Bids"));
        }
    }

    // 공개 경매 컬랙션에서 가장 큰 입찰가 찾고 업로드
    private void updateHighestBidListener(CollectionReference bidsCollectionRef) {
        bidsCollectionRef.orderBy("입찰 가격", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (value != null && !value.isEmpty()) {
                        QueryDocumentSnapshot highestBidDoc = (QueryDocumentSnapshot) value.getDocuments().get(0);
                        Map<String, Object> bidData = highestBidDoc.getData();
                        String highestBidPriceStr = (String) bidData.get("입찰 가격");
                        String highestBidderId = (String) bidData.get("입찰자 아이디");

                        if (highestBidPriceStr != null) {
                            try {
                                int highestBidPrice = Integer.parseInt(highestBidPriceStr);
                                endPrice.setText(String.valueOf(highestBidPrice));
                                if (highestBidderId != null) {
                                    // 최고 입찰자 ID를 endPrice 아래에 표시 (선택 사항)
                                    buyer = highestBidderId;

                                }
                            } catch (NumberFormatException e) {
                                // 유효하지 않은 입찰 가격 처리
                            }
                        }
                    } else {
                        endPrice.setText("경매에 참여한 사람이 없습니다.");
                    }
                });
    }

    private void updateUserinfo(String bidPrice) {
        String uid = UserManager.getInstance().getUserUid();
        DocumentReference userDocRef = database.collection("User").document(uid);
        DocumentReference auctionDocRef = database.collection("OpenAuctionInProgress").document(document);

        auctionDocRef.collection("Bids").document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            Map<String, Object> existingBidData = document.getData();
                            String existingBidPriceStr = (String) existingBidData.get("입찰 가격");
                            if (existingBidPriceStr != null) {
                                try {
                                    int existingBidPrice = Integer.parseInt(existingBidPriceStr);
                                    int newBidPrice = Integer.parseInt(bidPrice);
                                    if (newBidPrice > existingBidPrice) {
                                        Map<String, Object> bidData = new HashMap<>();
                                        bidData.put("입찰자 아이디", userDocRef.getId());
                                        bidData.put("입찰 가격", bidPrice);
                                        bidData.put("경매 정보", this.document);

                                        auctionDocRef.collection("Bids").document(uid)
                                                .set(bidData)
                                                .addOnSuccessListener(aVoid -> {
                                                    updateHighestBidListener(auctionDocRef.collection("Bids"));
                                                });
                                        updateEndTime(this.document);
                                    } else if (newBidPrice == existingBidPrice) {
                                        Toast.makeText(OpenDetailItemtestActivity.this, "전 입찰가와 같은 금액을 입력하셨습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(OpenDetailItemtestActivity.this, "전 입찰가보다 높은 가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (NumberFormatException e) {
                                    Toast.makeText(OpenDetailItemtestActivity.this, "가격을 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }         // 해당 사용자의 입찰 정보가 없을 때
                        else {
                            // 경매 문서의 하위 컬렉션에 입찰 정보 저장
                            Map<String, Object> bidData = new HashMap<>();
                            bidData.put("입찰자 아이디", userDocRef.getId());
                            bidData.put("입찰 가격", bidPrice);
                            bidData.put("경매 정보", document);

                            auctionDocRef.collection("Bids").document(uid)
                                    .set(bidData)
                                    .addOnSuccessListener(aVoid -> {
                                        updateHighestBidListener(auctionDocRef.collection("Bids"));
                                    });
                        }
                    }
                });

    }
    @Override
    public void onBackPressed(){
//        startActivity(new Intent(ShareDetailActivity.this, ShareActivity.class));
        finish();
    }

}
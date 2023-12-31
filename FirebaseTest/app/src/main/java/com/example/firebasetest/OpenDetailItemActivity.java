package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class OpenDetailItemActivity extends AppCompatActivity {
    private long remainingTimeMillis;
    private CountDownTimer countDownTimer;
    private TextView itmeTitle, itemId, startPrice, endPrice, itemInfo, seller, category, timeinfo, futureMillis;
    private ImageView imgUrl1, imgUrl2, imgUrl3, imgUrl4, imgUrl5, imgUrl6;
    private EditText mETBidPrice;
    private ViewPager2 sliderViewPager;
    private Button btnCancle, btnChat, mBtnBidButton, mBtnBuy, mBtnConfirm, mBtnBidEnd, mBtnBigButton;
    String imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6;
    String document, buyer, highPrice, buyerId;
    FirebaseFirestore database;
    private String[] images = new String[6];
    boolean open;

    private boolean highestBidUpdated = false;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_detail_item);
        sliderViewPager = findViewById(R.id.sliderViewPager);

        sliderViewPager.setOffscreenPageLimit(1);

        itmeTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);

        category = findViewById(R.id.category);
        timeinfo = findViewById(R.id.timeInfoinfo);
        futureMillis = findViewById(R.id.futureMillis);
        remainingTimeMillis = 0;
        highPrice = "0";

        open = false;

        buyer = " ";
        buyerId = " ";

        database = FirebaseFirestore.getInstance();

        mBtnBidButton = findViewById(R.id.btn_bidbutton);
        mBtnBigButton = findViewById(R.id.btn_bigbutton);
        mBtnBidEnd = findViewById(R.id.btn_bidEnd);
        mBtnBuy = findViewById(R.id.btn_buy);
        mBtnConfirm = findViewById(R.id.btn_buyConfirm);
        mETBidPrice = findViewById(R.id.et_bid); // 경매 가격 받기
        btnChat = findViewById(R.id.btn_chat);
        btnCancle = findViewById(R.id.btn_cancle);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String sellerName = intent.getStringExtra("seller");
        String buyerUid = intent.getStringExtra("buyer");
        String confirm = intent.getStringExtra("confirm");
        String seller = intent.getStringExtra("seller");

        String documentId = title + sellerName;
        this.document = documentId;
        database = FirebaseFirestore.getInstance();
        DocumentReference docRef = database.collection("OpenItem").document(documentId);
        String uid = UserManager.getInstance().getUserUid(); // 현재 유저의 uid
        String userEmail = UserManager.getInstance().getUserEmail(); // 현재 유저의 email
        getSelectedItem();

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
        sliderViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenDetailItemActivity.this, LargeImageActivity.class);
                intent.putExtra("images", images); // 이미지 URL 배열 전달
                startActivity(intent);
            }
        });

        Calendar calendar1 = Calendar.getInstance();
        Long nowMillis = calendar1.getTimeInMillis();
        Long futureMillis = Long.valueOf(intent.getStringExtra("futureMillis"));
        Long isEnd = futureMillis - nowMillis;
        // 이미 구매가 완료된 경우
        if(buyerUid.equals(uid) && confirm.equals("true")){
            Log.d(TAG, "uid값 : "+uid+"confirm 값 : "+ confirm);
            // 구매 완료 버튼만 보이게
            mBtnConfirm.setVisibility(View.VISIBLE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);
            // 경매가 종료되고 낙찰자가 나로 정해졌는데 아직 구매가 완료되지 않은 경우
        }else if(buyerUid.equals(uid) && confirm.equals("false") && isEnd <= 0){
            // 구매 버튼, 채팅 버튼 보이게
            mBtnBuy.setVisibility(View.VISIBLE);
            btnChat.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);

            // 판매자가 '나'고 경매가 끝났지만 구매가 완료되지 않은 경우
        }else if(userEmail.equals(seller) && confirm.equals("false") && isEnd <= 0) {
            // 구매 버튼, 채팅 버튼 보이게
            mBtnBuy.setVisibility(View.VISIBLE);
            btnChat.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);

            // 누군가 구매를 해서 경매가 종료되었을 때
        }else if(confirm.equals("true") && isEnd <= 0){
            mBtnBidEnd.setVisibility(View.VISIBLE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);
        }else{
            // 경매중인 경우
            // 입찰 버튼만 보이게
            mBtnBidButton.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);
        }
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
                Toast.makeText(OpenDetailItemActivity.this, "구매가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OpenDetailItemActivity.this, OpenAuctionActivity.class));
                finish();
            }
        });

        // 낙찰 포기 클릭 시
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업 창 띄워서 확인 눌렀을 때 낙찰 취소되도록
                AlertDialog.Builder builder = new AlertDialog.Builder(OpenDetailItemActivity.this);
                builder.setTitle("낙찰을 취소 하시겠습니까?"); // 다이얼로그 제목
                builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
                builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        // 취소 횟수 3회 이상이면 X
                        db.collection("User").whereEqualTo("email", userEmail)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : (task.getResult())){
                                            Integer cancleCnt = Integer.parseInt(String.valueOf(document.getData().get("cancleCnt")));
                                            if(cancleCnt >= 3){
                                                Toast.makeText(OpenDetailItemActivity.this, "이미 낙찰 포기 횟수를 초과했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                // 낙찰자 위임하는 코드
                                                // Bids에서 해당 유저의 문서를 지우면 될듯
                                                DocumentReference auctionDocRef = db.collection("OpenAuctionInProgress").document(title+seller);
                                                DocumentReference delCurrentUser = auctionDocRef.collection("Bids").document(uid);
                                                delCurrentUser.delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // 삭제 성공 시 처리
                                                                    DocumentReference auctionDocRef = database.collection("OpenAuctionInProgress").document(title + seller);
                                                                    updateHighestBidListener(auctionDocRef.collection("Bids"));
                                                                    // 취소 횟수 카운팅 ( +1 )
                                                                    db.collection("User").document(uid).update("cancleCnt", cancleCnt+1);
//                                                                    document.getData().put("cancleCnt", cancleCnt+1);
                                                                    Toast.makeText(OpenDetailItemActivity.this, "낙찰을 포기했습니다.", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    // 삭제 실패 시 처리
                                                                    Toast.makeText(OpenDetailItemActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    }
                                });


                    }
                });

                builder.setNegativeButton("아니요", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show(); // 다이얼로그 보이기

            }
        });

        // 문서 읽기
        docRef.get().addOnCompleteListener(task -> {
            if (true) {
                DocumentSnapshot document = task.getResult();
                if (true) {
                    Calendar calendar = Calendar.getInstance();
                    String endPrice =String.valueOf(document.getString("endPrice"));
                    if (true) {
                        if(endPrice.equals("0")){
                            Map<String, Object> data = new HashMap<>();
                            data.put("endPrice", highPrice);
                            data.put("buyer",buyer);

                            docRef.update(data)
                                    .addOnSuccessListener(aVoid -> {
                                        // 등록된 리스트 새로 갱신
//                                        UserDataHolderOpenItems.loadOpenItems();
                                        Toast.makeText(OpenDetailItemActivity.this, "종료 가격 설정 완료", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "종료 가격 설정 실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                        if(true){

                            Map<String, Object> data = new HashMap<>();
                            data.put("endPrice", highPrice);
                            data.put("buyer",buyer);
                            docRef.update(data)
                                    .addOnSuccessListener(aVoid -> {
                                        // 등록된 리스트 새로 갱신
//                                        UserDataHolderOpenItems.loadOpenItems();
                                        Toast.makeText(OpenDetailItemActivity.this, "종료 가격 설정 완료", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "종료 가격 설정 실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                        if(open){
                            String buyer = document.getString("buyer");
                            String strEndPrice = document.getString("endPrice");



                            db.collection("User")
                                    .document(buyer)
                                    .get()
                                    .addOnSuccessListener(userDocument -> {
                                        if (userDocument.exists()) {
                                            String winningUserEmail = userDocument.getString("email");
                                            Toast.makeText(OpenDetailItemActivity.this,
                                                    "경매가 종료되었습니다.\n낙찰자: " + winningUserEmail + "\n입찰금액: " + strEndPrice,
                                                    Toast.LENGTH_LONG).show();

                                            // Rest of the code...
                                        } else {
                                            // Handle the case where the user document doesn't exist
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the failure to fetch user document
                                    });



//                            Toast.makeText(OpenDetailItemActivity.this, "경매가 종료되었습니다.\n낙찰자: " + buyerId + "\n입찰금액: " + strEndPrice, Toast.LENGTH_LONG).show();
                            return;
                        }

                    } else {
                        // futureMillis 필드가 null인 경우
                    }
                } else {
                    // 해당 문서가 존재하지 않을 경우
                }
            } else {
                // 작업 실패
            }
        });

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
                                    Toast.makeText(OpenDetailItemActivity.this, "시작 가격보다 높은 금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(OpenDetailItemActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
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
        mBtnBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenDetailItemActivity.this, LargeImageActivity.class);
                intent.putExtra("images", images); // 이미지 URL 배열 전달
                startActivity(intent);
            }
        });

        // 이하 내용은 코드 조각입니다. (updateEndTime 및 getEndTime 메소드 호출 등)
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
                    Toast.makeText(OpenDetailItemActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
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

    private void setItemValues(Item selectedItem) {
        UserDataHolderOpenItems.loadOpenItems();
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
//                    Glide.with(this)
//                            .load(selectedItem.getImageUrl())
//                            .into(imgUrl);

                    startCountdownTimer(futureTimeMillis, 1000);
                });
    }

    private void updateCountdownText() {
        String remainingTime = formatRemainingTime(remainingTimeMillis);
        futureMillis.setText(remainingTime);
    }

    private void btnBid() {
        mBtnBidButton.setVisibility(View.GONE);
        mETBidPrice.setVisibility(View.GONE);
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
                open = true;
                // 타이머가 종료될 때의 동작을 여기에 추가하면 됩니다.
                // 예를 들어, 경매 종료 처리 등을 수행할 수 있습니다.
            }
        };
        countDownTimer = timer;
        countDownTimer.start();
    }


    private void getSelectedItem() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Item selectedItem = null;
        for (Item item : UserDataHolderOpenItems.openItemList) {
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
                                    buyer = highestBidderId;
                                    highPrice = String.valueOf(highestBidPrice);
                                    if (!highestBidUpdated) {
                                        highestBidUpdated = true;
//                                        Toast.makeText(OpenDetailItemActivity.this, "최고가가 바뀌었습니다! 서두르세요!", Toast.LENGTH_SHORT).show();
                                    }
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
                                        Toast.makeText(OpenDetailItemActivity.this, "전 입찰가와 같은 금액을 입력하셨습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(OpenDetailItemActivity.this, "전 입찰가보다 높은 가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (NumberFormatException e) {
                                    Toast.makeText(OpenDetailItemActivity.this, "가격을 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
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
//        startActivity(new Intent(OpenDetailItemActivity.this, OpenAuctionActivity.class));
        finish();
    }
}
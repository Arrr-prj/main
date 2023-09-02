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
//import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ShareDetailActivity extends AppCompatActivity {
    private long remainingTimeMillis;
    private CountDownTimer countDownTimer;
    private TextView itemTitle, itemId, startPrice, endPrice, itemInfo, seller, category, timeinfo, futureMillis;
    private EditText mETBidPrice; // 입찰가
    private Button mBtnBidJoin, mBtnAgain, mBtnCong, mBtnBigButton, mBtnback; // 입찰하기 버튼
    private String sellerName, bidAmount;
    private ViewPager2 sliderViewPager;
//    private PhotoView photoViewSlider;
    private LinearLayout layoutIndicator;
    private String[] imageUrls;
    String buyer, document;
//    String imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6;
    FirebaseFirestore database;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    boolean open;
    String userEmail; // 현재 유저의 이메일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        sliderViewPager.setOffscreenPageLimit(1);

        // 아이템 정보들
        itemTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        category = findViewById(R.id.category);
        timeinfo = findViewById(R.id.timeInfoinfo);
        futureMillis = findViewById(R.id.futureMillis);
        mBtnback = findViewById(R.id.btn_back);
        database = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String seller = intent.getStringExtra("seller");
        String buyerUid = intent.getStringExtra("buyer");
        // 두번째 매개변수는 값을 잘 불러오지 못했을때 할당해줄 기본 값
//        String confirm = intent.getStringExtra("confirm");
        String futureMillis = intent.getStringExtra("futureMillis");
        String documentId = title + seller;
        String uid = UserManager.getInstance().getUserUid(); // 현재 유저의 uid


        Calendar calendar = Calendar.getInstance();
        String currentMillis = String.valueOf(calendar.getTimeInMillis());
        if (user != null) {
            userEmail = user.getEmail(); // 현재 사용자의 이메일
            UserManager.getInstance().setUserEmail(userEmail); // UserManager에 이메일 저장
        }
        open = false;
        getSelectoItem();
        // 이미지 URL 배열을 받아옵니다.
        imageUrls = getIntent().getStringArrayExtra("imageUrls");

        // ViewPager2 어댑터를 설정합니다.
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);
        sliderViewPager.setAdapter(imageSliderAdapter);

        // 입력받은 입찰 가격
        mBtnBidJoin = findViewById(R.id.btn_shareJoin); // 입찰하기 버튼
        mBtnCong = findViewById(R.id.btn_Cong); // 당첨
        mBtnAgain = findViewById(R.id.btn_Again); // 당첨x
        mBtnBigButton = findViewById(R.id.btn_bigbutton);
        buyer = " ";

        getSelectoItem();


        if (buyerUid.equals(uid)) {
//            Log.d(TAG, "uid값 : " + uid + "confirm 값 : " + confirm);
            // 당첨자가 나인 경우
            mBtnCong.setVisibility(View.VISIBLE);
            mBtnBidJoin.setVisibility(View.GONE);
            mBtnAgain.setVisibility(View.GONE);
        } else if (Long.parseLong(currentMillis) >= Long.parseLong(futureMillis)) {
            // 낙찰자가 생겼지만 내가 아닌 경우
            mBtnAgain.setVisibility(View.VISIBLE);
            mBtnCong.setVisibility(View.GONE);
            mBtnBidJoin.setVisibility(View.GONE);
        } else if (Long.parseLong(currentMillis) < Long.parseLong(futureMillis)) {
            // 경매가 진행중인 경우
            mBtnBidJoin.setVisibility(View.VISIBLE);
            mBtnCong.setVisibility(View.GONE);
            mBtnAgain.setVisibility(View.GONE);
        }
// 만약 판매자의 이메일과 현재 로그인한 사용자의 이메일이 같다면, 신청하기 버튼을 숨김
        if (seller.equals(userEmail)) {
            mBtnBidJoin.setVisibility(View.GONE);
        } else {
            mBtnBidJoin.setVisibility(View.VISIBLE);
        }
        DocumentReference docRef = database.collection("ShareItem").document(documentId);
        // 문서 읽기
        docRef.get().addOnCompleteListener(task -> {
            if (true) {
                DocumentSnapshot document = task.getResult();
                if (open && buyer.equals("")) {
                    String buyer = document.getString("buyer");
                    db.collection("User")
                            .document(buyer)
                            .get()
                            .addOnSuccessListener(userDocument -> {
                                if (userDocument.exists()) {
                                    String winningUserEmail = userDocument.getString("email");
                                    Toast.makeText(ShareDetailActivity.this,
                                            "경매가 종료되었습니다낙.\n나눔받은 사람: " + winningUserEmail,
                                            Toast.LENGTH_LONG).show();

                                    // Rest of the code...
                                } else {
                                    // Handle the case where the user document doesn't exist
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Handle the failure to fetch user document`
                            });


//                            Toast.makeText(OpenDetailItemActivity.this, "경매가 종료되었습니다.\n낙찰자: " + buyerId + "\n입찰금액: " + strEndPrice, Toast.LENGTH_LONG).show();
                    return;
                } else{
                    Toast.makeText(ShareDetailActivity.this,
                            "나눔을 신청한 사람이 없습니다.",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                // futureMillis 필드가 null인 경우
            }
        });
        mBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareDetailActivity.this, ShareActivity.class);

                startActivity(intent);
            }
        });
        mBtnBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareDetailActivity.this, LargeImageActivity.class);
                intent.putExtra("images", imageUrls); // 이미지 URL 배열 전달
                startActivity(intent);
            }
        });
        // 신청하기를 눌렀을 때
        mBtnBidJoin.setOnClickListener(new View.OnClickListener() {


            private String document;

            @Override
            public void onClick(View view) {
                String uid = UserManager.getInstance().getUserUid();
                mETBidPrice = findViewById(R.id.et_bid); // 경매 가격 받기
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
                    itemTitle.setText(selectedItem.getTitle());
                    itemId.setText(selectedItem.getId());
                    itemInfo.setText(selectedItem.getInfo());
                    category.setText(selectedItem.getCategory());
                    seller.setText(selectedItem.getSeller());
                    timeinfo.setText(selectedItem.getFutureDate());

                    String title = selectedItem.getTitle();
                    String sellerName = selectedItem.getSeller();

                    String documentId1 = title + sellerName;

                    this.document = documentId1;

                    long currentTimeMillis = System.currentTimeMillis();
                    String futureMillisStr = selectedItem.getFutureMillis();
                    Long futureMillisValue = Long.valueOf(futureMillisStr);
                    long futureTimeMillis = futureMillisValue - currentTimeMillis;
                    String remainingTime = formatRemainingTime(futureTimeMillis);
                    if (futureTimeMillis <= 0) {
                        btnBid();
                        selbuyer();
                        //여기에 뭐 넣기
                    }
                    futureMillis.setText(remainingTime);

                    startCountdownTimer(futureTimeMillis);
                });
    }

    private void btnBid() {
        mBtnBidJoin.setVisibility(View.GONE);
    }

    private void updateCountdownText() {
        String remainingTime = formatRemainingTime(remainingTimeMillis);
        futureMillis.setText(remainingTime);
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
                open = true;
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
        for (Item item : UserDataHolderShareItem.shareItemList) {
            if (item.getId() != null) {
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

//    private void selbuyer() {
//        DocumentReference auctionDocRef = database.collection("ShareInProgress").document(document);
//
//        auctionDocRef.collection("Share")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        ArrayList<String> bidderUids = new ArrayList<>();
//                        for (QueryDocumentSnapshot docu : task.getResult()) {
//                            bidderUids.add(docu.getId());
//                        }

    //                        if (!bidderUids.isEmpty()) {
//                            int randomIndex = (int) (Math.random() * bidderUids.size());
//                            String selectedBidderUid = bidderUids.get(randomIndex);
//
//                            // 선택된 입찰자 uid를 이용하여 유저의 이름 등을 가져오는 등의 로직을 추가할 수 있습니다.
//                            // 이 부분에서 필요한 데이터를 가져와서 처리하면 됩니다.
//                            // 예: database.collection("User").document(selectedBidderUid).get() 등
//                            // 선택된 입찰자 uid를 전역 변수 buyer에 할당합니다.
//                            buyer = selectedBidderUid;
//                            Map<String, Object> updateData = new HashMap<>();
//                            updateData.put("buyer", buyer);
//
//                            database.collection("ShareItem").document(document)
//                                    .update(updateData)
//                                    .addOnSuccessListener(aVoid -> {
//                                        // 업데이트 성공 시의 처리
//                                        Toast.makeText(ShareDetailActivity.this,
//                                                itemId + " 경매가 종료되었습니다.\n낙찰자: " + buyer,
//                                                Toast.LENGTH_LONG).show();
//
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        // 업데이트 실패 시의 처리
//                                        Toast.makeText(ShareDetailActivity.this,
//                                                "낙찰자를 결정하는 중에 오류가 발생했습니다.",
//                                                Toast.LENGTH_SHORT).show();
//                                        Log.d(TAG, document);
//                                    });
//                        }
//                    }
//                });
//    }
    private void selbuyer() {
        DocumentReference auctionDocRef = database.collection("ShareInProgress").document(document);

        auctionDocRef.collection("Share")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> bidderUids = new ArrayList<>();
                        for (QueryDocumentSnapshot docu : task.getResult()) {
                            bidderUids.add(docu.getId());
                        }

                        if (!bidderUids.isEmpty()) {
                            int randomIndex = (int) (Math.random() * bidderUids.size());
                            String selectedBidderUid = bidderUids.get(randomIndex);

                            // 선택된 입찰자 uid를 이용하여 유저의 이름 등을 가져오는 등의 로직을 추가할 수 있습니다.
                            // 이 부분에서 필요한 데이터를 가져와서 처리하면 됩니다.
                            // 예: database.collection("User").document(selectedBidderUid).get() 등
                            // 선택된 입찰자 uid를 전역 변수 buyer에 할당합니다.
                            buyer = selectedBidderUid;

                            DocumentReference shareItemDocRef = database.collection("ShareItem").document(document);
                            shareItemDocRef.get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String currentBuyer = documentSnapshot.getString("buyer");
                                            if (currentBuyer == null || currentBuyer.isEmpty()) {
                                                Map<String, Object> updateData = new HashMap<>();
                                                updateData.put("buyer", buyer);

                                                shareItemDocRef.update(updateData)
                                                        .addOnSuccessListener(aVoid -> {
                                                            // 업데이트 성공 시의 처리
                                                            Toast.makeText(ShareDetailActivity.this,
                                                                    itemId + " 경매가 종료되었습니다.\n낙찰자: " + buyer,
                                                                    Toast.LENGTH_LONG).show();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // 업데이트 실패 시의 처리
                                                            Toast.makeText(ShareDetailActivity.this,
                                                                    "낙찰자를 결정하는 중에 오류가 발생했습니다.",
                                                                    Toast.LENGTH_SHORT).show();
                                                            Log.d(TAG, document);
                                                        });
                                            } else {
                                                // 이미 buyer가 존재하는 경우 처리
                                                Toast.makeText(ShareDetailActivity.this,
                                                        "경매가 종료되었습니다.\n낙찰자: " + currentBuyer,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // 문서 가져오기 실패 시의 처리
                                        Toast.makeText(ShareDetailActivity.this,
                                                "문서를 가져오는 중에 오류가 발생했습니다.",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, document);
                                    });
                        } else {

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(ShareDetailActivity.this, ShareActivity.class));
        finish();
    }
}
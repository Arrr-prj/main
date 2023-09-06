package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BiddingDetailItemActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private long remainingTimeMillis; // 전역 변수로 추가
    private Button btnEdit, btnDelete;
    private Button mBtnBidButton, mBtnBuy, mBtnConfirm, mBtnBidEnd, mBtnBigButton, mBtnback, btnCancle, mBtnreport;
    private EditText bidText;
    private TextView itemTitle, itemId, startPrice, endPrice, itemInfo, seller, category, timeinfo, futureMillis;

    private LinearLayout bidPopupLayout;
    private ViewPager2 sliderViewPager;
    private PhotoView photoViewSlider;
    private LinearLayout layoutIndicator;
    //    private FirebaseFirestore db;
    private String sellerName, bidAmount;

    String buyer, document, sellerr;
    Item item;
    Bitmap bitmap;
    String userEmail; // 현재 유저의 이메일
    private String[] imageUrls;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private DecimalFormat decimalFormat;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bidding_item);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        sliderViewPager.setOffscreenPageLimit(1);

//        btnChat = findViewById(R.id.btn_chat);
        btnCancle = findViewById(R.id.btncancel);

        // 아이템 정보들
        itemTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
//        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        category = findViewById(R.id.category);
        timeinfo = findViewById(R.id.endTime);
        futureMillis = findViewById(R.id.futureMillis);
        userEmail = UserManager.getInstance().getUserEmail(); // 현재 사용자의 이메일
        decimalFormat = new DecimalFormat("#,###");
//        getSelectbItem();
        mBtnreport = findViewById(R.id.btn_report); //신고버튼

        sliderViewPager.setAdapter(new ImageSliderAdapter(this, imageUrls));
        remainingTimeMillis = 0;

        buyer = " ";
        //입찰하기 버튼
        mBtnBidButton = findViewById(R.id.biddng_bid);
        mBtnBidEnd = findViewById(R.id.btn_bidEnd);
        bidPopupLayout = findViewById(R.id.bidPopupLayout);
        bidText = findViewById(R.id.bidAmountEditText);
        mBtnBuy = findViewById(R.id.btn_buy);
        mBtnConfirm = findViewById(R.id.btn_buyConfirm);
        mBtnBigButton = findViewById(R.id.btn_bigbutton);
        mBtnback = findViewById(R.id.btn_back);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String documentId = getIntent().getStringExtra("documentId");
        String title = intent.getStringExtra("title");
        String buyerUid = intent.getStringExtra("buyer");
        String seller = intent.getStringExtra("seller");
        String confirm = intent.getStringExtra("confirm");
        String id = intent.getStringExtra("id");
        String endPrice = intent.getStringExtra("endPrice");
        String uid = UserManager.getInstance().getUserUid(); // 현재 유저의 uid

        ImageView SellerInfo = findViewById(R.id.iv_profile); // 판매자정보



        // User 컬렉션에서 판매자 정보 가져오기
        db.collection("User")
                .whereEqualTo("email", seller)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // 첫 번째 문서 가져오기
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        // User 문서에서 프로필 사진 URL 가져오기
                        String profileImageUrl = document.getString("profile picture");

                        Glide.with(this)
                                .load(profileImageUrl)
                                .into(SellerInfo);
                    } else {
                        // 해당 이메일을 가진 User 문서가 없을 때 처리
                    }
                })
                .addOnFailureListener(e -> {
                    // User 컬렉션에서 데이터 가져오기 실패 처리
                });


// 이미지 URL 배열을 받아옵니다.
        imageUrls = getIntent().getStringArrayExtra("imageUrls");

        // ViewPager2 어댑터를 설정합니다.
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);
        sliderViewPager.setAdapter(imageSliderAdapter);
        getSelectbItem();

        Calendar calendar = Calendar.getInstance();
        Long nowMillis = calendar.getTimeInMillis();
        Long futureMillis = Long.valueOf(intent.getStringExtra("futureMillis"));
        Long isEnd = futureMillis - nowMillis;
        // 이미 구매가 완료된 경우
        if (buyerUid.equals(uid) && confirm.equals("true")) {
            Log.d(TAG, "uid값 : " + uid + "confirm 값 : " + confirm);
            // 구매 완료 버튼만 보이게
            mBtnConfirm.setVisibility(View.VISIBLE);
            mBtnBidButton.setVisibility(View.GONE);
//            btnChat.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);
            // 낙찰자가 '나'지만 구매가 완료되지 않은 경우
        } else if (buyerUid.equals(uid) && confirm.equals("false") && isEnd <= 0) {
            // 구매 버튼, 채팅 버튼 보이게
            mBtnBuy.setVisibility(View.VISIBLE);
//            btnChat.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);

            // 판매자가 '나'고 경매가 끝났지만 구매가 완료되지 않은 경우
        } else if (userEmail.equals(seller) && confirm.equals("false") && isEnd <= 0) {
            // 구매 버튼, 채팅 버튼 보이게
            mBtnBuy.setVisibility(View.VISIBLE);
//            btnChat.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);

            // 누군가 구매를 해서 경매가 종료되었을 때
        } else if (confirm.equals("true") && isEnd <= 0) {
            mBtnBidEnd.setVisibility(View.VISIBLE);
            mBtnBuy.setVisibility(View.GONE);
//            btnChat.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);
        } else {
            // 경매중인 경우
            // 입찰 버튼만 보이게
            mBtnBidButton.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
//            btnChat.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);
        }

        SellerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BiddingDetailItemActivity.this, SellerInfoActivity.class);
                intent.putExtra("sellerId", sellerName);
                startActivity(intent);
            }
        });

        // 낙찰 포기 클릭 시
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String canc = intent.getStringExtra("cancel");
                // 팝업 창 띄워서 확인 눌렀을 때 낙찰 취소되도록
                AlertDialog.Builder builder = new AlertDialog.Builder(BiddingDetailItemActivity.this);
                builder.setTitle("낙찰을 취소 하시겠습니까?"); // 다이얼로그 제목
                builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
                builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 취소 횟수 3회 이상이면 X
                        db.collection("User").whereEqualTo("email", userEmail)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : (task.getResult())) {
                                            Integer cancleCnt = Integer.parseInt(String.valueOf(document.getData().get("cancleCnt")));
                                            if (cancleCnt > 3) {
                                                Toast.makeText(BiddingDetailItemActivity.this, "이미 낙찰 포기 횟수를 초과했습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // 낙찰자 위임하는 코드
                                                // Bids에서 해당 유저의 문서를 지우면 될듯
                                                DocumentReference auctionDocRef = db.collection("BiddingAuctionItems").document(title + seller);
                                                DocumentReference delCurrentUser = auctionDocRef.collection("Bids").document(uid);
                                                delCurrentUser.delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // 삭제 성공 시 처리
                                                                    DocumentReference auctionDocRef = db.collection("BiddingAuctionItems").document(title + seller);
                                                                    // 취소 횟수 카운팅 ( +1 )
                                                                    db.collection("User").document(uid).update("cancleCnt", cancleCnt + 1);
                                                                    sendAuctionFCM();
                                                                    // 유찰을 위한 취소 횟수 카운팅
                                                                    Integer cancel = Integer.parseInt(String.valueOf(canc));
                                                                    db.collection("BiddingItem").document(title+seller).update("cancel",String.valueOf(cancel+1));
                                                                    if(cancel>= 3){
                                                                        sendFCMToUsersForItem(title, seller, id);
                                                                        String documentId = title + seller;
//
                                                                        DocumentReference delCurrentUser = db.collection("BiddingItem").document(documentId);
                                                                        delCurrentUser.delete();
                                                                    } else {
                                                                        Log.d(TAG, "유찰까지 취소 횟수 남음");
                                                                    }
                                                                    Toast.makeText( BiddingDetailItemActivity.this, "낙찰을 포기했습니다.", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    // 삭제 실패 시 처리
                                                                    Toast.makeText(BiddingDetailItemActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
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

        mBtnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReportDialog();
            }
        });
        mBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BiddingDetailItemActivity.this, BiddingActivity.class);

                startActivity(intent);
            }
        });

//        // 채팅 버튼 클릭 시 이벤트
//        btnChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent chat = new Intent(BiddingDetailItemActivity.this, ChatActivity.class);
//                chat.putExtra("documentId", title+seller);
//                chat.putExtra("seller", seller);
//                chat.putExtra("buyer", buyerUid); // buyer의 uid 전달
//                chat.putExtra("endPrice", endPrice);
//                chat.putExtra("id", id);
//                chat.putExtra("bidType", "bidding");
//
//                startActivity(chat);
//            }
//        });

        // 구매 버튼 클릭 시 이벤트
        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 문서의 참조 가져오기
                DocumentReference docRef = db.collection("BiddingItem").document(documentId);

                Map<String, Object> updates = new HashMap<>();
                updates.put("confirm", true);
                // 문서 수정
                docRef.update(updates);
                // 수정된 내용 갱신
                UserDataHolderBiddingItems.loadBiddingItems();
                Toast.makeText(BiddingDetailItemActivity.this, "구매가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BiddingDetailItemActivity.this, BiddingActivity.class));
                finish();
            }
        });
        mBtnBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BiddingDetailItemActivity.this, LargeImageActivity.class);
                intent.putExtra("images", imageUrls); // 이미지 URL 배열 전달
                startActivity(intent);
            }
        });
        // 입찰하기 버튼을 눌렀을 때 -> dialog 로 입력받을 수 있는 팝업창 띄움
        mBtnBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidPopupLayout.setVisibility(View.VISIBLE);
            }
        });
        Button confirmBidButton = findViewById(R.id.confirmBidButton);
        Button cancelBidButton = findViewById(R.id.cancelBidButton);
// 만약 판매자의 이메일과 현재 로그인한 사용자의 이메일이 같다면, 신청하기 버튼을 숨김
        if (seller.equals(userEmail)) {
            mBtnBidButton.setVisibility(View.GONE);
        } else {
            mBtnBidButton.setVisibility(View.VISIBLE);
        }
        mBtnBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidPopupLayout.setVisibility(View.VISIBLE);
            }
        });

        confirmBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = getIntent().getStringExtra("id");
                bidAmount = bidText.getText().toString();
                if (bidAmount.equals("") || bidAmount.equals((null))) {
                    Toast.makeText(BiddingDetailItemActivity.this, "값을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!bidAmount.isEmpty()) {
                        // 컬렉션만들고 로직만들자리
                        long userBidAmount = Long.parseLong(bidAmount);
                        long currentHighestBid = Long.parseLong(startPrice.getText().toString());


                        if (userBidAmount > currentHighestBid) {
                            // 비딩금액이 컬렉션에 저장된금액보다 크면 데이터베이스 업데이트
                            updateBidData(String.valueOf(userBidAmount));
                            // 경매타이머 설정후에 performAuctionEnd()메소드로 낙찰기능
                            //performAuctionEnd(itemId);
                        } else {
//                        Map<String, Object> data = new HashMap<>();
//                        data.put("buyer", buyer);
//
//                        DocumentReference userDocRef = db.collection("BiddingItem").document(document);
//                        userDocRef.update(data)
//                                .addOnSuccessListener(aVoid -> {
//                                    // 등록된 리스트 새로 갱신
//                                    UserDataHolderBiddingItems.loadBiddingItems();
//                                    Toast.makeText(BiddingDetailItemActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
//                                })
//                                .addOnFailureListener(e -> {
//                                    Toast.makeText(getApplicationContext(), "종료 시간 업데이트에 실패했습니다." + e.getMessage(),
//                                            Toast.LENGTH_SHORT).show();
//                                });
                            Toast.makeText(BiddingDetailItemActivity.this, "시작 가격보다 높은 금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
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
    private void showReportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("신고하기");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_report2, null);
        builder.setView(dialogView);
        Item item = new Item();
        builder.setPositiveButton("신고", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String userEmail = sellerr;

                updateReports(userEmail);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 취소 버튼을 눌렀을 때 실행할 동작을 여기에 추가
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updateReports(String email) {
        // 사용자의 이메일을 기반으로 문서를 찾아옴
        db.collection("User")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference userRef = document.getReference();

                            db.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot userSnapshot = transaction.get(userRef);
                                    if (userSnapshot.exists()) {
                                        long currentReports = userSnapshot.getLong("reports");
                                        transaction.update(userRef, "reports", currentReports + 1);
                                    }
                                    return null;
                                }
                            });
                        }
                    } else {
                        // 처리 실패 시 동작
                    }
                });
    }
    private void updateEndTime(String documentId) {
        // 현재 시간을 기준으로 종료 시간 업데이트
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 30); // 1분 후 종료 시간으로 설정

        String futureMillis = String.valueOf(calendar.getTimeInMillis());

        // "yyyy-MM-dd HH:mm:ss" 포맷으로 변환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(calendar.getTime());

        Map<String, Object> data = new HashMap<>();
        data.put("futureMillis", futureMillis);
        data.put("futureDate", formattedDate);

        DocumentReference userDocRef = db.collection("BiddingItem").document(documentId);
        userDocRef.update(data)
                .addOnSuccessListener(aVoid -> {
                    // 등록된 리스트 새로 갱신
                    UserDataHolderBiddingItems.loadBiddingItems();
                    Toast.makeText(BiddingDetailItemActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "종료 시간 업데이트에 실패했습니다." + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void setbValues(Item selectedItem) {
        itemTitle.setText(selectedItem.getTitle());
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        double price = Double.parseDouble(selectedItem.getPrice());
        String formattedPrice = decimalFormat.format(price);
        startPrice.setText(formattedPrice);
//        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        timeinfo.setText(selectedItem.getFutureDate());

        String title = selectedItem.getTitle();
        String sellerName = selectedItem.getSeller();

        String documentId1 = title + sellerName;

        db.collection("BiddingItem").document(documentId1).update("views", selectedItem.getViews() + 1);

        this.document = documentId1;

        long currentTimeMillis = System.currentTimeMillis();
        String aa = selectedItem.getFutureMillis();
        Long futureMillisValue = Long.valueOf(aa); // 형변환
        remainingTimeMillis = futureMillisValue - currentTimeMillis;
        String remainingTime = formatRemainingTime(remainingTimeMillis);
        if (remainingTimeMillis <= 0) {
            bidend();
            performAuctionEnd(document);
//            highhigh(db.collection("BiddingAuctionItems")
//                    .document(document)
//                    .collection("Bids"));
        }
        futureMillis.setText(remainingTime);

        startCountdownTimer(remainingTimeMillis);
    }

    private void bidend() {
        mBtnBidButton.setVisibility(View.GONE);
        mBtnBidEnd.setVisibility(View.VISIBLE);
    }

    private void highhigh(CollectionReference bidsCollectionRef) {
        bidsCollectionRef.orderBy("비딩금액", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        QueryDocumentSnapshot highestBidDoc = (QueryDocumentSnapshot) value.getDocuments().get(0);
                        Map<String, Object> bidData = highestBidDoc.getData();
                        String highestBidPriceStr = (String) bidData.get("비딩금액");
                        String highestBidderId = (String) bidData.get("사용자아이디");

                        if (highestBidPriceStr != null) {
                            try {
                                int highestBidPrice = Integer.parseInt(highestBidPriceStr);
                                double price = Double.parseDouble(String.valueOf(highestBidPrice));
                                String formattedPrice = decimalFormat.format(price);
                                endPrice.setText(formattedPrice);
                                if (highestBidderId != null) {
                                    // 최고 입찰자 ID를 endPrice 아래에 표시 (선택 사항)
                                    buyer = highestBidderId;
                                    updateUserinfo(bidAmount);
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
                bidend();
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
        Item selectedItem = null;
        for (Item item : UserDataHolderBiddingItems.biddingItemList) {
            if (documentId.equals(item.getTitle() + item.getSeller())) {
                selectedItem = item;
                break;
            }
        }
        if (selectedItem != null) {
            // selectedItem 사용하기
            setbValues(selectedItem);

        } else {
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }

    private void performAuctionEnd(String auctionItemId) {
        String itemId = getIntent().getStringExtra("id");

        String seller = sellerName;
        String Itemdocument = itemId + seller;

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
                        String winningBidAmount = highestBidDoc.getString("비딩금액");


                        Map<String, Object> updateData = new HashMap<>();
                        updateData.put("경매 정보", document);

                        updateData.put("buyer", winningUserId);
                        updateData.put("price", winningBidAmount);

                        db.collection("BiddingItem").document(document)
                                .update(updateData)
                                .addOnSuccessListener(aVoid -> {
                                    db.collection("User")
                                            .document(winningUserId)
                                            .get()
                                            .addOnSuccessListener(userDocument -> {
                                                if (userDocument.exists()) {
                                                    String winningUserEmail = userDocument.getString("email");
                                                    Toast.makeText(BiddingDetailItemActivity.this,
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


                                    // 업데이트 성공 시의 처리
//                                    Toast.makeText(BiddingDetailItemActivity.this, itemId+" 경매가 종료되었습니다.\n낙찰자: " + winningUserId + "\n입찰금액: " + winningBidAmount, Toast.LENGTH_LONG).show();
                                    // remainingTimeMillis 값이 음수인 경우 입찰하기 버튼 비활성화
                                    if (remainingTimeMillis <= 0) {
                                        mBtnBidButton.setEnabled(false);
                                        mBtnBidButton.setText("경매 종료");
                                        sendAuctionFCM();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    // 업데이트 실패 시의 처리
                                    Toast.makeText(BiddingDetailItemActivity.this,
                                            "낙찰자를 결정하는 중에 오류가 발생했습니다.",
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, Itemdocument);
                                });
                    } else {
                        Toast.makeText(BiddingDetailItemActivity.this,
                                "낙찰자를 결정하는 중에 오류가 발생했습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateBidData(String newBidAmount) {
        String itemId = getIntent().getStringExtra("id");
        String seller = sellerName;
        String Itemdocument = itemId + seller;

        String userId = UserManager.getInstance().getUserUid();

        String documentId = getIntent().getStringExtra("documentId"); // 현재 아이템의 ID 가져오기

        // BiddingAuctionProgress 컬렉션 안에 itemId로 문서 생성 또는 업데이트
        Map<String, Object> bidData = new HashMap<>();
        bidData.put("경매 정보", this.document);
        bidData.put("비딩금액", String.valueOf(newBidAmount));
        bidData.put("사용자아이디", userId);

        db.collection("BiddingAuctionItems").document(documentId)

                .collection("Bids")
                .document(userId) // 사용자 ID로 문서 생성
                .set(bidData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(BiddingDetailItemActivity.this,
                            "입찰이 성공적으로 처리되었습니다.",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BiddingDetailItemActivity.this,
                            "입찰을 처리하는 중에 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserinfo(String bidPrice) {
        String uid = UserManager.getInstance().getUserUid();
        DocumentReference userDocRef = db.collection("User").document(uid);
        DocumentReference auctionDocRef = db.collection("BiddingAuctionItems").document(document);

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
                                                    highhigh(auctionDocRef.collection("Bids"));
                                                });
                                        updateEndTime(this.document);
                                    } else if (newBidPrice == existingBidPrice) {
                                        Toast.makeText(BiddingDetailItemActivity.this, "전 입찰가와 같은 금액을 입력하셨습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();

                                    }
                                    else if(bidPrice.isEmpty()){
                                        Toast.makeText(BiddingDetailItemActivity.this, "가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(BiddingDetailItemActivity.this, "전 입찰가보다 높은 가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (NumberFormatException e) {
                                    Toast.makeText(BiddingDetailItemActivity.this, "가격을 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                                        highhigh(auctionDocRef.collection("Bids"));
                                    });
                        }
                    }
                });

    }
    private void sendAuctionFCM() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(calendar.getTime());

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String seller = intent.getStringExtra("seller");
        String item = intent.getStringExtra("id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("BiddingAuctionItems")
                .document(title + seller)
                .collection("Bids")
                .orderBy("비딩금액", Query.Direction.DESCENDING) // "입찰 가격" 필드를 내림차순으로 정렬
                .limit(1) // 최상위 1개 문서만 가져오기
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot highestBidDocument = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0); // 가장 높은 입찰 가격의 문서
                        String documentId = highestBidDocument.getId();

                        db.collection("FCMTOKEN")
                                .document(documentId)
                                .get()
                                .addOnSuccessListener(documentSnapshot1 -> {
                                    if (documentSnapshot1.exists()) {
                                        String userToken = documentSnapshot1.getString("token");
                                        if (userToken != null) {
                                            Map<String, String> data = new HashMap<>();
                                            data.put("title", "경매 결과 알림");
                                            data.put("message", "고객님이 참여하신 " + item + " 경매에 낙찰 되셨습니다. 구매 확정 해주세요.");
                                            data.put("uid", userToken);
                                            data.put("time", formattedDate);
                                            data.put("userId", documentId);
                                            data.put("itemTitle", title);
                                            data.put("itemType", "BiddingItem");

                                            db.collection("notifications")
                                                    .add(data)
                                                    .addOnSuccessListener(aVoid -> {
                                                        Log.d("FCM", "메시지 저장 성공: " + documentId);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Log.e("FCM", "메시지 저장 실패: " + e.getMessage());
                                                    });
                                        } else {
                                            Log.d("FCM", "해당 사용자의 FCM 토큰이 없습니다.");
                                        }
                                    }
                                });
                    } else {
                        Log.d("Firestore", "Bids 컬렉션에 문서가 없습니다.");
                    }
                });
    }
    private void sendFCMToUsersForItem(String title, String seller, String itemId) {
        Calendar calendar = Calendar.getInstance(); // 현재 시간 가져옴
        String millis = String.valueOf(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(calendar.getTime());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .whereEqualTo("email", seller)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // 해당 판매자의 문서 ID 가져오기
                            String documentId = documentSnapshot.getId();
                            // documentId를 사용하여 원하는 작업 수행
                            Log.d(TAG, "Document ID: " + documentId);

                            db.collection("FCMTOKEN").document(documentId).get()
                                    .addOnSuccessListener(documentSnapshot2 -> {
                                        if (documentSnapshot2.exists()) {
                                            String userToken = documentSnapshot2.getString("token");// 로그인시에 저장되는 FCM토큰
                                            if (userToken != null) {
                                                // FCM 메시지 생성
                                                Map<String, String> messageData = new HashMap<>();
                                                messageData.put("title", "경매 유찰 알림");
                                                messageData.put("body", seller+"님 의 "+itemId+ "이(가) 유찰됐습니다.");
                                                Map<String, String> data = new HashMap<>();
                                                data.put("title", messageData.get("title"));
                                                data.put("message", messageData.get("body"));
                                                data.put("uid", userToken);
                                                data.put("time", formattedDate);
                                                data.put("userId",documentId);
                                                data.put("itemTitle", title);
                                                data.put("itemType", "BiddingItem");

                                                db.collection("notifications")
                                                        .add(data)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d("FCM", "메시지 저장 성공: " + documentId);
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.e("FCM", "메시지 저장 실패: " + e.getMessage());
                                                        });
                                            } else {
                                                Log.d("FCM", "해당 사용자의 FCM 토큰이 없습니다.");
                                            }
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 오류 처리
                        Log.w(TAG, "Error getting documents", e);
                    }
                });
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(BiddingDetailItemActivity.this, BiddingActivity.class));
        finish();
    }
}
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
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OpenDetailItemActivity extends AppCompatActivity {
    private long remainingTimeMillis;
    private CountDownTimer countDownTimer;
    private TextView itmeTitle, itemId, startPrice, endPrice, itemInfo, seller, category, timeinfo, futureMillis;

    private EditText mETBidPrice;
    private ViewPager2 sliderViewPager;
    private Button btnCancle, btnChat, mBtnBidButton, mBtnBuy, mBtnConfirm, mBtnBidEnd, mBtnBigButton, mBtnreport, mBtnback;

    String document, buyer, highPrice, buyerId, sellerr, upfutureMillis;
    private String[] imageUrls;
    boolean open;
    private boolean first = true;
    private DecimalFormat decimalFormat;

    private boolean highestBidUpdated = false;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    String strEndPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_detail_item);
        sliderViewPager = findViewById(R.id.sliderViewPager);

        db = FirebaseFirestore.getInstance();

        sliderViewPager.setOffscreenPageLimit(1);

        itmeTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        decimalFormat = new DecimalFormat("#,###");
        mBtnback = findViewById(R.id.btn_back);
        strEndPrice = null;

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String sellerEmail = intent.getStringExtra("seller");
        String buyerUid = intent.getStringExtra("buyer");
        String confirm = intent.getStringExtra("confirm");
        String seller = intent.getStringExtra("seller");


        ImageView SellerInfo = findViewById(R.id.iv_profile); // 판매자정보

        // User 컬렉션에서 판매자 정보 가져오기
        db.collection("User")
                .whereEqualTo("email", sellerEmail)
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

        category = findViewById(R.id.category);
        timeinfo = findViewById(R.id.endTime);
        futureMillis = findViewById(R.id.futureMillis);
        remainingTimeMillis = 0;
        highPrice = "0";

        open = false;

        buyer = " ";
        buyerId = " ";
        mBtnreport = findViewById(R.id.btn_report); //신고버튼

        mBtnBidButton = findViewById(R.id.btn_bidbutton);
        mBtnBigButton = findViewById(R.id.btn_bigbutton);
        mBtnBidEnd = findViewById(R.id.btn_bidEnd);
        mBtnBuy = findViewById(R.id.btn_buy);
        mBtnConfirm = findViewById(R.id.btn_buyConfirm);
        mETBidPrice = findViewById(R.id.et_bid); // 경매 가격 받기
        btnChat = findViewById(R.id.btn_chat);
        btnCancle = findViewById(R.id.btn_cancle);

        String documentId = title + sellerEmail;
        this.document = documentId;
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("OpenItem").document(documentId);
        String uid = UserManager.getInstance().getUserUid(); // 현재 유저의 uid
        String userEmail = UserManager.getInstance().getUserEmail(); // 현재 유저의 email
        getSelectedItem();
        imageUrls = getIntent().getStringArrayExtra("imageUrls");

        // ViewPager2 어댑터를 설정합니다.
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);
        sliderViewPager.setAdapter(imageSliderAdapter);

        sliderViewPager.setAdapter(new ImageSliderAdapter(this, imageUrls));
        sliderViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenDetailItemActivity.this, LargeImageActivity.class);
                intent.putExtra("images", imageUrls); // 이미지 URL 배열 전달
                startActivity(intent);
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
//                                        Toast.makeText(OpenDetailItemActivity.this, "종료 가격 설정 완료", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
//                                        Toast.makeText(getApplicationContext(), "종료 가격 설정 실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
//                                        Toast.makeText(OpenDetailItemActivity.this, "종료 가격 설정 완료", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
//                                        Toast.makeText(getApplicationContext(), "종료 가격 설정 실패" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                        if(open){
                            String buyer = document.getString("buyer");
                            strEndPrice = document.getString("endPrice");

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

        // 판매자 정보를 눌렀을 때
        SellerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenDetailItemActivity.this, SellerInfoActivity.class);
                intent.putExtra("sellerId",seller);
                startActivity(intent);
            }
        });

        Calendar calendar = Calendar.getInstance();
        Long nowMillis = calendar.getTimeInMillis();
        Long futureMillis = Long.valueOf(intent.getStringExtra("futureMillis"));
        Long isEnd = futureMillis - nowMillis;
        if(buyerUid.equals(uid) && confirm.equals("true")){
            Log.d(TAG, "uid값 : "+uid+"confirm 값 : "+ confirm);
            // 구매 완료 버튼만 보이게
            mBtnConfirm.setVisibility(View.VISIBLE);
            mBtnBidButton.setVisibility(View.GONE);
            btnChat.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);
            // 낙찰자가 '나'지만 구매가 완료되지 않은 경우
        }else if(buyerUid.equals(uid) && confirm.equals("false") && isEnd <= 0){
            // 구매 버튼, 채팅 버튼 보이게
            mBtnBuy.setVisibility(View.VISIBLE);
            btnChat.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);

            // 판매자가 '나'고 경매가 끝났지만 구매가 완료되지 않은 경우
        }else if(userEmail.equals(sellerEmail) && confirm.equals("false") && isEnd <= 0) {
            // 구매 버튼, 채팅 버튼 보이게
            mBtnBuy.setVisibility(View.GONE);
            btnChat.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);

            // 누군가 구매를 해서 경매가 종료되었을 때
        }else if(confirm.equals("true") && isEnd <= 0){
            mBtnBidEnd.setVisibility(View.VISIBLE);
            mBtnBuy.setVisibility(View.GONE);
            btnChat.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.GONE);
            mBtnBidButton.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);
            mETBidPrice.setVisibility(View.GONE);


        } else if(userEmail.equals(seller)){
            mBtnBidButton.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            btnChat.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);
            mETBidPrice.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);

        }else{
            // 경매중인 경우
            // 입찰 버튼만 보이게
            mBtnBidButton.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
            btnChat.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.GONE);
            mBtnBidEnd.setVisibility(View.GONE);
            btnCancle.setVisibility(View.GONE);
        }
        // 구매 버튼 클릭 시 이벤트
        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 문서의 참조 가져오기
                DocumentReference docRef = db.collection("OpenItem").document(documentId);

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
        mBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenDetailItemActivity.this, OpenAuctionActivity.class);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(OpenDetailItemActivity.this);
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
                                                Toast.makeText(OpenDetailItemActivity.this, "이미 낙찰 포기 횟수를 초과했습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // 낙찰자 위임하는 코드
                                                // Bids에서 해당 유저의 문서를 지우면 될듯
                                                DocumentReference auctionDocRef = db.collection("OpenAuctionInProgress").document(title + seller);
                                                DocumentReference delCurrentUser = auctionDocRef.collection("Bids").document(uid);
                                                delCurrentUser.delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // 삭제 성공 시 처리
                                                                    DocumentReference auctionDocRef = db.collection("OpenAuctionInProgress").document(title + seller);
                                                                    updateHighestBidListener(auctionDocRef.collection("Bids"));
                                                                    // 취소 횟수 카운팅 ( +1 )
                                                                    db.collection("User").document(uid).update("cancleCnt", cancleCnt + 1);
                                                                    sendAuctionFCM();
                                                                    // 유찰을 위한 취소 횟수 카운팅
                                                                    Integer cancel = Integer.parseInt(String.valueOf(canc));
                                                                    db.collection("OpenItem").document(title+seller).update("cancel",String.valueOf(cancel+1));
                                                                    if(cancel>= 3){
                                                                        sendFCMToUsersForItem(title, seller, id);
                                                                        String documentId = title + seller;
//
                                                                        DocumentReference delCurrentUser = db.collection("OpenItem").document(documentId);
                                                                        delCurrentUser.delete();
                                                                    } else {
                                                                        Log.d(TAG, "유찰까지 취소 횟수 남음");
                                                                    }
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

        mBtnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReportDialog();
            }
        });

        mBtnBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                mETBidPrice = findViewById(R.id.et_bid); // 경매 가격 받기
                String bidPrice = mETBidPrice.getText().toString(); // 경매 가격 초기화하기

                // 문서 읽기
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String price = document.getString("price");
                            if (price != null && !bidPrice.isEmpty()) {
                                int priceValue = Integer.parseInt(price);
                                int enterPrice = Integer.valueOf(bidPrice);
                                if (enterPrice < priceValue) {
                                    Toast.makeText(OpenDetailItemActivity.this, "시작 가격보다 높은 금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                } else if (enterPrice > 1000000000) {
                                    Toast.makeText(OpenDetailItemActivity.this, "10억원보다 낮은 금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
//                                                Toast.makeText(OpenDetailItemActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
//                                                Toast.makeText(getApplicationContext(), "종료 시간 업데이트에 실패했습니다." + e.getMessage(),
//                                                        Toast.LENGTH_SHORT).show();
                                            });
                                }
                            } else {
                                // price 필드가 null인 경우
                                Toast.makeText(OpenDetailItemActivity.this, "가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
        // 채팅 버튼 클릭 시 이벤트
        btnChat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent chat = new Intent(OpenDetailItemActivity.this, ChatActivity.class);
                chat.putExtra("seller", seller);
                chat.putExtra("buyer", buyerUid); // buyer의 uid 전달
                chat.putExtra("endPrice", strEndPrice);
                chat.putExtra("id", id);
                chat.putExtra("bidType","open");
                chat.putExtra("documentId", title+seller);
                startActivity(chat);
            }
        });

        mBtnBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenDetailItemActivity.this, LargeImageActivity.class);
                intent.putExtra("images", imageUrls); // 이미지 URL 배열 전달
                startActivity(intent);
            }
        });

        // 이하 내용은 코드 조각입니다. (updateEndTime 및 getEndTime 메소드 호출 등)
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
        calendar.add(Calendar.MINUTE, 5); // 1분 후 종료 시간으로 설정

        upfutureMillis = String.valueOf(calendar.getTimeInMillis());

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
//                    Toast.makeText(OpenDetailItemActivity.this, "종료 시간이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
//                    Toast.makeText(getApplicationContext(), "종료 시간 업데이트에 실패했습니다." + e.getMessage(),
//                            Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(getApplicationContext(), "데이터 가져오기에 실패했습니다." + e.getMessage(),
//                            Toast.LENGTH_SHORT).show();
                });
    }

    private void setItemValues(Item selectedItem) {
        double price = Double.parseDouble(String.valueOf(selectedItem.getPrice()));
        String formattedPrice = decimalFormat.format(price);
        UserDataHolderOpenItems.loadOpenItems();
        db.collection("OpenAuctionInProgress")
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
                    startPrice.setText(formattedPrice);
                    endPrice.setText(String.valueOf(highestBid));
                    seller.setText(selectedItem.getSeller());
                    sellerr = selectedItem.getSeller();
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
            boolean first = true;
            @Override
            public void onFinish() {
                if(first){
                    sendAuctionFCM();
                    first = false;
                }
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

        } else {
            // 해당 id와 일치하는 아이템이 없는 경우
        }

        // 최고가 업데이트를 위해 호출
        if (selectedItem != null) {
            updateHighestBidListener(db.collection("OpenAuctionInProgress")
                    .document(document)
                    .collection("Bids"));
            updatetimerListener(db.collection("OpenItem"));

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
                                double price = Double.parseDouble(String.valueOf(highestBidPrice));
                                String formattedPrice = decimalFormat.format(price);
                                endPrice.setText(formattedPrice);
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

    private void updatetimerListener(CollectionReference openItem) {
        openItem.orderBy("futureMillis", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (value != null && !value.isEmpty()) {
                        QueryDocumentSnapshot highestBidDoc = (QueryDocumentSnapshot) value.getDocuments().get(0);
                        Map<String, Object> bidData = highestBidDoc.getData();
                        upfutureMillis = (String) bidData.get("futureMillis");
                        futureMillis.setText(upfutureMillis);
                        DocumentReference userDocRef = db.collection("OpenItem").document(this.document);

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
                                            if (first) {
                                                first = false;
                                            } else {
//                                                Toast.makeText(OpenDetailItemActivity.this, "낙찰을 위해서 서두르세요!", Toast.LENGTH_SHORT).show();
                                            }
                                            startCountdownTimer(futureTimeMillis, 1000);
                                            // 기존 시간 텍스트를 숨김 처리
                                            //futureMillis.setVisibility(View.GONE);

                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
//                                    Toast.makeText(getApplicationContext(), "데이터 가져오기에 실패했습니다." + e.getMessage(),
//                                            Toast.LENGTH_SHORT).show();
                                });


                    } else {
                        futureMillis.setText(upfutureMillis);
                        DocumentReference userDocRef = db.collection("OpenItem").document(this.document);

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
//                                    Toast.makeText(getApplicationContext(), "데이터 가져오기에 실패했습니다." + e.getMessage(),
//                                            Toast.LENGTH_SHORT).show();
                                });
                    }

                });
    }

    private void updateUserinfo(String bidPrice) {
        String uid = UserManager.getInstance().getUserUid();
        DocumentReference userDocRef = db.collection("User").document(uid);
        DocumentReference auctionDocRef = db.collection("OpenAuctionInProgress").document(document);
        DocumentReference openItemDocRef = db.collection("OpenItem").document(document);

        openItemDocRef.get().addOnCompleteListener(openItemTask -> {
            if (openItemTask.isSuccessful()) {
                DocumentSnapshot openItemDocument = openItemTask.getResult();
                if (openItemDocument.exists()) {
                    String buyerUid = (String) openItemDocument.get("buyer");
                    if (buyerUid != null && buyerUid.equals(uid)) { // 입찰하는 사람이 이미 최고 입찰자인 경우 메세지 표시
                        Toast.makeText(OpenDetailItemActivity.this, "이미 최고 입찰자입니다.", Toast.LENGTH_SHORT).show();
                    } else { // 입찰하는 사람이 최고 입찰자가 아닌 경우
//                        performBid(userDocRef, auctionDocRef, bidPrice);


//                        String uid = UserManager.getInstance().getUserUid();
                        DocumentReference openDocRef = db.collection("OpenItem").document(document);

                        auctionDocRef.collection("Bids").document(uid)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();

                                        if (document.exists()) { // 여기가 안됨 왜?
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
                                                                    updateEndTime(this.document);
                                                                });

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
                                            bidData.put("경매 정보", this.document);

                                            auctionDocRef.collection("Bids").document(uid)
                                                    .set(bidData)
                                                    .addOnSuccessListener(aVoid -> {
                                                        updateEndTime(this.document);

                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // 실패한 경우에 대한 처리를 여기에 작성하세요.

                                                    });

                                        }
                                    }
                                });
                        updateHighestBidListener(auctionDocRef.collection("Bids"));

                        updatetimerListener(openDocRef.collection(this.document)); // 얘도 안돼


                    }
                }
            }
        });
    }

    private void performBid(DocumentReference userDocRef, DocumentReference auctionDocRef, String bidPrice) {

    }

    //    private void sendFCMToUsersForItem() {
//        Calendar calendar = Calendar.getInstance(); // 현재 시간 가져옴
//        String millis = String.valueOf(calendar.getTimeInMillis());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = sdf.format(calendar.getTime());
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("User")
//                .whereEqualTo("email", seller)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                            // 해당 판매자의 문서 ID 가져오기
//                            String documentId = documentSnapshot.getId();
//                            // documentId를 사용하여 원하는 작업 수행
//                            Log.d(TAG, "Document ID: " + documentId);
//
//                            db.collection("FCMTOKEN").document(documentId).get()
//                                    .addOnSuccessListener(documentSnapshot1 -> {
//                                        if (documentSnapshot1.exists()) {
//                                            String userToken = documentSnapshot1.getString("token");// 로그인시에 저장되는 FCM토큰
//                                            if (userToken != null) {
//                                                // FCM 메시지 생성
//                                                Map<String, String> messageData = new HashMap<>();
//                                                messageData.put("title", category + " 경매알림");
//                                                messageData.put("body", seller+"님 의 "+itemId+ "이(가) 유찰됐습니다.");
//                                                Map<String, String> data = new HashMap<>();
//                                                data.put("title", messageData.get("title"));
//                                                data.put("message", messageData.get("body"));
//                                                data.put("time", formattedDate);
//                                                data.put("userId",documentId);
//                                                data.put("itemType", "OpenItem");
//
//                                                db.collection("notifications")
//                                                        .add(data)
//                                                        .addOnSuccessListener(aVoid -> {
//                                                            Log.d("FCM", "메시지 저장 성공: " + documentId);
//                                                        })
//                                                        .addOnFailureListener(e -> {
//                                                            Log.e("FCM", "메시지 저장 실패: " + e.getMessage());
//                                                        });
//                                            } else {
//                                                Log.d("FCM", "해당 사용자의 FCM 토큰이 없습니다.");
//                                            }
//                                        }
//                                    });
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // 오류 처리
//                        Log.w(TAG, "Error getting documents", e);
//                    }
//                });
//    }
    private void sendAuctionFCM() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(calendar.getTime());

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String seller = intent.getStringExtra("seller");
        String item = intent.getStringExtra("id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OpenAuctionInProgress")
                .document(title + seller)
                .collection("Bids")
                .orderBy("입찰 가격", Query.Direction.DESCENDING) // "입찰 가격" 필드를 내림차순으로 정렬
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
                                            data.put("itemType", "OpenItem");

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
                                            data.put("itemType", "OpenItem");

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
    public void onBackPressed(){
//        startActivity(new Intent(OpenDetailItemActivity.this, OpenAuctionActivity.class));
        finish();
    }
}
package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<ChatData> chatList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private FirebaseFirestore db;
    private EditText EditText_chat;
    private Button Button_buyer, Button_seller;
    private ImageButton Button_send, Button_pay;
    private DatabaseReference myRef;
    private String buyerEmail = "";
    private Boolean confirm = false;
    private Boolean buyerConfirm = false;
    private String score = "";
    String newScore = "";
    private String originScore = "";
    private String itemImg = "";
    private String sellerImg = "";
    private String sellerUid = "";
    private String nickname = "";
    private String sellerNickname = "";
    private String buyerNickname = "";
    private ChatRoom chatRoom = new ChatRoom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String seller = intent.getStringExtra("seller");
        String id = intent.getStringExtra("id");
        String endPrice = intent.getStringExtra("endPrice");
        int intEndPrice = Integer.valueOf(endPrice);
        double doubleEndPrice = Double.valueOf(intEndPrice);


        String bidType = intent.getStringExtra("bidType");
        String buyerUid = intent.getStringExtra("buyer");
        Log.d(TAG, "" + buyerUid);
        String documentId = intent.getStringExtra("documentId");
        String userUid = UserManager.getInstance().getUserUid();
        String userEmail = UserManager.getInstance().getUserEmail();
        String futureMillis = intent.getStringExtra("futureMillis");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        EditText_chat = findViewById(R.id.EditText_chat);
        Button_send = findViewById(R.id.Button_send);
        Button_pay = findViewById(R.id.Button_pay);
        Button_buyer = findViewById(R.id.btn_buyer);
        Button_seller = findViewById(R.id.btn_seller);

        setBuyerConfirm(bidType, documentId);
        setButtonVisibility(bidType, documentId);


//        Log.d(TAG, "seller의 documentId : "+sellerUid);
//        getNickname(sellerUid, buyerUid); // sellerNickname, buyerNickname 설정됨


        // buyerEmail 구해보자
        db.collection("User").document(buyerUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                buyerEmail = document.getString("email");
                                Log.d(TAG, "buyerEmail : " + buyerEmail);
                                // 여기에서 fieldValue 변수를 사용하세요.
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        // buyer의 nickname을 구해보자
        db.collection("User").document(buyerUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                buyerNickname = document.getString("nickname");
                                Log.d(TAG, " onCreate 에서의 buyerNickname : " + buyerNickname);
                                // 여기에서 fieldValue 변수를 사용하세요.
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        Query query = db.collection("User").whereEqualTo("email", seller);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    sellerUid = document.getId();
                    Log.d(TAG, "seller의 documentId : " + sellerUid);

                    db.collection("User").document(sellerUid).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            sellerNickname = document.getString("nickname");
                                            // 현재 유저가 buyer일때
                                            if (userUid.equals(buyerUid)) {
                                                mAdapter = new ChatAdapter(chatList, ChatActivity.this, buyerNickname, sellerNickname);
                                            } else {
                                                // 현재 유저가 seller일때
                                                mAdapter = new ChatAdapter(chatList, ChatActivity.this, sellerNickname, buyerNickname);
                                            }
                                            mRecyclerView.setAdapter(mAdapter);
                                            Log.d(TAG, "sellerNickname : " + sellerNickname);
                                            // 여기에서 fieldValue 변수를 사용하세요.
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        // buyer 일때
        if (userUid.equals(buyerUid)) {
            // 수신자는 seller
            mAdapter = new ChatAdapter(chatList, ChatActivity.this, userEmail, documentId);
            Button_buyer.setVisibility(View.VISIBLE);
            Button_seller.setVisibility(View.GONE);

            // originScore 불러오기
            getScore(seller);
        }
        // seller 일때
        else {
            mAdapter = new ChatAdapter(chatList, ChatActivity.this, seller, documentId);
            Button_pay.setVisibility(View.GONE);
            Button_buyer.setVisibility(View.GONE);
            // 보이긴 하는데 buyer가 구매완료 눌러줘야 enable이 true가 돼서 클릭 가능함.
            Button_seller.setVisibility(View.VISIBLE);
            // originScore 불러오기
            getScore(buyerEmail);
            // 여기다가 Arrrpay true면 TextView로 buyer의 주소 띄워주기
        }

//        mRecyclerView.setAdapter(mAdapter);

        // buyer가 먼저 완료 눌러야함
        Button_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("구매 완료가 완료되었나요?"); // 다이얼로그 제목
                builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
                builder.setPositiveButton("완료", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (bidType.equals("bidding")) {
                            Log.d(TAG, "bidding일 때 buyerConfirm true로 바꿔줌");
                            db.collection("BiddingItem").document(documentId).update("buyerConfirm", true);
                            if (userUid.equals(buyerUid)) {
                                popup(seller);
                            } else {
                                popup(buyerEmail);
                            }


                        } else if (bidType.equals("open")) {
                            Log.d(TAG, "open일 때 buyerConfirm true로 바꿔줌");
                            db.collection("OpenItem").document(documentId).update("buyerConfirm", true);
                            if (userUid.equals(buyerUid)) {
                                popup(seller);
                            } else {
                                popup(buyerEmail);
                            }
                        }
                    }
                });
                builder.setNegativeButton("취소", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show(); // 다이얼로그 보이기
            }
        });

        // buyer가 완료한 뒤 seller가 완료 눌렀을 시
        Button_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("구매가 완료되었나요?"); // 다이얼로그 제목
                builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
                builder.setPositiveButton("완료", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (bidType.equals("bidding")) {
                            Log.d(TAG, "bidding일 때 confirm true로 바꿔줌");
                            db.collection("BiddingItem").document(documentId).update("confirm", true);
                        } else if (bidType.equals("open")) {
                            Log.d(TAG, "open일 때 confirm true로 바꿔줌");
                            db.collection("OpenItem").document(documentId).update("confirm", true);
                        }
                        Button_buyer.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        Button_buyer.setText("거래 완료");
                        Button_buyer.setEnabled(false);

                    }
                });
                builder.setNegativeButton("취소", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show(); // 다이얼로그 보이기
            }
        });

        // 전송 버튼 눌렀을 시
        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chatList를 못들고 오나본데
                String msgCount = String.valueOf(chatList.size());  // 여기서 size 들고와서 msgCount 라는 게 0, 1, 2 역할 해줄 줄 알았는데 계속 0임
                String msg = EditText_chat.getText().toString(); // msg
                if (msg != null) {
                    // 처음 보내는 메세지인 경우 -> 근데 지금 처음 보내고 나서 chatList.size()가 갱신이 안돼서
                    // 1로 안넘어가지는데 어케 해결해야할 지 모르겠음
                    // 아래에서 또 0~n까지 돌리면서 add해서 화면에 띄워줘야하는데 그것도 해야함
                    if (msgCount.equals("0")) {
                        chatRoom.setRoomId(buyerUid + futureMillis + endPrice);
                        // buyer인 경우 상대방은 seller
                        if (userUid.equals(buyerUid)) {
                            myRef.child(buyerUid + endPrice).child("dataSet").child("nickname").setValue(seller);

                            chatRoom.setNickname(seller);
                            // 아닌 경우에는 seller라는거니까 받는 대상이 buyer의 이메일이어야함
                        } else {
                            myRef.child(buyerUid + endPrice).child("dataSet").child("nickname").setValue(buyerEmail);

                            chatRoom.setNickname(buyerEmail);
                        }
                        chatRoom.setMyName(userEmail);
                        // 처음 받은 msg값으로 할당 해줌
                        chatRoom.setLastMessage(msg);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("myName").setValue(userEmail);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("bidType").setValue(bidType);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("seller").setValue(seller);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("buyer").setValue(buyerUid);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("endPrice").setValue(endPrice);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("documentId").setValue(documentId);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("id").setValue(id);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("futureMillis").setValue(futureMillis);


                        getItemImg(documentId, bidType, buyerUid, endPrice);
                        chatRoom.setItemImageUrl(itemImg);
                        getProfileImg(seller, buyerUid, endPrice);
                        chatRoom.setProfileImageUrl(sellerImg);
                        chatRoom.setBuyerUid(buyerUid);
                        myRef.child(buyerUid + endPrice).child("dataSet").child("buyerUid").setValue(buyerUid);
//                        myRef.child(buyerUid+endPrice).child("dataSet").push().setValue(chatRoom);
                    }
                    ChatData chat = new ChatData(); // 이건 채팅 데이터
                    String timestamp = String.valueOf(System.currentTimeMillis()); // 보낸시간
                    // 현재 유저가 buyer인 경우 상대방은 seller
                    if (userUid.equals(buyerUid)) {
                        addChat(seller, chat, timestamp, msg, buyerUid, endPrice, 0);

                        // 아닌 경우에는 seller라는거니까 받는 대상이 buyer의 이메일이어야함
                    } else {
                        addChat(seller, chat, timestamp, msg, buyerUid, endPrice, 1);
                    }
                    Log.d(TAG, "chat에서 가져온 name : " + chat.getName());
                    Log.d(TAG, "email : " + userEmail);


                    EditText_chat.setText(""); // 한번 전송버튼 누르면 reset

                }
            }
        });


        // ArrrPay 눌렀을 시
        Button_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pay = new Intent(ChatActivity.this, ArrrPayActivity.class);
                pay.putExtra("title", id);
                pay.putExtra("id", id);
                pay.putExtra("endPrice", doubleEndPrice);
                pay.putExtra("bidType", bidType);
                pay.putExtra("documentId", documentId);

                startActivity(pay);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 스크롤이 멈춘 경우 처리
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // 스크롤 중인 경우 처리
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    // 스크롤이 자연스럽게 멈추는 경우 처리
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 스크롤 중에 호출됩니다.
//                Log.d(TAG, "chatList의 size : "+chatList.size());
//                Log.d(TAG, "userUid : "+userUid);
                // 여기에 스크롤 중에 따른 작업을 추가하세요.
            }
        });

        myRef.child(buyerUid + endPrice).child("msgData").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // 여기서 데이터를 가져와 ChatData 객체로 변환
                ChatData chat = snapshot.getValue(ChatData.class);
                getChatData(seller, buyerUid, chat, userUid);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setBuyerConfirm(String bidType, String documentId) {
        if (bidType.equals("bidding")) {
            db.collection("BiddingItem").document(documentId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    buyerConfirm = document.getBoolean("buyerConfirm");
                                    if (buyerConfirm) {
                                        Button_seller.setEnabled(true);
                                        Button_buyer.setEnabled(false); // 이미 한 번 눌렀으면 못하게
                                    }
                                    // 여기에서 fieldValue 변수를 사용하세요.
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        } else if (bidType.equals("open")) {
            db.collection("OpenItem").document(documentId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    buyerConfirm = document.getBoolean("buyerConfirm");
                                    if (buyerConfirm) {
                                        Button_seller.setEnabled(true);
                                        Button_buyer.setEnabled(false); // 이미 한 번 눌렀으면 못하게
                                    }
                                    // 여기에서 fieldValue 변수를 사용하세요.
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }
    }

    public void setButtonVisibility(String bidType, String documentId) {
        if (bidType.equals("bidding")) {
            db.collection("BiddingItem").document(documentId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    confirm = document.getBoolean("confirm");
                                    if (confirm) {
                                        Button_seller.setEnabled(false);
                                        Button_seller.setText("거래 완료");
                                        Button_buyer.setEnabled(false);
                                        Button_buyer.setText("거래 완료");
                                    }
                                    // 여기에서 fieldValue 변수를 사용하세요.
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        } else if (bidType.equals("open")) {
            db.collection("OpenItem").document(documentId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    confirm = document.getBoolean("confirm");
                                    if (confirm) {
                                        Button_seller.setEnabled(false);
                                        Button_seller.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                        Button_seller.setText("거래 완료");
                                        Button_buyer.setEnabled(false);
                                        Button_buyer.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                        Button_buyer.setText("거래 완료");
                                    }
                                    // 여기에서 fieldValue 변수를 사용하세요.
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }
    }

    public void popup(String email) {
        Log.d(TAG, "popup이 떠야함");
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("거래자는 어땠나요 ?");
        alert.setMessage("0~100까지의 점수를 입력해주세요 !");

        final EditText input_score = new EditText(this);
        alert.setView(input_score);

        alert.setPositiveButton("입력 완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                score = input_score.getText().toString();
                Log.d(TAG, "" + score);

                if (score.equals("")) {
                    newScore = String.valueOf((Integer.parseInt(score) + Integer.parseInt(originScore)) / 2);
                } else {
                    newScore = originScore;
                }

                Log.d(TAG, "" + newScore);
                // getScore 응용해서 whereEqualTo로 email 받아와서 update 할 수 있을 듯
                db.collection("User").whereEqualTo("email", email).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : (task.getResult())) {
                                    DocumentReference docRef = db.collection("User").document(document.getId());
                                    docRef.update("score", newScore)
                                            .addOnSuccessListener(aVoid -> Log.d(TAG, "score 업데이트 성공"))
                                            .addOnFailureListener(e -> Log.w(TAG, "score 업데이트 실패", e));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                dialogInterface.dismiss();
            }
        });

        alert.setNegativeButton("입력 취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                score = "100"; // 이렇게 처리하는게 맞으려나 일단 보류
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    public void getScore(String email) {
        db.collection("User").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : (task.getResult())) {
                            originScore = document.getString("score");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // seller의 프로필 이미지 가져오기근데 필드는 있는거임? 값이 없고 있고 차이고?
    public void getProfileImg(String email, String buyerUid, String endPrice) {
        db.collection("User").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : (task.getResult())) {
//                            sellerImg = document.getString("imgUrl");
                            sellerImg = "https://firebasestorage.googleapis.com/v0/b/arrr-48e3f.appspot.com/o/1693339987939_1.png?alt=media&token=e6c84b66-cc18-4934-9736-14dce67adf10";
                            chatRoom.setProfileImageUrl(sellerImg);
                            myRef.child(buyerUid + endPrice).child("dataSet").child("profileImageUrl").setValue(sellerImg);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    // item의 대표사진 가져오기
    public void getItemImg(String documentId, String bidType, String buyerUid, String endPrice) {
        if (bidType.equals("open")) {
            db.collection("OpenItem").document(documentId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                List<String> itemImgs = (List<String>) document.get("imageUrls");
                                if (itemImgs != null && itemImgs.size() > 0) {
                                    itemImg = itemImgs.get(0);
                                    chatRoom.setItemImageUrl(itemImg);
                                    myRef.child(buyerUid + endPrice).child("dataSet").child("itemImageUrl").setValue(itemImg);
                                    Log.d(TAG, "첫 번째 이미지 : " + itemImg);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
        } else {
            db.collection("BiddingItem").document(documentId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                List<String> itemImgs = (List<String>) document.get("imageUrls");
                                if (itemImgs != null && itemImgs.size() > 0) {
                                    itemImg = itemImgs.get(0);
                                    Log.d(TAG, "첫 번째 이미지 : " + itemImg);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
        }
    }

    // chatData add 시켜주는 메소드...
    public void addChat(String seller, ChatData chat, String timestamp, String msg, String buyerUid, String endPrice, int i) {
        if (i == 0) { // buyer
            Query query = db.collection("User").whereEqualTo("email", seller);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        sellerUid = document.getId();
                        Log.d(TAG, "seller의 documentId : " + sellerUid);

                        db.collection("User").document(sellerUid).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                sellerNickname = document.getString("nickname");

                                                mAdapter = new ChatAdapter(chatList, ChatActivity.this, buyerNickname, sellerNickname);
                                                mRecyclerView.setAdapter(mAdapter);
                                                chat.setName(buyerNickname);
                                                chat.setTo(sellerNickname);
                                                chat.setSendTime(timestamp);
                                                chat.setMsg(msg);
                                                myRef.child(buyerUid + endPrice).child("msgData").push().setValue(chat);
                                                // 여기서 새로운 메세지로 계속 업데이트 해주겠지?
                                                myRef.child(buyerUid + endPrice).child("dataSet").child("lastMessage").setValue(msg);
                                                mAdapter.notifyDataSetChanged();
                                                mRecyclerView.scrollToPosition(chatList.size() - 1);

                                                Log.d(TAG, "sellerNickname : " + sellerNickname);
                                                // 여기에서 fieldValue 변수를 사용하세요.
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
        } else if (i == 1) {
            Query query = db.collection("User").whereEqualTo("email", seller);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        sellerUid = document.getId();
                        Log.d(TAG, "seller의 documentId : " + sellerUid);

                        db.collection("User").document(sellerUid).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                sellerNickname = document.getString("nickname");

                                                mAdapter = new ChatAdapter(chatList, ChatActivity.this, sellerNickname, buyerNickname);
                                                mRecyclerView.setAdapter(mAdapter);
                                                chat.setName(sellerNickname);
                                                chat.setTo(buyerNickname);
                                                chat.setSendTime(timestamp);
                                                chat.setMsg(msg);
                                                myRef.child(buyerUid + endPrice).child("msgData").push().setValue(chat);
                                                // 여기서 새로운 메세지로 계속 업데이트 해주겠지?
                                                myRef.child(buyerUid + endPrice).child("dataSet").child("lastMessage").setValue(msg);
                                                mAdapter.notifyDataSetChanged();
                                                mRecyclerView.scrollToPosition(chatList.size() - 1);
                                                Log.d(TAG, "sellerNickname : " + sellerNickname);
                                                // 여기에서 fieldValue 변수를 사용하세요.
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
        }
    }

    // ChatDataList에 add 해주는 메소드
    public void getChatData(String seller, String buyerUid, ChatData chat, String userUid) {
        // buyer의 경우 + 보낸 chatting

        Query query = db.collection("User").whereEqualTo("email", seller);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    if (userUid.equals(buyerUid)) {
                        sellerUid = document.getId();
                    } else {
                        sellerUid = userUid;
                    }
                    Log.d(TAG, "seller의 documentId : " + sellerUid);

                    db.collection("User").document(sellerUid).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            sellerNickname = document.getString("nickname");

                                            if (userUid.equals(buyerUid)) {
                                                // getTo가 seller와 같다는건 현재 유저가 buyer라는 것 && 현재 이메일이 보낸사람과 같을때
                                                if (chat.getTo().equals(sellerNickname) && chat.getName().equals(buyerNickname)) {
                                                    chatList.add(chat);
                                                }
                                                // buyer의 경우 + 받은 chatting
                                                // 수신자가 현재 && 보낸사람이 seller인 경우
                                                if (chat.getTo().equals(buyerNickname) && chat.getName().equals(sellerNickname)) {
                                                    chatList.add(chat);
                                                }
                                                mAdapter = new ChatAdapter(chatList, ChatActivity.this, buyerNickname, sellerNickname);
                                                mRecyclerView.setAdapter(mAdapter);
                                                mAdapter.notifyDataSetChanged();
                                                mRecyclerView.scrollToPosition(chatList.size() - 1);
                                            } else {
                                                // seller의 경우 + 보낸 chatting
                                                // 수신자가 buyer일 때 라는 건 현재 유저가 seller라는 것 && 현재 이메일이 보낸 사람과 같음
                                                if (chat.getTo().equals(buyerEmail) && chat.getName().equals(sellerNickname)) {
                                                    chatList.add(chat);
                                                    // seller의 경우 + 받은 chatting
                                                    // 수신자가 현재 && 보낸사람이 buyer인 경우
                                                }
                                                if (chat.getTo().equals(sellerNickname) && chat.getName().equals(buyerEmail)) {
                                                    chatList.add(chat);
                                                }
                                                mAdapter = new ChatAdapter(chatList, ChatActivity.this, sellerNickname, buyerNickname);
                                                mRecyclerView.setAdapter(mAdapter);
                                                mAdapter.notifyDataSetChanged();
                                                mRecyclerView.scrollToPosition(chatList.size() - 1);
                                            }
                                            Log.d(TAG, "sellerNickname : " + sellerNickname);
                                            // 여기에서 fieldValue 변수를 사용하세요.
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });


    }

    // setAdapter을 위한.....
    public void setmAdapter(String userUid, String buyerUid, String seller) {
        Query query = db.collection("User").whereEqualTo("email", seller);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    sellerUid = document.getId();
                    Log.d(TAG, "seller의 documentId : " + sellerUid);

                    db.collection("User").document(sellerUid).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            sellerNickname = document.getString("nickname");
                                            // 현재 유저가 buyer일때
                                            if (userUid.equals(buyerUid)) {
                                                mAdapter = new ChatAdapter(chatList, ChatActivity.this, buyerNickname, sellerNickname);
                                            } else {
                                                mAdapter = new ChatAdapter(chatList, ChatActivity.this, sellerNickname, buyerNickname);
                                            }
                                            mRecyclerView.setAdapter(mAdapter);
                                            Log.d(TAG, "sellerNickname : " + sellerNickname);
                                            // 여기에서 fieldValue 변수를 사용하세요.
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }


}
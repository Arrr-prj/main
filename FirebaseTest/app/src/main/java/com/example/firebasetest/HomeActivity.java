package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
<<<<<<< Updated upstream
import android.widget.EditText;
=======
import android.widget.Toast;
>>>>>>> Stashed changes

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
<<<<<<< Updated upstream
    private Button btnBidding, btnOpen, btnBest, btnSearch, btnShare;
    private FloatingActionButton btnMypage;
=======
    private FirebaseFirestore db;
    private Button btnBidding, btnOpen, btnBest, btnSearch, btnShare, btnEvent;
    private FloatingActionButton btnMypage, btnAlarm;
>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


<<<<<<< Updated upstream
        btnBidding = findViewById(R.id.btn_bidding);
        btnOpen = findViewById(R.id.btn_open);
        btnBest = findViewById(R.id.btn_best);
        btnMypage = findViewById(R.id.btn_mypage);
        btnShare = findViewById(R.id.btn_share);
//        btnSearch = findViewById(R.id.btn_search);

=======
        btnBidding = findViewById(R.id.btn_bidding); // 비공개 경매
        btnOpen = findViewById(R.id.btn_open); // 공개 경매
        btnBest = findViewById(R.id.btn_best); // 인기 상품
        btnMypage = findViewById(R.id.btn_mypage); // 마이페이지
        btnShare = findViewById(R.id.btn_share); // 무료나눔
        btnEvent = findViewById(R.id.btn_eventAuction); // 이벤트 경매
        btnAlarm = findViewById(R.id.btn_alarm); // 알림 기능


//        btnSearch = findViewById(R.id.btn_search);

        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
        db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("User").document(uid);

        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Boolean membershipValue = documentSnapshot.getBoolean("membership");
                        if (membershipValue) { // membership이 true일 때
                            btnEvent.setEnabled(true); // 이벤트 경매 버튼 활성화
                            btnAlarm.setEnabled(true); // 알림 버튼 활성화

                            // 알림 버튼을 눌렀을 때
                            btnAlarm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // 알림 기능을 하는 액티비티로 넘어가야됨
                                    Toast.makeText(HomeActivity.this, "빠른 시일 내에 업데이트 하겠습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            // 이벤트 경매 버튼을 눌렀을 때
                            btnEvent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // 이벤트 경매 버튼을 클릭할 때
                                    Intent intent = new Intent(HomeActivity.this, EventAuctionActivity.class);
                                    startActivity(intent);

                                }

                            });
                        } else { // membership이 false일 때
                            btnEvent.setEnabled(false); // 이벤트 버튼 비활성화
                            btnAlarm.setEnabled(false); // 알림 버튼 비활성화
                        }
                    }
                });

>>>>>>> Stashed changes
        btnBidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BiddingActivity.class);
                startActivity(intent);
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, OpenAuctionActivity.class);
                startActivity(intent);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });
        btnBest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BiddingActivity.class);
                startActivity(intent);
            }
        });
        btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyTransactionActivity extends AppCompatActivity {
    private Button mBtnBackSpace, mBtnSell, mBtnBuy, mBtnFree, mBtnEvent, mBtnConfirm;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transaction);

        mBtnBackSpace = findViewById(R.id.btn_backSpace);
        mBtnSell = findViewById(R.id.btn_mySell);
        mBtnBuy = findViewById(R.id.btn_myBuy);
        mBtnFree = findViewById(R.id.btn_myFree);
        mBtnEvent = findViewById(R.id.btn_mevent);
        mBtnConfirm = findViewById(R.id.btn_confirm);
        db = FirebaseFirestore.getInstance();

        // 뒤로가기 버튼을 눌렀을 때
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 판매한 아이템 클릭 시 이벤트
        mBtnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTransactionActivity.this, MyItemsActivity.class);
                startActivity(intent);
            }
        });
        // 구매한 아이템 클릭 시 이벤트
        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아직 작동 안함 ( 경매 기능 추가되었을때 넣어주기 )
                Intent intent = new Intent(MyTransactionActivity.this, MyBuyItemsActivity.class);
                startActivity(intent);
            }
        });
        // 무료 나눔 아이템 클릭 시 이벤트
        mBtnFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTransactionActivity.this, MyShareActivity.class);
                startActivity(intent);
            }
        });
        // 구매해야하는 아이템 클릭 시 이벤트
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTransactionActivity.this, MyConfirmItemActivity.class);
                startActivity(intent);
            }
        });

        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
        db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("User").document(uid);

        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Boolean membershipValue = documentSnapshot.getBoolean("membership");
                        if (membershipValue != null && membershipValue) { // admin 계정일 때
                            mBtnEvent.setVisibility(View.VISIBLE);// 버튼 활성화
                            mBtnEvent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {// 이벤트 경매 등록
                                    Intent intent = new Intent(MyTransactionActivity.this, MyEventActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else { // admin 계정이 아닐 때
                            mBtnEvent.setVisibility(View.GONE); // 버튼 비활성화
                        }
                    }
                });
    }
    @Override
    public void onBackPressed(){
        finish();
    }
}
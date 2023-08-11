package com.example.firebasetest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MyPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private TextView mTvName, mTvEmail, mTvAddress;

    private Button mBtnBackSpace, mBtnModify, mBtnLogout, mBtnWithdrawal, mBtnMyItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);


        mBtnBackSpace = findViewById(R.id.btn_backSpace);
        mBtnModify = findViewById(R.id.btn_modify);
        mBtnLogout = findViewById(R.id.btn_logout);
        mBtnWithdrawal = findViewById(R.id.btn_withdrawal);
        mBtnMyItem = findViewById(R.id.btn_myItem);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        mTvName = findViewById(R.id.tv_name);
        mTvEmail = findViewById(R.id.tv_email);
        mTvAddress = findViewById(R.id.tv_address);

        loadUserData();


//         수정 버튼을 눌렀을 때
        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, ModifyActivity.class);
                startActivity(intent);
            }
        });
        // 경매 아이템 눌렀을 때
        mBtnMyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, MyItemsActivity.class);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼을 눌렀을 때
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 로그아웃 버튼을 눌렀을 때
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

                // 로그인 인텐트 생성 후 이동

                FirebaseUser user = mAuth.getCurrentUser();
                String uid = UserManager.getInstance().getUserUid();

                UserManager.getInstance().clearUserUid();

                startActivity(new Intent(MyPageActivity.this, LobyActivity.class));
                finish();
            }
        });

        // 회원탈퇴 버튼을 눌렀을 때
        mBtnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = UserManager.getInstance().getUserUid();
                FirebaseUser user = mAuth.getCurrentUser();

                deleteUserDataFromFirestore(uid);

                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MyPageActivity.this, "회원탈퇴 완료", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MyPageActivity.this, LobyActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    // firestore에서 데이터 가져오는 메서드
    private void loadUserData() {
        String uid = UserManager.getInstance().getUserUid(); // uid 가져오기

        if (uid != null) {
            DocumentReference userDocRef = db.collection("User").document(uid);

            userDocRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                // 문서가 존재할 때 데이터 가져오기
                                String name = documentSnapshot.getString("name");
                                String email = documentSnapshot.getString("email");
                                String address = documentSnapshot.getString("address");

                                mTvName.setText("이름   :  " + name);
                                mTvEmail.setText("이메일 :  " + email);
                                mTvAddress.setText("주소   :  " + address);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 데이터 가져오기 실패 처리
                            Toast.makeText(MyPageActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void deleteUserDataFromFirestore(String uid) {
        DocumentReference userDocRef = db.collection("User").document(uid);

        userDocRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Firestore에서 사용자 데이터 삭제 성공
                    }
                });

    }
}

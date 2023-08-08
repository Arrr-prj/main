package com.example.skgudwls;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MypageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private TextView mTvName, mTvEmail, mTvAddress;

    private Button mBtnBackSpace, mBtnModify, mBtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        mBtnBackSpace = findViewById(R.id.btn_backSpace);
        mBtnModify = findViewById(R.id.btn_modify);
        mBtnLogout = findViewById(R.id.btn_logout);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        mTvName = findViewById(R.id.tv_name);
        mTvEmail = findViewById(R.id.tv_email);
        mTvAddress = findViewById(R.id.tv_address);

        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("User").document(uid).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String name = document.getString("name");
                                    String email = document.getString("email");
                                    String address = document.getString("address");
                                    String password = document.getString("password");
                                    Toast.makeText(MypageActivity.this, "제대로 들어옴", Toast.LENGTH_SHORT).show();

                                    // 텍스트 뷰에 값 넣기
                                    mTvName.setText("이름   :  " + name);
                                    mTvEmail.setText("이메일 :  " + email);
                                    mTvAddress.setText("주소   :  " + address);

                                    // 수정하기 버튼을 눌렀을 때

                                } else {
                                    Toast.makeText(MypageActivity.this, "데이터 없음", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MypageActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(MypageActivity.this, "로그인이 안됨", Toast.LENGTH_SHORT).show();
        }

        // 수정 버튼을 눌렀을 때
        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MypageActivity.this, "수정하기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MypageActivity.this, ModifyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 뒤로가기 버튼을 눌렀을 때
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MypageActivity.this, "뒤로가기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MypageActivity.this, /*뒤로 갈 클래스*/MypageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 로그아웃 누르면
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(MypageActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MypageActivity.this, /*회원가입 보이는 클래스*/MypageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 회원탈퇴 누르면
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getCurrentUser().delete();
                Toast.makeText(MypageActivity.this, "회원탈퇴", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MypageActivity.this, /*회원가입 보이는 클래스(회원은 탈퇴됨)*/MypageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
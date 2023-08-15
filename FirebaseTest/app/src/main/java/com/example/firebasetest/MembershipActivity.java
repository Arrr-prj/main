package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MembershipActivity extends AppCompatActivity {

    private Button mBtnRegister, mBtnMembershipWithdrawal, mBtnBackSpace;
    private FirebaseFirestore db;
    private DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);


        mBtnRegister = findViewById(R.id.btn_membershipRegister); // 멤버십 가입 버튼
        mBtnMembershipWithdrawal = findViewById(R.id.btn_membershipWithdrawal); // 멤버십 탈퇴 버튼
        mBtnBackSpace = findViewById(R.id.btn_backSpace);

        String uid = UserManager.getInstance().getUserUid();
        db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("User").document(uid);

        // 가입하기 버튼이 눌렸을 때
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 가입한 유저의 필드에 있는 membership 값을 true로 변환. 이미 회원이면 이미 회원입니다. 출력
                userDocRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    boolean membershipValue = documentSnapshot.getBoolean("membership");
                                        if(membershipValue == false){ // 멤버십에 가입이 안돼있을 때 가입시키기
                                            userDocRef.update("membership", true)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) { // 업데이트 성공
                                                            Toast.makeText(getApplicationContext(), "멤버십 가입 완료", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) { // 업데이트 실패
                                                            Toast.makeText(getApplicationContext(), "멤버십 가입 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else { // 이미 멤버십에 가입되어있는 사람임
                                            Toast.makeText(getApplicationContext(), "이미 가입된 회원", Toast.LENGTH_SHORT).show();
                                        }
                            }
                        });
            }
        });

        mBtnMembershipWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 탈퇴하려는 유저의 필드에 있는 membership의 값을 확인한 후 true면 false로 변환. false면 회원이 아닙니다.
                userDocRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Boolean membershipValue = documentSnapshot.getBoolean("membership");
                                if (membershipValue != null) {
                                    if (membershipValue) { // 멤버십 회원일 때
                                        userDocRef.update("membership", false) // 탈퇴 시키기
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {// 업데이트 성공
                                                        Toast.makeText(getApplicationContext(), "멤버십 탈퇴 완료", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else { // 이미 회원이 아닌 상태임
                                        Toast.makeText(getApplicationContext(), "이미 회원이 아님", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MembershipActivity.this, MyPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
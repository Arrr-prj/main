package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class MembershipActivity extends AppCompatActivity {

    private Button mBtnRegister, mBtnMembershipWithdrawal, mBtnBackSpace;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);


        mBtnRegister = findViewById(R.id.btn_membershipRegister); // 멤버십 가입 버튼
        mBtnMembershipWithdrawal = findViewById(R.id.btn_membershipWithdrawal); // 멤버십 탈퇴 버튼
        mBtnBackSpace = findViewById(R.id.btn_backSpace);
        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getUid();
        db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("User").document(uid);

        // userDocRef를 사용하여 문서의 데이터를 가져오기
        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // 문서가 존재하면 데이터를 가져와서 처리할 수 있습니다.
                            boolean membership = documentSnapshot.getBoolean("membership");

                            if (membership) { // 멤버십 회원일 때
                                mBtnMembershipWithdrawal.setVisibility(View.VISIBLE); // 탈퇴하기 보이기
                                mBtnRegister.setVisibility(View.INVISIBLE); // 가입하기 숨기기

                                mBtnMembershipWithdrawal.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        userDocRef.get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        Boolean membershipValue = documentSnapshot.getBoolean("membership");
                                                        if (membershipValue != null) {
                                                            if (membershipValue) { // 멤버십 회원일 때
                                                                Map<String, Object> updates = new HashMap<>();
                                                                updates.put("membership", false);
                                                                updates.put("Nike", false);
                                                                updates.put("Adidas", false);
                                                                updates.put("Apple", false);
                                                                updates.put("Samsung", false);
                                                                updates.put("차량", false);
                                                                updates.put("액세서리", false);
                                                                updates.put("의류", false);
                                                                updates.put("한정판", false);
                                                                updates.put("프리미엄", false);
                                                                updates.put("신발", false);
                                                                updates.put("굿즈", false);
                                                                updates.put("가방", false);
                                                                updates.put("가구 인테리어", false);
                                                                updates.put("스포츠 레저", false);
                                                                updates.put("취미 게임", false);
                                                                updates.put("기타", false);

                                                                userDocRef.update(updates)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {// 업데이트 성공
                                                                                Toast.makeText(getApplicationContext(), "멤버십 탈퇴 완료", Toast.LENGTH_SHORT).show();
                                                                                mBtnMembershipWithdrawal.setVisibility(View.GONE);
                                                                                mBtnRegister.setVisibility(View.VISIBLE);
                                                                                Intent intent = getIntent();
                                                                                finish();
                                                                                startActivity(intent);
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
                            } else { // 회원이 아닐 때
                                mBtnRegister.setVisibility(View.VISIBLE); // 가입하기 보이기
                                mBtnMembershipWithdrawal.setVisibility(View.INVISIBLE); // 탈퇴하기 숨기기

                                // 가입하기 버튼이 눌렸을 때
                                mBtnRegister.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // 가입한 유저의 필드에 있는 membership 값을 true로 변환. 이미 회원이면 이미 회원입니다. 출력
                                        userDocRef.get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        Map<String, Object> updates = new HashMap<>();
                                                        updates.put("membership", true);
                                                        updates.put("Nike", false);
                                                        updates.put("Adidas", false);
                                                        updates.put("Apple", false);
                                                        updates.put("Samsung", false);
                                                        updates.put("차량", false);
                                                        updates.put("액세서리", false);
                                                        updates.put("의류", false);
                                                        updates.put("한정판", false);
                                                        updates.put("프리미엄", false);
                                                        updates.put("신발", false);
                                                        updates.put("굿즈", false);
                                                        updates.put("가방", false);
                                                        updates.put("가구 인테리어", false);
                                                        updates.put("스포츠 레저", false);
                                                        updates.put("취미 게임", false);
                                                        updates.put("기타", false);

                                                        boolean membershipValue = documentSnapshot.getBoolean("membership");
                                                        if (membershipValue == false) { // 멤버십에 가입이 안돼있을 때 가입시키기
                                                            userDocRef.update(updates)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) { // 업데이트 성공
                                                                            Toast.makeText(getApplicationContext(), "멤버십 가입 완료", Toast.LENGTH_SHORT).show();
                                                                            mBtnRegister.setVisibility(View.GONE);
                                                                            mBtnMembershipWithdrawal.setVisibility(View.VISIBLE);
                                                                            Intent intent = getIntent();
                                                                            finish();
                                                                            startActivity(intent);
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
                            }

                            // 가져온 데이터를 이용하여 원하는 동작 수행
                            // 예: TextView에 username을 설정하거나, membership 값을 확인하여 버튼 가시성 조절
                        } else {
                            // 문서가 존재하지 않는 경우 처리
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 문서를 가져오는 과정에서 실패한 경우 처리
                    }
                });

        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MembershipActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(MembershipActivity.this, HomeActivity.class));
        finish();
    }
}
package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ModifyActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button mBtnBackSpace;
    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private EditText mEtName, mEtEmail, mEtPwd, mEtAddress;
    private Button mBtnModify;

    String name = " ";
    String email = " ";
    String address = " ";
    String password = " ";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();


        mBtnModify = findViewById(R.id.btn_modify);
        mBtnBackSpace = findViewById(R.id.btn_backSpace);

        mEtName = findViewById(R.id.et_name);
        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtAddress = findViewById(R.id.et_address);


        String uid = currentUser.getUid();
        db.collection("User").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (/*task.isSuccessful()*/true) {
                            DocumentSnapshot document = task.getResult();
                            name = document.getString("name");
                            email = document.getString("email");
                            address = document.getString("address");
                            password = document.getString("password");
                            if (true/*document.exists()*/) {

                                mEtName.setHint("이름 : " + name);
                                mEtEmail.setHint("이메일 : " + email);
                                mEtPwd.setHint("비밀번호 : " + password);
                                mEtAddress.setHint("주소 : " + address);

                            } else {
                                Toast.makeText(ModifyActivity.this, "데이터 없음", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ModifyActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = currentUser.getUid();
                DocumentReference userRef = db.collection("User").document(uid);

                Map<String, Object> updates = new HashMap<>();

                String strName = mEtName.getText().toString();
                String strEmail = mEtEmail.getText().toString();
                String strAddress = mEtAddress.getText().toString();
                String strPassword = mEtPwd.getText().toString();


                // 입력받은 값이 없으면 원래 저장 돼있던 값 그대로


                // 이메일 수정
                if (!(strEmail.isEmpty())) {
                    currentUser.updateEmail(strEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ModifyActivity.this, "이메일 수정 완료", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ModifyActivity.this, "이메일 수정 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    updates.put("email", strEmail);
                } else{
                    updates.put("email", email);
                }

                // 비밀번호 수정
                if (!(strPassword.isEmpty())) {
                    currentUser.updatePassword(strPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ModifyActivity.this, "비밀번호 수정 완료", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ModifyActivity.this, "비밀번호 수정 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    updates.put("password", strPassword);
                } else{
                    updates.put("password", password);
                }

                // 이름 수정
                if (!(strName.isEmpty())) {
                    updates.put("name", strName);
                } else {
                    updates.put("name", name);
                }

                // 주소 수정
                if (!(strAddress.isEmpty())) {
                    updates.put("address", strAddress);
                } else {
                    updates.put("address", address);
                }

                // Firestore에 업데이트
                userRef.update(updates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ModifyActivity.this, "수정 완료", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ModifyActivity.this, MyPageActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ModifyActivity.this, "수정 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        // 뒤로가기 버튼을 눌렀을 때
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ModifyActivity.this, "뒤로가기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ModifyActivity.this, MyPageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
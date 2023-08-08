package com.example.skgudwls;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        mBtnModify = findViewById(R.id.btn_modify);
        mBtnBackSpace = findViewById(R.id.btn_backSpace);


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
                                    Toast.makeText(ModifyActivity.this, "초기화 완료", Toast.LENGTH_SHORT).show();

                                    mEtName.setHint(name);
                                    mEtEmail.setHint(email);
                                    mEtPwd.setHint(password);
                                    mEtAddress.setHint(address);


                                } else {
                                    Toast.makeText(ModifyActivity.this, "데이터 없음", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ModifyActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(ModifyActivity.this, "로그인이 안됨", Toast.LENGTH_SHORT).show();
        }

        // 수정하기 버튼을 누르면
        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid = currentUser.getUid();
                DocumentReference userRef = db.collection("User").document(uid);

                Map<String, Object> updates = new HashMap<>();
                mEtName = findViewById(R.id.et_name);
                mEtEmail = findViewById(R.id.et_email);
                mEtPwd = findViewById(R.id.et_pwd);
                mEtAddress = findViewById(R.id.et_address);

                String name = mEtName.getText().toString();
                String email = mEtEmail.getText().toString();
                String pwd = mEtPwd.getText().toString();
                String address = mEtAddress.getText().toString();

                if(!(name.isEmpty())){
                    updates.put("name", name);
                }
                if(!(email.isEmpty())){
                    updates.put("email", email);
                }
                if(!(pwd.isEmpty())){
                    updates.put("password", pwd);
                }
                if(!(address.isEmpty())){
                    updates.put("address", address);
                }

                Toast.makeText(ModifyActivity.this, "수정 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ModifyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 뒤로가기 버튼을 눌렀을 때
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ModifyActivity.this, "뒤로가기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ModifyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
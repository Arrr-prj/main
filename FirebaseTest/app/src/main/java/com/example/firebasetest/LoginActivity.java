package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseFirestore database;
    private EditText mInputId, mInputPwd;
    private Button mBtnRegister, mBtnLogin, mBtnBack;
    private String strNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mInputId = findViewById(R.id.input_id);
        mInputPwd = findViewById(R.id.input_pwd);
        mBtnRegister = findViewById(R.id.btn_regist);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnBack = findViewById(R.id.btn_back);
        strNickname = "";

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(mInputId) && isEmpty(mInputPwd)) { // 둘 다 입력 안했을 때
                    Toast.makeText(LoginActivity.this, "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isEmpty(mInputId)) { // 아이디 입력 안했을 때
                    Toast.makeText(LoginActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isEmpty(mInputPwd)) { // 비밀번호 입력 안했을 때
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String strId = mInputId.getText().toString();
                String strPwd = mInputPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strId, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        UserDataHolderBiddingItems.loadBiddingItems();
                        UserDataHolderShareItem.loadShareItems();
                        UserDataHolderOpenItems.loadOpenItems();
                        UserDataHolderEventItems.loadEventItems();

                        if (task.isSuccessful()) {
                            // 로그인 성공
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            String uid = user.getUid();
                            UserManager.getInstance().setUserUid(uid);
                            if (user != null) {
                                String userEmail = user.getEmail(); // 현재 사용자의 이메일
                                UserManager.getInstance().setUserEmail(userEmail); // UserManager에 이메일 저장
                            }
                            Log.w("id", strId, task.getException());
//                            setNickname(uid);

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("result_key", "작업 완료 결과");
                            setResult(RESULT_OK, resultIntent);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 종료
                        } else {
                            Toast.makeText(LoginActivity.this, "아이디나 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get FCM token
                            String fcmToken = task.getResult();
                            Log.d("FCM", "FCM Token: " + fcmToken);

                            // Save FCM token to Firestore
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();

                                // Create or update a document in the "FCMTOKEN" collection
                                Map<String, Object> tokenData = new HashMap<>();
                                tokenData.put("token", fcmToken);

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("FCMTOKEN").document(uid)
                                        .set(tokenData)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("FCM", "FCM token saved to Firestore");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("FCM", "Error saving FCM token to Firestore", e);
                                        });
                            }
                        });
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void setNickname(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String nickname = document.getString("nickname");
                        UserManager.getInstance().setNickName(nickname);
                        // 여기서부터 가져온 데이터를 활용하여 원하는 작업을 수행할 수 있습니다.
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                } else {
                    Log.d("Firestore", "Task failed: " + task.getException());
                }
            }
        });
    }

}
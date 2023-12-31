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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    private EditText mEtName, mEtEmail, mEtPwd, mEtPwd2, mEtRrn, mEtSex, mEtAddress;

    private Button mBtnRegister, mBtnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mEtName = findViewById(R.id.et_name);
        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtPwd2 = findViewById(R.id.et_pwd2);
        mEtRrn = findViewById(R.id.et_rrn);
        mEtSex = findViewById(R.id.et_sex);
        mEtAddress = findViewById(R.id.et_address);
        mBtnRegister = findViewById(R.id.btn_register);
        mBtnBack = findViewById(R.id.btn_back);

        Button mBtnRegister = findViewById(R.id.btn_register);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LobyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 가입완료 버튼을 눌렀을 때
        mBtnRegister.setOnClickListener(new View.OnClickListener() {

            // 잠깐 사용할 변수들


            @Override
            public void onClick(View view) {

                if (isEmpty(mEtName) || isEmpty(mEtEmail) || isEmpty(mEtPwd) || isEmpty(mEtPwd2) || isEmpty(mEtRrn) || isEmpty(mEtSex) || isEmpty(mEtAddress)) {
                    Toast.makeText(RegisterActivity.this, "빈칸을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String strName = mEtName.getText().toString();
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strPwd2 = mEtPwd2.getText().toString();
                String strRrn = mEtRrn.getText().toString();
                String strSex = mEtSex.getText().toString();
                String strAddrees = mEtAddress.getText().toString();






                char x = strRrn.charAt(0);
                char y = strRrn.charAt(1);
                String year = String.valueOf(x) + String.valueOf(y);



                // 미성년자 구분
                if(Integer.valueOf(year) > 04 && Integer.valueOf(year) < 23 ){
                    Toast.makeText(RegisterActivity.this, "미성년자는 회원가입이 불가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 남자 여자 구분
                if ((strSex.charAt(0) == '1') || (strSex.charAt(0) == '3')) {
                    strSex = "남자";
                } else if ((strSex.charAt(0) == '2') || (strSex.charAt(0) == '4')){
                    strSex = "여자";
                }else{
                    Toast.makeText(RegisterActivity.this, "주민등록 번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore database = FirebaseFirestore.getInstance();

                if (!strPwd.equals(strPwd2)) { // 비밀번호 재입력이 다를 때
                    Toast.makeText(RegisterActivity.this, "입력하신 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Firebase Auth 진행
                String finalStrSex = strSex;
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 실행됐을 때
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();

                            DocumentReference userDocRef = database.collection("User").document(uid);


                            Map<String, Object> data = new HashMap<>();

                            data.put("name", strName);
                            data.put("email", strEmail);
                            data.put("password", strPwd); // 비밀번호는 6자리 이상만 받음
                            data.put("rrn", strRrn);
//                            data.put("sex", finalStrSex);
                            data.put("address", strAddrees);
                            data.put("membership",false); // 멤버십 기능 추가
                            data.put("Nike", false);
                            data.put("Adidas", false);
                            data.put("Apple", false);
                            data.put("Samsung", false);
                            data.put("차량", false);
                            data.put("액세서리", false);
                            data.put("의류", false);
                            data.put("한정판", false);
                            data.put("프리미엄", false);
                            data.put("신발", false);
                            data.put("굿즈", false);
                            data.put("가방", false);
                            data.put("가구 인테리어", false);
                            data.put("스포츠 레저", false);
                            data.put("취미 게임", false);
                            data.put("기타", false);
                            data.put("cancleCnt", 0);

                            userDocRef.set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // 문서 생성 및 데이터 저장 성공
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("result_key", "작업 완료 결과");
                                            setResult(RESULT_OK, resultIntent);
                                            Intent intent = new Intent(RegisterActivity.this, LobyActivity.class);
                                            intent.putExtra("firstVisit","yes");
                                            startActivity(intent);
                                            finish(); // 현재 액티비티 종료
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // 문서 생성 및 데이터 저장 실패
                                            Toast.makeText(RegisterActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "이메일을 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}
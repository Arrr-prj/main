package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AnnounceRegistActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    EditText aTitle, aContents;
    Button aRegist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_regist);

        aTitle = findViewById(R.id.input_title);
        aContents = findViewById(R.id.input_contents);
        aRegist = findViewById(R.id.btn_registA);

        aRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = aTitle.getText().toString();
                String strContents = aContents.getText().toString();

                // 쓰기
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                Intent intent = new Intent(AnnounceRegistActivity.this, AnnounceActivity.class);
                startActivity(intent);

                Calendar calendar = Calendar.getInstance();
                String uploadMillis = String.valueOf(calendar.getTimeInMillis());

                Map<String, Object> data = new HashMap<>();
                data.put("title", strTitle);
                data.put("contents", strContents);
                data.put("uploadMillis", uploadMillis);

                DocumentReference userDocRef = firestore.collection("Announcement").document(strTitle + "admin"); // 생성되는 문서 이름 정해주기
                userDocRef.set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // 문서 생성 및 데이터 저장 성공
                                Toast.makeText(AnnounceRegistActivity.this, "공지사항 등록에 성공했습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AnnounceRegistActivity.this, AnnounceActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // 문서 생성 및 데이터 저장 실패
                                Toast.makeText(AnnounceRegistActivity.this, "공지사항 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}
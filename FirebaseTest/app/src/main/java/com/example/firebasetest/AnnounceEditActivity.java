package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnnounceEditActivity extends AppCompatActivity {
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    TextView aTitle;
    EditText aContent;
    Button aEdit;
    public static ArrayList<Announcement> announcements = new ArrayList<Announcement>();
    AnnounceAdapter announceAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_edit);

        aTitle = findViewById(R.id.input_title);
        aContent = findViewById(R.id.input_content);
        aEdit = findViewById(R.id.btn_editA);

        // 등록 버튼 클릭 시 이벤트 ( 수정 )
        aEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToFirebase();
            }
        });
        this.getAnnouncements();
    }
    // 파이어베이스에 업로드하기
    private void updateToFirebase(){
        Intent intent = getIntent();
        String documentId= intent.getStringExtra("documentId");
        Announcement selectedItem = null;

        // Announce 목록에서 선택한 아이템 검색
        for (Announcement writing : UserDataHolderAnnouncements.announcements) {
            String doc = writing.getTitle()+"admin";
            if(doc.equals(documentId)){
                selectedItem = writing;
                break;
            }
        }
        if (selectedItem != null) {
            // Announcement를 Firestore에서 업데이트
            uploadToAFirebase(selectedItem.getTitle() , aContent.getText().toString());
        }else {
            Toast.makeText(AnnounceEditActivity.this, "선택한 아이템을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadToAFirebase(String strTitle, String strContent) {

            Map<String, Object> data = new HashMap<>();
            data.put("title", strTitle);
            data.put("contents", strContent);

            DocumentReference userDocRef = db.collection("Announcement").document(strTitle + "admin");
            userDocRef.update(data)
                    .addOnSuccessListener(aVoid -> {
                        // 등록된 리스트 새로 갱신
                        UserDataHolderBiddingItems.loadBiddingItems();
                        Toast.makeText(AnnounceEditActivity.this, "상품 수정에 성공했습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AnnounceEditActivity.this, AnnounceActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "상품 수정에 실패했습니다." + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
    }
    private void getAnnouncements() {
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, ""+documentId);
        db = FirebaseFirestore.getInstance();
        db.collection("Announcement").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        String strTitle = document.getString("title");
                        String strContent = document.getString("contents");

                        aTitle.setText(strTitle);
                        aContent.setHint(strContent);

                    }
                });
    }
}
package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AnnounceDetailActivity extends AppCompatActivity {
    private TextView aTitle, aContents;
    private Button btnEdit, btnDelete, btnList;
    public static ArrayList<Announcement> announcements = new ArrayList<Announcement>();
    AnnounceAdapter announceAdapter;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_detail);

        // 공지사항에 들어가는 글 제목 , 글 내용
        aTitle = findViewById(R.id.title);
        aContents = findViewById(R.id.contents);

        // 수정 or 삭제
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);
        btnList = findViewById(R.id.btn_list);
        announceAdapter = new AnnounceAdapter(this, new ArrayList<>());

        // 페이지 접속 시 새로 코딩해줌
        UserDataHolderAnnouncements.announcements.clear();
        UserDataHolderAnnouncements.loadAnnouncement();

        // 기존 글 리스트 비워줘서 로딩할 때 기존 리스트들이 중복으로 추가되지 않도록 한다
        announcements.clear();
        announcements.addAll(UserDataHolderAnnouncements.announcements);

        getAnnouncements();


        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
        database = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = database.collection("User").document(uid);

        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Boolean adminValue = documentSnapshot.getBoolean("admin");
                if (adminValue != null && adminValue) { // admin 계정일 때
                    btnDelete.setVisibility(View.VISIBLE); // 삭제 버튼 활성화
                    btnEdit.setVisibility(View.VISIBLE); // 수정 버튼 활성화
                    // 삭제 버튼 눌렀을 때
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            // 전달받은 documentId를 변수에 저장
                            Intent intent = getIntent();
                            String documentId = intent.getStringExtra("documentId");
                            // 공지사항 클릭 시 담아둘 변수 set
                            Announcement selectedItem = null;

                            // 공지사항의 documentId 중 글 제목+"admin" 과 documentId가 같으면 선택된 아이템을 변수에 담아둔다
                            for (Announcement item : UserDataHolderAnnouncements.announcements) {
                                if (documentId.equals(item.getTitle() + "admin")) {
                                    selectedItem = item;
                                    // delete() 이용하여 해당 데이터 삭제
                                    db.collection("Announcement").document(selectedItem.getTitle() + "admin")
                                            .delete()
                                            .addOnSuccessListener(aVoid -> {
                                                AnnounceActivity.announcements.remove(item);
                                                announceAdapter.notifyDataSetChanged();
                                                // 삭제된 리스트 새로 갱신
                                                UserDataHolderAnnouncements.loadAnnouncement();
                                                Toast.makeText(AnnounceDetailActivity.this, "공지사항이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(AnnounceDetailActivity.this, AnnounceActivity.class);
                                                startActivity(intent1);
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(AnnounceDetailActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                            });
                                    break;
                                } else {
                                }
                            }
                        }
                    });

                    // 목록 버튼 눌렀을 때
                    btnList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AnnounceDetailActivity.this, AnnounceActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    // 수정 버튼 눌렀을 때
                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = getIntent();
                            String documentId = intent.getStringExtra("documentId");
                            Announcement selectedItem = null;
                            // Announcement 에서 선택한 아이템 검색
                            for (Announcement item : UserDataHolderAnnouncements.announcements) {
                                if (documentId.equals(item.getTitle() + "admin")) {
                                    selectedItem = item;
                                    break;
                                }
                            }

                            if (selectedItem != null) {
                                UserDataHolderBiddingItems.loadBiddingItems();
                                Toast.makeText(AnnounceDetailActivity.this, "공지사항 수정", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(getApplicationContext(), AnnounceEditActivity.class);
                                intent1.putExtra("documentId", selectedItem.getTitle() + "admin");
                                startActivity(intent1);
                            }
                        }
                    });
                } else { // admin 계정이 아닐 때
                    btnDelete.setVisibility(View.GONE); // 삭제 버튼 비활성화
                    btnEdit.setVisibility(View.GONE); // 수정 버튼 비활성화
                }
            }
        });



    }

    private void getAnnouncements() {
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, "" + documentId);
        announcements.clear();
        UserDataHolderAnnouncements.loadAnnouncement();
        announcements.addAll(UserDataHolderAnnouncements.announcements);
        database = FirebaseFirestore.getInstance();
        database.collection("Announcement").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (/*task.isSuccessful()*/true) {
                            DocumentSnapshot document = task.getResult();
                            String strTitle = document.getString("title");
                            String strContent = document.getString("contents");
                            if (true/*document.exists()*/) {
                                aTitle.setText(strTitle);
                                aContents.setText(strContent);

                            } else {
                                Toast.makeText(AnnounceDetailActivity.this, "데이터 없음", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AnnounceDetailActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

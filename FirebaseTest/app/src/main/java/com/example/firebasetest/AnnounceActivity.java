package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class AnnounceActivity extends AppCompatActivity {
    ListView listView;
    Button mBtnBck;
    ImageButton mBtnregistAnnounce;
    public static ArrayList<Announcement> announcements = new ArrayList<Announcement>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce);

        listView = (ListView)findViewById(R.id.listView);
        mBtnregistAnnounce = findViewById(R.id.btn_registA);
        mBtnBck = findViewById(R.id.btn_back);

        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
        db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("User").document(uid);

        // 아이템 로드
        this.InitializeAnnounce();
        setOnClickaListener();
        // admin계정 확인 코드
        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Boolean adminValue = documentSnapshot.getBoolean("admin");
                        if (adminValue != null && adminValue) { // admin 계정일 때
                            mBtnregistAnnounce.setVisibility(View.VISIBLE);// 버튼 활성화
                            mBtnregistAnnounce.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {// 공지 사항 등록
                                    Intent intent = new Intent(AnnounceActivity.this, AnnounceRegistActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else { // admin 계정이 아닐 때
                            mBtnregistAnnounce.setVisibility(View.GONE); // 버튼 비활성화
                        }
                    }
                });
        // 뒤로가기 버튼을 눌렀을 때
        mBtnBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void InitializeAnnounce(){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        announcements.clear();
        db.collection("Announcement")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            // Firebase Storage에서 이미지 불러오기
                            announcements.add(
                                    new Announcement(
                                            String.valueOf(document.getData().get("title")),
                                            String.valueOf(document.getData().get("contents")),
                                            String.valueOf(document.getData().get("uploadMillis")),
                                            String.valueOf(calMillis(String.valueOf(document.getData().get("uploadMillis")))) // 오늘이랑 업로드한 날짜 차이
                                    )
                            );

                        }

                        // 정렬
                        Comparator<Announcement> calDaysComparator = new Comparator<Announcement>() {
                            @Override
                            public int compare(Announcement item1, Announcement item2) {
                                Integer days1 = Integer.parseInt(item1.getDifferenceMillis());
                                Integer days2 = Integer.parseInt(item2.getDifferenceMillis());
                                return days1.compareTo(days2);
                            }
                        };
                        // endItemList을 calDays 함수의 결과를 기준으로 정렬
                        Collections.sort(announcements, calDaysComparator);

                        AnnounceAdapter announceAdapter = new AnnounceAdapter(this, announcements);
                        listView.setAdapter(announceAdapter);
                    }
                });

    }
    public String calMillis(String upload){
        Log.d(TAG, "uploadMillis의 값 : "+upload);
        // upload한 날짜의 Millis
        Long uploadMillis = Long.parseLong(upload);
        // 오늘 날짜의 Millis
        Calendar now = Calendar.getInstance();
        Long nowMillis = Long.parseLong(String.valueOf(now.getTimeInMillis()));
        // 오늘 Millis - upload Millis의 값이 작을수록 최근에 올린 것.
        Long differenceMillis = nowMillis - uploadMillis;
        return String.valueOf(differenceMillis);
    }
    // 상세 페이지 이벤트
    public void setOnClickaListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 항목을 가져옴
                Announcement announcement = (Announcement) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), AnnounceDetailActivity.class);
                showDetail.putExtra("documentId", announcement.getTitle()+"admin");
                startActivity(showDetail);
            }
        });
    }
}
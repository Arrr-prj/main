package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MemberActivity extends AppCompatActivity {
    public static ArrayList<Item> userList = new ArrayList<Item>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listView;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        listView = (ListView)findViewById(R.id.listView);
        btnBack = (Button)findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 기본값은 리스트 출력
        InitializeEndItemList();
    }
    public void InitializeEndItemList(){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        userList.clear();

        db.collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear(); // 기존 데이터를 비움
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            userList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("email")),
                                            String.valueOf(document.getData().get("name")),
                                            String.valueOf(document.getData().get("address")),
                                            String.valueOf(document.getData().get("membership")),
                                            String.valueOf(document.getData().get("nickname")),
                                            String.valueOf(document.getData().get("reports"))
                                    )
                            );
                        }

                        // 데이터 로드가 완료되면 어댑터를 업데이트
                        updateAdapter();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

    }
    private void updateAdapter() {
        // 어댑터 생성 및 설정
        MemberAdapter adapter = new MemberAdapter(this, userList);
        listView.setAdapter(adapter);
        // 리스트뷰 갱신
        adapter.notifyDataSetChanged();
    }
}
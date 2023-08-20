package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyEventActivity extends AppCompatActivity {
    ListView listView;
    public static ArrayList<Item> eventItemList;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    private Button btnbck;
    private Switch btnSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        eventItemList = new ArrayList<Item>();
        listView = (ListView)findViewById(R.id.listView);
        btnbck = findViewById(R.id.btn_back);
        btnSwitch = findViewById(R.id.sw_cORw);
        btnSwitch.setOnCheckedChangeListener(new cORwSwitchListener());

        String currentUser = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid

        // Off 일 때 구매 대기 중인 아이템
        this.InitializeEventItem(currentUser);
        setOnClickeConfirmListener();

        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // 상세 페이지 이벤트
    public void setOnClickeListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 항목을 가져옴
                Item item = (Item) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), MyItemDetailActivity.class);
                Log.d(TAG, ""+item.getSeller());
                showDetail.putExtra("isBuy","Buy");
                showDetail.putExtra("state", "Event");
                showDetail.putExtra("documentId", item.getTitle()+firebaseUser.getEmail());
                startActivity(showDetail);
            }
        });
    }
    // 상세 페이지 이벤트
    public void setOnClickeConfirmListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 항목을 가져옴
                Item item = (Item) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), MyItemDetailActivity.class);
                Log.d(TAG, ""+item.getSeller());
                showDetail.putExtra("isBuy","Confirm");
                showDetail.putExtra("state", "Event");
                showDetail.putExtra("documentId", item.getTitle()+firebaseUser.getEmail());
                startActivity(showDetail);
            }
        });
    }

    public void InitializeEventItem(String currentUser){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        eventItemList.clear();
        db.collection("EventItem").whereEqualTo("buyer", currentUser)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            eventItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            String.valueOf(document.getData().get("imgUrl")),
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("price")),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")),
                                            String.valueOf(document.getData().get("futureMillis")),
                                            String.valueOf(document.getData().get("futureDate"))
                                    )
                            );

                        }
                        ListAdapter listAdapter = new ListAdapter(this, eventItemList);
                        listView.setAdapter(listAdapter);
                    }
                });

    }
    public void InitializeConfirmEventItem(String currentUser){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        eventItemList.clear();
        db.collection("EventItem").whereEqualTo("buyer", currentUser)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            // confirm이 true인 아이템 불러오기 = 구매가 완료된 아이템
                            if(Boolean.parseBoolean(String.valueOf(document.getData().get("confirm")))) {
                                eventItemList.add(
                                        new Item(
                                                String.valueOf(document.getData().get("title")),
                                                String.valueOf(document.getData().get("imgUrl")),
                                                String.valueOf(document.getData().get("id")),
                                                String.valueOf(document.getData().get("price")),
                                                String.valueOf(document.getData().get("category")),
                                                String.valueOf(document.getData().get("info")),
                                                String.valueOf(document.getData().get("futureMillis")),
                                                String.valueOf(document.getData().get("futureDate"))
                                        )
                                );
                            }

                        }
                        ListAdapter listAdapter = new ListAdapter(this, eventItemList);
                        listView.setAdapter(listAdapter);
                    }
                });

    }
    class cORwSwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String currentUser = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
            if (isChecked) {
                InitializeConfirmEventItem(currentUser);
                btnSwitch.setText("구매 확정 아이템");
                setOnClickeListener();
            } else {
                InitializeEventItem(currentUser);
                btnSwitch.setText("구매 대기 아이템");
                setOnClickeConfirmListener();
            }
        }
    }

}
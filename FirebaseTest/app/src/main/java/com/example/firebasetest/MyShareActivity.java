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

public class MyShareActivity extends AppCompatActivity {
    ListView listView;
    public static ArrayList<Item> freeItemList;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    private Switch bORo;
    private Button btnbck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_free_items);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String seller = firebaseUser.getEmail();
        db = FirebaseFirestore.getInstance();
        freeItemList = new ArrayList<Item>();
        listView = (ListView)findViewById(R.id.listView);
        btnbck = findViewById(R.id.btn_back);

        bORo = findViewById(R.id.sw_bORo);
        bORo.setOnCheckedChangeListener(new bORoSwitchListener());

        this.InitializeFreeItem(seller);
        setOnClickfListener();

        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyShareActivity.this, MyTransactionActivity.class);
                startActivity(intent);
            }
        });

    }
    // 상세 페이지 이벤트
    public void setOnClickfListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 항목을 가져옴
                Item item = (Item) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), MyShareDetailActivity.class);
                Log.d(TAG, ""+item.getSeller());
                showDetail.putExtra("state", "On");
                showDetail.putExtra("documentId", item.getTitle()+firebaseUser.getEmail());
//                showDetail.putExtra("sellerId", )
                startActivity(showDetail);
            }
        });
    }

    public void InitializeFreeItem(String seller){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        freeItemList.clear();
        db.collection("ShareItem").whereEqualTo("seller", seller)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            freeItemList.add(
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
                        OpenAuctionAdapter openAuctionAdapter = new OpenAuctionAdapter(this, freeItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });

    }
    class bORoSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            String seller = firebaseUser.getEmail();
            if(isChecked){
                InitializeFreeItem(seller);
                bORo.setText("나눔 받은 아이템");
                setOnClickfListener();
            }else{
                InitializeFreeItem(seller);
                bORo.setText("나눔한 아이템");
                setOnClickfListener();
            }
        }
    }
}
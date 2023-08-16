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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyBuyItemsActivity extends AppCompatActivity {
    ListView listView;
    public static ArrayList<Item> openItemList;
    public static ArrayList<Item> biddingItemList;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    private Button btnbck;
    private Switch bORo;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy_items);

        listView = (ListView)findViewById(R.id.listView);
        openItemList = new ArrayList<Item>();
        biddingItemList = new ArrayList<Item>();
        uid = UserManager.getInstance().getUserUid();
        db = FirebaseFirestore.getInstance();

        btnbck = findViewById(R.id.btn_back);
        bORo = findViewById(R.id.sw_bORo);
        bORo.setOnCheckedChangeListener(new MyBuyItemsActivity.bORoSwitchListener());
        // 기본 값은 bidding
        this.InitializeBiddingItem();
        setOnClickbListener();

        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyBuyItemsActivity.this, MyTransactionActivity.class);
                startActivity(intent);
            }
        });
    }
    public void InitializeOpenItem(){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        openItemList.clear();
        db.collection("OpenItem").whereEqualTo("buyer", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            openItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            String.valueOf(document.getData().get("imgUrl")),
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("price")),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")),
                                            String.valueOf(document.getData().get("seller")),
                                            String.valueOf(document.getData().get("futureMillis")),
                                            String.valueOf(document.getData().get("futureDate"))
                                    )
                            );

                        }
                        OpenAuctionAdapter openAuctionAdapter = new OpenAuctionAdapter(this, openItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });

    }
    public void InitializeBiddingItem(){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        biddingItemList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        db.collection("BiddingItem").whereEqualTo("buyer", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("seller"));
                            biddingItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            String.valueOf(document.getData().get("imgUrl")),
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("price")),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")),
                                            String.valueOf(document.getData().get("seller")),
                                            String.valueOf(document.getData().get("futureMillis")),
                                            String.valueOf(document.getData().get("futureDate"))
                                    ));
                        }
                        BiddingItemAdapter biddingItemAdapter = new BiddingItemAdapter(this, biddingItemList);
                        listView.setAdapter(biddingItemAdapter);
                    }
                });
    }
    // 상세 페이지 이벤트
    public void setOnClickoListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 항목을 가져옴
                Item item = (Item) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), MyItemDetailActivity.class);
                Log.d(TAG, "seller를 확인해보자. "+item.getSeller());
                Log.d(TAG, "info를 확인해보자. "+item.getInfo());
                showDetail.putExtra("state", "On");
                showDetail.putExtra("isBuy","Buy");
                // item.getSeller() 에서 error 생길 수 있음 ***********
                showDetail.putExtra("documentId", item.getTitle()+item.getSeller());
                startActivity(showDetail);
            }
        });
    }
    public void setOnClickbListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 항목을 가져옴
                Item item = (Item) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), MyItemDetailActivity.class);
                showDetail.putExtra("state", "Off");
                showDetail.putExtra("isBuy","Buy");
                showDetail.putExtra("documentId", item.getTitle()+item.getSeller());
                startActivity(showDetail);
            }
        });
    }
    class bORoSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            if(isChecked){
                InitializeOpenItem();
                bORo.setText("오픈 경매 아이템");
                setOnClickoListener();
            }else{
                InitializeBiddingItem();
                bORo.setText("비공개 경매 아이템");
                setOnClickbListener();
            }
        }
    }
}
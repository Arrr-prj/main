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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyItemsActivity extends AppCompatActivity {
    ListView listView;
    public static ArrayList<Item> openItemList = new ArrayList<Item>();
    public static ArrayList<Item> biddingItemList = new ArrayList<Item>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageReference = storage.getReference();

    private Button btnbck;
    private Switch bORo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        listView = (ListView)findViewById(R.id.listView);

        btnbck = findViewById(R.id.btn_back);

        this.InitializeOpenItem();
        this.InitializeBiddingItem();
        final BiddingItemAdapter biddingItemAdapter = new BiddingItemAdapter(this, biddingItemList);
        final OpenAuctionAdapter openAuctionAdapter = new OpenAuctionAdapter(this, openItemList);

        listView.setAdapter(biddingItemAdapter);
        listView.setAdapter(openAuctionAdapter);
        listView.setOnItemClickListener(listener);

        bORo = findViewById(R.id.sw_bORo);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String seller = firebaseUser.getEmail();
        bORo.setOnCheckedChangeListener(new bORoSwitchListener());
        // 기본 값은 bidding
        this.InitializeBiddingItem(seller);
        setOnClickbListener();


        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyItemsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Toast.makeText(MyItemsActivity.this, "Clicked position: " + parent, Toast.LENGTH_SHORT).show();
        }
    };
    public void InitializeOpenItem(String seller){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        openItemList.clear();
        db.collection("OpenItem").whereEqualTo("seller", seller)
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
    public void InitializeBiddingItem(String seller){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        biddingItemList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        db.collection("BiddingItem").whereEqualTo("seller", seller)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("id") + document.getData().get("imgUrl"));
                            biddingItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            String.valueOf(document.getData().get("imgUrl")),
                                            String.valueOf(document.getData().get("id")),
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

}

    // 상세 페이지 이벤트
    public void setOnClickoListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 항목을 가져옴
                Item item = (Item) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), MyItemDetailActivity.class);
                Log.d(TAG, ""+item.getSeller());
                showDetail.putExtra("state", "On");
                showDetail.putExtra("documentId", item.getTitle()+firebaseUser.getEmail());
//                showDetail.putExtra("sellerId", )
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
                showDetail.putExtra("documentId", item.getTitle()+firebaseUser.getEmail());
                startActivity(showDetail);
            }
        });
    }
    class bORoSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            String seller = firebaseUser.getEmail();
            if(isChecked){
                InitializeOpenItem(seller);
                bORo.setText("오픈 경매 아이템");
                setOnClickoListener();
            }else{
                InitializeBiddingItem(seller);
                bORo.setText("비딩 경매 아이템");
                setOnClickbListener();
            }
        }
    }
}

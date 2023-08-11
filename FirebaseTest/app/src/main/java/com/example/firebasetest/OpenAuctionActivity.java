package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenAuctionActivity extends AppCompatActivity {
    private Button btnRegistItem;
    ListView listView;

    private Button btnbck;
    private OpenAuctionAdapter oitemAdapter;
    public static ArrayList<Item> openItemList = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_auction);

        listView = (ListView)findViewById(R.id.listView);
        oitemAdapter = new OpenAuctionAdapter(this, openItemList);
        listView.setAdapter(oitemAdapter);


        btnbck = findViewById(R.id.btn_back);
        this.InitializeBiddingItem();
        btnRegistItem = findViewById(R.id.btn_registItem);
        // 아이템 등록 버튼 클릭 시 이벤트
        btnRegistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenAuctionActivity.this, OpenRegistItemActivity.class);
                startActivity(intent);
            }
        });
        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenAuctionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        // DetailPage
        setUpOnClickListener();
    }
    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Item item = (Item) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), DetailItemActivity.class);
                showDetail.putExtra("id", item.getId());
                startActivity(showDetail);
            }
        });
    }
    public void InitializeBiddingItem(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        openItemList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("OpenItem")
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String id = document.getString("id");
                            String info = document.getString("info");
                            String category = document.getString("category");
                            String seller = document.getString("seller");
                            String imgUrl = document.getString("imgUrl");
                            String price = document.getString("price"); // 정수형으로 변환

                            // Item 생성자에 맞게 데이터 추가
                            Item item = new Item(title, imgUrl, id, price, category, info, seller);
                            openItemList.add(item);
                        }
                        oitemAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

}
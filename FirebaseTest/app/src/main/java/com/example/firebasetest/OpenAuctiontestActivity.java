package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OpenAuctiontestActivity extends AppCompatActivity {
    private Button btnRegistItem;
    ListView listView;

    private Button btnbck;
    public static ArrayList<Itemtest> openItemList = new ArrayList<Itemtest>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_auction);

        listView = (ListView)findViewById(R.id.listView);

        btnbck = findViewById(R.id.btn_back);

        this.InitializeOpenItem();

        btnRegistItem = findViewById(R.id.btn_registItem);
        // 아이템 등록 버튼 클릭 시 이벤트
        btnRegistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenAuctiontestActivity.this, OpenRegistItemtestActivity.class);
                startActivity(intent);
            }
        });
        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenAuctiontestActivity.this, HomeActivity.class);
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
                Itemtest item = (Itemtest) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), OpenDetailItemtestActivity.class);
                showDetail.putExtra("id", item.getId());
                showDetail.putExtra("title", item.getTitle());
                showDetail.putExtra("seller", item.getSeller());
                startActivity(showDetail);
            }
        });
    }
    public void InitializeOpenItem(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // 기존 아이템 리스트 비워주기
        openItemList.clear();
        db.collection("OpenItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            openItemList.add(
                                    new Itemtest(
                                            String.valueOf(document.getData().get("title")),
                                            String.valueOf(document.getData().get("imgUrl1")),
                                            String.valueOf(document.getData().get("imgUrl2")),
                                            String.valueOf(document.getData().get("imgUrl3")),
                                            String.valueOf(document.getData().get("imgUrl4")),
                                            String.valueOf(document.getData().get("imgUrl5")),
                                            String.valueOf(document.getData().get("imgUrl6")),
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
                        OpenAuctionAdaptertest openAuctionAdapter = new OpenAuctionAdaptertest(this, openItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });
    }
}
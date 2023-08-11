package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Intent;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BiddingActivity extends AppCompatActivity {
    private Button btnRegistItem;
    private ListView listView;

    private Button btnbck;
    public static ArrayList<BiddingItem> biddingItemList;
    private List<BiddingItem> search_biddingItemList;
    private SearchView searchView;
    private BiddingItemAdapter adapter;
    private boolean searching = false;

    public BiddingActivity(){
        biddingItemList = new ArrayList<BiddingItem>();
        search_biddingItemList = new ArrayList<>();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);

        btnbck = findViewById(R.id.btn_back);

//        setupEvents();
        listView = (ListView)findViewById(R.id.listView);
        // 저장되어있는 데이터 가져오기
        this.InitializeBiddingItem();



        adapter = new BiddingItemAdapter(this, biddingItemList);
        listView.setAdapter(adapter);



        searchView = findViewById(R.id.search_View);


        //검색창에 텍스트가 입력됐을경우
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()  {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 사용자가 검색 버튼을 눌렀을 때의 동작
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색창에 텍스트가 입력될 때의 동작
                if (newText.length() > 0) {
                    searching = true;
                    filterItemList(newText);
                } else {
                    searching = false;
                    updateListView(biddingItemList);
                }
                return false;
            }
        });


        btnRegistItem = findViewById(R.id.btn_registItem);
        // 아이템 등록 버튼 클릭 시 이벤트
        btnRegistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BiddingActivity.this, BiddingRegistItemActivity.class);
                startActivity(intent);
            }
        });
        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BiddingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        // DetailPage
        setUpOnClickListener();

    }
    // 상세 페이지 이벤트
    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BiddingItem item = (BiddingItem) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), OpenDetailItemActivity.class);
                showDetail.putExtra("id", item.getId());
                startActivity(showDetail);
            }
        });
    }

    public void InitializeBiddingItem(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        biddingItemList.clear();
        db.collection("BiddingItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id")+document.getData().get("imgUrl"));
                            biddingItemList.add(
                                    new BiddingItem(
                                            String.valueOf(document.getData().get("imgUrl")),
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")),
                                            String.valueOf(document.getData().get("seller")),
                                            String.valueOf(document.getData().get("time"))
                                    )
                            );

                        }
                        BiddingItemAdapter biddingItemAdapter = new BiddingItemAdapter(this, biddingItemList);
                        listView.setAdapter(biddingItemAdapter);
                    }
                });
    }

    public void filterItemList(String query){
        search_biddingItemList.clear();
        for(BiddingItem item : biddingItemList){
            if(item.getId().toLowerCase().contains(query.toLowerCase())){
                search_biddingItemList.add(item);
            }
            updateListView(search_biddingItemList);
        }
    }
    public void updateListView(List<BiddingItem> itemList){
        adapter = new BiddingItemAdapter(this, itemList);
        listView.setAdapter(adapter);
    }

}
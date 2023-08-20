package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiddingActivity extends AppCompatActivity {
    private ImageButton btnRegistItem;
    ListView listView;
    private FirebaseFirestore db;

    private Button btnbck;
    public static ArrayList<Item> biddingItemList = new ArrayList<Item>();

    private ArrayList<Item> search_biddingItemList = new ArrayList<>();
    private SearchView searchView;
    private boolean searching = false;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);

        btnbck = findViewById(R.id.btn_back);

//        setupEvents();
        listView = (ListView)findViewById(R.id.listView);
        // 저장되어있는 데이터 가져오기
        this.InitializeBiddingItem();
        btnRegistItem = findViewById(R.id.btn_registItem);
        db = FirebaseFirestore.getInstance();
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>0){
                    searching = true;
                    filterItemList(newText);
                } else {
                    searching = false;
                    updateListView(biddingItemList);
                }
                return false;
            }
        });
        // 아이템 등록 버튼 클릭 시 이벤트
        btnRegistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BiddingActivity.this, BiddingRegistItemActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BiddingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // DetailPage
        setUpOnClickListener();

//        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
//        db = FirebaseFirestore.getInstance();
//        db.collection("BiddingItem").document(uid)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : (task.getResult())) {
//                            Log.d(TAG, "DocumentSnapshot Data : " + document.getData().get("buyer"));
//                            Calendar calendar = Calendar.getInstance();
//                            String now = String.valueOf(calendar.getTimeInMillis());
//                            Integer nowMillis = Integer.parseInt(now);
//                            Integer endMillis = Integer.parseInt(String.valueOf(document.getData().get("futureMillis")));
//                            // 종료되었을때
//                            if (nowMillis >= endMillis) {
//
//                            }
//                        }
//                    }
//                });


        // 접속 유저가 buyer와 같고 && 경매 시간이 종료되었을 때 팝업 띄워서 낙찰 하도록
        // 팝업 창에서 "결제하기" 하면 buyer 확정.
        // 팝업 창에서 "낙찰포기" 하면 buyer = null로 할당.

    }
    // 상세 페이지 이벤트
    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                Item item = (Item) listView.getItemAtPosition(position);

                // 조회 수 카운팅
                String documentId = item.getTitle()+item.getSeller();
                DocumentReference docRef = db.collection("BiddingItem").document(documentId);
                Map<String, Object> updates = new HashMap<>();
                updates.put("views", item.getViews()+1);
                // 문서 수정
                docRef.update(updates);
                // 수정된 내용 갱신
                UserDataHolderBiddingItems.loadBiddingItems();

                Intent showDetail = new Intent(getApplicationContext(), BiddingDetailItemActivity.class);
                showDetail.putExtra("documentId", item.getTitle()+item.getSeller());
                showDetail.putExtra("seller", item.getSeller());
                showDetail.putExtra("buyer", item.getBuyer());
                showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                showDetail.putExtra("futureMillis", item.getFutureMillis());
                startActivity(showDetail);
                finish();
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
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id")+document.getData().get("imgUrl1"));
                            biddingItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            String.valueOf(document.getData().get("imgUrl1")),
                                            String.valueOf(document.getData().get("imgUrl2")),
                                            String.valueOf(document.getData().get("imgUrl3")),
                                            String.valueOf(document.getData().get("imgUrl4")),
                                            String.valueOf(document.getData().get("imgUrl5")),
                                            String.valueOf(document.getData().get("imgUrl6")),
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("price")),
                                            String.valueOf(document.getData().get("endPrice")),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")),
                                            String.valueOf(document.getData().get("seller")),
                                            String.valueOf(document.getData().get("buyer")),
                                            String.valueOf(document.getData().get("futureMillis")),
                                            String.valueOf(document.getData().get("futureDate")),
                                            String.valueOf(document.getData().get("uploadMillis")),
                                            String.valueOf(calMillis(String.valueOf(document.getData().get("uploadMillis")))), // 오늘이랑 업로드한 날짜 차이,
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );

                        }
                        // 정렬
                        Comparator<Item> calDaysComparator = new Comparator<Item>() {
                            @Override
                            public int compare(Item item1, Item item2) {
                                Integer days1 = Integer.parseInt(item1.getDifferenceDays());
                                Integer days2 = Integer.parseInt(item2.getDifferenceDays());
                                return days1.compareTo(days2);
                            }
                        };
                        // endItemList을 calDays 함수의 결과를 기준으로 정렬
                        Collections.sort(biddingItemList, calDaysComparator);

                        ListAdapter biddingItemAdapter = new ListAdapter(this, biddingItemList);
                        listView.setAdapter(biddingItemAdapter);
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
    public void filterItemList(String query){
        search_biddingItemList.clear();
        for(Item item : biddingItemList){
            if(item.getId().toLowerCase().contains(query.toLowerCase())){
                search_biddingItemList.add(item);
            }
            updateListView(search_biddingItemList);
        }
    }
    public void updateListView(List<Item> itemList){
        adapter = new ListAdapter(this, itemList);
        listView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(BiddingActivity.this, HomeActivity.class));
    }

}
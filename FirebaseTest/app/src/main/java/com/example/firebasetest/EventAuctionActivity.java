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
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class EventAuctionActivity extends AppCompatActivity {
    private ImageButton btnRegistItem;
    ListView listView;
    private Button btnbck;
    private FirebaseFirestore db;
    public static ArrayList<Item> EventAuctionItemList = new ArrayList<Item>();
    private ArrayList<Item> search_EventItemList = new ArrayList<>();
    private SearchView searchView;
    private boolean searching = false;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_auction);

        listView = (ListView)findViewById(R.id.listView);

        btnbck = findViewById(R.id.btn_back);

        this.InitializeOpenItem();

        btnRegistItem = findViewById(R.id.btn_registItem);


        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
        db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("User").document(uid);

        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Boolean adminValue = documentSnapshot.getBoolean("admin");
                        if (adminValue != null && adminValue) { // admin 계정일 때
                            btnRegistItem.setVisibility(View.VISIBLE);// 버튼 활성화
                            btnRegistItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {// 이벤트 경매 등록
                                    Intent intent = new Intent(EventAuctionActivity.this, EventAuctionRegisterItemActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else { // admin 계정이 아닐 때
                            btnRegistItem.setVisibility(View.GONE); // 버튼 비활성화
                        }
                    }
                });
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
                    updateListView(EventAuctionItemList);
                }
                return false;
            }
        });
        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventAuctionActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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

                // 조회수 카운팅
                String documentId = item.getTitle()+item.getSeller();
                DocumentReference docRef = db.collection("EventItem").document(documentId);
                Map<String, Object> updates = new HashMap<>();
                updates.put("views", item.getViews()+1);
                // 문서 수정
                docRef.update(updates);
                // 수정된 내용 갱신
                UserDataHolderEventItems.loadEventItems();

                Intent showDetail = new Intent(getApplicationContext(), EventAuctionDetailItemActivity.class);
                showDetail.putExtra("id", item.getId());
                showDetail.putExtra("title", item.getTitle());
                showDetail.putExtra("seller", item.getSeller());
                showDetail.putExtra("buyer", item.getBuyer());
                showDetail.putExtra("imageUrls", item.getImageUrls());
                showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                showDetail.putExtra("futureMillis", item.getFutureMillis());
                startActivity(showDetail);
            }
        });
    }
    public void InitializeOpenItem(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // 기존 아이템 리스트 비워주기
        EventAuctionItemList.clear();
        db.collection("EventItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // 이 코드를 사용하여 이미지 URL 배열을 가져옵니다.
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            // Firebase Storage에서 이미지 불러오기
                            EventAuctionItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,  // 수정된 이미지 URL
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
                                            String.valueOf(calMillis(String.valueOf(document.getData().get("uploadMillis")))), // 오늘이랑 업로드한 날짜 차이
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
                        Collections.sort(EventAuctionItemList, calDaysComparator);
                        Collections.sort(EventAuctionItemList, calDaysComparator);

                        ListAdapter eventItemAdapter = new ListAdapter(this, EventAuctionItemList);
                        listView.setAdapter(eventItemAdapter);
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
        search_EventItemList.clear();
        for(Item item : EventAuctionItemList){
            if(item.getId().toLowerCase().contains(query.toLowerCase())){
                search_EventItemList.add(item);
            }
            updateListView(search_EventItemList);
        }
    }
    public void updateListView(List<Item> itemList){
        adapter = new ListAdapter(this, itemList);
        listView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(EventAuctionActivity.this, HomeActivity.class));
    }
}
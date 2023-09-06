package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.ims.ImsMmTelManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeSearchActivity extends AppCompatActivity{
    private ArrayList<Item> allItemList = new ArrayList<>();
    private ArrayList<Item> biddingItemList = new ArrayList<>();
    private ArrayList<Item> openItemList = new ArrayList<>();
    private ArrayList<Item> shareItemList = new ArrayList<>();
    private ArrayList<Item> eventItemList = new ArrayList<>();
    private ArrayList<Item> search_ItemList = new ArrayList<>();
    private SearchView searchView;
    private boolean searching = false;
    private ListAdapter adapter;

    private ListView listView;
    private Button btnbck, btnAll, btnOpen, btnBid, btnShare, btnEvent;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);

        btnAll = findViewById(R.id.all_search);
        btnOpen = findViewById(R.id.open_search);
        btnBid = findViewById(R.id.bidding_search);
        btnShare = findViewById(R.id.share_search);
        btnEvent = findViewById(R.id.event_search);

        btnbck = findViewById(R.id.btn_back);

//        setupEvents();
        listView = (ListView)findViewById(R.id.listView);
        // 저장되어있는 데이터 가져오기
        this.InitializeBiddingItem();
        db = FirebaseFirestore.getInstance();
        searchView = findViewById(R.id.search_view);
        this.InitializeAllItem();
        searchView.setOnQueryTextListener
                (new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText.length()>0){
                            searching = true;
                            filterItemList(newText,allItemList);
                        } else {
                            searching = false;
                            updateListView(allItemList);
                        }
                        return false;
                    }
                });

        // DetailPage
        this.setUpOnClickListener();

        btnAll.setOnClickListener(new SearchView.OnClickListener() {

            @Override
            public void onClick(View view) {
                btnAll.setBackgroundColor(Color.GRAY);
                btnBid.setBackgroundColor(Color.WHITE);
                btnOpen.setBackgroundColor(Color.WHITE);
                btnShare.setBackgroundColor(Color.WHITE);
                btnEvent.setBackgroundColor(Color.WHITE);

                InitializeAllItem();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText.length()>0){
                            searching = true;
                            filterItemList(newText,allItemList);
                        } else {
                            searching = false;
                            updateListView(biddingItemList);
                        }
                        return false;
                    }
                });
            }
        });
        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAll.setBackgroundColor(Color.WHITE);
                btnBid.setBackgroundColor(Color.GRAY);
                btnOpen.setBackgroundColor(Color.WHITE);
                btnShare.setBackgroundColor(Color.WHITE);
                btnEvent.setBackgroundColor(Color.WHITE);

                InitializeBiddingItem();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText.length()>0){
                            searching = true;
                            filterItemList(newText,biddingItemList);
                        } else {
                            searching = false;
                            updateListView(biddingItemList);
                        }
                        return false;
                    }
                });
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAll.setBackgroundColor(Color.WHITE);
                btnBid.setBackgroundColor(Color.WHITE);
                btnOpen.setBackgroundColor(Color.GRAY);
                btnShare.setBackgroundColor(Color.WHITE);
                btnEvent.setBackgroundColor(Color.WHITE);

                InitializeOpenItem();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText.length()>0){
                            searching = true;
                            filterItemList(newText,openItemList);
                        } else {
                            searching = false;
                            updateListView(openItemList);
                        }
                        return false;
                    }
                });
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnAll.setBackgroundColor(Color.WHITE);
                btnBid.setBackgroundColor(Color.WHITE);
                btnOpen.setBackgroundColor(Color.WHITE);
                btnShare.setBackgroundColor(Color.GRAY);
                btnEvent.setBackgroundColor(Color.WHITE);

                InitializeShareItem();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText.length()>0){
                            searching = true;
                            filterItemList(newText,shareItemList);
                        } else {
                            searching = false;
                            updateListView(shareItemList);
                        }
                        return false;
                    }
                });
            }
        });
        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAll.setBackgroundColor(Color.WHITE);
                btnBid.setBackgroundColor(Color.WHITE);
                btnOpen.setBackgroundColor(Color.WHITE);
                btnShare.setBackgroundColor(Color.WHITE);
                btnEvent.setBackgroundColor(Color.GRAY);

                InitializeEventItem();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText.length()>0){
                            searching = true;
                            filterItemList(newText,eventItemList);
                        } else {
                            searching = false;
                            updateListView(eventItemList);
                        }
                        return false;
                    }
                });
            }
        });

        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(HomeSearchActivity.this, HomeActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                Item item = (Item) listView.getItemAtPosition(position);

                if("BiddingItem".equals(item.getItemType())){
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
                    showDetail.putExtra("imageUrls", item.getImageUrls());
                    showDetail.putExtra("buyer", item.getBuyer());
                    showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                    showDetail.putExtra("futureMillis", item.getFutureMillis());
                    startActivity(showDetail);
                    finish();
                }
                else if("OpenItem".equals(item.getItemType())){
                    String documentId = item.getTitle()+item.getSeller();
                    DocumentReference docRef = db.collection("OpenItem").document(documentId);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("views", item.getViews()+1);
                    // 문서 수정
                    docRef.update(updates);
                    // 수정된 내용 갱신
                    UserDataHolderOpenItems.loadOpenItems();

                    Intent showDetail = new Intent(getApplicationContext(), OpenDetailItemActivity.class);
                    showDetail.putExtra("id", item.getId());
                    showDetail.putExtra("title", item.getTitle());
                    showDetail.putExtra("seller", item.getSeller());
                    showDetail.putExtra("imageUrls", item.getImageUrls());
                    showDetail.putExtra("buyer", item.getBuyer());
                    showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                    showDetail.putExtra("futureMillis", item.getFutureMillis());
                    startActivity(showDetail);
                    finish();
                }
                else if("ShareItem".equals(item.getItemType())){
                    String documentId = item.getTitle()+item.getSeller();
                    DocumentReference docRef = db.collection("ShareItem").document(documentId);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("views", item.getViews()+1);
                    Log.d(TAG, "views : "+item.getSeller());
                    // 문서 수정
                    docRef.update(updates);
                    // 수정된 내용 갱신
                    UserDataHolderShareItem.loadShareItems();

                    Intent showDetail = new Intent(getApplicationContext(), ShareDetailActivity.class);
                    showDetail.putExtra("id", item.getId());
                    showDetail.putExtra("title", item.getTitle());
                    showDetail.putExtra("seller", item.getSeller());
                    showDetail.putExtra("imageUrls", item.getImageUrls());
                    showDetail.putExtra("buyer", item.getSeller());
                    showDetail.putExtra("futureMillis", item.getFutureMillis());
                    startActivity(showDetail);
                    finish();
                }
                else if("EventItem".equals(item.getItemType())){
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
                    showDetail.putExtra("imageUrls", item.getImageUrls());
                    showDetail.putExtra("buyer", item.getBuyer());
                    showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                    showDetail.putExtra("futureMillis", item.getFutureMillis());
                    startActivity(showDetail);
                    finish();
                }
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
                            // Firebase Storage에서 이미지 불러오기
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            biddingItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,
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
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("uploadMillis"));
                            // Firebase Storage에서 이미지 불러오기
                            // Firebase Storage에서 이미지 불러오기
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            openItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,
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
                                            Boolean.getBoolean(String.valueOf(document.getData().get("confirm"))),
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
                        Collections.sort(openItemList, calDaysComparator);

                        ListAdapter openAuctionAdapter = new ListAdapter(this, openItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });
    }
    public void InitializeAllItem(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // 기존 아이템 리스트 비워주기
        allItemList.clear();
        db.collection("OpenItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("uploadMillis"));
                            // Firebase Storage에서 이미지 불러오기
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            allItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,
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
                                            Boolean.getBoolean(String.valueOf(document.getData().get("confirm"))),
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
                        Collections.sort(allItemList, calDaysComparator);

                        ListAdapter openAuctionAdapter = new ListAdapter(this, allItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });
        db.collection("BiddingItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("uploadMillis"));
                            // Firebase Storage에서 이미지 불러오기
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            allItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,
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
                                            Boolean.getBoolean(String.valueOf(document.getData().get("confirm"))),
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
                        Collections.sort(allItemList, calDaysComparator);

                        ListAdapter openAuctionAdapter = new ListAdapter(this, allItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });
        db.collection("ShareItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("uploadMillis"));
                            // Firebase Storage에서 이미지 불러오기
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            allItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,
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
                                            Boolean.getBoolean(String.valueOf(document.getData().get("confirm"))),
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
                        Collections.sort(allItemList, calDaysComparator);

                        ListAdapter openAuctionAdapter = new ListAdapter(this, allItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });
        db.collection("EventItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("uploadMillis"));
                            // Firebase Storage에서 이미지 불러오기
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);

                            allItemList.add(
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
                                            Boolean.getBoolean(String.valueOf(document.getData().get("confirm"))),
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
                        Collections.sort(allItemList, calDaysComparator);

                        ListAdapter openAuctionAdapter = new ListAdapter(this, allItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });
    }
    public void InitializeShareItem(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // 기존 아이템 리스트 비워주기
        shareItemList.clear();
        db.collection("ShareItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);

                            shareItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("price")),
                                            String.valueOf(document.getData().get("endPrice")),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")),
                                            String.valueOf(document.getData().get("seller")),
                                            String.valueOf(document.getData().get("buyer")),
                                            String.valueOf(document.getData().get("futureMillis")),
                                            String.valueOf(document.getData().get("futureDate")),
                                            String.valueOf(document.getData().get("uploadDate")),
                                            String.valueOf(calMillis(String.valueOf(document.getData().get("uploadMillis")))), // 오늘이랑 업로드한 날짜 차이
                                            Boolean.getBoolean(String.valueOf(document.getData().get("confirm"))),
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
                        Collections.sort(shareItemList, calDaysComparator);

                        ListAdapter openAuctionAdapter = new ListAdapter(this, shareItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });
    }
    public void InitializeEventItem(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // 기존 아이템 리스트 비워주기
        eventItemList.clear();
        db.collection("EventItem")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            eventItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,
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
                        Collections.sort(eventItemList, calDaysComparator);
                        Collections.sort(eventItemList, calDaysComparator);

                        ListAdapter eventItemAdapter = new ListAdapter(this, eventItemList);
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
    public void filterItemList(String query,ArrayList<Item> list){
        search_ItemList.clear();
        for(Item item : list){
            if(item.getId().toLowerCase().contains(query.toLowerCase())){
                search_ItemList.add(item);
            }
            updateListView(search_ItemList);
        }
    }
    public void updateListView(List<Item> itemList){
        adapter = new ListAdapter(this, itemList);
        listView.setAdapter(adapter);
    }
    //공개경매

    @Override
    public void onBackPressed(){
        startActivity(new Intent(HomeSearchActivity.this, HomeActivity.class));
    }
}

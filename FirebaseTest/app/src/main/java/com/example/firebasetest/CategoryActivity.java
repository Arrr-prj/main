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
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    public static ArrayList<Item> endItemList = new ArrayList<Item>();

    Intent receivedIntent;
    String receivedValue;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listView;
    Button btnBack;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_car);

        receivedIntent = getIntent();
        receivedValue = receivedIntent.getStringExtra("category");

        listView = (ListView)findViewById(R.id.listView);
        btnBack = (Button)findViewById(R.id.btn_back);
        textView = findViewById(R.id.TV_categoryType);
        textView.setText(receivedValue);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 기본값은 리스트 출력
        InitializeEndItemList();
        setUpOnClickListener();
    }
    public void InitializeEndItemList(){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        endItemList.clear();

        // 낙찰자가 있는 아이템을 불러오도록 함
        // Open Item
        db.collection("OpenItem").whereEqualTo("category", receivedValue)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        ArrayList<Item> openItem  = new ArrayList<>(); // 새로운 임시 리스트에 저장
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            endItemList.add(
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
                                            String.valueOf(calMillis(String.valueOf(document.getData().get("uploadMillis")))), // 오늘이랑 업로드한 날짜 차이,
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }

                    }
                });
        // Bidding Item
        db.collection("BiddingItem").whereEqualTo("category", receivedValue)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            endItemList.add(
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
                                            String.valueOf(calMillis(String.valueOf(document.getData().get("uploadMillis")))), // 오늘이랑 업로드한 날짜 차이,
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }
                    }
                });

        // ShareItem
        db.collection("ShareItem").whereEqualTo("category", receivedValue)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            endItemList.add(
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
                                            String.valueOf(calMillis(String.valueOf(document.getData().get("uploadMillis")))), // 오늘이랑 업로드한 날짜 차이,
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }
                    }
                });

        // EventItem
        db.collection("EventItem").whereEqualTo("category", receivedValue)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            endItemList.add(
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
                        Collections.sort(endItemList, calDaysComparator);
                        ListAdapter listAdapter = new ListAdapter(this, endItemList);
                        listView.setAdapter(listAdapter);
                    }
                });



    }
    // 상세 페이지 이벤트
    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Item item = (Item) listView.getItemAtPosition(position);
                if(item.getItemType().equals("BiddingItem")){
                    Intent showDetail = new Intent(CategoryActivity.this, BiddingDetailItemActivity.class);

                    // 조회수 카운팅
                    String documentId = item.getTitle()+item.getSeller();
                    DocumentReference docRef = db.collection("BiddingItem").document(documentId);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("views", item.getViews()+1);
                    Log.d(TAG, "views : "+item.getSeller());
                    // 문서 수정
                    docRef.update(updates);
                    // 수정된 내용 갱신
                    UserDataHolderShareItem.loadShareItems();

                    showDetail.putExtra("documentId", item.getTitle()+item.getSeller());
                    showDetail.putExtra("id", item.getId());
                    showDetail.putExtra("title", item.getTitle());
                    showDetail.putExtra("seller", item.getSeller());
                    showDetail.putExtra("imageUrls", item.getImageUrls());
                    showDetail.putExtra("buyer", item.getBuyer());
                    showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                    showDetail.putExtra("futureMillis", item.getFutureMillis());
                    startActivity(showDetail);
                }else if(item.getItemType().equals("OpenItem")){
                    Log.d(TAG, "ItemType : "+item.getItemType());
                    Log.d(TAG, "DifferenceDays : "+item.getDifferenceDays());
                    Log.d(TAG, "documentId : "+item.getTitle()+item.getSeller());
                    Intent showDetail = new Intent(CategoryActivity.this, OpenDetailItemActivity.class);

                    // 조회수 카운팅
                    String documentId = item.getTitle()+item.getSeller();
                    DocumentReference docRef = db.collection("OpenItem").document(documentId);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("views", item.getViews()+1);
                    Log.d(TAG, "views : "+item.getSeller());
                    // 문서 수정
                    docRef.update(updates);
                    // 수정된 내용 갱신
                    UserDataHolderShareItem.loadShareItems();

                    showDetail.putExtra("documentId", item.getTitle()+item.getSeller());
                    showDetail.putExtra("id", item.getId());
                    showDetail.putExtra("title", item.getTitle());
                    showDetail.putExtra("seller", item.getSeller());
                    showDetail.putExtra("imageUrls", item.getImageUrls());
                    showDetail.putExtra("buyer", item.getBuyer());
                    showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                    showDetail.putExtra("futureMillis", item.getFutureMillis());
                    startActivity(showDetail);
                }else if(item.getItemType().equals("ShareItem")){
                    Intent showDetail = new Intent(CategoryActivity.this, ShareDetailActivity.class);

                    // 조회수 카운팅
                    String documentId = item.getTitle()+item.getSeller();
                    DocumentReference docRef = db.collection("ShareItem").document(documentId);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("views", item.getViews()+1);
                    Log.d(TAG, "views : "+item.getSeller());
                    // 문서 수정
                    docRef.update(updates);
                    // 수정된 내용 갱신
                    UserDataHolderShareItem.loadShareItems();

                    showDetail.putExtra("documentId", item.getTitle()+item.getSeller());
                    showDetail.putExtra("id", item.getId());
                    showDetail.putExtra("title", item.getTitle());
                    showDetail.putExtra("seller", item.getSeller());
                    showDetail.putExtra("imageUrls", item.getImageUrls());
                    showDetail.putExtra("buyer", item.getBuyer());
                    showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                    showDetail.putExtra("futureMillis", item.getFutureMillis());
                    startActivity(showDetail);
                }else if(item.getItemType().equals("EventItem")){
                    Intent showDetail = new Intent(CategoryActivity.this, EventAuctionDetailItemActivity.class);

                    // 조회수 카운팅
                    String documentId = item.getTitle()+item.getSeller();
                    DocumentReference docRef = db.collection("EventItem").document(documentId);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("views", item.getViews()+1);
                    Log.d(TAG, "views : "+item.getSeller());
                    // 문서 수정
                    docRef.update(updates);
                    // 수정된 내용 갱신
                    UserDataHolderShareItem.loadShareItems();

                    showDetail.putExtra("documentId", item.getTitle()+item.getSeller());
                    showDetail.putExtra("id", item.getId());
                    showDetail.putExtra("title", item.getTitle());
                    showDetail.putExtra("seller", item.getSeller());
                    showDetail.putExtra("imageUrls", item.getImageUrls());
                    showDetail.putExtra("buyer", item.getBuyer());
                    showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                    showDetail.putExtra("futureMillis", item.getFutureMillis());
                    startActivity(showDetail);
                }

            }
        });
    }
    // 오늘 날짜 - 업로드한 날짜 계산해주는 메소드
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

    // 오늘 날짜 - 경매 마감된 날짜 계산해주는 메소드
    public String calDays(String endDate1){
        // SimpleDateFormat을 사용하여 String을 Date로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date date;
        try{
            date = dateFormat.parse(endDate1);

            // Date를 Calendar로 변환
            calendar.setTime(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Calendar nowMillis = Calendar.getInstance();
        long differenceInMillis = nowMillis.getTimeInMillis() - calendar.getTimeInMillis();
        long differenceInDays = differenceInMillis / (24 * 60 * 60 * 1000);

        return String.valueOf(differenceInDays);
    }

}
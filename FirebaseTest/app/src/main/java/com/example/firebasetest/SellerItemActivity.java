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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Map;

public class SellerItemActivity extends AppCompatActivity {
    ListView listView;
    public static ArrayList<Item> openItemList = new ArrayList<Item>();
    public static ArrayList<Item> biddingItemList = new ArrayList<Item>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;
    Intent intent;

    private Button btnbck;
    private TextView text;
    private Switch bORo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_item);

        listView = (ListView)findViewById(R.id.listView);

        btnbck = findViewById(R.id.btn_back);
        bORo = findViewById(R.id.sw_bORo);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        intent = getIntent();
        String seller = intent.getStringExtra("sellerId");
        text = findViewById(R.id.textView7);
        bORo.setOnCheckedChangeListener(new bORoSwitchListener());
        this.InitializeBiddingItem(seller);
        setOnClickbListener(seller);

        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        db.collection("User").whereEqualTo("email", seller)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : (task.getResult())) {
                            String sellerNickname = document.getString("nickname");
                            text.setText(sellerNickname + "님 상품");
                        }
                    }
                });

    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){

        }
    };
    public void InitializeOpenItem(String seller){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // 기존 아이템 리스트 비워주기
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
    public void InitializeBiddingItem(String seller){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        biddingItemList.clear();
        db.collection("BiddingItem").whereEqualTo("seller", seller)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("id") + document.getData().get("imgUrl"));
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

    private void setOnClickoListener(String seller) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Item item = (Item) listView.getItemAtPosition(position);

                // 조회수 카운팅
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
                showDetail.putExtra("buyer", item.getBuyer());
                showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                showDetail.putExtra("futureMillis", item.getFutureMillis());
                startActivity(showDetail);
            }
        });
    }

    public void setOnClickbListener(String seller) {
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
    class bORoSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            String seller = intent.getStringExtra("sellerId");
            if(isChecked){
                InitializeOpenItem(seller);
                bORo.setText("공개 상품");
                setOnClickoListener(seller);
            }else{
                InitializeBiddingItem(seller);
                bORo.setText("비공개 상품");
                setOnClickbListener(seller);
            }
        }
    }
}
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAuctionActivity extends AppCompatActivity {
    private ImageButton btnRegistItem;
    ListView listView;
    private FirebaseFirestore db;
    private ArrayList<Item> search_openItemList = new ArrayList<>();
    private SearchView searchView;
    private boolean searching = false;
    private ListAdapter adapter;


    private Button btnbck;
    public static ArrayList<Item> openItemList = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_auction);

        db = FirebaseFirestore.getInstance();
        listView = (ListView)findViewById(R.id.listView);

        btnbck = findViewById(R.id.btn_back);

        this.InitializeOpenItem();

        btnRegistItem = findViewById(R.id.btn_registOpenItem);

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
                    updateListView(openItemList);
                }
                return false;
            }
        });
        // 아이템 등록 버튼 클릭 시 이벤트
        btnRegistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenAuctionActivity.this, OpenRegistItemActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenAuctionActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // DetailPage
        setUpOnClickListener();

        // 접속 유저가 buyer와 같고 && 타이머가 종료되었을 때 팝업 띄워서 낙찰 하도록
        // 팝업 창에서 "결제하기" 하면 buyer 확정.
        // 팝업 창에서 "낙찰포기" 하면 buyer = null로 할당.
    }
    private void setUpOnClickListener() {
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
                showDetail.putExtra("imageUrls", item.getImageUrls());
                showDetail.putExtra("confirm", String.valueOf(item.getConfirm()));
                showDetail.putExtra("futureMillis", item.getFutureMillis());
                showDetail.putExtra("endprice", item.getEndPrice());
                showDetail.putExtra("cancel",item.getCancel());

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
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("uploadMillis"));
                            // 이 코드를 사용하여 이미지 URL 배열을 가져옵니다.
                            List<String> imageUrlsList = (List<String>) document.get("imageUrls");
                            String[] imageUrlsArray = new String[imageUrlsList.size()];
                            imageUrlsList.toArray(imageUrlsArray);
                            // Firebase Storage에서 이미지 불러오기
                            openItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("title")),
                                            imageUrlsArray,  // 수정된 이미지 URL
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("price")),
                                            String.valueOf(document.getData().get("endPrice")),
                                            String.valueOf(document.getData().get("magamPrice")),
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
                                            Integer.valueOf(String.valueOf(document.getData().get("views"))),
                                            String.valueOf(document.getData().get("cancel"))
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
        search_openItemList.clear();
        for(Item item : openItemList){
            if(item.getId().toLowerCase().contains(query.toLowerCase())){
                search_openItemList.add(item);
            }
            updateListView(search_openItemList);
        }
    }
    public void updateListView(List<Item> itemList){
        adapter = new ListAdapter(this, itemList);
        listView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(OpenAuctionActivity.this, HomeActivity.class));
    }

}
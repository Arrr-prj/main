package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TotalAuctionActivity extends AppCompatActivity {

    // open,  bidding, share, event Item
    public static ArrayList<Item> endItemList = new ArrayList<Item>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listView;
    TextView totalProfit;
    Button btnBack;
    BarChart chartSales;

    ArrayList<Entry> entries = new ArrayList<>(); // 값 - 인덱스 넣어주면 순차적으로 그려줌, y축 이름(데이터 값)
    ArrayList<String> xVals = new ArrayList<>(); // x축 이름 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_auction);

        listView = (ListView)findViewById(R.id.listView);
        totalProfit = (TextView)findViewById(R.id.totalProfit);
        btnBack = (Button)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 기본값은 리스트 출력
        InitializeEndItemList();
    }
    public void InitializeEndItemList(){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        endItemList.clear();

        // 낙찰자가 있는 아이템을 불러오도록 함
        // Open Item
        db.collection("OpenItem").whereEqualTo("confirm", true)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            endItemList.add(
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
                                            String.valueOf(document.getData().get("uploadDate")),
                                            String.valueOf(calDays((String.valueOf(document.getData().get("futureDate"))))), // differenceDays
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }
                    }
                });
        // Bidding Item
        db.collection("BiddingItem").whereEqualTo("confirm", true)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            endItemList.add(
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
                                            String.valueOf(document.getData().get("uploadDate")),
                                            String.valueOf(calDays((String.valueOf(document.getData().get("futureDate"))))), // differenceDays
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }
                    }
                });
        // ShareItem
        db.collection("ShareItem").whereEqualTo("confirm", true)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            endItemList.add(
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
                                            String.valueOf(document.getData().get("uploadDate")),
                                            String.valueOf(calDays((String.valueOf(document.getData().get("futureDate"))))), // differenceDays
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }
                    }
                });

        // EventItem
        db.collection("EventItem").whereEqualTo("confirm", true)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            endItemList.add(
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
                                            String.valueOf(document.getData().get("uploadDate")),
                                            String.valueOf(calDays((String.valueOf(document.getData().get("futureDate"))))), // differenceDays
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }

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


                        SalesAdapter salesAdapter = new SalesAdapter(this, endItemList);
                        listView.setAdapter(salesAdapter);
                        totalProfit.setText("총 이익 : "+String.valueOf(salesAdapter.getTotalProfit())+" 원");
                    }
                });

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
        long differenceInMillis = calendar.getTimeInMillis() - nowMillis.getTimeInMillis();
        long differenceInDays = differenceInMillis / (24 * 60 * 60 * 1000);

        return String.valueOf(differenceInDays);
    }

}
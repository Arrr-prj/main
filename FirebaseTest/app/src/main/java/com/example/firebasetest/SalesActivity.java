package com.example.firebasetest;

import static android.content.ContentValues.TAG;
import static android.icu.number.NumberRangeFormatter.RangeIdentityFallback.RANGE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SalesActivity extends AppCompatActivity {
    // open,  bidding, share, event Item
    public static ArrayList<Item> endItemList = new ArrayList<Item>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listView;
    TextView totalProfit;
    Switch gORl;
    Button btnBack;
    BarChart chartSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        listView = (ListView)findViewById(R.id.listView);
        totalProfit = (TextView)findViewById(R.id.totalProfit);
        gORl = (Switch)findViewById(R.id.sw_gORl);
        btnBack = (Button)findViewById(R.id.btn_back);
        chartSales = (BarChart) findViewById(R.id.day_sale_chart);
        gORl.setOnCheckedChangeListener(new gORlSwitchListener());

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
        db.collection("OpenItem").whereNotEqualTo("buyer", null)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        ArrayList<Item> openItem  = new ArrayList<>(); // 새로운 임시 리스트에 저장
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
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
                                            String.valueOf(calDays(((String)document.getData().get("futureDate")))),
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }

                    }
                });
        // Bidding Item
        db.collection("BiddingItem").whereNotEqualTo("buyer", null)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));

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
                                            String.valueOf(calDays(((String)document.getData().get("futureDate")))),
                                            Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                            String.valueOf(document.getData().get("itemType")),
                                            Integer.valueOf(String.valueOf(document.getData().get("views")))
                                    )
                            );
                        }
                    }
                });

        // EventItem
        db.collection("EventItem").whereNotEqualTo("buyer", null)
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
                                            String.valueOf(calDays(((String)document.getData().get("futureDate")))),
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
                        SalesAdapter salesAdapter = new SalesAdapter(this, endItemList);
                        listView.setAdapter(salesAdapter);
                        totalProfit.setText("총 이익 : "+String.valueOf(salesAdapter.getTotalProfit())+" 원");
                    }
                });



        }

        public void InitializeEndItemGraph(){
            // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
            endItemList.clear();

            // 낙찰자가 있는 아이템을 불러오도록 함
            // Open Item
            db.collection("OpenItem").whereNotEqualTo("buyer", null)
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
                                                String.valueOf(calDays(((String)document.getData().get("futureDate")))),
                                                Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                                String.valueOf(document.getData().get("itemType")),
                                                Integer.valueOf(String.valueOf(document.getData().get("views")))
                                        )
                                );
                            }
                        }
                    });
            // Bidding Item
            db.collection("BiddingItem").whereNotEqualTo("buyer", null)
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
                                                String.valueOf(calDays(((String)document.getData().get("futureDate")))),
                                                Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                                String.valueOf(document.getData().get("itemType")),
                                                Integer.valueOf(String.valueOf(document.getData().get("views")))
                                        )
                                );
                            }
                        }
                    });
            // ShareItem
            db.collection("ShareItem").whereNotEqualTo("buyer", null)
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
                                                String.valueOf(calDays(((String)document.getData().get("futureDate")))),
                                                Boolean.parseBoolean(String.valueOf(document.getData().get("confirm"))),
                                                String.valueOf(document.getData().get("itemType")),
                                                Integer.valueOf(String.valueOf(document.getData().get("views")))
                                        )
                                );
                            }
                        }
                    });

            // EventItem
            db.collection("EventItem").whereNotEqualTo("buyer", null)
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
                                                String.valueOf(calDays(((String)document.getData().get("futureDate")))),
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
                            ArrayList<BarEntry> entries = new ArrayList<>();
                            entries.add(new BarEntry(0, salesAdapter.get5Daysago()));
                            entries.add(new BarEntry(1, salesAdapter.get4Daysago()));
                            entries.add(new BarEntry(2, salesAdapter.get3Daysago()));
                            entries.add(new BarEntry(3, salesAdapter.get2Daysago()));
                            entries.add(new BarEntry(4, salesAdapter.get1Daysago()));
                            entries.add(new BarEntry(5, salesAdapter.getToday()));
                            setSalesChart(entries);

                            totalProfit.setText("총 이익 : "+String.valueOf(salesAdapter.getTotalProfit())+" 원");
                        }
                    });

        }
        // 그래프 그려주는 메소드
        public void setSalesChart(ArrayList<BarEntry> entries){
            // 문자열 x 축 데이터
            final String[] labels = {"5일 전", "4일 전", "3일 전", "2일 전", "1일 전", "오늘"};


            BarDataSet dataSet = new BarDataSet(entries, "최근 5일간 수익 현황");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            BarData barData = new BarData(dataSet);
            barData.setValueTextSize(15);

            chartSales.setData(barData);

            // X축 설정
            XAxis xAxis = chartSales.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int index = (int) value;
                    if (index >= 0 && index < labels.length) {
                        return labels[index];
                    }
                    return ""; // 범위를 벗어나면 빈 문자열 반환
                }
            });
            // Y축 설정
            YAxis yAxisLeft = chartSales.getAxisLeft();
            yAxisLeft.setAxisMinimum(0f);

            YAxis yAxisRight = chartSales.getAxisRight();
            yAxisRight.setEnabled(false); // 오른쪽 Y축 비활성화

            // 범례 설정
            Legend legend = chartSales.getLegend();
            legend.setEnabled(true);


            chartSales.invalidate(); // 차트 업데이트
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
    class gORlSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            if(isChecked){
                listView.setVisibility(View.GONE);
                chartSales.setVisibility(View.VISIBLE);
                InitializeEndItemGraph();
                gORl.setText("그래프");
            }else{
                listView.setVisibility(View.VISIBLE);
                chartSales.setVisibility(View.GONE);
                InitializeEndItemList();
                gORl.setText("리스트");
            }
        }
    }
}


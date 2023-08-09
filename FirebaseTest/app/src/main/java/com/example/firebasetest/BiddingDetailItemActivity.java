package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiddingDetailItemActivity extends AppCompatActivity {

    private Button btnEdit, btnDelete;
    private TextView itemId, startPrice, endPrice, itemInfo, seller, category, timeInfo, timeRemaining;
    private ImageView imgUrl;
    BiddingItem item;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding_detail_item);

        // 아이템 정보들
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        imgUrl = findViewById(R.id.imgUrl);
        category = findViewById(R.id.category);
        timeRemaining = findViewById(R.id.timeInfo); // 추가

        // 수정 or 삭제
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);

        getSelectbItem();
        // 삭제 버튼 눌렀을 때
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
                Intent intent = new Intent(BiddingDetailItemActivity.this, BiddingActivity.class);
                String id = intent.getStringExtra("id");
                for(BiddingItem item: BiddingActivity.biddingItemList){
                    if(item.getId().equals(id)){

                        deleteDatabase(id);
                        startActivity(intent);
                        break;
                    }
                }
                // 아이템 정보를 다른 화면으로 전달하고 전환

            }
        });
        // 수정 버튼 눌렀을 때
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BiddingDetailItemActivity.this, MyItemsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setbValues(BiddingItem selectedItem) {
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText("0");
        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());

        // 시간 차이 계산 및 표시
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date futureTime = dateFormat.parse(selectedItem.getTime()); // 예정 시간
            Date currentTime = Calendar.getInstance().getTime(); // 현재 시간
            long timeDifferenceMillis = futureTime.getTime() - currentTime.getTime(); // 시간 차이 (밀리초)

            // 밀리초를 분과 초로 변환하여 표시
            long minutes = timeDifferenceMillis / (60 * 1000);
            long seconds = (timeDifferenceMillis % (60 * 1000)) / 1000;

            String timeRemainingText = String.format("%02d:%02d", minutes, seconds);
            timeRemaining.setText(timeRemainingText);

            // 시간 차이를 일(day)과 시간(hour) 형식으로 계산하여 timeInfo TextView에 설정
            long timeDifferenceSeconds = timeDifferenceMillis / 1000;
            long days = timeDifferenceSeconds / (24 * 60 * 60);
            long hours = (timeDifferenceSeconds % (24 * 60 * 60)) / 3600;

            String timeDifferenceText = String.format("%d일 %d시간", days, hours);
            timeInfo.setText(timeDifferenceText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // bidding Item 클릭 시 이벤트
    private void getSelectbItem(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        BiddingItem selectedItem = null;
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        for(BiddingItem item: BiddingActivity.biddingItemList){
            if(item.getId().equals(id)){
                selectedItem = item;
                break;
            }
        }
        if(selectedItem != null){
            // selectedItem 사용하기
            setbValues(selectedItem);
        }
        else{
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }

}
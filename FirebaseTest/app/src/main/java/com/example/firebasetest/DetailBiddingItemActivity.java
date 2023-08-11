package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailBiddingItemActivity extends AppCompatActivity {

    private Button btnEdit, btnDelete;
    private Button btnBid;
    private EditText bidText;
    private TextView itemId, startPrice, endPrice, itemInfo, seller, category;
    private ImageView imgUrl;
    private LinearLayout bidPopupLayout;
    BiddingItem item;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bidding_item);

        // 아이템 정보들
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
//        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        imgUrl = findViewById(R.id.imgUrl);
        category = findViewById(R.id.category);

        //입찰하기 버튼
        btnBid = findViewById(R.id.biddng_bid);
        bidPopupLayout = findViewById(R.id.bidPopupLayout);
        bidText = findViewById(R.id.bidAmountEditText);


        getSelectbItem();
        getSelectoItem();

        // 삭제 버튼 눌렀을 때
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
                Intent intent = new Intent(DetailBiddingItemActivity.this, BiddingActivity.class);
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
//        // 수정 버튼 눌렀을 때
//        btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//                Intent intent = new Intent(DetailBiddingItemActivity.this, MyItemsActivity.class);
//                startActivity(intent);
//            }
//        });
        // 입찰하기 버튼을 눌렀을 때 -> dialog 로 입력받을 수 있는 팝업창 띄움
        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidPopupLayout.setVisibility(View.VISIBLE);
            }
        });
        Button confirmBidButton = findViewById(R.id.confirmBidButton);
        Button cancelBidButton = findViewById(R.id.cancelBidButton);

        confirmBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bidAmount = bidText.getText().toString();
                if (!bidAmount.isEmpty()) {
                    // 컬렉션만들고 로직만들자리
                }
                bidPopupLayout.setVisibility(View.GONE);
            }
        });

        cancelBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidPopupLayout.setVisibility(View.GONE);
            }
        });
    }

    private  void setbValues(BiddingItem selectedItem){
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText("0");
        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        Glide.with(this)
                .load(selectedItem.getImageUrl())
                .into(imgUrl);
    }
    private  void setoValues(Item selectedItem){
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText(String.valueOf(selectedItem.getPrice()));
        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        Glide.with(this)
                .load(selectedItem.getImageUrl())
                .into(imgUrl);
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
    private void getSelectoItem(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Item selectedItem = null;
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        for(Item item: OpenAuctionActivity.openItemList){
            if(item.getId().equals(id)){
                selectedItem = item;
                break;
            }
        }
        if(selectedItem != null){
            // selectedItem 사용하기
            setoValues(selectedItem);
        }
        else{
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }

}
package com.example.skgudwls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {
    Goods selectedGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSelectedGoods(); // 선택한 상품정보 가져오기

        setValues(); // 가져온 정보 화면에 보여주기

        // 툴바 생성
        Toolbar toolbar = findViewById(R.id.next_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("상품 이름");
    }

    public boolean onOptionItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void setValues(){
        ImageView iv = findViewById(R.id.goods_detail_image);

        iv.setImageResource(selectedGoods.getImage());
    }

    private void getSelectedGoods(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        selectedGoods = PublicauctionActivity.goodsList.get(Integer.valueOf(id));
    }

//    @Override
//        public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_top, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.search:
//                // 여기에 입력
//                return true;
//            case R.id.menu:
//                // 여기에 입력
//                return true;
//        }
//        return false;
//    }
}
package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class OpenAuctionActivity extends AppCompatActivity {
    private Button btnRegistItem;
    ListView listView;

    private Button btnbck;
    public static OpenAuctionAdapter oitemAdapter;
    public static ArrayList<Item> openItemList = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_auction);

        listView = (ListView)findViewById(R.id.listView);
        oitemAdapter = new OpenAuctionAdapter(this, openItemList);
        listView.setAdapter(oitemAdapter);


        btnbck = findViewById(R.id.btn_back);
        InitializeOpenItem();
        btnRegistItem = findViewById(R.id.btn_registItem);
        // 아이템 등록 버튼 클릭 시 이벤트
        btnRegistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenAuctionActivity.this, OpenRegistItemActivity.class);
                startActivity(intent);
            }
        });
        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpenAuctionActivity.this, HomeActivity.class);
                startActivity(intent);
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
                Intent showDetail = new Intent(getApplicationContext(), DetailItemActivity.class);
                showDetail.putExtra("id", item.getId());
                startActivity(showDetail);
            }
        });
    }
    public void InitializeOpenItem(){
        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        openItemList.clear();
        // 임시 클래스에 담아둔 firestore의 데이터들을 불러와서 현재 페이지에 보여줌
        openItemList.addAll(UserDataHolderOpenItems.openItemList);
        // 새로운 데이터 갱신
        oitemAdapter.notifyDataSetChanged();
    }

}
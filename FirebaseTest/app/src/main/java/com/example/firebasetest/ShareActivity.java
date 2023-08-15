package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    public static ShareAdapterActivity saa;
    private Button btnRegistItem;
    ListView listView;
    private Button btnbck;
    public static ArrayList<Item> shareItemList = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        listView = (ListView) findViewById(R.id.listView);

        UserDataHolderShareItem.loadShareItems();
        saa = new ShareAdapterActivity(this, UserDataHolderShareItem.shareItemList);
        listView.setAdapter(saa);
        saa.notifyDataSetChanged();

        btnbck = findViewById(R.id.btn_back);

        btnRegistItem = findViewById(R.id.btn_registItem);
        // 아이템 등록 버튼 클릭 시 이벤트
        btnRegistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareActivity.this, ShareRegistItemActivity.class);
                startActivity(intent);
            }
        });

        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareActivity.this, HomeActivity.class);
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
                Intent showDetail = new Intent(getApplicationContext(), ShareDetailActivity.class);
                showDetail.putExtra("id", item.getId());
                showDetail.putExtra("title", item.getTitle());
                showDetail.putExtra("seller", item.getSeller());
                startActivity(showDetail);
            }
        });
    }


//    public void InitializeOpenItem() {
//        // 예비 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
//        shareItemList.clear();
//        // 임시 클래스에 담아둔 firestore의 데이터들을 불러와서 현재 페이지에 보여줌
//        shareItemList.addAll(UserDataHolderShareItem.shareItemList);
//        // 새로운 데이터 갱신
//        saa.notifyDataSetChanged();
//    }
}
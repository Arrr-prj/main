package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyTransactionActivity extends AppCompatActivity {
    private Button mBtnBackSpace, mBtnSell, mBtnBuy, mBtnFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transaction);

        mBtnBackSpace = findViewById(R.id.btn_backSpace);
        mBtnSell = findViewById(R.id.btn_mySell);
        mBtnBuy = findViewById(R.id.btn_myBuy);
        mBtnFree = findViewById(R.id.btn_myFree);

        // 뒤로가기 버튼을 눌렀을 때
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTransactionActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
        // 판매한 아이템 클릭 시 이벤트
        mBtnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTransactionActivity.this, MyItemsActivity.class);
                startActivity(intent);
            }
        });
        // 구매한 아이템 클릭 시 이벤트
        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 아직 작동 안함 ( 경매 기능 추가되었을때 넣어주기 )
//                Intent intent = new Intent(MyTransactionActivity.this, MyItemsActivity.class);
//                startActivity(intent);
            }
        });
        // 무료 나눔 아이템 클릭 시 이벤트
        mBtnFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTransactionActivity.this, MyShareActivity.class);
                startActivity(intent);
            }
        });
    }
}
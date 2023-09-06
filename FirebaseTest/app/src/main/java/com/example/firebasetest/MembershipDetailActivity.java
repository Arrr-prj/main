package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MembershipDetailActivity extends AppCompatActivity {

    private Button mBtnBackSpace;
    private TextView choice, title, subTitle, body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_detail);

        choice = findViewById(R.id.tv_choice);
        title = findViewById(R.id.tv_membershipTitle);
        subTitle = findViewById(R.id.tv_membershipSubTitle);
        body = findViewById(R.id.tv_membershipBody);



        Intent intent = getIntent();
        String choiceType = intent.getStringExtra("choiceType");


        if(choiceType.equals("알림")){
            choice.setText("알 림 기 능");
            title.setText("Arrr 회원이라면, 카테고리 별로 알림 설정이 가능!");
            subTitle.setText("");
            body.setText("내가 알림받고 싶은 카테고리를 선택해서 그 카테고리 경매가 등록되었을 때 알림을 받을 수 있습니다.");
        }

        if(choiceType.equals("이벤트")){
            choice.setText("Arrr 경매");
            title.setText("Arrr 회원이라면, Arrr 경매 참여 가능!");
            subTitle.setText("(Arrr경매는 Arrr ! 에서 개최하는 이벤트 경매입니다.)");
            body.setText("Arrr경매에서는 회원이 아닌 사람들이 부러워할만한 물건을 경매합니다.");
        }

        if(choiceType.equals("수수료")){
            choice.setText("ArrrPay 수수료 무료");
            title.setText("Arrr 회원이라면, ArrrPay 사용시 수수료 무료!");
            subTitle.setText("(ArrrPay는 Arrr ! 에서 사용되는 결제 시스템입니다.)");
            body.setText("중고거래 사기를 방지하기 위한 ArrrPay의 수수료가 무료가 됩니다.");
        }

        mBtnBackSpace = findViewById(R.id.btn_backSpace);
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
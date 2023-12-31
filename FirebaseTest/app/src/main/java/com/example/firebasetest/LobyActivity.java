package com.example.firebasetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobyActivity extends AppCompatActivity {

    private Button mBtnRegister, mBtnLogin, mBtnKakaoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loby);

        mBtnRegister = findViewById(R.id.btn_registerScreen); // 회원가입 버튼
        mBtnLogin = findViewById(R.id.btn_loginScreen); // 로그인 버튼
        mBtnKakaoLogin = findViewById(R.id.btn_kakaoLoginScreen); // 카카오 로그인 버튼

        // 회원가입 버튼을 눌렀을 때
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 인텐트 생성 후 이동
                Intent intent = new Intent(LobyActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 로그인 버튼을 눌렀을 때
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 인텐트 생성 후 이동
                Intent intent = new Intent(LobyActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 카카오 로그인 버튼을 클릭했을 때
        mBtnKakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // 카카오 로그인 url 선언
//                String url = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=6a5618b5907079f2ecf86363b3e26637&redirect_uri=https:http://localhost:8080";
//
//                // 웹 페이지로 이동하는 인텐트 생성 후 이동
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
//
//                // 콜백 함수 생성 필요
            }
        });
    }

    // Loby에서 뒤로가기 했을 때 경고 창 띄워주기
    @Override
    public void onBackPressed(){
        // 팝업 창 띄워서 확인 눌렀을 때 나가지도록
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("지금 종료하시겠습니까?"); // 다이얼로그 제목
        builder.setCancelable(false);   // 다이얼로그 화면 밖 터치 방지
        builder.setPositiveButton("예", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                exit();
            }
        });

        builder.setNegativeButton("아니요", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show(); // 다이얼로그 보이기
    }
    public void exit() { // 종료
        super.onBackPressed();
    }

}
package com.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    EditText editTextName;
    EditText editTextPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editName);
        editTextPrice = findViewById(R.id.editPrice);

        // 버튼 클릭 이벤트
        Button button = findViewById(R.id.buttonPay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에 입력한 상품 정보를 가져온다.
                String name = editTextName.getText().toString();
                String price = editTextPrice.getText().toString();

                // 결제가 이루어지는 PayActivity를 생성한다.
                // - 생성자를 이용하여 상품 정보를 입력한다.
                PayActivity payActivity = new PayActivity(name, price);

                // Intent로 새로운 Activity를 실행한다.
                Intent intent = new Intent(getApplicationContext(), payActivity.getClass());
                startActivity(intent);
            }
        });

    }
}
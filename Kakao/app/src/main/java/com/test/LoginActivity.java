package com.test;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("Debug", Utility.INSTANCE.getKeyHash(this));

        // SDK 초기화
        KakaoSdk.init(this, "33cc68257af444cd087a37f01186fc45");

        textView = findViewById(R.id.textView);

        // 로그인 버튼 클릭 이벤트
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApiClient.getInstance().loginWithKakaoAccount(getApplicationContext(), new Function2<OAuthToken, Throwable, Unit>() {
                    @Override
                    public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {

                        if (throwable != null) {
                            Log.e("Debug", "로그인 실패!");
                        } else if (oAuthToken != null) {
                            Log.e("Debug", "로그인 성공!");
                            // 로그인 성공 시 사용자 정보 받기
                            UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                                @Override
                                public Unit invoke(User user, Throwable throwable) {
                                    if (throwable != null) {
                                        Log.e("Deubg", "사용자 정보 받기 실패!");
                                    } else if (user != null) {
                                        Log.e("Debug", "사용자 정보 받기 성공!");
                                        String nickName = user.getKakaoAccount().getProfile().getNickname();
                                        textView.setText(nickName);
                                    }
                                    return null;
                                }
                            });
                        }
                        return null;
                    }
                });
            }
        });
    }
}
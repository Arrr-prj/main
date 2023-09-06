package com.example.firebasetest;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SellerInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private TextView mTvName, mTvEmail, mTvAddress, name;
    Intent intent;

    private Button mBtnBackSpace,btnSellerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_info);

        mBtnBackSpace = findViewById(R.id.btn_backSpace);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        mTvName = findViewById(R.id.tv_nickname);
        mTvEmail = findViewById(R.id.tv_email);
        mTvAddress = findViewById(R.id.tv_address);
        btnSellerItem = findViewById(R.id.sellerItem);
        name = findViewById(R.id.textView);

        intent = getIntent();
        String sellerId = intent.getStringExtra("sellerId");

        loadUserData(sellerId);
        UserDataHolderOpenItems.openItemList.clear();
        // OpenItemList, BiddingItemList, 무료 나눔 세팅
        UserDataHolderBiddingItems.loadBiddingItems();
        UserDataHolderOpenItems.loadOpenItems();
        UserDataHolderShareItem.loadShareItems();
        db.collection("User").whereEqualTo("email", sellerId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : (task.getResult())) {
                            String sellerNickname = document.getString("nickname");
                            name.setText(sellerNickname + "님");
                        }
                    }
                });

        // 뒤로가기 버튼을 눌렀을 때
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 판매자의 상품보기 버튼 눌렀을 떄
        btnSellerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerInfoActivity.this, SellerItemActivity.class);
                intent.putExtra("sellerId",sellerId);
                startActivity(intent);
            }
        });
    }

    // firestore에서 데이터 가져오는 메서드
    private void loadUserData(String sellerId) {
        db.collection("User")
                .whereEqualTo("email", sellerId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // 찾은 문서에 대한 처리를 수행합니다.
                                String uid = document.getId();
                                DocumentReference userDocRef = db.collection("User").document(uid);

                                userDocRef.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    // 문서가 존재할 때 데이터 가져오기
                                                    String name = documentSnapshot.getString("nickname");
                                                    String email = documentSnapshot.getString("email");
                                                    String address = documentSnapshot.getString("address");

                                                    mTvName.setText("닉네임   :  " + name);
                                                    mTvEmail.setText("이메일 :  " + email);
                                                    mTvAddress.setText("주소   :  " + address);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // 데이터 가져오기 실패 처리
                                                Toast.makeText(SellerInfoActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                        }
                    }
                });
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(SellerInfoActivity.this, HomeActivity.class));
    }
}

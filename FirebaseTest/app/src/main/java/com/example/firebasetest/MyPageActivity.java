package com.example.firebasetest;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import javax.security.auth.callback.Callback;

public class MyPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private TextView mTvName, mTvEmail, mTvAddress;
    private ImageView myImageView;
    private Uri imageUrl;

    private Button mBtnBackSpace, mBtnModify, mBtnLogout, mBtnWithdrawal, mBtnMyItem, mBtnMembership, mBtnAnnounce, mBtnSales, mBtnTotal;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ActivityResultLauncher<Intent> activityResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mBtnBackSpace = findViewById(R.id.btn_backSpace);
        mBtnModify = findViewById(R.id.btn_modify);
        mBtnLogout = findViewById(R.id.btn_logout);
        mBtnWithdrawal = findViewById(R.id.btn_withdrawal);
//        mBtnMyItem = findViewById(R.id.btn_myItem);
        mBtnMembership = findViewById(R.id.btn_membership);
//        mBtnAnnounce = findViewById(R.id.btn_announce);
        mBtnSales = findViewById(R.id.btn_sales);
        mBtnTotal = findViewById(R.id.btn_totalA);
        myImageView = findViewById(R.id.mypage_myimage); // 이미지 뷰 변수

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        mTvName = findViewById(R.id.tv_name);
        mTvEmail = findViewById(R.id.tv_email);
        mTvAddress = findViewById(R.id.tv_address);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        loadUserData();
        UserDataHolderOpenItems.openItemList.clear();
        // OpenItemList, BiddingItemList, 무료 나눔 세팅
        UserDataHolderBiddingItems.loadBiddingItems();
        UserDataHolderOpenItems.loadOpenItems();
        UserDataHolderShareItem.loadShareItems();


        // 이미지 URL 가져오기
        String uid = UserManager.getInstance().getUserUid();
        if (uid != null) {
            DocumentReference userDocRef = db.collection("User").document(uid);
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String profilePictureUrl = documentSnapshot.getString("profile picture");
                    if (profilePictureUrl != null) {// 이미지 로드 및 표시
                        Glide.with(this).load(profilePictureUrl).into(myImageView);
                    } else { // 프로필 사진이 null일 때 기본 이미지 저장
                        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/arrr-48e3f.appspot.com/o/qweqweqweqweqweqweqweqweqwe.png?alt=media&token=64c01f5b-5354-4e32-b47b-7cb85fcf9726").into(myImageView);
                    }
                }
            }).addOnFailureListener(e -> {
                // 데이터 가져오기 실패 처리
                Toast.makeText(MyPageActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
            });
        }

        myImageView.setOnClickListener(new View.OnClickListener() { // 프로필 사진 업로드
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                activityResult.launch(intent);
            }
        });



        activityResult = registerForActivityResult( // 프로필 사진 업로드 (파이어베이스 스토리지에 저장)
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();

                            // Firebase Storage에 이미지 업로드
                            StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());
                            UploadTask uploadTask = imageRef.putFile(selectedImageUri);

                            uploadTask.addOnSuccessListener(taskSnapshot -> {
                                // 업로드 성공한 경우 이미지 URL 가져오기
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();

                                    // 이미지 URL을 Firebase Firestore의 유저 문서에 업데이트
                                    String uid = UserManager.getInstance().getUserUid();
                                    if (uid != null) {
                                        DocumentReference userDocRef = db.collection("User").document(uid);
                                        userDocRef.update("profile picture", imageUrl)
                                                .addOnSuccessListener(aVoid -> {
                                                    // 업데이트 성공한 경우 이미지 뷰에 표시
                                                    Glide.with(MyPageActivity.this).load(imageUrl).into(myImageView);
                                                })
                                                .addOnFailureListener(e -> {
                                                    // 업데이트 실패 처리
                                                    Toast.makeText(MyPageActivity.this, "프로필 사진 업데이트 실패", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                            }).addOnFailureListener(e -> {
                                // 업로드 실패 처리
                                Toast.makeText(MyPageActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });





        // 수정 버튼을 눌렀을 때
        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, ModifyActivity.class);
                startActivity(intent);
            }
        });
        // 경매 아이템 눌렀을 때
//        mBtnMyItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MyPageActivity.this, MyTransactionActivity.class);
//                startActivity(intent);
//            }
//        });
        // 뒤로가기 버튼을 눌렀을 때
        mBtnBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        // 로그아웃 버튼을 눌렀을 때
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogClick(view);
            }
        });

        // 회원탈퇴 버튼을 눌렀을 때
        mBtnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = UserManager.getInstance().getUserUid();
                FirebaseUser user = mAuth.getCurrentUser();

                deleteUserDataFromFirestore(uid);

                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MyPageActivity.this, "회원탈퇴 완료", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MyPageActivity.this, LobyActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

        // 멤버십 가입하기 버튼을 눌렀을 때
        mBtnMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyPageActivity.this, MembershipActivity.class));
            }
        });
        // 공지사항 버튼 눌렀을 때
//        mBtnAnnounce.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MyPageActivity.this, AnnounceActivity.class));
//            }
//        });


    }

    // firestore에서 데이터 가져오는 메서드
    private void loadUserData() {
        String uid = UserManager.getInstance().getUserUid(); // uid 가져오기

        if (uid != null) {
            DocumentReference userDocRef = db.collection("User").document(uid);

            userDocRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Boolean adminValue = documentSnapshot.getBoolean("admin");
                            if (documentSnapshot.exists()) {
                                // 문서가 존재할 때 데이터 가져오기
                                String name = documentSnapshot.getString("name");
                                String email = documentSnapshot.getString("email");
                                String address = documentSnapshot.getString("address");

                                mTvName.setText("이름   :  " + name);
                                mTvEmail.setText("이메일 :  " + email);
                                mTvAddress.setText("주소   :  " + address);
                            }
                            if (adminValue != null && adminValue) {
                                mBtnSales.setVisibility(View.VISIBLE);
                                mBtnTotal.setVisibility(View.VISIBLE);
                                // 매출 현황 눌렀을 때
                                mBtnSales.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(MyPageActivity.this, SalesActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                // 전체 경매 버튼 눌렀을 때
                                mBtnTotal.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(MyPageActivity.this, TotalAuctionActivity.class));
                                    }
                                });
                            } else {
                                mBtnSales.setVisibility(View.INVISIBLE);
                                mBtnTotal.setVisibility(View.INVISIBLE);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 데이터 가져오기 실패 처리
                            Toast.makeText(MyPageActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void deleteUserDataFromFirestore(String uid) {
        DocumentReference userDocRef = db.collection("User").document(uid);

        userDocRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Firestore에서 사용자 데이터 삭제 성공
                    }
                });
    }

    public void DialogClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout").setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyPageActivity.this, HomeActivity.class));
    }


}

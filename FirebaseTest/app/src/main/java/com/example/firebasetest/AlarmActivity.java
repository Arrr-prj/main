package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Notification> alarmList;
    private ListView listView;
    private Button btnbck;
    private ImageButton btnCategory;
    private LinearLayout categoryLayout;
    private Switch nike, adidas, apple, samsung, car, acc, clo, limi, pre, shoes, goods, furn, sports, game, another, bag;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        btnbck = findViewById(R.id.btn_back);

        btnCategory = findViewById(R.id.btn_category);
        btnCategory.setOnClickListener((View.OnClickListener) this);

        categoryLayout = findViewById(R.id.rr_drawer);
        LinearLayout rightArea = findViewById(R.id.rr_right_area);
        rightArea.setOnClickListener((View.OnClickListener)this);

        nike = findViewById(R.id.sw_1);
        adidas = findViewById(R.id.sw_2);
        apple = findViewById(R.id.sw_3);
        samsung = findViewById(R.id.sw_4);
        car = findViewById(R.id.sw_5);
        limi = findViewById(R.id.sw_6);
        pre = findViewById(R.id.sw_7);
        furn = findViewById(R.id.sw_8);
        clo = findViewById(R.id.sw_9);
        acc = findViewById(R.id.sw_10);
        shoes = findViewById(R.id.sw_11);
        bag = findViewById(R.id.sw_12);
        sports = findViewById(R.id.sw_13);
        goods = findViewById(R.id.sw_14);
        game = findViewById(R.id.sw_15);
        another = findViewById(R.id.sw_16);



        // Firebase Firestore 인스턴스 초기화
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 사용자 UID 가져오기
        String uid = FirebaseAuth.getInstance().getUid();


        // 사용자 문서에 접근
        DocumentReference userDocRef = db.collection("User").document(uid);

        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Listen failed: " + e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    // 스위치 상태를 가져와서 각 스위치에 적용
                    car.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("차량")));
                    acc.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("액세서리")));
                    clo.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("의류")));
                    furn.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("가구 인테리어")));

                    nike.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("Nike")));
                    adidas.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("Adidas")));
                    apple.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("Apple")));
                    samsung.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("Samsung")));

                    limi.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("한정판")));
                    pre.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("프리미엄")));
                    shoes.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("신발")));
                    goods.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("굿즈")));

                    bag.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("가방")));
                    sports.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("스포츠 레저")));
                    game.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("취미 게임")));
                    another.setChecked(Boolean.TRUE.equals(snapshot.getBoolean("기타")));




                    boolean membership = Boolean.TRUE.equals(snapshot.getBoolean("membership"));
                    if (membership) {
                        // 어떤 처리를 수행
                        setSwitchListeners(userDocRef);
                    }
                    // 스위치 상태 변경 이벤트 리스너 설정
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });


        car.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("차량", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("차량", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });

        acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("액세서리", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("액세서리", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });


        clo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("의류", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("의류", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });


        furn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("가구 인테리어", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("가구 인테리어", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });




        nike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("Nike", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("Nike", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        adidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("Adidas", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("Adidas", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        apple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("Apple", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("Apple", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        samsung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("Samsung", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("Samsung", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        limi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("한정판", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("한정판", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        pre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("프리미엄", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("프리미엄", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        shoes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("신발", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("신발", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        bag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("가방", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("가방", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        sports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("스포츠 레저", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("스포츠 레저", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        goods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("굿즈", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("굿즈", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        game.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("취미 게임", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("취미 게임", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });
        another.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("기타", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("기타", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });



        // alarmList 초기화
        alarmList = new ArrayList<>();

        // InitializeAlarm 메서드 호출
        InitializeAlarm();

        listView = findViewById(R.id.listView);
        NotificationAdapter notificationAdapter = new NotificationAdapter(this, alarmList);

        listView.setAdapter(notificationAdapter);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout); // Correct the ID

        btnbck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setUpOnClickListener();


    }

    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Notification notification = (Notification) listView.getItemAtPosition(position);

                String itemType = notification.getItemType();
                String itemTitle = notification.getItemTitle(); // 예시로 아이템 타이틀 가져오기

                if (itemType.equals("OpenItem")) {

                    DocumentReference itemDocRef = db.collection(itemType).document(itemTitle);
                    itemDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Intent showDetail = new Intent(getApplicationContext(), OpenDetailItemActivity.class);
                                showDetail.putExtra("documentId", itemTitle);
                                showDetail.putExtra("seller", documentSnapshot.getString("seller"));
                                showDetail.putExtra("buyer", documentSnapshot.getString("buyer"));
                                showDetail.putExtra("itemType", documentSnapshot.getString("itemType"));
                                showDetail.putExtra("confirm", documentSnapshot.getBoolean("confirm"));
                                showDetail.putExtra("futureMillis", documentSnapshot.getString("futureMillis"));
                                startActivity(showDetail);
                                finish();
                            } else {
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
                if (itemType.equals("BiddingItem")) {
                    DocumentReference itemDocRef = db.collection(itemType).document(itemTitle);
                    itemDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Intent showDetail = new Intent(getApplicationContext(), BiddingDetailItemActivity.class);
                                showDetail.putExtra("documentId", itemTitle);
                                showDetail.putExtra("seller", documentSnapshot.getString("seller"));
                                showDetail.putExtra("buyer", documentSnapshot.getString("buyer"));
                                showDetail.putExtra("itemType", documentSnapshot.getString("itemType"));
                                showDetail.putExtra("confirm", documentSnapshot.getBoolean("confirm"));
                                showDetail.putExtra("futureMillis", documentSnapshot.getString("futureMillis"));
                                startActivity(showDetail);
                            } else {
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                }
                if (itemType.equals("ShareItem")) {
                    DocumentReference itemDocRef = db.collection(itemType).document(itemTitle);
                    itemDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Intent showDetail = new Intent(getApplicationContext(), ShareDetailActivity.class);
                                showDetail.putExtra("documentId", itemTitle);
                                showDetail.putExtra("seller", documentSnapshot.getString("seller"));
                                showDetail.putExtra("buyer", documentSnapshot.getString("buyer"));
                                showDetail.putExtra("itemType", documentSnapshot.getString("itemType"));
                                showDetail.putExtra("confirm", documentSnapshot.getBoolean("confirm"));
                                showDetail.putExtra("futureMillis", documentSnapshot.getString("futureMillis"));
                                startActivity(showDetail);
                            } else {
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

                }
                if (itemType.equals("EventItem")) {
                    DocumentReference itemDocRef = db.collection(itemType).document(itemTitle);
                    itemDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Intent showDetail = new Intent(getApplicationContext(), EventAuctionDetailItemActivity.class);
                                showDetail.putExtra("documentId", itemTitle);
                                showDetail.putExtra("seller", documentSnapshot.getString("seller"));
                                showDetail.putExtra("buyer", documentSnapshot.getString("buyer"));
                                showDetail.putExtra("itemType", documentSnapshot.getString("itemType"));
                                showDetail.putExtra("confirm", documentSnapshot.getBoolean("confirm"));
                                showDetail.putExtra("futureMillis", documentSnapshot.getString("futureMillis"));
                                startActivity(showDetail);
                            } else {
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
        });
    }
    public void InitializeAlarm() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // 기존 아이템 리스트 비워주기
        alarmList.clear();
        db.collection("notifications")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : (task.getResult())) {
                            String title = String.valueOf(document.getData().get("title"));
                            String message = String.valueOf(document.getData().get("message"));
                            String time = String.valueOf(document.getData().get("time"));
                            String userId = String.valueOf(document.getData().get("userId")); // userId 가져오기
                            String itemTitle = String.valueOf(document.getData().get("itemTitle"));
                            String itemType = String.valueOf(document.getData().get("itemType"));

                            String currentUserUid = currentUser.getUid(); // uid 가져오기

                            if (currentUserUid.equals(userId)) { // 현재 로그인한 사용자와 비교
                                alarmList.add(new Notification(title, message, time, itemTitle, itemType));
                            }
                        }

                        Collections.sort(alarmList, new Comparator<Notification>() { // 리스트를 정렬
                            @Override
                            public int compare(Notification n1, Notification n2) {
                                return n2.getTimestamp().compareTo(n1.getTimestamp()); // 역순으로 정렬
                            }
                        });


                        NotificationAdapter notificationAdapter = new NotificationAdapter(this, alarmList);
                        listView.setAdapter(notificationAdapter);
                    }
                });
    }

    @Override
    public void onClick(@Nullable View pO){
        if(pO != null){
            switch(pO.getId()){
                case R.id.btn_category:
                    if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                        drawerLayout.closeDrawer(GravityCompat.END);
                    }else{
                        drawerLayout.openDrawer(GravityCompat.END);
                    }
                    break;
                case R.id.rr_right_area:
                    if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                        drawerLayout.closeDrawer(GravityCompat.END);
                    }
                    break;
            }
        }
    }
    // 각 스위치들에 대한 상태 변경 이벤트 리스너 설정 메소드
    private void setSwitchListeners(final DocumentReference userDocRef) {
        car.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("차량", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        acc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("액세서리", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        clo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("의류", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        furn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("가구 인테리어", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        nike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("Nike", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        adidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("Adidas", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        apple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("Apple", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        samsung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("Samsung", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        limi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("한정판", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        pre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("프리미엄", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        shoes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("신발", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        bag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("가방", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        sports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("스포츠 레저", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        game.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("취미 게임", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });
        another.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("기타", isChecked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {}
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
            }
        });


    }


}
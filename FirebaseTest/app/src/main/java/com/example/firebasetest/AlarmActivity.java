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
    Switch car,acc,home,art,clo,cur,food,furn;
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

        car = findViewById(R.id.sw_1); // 차량 카테고리 스위치
        acc = findViewById(R.id.sw_2); // 액세서리 스위치
        home = findViewById(R.id.sw_3); // 가전제품 스위치
        art = findViewById(R.id.sw_4); // 예술품 스위치
        clo = findViewById(R.id.sw_5); // 의류 스위치
        cur = findViewById(R.id.sw_6); // 골동품 스위치
        food = findViewById(R.id.sw_7); // 식품 스위치
        furn = findViewById(R.id.sw_8); // 가구 스위치

        // Firebase Firestore 인스턴스 초기화
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 사용자 UID 가져오기
        String uid = UserManager.getInstance().getUserUid();

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
                    car.setChecked(snapshot.getBoolean("차량"));
                    acc.setChecked(snapshot.getBoolean("액세서리"));
                    home.setChecked(snapshot.getBoolean("가전제품"));
                    art.setChecked(snapshot.getBoolean("예술품"));
                    clo.setChecked(snapshot.getBoolean("의류"));
                    cur.setChecked(snapshot.getBoolean("골동품"));
                    food.setChecked(snapshot.getBoolean("식품"));
                    furn.setChecked(snapshot.getBoolean("가구"));
                    boolean membership = snapshot.getBoolean("membership");
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

        home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("가전제품", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("가전제품", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });

        art.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("예술품", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("예술품", false).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        cur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("골동품", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("골동품", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {}});
                }
            }
        });

        food.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // 차량 카테고리 on
                    userDocRef.update("식품", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("식품", false).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    userDocRef.update("가구", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {}}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {}});
                } else { // 차량 카테고리 off
                    userDocRef.update("가구", false).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("가전제품", isChecked)
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
        art.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("예술품", isChecked)
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
        cur.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("골동품", isChecked)
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
        food.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userDocRef.update("식품", isChecked)
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
                userDocRef.update("가구", isChecked)
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

        // 나머지 스위치들도 동일하게 설정
        // ...
    }


}
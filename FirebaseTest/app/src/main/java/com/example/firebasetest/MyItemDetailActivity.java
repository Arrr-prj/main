package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyItemDetailActivity extends AppCompatActivity {

    private Button btnEdit, btnDelete, btnList, btnConfirm, btnCancle;
    private TextView itemTitle, itemId, startPrice, endPrice, itemInfo, seller, category;
    private ImageView imgUrl1, imgUrl2, imgUrl3, imgUrl4, imgUrl5, imgUrl6;
    ListAdapter listAdapter;
    FirebaseUser firebaseUser;
    private ViewPager2 sliderViewPager;
//    private PhotoView photoViewSlider;
    private LinearLayout layoutIndicator;



    private String[] images = new String[6];
    public static ArrayList<Item> biddingItemList = new ArrayList<Item>();
    public static ArrayList<Item> openItemList = new ArrayList<Item>();
    public static ArrayList<Item> eventItemList = new ArrayList<Item>();
    String imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6;
    Item item;
    Bitmap bitmap;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_detail);
        sliderViewPager = findViewById(R.id.sliderViewPager);

        sliderViewPager.setOffscreenPageLimit(1);
        // 아이템 정보들
        itemTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        category = findViewById(R.id.category);

        listAdapter = new ListAdapter(this, new ArrayList<>());
        listAdapter = new ListAdapter(this, new ArrayList<>());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();
        // 수정 or 삭제
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);
        btnList = findViewById(R.id.btn_list);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancle = findViewById(R.id.btn_cancle);

        // 페이지 접속 시 새로 로딩해준다.


        UserDataHolderOpenItems.openItemList.clear();
        UserDataHolderBiddingItems.loadBiddingItems();
        UserDataHolderOpenItems.loadOpenItems();

        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        biddingItemList.clear();
        // 임시 클래스에 담아둔 firestore의 데이터들을 불러옴
        biddingItemList.addAll(UserDataHolderBiddingItems.biddingItemList);
        Log.d(TAG, "" + biddingItemList.size());

        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        openItemList.clear();
        // 임시 클래스에 담아둔 firestore의 데이터들을 불러옴
        openItemList.addAll(UserDataHolderOpenItems.openItemList);
        Log.d(TAG, "" + openItemList.size());

        String uid = UserManager.getInstance().getUserUid(); // 현재 사용자의 uid
        db = FirebaseFirestore.getInstance();


        Intent intent = getIntent();
        String state = intent.getStringExtra("state");
        if(state.equals("On")){
            getSelectoItem();
        }else if(state.equals("Off")){
            getSelectbItem();
            // 이벤트 아이템인 경우
        }else if(state.equals("Event")){
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            getSelecteItem();
        }
        if (imageUrl1 != null && !imageUrl1.isEmpty()) {
            images[0] = imageUrl1;
        }
        if (imageUrl2 != null && !imageUrl2.isEmpty()) {
            images[1] = imageUrl2;
        }
        if (imageUrl3 != null && !imageUrl3.isEmpty()) {
            images[2] = imageUrl3;
        }
        if (imageUrl4 != null && !imageUrl4.isEmpty()) {
            images[3] = imageUrl4;
        }
        if (imageUrl5 != null && !imageUrl5.isEmpty()) {
            images[4] = imageUrl5;
        }
        if (imageUrl6 != null && !imageUrl6.isEmpty()) {
            images[5] = imageUrl6;
        }
        // 구매한 아이템 수정 삭제 막기
        String isBuy = intent.getStringExtra("isBuy");
        if(isBuy.equals("Buy")){
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }else if(isBuy.equals("Confirm")){
            btnConfirm.setVisibility(View.VISIBLE);
            btnCancle.setVisibility(View.VISIBLE);
        }
        else{
        }

        // 낙찰 확인 버튼 클릭 시 이벤트
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // 전달받은 documentId를 변수에 저장
                Intent intent = getIntent();
                String documentId = intent.getStringExtra("documentId");

                // 비공개 아이템 리스트 중 글 제목+판매자 이메일 같으면 선택된 아이템을 변수에 담아둔다
                for (Item item : UserDataHolderBiddingItems.biddingItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        CollectionReference biddingItemCollection = db.collection("BiddingItem");
                        // update() 이용하여 해당 데이터 confirm : false -> true
                        biddingItemCollection.whereEqualTo("buyer", uid)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            // 해당 문서의 참조 가져오기
                                            DocumentReference documentReference = biddingItemCollection.document(document.getId());

                                            // 업데이트할 데이터 생성
                                            Map<String, Object> updates = new HashMap<>();
                                            updates.put("confirm", true);

                                            // 문서 업데이트
                                            documentReference.update(updates)
                                                    .addOnSuccessListener(aVoid ->{
                                                        btnConfirm.setVisibility(View.GONE);
                                                        btnCancle.setVisibility(View.VISIBLE);
                                                        // notify도 해줘야하나?
                                                        listAdapter.notifyDataSetChanged();
                                                        // 수정된 리스트 새로 갱신
                                                        UserDataHolderBiddingItems.loadBiddingItems();
                                                        Toast.makeText(MyItemDetailActivity.this, "상품이 낙찰되었습니다.", Toast.LENGTH_SHORT).show();
                                                        btnConfirm.setText("구매 완료");

                                                        startActivity(new Intent(MyItemDetailActivity.this, MyConfirmItemActivity.class));
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(MyItemDetailActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                    } else {
                    }
                }
                // 공개 아이템 리스트 중 글 제목+판매자 이메일 같으면 선택된 아이템을 변수에 담아둔다
                for (Item item : UserDataHolderOpenItems.openItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        CollectionReference openItemCollection = db.collection("OpenItem");
                        // update() 이용하여 해당 데이터 confirm : false -> true
                        openItemCollection.whereEqualTo("buyer", uid)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            // 해당 문서의 참조 가져오기
                                            DocumentReference documentReference = openItemCollection.document(document.getId());

                                            // 업데이트할 데이터 생성
                                            Map<String, Object> updates = new HashMap<>();
                                            updates.put("confirm", true);

                                            // 문서 업데이트
                                            documentReference.update(updates)
                                                    .addOnSuccessListener(aVoid ->{
                                                        btnConfirm.setVisibility(View.GONE);
                                                        btnCancle.setVisibility(View.GONE);
                                                        // notify도 해줘야하나?
                                                        listAdapter.notifyDataSetChanged();
                                                        // 수정된 리스트 새로 갱신
                                                        UserDataHolderOpenItems.loadOpenItems();
                                                        Toast.makeText(MyItemDetailActivity.this, "상품이 낙찰되었습니다.", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(MyItemDetailActivity.this, MyConfirmItemActivity.class));
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(MyItemDetailActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });

                    } else {
                    }
                }
                // 이벤트 아이템 리스트 중 글 제목+판매자 이메일 같으면 선택된 아이템을 변수에 담아둔다
                for (Item item : UserDataHolderEventItems.eventItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        CollectionReference biddingItemCollection = db.collection("BiddingItem");
                        // update() 이용하여 해당 데이터 confirm : false -> true
                        biddingItemCollection.whereEqualTo("buyer", uid)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            // 해당 문서의 참조 가져오기
                                            DocumentReference documentReference = biddingItemCollection.document(document.getId());

                                            // 업데이트할 데이터 생성
                                            Map<String, Object> updates = new HashMap<>();
                                            updates.put("confirm", true);

                                            // 문서 업데이트
                                            documentReference.update(updates)
                                                    .addOnSuccessListener(aVoid ->{
                                                        btnConfirm.setVisibility(View.GONE);
                                                        btnCancle.setVisibility(View.GONE);
                                                        // notify도 해줘야하나?
                                                        listAdapter.notifyDataSetChanged();
                                                        // 수정된 리스트 새로 갱신
                                                        UserDataHolderEventItems.loadEventItems();
                                                        Toast.makeText(MyItemDetailActivity.this, "상품이 낙찰되었습니다.", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(MyItemDetailActivity.this, MyEventActivity.class));
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(MyItemDetailActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                });
                    } else {
                    }
                }
            }
        });

        // 낙찰 포기 버튼 클릭 시 이벤트
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyItemDetailActivity.this, "낙찰을 포기했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 목록 버튼 클릭 시 이벤트
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 삭제 버튼 눌렀을 때
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // 전달받은 documentId를 변수에 저장
                Intent intent = getIntent();
                String documentId = intent.getStringExtra("documentId");
                // 공개, 비공개 아이템 클릭 시 담아둘 변수 set
                Item selectedItem = null;
                Item seletedOItem = null;

                // 비공개 아이템 리스트 중 글 제목+판매자 이메일 같으면 선택된 아이템을 변수에 담아둔다
                for (Item item : UserDataHolderBiddingItems.biddingItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        selectedItem = item;
                        // delete() 이용하여 해당 데이터 삭제
                        db.collection("BiddingItem").document(selectedItem.getTitle() + selectedItem.getSeller())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    BiddingActivity.biddingItemList.remove(item);
                                    listAdapter.notifyDataSetChanged();
                                    // 삭제된 리스트 새로 갱신
                                    UserDataHolderBiddingItems.loadBiddingItems();
                                    Toast.makeText(MyItemDetailActivity.this, "상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(MyItemDetailActivity.this, MyItemsActivity.class);
                                    startActivity(intent1);
                                }).addOnFailureListener(e -> {
                                            Toast.makeText(MyItemDetailActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                );
                        break;
                    } else {
                    }
                }
                // 공개 아이템 리스트 중 글 제목+판매자 이메일 같으면 선택된 아이템을 변수에 담아둔다
                for (Item item : UserDataHolderOpenItems.openItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        seletedOItem = item;
                        // delete() 이용하여 해당 데이터 삭제
                        db.collection("OpenItem").document(seletedOItem.getTitle() + seletedOItem.getSeller())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    MyItemDetailActivity.openItemList.remove(item);
                                    listAdapter.notifyDataSetChanged();
                                    // 삭제된 리스트 새로 갱신
                                    UserDataHolderOpenItems.loadOpenItems();
                                    Toast.makeText(MyItemDetailActivity.this, "상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(MyItemDetailActivity.this, MyItemsActivity.class);
                                    startActivity(intent1);

                                }).addOnFailureListener(e -> {
                                    Toast.makeText(MyItemDetailActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                });
                        break;
                    } else {
                    }
                }
            }
        });
        // 수정 버튼 눌렀을 때
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String documentId = intent.getStringExtra("documentId");
                Item selectedItem = null;
                Item selectedoItem = null;
                // BiddingItemList에서 선택한 아이템 검색
                for (Item item : UserDataHolderBiddingItems.biddingItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        selectedItem = item;
                        break;
                    }
                }

                // openItemList에서 선택한 아이템 검색
                for (Item item : UserDataHolderOpenItems.openItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        selectedoItem = item;
                        break;
                    }
                }
                if (selectedItem != null) {
                    UserDataHolderBiddingItems.loadBiddingItems();
                    Toast.makeText(MyItemDetailActivity.this, "비공개 아이템 수정", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), EditItemActivity.class);
                    intent1.putExtra("state", "bidding");
                    // 여기서 해당 유저의 이메일을 불러와서 해당 아이템의 글 제목과 이메일이 documentId와 일치하는지 넘겨서 확인해줌
                    intent1.putExtra("documentId", selectedItem.getTitle() + firebaseUser.getEmail());
                    startActivity(intent1);
                } else if (selectedoItem != null) {
                    UserDataHolderOpenItems.loadOpenItems();
                    Log.d(TAG, "selectedoItem.getSeller()의 값 : "+selectedoItem.getSeller());
                    Toast.makeText(MyItemDetailActivity.this, "공개 아이템 수정", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), EditItemActivity.class);
                    intent1.putExtra("state", "open");
                    intent1.putExtra("documentId", selectedoItem.getTitle() + firebaseUser.getEmail());
                    startActivity(intent1);
                }
            }
        });
    }

    // bidding Item 클릭 시 이벤트
    private void getSelectbItem() {
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, "" + documentId);
        biddingItemList.clear();
        UserDataHolderBiddingItems.loadBiddingItems();

        db.collection("BiddingItem").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (/*task.isSuccessful()*/true) {
                            DocumentSnapshot document = task.getResult();
                            String get_itemTitle = document.getString("title");
                            String get_category = document.getString("category");
                            String get_id = document.getString("id");
                            String get_info = document.getString("info");
                            String get_price = document.getString("price");
                            if (true/*document.exists()*/) {
                                // 이미지 URL들을 images 배열에 저장
                                imageUrl1 = document.getString("imgUrl1");
                                imageUrl2 = document.getString("imgUrl2");
                                imageUrl3 = document.getString("imgUrl3");
                                imageUrl4 = document.getString("imgUrl4");
                                imageUrl5 = document.getString("imgUrl5");
                                imageUrl6 = document.getString("imgUrl6");
                                // 이미지 URL들을 images 배열에 저장
                                images[0] = imageUrl1;
                                images[1] = imageUrl2;
                                images[2] = imageUrl3;
                                images[3] = imageUrl4;
                                images[4] = imageUrl5;
                                images[5] = imageUrl6;
                                // 이미지 슬라이더 어댑터 업데이트
                                ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(MyItemDetailActivity.this, images);
                                sliderViewPager.setAdapter(imageSliderAdapter);
                                itemTitle.setText(get_itemTitle);
                                itemId.setText(get_id);
                                itemInfo.setText(get_info);
                                category.setText(get_category);
                                startPrice.setText(get_price);
                                endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
                                seller.setText(firebaseUser.getEmail());


                            } else {
                                Toast.makeText(MyItemDetailActivity.this, "데이터 없음", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MyItemDetailActivity.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "bidding Item이 아님 -> open Item 임");
                    }
                });
    }

    private void getSelectoItem() {
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, "" + documentId);
        Item selectedItem = null;
        // 기존 리스트 없애주고 다시 로드해준 뒤 for문 돌리도록
        openItemList.clear();

        UserDataHolderOpenItems.loadOpenItems();
        db.collection("OpenItem").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        String get_itemTitle = document.getString("title");
                        String get_category = document.getString("category");
                        String get_id = document.getString("id");
                        String get_info = document.getString("info");
                        String get_price = document.getString("price");
                        String get_url = document.getString("imgUrl");
                        // 이미지 URL들을 images 배열에 저장
                        imageUrl1 = document.getString("imgUrl1");
                        imageUrl2 = document.getString("imgUrl2");
                        imageUrl3 = document.getString("imgUrl3");
                        imageUrl4 = document.getString("imgUrl4");
                        imageUrl5 = document.getString("imgUrl5");
                        imageUrl6 = document.getString("imgUrl6");
                        // 이미지 URL들을 images 배열에 저장
                        images[0] = imageUrl1;
                        images[1] = imageUrl2;
                        images[2] = imageUrl3;
                        images[3] = imageUrl4;
                        images[4] = imageUrl5;
                        images[5] = imageUrl6;
                        // 이미지 슬라이더 어댑터 업데이트
                        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(MyItemDetailActivity.this, images);

                        sliderViewPager.setAdapter(imageSliderAdapter);
                        itemTitle.setText(get_itemTitle);
                        itemId.setText(get_id);
                        itemInfo.setText(get_info);
                        category.setText(get_category);
                        startPrice.setText(get_price);
                        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
                        seller.setText(firebaseUser.getEmail());

                    }
                });
    }
    // event Item
    private void getSelecteItem() {
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, "" + documentId);
        Item selectedItem = null;
        // 기존 리스트 없애주고 다시 로드해준 뒤 for문 돌리도록
        eventItemList.clear();

        UserDataHolderOpenItems.loadOpenItems();
        db.collection("EventItem").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        String get_itemTitle = document.getString("title");
                        String get_category = document.getString("category");
                        String get_id = document.getString("id");
                        String get_info = document.getString("info");
                        String get_price = document.getString("price");
                        String get_url = document.getString("imgUrl");
                        // 이미지 URL들을 images 배열에 저장
                        imageUrl1 = document.getString("imgUrl1");
                        imageUrl2 = document.getString("imgUrl2");
                        imageUrl3 = document.getString("imgUrl3");
                        imageUrl4 = document.getString("imgUrl4");
                        imageUrl5 = document.getString("imgUrl5");
                        imageUrl6 = document.getString("imgUrl6");
                        // 이미지 URL들을 images 배열에 저장
                        images[0] = imageUrl1;
                        images[1] = imageUrl2;
                        images[2] = imageUrl3;
                        images[3] = imageUrl4;
                        images[4] = imageUrl5;
                        images[5] = imageUrl6;
                        // 이미지 슬라이더 어댑터 업데이트
                        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(MyItemDetailActivity.this, images);

                        sliderViewPager.setAdapter(imageSliderAdapter);
                        itemTitle.setText(get_itemTitle);
                        itemId.setText(get_id);
                        itemInfo.setText(get_info);
                        category.setText(get_category);
                        startPrice.setText(get_price);
                        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
                        seller.setText(firebaseUser.getEmail());

                    }
                });
    }

}
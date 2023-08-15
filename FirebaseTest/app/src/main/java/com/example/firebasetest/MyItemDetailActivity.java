package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyItemDetailActivity extends AppCompatActivity {

    private Button btnEdit, btnDelete;
    private TextView itemTitle, itemId, startPrice, endPrice, itemInfo, seller, category;
    private ImageView imgUrl;
    BiddingItemAdapter biddingItemAdapter;
    FirebaseUser firebaseUser;

    FirebaseUser user;

    private FirebaseFirestore db;
    OpenAuctionAdapter openAuctionAdapter;
    public BiddingItemAdapter bitemAdapter;
    public OpenAuctionAdapter oitemAdapter;

    public static ArrayList<BiddingItem> biddingItemList = new ArrayList<BiddingItem>();
    public static ArrayList<Item> openItemList = new ArrayList<Item>();

    BiddingItem item;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_detail);

        // 아이템 정보들
        itemTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        imgUrl = findViewById(R.id.imgUrl);
        category = findViewById(R.id.category);
        biddingItemAdapter = new BiddingItemAdapter(this, new ArrayList<>());
        openAuctionAdapter = new OpenAuctionAdapter(this, new ArrayList<>());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();
        // 수정 or 삭제
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);

        // 페이지 접속 시 새로 로딩해준다.
        Toast.makeText(this, "clear 전 size" + UserDataHolderOpenItems.openItemList.size(), Toast.LENGTH_SHORT).show();

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

        Intent intent = getIntent();
        String state = intent.getStringExtra("state");
        if(state.equals("On")){
            getSelectoItem();
        }else{
            getSelectbItem();
        }
        // 삭제 버튼 눌렀을 때
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // 전달받은 documentId를 변수에 저장
                Intent intent = getIntent();
                String documentId = intent.getStringExtra("documentId");
                // 공개, 비공개 아이템 클릭 시 담아둘 변수 set
                BiddingItem selectedItem = null;
                Item seletedOItem = null;

                // 비공개 아이템 리스트 중 글 제목+판매자 이메일 같으면 선택된 아이템을 변수에 담아둔다
                for (BiddingItem item : UserDataHolderBiddingItems.biddingItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        selectedItem = item;
                        // delete() 이용하여 해당 데이터 삭제
                        db.collection("BiddingItem").document(selectedItem.getTitle() + selectedItem.getSeller())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    BiddingActivity.biddingItemList.remove(item);
                                    biddingItemAdapter.notifyDataSetChanged();
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
                                    openAuctionAdapter.notifyDataSetChanged();
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
                BiddingItem selectedItem = null;
                Item selectedoItem = null;
                // BiddingItemList에서 선택한 아이템 검색
                for (BiddingItem item : UserDataHolderBiddingItems.biddingItemList) {
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
                    intent1.putExtra("documentId", selectedItem.getTitle() + firebaseUser.getEmail());
                    startActivity(intent1);
                } else if (selectedoItem != null) {
                    UserDataHolderOpenItems.loadOpenItems();
                    Toast.makeText(MyItemDetailActivity.this, "공개 아이템 수정", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), EditItemActivity.class);
                    intent1.putExtra("documentId", selectedoItem.getTitle() + firebaseUser.getEmail());
                    startActivity(intent1);
                }
            }
        });
    }

    private void setbValues(BiddingItem selectedItem) {
        itemTitle.setText(selectedItem.getTitle());
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText("0");
        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        Glide.with(this)
                .load(selectedItem.getImageUrl())
                .into(imgUrl);
    }

    private void setoValues(Item selectedItem) {
        itemTitle.setText(selectedItem.getTitle());
        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText(String.valueOf(selectedItem.getPrice()));
        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        Glide.with(this)
                .load(selectedItem.getImageUrl())
                .into(imgUrl);
    }

    // bidding Item 클릭 시 이벤트
    private void getSelectbItem() {
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, "" + documentId);
        BiddingItem selectedItem = null;
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
                            String get_url = document.getString("imgUrl");
                            if (true/*document.exists()*/) {
                                itemTitle.setText(get_itemTitle);
                                itemId.setText(get_id);
                                itemInfo.setText(get_info);
                                category.setText(get_category);
                                startPrice.setText(get_price);
                                endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
                                seller.setText(firebaseUser.getEmail());
                                Glide.with(MyItemDetailActivity.this)
                                        .load(get_url)
                                        .into(imgUrl);

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
        Toast.makeText(this, "clear 후 size" + openItemList.size(), Toast.LENGTH_SHORT).show();
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

                        itemTitle.setText(get_itemTitle);
                        itemId.setText(get_id);
                        itemInfo.setText(get_info);
                        category.setText(get_category);
                        startPrice.setText(get_price);
                        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************


                        seller.setText(firebaseUser.getEmail());


                        Glide.with(MyItemDetailActivity.this)
                                .load(get_url)
                                .into(imgUrl);

                    }
                });
    }

}
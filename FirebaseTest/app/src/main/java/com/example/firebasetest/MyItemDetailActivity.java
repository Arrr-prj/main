package com.example.firebasetest;

import static android.content.ContentValues.TAG;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyItemDetailActivity extends AppCompatActivity {

    private Button btnEdit, btnDelete;
    private TextView itemTitle, itemId, startPrice, endPrice, itemInfo, seller, category;
    private ImageView imgUrl;
    BiddingItemAdapter biddingItemAdapter;
    OpenAuctionAdapter openAuctionAdapter;
    public BiddingItemAdapter bitemAdapter;
    public OpenAuctionAdapter oitemAdapter;

    public static ArrayList<BiddingItem> biddingItemList = new ArrayList<BiddingItem>();
    public static ArrayList<Item> openItemList = new ArrayList<Item>();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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

        // 수정 or 삭제
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);

        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        biddingItemList.clear();
        // 임시 클래스에 담아둔 firestore의 데이터들을 불러옴
        biddingItemList.addAll(UserDataHolderBiddingItems.biddingItemList);
        Log.d(TAG, ""+biddingItemList.size());

        // 기존 아이템 리스트 비워줘서 로딩할때 다시 기존 리스트들이 추가되지 않도록 방지
        openItemList.clear();
        // 임시 클래스에 담아둔 firestore의 데이터들을 불러옴
        openItemList.addAll(UserDataHolderOpenItems.openItemList);
        Log.d(TAG, ""+openItemList.size());


        getSelectbItem();
        getSelectoItem();
        // 삭제 버튼 눌렀을 때
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                BiddingItem selectedItem = null;
                Item seletedOItem = null;

                for (BiddingItem item : UserDataHolderBiddingItems.biddingItemList) {
                    if (item.getId().equals(id)) {
                        selectedItem = item;
                        db.collection("BiddingItem").whereEqualTo("id", selectedItem.getId())
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String documentId = document.getId();
                                            db.collection("BiddingItem").document(documentId)
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        BiddingActivity.biddingItemList.remove(item);
                                                        biddingItemAdapter.notifyDataSetChanged();
                                                        // 삭제된 리스트 새로 갱신
                                                        UserDataHolderBiddingItems.loadBiddingItems();
                                                        Toast.makeText(MyItemDetailActivity.this, "아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                        Intent intent1 = new Intent(MyItemDetailActivity.this, MyItemsActivity.class);
                                                        startActivity(intent1);
                                                    });
                                        }
                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(MyItemDetailActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                });
                        break;
                    } else {
                        Log.d(TAG, "일치하는 아이디가 없습니다.");
                    }
                }
                for (Item item : UserDataHolderOpenItems.openItemList) {
                    if (item.getId().equals(id)) {
                        seletedOItem = item;
                        db.collection("OpenItem").whereEqualTo("id", seletedOItem.getId())
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String documentId = document.getId();
                                            db.collection("OpenItem").document(documentId)
                                                    .delete()
                                                    .addOnSuccessListener(aVoid -> {
                                                        MyItemDetailActivity.openItemList.remove(item);
                                                        openAuctionAdapter.notifyDataSetChanged();
                                                        // 삭제된 리스트 새로 갱신
                                                        UserDataHolderOpenItems.loadOpenItems();
                                                        Toast.makeText(MyItemDetailActivity.this, "아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                        Intent intent1 = new Intent(MyItemDetailActivity.this, MyItemsActivity.class);
                                                        startActivity(intent1);
                                                    });
                                        }
                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(MyItemDetailActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                });
                        break;
                    } else {
                        Log.d(TAG, "일치하는 아이디가 없습니다.");
                    }
                }
            }
        });
        // 수정 버튼 눌렀을 때
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                BiddingItem selectedItem = null;
                Item selectedoItem = null;
// BiddingItem 목록에서 선택한 아이템 검색
                for (BiddingItem item : UserDataHolderBiddingItems.biddingItemList) {
                    if(item.getId() != null){
                        if(item.getId().equals(id)){
                            selectedItem = item;
                            break;
                        }
                    }
                }

                // openItemList에서 선택한 아이템 검색
                for (Item item : UserDataHolderOpenItems.openItemList) {
                    if(item.getId() != null){
                        if(item.getId().equals(id)){
                            selectedoItem = item;
                            break;
                        }
                    }
                }
                if (selectedItem != null) {
                    Toast.makeText(MyItemDetailActivity.this, "비공개 아이템 수정 모드", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), EditItemActivity.class);
                    intent1.putExtra("id", selectedItem.getId());
                    startActivity(intent1);
                }
                else if (selectedoItem != null) {
                    Toast.makeText(MyItemDetailActivity.this, "공개 아이템 수정 모드", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), EditItemActivity.class);
                    intent1.putExtra("id", selectedoItem.getId());
                    startActivity(intent1);
                }
            }
        });
    }

    private  void setbValues(BiddingItem selectedItem){
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
    private  void setoValues(Item selectedItem){
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
    private void getSelectbItem(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.d(TAG, ""+id);
        BiddingItem selectedItem = null;
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        for(BiddingItem item: UserDataHolderBiddingItems.biddingItemList){
            if(item.getId() != null){
                if(item.getId().equals(id)){
                    selectedItem = item;
                    break;
                }
            }
        }
        if(selectedItem != null){
            // selectedItem 사용하기
            setbValues(selectedItem);
        }
        else{
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }
    private void getSelectoItem(){

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.d(TAG, ""+id);
        Item selectedItem = null;
        for(Item item: UserDataHolderOpenItems.openItemList){
            if(item.getId() != null){
                if(item.getId().equals(id)){
                    selectedItem = item;
                    break;
                }
            }
        }
        Toast.makeText(MyItemDetailActivity.this, "openItemList size : "+OpenAuctionActivity.openItemList.size(), Toast.LENGTH_SHORT).show();

        if(selectedItem != null){
            // selectedItem 사용하기
            setoValues(selectedItem);
        }
        else{
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }

}
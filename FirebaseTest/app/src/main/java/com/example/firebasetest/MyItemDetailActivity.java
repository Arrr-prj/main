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

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        getSelectbItem(id);
        getSelectoItem(id);

        // 삭제 버튼 눌렀을 때
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                BiddingItem selectedItem = null;
                Item seletedOItem = null;

                for (BiddingItem item : BiddingActivity.biddingItemList) {
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
                                                        Toast.makeText(MyItemDetailActivity.this, "아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                        Intent intent1 = new Intent(MyItemDetailActivity.this, BiddingActivity.class);
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
                for (Item item : OpenAuctionActivity.openItemList) {
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
                                                        OpenAuctionActivity.openItemList.remove(item);
                                                        openAuctionAdapter.notifyDataSetChanged();
                                                        Toast.makeText(MyItemDetailActivity.this, "아이템이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                        Intent intent1 = new Intent(MyItemDetailActivity.this, OpenAuctionActivity.class);
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
                Item seletedOItem = null;

                for (BiddingItem item : BiddingActivity.biddingItemList) {
                    if (item.getId().equals(id)) {
                        selectedItem = item;

                        db.collection("BiddingItem").whereEqualTo("id", selectedItem.getId())
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Intent showEditPage = new Intent(getApplicationContext(), EditItemActivity.class);
                                            showEditPage.putExtra("id", item.getId());
                                            startActivity(showEditPage);
                                        }
                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(MyItemDetailActivity.this, "수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                });
                        break;
                    } else {
                        Log.d(TAG, "일치하는 아이디가 없습니다.");
                    }
                }
                for (Item item : OpenAuctionActivity.openItemList) {
                    if (item.getId().equals(id)) {
                        seletedOItem = item;

                        db.collection("OpenItem").whereEqualTo("id", seletedOItem.getId())
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String documentId = document.getId();
                                            Intent showEditPage = new Intent(getApplicationContext(), EditItemActivity.class);
                                            showEditPage.putExtra("documentId", documentId);
                                            startActivity(showEditPage);
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
    }

    private void setbValues(BiddingItem selectedItem){

        itemId.setText(selectedItem.getId());
        itemInfo.setText(selectedItem.getInfo());
        category.setText(selectedItem.getCategory());
        startPrice.setText(selectedItem.getPrice());
        endPrice.setText("0"); // 낙찰가 설정 방법 구상 필요 **************
        seller.setText(selectedItem.getSeller());
        Glide.with(this)
                .load(selectedItem.getImageUrl())
                .into(imgUrl);
    }
    private  void setoValues(Item selectedItem){
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
    private void getSelectbItem(String id){

        Toast.makeText(MyItemDetailActivity.this, "id:"+id, Toast.LENGTH_SHORT).show();
        BiddingItem selectedItem = null;
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        for(BiddingItem item: BiddingActivity.biddingItemList){
            Toast.makeText(this, "bItem 검사"+item.getId(), Toast.LENGTH_SHORT).show();
            if(item.getId().equals(id)){
                selectedItem = item;
                break;
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
    private void getSelectoItem(String id){
        Toast.makeText(MyItemDetailActivity.this, "id:"+id, Toast.LENGTH_SHORT).show();
        Item selectedItem = null;
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        for(Item item: OpenAuctionActivity.openItemList){
            Toast.makeText(this, "oItem 검사"+item.getId(), Toast.LENGTH_SHORT).show();
            if(item.getId().equals(id)){
                selectedItem = item;
                break;
            }
        }
        if(selectedItem != null){
            // selectedItem 사용하기
            setoValues(selectedItem);
        }
        else{
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }

}
package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyShareDetailActivity extends AppCompatActivity {
    public static ArrayList<Item> shareItemList = new ArrayList<Item>();
    private Button btnEdit, btnDelete, btnList;
    private FirebaseFirestore db;
    private TextView itemTitle, itemId, startPrice, endPrice, itemInfo, seller, category;
    private ImageView imgUrl;
    FirebaseUser firebaseUser;

    ShareAdapter shareAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share_detail);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // 아이템 정보들
        itemTitle = findViewById(R.id.itemTitle);
        itemId = findViewById(R.id.itemId);
        startPrice = findViewById(R.id.startPrice);
        endPrice = findViewById(R.id.endPrice);
        itemInfo = findViewById(R.id.itemInfo);
        seller = findViewById(R.id.seller);
        imgUrl = findViewById(R.id.imgUrl);
        category = findViewById(R.id.category);

        // 수정 or 삭제
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);
        btnList = findViewById(R.id.btn_list);

        Intent intent = getIntent();
        String state = intent.getStringExtra("state");
        // 나눔 받은 아이템인 경우
        if(state.equals("On")){
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        // 나눔한 아이템인 경우
        }else if(state.equals("Off")) {
        }else{
        }

        // 페이지 접속 시 새로 로딩해준다.
        Toast.makeText(this, "clear 전 size" + UserDataHolderShareItem.shareItemList.size(), Toast.LENGTH_SHORT).show();

        UserDataHolderShareItem.shareItemList.clear();
        UserDataHolderShareItem.loadShareItems();

        shareItemList.clear();
        shareItemList.addAll(UserDataHolderShareItem.shareItemList);
        getSelectfItem();

        // 목록 버튼 클릭 시 이벤트
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyShareDetailActivity.this, MyShareActivity.class);
                startActivity(intent);
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

                // 비공개 아이템 리스트 중 글 제목+판매자 이메일 같으면 선택된 아이템을 변수에 담아둔다
                for (Item item : UserDataHolderShareItem.shareItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        selectedItem = item;
                        // delete() 이용하여 해당 데이터 삭제
                        db.collection("ShareItem").document(selectedItem.getTitle() + selectedItem.getSeller())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    BiddingActivity.biddingItemList.remove(item);
                                    shareAdapter.notifyDataSetChanged();
                                    // 삭제된 리스트 새로 갱신
                                    UserDataHolderShareItem.loadShareItems();
                                    Toast.makeText(MyShareDetailActivity.this, "상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(MyShareDetailActivity.this, MyShareActivity.class);
                                    startActivity(intent1);
                                }).addOnFailureListener(e -> {
                                            Toast.makeText(MyShareDetailActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                );
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
                // BiddingItemList에서 선택한 아이템 검색
                for (Item item : UserDataHolderShareItem.shareItemList) {
                    if (documentId.equals(item.getTitle() + item.getSeller())) {
                        selectedItem = item;
                        break;
                    }
                }

                if (selectedItem != null) {
                    UserDataHolderShareItem.loadShareItems();
                    Toast.makeText(MyShareDetailActivity.this, "무료 아이템 수정", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), EditItemActivity.class);
                    intent1.putExtra("state", "share");
                    intent1.putExtra("documentId", selectedItem.getTitle() + firebaseUser.getEmail());
                    startActivity(intent1);
                }
            }
        });
    }


    // share Item 클릭 시 이벤트
    private void getSelectfItem() {
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, "" + documentId);
        // 기존 리스트 없애주고 다시 로드해준 뒤 for문 돌리도록
        shareItemList.clear();
        Toast.makeText(this, "clear 후 size" + shareItemList.size(), Toast.LENGTH_SHORT).show();
        UserDataHolderShareItem.loadShareItems();
        db.collection("ShareItem").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot document = task.getResult();
                        Toast.makeText(MyShareDetailActivity.this, "title : "+document.getString("title"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MyShareDetailActivity.this, "info : "+document.getString("info"), Toast.LENGTH_SHORT).show();

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
                        Glide.with(MyShareDetailActivity.this)
                                .load(get_url)
                                .into(imgUrl);

                    }
                });
    }
}
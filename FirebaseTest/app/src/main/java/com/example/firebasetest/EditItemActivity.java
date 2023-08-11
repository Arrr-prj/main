package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditItemActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    TextView write_text;
    EditText itemName, itemPrice, itemInfo;
    Button itemCategory;
    BiddingItemAdapter biddingItemAdapter;
    OpenAuctionAdapter openAuctionAdapter;
    private Uri imageUrl;
    String url;
    ImageView imageView;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference().child("image");
    private String[] categories = {"카테고리 1", "카테고리 2", "카테고리 3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        itemName = findViewById(R.id.input_itemName);
        itemPrice = findViewById(R.id.input_itemPrice);
        itemInfo = findViewById(R.id.input_itemExplain);
        itemCategory = findViewById(R.id.input_itemCategory);
        write_text = findViewById(R.id.write_text);

        imageView = findViewById(R.id.input_itemImg);

        String name = getIntent().getStringExtra("id");
        itemName.setText(name);
        String info = getIntent().getStringExtra("info");
        itemInfo.setHint(info);
        String category = getIntent().getStringExtra("category");
        itemCategory.setHint(category);
        url = getIntent().getStringExtra("imgUrl");
        Glide.with(this).load(url).into(imageView);

        // 카테고리 버튼 클릭 시 이벤트
        itemCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

        // 등록 버튼 클릭 시 이벤트 ( 수정 )
        Button registBtn = findViewById(R.id.btn_itemRegist);
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToFirebase();
            }
        });

    }
    // 파이어베이스에 업로드하기
    private void updateToFirebase(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        BiddingItem selectedItem = null;
        String strPrice = itemPrice.getText().toString();
        String strInfo = itemInfo.getText().toString();

        String strCategory = itemCategory.getText().toString();

        for (BiddingItem item : BiddingActivity.biddingItemList) {
            if (item.getId().equals(id)) {
                selectedItem = item;

                db.collection("BiddingItem").whereEqualTo("id", selectedItem.getId())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentId = document.getId();
                                    Log.d(TAG, "documentId : "+documentId);
                                    if (url != null) {
                                        db.collection("BiddingItem").document(documentId).update("imgUrl", url);
                                    }
                                    if (strCategory != null) {
                                        db.collection("BiddingItem").document().update("category", strCategory);
                                    }
                                    if (strInfo != strInfo) {
                                        db.collection("BiddingItem").document().update("info", strInfo);
                                    }
                                    Toast.makeText(EditItemActivity.this, "아이템이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(EditItemActivity.this, BiddingActivity.class);
                                    startActivity(intent1);
                                }
                            }
                        });
                break;
            } else {
                Log.d(TAG, "일치하는 아이디가 없습니다.");
            }
        }
        Item selectedoItem = null;
        for (Item item : OpenAuctionActivity.openItemList) {
            if (item.getId().equals(id)) {
                selectedoItem = item;
                String price = getIntent().getStringExtra("price");
                itemPrice.setText(Integer.parseInt(price));
                db.collection("OpenItem").whereEqualTo("id", selectedItem.getId())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentId = document.getId();
                                    db.collection("OpenItem").document(documentId).update("imgUrl", url);
                                    db.collection("OpenItem").document().update("category", strCategory);
                                    db.collection("OpenItem").document().update("price", strPrice);
                                    db.collection("OpenItem").document().update("info", strInfo)
                                            .addOnSuccessListener(aVoid -> {
                                                biddingItemAdapter.notifyDataSetChanged();
                                                Toast.makeText(EditItemActivity.this, "아이템이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                                Intent intent1 = new Intent(EditItemActivity.this, BiddingActivity.class);
                                                startActivity(intent1);
                                            });
                                }
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(EditItemActivity.this, "수정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        });
                break;
            } else {
                Log.d(TAG, "일치하는 아이디가 없습니다.");
            }
        }
    }
    public void showDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("카테고리 선택")
                .setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedCategory = categories[which];
                        // 선택한 카테고리 값을 TextView에 할당
                        itemCategory.setText(selectedCategory);
                    }
                })
                .setNegativeButton("취소", null); // 취소 버튼
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
package com.example.firebasetest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class BiddingRegistItemActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView write_text;
    EditText itemTitle, itemName, itemPrice, itemInfo;
    Button itemCategory;
    private ImageView imageView;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference().child("image");

    private Uri imageUrl;
    private String[] categories = {"카테고리 1", "카테고리 2", "카테고리 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding_regist_item);
        // 컴포넌트 객체에 담기
        itemTitle = findViewById(R.id.input_title);
        itemName = findViewById(R.id.input_itemName);
        itemPrice = findViewById(R.id.input_itemPrice);
        itemInfo = findViewById(R.id.input_itemExplain);
        itemCategory = findViewById(R.id.input_itemCategory);
        write_text = findViewById(R.id.write_text);

        imageView = findViewById(R.id.input_itemImg);

        // 이미지 클릭 이벤트
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                activityResult.launch(intent); // activityResult? launcher?
            }
        });
        // 카테고리 클릭 시 이벤트
        itemCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

        // 등록 클릭 시 이벤트
        Button registBtn = findViewById(R.id.btn_itemRegist);
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = itemTitle.getText().toString();
                String strName = itemName.getText().toString();
                String strPrice = itemPrice.getText().toString();
                String strInfo = itemInfo.getText().toString();
                String strCategory = itemCategory.getText().toString();
                String seller = firebaseUser.getEmail();
                // 쓰기
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                if(imageUrl != null){
                    uploadToFirebase(strTitle, imageUrl, strName, strPrice, strInfo, strCategory, seller);
                    Intent intent = new Intent(BiddingRegistItemActivity.this, BiddingActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(BiddingRegistItemActivity.this, "사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                }

                // 이미지 초기화
                imageView.setImageResource(R.drawable.ic_add_photo);
            }
        });

        // 아이템 리스트 버튼 클릭 이벤트
        Button listBtn = findViewById(R.id.btn_itemList);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BiddingRegistItemActivity.this, BiddingActivity.class);
                startActivity(intent);
            }
        });
    } // onCreate
    // 사진 가져오기
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK && result.getData() != null){
                        imageUrl = result.getData().getData();
                        imageView.setImageURI(imageUrl);
                    }
                }
            });
    // 파이어베이스 이미지 업로드

    private void uploadToFirebase(String strTitle, Uri uri, String strName, String strPrice, String strInfo, String strCategory, String sellerId) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Map<String, Object> data = new HashMap<>();
            data.put("title", strTitle);
            data.put("id", strName);
            data.put("price",strPrice);
            data.put("info", strInfo);
            data.put("category", strCategory);
            data.put("seller", sellerId);

            // 성공 시
            fileRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                // 이미지 아이템에 담기
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                data.put("imgUrl", uriResult.toString());

                DocumentReference userDocRef = db.collection("BiddingItem").document(strTitle + "time");
                userDocRef.set(data)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(BiddingRegistItemActivity.this, "상품 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BiddingRegistItemActivity.this, BiddingActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "상품 등록에 실패했습니다." + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        });
            });
        });
    }
    // 파일 타입 가져오기
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    // 다이얼로그를 보여주는 메서드
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
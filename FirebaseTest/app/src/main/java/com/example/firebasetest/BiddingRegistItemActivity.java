package com.example.firebasetest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class BiddingRegistItemActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    TextView write_text;
<<<<<<< Updated upstream
    EditText itemName, itemPrice, itemInfo, itemCategory;
=======
    EditText itemTitle, itemName, itemPrice, itemInfo, itemCategory;
>>>>>>> Stashed changes
    private ImageView imageView;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference().child("image");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Uri imageUrl;
    // 로그인된 사용자의 ID 가져오기
    private String sellerId = firebaseUser.getDisplayName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding_regist_item);
        // 컴포넌트 객체에 담기
        itemName = findViewById(R.id.input_itemName);
        itemPrice = findViewById(R.id.input_itemPrice);
        itemInfo = findViewById(R.id.input_itemExplain);
        itemCategory = findViewById(R.id.input_itemCategory);
        write_text = findViewById(R.id.write_text);

        imageView = findViewById(R.id.input_itemImg);

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
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

        // 등록 클릭 이벤트
        Button registBtn = findViewById(R.id.btn_itemRegist);
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = itemName.getText().toString();
                String strPrice = itemPrice.getText().toString();
                String strInfo = itemInfo.getText().toString();
                String strCategory = itemCategory.getText().toString();
                String seller = firebaseUser.getEmail();

                // 쓰기
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Items");
                if (imageUrl != null) {
<<<<<<< Updated upstream
                    uploadToFirebase(imageUrl, strName, strPrice, strInfo, strCategory, seller, currentTime);
=======
                    uploadToFirebase(strTitle, imageUrl, strName, strPrice, strInfo, strCategory, seller);
>>>>>>> Stashed changes
                    Intent intent = new Intent(BiddingRegistItemActivity.this, BiddingActivity.class);
                    startActivity(intent);
                } else {
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

<<<<<<< Updated upstream
    private void uploadToFirebase(Uri uri, String strName, String strPrice, String strInfo, String strCategory, String sellerId, String currentTime){
=======
    private void uploadToFirebase(String strTitle, Uri uri, String strName, String strPrice, String strInfo, String strCategory, String sellerId ){
>>>>>>> Stashed changes
        StorageReference fileRef = reference.child("image");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Map<String, Object> data = new HashMap<>();
<<<<<<< Updated upstream
=======

                Calendar calendar = Calendar.getInstance(); // 1일 후의 시간 계산
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String futureMillis = String.valueOf(calendar.getTimeInMillis()); //
                // "yyyy-MM-dd HH:mm:ss" 포맷으로 변환
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(calendar.getTime());

                data.put("title",strTitle);
>>>>>>> Stashed changes
                data.put("id",strName);
                data.put("info", strInfo);
                data.put("category", strCategory);
                data.put("seller", sellerId);
                data.put("uploadTime", currentTime);
                // 성공 시
<<<<<<< Updated upstream
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // 이미지 아이템에 담기
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        data.put("imgUrl", uri.toString());

                        database.collection("BiddingItem").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference){
                                Toast.makeText(BiddingRegistItemActivity.this, "상품 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BiddingRegistItemActivity.this, BiddingActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "상품 등록에 실패했습니다."+e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
=======
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        // 이미지 아이템에 담기
//                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//
//                        data.put("imgUrl", uri.toString());
//
//                        DocumentReference userDocRef = db.collection("BiddingItem").document(strTitle + sellerId);
//                        database.collection("BiddingItem").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference){
//                                Toast.makeText(BiddingRegistItemActivity.this, "상품 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(BiddingRegistItemActivity.this, BiddingActivity.class);
//                                startActivity(intent);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getApplicationContext(), "상품 등록에 실패했습니다."+e.getMessage(),
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
                fileRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                    // 이미지 아이템에 담기
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    data.put("imgUrl", uriResult.toString());

                    DocumentReference userDocRef = db.collection("BiddingItem").document(strTitle + sellerId);
                    userDocRef.set(data).addOnSuccessListener(aVoid -> {
                                // 등록된 리스트 새로 갱신
                                UserDataHolderBiddingItems.loadBiddingItems();
                                Toast.makeText(BiddingRegistItemActivity.this, "상품 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BiddingRegistItemActivity.this, BiddingActivity.class);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "상품 등록에 실패했습니다." + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });
>>>>>>> Stashed changes
                });
            }
        });
    }
    // 파일 타입 가져오기
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
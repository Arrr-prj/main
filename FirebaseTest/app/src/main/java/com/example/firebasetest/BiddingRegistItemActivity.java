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
import android.util.Log;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BiddingRegistItemActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    TextView write_text;

    EditText itemTitle, itemName, itemPrice, itemInfo;
    Button itemCategory;
    private ImageView imageView;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference().child("image");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String[] categories = {"차량", "액세서리", "가전제품", "예술품", "의류", "골동품", "식품", "가구"};


    private Uri imageUrl;
    // 로그인된 사용자의 ID 가져오기
    private String sellerId = firebaseUser.getDisplayName();

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

        // 카테고리 클릭 시 이벤트
        itemCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view);
            }
        });

        // 등록 클릭 이벤트
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
                DatabaseReference myRef = database.getReference("Items");
                if (imageUrl != null) {

                    uploadToFirebase(strTitle, imageUrl, strName, strPrice, strInfo, strCategory, seller);

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

    private void uploadToFirebase(String strTitle, Uri uri, String strName, String strPrice, String strInfo, String strCategory, String sellerId ){

        StorageReference fileRef = reference.child("image");
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Map<String, Object> data = new HashMap<>();

                Calendar calendar = Calendar.getInstance(); // 1일 후의 시간 계산
                String uploadMillis = String.valueOf(calendar.getTimeInMillis());
                calendar.add(Calendar.SECOND, 30);
                String futureMillis = String.valueOf(calendar.getTimeInMillis()); //

                // "yyyy-MM-dd HH:mm:ss" 포맷으로 변환
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(calendar.getTime());
                data.put("title",strTitle);
                data.put("id",strName);
                data.put("info", strInfo);
                data.put("category", strCategory);
                if(Integer.parseInt(strPrice) >= 100){
                    data.put("price",strPrice);
                }else{
                    data.put("price","100");
                }
                if(Integer.parseInt(strPrice) >= 100){
                    data.put("endPrice",strPrice);
                }else{
                    data.put("endPrice","100");
                }
                if(Integer.parseInt(strPrice) >= 100){
                    data.put("endPrice",strPrice);
                }else{
                    data.put("endPrice","100");
                }
                data.put("seller", sellerId);
                data.put("buyer", ""); // 경매 끝났을때 uid 혹은 이메일 넣기
                data.put("futureMillis", futureMillis);
                data.put("futureDate", formattedDate);
                data.put("uploadMillis", uploadMillis);
                data.put("confirm", false);
                data.put("itemType", "BiddingItem");
                data.put("views", 0);
                // 성공 시

                fileRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                    // 이미지 아이템에 담기
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    data.put("imgUrl", uriResult.toString());

                    DocumentReference userDocRef = db.collection("BiddingItem").document(strTitle + sellerId);
                    userDocRef.set(data).addOnSuccessListener(aVoid -> {
                                // 등록된 리스트 새로 갱신
                                UserDataHolderBiddingItems.loadBiddingItems();
                                Toast.makeText(BiddingRegistItemActivity.this, "상품 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                sendMessage(strName,strCategory,firebaseUser,strTitle+sellerId);
                                Intent intent = new Intent(BiddingRegistItemActivity.this, BiddingActivity.class);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "상품 등록에 실패했습니다." + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });

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
    private void sendMessage(String strName, String strCategory, FirebaseUser firebaseUser,String title){
        db.collection("User").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.contains(strCategory) && documentSnapshot.contains("membership")) {
                            boolean member = documentSnapshot.getBoolean("membership");
                            if (member) {
                                boolean user = documentSnapshot.getBoolean(strCategory);
                                if (user) {
                                    String userID = documentSnapshot.getId();
                                    if(!userID.equals(firebaseUser.getUid())) {
                                        sendFCMToUsersForItem(strName, userID,title,strCategory);
                                    }
                                }
                            }
                        }
                    }
                });
    }
    private void sendFCMToUsersForItem(String itemName, String userID, String title, String category) {

        FirebaseUser fb = firebaseAuth.getInstance().getCurrentUser();
        Calendar calendar = Calendar.getInstance(); // 현재 시간 가져옴
        String millis = String.valueOf(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(calendar.getTime());


        db.collection("FCMTOKEN").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userToken = documentSnapshot.getString("token");// 로그인시에 저장되는 FCM토큰
                        if (userToken != null) {
                            // FCM 메시지 생성
                            Map<String, String> messageData = new HashMap<>();
                            messageData.put("title", category+" 경매알림");
                            messageData.put("body", itemName + "이(가) 비공개 경매에 올라왔습니다!");
                            Map<String, String> data = new HashMap<>();
                            data.put("title", messageData.get("title"));
                            data.put("message", messageData.get("body"));
                            data.put("uid", userToken);
                            data.put("time", formattedDate);
                            data.put("userId", userID);
                            data.put("itemTitle", title);
                            data.put("itemType", "BiddingItem");

                            db.collection("notifications")           // 문서 ID를 userID로 설정하여 문서에 접근 (컬렉션도 생성됨)
                                    .add(data)                  // 데이터를 문서에 저장
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("FCM", "메시지 저장 성공: " + userID);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("FCM", "메시지 저장 실패: " + e.getMessage());
                                    });
                        } else {
                            Log.d("FCM", "해당 사용자의 FCM 토큰이 없습니다.");
                        }
                    }
                });
    }
}
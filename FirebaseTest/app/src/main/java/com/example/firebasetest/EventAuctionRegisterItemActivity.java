package com.example.firebasetest;

import static android.content.ContentValues.TAG;

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
import android.os.CountDownTimer;
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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventAuctionRegisterItemActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CountDownTimer countDownTimer;
    TextView write_text;
    EditText itemTitle, itemName, itemPrice, itemInfo;
    Button itemCategory;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    String document, buyer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Uri imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private String[] categories = {"나이키", "아디다스", "애플", "삼성", "차량", "액세서리", "의류", "한정판", "프리미엄", "신발", "굿즈", "가방", "가구 / 인테리어", "스포츠 / 레저", "취미 / 게임", "기타"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_regist_item);
        // 컴포넌트 객체에 담기
        itemTitle = findViewById(R.id.input_title);
        itemName = findViewById(R.id.input_itemName);
        itemPrice = findViewById(R.id.input_itemPrice);
        itemInfo = findViewById(R.id.input_itemExplain);
        itemCategory = findViewById(R.id.input_itemCategory);
        write_text = findViewById(R.id.write_text);

        imageView1 = findViewById(R.id.input_itemImg1);
        imageView2 = findViewById(R.id.input_itemImg2);
        imageView3 = findViewById(R.id.input_itemImg3);
        imageView4 = findViewById(R.id.input_itemImg4);
        imageView5 = findViewById(R.id.input_itemImg5);
        imageView6 = findViewById(R.id.input_itemImg6);
        buyer = " ";

        Log.d(TAG, "<< 시작 가격 최소 금액은 100원입니다. >>");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String sellerName = intent.getStringExtra("seller");

        String documentId = title + sellerName;
        this.document = documentId;

        // 이미지 클릭 이벤트
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                activityResult1.launch(intent); // activityResult? launcher?
            }
        });
        // 이미지 클릭 이벤트
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                activityResult2.launch(intent); // activityResult? launcher?
            }
        });
        // 이미지 클릭 이벤트
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                activityResult3.launch(intent); // activityResult? launcher?
            }
        });
        // 이미지 클릭 이벤트
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                activityResult4.launch(intent); // activityResult? launcher?
            }
        });
        // 이미지 클릭 이벤트
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                activityResult5.launch(intent); // activityResult? launcher?
            }
        });
        // 이미지 클릭 이벤트
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                activityResult6.launch(intent); // activityResult? launcher?
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
                String sellerId = firebaseUser.getEmail();
                Log.d(TAG, "" + sellerId);
                // 쓰기
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Items");
                if (imageUrl1 != null && imageUrl2 != null && imageUrl3 != null && imageUrl4 != null && imageUrl5 != null && imageUrl6 != null) {
                    uploadToFirebase(strTitle, imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6, strName, strPrice, strInfo, strCategory, sellerId);
                    Intent intent = new Intent(EventAuctionRegisterItemActivity.this, EventAuctionActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EventAuctionRegisterItemActivity.this, "사진을 선택해주세요", Toast.LENGTH_SHORT).show();
                }

                // 이미지 초기화
                imageView1.setImageResource(R.drawable.ic_add_photo);
                imageView2.setImageResource(R.drawable.ic_add_photo);
                imageView3.setImageResource(R.drawable.ic_add_photo);
                imageView4.setImageResource(R.drawable.ic_add_photo);
                imageView5.setImageResource(R.drawable.ic_add_photo);
                imageView6.setImageResource(R.drawable.ic_add_photo);
            }
        });

        // 아이템 리스트 버튼 클릭 이벤트
        Button listBtn = findViewById(R.id.btn_itemList);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventAuctionRegisterItemActivity.this, EventAuctionActivity.class);
                startActivity(intent);
            }
        });
    }// onCreate

    // 사진 가져오기
    ActivityResultLauncher<Intent> activityResult1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUrl1 = result.getData().getData();
                        imageView1.setImageURI(imageUrl1);
                    }
                }
            });
    ActivityResultLauncher<Intent> activityResult2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUrl2 = result.getData().getData();
                        imageView2.setImageURI(imageUrl2);
                    }
                }
            });

    ActivityResultLauncher<Intent> activityResult3 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUrl3 = result.getData().getData();
                        imageView3.setImageURI(imageUrl3);
                    }
                }
            });
    ActivityResultLauncher<Intent> activityResult4 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUrl4 = result.getData().getData();
                        imageView4.setImageURI(imageUrl4);
                    }
                }
            });

    ActivityResultLauncher<Intent> activityResult5 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUrl5 = result.getData().getData();
                        imageView5.setImageURI(imageUrl5);
                    }
                }
            });

    ActivityResultLauncher<Intent> activityResult6 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUrl6 = result.getData().getData();
                        imageView6.setImageURI(imageUrl6);
                    }
                }
            });

    // 파이어베이스 이미지 업로드
    private void uploadToFirebase(String strTitle,Uri uri1, Uri uri2, Uri uri3, Uri uri4, Uri uri5, Uri uri6, String strName, String strPrice, String strInfo, String strCategory, String sellerId) {
        // 각 이미지의 StorageReference 생성
        StorageReference fileRef1 = reference.child(System.currentTimeMillis() + "_1." + getFileExtension(uri1));
        StorageReference fileRef2 = reference.child(System.currentTimeMillis() + "_2." + getFileExtension(uri2));
        StorageReference fileRef3 = reference.child(System.currentTimeMillis() + "_3." + getFileExtension(uri3));
        StorageReference fileRef4 = reference.child(System.currentTimeMillis() + "_4." + getFileExtension(uri4));
        StorageReference fileRef5 = reference.child(System.currentTimeMillis() + "_5." + getFileExtension(uri5));
        StorageReference fileRef6 = reference.child(System.currentTimeMillis() + "_6." + getFileExtension(uri6));
        // 각 이미지 업로드 및 이미지 URL 가져오기 작업을 리스트로 생성
        List<Task<Uri>> uploadTasks = new ArrayList<>();
        uploadTasks.add(uploadImageAndGetUrl(fileRef1, uri1));
        uploadTasks.add(uploadImageAndGetUrl(fileRef2, uri2));
        uploadTasks.add(uploadImageAndGetUrl(fileRef3, uri3));
        uploadTasks.add(uploadImageAndGetUrl(fileRef4, uri4));
        uploadTasks.add(uploadImageAndGetUrl(fileRef5, uri5));
        uploadTasks.add(uploadImageAndGetUrl(fileRef6, uri6));

        // 모든 이미지 업로드 및 URL 가져오기 작업이 완료되면 Firestore에 데이터 업로드
        Tasks.whenAllSuccess(uploadTasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> results) {
                // 모든 이미지의 URL을 가져온 후 Firestore 데이터 업로드
                Map<String, Object> data = new HashMap<>();
                Calendar calendar = Calendar.getInstance(); // 1일 후의 시간 계산
                String uploadMillis = String.valueOf(calendar.getTimeInMillis());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String futureMillis = String.valueOf(calendar.getTimeInMillis()); //
                // "yyyy-MM-dd HH:mm:ss" 포맷으로 변환
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(calendar.getTime());

                data.put("title", strTitle);
                data.put("id", strName);
                if (Integer.parseInt(strPrice) >= 100) {
                    data.put("price", strPrice);
                } else {
                    data.put("price", "100");
                }
                data.put("info", strInfo);
                data.put("category", strCategory);
                data.put("seller", sellerId);
                data.put("buyer", ""); // 경매 끝났을때 uid 혹은 이메일 넣기
                data.put("futureMillis", futureMillis);
                data.put("futureDate", formattedDate);
                data.put("uploadMillis", uploadMillis);
                data.put("confirm", false);
                data.put("itemType", "EventItem");
                data.put("views", 0);

                for (int i = 0; i < results.size(); i++) {
                    data.put("imgUrl" + (i + 1), results.get(i).toString());
                }

                // Firestore에 데이터 업로드
                DocumentReference userDocRef = db.collection("EventItem").document(strTitle + sellerId);

                userDocRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // 이미 해당 문서가 존재하므로 업데이트하지 않고 Toast 메시지를 표시
                            Toast.makeText(EventAuctionRegisterItemActivity.this, "이미 해당 상품이 등록되어 있습니다.\n도배 게시글 방지를 위해 등록이 제한됩니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 해당 문서가 존재하지 않으므로 데이터 업로드 수행
                            userDocRef.set(data)
                                    .addOnSuccessListener(aVoid -> {
                                        // 등록된 리스트 새로 갱신
                                        UserDataHolderEventItems.loadEventItems();
                                        Toast.makeText(EventAuctionRegisterItemActivity.this, "상품 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EventAuctionRegisterItemActivity.this, EventAuctionActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "상품 등록에 실패했습니다." + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
            }
        });
    }

    // 파일 타입 가져오기
    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private Task<Uri> uploadImageAndGetUrl(StorageReference fileRef, Uri uri) {
        final TaskCompletionSource<Uri> taskCompletionSource = new TaskCompletionSource<>();

        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            // 이미지 업로드 성공 시 처리
            // 이미지 다운로드 URL을 가져와서 TaskCompletionSource에 결과 저장
            fileRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                taskCompletionSource.setResult(uriResult);
            });
        });

        return taskCompletionSource.getTask();
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

    private void sendMessage(String strName, String strCategory, FirebaseUser firebaseUser, String title) {
        db.collection("User").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.contains(strCategory) && documentSnapshot.contains("membership")) {
                            boolean member = documentSnapshot.getBoolean("membership");
                            if (member) {
                                boolean user = documentSnapshot.getBoolean(strCategory);
                                if (user) {
                                    String userID = documentSnapshot.getId();
                                    if (!userID.equals(firebaseUser.getUid())) {
                                        sendFCMToUsersForItem(strName, userID, strCategory, title);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void sendFCMToUsersForItem(String itemName, String userID, String categry, String title) {

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
                            messageData.put("title", categry + " 경매알림");
                            messageData.put("body", itemName + "이(가) 이벤트 경매에 올라왔습니다!");
                            Map<String, String> data = new HashMap<>();
                            data.put("title", messageData.get("title"));
                            data.put("message", messageData.get("body"));
                            data.put("uid", userToken);
                            data.put("time", formattedDate);
                            data.put("userId", userID);
                            data.put("itemTitle", title);
                            data.put("itemType", "EventItem");

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
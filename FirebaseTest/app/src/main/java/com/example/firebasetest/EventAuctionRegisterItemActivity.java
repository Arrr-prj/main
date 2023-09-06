package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    ImageView selectImagesBtn, delImageBtn;
    Button mBtnback;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    String document, buyer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String selectedCategory;
    private Spinner itemCategorySpinner;
    private String[] categories = {"Nike", "Adidas", "Apple", "Samsung", "차량", "액세서리", "의류", "한정판", "프리미엄", "신발", "굿즈", "가구 인테리어", "스포츠 레저", "취미 게임", "기타"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_regist_item);
        // 컴포넌트 객체에 담기
        itemTitle = findViewById(R.id.input_title);
        itemName = findViewById(R.id.input_itemName);
        itemPrice = findViewById(R.id.input_itemPrice);
        itemInfo = findViewById(R.id.input_itemExplain);
        itemCategorySpinner = findViewById(R.id.input_itemCategory);
        write_text = findViewById(R.id.write_text);
        selectImagesBtn = findViewById(R.id.iv_camera);
        delImageBtn = findViewById(R.id.iv_del);
        mBtnback = findViewById(R.id.btn_back);
        buyer = " ";

        Log.d(TAG, "<< 시작 가격 최소 금액은 100원입니다. >>");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String sellerName = intent.getStringExtra("seller");

        String documentId = title + sellerName;
        this.document = documentId;
        // 스피너 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategorySpinner.setAdapter(adapter);
// 아이템 리스트 버튼 클릭 이벤트
        mBtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        delImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });
        // 카테고리 클릭 시 이벤트
        itemCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 선택된 카테고리를 처리하는 코드를 여기에 추가하세요.
                selectedCategory = categories[position];
                // 선택된 카테고리를 사용하여 원하는 작업을 수행합니다.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때 처리할 코드를 여기에 추가하세요.
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
                String strCategory = selectedCategory;
                String sellerId = firebaseUser.getEmail();
                Log.d(TAG, "" + sellerId);
                if (strCategory.isEmpty()) {
                    Toast.makeText(EventAuctionRegisterItemActivity.this, "카테고리를 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 쓰기
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Items");

                uploadToFirebase(strTitle, strName, strPrice, strInfo, strCategory, sellerId);
                Intent intent = new Intent(EventAuctionRegisterItemActivity.this, EventAuctionActivity.class);
                startActivity(intent);

            }
        });
        selectImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }// onCreate

    private void deleteImage() {
        if (mArrayUri.size() > 0) {
            // 선택한 이미지들을 모두 삭제
            mArrayUri.clear();

            // ViewPager2를 업데이트하여 이미지가 삭제된 것을 사용자에게 표시
            ViewPager2 viewPager2 = findViewById(R.id.sliderViewPager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, mArrayUri);
            viewPager2.setAdapter(viewPagerAdapter);

            Toast.makeText(this, "이미지가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "삭제할 이미지가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인")
                .setMessage("정말 작성을 취소하시겠습니까?\n작성중인 내용이 삭제됩니다.")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 예 버튼 클릭 시 처리할 로직 작성
                        Intent intent = new Intent(EventAuctionRegisterItemActivity.this, EventAuctionActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("아니요", null); // 아니요 버튼

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null) {
            // If a single image is selected
            if (data.getData() != null) {
                mArrayUri.add(data.getData());
            }
            // If multiple images are selected
            else if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);
                }
            }

            // Update the ViewPager2 after images are selected
            ViewPager2 viewPager2 = findViewById(R.id.sliderViewPager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, mArrayUri);
            viewPager2.setAdapter(viewPagerAdapter);
        }
    }

    public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

        private ArrayList<Uri> mArrayUri;
        private Context context;

        public ViewPagerAdapter(Context context, ArrayList<Uri> mArrayUri) {
            this.context = context;
            this.mArrayUri = mArrayUri;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_banner, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context)
                    .load(mArrayUri.get(position))
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mArrayUri.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.iv_banner_image);
            }
        }
    }


    // 파이어베이스 이미지 업로드
    private void uploadToFirebase(String strTitle, String strName, String strPrice, String strInfo, String strCategory, String sellerId) {

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
        if (Integer.parseInt(strPrice) >= 100) {
            data.put("endPrice", strPrice);
        } else {
            data.put("endPrice", "100");
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

        // 업로드된 이미지 URL을 저장
        ArrayList<String> imageUrls = new ArrayList<>();
        for (Uri imageUri : mArrayUri) {
            StorageReference imageRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadImageAndGetUrl(imageRef, imageUri).addOnSuccessListener(uriResult -> {
                // 이미지 업로드 성공 시 URL을 리스트에 추가
                imageUrls.add(uriResult.toString());

                // 모든 이미지가 업로드되면 Firestore에 데이터 업로드
                if (imageUrls.size() == mArrayUri.size()) {
                    // 이미지 URL을 Firestore 데이터에 추가
                    data.put("imageUrls", imageUrls);

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
                                data.put("imageUrls", imageUrls); // 이미지 URL 추가

                                userDocRef.set(data)
                                        .addOnSuccessListener(aVoid -> {
                                            // 등록된 리스트 새로 갱신
                                            UserDataHolderOpenItems.loadOpenItems();
                                            Toast.makeText(EventAuctionRegisterItemActivity.this, "상품 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                            sendMessage(strName, strCategory, firebaseUser, strTitle + sellerId);
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
            }).addOnFailureListener(e -> {
                // 이미지 업로드 실패 처리
            });
        }


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
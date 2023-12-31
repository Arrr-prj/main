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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;
import java.util.Map;

public class EditItemActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    TextView write_text, itemTitle;
    EditText itemId, itemPrice, itemInfo;
    Button itemCategory;
    private Uri imageUrl;
    String url;
    ImageView imageView;
    private final StorageReference reference = FirebaseStorage.getInstance().getReference().child("image");
    private String[] categories = {"차량", "액세서리", "가전제품", "예술품", "의류", "골동품", "식품", "가구"};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // 아이템 정보들
        itemTitle = findViewById(R.id.input_itemTitle);
        itemId = findViewById(R.id.input_itemId);
        itemPrice = findViewById(R.id.input_itemPrice);
        itemInfo = findViewById(R.id.input_itemExplain);
        itemCategory = findViewById(R.id.input_itemCategory);
        write_text = findViewById(R.id.write_text);

        imageView = findViewById(R.id.input_itemImg);
        Intent intent = getIntent();
        String state = intent.getStringExtra("state");
        if(state.equals("bidding")){
            getSelectbItem();
        }else if(state.equals("open")){
            getSelectoItem();
        }else{
            getSelectsItem();
        }

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
        String documentId= intent.getStringExtra("documentId");
        Item selectedItem = null;
        Item selectedoItem = null;
        Item selectedsItem = null;

        // BiddingItem 목록에서 선택한 아이템 검색
        for (Item item : UserDataHolderBiddingItems.biddingItemList) {
            String doc = item.getTitle()+firebaseUser.getEmail();
            if(doc.equals(documentId)){
                selectedItem = item;
                break;
            }
        }

        // openItemList에서 선택한 아이템 검색
        for (Item item : UserDataHolderOpenItems.openItemList) {
            String doc = item.getTitle()+firebaseUser.getEmail();
            if(doc.equals(documentId)){
                selectedoItem = item;
                break;
            }
        }

        // shareItemList에서 선택한 아이템 검색
        for (Item item : UserDataHolderShareItem.shareItemList) {
            String doc = item.getTitle()+firebaseUser.getEmail();
            if(doc.equals(documentId)){
                selectedsItem = item;
                break;
            }
        }

        if (selectedItem != null) {
            // BiddingItem을 Firestore에서 업데이트
            uploadToBFirebase(selectedItem.getTitle() , imageUrl, selectedItem.getId(), itemPrice.getText().toString(), itemInfo.getText().toString(), itemCategory.getText().toString(), firebaseUser.getEmail());
        } else if (selectedoItem != null) {
            Log.d(TAG, ""+imageUrl);
            // OpenItem을 Firestore에서 업데이트
            uploadToOFirebase(selectedoItem.getTitle() , imageUrl, selectedoItem.getId(), itemPrice.getText().toString(), itemInfo.getText().toString(), itemCategory.getText().toString(), firebaseUser.getEmail());
        } else if(selectedsItem != null){
            uploadToSFirebase(selectedsItem.getTitle(), imageUrl, selectedsItem.getId(), "share item", itemInfo.getText().toString(), itemCategory.getText().toString(), firebaseUser.getEmail());
        }else {
            Toast.makeText(EditItemActivity.this, "선택한 아이템을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
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


    // bidding Item 클릭 시 이벤트
    private void getSelectbItem(){
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, ""+documentId);
//        item = BiddingActivity.biddingItemList.get().getId().equals(id);
        db.collection("BiddingItem").document(documentId).get()
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
                        itemId.setHint(get_id);
                        itemInfo.setHint(get_info);
                        itemCategory.setText(get_category);
                        itemPrice.setHint(get_price);

                        Glide.with(EditItemActivity.this)
                                .load(get_url)
                                .into(imageView);

                    }
                });
    }
    // openItemList 클릭 시 이벤트
    private void getSelectoItem(){
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Item selectedItem = null;
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
                        itemId.setHint(get_id);
                        itemInfo.setHint(get_info);
                        itemCategory.setText(get_category);
                        itemPrice.setHint(get_price);

                        Glide.with(EditItemActivity.this)
                                .load(get_url)
                                .into(imageView);

                    }
                });
    }

    // share Item 클릭 시 이벤트
    private void getSelectsItem(){
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, ""+documentId);
        itemPrice.setText("share item");
        itemPrice.setEnabled(false);
        db.collection("ShareItem").document(documentId).get()
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
                        itemId.setHint(get_id);
                        itemInfo.setHint(get_info);
                        itemCategory.setText(get_category);
                        itemPrice.setText(get_price);

                        Glide.with(EditItemActivity.this)
                                .load(get_url)
                                .into(imageView);

                    }
                });
    }

    private void uploadToBFirebase(String strTitle, Uri uri, String strName, String strPrice, String strInfo, String strCategory, String sellerId) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Map<String, Object> data = new HashMap<>();
            data.put("title", strTitle);
            data.put("id", strName);
            if(Integer.parseInt(strPrice) >= 100){
                data.put("price",strPrice);
            }else{
                data.put("price","100");
            }
            data.put("info", strInfo);
            data.put("category", strCategory);
            data.put("seller", sellerId);

            // 성공 시
            fileRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                // 이미지 아이템에 담기
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(uriResult != null){
                    data.put("imgUrl", uriResult.toString());
                }else{
                    data.put("imgUrl", imageView);
                }

                DocumentReference userDocRef = db.collection("BiddingItem").document(strTitle + sellerId);
                userDocRef.update(data)
                        .addOnSuccessListener(aVoid -> {
                            // 등록된 리스트 새로 갱신
                            UserDataHolderBiddingItems.loadBiddingItems();
                            Toast.makeText(EditItemActivity.this, "상품 수정에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditItemActivity.this, MyItemsActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "상품 수정에 실패했습니다." + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        });
            });
        });
    }
    private void uploadToOFirebase(String strTitle, Uri uri, String strName, String strPrice, String strInfo, String strCategory, String sellerId) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Map<String, Object> data = new HashMap<>();
            data.put("title", strTitle);
            data.put("id", strName);
            if(Integer.parseInt(strPrice) >= 100){
                data.put("price",strPrice);
            }else{
                data.put("price","100");
            }
            data.put("info", strInfo);
            data.put("category", strCategory);
            data.put("seller", sellerId);

            // 성공 시
            fileRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                // 이미지 아이템에 담기
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                data.put("imgUrl", uriResult.toString());

                DocumentReference userDocRef = db.collection("OpenItem").document(strTitle + sellerId);
                userDocRef.update(data)
                        .addOnSuccessListener(aVoid -> {
                            // 등록된 리스트 새로 갱신
                            UserDataHolderOpenItems.loadOpenItems();
                            Toast.makeText(EditItemActivity.this, "상품 수정에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditItemActivity.this, MyItemsActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "상품 수정에 실패했습니다." + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        });
            });
        });
    }
    // 무료 아이템
    private void uploadToSFirebase(String strTitle, Uri uri, String strName, String strPrice, String strInfo, String strCategory, String sellerId) {
        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Map<String, Object> data = new HashMap<>();
            data.put("title", strTitle);
            data.put("id", strName);
            data.put("price", "share item");
            data.put("info", strInfo);
            data.put("category", strCategory);
            data.put("seller", sellerId);

            // 성공 시
            fileRef.getDownloadUrl().addOnSuccessListener(uriResult -> {
                // 이미지 아이템에 담기
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                data.put("imgUrl", uriResult.toString());

                DocumentReference userDocRef = db.collection("ShareItem").document(strTitle + sellerId);
                userDocRef.update(data)
                        .addOnSuccessListener(aVoid -> {
                            // 등록된 리스트 새로 갱신
                            UserDataHolderOpenItems.loadOpenItems();
                            Toast.makeText(EditItemActivity.this, "상품 수정에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditItemActivity.this, MyItemsActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "상품 수정에 실패했습니다." + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        });
            });
        });
    }
    // 파일 타입 가져오기
    private String getFileExtension(Uri uri) {
        if (uri == null) {
            Log.d(TAG, "이미지 처리에 실패했습니다.");
            return null; // 혹은 다른 처리 방식으로 수정
        }

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
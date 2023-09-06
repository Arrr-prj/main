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
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditItemActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    TextView write_text;
    ImageView selectImagesBtn, delImageBtn;
    EditText itemId, itemPrice, itemInfo, itemTitle;
    Button mBtnback;
    private String selectedCategory;


    String url;
//    ImageView imageView;
    private final int PICK_IMAGE_MULTIPLE = 1;
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    private final StorageReference reference = FirebaseStorage.getInstance().getReference().child("image");
    private String[] categories = {"Nike", "Adidas", "Apple", "Samsung", "차량", "액세서리", "의류", "한정판", "프리미엄", "신발", "굿즈", "가구 인테리어", "스포츠 레저", "취미 게임", "기타"};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] imageUrls;
    private Spinner itemCategorySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // 아이템 정보들
        itemTitle = findViewById(R.id.input_itemtitle);
        itemId = findViewById(R.id.input_itemId);
        selectImagesBtn = findViewById(R.id.iv_camera);
        itemPrice = findViewById(R.id.input_itemPrice);
        delImageBtn = findViewById(R.id.iv_del);
        itemInfo = findViewById(R.id.input_itemExplain);
        mBtnback = findViewById(R.id.btn_back);
        itemCategorySpinner = findViewById(R.id.input_itemCategory);
        write_text = findViewById(R.id.write_text);

//        imageView = findViewById(R.id.input_itemImg); 사진 수정
        Intent intent = getIntent();
        String state = intent.getStringExtra("state");
        imageUrls = intent.getStringArrayExtra("imageUrls");
        ViewPager2 viewPager2 = findViewById(R.id.sliderViewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, mArrayUri);
        viewPager2.setAdapter(viewPagerAdapter);

        // 페이지 접속 시 새로 로딩해준다.
        imageUrls = getIntent().getStringArrayExtra("imageUrls");

        // ViewPager2 어댑터를 설정합니다.
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this, imageUrls);
//        sliderViewPager.setAdapter(imageSliderAdapter);
        if(state.equals("bidding")){
            getSelectbItem();
        }else if(state.equals("open")){
            getSelectoItem();
        }else{
            getSelectsItem();
        }
        // 스피너 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategorySpinner.setAdapter(adapter);
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

        // 등록 버튼 클릭 시 이벤트 ( 수정 )
        Button registBtn = findViewById(R.id.btn_itemRegist);
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToFirebase();
            }
        });
        selectImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
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
        public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewHolder holder, int position) {
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
        // 이미지 배열 업데이트 메서드 추가
        public void updateImages(ArrayList<Uri> updatedImages) {
            mArrayUri.clear();
            mArrayUri.addAll(updatedImages);
            notifyDataSetChanged();
        }
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

//        if (selectedItem != null) {
//            // BiddingItem을 Firestore에서 업데이트
//            uploadToBFirebase(selectedItem.getTitle() , imageUrl, selectedItem.getId(), itemPrice.getText().toString(), itemInfo.getText().toString(),selectedCategory, firebaseUser.getEmail());
//        } else if (selectedoItem != null) {
//            Log.d(TAG, ""+imageUrl);
//            // OpenItem을 Firestore에서 업데이트
//            uploadToOFirebase(selectedoItem.getTitle() , imageUrl, selectedoItem.getId(), itemPrice.getText().toString(), itemInfo.getText().toString(), selectedCategory, firebaseUser.getEmail());
//        } else if(selectedsItem != null){
//            uploadToSFirebase(selectedsItem.getTitle(), imageUrl, selectedsItem.getId(), "share item", itemInfo.getText().toString(), selectedCategory, firebaseUser.getEmail());
//        }else {
//            Toast.makeText(EditItemActivity.this, "선택한 아이템을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
//        }
    }
    private void getSelectbItem(){
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");
        Log.d(TAG, ""+documentId);
//    item = BiddingActivity.biddingItemList.get().getId().equals(id);
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
                        selectedCategory = get_category; // itemCategory 대신 selectedCategory에 설정
                        itemPrice.setHint(get_price);
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
                        selectedCategory = get_category; // itemCategory 대신 selectedCategory에 설정
                        itemPrice.setHint(get_price);
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
                        selectedCategory = get_category; // itemCategory 대신 selectedCategory에 설정
                        itemPrice.setText(get_price);

//                        Glide.with(EditItemActivity.this)
//                                .load(get_url)
//                                .into(imageView);

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
//                    data.put("imgUrl", imageView);
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
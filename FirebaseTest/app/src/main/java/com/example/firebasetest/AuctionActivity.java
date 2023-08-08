package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;



public class AuctionActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebasestorage;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private EditText InputItemName, InputItemImg, InputItemPrice, InputItemSeller;
    private ImageView ivProfile;
    private String imageUrl = "";
    private int GALLEY_CODE = 10;
    private Button BtnItemRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        firebaseAuth = FirebaseAuth.getInstance();
        firebasestorage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        initView();
        listener();
    }

    private void initView() {
        BtnItemRegist = (Button)findViewById(R.id.btn_itemRegist);
        ivProfile = (ImageView)findViewById(R.id.input_itemImg);
        InputItemName = findViewById(R.id.input_itemName);
        InputItemPrice = findViewById(R.id.input_itemPrice);
    }

    private void listener() {
        BtnItemRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg(imageUrl);
            }
        });
        // 이미지 업로드
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로컬 사진첩으로 넘어간다.
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                startActivity(intent);
            }
        });
    }

    // 사진 고른 후 돌아오는 코드
    // 로컬 파일에서 업로드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLEY_CODE) {
            imageUrl = getRealPathFromUri(data.getData());
            RequestOptions cropOptions = new RequestOptions();
            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .apply(cropOptions.optionalCenterCrop())
                    .into(ivProfile);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromUri(Uri uri) {
        String[] proj = {
            MediaStore.Images.Media.DATA
        } ;
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return url;
    }

    private void uploadImg(String uri) {
        try {
            // Create a storage reference from our app
            StorageReference storageReference = firebasestorage.getReference();

            Uri file = Uri.fromFile(new File(uri));
            final StorageReference riversRef = storageReference.child("images/" + file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AuctionActivity.this, "업로드 성공", Toast.LENGTH_SHORT).show();

                        //파이어베이스에 데이터베이스 업로드
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = task.getResult();

                        Item item = new Item();
//                        item.setImageUrl(downloadUrl.toString());
                        item.setId(InputItemName.getText().toString());
                        item.setPrice(Integer.parseInt(InputItemPrice.getText().toString()));

                        //image 라는 테이블에 json 형태로 담긴다.
                        //database.getReference().child("Profile").setValue(imageDTO);
                        //  .push()  :  데이터가 쌓인다.
                        database.getReference().child("Profile").push().setValue(item);

                        Intent intent = new Intent(getApplicationContext(), Item.class);
                        startActivity(intent);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        } catch (NullPointerException e) {
            Toast.makeText(AuctionActivity.this, "이미지 선택 안함", Toast.LENGTH_SHORT).show();

        }
    }

}
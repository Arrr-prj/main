package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class LargeImageActivity extends AppCompatActivity implements ImageSliderAdapter.OnImageClickListener {
    private ViewPager2 sliderViewPager;
    private TextView itmeTitle, itemId, startPrice, endPrice, itemInfo, seller, category, timeinfo, futureMillis;
    String imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, imageUrl6;
    String document, buyer;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final StorageReference reference = FirebaseStorage.getInstance().getReference();
    private ImageView imageView;
    private String[] images = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        sliderViewPager.setOffscreenPageLimit(1);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(this, images);
        imageSliderAdapter.setImageClickListener(this); // 클릭 리스너 설정
// 클릭 리스너 설정
        imageSliderAdapter.setImageClickListener(position -> {

            // 이미지 클릭 시 전체 화면으로 이미지 보기 처리
            Intent intent = new Intent(LargeImageActivity.this, FullScreenImageActivity.class);
            intent.putExtra("images", images);
            intent.putExtra("position", position);
            startActivity(intent);
        });
        sliderViewPager.setAdapter(imageSliderAdapter);
        Intent intent = getIntent();
        images =   images = intent.getStringArrayExtra("images"); // 이미지 URL 배열 받아오기

        String title = intent.getStringExtra("title");
        String sellerName = intent.getStringExtra("seller");

        String documentId = title + sellerName;
        this.document = documentId;
        database = FirebaseFirestore.getInstance();
        DocumentReference docRef = database.collection("OpenItem").document(documentId);
        getSelectedItem();

//        if (imageUrl1 != null && !imageUrl1.isEmpty()) {
//            images[0] = imageUrl1;
//        }
//        if (imageUrl2 != null && !imageUrl2.isEmpty()) {
//            images[1] = imageUrl2;
//        }
//        if (imageUrl3 != null && !imageUrl3.isEmpty()) {
//            images[2] = imageUrl3;
//        }
//        if (imageUrl4 != null && !imageUrl4.isEmpty()) {
//            images[3] = imageUrl4;
//        }
//        if (imageUrl5 != null && !imageUrl5.isEmpty()) {
//            images[4] = imageUrl5;
//        }
//        if (imageUrl6 != null && !imageUrl6.isEmpty()) {
//            images[5] = imageUrl6;
//        }

        sliderViewPager.setAdapter(new ImageSliderAdapter(this, images));

    }

    private void getSelectedItem() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Item selectedItem = null;
        for (Item item : OpenAuctionActivity.openItemList) {
            if (item.getId().equals(id)) {
                selectedItem = item;
                break;
            }
        }
        if (selectedItem != null) {
            setItemValues(selectedItem);
            imageUrl1 = selectedItem.getImageUrl1();
            imageUrl2 = selectedItem.getImageUrl2();
            imageUrl3 = selectedItem.getImageUrl3();
            imageUrl4 = selectedItem.getImageUrl4();
            imageUrl5 = selectedItem.getImageUrl5();
            imageUrl6 = selectedItem.getImageUrl6();
        } else {
            // 해당 id와 일치하는 아이템이 없는 경우
        }
    }

    private void setItemValues(Item selectedItem) {
        database.collection("OpenAuctionInProgress")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    int highestBid = 0;
                    if (value != null && !value.isEmpty()) {
                        for (QueryDocumentSnapshot doc : value) {
                            Map<String, Object> bidData = doc.getData();
                            String bidPriceStr = (String) bidData.get("입찰 가격");
                            if (bidPriceStr != null) {
                                try {
                                    int bidPrice = Integer.parseInt(bidPriceStr);
                                    highestBid = Math.max(highestBid, bidPrice);
                                } catch (NumberFormatException e) {
                                    highestBid = 0;
                                }
                            }
                        }
                    }
                    itmeTitle.setText(selectedItem.getTitle());
                    itemId.setText(selectedItem.getId());
                    endPrice.setText(String.valueOf(highestBid));
                    seller.setText(selectedItem.getSeller());



                });
    }
    @Override
    public void onImageClick(int position) {
        // 이미지 클릭 시 실행할 동작을 여기에 구현

    }

}

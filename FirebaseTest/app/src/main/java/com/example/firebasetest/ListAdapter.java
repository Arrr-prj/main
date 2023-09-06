package com.example.firebasetest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Item> {
    private List<Item> oItem;

    private Context context;
    private Uri imageUrl;
    private ImageView imageView, mBtnMore;
    private FirebaseFirestore db;

    LayoutInflater layoutInflater = null;
    ImageView load;
    Bitmap bitmap;
    private DecimalFormat decimalFormat;
    public ListAdapter(Context context, List<Item> dataList) {
        super(context, 0, dataList);
        this.oItem = dataList;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.open_items, parent, false);
        }
        Item data = getItem(position);
        if (data != null) {
            ImageView imgView = (ImageView) itemView.findViewById(R.id.ImgUrl);
            TextView title = (TextView) itemView.findViewById(R.id.title);
            TextView id = (TextView) itemView.findViewById(R.id.id);
            TextView price = (TextView) itemView.findViewById(R.id.price);
            TextView category = (TextView) itemView.findViewById(R.id.category);

            imageView = itemView.findViewById(R.id.ImgUrl);

            title.setText(data.getTitle());
            category.setText("카테고리 : " + data.getCategory());
            id.setText("판매자 : " + data.getSeller());

            if (String.valueOf(data.getPrice()).equals("share item")) {
                price.setText("무료나눔 상품입니다");
            } else {
                decimalFormat = new DecimalFormat("#,###");
                String formattedPrice = decimalFormat.format(Double.valueOf(data.getPrice()));
                price.setText("시작 가격 : " + formattedPrice);
            }
            //현재시간을 가져오고 아이템의 저장돼있는 마감시간을 비교해서 현재시간이 더 높으면 색깔처리 (회색말고 색깔 변경가능)
            long currenTimeMillis = System.currentTimeMillis();
            boolean isTimeElapsed = currenTimeMillis> Long.parseLong(data.getFutureMillis());

            int color;
            if (isTimeElapsed) {
                color = Color.rgb(100,100,100);
                itemView.setBackgroundColor(color);
            } else {
                itemView.setBackgroundColor(Color.WHITE);
            }
            String[] imageUrls = data.getImageUrls();
            if (imageUrls != null && imageUrls.length > 0) {
                Glide.with(getContext())
                        .load(imageUrls[0])
                        .into(imgView);
            } else {
                // 이미지 URL이 없는 경우 처리할 내용 추가
            }


        }
        ImageView mBtnMore = itemView.findViewById(R.id.moreicon);

        db = FirebaseFirestore.getInstance();
        String uid = UserManager.getInstance().getUserUid(); // uid 가져오기
//        mBtnMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(view, position);
//            }
//        });
        if (uid != null) {
            DocumentReference userDocRef = db.collection("User").document(uid);

            userDocRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Boolean adminValue = documentSnapshot.getBoolean("admin");

                            if(adminValue != null && adminValue){
                                mBtnMore.setVisibility(View.VISIBLE);

                                mBtnMore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showPopupMenu(view, position);
                                    }
                                });


                            }else{
                                mBtnMore.setVisibility(View.INVISIBLE);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 데이터 가져오기 실패 처리
                            Toast.makeText(context, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return itemView;
    }
    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.popup_menu); // 팝업 메뉴 리소스를 inflate

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_delete:
                        showDeleteConfirmationDialog(position);
                        return true;

                    case R.id.menu_edit:
                        // 아이템 수정 로직을 구현
                        // 수정할 아이템의 정보를 가져와서 수정 작업 수행
                        return true;

                    default:
                        return false;
                }
            }
        });

        popupMenu.show(); // 팝업 메뉴를 보여줌
    }
    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("삭제 확인");
        builder.setMessage("정말로 삭제하시겠습니까?");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Item itemToDelete = oItem.get(position);
                String documentId = itemToDelete.getTitle() + itemToDelete.getSeller(); // 문서 ID 생성

                // Firestore에서 컬렉션들의 해당 문서 삭제
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // 컬렉션들의 이름 배열 (OpenItem, BiddingItem 등)
                String[] collections = {"OpenItem", "BiddingItem", "ShareItem", "EventItem"};

                for (String collectionName : collections) {
                    db.collection(collectionName).document(documentId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // 삭제 성공 시 처리
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // 삭제 실패 시 처리
                                }
                            });
                }

                oItem.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "문서가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 취소 버튼을 눌렀을 때 실행할 동작을 여기에 추가
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getCount() {
        return oItem.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Item getItem(int position) {
        return oItem.get(position);
    }

    public void updateData(List<Item> newDataList) {
        oItem = newDataList;
        notifyDataSetChanged();
    }
}
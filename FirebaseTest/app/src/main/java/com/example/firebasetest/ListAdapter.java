package com.example.firebasetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Item> {
    private List<Item> oItem;

    private Context context;
    private Uri imageUrl;
    private ImageView imageView;
    LayoutInflater layoutInflater = null;
    ImageView load;
    Bitmap bitmap;

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
                price.setText("시작 가격 : " + String.valueOf(data.getPrice()));
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

            Glide.with(getContext())
                    .load(data.getImageUrl1())
                    .into(imgView);
        }
        return itemView;
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
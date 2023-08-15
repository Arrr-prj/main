package com.example.firebasetest;

import android.content.Context;
import android.graphics.Bitmap;
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

public class ShareAdapterActivity extends ArrayAdapter<Item> {
    private List<Item> oItem;

    private Context context;
    private Uri imageUrl;
    private ImageView imageView;
    LayoutInflater layoutInflater = null;
    ImageView load;
    Bitmap bitmap;

    public ShareAdapterActivity(Context context, List<Item>  dataList) {
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
            TextView id = (TextView) itemView.findViewById(R.id.id);
            TextView category = (TextView) itemView.findViewById(R.id.category);
            TextView info = (TextView) itemView.findViewById(R.id.info);
            TextView seller = (TextView) itemView.findViewById(R.id.seller);

            imageView = itemView.findViewById(R.id.ImgUrl);

            seller.setText(data.getSeller());
            id.setText(data.getId());
            category.setText(data.getCategory());
            info.setText(data.getInfo());

            Glide.with(getContext())
                    .load(data.getImageUrl())
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

    public void updateData(List<Item> newDataList){
        oItem = newDataList;
        notifyDataSetChanged();
    }
}

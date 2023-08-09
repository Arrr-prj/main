package com.example.firebasetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BiddingItemAdapter extends ArrayAdapter<BiddingItem> {
    List<BiddingItem> bItem;

    private Context context;
    private Uri imageUrl;
    private ImageView imageView;
    LayoutInflater layoutInflater = null;
    ImageView load;
    Bitmap bitmap;
    public BiddingItemAdapter(Context context, List<BiddingItem> dataList){
        super(context, 0, dataList);
        this.bItem = dataList;
        this.context = context;
    }
    @Override
    public int getCount(){
        return bItem.size();
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public BiddingItem getItem(int position){
        return bItem.get(position);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.bidding_items, parent, false);
        }
        BiddingItem data = getItem(position);
        if(data != null){
            ImageView imgView = (ImageView)itemView.findViewById(R.id.ImgUrl);
            TextView id = (TextView)itemView.findViewById(R.id.id);
            TextView category = (TextView)itemView.findViewById(R.id.category);
            TextView info = (TextView)itemView.findViewById(R.id.info);
            TextView seller = (TextView) itemView.findViewById(R.id.seller);

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

    public void updateData(List<BiddingItem> newDataList){
        bItem = newDataList;
        notifyDataSetChanged();
    }
}

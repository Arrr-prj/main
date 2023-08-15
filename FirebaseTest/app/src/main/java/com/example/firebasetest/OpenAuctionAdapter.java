package com.example.firebasetest;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OpenAuctionAdapter extends ArrayAdapter<Item> {
    private List<Item> oItem;

    private Context context;
    private Uri imageUrl;
    private ImageView imageView;
    LayoutInflater layoutInflater = null;
    ImageView load;
    Bitmap bitmap;

    public OpenAuctionAdapter(Context context, List<Item> dataList) {
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
            TextView price = (TextView) itemView.findViewById(R.id.price);
            TextView seller = (TextView) itemView.findViewById(R.id.seller);

            imageView = itemView.findViewById(R.id.ImgUrl);

            seller.setText(data.getSeller());
            id.setText(data.getId());
            category.setText(data.getCategory());
            info.setText(data.getInfo());
            price.setText(String.valueOf(data.getPrice()));

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

}


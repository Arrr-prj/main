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

public class OpenAuctionAdaptertest  extends ArrayAdapter<Itemtest> {
    private List<Itemtest> oItem;

    private Context context;
    private Uri imageUrl1;
//    private Uri imageUrl2;
//    private Uri imageUrl3;
//    private Uri imageUrl4;
//    private Uri imageUrl5;
//    private Uri imageUrl6;

    private ImageView imageView1;
//    private ImageView imageView2;
//    private ImageView imageView3;
//    private ImageView imageView4;
//    private ImageView imageView5;
//    private ImageView imageView6;

    LayoutInflater layoutInflater = null;
    ImageView load;
    Bitmap bitmap;

    public OpenAuctionAdaptertest(Context context, List<Itemtest> dataList) {
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
        Itemtest data = getItem(position);
        if (data != null) {
            ImageView imgView1 = itemView.findViewById(R.id.ImgUrl);
//            ImageView imgView2 = itemView.findViewById(R.id.ImgUrl2);
//            ImageView imgView3 = itemView.findViewById(R.id.ImgUrl3);
//            ImageView imgView4 = itemView.findViewById(R.id.ImgUrl4);
//            ImageView imgView5 = itemView.findViewById(R.id.ImgUrl5);
//            ImageView imgView6 = itemView.findViewById(R.id.ImgUrl6);

            TextView id = itemView.findViewById(R.id.id);
            TextView category = itemView.findViewById(R.id.category);
            TextView info = itemView.findViewById(R.id.info);
            TextView price = itemView.findViewById(R.id.price);
            TextView seller = itemView.findViewById(R.id.seller);

            seller.setText(data.getSeller());
            id.setText(data.getId());
            category.setText(data.getCategory());
            info.setText(data.getInfo());
            price.setText(String.valueOf(data.getPrice()));

            Glide.with(getContext())
                    .load(data.getImageUrl1())
                    .into(imgView1);
//
//            Glide.with(getContext())
//                    .load(data.getImageUrl2())
//                    .into(imgView2);
//
//            Glide.with(getContext())
//                    .load(data.getImageUrl3())
//                    .into(imgView3);
//
//            Glide.with(getContext())
//                    .load(data.getImageUrl4())
//                    .into(imgView4);
//
//            Glide.with(getContext())
//                    .load(data.getImageUrl5())
//                    .into(imgView5);
//
//            Glide.with(getContext())
//                    .load(data.getImageUrl6())
//                    .into(imgView6);
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
    public Itemtest getItem(int position) {
        return oItem.get(position);
    }

}
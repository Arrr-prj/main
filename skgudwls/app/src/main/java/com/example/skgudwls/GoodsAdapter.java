package com.example.skgudwls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class GoodsAdapter extends ArrayAdapter<Goods> {
    public GoodsAdapter(Context context, int resource, List<Goods> goodsList){
        super(context, resource, goodsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        Goods goods = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.goods_item, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.goods_name);
        ImageView iv = convertView.findViewById(R.id.goods_image);

        tv.setText(goods.getName());
        iv.setImageResource(goods.getImage());

        return convertView;
    }
}

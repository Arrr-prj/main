package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;

public class AnnounceAdapter extends ArrayAdapter<Announcement> {
    private List<Announcement> announcementList;
    private Context context;

    public AnnounceAdapter(Context context, List<Announcement> dataList){
        super(context, 0, dataList);
        this.announcementList = dataList;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.announcement_list, parent, false);
        }
        Announcement data = getItem(position);
        if (data != null) {
            TextView title = (TextView) itemView.findViewById(R.id.title);
            TextView contents = (TextView) itemView.findViewById(R.id.contents);


            title.setText(data.getTitle());
            contents.setText(data.getContents());

        }
        return itemView;
    }

    @Override
    public int getCount() {
        return announcementList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Announcement getItem(int position) {
        return announcementList.get(position);
    }
}
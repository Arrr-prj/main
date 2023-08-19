package com.example.firebasetest;

// 알림 페이지에 사용하는 어댑터입니다.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends BaseAdapter {
    private Context mContext;
    private List<Notification> notificationList;
    private LayoutInflater mLayoutInflater;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationList) {
        mContext = context;
        this.notificationList = notificationList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Notification getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.alarm, parent, false);
        }

        TextView titleTextView = view.findViewById(R.id.text_title);
        TextView timeTextView = view.findViewById(R.id.text_time);
        TextView messageTextView = view.findViewById(R.id.text_message);

        Notification notification = notificationList.get(position);

        titleTextView.setText(notification.getTitle());
        timeTextView.setText(notification.getTime());
        messageTextView.setText(notification.getMessage());


        return view;
    }
}

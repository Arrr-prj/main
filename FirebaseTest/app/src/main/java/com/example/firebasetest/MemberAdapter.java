package com.example.firebasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MemberAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    private ArrayList<Item> mUserList;

    public MemberAdapter(Context context, ArrayList<Item> userList) {
        super(context, 0, userList);
        mContext = context;
        mUserList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.member_items, parent, false);
        }

        Item currentItem = mUserList.get(position);

        TextView idTextView = listItem.findViewById(R.id.id);
        TextView nameTextView = listItem.findViewById(R.id.name);
        TextView nicknameTextView = listItem.findViewById(R.id.nickname);
        TextView addressTextView = listItem.findViewById(R.id.address);
        TextView membershipTextView = listItem.findViewById(R.id.membership);
        TextView reportTextView = listItem.findViewById(R.id.report);

        idTextView.setText(currentItem.getEmail());
        nameTextView.setText(currentItem.getName());
        nicknameTextView.setText(currentItem.getNickname());
        addressTextView.setText(currentItem.getAddress());
        membershipTextView.setText(currentItem.getMembership());
        reportTextView.setText(currentItem.getReports());

        return listItem;
    }
}

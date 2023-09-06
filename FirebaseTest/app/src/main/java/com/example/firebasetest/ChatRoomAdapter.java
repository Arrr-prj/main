package com.example.firebasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatRoomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChatRoom> chatRoomList;

    public ChatRoomAdapter(Context context, ArrayList<ChatRoom> chatRoomList) {
        this.context = context;
        this.chatRoomList = chatRoomList;
    }

    @Override
    public int getCount() {
        return chatRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_room, parent, false);
        }

        ImageView profileImageView = convertView.findViewById(R.id.profile_image);
        TextView nicknameTextView = convertView.findViewById(R.id.nickname_text_view);
        TextView lastMessageTextView = convertView.findViewById(R.id.last_message_text_view);
        ImageView itemImageView = convertView.findViewById(R.id.item_image);

        ChatRoom chatRoom = chatRoomList.get(position);

        nicknameTextView.setText(chatRoom.getNickname());
        lastMessageTextView.setText(chatRoom.getLastMessage());

        Glide.with(context)
                .load(chatRoom.getProfileImageUrl())
                .into(profileImageView);

        Glide.with(context)
                .load(chatRoom.getItemImageUrl())
                .into(itemImageView);

        return convertView;
    }
}
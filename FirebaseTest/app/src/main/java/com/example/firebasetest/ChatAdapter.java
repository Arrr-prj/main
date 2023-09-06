package com.example.firebasetest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myName;
    private String toName;

    public ChatAdapter(List<ChatData> mDataset){
        this.mDataset = mDataset;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView TextView_name;
        public TextView TextView_msg;
        public TextView TextView_send_msg;
        public TextView TextView_receive_msg;
        public FrameLayout msgCover;
        public View rootView;

        public MyViewHolder(@NonNull View v){
            super(v);
            TextView_name = v.findViewById(R.id.TextView_name);
            TextView_send_msg = v.findViewById(R.id.tv_send_message);
            TextView_receive_msg = v.findViewById(R.id.tv_recieve_message);
//            TextView_msg = v.findViewById(R.id.tv_recieve_message);
//            TextView_msg = v.findViewById(R.id.TextView_msg);
//            msgCover = v.findViewById(R.id.msgCover);
            rootView = v;

            v.setClickable(true); // 누를 수 있다 없다
            v.setEnabled(true);  // 활성화 상태 의미
        }
    }
    public ChatAdapter(List<ChatData> myDataset, Context context, String myName, String toName){
        mDataset = myDataset;
        this.myName = myName; // 내가 buyer일땐 buyer의 nickname, seller일땐 seller의 nickname이 할당된다.
//        this.documentId = documentId;
        this.toName = toName;
    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        ConstraintLayout v = (ConstraintLayout ) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        ChatData chat = mDataset.get(position);

        holder.TextView_name.setText(chat.getName());
        holder.TextView_receive_msg.setText(chat.getMsg());
        holder.TextView_send_msg.setText(chat.getMsg());

        if(chat.getName() != null && chat.getName().equals(this.myName)){ // chat 데이터의 발신자가 나와 동일한 경우
//            holder.msgCover.setBackgroundColor(Color.parseColor("#8d98e0"));

            holder.TextView_receive_msg.setVisibility(View.GONE);
            holder.TextView_name.setVisibility(View.GONE);
        }else if(chat.getTo() != null && chat.getTo().equals(this.myName)){ // 받는 이가 나 일때
            holder.TextView_name.setText(chat.getName());
            holder.TextView_send_msg.setVisibility(View.GONE);
//            holder.TextView_name.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }
    }

    @Override
    public int getItemCount(){
        // true면 0리턴, false면 size 리턴
        return mDataset == null ? 0 : mDataset.size();
    }

    public ChatData getChat(int position){
        return mDataset != null ? mDataset.get(position) : null;
    }
    public void addChat(ChatData chat){
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1); // 갱신
    }
}

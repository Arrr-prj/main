package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private RecyclerView.Adapter mAdapter;
    private FirebaseFirestore db;
    private EditText EditText_chat;
    private Button Button_send, Button_pay;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String seller = intent.getStringExtra("seller");
        String id = intent.getStringExtra("id");
        String endPrice = intent.getStringExtra("endPrice");
        String userEmail = UserManager.getInstance().getUserEmail();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, ChatActivity.this, userEmail, seller);
        mRecyclerView.setAdapter(mAdapter);

        EditText_chat = findViewById(R.id.EditText_chat);
        Button_send = findViewById(R.id.Button_send);
        Button_pay = findViewById(R.id.Button_pay);
        // 전송 버튼 눌렀을 시
        Button_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String msg = EditText_chat.getText().toString(); // msg
                if(msg != null){
//                    long timestamp = System.currentTimeMillis(); 보낸시간
                    ChatData chat = new ChatData();
                    chat.setMsg(msg);
                    chat.setTo(seller);
                    chat.setName(userEmail);
                    Log.d(TAG, "chat에서 가져온 name : "+chat.getName());
                    Log.d(TAG, "email : "+userEmail);
                    myRef.push().setValue(chat);
                    EditText_chat.setText(""); // 한번 전송버튼 누르면 reset
                }
            }
        });
        // ArrrPay 눌렀을 시
        Button_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pay = new Intent(ChatActivity.this, ArrrPayActivity.class);
                pay.putExtra("id", id);
                pay.putExtra("endPrice", endPrice);
                startActivity(pay);
            }
        });



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // 여기서 데이터를 가져와 ChatData 객체로 변환
                ChatData chat = snapshot.getValue(ChatData.class);
                chatList.add(chat);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
package com.example.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ChatRoomListActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private ArrayList<ChatRoom> chatRoomList = new ArrayList<>();
    private ListView listView;
    private ChatRoomAdapter chatRoomAdapter;
    private String userUid;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Firebase 데이터베이스 레퍼런스 가져오기
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("message");

        // 현재 사용자의 UID 가져오기
        userUid = UserManager.getInstance().getUserUid();

        listView = findViewById(R.id.listView_chatroom);
        chatRoomAdapter = new ChatRoomAdapter(this, chatRoomList);
        listView.setAdapter(chatRoomAdapter);
        btnBack = findViewById(R.id.btn_chatBack);

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRoomList.clear();
                for (DataSnapshot chatRoomSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot dataSetSnapshot = chatRoomSnapshot.child("dataSet");
                    String buyerUid = dataSetSnapshot.child("buyerUid").getValue(String.class);
                    String seller = dataSetSnapshot.child("seller").getValue(String.class);
                    if (buyerUid.equals(userUid) || seller.equals(UserManager.getInstance().getUserEmail())) {
                        String chatRoomName = chatRoomSnapshot.getKey();
                        String bidType = dataSetSnapshot.child("bidType").getValue(String.class);
                        String buyer = dataSetSnapshot.child("buyer").getValue(String.class);
                        String documentId = dataSetSnapshot.child("documentId").getValue(String.class);
                        String endPrice = dataSetSnapshot.child("endPrice").getValue(String.class);
                        String id = dataSetSnapshot.child("id").getValue(String.class);
                        String itemImageUrl = dataSetSnapshot.child("itemImageUrl").getValue(String.class);
                        String lastMessage = dataSetSnapshot.child("lastMessage").getValue(String.class);
                        String myName = dataSetSnapshot.child("myName").getValue(String.class);
                        String nickname = dataSetSnapshot.child("nickname").getValue(String.class);
                        String profileImageUrl = dataSetSnapshot.child("profileImageUrl").getValue(String.class);
                        ChatRoom chatRoom = new ChatRoom(chatRoomName, bidType, buyer, buyerUid, documentId, endPrice, id, itemImageUrl, lastMessage, myName, nickname, profileImageUrl, seller);
                        chatRoomList.add(chatRoom);
                    }
                }
                ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter(ChatRoomListActivity.this, chatRoomList);
                listView.setAdapter(chatRoomAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리 (옵션)
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 새로운 채팅방이 추가될 때의 처리 (옵션)
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 채팅방이 변경될 때의 처리
                ChatRoom updatedChatRoom = dataSnapshot.getValue(ChatRoom.class);
                if (updatedChatRoom != null) {
                    for (int i = 0; i < chatRoomList.size(); i++) {
                        if (chatRoomList.get(i).getRoomId().equals(updatedChatRoom.getRoomId())) {
                            chatRoomList.set(i, updatedChatRoom);
                            chatRoomAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // 채팅방이 삭제될 때의 처리
                ChatRoom removedChatRoom = dataSnapshot.getValue(ChatRoom.class);
                if (removedChatRoom != null) {
                    for (int i = 0; i < chatRoomList.size(); i++) {
                        if (chatRoomList.get(i).getRoomId().equals(removedChatRoom.getRoomId())) {
                            chatRoomList.remove(i);
                            chatRoomAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // 채팅방의 순서가 변경될 때의 처리 (옵션)
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리 (옵션)
            }
        });

        setUpOnClickListener();
    }

    private void setUpOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭한 아이템의 ChatRoom 객체 가져오기
                ChatRoom clickedChatRoom = chatRoomList.get(position);
                // Intent를 사용하여 데이터를 다른 액티비티로 전달
                Intent intent = new Intent(ChatRoomListActivity.this, ChatActivity.class);
                intent.putExtra("seller", clickedChatRoom.getSeller());
                intent.putExtra("id", clickedChatRoom.getId());
                intent.putExtra("endPrice", clickedChatRoom.getEndPrice());
                intent.putExtra("bidType", clickedChatRoom.getBidType());
                intent.putExtra("buyer", clickedChatRoom.getBuyer());
                intent.putExtra("documentId", clickedChatRoom.getDocumentId());
                intent.putExtra("futureMillis", clickedChatRoom.getFutureMillis());
                // 다른 액티비티를 시작하고 데이터를 전달
                startActivity(intent);
            }
        });
    }
}
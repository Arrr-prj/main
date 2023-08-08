package com.example.firebasetest;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyItemsActivity extends AppCompatActivity {
    ListView listView;
    public static ArrayList<Item> openItemList = new ArrayList<Item>();
    public static ArrayList<BiddingItem> biddingItemList = new ArrayList<BiddingItem>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        listView = (ListView)findViewById(R.id.listView);

        this.InitializeOpenItem();
        this.InitializeBiddingItem();
        final BiddingItemAdapter biddingItemAdapter = new BiddingItemAdapter(this, biddingItemList);
        final OpenAuctionAdapter openAuctionAdapter = new OpenAuctionAdapter(this, openItemList);

        listView.setAdapter(biddingItemAdapter);
        listView.setAdapter(openAuctionAdapter);
        listView.setOnItemClickListener(listener);

    }
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            Toast.makeText(MyItemsActivity.this, "Clicked position: " + position, Toast.LENGTH_SHORT).show();
        }
    };
    public void InitializeOpenItem(){

        db.collection("OpenItem").whereEqualTo("seller", "user1")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : (task.getResult())){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id"));
                            // Firebase Storage에서 이미지 불러오기
                            openItemList.add(
                                    new Item(
                                            String.valueOf(document.getData().get("imgUrl")),
                                            String.valueOf(document.getData().get("id")),
                                            Integer.valueOf(String.valueOf(document.getData().get("price"))),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")
                                            )
                                    )
                            );

                        }
                        OpenAuctionAdapter openAuctionAdapter = new OpenAuctionAdapter(this, openItemList);
                        listView.setAdapter(openAuctionAdapter);
                    }
                });
    }
    public void InitializeBiddingItem(){;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        db.collection("BiddingItem").whereEqualTo("seller", "user1")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData().get("id")+document.getData().get("imgUrl"));
                            biddingItemList.add(
                                    new BiddingItem(
                                            String.valueOf(document.getData().get("imgUrl")),
                                            String.valueOf(document.getData().get("id")),
                                            String.valueOf(document.getData().get("category")),
                                            String.valueOf(document.getData().get("info")),
                                            String.valueOf(document.getData().get("seller"))
                                    )
                            );

                        }
                        BiddingItemAdapter biddingItemAdapter = new BiddingItemAdapter(this, biddingItemList);
                        listView.setAdapter(biddingItemAdapter);
                    }
                });
    }
}


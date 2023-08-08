package com.example.skgudwls;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PublicauctionActivity extends AppCompatActivity {

    public static ArrayList<Goods> goodsList = new ArrayList<Goods>();

    ListView listView;
    Button btn_dialog;
    AlertDialog.Builder builder;
    String[] goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicauction);

        searchGoods();

        setUpdata(); // 데이터 셋팅
        setUpList(); // 리스트 셋팅
        setUpOnClickListener(); // 상세페이지 이벤트

        // 툴바 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
        getSupportActionBar().setTitle("상품 검색");
        // dialog
        btn_dialog = findViewById(R.id.btn_dialog);
        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    // dialog 실행
    public void showDialog(){
        goods = getResources().getStringArray(R.array.goods);
        builder = new AlertDialog.Builder(PublicauctionActivity.this);
        builder.setTitle("카테고리를 선택하세요");
        // dialog에 리스트 담기
        builder.setItems(goods, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "선택된 카테고리는" + goods[which], Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    // 툴바
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{ // toolbar의 back키 눌렀을 때 동작
                // todo

                Intent intent = new Intent(PublicauctionActivity.this, MainpageActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchGoods(){
        SearchView searchView = findViewById(R.id.goods_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Goods> filterGoods = new ArrayList<>();
                for(int i = 0; i < goodsList.size(); i++){
                    Goods goods = goodsList.get(i);
                    //데이터와 비교해서 내가 쓴 상품 이름이 있다면
                    if(goods.getName().toLowerCase().contains(newText.toLowerCase())){
                        filterGoods.add(goods);
                    }
                }
                GoodsAdapter adapter = new GoodsAdapter(getApplicationContext(), 0, filterGoods);
                listView.setAdapter(adapter);
                return false;
            }
        });
    }
    /**
     * 데이터 셋팅
     */
    private void setUpdata(){
        Goods shoes = new Goods("0", "shoes", R.drawable.shoes);
        goodsList.add(shoes);

        Goods car = new Goods("1", "car", R.drawable.car);
        goodsList.add(car);

        Goods ceramic = new Goods("2", "ceramic", R.drawable.ceramic);
        goodsList.add(ceramic);

        Goods photoframe = new Goods("3", "photoframe", R.drawable.photoframe);
        goodsList.add(photoframe);

        Goods watch = new Goods("4", "watch", R.drawable.watch);
        goodsList.add(watch);

        Goods shoes2 = new Goods("5", "shoes2", R.drawable.shoes);
        goodsList.add(shoes2);

        Goods car2 = new Goods("6", "car2", R.drawable.car);
        goodsList.add(car2);

        Goods ceramic2 = new Goods("7", "ceramic2", R.drawable.ceramic);
        goodsList.add(ceramic2);

        Goods photoframe2 = new Goods("8", "photoframe2", R.drawable.photoframe);
        goodsList.add(photoframe2);

        Goods watch2 = new Goods("9", "watch2", R.drawable.watch);
        goodsList.add(watch2);
    }
    /**
     * 리스트 셋팅
     */
    private void setUpList(){
        listView = findViewById(R.id.goods_listView);

        GoodsAdapter adapter = new GoodsAdapter(getApplicationContext(), 0, goodsList);
        listView.setAdapter(adapter);
    }
    /**
     * 상세페이지 이벤트
     */
    private void setUpOnClickListener(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long I){

                Goods selectGoods = (Goods) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), DetailActivity.class);
                showDetail.putExtra("id", selectGoods.getId());
                startActivity(showDetail);
            }
        });
    }

}
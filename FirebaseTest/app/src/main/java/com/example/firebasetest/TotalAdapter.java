package com.example.firebasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class TotalAdapter extends ArrayAdapter<Item> {
    private List<Item> endItem;
    private Context context;

    // 총 이익 구할 변수
    private int totalProfitValue = 0;
    private int day5, day4, day3, day2, day1, dday;

    public TotalAdapter(Context context, List<Item> dataList){
        super(context, 0, dataList);
        this.endItem = dataList;
        this.context = context;
        // dataList 내의 각 Item에 대한 profitValue 누적
        for (Item item : dataList) {
            if (!item.getPrice().equals("share item")) {
                totalProfitValue += Integer.parseInt(item.getEndPrice()) / 20;
                if ((Integer.parseInt(item.getDifferenceDays()))==0) {
                    dday += Integer.parseInt(item.getEndPrice()) / 20;
                }
                if ((Integer.parseInt(item.getDifferenceDays()))==1) {
                    day1 += Integer.parseInt(item.getEndPrice()) / 20;
                }
                if ((Integer.parseInt(item.getDifferenceDays()))==2) {
                    day2 += Integer.parseInt(item.getEndPrice()) / 20;
                }
                if ((Integer.parseInt(item.getDifferenceDays()))==3) {
                    day3 += Integer.parseInt(item.getEndPrice()) / 20;
                }
                if ((Integer.parseInt(item.getDifferenceDays()))==4) {
                    day4 += Integer.parseInt(item.getEndPrice()) / 20;
                }
                if ((Integer.parseInt(item.getDifferenceDays()))==5) {
                    day5 += Integer.parseInt(item.getEndPrice()) / 20;
                }
            }

        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.total_items, parent, false);
        }
        Item data = getItem(position);
        if (data != null) {
            ImageView imgView = (ImageView) itemView.findViewById(R.id.ImgUrl);
            TextView id = (TextView) itemView.findViewById(R.id.id);
            TextView price = (TextView) itemView.findViewById(R.id.price);
            TextView days = (TextView) itemView.findViewById(R.id.days);

            id.setText("상품명 : "+data.getId());
            if(data.getPrice().equals("share item")){
                price.setText("무료 나눔");
            }else{
                price.setText("최종가 : "+data.getEndPrice()+" 원");
            }
            if(data.getDifferenceDays().equals("0")){
                days.setText("오늘");
            }else{
                days.setText(String.valueOf(data.getDifferenceDays())+"일 전");
            }
            Glide.with(getContext())
                    .load(data.getImageUrl1())
                    .into(imgView);
        }
        return itemView;

    }

    @Override
    public int getCount() {
        return endItem.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Item getItem(int position) {
        return endItem.get(position);
    }

    // totalProfitValue을 외부에서 불러올 수 있다.
    public Integer getTotalProfit(){
        return totalProfitValue;
    }
    public Integer get5Daysago(){
        return day5;
    }
    public Integer get4Daysago(){
        return day4;
    }
    public Integer get3Daysago(){
        return day3;
    }
    public Integer get2Daysago(){
        return day2;
    }
    public Integer get1Daysago(){
        return day1;
    }
    public Integer getToday(){
        return dday;
    }

}

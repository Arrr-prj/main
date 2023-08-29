package com.example.firebasetest;
// 홈에서 사용합니다

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firebasetest.R;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BannerItem> bannerItemList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_banner, parent, false);
        return new BannerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (bannerItemList != null) {
            ((BannerViewHolder) holder).bind(bannerItemList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // Functions
    public void submitList(List<BannerItem> list) {
        bannerItemList = list;
        notifyDataSetChanged();
    }

    // ViewHolder
    static class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivBannerImage;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBannerImage = itemView.findViewById(R.id.iv_banner_image);
        }

        public void bind(BannerItem bannerItem) {
            ivBannerImage.setImageResource(bannerItem.getImage());
        }
    }
}
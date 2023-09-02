package com.example.firebasetest;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageSliderAdaptertest extends RecyclerView.Adapter<ImageSliderAdaptertest.ImageSliderViewHolder> {

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    private Context context;
    private OnImageClickListener imageClickListener;
    private ArrayList<Uri> sliderImages;

    // 생성자 수정
    public ImageSliderAdaptertest(ArrayList<Uri> sliderImages) {
        this.context = context; // Context 초기화
        this.sliderImages = sliderImages;
    }

    public void setImageClickListener(OnImageClickListener listener) {
        this.imageClickListener = listener;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slider, parent, false);
        return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        Uri imageUri = sliderImages.get(position);
        holder.bindSliderImage(imageUri);
    }

    @Override
    public int getItemCount() {
        return sliderImages.size();
    }

    public class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);

            imageView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && imageClickListener != null) {
                    imageClickListener.onImageClick(position);
                } else {
                    // 다른 처리 로직 추가
                }
            });
        }

        public void bindSliderImage(Uri imageUri) {
            Glide.with(context)
                    .load(imageUri)
                    .into(imageView);
        }
    }
}

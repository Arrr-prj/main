// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.firebasetest.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityOpenDetailItemBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBack;

  @NonNull
  public final Button btnBidEnd;

  @NonNull
  public final Button btnBidbutton;

  @NonNull
  public final Button btnBigbutton;

  @NonNull
  public final Button btnBuy;

  @NonNull
  public final Button btnBuyConfirm;

  @NonNull
  public final ImageButton btnHome;

  @NonNull
  public final TextView category;

  @NonNull
  public final TextView endPrice;

  @NonNull
  public final EditText etBid;

  @NonNull
  public final TextView futureMillis;

  @NonNull
  public final ConstraintLayout item;

  @NonNull
  public final TextView itemId;

  @NonNull
  public final TextView itemInfo;

  @NonNull
  public final TextView itemTitle;

  @NonNull
  public final TextView seller;

  @NonNull
  public final ViewPager2 sliderViewPager;

  @NonNull
  public final TextView startPrice;

  @NonNull
  public final TextView timeInfoinfo;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView tvCategory;

  @NonNull
  public final TextView tvEndPrice;

  @NonNull
  public final TextView tvItemId;

  @NonNull
  public final TextView tvItemInfo;

  @NonNull
  public final TextView tvSeller;

  @NonNull
  public final TextView tvStartPrice;

  @NonNull
  public final TextView tvTimeInfo;

  private ActivityOpenDetailItemBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnBack,
      @NonNull Button btnBidEnd, @NonNull Button btnBidbutton, @NonNull Button btnBigbutton,
      @NonNull Button btnBuy, @NonNull Button btnBuyConfirm, @NonNull ImageButton btnHome,
      @NonNull TextView category, @NonNull TextView endPrice, @NonNull EditText etBid,
      @NonNull TextView futureMillis, @NonNull ConstraintLayout item, @NonNull TextView itemId,
      @NonNull TextView itemInfo, @NonNull TextView itemTitle, @NonNull TextView seller,
      @NonNull ViewPager2 sliderViewPager, @NonNull TextView startPrice,
      @NonNull TextView timeInfoinfo, @NonNull Toolbar toolbar, @NonNull TextView tvCategory,
      @NonNull TextView tvEndPrice, @NonNull TextView tvItemId, @NonNull TextView tvItemInfo,
      @NonNull TextView tvSeller, @NonNull TextView tvStartPrice, @NonNull TextView tvTimeInfo) {
    this.rootView = rootView;
    this.btnBack = btnBack;
    this.btnBidEnd = btnBidEnd;
    this.btnBidbutton = btnBidbutton;
    this.btnBigbutton = btnBigbutton;
    this.btnBuy = btnBuy;
    this.btnBuyConfirm = btnBuyConfirm;
    this.btnHome = btnHome;
    this.category = category;
    this.endPrice = endPrice;
    this.etBid = etBid;
    this.futureMillis = futureMillis;
    this.item = item;
    this.itemId = itemId;
    this.itemInfo = itemInfo;
    this.itemTitle = itemTitle;
    this.seller = seller;
    this.sliderViewPager = sliderViewPager;
    this.startPrice = startPrice;
    this.timeInfoinfo = timeInfoinfo;
    this.toolbar = toolbar;
    this.tvCategory = tvCategory;
    this.tvEndPrice = tvEndPrice;
    this.tvItemId = tvItemId;
    this.tvItemInfo = tvItemInfo;
    this.tvSeller = tvSeller;
    this.tvStartPrice = tvStartPrice;
    this.tvTimeInfo = tvTimeInfo;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityOpenDetailItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityOpenDetailItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_open_detail_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityOpenDetailItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_back;
      Button btnBack = ViewBindings.findChildViewById(rootView, id);
      if (btnBack == null) {
        break missingId;
      }

      id = R.id.btn_bidEnd;
      Button btnBidEnd = ViewBindings.findChildViewById(rootView, id);
      if (btnBidEnd == null) {
        break missingId;
      }

      id = R.id.btn_bidbutton;
      Button btnBidbutton = ViewBindings.findChildViewById(rootView, id);
      if (btnBidbutton == null) {
        break missingId;
      }

      id = R.id.btn_bigbutton;
      Button btnBigbutton = ViewBindings.findChildViewById(rootView, id);
      if (btnBigbutton == null) {
        break missingId;
      }

      id = R.id.btn_buy;
      Button btnBuy = ViewBindings.findChildViewById(rootView, id);
      if (btnBuy == null) {
        break missingId;
      }

      id = R.id.btn_buyConfirm;
      Button btnBuyConfirm = ViewBindings.findChildViewById(rootView, id);
      if (btnBuyConfirm == null) {
        break missingId;
      }

      id = R.id.btn_home;
      ImageButton btnHome = ViewBindings.findChildViewById(rootView, id);
      if (btnHome == null) {
        break missingId;
      }

      id = R.id.category;
      TextView category = ViewBindings.findChildViewById(rootView, id);
      if (category == null) {
        break missingId;
      }

      id = R.id.endPrice;
      TextView endPrice = ViewBindings.findChildViewById(rootView, id);
      if (endPrice == null) {
        break missingId;
      }

      id = R.id.et_bid;
      EditText etBid = ViewBindings.findChildViewById(rootView, id);
      if (etBid == null) {
        break missingId;
      }

      id = R.id.futureMillis;
      TextView futureMillis = ViewBindings.findChildViewById(rootView, id);
      if (futureMillis == null) {
        break missingId;
      }

      ConstraintLayout item = (ConstraintLayout) rootView;

      id = R.id.itemId;
      TextView itemId = ViewBindings.findChildViewById(rootView, id);
      if (itemId == null) {
        break missingId;
      }

      id = R.id.itemInfo;
      TextView itemInfo = ViewBindings.findChildViewById(rootView, id);
      if (itemInfo == null) {
        break missingId;
      }

      id = R.id.itemTitle;
      TextView itemTitle = ViewBindings.findChildViewById(rootView, id);
      if (itemTitle == null) {
        break missingId;
      }

      id = R.id.seller;
      TextView seller = ViewBindings.findChildViewById(rootView, id);
      if (seller == null) {
        break missingId;
      }

      id = R.id.sliderViewPager;
      ViewPager2 sliderViewPager = ViewBindings.findChildViewById(rootView, id);
      if (sliderViewPager == null) {
        break missingId;
      }

      id = R.id.startPrice;
      TextView startPrice = ViewBindings.findChildViewById(rootView, id);
      if (startPrice == null) {
        break missingId;
      }

      id = R.id.timeInfoinfo;
      TextView timeInfoinfo = ViewBindings.findChildViewById(rootView, id);
      if (timeInfoinfo == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.tv_category;
      TextView tvCategory = ViewBindings.findChildViewById(rootView, id);
      if (tvCategory == null) {
        break missingId;
      }

      id = R.id.tv_endPrice;
      TextView tvEndPrice = ViewBindings.findChildViewById(rootView, id);
      if (tvEndPrice == null) {
        break missingId;
      }

      id = R.id.tv_itemId;
      TextView tvItemId = ViewBindings.findChildViewById(rootView, id);
      if (tvItemId == null) {
        break missingId;
      }

      id = R.id.tv_itemInfo;
      TextView tvItemInfo = ViewBindings.findChildViewById(rootView, id);
      if (tvItemInfo == null) {
        break missingId;
      }

      id = R.id.tv_seller;
      TextView tvSeller = ViewBindings.findChildViewById(rootView, id);
      if (tvSeller == null) {
        break missingId;
      }

      id = R.id.tv_startPrice;
      TextView tvStartPrice = ViewBindings.findChildViewById(rootView, id);
      if (tvStartPrice == null) {
        break missingId;
      }

      id = R.id.tv_timeInfo;
      TextView tvTimeInfo = ViewBindings.findChildViewById(rootView, id);
      if (tvTimeInfo == null) {
        break missingId;
      }

      return new ActivityOpenDetailItemBinding((ConstraintLayout) rootView, btnBack, btnBidEnd,
          btnBidbutton, btnBigbutton, btnBuy, btnBuyConfirm, btnHome, category, endPrice, etBid,
          futureMillis, item, itemId, itemInfo, itemTitle, seller, sliderViewPager, startPrice,
          timeInfoinfo, toolbar, tvCategory, tvEndPrice, tvItemId, tvItemInfo, tvSeller,
          tvStartPrice, tvTimeInfo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public final class ActivityDetailBiddingItemBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText bidAmountEditText;

  @NonNull
  public final LinearLayout bidPopupLayout;

  @NonNull
  public final Button biddngBid;

  @NonNull
  public final Button btnBack;

  @NonNull
  public final Button btnBidEnd;

  @NonNull
  public final Button btnBigbutton;

  @NonNull
  public final Button btnBuy;

  @NonNull
  public final Button btnBuyConfirm;

  @NonNull
  public final ImageButton btnHome;

  @NonNull
  public final Button cancelBidButton;

  @NonNull
  public final TextView category;

  @NonNull
  public final Button confirmBidButton;

  @NonNull
  public final TextView endTime;

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
  public final LinearLayout linearLayout;

  @NonNull
  public final TextView seller;

  @NonNull
  public final ViewPager2 sliderViewPager;

  @NonNull
  public final TextView startPrice;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView tvCategory;

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

  private ActivityDetailBiddingItemBinding(@NonNull ConstraintLayout rootView,
      @NonNull EditText bidAmountEditText, @NonNull LinearLayout bidPopupLayout,
      @NonNull Button biddngBid, @NonNull Button btnBack, @NonNull Button btnBidEnd,
      @NonNull Button btnBigbutton, @NonNull Button btnBuy, @NonNull Button btnBuyConfirm,
      @NonNull ImageButton btnHome, @NonNull Button cancelBidButton, @NonNull TextView category,
      @NonNull Button confirmBidButton, @NonNull TextView endTime, @NonNull TextView futureMillis,
      @NonNull ConstraintLayout item, @NonNull TextView itemId, @NonNull TextView itemInfo,
      @NonNull TextView itemTitle, @NonNull LinearLayout linearLayout, @NonNull TextView seller,
      @NonNull ViewPager2 sliderViewPager, @NonNull TextView startPrice, @NonNull Toolbar toolbar,
      @NonNull TextView tvCategory, @NonNull TextView tvItemId, @NonNull TextView tvItemInfo,
      @NonNull TextView tvSeller, @NonNull TextView tvStartPrice, @NonNull TextView tvTimeInfo) {
    this.rootView = rootView;
    this.bidAmountEditText = bidAmountEditText;
    this.bidPopupLayout = bidPopupLayout;
    this.biddngBid = biddngBid;
    this.btnBack = btnBack;
    this.btnBidEnd = btnBidEnd;
    this.btnBigbutton = btnBigbutton;
    this.btnBuy = btnBuy;
    this.btnBuyConfirm = btnBuyConfirm;
    this.btnHome = btnHome;
    this.cancelBidButton = cancelBidButton;
    this.category = category;
    this.confirmBidButton = confirmBidButton;
    this.endTime = endTime;
    this.futureMillis = futureMillis;
    this.item = item;
    this.itemId = itemId;
    this.itemInfo = itemInfo;
    this.itemTitle = itemTitle;
    this.linearLayout = linearLayout;
    this.seller = seller;
    this.sliderViewPager = sliderViewPager;
    this.startPrice = startPrice;
    this.toolbar = toolbar;
    this.tvCategory = tvCategory;
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
  public static ActivityDetailBiddingItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDetailBiddingItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_detail_bidding_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDetailBiddingItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bidAmountEditText;
      EditText bidAmountEditText = ViewBindings.findChildViewById(rootView, id);
      if (bidAmountEditText == null) {
        break missingId;
      }

      id = R.id.bidPopupLayout;
      LinearLayout bidPopupLayout = ViewBindings.findChildViewById(rootView, id);
      if (bidPopupLayout == null) {
        break missingId;
      }

      id = R.id.biddng_bid;
      Button biddngBid = ViewBindings.findChildViewById(rootView, id);
      if (biddngBid == null) {
        break missingId;
      }

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

      id = R.id.cancelBidButton;
      Button cancelBidButton = ViewBindings.findChildViewById(rootView, id);
      if (cancelBidButton == null) {
        break missingId;
      }

      id = R.id.category;
      TextView category = ViewBindings.findChildViewById(rootView, id);
      if (category == null) {
        break missingId;
      }

      id = R.id.confirmBidButton;
      Button confirmBidButton = ViewBindings.findChildViewById(rootView, id);
      if (confirmBidButton == null) {
        break missingId;
      }

      id = R.id.endTime;
      TextView endTime = ViewBindings.findChildViewById(rootView, id);
      if (endTime == null) {
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

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
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

      return new ActivityDetailBiddingItemBinding((ConstraintLayout) rootView, bidAmountEditText,
          bidPopupLayout, biddngBid, btnBack, btnBidEnd, btnBigbutton, btnBuy, btnBuyConfirm,
          btnHome, cancelBidButton, category, confirmBidButton, endTime, futureMillis, item, itemId,
          itemInfo, itemTitle, linearLayout, seller, sliderViewPager, startPrice, toolbar,
          tvCategory, tvItemId, tvItemInfo, tvSeller, tvStartPrice, tvTimeInfo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public final class ActivityMyItemDetailBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBigbutton;

  @NonNull
  public final Button btnCancle;

  @NonNull
  public final Button btnConfirm;

  @NonNull
  public final Button btnDelete;

  @NonNull
  public final Button btnEdit;

  @NonNull
  public final ImageButton btnHome;

  @NonNull
  public final Button btnList;

  @NonNull
  public final TextView category;

  @NonNull
  public final TextView endPrice;

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
  public final LinearLayout linearLayoutConfirm;

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
  public final TextView tvEndPrice;

  @NonNull
  public final TextView tvItemId;

  @NonNull
  public final TextView tvItemInfo;

  @NonNull
  public final TextView tvSeller;

  @NonNull
  public final TextView tvStartPrice;

  private ActivityMyItemDetailBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btnBigbutton, @NonNull Button btnCancle, @NonNull Button btnConfirm,
      @NonNull Button btnDelete, @NonNull Button btnEdit, @NonNull ImageButton btnHome,
      @NonNull Button btnList, @NonNull TextView category, @NonNull TextView endPrice,
      @NonNull ConstraintLayout item, @NonNull TextView itemId, @NonNull TextView itemInfo,
      @NonNull TextView itemTitle, @NonNull LinearLayout linearLayout,
      @NonNull LinearLayout linearLayoutConfirm, @NonNull TextView seller,
      @NonNull ViewPager2 sliderViewPager, @NonNull TextView startPrice, @NonNull Toolbar toolbar,
      @NonNull TextView tvCategory, @NonNull TextView tvEndPrice, @NonNull TextView tvItemId,
      @NonNull TextView tvItemInfo, @NonNull TextView tvSeller, @NonNull TextView tvStartPrice) {
    this.rootView = rootView;
    this.btnBigbutton = btnBigbutton;
    this.btnCancle = btnCancle;
    this.btnConfirm = btnConfirm;
    this.btnDelete = btnDelete;
    this.btnEdit = btnEdit;
    this.btnHome = btnHome;
    this.btnList = btnList;
    this.category = category;
    this.endPrice = endPrice;
    this.item = item;
    this.itemId = itemId;
    this.itemInfo = itemInfo;
    this.itemTitle = itemTitle;
    this.linearLayout = linearLayout;
    this.linearLayoutConfirm = linearLayoutConfirm;
    this.seller = seller;
    this.sliderViewPager = sliderViewPager;
    this.startPrice = startPrice;
    this.toolbar = toolbar;
    this.tvCategory = tvCategory;
    this.tvEndPrice = tvEndPrice;
    this.tvItemId = tvItemId;
    this.tvItemInfo = tvItemInfo;
    this.tvSeller = tvSeller;
    this.tvStartPrice = tvStartPrice;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMyItemDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMyItemDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_my_item_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMyItemDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_bigbutton;
      Button btnBigbutton = ViewBindings.findChildViewById(rootView, id);
      if (btnBigbutton == null) {
        break missingId;
      }

      id = R.id.btn_cancle;
      Button btnCancle = ViewBindings.findChildViewById(rootView, id);
      if (btnCancle == null) {
        break missingId;
      }

      id = R.id.btn_confirm;
      Button btnConfirm = ViewBindings.findChildViewById(rootView, id);
      if (btnConfirm == null) {
        break missingId;
      }

      id = R.id.btn_delete;
      Button btnDelete = ViewBindings.findChildViewById(rootView, id);
      if (btnDelete == null) {
        break missingId;
      }

      id = R.id.btn_edit;
      Button btnEdit = ViewBindings.findChildViewById(rootView, id);
      if (btnEdit == null) {
        break missingId;
      }

      id = R.id.btn_home;
      ImageButton btnHome = ViewBindings.findChildViewById(rootView, id);
      if (btnHome == null) {
        break missingId;
      }

      id = R.id.btn_list;
      Button btnList = ViewBindings.findChildViewById(rootView, id);
      if (btnList == null) {
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

      id = R.id.linearLayout_confirm;
      LinearLayout linearLayoutConfirm = ViewBindings.findChildViewById(rootView, id);
      if (linearLayoutConfirm == null) {
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

      return new ActivityMyItemDetailBinding((ConstraintLayout) rootView, btnBigbutton, btnCancle,
          btnConfirm, btnDelete, btnEdit, btnHome, btnList, category, endPrice, item, itemId,
          itemInfo, itemTitle, linearLayout, linearLayoutConfirm, seller, sliderViewPager,
          startPrice, toolbar, tvCategory, tvEndPrice, tvItemId, tvItemInfo, tvSeller,
          tvStartPrice);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
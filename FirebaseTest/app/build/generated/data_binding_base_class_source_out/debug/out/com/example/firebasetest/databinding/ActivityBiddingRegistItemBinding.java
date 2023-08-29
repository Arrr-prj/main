// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.firebasetest.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityBiddingRegistItemBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBack;

  @NonNull
  public final Button btnCancel;

  @NonNull
  public final ImageButton btnHome;

  @NonNull
  public final Button btnItemRegist;

  @NonNull
  public final Button inputItemCategory;

  @NonNull
  public final EditText inputItemExplain;

  @NonNull
  public final ImageView inputItemImg1;

  @NonNull
  public final ImageView inputItemImg2;

  @NonNull
  public final ImageView inputItemImg3;

  @NonNull
  public final ImageView inputItemImg4;

  @NonNull
  public final ImageView inputItemImg5;

  @NonNull
  public final ImageView inputItemImg6;

  @NonNull
  public final EditText inputItemName;

  @NonNull
  public final EditText inputItemPrice;

  @NonNull
  public final EditText inputTitle;

  @NonNull
  public final LinearLayout linearimage;

  @NonNull
  public final LinearLayout linearimage1;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView writeText;

  private ActivityBiddingRegistItemBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btnBack, @NonNull Button btnCancel, @NonNull ImageButton btnHome,
      @NonNull Button btnItemRegist, @NonNull Button inputItemCategory,
      @NonNull EditText inputItemExplain, @NonNull ImageView inputItemImg1,
      @NonNull ImageView inputItemImg2, @NonNull ImageView inputItemImg3,
      @NonNull ImageView inputItemImg4, @NonNull ImageView inputItemImg5,
      @NonNull ImageView inputItemImg6, @NonNull EditText inputItemName,
      @NonNull EditText inputItemPrice, @NonNull EditText inputTitle,
      @NonNull LinearLayout linearimage, @NonNull LinearLayout linearimage1,
      @NonNull Toolbar toolbar, @NonNull TextView writeText) {
    this.rootView = rootView;
    this.btnBack = btnBack;
    this.btnCancel = btnCancel;
    this.btnHome = btnHome;
    this.btnItemRegist = btnItemRegist;
    this.inputItemCategory = inputItemCategory;
    this.inputItemExplain = inputItemExplain;
    this.inputItemImg1 = inputItemImg1;
    this.inputItemImg2 = inputItemImg2;
    this.inputItemImg3 = inputItemImg3;
    this.inputItemImg4 = inputItemImg4;
    this.inputItemImg5 = inputItemImg5;
    this.inputItemImg6 = inputItemImg6;
    this.inputItemName = inputItemName;
    this.inputItemPrice = inputItemPrice;
    this.inputTitle = inputTitle;
    this.linearimage = linearimage;
    this.linearimage1 = linearimage1;
    this.toolbar = toolbar;
    this.writeText = writeText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityBiddingRegistItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityBiddingRegistItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_bidding_regist_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityBiddingRegistItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_back;
      Button btnBack = ViewBindings.findChildViewById(rootView, id);
      if (btnBack == null) {
        break missingId;
      }

      id = R.id.btn_cancel;
      Button btnCancel = ViewBindings.findChildViewById(rootView, id);
      if (btnCancel == null) {
        break missingId;
      }

      id = R.id.btn_home;
      ImageButton btnHome = ViewBindings.findChildViewById(rootView, id);
      if (btnHome == null) {
        break missingId;
      }

      id = R.id.btn_itemRegist;
      Button btnItemRegist = ViewBindings.findChildViewById(rootView, id);
      if (btnItemRegist == null) {
        break missingId;
      }

      id = R.id.input_itemCategory;
      Button inputItemCategory = ViewBindings.findChildViewById(rootView, id);
      if (inputItemCategory == null) {
        break missingId;
      }

      id = R.id.input_itemExplain;
      EditText inputItemExplain = ViewBindings.findChildViewById(rootView, id);
      if (inputItemExplain == null) {
        break missingId;
      }

      id = R.id.input_itemImg1;
      ImageView inputItemImg1 = ViewBindings.findChildViewById(rootView, id);
      if (inputItemImg1 == null) {
        break missingId;
      }

      id = R.id.input_itemImg2;
      ImageView inputItemImg2 = ViewBindings.findChildViewById(rootView, id);
      if (inputItemImg2 == null) {
        break missingId;
      }

      id = R.id.input_itemImg3;
      ImageView inputItemImg3 = ViewBindings.findChildViewById(rootView, id);
      if (inputItemImg3 == null) {
        break missingId;
      }

      id = R.id.input_itemImg4;
      ImageView inputItemImg4 = ViewBindings.findChildViewById(rootView, id);
      if (inputItemImg4 == null) {
        break missingId;
      }

      id = R.id.input_itemImg5;
      ImageView inputItemImg5 = ViewBindings.findChildViewById(rootView, id);
      if (inputItemImg5 == null) {
        break missingId;
      }

      id = R.id.input_itemImg6;
      ImageView inputItemImg6 = ViewBindings.findChildViewById(rootView, id);
      if (inputItemImg6 == null) {
        break missingId;
      }

      id = R.id.input_itemName;
      EditText inputItemName = ViewBindings.findChildViewById(rootView, id);
      if (inputItemName == null) {
        break missingId;
      }

      id = R.id.input_itemPrice;
      EditText inputItemPrice = ViewBindings.findChildViewById(rootView, id);
      if (inputItemPrice == null) {
        break missingId;
      }

      id = R.id.input_title;
      EditText inputTitle = ViewBindings.findChildViewById(rootView, id);
      if (inputTitle == null) {
        break missingId;
      }

      id = R.id.linearimage;
      LinearLayout linearimage = ViewBindings.findChildViewById(rootView, id);
      if (linearimage == null) {
        break missingId;
      }

      id = R.id.linearimage1;
      LinearLayout linearimage1 = ViewBindings.findChildViewById(rootView, id);
      if (linearimage1 == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.write_text;
      TextView writeText = ViewBindings.findChildViewById(rootView, id);
      if (writeText == null) {
        break missingId;
      }

      return new ActivityBiddingRegistItemBinding((ConstraintLayout) rootView, btnBack, btnCancel,
          btnHome, btnItemRegist, inputItemCategory, inputItemExplain, inputItemImg1, inputItemImg2,
          inputItemImg3, inputItemImg4, inputItemImg5, inputItemImg6, inputItemName, inputItemPrice,
          inputTitle, linearimage, linearimage1, toolbar, writeText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

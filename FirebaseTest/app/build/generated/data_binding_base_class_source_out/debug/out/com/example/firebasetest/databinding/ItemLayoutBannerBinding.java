// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.firebasetest.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class ItemLayoutBannerBinding implements ViewBinding {
  @NonNull
  private final ImageView rootView;

  @NonNull
  public final ImageView ivBannerImage;

  private ItemLayoutBannerBinding(@NonNull ImageView rootView, @NonNull ImageView ivBannerImage) {
    this.rootView = rootView;
    this.ivBannerImage = ivBannerImage;
  }

  @Override
  @NonNull
  public ImageView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemLayoutBannerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemLayoutBannerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_layout_banner, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemLayoutBannerBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    ImageView ivBannerImage = (ImageView) rootView;

    return new ItemLayoutBannerBinding((ImageView) rootView, ivBannerImage);
  }
}

// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.firebasetest.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityFullScreenImageBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ViewPager2 fullScreenViewPager;

  private ActivityFullScreenImageBinding(@NonNull LinearLayout rootView,
      @NonNull ViewPager2 fullScreenViewPager) {
    this.rootView = rootView;
    this.fullScreenViewPager = fullScreenViewPager;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityFullScreenImageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityFullScreenImageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_full_screen_image, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityFullScreenImageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.fullScreenViewPager;
      ViewPager2 fullScreenViewPager = ViewBindings.findChildViewById(rootView, id);
      if (fullScreenViewPager == null) {
        break missingId;
      }

      return new ActivityFullScreenImageBinding((LinearLayout) rootView, fullScreenViewPager);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
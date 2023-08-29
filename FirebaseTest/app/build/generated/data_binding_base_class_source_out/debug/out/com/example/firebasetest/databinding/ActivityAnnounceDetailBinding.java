// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public final class ActivityAnnounceDetailBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBack;

  @NonNull
  public final Button btnDelete;

  @NonNull
  public final Button btnEdit;

  @NonNull
  public final TextView contents;

  @NonNull
  public final LinearLayout linear1;

  @NonNull
  public final LinearLayout linear2;

  @NonNull
  public final TextView title;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView tvItemId;

  @NonNull
  public final TextView writeText;

  private ActivityAnnounceDetailBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnBack,
      @NonNull Button btnDelete, @NonNull Button btnEdit, @NonNull TextView contents,
      @NonNull LinearLayout linear1, @NonNull LinearLayout linear2, @NonNull TextView title,
      @NonNull Toolbar toolbar, @NonNull TextView tvItemId, @NonNull TextView writeText) {
    this.rootView = rootView;
    this.btnBack = btnBack;
    this.btnDelete = btnDelete;
    this.btnEdit = btnEdit;
    this.contents = contents;
    this.linear1 = linear1;
    this.linear2 = linear2;
    this.title = title;
    this.toolbar = toolbar;
    this.tvItemId = tvItemId;
    this.writeText = writeText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityAnnounceDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityAnnounceDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_announce_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityAnnounceDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_back;
      Button btnBack = ViewBindings.findChildViewById(rootView, id);
      if (btnBack == null) {
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

      id = R.id.contents;
      TextView contents = ViewBindings.findChildViewById(rootView, id);
      if (contents == null) {
        break missingId;
      }

      id = R.id.linear1;
      LinearLayout linear1 = ViewBindings.findChildViewById(rootView, id);
      if (linear1 == null) {
        break missingId;
      }

      id = R.id.linear2;
      LinearLayout linear2 = ViewBindings.findChildViewById(rootView, id);
      if (linear2 == null) {
        break missingId;
      }

      id = R.id.title;
      TextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.tv_itemId;
      TextView tvItemId = ViewBindings.findChildViewById(rootView, id);
      if (tvItemId == null) {
        break missingId;
      }

      id = R.id.write_text;
      TextView writeText = ViewBindings.findChildViewById(rootView, id);
      if (writeText == null) {
        break missingId;
      }

      return new ActivityAnnounceDetailBinding((ConstraintLayout) rootView, btnBack, btnDelete,
          btnEdit, contents, linear1, linear2, title, toolbar, tvItemId, writeText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

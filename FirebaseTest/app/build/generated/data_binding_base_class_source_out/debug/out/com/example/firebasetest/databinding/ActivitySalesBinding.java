// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.firebasetest.R;
import com.github.mikephil.charting.charts.BarChart;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySalesBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBack;

  @NonNull
  public final BarChart daySaleChart;

  @NonNull
  public final ListView listView;

  @NonNull
  public final Switch swGORl;

  @NonNull
  public final TextView textView7;

  @NonNull
  public final Toolbar toolbar2;

  @NonNull
  public final TextView totalProfit;

  private ActivitySalesBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnBack,
      @NonNull BarChart daySaleChart, @NonNull ListView listView, @NonNull Switch swGORl,
      @NonNull TextView textView7, @NonNull Toolbar toolbar2, @NonNull TextView totalProfit) {
    this.rootView = rootView;
    this.btnBack = btnBack;
    this.daySaleChart = daySaleChart;
    this.listView = listView;
    this.swGORl = swGORl;
    this.textView7 = textView7;
    this.toolbar2 = toolbar2;
    this.totalProfit = totalProfit;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySalesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySalesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_sales, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySalesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_back;
      Button btnBack = ViewBindings.findChildViewById(rootView, id);
      if (btnBack == null) {
        break missingId;
      }

      id = R.id.day_sale_chart;
      BarChart daySaleChart = ViewBindings.findChildViewById(rootView, id);
      if (daySaleChart == null) {
        break missingId;
      }

      id = R.id.listView;
      ListView listView = ViewBindings.findChildViewById(rootView, id);
      if (listView == null) {
        break missingId;
      }

      id = R.id.sw_gORl;
      Switch swGORl = ViewBindings.findChildViewById(rootView, id);
      if (swGORl == null) {
        break missingId;
      }

      id = R.id.textView7;
      TextView textView7 = ViewBindings.findChildViewById(rootView, id);
      if (textView7 == null) {
        break missingId;
      }

      id = R.id.toolbar2;
      Toolbar toolbar2 = ViewBindings.findChildViewById(rootView, id);
      if (toolbar2 == null) {
        break missingId;
      }

      id = R.id.totalProfit;
      TextView totalProfit = ViewBindings.findChildViewById(rootView, id);
      if (totalProfit == null) {
        break missingId;
      }

      return new ActivitySalesBinding((ConstraintLayout) rootView, btnBack, daySaleChart, listView,
          swGORl, textView7, toolbar2, totalProfit);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

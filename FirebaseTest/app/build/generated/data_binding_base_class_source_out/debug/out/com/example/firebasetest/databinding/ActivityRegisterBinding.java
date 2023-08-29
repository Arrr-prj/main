// Generated by view binder compiler. Do not edit!
package com.example.firebasetest.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public final class ActivityRegisterBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnBack;

  @NonNull
  public final Button btnRegister;

  @NonNull
  public final EditText etAddress;

  @NonNull
  public final EditText etEmail;

  @NonNull
  public final EditText etName;

  @NonNull
  public final EditText etNickname;

  @NonNull
  public final EditText etPhoneNum;

  @NonNull
  public final EditText etPwd;

  @NonNull
  public final EditText etPwd2;

  @NonNull
  public final EditText etRrn;

  @NonNull
  public final EditText etSex;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final TextView textView9;

  @NonNull
  public final Toolbar toolbar2;

  private ActivityRegisterBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnBack,
      @NonNull Button btnRegister, @NonNull EditText etAddress, @NonNull EditText etEmail,
      @NonNull EditText etName, @NonNull EditText etNickname, @NonNull EditText etPhoneNum,
      @NonNull EditText etPwd, @NonNull EditText etPwd2, @NonNull EditText etRrn,
      @NonNull EditText etSex, @NonNull TextView textView, @NonNull TextView textView2,
      @NonNull TextView textView9, @NonNull Toolbar toolbar2) {
    this.rootView = rootView;
    this.btnBack = btnBack;
    this.btnRegister = btnRegister;
    this.etAddress = etAddress;
    this.etEmail = etEmail;
    this.etName = etName;
    this.etNickname = etNickname;
    this.etPhoneNum = etPhoneNum;
    this.etPwd = etPwd;
    this.etPwd2 = etPwd2;
    this.etRrn = etRrn;
    this.etSex = etSex;
    this.textView = textView;
    this.textView2 = textView2;
    this.textView9 = textView9;
    this.toolbar2 = toolbar2;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_back;
      Button btnBack = ViewBindings.findChildViewById(rootView, id);
      if (btnBack == null) {
        break missingId;
      }

      id = R.id.btn_register;
      Button btnRegister = ViewBindings.findChildViewById(rootView, id);
      if (btnRegister == null) {
        break missingId;
      }

      id = R.id.et_address;
      EditText etAddress = ViewBindings.findChildViewById(rootView, id);
      if (etAddress == null) {
        break missingId;
      }

      id = R.id.et_email;
      EditText etEmail = ViewBindings.findChildViewById(rootView, id);
      if (etEmail == null) {
        break missingId;
      }

      id = R.id.et_name;
      EditText etName = ViewBindings.findChildViewById(rootView, id);
      if (etName == null) {
        break missingId;
      }

      id = R.id.et_nickname;
      EditText etNickname = ViewBindings.findChildViewById(rootView, id);
      if (etNickname == null) {
        break missingId;
      }

      id = R.id.et_phoneNum;
      EditText etPhoneNum = ViewBindings.findChildViewById(rootView, id);
      if (etPhoneNum == null) {
        break missingId;
      }

      id = R.id.et_pwd;
      EditText etPwd = ViewBindings.findChildViewById(rootView, id);
      if (etPwd == null) {
        break missingId;
      }

      id = R.id.et_pwd2;
      EditText etPwd2 = ViewBindings.findChildViewById(rootView, id);
      if (etPwd2 == null) {
        break missingId;
      }

      id = R.id.et_rrn;
      EditText etRrn = ViewBindings.findChildViewById(rootView, id);
      if (etRrn == null) {
        break missingId;
      }

      id = R.id.et_sex;
      EditText etSex = ViewBindings.findChildViewById(rootView, id);
      if (etSex == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      id = R.id.textView9;
      TextView textView9 = ViewBindings.findChildViewById(rootView, id);
      if (textView9 == null) {
        break missingId;
      }

      id = R.id.toolbar2;
      Toolbar toolbar2 = ViewBindings.findChildViewById(rootView, id);
      if (toolbar2 == null) {
        break missingId;
      }

      return new ActivityRegisterBinding((ConstraintLayout) rootView, btnBack, btnRegister,
          etAddress, etEmail, etName, etNickname, etPhoneNum, etPwd, etPwd2, etRrn, etSex, textView,
          textView2, textView9, toolbar2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

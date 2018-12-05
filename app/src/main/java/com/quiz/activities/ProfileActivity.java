package com.quiz.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.quiz.BaseActivity;
import com.quiz.R;
import com.quiz.font.TextViewRegular;
import com.quiz.utils.QUIZPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {
    Activity mActivity = ProfileActivity.this;
    String TAG = ProfileActivity.this.getClass().getSimpleName();
    @BindView(R.id.imgBackIV)
    ImageView imgBackIV;
    @BindView(R.id.imgLogoutIV)
    ImageView imgLogoutIV;
    @BindView(R.id.txtNameTV)
    TextViewRegular txtNameTV;
    @BindView(R.id.txtEmailTV)
    TextViewRegular txtEmailTV;
    @BindView(R.id.txtMobileTV)
    TextViewRegular txtMobileTV;
    @BindView(R.id.txtPrivacyTV)
    TextViewRegular txtPrivacyTV;
    @BindView(R.id.txtInformationTV)
    TextViewRegular txtInformationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setData();
    }

    private void setData() {
        txtNameTV.setText(QUIZPreference.readString(mActivity, QUIZPreference.USER_FIRSTNAME, "") + " " + QUIZPreference.readString(mActivity, QUIZPreference.USER_LASTNAME, ""));
        txtMobileTV.setText(QUIZPreference.readString(mActivity, QUIZPreference.USER_MOBILENO, ""));
        txtEmailTV.setText(QUIZPreference.readString(mActivity, QUIZPreference.USER_EMAIL, ""));
    }

    @OnClick({R.id.imgBackIV, R.id.imgLogoutIV,R.id.txtPrivacyTV, R.id.txtInformationTV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBackIV:
                onBackPressed();
                break;
            case R.id.imgLogoutIV:
                logoutAlert();
                break;
            case R.id.txtPrivacyTV:
                Intent mPPIntent = new Intent(mActivity,InformationActivity.class);
                mPPIntent.putExtra("TYPE","PP");
                startActivity(mPPIntent);
                break;
            case R.id.txtInformationTV:
                Intent mInfoIntent = new Intent(mActivity,InformationActivity.class);
                mInfoIntent.putExtra("TYPE","INFO");
                startActivity(mInfoIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void logoutAlert() {
        final Dialog alertDialog = new Dialog(mActivity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_logout);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtTitleTV = (TextView) alertDialog.findViewById(R.id.txtTitleTV);
        TextView txtMessageTV = (TextView) alertDialog.findViewById(R.id.txtMessageTV);
        TextView txtCancelTV = (TextView) alertDialog.findViewById(R.id.txtCancelTV);
        TextView txtOkTV = (TextView) alertDialog.findViewById(R.id.txtOkTV);

        txtTitleTV.setText(getString(R.string.app_name) + "!");
        txtMessageTV.setText(getString(R.string.are_you_want));
        txtOkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                logout();
            }
        });
        txtCancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        SharedPreferences preferences = QUIZPreference.getPreferences(mActivity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent mIntent = new Intent(mActivity, LoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
        finish();
    }
}

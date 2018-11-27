package com.quiz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    public Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    public void showToast(Activity mActivity, String strMsg) {
        Toast.makeText(mActivity, strMsg, Toast.LENGTH_SHORT).show();
    }

    public void showAlert(Activity mActivity, String strTitle, String strMessage) {
        final Dialog alertDialog = new Dialog(mActivity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_alert);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // set the custom dialog components - text, image and button
        TextView txtTitleTV = (TextView) alertDialog.findViewById(R.id.txtTitleTV);
        TextView txtMessageTV = (TextView) alertDialog.findViewById(R.id.txtMessageTV);
        TextView txtOkTV = (TextView) alertDialog.findViewById(R.id.txtOkTV);
        TextView txtCancelTV = (TextView) alertDialog.findViewById(R.id.txtCancelTV);

        txtTitleTV.setText(strTitle);
        txtMessageTV.setText(strMessage);
        txtOkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
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

    public void showProgressDialog(Activity mActivity) {
        progressDialog = new Dialog(mActivity);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    public void hideProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionSlideDownExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionSlideUPEnter();
    }


    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    public void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    public void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_to_left, R.anim.slide_from_right);
    }


    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    public void overridePendingTransitionSlideUPEnter() {
        overridePendingTransition(R.anim.bottom_up, 0);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    public void overridePendingTransitionSlideDownExit() {
        overridePendingTransition(0, R.anim.bottom_down);
    }
}

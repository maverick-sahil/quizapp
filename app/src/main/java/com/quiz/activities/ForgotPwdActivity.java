package com.quiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quiz.BaseActivity;
import com.quiz.QUIZApplication;
import com.quiz.R;
import com.quiz.font.ButtonRegular;
import com.quiz.font.EditTextRegular;
import com.quiz.utils.Constants;
import com.quiz.utils.Utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPwdActivity extends BaseActivity {
    Activity mActivity = ForgotPwdActivity.this;
    String TAG = ForgotPwdActivity.this.getClass().getSimpleName();
    @BindView(R.id.usernameET)
    EditTextRegular usernameET;
    @BindView(R.id.btnForgotPwdB)
    ButtonRegular btnForgotPwdB;
    @BindView(R.id.imgBackIV)
    ImageView imgBackIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnForgotPwdB, R.id.imgBackIV})
    public void onViewClicked(View mView) {
        switch (mView.getId()) {
            case R.id.btnForgotPwdB:
                validate();
                break;
            case R.id.imgBackIV:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void validate() {
        if (usernameET.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_username));
        } else if (!Utilities.isValidEmaillId(usernameET.getText().toString().trim())) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_valid_username));
        } else {
            executeForgotPwdAPI();
        }
    }

    private void executeForgotPwdAPI() {
        String strEmail = usernameET.getText().toString().trim();
        String strAPIUrl = Constants.FORGOT_PWD + "&email=" + strEmail;
        showProgressDialog(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strAPIUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
                        Log.e(TAG, "onResponse: " + response);
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        if (error instanceof NetworkError) {
                            showAlert(mActivity, getString(R.string.error), getString(R.string.internetconnection));
                            return;
                        } else {
                            showAlert(mActivity, getString(R.string.error), getString(R.string.server_error));
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        QUIZApplication.getInstance().addToRequestQueue(stringRequest);
    }

    private void parseResponse(String response) {
        try{
            JSONObject mJsonObject = new JSONObject(response);
            JSONObject mResponseObj = mJsonObject.getJSONObject("response");
            if (mResponseObj.getString("success").equals("1")){
                showToast(mActivity,mResponseObj.getString("msg"));
                usernameET.setText("");
            }else{
                showAlert(mActivity,getString(R.string.error),getString(R.string.server_error));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

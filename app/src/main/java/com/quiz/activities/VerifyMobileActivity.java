package com.quiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.quiz.BaseActivity;
import com.quiz.QUIZApplication;
import com.quiz.R;
import com.quiz.utils.Constants;
import com.quiz.utils.QUIZPreference;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyMobileActivity extends BaseActivity {
    Activity mActivity = VerifyMobileActivity.this;
    String TAG = VerifyMobileActivity.this.getClass().getSimpleName();
    @BindView(R.id.editTextOtp)
    EditText editTextOtp;
    @BindView(R.id.btnVerify)
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnVerify, R.id.imgBackIV})
    public void onViewClicked(View mView) {
        switch (mView.getId()) {
            case R.id.btnVerify:
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
        if (editTextOtp.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), "Please enter OTP");
        } else {
            executeForgotPwdAPI();
        }
    }

    private void executeForgotPwdAPI() {
        String strOtp = editTextOtp.getText().toString().trim();
        String email = QUIZPreference.readString(mActivity, QUIZPreference.USER_EMAIL, "");
        String strAPIUrl = Constants.SERVER_LINK + "?email=" + email + "&verifytoken=" + strOtp;
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
        try {
            JSONObject mJsonObject = new JSONObject(response);
            JSONObject mResponseObj = mJsonObject.getJSONObject("response");
            if (mResponseObj.getString("success").equals("1")) {
//                showToast(mActivity, mResponseObj.getString("msg"));
                startActivity(new Intent(mActivity, HomeActivity.class));
                finish();
            } else {
                showAlert(mActivity, getString(R.string.error), mResponseObj.getString("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

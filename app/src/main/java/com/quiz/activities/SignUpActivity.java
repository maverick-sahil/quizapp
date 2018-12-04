package com.quiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import com.quiz.utils.Utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity {
    Activity mActivity = SignUpActivity.this;
    String TAG = SignUpActivity.this.getClass().getSimpleName();
    @BindView(R.id.firstnameET)
    EditText firstnameET;
    @BindView(R.id.lastNameET)
    EditText lastNameET;
    @BindView(R.id.usernameET)
    EditText usernameET;
    @BindView(R.id.mobileNumberET)
    EditText mobileNumberET;
    @BindView(R.id.passwordETL)
    EditText passwordETL;
    @BindView(R.id.confirmPasswordETL)
    EditText confirmPasswordETL;
    @BindView(R.id.btnSignUpB)
    Button btnSignUpB;
    @BindView(R.id.txtAlreadyAcLL)
    LinearLayout txtAlreadyAcLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnSignUpB, R.id.txtAlreadyAcLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSignUpB:
                validate();
                break;
            case R.id.txtAlreadyAcLL:
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();
                break;
        }
    }

    private void validate() {
        if (firstnameET.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_firstname));
        } else if (lastNameET.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_lastname));
        } else if (usernameET.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_username));
        } else if (!Utilities.isValidEmaillId(usernameET.getText().toString().trim())) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_valid_username));
        } else if (mobileNumberET.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_mobile_number));
        } else if (mobileNumberET.getText().toString().trim().length() != 10) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_valid_mobile_no));
        } else if (passwordETL.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_password));
        } else if (confirmPasswordETL.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter__confirm_password));
        } else if (!confirmPasswordETL.getText().toString().trim().equals(passwordETL.getText().toString().trim())) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_valid_confirm_password));
        } else {
            executeSignUpAPI();
        }
    }

    private void executeSignUpAPI() {
        String strFirstName = firstnameET.getText().toString().trim();
        String strLastName = lastNameET.getText().toString().trim();
        String strUserName = usernameET.getText().toString().trim();
        String strMobileNumber = mobileNumberET.getText().toString().trim();
        String strPassword = passwordETL.getText().toString().trim();
        String strAPIUrl = Constants.SIGNUP + "&fname=" + strFirstName + "&lname=" + strLastName + "&email=" + strUserName + "&mobile=" + strMobileNumber + "&password=" + strPassword;
        showProgressDialog(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strAPIUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
                        Log.e(TAG, "onResponse: " + response);
                        parseSearchResponse(response);
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

    private void parseSearchResponse(String response) {
        try {

            JSONObject mJsonObject = new JSONObject(response);
            JSONObject mResponseObj = mJsonObject.getJSONObject("response");
            if (mResponseObj.getString("success").equals("1")) {
                JSONObject mDataObj = mResponseObj.getJSONObject("data");
                QUIZPreference.writeBoolean(mActivity, QUIZPreference.IS_LOGIN, true);
                QUIZPreference.writeString(mActivity, QUIZPreference.USER_EMAIL, mDataObj.getString("email"));
                QUIZPreference.writeString(mActivity, QUIZPreference.USER_FIRSTNAME, mDataObj.getString("firstname"));
                QUIZPreference.writeString(mActivity, QUIZPreference.USER_LASTNAME, mDataObj.getString("lastname"));
                if (!mDataObj.isNull("mobile"))
                    QUIZPreference.writeString(mActivity, QUIZPreference.USER_MOBILENO, mDataObj.getString("mobile"));
//                startActivity(new Intent(mActivity, HomeActivity.class));
//                finish();
//                showToast(mActivity, "SignUp Successfully");
                startActivity(new Intent(mActivity, VerifyMobileActivity.class));
                finish();
            } else {
                showAlert(mActivity, getString(R.string.error), "" + mResponseObj.getString("msg"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

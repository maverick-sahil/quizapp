package com.quiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    Activity mActivity = LoginActivity.this;
    String TAG = LoginActivity.this.getClass().getSimpleName();
    @BindView(R.id.usernameET)
    EditText usernameET;
    @BindView(R.id.passwordETL)
    EditText passwordETL;
    @BindView(R.id.btnLoginB)
    Button btnLoginB;
    @BindView(R.id.txtSignUpNowTV)
    TextView txtSignUpNowTV;
    @BindView(R.id.imgGooglePlusIV)
    ImageView imgGooglePlusIV;
    @BindView(R.id.imgFacebookIV)
    ImageView imgFacebookIV;
    @BindView(R.id.imgLinkedInIV)
    ImageView imgLinkedInIV;
    @BindView(R.id.imgTwitterIV)
    ImageView imgTwitterIV;
    @BindView(R.id.txtForgotPwdTV)
    TextView txtForgotPwdTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnLoginB, R.id.txtSignUpNowTV, R.id.txtForgotPwdTV, R.id.imgGooglePlusIV, R.id.imgFacebookIV, R.id.imgLinkedInIV, R.id.imgTwitterIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLoginB:
                validate();
                break;
            case R.id.txtSignUpNowTV:
                startActivity(new Intent(mActivity, SignUpActivity.class));
                finish();
                break;
            case R.id.txtForgotPwdTV:
                startActivity(new Intent(mActivity, ForgotPwdActivity.class));
                break;
            case R.id.imgGooglePlusIV:
                break;
            case R.id.imgFacebookIV:
                break;
            case R.id.imgLinkedInIV:
                break;
            case R.id.imgTwitterIV:
                break;
        }
    }

    private void validate() {
        if (usernameET.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_username));
        }else if(!Utilities.isValidEmaillId(usernameET.getText().toString().trim())){
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_valid_username));
        } else if (passwordETL.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_password));
        } else {
            executeLoginAPI();
        }
    }

    private void executeLoginAPI() {
        String strEmail = usernameET.getText().toString().trim();
        String strPassword = passwordETL.getText().toString().trim();
        String strAPIUrl = Constants.LOGIN + "&email=" + strEmail + "&password=" + strPassword;
        showProgressDialog(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strAPIUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
                        Log.e(TAG, "onResponse: "+response);
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
        try{

            JSONObject mJsonObject = new JSONObject(response);
            JSONObject mResponseObj = mJsonObject.getJSONObject("response");
            if (mResponseObj.getString("success").equals("1")){
                JSONArray mJsonArray = mResponseObj.getJSONArray("data");
                for (int i = 0; i < mJsonArray.length(); i++){
                    JSONObject mDataObj = mJsonArray.getJSONObject(i);
                    QUIZPreference.writeBoolean(mActivity,QUIZPreference.IS_LOGIN,true);
                    QUIZPreference.writeString(mActivity,QUIZPreference.USER_EMAIL,mDataObj.getString("email"));
                    QUIZPreference.writeString(mActivity,QUIZPreference.USER_FIRSTNAME,mDataObj.getString("firstname"));
                    QUIZPreference.writeString(mActivity,QUIZPreference.USER_LASTNAME,mDataObj.getString("lastname"));
                    QUIZPreference.writeString(mActivity,QUIZPreference.USER_MOBILENO,mDataObj.getString("mobile"));
                    startActivity(new Intent(mActivity,HomeActivity.class));
                    finish();
                    showToast(mActivity,"Login Sucessfully");
                }
            }else{
                 showAlert(mActivity,getString(R.string.error),""+mResponseObj.getString("msg"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

package com.quiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import com.quiz.font.TextViewRegular;
import com.quiz.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InformationActivity extends BaseActivity {
    String TAG = InformationActivity.this.getClass().getSimpleName();
    Activity mActivity = InformationActivity.this;
    @BindView(R.id.imgBackIV)
    ImageView imgBackIV;
    @BindView(R.id.txtHeaderTextTV)
    TextViewRegular txtHeaderTextTV;
    @BindView(R.id.txtInformationTV)
    TextViewRegular txtInformationTV;

    String strType = "", strTitle = "", strContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            strType = getIntent().getStringExtra("TYPE");
        }


        executeAPI();
    }

    private void executeAPI() {
        String strAPIUrl = "";
        if (strType.equals("PP")){
            strAPIUrl = Constants.PRIVACY_POLICY;
        }else if (strType.equals("INFO")){
            strAPIUrl = Constants.INFORMATION;
        }
        
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
            JSONArray mJsonArray = new JSONArray(response);
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                strTitle = mJsonObject.getString("title");
                strContent = mJsonObject.getString("content");
            }

            txtHeaderTextTV.setText(strTitle);
            txtInformationTV.setText(strContent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imgBackIV)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

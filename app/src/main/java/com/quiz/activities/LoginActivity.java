package com.quiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.quiz.BaseActivity;
import com.quiz.QUIZApplication;
import com.quiz.R;
import com.quiz.utils.Constants;
import com.quiz.utils.QUIZPreference;
import com.quiz.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    //Google
    private final int RC_SIGN_IN = 234;
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
    @BindView(R.id.txtForgotPwdTV)
    TextView txtForgotPwdTV;
    @BindView(R.id.FBlogin_button)
    LoginButton FBlogin_button;
    @BindView(R.id.btnGoogleLogin)
    com.google.android.gms.common.SignInButton btnGoogleLogin;
    //Facebook
    CallbackManager callbackManager;
    String fbFirstName = "", fbLastName = "", fbUserName = "", fbEmail = "", fbAccessToken = "", fbID = "", fbProfilePicLarge = "", fbProfilePicSmall = "";
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    String googleFirstName = "", googleLastName = "", googleUserName = "", googleEmail = "", googleToken = "", googleMobileNO = "", googleProfilePicLarge = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook Callbacks
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Google Login Config
        googleLoginConfig();
    }
    
    private void googleLoginConfig() {

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getResources().getString(R.string.google_Client_ID))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @OnClick({R.id.btnLoginB, R.id.txtSignUpNowTV, R.id.txtForgotPwdTV, R.id.imgGooglePlusIV, R.id.imgFacebookIV})
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
                loginWithGoogle();
                break;
            case R.id.imgFacebookIV:
                loginWithFacebook();
                break;
        }
    }

    private void loginWithGoogle() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*Validate for Email Login*/
    private void validate() {
        if (usernameET.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_username));
        } else if (!Utilities.isValidEmaillId(usernameET.getText().toString().trim())) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_valid_username));
        } else if (passwordETL.getText().toString().trim().equals("")) {
            showAlert(mActivity, getString(R.string.error), getString(R.string.please_enter_password));
        } else {
            executeLoginAPI();
        }
    }

    /*Login With Email*/
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
                JSONArray mJsonArray = mResponseObj.getJSONArray("data");
                for (int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject mDataObj = mJsonArray.getJSONObject(i);
                    QUIZPreference.writeBoolean(mActivity, QUIZPreference.IS_LOGIN, true);
                    QUIZPreference.writeString(mActivity, QUIZPreference.USER_EMAIL, mDataObj.getString("email"));
                    QUIZPreference.writeString(mActivity, QUIZPreference.USER_FIRSTNAME, mDataObj.getString("firstname"));
                    QUIZPreference.writeString(mActivity, QUIZPreference.USER_LASTNAME, mDataObj.getString("lastname"));
                    QUIZPreference.writeString(mActivity, QUIZPreference.USER_MOBILENO, mDataObj.getString("mobile"));
                    startActivity(new Intent(mActivity, HomeActivity.class));
                    finish();
                    showToast(mActivity, "Login Sucessfully");
                }
            } else {
                showAlert(mActivity, getString(R.string.error), "" + mResponseObj.getString("msg"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //aauthenticating with firebase
                authenticateWithGoogle(account);
            } catch (ApiException e) {
                System.out.println("ERROR===" + e);
                 Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            //Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    /*****************************
     * Login with Facebook
     * **********************************************/
    private void loginWithFacebook() {
        if (Utilities.isNetworkAvailable(mActivity) == false) {
            showAlert(mActivity, getString(R.string.app_name), getString(R.string.internetconnection));
        } else {
            LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "email"));
            getFacebookLogin();
        }
    }


    private void getFacebookLogin() {
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e(TAG, "******onSuccess******" + loginResult);
                        getFbProfile(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.e(TAG, "*****onCancel*****");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e(TAG, "*****onError*****" + exception.toString());
                    }
                });
    }

    //Facebook
    private void getFbProfile(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        String strToken = accessToken.getToken();
                        Log.e(TAG, "*****Token*****" + strToken);
                        initiateLoginWithFB(object, strToken);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link, email");
        request.setParameters(parameters);

        request.executeAsync();

    }

    //Facebook  Data
    private void initiateLoginWithFB(JSONObject fbObject, String token) {
        try {
            try {
                String arr[] = fbObject.getString("name").split(" ");
                fbFirstName = arr[0];
                if (arr.length > 0)
                    fbLastName = arr[1];
                fbUserName = fbFirstName + fbLastName;
                if (fbObject.has("email"))
                    fbEmail = fbObject.getString("email");
                else
                    fbEmail = fbObject.getString("id") + "@facebook.com";
                fbAccessToken = token;
                fbID = fbObject.getString("id");
                fbProfilePicLarge = "https://graph.facebook.com/" + fbObject.getString("id") + "/picture?access_token=" + token;
                fbProfilePicSmall = "https://graph.facebook.com/" + fbObject.getString("id") + "/picture?access_token=" + token;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*Execute Login With Facebook Api*/
            executeFacebookLoginAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LoginManager.getInstance().logOut();
    private void executeFacebookLoginAPI() {
        String strAPIUrl = Constants.FB_SIGNUP + "&fname=" + fbFirstName + "&lname=" + fbLastName + "&email=" + fbEmail + "&mobileno=" + "1234567890" + "&guid=" + fbAccessToken;
        showProgressDialog(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strAPIUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
                        Log.e(TAG, "onResponse: " + response);
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
                                startActivity(new Intent(mActivity, HomeActivity.class));
                                finish();
                                showToast(mActivity, "SignUp Sucessfully");
                            } else {
                                showAlert(mActivity, getString(R.string.error), "" + mResponseObj.getString("msg"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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


    /*****************************
     * Login with Google
     * **********************************************/
    private void authenticateWithGoogle(GoogleSignInAccount mAccount) {
        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(mAccount.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //  Log.d(TAG, "signInWithCredential:success");
                            user = mFirebaseAuth.getCurrentUser();

                            googleEmail = user.getEmail();
                            String arr[] = user.getDisplayName().split(" ");
                            googleFirstName = arr[0];
                            if (arr.length > 0)
                                googleLastName = arr[1];
                            googleToken = user.getUid();
                            googleProfilePicLarge = user.getPhotoUrl().toString();
                            googleMobileNO = user.getPhoneNumber();

                            executeLoginWithGoogle();
                        } else {
                            // If sign in fails, display a message to the user.
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void executeLoginWithGoogle() {
        String strAPIUrl = Constants.GOOGLE_SIGNUP + "&fname=" + googleFirstName + "&lname=" + googleLastName + "&email=" + googleEmail + "&mobileno=" + googleMobileNO + "&guid=" + googleToken;
        showProgressDialog(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strAPIUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
                        Log.e(TAG, "onResponse: " + response);
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
                                startActivity(new Intent(mActivity, HomeActivity.class));
                                finish();
                                showToast(mActivity, "SignUp Sucessfully");
                            } else {
                                showAlert(mActivity, getString(R.string.error), "" + mResponseObj.getString("msg"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
}

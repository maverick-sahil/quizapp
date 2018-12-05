package com.quiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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
import com.quiz.beans.QuestionsModel;
import com.quiz.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PracticeQuizActivity extends BaseActivity {
    Activity mActivity = PracticeQuizActivity.this;
    String TAG = PracticeQuizActivity.this.getClass().getSimpleName();
    @BindView(R.id.imgBackIV)
    ImageView imgBackIV;
    @BindView(R.id.imgDashboardIV)
    ImageView imgDashboardIV;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;
    @BindView(R.id.radioButton4)
    RadioButton radioButton4;
    @BindView(R.id.radioGroupRG)
    RadioGroup radioGroupRG;
    @BindView(R.id.btnSubmitB)
    Button btnSubmitB;
    @BindView(R.id.txtQuestionTV)
    TextView txtQuestionTV;
    @BindView(R.id.txtTimeTV)
    TextView txtTimeTV;
    @BindView(R.id.txtQuestionNo)
    TextView txtQuestionNo;
    @BindView(R.id.seekBar)
    SeekBar seekBar;

    ArrayList<QuestionsModel> mQuestionsArrayList = new ArrayList<QuestionsModel>();
    int questionNum = 0;
    CountDownTimer mCountDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_quiz);
        ButterKnife.bind(this);

        seekBar.getThumb().mutate().setAlpha(0);
        seekBar.setEnabled(false);

        declareCountTimer();
        setRaioButtonFonts();
        executeQuestionsApi();
    }

    private void setRaioButtonFonts() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Lato_Regular.ttf");
        radioButton1.setTypeface(font);
        radioButton2.setTypeface(font);
        radioButton3.setTypeface(font);
        radioButton4.setTypeface(font);
    }

    private void declareCountTimer() {
        mCountDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void executeQuestionsApi() {
        showProgressDialog(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.QUESTIONS,
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
            JSONArray mJsonArray = new JSONArray(response);
            for (int i = 0; i < mJsonArray.length(); i++) {
                JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                QuestionsModel mModel = new QuestionsModel();
                if (!mJsonObject.isNull("id"))
                    mModel.setId(mJsonObject.getString("id"));
                if (!mJsonObject.isNull("questioncode"))
                    mModel.setQuestioncode(mJsonObject.getString("questioncode"));
                if (!mJsonObject.isNull("question"))
                    mModel.setQuestion(mJsonObject.getString("question"));
                if (!mJsonObject.isNull("question_desc"))
                    mModel.setQuestion_desc(mJsonObject.getString("question_desc"));
                if (!mJsonObject.isNull("ch1"))
                    mModel.setCh1(mJsonObject.getString("ch1"));
                if (!mJsonObject.isNull("ch2"))
                    mModel.setCh2(mJsonObject.getString("ch2"));
                if (!mJsonObject.isNull("ch3"))
                    mModel.setCh3(mJsonObject.getString("ch3"));
                if (!mJsonObject.isNull("ch4"))
                    mModel.setCh4(mJsonObject.getString("ch4"));
                if (!mJsonObject.isNull("correctanswer"))
                    mModel.setCorrectanswer(mJsonObject.getString("correctanswer"));
                if (!mJsonObject.isNull("questiontime"))
                    mModel.setQuestiontime(mJsonObject.getString("questiontime"));

                mQuestionsArrayList.add(mModel);
            }

            setDataOnWidget();
            seekBar.setMax(mQuestionsArrayList.size());

            Log.e(TAG, "==Questions==" + mQuestionsArrayList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.imgBackIV, R.id.imgDashboardIV, R.id.btnSubmitB})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBackIV:
                onBackPressed();
                break;
            case R.id.imgDashboardIV:
                break;
            case R.id.btnSubmitB:
                setUpSubmitClick();
                break;
        }
    }

    private void setUpSubmitClick() {
        if (mQuestionsArrayList.size() - 1 == questionNum) {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
                mCountDownTimer = null;
            }

            Intent mIntent = new Intent(mActivity, ResultActivity.class);
            mIntent.putExtra("LIST", mQuestionsArrayList);
            startActivity(mIntent);
            finish();
            showToast(mActivity, "Check Your Result!");
        } else {
            ++questionNum;
            //cancel the old countDownTimer
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
                mCountDownTimer = null;
            }
            setDataOnWidget();

            if (mQuestionsArrayList.size() - 1 == questionNum) {
                btnSubmitB.setText("Submit");
            }
        }
    }

    private void setDataOnWidget() {
        QuestionsModel mQuestionsModel = mQuestionsArrayList.get(questionNum);
        txtQuestionTV.setText(mQuestionsModel.getQuestion());

        radioGroupRG.clearCheck();

        radioButton1.setText(mQuestionsModel.getCh1());
        radioButton1.setChecked(false);
        radioButton2.setText(mQuestionsModel.getCh2());
        radioButton2.setChecked(false);
        radioButton3.setText(mQuestionsModel.getCh3());
        radioButton3.setChecked(false);
        radioButton4.setText(mQuestionsModel.getCh4());
        radioButton4.setChecked(false);
//        countDownTimer(Utilities.getMilliseconds(mQuestionsModel.getQuestiontime()));
        long time = Long.parseLong(mQuestionsModel.getQuestiontime()) * 1000;

        countDownTimer(time);

        txtQuestionNo.setText((questionNum + 1) + "/" + mQuestionsArrayList.size());
        seekBar.setProgress((questionNum + 1));


        radioGroupRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.radioButton1:
                        mQuestionsArrayList.get(questionNum).setChooseAnswer(rb.getText().toString());
                        mQuestionsArrayList.get(questionNum).setSkip(false);
                        break;
                    case R.id.radioButton2:
                        mQuestionsArrayList.get(questionNum).setChooseAnswer(rb.getText().toString());
                        mQuestionsArrayList.get(questionNum).setSkip(false);
                        break;
                    case R.id.radioButton3:
                        mQuestionsArrayList.get(questionNum).setChooseAnswer(rb.getText().toString());
                        mQuestionsArrayList.get(questionNum).setSkip(false);
                        break;
                    case R.id.radioButton4:
                        mQuestionsArrayList.get(questionNum).setChooseAnswer(rb.getText().toString());
                        mQuestionsArrayList.get(questionNum).setSkip(false);
                        break;
                    default:
                        mQuestionsArrayList.get(questionNum).setChooseAnswer("na");
                        mQuestionsArrayList.get(questionNum).setSkip(true);
                        Log.e(TAG, "======NA=====");
                        break;
                }

            }
        });
    }

    private void countDownTimer(long milliseconds) {
        mCountDownTimer = new CountDownTimer(milliseconds * 1000, 1000) {

            @Override
            public void onFinish() {
                txtTimeTV.setText("0:00");
                if (mQuestionsArrayList.size() - 1 < questionNum) {
                    setUpSubmitClick();
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                txtTimeTV.setText("" + String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

        };

        mCountDownTimer.start();
    }
}

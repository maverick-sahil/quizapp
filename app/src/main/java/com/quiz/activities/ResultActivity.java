package com.quiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.quiz.BaseActivity;
import com.quiz.R;
import com.quiz.beans.QuestionsModel;
import com.quiz.font.TextViewLight;
import com.quiz.font.TextViewMedium;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends BaseActivity {
    Activity mActivity = ResultActivity.this;
    String TAG = ResultActivity.this.getClass().getSimpleName();
    @BindView(R.id.imgBackIV)
    ImageView imgBackIV;
    @BindView(R.id.imgDashboardIV)
    ImageView imgDashboardIV;
    @BindView(R.id.txtTotalQuesValueTV)
    TextViewLight txtTotalQuesValueTV;
    @BindView(R.id.txtCorrectQuesValueTV)
    TextViewLight txtCorrectQuesValueTV;
    @BindView(R.id.txtINCorrectQuesValueTV)
    TextViewLight txtINCorrectQuesValueTV;
    @BindView(R.id.txtSkiptQuesValueTV)
    TextViewLight txtSkiptQuesValueTV;
    @BindView(R.id.txtScoreValueTV)
    TextViewMedium txtScoreValueTV;

    String strTotalQuestions = "", strCorrectAnswer = "", strIncorrectAnswer = "", strSkipQuestion = "";

    ArrayList<QuestionsModel> mQuestionsArrayList = new ArrayList<QuestionsModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            mQuestionsArrayList = (ArrayList<QuestionsModel>) getIntent().getSerializableExtra("LIST");
            setDataResults();
        }
    }

    private void setDataResults() {
        /*Set Up Results*/
        strTotalQuestions = "" + mQuestionsArrayList.size();
        txtTotalQuesValueTV.setText(strTotalQuestions);
        int skipCount = 0;
        int correctCount = 0;
        int inCorrectCount = 0;
        int yourScore = 0;
        for (int i = 0; i < mQuestionsArrayList.size(); i++) {
            if (mQuestionsArrayList.get(i).isSkip() == true) {
                skipCount++;
            } else if (mQuestionsArrayList.get(i).getCorrectanswer().toLowerCase().trim().equalsIgnoreCase(mQuestionsArrayList.get(i).getChooseAnswer().trim())) {
                correctCount++;
            } else {
                inCorrectCount++;
            }
        }

        yourScore = mQuestionsArrayList.size() - (inCorrectCount + skipCount);

        /*Set Values*/
        txtCorrectQuesValueTV.setText("" + correctCount);
        txtINCorrectQuesValueTV.setText("" + inCorrectCount);
        txtScoreValueTV.setText("" + yourScore);
        txtSkiptQuesValueTV.setText("" + skipCount);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.imgBackIV, R.id.imgDashboardIV})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBackIV:
                onBackPressed();
                break;
            case R.id.imgDashboardIV:
                showToast(mActivity, "Coming Soon...");
                break;
        }
    }
}

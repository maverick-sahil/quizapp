package com.quiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quiz.BaseActivity;
import com.quiz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {
    Activity mActivity = HomeActivity.this;
    String TAG = HomeActivity.this.getClass().getSimpleName();
    @BindView(R.id.imgBackIV)
    ImageView imgBackIV;
    @BindView(R.id.imgDashboardIV)
    ImageView imgDashboardIV;
    @BindView(R.id.playQuizLL)
    LinearLayout playQuizLL;
    @BindView(R.id.practiceQuizLL)
    LinearLayout practiceQuizLL;
    @BindView(R.id.leaderBoardLL)
    LinearLayout leaderBoardLL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.imgBackIV, R.id.imgDashboardIV, R.id.playQuizLL, R.id.practiceQuizLL, R.id.leaderBoardLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBackIV:
                onBackPressed();
                break;
            case R.id.imgDashboardIV:
                break;
            case R.id.playQuizLL:
                startActivity(new Intent(mActivity,PlayQuizActivity.class));
                break;
            case R.id.practiceQuizLL:
                startActivity(new Intent(mActivity,PracticeQuizActivity.class));
                break;
            case R.id.leaderBoardLL:
                startActivity(new Intent(mActivity,LeaderBoardActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




}

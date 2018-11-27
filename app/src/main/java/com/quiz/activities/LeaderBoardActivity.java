package com.quiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.quiz.BaseActivity;
import com.quiz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeaderBoardActivity extends BaseActivity {
    Activity mActivity = LeaderBoardActivity.this;
    String TAG = LeaderBoardActivity.this.getClass().getSimpleName();
    @BindView(R.id.imgBackIV)
    ImageView imgBackIV;
    @BindView(R.id.imgDashboardIV)
    ImageView imgDashboardIV;
    @BindView(R.id.btnSubmitB)
    Button btnSubmitB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        ButterKnife.bind(this);
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
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

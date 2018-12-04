package com.quiz.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quiz.BaseActivity;
import com.quiz.R;
import com.quiz.font.TextViewMedium;
import com.quiz.receiver.AlarmReceiver;
import com.quiz.receiver.ObservableObject;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements Observer {
    Activity mActivity = HomeActivity.this;
    String TAG = HomeActivity.this.getClass().getSimpleName();
    @BindView(R.id.imgProfileIV)
    ImageView imgProfileIV;
    @BindView(R.id.imgDashboardIV)
    ImageView imgDashboardIV;
    @BindView(R.id.playQuizLL)
    LinearLayout playQuizLL;
    @BindView(R.id.practiceQuizLL)
    LinearLayout practiceQuizLL;
    @BindView(R.id.leaderBoardLL)
    LinearLayout leaderBoardLL;
    private Calendar calendar;
    private static CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setPeriodicAlarm();
        ObservableObject.getInstance().addObserver(this);
    }


    @OnClick({R.id.imgProfileIV, R.id.imgDashboardIV, R.id.playQuizLL, R.id.practiceQuizLL, R.id.leaderBoardLL})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgProfileIV:
                startActivity(new Intent(mActivity, ProfileActivity.class));
                break;
            case R.id.imgDashboardIV:
                break;
            case R.id.playQuizLL:
                startActivity(new Intent(mActivity, PlayQuizActivity.class));
//                startQuiz();
                break;
            case R.id.practiceQuizLL:
                startActivity(new Intent(mActivity, PracticeQuizActivity.class));
                break;
            case R.id.leaderBoardLL:
                startActivity(new Intent(mActivity, LeaderBoardActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mCountDownTimer !=null){
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        finish();
    }


    private void setPeriodicAlarm() {
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, i, 0);
        long first = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);

        calendar.set(Calendar.MINUTE, minute + 1);
        calendar.set(Calendar.SECOND, 0);
        long interval = 60 * 1000;
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), interval, broadcast);
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean isVisible = (boolean) arg;

        if (isVisible)
            playQuizLL.setVisibility(View.VISIBLE);
        else
            playQuizLL.setVisibility(View.GONE);
    }


    private void startQuiz() {
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int hour = calendar.get(Calendar.HOUR);

        if (minute == 29 || minute == 59) {
            showCountDownTimer(60 - second);
        } else if (minute == 30 || minute == 0) {
            startActivity(new Intent(mActivity, PlayQuizActivity.class));
        }
    }

    private void showCountDownTimer(long milliSeconds) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_countdown_timer);

        final TextViewMedium text = (TextViewMedium) dialog.findViewById(R.id.textViewCountdown);
        mCountDownTimer = new CountDownTimer(milliSeconds * 1000, 1000) {
            @Override
            public void onFinish() {
                Log.d("ontick", "--------------------");
                text.setText("00:00");
                if (dialog != null) {
                    dialog.dismiss();
                }
                startActivity(new Intent(mActivity, PlayQuizActivity.class));
            }

            @Override
            public void onTick(long millisUntilFinished) {
                text.setText("" + String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
        };

        mCountDownTimer.start();

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);

        if (minute == 29 || minute == 59)
            playQuizLL.setVisibility(View.VISIBLE);
        else
            playQuizLL.setVisibility(View.GONE);
    }
}

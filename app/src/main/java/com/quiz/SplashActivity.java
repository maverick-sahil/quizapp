package com.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.quiz.activities.HomeActivity;
import com.quiz.activities.LoginActivity;
import com.quiz.utils.QUIZPreference;

public class SplashActivity extends AppCompatActivity {
    int SPLASH_TIME_OUT = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setSplash();
    }

    private void setSplash(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(SPLASH_TIME_OUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (QUIZPreference.readBoolean(SplashActivity.this,QUIZPreference.IS_LOGIN,false) == true){
                    startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }
        }).start();
    }
}

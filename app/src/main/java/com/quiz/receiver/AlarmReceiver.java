package com.quiz.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.quiz.R;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Received", new Date() + "");

        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);

        if (minute == 25 || minute == 55) {
            sendTestNotification(context);
        }

        if (minute == 29 || minute == 59 || minute == 0 || minute == 30) {
            ObservableObject.getInstance().updateValue(true);
        }else{
            ObservableObject.getInstance().updateValue(false);
        }
    }

    private void sendTestNotification(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Quiz App")
                        .setContentText("Test starts in 5 minutes.");

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
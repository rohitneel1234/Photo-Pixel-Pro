package com.rohitneel.photopixelpro.photocollage.support;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.rohitneel.photopixelpro.activities.MainActivity;
import com.rohitneel.photopixelpro.photocollage.Photo;


public class MyExceptionHandlerPix implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public MyExceptionHandlerPix(Activity a) {
        activity = a;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = null;
        if (activity != null) {
            intent = new Intent(activity, MainActivity.class);
        } else if (Photo.getContext() != null) {
            intent = new Intent(Photo.getContext(), MainActivity.class);
        }
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(Photo.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) Photo.getContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, pendingIntent);
        System.exit(2);
    }
}
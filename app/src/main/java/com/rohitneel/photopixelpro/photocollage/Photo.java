package com.rohitneel.photopixelpro.photocollage;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

//import com.onesignal.OneSignal;
import com.rohitneel.photopixelpro.R;


public class Photo extends Application {
    private static Photo queShot;

    public void onCreate() {
        super.onCreate();
       /* OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getResources().getString(R.string.oneSignal_id));*/
        queShot = this;
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke( null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Context getContext() {
        return queShot.getContext();
    }

}

package com.wastatus.savestory.statussaver.directmessage.savemedia;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;

import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobOpenAds;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;

public class App extends Application {
    public static String channelId = "";
    public static App instance;

    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPreferences;
    private static final String ONESIGNAL_APP_ID = "d751619d-fa4a-431b-bb4d-2692d2b6dbdc";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        channelId = getString(R.string.app_name);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(getString(R.string.app_name));
            getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        }


        SharedPreferences sharedPreferences2 = getSharedPreferences("Delete message recover", 0);
        sharedPreferences = sharedPreferences2;
        SharedPreferences.Editor edit = sharedPreferences2.edit();
        editor = edit;
        edit.apply();

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        if (AdmobAdsManager.isAdmob) {

            MobileAds.initialize(this, initializationStatus -> {
                new AdmobOpenAds(App.this);
            });
        }


    }

    public static App getInstance() {
        return instance;
    }
}

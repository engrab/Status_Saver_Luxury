package com.wastatus.savestory.statussaver.directmessage.savemedia;

import android.app.Application;

import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobOpenAds;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;

public class App extends Application {

    private static final String ONESIGNAL_APP_ID = "d751619d-fa4a-431b-bb4d-2692d2b6dbdc";

    @Override
    public void onCreate() {
        super.onCreate();

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
}

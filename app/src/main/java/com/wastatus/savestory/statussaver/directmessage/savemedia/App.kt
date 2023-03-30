package com.wastatus.savestory.statussaver.directmessage.savemedia

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobOpenAds
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.CHANNEL_ID_PERIOD_WORK

class App : Application() {
    companion object {


        const val channelId: String = "notification"
        private const val ONESIGNAL_APP_ID = "d751619d-fa4a-431b-bb4d-2692d2b6dbdc"
    }

    override fun onCreate() {
        super.onCreate()

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(App.ONESIGNAL_APP_ID)

        if (AdmobAdsManager.isAdmob) {
            MobileAds.initialize(
                this
            ) {
                AdmobOpenAds(
                    this@App
                )
            }
        }
    }
}
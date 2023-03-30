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
        createNotificationChannel()

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

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelPeriodic =
                NotificationChannel(CHANNEL_ID_PERIOD_WORK, "Period Work Request", importance)
            channelPeriodic.description = "Periodic Work"

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager!!.createNotificationChannel(channelPeriodic)
        }
    }
}
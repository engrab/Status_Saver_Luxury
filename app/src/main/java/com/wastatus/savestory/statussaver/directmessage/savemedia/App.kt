package com.wastatus.savestory.statussaver.directmessage.savemedia

import android.app.Application
import androidx.work.*
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobOpenAds
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.PeriodicBackgroundNotification
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.utlis.SharedPrefs
import java.util.concurrent.TimeUnit

class App : Application() {
    companion object {


        private const val ONESIGNAL_APP_ID = "d751619d-fa4a-431b-bb4d-2692d2b6dbdc"
    }

    override fun onCreate() {
        super.onCreate()
        observeChanges()
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

    private fun observeChanges() {
        if (SharedPrefs.getNotify(this)) {

//            val periodic = PeriodicWorkRequestBuilder<PeriodicBackgroundNotification>(1, TimeUnit.DAYS)
//                .setInitialDelay(1, TimeUnit.HOURS)
//                .build()


            val sendNotification =
                PeriodicWorkRequest.Builder(PeriodicBackgroundNotification::class.java, 1, TimeUnit.DAYS)
                    .setConstraints(
                        Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    )
                    .build()
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("notification",ExistingPeriodicWorkPolicy.UPDATE,sendNotification)



        }

    }
}
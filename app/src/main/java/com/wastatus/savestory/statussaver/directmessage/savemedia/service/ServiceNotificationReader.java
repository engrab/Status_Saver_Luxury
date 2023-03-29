package com.wastatus.savestory.statussaver.directmessage.savemedia.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat.Builder;

import com.wastatus.savestory.statussaver.directmessage.savemedia.App;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.start.MainActivity;

public class ServiceNotificationReader extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        startForeground(1, new Builder(this, App.channelId).setContentTitle("Service Running").setContentText("Waiting for a deleted messege").setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24).setContentIntent(PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), 0)).build());
        return START_NOT_STICKY;

        //return 2;
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

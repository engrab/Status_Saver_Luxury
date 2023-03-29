package com.wastatus.savestory.statussaver.directmessage.savemedia.service;

import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;

public class StartGetting extends Service {
    FileObserver fileObserver;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    public void onCreate() {
        super.onCreate();
        FileObserver fileObserver2 = this.fileObserver;
        if (fileObserver2 != null) {
            fileObserver2.stopWatching();
        }
    }
}

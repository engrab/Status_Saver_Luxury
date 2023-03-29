package com.wastatus.savestory.statussaver.directmessage.savemedia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.wastatus.savestory.statussaver.directmessage.savemedia.noti.MyNotification;

import java.io.File;

public class WhatsappService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String TAG = "WhatsappService";
    public static FileObserver fo;
    private MyNotification myNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        myNotification = new MyNotification(this);

        final File directoryToWatch = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/Android/media/com.whatsapp/FWhatsApp/Media/.Statuses/");

        fo = new FileObserver(directoryToWatch.getAbsolutePath(), FileObserver.CREATE) {
            @Override
            public void onEvent(int event, @Nullable String path) {

                myNotification.showNotification("File Modified", "The file at " + path + " has been modified.");

            }
        };
        fo.startWatching();

        Toast.makeText(this, "Log Watcher Service is started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStartCommand: Log Watcher Service is started and trying to watch: " + directoryToWatch);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Log Watcher Service is stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy: Log Watcher Service is stopped");

//        fo.stopWatching();
    }

}

package com.wastatus.savestory.statussaver.directmessage.savemedia.fileObserve;

import android.content.Context;
import android.os.FileObserver;
import android.util.Log;

import com.wastatus.savestory.statussaver.directmessage.savemedia.noti.MyNotification;

public class MyFileObserver extends FileObserver {
    private Context context;
    private MyNotification myNotification;

    public MyFileObserver(String path, Context context) {
        super(path, FileObserver.ALL_EVENTS);
        this.context = context;
        myNotification = new MyNotification(context);
    }

    @Override
    public void onEvent(int event, String path) {
        if (event == FileObserver.CREATE) {
            Log.d("TAG", "onEvent: ");
            myNotification.showNotification("File Modified", "The file at " + path + " has been modified.");
        }
    }
}

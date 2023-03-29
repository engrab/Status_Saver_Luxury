package com.wastatus.savestory.statussaver.directmessage.savemedia.service;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.noti.sendNotification;

import java.io.File;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotifyListener extends NotificationListenerService {
   private BroadcastReceiver broadcastReceiver;
   private Context context;
   private boolean onserving;
   //whatsapp
   private Observer observerWABStatus = null;
   private Observer observerWAStatus = null;

    String packagename;
    public ArrayList<String> packs;

    private class Observer extends FileObserver {
        public Observer(String str) {
            super(str, ALL_EVENTS);
            Log.d("TAG", "Observer: "+str);
        }

        public void onEvent(int event, String file) {
            Log.d("onEvent", "onEvent: "+file);
            String str2 = "filedellog";
            if (event == FileObserver.CREATE) {
                String sb = "create File path--> " + file;
                Log.d(str2, sb);
                try {

                    SharedPreferences sharedPreferences = getSharedPreferences("whatsapp", Context.MODE_PRIVATE);
                    packagename = sharedPreferences.getString("ispackage", "");
//                    if (packagename.equals("com.whatsapp")) {
//                        new SaveFiles().save(file, context);
//                    } else if (packagename.equals("com.whatsapp.w4b")) {
//                        new SaveFiles().saveWb(file, context);
//                    }
                    new sendNotification().sendBackground(context, "New Status Found", "Tap to check now.");
                    Log.d("onEvent", "save: "+file);
                } catch (Exception e) {
                    String sb2 = "create error: " + e.toString();
                    Log.d(str2, sb2);
                }
            }
        }
    }

    public NotifyListener() {
        onserving = false;
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                try {
                    Log.d("onserlog", "received");
                    String stringExtra = intent.getStringExtra(context.getString(R.string.noti_obserb));
                    boolean equals = stringExtra.equals("true");
                    if (equals) {
                        if (!onserving) {
                            StartFileObservingWhatsapp();
                            StartFileObservingWhatsAppBusiness();
                            onserving = equals;
                        }
                    } else if (stringExtra.equals("update")) {
                        updateList();
                    } else {
                        onserving = equals;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("notilogm", "on create");
        context = getApplicationContext();
        isNotificationServiceRunning();
        packs = new ArrayList<>();
        updateList();
        if (VERSION.SDK_INT < 23) {
            StartFileObservingWhatsapp();
            StartFileObservingWhatsAppBusiness();
        } else if (checkPermission()) {
            StartFileObservingWhatsapp();
            StartFileObservingWhatsAppBusiness();
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(context.getString(R.string.noti_obserb)));
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d("notilogm", "on connect");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        super.onNotificationPosted(statusBarNotification);

        // new

        if (VERSION.SDK_INT < 23) {
            StartFileObservingWhatsapp();
            StartFileObservingWhatsAppBusiness();
        } else if (checkPermission()) {
            StartFileObservingWhatsapp();
            StartFileObservingWhatsAppBusiness();
        }
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d("notilogm", "on dis connect");
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d("notilogm", "on cresate");
        tryReconnectService();
        return START_STICKY;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        super.onNotificationRemoved(statusBarNotification);
        Log.d("notilogm", "on removed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("notilogm", "on destroy");
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        Log.d("notilogm", "on task removed");
    }

    @RequiresApi(api = 23)
    private boolean checkPermission() {
        return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public IBinder onBind(Intent intent) {
        Log.d("notilogm", "bind");
        return super.onBind(intent);
    }

    public boolean onUnbind(Intent intent) {
        Log.d("unblog", "unb ");
        return super.onUnbind(intent);
    }


    public void StartFileObservingWhatsapp() {

        File  WAStatusFile;


        if (VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            WAStatusFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses");
        } else {
            WAStatusFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses");

        }


        // whatsapp  observer
        if (observerWAStatus != null) {
            observerWAStatus.stopWatching();
        }
        observerWAStatus = new Observer(WAStatusFile.getPath());
        observerWAStatus.startWatching();





    }

    public void StartFileObservingWhatsAppBusiness() {


        File WABStatusFile;


        if (VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            WABStatusFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp/Media/.Statuses");
        } else {
            WABStatusFile =new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses");

        }

        // whatsapp business observer
        if (observerWABStatus != null) {
            observerWABStatus.stopWatching();
        }
        observerWABStatus = new Observer(WABStatusFile.getPath());
        observerWABStatus.startWatching();




    }

    public void updateList() {
        packs.clear();
        new AsyncTask<Void, Void, Void>() {

            public Void doInBackground(Void... voidArr) {

                return null;
            }
        }.execute();
    }

    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (VERSION.SDK_INT >= 24) {
            requestRebind(new ComponentName(getApplicationContext(), NotifyListener.class));
        }
    }

    private void toggleNotificationListenerService() {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public boolean isNotificationServiceRunning() {
        String string = Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        String packageName = context.getPackageName();
        if (string != null) {
            boolean contains = string.contains(packageName);
            if (contains) {
                return contains;
            }
        }
        return false;
    }
}
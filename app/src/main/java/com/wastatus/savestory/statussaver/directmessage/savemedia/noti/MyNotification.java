package com.wastatus.savestory.statussaver.directmessage.savemedia.noti;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.wastatus.savestory.statussaver.directmessage.savemedia.start.MainActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;

public class MyNotification {

    public static final String CHANNEL_ID ="channel_id";
    public static final String CHANNEL_NAME= "channel_name";

    private final Context context;
    private final NotificationManager notificationManager;

    public MyNotification(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification(String title, String message) {

        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean notificationEnabled = preferences.getBoolean("notification_switch", true);
        if (notificationEnabled) {
            Notification.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder = new Notification.Builder(context, CHANNEL_ID);
            } else {
                builder = new Notification.Builder(context);
            }

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            Notification notification = builder.build();
            notificationManager.notify(0, notification);
        }


    }
}

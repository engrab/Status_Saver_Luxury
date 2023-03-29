package com.wastatus.savestory.statussaver.directmessage.savemedia.noti;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

import androidx.core.app.NotificationCompat.Builder;

import com.wastatus.savestory.statussaver.directmessage.savemedia.App;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.start.MainActivity;


public class sendNotification {
    public void sendBackground(Context context, String str, String str2) {
        try {
PendingIntent pendingIntent;
            if(VERSION.SDK_INT>Build.VERSION_CODES.R) {

                pendingIntent = PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
            }else {
                pendingIntent = PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

            }
            Builder contentIntent = new Builder(context, App.channelId)
                    .setContentTitle(str)
                    .setContentText(str2)
                    .setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setBadgeIconType(R.drawable.ic_baseline_download_24)
                    .setAutoCancel(true)
                    .setPriority(1)
                    .setContentIntent(pendingIntent);
            if (VERSION.SDK_INT >= 24) {
                contentIntent.setPriority(5);
            }
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, contentIntent.build());
            Log.d("notisendlog", "snd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

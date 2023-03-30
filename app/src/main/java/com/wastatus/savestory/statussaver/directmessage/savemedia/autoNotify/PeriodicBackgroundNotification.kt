package com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.CHANNEL_ID
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.NOTIFICATION_ID
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.activities.HomeActivity


class PeriodicBackgroundNotification(private val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        makeStatusNotification("It's time to save status", context)
        return Result.success()
    }
    private fun makeStatusNotification(message: String, context: Context) {

        val notifyIntent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = VERBOSE_NOTIFICATION_CHANNEL_NAME
            val description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setContentIntent(notifyPendingIntent)
        }
        // Create the notification

        notification.setSmallIcon(R.mipmap.ic_launcher)
        notification.setContentTitle(context.resources.getString(R.string.app_name))
        notification.setContentText(message)
        notification.setAutoCancel(true)
        notification.setPriority(NotificationCompat.PRIORITY_HIGH)
        notification.setVibrate(LongArray(0))


        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }


}
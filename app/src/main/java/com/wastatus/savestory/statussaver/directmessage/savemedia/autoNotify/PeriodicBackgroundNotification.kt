package com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.CHANNEL_ID
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.CHANNEL_ID_PERIOD_WORK
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.NOTIFICATION_ID
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.NOTIFICATION_TITLE
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.VERBOSE_NOTIFICATION_CHANNEL_NAME
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.activities.HomeActivity


class PeriodicBackgroundNotification(private val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        makeStatusNotification("It's time to save status", context)
        return Result.success()
    }
    private fun makeStatusNotification(message: String, context: Context) {

//        val intent = Intent(context, HomeActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent = TaskStackBuilder.create(context).run {
//            // Add the intent, which inflates the back stack
//            addNextIntentWithParentStack(intent)
//            // Get the PendingIntent containing the entire back stack
//            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//        }
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
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
//            setContentIntent(pendingIntent)
//        }
        // Create the notification
        val build = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_download_24)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

        // Show the notification
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, build.build())
    }


    private fun showNotification() {
        val intent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = TaskStackBuilder.create(context).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_PERIOD_WORK).apply {
            setContentIntent(pendingIntent)
        }
        notification.setContentTitle(context.resources.getString(R.string.app_name))
        notification.setContentText(context.resources.getString(R.string.available_noti))
        notification.priority = NotificationCompat.PRIORITY_HIGH
        notification.setCategory(NotificationCompat.CATEGORY_ALARM)
        notification.setSmallIcon(R.drawable.ic_baseline_download_24)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        notification.setSound(sound)
        val vibrate = longArrayOf(0, 100, 200, 300)
        notification.setVibrate(vibrate)
        notification.setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1, notification.build())
        }
    }

}
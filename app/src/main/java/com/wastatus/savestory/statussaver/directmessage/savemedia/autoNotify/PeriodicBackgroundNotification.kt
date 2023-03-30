package com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.SharedPrefs
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.Constants.CHANNEL_ID_PERIOD_WORK
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.activities.HomeActivity


class PeriodicBackgroundNotification(private val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        showNotification()
        return Result.success()
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